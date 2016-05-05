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

package com.github.devmix.commons.javafx.api.mixins.javafx.scene.text;

public interface TextMixin<D> extends com.github.devmix.commons.javafx.api.mixins.javafx.scene.shape.ShapeMixin<D> {
    D withTextAlignment(javafx.scene.text.TextAlignment arg0);

    D withFont(javafx.scene.text.Font arg0);

    D withUnderline(boolean arg0);

    D withLineSpacing(double arg0);

    D withX(double arg0);

    D withY(double arg0);

    D withText(java.lang.String arg0);

    D withTextOrigin(javafx.geometry.VPos arg0);

    D withBoundsType(javafx.scene.text.TextBoundsType arg0);

    D withWrappingWidth(double arg0);

    D withStrikethrough(boolean arg0);

    D withFontSmoothingType(javafx.scene.text.FontSmoothingType arg0);

}