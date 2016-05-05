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

public interface LabeledMixin<D> extends com.github.devmix.commons.javafx.api.mixins.javafx.scene.control.ControlMixin<D> {
    D withMnemonicParsing(boolean arg0);

    D withTextAlignment(javafx.scene.text.TextAlignment arg0);

    D withTextOverrun(javafx.scene.control.OverrunStyle arg0);

    D withEllipsisString(java.lang.String arg0);

    D withWrapText(boolean arg0);

    D withFont(javafx.scene.text.Font arg0);

    D withUnderline(boolean arg0);

    D withLineSpacing(double arg0);

    D withContentDisplay(javafx.scene.control.ContentDisplay arg0);

    D withGraphicTextGap(double arg0);

    D withTextFill(javafx.scene.paint.Paint arg0);

    D withText(java.lang.String arg0);

    D withGraphic(javafx.scene.Node arg0);

    D withAlignment(javafx.geometry.Pos arg0);

}