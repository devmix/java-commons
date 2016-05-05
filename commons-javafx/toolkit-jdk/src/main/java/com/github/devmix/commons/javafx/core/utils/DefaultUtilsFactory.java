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

package com.github.devmix.commons.javafx.core.utils;

import com.github.devmix.commons.javafx.api.ToolkitService;
import com.github.devmix.commons.javafx.api.utils.GridPaneUtils;
import com.github.devmix.commons.javafx.api.utils.TableViewUtils;
import com.github.devmix.commons.javafx.api.utils.UtilsFactory;
import com.github.devmix.commons.javafx.api.utils.gridpane.ColumnConstraintsDecorator;
import com.github.devmix.commons.javafx.api.utils.gridpane.RowConstraintsDecorator;
import com.github.devmix.commons.javafx.api.utils.tableview.TableColumnDecorator;
import com.github.devmix.commons.javafx.core.JavaFXToolkitService;

/**
 * @author Sergey Grachev
 */
public final class DefaultUtilsFactory implements UtilsFactory<DefaultUtils> {

    @Override
    public DefaultUtils createUtils(final ToolkitService toolkitService) {
        return new DefaultUtils(this, toolkitService);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends GridPaneUtils> T createGridPaneUtils(final ToolkitService toolkitService) {
        return (T) new DefaultGridPaneUtils(toolkitService);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends TableViewUtils> T createTableViewUtils(final ToolkitService toolkitService) {
        return (T) new DefaultTableViewUtils(toolkitService);
    }

    static final class DefaultTableViewUtils implements TableViewUtils {

        private final ToolkitService toolkitService;

        public DefaultTableViewUtils(final ToolkitService toolkitService) {
            this.toolkitService = toolkitService;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <S, T> TableColumnDecorator<S, T> column() {
            return JavaFXToolkitService.adapters().findAndCreate(TableColumnDecorator.class, toolkitService.i18n());
        }
    }

    static final class DefaultGridPaneUtils implements GridPaneUtils {

        private final ToolkitService toolkitService;

        public DefaultGridPaneUtils(final ToolkitService toolkitService) {
            this.toolkitService = toolkitService;
        }

        @Override
        public ColumnConstraintsDecorator columnConstraint() {
            return JavaFXToolkitService.adapters().findAndCreate(ColumnConstraintsDecorator.class);
        }

        @Override
        public RowConstraintsDecorator rowConstraint() {
            return JavaFXToolkitService.adapters().findAndCreate(RowConstraintsDecorator.class);
        }
    }
}
