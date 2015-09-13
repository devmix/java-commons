/*
 * Commons Library
 * Copyright (c) 2015 Sergey Grachev (sergey.grachev@yahoo.com). All rights reserved.
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

package com.github.devmix.commons.swing.api.bindings.standard;

/**
 * @author Sergey Grachev
 */
public final class ColumnBinding {

    private String propertyName;
    private String title;
    private Class<?> columnClass;
    private boolean editable;

    public static ColumnBinding column() {
        return new ColumnBinding();
    }

    public ColumnBinding editable(final boolean editable) {
        this.editable = editable;
        return this;
    }

    public Class<?> getColumnClass() {
        return columnClass;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getTitle() {
        return title;
    }

    public boolean isEditable() {
        return editable;
    }

    public ColumnBinding property(final String propertyName) {
        this.propertyName = propertyName;
        return this;
    }

    public ColumnBinding title(final String title) {
        this.title = title;
        return this;
    }

    public ColumnBinding type(final Class<?> columnClass) {
        this.columnClass = columnClass;
        return this;
    }
}
