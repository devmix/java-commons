/*
 * Commons Library
 * Copyright (c) 2015-2016 Sergey Grachev (sergey.grachev@yahoo.com). All rights reserved.
 *
 * This software is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.devmix.commons.adapters.core.processor;

import com.github.devmix.commons.adapters.api.annotations.BeanProperty;
import com.github.devmix.commons.adapters.api.exceptions.AdapterGenerationException;
import com.github.devmix.commons.adapters.core.utils.ContextUtils;
import com.github.devmix.commons.adapters.core.utils.ProcessorUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.devmix.commons.adapters.core.utils.ProcessorUtils.writeFieldType;
import static com.github.devmix.commons.adapters.core.utils.ProcessorUtils.writeMethodModifiers;
import static com.github.devmix.commons.adapters.core.utils.ProcessorUtils.writeMethodParameters;
import static com.github.devmix.commons.adapters.core.utils.ProcessorUtils.writeMethodReturnType;
import static com.github.devmix.commons.adapters.core.utils.ProcessorUtils.writeTypeParameters;

/**
 * @author Sergey Grachev
 */
final class GeneratorBeanProperty {

    private static final Pattern PROPERTY = Pattern.compile("(set|get|is)([A-Z].*)");

    private final Map<String, BeanPropertyInfo> beanProperties = new HashMap<>();
    private final Types types;
    private final DefaultAnnotationsScanner annotations;
    private final GeneratorAdapteeProvider adapteeProvider;
    private final DeclaredType adapterClassView;
    private final DeclaredType adapteeClassView;
    private final List<? extends Element> adapteeClassViewElements;
    private Appendable buffer;

    public GeneratorBeanProperty(final DefaultAnnotationsScanner annotations, final GeneratorAdapteeProvider adapteeProvider, final Types types, final Elements elements,
                                 final DeclaredType adapterClassView, final DeclaredType adapteeClassView) {
        this.annotations = annotations;
        this.adapteeProvider = adapteeProvider;
        this.types = types;
        this.adapterClassView = adapterClassView;
        this.adapteeClassView = adapteeClassView;
        this.adapteeClassViewElements = elements.getAllMembers((TypeElement) adapteeClassView.asElement());
    }

    public void initialize(final Appendable buffer) {
        this.buffer = buffer;
        this.beanProperties.clear();
    }

    public boolean checkAndAdd(final ExecutableElement method, final BeanProperty... rules) throws AdapterGenerationException {
        final BeanProperty[] overrides = annotations.methodBeanPropertiesOf(method);
        if ((rules.length == 0 || hasSkip(rules)) && (overrides.length == 0 || hasSkip(overrides))) {
            return false;
        }

        final Matcher matcher = PROPERTY.matcher(method.getSimpleName());
        if (!matcher.matches()) {
            return false;
        }

        final String type = matcher.group(1);
        final String name = ContextUtils.fieldNameToCamelCase(matcher.group(2));
        final boolean isGetter = "get".equals(type) || "is".equals(type);
        final int parametersSize = method.getParameters().size();
        if (isGetter) {
            if (parametersSize > 0 || ProcessorUtils.isVoid(method.getReturnType())) {
                // TODO log incorrect signature
                return false;
            }
        } else {
            if (parametersSize != 1) {
                // TODO log incorrect signature
                return false;
            }
        }

        final boolean isAutoGeneratedAdaptee = types.isSameType(adapteeClassView, adapterClassView);
        final BeanPropertyInfo property = declareProperty(name);
        for (final Element element : adapteeClassViewElements) {
            if (ElementKind.METHOD.equals(element.getKind())) {
                final ExecutableElement adapteeMethod = (ExecutableElement) element;
                final boolean matches;
                if (isGetter) {
                    matches = element.getSimpleName().equals(method.getSimpleName())
                            && adapteeMethod.getParameters().isEmpty()
                            && !ProcessorUtils.isVoid(method.getReturnType());
                } else {
                    matches = element.getSimpleName().equals(method.getSimpleName())
                            && adapteeMethod.getParameters().size() == 1
                            && ProcessorUtils.isVoid(method.getReturnType());
                }
                if (matches) {
                    if (isGetter) {
                        property.adapterGetter(method);
                        if (!isAutoGeneratedAdaptee) {
                            property.adapteeGetter(adapteeMethod);
                        }
                    } else {
                        property.adapterSetter(method);
                        if (!isAutoGeneratedAdaptee) {
                            property.adapteeSetter(adapteeMethod);
                        }
                    }
                }
            }

            if (ElementKind.FIELD.equals(element.getKind())) {
                final boolean matches = element.getSimpleName().toString().equals(name);
                if (matches) {
                    if (isGetter) {
                        property.adapterGetter(method).field(element);
                    } else {
                        property.adapterSetter(method).field(element);
                    }
                    property.field(element);
                }
            }

        }

        if (!isAutoGeneratedAdaptee && property.isAdapteeNotAccessible()) {
            throw new AdapterGenerationException("Adaptee fields not accessible for property '" + name + "' via method '"
                    + method.toString() + "' of class " + adapterClassView.toString());
        }

        return false;
    }

