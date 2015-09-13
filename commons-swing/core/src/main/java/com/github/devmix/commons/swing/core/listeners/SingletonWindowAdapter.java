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

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.function.Consumer;

import static com.github.devmix.commons.swing.core.utils.EventsUtils.fire;

/**
 * @author Sergey Grachev
 */
public class SingletonWindowAdapter implements WindowListener {

    private Consumer<WindowEvent> windowOpened;
    private Consumer<WindowEvent> windowClosing;
    private Consumer<WindowEvent> windowClosed;
    private Consumer<WindowEvent> windowIconified;
    private Consumer<WindowEvent> windowDeiconified;
    private Consumer<WindowEvent> windowActivated;
    private Consumer<WindowEvent> windowDeactivated;

    @Override
    public void windowOpened(final WindowEvent e) {
        fire(e, windowOpened);
    }

    @Override
    public void windowClosing(final WindowEvent e) {
        fire(e, windowClosing);
    }

    @Override
    public void windowClosed(final WindowEvent e) {
        fire(e, windowClosed);
    }

    @Override
    public void windowIconified(final WindowEvent e) {
        fire(e, windowIconified);
    }

    @Override
    public void windowDeiconified(final WindowEvent e) {
        fire(e, windowDeiconified);
    }

    @Override
    public void windowActivated(final WindowEvent e) {
        fire(e, windowActivated);
    }

    @Override
    public void windowDeactivated(final WindowEvent e) {
        fire(e, windowDeactivated);
    }

    public void setWindowOpened(final Consumer<WindowEvent> windowOpened) {
        this.windowOpened = windowOpened;
    }

    public void setWindowClosing(final Consumer<WindowEvent> windowClosing) {
        this.windowClosing = windowClosing;
    }

    public void setWindowClosed(final Consumer<WindowEvent> windowClosed) {
        this.windowClosed = windowClosed;
    }

    public void setWindowIconified(final Consumer<WindowEvent> windowIconified) {
        this.windowIconified = windowIconified;
    }

    public void setWindowDeiconified(final Consumer<WindowEvent> windowDeiconified) {
        this.windowDeiconified = windowDeiconified;
    }

    public void setWindowActivated(final Consumer<WindowEvent> windowActivated) {
        this.windowActivated = windowActivated;
    }

    public void setWindowDeactivated(final Consumer<WindowEvent> windowDeactivated) {
        this.windowDeactivated = windowDeactivated;
    }
}
