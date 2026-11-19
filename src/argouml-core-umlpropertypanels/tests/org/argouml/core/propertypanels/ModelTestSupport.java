package org.argouml.core.propertypanels;

import org.argouml.model.Model;
import org.argouml.model.ModelImplementation;

public final class ModelTestSupport {

    private static final String MDR_IMPLEMENTATION =
        "org.argouml.model.mdr.MDRModelImplementation";

    private ModelTestSupport() {
    }

    public static synchronized void initializeModel() {
        if (Model.isInitiated()) {
            return;
        }
        try {
            Class<?> type = Class.forName(MDR_IMPLEMENTATION);
            ModelImplementation impl =
                (ModelImplementation) type.getDeclaredConstructor().newInstance();
            Model.setImplementation(impl);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to initialize " + MDR_IMPLEMENTATION, e);
        }
    }
}
