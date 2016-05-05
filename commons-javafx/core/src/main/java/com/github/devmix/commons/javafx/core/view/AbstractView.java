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

package com.github.devmix.commons.javafx.core.view;

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
import com.github.devmix.commons.javafx.api.views.View;

/**
 * @author Sergey Grachev
 */
public abstract class AbstractView implements View {

    @Override
    public <T, D extends Decorator<T, D>> D create(final Class<D> decoratorClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <D extends Decorator<?, D>> D wrap(final Object instance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BorderPaneDecorator borderPane() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ButtonDecorator button() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ContextMenuDecorator contextMenu() {
        throw new UnsupportedOperationException();
    }

    @Override
    public GridPaneDecorator gridPane() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> ListViewDecorator<T> listView() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MaskerPaneDecorator maskerPane() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MenuDecorator menu() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MenuBarDecorator menuBar() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MenuItemDecorator menuItem() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NotificationPaneDecorator notificationPane() {
        throw new UnsupportedOperationException();
    }

    @Override
    public StackPaneDecorator stackPane() {
        throw new UnsupportedOperationException();
    }

    @Override
    public StatusBarDecorator statusBar() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S> TableViewDecorator<S> tableView() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TabDecorator tab() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TabPaneDecorator tabPane() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextDecorator text() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextFieldDecorator textField() {
        throw new UnsupportedOperationException();
    }
}
