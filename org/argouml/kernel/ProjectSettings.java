// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

import java.beans.PropertyChangeEvent;

import org.argouml.application.api.Configuration;
import org.argouml.application.api.ConfigurationKey;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.tigris.gef.undo.Memento;
import org.tigris.gef.undo.UndoManager;

/**
 * A datastructure for settings for a Project. <p>
 * 
 * Most getters return a string, since they are used by "argo.tee".
 * This is also the reason all these attributes 
 * are not part of a Map or something.
 *
 * @author michiel
 */
public class ProjectSettings {

    /* The notation settings with project scope: */
    private String notationLanguage;
    private boolean showBoldNames;
    private boolean useGuillemots;
    private boolean showVisibility;
    private boolean showMultiplicity;
    private boolean showInitialValue;
    private boolean showProperties;
    private boolean showTypes;
    private boolean showStereotypes;
    private int defaultShadowWidth;


    /**
     * Create a new set of project settings, 
     * based on the application defaults. <p>
     * 
     * The constructor is not public, since this 
     * class is only created from the Project..
     */
    ProjectSettings() {
        super();
        
        notationLanguage = 
            Notation.getConfigueredNotation().getConfigurationValue();
        showBoldNames = Configuration.getBoolean(
                Notation.KEY_SHOW_BOLD_NAMES);
        useGuillemots = Configuration.getBoolean(
                Notation.KEY_USE_GUILLEMOTS, false);
        showVisibility = Configuration.getBoolean(
                Notation.KEY_SHOW_VISIBILITY);
        showMultiplicity = Configuration.getBoolean(
                Notation.KEY_SHOW_MULTIPLICITY);
        showInitialValue = Configuration.getBoolean(
                Notation.KEY_SHOW_INITIAL_VALUE);
        showProperties = Configuration.getBoolean(
                Notation.KEY_SHOW_PROPERTIES);
        /*
         * The next one defaults to TRUE, to stay compatible with older
         * ArgoUML versions that did not have this setting:
         */
        showTypes = Configuration.getBoolean(Notation.KEY_SHOW_TYPES, true);
        showStereotypes = Configuration.getBoolean(
                Notation.KEY_SHOW_STEREOTYPES);
        defaultShadowWidth = Configuration.getInteger(
                Notation.KEY_DEFAULT_SHADOW_WIDTH, 1);
    }


    /**
     * Used by "argo.tee".
     * 
     * @return Returns the notation language.
     */
    public String getNotationLanguage() {
        return notationLanguage;
    }

    /**
     * @return Returns the notation language.
     */
    public NotationName getNotationName() {
        return Notation.findNotation(notationLanguage);
    }

