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

import com.github.devmix.commons.adapters.api.annotations.DelegateMethod;
import com.github.devmix.commons.adapters.core.utils.ProcessorUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.devmix.commons.adapters.core.utils.ProcessorUtils.writeMethodInvokeParameters;
import static com.github.devmix.commons.adapters.core.utils.ProcessorUtils.writeMethodModifiers;
import static com.github.devmix.commons.adapters.core.utils.ProcessorUtils.writeMethodParameters;
import static com.github.devmix.commons.adapters.core.utils.ProcessorUtils.writeMethodReturnType;
import static com.github.devmix.commons.adapters.core.utils.ProcessorUtils.writeTypeParameters;

/**
 * @author Sergey Grachev
 */
final class GeneratorDelegateMethod {

    private final List<DelegateMethodInfo> delegateMethods = new LinkedList<>();
    private final Types types;
    private final DefaultAnnotationsScanner annotations;
    private final GeneratorAdapteeProvider adapteeProvider;
    private final DeclaredType adapteeClassView;
    private final DeclaredType adapterClassView;
    private final List<? extends Element> adapteeClassViewElements;

    private Appendable buffer;

    public GeneratorDelegateMethod(final DefaultAnnotationsScanner annotations, final GeneratorAdapteeProvider adapteeProvider,
                                   final Types types, final Elements elements,
                                   final DeclaredType adapteeClassView, final DeclaredType adapterClassView) {
        this.annotations = annotations;
        this.adapteeProvider = adapteeProvider;
        this.types = types;
        this.adapteeClassView = adapteeClassView;
        this.adapterClassView = adapterClassView;
        this.adapteeClassViewElements = elements.getAllMembers((TypeElement) adapteeClassView.asElement());
    }

    public void initialize(final Appendable buffer) {
        this.buffer = buffer;
        this.delegateMethods.clear();
    }

    public void write() throws IOException {
        for (final DelegateMethodInfo method : delegateMethods) {
            writeMethod(method);
        }
    }

    public boolean checkAndAdd(final ExecutableElement method, final DelegateMethod[] rules) {
        Pair<ExecutableElement, DelegateMethod> result = null;

        final DelegateMethod[] overrides = annotations.methodDelegateMethodsOf(method);
        if (overrides != null && overrides.length > 0) {
            result = findTargetMethod(overrides, method);
        }

        if (result == null) {
            result = findTargetMethod(rules, method);
        }

        if (result != null) {
            declareMethod(method)
                    .adapteeMethod(result.getKey())
                    .returnValue(result.getValue().returnValue());
            return true;
        }

        return false;
    }

    private void writeMethod(final DelegateMethodInfo method) throws IOException {
        final ExecutableElement origin = method.getAdapterMethod();
        writeMethodModifiers(buffer, origin.getModifiers());
        buffer.append(' ');

        final Set<TypeMirror> typeParameters = writeTypeParameters(buffer, origin.getTypeParameters());
        buffer.append(' ');

        writeMethodReturnType(buffer, types, adapterClassView, origin);
        buffer.append(' ').append(origin.getSimpleName());

        writeMethodParameters(buffer, types, adapterClassView, origin.getParameters(), typeParameters);

        writeMethodBody(method.getAdapterMethod(), method.getAdapteeMethod(), method.getReturnValue());

        buffer.append('\n');
    }

    private void writeMethodBody(final ExecutableElement adapterMethod,
                                 final ExecutableElement adapteeMethod, final DelegateMethod.ReturnValue returnValue) throws IOException {
        buffer.append('{');

        // ((adapteeType) this.adapteeProviderMethod()).adapteeMethod(...);
        final StringBuilder body = new StringBuilder()
                .append(adapteeProvider.toCode())
                .append('.').append(adapteeMethod.getSimpleName());
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

    /**
     * Declare new method of adapter
     *
     * @param method method of adapter
     * @return builder for method
     */
    private DelegateMethodInfo declareMethod(final ExecutableElement method) {
        final DelegateMethodInfo result = new DelegateMethodInfo(method);
        delegateMethods.add(result);
        return result;
    }

    @Nullable
    private Pair<ExecutableElement, DelegateMethod> findTargetMethod(final DelegateMethod[] rules, final ExecutableElement adapterMethod) {
        for (final DelegateMethod item : rules) {
            final ExecutableElement result = findTargetMethod(item, adapterMethod);
            if (result != null) {
                return Pair.of(result, item);
            }
        }
        return null;
    }

    @Nullable
    private ExecutableElement findTargetMethod(final DelegateMethod rule, final ExecutableElement adapterMethod) {
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
        for (final Element adapteeMethod : adapteeClassViewElements) {
            if (!ProcessorUtils.isInvokableMethod(adapteeMethod)) {
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

    private static final class DelegateMethodInfo {

        private final ExecutableElement adapterMethod;
        private ExecutableElement adapteeMethod;
        private DelegateMethod.ReturnValue returnValue;

        public DelegateMethodInfo(final ExecutableElement adapterMethod) {
            this.adapterMethod = adapterMethod;
        }

        public DelegateMethodInfo adapteeMethod(final ExecutableElement adapteeMethod) {
            this.adapteeMethod = adapteeMethod;
            return this;
        }

        public DelegateMethodInfo returnValue(final DelegateMethod.ReturnValue value) {
            this.returnValue = value;
            return this;
        }

        public ExecutableElement getAdapterMethod() {
            return adapterMethod;
        }

        public ExecutableElement getAdapteeMethod() {
            return adapteeMethod;
        }

        public DelegateMethod.ReturnValue getReturnValue() {
            return returnValue;
        }
    }
}
