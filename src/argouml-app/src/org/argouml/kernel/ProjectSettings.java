/* $Id$
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel van der Wulp
 *    Bob Tarling
 *******************************************************************************
 *
 * Some portions of this file were previously release using the BSD License:
 */

// $Id$
// Copyright (c) 2006-2009 The Regents of the University of California. All
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

package org.argouml.kernel;

import java.awt.Font;
import java.beans.PropertyChangeEvent;

import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.argouml.notation.NotationSettings;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.undo.Memento;
import org.tigris.gef.undo.UndoManager;

/**
 * A datastructure for settings for a Project. <p>
 *
 * Most getters return a string, since they are used by "argo.tee".
 * This is also the reason all these attributes
 * are not part of a Map or something. <p>
 *
 * TODO: The header comment is currently not used - this function
 * is not completely implemented yet. How do we store this in the project?
 * Where should the user enter his header comment? See issue 4813.
 *
 * @author michiel
 */
public class ProjectSettings {

    // Default diagram settings
    private DiagramSettings diaDefault;

    // Default notation settings
    private NotationSettings npSettings;
    
    /** Setting to control whether stereotypes are shown in explorer view */
    private boolean showExplorerStereotypes;
    
    /* Generation preferences: */
    private String headerComment =
        "Your copyright and other header comments";
    
    private Project project;

    /**
     * Create a new set of project settings for the given project,
     * based on the application defaults. <p>
     *
     * The constructor is not public, since this
     * class is only created from the Project..
     * @param project the project owning these settings
     */
    ProjectSettings(Project project) {
        this();
        this.project = project;
    }

    /**
     * Create a new set of project settings,
     * based on the application defaults. <p>
     *
     * The constructor is not public, since this
     * class is only created from the Project..
     */
    ProjectSettings() {
        super();

        diaDefault = new DiagramSettings();
        diaDefault.initFromConfiguration();

        npSettings = diaDefault.getNotationSettings();
        
        String notationLanguage =
            Notation.getConfiguredNotation().getConfigurationValue();
        npSettings.setNotationLanguage(notationLanguage);
        
        npSettings.setUseGuillemets(Configuration.getBoolean(
                Notation.KEY_USE_GUILLEMOTS, false));
        /*
         * The next one defaults to TRUE, to stay compatible with older
         * ArgoUML versions that did not have this setting:
         */
        npSettings.setShowAssociationNames(Configuration.getBoolean(
                Notation.KEY_SHOW_ASSOCIATION_NAMES, true));
        npSettings.setShowVisibilities(Configuration.getBoolean(
                Notation.KEY_SHOW_VISIBILITY));
        npSettings.setShowMultiplicities(Configuration.getBoolean(
                Notation.KEY_SHOW_MULTIPLICITY));
        npSettings.setShowInitialValues(Configuration.getBoolean(
                Notation.KEY_SHOW_INITIAL_VALUE));
        npSettings.setShowProperties(Configuration.getBoolean(
                Notation.KEY_SHOW_PROPERTIES));
        /*
         * The next ones defaults to TRUE, to stay compatible with older
         * ArgoUML versions that did not have this setting:
         */
        npSettings.setShowTypes(Configuration.getBoolean(
                Notation.KEY_SHOW_TYPES, true));
        
        showExplorerStereotypes = Configuration.getBoolean(
                Notation.KEY_SHOW_STEREOTYPES);
        /*
         * The next one defaults to TRUE, despite that this is
         * NOT compatible with older ArgoUML versions
         * (before 0.24) that did
         * not have this setting - see issue 1395 for the rationale:
         */
        npSettings.setShowSingularMultiplicities(Configuration.getBoolean(
                Notation.KEY_SHOW_SINGULAR_MULTIPLICITIES, true));
    }

    /**
     * Send all events required for post-load of project.<p>
     * @deprecated for 0.27.2 by tfmorris.  No replacement.  Diagrams/Figs are
     * now created with the correct initial settings.
     */
    @Deprecated
    public void init() {
        /*
         * Since this is (hopefully) a temporary solution, and nobody ever looks
         * at the type of notation event, we can simplify from sending every
         * existing event to one event only. But since there is no catch-all
         * event defined, we just make one up. Rationale: reduce the number of
         * total refreshes of the drawing.
         */
        init(true, Configuration.makeKey("notation", "all"));

        diaDefault.notifyOfChangedSettings();
    }

