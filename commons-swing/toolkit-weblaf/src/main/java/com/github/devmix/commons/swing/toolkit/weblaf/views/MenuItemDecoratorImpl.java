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

import com.alee.laf.menu.WebMenuItem;
import com.github.devmix.commons.swing.api.View;
import com.github.devmix.commons.swing.api.decorators.standard.MenuItemDecorator;
import com.github.devmix.commons.swing.core.i18n.LanguageManager;
import com.github.devmix.commons.swing.core.listeners.SingletonActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @author Sergey Grachev
 */
abstract class MenuItemDecoratorImpl extends WebMenuItem implements MenuItemDecorator {

    private static final long serialVersionUID = 4592904379979098941L;

    private final ViewWebLaF view;
    private SingletonActionListener actionListener;

    public MenuItemDecoratorImpl(final ViewWebLaF view) {
        this.view = view.register(this);
    }

    @Override
    public View view() {
        return view;
    }

    @Override
    public MenuItemDecorator i18n(final String i18n) {
        System.out.println(getParent());
        setText(LanguageManager.lang().of(i18n));
        return this;
    }

    @Override
    public MenuItemDecorator onAction(final ActionListener listener) {
        singletonActionListener().setListener(listener);
        return this;
    }

    @Override
    public MenuItemDecorator accelerator(final boolean ctrl, final boolean alt, final boolean shift, final int keyCode) {
        setAccelerator(KeyStroke.getKeyStroke(keyCode,
                (ctrl ? Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() : 0)
                        | (alt ? Event.ALT_MASK : 0)
                        | (shift ? Event.SHIFT_MASK : 0)));
        return this;
    }

    private synchronized SingletonActionListener singletonActionListener() {
        if (actionListener == null) {
            actionListener = new SingletonActionListener();
            this.addActionListener(actionListener);
        }
        return actionListener;
    }
}
