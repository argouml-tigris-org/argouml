// Copyright (c) 1996-01 The Regents of the University of California. All
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

import java.beans.*;
import org.tigris.gef.base.*;
import org.tigris.gef.graph.*;
import org.tigris.gef.presentation.*;
import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.uml.diagram.static_structure.ui.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;


/**
 * Instances of this class interface the current diagram.
 *
 * @author <a href="mailto:a_rueckert@gmx.net">Andreas Rueckert</a>
 * @version 1.0
 * @since 1.0
 */
public class DiagramInterface {
    
    Editor _currentEditor = null;

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
     * Add a package to the current diagram.
     *
     * @param newPackage The package to add.
     */
    public void addPackage(MPackage newPackage) {
	GraphModel gm            = getEditor().getGraphModel();
	Layer      lay           = getEditor().getLayerManager().getActiveLayer(); 
	FigPackage newPackageFig = new FigPackage( gm, newPackage); 

	getEditor().add( newPackageFig);
	getEditor().damaged( newPackageFig);
    }


    /**
     * Create a diagram name from a package name
     *
     * @param packageName The package name.
     * @return The name for the diagram.
     */
    private String getDiagramName(String packageName) {
	return packageName.replace('.','_') + "_classes";
    }

    /**
     * Create a diagram name for a package
     *
     * @param p The package.
     * @return The name for the diagram.
     */
    private String getDiagramName(MPackage p) {
	return getDiagramName(p.getName());
    }

    /**
     * Select or create a class diagram for a package.
     *
     * @param p The package.
     * @param name The fully qualified name of this package.
     */
    public void selectClassDiagram(MPackage p, String name) {

	// Check if this diagram already exists in the project
	ProjectMember m;
	if((m = ProjectBrowser.TheInstance.getProject().findMemberByName( getDiagramName(name) + ".pgml")) != null) {
	    ProjectBrowser.TheInstance.setTarget(m);  // Select the existing diagram.
	} else {  // Otherwise
	    addClassDiagram(p, name);  // create a new classdiagram for the package.
	}
    }

    /**
     * Add a new class diagram for a package to the project.
     *
     * @param target The package to attach the diagram to.
     * @param name The fully qualified name of the package, which is
     *             used to generate the diagram name from.
     */
    public void addClassDiagram(MPackage target, String name) {
	Project p = ProjectBrowser.TheInstance.getProject();
	MNamespace ns = (MNamespace) target;
	try {
	    Diagram d = new UMLClassDiagram(ns);
	    d.setName(getDiagramName(name));
	    p.addMember(d);
	    ProjectBrowser.TheInstance.getNavPane().addToHistory(d);
	    ProjectBrowser.TheInstance.setTarget(d);
	}
	catch (PropertyVetoException pve) { }
    }
    
    /**
     * Add a class to the current diagram.
     *
     * @param newClass The new class to add to the editor.
     */
    public void addClass(MClass newClass) {
	GraphModel gm = getEditor().getGraphModel();

	if ( !( gm instanceof MutableGraphModel)) {
	    System.err.println( "DiagramInterface.createClass(): Graphmodel is not mutable!");
	} else {
	    MutableGraphModel mgm         = (MutableGraphModel) gm;
	    LayerPerspective  lay         = (LayerPerspective)getEditor().getLayerManager().getActiveLayer(); 
	    FigClass          newClassFig = new FigClass( gm, (Object)newClass); 
	    if ( mgm.canAddNode( newClass)) {
		getEditor().add( newClassFig);
		lay.putInPosition( (Fig)newClassFig);
		getEditor().damaged( newClassFig);
	    } else
		System.err.println( "Cannot add fig " + newClass.getName() + " to the current editor");
	}
    }

    /**
     * Add a interface to the current diagram.
     *
     * @param newInterface The interface to add.
     */
    public void addInterface(MInterface newInterface) {
	GraphModel       gm              = getEditor().getGraphModel();
	LayerPerspective lay             = (LayerPerspective)getEditor().getLayerManager().getActiveLayer(); 
	FigInterface     newInterfaceFig = new FigInterface( gm, newInterface); 

	getEditor().add( newInterfaceFig);
	lay.putInPosition( (Fig)newInterfaceFig);
	getEditor().damaged( newInterfaceFig);		
    }
}

























