/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2008-2009 Tom Morris and other contributors. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Contributors.
// The software program and documentation are supplied "AS
// IS", without any accompanying services from The Contributors. The 
// Contributors do not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// CONTRIBUTORS BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE CONTRIBUTORS HAVE BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE CONTRIBUTORS SPECIFICALLY DISCLAIM ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE CONTRIBUTORS
// HAVE NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.notation;

import org.tigris.gef.undo.Memento;

/**
 * Notation settings value object. Stores settings which control how text is
 * rendered on diagrams.
 * <p>
 * TODO: This needs to go on a diet. It's used everywhere, so is performance
 * sensitive. The current set of settings is the union of all those found in the
 * legacy code, but it's not clear that all of them are actually used.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class NotationSettings {

    private static final NotationSettings DEFAULT_SETTINGS = 
        initializeDefaultSettings();
    
    private NotationSettings parent;

    private String notationLanguage;
    
    // No valid field for above notationLanguage.  It's valid if not null.
    
    private boolean showAssociationNames;

    private boolean showAssociationNamesSet = false;

    private boolean showVisibilities;

    private boolean showVisibilitiesSet = false;

    private boolean showPaths;

    private boolean showPathsSet = false;

    private boolean fullyHandleStereotypes;

    private boolean fullyHandleStereotypesSet = false;

    private boolean useGuillemets;

    private boolean useGuillemetsSet = false;

    private boolean showMultiplicities;

    private boolean showMultiplicitiesSet = false;

    private boolean showSingularMultiplicities;

    private boolean showSingularMultiplicitiesSet = false;

    // TODO: Do we need to control separately for attributes and operations?
    private boolean showTypes;

    private boolean showTypesSet = false;

    private boolean showProperties;

    private boolean showPropertiesSet = false;

    private boolean showInitialValues;

    private boolean showInitialValuesSet = false;

    /**
     * Create a notation settings value object with all default values.
     * <p>
     * TODO: This class only has partial Undo support (basically just those
     * members that had it as part of a previous implementation).
     */
    public NotationSettings() {
        super();
        parent = getDefaultSettings();
    }

    /**
     * Create a notation settings object which uses the given settings as its
     * default values.  Note that there can be multiple levels of settings in
     * the hierarchy.
     */
    public NotationSettings(NotationSettings parentSettings) {
        this();
        parent = parentSettings;
    }

    // TODO: These defaults need to be checked against historical ones
    private static NotationSettings initializeDefaultSettings() {
        NotationSettings settings = new NotationSettings();
        settings.parent = null;
        settings.setNotationLanguage(Notation.DEFAULT_NOTATION);
        settings.setFullyHandleStereotypes(false);
        settings.setShowAssociationNames(true);
        settings.setShowInitialValues(false);
        settings.setShowMultiplicities(false);
        settings.setShowPaths(false);
        settings.setShowProperties(false);
        settings.setShowSingularMultiplicities(true);
        settings.setShowTypes(true);
        settings.setShowVisibilities(false);
        settings.setUseGuillemets(false);
        return settings;
    }
    
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
            if (parent != null) {
                return parent.getNotationLanguage();
            } else {
                return Notation.DEFAULT_NOTATION;
            }
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
     * @return Returns the fullyHandleStereotypes setting. If true, it will
     *         cause notation providers to include the names of the stereotypes
     *         for an element in the editable string presented to the user.
     */
    public boolean isFullyHandleStereotypes() {
        if (fullyHandleStereotypesSet) {
            return fullyHandleStereotypes;
        } else {
            if (parent != null) {
                return parent.isFullyHandleStereotypes();
            } else {
                return getDefaultSettings().isFullyHandleStereotypes();
            }
        }
    }

    /**
     * @param newValue The fullyHandleStereotypes to set. If true, it will cause
     *            notation providers to include the names of the stereotypes for
     *            an element in the editable string presented to the user.
     */
    public void setFullyHandleStereotypes(boolean newValue) {
        fullyHandleStereotypes = newValue;
        fullyHandleStereotypesSet = true;
    }

    /**
     * @return Returns the showSingularMultiplicities.
     */
    public boolean isShowSingularMultiplicities() {
        if (showSingularMultiplicitiesSet) {
            return showSingularMultiplicities;
        } else if (parent != null) {
            return parent.isShowSingularMultiplicities();
        }
        return getDefaultSettings().isShowSingularMultiplicities();
    }

    /**
     * @param showem <code>true</code> if "1" Multiplicities are to be shown.
     */
    public void setShowSingularMultiplicities(final boolean showem) {
        if (showSingularMultiplicities == showem 
                && showSingularMultiplicitiesSet) {
            return;
        }

        final boolean oldValid = showSingularMultiplicitiesSet;
        Memento memento = new Memento() {
            public void redo() {
                showSingularMultiplicities = showem;
                showSingularMultiplicitiesSet = true;
            }

            public void undo() {
                showSingularMultiplicities = !showem;
                showSingularMultiplicitiesSet = oldValid;
            }
        };
        doUndoable(memento);
    }

    /**
     * @return Returns the useGuillemets.
     */
    public boolean isUseGuillemets() {
        if (useGuillemetsSet) {
            return useGuillemets;
        } else if (parent != null) {
            return parent.isUseGuillemets();
        }
        return getDefaultSettings().isUseGuillemets();
    }

    /**
     * @param showem <code>true</code> if guillemets are to be shown.
     */
    public void setUseGuillemets(final boolean showem) {
        if (useGuillemets == showem && useGuillemetsSet) {
            return;
        }

        final boolean oldValid = useGuillemetsSet;
        
        Memento memento = new Memento() {
            public void redo() {
                useGuillemets = showem;
                useGuillemetsSet = true;
            }

            public void undo() {
                useGuillemets = !showem;
                useGuillemetsSet = oldValid;
            }
        };
        doUndoable(memento);
    }

    /**
     * @return Returns the showTypes.
     */
    public boolean isShowTypes() {
        if (showTypesSet) {
            return showTypes;
        } else if (parent != null) {
            return parent.isShowTypes();
        }
        return getDefaultSettings().isShowTypes();
    }


    /**
     * @param showem <code>true</code> if types are to be shown.
     */
    public void setShowTypes(final boolean showem) {
        if (showTypes == showem && showTypesSet) {
            return;
        }

        final boolean oldValid = showTypesSet;
        
        Memento memento = new Memento() {
            public void redo() {
                showTypes = showem;
                showTypesSet = true;
            }

            public void undo() {
                showTypes = !showem;
                showTypesSet = oldValid;
            }
        };
        doUndoable(memento);

    }

    /**
     * @return Returns the showProperties.
     */
    public boolean isShowProperties() {
        if (showPropertiesSet) {
            return showProperties;
        } else if (parent != null) {
            return parent.isShowProperties();
        }
        return getDefaultSettings().isShowProperties();
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowProperties(final boolean showem) {
        if (showProperties == showem && showPropertiesSet) {
            return;
        }

        final boolean oldValid = showPropertiesSet;
        
        Memento memento = new Memento() {
            public void redo() {
                showProperties = showem;
                showPropertiesSet = true;
            }

            public void undo() {
                showProperties = !showem;
                showPropertiesSet = oldValid;
            }
        };
        doUndoable(memento);

    }

    /**
     * @return Returns the showInitialValues.
     */
    public boolean isShowInitialValues() {
        if (showInitialValuesSet) {
            return showInitialValues;
        } else if (parent != null) {
            return parent.isShowInitialValues();
        }
        return getDefaultSettings().isShowInitialValues();
    }


    /**
     * @param showem <code>true</code> if initial values are to be shown.
     */
    public void setShowInitialValues(final boolean showem) {
        if (showInitialValues == showem && showInitialValuesSet) {
            return;
        }

        final boolean oldValid = showInitialValuesSet;
        
        Memento memento = new Memento() {
            public void redo() {
                showInitialValues = showem;
                showInitialValuesSet = true;
            }

            public void undo() {
                showInitialValues = !showem;
                showInitialValuesSet = oldValid;
            }
        };
        doUndoable(memento);

    }

    /**
     * @return Returns the showMultiplicities.
     */
    public boolean isShowMultiplicities() {
        if (showMultiplicitiesSet) {
            return showMultiplicities;
        } else if (parent != null) {
            return parent.isShowMultiplicities();
        }
        return getDefaultSettings().isShowMultiplicities();
    }

    /**
     * @param showem <code>true</code> if the multiplicity is to be shown.
     */
    public void setShowMultiplicities(final boolean showem) {
        if (showMultiplicities == showem && showMultiplicitiesSet) {
            return;
        }

        final boolean oldValid = showMultiplicitiesSet;
        
        Memento memento = new Memento() {
            public void redo() {
                showMultiplicities = showem;
                showMultiplicitiesSet = true;
            }

            public void undo() {
                showMultiplicities = !showem;
                showMultiplicitiesSet = oldValid;
            }
        };
        doUndoable(memento);
    }



    /**
     * @return Returns the showAssociationNames.
     */
    public boolean isShowAssociationNames() {
        if (showAssociationNamesSet) {
            return showAssociationNames;
        } else if (parent != null) {
            return parent.isShowAssociationNames();
        }
        return getDefaultSettings().isShowAssociationNames();
    }

    /**
     * @param showem <code>true</code> if association names are to be shown.
     */
    public void setShowAssociationNames(final boolean showem) {
        if (showAssociationNames == showem && showAssociationNamesSet) {
            return;
        }

        final boolean oldValid = showAssociationNamesSet;
        
        Memento memento = new Memento() {

            public void redo() {
                showAssociationNames = showem;
                showAssociationNamesSet = true;
            }

            public void undo() {
                showAssociationNames = !showem;
                showAssociationNamesSet = oldValid;
            }
        };
        doUndoable(memento);
    }

    /**
     * @return Returns the showVisibilities.
     */
    public boolean isShowVisibilities() {
        if (showVisibilitiesSet) {
            return showVisibilities;
        } else if (parent != null) {
            return parent.isShowVisibilities();
        }
        return getDefaultSettings().isShowVisibilities();
    }


    /**
     * @param showem <code>true</code> if visibilities are to be shown.
     */
    public void setShowVisibilities(final boolean showem) {
        
        if (showVisibilities == showem && showVisibilitiesSet) {
            return;
        }

        final boolean oldValid = showVisibilitiesSet;
        
        Memento memento = new Memento() {
            public void redo() {
                showVisibilities = showem;
                showVisibilitiesSet = true;
            }

            public void undo() {
                showVisibilities = !showem;
                showVisibilitiesSet = oldValid;
            }
        };
        doUndoable(memento);
    }

    /**
     * @return Returns the showPaths.
     */
    public boolean isShowPaths() {
        if (showPathsSet) {
            return showPaths;
        } else if (parent != null) {
            return parent.isShowPaths();
        }
        return getDefaultSettings().isShowPaths();
    }


    /**
     * @param showPaths The showPaths to set.
     */
    public void setShowPaths(boolean showPaths) {
        this.showPaths = showPaths;
        showPathsSet = true;
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
