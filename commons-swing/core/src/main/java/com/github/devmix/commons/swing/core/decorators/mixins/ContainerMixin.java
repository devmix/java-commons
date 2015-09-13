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

import com.github.devmix.commons.swing.api.decorators.ComponentDecorator;
import com.github.devmix.commons.swing.core.utils.DecoratorUtils;
import javassist.CannotCompileException;
import javassist.NotFoundException;

import java.awt.*;

/**
 * @author Sergey Grachev
 */
public class ContainerMixin extends AbstractMixin {

    @Override
    protected void execute() throws CannotCompileException, NotFoundException {
        final String u = DecoratorUtils.class.getName();

        m("a",
                "{$().add($1); return $0;}", c(Component.class));

        m("a",
                "{" + u + ".add($(), $1); return $0;}", c(Component[].class));

        m("a",
                "{" + u + ".add($0, $1); return $0;}", c(ComponentDecorator.class));

        m("a",
                "{" + u + ".add($0, $1); return $0;}", c(ComponentDecorator[].class));

        m("a",
                "{$().add($1, $2); return $0;}", c(Component.class), c(String.class));

        m("a",
                "{" + u + ".add($(), $1, $2); return $0;}", c(ComponentDecorator.class), c(String.class));

        m("layout",
                "{$().setLayout($1); return $0;}", c(LayoutManager.class));
    }
}
