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

// Copyright (c) 2008, 2009 The Regents of the University of California. All
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
import java.beans.PropertyChangeEvent;

import org.argouml.application.events.ArgoDiagramAppearanceEvent;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.kernel.ProfileConfiguration;
import org.argouml.notation.NotationSettings;
import org.tigris.gef.undo.Memento;

/**
 * Diagram appearance settings.  This includes basic things like colors and
 * fonts, as well as the {@link NotationSettings} which contain all the settings
 * that control text formatting.
 * <p>
 * The settings are designed to work in a hierarchical fashion with any settings
 * that are defaulted at the current level inheriting from the next level in
 * the hierarchy.  A typical hierarchy would be Fig->StyleSheet->Project where 
 * a style sheet is a named set of attributes that can be applied as a set to
 * a fig (perhaps in conjunction with a stereotype).  The hierarchy which is
 * currently used in ArgoUML is Fig->Diagram->Project, although there's no
 * support for changing anything but the Project (and a few of the attributes
 * managed directly by GEF).
 * <p>
 * Notation settings have a semantic meaning in the UML, which
 * defines the difference between notation settings and 
 * diagram appearance settings.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class DiagramSettings {

    /*
     * The special value <code>null</code> is used internally to indicate that
     * the default value should be inherited from the next level of settings.
     */
    
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
         * Stereotype rendered with small icon
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
    
    /**
     * Next level in the settings hierarchy to inherit from if the value
     * isn't set (ie is <default>) at the current level.
     */
    private DiagramSettings parent;
    
    private NotationSettings notationSettings;

    /* Diagram appearance settings with project scope: */
    private String fontName;
    private Integer fontSize;
    
    // We can not remove this and have the application manage things directly
    // based on the font, since only the names should be bold.
    private Boolean showBoldNames;

    private Boolean showBidirectionalArrows;
    
    private Integer defaultShadowWidth;
    
    private StereotypeStyle defaultStereotypeView;

    /* Some cached fonts based on the above settings */
    private Font fontPlain;
    private Font fontItalic;
    private Font fontBold;
    private Font fontBoldItalic;

    /**
     * Construct an empty diagram settings with no parent and all values
     * defaulted. <p>
     */
    public DiagramSettings() {
        this(null);
    }
    
    /**
     * Construct a DiagramSettings object which inherits from the given
     * parent settings (e.g. project default diagram settings).
     * 
     * @param parentSettings settings to inherit from if default values aren't
     * overridden.
     */
    public DiagramSettings(DiagramSettings parentSettings) {
        super();
        parent = parentSettings;
        if (parentSettings == null) {
            notationSettings = new NotationSettings();
        } else {
            notationSettings = 
                new NotationSettings(parent.getNotationSettings());
        }
        recomputeFonts();
    }

    /**
     * Send all events when the settings are changed to refresh
     * anything rendered with these settings.
     */
    public void notifyOfChangedSettings() {
        /*
         * Since body ever looks
         * at the type of the diagram appearance event, we can simplify from
         * sending every existing event to one event only. But since there is no
         * catch-all event defined, we just use one. Rationale: reduce the
         * number of total refreshes of the drawing.
         */
        ConfigurationKey key = 
            Configuration.makeKey("diagramappearance", "all");
        ArgoEventPump.fireEvent(new ArgoDiagramAppearanceEvent(
                ArgoEventTypes.DIAGRAM_FONT_CHANGED, new PropertyChangeEvent(
                        this, key.getKey(),  "0", "0")));
    }

    /**
     * Initialize the diagram settings with application default values 
     * from the Configuration retrieved from disk.
     */
    public void initFromConfiguration() {
        setShowBoldNames(Configuration.getBoolean(
                DiagramAppearance.KEY_SHOW_BOLD_NAMES));
        
        setShowBidirectionalArrows(!Configuration.getBoolean(
                DiagramAppearance.KEY_HIDE_BIDIRECTIONAL_ARROWS, true));
        
        setDefaultShadowWidth(Configuration.getInteger(
                DiagramAppearance.KEY_DEFAULT_SHADOW_WIDTH, 1));

        setDefaultStereotypeView(Configuration.getInteger(
                ProfileConfiguration.KEY_DEFAULT_STEREOTYPE_VIEW,
                DiagramAppearance.STEREOTYPE_VIEW_TEXTUAL));

        setFontName(
                DiagramAppearance.getInstance().getConfiguredFontName());
        setFontSize(
                Configuration.getInteger(DiagramAppearance.KEY_FONT_SIZE));
    }

    /**
     * @return Returns the notationSettings.
     */
    public NotationSettings getNotationSettings() {
        return notationSettings;
    }


    /**
     * @param notationSettings The notationSettings to set.
     */
    public void setNotationSettings(NotationSettings notationSettings) {
        this.notationSettings = notationSettings;
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
        // If we've got a local (uninherited) font name or size or if we've got
        // no parent to inherit from recompute our cached fonts
        if ((fontName != null && !"".equals(fontName) && fontSize != null)
                || parent == null) {
            String name = getFontName();
            int size = getFontSize();
            fontPlain = new Font(name, Font.PLAIN, size);
            fontItalic = new Font(name, Font.ITALIC, size);
            fontBold = new Font(name, Font.BOLD, size);
            fontBoldItalic = new Font(name, Font.BOLD | Font.ITALIC, size);
        } else {
            fontPlain = null;
            fontItalic = null;
            fontBold = null;
            fontBoldItalic = null;
        }
    }

    /**
     * Returns the Plain diagram font which corresponds
     * to selected parameters.
     *
     * @return plain diagram font
     */
    public Font getFontPlain() {
        if (fontPlain == null) {
            return parent.getFontPlain();
        }
        return fontPlain;
    }

    /**
     * Returns the Italic diagram font which corresponds
     * to selected parameters.
     *
     * @return italic diagram font
     */
    public Font getFontItalic() {
        if (fontItalic == null) {
            return parent.getFontItalic();
        }
        return fontItalic;
    }

    /**
     * Returns the Bold diagram font which corresponds
     * to selected parameters.
     *
     * @return bold diagram font
     */
    public Font getFontBold() {
        if (fontBold == null) {
            return parent.getFontBold();
        }
        return fontBold;
    }

    /**
     * Returns the Bold-Italic diagram font which corresponds
     * to selected parameters.
     *
     * @return bold-italic diagram font
     */
    public Font getFontBoldItalic() {
        if (fontBoldItalic == null) {
            return parent.getFontBoldItalic();
        }
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
                return getFontBoldItalic();
            } else {
                return getFontItalic();
            }
        } else {
            if ((fontStyle & Font.BOLD) != 0) {
                return getFontBold();
            } else {
                return getFontPlain();
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

