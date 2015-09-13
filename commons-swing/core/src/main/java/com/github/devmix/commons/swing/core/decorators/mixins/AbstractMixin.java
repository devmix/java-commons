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

package com.github.devmix.commons.swing.core.decorators.mixins;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import java.awt.*;

/**
 * @author Sergey Grachev
 */
public abstract class AbstractMixin implements BuilderMixin {

    protected ClassPool pool;
    protected CtClass decoratorClass;
    protected CtClass interfaceClass;
    protected Class<? extends Component> decoratedClass;

    @Override
    public void mix(final ClassPool pool, final CtClass decoratorClass, final CtClass interfaceClass,
                    final Class<? extends Component> decoratedClass) throws NotFoundException, CannotCompileException {
        configure(pool, decoratorClass, interfaceClass, decoratedClass);
        try {
            execute();
        } finally {
            cleanup();
        }
    }

    protected abstract void execute() throws CannotCompileException, NotFoundException;

    protected void configure(final ClassPool pool, final CtClass decoratorClass, final CtClass interfaceClass,
                             final Class<? extends Component> decoratedClass) {
        this.pool = pool;
        this.decoratorClass = decoratorClass;
        this.interfaceClass = interfaceClass;
        this.decoratedClass = decoratedClass;
    }

    protected void cleanup() {
        this.pool = null;
        this.decoratorClass = null;
        this.interfaceClass = null;
        this.decoratedClass = null;
    }

    protected void m(final String name, final String body, final CtClass... parameters) throws CannotCompileException {
        m(name, interfaceClass, body, parameters);
    }

    protected void m(final String name, final CtClass returnType, final String body, final CtClass... parameters) throws CannotCompileException {
        decoratorClass.addMethod(CtNewMethod.make(returnType, name, parameters, null, body, decoratorClass));
    }

    protected CtClass c(final Class<?> clazz) throws NotFoundException {
        return pool.get(clazz.getName());
    }
}
