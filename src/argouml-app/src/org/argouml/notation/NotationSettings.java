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

import org.tigris.gef.undo.Memento;

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

    private String notationLanguage;
    
    private boolean showAssociationNames;
    
    private boolean showVisibilities;
    
    private boolean showPaths;
    
    private boolean fullyHandleStereotypes;

    private boolean showStereotypes = true;
    
    private boolean useGuillemets;
    
    private boolean showMultiplicities;
    
    private boolean showSingularMultiplicities;

    // TODO: Do we need to control separately for attributes and operations?
    private boolean showTypes;
    
    private boolean showProperties;
    
    private boolean showInitialValues;
    
 
    
    /**
     * @return the default settings
     */
    public static NotationSettings getDefaultSettings() {
        return DEFAULT_SETTINGS;
    }
    
    /**
     * @return Return the notation language.
     */
    public String getNotationLanguage() {
        if (notationLanguage == null) {
//            if (parent != null) {
//                return parent.getNotationLanguage();
//            } else {
            return "UML 1.4";
//            }
        }
        return notationLanguage;
    }


    /**
     * @param newLanguage the notation language.
     * @return true if the notation is set - false if it does not exist
     */
    public boolean setNotationLanguage(final String newLanguage) {
        if (notationLanguage != null 
                && notationLanguage.equals(newLanguage)) {
            return true;
        }
        
        // TODO: Do we care?
        if (Notation.findNotation(newLanguage) == null) {
            /* This Notation is not available! */
            return false;
        }

        final String oldLanguage = notationLanguage;

        Memento memento = new Memento() {
            public void redo() {
                notationLanguage = newLanguage;
                // TODO: We can't have a global "current" language
                // NotationProviderFactory2.setCurrentLanguage(newLanguage);
            }

            public void undo() {
                notationLanguage = oldLanguage;
                // TODO: We can't have a global "current" language
                // NotationProviderFactory2.setCurrentLanguage(oldLanguage);
            }
        };
        doUndoable(memento);
        return true;
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
     * @param showem <code>true</code> if "1" Multiplicities are to be shown.
     */
    public void setShowSingularMultiplicities(final boolean showem) {
        if (showSingularMultiplicities == showem) {
            return;
        }

        Memento memento = new Memento() {
            public void redo() {
                showSingularMultiplicities = showem;
            }

            public void undo() {
                showSingularMultiplicities = !showem;
            }
        };
        doUndoable(memento);
    }

    /**
     * @return Returns the useGuillemets.
     */
    public boolean isUseGuillemets() {
        return useGuillemets;
    }

    /**
     * @param showem <code>true</code> if guillemets are to be shown.
     */
    public void setUseGuillemets(final boolean showem) {
        if (useGuillemets == showem) {
            return;
        }

        Memento memento = new Memento() {
            public void redo() {
                useGuillemets = showem;
            }

            public void undo() {
                useGuillemets = !showem;
            }
        };
        doUndoable(memento);
    }

    /**
     * @return Returns the showTypes.
     */
    public boolean isShowTypes() {
        return showTypes;
    }


    /**
     * @param showem <code>true</code> if types are to be shown.
     */
    public void setShowTypes(final boolean showem) {
        if (showTypes == showem) {
            return;
        }

        Memento memento = new Memento() {
            public void redo() {
                showTypes = showem;
            }

            public void undo() {
                showTypes = !showem;
            }
        };
        doUndoable(memento);

    }

    /**
     * @return Returns the showProperties.
     */
    public boolean isShowProperties() {
        return showProperties;
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowProperties(final boolean showem) {
        if (showProperties == showem) {
            return;
        }

        Memento memento = new Memento() {
            public void redo() {
                showProperties = showem;
            }

            public void undo() {
                showProperties = !showem;
            }
        };
        doUndoable(memento);

    }

    /**
     * @return Returns the showInitialValues.
     */
    public boolean isShowInitialValues() {
        return showInitialValues;
    }


    /**
     * @param showem <code>true</code> if initial values are to be shown.
     */
    public void setShowInitialValues(final boolean showem) {
        if (showInitialValues == showem) {
            return;
        }

        Memento memento = new Memento() {
            public void redo() {
                showInitialValues = showem;
            }

            public void undo() {
                showInitialValues = !showem;
            }
        };
        doUndoable(memento);

    }

    /**
     * @return Returns the showMultiplicities.
     */
    public boolean isShowMultiplicities() {
        return showMultiplicities;
    }

    /**
     * @param showem <code>true</code> if the multiplicity is to be shown.
     */
    public void setShowMultiplicities(final boolean showem) {
        if (showMultiplicities == showem) {
            return;
        }

        Memento memento = new Memento() {
            public void redo() {
                showMultiplicities = showem;
            }

            public void undo() {
                showMultiplicities = !showem;
            }
        };
        doUndoable(memento);
    }



    /**
     * @return Returns the showAssociationNames.
     */
    public boolean isShowAssociationNames() {
        return showAssociationNames;
    }

    /**
     * @param showem <code>true</code> if association names are to be shown.
     */
    public void setShowAssociationNames(final boolean showem) {
        if (showAssociationNames == showem) {
            return;
        }

        Memento memento = new Memento() {

            public void redo() {
                showAssociationNames = showem;
            }

            public void undo() {
                showAssociationNames = !showem;
            }
        };
        doUndoable(memento);
    }

    /**
     * @return Returns the showVisibilities.
     */
    public boolean isShowVisibilities() {
        return showVisibilities;
    }


    /**
     * @param showem <code>true</code> if visibilities are to be shown.
     */
    public void setShowVisibilities(final boolean showem) {
        if (showVisibilities == showem) {
            return;
        }

        Memento memento = new Memento() {
            public void redo() {
                showVisibilities = showem;
            }

            public void undo() {
                showVisibilities = !showem;
            }
        };
        doUndoable(memento);
    }

    /**
     * @return Returns the showPaths.
     */
    public boolean isShowPaths() {
        return showPaths;
    }


    /**
     * @param showPaths The showPaths to set.
     */
    public void setShowPaths(boolean showPaths) {
        this.showPaths = showPaths;
    }


    /**
     * @return Returns the showStereotypes.
     */
    public boolean isShowStereotypes() {
        return showStereotypes;
    }


    /**
     * @param showem <code>true</code> if stereotypes are to be shown.
     */
    public void setShowStereotypes(final boolean showem) {
        if (showStereotypes == showem) {
            return;
        }

        Memento memento = new Memento() {
            public void redo() {
                showStereotypes = showem;
            }

            public void undo() {
                showStereotypes = !showem;
            }
        };
        doUndoable(memento);

    }
    
    
    private void doUndoable(Memento memento) {
        // TODO: Undo should be managed externally or we should be given 
        // an Undo manager to use (the project's) rather than using a global one
//        if (DiagramUndoManager.getInstance().isGenerateMementos()) {
//            DiagramUndoManager.getInstance().addMemento(memento);
//        }
        memento.redo();
        // TODO: Mark diagram/project as dirty?
    }

}
