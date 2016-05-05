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

public interface TableColumnMixin<D, S, T> extends com.github.devmix.commons.javafx.api.mixins.javafx.scene.control.TableColumnBaseMixin<D, S, T> {
    D withSortType(javafx.scene.control.TableColumn.SortType arg0);

    D withCellValueFactory(javafx.util.Callback<javafx.scene.control.TableColumn.CellDataFeatures<S, T>, javafx.beans.value.ObservableValue<T>> arg0);

    D withCellFactory(javafx.util.Callback<javafx.scene.control.TableColumn<S, T>, javafx.scene.control.TableCell<S, T>> arg0);

    D withOnEditStart(javafx.event.EventHandler<javafx.scene.control.TableColumn.CellEditEvent<S, T>> arg0);

    D withOnEditCommit(javafx.event.EventHandler<javafx.scene.control.TableColumn.CellEditEvent<S, T>> arg0);

    D withOnEditCancel(javafx.event.EventHandler<javafx.scene.control.TableColumn.CellEditEvent<S, T>> arg0);

}