    private void init(boolean value, ConfigurationKey key) {
        fireNotationEvent(key, value, value);
    }
    
    /**
     * @return the default diagram settings
     */
    public DiagramSettings getDefaultDiagramSettings() {
        return diaDefault;
    }

    /**
     * @return the default project wide notation settings
     */
    public NotationSettings getNotationSettings() {
        return npSettings;
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns the notation language.
     */
    public String getNotationLanguage() {
        return npSettings.getNotationLanguage();
    }

    /**
     * @return Return the notation name.
     */
    public NotationName getNotationName() {
        return Notation.findNotation(getNotationLanguage());
    }

    /**
     * @param newLanguage the notation language.
     * @return true if the notation is set - false if it does not exist
     */
    public boolean setNotationLanguage(final String newLanguage) {
        if (getNotationLanguage().equals(newLanguage)) {
            return true;
        }
        if (Notation.findNotation(newLanguage) == null) {
            /* This Notation is not available! */
            return false;
        }

        final String oldLanguage = getNotationLanguage();

        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_DEFAULT_NOTATION;

            public void redo() {
                npSettings.setNotationLanguage(newLanguage);
                fireNotationEvent(key, oldLanguage, newLanguage);
            }

            public void undo() {
                npSettings.setNotationLanguage(oldLanguage);
                fireNotationEvent(key, newLanguage, oldLanguage);
            }
        };
        doUndoable(memento);
        return true;
    }

    private void doUndoable(Memento memento) {
        // TODO: This needs to be managing undo on a per-project basis
        // instead of using GEF's global undo manager
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        project.setDirty(true);
    }

