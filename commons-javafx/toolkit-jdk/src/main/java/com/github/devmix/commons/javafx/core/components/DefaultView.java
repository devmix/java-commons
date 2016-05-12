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

package com.github.devmix.commons.javafx.core.components;

import com.github.devmix.commons.javafx.api.ToolkitService;
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
import com.github.devmix.commons.javafx.core.JavaFXToolkitService;

/**
 * @author Sergey Grachev
 */
public final class DefaultView implements View {

    private final ToolkitService toolkitService;

    public DefaultView(final ToolkitService toolkitService) {
        this.toolkitService = toolkitService;
    }

    @Override
    public <T, D extends Decorator<T, D>> D create(final Class<D> decoratorClass) {
        return JavaFXToolkitService.adapters().create(decoratorClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D extends Decorator<?, D>> D wrap(final Object instance) {
        return (D) JavaFXToolkitService.adapters().createByAdaptee(instance.getClass(), instance);
    }

    @Override
    public NotificationPaneDecorator notificationPane() {
        return JavaFXToolkitService.adapters().create(NotificationPaneDecorator.class);
    }

    @Override
    public StackPaneDecorator stackPane() {
        return JavaFXToolkitService.adapters().create(StackPaneDecorator.class);
    }

    @Override
    public BorderPaneDecorator borderPane() {
        return JavaFXToolkitService.adapters().create(BorderPaneDecorator.class);
    }

    @Override
    public MenuBarDecorator menuBar() {
        return JavaFXToolkitService.adapters().create(MenuBarDecorator.class);
    }

    @Override
    public MenuDecorator menu() {
        return JavaFXToolkitService.adapters().create(MenuDecorator.class, toolkitService.i18n());
    }

    @Override
    public MenuItemDecorator menuItem() {
        return JavaFXToolkitService.adapters().create(MenuItemDecorator.class, toolkitService.i18n());
    }

    @Override
    public TabPaneDecorator tabPane() {
        return JavaFXToolkitService.adapters().create(TabPaneDecorator.class);
    }

    @Override
    public TabDecorator tab() {
        return JavaFXToolkitService.adapters().create(TabDecorator.class);
    }

    @Override
    public GridPaneDecorator gridPane() {
        return JavaFXToolkitService.adapters().create(GridPaneDecorator.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <S> TableViewDecorator<S> tableView() {
        return JavaFXToolkitService.adapters().create(TableViewDecorator.class);
    }

    @Override
    public StatusBarDecorator statusBar() {
        return JavaFXToolkitService.adapters().create(StatusBarDecorator.class);
    }

    @Override
    public MaskerPaneDecorator maskerPane() {
        return JavaFXToolkitService.adapters().create(MaskerPaneDecorator.class);
    }

    @Override
    public ButtonDecorator button() {
        return JavaFXToolkitService.adapters().create(ButtonDecorator.class);
    }

    @Override
    public ContextMenuDecorator contextMenu() {
        return JavaFXToolkitService.adapters().create(ContextMenuDecorator.class);
    }

    @Override
    public TextDecorator text() {
        return JavaFXToolkitService.adapters().create(TextDecorator.class);
    }

    @Override
    public TextFieldDecorator textField() {
        return JavaFXToolkitService.adapters().create(TextFieldDecorator.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> ListViewDecorator<T> listView() {
        return JavaFXToolkitService.adapters().create(ListViewDecorator.class);
    }
}
