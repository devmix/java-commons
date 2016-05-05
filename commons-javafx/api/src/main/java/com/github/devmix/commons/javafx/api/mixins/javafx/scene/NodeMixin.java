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

package com.github.devmix.commons.javafx.api.mixins.javafx.scene;

public interface NodeMixin<D> {
    D withAccessibleRole(javafx.scene.AccessibleRole arg0);

    D withPickOnBounds(boolean arg0);

    D withUserData(java.lang.Object arg0);

    D withId(java.lang.String arg0);

    D withStyle(java.lang.String arg0);

    D withVisible(boolean arg0);

    D withCursor(javafx.scene.Cursor arg0);

    D withOpacity(double arg0);

    D withBlendMode(javafx.scene.effect.BlendMode arg0);

    D withClip(javafx.scene.Node arg0);

    D withCache(boolean arg0);

    D withCacheHint(javafx.scene.CacheHint arg0);

    D withEffect(javafx.scene.effect.Effect arg0);

    D withDepthTest(javafx.scene.DepthTest arg0);

    D withDisable(boolean arg0);

    D withOnDragEntered(javafx.event.EventHandler<? super javafx.scene.input.DragEvent> arg0);

    D withOnDragExited(javafx.event.EventHandler<? super javafx.scene.input.DragEvent> arg0);

    D withOnDragOver(javafx.event.EventHandler<? super javafx.scene.input.DragEvent> arg0);

    D withOnDragDropped(javafx.event.EventHandler<? super javafx.scene.input.DragEvent> arg0);

    D withOnDragDone(javafx.event.EventHandler<? super javafx.scene.input.DragEvent> arg0);

    D withManaged(boolean arg0);

    D withLayoutX(double arg0);

    D withLayoutY(double arg0);

    D withTranslateX(double arg0);

    D withTranslateY(double arg0);

    D withTranslateZ(double arg0);

    D withScaleX(double arg0);

    D withScaleY(double arg0);

    D withScaleZ(double arg0);

    D withRotate(double arg0);

    D withRotationAxis(javafx.geometry.Point3D arg0);

    D withNodeOrientation(javafx.geometry.NodeOrientation arg0);

    D withMouseTransparent(boolean arg0);

    D withOnContextMenuRequested(javafx.event.EventHandler<? super javafx.scene.input.ContextMenuEvent> arg0);

    D withOnMouseClicked(javafx.event.EventHandler<? super javafx.scene.input.MouseEvent> arg0);

    D withOnMouseDragged(javafx.event.EventHandler<? super javafx.scene.input.MouseEvent> arg0);

    D withOnMouseEntered(javafx.event.EventHandler<? super javafx.scene.input.MouseEvent> arg0);

    D withOnMouseExited(javafx.event.EventHandler<? super javafx.scene.input.MouseEvent> arg0);

    D withOnMouseMoved(javafx.event.EventHandler<? super javafx.scene.input.MouseEvent> arg0);

    D withOnMousePressed(javafx.event.EventHandler<? super javafx.scene.input.MouseEvent> arg0);

    D withOnMouseReleased(javafx.event.EventHandler<? super javafx.scene.input.MouseEvent> arg0);

    D withOnDragDetected(javafx.event.EventHandler<? super javafx.scene.input.MouseEvent> arg0);

    D withOnMouseDragOver(javafx.event.EventHandler<? super javafx.scene.input.MouseDragEvent> arg0);

    D withOnMouseDragReleased(javafx.event.EventHandler<? super javafx.scene.input.MouseDragEvent> arg0);

    D withOnMouseDragEntered(javafx.event.EventHandler<? super javafx.scene.input.MouseDragEvent> arg0);

    D withOnMouseDragExited(javafx.event.EventHandler<? super javafx.scene.input.MouseDragEvent> arg0);

    D withOnScrollStarted(javafx.event.EventHandler<? super javafx.scene.input.ScrollEvent> arg0);

    D withOnScroll(javafx.event.EventHandler<? super javafx.scene.input.ScrollEvent> arg0);

    D withOnScrollFinished(javafx.event.EventHandler<? super javafx.scene.input.ScrollEvent> arg0);

    D withOnRotationStarted(javafx.event.EventHandler<? super javafx.scene.input.RotateEvent> arg0);

    D withOnRotate(javafx.event.EventHandler<? super javafx.scene.input.RotateEvent> arg0);

    D withOnRotationFinished(javafx.event.EventHandler<? super javafx.scene.input.RotateEvent> arg0);

    D withOnZoomStarted(javafx.event.EventHandler<? super javafx.scene.input.ZoomEvent> arg0);

    D withOnZoom(javafx.event.EventHandler<? super javafx.scene.input.ZoomEvent> arg0);

    D withOnZoomFinished(javafx.event.EventHandler<? super javafx.scene.input.ZoomEvent> arg0);

    D withOnSwipeUp(javafx.event.EventHandler<? super javafx.scene.input.SwipeEvent> arg0);

    D withOnSwipeDown(javafx.event.EventHandler<? super javafx.scene.input.SwipeEvent> arg0);

    D withOnSwipeLeft(javafx.event.EventHandler<? super javafx.scene.input.SwipeEvent> arg0);

    D withOnSwipeRight(javafx.event.EventHandler<? super javafx.scene.input.SwipeEvent> arg0);

    D withOnTouchPressed(javafx.event.EventHandler<? super javafx.scene.input.TouchEvent> arg0);

    D withOnTouchMoved(javafx.event.EventHandler<? super javafx.scene.input.TouchEvent> arg0);

    D withOnTouchReleased(javafx.event.EventHandler<? super javafx.scene.input.TouchEvent> arg0);

    D withOnTouchStationary(javafx.event.EventHandler<? super javafx.scene.input.TouchEvent> arg0);

    D withOnKeyPressed(javafx.event.EventHandler<? super javafx.scene.input.KeyEvent> arg0);

    D withOnKeyReleased(javafx.event.EventHandler<? super javafx.scene.input.KeyEvent> arg0);

    D withOnKeyTyped(javafx.event.EventHandler<? super javafx.scene.input.KeyEvent> arg0);

    D withOnInputMethodTextChanged(javafx.event.EventHandler<? super javafx.scene.input.InputMethodEvent> arg0);

    D withInputMethodRequests(javafx.scene.input.InputMethodRequests arg0);

    D withFocusTraversable(boolean arg0);

    D withEventDispatcher(javafx.event.EventDispatcher arg0);

    D withAccessibleRoleDescription(java.lang.String arg0);

    D withAccessibleText(java.lang.String arg0);

    D withAccessibleHelp(java.lang.String arg0);

}