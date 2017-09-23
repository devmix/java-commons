/*
 * Commons Library
 * Copyright (c) 2013-2016 Sergey Grachev (sergey.grachev@yahoo.com). All rights reserved.
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

package com.github.devmix.commons.properties;

import com.github.devmix.commons.properties.annotations.Group;
import com.github.devmix.commons.properties.annotations.Key;
import com.github.devmix.commons.properties.annotations.Value;
import com.github.devmix.commons.properties.exceptions.PropertyKeyException;
import com.github.devmix.commons.properties.identity.Identity;
import com.github.devmix.commons.properties.restrictions.Pattern;
import com.github.devmix.commons.properties.storages.annotations.Levels;
import com.github.devmix.commons.properties.utils.PropertiesUtils;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.WeakHashMap;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Sergey Grachev
 */
public final class Caches {

    private static final Levels DEFAULT_LEVEL = new Levels.Instance(0, 0xFF);
    private static final Property.Type DEFAULT_TYPE = Property.Type.STRING;
    private static final Annotation[] DEFAULT_RESTRICTIONS = new Annotation[0];

    private static final WeakHashMap<Property, Levels> CACHE_LEVELS = new WeakHashMap<>();
    private static final WeakHashMap<Property, Annotation[]> CACHE_RESTRICTIONS = new WeakHashMap<>();
    private static final WeakHashMap<Property, String> CACHE_GROUPS = new WeakHashMap<>();
    private static final WeakHashMap<Property, String> CACHE_KEYS = new WeakHashMap<>();
    private static final WeakHashMap<Property, String> CACHE_IDS = new WeakHashMap<>();
    private static final WeakHashMap<Property, Object> CACHE_VALUE = new WeakHashMap<>();
    private static final WeakHashMap<Property, Property.Type> CACHE_TYPE = new WeakHashMap<>();
    private static final WeakHashMap<Property, Property[]> CACHE_DEPENDENCIES = new WeakHashMap<>();
    private static final WeakHashMap<Annotation, java.util.regex.Pattern> CACHE_PATTERNS = new WeakHashMap<>();

    private Caches() {
    }

    public static java.util.regex.Pattern patternOf(final Pattern.String restriction) {
        final java.util.regex.Pattern pattern;
        synchronized (CACHE_PATTERNS) {
            if (CACHE_PATTERNS.containsKey(restriction)) {
                pattern = CACHE_PATTERNS.get(restriction);
            } else {
                pattern = java.util.regex.Pattern.compile(restriction.value());
                CACHE_PATTERNS.put(restriction, pattern);
            }
        }
        return pattern;
    }

    @Nullable
    public static Group groupRawOf(Property property) {
        return property instanceof Property.Wrapper
                ? ((Property.Wrapper) property).group() : property.getClass().getAnnotation(Group.class);
    }

    @Nullable
    public static String groupOf(final Property property) {
        synchronized (CACHE_GROUPS) {
            if (CACHE_GROUPS.containsKey(property)) {
                return CACHE_GROUPS.get(property);
            }
        }

        Group group = groupRawOf(property);

        Class<?> propertyClass = property.getClass();
        final StringBuilder sb = new StringBuilder();
        while (group != null) {
            sb.insert(0, group.separator()).insert(0, isBlank(group.value())
                    ? propertyClass.getSimpleName()
                    : group.value());

            if (isNotBlank(group.subGroup())) {
                sb.append(group.subGroup()).append(group.separator());
            }

            propertyClass = propertyClass.getEnclosingClass();
            group = propertyClass == null ? null : propertyClass.getAnnotation(Group.class);
        }

        final String result = sb.length() > 0 ? sb.toString() : null;

        synchronized (CACHE_GROUPS) {
            if (!CACHE_GROUPS.containsKey(property)) {
                CACHE_GROUPS.put(property, result);
            }
        }

        return result;
    }

    @Nullable
    public static Key keyRawOf(final Property property) {
        return property instanceof Property.Wrapper
                ? ((Property.Wrapper) property).key() : PropertiesUtils.findAnnotation(property, Key.class);
    }

    public static String keyOf(final Property property) {
        synchronized (CACHE_KEYS) {
            if (CACHE_KEYS.containsKey(property)) {
                return CACHE_KEYS.get(property);
            }
        }

        final Key key = keyRawOf(property);

        final String result;
        if (key != null) {
            String keyName = key.value();
            if (isBlank(keyName)) {
                keyName = PropertiesUtils.providerOf(key).keyOf(property);
            }
            result = keyName;
        } else {
            result = Identity.standard().keyOf(property);
        }

        if (isBlank(result)) {
            throw new PropertyKeyException(key, "Key provided for property is empty");
        }

        synchronized (CACHE_KEYS) {
            if (!CACHE_KEYS.containsKey(property)) {
                CACHE_KEYS.put(property, result);
            }
        }

        return result;
    }

