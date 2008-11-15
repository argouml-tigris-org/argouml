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

package org.argouml.uml.diagram;

import java.awt.Font;

import org.argouml.notation.Notation;
import org.tigris.gef.undo.Memento;

/**
 * Diagram appearance settings.  This includes basic things like colors and
 * fonts, but also settings that affect more complex things like whether or
 * not certain labels and text fields are displayed.
 * <p>
 * The special value <code>null</code> is used internally to indicate that the
 * default value should be inherited from the next level of settings.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class DiagramSettings {
    
    /**
     * Enumeration representing different stereotype presentation styles
     */
    public enum StereotypeStyle {
        
        // *IMPORTANT* - These MUST remain in order!
        
        /**
         * Textual rendering for stereotype
         */
        TEXTUAL(DiagramAppearance.STEREOTYPE_VIEW_TEXTUAL), 
        /**
         * Stereotype rendered with large icon
         */
        BIG_ICON(DiagramAppearance.STEREOTYPE_VIEW_BIG_ICON), 
        /**
         * Stereotype rendered with small ico
         */
        SMALL_ICON(DiagramAppearance.STEREOTYPE_VIEW_SMALL_ICON);
       
        StereotypeStyle(int value) {
            assert value == this.ordinal();
        }
        

        /**
         * Convert old style int representation to an enum.  The enum returned
         * is the one at the given ordinal (ie this is the inverse of the
         * mapping done by ordinal()).
         * 
         * @param value one of the defined int values in DiagramAppearance
         * @return the corresponding StereotypeView enum
         */
        public static StereotypeStyle getEnum(int value) {
            int counter = 0;
            for (StereotypeStyle sv : StereotypeStyle.values()) {
                if (counter == value) {
                    return sv;
                }
                counter++;
            }
            return null;
        }
    }
    
