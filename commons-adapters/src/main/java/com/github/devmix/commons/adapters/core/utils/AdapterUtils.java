/*
 * Commons Library
 * Copyright (c) 2016 Sergey Grachev (sergey.grachev@yahoo.com). All rights reserved.
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

import javassist.CtMethod;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.TypeVariable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility methods for adapter classes
 *
 * @author Sergey Grachev
 */
public final class AdapterUtils {

    private static final EnumSet<Modifier> SEALED_METHOD_MODIFIEDS =
            EnumSet.of(Modifier.FINAL, Modifier.NATIVE, Modifier.PRIVATE);

    private static final int SEALED_METHOD_MODIFIEDS_INT =
            java.lang.reflect.Modifier.FINAL | java.lang.reflect.Modifier.NATIVE | java.lang.reflect.Modifier.PRIVATE;

    private AdapterUtils() {
    }

    /**
     * Find all classes annotated by some annotation at runtime
     *
     * @param packages   list of packages where should do search
     * @param annotation annotation class
     * @return list of found classes
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Class<?>[] findClassesWithAnnotation(final Set<String> packages,
                                                       final Class<? extends Annotation> annotation) throws IOException, ClassNotFoundException {
        final Set<Class<?>> result = new HashSet<>();
        for (final String packageName : packages) {
            scanPackage(packageName, annotation, result);
        }
        return result.toArray(new Class<?>[result.size()]);
    }

    private static void scanPackage(final String packageName, final Class<? extends Annotation> annotation, final Set<Class<?>> classes) throws ClassNotFoundException, IOException {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final String path = packageName.replace('.', '/');
        final Enumeration<URL> resources = classLoader.getResources(path);
        final List<File> dirs = new ArrayList<>();

        while (resources.hasMoreElements()) {
            final URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }

        for (final File directory : dirs) {
            scanDirectory(directory, packageName, annotation, classes);
        }
    }

    private static void scanDirectory(final File directory, final String packageName,
                                      final Class<? extends Annotation> annotation, final Set<Class<?>> classes) throws ClassNotFoundException {
        if (!directory.exists()) {
            return;
        }
        final File[] files = directory.listFiles();
        assert files != null;
        for (final File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName(), annotation, classes);
            } else if (file.getName().endsWith(".class")) {
                final Class<?> clazz = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                if (clazz.isAnnotationPresent(annotation)) {
                    classes.add(clazz);
                }
            }
        }
    }

    public static boolean isAcceptableMethod(final CharSequence methodName, final Collection<Modifier> modifiers) {
        if (isFinalize(methodName)) {
            return false;
        }

        for (final Modifier modifier : modifiers) {
            if (SEALED_METHOD_MODIFIEDS.contains(modifier)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isAcceptableMethod(final CharSequence methodName, final int modifiers) {
        return !"finalize".equals(methodName) && (modifiers & SEALED_METHOD_MODIFIEDS_INT) == 0;
    }

    public static boolean isInvokableMethod(final Element method) {
        if (!ElementKind.METHOD.equals(method.getKind())) {
            return false;
        }

        if (isFinalize(method.getSimpleName())) {
            return false;
        }

        for (final Modifier modifier : method.getModifiers()) {
            if (Modifier.PUBLIC.equals(modifier)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInvokableMethod(final CtMethod method) {
        return !isFinalize(method.getName()) && java.lang.reflect.Modifier.isPublic(method.getModifiers());
    }

    private static boolean isFinalize(final CharSequence name) {
        return "finalize".equals(name);
    }

    public static boolean isVoid(final TypeMirror type) {
        final String className = type.toString();
        return "void".equals(className) || Void.class.getName().equals(className);
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
}
