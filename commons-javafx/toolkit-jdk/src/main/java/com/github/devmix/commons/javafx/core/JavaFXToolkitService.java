/*
 * Commons Library
 * Copyright (c) 2015-2016 Sergey Grachev (sergey.grachev@yahoo.com). All rights reserved.
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

package com.github.devmix.commons.javafx.core;

import com.github.devmix.commons.adapters.api.AdaptersContext;
import com.github.devmix.commons.adapters.api.exceptions.AdapterGenerationException;
import com.github.devmix.commons.adapters.core.contexts.AdaptersContextBuilders;
import com.github.devmix.commons.javafx.core.components.DefaultView;
import com.github.devmix.commons.javafx.core.i18n.DefaultI18n;
import com.github.devmix.commons.javafx.core.utils.DefaultUtils;
import com.github.devmix.commons.javafx.core.utils.DefaultUtilsFactory;

/**
 * @author Sergey Grachev
 */
public final class JavaFXToolkitService extends AbstractToolkitService<DefaultView, DefaultUtils, DefaultI18n> {

    private static final String ID = "javafx";

    private static final Object ADAPTERS_LOCK = new Object();
    private static AdaptersContext adaptersContext;

    public JavaFXToolkitService() {
        this.i18nFactory = DefaultI18n::new;
        this.viewsFactory = DefaultView::new;
        this.utilsFactory = new DefaultUtilsFactory();
    }

    @Override
    public String id() {
        return ID;
    }

    public static AdaptersContext adapters() {
        if (adaptersContext == null) {
            synchronized (ADAPTERS_LOCK) {
                if (adaptersContext == null) {
                    try {
                        adaptersContext = AdaptersContextBuilders.standard()
                                .addPackage(JavaFXToolkitService.class.getPackage().getName())
                                .build();
                    } catch (final AdapterGenerationException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return adaptersContext;
    }
}
