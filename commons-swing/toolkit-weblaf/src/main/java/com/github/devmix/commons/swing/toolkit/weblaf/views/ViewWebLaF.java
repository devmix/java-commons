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

package com.github.devmix.commons.swing.toolkit.weblaf.views;

import com.github.devmix.commons.swing.api.View;
import com.github.devmix.commons.swing.api.decorators.ComponentDecorator;
import com.github.devmix.commons.swing.api.decorators.ComponentViewDecorator;
import com.github.devmix.commons.swing.api.decorators.extended.BreadcrumbDecorator;
import com.github.devmix.commons.swing.api.decorators.extended.BreadcrumbLabelDecorator;
import com.github.devmix.commons.swing.api.decorators.extended.DateFieldDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.ButtonDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.ComboBoxDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.DialogDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.FormattedTextFieldDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.LabelDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.ListDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.MenuBarDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.MenuDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.MenuItemDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.PanelDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.PopupMenuDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.ScrollPaneDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.SplitPaneDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.TabbedPaneDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.TableDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.TextAreaDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.TextFieldDecorator;
import com.github.devmix.commons.swing.api.views.ComponentsRegistry;
import com.github.devmix.commons.swing.api.views.VerifiersRegistry;
import com.github.devmix.commons.swing.core.decorators.DefaultDecoratorsRegistry;
import com.github.devmix.commons.swing.core.decorators.mixins.ComponentMixin;
import com.github.devmix.commons.swing.core.decorators.mixins.ContainerMixin;
import com.github.devmix.commons.swing.core.views.DefaultComponentsRegistry;
import com.github.devmix.commons.swing.core.views.DefaultVerifiersRegistry;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author Sergey Grachev
 */
public final class ViewWebLaF implements View {

    private static final Class[] CONSTRUCTOR_ARGS_SCROLL_PANE = new Class[]{ViewWebLaF.class, Component.class};

    private static final DefaultDecoratorsRegistry D = new DefaultDecoratorsRegistry();
    private static final ComponentMixin COMPONENT = new ComponentMixin();
    private static final ContainerMixin CONTAINER = new ContainerMixin();

    static {
        D.register(BreadcrumbDecorator.class, BreadcrumbDecoratorImpl.class, JPanel.class, COMPONENT, CONTAINER);
        D.register(BreadcrumbLabelDecorator.class, BreadcrumbLabelDecoratorImpl.class, JLabel.class, COMPONENT);
        D.register(ButtonDecorator.class, ButtonDecoratorImpl.class, JButton.class, COMPONENT);
        D.register(ComboBoxDecorator.class, ComboBoxDecoratorImpl.class, JComboBox.class, COMPONENT);
        D.register(DateFieldDecorator.class, DateFieldDecoratorImpl.class, JFormattedTextField.class, COMPONENT);
        D.register(DialogDecorator.class, DialogDecoratorImpl.class, JDialog.class, COMPONENT, CONTAINER);
        D.register(FormattedTextFieldDecorator.class, FormattedTextFieldDecoratorImpl.class, JFormattedTextField.class, COMPONENT);
        D.register(LabelDecorator.class, LabelDecoratorImpl.class, JLabel.class, COMPONENT);
        D.register(ListDecorator.class, ListDecoratorImpl.class, JList.class, COMPONENT);
        D.register(MenuItemDecorator.class, MenuItemDecoratorImpl.class, JMenuItem.class, COMPONENT);
        D.register(PanelDecorator.class, PanelDecoratorImpl.class, JPanel.class, COMPONENT, CONTAINER);
        D.register(PopupMenuDecorator.class, PopupMenuDecoratorImpl.class, JPopupMenu.class, COMPONENT);
        D.register(ScrollPaneDecorator.class, ScrollPaneDecoratorImpl.class, JScrollPane.class, COMPONENT);
        D.register(SplitPaneDecorator.class, SplitPaneDecoratorImpl.class, JSplitPane.class, COMPONENT);
        D.register(TabbedPaneDecorator.class, TabbedPaneDecoratorImpl.class, JTabbedPane.class, COMPONENT, CONTAINER);
        D.register(TableDecorator.class, TableDecoratorImpl.class, JTable.class, COMPONENT);
        D.register(TextAreaDecorator.class, TextAreaDecoratorImpl.class, JTextArea.class, COMPONENT);
        D.register(TextFieldDecorator.class, TextFieldDecoratorImpl.class, JTextField.class, COMPONENT);
        D.register(MenuBarDecorator.class, MenuBarDecoratorImpl.class, JMenuBar.class, COMPONENT);
        D.register(MenuDecorator.class, MenuDecoratorImpl.class, JMenu.class, COMPONENT);
    }

