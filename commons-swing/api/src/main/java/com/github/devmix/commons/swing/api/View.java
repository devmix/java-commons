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

package com.github.devmix.commons.swing.api;

import com.github.devmix.commons.swing.api.decorators.ComponentDecorator;
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
import com.github.devmix.commons.swing.api.views.VerifiersRegistry;

import java.awt.*;

/**
 * @author Sergey Grachev
 */
public interface View {

    //    <D extends ComponentDecorator<?>> D name(String name);
    ComponentDecorator<?> name(String name);

//    Verifier.Status verifyAll();
//
//    Verifier.Status verifyAll(String name);
//
//    Verifier.Status verifyAll(ComponentDecorator<?> decorator);
//
//    Verifier.Status verifyAll(VerifierIterator iterator);

    VerifiersRegistry verifiers();

    BreadcrumbDecorator breadcrumb();

    BreadcrumbLabelDecorator breadcrumbLabel();

    ButtonDecorator button();

    ComboBoxDecorator comboBox();

    DateFieldDecorator dateField();

    DialogDecorator dialog();

    FormattedTextFieldDecorator formattedTextField();

    LabelDecorator label();

    ListDecorator list();

    MenuDecorator menu();

    MenuBarDecorator menuBar();

    MenuItemDecorator menuItem();

    PanelDecorator panel();

    PopupMenuDecorator popupMenu();

    ScrollPaneDecorator scrollPane(Component component);

    SplitPaneDecorator splitPane();

    TabbedPaneDecorator tabbedPane();

    TableDecorator table();

    TextAreaDecorator textArea();

    TextFieldDecorator textField();
}
