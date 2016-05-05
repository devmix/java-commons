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

public interface ListViewMixin<D, T> extends com.github.devmix.commons.javafx.api.mixins.javafx.scene.control.ControlMixin<D> {
    D withItems(javafx.collections.ObservableList<T> arg0);

    D withPlaceholder(javafx.scene.Node arg0);

    D withSelectionModel(javafx.scene.control.MultipleSelectionModel<T> arg0);

    D withFocusModel(javafx.scene.control.FocusModel<T> arg0);

    D withEditable(boolean arg0);

    D withFixedCellSize(double arg0);

    D withOnScrollTo(javafx.event.EventHandler<javafx.scene.control.ScrollToEvent<java.lang.Integer>> arg0);

    D withOrientation(javafx.geometry.Orientation arg0);

    D withCellFactory(javafx.util.Callback<javafx.scene.control.ListView<T>, javafx.scene.control.ListCell<T>> arg0);

    D withOnEditStart(javafx.event.EventHandler<javafx.scene.control.ListView.EditEvent<T>> arg0);

    D withOnEditCommit(javafx.event.EventHandler<javafx.scene.control.ListView.EditEvent<T>> arg0);

    D withOnEditCancel(javafx.event.EventHandler<javafx.scene.control.ListView.EditEvent<T>> arg0);

}