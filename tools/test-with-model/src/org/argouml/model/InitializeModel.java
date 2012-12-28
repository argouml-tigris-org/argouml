/* $Id$
 *****************************************************************************
 * Copyright (c) 2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *    Linus Tolke
 *****************************************************************************
 *
 * This file was copied from the tests of argouml-app.
 */

package org.argouml.model;

import junit.framework.TestCase;

/**
 * Class to initialize the model.
 *
 * @author lito
 */
public final class InitializeModel {
    
    /**
     * The default model implementation to start.
     */
    private static final String DEFAULT_MODEL_IMPLEMENTATION =
        "org.argouml.model.mdr.MDRModelImplementation";


    /**
     * This is never instantiated.
     */
    private InitializeModel() {
    }
    
    /**
     * Initialize the Model subsystem with the default ModelImplementation.
     */
    public static void initializeDefault() {
    	if (Model.isInitiated()) {
    	    return;
    	}
        String className =
            System.getProperty(
                    "argouml.model.implementation",
                    DEFAULT_MODEL_IMPLEMENTATION);
        initializeModelImplementation(className);
    }

    /**
     * Initialize the Model subsystem with the MDR ModelImplementation.
     */
    public static void initializeMDR() {
        initializeModelImplementation(
                "org.argouml.model.mdr.MDRModelImplementation");
    }

    /**
     * Initialize the Model subsystem with the Mock Implementation.
     *
     * @return the newly created MockModelImplementation.
     */
    public static MockModelImplementation initializeMock() {
        return (MockModelImplementation)
            initializeModelImplementation(
                "org.argouml.model.MockModelImplementation");
    }

    private static ModelImplementation initializeModelImplementation(
            String name) {
        ModelImplementation impl = null;

        Class implType;
        try {
            implType =
                Class.forName(name);
        } catch (ClassNotFoundException e) {
            TestCase.fail(e.toString());
            return null;
        }

        try {
            impl = (ModelImplementation) implType.newInstance();
        } catch (InstantiationException e) {
            TestCase.fail(e.toString());
        } catch (IllegalAccessException e) {
            TestCase.fail(e.toString());
        }
        Model.setImplementation(impl);
        return impl;
    }
}
