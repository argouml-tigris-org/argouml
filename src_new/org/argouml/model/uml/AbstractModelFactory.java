package org.argouml.model.uml;

import ru.novosoft.uml.MBase;

import org.apache.log4j.Category;

public abstract class AbstractModelFactory {

    public static Category logger =
                  Category.getInstance("org.argouml.model.uml");

    protected AbstractModelFactory() {
    }

    protected void addListener(MBase mbase) {
        mbase.addMElementListener(ModelListener.getInstance());
    }


    protected void postprocess(Object o) {
        logger.debug("postprocess(" + o + ")");
    }

}

