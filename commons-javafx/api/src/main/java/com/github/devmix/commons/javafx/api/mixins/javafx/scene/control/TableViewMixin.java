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

package com.github.devmix.commons.javafx.api.mixins.javafx.scene.control;

public interface TableViewMixin<D, S> extends com.github.devmix.commons.javafx.api.mixins.javafx.scene.control.ControlMixin<D> {
    D withColumnResizePolicy(javafx.util.Callback<javafx.scene.control.TableView.ResizeFeatures, java.lang.Boolean> arg0);

    D withItems(javafx.collections.ObservableList<S> arg0);

    D withTableMenuButtonVisible(boolean arg0);

    D withRowFactory(javafx.util.Callback<javafx.scene.control.TableView<S>, javafx.scene.control.TableRow<S>> arg0);

    D withPlaceholder(javafx.scene.Node arg0);

    D withSelectionModel(javafx.scene.control.TableView.TableViewSelectionModel<S> arg0);

    D withFocusModel(javafx.scene.control.TableView.TableViewFocusModel<S> arg0);

    D withEditable(boolean arg0);

    D withFixedCellSize(double arg0);

    D withSortPolicy(javafx.util.Callback<javafx.scene.control.TableView<S>, java.lang.Boolean> arg0);

    D withOnSort(javafx.event.EventHandler<javafx.scene.control.SortEvent<javafx.scene.control.TableView<S>>> arg0);

    D withOnScrollTo(javafx.event.EventHandler<javafx.scene.control.ScrollToEvent<java.lang.Integer>> arg0);

    D withOnScrollToColumn(javafx.event.EventHandler<javafx.scene.control.ScrollToEvent<javafx.scene.control.TableColumn<S, ?>>> arg0);

}