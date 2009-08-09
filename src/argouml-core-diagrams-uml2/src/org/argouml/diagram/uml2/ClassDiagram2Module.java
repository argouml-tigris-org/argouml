package org.argouml.diagram.uml2;


import org.apache.log4j.Logger;
import org.argouml.moduleloader.ModuleInterface;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.DiagramFactoryInterface2;
import org.argouml.uml.diagram.DiagramFactory.DiagramType;
import org.argouml.uml.ui.PropPanelFactoryManager;

/**
 * The Class Diagram Module description.
 *
 * @see org.argouml.moduleloader.ModuleInterface
 */
public class ClassDiagram2Module implements ModuleInterface {

    private static final Logger LOG =
        Logger.getLogger(ClassDiagram2Module.class);

    private ClassDiagram2PropPanelFactory propPanelFactory;
    
    public boolean enable() {
        
        propPanelFactory =
            new ClassDiagram2PropPanelFactory();
        PropPanelFactoryManager.addPropPanelFactory(propPanelFactory);
        // TODO: Remove the casting to DiagramFactoryInterface2
        // as soon as DiagramFactoryInterface is removed.
        DiagramFactory.getInstance().registerDiagramFactory(
                DiagramType.Class, 
                (DiagramFactoryInterface2) new ClassDiagram2Factory());

        LOG.info("ClassDiagram Module enabled.");
        return true;
    }

    public boolean disable() {

        PropPanelFactoryManager.removePropPanelFactory(propPanelFactory);

        // TODO: Remove the casting to DiagramFactoryInterface2
        // as soon as DiagramFactoryInterface is removed.
        DiagramFactory.getInstance().registerDiagramFactory(
                DiagramType.Class, (DiagramFactoryInterface2) null);

        LOG.info("ClassDiagram Module disabled.");
        return true;
    }
    
    public String getName() {
        return "ArgoUML-Class";
    }

    public String getInfo(int type) {
        switch (type) {
        case DESCRIPTION:
            return "The new class diagram implementation";
        case AUTHOR:
            return "ArgoUML Core Development Team";
        case VERSION:
            return "0.28";
        case DOWNLOADSITE:
            return "http://argouml.tigris.org";
        default:
            return null;
        }
    }
}