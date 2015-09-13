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

package com.github.devmix.commons.i18n.core.values.reference;

import com.github.devmix.commons.i18n.core.utils.Tokenizer;
import com.github.devmix.commons.i18n.core.values.ReferenceValue;
import com.github.devmix.commons.i18n.core.values.Value;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.devmix.commons.i18n.core.utils.Tokenizer.Token.BOOLEAN;
import static com.github.devmix.commons.i18n.core.utils.Tokenizer.Token.COLON;
import static com.github.devmix.commons.i18n.core.utils.Tokenizer.Token.COMMA;
import static com.github.devmix.commons.i18n.core.utils.Tokenizer.Token.ID;
import static com.github.devmix.commons.i18n.core.utils.Tokenizer.Token.INTEGER;
import static com.github.devmix.commons.i18n.core.utils.Tokenizer.Token.PARAMETERS_BEGIN;
import static com.github.devmix.commons.i18n.core.utils.Tokenizer.Token.PARAMETERS_END;
import static com.github.devmix.commons.i18n.core.utils.Tokenizer.Token.REFERENCE_END;

/**
 * @author Sergey Grachev
 */
public final class ReferenceParser {

    // (?:\$\{(?!\()\s*(.*?)\s*}\s*)
    // .*\$\{(?!\().*?}.*
    private static final Pattern PATTERN = Pattern.compile(".*\\$\\{(?!\\().*?\\}.*", Pattern.DOTALL);

    private ReferenceParser() {
    }

    @Nullable
    public static ReferenceValue tryParse(final String value) {
        final Matcher matcher = PATTERN.matcher(value);
        if (matcher.matches()) {
            return new DefaultReferenceValue(value);
        }
        return null;
    }

    public static Operation[] parse(final String text) {
        final Tokenizer t = new Tokenizer(text);
        final List<Operation> list = new LinkedList<>();
        while (t.hasNext()) {
            switch (t.getToken()) {
                case CODE_BEGIN:
                    t.next();
                    break;

                case TEXT:
                    list.add(new Operation(OperationCode.TEXT, t.tokenAsString()));
                    t.next();
                    break;

                case REFERENCE_START:
                    t.next();
                    list.add(parseReference(t));
                    break;

                default:
                    // TODO
                    throw new IllegalArgumentException(t.getToken().name());
            }
        }
        return list.toArray(new Operation[list.size()]);
    }

    private static Operation parseReference(final Tokenizer t) {
        t.check(ID);
        final String referencePath = t.tokenAsString();
        t.next();

        Value.Arguments parameters = ArgumentsFactory.none();
        if (t.is(PARAMETERS_BEGIN)) {
            t.next();
            parameters = parseParameters(t);
        }

        t.nextIf(REFERENCE_END);

        return new Operation(OperationCode.REFERENCE, new Object[]{referencePath, parameters});
    }

    private static Value.Arguments parseParameters(final Tokenizer t) {
        String id;
        boolean plural = false;
        boolean shortName = false;
        boolean numericExists = false;
        int numeric = 0;
        boolean capitalize = false;
        boolean hasParameters = false;

        while (t.hasNext()) {
            if (t.is(PARAMETERS_END)) {
                break;
            } else if (t.is(ID)) {
                id = t.tokenAsString();
                t.next(COLON);
                t.next();
                if ("p".equals(id) || "plural".equals(id)) {
                    plural = parsePlural(t);
                } else if ("s".equals(id) || "short".equals(id)) {
                    shortName = parseShortName(t);
                } else if ("n".equals(id) || "numeric".equals(id)) {
                    numericExists = true;
                    numeric = parseNumeric(t);
                } else if ("c".equals(id) || "case".equals(id)) {
                    capitalize = parseCapitalize(t);
                } else {
                    // TODO
                    throw new IllegalArgumentException(id);
                }
                if (t.is(COMMA)) {
                    t.next();
                }
                hasParameters = true;
            } else {
                // TODO
                throw new IllegalArgumentException(t.getToken().name());
            }
        }

        t.nextIf(PARAMETERS_END);

        return hasParameters
                ? ArgumentsFactory.create(plural, shortName, numericExists, numeric, capitalize)
                : ArgumentsFactory.inherited();
    }

    private static boolean parsePlural(final Tokenizer t) {
        t.check(BOOLEAN);
        final boolean value = t.tokenAsBoolean();
        t.next();
        return value;
    }

    private static boolean parseShortName(final Tokenizer t) {
        t.check(BOOLEAN);
        final boolean value = t.tokenAsBoolean();
        t.next();
        return value;
    }

    private static int parseNumeric(final Tokenizer t) {
        t.check(INTEGER);
        final int value = t.tokenAsInteger();
        t.next();
        return value;
    }

    private static boolean parseCapitalize(final Tokenizer t) {
        t.check(BOOLEAN);
        final boolean value = t.tokenAsBoolean();
        t.next();
        return value;
    }
}
