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

import com.alee.laf.text.WebTextField;
import com.github.devmix.commons.swing.api.View;
import com.github.devmix.commons.swing.api.decorators.standard.TextFieldDecorator;
import com.github.devmix.commons.swing.api.verifiers.ComponentDecoratorVerifier;
import com.github.devmix.commons.swing.api.verifiers.ComponentVerifier;
import com.github.devmix.commons.swing.core.listeners.SingletonDocumentListener;
import com.github.devmix.commons.swing.core.listeners.SingletonFocusListener;

import javax.swing.event.DocumentEvent;
import java.awt.event.FocusEvent;

/**
 * @author Sergey Grachev
 */
abstract class TextFieldDecoratorImpl extends WebTextField implements TextFieldDecorator {

    private static final long serialVersionUID = 4746041816977645466L;

    private final ViewWebLaF view;
    private ComponentVerifier verifier;
    private SingletonFocusListener focusListener;
    private SingletonDocumentListener documentListener;

    public TextFieldDecoratorImpl(final ViewWebLaF view) {
        this.view = view.register(this);
    }

    @Override
    public View view() {
        return view;
    }

    @Override
    public TextFieldDecorator verifier(final ComponentDecoratorVerifier verifier) {
        focusListener();
        documentListener();
        this.verifier = verifier;
        view.verifiers().put(this, verifier);
        return this;
    }

    private synchronized SingletonFocusListener focusListener() {
        if (focusListener == null) {
            focusListener = new SingletonFocusListener() {
                @Override
                public void focusLost(final FocusEvent e) {
                    if (verifier != null) {
                        verifier.verify(TextFieldDecoratorImpl.this);
                    }
                    super.focusLost(e);
                }
            };
            this.addFocusListener(focusListener);
        }
        return focusListener;
    }

    private synchronized SingletonDocumentListener documentListener() {
        if (documentListener == null) {
            documentListener = new SingletonDocumentListener() {
                @Override
                public void insertUpdate(final DocumentEvent e) {
                    super.insertUpdate(e);
                    verify();
                }

                @Override
                public void removeUpdate(final DocumentEvent e) {
                    super.removeUpdate(e);
                    verify();
                }

                @Override
                public void changedUpdate(final DocumentEvent e) {
                    super.changedUpdate(e);
                    verify();
                }

                private void verify() {
                    if (verifier != null) {
                        verifier.verify(TextFieldDecoratorImpl.this);
                    }
                }
            };
            this.getDocument().addDocumentListener(documentListener);
        }
        return documentListener;
    }
}
