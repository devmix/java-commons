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

package com.github.devmix.commons.swing.core.listeners;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.function.Consumer;

import static com.github.devmix.commons.swing.core.utils.EventsUtils.fire;

/**
 * @author Sergey Grachev
 */
public class SingletonDocumentListener implements DocumentListener {

    private Consumer<DocumentEvent> insertUpdate;
    private Consumer<DocumentEvent> removeUpdate;
    private Consumer<DocumentEvent> changedUpdate;

    @Override
    public void insertUpdate(final DocumentEvent e) {
        fire(e, insertUpdate);
    }

    @Override
    public void removeUpdate(final DocumentEvent e) {
        fire(e, removeUpdate);
    }

    @Override
    public void changedUpdate(final DocumentEvent e) {
        fire(e, changedUpdate);
    }

    public void setInsertUpdate(final Consumer<DocumentEvent> insertUpdate) {
        this.insertUpdate = insertUpdate;
    }

    public void setRemoveUpdate(final Consumer<DocumentEvent> removeUpdate) {
        this.removeUpdate = removeUpdate;
    }

    public void setChangedUpdate(final Consumer<DocumentEvent> changedUpdate) {
        this.changedUpdate = changedUpdate;
    }
}