//    private static final StereotypeView[] stereotypeViewMap;
//    
//    static {
//        stereotypeViewMap = new StereotypeView[3];
//        StereotypeView.
//        stereotypeViewMap[DiagramAppearance.STEREOTYPE_VIEW_TEXTUAL] = 
//            StereotypeView.TEXTUAL;
//        stereotypeViewMap[DiagramAppearance.STEREOTYPE_VIEW_BIG_ICON] = 
//            StereotypeView.BIG_ICON;
//        stereotypeViewMap[DiagramAppearance.STEREOTYPE_VIEW_SMALL_ICON] = 
//            StereotypeView.SMALL_ICON;
//    }
    
    private DiagramSettings parent;

    /* Diagram appearance settings with project scope: */
    private String fontName;
    private Integer fontSize;
    
    // TODO: Can we remove this and have the application manage things directly
    // based on the font?
    private Boolean showBoldNames;
    
    /* Some cached fonts based on the above settings */
    private Font fontPlain;
    private Font fontItalic;
    private Font fontBold;
    private Font fontBoldItalic;
    
    /* Notation settings  */
    private String notationLanguage;
    private Boolean showBidirectionalArrows;
    private Boolean showAssociationNames;
    private Boolean showVisibility;
    private Boolean showMultiplicity;
    private Boolean showInitialValue;
    private Boolean showProperties;
    private Boolean showTypes;
    private Boolean showStereotypes;
    private Boolean showSingularMultiplicities;
    private Integer defaultShadowWidth;
    private StereotypeStyle defaultStereotypeView;

    private Boolean useGuillemets;


    /**
     * Construct an empty project settings with no parent and all values
     * defaulted. <p>
     */
    public DiagramSettings() {
        super();
        parent = null;
    }
    
    
    /**
     * Construct a DiagramSettings object which inherits from the given
     * parent settings (e.g. project default diagram settings).
     * 
     * @param parentSettings settings to inherit from if default values aren't
     * overridden.
     */
    public DiagramSettings(DiagramSettings parentSettings) {
        this();
        parent = parentSettings;
    }


    /**
     * @return Return the notation language.
     */
    public String getNotationLanguage() {
        if (notationLanguage == null) {
            if (parent != null) {
                return parent.getNotationLanguage();
            } else {
                // TODO: What's the correct fallback default?
                return "UML";
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
     * @return Returns <code>true</code> if we show bold names.
     */
    public boolean isShowBoldNames() {
        if (showBoldNames == null) {
            if (parent != null) {
                return parent.isShowBoldNames();
            } else {
                return false;
            }
        }
        return showBoldNames;
    }

    /**
     * @param showem <code>true</code> if names are to be shown in bold font.
     */
    public void setShowBoldNames(final boolean showem) {
        if (showBoldNames != null && showBoldNames == showem) {
            return;
        }

        Memento memento = new Memento() {
            public void redo() {
                showBoldNames = showem;
            }

            public void undo() {
                showBoldNames = !showem;
            }
        };
        doUndoable(memento);
    }

    /**
     * @return Returns <code>true</code> if we show guillemets.
     */
    public boolean isUseGuillemets() {
        if (useGuillemets == null) {
            if (parent != null) {
                return parent.isUseGuillemets();
            } else {
                return false;
            }
        }
        return useGuillemets;
    }


    /**
     * @param showem <code>true</code> if guillemets are to be shown.
     */
    public void setUseGuillemets(final boolean showem) {
        if (useGuillemets != null && useGuillemets == showem) {
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
     * @return Returns <code>true</code> if we show association names.
     */
    public boolean isShowAssociationNames() {
        if (showAssociationNames == null) {
            if (parent != null) {
                return parent.isShowAssociationNames();
            } else {
                return false;
            }
        }
        return showAssociationNames;
    }

    /**
     * @param showem <code>true</code> if association names are to be shown.
     */
    public void setShowAssociationNames(final boolean showem) {
        if (showAssociationNames != null && showAssociationNames == showem) {
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
     * @return Returns <code>true</code> if we show visibilities.
     */
    public boolean isShowVisibility() {
        if (showVisibility == null) {
            if (parent != null) {
                return parent.isShowVisibility();
            } else {
                return false;
            }
        }
        return showVisibility;
    }

    /**
     * @param showem <code>true</code> if visibilities are to be shown.
     */
    public void setShowVisibility(final boolean showem) {
        if (showVisibility != null && showVisibility == showem) {
            return;
        }

        Memento memento = new Memento() {
            public void redo() {
                showVisibility = showem;
            }

            public void undo() {
                showVisibility = !showem;
            }
        };
        doUndoable(memento);
    }


    /**
     * @return Returns <code>true</code> if we show multiplicities.
     */
    public boolean isShowMultiplicity() {
        if (showMultiplicity == null) {
            if (parent != null) {
                return parent.isShowMultiplicity();
            } else {
                return false;
            }
        }
        return showMultiplicity;
    }


    /**
     * @param showem <code>true</code> if the multiplicity is to be shown.
     */
    public void setShowMultiplicity(final boolean showem) {
        if (showMultiplicity != null && showMultiplicity == showem) {
            return;
        }

        Memento memento = new Memento() {
            public void redo() {
                showMultiplicity = showem;
            }

            public void undo() {
                showMultiplicity = !showem;
            }
        };
        doUndoable(memento);
    }

    /**
     * @return Returns <code>true</code> if we show initial values.
     */
    public boolean isShowInitialValue() {
        if (showInitialValue == null) {
            if (parent != null) {
                return parent.isShowInitialValue();
            } else {
                return false;
            }
        }
        return showInitialValue;
    }


    /**
     * @param showem <code>true</code> if initial values are to be shown.
     */
    public void setShowInitialValue(final boolean showem) {
        if (showInitialValue != null && showInitialValue == showem) {
            return;
        }

        Memento memento = new Memento() {
            public void redo() {
                showInitialValue = showem;
            }

            public void undo() {
                showInitialValue = !showem;
            }
        };
        doUndoable(memento);

    }


    /**
     * @return Returns <code>true</code> if we show properties.
     */
    public boolean isShowProperties() {
        if (showProperties == null) {
            if (parent != null) {
                return parent.isShowProperties();
            } else {
                return false;
            }
        }
        return showProperties;
    }


    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowProperties(final boolean showem) {
        if (showProperties != null && showProperties == showem) {
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
     * @return Returns <code>true</code> if we show types.
     */
    public boolean isShowTypes() {
        if (showTypes == null) {
            if (parent != null) {
                return parent.isShowTypes();
            } else {
                return false;
            }
        }
        return showTypes;
    }


    /**
     * @param showem <code>true</code> if types are to be shown.
     */
    public void setShowTypes(final boolean showem) {
        if (showTypes != null && showTypes == showem) {
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
     * @return Returns <code>true</code> if we show stereotypes.
     */
    public boolean isShowStereotypes() {
        if (showStereotypes == null) {
            if (parent != null) {
                return parent.isShowStereotypes();
            } else {
                return true;
            }
        }
        return showStereotypes;
    }


    /**
     * @param showem <code>true</code> if stereotypes are to be shown.
     */
    public void setShowStereotypes(final boolean showem) {
        if (showStereotypes != null && showStereotypes == showem) {
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

    /**
     * @return Returns <code>true</code> if we show  "1" Multiplicities.
     */
    public boolean isShowSingularMultiplicities() {
        if (showSingularMultiplicities == null) {
            if (parent != null) {
                return parent.isShowSingularMultiplicities();
            } else {
                return false;
            }
        }
        return showSingularMultiplicities;
    }


    /**
     * @param showem <code>true</code> if "1" Multiplicities are to be shown.
     */
    public void setShowSingularMultiplicities(final boolean showem) {
        if (showSingularMultiplicities != null 
                && showSingularMultiplicities == showem) {
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
     * @return Returns <code>true</code> if we show the arrows when
     * both association ends of an association are navigable.
     */
    public boolean isShowBidirectionalArrows() {
        if (showBidirectionalArrows == null) {
            if (parent != null) {
                return parent.isShowBidirectionalArrows();
            } else {
                return false;
            }
        }
        return showBidirectionalArrows;
    }

    /**
     * @param showem <code>true</code> if both arrows are to be shown when
     * both association ends of an association are navigable.
     */
    public void setShowBidirectionalArrows(final boolean showem) {
        if (showBidirectionalArrows != null 
                && showBidirectionalArrows == showem) {
            return;
        }

        Memento memento = new Memento() {
            public void redo() {
                showBidirectionalArrows = showem;
            }

            public void undo() {
                showBidirectionalArrows = !showem;
            }
        };
        doUndoable(memento);

    }


    /**
     * @return Returns the shadow width.
     */
    public int getDefaultShadowWidth() {
        if (defaultShadowWidth == null) {
            if (parent != null) {
                return parent.getDefaultShadowWidth();
            } else {
                return 0;
            }
        }
        return defaultShadowWidth;
    }

    /**
     * @param newWidth The Shadow Width.
     */
    public void setDefaultShadowWidth(final int newWidth) {
        if (defaultShadowWidth != null && defaultShadowWidth == newWidth) {
            return;
        }

        final Integer oldValue = defaultShadowWidth;

        Memento memento = new Memento() {
            public void redo() {
                defaultShadowWidth = newWidth;
            }

            public void undo() {
                defaultShadowWidth = oldValue;
            }
        };
        doUndoable(memento);

    }


    /**
     * @return Returns the default stereotype view
     * TODO: Enumeration here?
     */
    public StereotypeStyle getDefaultStereotypeView() {
        if (defaultStereotypeView == null) {
            if (parent != null) {
                return parent.getDefaultStereotypeView();
            } else {
                return StereotypeStyle.TEXTUAL;
            }
        }
        return defaultStereotypeView;
    }

    /**
     * @return Returns the default stereotype view as an int compatible with
     * old users of DiagramAppearance-defined ints.
     * @deprecated for 0.27.2 by tfmorris.  For backward compatibility only.
     */
    public int getDefaultStereotypeViewInt() {
        return getDefaultStereotypeView().ordinal();
    }
    
    /**
     * @param newView the default stereotype view
     * @deprecated for 0.27.2 by tfmorris. Not for use in new code. Only for
     *             help in transitioning to enum based methods. Use
     *             {@link #setDefaultStereotypeView(StereotypeStyle)}.
     */
    public void setDefaultStereotypeView(final int newView) {
        StereotypeStyle sv = StereotypeStyle.getEnum(newView);
        if (sv == null) {
            throw new IllegalArgumentException("Bad argument " + newView);
        }
        setDefaultStereotypeView(sv);
    }

    /**
     * @param newView the default stereotype view
     */
    public void setDefaultStereotypeView(final StereotypeStyle newView) {
        if (defaultStereotypeView != null && defaultStereotypeView == newView) {
            return;
        }

        final StereotypeStyle oldValue = defaultStereotypeView;

        Memento memento = new Memento() {
            public void redo() {
                defaultStereotypeView = newView;
            }

            public void undo() {
                defaultStereotypeView = oldValue;
            }
        };
        doUndoable(memento);
    }


    /**
     * Diagram font name. <p>
     * 
     * @return diagram font name.
     */
    public String getFontName() {
        if (fontName == null) {
            if (parent != null) {
                return parent.getFontName();
            } else {
                return "Dialog";
            }
        }
        return fontName;
    }

    /**
     * Diagram font name.
     * @param newFontName diagram font name.
     */
    public void setFontName(String newFontName) {
        if (fontName != null && fontName.equals(newFontName)) {
            return;
        }
        fontName = newFontName;
        recomputeFonts();
    }

    /**
     * Diagram font size. <p>
     *
     * @return diagram font size.
     */
    public int getFontSize() {
        if (fontSize == null) {
            if (parent != null) {
                return parent.getFontSize();
            } else {
                return 10;
            }
        }
        return fontSize;
    }

    /**
     * Diagram font size.
     * @param newFontSize diagram font size.
     */
    public void setFontSize(int newFontSize) {
        if (fontSize != null && fontSize == newFontSize) {
            return;
        }
        fontSize = newFontSize;
        recomputeFonts();
    }

    private void recomputeFonts() {
        String name = getFontName();
        int size = getFontSize();
//        if (size == 0) {
//            size = 10;
//        }
        
        if (name != null && !"".equals(name) && size > 0) {
            fontPlain = new Font(name, Font.PLAIN, size);
            fontItalic = new Font(name, Font.ITALIC, size);
            fontBold = new Font(name, Font.BOLD, size);
            fontBoldItalic = new Font(name, Font.BOLD | Font.ITALIC, size);
        }
    }

    /**
     * Returns the Plain diagram font which corresponds
     * to selected parameters.
     *
     * @return plain diagram font
     */
    public Font getFontPlain() {
        return fontPlain;
    }

    /**
     * Returns the Italic diagram font which corresponds
     * to selected parameters.
     *
     * @return italic diagram font
     */
    public Font getFontItalic() {
        return fontItalic;
    }

    /**
     * Returns the Bold diagram font which corresponds
     * to selected parameters.
     *
     * @return bold diagram font
     */
    public Font getFontBold() {
        return fontBold;
    }

    /**
     * Returns the Bold-Italic diagram font which corresponds
     * to selected parameters.
     *
     * @return bold-italic diagram font
     */
    public Font getFontBoldItalic() {
        return fontBoldItalic;
    }

    /**
     * Utility function to convert a font style integer into a Font.
     *
     * @param fontStyle the style; see the predefined constants in Font
     * @return the Font that corresponds to the style
     */
    public Font getFont(int fontStyle) {
        if ((fontStyle & Font.ITALIC) != 0) {
            if ((fontStyle & Font.BOLD) != 0) {
                return fontBoldItalic;
            } else {
                return fontItalic;
            }
        } else {
            if ((fontStyle & Font.BOLD) != 0) {
                return fontBold;
            } else {
                return fontPlain;
            }
        }
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

