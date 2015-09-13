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

package com.github.devmix.commons.swing.samples;

import com.alee.extended.layout.FormLayout;
import com.alee.extended.layout.ToolbarLayout;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.rootpane.WebDialog;
import com.github.devmix.commons.i18n.api.LanguageOptions;
import com.github.devmix.commons.i18n.datasource.ResourceBundleDataSource;
import com.github.devmix.commons.swing.api.ToolkitService;
import com.github.devmix.commons.swing.api.View;
import com.github.devmix.commons.swing.api.bindings.Binder;
import com.github.devmix.commons.swing.api.decorators.standard.DialogDecorator;
import com.github.devmix.commons.swing.api.verifiers.VerifierStatus;
import com.github.devmix.commons.swing.api.verifiers.Verifiers;
import com.github.devmix.commons.swing.core.bindings.AbstractController;
import com.github.devmix.commons.swing.core.i18n.LanguageManager;
import com.github.devmix.commons.swing.toolkit.weblaf.WebLaFToolkitService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Sergey Grachev
 */
public final class SampleDialog {

    private final View v;
    private final DialogDecorator dialog;

    private SampleDialog(final ToolkitService toolkit) {
        this.v = toolkit.createView();
        final Binder b = toolkit.createBinder();
        final SampleCtrl c = new SampleCtrl();
        final Verifiers iv = toolkit.verifiers();

        this.dialog = v.dialog().name("dialog")
                .a(v.panel().layout(new BorderLayout()).margin(8)
                        .a(v.list().name("users")
                                .listData(new User[]{
                                        new User("User1", "acc1", "user1"),
                                        new User("User2", "acc2", "user2"),
                                        new User("User3", "acc3", "user3")
                                }).preferredWidth(100).wrapScrollPane(), BorderLayout.WEST)

                        .a(v.panel().layout(new FormLayout(8, 8)).margin(0, 8, 0, 0)
                                .a(v.label().text("Account"),
                                        v.textField().name("account").verifier(iv.blank()))
                                .a(v.label().text("Login"),
                                        v.textField().name("login").verifier(iv.blank()))
                                .a(v.label().text("Password"),
                                        v.textField().name("password").verifier(iv.blank())), BorderLayout.CENTER)

                        .a(v.panel().layout(new ToolbarLayout()).margin(8, 0, 0, 0)
                                .a(v.button().name("login-btn").text("Login")
                                        .onAction(this::onLoginBtn), ToolbarLayout.END)
                                .a(v.button().name("close-btn").text("Close")
                                        .onAction(this::onCloseBtn), ToolbarLayout.END)
                                .$(o -> {
                                    toolkit.utils().swing()
                                            .equalizeComponentsSize(v.name("login-btn").$(), v.name("close-btn").$());
                                }), BorderLayout.SOUTH));

        b.with(c, v)
                .bind("users").selectedElement().to(SampleCtrl::getSelectedUser)
                .bind("dialog").read().property("title").to(SampleCtrl::getTitle)
                .bind("login").read().to("selectedUser.login")
                .bind("account").read().to("selectedUser.account")
                .bind();

        dialog.$(o -> {
            o.setPreferredSize(new Dimension(400, 300));
            o.setDefaultCloseOperation(WebDialog.DISPOSE_ON_CLOSE);
            o.setResizable(false);
            o.pack();
            o.setLocationRelativeTo(null);
            o.setVisible(true);
        });
    }

    private void onLoginBtn(final ActionEvent e) {
        if (!VerifierStatus.VALID.equals(v.verifiers().verifyAll())) {
            JOptionPane.showMessageDialog(dialog.$(),
                    "Value of some fields is incorrect", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            onCloseBtn(e);
        }
    }

    private void onCloseBtn(final ActionEvent e) {
        dialog.$().dispose();
    }

    public static void main(final String[] args) {
        WebLookAndFeel.setDecorateFrames(true);
        WebLookAndFeel.setDecorateDialogs(true);
        WebLookAndFeel.install();
        LanguageManager.install(new ResourceBundleDataSource("lang"), LanguageOptions.create()
                .enableFeature(LanguageOptions.Feature.RETURN_KEY_INSTEAD_EMPTY_VALUE));
        new SampleDialog(new WebLaFToolkitService());
    }

    public static final class User {
        private final String name;
        private final String account;
        private final String login;

        public User(final String name, final String account, final String login) {
            this.name = name;
            this.account = account;
            this.login = login;
        }

        public String getName() {
            return name;
        }

        public String getLogin() {
            return login;
        }

        public String getAccount() {
            return account;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class SampleCtrl extends AbstractController {
        private User selectedUser;

        public User getSelectedUser() {
            return selectedUser;
        }

        public String getTitle() {
            return selectedUser == null ? "Please select user"
                    : selectedUser.name + '(' + selectedUser.account + '@' + selectedUser.login + ')';
        }

        public void setSelectedUser(final User selectedUser) {
            pcs.firePropertyChange("selectedUser", this.selectedUser, this.selectedUser = selectedUser);
            pcs.firePropertyChange("title", null, null);
        }
    }
}
