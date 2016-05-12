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

package com.github.devmix.commons.adapters.core.contexts;

import com.github.devmix.commons.adapters.api.AdapterContextBuilder;
import com.github.devmix.commons.adapters.api.AdaptersContext;
import com.github.devmix.commons.adapters.api.annotations.Adapter;
import com.github.devmix.commons.adapters.api.annotations.DelegateMethod;
import com.github.devmix.commons.adapters.api.exceptions.AdapterGenerationException;
import com.github.devmix.commons.adapters.core.utils.Constants;
import com.github.devmix.commons.adapters.core.utils.ContextUtils;
import com.github.devmix.commons.adapters.core.utils.ProcessorUtils;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.Descriptor;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.devmix.commons.adapters.api.annotations.Adapter.Processing;
import static com.github.devmix.commons.adapters.core.utils.ContextUtils.className;

/**
 * @author Sergey Grachev
 */
final class DefaultAdapterContextBuilder implements AdapterContextBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultAdapterContextBuilder.class);

    private final Set<String> packages = new HashSet<>();
    private final DefaultAnnotationsScanner annotationsScanner = new DefaultAnnotationsScanner();
    private Processing forcedProcessing;

    @Override
    public AdapterContextBuilder addPackage(final String packageName) {
        packages.add(packageName);
        return this;
    }

    @Override
    public AdaptersContext build() throws AdapterGenerationException {
        if (packages.isEmpty()) {
            throw new IllegalStateException("No packages defined");
        }

        final long time = System.nanoTime();
        final Map<Class<?>, AdapterDescriptor> adapters;
        try {
            adapters = generate(ContextUtils.findClassesWithAnnotation(packages, Adapter.class));
        } catch (IOException | ClassNotFoundException | CannotCompileException | NotFoundException e) {
            throw new AdapterGenerationException(e);
        }
        LOG.info("Generated {} ms", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - time));

        return new DefaultAdaptersContext(adapters);
    }

    @Override
    public AdapterContextBuilder forceProcessing(final Processing value) {
        this.forcedProcessing = value;
        return this;
    }

    private Map<Class<?>, AdapterDescriptor> generate(final Class<?>[] classes)
            throws ClassNotFoundException, AdapterGenerationException, NotFoundException, CannotCompileException {

        if (classes.length == 0) {
            return Collections.emptyMap();
        }

        final ClassPool pool = ClassPool.getDefault();
        final Map<Class<?>, AdapterDescriptor> result = new HashMap<>();

        for (final Class<?> adapterClass : classes) {
            processAdapter(pool, result, adapterClass);
        }

        return result;
    }

    private void processAdapter(final ClassPool pool, final Map<Class<?>, AdapterDescriptor> result,
                                final Class<?> adapterClass) throws AdapterGenerationException, CannotCompileException, NotFoundException, ClassNotFoundException {

        if (result.containsKey(adapterClass)) {
            return;
        }

//        if (adapterClass.isInterface()) {
//            throw new AdapterGenerationException("Annotation @Adapter not allowed for interfaces");
//        }

        final Adapter adapter = annotationsScanner.adapterFor(adapterClass);
        if (adapter == null || Processing.IGNORE.equals(adapter.processing())) {
            return;
        }

        final Adapter.Processing processing = forcedProcessing == null ? adapter.processing() : forcedProcessing;

        Class generatedClass = !Processing.RUNTIME.equals(processing)
                ? findCompiledImplementation(pool, adapterClass) : null;

        Method adapteeMethod = annotationsScanner.adapteeFor(adapterClass);
        if (adapteeMethod == null) {
            adapteeMethod = annotationsScanner.adapteeFor(generatedClass);
            if (adapteeMethod == null) {
                throw new AdapterGenerationException("Adapter '" + adapterClass + "' must contain adaptee provider method");
            }
        }

        final Class<?> adapteeClass = getAdapteeClass(adapter, adapterClass, adapteeMethod);

        if (generatedClass == null) {
            if (!Processing.COMPILE.equals(processing)) {
                generatedClass = generateAdapter(pool, adapterClass, adapter, adapteeMethod, adapteeClass).toClass();
            }
        } else {
            LOG.trace("Found compiled implementation of adapter");
        }

        if (generatedClass != null) {
            result.put(adapterClass, new AdapterDescriptor(adapteeClass, generatedClass));
            LOG.trace("Generated new adapter:\n\tadapterClass:\t" + adapterClass.getName() +
                    "\n\tadapteeMethod:\t" + adapteeMethod +
                    "\n\tadapteeClass:\t" + adapteeClass.getName() +
                    "\n\tadapterMethods:\t" + Arrays.toString(adapterClass.getMethods()) +
                    "\n\tgeneratedClass:\t" + generatedClass.getName());
        }
    }

    private CtClass generateAdapter(final ClassPool pool, final Class<?> adapterClass, final Adapter adapter,
                                    final Method adapteeMethod, final Class<?> adapteeClass) throws NotFoundException, CannotCompileException, ClassNotFoundException {

        final CtClass ctAdapterClass = pool.get(adapterClass.getName());
        final CtClass ctAdapteeClass = pool.get(adapteeClass.getName());
        final CtClass ctImplementationClass = pool.makeClass(className(adapterClass, Constants.ADAPTER_RUNTIME_CLASS_NAME));
        ctImplementationClass.setSuperclass(ctAdapterClass);

        final DelegateMethod[] rules = annotationsScanner.globalDelegateMethodsOf(adapterClass);

        for (final CtMethod adapterMethod : ctAdapterClass.getMethods()) {
            if (!adapterMethod.isEmpty()
                    || !ContextUtils.isAcceptableMethod(adapterMethod.getName(), adapterMethod.getModifiers())) {
                continue;
            }

            final Pair<CtMethod, DelegateMethod> foundMethod = findMethod(rules, adapterMethod, ctAdapteeClass);
            if (foundMethod == null) {
                LOG.debug("No adaptee method for {}", adapterMethod);
                continue;
            }

            final CtMethod adaptedMethod = foundMethod.getKey();
            final CtClass[] parameters = adaptedMethod.getParameterTypes();

            final StringBuilder sb = generateMethodCode(adapteeMethod, adapteeClass, ctAdapterClass, adapterMethod,
                    adaptedMethod, foundMethod.getValue(), parameters);

            ctImplementationClass.addMethod(CtNewMethod.make(
                    adapterMethod.getReturnType(), adapterMethod.getName(), parameters, null, sb.toString(), ctImplementationClass));
        }
        return ctImplementationClass;
    }

    @Nullable
    private Class findCompiledImplementation(final ClassPool pool, final Class<?> adapterClass) {
        final String className = className(adapterClass, Constants.ADAPTER_COMPILED_CLASS_NAME);
        if (pool.find(className) != null) {
            try {
                return Class.forName(className);
            } catch (final ClassNotFoundException ignore) {
                // ignore
            }
        }
        return null;
    }

    private Class<?> getAdapteeClass(final Adapter adapter, final Class<?> adapterClass, final Method adapteeMethod) {
        if (adapter.adaptee() != Void.class && adapter.adaptee() != void.class) {
            return adapter.adaptee();
        }

        Type type = adapteeMethod.getGenericReturnType();
        if (type instanceof TypeVariable) {
            final TypeVariable adapteeReturnType = (TypeVariable) type;
            type = adapterClass.getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                final ParameterizedType adapterGeneric = (ParameterizedType) type;
                final GenericDeclaration adapteeMethodOwner = adapteeReturnType.getGenericDeclaration();
                if (adapterGeneric.getRawType().equals(adapteeMethodOwner)) {
                    final int parameterIndex = ProcessorUtils.typeVarIndex(adapteeReturnType);
                    if (parameterIndex != -1) {
                        final Type actualType = adapterGeneric.getActualTypeArguments()[parameterIndex];
                        return (Class<?>) (actualType instanceof ParameterizedType
                                ? ((ParameterizedType) actualType).getRawType() : actualType);
                    }
                }
            }
        }

        return adapteeMethod.getReturnType();
    }

    private StringBuilder generateMethodCode(final Method adapteeMethod, final Class<?> adapteeClass,
                                             final CtClass ctAdapterClass, final CtMethod adapterMethod,
                                             final CtMethod adaptedMethod, final DelegateMethod rule, final CtClass[] parameters) throws NotFoundException {

        final StringBuilder sb = new StringBuilder("((")
                .append(adapteeClass.getName()).append(") ").append(adapteeMethod.getName()).append("()).")
                .append(adaptedMethod.getName()).append("(");
        for (int i = 0; i < parameters.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("$").append(i + 1);
        }
        sb.append(");");

        switch (rule.returnValue()) {
            case THIS:
                sb.insert(0, "{").append(" return $0;}");
                break;

            case RESULT:
                sb.insert(0, "{return ").append("}");
                break;

            default:
                final boolean isAdapterReturnVoid = "void".equals(adapterMethod.getReturnType().getName());
                final boolean isAdaptedReturnVoid = "void".equals(adaptedMethod.getReturnType().getName());

                if (isAdapterReturnVoid) {
                    sb.insert(0, "{").append("}");
                } else if (isAdaptedReturnVoid) {
                    if (ctAdapterClass.subtypeOf(adapterMethod.getReturnType())) {
                        sb.insert(0, "{").append(" return $0;}");
                    } else {
                        sb.insert(0, "{").append(" return null;}");
                    }
                } else {
                    if (adaptedMethod.getReturnType().subtypeOf(adapterMethod.getReturnType())) {
                        sb.insert(0, "{return ").append("}");
                    } else {
                        if (ctAdapterClass.subtypeOf(adapterMethod.getReturnType())) {
                            sb.insert(0, "{").append(" return $0;}");
                        } else {
                            sb.insert(0, "{").append(" return null;}");
                        }
                    }
                }
        }

        return sb;
    }

    @Nullable
    private Pair<CtMethod, DelegateMethod> findMethod(final DelegateMethod[] rules, final CtMethod method,
                                                      final CtClass adapteeClass) throws ClassNotFoundException, NotFoundException {
        Pair<CtMethod, DelegateMethod> result = null;

        final DelegateMethod[] overrideRules = annotationsScanner.methodDelegateMethodsOf(method);
        if (overrideRules != null && overrideRules.length > 0) {
            result = findTargetMethodByRules(overrideRules, method, adapteeClass);
        }

        if (result == null) {
            result = findTargetMethodByRules(rules, method, adapteeClass);
        }

        return result;
    }

    @Nullable
    private Pair<CtMethod, DelegateMethod> findTargetMethodByRules(final DelegateMethod[] rules, final CtMethod method,
                                                                   final CtClass adapteeClass) throws NotFoundException {
        for (final DelegateMethod item : rules) {
            final CtMethod result = findTargetMethodByRule(item, method, adapteeClass);
            if (result != null) {
                return Pair.of(result, item);
            }
        }
        return null;
    }

    @Nullable
    private CtMethod findTargetMethodByRule(final DelegateMethod rule, final CtMethod adapterMethod, final CtClass adapteeClass) throws NotFoundException {
        final String fullAdapterMethodName = adapterMethod.getName();
        final Pattern adapterMethodPattern = Pattern.compile(rule.from());
        final Matcher adapterMethodMatcher = adapterMethodPattern.matcher(fullAdapterMethodName);
        if (!adapterMethodMatcher.matches()) {
            return null;
        }

        final boolean isAdapterMethodPattern = adapterMethodMatcher.groupCount() != 0;
        final String adapterMethodName = isAdapterMethodPattern
                ? adapterMethodMatcher.group(1) : fullAdapterMethodName;
        final Pattern adapteeMethodPattern = Pattern.compile(rule.to());
        for (final CtMethod adapteeMethod : adapteeClass.getMethods()) {
            if (!ContextUtils.isInvokableMethod(adapteeMethod)) {
                continue;
            }

            final String fullAdapteeMethodName = adapteeMethod.getName();

            final Matcher adapteeMethodMatcher = adapteeMethodPattern.matcher(adapteeMethod.getName());
            if (!adapteeMethodMatcher.matches()) {
                continue;
            }

            final boolean isAdapteeMethodPattern = adapteeMethodMatcher.groupCount() != 0;
            final String adapteeMethodName = isAdapteeMethodPattern
                    ? adapteeMethodMatcher.group(1) : fullAdapteeMethodName;

            final boolean matches = (isAdapterMethodPattern && isAdapteeMethodPattern)
                    ? adapterMethodName.equals(adapteeMethodName)
                    : fullAdapterMethodName.equals(rule.from()) && fullAdapteeMethodName.equals(rule.to());

            if (matches) {
                final String targetSignature = adapteeMethod.getSignature();
                final String desc = Descriptor.getParamDescriptor(adapterMethod.getSignature())
                        + targetSignature.substring(targetSignature.indexOf(')') + 1);

                CtMethod method = null;
                try {
                    method = adapteeClass.getMethod(fullAdapteeMethodName, desc);
                } catch (final NotFoundException ignore) {
                }

                if (method != null) {
                    return method;
                }
            }
        }

        return null;
    }
}
