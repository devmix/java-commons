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

package com.github.devmix.commons.javafx.samples;

import com.github.devmix.commons.adapters.api.AdaptersContext;
import com.github.devmix.commons.adapters.api.exceptions.AdapterGenerationException;
import com.github.devmix.commons.adapters.core.contexts.AdaptersContextBuilders;
import com.github.devmix.commons.javafx.api.views.View;
import com.github.devmix.commons.javafx.api.views.ViewsFactory;
import com.github.devmix.commons.javafx.core.AbstractToolkitService;
import com.github.devmix.commons.javafx.core.JavaFXToolkitService;
import com.github.devmix.commons.javafx.core.i18n.DefaultI18n;
import com.github.devmix.commons.javafx.core.utils.DefaultUtils;

import java.util.Objects;

import static com.github.devmix.commons.javafx.api.toolkit.Attributes.I18N_FACTORY;
import static com.github.devmix.commons.javafx.api.toolkit.Attributes.UTILS_FACTORY;
import static com.github.devmix.commons.javafx.api.toolkit.Attributes.VIEW_FACTORY;

/**
 * @author Sergey Grachev
 */
final class MyToolkitService extends AbstractToolkitService<View, DefaultUtils, DefaultI18n> {

    private final JavaFXToolkitService parent;
    private final AdaptersContext adaptersContext;

    public MyToolkitService() throws AdapterGenerationException {
        parent = new JavaFXToolkitService();
        adaptersContext = AdaptersContextBuilders.standard()
                .addPackage(getClass().getPackage().getName())
                .build();

        final ViewsFactory viewsFactory = Objects.requireNonNull(parent.get(VIEW_FACTORY));

        this.i18nFactory = parent.get(I18N_FACTORY);
        this.viewsFactory = toolkitService -> adaptersContext.create(MyViewImpl.class, viewsFactory.create(toolkitService));
        this.utilsFactory = parent.get(UTILS_FACTORY);
    }

    @Override
    public String id() {
        return parent.id() + "-my";
    }
}
