package com.github.devmix.commons.properties.wrappers;

import com.github.devmix.commons.properties.Property;
import com.github.devmix.commons.properties.annotations.Group;
import com.github.devmix.commons.properties.annotations.Key;
import com.github.devmix.commons.properties.storages.annotations.Levels;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

/**
 * @author Sergey Grachev
 */
final class DefaultWrapper implements Property.Wrapper {

    private final Type type;
    private final Key key;
    private final Group group;
    private final Levels levels;
    private final Annotation[] restrictions;
    private final String value;

    public DefaultWrapper(final Type type, final Key key, final String value, final Group group, final Levels levels,
                          final Annotation[] restrictions) {

        this.type = type;
        this.key = key;
        this.group = group;
        this.levels = levels;
        this.restrictions = restrictions;
        this.value = value;
    }

    @Override
    public Type type() {
        return type;
    }

    @Nullable
    @Override
    public Key key() {
        return key;
    }

    @Nullable
    @Override
    public Group group() {
        return group;
    }

    @Nullable
    @Override
    public String value() {
        return value;
    }

    @Nullable
    @Override
    public Levels levels() {
        return levels;
    }

    @Nullable
    @Override
    public Annotation[] restrictions() {
        return restrictions;
    }
}
