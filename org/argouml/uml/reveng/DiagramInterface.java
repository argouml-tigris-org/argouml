// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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

package org.argouml.uml.reveng;

import java.beans.PropertyVetoException;
import java.util.Vector;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ProjectMember;
import org.argouml.ui.ArgoDiagram;
import org.argouml.uml.diagram.ProjectMemberDiagram;
import org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel;
import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.argouml.uml.diagram.static_structure.ui.FigInterface;
import org.argouml.uml.diagram.static_structure.ui.FigPackage;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.presentation.Fig;
import org.argouml.model.ModelFacade;

/**
 * Instances of this class interface the current Class diagram.
 * <p>
 * This class is used by the import mechanism to create packages,
 * interfaces and classes within the diagrams.
 * It is also used to find the correct diagram to work in.
 *
 * @author Andreas Rueckert
 * @since 0.9
 */
public class DiagramInterface {

    Editor _currentEditor = null;

    /** To know what diagrams we have to layout after the import,
     * we store them in this Vector.
     */
    Vector _modifiedDiagrams = new Vector();

    /**
     * the current GraphModel of the current classdiagram
     */
    ClassDiagramGraphModel currentGM;
    
    /**
     * the current Layer of the current classdiagram
     */
    LayerPerspective currentLayer;
    
    /**
     * the current diagram for the isInDiagram method
     */
    ArgoDiagram currentDiagram;
    
    /**
     * Creates a new DiagramInterface
     *
     * @param editor The editor to operate on.
     */
    public DiagramInterface(Editor editor) {
  	_currentEditor = editor;
    }

    /**
     * Get the current editor.
     *
     * @return The current editor.
     */
    Editor getEditor() {
	return _currentEditor;
    }

    /**
     * Mark a diagram as modified, so we can layout it, after the
     * import is complete.
     *
     * If the diagram is not already marked, add it to the list.
     *
     * @param diagram The diagram to mark as modified.
     */
    void markDiagramAsModified(Object diagram) {
	if (!_modifiedDiagrams.contains(diagram)) {
	    _modifiedDiagrams.add(diagram);       
	}
    }

    /**
     * Get the list of modified diagrams.
     *
     * @return The list of modified diagrams.
     */
    public Vector getModifiedDiagrams() {
	return _modifiedDiagrams;
    }

    /**
     * Reset the list of modified diagrams.
     */
    void resetModifiedDiagrams() {
	_modifiedDiagrams = new Vector();
    }

    /**
     * Add a package to the current diagram. If the package already has
     * a representation in the current diagram, it is not(!) added.
     *
     * @param newPackage The package to add.
     */
    public void addPackage(Object newPackage) {
        
        if (!isInDiagram(newPackage)) {
            
            FigPackage newPackageFig = new FigPackage( currentGM, newPackage);
            if (currentGM.canAddNode(newPackage)) {
                
                currentGM.addNode(newPackage);
                currentLayer.add(newPackageFig);
                currentLayer.putInPosition((Fig) newPackageFig);
            }
        }
    }

    /**
     * Check if a given package has a representation in the current
     * diagram.
     *
     * @param p The package to lookup in the current diagram.
     * @return true if this package has a figure in the current diagram,
     *         false otherwise.
     */
    public boolean isInDiagram(Object p) {
        
	if (currentDiagram == null)
            return false;
        else
            return currentDiagram.getNodes().contains(p);
    }
    
    /**
     *Check if this diagram already exists in the project.
     *@param diagram name
     *@return true if diagram exists in project
     */
    public boolean isDiagramInProject(String name) {
	return ((ProjectManager.getManager().getCurrentProject()
		 .findMemberByName( getDiagramName(name) + ".pgml")) != null);
    }

    /**
     * Create a diagram name from a package name
     *
     * @param packageName The package name.
     * @return The name for the diagram.
     */
    private String getDiagramName(String packageName) {
	return packageName.replace('.', '_') + "_classes";
    }

    /**
     * Create a diagram name for a package
     *
     * @param p The package.
     * @return The name for the diagram.
     */
    private String getDiagramName(Object p) {
	return getDiagramName(ModelFacade.getName(p));
    }

