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
import com.github.devmix.commons.javafx.api.utils.Utils;
import com.github.devmix.commons.javafx.api.utils.UtilsFactory;

/**
 * @author Sergey Grachev
 */
public final class DefaultUtils implements Utils {

    private final GridPaneUtils gridPaneUtils;
    private final TableViewUtils tableViewUtils;

    public DefaultUtils(final UtilsFactory utilsFactory, final ToolkitService toolkitService) {
        this.gridPaneUtils = utilsFactory.createGridPaneUtils(toolkitService);
        this.tableViewUtils = utilsFactory.createTableViewUtils(toolkitService);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends GridPaneUtils> T gridPane() {
        return (T) gridPaneUtils;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends TableViewUtils> T tableView() {
        return (T) tableViewUtils;
    }
}
