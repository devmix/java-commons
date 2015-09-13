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

import com.alee.laf.label.WebLabel;
import com.github.devmix.commons.swing.api.View;
import com.github.devmix.commons.swing.api.decorators.standard.LabelDecorator;
import com.github.devmix.commons.swing.core.i18n.LanguageManager;

/**
 * @author Sergey Grachev
 */
abstract class LabelDecoratorImpl extends WebLabel implements LabelDecorator {

    private static final long serialVersionUID = 3603037452513759327L;

    private final ViewWebLaF view;

    public LabelDecoratorImpl(final ViewWebLaF view) {
        this.view = view.register(this);
    }

    @Override
    public View view() {
        return view;
    }

    @Override
    public LabelDecorator i18n(final String i18n) {
        setText(LanguageManager.lang().of(i18n));
        return this;
    }

    @Override
    public LabelDecorator text(final String text) {
        setText(text);
        return this;
    }
}
