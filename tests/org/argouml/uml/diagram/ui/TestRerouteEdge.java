package org.argouml.uml.diagram.ui;


import java.awt.Point;
import java.awt.event.MouseEvent;

import junit.framework.TestCase;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.argouml.uml.reveng.DiagramInterface;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.LayerPerspective;

import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.model_management.MPackage;

/**
 * A JUnit testcase that tests whether, having dragged an edge to
 * a different node the corresponding association end changes to the
 * correct type also.
 *
 * <p>NOTE: tests rerouting of associations only at the moment.
 *
 * @author  alexb
 */
public class TestRerouteEdge extends TestCase {
    
    /**
     * helps set up a class diagram programmatically.
     */
    DiagramInterface diagramInterface;
    
    /*
     * elements to test:
     */
    MClass class1;
        MClass class2;
        MClass class3;
        MAssociation assoc;
    
    public TestRerouteEdge(String arg0) {
        super(arg0);
    }
    
    /**
     * Creates a class diagram with three classes (a,b,c) and one association
     * (aTob) connecting a to b.
     */
    protected void setUp() throws Exception  {
        super.setUp();
        
        // create a project browser
        ArgoSecurityManager.getInstance().setAllowExit(true);
        ProjectManager.getManager().getCurrentProject();
        
        // create a DiagramInterface, that Java RE uses to create
        // diagrams.
        diagramInterface = new DiagramInterface(Globals.curEditor());
        
        // create a class diagram
        diagramInterface.addClassDiagram((MPackage)ProjectManager.getManager().getCurrentProject().getRoot(),"test-reroute");

        // create 3 classes and an association
        class1 = CoreFactory.getFactory().createClass();
        class2 = CoreFactory.getFactory().createClass();
        class3 = CoreFactory.getFactory().createClass();
        
        ProjectManager.getManager().getCurrentProject().getRoot().addOwnedElement(class1);
        ProjectManager.getManager().getCurrentProject().getRoot().addOwnedElement(class2);
        
        assoc = CoreFactory.getFactory().buildAssociation(class1,class2);
        ProjectManager.getManager().getCurrentProject().getRoot().addOwnedElement(class3);
        
        // add the classes to the diagram (the association will be added
        // automatically).
        diagramInterface.addClass(class1,false);
        diagramInterface.addClass(class2,false);
        diagramInterface.addClass(class3,false);
        
        // move the figs into position
        LayerPerspective lay =
        (LayerPerspective)Globals.curEditor().getLayerManager().getActiveLayer();
        
        FigClass figClass2 = (FigClass)lay.presentationFor(class2);
        FigClass figClass3 = (FigClass)lay.presentationFor(class3);
        FigClass figClass1 = (FigClass)lay.presentationFor(class3);
        
        figClass1.setBounds(100,100,100,100);
        figClass2.setBounds(300,100,100,100);
        figClass3.setBounds(300,300,100,100);
    }
    
    protected void tearDown() throws Exception {
        
        super.tearDown();
        UmlFactory.getFactory().delete(class1);
        UmlFactory.getFactory().delete(class2);
        UmlFactory.getFactory().delete(class3);
        UmlFactory.getFactory().delete(assoc);
        
    }
    
    /**
     * Test association rerouting.
     * <p>tests whether the type of the association end has been changed
     * correctly.
     * <p>does NOT test whether the Figs have been updated correctly.
     * couldn't make that work. may need to use AWTRobot?
     */
    public void testRerouteAssociation() {
        
        // now get the FigAssociation
        LayerPerspective lay =
        (LayerPerspective)Globals.curEditor().getLayerManager().getActiveLayer();
        FigAssociation figAssoc = (FigAssociation)lay.presentationFor(assoc);
        FigClass figClass2 = (FigClass)lay.presentationFor(class2);
        FigClass figClass3 = (FigClass)lay.presentationFor(class3);
        
        // make a selection
        SelectionRerouteEdge selection = (SelectionRerouteEdge)figAssoc.makeSelection();
        
        // get the co-rds of the first point on the edge:
        int[] xs = figAssoc.getXs();
        int[] ys = figAssoc.getYs();
        
        // simulate the user clicking on the edge
        MouseEvent me = 
        new MouseEvent(ProjectBrowser.TheInstance,
            1,1, 1,xs[0], ys[0], 1, false);
        selection.mousePressed(me);
        selection.mouseDragged(me);
        
        // get the co-ords of class 3
        Point location = figClass3.getLocation();
        
        // simulate the user releasing on class 3
        MouseEvent me2 = 
        new MouseEvent(ProjectBrowser.TheInstance, 1,1, 1,
            location.x+5,location.y+5, 1, false);
        selection.mouseReleased(me2);
        
        // ----- assert that the rerouting was correct: -----------
        // this is just a simple test.
        
        // the type of the rerouted association is now class3.
        MAssociationEnd ae0 =
        (MAssociationEnd)((Object[])(assoc.getConnections()).toArray())[0];
        
        MClassifier classifier = ae0.getType();
        
        assertTrue(classifier == class3);
    }
    
}
