package org.argouml.model.uml;

import org.argouml.model.uml.foundation.core.*;
import org.argouml.model.uml.foundation.datatypes.*;
import org.argouml.model.uml.foundation.extensionmechanisms.*;
import org.argouml.model.uml.behavioralelements.commonbehavior.*;
import org.argouml.model.uml.behavioralelements.usecases.*;
import org.argouml.model.uml.behavioralelements.statemachines.*;
import org.argouml.model.uml.behavioralelements.collaborations.*;
import org.argouml.model.uml.behavioralelements.activitygraphs.*;
import org.argouml.model.uml.modelmanagement.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.parsers.ParserConfigurationException;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.xmi.XMIReader;
import ru.novosoft.uml.xmi.XMIWriter;

import org.apache.log4j.Category;

import org.xml.sax.SAXException;

public class UmlFactory extends AbstractModelFactory {

    private static UmlFactory SINGLETON =
                   new UmlFactory();
    protected Category logger = null;

    public static UmlFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation.
     *  Create a logger.
     */
    private UmlFactory() {
        logger =Category.getInstance("org.argouml.model.uml.factory");
    }

    public ExtensionMechanismsFactory getExtensionMechanisms() {
        return ExtensionMechanismsFactory.getFactory();
    }

    public DataTypesFactory getDataTypes() {
        return DataTypesFactory.getFactory();
    }

    public CoreFactory getCore() {
        return CoreFactory.getFactory();
    }

    public CommonBehaviorFactory getCommonBehavior() {
        return CommonBehaviorFactory.getFactory();
    }

    public UseCasesFactory getUseCases() {
        return UseCasesFactory.getFactory();
    }

    public StateMachinesFactory getStateMachines() {
        return StateMachinesFactory.getFactory();
    }

    public CollaborationsFactory getCollaborations() {
        return CollaborationsFactory.getFactory();
    }

    public ActivityGraphsFactory getActivityGraphs() {
        return ActivityGraphsFactory.getFactory();
    }

    public ModelManagementFactory getModelManagement() {
        return ModelManagementFactory.getFactory();
    }

    public XMIReader getXMIReader() 
    throws SAXException,
           ParserConfigurationException {
        XMIReader reader = new XMIReader();
	return reader;
    }

    public XMIWriter getXMIWriter(MModel model, String filename)
    throws IOException {
        XMIWriter xmiWriter = new XMIWriter(model, filename);
	return xmiWriter;
    }

    public XMIWriter getXMIWriter(MModel model, String filename, String encoding)
    throws IOException {
        XMIWriter xmiWriter = new XMIWriter(model, filename, encoding);
	return xmiWriter;
    }

    public XMIWriter getXMIWriter(MModel model, Writer writer)
    throws IOException {
        XMIWriter xmiWriter = new XMIWriter(model, writer);
	return xmiWriter;
    }


    public void addListenersToModel(MModel model) {
	addListenersToMBase(model);
    }

    protected void addListenersToMBase(MBase mbase) {
        logger.debug ("Adding listener to: " + mbase);
        mbase.addMElementListener(ModelListener.getInstance());
	Collection elements = mbase.getModelElementContents();
	if (elements != null) {
	    Iterator iterator = elements.iterator();
	    while(iterator.hasNext()) {
	        Object o = iterator.next();
	        if (o instanceof MBase) {
	            addListenersToMBase((MBase)o);
	        }
	    }
	}
    }

}

