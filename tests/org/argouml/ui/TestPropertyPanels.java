// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.ui;

// import com.sun.image.codec.jpeg.JPEGCodec;
// import com.sun.image.codec.jpeg.JPEGEncodeParam;
// import com.sun.image.codec.jpeg.JPEGImageEncoder;

// import java.awt.image.BufferedImage;
// import java.awt.Component;
// import java.awt.Dimension;
// import java.awt.Image;
// import java.awt.Graphics2D;
import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JPanel;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.persistence.AbstractFilePersister;
import org.argouml.persistence.PersistenceManager;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.uml.cognitive.critics.ChildGenUML;
import org.argouml.uml.ui.TabProps;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.util.ChildGenerator;
import org.tigris.gef.util.EnumerationComposite;
import org.tigris.gef.util.EnumerationEmpty;
import org.tigris.gef.util.EnumerationSingle;
import org.tigris.swidgets.Horizontal;

/**
 * TestPropertyPanels attempts to load a project file and iterates through
 * all known modelelements of this project. For each modelelement it creates
 * a test case, which tries to invoke the according property panel.
 * This test implements a simple verification that creation of property panels
 * is at least exception free. It does not provide a test for the functionality
 * of the respective modelelements.
 *
 * @author mkl
 */
public class TestPropertyPanels extends TestCase {

    /**
     * Name of the zargo file to read.
     */
    private static final String TEST_PROPERTY_PANELS_ZARGO =
        "/testmodels/GUITestPropertyPanels.zargo";

    private static Project p = null;
    private Object modelElement;

    /**
     * For an explanation on why this is static.
     *
     * @see #setUp
     */
    private static DetailsPane theDetailsPane = null;
    private JPanel propertyPane;

    // we need the translator to work in order to access
    // the property panels. It is also a common source for
    // problems.
    static {
        Translator.init();
    }


    /**
     * @param me is the type of object to test
     * @param arg0 is the name of the test case
     */
    public TestPropertyPanels(Object me, String arg0) {
        super(arg0);
        modelElement = me;
    }

