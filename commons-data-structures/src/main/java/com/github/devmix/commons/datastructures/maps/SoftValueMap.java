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

package com.github.devmix.commons.datastructures.maps;

import javax.annotation.Nullable;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Sergey Grachev
 */
@SuppressWarnings("NullableProblems")
public final class SoftValueMap<K, V> implements Map<K, V> {

    private final Map<K, WeakValue<K, V>> map;
    private final ReferenceQueue<V> queue = new ReferenceQueue<>();

    @SuppressWarnings("UnusedDeclaration")
    public SoftValueMap() {
        this(LinkedHashMap::new);
    }

    @SuppressWarnings("unchecked")
    public SoftValueMap(final Supplier<Map<K, ?>> containerFactory) {
        this.map = (Map<K, WeakValue<K, V>>) containerFactory.get();
    }

    @SuppressWarnings("unchecked")
    private void processQueue() {
        WeakValue<K, V> ref;
        while ((ref = (WeakValue<K, V>) queue.poll()) != null) {
            if (ref == map.get(ref.key)) {
                map.remove(ref.key);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        processQueue();
        final Set<Entry<K, V>> result = new LinkedHashSet<>();
        //noinspection Convert2streamapi
        for (final Entry<K, WeakValue<K, V>> entry : map.entrySet()) {
            result.add(new AbstractMap.SimpleEntry<>(entry.getKey(), WeakValue.unwrap(entry.getValue())));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        processQueue();
        return map.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<K> keySet() {
        processQueue();
        return map.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<V> values() {
        processQueue();
        final Collection<V> values = new LinkedHashSet<>();
        for (final WeakValue<K, V> value : map.values()) {
            values.add(WeakValue.unwrap(value));
        }
        return values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        processQueue();
        return map.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(final Object key) {
        processQueue();
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        processQueue();
        for (final Entry<K, WeakValue<K, V>> entry : map.entrySet()) {
            if (value == WeakValue.unwrap(entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V get(final Object key) {
        processQueue();
        return WeakValue.unwrap(map.get(key));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V put(final K key, final V value) {
        processQueue();
        return WeakValue.unwrap(map.put(key, new WeakValue<>(key, value, queue)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V remove(final Object key) {
        processQueue();
        return WeakValue.unwrap(map.remove(key));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        for (final Entry<? extends K, ? extends V> e : m.entrySet()) {
            map.put(e.getKey(), WeakValue.wrap(e, queue));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        processQueue();
        map.clear();
    }

    private static final class WeakValue<K, V> extends SoftReference<V> {
        private final K key;

        private WeakValue(final K key, final V value, final ReferenceQueue<V> queue) {
            super(value, queue);
            this.key = key;
        }

        @Nullable
        public static <K, V> V unwrap(final WeakValue<K, V> value) {
            return value == null ? null : value.get();
        }

        public static <K, V> WeakValue<K, V> wrap(final Entry<? extends K, ? extends V> e, final ReferenceQueue<V> queue) {
            return new WeakValue<>(e.getKey(), e.getValue(), queue);
        }
    }
}
