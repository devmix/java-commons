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
import com.github.devmix.commons.adapters.api.annotations.DelegateMethod;
import com.github.devmix.commons.adapters.api.exceptions.AdapterGenerationException;
import com.github.devmix.commons.adapters.core.utils.Constants;
import com.github.devmix.commons.adapters.core.utils.ProcessorUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.github.devmix.commons.adapters.core.utils.ContextUtils.isAcceptableMethod;
import static com.github.devmix.commons.adapters.core.utils.ProcessorUtils.asClassWithTypes;
import static com.github.devmix.commons.adapters.core.utils.ProcessorUtils.className;
import static com.github.devmix.commons.adapters.core.utils.ProcessorUtils.writeClassParameters;
import static com.github.devmix.commons.adapters.core.utils.ProcessorUtils.writeMethodInvokeParameters;
import static com.github.devmix.commons.adapters.core.utils.ProcessorUtils.writeMethodModifiers;
import static com.github.devmix.commons.adapters.core.utils.ProcessorUtils.writeMethodParameters;
import static com.github.devmix.commons.adapters.core.utils.ProcessorUtils.writeTypeParameters;


/**
 * Utility class for building adapter source code (one per adapter). Output can be wrote directly into source file.
 *
 * @author Sergey Grachev
 */
final class GeneratorAdapter {

    private final Types types;
    private final DefaultAnnotationsScanner annotations;
    private final TypeElement adapterClass;
    private final DeclaredType adapterClassView;
    private final String packageName;
    private final String className;
    private final List<? extends Element> adapteeClassViewElements;
    private final List<? extends Element> adapterClassViewElements;

    private final GeneratorAdapteeProvider adapteeProviderGenerator;
    private final GeneratorDelegateMethod delegateMethodsGenerator;
    private final GeneratorBeanProperty beanPropertiesGenerator;
    private Appendable buffer;

    /**
     * @param elements     {@link Elements} of current processing environment
     * @param types        {@link Types} of current processing environment
     * @param annotations  annotations scanner
     * @param adapterClass class annotated by {@link com.github.devmix.commons.adapters.api.annotations.Adapter}
     * @param adapter      adapter annotation
     */
    public GeneratorAdapter(final Elements elements, final Types types, final DefaultAnnotationsScanner annotations,
                            final TypeElement adapterClass, final MirrorAdapter adapter) {

        this.types = types;
        this.annotations = annotations;
        this.packageName = elements.getPackageOf(adapterClass).getQualifiedName().toString();
        this.className = className(adapterClass, Constants.ADAPTER_COMPILED_CLASS_NAME);
        this.adapterClass = Objects.requireNonNull(adapterClass);
        this.adapterClassView = asClassWithTypes(types, adapterClass, adapterClass.getTypeParameters());

        final ExecutableElement adapteeProviderMethod = annotations.adapteeFor(adapterClass);
        final TypeMirror adaptee = ProcessorUtils.isVoid(adapter.adaptee())
                ? adapteeProviderMethod == null ? adapterClass.asType() : adapteeProviderMethod.getReturnType() : adapter.adaptee();
        final DeclaredType adapteeClass;
        if (Objects.requireNonNull(adaptee) instanceof TypeVariable) {
            adapteeClass = (DeclaredType) types.asMemberOf(adapterClassView, types.asElement(adaptee));
        } else {
            adapteeClass = (DeclaredType) adaptee;
        }

        final DeclaredType adapteeClassView = asClassWithTypes(types, (TypeElement) types.asElement(adapteeClass), this.adapterClass.getTypeParameters());
        this.adapteeClassViewElements = elements.getAllMembers((TypeElement) adapteeClassView.asElement());
        this.adapterClassViewElements = elements.getAllMembers((TypeElement) adapterClassView.asElement());

        this.adapteeProviderGenerator = new GeneratorAdapteeProvider(
                types, adapteeProviderMethod, adapterClassView, adapteeClassView);

        this.delegateMethodsGenerator = new GeneratorDelegateMethod(
                annotations, adapteeProviderGenerator, types, elements, adapteeClassView, adapterClassView);

        this.beanPropertiesGenerator = new GeneratorBeanProperty(
                annotations, adapteeProviderGenerator, types, elements, adapterClassView, adapteeClassView);
    }

    /**
     * Generate source code of adapter class and write into external buffer.
     *
     * @param buffer for writing result
     * @param <T>    any buffer which supports {@link Appendable} interface
     * @return origin buffer
     * @throws IOException
     */
    public <T extends Appendable> T appendTo(final T buffer) throws IOException, AdapterGenerationException {
        initialize(buffer);

        writePackage();
        writeClassSignature();
        writeConstructors();

        adapteeProviderGenerator.write();
        delegateMethodsGenerator.write();
        beanPropertiesGenerator.write();

        writeEnd();

        return buffer;
    }

    private void initialize(final Appendable buffer) throws AdapterGenerationException {
        this.buffer = buffer;
        adapteeProviderGenerator.initialize(buffer);
        delegateMethodsGenerator.initialize(buffer);
        beanPropertiesGenerator.initialize(buffer);

        final DelegateMethod[] rules = annotations.globalDelegateMethodsOf(adapterClass);
        final BeanProperty[] beanProperty = annotations.globalBeanPropertiesOf(adapterClass);

        for (final Element element : adapterClassViewElements) {
            if (!ElementKind.METHOD.equals(element.getKind())
                    || adapteeProviderGenerator.isEquals(element)
                    || !isAcceptableMethod(element.getSimpleName(), element.getModifiers())) {
                continue;
            }

            final ExecutableElement method = (ExecutableElement) element;

            if (delegateMethodsGenerator.checkAndAdd(method, rules)) {
                continue;
            }

            beanPropertiesGenerator.checkAndAdd(method, beanProperty);
        }
    }

    private void writePackage() throws IOException {
        buffer.append("package ").append(packageName).append(";\n");
    }

    private void writeClassSignature() throws IOException {
        buffer.append("public final class ").append(className);

        writeTypeParameters(buffer, adapterClass.getTypeParameters());

        if (ElementKind.INTERFACE.equals(adapterClass.getKind())) {
            buffer.append(" implements ");
        } else {
            buffer.append(" extends ");
        }

        buffer.append(adapterClass.getQualifiedName());

        writeClassParameters(buffer, adapterClass.getTypeParameters());

        buffer.append("{\n");
    }

    private void writeConstructors() throws IOException {
        for (final Element constructor : adapterClass.getEnclosedElements()) {
            if (!ElementKind.CONSTRUCTOR.equals(constructor.getKind())) {
                continue;
            }

            final List<? extends VariableElement> parameters = ((ExecutableElement) constructor).getParameters();

            // constructor modifiers
            writeMethodModifiers(buffer, constructor.getModifiers());
            buffer.append(' ');
            // constructor name
            buffer.append(className);
            // constructor parameters
            writeMethodParameters(buffer, types, adapterClassView, parameters, Collections.<TypeMirror>emptySet());
            // begin
            buffer.append('{');
            // invoke super
            buffer.append("super");
            writeMethodInvokeParameters(buffer, parameters);
            // end
            buffer.append(';').append('}').append('\n');
        }
    }

    private void writeEnd() throws IOException {
        buffer.append('}');
    }

    /**
     * Full name of new adapter class
     *
     * @return class name
     */
    public String fullClassName() {
        return packageName == null || packageName.isEmpty() ? className : packageName + '.' + className;
    }
}
