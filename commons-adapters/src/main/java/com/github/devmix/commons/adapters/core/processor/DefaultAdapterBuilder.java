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

import com.github.devmix.commons.adapters.api.annotations.DelegateRule;
import com.github.devmix.commons.adapters.core.utils.AdapterUtils;
import com.github.devmix.commons.adapters.core.utils.Constants;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Utility class for building adapter source code (one per adapter). Output can be wrote directly into source file.
 *
 * @author Sergey Grachev
 */
final class DefaultAdapterBuilder {

    private final List<Method> methods = new LinkedList<>();
    private final Elements elements;
    private final Types types;
    private final DefaultAnnotationsScanner annotationsScanner;
    private final TypeElement adapterClass;
    private final DeclaredType adapterClassView;
    private final DeclaredType adapteeClass;
    private final DeclaredType adapteeClassView;
    private final ExecutableElement adapteeProviderMethod;
    private final String packageName;
    private final String className;

    /**
     * @param elements              {@link Elements} of current processing environment
     * @param types                 {@link Types} of current processing environment
     * @param annotationsScanner    annotations scanner
     * @param adapterClass          class annotated by {@link com.github.devmix.commons.adapters.api.annotations.Adapter}
     * @param adapteeClass          class of adaptee
     * @param adapteeProviderMethod method which provides instance of adaptee
     */
    public DefaultAdapterBuilder(final Elements elements, final Types types, final DefaultAnnotationsScanner annotationsScanner,
                                 final TypeElement adapterClass, final TypeMirror adapteeClass,
                                 final ExecutableElement adapteeProviderMethod) {
        this.elements = elements;
        this.types = types;
        this.annotationsScanner = annotationsScanner;
        this.adapteeProviderMethod = Objects.requireNonNull(adapteeProviderMethod);
        this.packageName = elements.getPackageOf(adapterClass).getQualifiedName().toString();
        this.className = adapterClass.getSimpleName() + "$" + Constants.ADAPTER_COMPILED_CLASS_NAME;
        this.adapterClass = Objects.requireNonNull(adapterClass);
        this.adapterClassView = makeClassView(adapterClass, adapterClass.getTypeParameters());
        if (Objects.requireNonNull(adapteeClass) instanceof TypeVariable) {
            this.adapteeClass = (DeclaredType) types.asMemberOf(adapterClassView, types.asElement(adapteeClass));
        } else {
            this.adapteeClass = (DeclaredType) adapteeClass;
        }
        this.adapteeClassView = makeClassView((TypeElement) types.asElement(this.adapteeClass), this.adapterClass.getTypeParameters());
    }

    /**
     * Generate source code of adapter class and write into external buffer.
     *
     * @param buffer for writing result
     * @param <T>    any buffer which supports {@link Appendable} interface
     * @return origin buffer
     * @throws IOException
     */
    public <T extends Appendable> T appendTo(final T buffer) throws IOException {
        if (adapteeClass == null) {
            throw new IllegalStateException("No adaptee type");
        }

        if (adapteeProviderMethod == null) {
            throw new IllegalStateException("No adaptee provider");
        }

        buffer
                .append("package ").append(packageName).append(";\n")
                .append("public final class ").append(className);

        writeTypeParameters(buffer, adapterClass.getTypeParameters());

        buffer.append(" extends ").append(adapterClass.getQualifiedName());

        writeClassParameters(buffer, adapterClass.getTypeParameters());

        buffer.append("{\n");

        writeConstructors(buffer);

        writeMethods(buffer);

        buffer.append('}');

        return buffer;
    }

    /**
     * Search and declare all methods of adapter
     *
     * @return current instance of builder
     */
    public DefaultAdapterBuilder declareMethodsOfAdaptee() {
        final DelegateRule[] rules = annotationsScanner.globalDelegateRulesFor(adapterClass);

        for (final Element element : elements.getAllMembers((TypeElement) adapterClassView.asElement())) {
            if (!ElementKind.METHOD.equals(element.getKind()) || adapteeProviderMethod.equals(element)
                    || !AdapterUtils.isAcceptableMethod(element.getSimpleName(), element.getModifiers())) {
                continue;
            }

            final ExecutableElement method = (ExecutableElement) element;

            final Pair<ExecutableElement, DelegateRule> foundMethod = matchMethodByRules(rules, method);
            if (foundMethod == null) {
//                log.printMessage(Diagnostic.Kind.WARNING, "No adaptee method for {}", method);
                continue;
            }

            declareMethod(method)
                    .invokeAdapteeMethod(foundMethod.getKey())
                    .thenReturn(foundMethod.getValue().returnValue());
        }

        return this;
    }

