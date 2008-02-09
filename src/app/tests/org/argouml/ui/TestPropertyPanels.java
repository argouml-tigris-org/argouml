// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.cognitive.checklist.ui.InitCheckListUI;
import org.argouml.cognitive.ui.InitCognitiveUI;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.notation.InitNotation;
import org.argouml.notation.providers.java.InitNotationJava;
import org.argouml.notation.providers.uml.InitNotationUml;
import org.argouml.persistence.AbstractFilePersister;
import org.argouml.persistence.PersistenceManager;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.uml.diagram.ui.InitDiagramAppearanceUI;
import org.argouml.uml.ui.InitUmlUI;
import org.argouml.uml.ui.TabProps;
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
        "/testmodels/uml14/GUITestPropertyPanels.zargo";

    private static Project p = null;
    private Object modelElement;

    /**
     * For an explanation on why this is static.
     *
     * @see #setUp
     */
    private static DetailsPane theDetailsPane = null;
    // private JPanel propertyPane;

    /**
     * @param me is the type of object to test
     * @param arg0 is the name of the test case
     */
    public TestPropertyPanels(Object me, String arg0) {
        super(arg0);
        modelElement = me;
    }

    /*
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
        (new InitNotation()).init();
        (new InitNotationUml()).init();
        (new InitNotationJava()).init();
        (new InitDiagramAppearanceUI()).init();

        if (theDetailsPane == null) {
            theDetailsPane =
        	new DetailsPane("South", Horizontal.getInstance());
            for (AbstractArgoJPanel tab : new InitUmlUI().getDetailsTabs()) {
                theDetailsPane.addTab(tab, false);
            }
            for (AbstractArgoJPanel tab : new InitCheckListUI().getDetailsTabs()) {
                theDetailsPane.addTab(tab, false);
            }        
            for (AbstractArgoJPanel tab : new InitCognitiveUI().getDetailsTabs()) {
                theDetailsPane.addTab(tab, true);
            }
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
        InitializeModel.initializeDefault();
        new InitNotation().init();
        (new InitNotationUml()).init();
        (new InitNotationJava()).init();
        (new InitProfileSubsystem()).init();


        // constains instances of each modelelement
        // used for testing so that we only use each modelelement
        // once
        HashMap<Class, Object> meMap = new HashMap<Class, Object>();

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

        Iterator meIter = me.iterator();

        while (meIter.hasNext()) {
            Object obj = meIter.next();
            if (Model.getFacade().isAUMLElement(obj)) {
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



    /*
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

        // propertyPane = /*TabProps */
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