    /**
     * Here we are actually violating the test independance since we keep
     * the DetailsPane from test to test. The reason to do this is to make
     * it possible to run the tests with less memory requirements.
     *
     * Hopefully someone might eventually fix the DetailsPane so that it is
     * garbage collected properly and this is no longer needed.
     *
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        if (theDetailsPane == null) {
            theDetailsPane =
        	new DetailsPane("detail", Horizontal.getInstance());
        }
    }

    /**
     * @param args the arguments given on the commandline
     */
    public static void main(java.lang.String[] args) {
        try {
            junit.textui.TestRunner.run(suite());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @return the test suite
     * @throws Exception any exception
     */
    public static Test suite() throws Exception {

        // constains instances of each modelelement
        // used for testing so that we only use each modelelement
        // once
        HashMap meMap = new HashMap();
        ChildGenerator cg = new ChildGenModelElements();

        TestSuite suite =
	    new TestSuite("Tests to access proppanels "
			  + "for all known model elements");

        p = ProjectManager.getManager().makeEmptyProject();
        URL url =
            TestPropertyPanels.class.getResource(
                        TEST_PROPERTY_PANELS_ZARGO);

        if (url == null) {
            System.out.println(TestPropertyPanels.class.getName()
                    + ": WARNING: Inconclusive tests.");
            System.out.println("This test must be able to"
                    + " find the resource " + TEST_PROPERTY_PANELS_ZARGO
                    + " on the classpath.");
            System.out.println("Examine your set up and try again!");
            return suite;
        }

        File testfile = new File(url.getFile());

        AbstractFilePersister persister =
            PersistenceManager.getInstance().getPersisterFromFileName(
                TEST_PROPERTY_PANELS_ZARGO);
        p = persister.doLoad(testfile);
        ProjectManager.getManager().setCurrentProject(p);
        Object model = p.getRoot();
        Collection me =
            Model.getModelManagementHelper()
            	.getAllModelElementsOfKind(
            	        model,
            	        Model.getMetaTypes().getModelElement());

        Enumeration meEnum = getAllModelElements(p);

        while (meEnum.hasMoreElements()) {
            Object obj = meEnum.nextElement();
            if (Model.getFacade().isAModelElement(obj)) {
                if (!meMap.containsKey(obj.getClass())) {
                    suite.addTest(new TestPropertyPanels(
			    obj,
			    "PropPanel"
			    + Model.getFacade().getUMLClassName(obj)));
                    meMap.put(obj.getClass(), obj);
                }
            }
        }

        return suite;
    }

    private static Enumeration getAllModelElements(Object me) {
        Enumeration elem = new EnumerationComposite();
        return getAllModelElements(me, elem);
    }

    private static Enumeration getAllModelElements(Object me,
						   Enumeration elem) {
        ChildGenUML cg = new ChildGenUML();

        elem = new EnumerationComposite(elem, new EnumerationSingle(me));
        Enumeration elem2 = cg.gen(me);
        if (elem2 == EnumerationEmpty.theInstance()) {
            return elem;
        }
        while (elem2.hasMoreElements()) {
            Object newMe = elem2.nextElement();
            elem = getAllModelElements(newMe, elem);
        }
        return elem;
    }


    /**
     * @see junit.framework.TestCase#runTest()
     */
    protected void runTest() throws Throwable {
        testPropertyTab();
    }

    /**
     * @throws Throwable any error or exception
     */
    public void testPropertyTab() throws Throwable {
        TargetEvent e =
	    new TargetEvent(this,
			    TargetEvent.TARGET_SET,
			    new Object[] {
				null,
			    },
			    new Object[] {
				modelElement,
			    });
        theDetailsPane.targetSet(e);

        propertyPane = /*TabProps */
            theDetailsPane.getTab(TabProps.class);
//            theDetailsPane.getNamedTab(Translator.localize("tab.properties"));

        // currently this is in this try block as it does not work
        // _propertyPanel always has size 0,0
    /*
          try {
            saveImageAsJPEG((BufferedImage)createImageFromComponent(
                _propertyPane),
                1000000, "/Users/mkl/argoimg/"+this.getName() + ".jpg");
        }
        catch (Exception ex) {
           // System.out.println(ex);
        }
         */
    }
    /*
    public static Image createImageFromComponent(Component comp) {
        BufferedImage image = new BufferedImage(comp.getWidth(),
        comp.getHeight(), BufferedImage.TYPE_INT_BGR);
        Graphics2D g = image.createGraphics();
        comp.paint(g);
        return image;
    }


    public static void saveImageAsJPEG(BufferedImage bi, float quality,
            String filename) {
        try {
            ByteArrayOutputStream boutstream = new ByteArrayOutputStream();
            JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(boutstream);
            JPEGEncodeParam enparam = JPEGCodec.getDefaultJPEGEncodeParam(bi);
            enparam.setQuality(quality, true );
            enc.encode(bi, enparam);
            FileOutputStream fimage = new FileOutputStream( new File(filename));
            boutstream.writeTo(fimage);
            fimage.close();
        }
        catch (Exception e) {
            System.out.println(e); }
    }
    */
}

class ChildGenModelElements implements ChildGenerator {


    public Enumeration gen(Object o) {

        if (o instanceof Project) {
            Project p = (Project) o;
            return new EnumerationComposite(p.getUserDefinedModels().elements(),
                    p.getDiagrams().elements());
        }

        if (o instanceof Diagram) {
            Collection figs = ((Diagram) o).getLayer().getContents();
            if (figs != null) {
                return new Vector(figs).elements();
            }
        }

        if (!Model.getFacade().isAModelElement(o)) {
            return EnumerationEmpty.theInstance();
        }

        EnumerationComposite res =
	    new EnumerationComposite(new EnumerationSingle(o));


        // now we deal only with modelelements
        if (Model.getFacade().getBehaviors(o) != null) {
            Vector beh = new Vector(Model.getFacade().getBehaviors(o));
            res.addSub(beh.elements());
        }

        if (Model.getFacade().isANamespace(o)) {
            if (Model.getFacade().getOwnedElements(o) != null) {
                Vector own = new Vector(Model.getFacade().getOwnedElements(o));
                res.addSub(own.elements());
            }
        }

        if (Model.getFacade().isAClassifier(o)) {
            if (Model.getFacade().getFeatures(o) != null) {
                Vector own = new Vector(Model.getFacade().getFeatures(o));
                res.addSub(own.elements());
            }
        }

        if (Model.getFacade().isABehavioralFeature(o)) {
            if (Model.getFacade().getParameters(o) != null) {
                Vector params = new Vector(Model.getFacade().getParameters(o));
                res.addSub(params.elements());
            }
        }

        if (Model.getFacade().isAAssociation(o)) {
            if (Model.getFacade().getConnections(o) != null) {
                Vector assocEnds =
                    new Vector(Model.getFacade().getConnections(o));
                res.addSub(assocEnds.elements());
            }
            //TODO: MAssociationRole
        }

        if (Model.getFacade().isAElementImport(o)) {
            Object me = Model.getFacade().getModelElement(o);
            res.addSub(new EnumerationSingle(me));
        }

        if (Model.getFacade().isACompositeState(o)) {
            Vector substates = new Vector(Model.getFacade().getSubvertices(o));
            if (substates != null) {
                res.addSub(substates.elements());
            }
        }

        if (Model.getFacade().isAStateMachine(o)) {
            EnumerationComposite res2 = new EnumerationComposite();
            Object top = Model.getStateMachinesHelper().getTop(o);
            if (top != null) {
                res2.addSub(new EnumerationSingle(top));
            }
            res2.addSub(new Vector(Model.getFacade().getTransitions(o)));
            res.addSub(res2);
        }

        // if (Model.getFacade().isATransition(o)) {
        ///   Vector action = new Vector(Model.getFacade().getAction(o));
        //if (action != null) res.addSub(action.elements());
        //}

        if (Model.getFacade().isANode(o)) {
            Vector substates = new Vector(Model.getFacade().getResidents(o));
            if (substates != null) {
                res.addSub(substates.elements());
            }
        }




        return res;

    }


}