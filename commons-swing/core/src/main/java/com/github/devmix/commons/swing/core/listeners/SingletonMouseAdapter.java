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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

import static com.github.devmix.commons.swing.core.utils.EventsUtils.fire;

/**
 * @author Sergey Grachev
 */
public class SingletonMouseAdapter implements MouseListener {

    private Consumer<MouseEvent> mouseReleased;
    private Consumer<MouseEvent> mousePressed;
    private Consumer<MouseEvent> mouseClicked;
    private Consumer<MouseEvent> mouseEntered;
    private Consumer<MouseEvent> mouseExited;

    @Override
    public void mouseClicked(final MouseEvent e) {
        fire(e, mouseClicked);
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        fire(e, mousePressed);
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        fire(e, mouseReleased);
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        fire(e, mouseEntered);
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        fire(e, mouseExited);
    }

    public void setMouseReleased(final Consumer<MouseEvent> mouseReleased) {
        this.mouseReleased = mouseReleased;
    }

    public void setMouseClicked(final Consumer<MouseEvent> mouseClicked) {
        this.mouseClicked = mouseClicked;
    }

    public void setMousePressed(final Consumer<MouseEvent> mousePressed) {
        this.mousePressed = mousePressed;
    }

    public void setMouseEntered(final Consumer<MouseEvent> mouseEntered) {
        this.mouseEntered = mouseEntered;
    }

    public void setMouseExited(final Consumer<MouseEvent> mouseExited) {
        this.mouseExited = mouseExited;
    }
}
