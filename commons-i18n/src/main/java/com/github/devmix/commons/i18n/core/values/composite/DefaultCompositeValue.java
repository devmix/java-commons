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

import com.github.devmix.commons.i18n.core.utils.NumericForms;
import com.github.devmix.commons.i18n.core.utils.Tokenizer;
import com.github.devmix.commons.i18n.core.values.CompositeValue;
import com.github.devmix.commons.i18n.core.values.Value;

import javax.annotation.Nullable;
import java.util.Locale;

import static com.github.devmix.commons.i18n.core.utils.Tokenizer.Token;

/**
 * @author Sergey Grachev
 */
final class DefaultCompositeValue implements CompositeValue {

    private String text;
    private Value.Attributes attributes;

    DefaultCompositeValue(final String value) {
        parseExpression(value);
    }

    private void parseExpression(final String value) {
        final Tokenizer t = new Tokenizer(value);
        t.next();

        if (t.is(Token.TEXT)) {
            text = t.tokenAsString();
            t.next();
        }

        if (t.is(Token.REFERENCE_START)) {
            t.next(Token.PARAMETERS_BEGIN);
            t.next();
            attributes = CompositeParser.parseParameters(t);
            t.nextIf(Token.REFERENCE_END);
        }

        t.nextIf(Token.CODE_END);
    }

    @Override
    public String toString() {
        return text;
    }

    @Nullable
    @Override
    public String evaluate(@Nullable final Arguments params, final Locale locale) {
        if (params == null) {
            return text;
        }

        if (params.shortName().exists()) {
            return attributes.shortName().text();
        }

        final StringBuilder result = new StringBuilder();

        if (params.plural().exists() && !params.numeric().exists()) {
            final Attributes.Plural attr = attributes.plural();
            if (attr.asPrefix()) {
                result.append(attr.text());
            }
            result.append(text);
            if (attr.asSuffix()) {
                result.append(attr.text());
            }
        } else {
            result.append(text);
        }

        if (params.capitalize().exists()) {
            final Attributes.Capitalize attr = attributes.capitalize();
            final int start = attr.rangeStart() == -1 ? 0 : attr.rangeStart();
            final int end = attr.rangeEnd() == -1 ? Math.min(start + 1, result.length() - 1) : attr.rangeEnd();
            result.replace(start, end, attr.toLower() ? attr.text().toLowerCase() : attr.text().toUpperCase());
        }

        if (params.numeric().exists()) {
            final int form = NumericForms.formOf(params.numeric().number(), locale);
            final String[] forms = attributes.numeric().forms();
            if (form < forms.length) {
                result.append(forms[form]);
            }
        }

        return result.toString();
    }
}
