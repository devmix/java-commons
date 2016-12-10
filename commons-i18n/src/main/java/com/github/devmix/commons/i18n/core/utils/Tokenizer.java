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

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author Sergey Grachev
 */
public final class Tokenizer {

    private final String text;
    private final int length;
    private int absolutePosition;
    private int line;
    private int linePosition;
    private char character;
    private State state;
    private String tokenValue;
    private Token token = Token.CODE_BEGIN;

    public Tokenizer(final String text) {
        this.text = text;
        this.absolutePosition = 0;
        this.line = 1;
        this.linePosition = 0;
        this.length = text.length();
        this.state = State.TEXT;
    }

    public boolean hasNext() {
        return absolutePosition < length;
    }

    public void next() {
        next0();

        switch (state) {
            case TEXT:
                if (Token.REFERENCE_START.equals(token)) {
                    // open expression
                    state = State.REFERENCE;
                }
                break;

            case REFERENCE:
                if (Token.REFERENCE_END.equals(token)) {
                    // close expression
                    state = State.TEXT;
                } else if (Token.PARAMETERS_BEGIN.equals(token)) {
                    // open parameters
                    state = State.PARAMETERS;
                }
                break;

            case PARAMETERS:
                if (Token.PARAMETERS_END.equals(token)) {
                    // close parameters
                    state = State.REFERENCE;
                }
                break;

            default:
                throw new IllegalStateException("Unknown token " + state);
        }
    }

    public void next(final Token... expected) {
        next();
        check(expected);
    }

    public void nextIf(final Token... expected) {
        check(expected);
        next();
    }

    public boolean is(final Token... expected) {
        return checkToken(expected);
    }

    public void check(final Token... expected) {
        if (!checkToken(expected)) {
            throw new IllegalArgumentException("Unexpected token " + token + ", required any of " + Arrays.toString(expected));
        }
    }

    private boolean checkToken(final Token... expected) {
        final Token current = token;
        for (final Token token : expected) {
            if (token.equals(current)) {
                return true;
            }
        }
        return false;

    }