    /**
     * Declare new method of adapter
     *
     * @param method method of adapter
     * @return builder for method
     */
    public Method declareMethod(final ExecutableElement method) {
        final Method result = new Method(method);
        methods.add(result);
        return result;
    }

    /**
     * Full name of new adapter class
     *
     * @return class name
     */
    public String fullClassName() {
        return packageName == null || packageName.isEmpty() ? className : packageName + '.' + className;
    }

    private void writeClassParameters(final Appendable buffer, final List<? extends TypeParameterElement> parameters) throws IOException {
        if (parameters.isEmpty()) {
            return;
        }

        buffer.append('<');
        for (int i = 0, parametersSize = parameters.size(); i < parametersSize; i++) {
            final TypeParameterElement parameter = parameters.get(i);
            if (i > 0) {
                buffer.append(',');
            }
            buffer.append(parameter.getSimpleName());
        }
        buffer.append('>');
    }


    private DeclaredType makeClassView(final TypeElement origin, final List<? extends TypeParameterElement> typeParameters) {
        if (typeParameters.isEmpty()) {
            return types.getDeclaredType(origin);
        }

        final TypeMirror[] typeMirrors = new TypeMirror[typeParameters.size()];
        for (int i = 0, typeParametersSize = typeParameters.size(); i < typeParametersSize; i++) {
            final TypeParameterElement type = typeParameters.get(i);
            typeMirrors[i] = type.asType();
        }

        return types.getDeclaredType(origin, typeMirrors);
    }

    private void writeConstructors(final Appendable buffer) throws IOException {
        for (final Element constructor : adapterClass.getEnclosedElements()) {
            if (!ElementKind.CONSTRUCTOR.equals(constructor.getKind())) {
                continue;
            }

            final List<? extends VariableElement> parameters = ((ExecutableElement) constructor).getParameters();

            writeMethodModifiers(buffer, constructor.getModifiers());
            buffer.append(' ');
            buffer.append(className);
            writeMethodParameters(buffer, parameters, Collections.<TypeMirror>emptySet());
            buffer.append('{');
            buffer.append("super");
            writeMethodInvokeParameters(buffer, parameters);
            buffer.append(';').append('}').append('\n');
        }
    }

    private void writeMethods(final Appendable buffer) throws IOException {
        for (final Method method : methods) {
            writeMethod(buffer, method);
        }
    }

    private void writeMethod(final Appendable buffer, final Method method) throws IOException {
        final ExecutableElement origin = method.adapterMethod;
        writeMethodModifiers(buffer, origin.getModifiers());
        buffer.append(' ');

        final Set<TypeMirror> typeParameters = writeTypeParameters(buffer, origin.getTypeParameters());
        buffer.append(' ');

        writeMethodReturnType(buffer, origin);
        buffer.append(' ').append(origin.getSimpleName());

        writeMethodParameters(buffer, origin.getParameters(), typeParameters);

        writeMethodBody(buffer, method.adapterMethod, method.adapteeMethod, method.returnValue);

        buffer.append('\n');
    }

