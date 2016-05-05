/*
 * Commons Library
 * Copyright (c) 2015-2016 Sergey Grachev (sergey.grachev@yahoo.com). All rights reserved.
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

package com.github.devmix.commons.javafx.samples.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Sergey Grachev
 */
public class User {

    private final StringProperty name;
    private final StringProperty account;
    private final StringProperty login;

    public User(final String name, final String account, final String login) {
        this.name = new SimpleStringProperty(name);
        this.account = new SimpleStringProperty(account);
        this.login = new SimpleStringProperty(login);
    }

    public String getName() {
        return name.get();
    }

    public String getLogin() {
        return login.get();
    }

    public String getAccount() {
        return account.get();
    }

    public void setName(final String name) {
        this.name.set(name);
    }

    public void setAccount(final String account) {
        this.account.set(account);
    }

    public void setLogin(final String login) {
        this.login.set(login);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty accountProperty() {
        return account;
    }

    public StringProperty loginProperty() {
        return login;
    }

    @Override
    public String toString() {
        return name.get();
    }


}
