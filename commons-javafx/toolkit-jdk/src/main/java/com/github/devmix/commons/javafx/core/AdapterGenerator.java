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

package com.github.devmix.commons.javafx.core;

import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.controlsfx.control.MaskerPane;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.StatusBar;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Sergey Grachev
 */
public final class AdapterGenerator {

    private static final String DEFAULT_PACKAGE = "com.github.devmix.commons.javafx.api.mixins";
    private static final Map<String, String> OVERRIDDEN_PARAMETERS = new HashMap<>();

    static {
        OVERRIDDEN_PARAMETERS.put(makeKey(TableView.class, "setColumnResizePolicy"),
                "javafx.util.Callback<javafx.scene.control.TableView.ResizeFeatures, java.lang.Boolean> arg0");

        OVERRIDDEN_PARAMETERS.put(makeKey(TableColumn.class, "setSortType"),
                "javafx.scene.control.TableColumn.SortType arg0");
    }

    private final String basePackage;
    private final String outputPath;
    private final List<AdapteeDescriptor> adapteeDescriptors = new LinkedList<>();
    private final Map<Class<?>, String> processed = new HashMap<>();
    private final Path outputDirectory;

    private AdapterGenerator(final String outputPath, final String basePackage) {
        this.outputPath = outputPath;
        this.basePackage = basePackage;
        this.outputDirectory = Paths.get(outputPath, basePackage.split("\\."));
    }

    public static void main(final String[] args) throws IOException {
        final String outputPath = "/mnt/work-crypted/projects/java/archive-manager/java-commons/commons-javafx/api/src/main/java";
//        final String outputPath = "/mnt/work-crypted/temp/adapters";
        final AdapterGenerator generator = new AdapterGenerator(
                outputPath, DEFAULT_PACKAGE)
                .add(BorderPane.class)
                .add(Button.class)
                .add(ContextMenu.class)
                .add(GridPane.class)
                .add(ListView.class)
                .add(MaskerPane.class)
                .add(MenuBar.class)
                .add(Menu.class)
                .add(MenuItem.class)
                .add(NotificationPane.class)
                .add(StackPane.class)
                .add(Stage.class)
                .add(StatusBar.class)
                .add(Tab.class)
                .add(TableView.class)
                .add(TabPane.class)
                .add(Text.class)
                .add(TextField.class)

                .add(ColumnConstraints.class)
                .add(RowConstraints.class)
                .add(TableColumn.class)
                .generate();
    }

    private AdapterGenerator generate() throws IOException {
        final Path outputDirectory = Paths.get(outputPath, basePackage.split("\\."));
        if (!Files.isDirectory(outputDirectory)) {
            Files.createDirectories(outputDirectory);
        }

        for (final AdapteeDescriptor descriptor : adapteeDescriptors) {
            generate(descriptor.adapteeClass);
        }

        return this;
    }

    @Nullable
    private String generate(final Class<?> adapteeClass) throws IOException {
        if (processed.containsKey(adapteeClass)) {
            return processed.get(adapteeClass);
        }

        String extendsClass = null;
        final Class<?> baseClass = adapteeClass.getSuperclass();
        if (baseClass != null && !Object.class.equals(baseClass)) {
            extendsClass = generate(baseClass);
        }

        System.out.println();
        System.out.println(adapteeClass);

        final String adapteePackage = adapteeClass.getPackage().getName();
        final String mixinClassName = adapteeClass.getSimpleName() + "Mixin";
        final String packageName = joinPackage(basePackage, adapteePackage);
        final String typeVariables = createTypeVariablesFor(adapteeClass);
        final String mixinFullClassName = joinPackage(packageName, mixinClassName) + typeVariables;

        final Path mixinPath = Paths.get(outputDirectory.toString(), adapteePackage.split("\\."));
        if (!Files.exists(mixinPath)) {
            Files.createDirectories(mixinPath);
        }

        final Path mixinFile = Paths.get(mixinPath.toString(), mixinClassName + ".java");

        try (BufferedWriter writer = Files.newBufferedWriter(mixinFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.append("package ").append(packageName).append(';').append('\n');


            writer.append("public interface ").append(mixinClassName).append(typeVariables);
            if (extendsClass != null) {
                writer.append(" extends ").append(extendsClass);
            }
            writer.append(" {\n");

            for (final Method adapteeMethod : adapteeClass.getDeclaredMethods()) {
                if (!Modifier.isPublic(adapteeMethod.getModifiers())
                        || Modifier.isAbstract(adapteeMethod.getModifiers())
                        || adapteeMethod.getAnnotation(Deprecated.class) != null) {
                    continue;
                }

                final String adapteeMethodName = adapteeMethod.getName();
                if (!adapteeMethodName.startsWith("set")) {
                    continue;
                }

                System.out.println(adapteeMethod);

                final String overriddenParameters = OVERRIDDEN_PARAMETERS.get(makeKey(adapteeClass, adapteeMethodName));
                final String mixinMethodName = "with" + adapteeMethodName.substring(3);

                writer.append('\t').append("D ").append(mixinMethodName).append('(');

                if (overriddenParameters != null) {
                    writer.append(overriddenParameters);
                } else {
                    int i = 0;
                    for (final Parameter parameter : adapteeMethod.getParameters()) {
                        if (i++ > 0) {
                            writer.append(", ");
                        }
                        writer
                                .append(parameterTypeToString(parameter))
                                .append(' ').append(parameter.getName());
                    }
                }
                writer.append(");\n\n");
            }

            writer.append("}");
            writer.flush();
        }

        processed.put(adapteeClass, mixinFullClassName);

        return mixinFullClassName;
    }

    private String createTypeVariablesFor(final Class<?> adapteeClass) {
        final StringBuilder result = new StringBuilder("<D");
        if (TypeUtils.containsTypeVariables(adapteeClass)) {
            for (final TypeVariable<?> typeVariable : adapteeClass.getTypeParameters()) {
                result.append(',').append(TypeUtils.toString(typeVariable));
            }
        }
        return result.append('>').toString();
    }

    private AdapterGenerator add(final Class<?> adapteeClass) {
        adapteeDescriptors.add(new AdapteeDescriptor(adapteeClass, null));
        return this;
    }

    private static String parameterTypeToString(final Parameter parameter) {
        final Type type = parameter.getParameterizedType();
//        if (type instanceof ParameterizedType) {
//            final ParameterizedType parameterizedType = (ParameterizedType) type;
//            final StringBuilder typeName = new StringBuilder(parameterizedType.toString());
//            if (parameterizedType.getOwnerType() != null) {
//                typeName.replace(0, parameterizedType.getOwnerType().getTypeName().length() + 1, "");
//            }
//            return typeName.toString().replaceAll("\\$", "\\.");
//        }
        return TypeUtils.toString(type);
    }

    private static String joinPackage(final String first, final String second) {
        return StringUtils.isBlank(first) ? second : first + '.' + second;
    }

    private static String makeKey(final Class<?> clazz, final String methodNamed) {
        return clazz.toString() + "#" + methodNamed;
    }

    private static final class AdapteeDescriptor {

        private final Class<?> adapteeClass;
        private final String packageName;

        public AdapteeDescriptor(final Class<?> adapteeClass, final String packageName) {
            this.adapteeClass = adapteeClass;
            this.packageName = packageName;
        }
    }
}
