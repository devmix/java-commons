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

import com.alee.laf.text.WebFormattedTextField;
import com.github.devmix.commons.swing.api.View;
import com.github.devmix.commons.swing.api.decorators.standard.FormattedTextFieldDecorator;

import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

/**
 * @author Sergey Grachev
 */
abstract class FormattedTextFieldDecoratorImpl extends WebFormattedTextField implements FormattedTextFieldDecorator {

    private static final long serialVersionUID = 3977560291779461319L;

    private final ViewWebLaF view;

    public FormattedTextFieldDecoratorImpl(final ViewWebLaF view) {
        this.view = view.register(this);
    }

    @Override
    public View view() {
        return view;
    }

    @Override
    public FormattedTextFieldDecorator asFloatNumber(final int minFractionDigits, final int maxFractionDigits) {
        final NumberFormat format = NumberFormat.getNumberInstance();
        format.setMinimumFractionDigits(minFractionDigits);
        format.setMaximumFractionDigits(maxFractionDigits);
        setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(format)));
        return this;
    }
}
