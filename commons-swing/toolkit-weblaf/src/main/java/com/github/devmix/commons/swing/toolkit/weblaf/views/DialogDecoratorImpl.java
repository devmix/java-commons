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

import com.alee.laf.rootpane.WebDialog;
import com.github.devmix.commons.swing.api.View;
import com.github.devmix.commons.swing.api.decorators.standard.DialogDecorator;
import com.github.devmix.commons.swing.core.i18n.LanguageManager;
import com.github.devmix.commons.swing.core.listeners.SingletonWindowAdapter;

import java.awt.event.WindowEvent;
import java.util.function.Consumer;

/**
 * @author Sergey Grachev
 */
abstract class DialogDecoratorImpl extends WebDialog implements DialogDecorator {

    private static final long serialVersionUID = -2899521527067793360L;

    private final ViewWebLaF view;
    private SingletonWindowAdapter windowAdapter;

    public DialogDecoratorImpl(final ViewWebLaF view) {
        this.view = view.register(this);
    }

    @Override
    public View view() {
        return view;
    }

    @Override
    public DialogDecorator i18n(final String i18n) {
        setTitle(LanguageManager.lang().of(i18n));
        return this;
    }

    @Override
    public DialogDecorator onWindowOpened(final Consumer<WindowEvent> listener) {
        windowAdapter().setWindowOpened(listener);
        return this;
    }

    @Override
    public DialogDecorator title(final String title) {
        setTitle(title);
        return this;
    }

    private synchronized SingletonWindowAdapter windowAdapter() {
        if (windowAdapter == null) {
            windowAdapter = new SingletonWindowAdapter();
            this.addWindowListener(windowAdapter);
        }
        return windowAdapter;
    }
}