    public void write() throws IOException {
        for (final BeanPropertyInfo property : beanProperties.values()) {
            writeMethod(property);
        }
    }

    private void writeMethod(final BeanPropertyInfo property) throws IOException {
        if (property.getField() == null) {
            buffer.append("private ");
            writeFieldType(buffer, types, adapterClassView, property.getType());
            buffer.append(' ').append(property.getName()).append(";\n");
        }

        final ExecutableElement setter = property.getAdapterSetMethod();
        if (setter != null) {
            writeMethodModifiers(buffer, setter.getModifiers());
            buffer.append(' ');

            final Set<TypeMirror> typeParameters = writeTypeParameters(buffer, setter.getTypeParameters());
            buffer.append(' ');

            writeMethodReturnType(buffer, types, adapterClassView, setter);
            buffer.append(' ').append(setter.getSimpleName());

            writeMethodParameters(buffer, types, adapterClassView, setter.getParameters(), typeParameters);

            buffer.append("{").append(adapteeProvider.toCode()).append('.');
            if (property.getAdapteeSetMethod() == null) {
                buffer.append(property.getName()).append("=").append(setter.getParameters().get(0).getSimpleName());
            } else {
                buffer.append(property.getAdapteeSetMethod().getSimpleName()).append('(')
                        .append(setter.getParameters().get(0).getSimpleName()).append(')');
            }
            buffer.append(";}").append('\n');
        }

        final ExecutableElement getter = property.getAdapterGetMethod();
        if (getter != null) {
            writeMethodModifiers(buffer, getter.getModifiers());
            buffer.append(' ');

            final Set<TypeMirror> typeParameters = writeTypeParameters(buffer, getter.getTypeParameters());
            buffer.append(' ');

            writeMethodReturnType(buffer, types, adapterClassView, getter);
            buffer.append(' ').append(getter.getSimpleName());

            writeMethodParameters(buffer, types, adapterClassView, getter.getParameters(), typeParameters);

            buffer.append("{ return ").append(adapteeProvider.toCode()).append('.');
            if (property.getAdapteeSetMethod() == null) {
                buffer.append(property.getName());
            } else {
                buffer.append(property.getAdapteeGetMethod().getSimpleName()).append("()");
            }
            buffer.append(";}").append('\n');
        }
    }

    private boolean hasSkip(final BeanProperty... rules) {
        for (final BeanProperty rule : rules) {
            if (rule.skip()) {
                return true;
            }
        }
        return false;
    }

    private BeanPropertyInfo declareProperty(final String name) {
        BeanPropertyInfo result = beanProperties.get(name);
        if (result == null) {
            result = new BeanPropertyInfo(name);
            beanProperties.put(name, result);
        }
        return result;
    }

    private static final class BeanPropertyInfo {

        private final String name;
        private TypeMirror type;
        private ExecutableElement adapterSetMethod;
        private ExecutableElement adapterGetMethod;
        private ExecutableElement adapteeSetMethod;
        private ExecutableElement adapteeGetMethod;
        private Element field;

        public BeanPropertyInfo(final String name) {
            this.name = name;
        }

        public BeanPropertyInfo adapterGetter(final ExecutableElement adapterMethod) {
            this.adapterGetMethod = adapterMethod;
            if (this.type == null) {
                this.type = adapterMethod.getReturnType();
            }
            return this;
        }

        public BeanPropertyInfo adapteeGetter(final ExecutableElement adapteeMethod) {
            this.adapteeGetMethod = adapteeMethod;
            if (this.type == null) {
                this.type = adapteeGetMethod.getReturnType();
            }
            return this;
        }

        public BeanPropertyInfo adapterSetter(final ExecutableElement adapterMethod) {
            this.adapterSetMethod = adapterMethod;
            if (this.type == null) {
                this.type = (TypeMirror) adapterMethod.getTypeParameters().get(0);
            }
            return this;
        }

        public BeanPropertyInfo adapteeSetter(final ExecutableElement adapteeMethod) {
            this.adapteeSetMethod = adapteeMethod;
            if (this.type == null) {
                this.type = (TypeMirror) adapteeSetMethod.getTypeParameters().get(0);
            }
            return this;
        }

        public void field(final Element field) {
            this.field = field;
            if (this.type == null) {
                this.type = field.asType();
            }
        }

        public String getName() {
            return name;
        }

        public TypeMirror getType() {
            return type;
        }

        public ExecutableElement getAdapterSetMethod() {
            return adapterSetMethod;
        }

        public ExecutableElement getAdapterGetMethod() {
            return adapterGetMethod;
        }

        public ExecutableElement getAdapteeSetMethod() {
            return adapteeSetMethod;
        }

        public ExecutableElement getAdapteeGetMethod() {
            return adapteeGetMethod;
        }

        public Element getField() {
            return field;
        }

        public boolean isAdapteeNotAccessible() {
            return field == null && (
                    (adapterSetMethod != null && adapteeSetMethod == null)
                            || (adapterGetMethod != null && adapteeGetMethod == null)
                            || (adapteeGetMethod == null && adapteeSetMethod == null));
        }
    }
}