    private Tokenizer next0() {
        if (!hasNext()) {
            token(Token.CODE_END);
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        this.token = Token.TEXT;
        while (hasNext()) {
            nextChar();
            switch (character) {
                case '\\':
                    nextMaskedSymbol(sb);
                    break;

                case '$':
                    if (State.PARAMETERS.equals(state)) {
                        return token(Token.DOLLAR);
                    }
                    if (hasNext()) {
                        nextChar();
                        if (character == '{') {
                            if (sb.length() > 0) {
                                returnChar(2);
                                return token(Token.TEXT, sb.toString());
                            }
                            return token(Token.REFERENCE_START);
                        } else {
                            sb.append('$').append(character);
                        }
                    } else {
                        sb.append(character);
                    }
                    break;

                case '{':
                    if (State.PARAMETERS.equals(state)) {
                        return token(Token.CURLY_BRACE_LEFT);
                    }
                    sb.append(character);
                    break;

                case '}':
                    if (State.REFERENCE.equals(state)) {
                        if (sb.length() > 0) {
                            returnChar();
                            return token(Token.ID, sb.toString());
                        }
                        return token(Token.REFERENCE_END);
                    } else if (State.PARAMETERS.equals(state)) {
                        return token(Token.CURLY_BRACE_RIGHT);
                    }
                    sb.append(character);
                    break;

                case '(':
                    if (State.REFERENCE.equals(state)) {
                        if (sb.length() > 0) {
                            returnChar();
                            return token(Token.ID, sb.toString());
                        }
                        return token(Token.PARAMETERS_BEGIN);
                    } else if (State.PARAMETERS.equals(state)) {
                        return token(Token.LEFTP);
                    }
                    sb.append(character);
                    break;

                case ')':
                    if (State.PARAMETERS.equals(state)) {
                        if (sb.length() > 0) {
                            returnChar();
                            return token(Token.TEXT, sb.toString());
                        }
                        return token(Token.PARAMETERS_END);
                    }
                    sb.append(character);
                    break;

                case ':':
                    if (State.PARAMETERS.equals(state)) {
                        return token(Token.COLON);
                    }
                    sb.append(character);
                    break;

                case ',':
                    if (State.PARAMETERS.equals(state)) {
                        return token(Token.COMMA);
                    }
                    sb.append(character);
                    break;

                case '*':
                    if (State.PARAMETERS.equals(state)) {
                        return token(Token.STAR);
                    }
                    sb.append(character);
                    break;

                case '\'':
                    if (State.PARAMETERS.equals(state)) {
                        nextChar();
                        while (hasNext() && character != '\'') {
                            sb.append(character);
                            nextChar();
                        }
                        return token(Token.STRING, sb.toString());
                    }
                    sb.append(character);
                    break;

                case '[':
                    if (State.PARAMETERS.equals(state)) {
                        return token(Token.ARRAY_START);
                    }
                    sb.append(character);
                    break;

                case ']':
                    if (State.PARAMETERS.equals(state)) {
                        return token(Token.ARRAY_END);
                    }
                    sb.append(character);
                    break;

                default:
                    if ((State.PARAMETERS.equals(state) || State.REFERENCE.equals(state)) && skipEmptyCharacters()) {
                        continue;
                    }
                    if (State.PARAMETERS.equals(state)) {
                        if (isCurrentCharacterIdSymbol()) {
                            while (hasNext() && isCurrentCharacterIdSymbol() || isCurrentCharacterNumber()) {
                                sb.append(character);
                                nextChar();
                            }
                            if (sb.length() > 0) {
                                if (!isCurrentCharacterIdSymbol() && !isCurrentCharacterNumber()) {
                                    returnChar();
                                }
                                final String id = sb.toString();
                                if ("true".equals(id) || "false".equals(id)) {
                                    return token(Token.BOOLEAN, id);
                                }
                                return token(Token.ID, id);
                            }
                        } else if (isCurrentCharacterNumber()) {
                            while (hasNext() && isCurrentCharacterNumber()) {
                                sb.append(character);
                                nextChar();
                            }
                            if (sb.length() > 0) {
                                if (!isCurrentCharacterNumber()) {
                                    returnChar();
                                }
                                return token(Token.INTEGER, sb.toString());
                            }
                        }
                    }
                    sb.append(character);
            }
        }

        return token(Token.TEXT, sb.toString());
    }

    private boolean skipEmptyCharacters() {
        boolean skipped = false;

        while (hasNext() && (character == '\n' || character == ' ')) {
            nextChar();
            skipped = true;
        }

        if (skipped) {
            returnChar();
            return true;
        }

        return false;
    }

    private boolean isCurrentCharacterIdSymbol() {
        return (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || character == '_';
    }

    private boolean isCurrentCharacterNumber() {
        return character > '0' && character < '9';
    }

    private void nextMaskedSymbol(final StringBuilder sb) {
        if (hasNext()) {
            nextChar();
            sb.append(character);
        } else {
            sb.append(character);
        }
    }

    private void returnChar(final int count) {
        if (absolutePosition - count > 0) {
            absolutePosition -= count;
        } else {
            absolutePosition = 0;
        }
        if (linePosition - count > 0) {
            linePosition -= count;
        } else {
            linePosition = 0;
        }
        character = text.charAt(absolutePosition);
    }

    private void returnChar() {
        if (absolutePosition > 0) {
            character = text.charAt(--absolutePosition);
            if (linePosition > 0) {
                linePosition--;
            }
        }
    }

    private Tokenizer token(final Token token, @Nullable final String value) {
        this.token = token;
        this.tokenValue = value;
        return this;
    }

    private Tokenizer token(final Token token) {
        return token(token, null);
    }

    private void nextChar() {
        character = text.charAt(absolutePosition++);
        if (character == '\n') {
            line++;
            linePosition = 0;
        } else {
            linePosition++;
        }
    }

    public int getLine() {
        return line;
    }

    public int getLinePosition() {
        return linePosition;
    }

    public Token getToken() {
        return token;
    }

    public String tokenAsString() {
        return tokenValue;
    }

    public int tokenAsInteger() {
        return Integer.valueOf(tokenValue);
    }

    public boolean tokenAsBoolean() {
        return Boolean.valueOf(tokenValue);
    }

    public enum Token {
        CODE_BEGIN,
        CODE_END,
        TEXT,
        ID,
        INTEGER,
        BOOLEAN,
        STRING,
        REFERENCE_START, REFERENCE_END,
        PARAMETERS_BEGIN, PARAMETERS_END,
        ARRAY_START, ARRAY_END,
        CURLY_BRACE_LEFT, CURLY_BRACE_RIGHT,
        DOLLAR,
        COLON,
        COMMA,
        STAR,
        LEFTP
    }

    public enum State {
        TEXT,
        REFERENCE,
        PARAMETERS
    }
}
