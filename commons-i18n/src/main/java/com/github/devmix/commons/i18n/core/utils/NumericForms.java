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

package com.github.devmix.commons.i18n.core.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Source <a href="http://localization-guide.readthedocs.org/en/latest/l10n/pluralforms.html">Plural forms</a>
 *
 * @author Sergey Grachev
 */
public final class NumericForms {

    private static final Map<String, Form> LANGUAGES = new HashMap<>();

    static {
        addForm(new Form() {
            @Override
            public int of(final long n) {
                return (n > 1) ? 1 : 0;
            }
        }, "ach", "ak", "am", "arn", "br", "fil", "fr", "gun", "ln", "mfe", "mg", "mi", "oc", "pt_BR", "tg", "ti", "tr", "uz", "wa", "zh");

        addForm(new Form() {
            @Override
            public int of(final long n) {
                return (n != 1) ? 1 : 0;
            }
        }, "af", "an", "anp", "as", "ast", "az", "bg", "bn", "brx", "ca", "da", "de", "doi", "el", "en", "eo", "es", "es_AR", "et", "eu", "ff", "fi", "fo", "fur", "fy", "gl", "gu", "ha", "he", "hi", "hne", "hy", "hu", "ia", "it", "kl", "kn", "ku", "lb", "mai", "ml", "mn", "mni", "mr", "nah", "nap", "nb", "ne", "nl", "se", "nn", "no", "nso", "or", "ps", "pa", "pap", "pms", "pt", "rm", "rw", "sat", "sco", "sd", "si", "so", "son", "sq", "sw", "sv", "ta", "te", "tk", "ur", "yo");

        addForm(new Form() {
            @Override
            public int of(final long n) {
                return n == 0 ? 0 : n == 1 ? 1 : n == 2 ? 2 : n % 100 >= 3 && n % 100 <= 10 ? 3 : n % 100 >= 11 ? 4 : 5;
            }
        }, "ar");

        addForm(new Form() {
            @Override
            public int of(final long n) {
                return 0;
            }
        }, "ay", "bo", "cgg", "dz", "fa", "id", "ja", "jbo", "ka", "kk", "km", "ko", "ky", "lo", "ms", "my", "sah", "su", "th", "tt", "ug", "vi", "wo", "zh");

        addForm(new Form() {
            @Override
            public int of(final long n) {
                return n % 10 == 1 && n % 100 != 11 ? 0 : n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20) ? 1 : 2;
            }
        }, "be", "bs", "hr", "ru", "sr", "uk");

        addForm(new Form() {
            @Override
            public int of(final long n) {
                return (n == 1) ? 0 : (n >= 2 && n <= 4) ? 1 : 2;
            }
        }, "cs", "sk");

        addForm(new Form() {
            @Override
            public int of(final long n) {
                return (n == 1) ? 0 : n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20) ? 1 : 2;
            }
        }, "csb");

        addForm(new Form() {
            @Override
            public int of(final long n) {
                return (n == 1) ? 0 : (n == 2) ? 1 : (n != 8 && n != 11) ? 2 : 3;
            }
        }, "cy");

        addForm(new Form() {
            @Override
            public int of(final long n) {
                return (n == 1) ? 0 : n == 2 ? 1 : n < 7 ? 2 : n < 11 ? 3 : 4;
            }
        }, "ga");

        addForm(new Form() {
            @Override
            public int of(final long n) {
                return (n == 1 || n == 11) ? 0 : (n == 2 || n == 12) ? 1 : (n > 2 && n < 20) ? 2 : 3;
            }
        }, "gd");

        addForm(new Form() {
            @Override
            public int of(final long n) {
                return n % 10 != 1 ? 1 : (n % 100 == 11 ? 1 : 0);
            }
        }, "is");

        addForm(new Form() {
            @Override
            public int of(final long n) {
                return (n == 1) ? 0 : (n == 2) ? 1 : (n == 3) ? 2 : 3;
            }
        }, "kw");

        addForm(new Form() {
            @Override
            public int of(final long n) {
                return n % 10 == 1 && n % 100 != 11 ? 0 : n % 10 >= 2 && (n % 100 < 10 || n % 100 >= 20) ? 1 : 2;
            }
        }, "lt");

        addForm(new Form() {
            @Override
            public int of(final long n) {
                return n % 10 == 1 && n % 100 != 11 ? 0 : n != 0 ? 1 : 2;
            }
        }, "lv");

        addForm(new Form() {
            @Override
            public int of(final long n) {
                return n == 1 || n % 10 == 1 ? 0 : 1;
            }
        }, "mk");

        addForm(new Form() {
            @Override
            public int of(final long n) {
                return n == 0 ? 0 : n == 1 ? 1 : 2;
            }
        }, "mnk");

        addForm(new Form() {
            @Override
            public int of(final long n) {
                return n == 1 ? 0 : n == 0 || (n % 100 > 1 && n % 100 < 11) ? 1 : (n % 100 > 10 && n % 100 < 20) ? 2 : 3;
            }
        }, "mt");

        addForm(new Form() {
            @Override
            public int of(final long n) {
                return n == 1 ? 0 : n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20) ? 1 : 2;
            }
        }, "pl");

        addForm(new Form() {
            @Override
            public int of(final long n) {
                return n == 1 ? 0 : (n == 0 || (n % 100 > 0 && n % 100 < 20)) ? 1 : 2;
            }
        }, "ro");

        addForm(new Form() {
            @Override
            public int of(final long n) {
                return n % 100 == 1 ? 1 : n % 100 == 2 ? 2 : n % 100 == 3 || n % 100 == 4 ? 3 : 0;
            }
        }, "sl");
    }

    private NumericForms() {
    }

    private static void addForm(final Form form, final String... languages) {
        for (final String language : languages) {
            LANGUAGES.put(language, form);
        }
    }

    public static int formOf(final int number, final Locale locale) {
        final Form form = LANGUAGES.get(locale.getLanguage());
        return form == null ? 0 : form.of(number);
    }

    public static int formOf(final int number) {
        return formOf(number, Locale.getDefault());
    }

    private interface Form {
        int of(long n);
    }
}
