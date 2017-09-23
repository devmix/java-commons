package com.github.devmix.commons.properties.wrappers;

import com.github.devmix.commons.properties.Property;
import com.github.devmix.commons.properties.annotations.Group;
import com.github.devmix.commons.properties.annotations.Key;
import com.github.devmix.commons.properties.storages.annotations.Levels;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

import static com.github.devmix.commons.properties.Caches.groupOf;
import static com.github.devmix.commons.properties.Caches.groupRawOf;
import static com.github.devmix.commons.properties.Caches.keyOf;
import static com.github.devmix.commons.properties.Caches.levelOf;
import static com.github.devmix.commons.properties.Caches.restrictionsOf;
import static com.github.devmix.commons.properties.Caches.typeOf;
import static com.github.devmix.commons.properties.Caches.valueRawOf;

/**
 * @author Sergey Grachev
 */
public final class Wrappers {

    public static DefaultWrapper wrap(final Property property) {
        final Key keyW = new Key.Instance(keyOf(property));
        final Group groupW = wrapGroup(property);
        final Property.Type type = typeOf(property);
        final String value = valueRawOf(property);
        final Levels levels = levelOf(property);
        final Annotation[] restrictions = restrictionsOf(property);
        return new DefaultWrapper(type, keyW, value, groupW, levels, restrictions);
    }

    @Nullable
    private static Group wrapGroup(Property property) {
        String group = groupOf(property);
        if (group != null) {
            final Group groupA = groupRawOf(property);
            final String separator = groupA == null ? "." : groupA.separator();
            group = group.substring(0, group.length() - separator.length());
            return new Group.Instance(group, null, separator);
        }
        return null;
    }
}
