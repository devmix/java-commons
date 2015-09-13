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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.function.Consumer;

import static com.github.devmix.commons.swing.core.utils.EventsUtils.fire;

/**
 * @author Sergey Grachev
 */
public class SingletonFocusListener implements FocusListener {

    private Consumer<FocusEvent> focusGained;
    private Consumer<FocusEvent> focusLost;

    @Override
    public void focusGained(final FocusEvent e) {
        fire(e, focusGained);
    }

    @Override
    public void focusLost(final FocusEvent e) {
        fire(e, focusLost);
    }

    public void setFocusGained(final Consumer<FocusEvent> focusGained) {
        this.focusGained = focusGained;
    }

    public void setFocusLost(final Consumer<FocusEvent> focusLost) {
        this.focusLost = focusLost;
    }
}