    private final ComponentsRegistry<ComponentViewDecorator<?>> components = new DefaultComponentsRegistry<>();
    private final VerifiersRegistry verifiers = new DefaultVerifiersRegistry();

    @Override
    public ComponentDecorator<?> find(final String name) {
        return Objects.requireNonNull(components.find(name));
    }

    @Override
    public VerifiersRegistry verifiers() {
        return verifiers;
    }

    @Override
    public BreadcrumbDecorator breadcrumb() {
        return D.create(BreadcrumbDecoratorImpl.class, this);
    }

    @Override
    public BreadcrumbLabelDecorator breadcrumbLabel() {
        return D.create(BreadcrumbLabelDecoratorImpl.class, this);
    }

    @Override
    public ButtonDecorator button() {
        return D.create(ButtonDecoratorImpl.class, this);
    }

    @Override
    public ComboBoxDecorator comboBox() {
        return D.create(ComboBoxDecoratorImpl.class, this);
    }

    @Override
    public DateFieldDecorator dateField() {
        return D.create(DateFieldDecoratorImpl.class, this);
    }

    @Override
    public DialogDecorator dialog() {
        return D.create(DialogDecoratorImpl.class, this);
    }

    @Override
    public FormattedTextFieldDecorator formattedTextField() {
        return D.create(FormattedTextFieldDecoratorImpl.class, this);
    }

    @Override
    public LabelDecorator label() {
        return D.create(LabelDecoratorImpl.class, this);
    }

    @Override
    public ListDecorator list() {
        return D.create(ListDecoratorImpl.class, this);
    }

    @Override
    public MenuItemDecorator menuItem() {
        return D.create(MenuItemDecoratorImpl.class, this);
    }

    @Override
    public PanelDecorator panel() {
        return D.create(PanelDecoratorImpl.class, this);
    }

    @Override
    public PopupMenuDecorator popupMenu() {
        return D.create(PopupMenuDecoratorImpl.class, this);
    }

    @Override
    public ScrollPaneDecorator scrollPane(final Component component) {
        return D.create(ScrollPaneDecoratorImpl.class,
                new Object[]{this, component}, CONSTRUCTOR_ARGS_SCROLL_PANE);
    }

    @Override
    public SplitPaneDecorator splitPane() {
        return D.create(SplitPaneDecoratorImpl.class, this);
    }

    @Override
    public TabbedPaneDecorator tabbedPane() {
        return D.create(TabbedPaneDecoratorImpl.class, this);
    }

    @Override
    public TableDecorator table() {
        return D.create(TableDecoratorImpl.class, this);
    }

    @Override
    public TextAreaDecorator textArea() {
        return D.create(TextAreaDecoratorImpl.class, this);
    }

    @Override
    public TextFieldDecorator textField() {
        return D.create(TextFieldDecoratorImpl.class, this);
    }

    @Override
    public MenuBarDecorator menuBar() {
        return D.create(MenuBarDecoratorImpl.class, this);
    }

    @Override
    public MenuDecorator menu() {
        return D.create(MenuDecoratorImpl.class, this);
    }

    public ViewWebLaF register(final ComponentViewDecorator<?> decorator) {
        components.register(decorator);
        return this;
    }
}