    /**
     * Select or create a class diagram for a package.
     *
     * @param p The package.
     * @param name The fully qualified name of this package.
     */
    public void selectClassDiagram(Object p, String name) {

	// Check if this diagram already exists in the project
	ProjectMember m;
	if ( isDiagramInProject(name) ) {
	    m = ProjectManager.getManager().getCurrentProject()
		.findMemberByName( getDiagramName(name) + ".pgml");

	    // The diagram already exists in this project. Select it
	    // as the current target.
	    if (m instanceof ProjectMemberDiagram) {
		setCurrentDiagram(((ProjectMemberDiagram) m).getDiagram());
	    }

	} else {
	    // Otherwise create a new classdiagram for the package.
	    addClassDiagram(p, name);
	}
    }

    /**
     * Add a new class diagram for a package to the project.
     *
     * @param target The package to attach the diagram to.
     * @param name The fully qualified name of the package, which is
     *             used to generate the diagram name from.
     */
    public void addClassDiagram(Object ns, String name) {
	Project p = ProjectManager.getManager().getCurrentProject();
	try {
	    ArgoDiagram d = new UMLClassDiagram(ns);
	    d.setName(getDiagramName(name));
            p.addMember(d);
	    setCurrentDiagram(d);
	}
	catch (PropertyVetoException pve) { }
    }

    /**
     * Add a class to the current diagram.
     *
     * @param newClass The new class to add to the editor.
     */
    public void addClass(Object newClass, boolean minimise) {
        
        FigClass newClassFig = new FigClass( currentGM, newClass);
        if (currentGM.canAddNode(newClass)) {
            currentLayer.add( newClassFig);
            currentGM.addNode(newClass);
            currentLayer.putInPosition( (Fig) newClassFig);
            currentGM.addNodeRelatedEdges( newClass);
            
            newClassFig.setAttributeVisible(!minimise);
            newClassFig.setOperationVisible(!minimise);
            
            newClassFig.setSize(newClassFig.getMinimumSize());
        }
    }

    /**
     * Add a interface to the current diagram.
     *
     * @param newInterface The interface to add.
     */
    public void addInterface(Object newInterface, boolean minimise) {
        
        FigInterface     newInterfaceFig =
	    new FigInterface( currentGM, newInterface);
        
        if (currentGM.canAddNode(newInterface)) {
            currentLayer.add( newInterfaceFig);
            currentGM.addNode(newInterface);
            currentLayer.putInPosition( (Fig) newInterfaceFig);
            currentGM.addNodeRelatedEdges( newInterface);
            
            newInterfaceFig.setOperationVisible(!minimise);
            
            newInterfaceFig.setSize(newInterfaceFig.getMinimumSize());
        }
    }

    /**
     * Creates new class diagram for package or selects existing one.
     * @param currentPackage  The package to attach the diagram to.
     * @param currentPackageName The fully qualified name of the
     * package, which is used to generate the diagram name from.
     */
    public void createOrSelectClassDiagram(Object currentPackage,
					   String currentPackageName)
    {
	Project p = ProjectManager.getManager().getCurrentProject();
	String diagramName = currentPackageName.replace('.', '_') + "_classes";
	UMLClassDiagram d =
	    new UMLClassDiagram(currentPackage == null
				? p.getRoot()
				: currentPackage);
	if (p.findMemberByName(diagramName + ".pgml") == null) {
	    try {
		d.setName(diagramName);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    p.addMember(d);
	    setCurrentDiagram(d);
	} else {
	    ArgoDiagram ddi =
		((ProjectMemberDiagram) p.findMemberByName(diagramName
							   + ".pgml"))
		.getDiagram();
	    setCurrentDiagram(ddi);
	}
    }
	
    /**
     * Creates class diagram under the root.
     * Is used for classes out of packages.
     *
     */
    public void createRootClassDiagram() {
	createOrSelectClassDiagram(null, "");
    }
        
    /**
     * selects a diagram without affecting the gui.
     */
    public void setCurrentDiagram(ArgoDiagram diagram) {
        
        if (diagram == null) {
            throw new RuntimeException("you can't select a null diagram");
        }
        
        currentGM = (ClassDiagramGraphModel) diagram.getGraphModel();
        currentLayer = diagram.getLayer();
        currentDiagram = diagram;
        
        markDiagramAsModified(diagram);
    }
}

























