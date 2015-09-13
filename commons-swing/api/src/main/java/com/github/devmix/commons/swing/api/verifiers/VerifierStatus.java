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

package com.github.devmix.commons.swing.api.verifiers;

/**
 * @author Sergey Grachev
 */
public enum VerifierStatus {

    VALID(0),
    NOTIFICATION(1),
    WARNING(2),
    ERROR(3);

    private final int severity;

    VerifierStatus(final int severity) {
        this.severity = severity;
    }

    public static VerifierStatus max(final VerifierStatus v1, final VerifierStatus v2) {
        if (v1 == null) {
            return v2;
        }
        return v2 == null || v1.severity == v2.severity ? v1 : (v1.severity > v2.severity ? v1 : v2);
    }
}
