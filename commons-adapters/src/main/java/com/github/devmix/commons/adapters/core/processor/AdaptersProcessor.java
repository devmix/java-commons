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

package com.github.devmix.commons.adapters.core.processor;

import com.github.devmix.commons.adapters.api.annotations.Adapter;
import com.github.devmix.commons.adapters.core.utils.AdapterUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.EnumSet;
import java.util.Set;

import static com.github.devmix.commons.adapters.api.annotations.Adapter.Processing;

/**
 * Adapter classes annotated by {@link Adapter}
 *
 * @author Sergey Grachev
 */
@SupportedAnnotationTypes({"com.github.devmix.commons.adapters.api.annotations.Adapter"})
public class AdaptersProcessor extends AbstractProcessor {

    private static final EnumSet<Adapter.Processing> IGNORED = EnumSet.of(Processing.RUNTIME, Processing.IGNORE);

    private Filer filer;
    private Messager log;
    private Types typeUtils;
    private Elements elementUtils;
    private DefaultAnnotationsScanner annotationsScanner;

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.filer = processingEnv.getFiler();
        this.log = processingEnv.getMessager();
        this.typeUtils = processingEnv.getTypeUtils();
        this.elementUtils = processingEnv.getElementUtils();
        this.annotationsScanner = new DefaultAnnotationsScanner(elementUtils, typeUtils);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }

        final Set<? extends Element> adapters = roundEnv.getElementsAnnotatedWith(annotations.iterator().next());
        for (final Element target : adapters) {
            if (target.getKind() == ElementKind.CLASS) {
                try {
                    processAdapter((TypeElement) target);
                } catch (final IOException e) {
                    log.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                    e.printStackTrace();
                }
            } else {
                log.printMessage(Diagnostic.Kind.ERROR,
                        "Annotation @" + Adapter.class.getSimpleName() + " not allowed for interfaces and final classes");
            }
        }

        return true;
    }

    /**
     * Generate source file for new adapter which will be extended by the annotated class
     *
     * @param adapterClass super class of adapter
     * @throws IOException
     */
    private void processAdapter(final TypeElement adapterClass) throws IOException {
        final MirrorAdapter adapter = annotationsScanner.adapterFor(adapterClass);
        if (adapter == null || IGNORED.contains(adapter.processing())) {
            return;
        }

        final ExecutableElement adapteeMethod = annotationsScanner.adapteeFor(adapterClass);
        if (adapteeMethod == null) {
            log.printMessage(Diagnostic.Kind.ERROR,
                    "Adapter '" + adapterClass + "' must contain exactly one of adaptee provider method");
            return;
        }

        final TypeMirror adaptee = AdapterUtils.isVoid(adapter.adaptee())
                ? adapteeMethod.getReturnType() : adapter.adaptee();

        final DefaultAdapterBuilder builder = new DefaultAdapterBuilder(
                elementUtils, typeUtils, annotationsScanner, adapterClass, adaptee, adapteeMethod
        ).declareMethodsOfAdaptee();

        final JavaFileObject jfo = filer.createSourceFile(builder.fullClassName());
        try (final Writer writer = jfo.openWriter()) {
            builder.appendTo(writer).flush();
        }
    }
}
