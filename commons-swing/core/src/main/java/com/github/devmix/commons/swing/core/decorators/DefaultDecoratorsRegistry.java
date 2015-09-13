/*
 * Commons Library
 * Copyright (c) 2015 Sergey Grachev (sergey.grachev@yahoo.com). All rights reserved.
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

package com.github.devmix.commons.swing.core.decorators;

import com.github.devmix.commons.swing.core.decorators.exceptions.DecoratorCreationException;
import com.github.devmix.commons.swing.core.decorators.mixins.BuilderMixin;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sergey Grachev
 */
public class DefaultDecoratorsRegistry {

    public static final String NO_APPROPRIATE_CONSTRUCTOR_OF_DECORATOR = "No appropriate constructor of decorator";
    private final Map<Class<?>, Class<?>> classes = new ConcurrentHashMap<>();
    private final Map<Key, Constructor<?>> constructorCache = new ConcurrentHashMap<>();

    public void register(final Class<?> interfaceClass, final Class<?> decoratorClass,
                         final Class<? extends Component> decoratedClass, final BuilderMixin... mixins) {

        final StringBuilder name = new StringBuilder();
        if (mixins != null && mixins.length > 0) {
            for (final BuilderMixin mixin : mixins) {
                if (name.length() > 0) {
                    name.append("_");
                }
                name.append(mixin.getClass().getSimpleName());
            }
        } else {
            name.append("NoMixin");
        }

        final ClassPool pool = ClassPool.getDefault();
        try {
            final CtClass ctSuperClass = pool.get(decoratorClass.getName());
            final CtClass ctDecoratorClass = pool.makeClass(decoratorClass.getName() + "$" + name.toString());
            final CtClass ctInterfaceClass = pool.get(interfaceClass.getName());
            ctDecoratorClass.setSuperclass(ctSuperClass);

            if (mixins != null && mixins.length > 0) {
                for (final BuilderMixin mixin : mixins) {
                    mixin.mix(pool, ctDecoratorClass, ctInterfaceClass, decoratedClass);
                }
            }

            classes.put(decoratorClass, ctDecoratorClass.toClass());
        } catch (NotFoundException | CannotCompileException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @throws com.github.devmix.commons.swing.core.decorators.exceptions.DecoratorCreationException
     */
    @SuppressWarnings("unchecked")
    public <T> T create(final Class<?> decoratorClass, final Object... args) {
        final Class<?> clazz = classes.get(decoratorClass);
        if (clazz == null) {
            throw new DecoratorCreationException(decoratorClass, "No decorator implementation");
        }
        try {
            if (args != null && args.length > 0) {
                final Key key = Key.of(clazz, args, null);
                Constructor<?> constructor = constructorCache.get(key);
                if (constructor == null) {
                    constructor = clazz.getConstructor(key.args);
                    constructorCache.putIfAbsent(key, constructor);
                }
                return (T) constructor.newInstance(args);
            }
            return (T) clazz.newInstance();
        } catch (final NoSuchMethodException e) {
            throw new DecoratorCreationException(decoratorClass, "No appropriate constructor of decorator", e);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DecoratorCreationException(decoratorClass, null, e);
        }
    }

    /**
     * @throws com.github.devmix.commons.swing.core.decorators.exceptions.DecoratorCreationException
     */
    @SuppressWarnings("unchecked")
    public <T> T create(final Class<?> decoratorClass, final Object[] args, final Class<?>[] argsClass) {
        final Class<?> clazz = classes.get(decoratorClass);
        if (clazz == null) {
            throw new DecoratorCreationException(decoratorClass, "No decorator implementation");
        }
        try {
            if (args != null && args.length > 0) {
                final Key key = Key.of(clazz, args, argsClass);
                Constructor<?> constructor = constructorCache.get(key);
                if (constructor == null) {
                    constructor = clazz.getConstructor(key.args);
                    constructorCache.putIfAbsent(key, constructor);
                }
                return (T) constructor.newInstance(args);
            }
            return (T) clazz.newInstance();
        } catch (final NoSuchMethodException e) {
            throw new DecoratorCreationException(decoratorClass, NO_APPROPRIATE_CONSTRUCTOR_OF_DECORATOR, e);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DecoratorCreationException(decoratorClass, null, e);
        }
    }

    private static final class Key {

        private final Class<?> clazz;
        private final Class<?>[] args;
        private final int hashCodeArgs;
        private final int hashCode;

        public Key(final Class<?> clazz, final Class[] args) {
            this.clazz = clazz;
            this.args = args;
            this.hashCodeArgs = Arrays.hashCode(args);
            this.hashCode = calculateHashCode(hashCodeArgs);
        }

        public static Key of(final Class<?> clazz, final Object[] args, final Class<?>[] argsClass) {
            final Class[] classes;
            if (argsClass == null) {
                classes = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    final Object arg = args[i];
                    classes[i] = arg == null ? null : arg.getClass();
                }
            } else {
                classes = argsClass;
            }
            return new Key(clazz, classes);
        }

        private int calculateHashCode(final int hashCodeArgs) {
            int result = clazz != null ? clazz.hashCode() : 0;
            result = 31 * result + (args != null ? hashCodeArgs : 0);
            return result;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final Key key = (Key) o;

            if (hashCode != key.hashCode) return false;
            if (clazz != null ? !clazz.equals(key.clazz) : key.clazz != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }
}
