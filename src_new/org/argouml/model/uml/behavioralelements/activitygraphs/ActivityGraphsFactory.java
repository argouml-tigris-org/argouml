package org.argouml.model.uml.behavioralelements.activitygraphs;

import org.argouml.model.uml.AbstractModelFactory;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.behavior.activity_graphs.MActionState;
import ru.novosoft.uml.behavior.activity_graphs.MActivityGraph;

public class ActivityGraphsFactory extends AbstractModelFactory {
    private static ActivityGraphsFactory SINGLETON =
                   new ActivityGraphsFactory();

    public static ActivityGraphsFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private ActivityGraphsFactory() {
    }

    public MActionState createActionState() {
        // Line split to hide from ant replace
        MActionState modelElement = MFactory.getDefaultFactory().
	       createActionState();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MActivityGraph createActivityGraph() {
        // Line split to hide from ant replace
        MActivityGraph modelElement = MFactory.getDefaultFactory().
	       createActivityGraph();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

}