    /**
     * @param nn the new notation language
     */
    public void setNotationLanguage(NotationName nn) {
        setNotationLanguage(nn.getConfigurationValue());
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show bold names.
     */
    public String getShowBoldNames() {
        return Boolean.toString(diaDefault.isShowBoldNames());
    }

    /**
     * Used by "argo.tee".
     *
     * @see #getUseGuillemotsValue()
     * @return Returns "true" if we show guillemets.
     */
    public String getUseGuillemots() {
        return Boolean.toString(getUseGuillemotsValue());
    }

    /**
     * Get setting controlling whether guillemets (the double angle brackets
     * quotation mark characters from Unicode) are to be used for formatting
     * instead of two individual characters for each quote mark (e.g. >>). NOTE:
     * This affects not only the Diagrams, but also display in the explorer view
     * and other places.
     * 
     * @return Returns <code>true</code> if we show guillemets.
     */
    public boolean getUseGuillemotsValue() {
        return npSettings.isUseGuillemets();
    }
    

    /**
     * @see #getUseGuillemotsValue()
     * @param showem <code>true</code> if guillemets are to be used.
     */
    public void setUseGuillemots(String showem) {
        setUseGuillemots(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @see #getUseGuillemotsValue()
     * @param showem <code>true</code> if guillemets are to be shown.
     */
    public void setUseGuillemots(final boolean showem) {
        if (getUseGuillemotsValue() == showem) {
            return;
        }

        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_USE_GUILLEMOTS;

            public void redo() {
                npSettings.setUseGuillemets(showem);
                fireNotationEvent(key, !showem, showem);
            }

            public void undo() {
                npSettings.setUseGuillemets(!showem);
                fireNotationEvent(key, showem, !showem);
            }
        };
        doUndoable(memento);
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show association names.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link NotationSettings}.
     */
    @Deprecated
    public String getShowAssociationNames() {
        return Boolean.toString(getShowAssociationNamesValue());
    }

    /**
     * @return Returns <code>true</code> if we show association names.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link NotationSettings}.
     */
    @Deprecated
    public boolean getShowAssociationNamesValue() {
        return npSettings.isShowAssociationNames();
    }

    /**
     * @param showem <code>true</code> if association names are to be shown.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link NotationSettings}.
     */
    @Deprecated
    public void setShowAssociationNames(String showem) {
        setShowAssociationNames(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if association names are to be shown.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link NotationSettings}.
     */
    @Deprecated
    public void setShowAssociationNames(final boolean showem) {
        if (npSettings.isShowAssociationNames() == showem) {
            return;
        }

        Memento memento = new Memento() {
            private final ConfigurationKey key = 
                Notation.KEY_SHOW_ASSOCIATION_NAMES;

            public void redo() {
                npSettings.setShowAssociationNames(showem);
                fireNotationEvent(key, !showem, showem);
            }

            public void undo() {
                npSettings.setShowAssociationNames(!showem);
                fireNotationEvent(key, showem, !showem);
            }
        };
        doUndoable(memento);
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show visibilities.
     * @deprecated for 0.27.2 by tfmorris. Use {@link NotationSettings}.
     */
    @Deprecated
    public String getShowVisibility() {
        return Boolean.toString(getShowVisibilityValue());
    }

    /**
     * @return Returns <code>true</code> if we show visibilities.
     * @deprecated for 0.27.2 by tfmorris. Use {@link NotationSettings}.
     */
    @Deprecated
    public boolean getShowVisibilityValue() {
        return npSettings.isShowVisibilities();
    }

    /**
     * @param showem <code>true</code> if visibilities are to be shown.
     * @deprecated for 0.27.2 by tfmorris. Use {@link NotationSettings}.
     */
    @Deprecated
    public void setShowVisibility(String showem) {
        setShowVisibility(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if visibilities are to be shown.
     * @deprecated for 0.27.2 by tfmorris. Use {@link NotationSettings}.
     */
    @Deprecated
    public void setShowVisibility(final boolean showem) {
        if (npSettings.isShowVisibilities() == showem) {
            return;
        }

        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_SHOW_VISIBILITY;

            public void redo() {
                npSettings.setShowVisibilities(showem);
                fireNotationEvent(key, !showem, showem);
            }

            public void undo() {
                npSettings.setShowVisibilities(!showem);
                fireNotationEvent(key, showem, !showem);
            }
        };
        doUndoable(memento);
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show multiplicities.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link NotationSettings}.
     */
    @Deprecated
    public String getShowMultiplicity() {
        return Boolean.toString(getShowMultiplicityValue());
    }

    /**
     * @return Returns <code>true</code> if we show multiplicities.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link NotationSettings}.
     */
    @Deprecated
    public boolean getShowMultiplicityValue() {
        return npSettings.isShowMultiplicities();
    }

    /**
     * @param showem <code>true</code> if multiplicity is to be shown.
      * @deprecated for 0.27.2 by tfmorris.  Use {@link NotationSettings}.
     */
    @Deprecated
    public void setShowMultiplicity(String showem) {
        setShowMultiplicity(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if the multiplicity is to be shown.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link NotationSettings}.
     */
    @Deprecated
    public void setShowMultiplicity(final boolean showem) {
        if (npSettings.isShowMultiplicities() == showem) {
            return;
        }

        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_SHOW_MULTIPLICITY;

            public void redo() {
                npSettings.setShowMultiplicities(showem);
                fireNotationEvent(key, !showem, showem);
            }

            public void undo() {
                npSettings.setShowMultiplicities(!showem);
                fireNotationEvent(key, showem, !showem);
            }
        };
        doUndoable(memento);
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show initial values.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link NotationSettings}.
     */
    @Deprecated
    public String getShowInitialValue() {
        return Boolean.toString(getShowInitialValueValue());
    }

    /**
     * @return Returns <code>true</code> if we show initial values.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link NotationSettings}.
     */
    @Deprecated
    public boolean getShowInitialValueValue() {
        return npSettings.isShowInitialValues();
    }

    /**
     * @param showem <code>true</code> if initial values are to be shown.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link NotationSettings}.
     */
    @Deprecated
    public void setShowInitialValue(String showem) {
        setShowInitialValue(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if initial values are to be shown.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link NotationSettings}.
     */
    @Deprecated
    public void setShowInitialValue(final boolean showem) {
        if (npSettings.isShowInitialValues() == showem) {
            return;
        }

        Memento memento = new Memento() {
            private final ConfigurationKey key =
                Notation.KEY_SHOW_INITIAL_VALUE;

            public void redo() {
                npSettings.setShowInitialValues(showem);
                fireNotationEvent(key, !showem, showem);
            }

            public void undo() {
                npSettings.setShowInitialValues(!showem);
                fireNotationEvent(key, showem, !showem);
            }
        };
        doUndoable(memento);
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show properties.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link NotationSettings}.
     */
    @Deprecated
    public String getShowProperties() {
        return Boolean.toString(getShowPropertiesValue());
    }

    /**
     * @return Returns <code>true</code> if we show properties.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link NotationSettings}.
     */
    @Deprecated
    public boolean getShowPropertiesValue() {
        return npSettings.isShowProperties();
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link NotationSettings}.
     */
    @Deprecated
    public void setShowProperties(String showem) {
        setShowProperties(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     * @deprecated for 0.27.2 by tfmorris. Use {@link NotationSettings}.
     */
    @Deprecated
    public void setShowProperties(final boolean showem) {
        if (npSettings.isShowProperties() == showem) {
            return;
        }

        Memento memento = new Memento() {
            private final ConfigurationKey key =
                Notation.KEY_SHOW_PROPERTIES;

            public void redo() {
                npSettings.setShowProperties(showem);
                fireNotationEvent(key, !showem, showem);
            }

            public void undo() {
                npSettings.setShowProperties(!showem);
                fireNotationEvent(key, showem, !showem);
            }
        };
        doUndoable(memento);
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show types.
     * @deprecated for 0.27.2 by tfmorris. Use {@link NotationSettings}.
     */
    @Deprecated
    public String getShowTypes() {
        return Boolean.toString(getShowTypesValue());
    }

    /**
     * @return Returns <code>true</code> if we show types.
     * @deprecated for 0.27.2 by tfmorris. Use {@link NotationSettings}.
     */
    @Deprecated
    public boolean getShowTypesValue() {
        return npSettings.isShowTypes();
    }

    /**
     * @param showem <code>true</code> if types are to be shown.
     * @deprecated for 0.27.2 by tfmorris. Use {@link NotationSettings}.
     */
    @Deprecated
    public void setShowTypes(String showem) {
        setShowTypes(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if types are to be shown.
     * @deprecated for 0.27.2 by tfmorris. Use {@link NotationSettings}.
     */
    @Deprecated
    public void setShowTypes(final boolean showem) {
        if (npSettings.isShowTypes() == showem) {
            return;
        }

        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_SHOW_TYPES;

            public void redo() {
                npSettings.setShowTypes(showem);
                fireNotationEvent(key, !showem, showem);
            }

            public void undo() {
                npSettings.setShowTypes(!showem);
                fireNotationEvent(key, showem, !showem);
            }
        };
        doUndoable(memento);
    }


    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show stereotypes in the explorer.
     */
    public String getShowStereotypes() {
        return Boolean.toString(getShowStereotypesValue());
    }

    /**
     * TODO: Is this used in places other than on Diagrams?  If so, it needs to
     * stay in ProjectSettings (as well as being a DiagramSetting).
     * 
     * @return Returns <code>true</code> if we show stereotypes in the explorer
     */
    public boolean getShowStereotypesValue() {
        return showExplorerStereotypes;
    }

    /**
     * @param showem <code>true</code> if stereotypes are to be shown in the
     * explorer.
     */
    public void setShowStereotypes(String showem) {
        setShowStereotypes(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if stereotypes are to be shown in the
     * explorer view.
     */
    public void setShowStereotypes(final boolean showem) {
        if (showExplorerStereotypes == showem) {
            return;
        }

        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_SHOW_STEREOTYPES;

            public void redo() {
                showExplorerStereotypes = showem;
                fireNotationEvent(key, !showem, showem);
            }

            public void undo() {
                showExplorerStereotypes = !showem;
                fireNotationEvent(key, showem, !showem);
            }
        };
        doUndoable(memento);
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show "1" Multiplicities.
     * @deprecated for 0.27.2 by tfmorris. Use {@link NotationSettings}.
     */
    @Deprecated
    public String getShowSingularMultiplicities() {
        return Boolean.toString(getShowSingularMultiplicitiesValue());
    }

    /**
     * @return Returns <code>true</code> if we show  "1" Multiplicities.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link NotationSettings}.
     */
    @Deprecated
    public boolean getShowSingularMultiplicitiesValue() {
        return npSettings.isShowSingularMultiplicities();
    }

    /**
     * @param showem <code>true</code> if "1" Multiplicities are to be shown.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link NotationSettings}.
     */
    @Deprecated
    public void setShowSingularMultiplicities(String showem) {
        setShowSingularMultiplicities(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if "1" Multiplicities are to be shown.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link NotationSettings}.
     */
    @Deprecated
    public void setShowSingularMultiplicities(final boolean showem) {
        if (npSettings.isShowSingularMultiplicities() == showem) {
            return;
        }

        Memento memento = new Memento() {
            private final ConfigurationKey key =
                Notation.KEY_SHOW_SINGULAR_MULTIPLICITIES;

            public void redo() {
                npSettings.setShowSingularMultiplicities(showem);
                fireNotationEvent(key, !showem, showem);
            }

            public void undo() {
                npSettings.setShowSingularMultiplicities(!showem);
                fireNotationEvent(key, showem, !showem);
            }
        };
        doUndoable(memento);
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show the arrows when
     * both association ends of an association are navigable.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public String getHideBidirectionalArrows() {
        return Boolean.toString(getHideBidirectionalArrowsValue());
    }

    /**
     * @return Returns <code>true</code> if we show the arrows when
     * both association ends of an association are navigable.

     * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public boolean getHideBidirectionalArrowsValue() {
        return !diaDefault.isShowBidirectionalArrows();
    }

    /**
     * @param hideem <code>true</code> if both arrows are to be shown when
     * both association ends of an association are navigable.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public void setHideBidirectionalArrows(String hideem) {
        setHideBidirectionalArrows(Boolean.valueOf(hideem).booleanValue());
    }

    /**
     * @param hideem <code>true</code> if both arrows are to be shown when
     * both association ends of an association are navigable.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public void setHideBidirectionalArrows(final boolean hideem) {
        if (diaDefault.isShowBidirectionalArrows() == !hideem) {
            return;
        }

        Memento memento = new Memento() {

            public void redo() {
                diaDefault.setShowBidirectionalArrows(!hideem);
            }

            public void undo() {
                diaDefault.setShowBidirectionalArrows(hideem);
            }
        };
        doUndoable(memento);
    }
    
    /**
     * Used by "argo.tee".
     *
     * @return Returns the shadow width.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public String getDefaultShadowWidth() {
        return Integer.valueOf(getDefaultShadowWidthValue()).toString();
    }

    /**
     * @return Returns the shadow width.
      * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public int getDefaultShadowWidthValue() {
        return diaDefault.getDefaultShadowWidth();
    }

    /**
     * @param newWidth The Shadow Width.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public void setDefaultShadowWidth(final int newWidth) {
        final int oldValue = diaDefault.getDefaultShadowWidth();
        if (oldValue == newWidth) {
            return;
        }

        Memento memento = new Memento() {

            public void redo() {
                diaDefault.setDefaultShadowWidth(newWidth);
            }

            public void undo() {
                diaDefault.setDefaultShadowWidth(oldValue);
            }
        };
        doUndoable(memento);
    }

    /**
     * @param width The shadow width to set.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public void setDefaultShadowWidth(String width) {
        setDefaultShadowWidth(Integer.parseInt(width));
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns the default stereotype view
     * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public String getDefaultStereotypeView() {
        return Integer.valueOf(getDefaultStereotypeViewValue()).toString();
    }

    /**
     * @return Returns the default stereotype view
     * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public int getDefaultStereotypeViewValue() {
        return diaDefault.getDefaultStereotypeViewInt();
    }


    /**
     * No longer used by "argo.tee". All uses deprecated.
     * 
     * @return the output directory name
     * @deprecated for 0.27.2 by tfmorris. This is a user setting, not a project
     *             setting.
     */
    @Deprecated
    public String getGenerationOutputDir() {
        return "";
    }

    /**
     * @param od the output directory name
     * @deprecated for 0.27.2 by tfmorris. This is a user setting, not a project
     *             setting. Any uses will be ignored.
     */
    @Deprecated
    public void setGenerationOutputDir(@SuppressWarnings("unused") String od) {
        // ignored
    }

    /**
     * @return the header comment string
     */
    public String getHeaderComment() {
        return headerComment;
    }

    /**
     * @param newView the default stereotype view
     * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public void setDefaultStereotypeView(final int newView) {
        final int oldValue = diaDefault.getDefaultStereotypeViewInt();
        if (oldValue == newView) {
            return;
        }

        Memento memento = new Memento() {
            private final ConfigurationKey key =
                ProfileConfiguration.KEY_DEFAULT_STEREOTYPE_VIEW;

            public void redo() {
                diaDefault.setDefaultStereotypeView(newView);
                fireNotationEvent(key, oldValue, newView);
            }

            public void undo() {
                diaDefault.setDefaultStereotypeView(oldValue);
                fireNotationEvent(key, newView, oldValue);
            }
        };
        doUndoable(memento);
    }

    /**
     * @param c the header comment string
     */
    public void setHeaderComment(String c) { headerComment = c; }


    /**
     * Convenience methods to fire notation configuration change events.
     *
     * @param key the ConfigurationKey that is related to the change
     * @param oldValue the old value
     * @param newValue the new value
     */
    private void fireNotationEvent(
            ConfigurationKey key, int oldValue, int newValue) {
        fireNotationEvent(key, Integer.toString(oldValue),
                Integer.toString(newValue));
    }

    /**
     * Convenience methods to fire notation configuration change events.
     *
     * @param key the ConfigurationKey that is related to the change
     * @param oldValue the old value
     * @param newValue the new value
     */
    private void fireNotationEvent(ConfigurationKey key, boolean oldValue,
            boolean newValue) {
        fireNotationEvent(key, Boolean.toString(oldValue),
                Boolean.toString(newValue));
    }

    /**
     * Convenience methods to fire notation configuration change events.
     *
     * @param key the ConfigurationKey that is related to the change
     * @param oldValue the old value
     * @param newValue the new value
     */
    private void fireNotationEvent(ConfigurationKey key, String oldValue,
            String newValue) {
        ArgoEventPump.fireEvent(new ArgoNotationEvent(
                ArgoEventTypes.NOTATION_CHANGED, new PropertyChangeEvent(this,
                        key.getKey(), oldValue, newValue)));
    }

    /**
     * Diagram font name. <p>
     *
     * Used by "argo.tee".
     *
     * @return diagram font name.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public String getFontName() {
        return diaDefault.getFontName();
    }

    /**
     * Diagram font name.
     * @param newFontName diagram font name.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public void setFontName(String newFontName) {
        diaDefault.setFontName(newFontName);
    }

    /**
     * Diagram font size. <p>
     *
     * Used by "argo.tee".
     *
     * @return diagram font size.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public int getFontSize() {
        return diaDefault.getFontSize();
    }

    /**
     * Diagram font size.
     * @param newFontSize diagram font size.
     * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public void setFontSize(int newFontSize) {
        diaDefault.setFontSize(newFontSize);
    }


    /**
     * Returns the Plain diagram font which corresponds
     * to selected parameters.
     *
     * @return plain diagram font
     * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public Font getFontPlain() {
        return diaDefault.getFontPlain();
    }

    /**
     * Returns the Italic diagram font which corresponds
     * to selected parameters.
     *
     * @return italic diagram font
     * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public Font getFontItalic() {
        return diaDefault.getFontItalic();
    }

    /**
     * Returns the Bold diagram font which corresponds
     * to selected parameters.
     *
     * @return bold diagram font
      * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public Font getFontBold() {
        return diaDefault.getFontBold();
    }

    /**
     * Returns the Bold-Italic diagram font which corresponds
     * to selected parameters.
     *
     * @return bold-italic diagram font
     * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public Font getFontBoldItalic() {
        return diaDefault.getFontBoldItalic();
    }

    /**
     * Utility function to convert a font style integer into a Font.
     *
     * @param fontStyle the style; see the predefined constants in Font
     * @return the Font that corresponds to the style
     * @deprecated for 0.27.2 by tfmorris.  Use {@link DiagramSettings}.
     */
    @Deprecated
    public Font getFont(int fontStyle) {
        return diaDefault.getFont(fontStyle);
    }
}
