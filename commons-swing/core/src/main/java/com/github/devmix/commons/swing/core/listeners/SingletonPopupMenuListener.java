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

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.util.function.Consumer;

import static com.github.devmix.commons.swing.core.utils.EventsUtils.fire;

/**
 * @author Sergey Grachev
 */
public class SingletonPopupMenuListener implements PopupMenuListener {

    private Consumer<PopupMenuEvent> popupMenuWillBecomeVisible;
    private Consumer<PopupMenuEvent> popupMenuWillBecomeInvisible;
    private Consumer<PopupMenuEvent> popupMenuCanceled;

    @Override
    public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
        fire(e, popupMenuWillBecomeVisible);
    }

    @Override
    public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
        fire(e, popupMenuWillBecomeInvisible);
    }

    @Override
    public void popupMenuCanceled(final PopupMenuEvent e) {
        fire(e, popupMenuCanceled);
    }

    public void setPopupMenuWillBecomeVisible(final Consumer<PopupMenuEvent> listener) {
        this.popupMenuWillBecomeVisible = listener;
    }

    public void setPopupMenuWillBecomeInvisible(final Consumer<PopupMenuEvent> listener) {
        this.popupMenuWillBecomeInvisible = listener;
    }

    public void setPopupMenuCanceled(final Consumer<PopupMenuEvent> listener) {
        this.popupMenuCanceled = listener;
    }
}
