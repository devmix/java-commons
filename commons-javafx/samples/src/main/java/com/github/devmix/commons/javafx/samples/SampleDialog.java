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

package com.github.devmix.commons.javafx.samples;

import com.github.devmix.commons.javafx.api.components.standard.StageDecorator;
import com.github.devmix.commons.javafx.api.utils.Utils;
import com.github.devmix.commons.javafx.api.views.View;
import com.github.devmix.commons.javafx.samples.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.controlsfx.control.MaskerPane;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.StatusBar;
import org.controlsfx.control.action.Action;

import static java.util.Arrays.asList;

/**
 * @author Sergey Grachev
 */
public final class SampleDialog {

    private final View v;
    private final Utils u;

    private final TableView<User> table;
    private NotificationPane notifications;
    private MaskerPane masker;
    private final User user = new User("1", "2", "3");

    public SampleDialog(final Stage stage, final MyToolkitService tk) {
        this.v = tk.newView();
        this.u = tk.utils();

        table = v.<User>tableView()
                .columns(asList(
                        u.tableView().<User, String>column()
                                .i18n("name")
                                .withCellValueFactory(new PropertyValueFactory<>("name")).$(),
                        u.tableView().<User, String>column()
                                .withText("Login")
                                .withCellValueFactory(new PropertyValueFactory<>("login")).$(),
                        u.tableView().<User, String>column()
                                .withText("Account")
                                .withCellValueFactory(new PropertyValueFactory<>("account")).$()))
                .withItems(createUsers())
                .withContextMenu(v.contextMenu().items(
                        v.menuItem().i18n("delete").withOnAction(this::onTableDeleteMenuItem).$(),
                        v.menuItem().i18n("add").$()).$())
                .$();

        final MenuBar menuBar = v.menuBar().mnemonicParsing(true).menus(
                v.menu().i18n("m1").items(
                        v.menuItem().i18n("m11"),
                        v.menuItem().i18n("m12")),
                v.menu().i18n("m2")).$();

        final TabPane tabPane = v.tabPane().tabs(
                v.tab().i18n("tab1")
                        .withClosable(false)
                        .withContent(v.gridPane()
                                .withMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE)
                                .columnConstraints(
                                        u.gridPane().columnConstraint().withPrefWidth(256),
                                        u.gridPane().columnConstraint().withHgrow(Priority.ALWAYS))
                                .rowConstraints(
                                        u.gridPane().rowConstraint().withVgrow(Priority.ALWAYS),
                                        u.gridPane().rowConstraint().withVgrow(Priority.ALWAYS))
                                .add(v.<User>listView()
                                        .withItems(createUsers())
                                        .$(), 0, 0)
                                .add(table, 1, 0).$()
                        ).$(),
                v.tab().i18n("tab2")
                        .withContent(v.button().i18n("btn1").$()).$()
        ).$();

        final StatusBar statusBar = v.statusBar().rightItems(
                v.button().i18n("test").withOnAction(this::onButtonClick).$(),
                v.textField().withText("test").$$(c -> c.textProperty().bind(user.accountProperty()))).$();


        notifications = v.notificationPane()
                .actions(new Action("Background", actionEvent -> this.notifications.hide()))
                .withOnShown(event -> this.masker.setVisible(true))
                .withOnHidden(event -> this.masker.setVisible(false))
                .withContent(v.stackPane().children(
                        v.borderPane()
                                .withTop(menuBar)
                                .withCenter(tabPane)
                                .withBottom(statusBar).$(),
                        masker = v.maskerPane()
                                .withVisible(false).$()
                ).$()).$();

        v.<StageDecorator>wrap(stage)
                .withTitle("Hello World")
                .withScene(new Scene(notifications, 800, 600)).$().show();
    }

    private void onButtonClick(final ActionEvent value) {
        notifications.getStyleClass().add(NotificationPane.STYLE_CLASS_DARK);
        notifications.show("Test: " + System.nanoTime());
        user.setAccount("ww");
    }

    private void onTableDeleteMenuItem(final ActionEvent event) {
        final User item = table.getItems().get(table.getSelectionModel().getSelectedIndex());
        if (item != null) {
            System.out.println("delete " + item);
        }
    }

    private ObservableList<User> createUsers() {
        return FXCollections.observableList(asList(
                new User("n1", "a1", "l1"),
                new User("n2", "a2", "l2")));
    }
}
