// $Id$
// Copyright (c) 2004-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import junit.framework.TestCase;
import org.argouml.model.InitializeModel;
import java.io.File;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.reveng.ImportInterface;

/**
 * Test SourcePathController implementation.
 * @author euluis
 * @since 0.17.3
 * @see SourcePathController
 */
public class TestSourcePathController extends TestCase {

    /** the model to use during testing */
    private Object model;
    
    /** A class model element... */
    private Object modelElem;
    /** its source path */
    private String modelElemSourcePath;
    /** and the corresponding file */
    private File modelElemSourcePathFile;
    
    /** A class model element without a source path defined */
    private Object modelElemWithoutSrcPath;
    
    /** Number of model elements in the setup model with source path set. */
    private int numOfMEsWithSrcPath;
    
    /** The SourcePathController instance. */
    private SourcePathController srcCtrl;

    /**
     * Creates a new instance of TestSourcePathController.
     * 
     * @param arg0 name of the test case
     */
    public TestSourcePathController(String arg0) {
        super(arg0);
        InitializeModel.initializeDefault();
    }
    
    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() {
        modelElemSourcePath = "ArgoUML/issues2579/src";
        // setup the Project due to its use in SourcePathTableModel ctor
        ProjectManager.getManager().makeEmptyProject();
        model = ProjectManager.getManager().getCurrentProject().getRoot();
        Object taggedValue = Model.getExtensionMechanismsFactory()
                .buildTaggedValue(ImportInterface.SOURCE_PATH_TAG,
                        modelElemSourcePath);
        Model.getExtensionMechanismsHelper().addTaggedValue(model, taggedValue);
        
        // create a class model element
        modelElem = Model.getCoreFactory().buildClass("AClass", model);
        taggedValue = Model.getExtensionMechanismsFactory().buildTaggedValue(
                ImportInterface.SOURCE_PATH_TAG, modelElemSourcePath);
        Model.getExtensionMechanismsHelper().addTaggedValue(
                modelElem, taggedValue);
        modelElemSourcePathFile = new File(modelElemSourcePath);
        
        modelElemWithoutSrcPath = Model.getCoreFactory().buildClass(
                "AClassWithoutSrcPath", model);
        srcCtrl = new SourcePathControllerImpl();
        
        // REMEMBER to change this if you change the number of MEs with source 
        // path settings
        numOfMEsWithSrcPath = 2;
    }
    
    /** 
     * Test the SourcePathController creation.
     */
    public void testCreation() {
        SourcePathController sourcePathCtrl = new SourcePathControllerImpl();
        assertNotNull(sourcePathCtrl);
    }
    
    /** 
     * Test retrieving the source path of a specific element.
     */
    public void testSimpleGetSourcePath() {
        File srcPath = srcCtrl.getSourcePath(modelElem);
        assertEquals(modelElemSourcePathFile, srcPath);
    }
    
    /** 
     * Test getting the source path for a model element without it.
     */
    public void testGetSourcePath4MEWithoutSourcePath() {
        File srcPath = srcCtrl.getSourcePath(modelElemWithoutSrcPath);
        assertNull(srcPath);
    }
    
    /** 
     * Test retrieving all source path settings in the model.
     */
    public void testGetSourcePathSettings() {
        SourcePathTableModel srcPathSets = srcCtrl.getSourcePathSettings();
        assertEquals(numOfMEsWithSrcPath, srcPathSets.getRowCount());
        for (int i = 0; i < srcPathSets.getRowCount(); i++) {
            assertEquals(modelElemSourcePathFile.toString(), 
                (String) srcPathSets.getValueAt(i, 
                    SourcePathTableModel.SOURCE_PATH_COLUMN));
        }
    }
    
    /** 
     * Test deletion of the source path settings for a model element.
     */
    public void testDeleteSourcePathSettings() {
        srcCtrl.deleteSourcePath(modelElem);
        assertNull(srcCtrl.getSourcePath(modelElem));
    }
    
    /**
     * Test setting the source path of a model element.
     */
    public void testSetSourcePath() {
        assertEquals(modelElemSourcePathFile, 
            srcCtrl.getSourcePath(modelElem));
        File newSrcPath = new File("../new/source/path");
        srcCtrl.setSourcePath(modelElem, newSrcPath);
        assertEquals(newSrcPath, srcCtrl.getSourcePath(modelElem));
    }
    
    /**
     * Test setting the source path according to the contents of Table model.
     */
    public void testSetSourcePathSettings() {
        SourcePathTableModel srcPathSets = srcCtrl.getSourcePathSettings();
        assertEquals(numOfMEsWithSrcPath, srcPathSets.getRowCount());
        File newSrcPath = new File("../new/source/path");
        for (int i = 0; i < srcPathSets.getRowCount(); i++) {
            srcPathSets.setValueAt(newSrcPath.toString(), i, 
                SourcePathTableModel.SOURCE_PATH_COLUMN);
        }
        srcCtrl.setSourcePath(srcPathSets);
        assertEquals(newSrcPath, srcCtrl.getSourcePath(modelElem));
        assertEquals(newSrcPath, srcCtrl.getSourcePath(model));
    }
}