    private void writeMethodBody(final Appendable buffer, final ExecutableElement adapterMethod,
                                 final ExecutableElement adapteeMethod, final DelegateRule.ReturnValue returnValue) throws IOException {
        buffer.append('{');

        // ((adapteeType) this.adapteeProviderMethod()).adapteeMethod(...);
        final StringBuilder body = new StringBuilder()
                .append("((").append(adapteeClass.toString()).append(") this.").append(adapteeProviderMethod.getSimpleName())
                .append("()).").append(adapteeMethod.getSimpleName());
        writeMethodInvokeParameters(body, adapterMethod.getParameters());
        body.append(';');

        switch (returnValue) {
            case THIS:
                body.append("return this;");
                break;

            case RESULT:
                body.insert(0, "return ");
                break;

            default:
                final TypeMirror adapterReturn = adapterMethod.getReturnType();
                final TypeMirror adapteeReturn = adapteeMethod.getReturnType();

                if (TypeKind.VOID.equals(adapterReturn.getKind())) {
                    break;
                }

                final TypeMirror adapterReturnView = ((ExecutableType) types.asMemberOf(adapterClassView, adapterMethod)).getReturnType();
                final TypeMirror adapteeReturnView = ((ExecutableType) types.asMemberOf(adapteeClassView, adapteeMethod)).getReturnType();

                if (TypeKind.VOID.equals(adapteeReturn.getKind()) || !types.isAssignable(adapterReturnView, adapteeReturnView)) {
                    final boolean isAssignable = types.isAssignable(adapterClassView, adapterReturnView);
                    if (isAssignable) {
                        body.append("return this;");
                    } else {
                        body.append("return null;");
                    }
                } else {
                    body.insert(0, "return ");
                }
        }

        buffer.append(body).append('}');
    }

    private void writeMethodInvokeParameters(final Appendable buffer, final List<? extends VariableElement> parameters) throws IOException {
        buffer.append('(');
        if (!parameters.isEmpty()) {
            for (int i = 0, parametersSize = parameters.size(); i < parametersSize; i++) {
                final VariableElement parameter = parameters.get(i);
                if (i > 0) {
                    buffer.append(',');
                }
                buffer.append(parameter.getSimpleName());
            }
        }
        buffer.append(')');
    }

    private void writeMethodReturnType(final Appendable buffer, final ExecutableElement method) throws IOException {
//        final TypeMirror returnType = method.getReturnType();
//        if (TypeKind.TYPEVAR.equals(returnType.getKind())) {
        buffer.append(((ExecutableType) types.asMemberOf(adapterClassView, method)).getReturnType().toString());
//        } else {
//            buffer.append(returnType.toString());
//        }
    }

    private void writeMethodParameters(final Appendable buffer, final List<? extends VariableElement> parameters,
                                       final Set<TypeMirror> typeParameters) throws IOException {
        buffer.append('(');
        for (int i = 0, parametersSize = parameters.size(); i < parametersSize; i++) {
            final VariableElement parameter = parameters.get(i);
            if (i > 0) {
                buffer.append(',');
            }

            TypeMirror parameterType = parameter.asType();
            if (TypeKind.TYPEVAR.equals(parameterType.getKind()) && !typeParameters.contains(parameterType)) {
                parameterType = types.asMemberOf(adapterClassView, types.asElement(parameterType));
            }

            buffer.append(parameterType.toString())
                    .append(' ')
                    .append(parameter.getSimpleName());
        }
        buffer.append(')');
    }

    private Set<TypeMirror> writeTypeParameters(final Appendable buffer, final List<? extends TypeParameterElement> parameters) throws IOException {
        if (parameters.isEmpty()) {
            return Collections.emptySet();
        }

        final Set<TypeMirror> result = new HashSet<>(parameters.size());

        buffer.append('<');
        for (int parameterIndex = 0, parametersSize = parameters.size(); parameterIndex < parametersSize; parameterIndex++) {
            final TypeParameterElement parameter = parameters.get(parameterIndex);
            if (parameterIndex > 0) {
                buffer.append(',');
            }
            buffer.append(parameter.getSimpleName());
            writeTypeParameterBounds(buffer, parameter);
            result.add(parameter.asType());
        }
        buffer.append('>');

        return result;
    }

    private void writeTypeParameterBounds(final Appendable buffer, final TypeParameterElement parameter) throws IOException {
        final List<? extends TypeMirror> bounds = parameter.getBounds();
        if (!bounds.isEmpty()) {
            buffer.append(" extends ");
            for (int boundIndex = 0, boundsSize = bounds.size(); boundIndex < boundsSize; boundIndex++) {
                final TypeMirror bound = bounds.get(boundIndex);
                if (boundIndex > 0) {
                    buffer.append('&');
                }
                buffer.append(bound.toString());
            }
        }
    }

