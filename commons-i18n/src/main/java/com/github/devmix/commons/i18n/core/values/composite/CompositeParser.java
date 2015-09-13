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

package com.github.devmix.commons.i18n.core.values.composite;

import com.github.devmix.commons.i18n.core.utils.Tokenizer;
import com.github.devmix.commons.i18n.core.values.CompositeValue;
import com.github.devmix.commons.i18n.core.values.Value;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sergey Grachev
 */
public final class CompositeParser {

    // /(.*)(?:\$\{\(\s*(.*?)\s*\)\}\s*$)/gm
    private static final Pattern PATTERN = Pattern.compile("(.*)(?:\\$\\{\\(\\s*(.*?)\\s*\\)\\}\\s*$)", Pattern.DOTALL);

    private CompositeParser() {
    }

    @Nullable
    public static CompositeValue tryParse(final String value) {
        final Matcher matcher = PATTERN.matcher(value);
        if (matcher.matches()) {
            return new DefaultCompositeValue(value);
        }
        return null;
    }

    @Nullable
    static Value.Attributes parseParameters(final Tokenizer t) {
        String id;
        Value.Attributes.Plural plural = null;
        Value.Attributes.ShortName shortName = null;
        Value.Attributes.Numeric numeric = null;
        Value.Attributes.Capitalize capitalize = null;
        while (t.hasNext()) {
            if (t.is(Tokenizer.Token.PARAMETERS_END)) {
                break;
            } else if (t.is(Tokenizer.Token.ID)) {
                id = t.tokenAsString();
                t.next(Tokenizer.Token.COLON);
                t.next();
                if ("p".equals(id) || "plural".equals(id)) {
                    plural = parseParameterPlural(t);
                } else if ("s".equals(id) || "short".equals(id)) {
                    shortName = parseParameterShortName(t);
                } else if ("n".equals(id) || "numeric".equals(id)) {
                    numeric = parseParameterNumeric(t);
                } else if ("c".equals(id) || "cap".equals(id)) {
                    capitalize = parseParameterCapitalize(t);
                } else {
                    // TODO
                    throw new IllegalArgumentException(id);
                }
                if (t.is(Tokenizer.Token.COMMA)) {
                    t.next();
                }
            } else {
                // TODO
                throw new IllegalArgumentException(t.getToken().name());
            }
        }

        t.nextIf(Tokenizer.Token.PARAMETERS_END);

        return ValueAttributes.create(plural, shortName, numeric, capitalize);
    }

    private static Value.Attributes.Plural parseParameterPlural(final Tokenizer t) {
        final String text;
        boolean asPrefix = false;
        boolean asSuffix = false;

        if (t.is(Tokenizer.Token.STAR)) {
            asPrefix = true;
            t.next();
        }

        t.check(Tokenizer.Token.STRING);
        text = t.tokenAsString();
        t.next();

        if (t.is(Tokenizer.Token.STAR)) {
            asSuffix = true;
            t.next();
        }

        return ValueAttributes.plural(text, asPrefix, !asPrefix || asSuffix);
    }

    private static Value.Attributes.Capitalize parseParameterCapitalize(final Tokenizer t) {
        final String text;
        boolean toLower = false;
        int rangeStart = -1;
        int rangeEnd = -1;

        if (t.is(Tokenizer.Token.ID)) {
            toLower = "l".equals(t.tokenAsString());
            t.next();
        }

        t.check(Tokenizer.Token.STRING);
        text = t.tokenAsString();
        t.next();

        if (t.is(Tokenizer.Token.CURLY_BRACE_LEFT)) {
            t.next(Tokenizer.Token.INTEGER);
            rangeStart = t.tokenAsInteger();
            t.next(Tokenizer.Token.COMMA, Tokenizer.Token.CURLY_BRACE_RIGHT);
            if (Tokenizer.Token.COMMA.equals(t.getToken())) {
                t.next(Tokenizer.Token.INTEGER);
                rangeEnd = t.tokenAsInteger();
                t.next(Tokenizer.Token.CURLY_BRACE_RIGHT);
            }
            t.next();
        }

        return ValueAttributes.capitalize(text, toLower, rangeStart, rangeEnd);
    }

    private static Value.Attributes.Numeric parseParameterNumeric(final Tokenizer t) {
        final List<String> forms = new ArrayList<>(4);

        t.nextIf(Tokenizer.Token.ARRAY_START);

        while (t.hasNext()) {
            if (t.is(Tokenizer.Token.ARRAY_END)) {
                break;
            }

            switch (t.getToken()) {
                case COMMA:
                    t.next();
                    break;

                case STRING:
                    forms.add(t.tokenAsString());
                    t.next();
                    break;

                default:
                    // TODO
                    throw new IllegalStateException(t.getToken().name());
            }
        }

        t.nextIf(Tokenizer.Token.ARRAY_END);

        return ValueAttributes.numeric(forms.toArray(new String[forms.size()]));
    }

    private static Value.Attributes.ShortName parseParameterShortName(final Tokenizer t) {
        t.check(Tokenizer.Token.STRING);
        final String text = t.tokenAsString();
        t.next();
        return ValueAttributes.shortName(text);
    }
}
