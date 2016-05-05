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

package com.github.devmix.commons.javafx.api.views;

import com.github.devmix.commons.javafx.api.components.controlsfx.MaskerPaneDecorator;
import com.github.devmix.commons.javafx.api.components.controlsfx.NotificationPaneDecorator;
import com.github.devmix.commons.javafx.api.components.controlsfx.StatusBarDecorator;
import com.github.devmix.commons.javafx.api.components.standard.BorderPaneDecorator;
import com.github.devmix.commons.javafx.api.components.standard.ButtonDecorator;
import com.github.devmix.commons.javafx.api.components.standard.ContextMenuDecorator;
import com.github.devmix.commons.javafx.api.components.standard.GridPaneDecorator;
import com.github.devmix.commons.javafx.api.components.standard.ListViewDecorator;
import com.github.devmix.commons.javafx.api.components.standard.MenuBarDecorator;
import com.github.devmix.commons.javafx.api.components.standard.MenuDecorator;
import com.github.devmix.commons.javafx.api.components.standard.MenuItemDecorator;
import com.github.devmix.commons.javafx.api.components.standard.StackPaneDecorator;
import com.github.devmix.commons.javafx.api.components.standard.TabDecorator;
import com.github.devmix.commons.javafx.api.components.standard.TabPaneDecorator;
import com.github.devmix.commons.javafx.api.components.standard.TableViewDecorator;
import com.github.devmix.commons.javafx.api.components.standard.TextDecorator;
import com.github.devmix.commons.javafx.api.components.standard.TextFieldDecorator;
import com.github.devmix.commons.javafx.api.decorators.Decorator;

/**
 * @author Sergey Grachev
 */
public interface View {

    <T, D extends Decorator<T, D>> D create(Class<D> decoratorClass);

    <D extends Decorator<?, D>> D wrap(Object instance);

    BorderPaneDecorator borderPane();

    ButtonDecorator button();

    ContextMenuDecorator contextMenu();

    GridPaneDecorator gridPane();

    <T> ListViewDecorator<T> listView();

    MaskerPaneDecorator maskerPane();

    MenuDecorator menu();

    MenuBarDecorator menuBar();

    MenuItemDecorator menuItem();

    NotificationPaneDecorator notificationPane();

    StackPaneDecorator stackPane();

    StatusBarDecorator statusBar();

    <S> TableViewDecorator<S> tableView();

    TabDecorator tab();

    TabPaneDecorator tabPane();

    TextDecorator text();

    TextFieldDecorator textField();
}
