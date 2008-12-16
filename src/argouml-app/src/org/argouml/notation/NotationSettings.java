// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.notation;

/**
 * Notation settings value object.  Stores settings which control how text is 
 * rendered on diagrams.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class NotationSettings {
    
    // TODO: This needs more complete initialization.  Everything defaults to
    // false for now.
    private static final NotationSettings DEFAULT_SETTINGS = 
        new NotationSettings();
    
    // TODO: If we want to support a hierarchy of inherited settings, we'll
    // need a link to the parent, but not clear this is needed right now
    // private NotationSettings parent;

    private boolean showAssociationName;
    
    private boolean showVisibility;
    
    private boolean showPath;
    
    private boolean fullyHandleStereotypes;
    
    private boolean showSingularMultiplicities;
    
    private boolean useGuillemets;
    
    // TODO: Do we need to control separately for attributes and operations?
    private boolean showTypes;
    
    private boolean showProperties;
    
    private boolean showInitialValues;
    
    private boolean showMultiplicities;
    
    /**
     * @return the default settings
     */
    public static NotationSettings getDefaultSettings() {
        return DEFAULT_SETTINGS;
    }
    
    /**
     * @return Returns the showAssociationName.
     */
    public boolean isShowAssociationName() {
        return showAssociationName;
    }
    
    /**
     * @param showAssociationName The showAssociationName to set.
     */
    public void setShowAssociationName(boolean showAssociationName) {
        this.showAssociationName = showAssociationName;
    }
    
    /**
     * @return Returns the showVisibility.
     */
    public boolean isShowVisibility() {
        return showVisibility;
    }
    
    /**
     * @param showVisibility The showVisibility to set.
     */
    public void setShowVisibility(boolean showVisibility) {
        this.showVisibility = showVisibility;
    }

    /**
     * @return Returns the showPath.
     */
    public boolean isShowPath() {
        return showPath;
    }

    /**
     * @param showPath The showPath to set.
     */
    public void setShowPath(boolean showPath) {
        this.showPath = showPath;
    }

    /**
     * @return Returns the fullyHandleStereotypes.
     */
    public boolean isFullyHandleStereotypes() {
        return fullyHandleStereotypes;
    }

    /**
     * @param fullyHandleStereotypes The fullyHandleStereotypes to set.
     */
    public void setFullyHandleStereotypes(boolean fullyHandleStereotypes) {
        this.fullyHandleStereotypes = fullyHandleStereotypes;
    }

    /**
     * @return Returns the showSingularMultiplicities.
     */
    public boolean isShowSingularMultiplicities() {
        return showSingularMultiplicities;
    }

    /**
     * @param showSingularMultiplicities The showSingularMultiplicities to set.
     */
    public void setShowSingularMultiplicities(boolean showSingularMultiplicities) {
        this.showSingularMultiplicities = showSingularMultiplicities;
    }

    /**
     * @return Returns the useGuillemets.
     */
    public boolean isUseGuillemets() {
        return useGuillemets;
    }

    /**
     * @param useGuillemets The useGuillemets to set.
     */
    public void setUseGuillemets(boolean useGuillemets) {
        this.useGuillemets = useGuillemets;
    }

    /**
     * @return Returns the showTypes.
     */
    public boolean isShowTypes() {
        return showTypes;
    }

    /**
     * @param showTypes The showTypes to set.
     */
    public void setShowTypes(boolean showTypes) {
        this.showTypes = showTypes;
    }

    /**
     * @return Returns the showProperties.
     */
    public boolean isShowProperties() {
        return showProperties;
    }

    /**
     * @param showProperties The showProperties to set.
     */
    public void setShowProperties(boolean showProperties) {
        this.showProperties = showProperties;
    }

    /**
     * @return Returns the showInitialValues.
     */
    public boolean isShowInitialValues() {
        return showInitialValues;
    }

    /**
     * @param showInitialValues The showInitialValues to set.
     */
    public void setShowInitialValues(boolean showInitialValues) {
        this.showInitialValues = showInitialValues;
    }

    /**
     * @return Returns the showMultiplicities.
     */
    public boolean isShowMultiplicities() {
        return showMultiplicities;
    }

    /**
     * @param showMultiplicities The showMultiplicities to set.
     */
    public void setShowMultiplicities(boolean showMultiplicities) {
        this.showMultiplicities = showMultiplicities;
    }
    
}