    /**
     * @param newLanguage the notation language.
     */
    public void setNotationLanguage(final String newLanguage) {
        if (notationLanguage.equals(newLanguage)) return;
        
        final String oldLanguage = notationLanguage;
        
        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_DEFAULT_NOTATION;

            public void redo() {
                notationLanguage = newLanguage;
                fireEvent(key, oldLanguage, newLanguage);
            }

            public void undo() {
                notationLanguage = oldLanguage;
                fireEvent(key, newLanguage, oldLanguage);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
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
        return Boolean.toString(showBoldNames);
    }

    /**
     * @return Returns <code>true</code> if we show bold names.
     */
    public boolean getShowBoldNamesValue() {
        return showBoldNames;
    }

    /**
     * @param showbold <code>true</code> if names are to be shown in bold font.
     */
    public void setShowBoldNames(String showbold) {
        setShowBoldNames(Boolean.valueOf(showbold).booleanValue());
    }

    /**
     * @param showem <code>true</code> if names are to be shown in bold font.
     */
    public void setShowBoldNames(final boolean showem) {
        if (showBoldNames == showem) return;

        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_SHOW_BOLD_NAMES;

            public void redo() {
                showBoldNames = showem;
                fireEvent(key, !showem, showem);
            }

            public void undo() {
                showBoldNames = !showem;
                fireEvent(key, showem, !showem);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * Used by "argo.tee".
     * 
     * @return Returns "true" if we show guillemots.
     */
    public String getUseGuillemots() {
        return Boolean.toString(useGuillemots);
    }

    /**
     * @return Returns <code>true</code> if we show guillemots.
     */
    public boolean getUseGuillemotsValue() {
        return useGuillemots;
    }

    /**
     * @param showem <code>true</code> if guillemots are to be shown.
     */
    public void setUseGuillemots(String showem) {
        setUseGuillemots(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if guillemots are to be shown.
     */

    public void setUseGuillemots(final boolean showem) {
        if (useGuillemots == showem) return;

        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_USE_GUILLEMOTS;

            public void redo() {
                useGuillemots = showem;
                fireEvent(key, !showem, showem);
            }

            public void undo() {
                useGuillemots = !showem;
                fireEvent(key, showem, !showem);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * @return the left pointing guillemot, i.e. << or the one-character symbol
     */
    public String getLeftGuillemot() {
        return useGuillemots ? "\u00ab" : "<<";
    }

    /**
     * @return the right pointing guillemot, i.e. >> or the one-character symbol
     */
    public String getRightGuillemot() {
        return useGuillemots ? "\u00bb" : ">>";
    }

    /**
     * Used by "argo.tee".
     * 
     * @return Returns "true" if we show visibilities.
     */
    public String getShowVisibility() {
        return Boolean.toString(showVisibility);
    }

    /**
     * @return Returns <code>true</code> if we show visibilities.
     */
    public boolean getShowVisibilityValue() {
        return showVisibility;
    }

    /**
     * @param showem <code>true</code> if visibilities are to be shown.
     */
    public void setShowVisibility(String showem) {
        setShowVisibility(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if visibilities are to be shown.
     */
    public void setShowVisibility(final boolean showem) {
        if (showVisibility == showem) return;

        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_SHOW_VISIBILITY;

            public void redo() {
                showVisibility = showem;
                fireEvent(key, !showem, showem);
            }

            public void undo() {
                showVisibility = !showem;
                fireEvent(key, showem, !showem);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * Used by "argo.tee".
     * 
     * @return Returns "true" if we show multiplicities.
     */
    public String getShowMultiplicity() {
        return Boolean.toString(showMultiplicity);
    }

    /**
     * @return Returns <code>true</code> if we show multiplicities.
     */
    public boolean getShowMultiplicityValue() {
        return showMultiplicity;
    }

    /**
     * @param showem <code>true</code> if multiplicity is to be shown.
     */
    public void setShowMultiplicity(String showem) {
        setShowMultiplicity(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if the multiplicity is to be shown.
     */
    public void setShowMultiplicity(final boolean showem) {
        if (showMultiplicity == showem) return;

        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_SHOW_MULTIPLICITY;

            public void redo() {
                showMultiplicity = showem;
                fireEvent(key, !showem, showem);
            }

            public void undo() {
                showMultiplicity = !showem;
                fireEvent(key, showem, !showem);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * Used by "argo.tee".
     * 
     * @return Returns "true" if we show initial values.
     */
    public String getShowInitialValue() {
        return Boolean.toString(showInitialValue);
    }

    /**
     * @return Returns <code>true</code> if we show initial values.
     */
    public boolean getShowInitialValueValue() {
        return showInitialValue;
    }

    /**
     * @param showem <code>true</code> if initial values are to be shown.
     */
    public void setShowInitialValue(String showem) {
        setShowInitialValue(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if initial values are to be shown.
     */
    public void setShowInitialValue(final boolean showem) {
        if (showInitialValue == showem) return;

        Memento memento = new Memento() {
            private final ConfigurationKey key = 
                Notation.KEY_SHOW_INITIAL_VALUE;

            public void redo() {
                showInitialValue = showem;
                fireEvent(key, !showem, showem);
            }

            public void undo() {
                showInitialValue = !showem;
                fireEvent(key, showem, !showem);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * Used by "argo.tee".
     * 
     * @return Returns "true" if we show properties.
     */
    public String getShowProperties() {
        return Boolean.toString(showProperties);
    }

    /**
     * @return Returns <code>true</code> if we show properties.
     */
    public boolean getShowPropertiesValue() {
        return showProperties;
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowProperties(String showem) {
        setShowProperties(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowProperties(final boolean showem) {
        if (showProperties == showem) return;

        Memento memento = new Memento() {
            private final ConfigurationKey key = 
                Notation.KEY_SHOW_PROPERTIES;

            public void redo() {
                showProperties = showem;
                fireEvent(key, !showem, showem);
            }

            public void undo() {
                showProperties = !showem;
                fireEvent(key, showem, !showem);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * Used by "argo.tee".
     * 
     * @return Returns "true" if we show types.
     */
    public String getShowTypes() {
        return Boolean.toString(showTypes);
    }

    /**
     * @return Returns <code>true</code> if we show types.
     */
    public boolean getShowTypesValue() {
        return showTypes;
    }

    /**
     * @param showem <code>true</code> if types are to be shown.
     */
    public void setShowTypes(String showem) {
        setShowTypes(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if types are to be shown.
     */
    public void setShowTypes(final boolean showem) {
        if (showTypes == showem) return;

        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_SHOW_TYPES;

            public void redo() {
                showTypes = showem;
                fireEvent(key, !showem, showem);
            }

            public void undo() {
                showTypes = !showem;
                fireEvent(key, showem, !showem);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }


    /**
     * Used by "argo.tee".
     * 
     * @return Returns "true" if we show stereotypes.
     */
    public String getShowStereotypes() {
        return Boolean.toString(showStereotypes);
    }

    /**
     * @return Returns <code>true</code> if we show stereotypes.
     */
    public boolean getShowStereotypesValue() {
        return showStereotypes;
    }

    /**
     * @param showem <code>true</code> if stereotypes are to be shown.
     */
    public void setShowStereotypes(String showem) {
        setShowStereotypes(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if stereotypes are to be shown.
     */
    public void setShowStereotypes(final boolean showem) {
        if (showStereotypes == showem) return;

        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_SHOW_STEREOTYPES;

            public void redo() {
                showStereotypes = showem;
                fireEvent(key, !showem, showem);
            }

            public void undo() {
                showStereotypes = !showem;
                fireEvent(key, showem, !showem);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * Used by "argo.tee".
     * 
     * @return Returns the shadow width.
     */
    public String getDefaultShadowWidth() {
        return new Integer(defaultShadowWidth).toString();
    }

    /**
     * @return Returns the shadow width.
     */
    public int getDefaultShadowWidthValue() {
        return defaultShadowWidth;
    }

    /**
     * @param newWidth The Shadow Width.
     */
    public void setDefaultShadowWidth(final int newWidth) {
        if (defaultShadowWidth == newWidth) return;

        final int oldValue = defaultShadowWidth;

        Memento memento = new Memento() {
            private final ConfigurationKey key = 
                Notation.KEY_DEFAULT_SHADOW_WIDTH;

            public void redo() {
                defaultShadowWidth = newWidth;
                fireEvent(key, oldValue, newWidth);
            }

            public void undo() {
                defaultShadowWidth = oldValue;
                fireEvent(key, newWidth, oldValue);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * @param width The shadow width to set.
     */
    public void setDefaultShadowWidth(String width) {
        setDefaultShadowWidth(Integer.parseInt(width));
    }
    

    /**
     * Convenience methods to fire notation configuration change events.
     *
     * @param key the ConfigurationKey that is related to the change
     * @param oldValue the old value
     * @param newValue the new value
     */
    private void fireEvent(ConfigurationKey key, int oldValue, int newValue) {
        fireEvent(key, Integer.toString(oldValue), Integer.toString(newValue));
    }

    /**
     * Convenience methods to fire notation configuration change events.
     *
     * @param key the ConfigurationKey that is related to the change
     * @param oldValue the old value
     * @param newValue the new value
     */
    private void fireEvent(ConfigurationKey key, boolean oldValue,
            boolean newValue) {
        fireEvent(key, Boolean.toString(oldValue), Boolean.toString(newValue));
    }
    
    /**
     * Convenience methods to fire notation configuration change events.
     *
     * @param key the ConfigurationKey that is related to the change
     * @param oldValue the old value
     * @param newValue the new value
     */
    private void fireEvent(ConfigurationKey key, String oldValue,
            String newValue) {
        ArgoEventPump.fireEvent(new ArgoNotationEvent(
                ArgoEventTypes.NOTATION_CHANGED, new PropertyChangeEvent(this,
                        key.getKey(), oldValue, newValue)));
    }
}
