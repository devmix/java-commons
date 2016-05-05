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

package com.github.devmix.commons.javafx.core.components;

import com.github.devmix.commons.adapters.api.annotations.Adapter;
import com.github.devmix.commons.adapters.api.annotations.DelegateRule;
import com.github.devmix.commons.javafx.api.components.standard.GridPaneDecorator;
import com.github.devmix.commons.javafx.api.utils.gridpane.ColumnConstraintsDecorator;
import com.github.devmix.commons.javafx.api.utils.gridpane.RowConstraintsDecorator;
import com.github.devmix.commons.javafx.core.annotations.DelegateWithToSet;
import com.github.devmix.commons.javafx.core.decorators.AbstractDecorator;
import javafx.collections.ObservableList;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 * @author Sergey Grachev
 */
@Adapter
@DelegateWithToSet
@DelegateRule(to = "add", from = "add")
abstract class GridPaneDecoratorImpl extends AbstractDecorator<GridPane> implements GridPaneDecorator {

    public GridPaneDecoratorImpl() {
        super(new GridPane());
    }

    @Override
    public GridPaneDecorator columnConstraints(final ColumnConstraintsDecorator... constraints) {
        final ObservableList<ColumnConstraints> list = subject.getColumnConstraints();
        for (final ColumnConstraintsDecorator item : constraints) {
            list.add(item.$());
        }
        return this;
    }

    @Override
    public GridPaneDecorator rowConstraints(final RowConstraintsDecorator... constraints) {
        final ObservableList<RowConstraints> list = subject.getRowConstraints();
        for (final RowConstraintsDecorator item : constraints) {
            list.add(item.$());
        }
        return this;
    }
}
