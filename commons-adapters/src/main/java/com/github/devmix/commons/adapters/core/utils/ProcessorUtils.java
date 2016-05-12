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

package com.github.devmix.commons.adapters.core.utils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
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
import javax.lang.model.util.Types;
import java.io.IOException;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Sergey Grachev
 */
public final class ProcessorUtils {

    private ProcessorUtils() {
    }

    public static Set<TypeMirror> writeTypeParameters(final Appendable buffer, final List<? extends TypeParameterElement> parameters) throws IOException {
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

    public static void writeTypeParameterBounds(final Appendable buffer, final TypeParameterElement parameter) throws IOException {
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

    public static void writeClassParameters(final Appendable buffer, final List<? extends TypeParameterElement> parameters) throws IOException {
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

    public static void writeMethodModifiers(final Appendable buffer, final Set<Modifier> modifiers) throws IOException {
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

    public static void writeMethodInvokeParameters(final Appendable buffer, final List<? extends VariableElement> parameters) throws IOException {
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

    public static void writeMethodParameters(final Appendable buffer, final Types types, final DeclaredType enclosingType,
                                             final List<? extends VariableElement> parameters,
                                             final Set<TypeMirror> typeParameters) throws IOException {
        buffer.append('(');
        for (int i = 0, parametersSize = parameters.size(); i < parametersSize; i++) {
            final VariableElement parameter = parameters.get(i);
            if (i > 0) {
                buffer.append(',');
            }

            TypeMirror parameterType = parameter.asType();
            if (TypeKind.TYPEVAR.equals(parameterType.getKind()) && !typeParameters.contains(parameterType)) {
                parameterType = types.asMemberOf(enclosingType, types.asElement(parameterType));
            }

            buffer.append(parameterType.toString())
                    .append(' ')
                    .append(parameter.getSimpleName());
        }
        buffer.append(')');
    }

    public static void writeMethodReturnType(final Appendable buffer, final Types types, final DeclaredType enclosingType,
                                             final ExecutableElement method) throws IOException {
//        final TypeMirror returnType = method.getReturnType();
//        if (TypeKind.TYPEVAR.equals(returnType.getKind())) {
        buffer.append(((ExecutableType) types.asMemberOf(enclosingType, method)).getReturnType().toString());
//        } else {
//            buffer.append(returnType.toString());
//        }
    }

    public static void writeFieldType(final Appendable buffer, final Types types, final DeclaredType enclosingType,
                                      final TypeMirror field) throws IOException {
        if (TypeKind.TYPEVAR.equals(field.getKind())) {
            buffer.append(types.asMemberOf(enclosingType, types.asElement(field)).toString());
        } else {
            buffer.append(field.toString());
        }
    }

    public static DeclaredType asClassWithTypes(final Types types, final TypeElement origin,
                                                final List<? extends TypeParameterElement> typeParameters) {
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

    public static String className(final TypeElement basedOn, final String className) {
        final StringBuilder result = new StringBuilder();
        Element enclosing = basedOn.getEnclosingElement();
        while (enclosing != null) {
            if (ElementKind.CLASS.equals(enclosing.getKind())) {
                result.append(enclosing.getSimpleName()).append('$');
            }
            enclosing = enclosing.getEnclosingElement();
        }
        return result.append(basedOn.getSimpleName()).append('$').append(className).toString();
    }

    public static boolean isVoid(final TypeMirror type) {
        final String className = type.toString();
        return "void".equals(className) || Void.class.getName().equals(className);
    }

    public static Map<? extends ExecutableElement, ? extends AnnotationValue> getAnnotationValues(final AnnotationMirror annotation) {
        final Map<ExecutableElement, AnnotationValue> result = new HashMap<>();
        for (final Element e : annotation.getAnnotationType().asElement().getEnclosedElements()) {
            if (ElementKind.METHOD.equals(e.getKind())) {
                final AnnotationValue value = ((ExecutableElement) e).getDefaultValue();
                if (value != null) {
                    result.put((ExecutableElement) e, value);
                }
            }
        }
        result.putAll(annotation.getElementValues());
        return result;
    }

    public static int typeVarIndex(final TypeVariable type) {
        final TypeVariable<?>[] parameters = type.getGenericDeclaration().getTypeParameters();
        int index = -1;
        for (final TypeVariable<?> parameter : parameters) {
            index++;
            if (type.equals(parameter)) {
                return index;
            }
        }
        return -1;
    }

    public static boolean isInvokableMethod(final Element method) {
        if (!ElementKind.METHOD.equals(method.getKind())) {
            return false;
        }

        if (ContextUtils.isFinalize(method.getSimpleName())) {
            return false;
        }

        for (final Modifier modifier : method.getModifiers()) {
            if (Modifier.PUBLIC.equals(modifier)) {
                return true;
            }
        }

        return false;
    }
}
