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

package com.github.devmix.commons.javafx.api.mixins.javafx.stage;

public interface WindowMixin<D> {
    D withX(double arg0);

    D withY(double arg0);

    D withOnCloseRequest(javafx.event.EventHandler<javafx.stage.WindowEvent> arg0);

    D withOnShowing(javafx.event.EventHandler<javafx.stage.WindowEvent> arg0);

    D withOnShown(javafx.event.EventHandler<javafx.stage.WindowEvent> arg0);

    D withOnHiding(javafx.event.EventHandler<javafx.stage.WindowEvent> arg0);

    D withOnHidden(javafx.event.EventHandler<javafx.stage.WindowEvent> arg0);

    D withWidth(double arg0);

    D withHeight(double arg0);

    D withUserData(java.lang.Object arg0);

    D withOpacity(double arg0);

    D withEventDispatcher(javafx.event.EventDispatcher arg0);

}