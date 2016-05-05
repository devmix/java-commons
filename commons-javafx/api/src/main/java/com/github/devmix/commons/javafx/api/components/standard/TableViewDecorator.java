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

package com.github.devmix.commons.javafx.api.components.standard;

import com.github.devmix.commons.javafx.api.decorators.Decorator;
import com.github.devmix.commons.javafx.api.mixins.javafx.scene.control.TableViewMixin;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

/**
 * @author Sergey Grachev
 */
public interface TableViewDecorator<S> extends
        TableViewMixin<TableViewDecorator<S>, S>, Decorator<TableView<S>, TableViewDecorator<S>> {

    @SuppressWarnings({"varargs", "unchecked"})
    TableViewDecorator<S> columns(TableColumn<S, ?>... value);

    TableViewDecorator<S> columns(List<TableColumn<S, ?>> value);
}
