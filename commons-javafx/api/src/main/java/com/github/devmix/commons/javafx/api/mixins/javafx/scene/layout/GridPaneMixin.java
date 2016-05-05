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

package com.github.devmix.commons.javafx.api.mixins.javafx.scene.layout;

public interface GridPaneMixin<D> extends com.github.devmix.commons.javafx.api.mixins.javafx.scene.layout.PaneMixin<D> {
    D withMargin(javafx.scene.Node arg0, javafx.geometry.Insets arg1);

    D withRowIndex(javafx.scene.Node arg0, java.lang.Integer arg1);

    D withColumnIndex(javafx.scene.Node arg0, java.lang.Integer arg1);

    D withRowSpan(javafx.scene.Node arg0, java.lang.Integer arg1);

    D withColumnSpan(javafx.scene.Node arg0, java.lang.Integer arg1);

    D withHalignment(javafx.scene.Node arg0, javafx.geometry.HPos arg1);

    D withValignment(javafx.scene.Node arg0, javafx.geometry.VPos arg1);

    D withHgrow(javafx.scene.Node arg0, javafx.scene.layout.Priority arg1);

    D withVgrow(javafx.scene.Node arg0, javafx.scene.layout.Priority arg1);

    D withFillWidth(javafx.scene.Node arg0, java.lang.Boolean arg1);

    D withFillHeight(javafx.scene.Node arg0, java.lang.Boolean arg1);

    D withConstraints(javafx.scene.Node arg0, int arg1, int arg2, int arg3, int arg4);

    D withConstraints(javafx.scene.Node arg0, int arg1, int arg2, int arg3, int arg4, javafx.geometry.HPos arg5, javafx.geometry.VPos arg6, javafx.scene.layout.Priority arg7, javafx.scene.layout.Priority arg8, javafx.geometry.Insets arg9);

    D withConstraints(javafx.scene.Node arg0, int arg1, int arg2, int arg3, int arg4, javafx.geometry.HPos arg5, javafx.geometry.VPos arg6, javafx.scene.layout.Priority arg7, javafx.scene.layout.Priority arg8);

    D withConstraints(javafx.scene.Node arg0, int arg1, int arg2, int arg3, int arg4, javafx.geometry.HPos arg5, javafx.geometry.VPos arg6);

    D withConstraints(javafx.scene.Node arg0, int arg1, int arg2);

    D withHgap(double arg0);

    D withVgap(double arg0);

    D withGridLinesVisible(boolean arg0);

    D withAlignment(javafx.geometry.Pos arg0);

}