    public static String idOf(final Property property) {
        synchronized (CACHE_IDS) {
            if (CACHE_IDS.containsKey(property)) {
                return CACHE_IDS.get(property);
            }
        }

        final String key = keyOf(property);
        final String group = groupOf(property);

        final StringBuilder sb = new StringBuilder();
        if (isNotBlank(group)) {
            sb.append(group);
        }

        final String result = sb.append(key).toString();

        synchronized (CACHE_IDS) {
            if (!CACHE_IDS.containsKey(property)) {
                CACHE_IDS.put(property, result);
            }
        }

        return result;
    }

    @Nullable
    public static String valueRawOf(final Property property) {
        if (property instanceof Property.Wrapper) {
            return ((Property.Wrapper) property).value();
        } else {
            final Value value = PropertiesUtils.findAnnotation(property, Value.class);
            return value == null || isBlank(value.value()) ? null : value.value();
        }
    }

    public static Object valueOf(final Property.Converter converter, final Property property) {
        synchronized (CACHE_VALUE) {
            if (CACHE_VALUE.containsKey(property)) {
                return CACHE_VALUE.get(property);
            }
        }

        final String value = valueRawOf(property);

        final Object result = value == null ? null : converter.asOf(Caches.typeOf(property), value);
        synchronized (CACHE_VALUE) {
            if (!CACHE_VALUE.containsKey(property)) {
                CACHE_VALUE.put(property, result);
            }
        }

        return result;
    }

    public static Levels levelOf(final Property property) {
        synchronized (CACHE_LEVELS) {
            if (CACHE_LEVELS.containsKey(property)) {
                return CACHE_LEVELS.get(property);
            }
        }

        Levels level = property instanceof Property.Wrapper
                ? ((Property.Wrapper) property).levels() : PropertiesUtils.findAnnotation(property, Levels.class);

        level = level == null ? DEFAULT_LEVEL : level;

        synchronized (CACHE_LEVELS) {
            if (!CACHE_LEVELS.containsKey(property)) {
                CACHE_LEVELS.put(property, level);
            }
        }

        return level;
    }

    public static Property.Type typeOf(final Property property) {
        synchronized (CACHE_TYPE) {
            if (CACHE_TYPE.containsKey(property)) {
                return CACHE_TYPE.get(property);
            }
        }

        Property.Type type;
        if (property instanceof Property.Wrapper) {
            type = ((Property.Wrapper) property).type();
        } else {
            final Value value = PropertiesUtils.findAnnotation(property, Value.class);
            type = value == null ? null : value.type();
        }

        type = type == null ? DEFAULT_TYPE : type;

        synchronized (CACHE_TYPE) {
            if (!CACHE_TYPE.containsKey(property)) {
                CACHE_TYPE.put(property, type);
            }
        }

        return type;
    }

    public static Annotation[] restrictionsOf(final Property property) {
        synchronized (CACHE_RESTRICTIONS) {
            if (CACHE_RESTRICTIONS.containsKey(property)) {
                return CACHE_RESTRICTIONS.get(property);
            }
        }

        Annotation[] restrictions = null;
        if (property instanceof Property.Wrapper) {
            restrictions = ((Property.Wrapper) property).restrictions();
        } else {
            final Class clazz = property.getClass();
            if (clazz.isEnum()) {
                try {
                    final String name = ((Enum) property).name();
                    restrictions = PropertiesUtils.filterRestrictions(clazz.getField(name).getAnnotations());
                } catch (final NoSuchFieldException ignore) {
                }
            } else {
                restrictions = PropertiesUtils.filterRestrictions(clazz.getAnnotations());
            }
        }

        restrictions = restrictions == null ? DEFAULT_RESTRICTIONS : restrictions;

        synchronized (CACHE_RESTRICTIONS) {
            if (!CACHE_RESTRICTIONS.containsKey(property)) {
                CACHE_RESTRICTIONS.put(property, restrictions);
            }
        }

        return restrictions;
    }

    public static Property[] dependenciesOf(final Property property) {
        throw new UnsupportedOperationException();
    /*
        // TODO
        synchronized (CACHE_DEPENDENCIES) {
            if (CACHE_DEPENDENCIES.containsKey(property)) {
                return CACHE_DEPENDENCIES.get(property);
            }
        }

        final DependOn dependOn;
        if (property instanceof Wrapper) {
            dependOn = null;
        } else {
            dependOn = PropertiesUtils.findAnnotation(property, DependOn.class);
        }

        final Property[] dependencies;
        if (dependOn != null && dependOn.value().length > 0) {
            dependencies = PropertiesUtils.resolveDependencies(property, dependOn);
        } else {
            dependencies = null;
        }

        synchronized (CACHE_DEPENDENCIES) {
            if (!CACHE_DEPENDENCIES.containsKey(property)) {
                CACHE_DEPENDENCIES.put(property, dependencies);
            }
        }

        return dependencies;
    */
    }

    private static Group findGroupAnnotation(final Property property) {
        return property instanceof Property.Wrapper
                ? ((Property.Wrapper) property).group() : property.getClass().getAnnotation(Group.class);
    }
}
