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

public interface ColumnConstraintsMixin<D> extends com.github.devmix.commons.javafx.api.mixins.javafx.scene.layout.ConstraintsBaseMixin<D> {
    D withHalignment(javafx.geometry.HPos arg0);

    D withHgrow(javafx.scene.layout.Priority arg0);

    D withFillWidth(boolean arg0);

    D withMinWidth(double arg0);

    D withPrefWidth(double arg0);

    D withMaxWidth(double arg0);

    D withPercentWidth(double arg0);

}