    private void writeMethodModifiers(final Appendable buffer, final Set<Modifier> modifiers) throws IOException {
        int index = 0;
        for (final Modifier modifier : modifiers) {
            if (modifier.equals(Modifier.ABSTRACT)) {
                continue;
            }
            if (index++ > 0) {
                buffer.append(' ');
            }
            buffer.append(modifier.toString());
        }
    }

    @Nullable
    private Pair<ExecutableElement, DelegateRule> matchMethodByRules(final DelegateRule[] rules, final ExecutableElement method) {

        Pair<ExecutableElement, DelegateRule> result = null;

        final DelegateRule[] overrideRules = annotationsScanner.methodDelegateRulesOf(method);
        if (overrideRules != null && overrideRules.length > 0) {
            result = findTargetMethodByRules(overrideRules, method);
        }

        if (result == null) {
            result = findTargetMethodByRules(rules, method);
        }

        return result;
    }

    @Nullable
    private Pair<ExecutableElement, DelegateRule> findTargetMethodByRules(final DelegateRule[] rules, final ExecutableElement adapterMethod) {
        for (final DelegateRule item : rules) {
            final ExecutableElement result = findTargetMethodByRule(item, adapterMethod);
            if (result != null) {
                return Pair.of(result, item);
            }
        }
        return null;
    }

    @Nullable
    private ExecutableElement findTargetMethodByRule(final DelegateRule rule, final ExecutableElement adapterMethod) {
        final String fullAdapterMethodName = adapterMethod.getSimpleName().toString();
        final Pattern adapterMethodPattern = Pattern.compile(rule.from());
        final Matcher adapterMethodMatcher = adapterMethodPattern.matcher(fullAdapterMethodName);
        if (!adapterMethodMatcher.matches()) {
            return null;
        }

        final boolean isAdapterMethodPattern = adapterMethodMatcher.groupCount() != 0;
        final String adapterMethodName = isAdapterMethodPattern
                ? adapterMethodMatcher.group(1) : fullAdapterMethodName;
        final Pattern targetPattern = Pattern.compile(rule.to());
        for (final Element adapteeMethod : elements.getAllMembers((TypeElement) adapteeClassView.asElement())) {
            if (!AdapterUtils.isInvokableMethod(adapteeMethod)) {
                continue;
            }

            final String fullAdapteeMethodName = adapteeMethod.getSimpleName().toString();

            final Matcher adapteeMethodMatcher = targetPattern.matcher(fullAdapteeMethodName);
            if (!adapteeMethodMatcher.matches()) {
                continue;
            }

            final boolean isAdapteeMethodPattern = adapteeMethodMatcher.groupCount() != 0;
            final String adapteeMethodName = isAdapteeMethodPattern
                    ? adapteeMethodMatcher.group(1) : fullAdapteeMethodName;

            final boolean matches = (isAdapterMethodPattern && isAdapteeMethodPattern)
                    ? adapterMethodName.equals(adapteeMethodName)
                    : fullAdapterMethodName.equals(rule.from()) && fullAdapteeMethodName.equals(rule.to());

            if (matches && isSubsignature(adapterMethod, (ExecutableElement) adapteeMethod)) {
                return (ExecutableElement) adapteeMethod;
            }
        }

        return null;
    }

    private boolean isSubsignature(final ExecutableElement adapterMethod, final ExecutableElement adapteeMethod) {
        final ExecutableType adapterMethodView = (ExecutableType) types.asMemberOf(adapterClassView, adapterMethod);
        final ExecutableType adapteeMethodView = (ExecutableType) types.asMemberOf(adapteeClassView, adapteeMethod);
        return types.isSubsignature(adapterMethodView, adapteeMethodView);
    }

    public static final class Method {

        private final ExecutableElement adapterMethod;
        private ExecutableElement adapteeMethod;
        private DelegateRule.ReturnValue returnValue;

        public Method(final ExecutableElement adapterMethod) {
            this.adapterMethod = adapterMethod;
        }

        public Method invokeAdapteeMethod(final ExecutableElement adapteeMethod) {
            this.adapteeMethod = adapteeMethod;
            return this;
        }

        public Method thenReturn(final DelegateRule.ReturnValue value) {
            this.returnValue = value;
            return this;
        }
    }
}
