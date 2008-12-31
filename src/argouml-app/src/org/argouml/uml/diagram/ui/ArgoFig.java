// $Id$
// Copyright (c) 2007-2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.diagram.ui;

import java.awt.Color;

import org.argouml.kernel.Project;
import org.argouml.uml.diagram.DiagramSettings;

/**
 * An interface that all ArgoUML Figs are required to interface. It provides a
 * single place to specify behaviors that we want all Figs to have since we
 * don't have access to the GEF class hierarchy (and it is made up of concrete
 * classes instead of interfaces.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 * @since 0.25.4
 */
public interface ArgoFig {

    // TODO: These have been used for most instances of new FigFoo(X0, Y0, ...
    // but additional work is required to extract the constant from
    // calculations, etc.
    
    /** Default X offset of origin used when building figs */
    static final int X0 = 10;

    /** Default Y offset of origin used when building figs */
    static final int Y0 = 10;

    /**
     * min. 17, used to calculate y pos of FigText items in a compartment
     */
    public static final int ROWHEIGHT = 17;
    /**
     * min. 18, used to calculate y pos of stereotype FigText items
     * in a compartment
     */
    public static final int STEREOHEIGHT = 18;

    /**
     * Default line width in pixels.
     */
    static final int LINE_WIDTH = 1;

    /**
     * Default color for lines.  This constant is an interim measure to get
     * rid of all the Color.black references before moving to settable defaults.
     */
    static final Color LINE_COLOR = Color.black;
    
    /**
     * Default color for filled figures. This constant is an interim measure to
     * get rid of all the Color.white references before moving to settable
     * defaults.
     */
    static final Color FILL_COLOR = Color.white;
    
    /**
     * Default color for text.  This constant is an interim measure to get
     * rid of all the Color.black references before moving to settable defaults.
     */
    static final Color TEXT_COLOR = Color.black;
    
    /**
     * The color used for things which shouldn't normally be visible, so we can
     * spot them easily when they are.
     */
    static final Color DEBUG_COLOR = Color.cyan;


    /**
     * Set the owning project for this Fig. This is an optional operation which
     * may throw an {@link UnsupportedOperationException} if not implemented.
     * 
     * @param project the project
     * @deprecated for 0.27.2 by tfmorris. This optional method has never been
     *             implemented by any concrete class that implements this
     *             interface and should not be used. Project ownership is
     *             maintained at a coarser granularity level.
     */
    @Deprecated
    public void setProject(Project project);

    /**
     * Get the owning project for this fig.
     * 
     * @return the owning project
     * @deprecated for 0.27.2 by tfmorris. Implementations should have all the
     *             information that they require in the DiagramSettings object.
     */
    @Deprecated
    public Project getProject();
    
    /**
     * Rerender the entire fig.
     * <p>
     * This may be an expensive operation for subclasses which are complex,
     * so should be used sparingly.  It is only intended to be used when 
     * some global change to the rendering defaults is made at the ArgoDiagram
     * level.
     */
    public void renderingChanged();

    /**
     * @return the rendering settings for the Fig
     */
    public DiagramSettings getSettings();
    
    /**
     * Set the rendering settings to be used for this fig. Currently this
     * normally will be a diagram-wide or project-wide settings object that is
     * shared by all Figs.
     * 
     * @param settings the rendering settings to use
     */
    public void setSettings(DiagramSettings settings);
    
    /**
     * Setting the owner of the Fig must be done in the constructor and
     * not changed aftewards for all ArgoUML figs.
     * 
     * @param owner owning UML element
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     * @deprecated for 0.27.3 by tfmorris.  Set owner in constructor.
     */
    @Deprecated
    public void setOwner(Object owner);
    
}
