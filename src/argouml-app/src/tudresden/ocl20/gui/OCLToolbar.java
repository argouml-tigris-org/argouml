/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * OCL Compiler                                                      *
 * Copyright (C) 2001 Steffen Zschaler (sz9@inf.tu-dresden.de).       *
 * All rights reserved.                                              *
 *                                                                   *
 * This work is free software; you can redistribute it and/or        *
 * modify it under the terms of the GNU Library General Public       *
 * License as published by the Free Software Foundation; either      *
 * version 2 of the License, or (at your option) any later version.  *
 *                                                                   *
 * This work is distributed in the hope that it will be useful,      *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU *
 * Library General Public License for more details.                  *
 *                                                                   *
 * You should have received a copy of the GNU Library General Public *
 * License along with this library; if not, write to the             *
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,      *
 * Boston, MA  02111-1307, USA.                                      *
 *                                                                   *
 * To submit a bug report, send a comment, or get the latest news on *
 * this project and other projects, please visit the web site:       *
 * http://www-st.inf.tu-dresden.de/ (Chair home page) or             *
 * http://www-st.inf.tu-dresden.de/ocl/ (project home page)          *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package tudresden.ocl20.gui;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.lang.ref.WeakReference;

import java.util.ArrayList;

/** 
 * A quick bar for the {@link OCLEditor}
 *
 * @author  sz9
 */
public class OCLToolbar extends JToolBar implements ActionListener {

  /**
	 * 
	 */
	private static final long serialVersionUID = -4484972493951577184L;

/**
   * The frame used for floating the tool bar.
   */
  private JFrame m_jfFloatFrame;

  /**
   * The OCLEditor for which this is the quick bar.
   */
  private WeakReference m_wrocleEditor;
  
  /** Creates new OCLToolbar */
  public OCLToolbar() {
    super();
    
    initComponents();
  }
  
  /**
   * Set the editor owning this tool bar.
   */
  public void setEditor (OCLEditor ocle) {
    m_wrocleEditor = new WeakReference (ocle);
  }
  
  public OCLEditor getEditor() {
    if (m_wrocleEditor == null) {
      return null;
    }
    else {
      return (OCLEditor) m_wrocleEditor.get();
    }
  }
  
  /**
   * Overridden to correctly hide the float frame, if it was open.
   */
  public void setVisible (boolean fVisible) {
    if (m_jfFloatFrame != null) {
      if (getUI() instanceof javax.swing.plaf.basic.BasicToolBarUI) {
        if (((javax.swing.plaf.basic.BasicToolBarUI) getUI()).isFloating()) {
          m_jfFloatFrame.setVisible (fVisible);
        }
      }
    }
    
    super.setVisible (fVisible);
  }
  
  /**
   * Overridden to allow access to the free float frame.
   */
  public void setUI (javax.swing.plaf.ToolBarUI tbui) {
    if (tbui instanceof javax.swing.plaf.metal.MetalToolBarUI) {
      super.setUI (new javax.swing.plaf.metal.MetalToolBarUI() {
        protected JFrame createFloatingFrame (JToolBar jtb) {
          if (jtb == OCLToolbar.this) {
            if (m_jfFloatFrame == null) {
              m_jfFloatFrame = super.createFloatingFrame (jtb);
              m_jfFloatFrame.setTitle ("Syntax Assistant");
            }
            
            return m_jfFloatFrame;
          }
          else {
            return super.createFloatingFrame (jtb);
          }
        }
      });
    }
    else if (tbui instanceof javax.swing.plaf.basic.BasicToolBarUI) {
      super.setUI (new javax.swing.plaf.basic.BasicToolBarUI() {
        protected JFrame createFloatingFrame (JToolBar jtb) {
          if (jtb == OCLToolbar.this) {
            if (m_jfFloatFrame == null) {
              m_jfFloatFrame = super.createFloatingFrame (jtb);
              m_jfFloatFrame.setTitle ("Syntax Assistant");
            }
            
            return m_jfFloatFrame;
          }
          else {
            return super.createFloatingFrame (jtb);
          }
        }
      });
    }
    else {
      throw new IllegalArgumentException ("L&F not valid with OCLToolbar: " + tbui);
    }
  }
  
  private void initComponents() {
    setFloatable (true);
    
    JComboBox jcb = new JComboBox (new ActionItem[] {
      new ActionItem ("General", -1),
      new ActionItem ("inv", 0),
      new ActionItem ("pre", 0),
      new ActionItem ("post", 0),
      new ActionItem ("self", 1),
      new ActionItem ("@pre", 2),
      new ActionItem ("result", 3)
    });
    jcb.addActionListener (this);
    add (jcb);
    
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // Inv, Pre, Post
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            null,
            "\n" + sOCLText + " ",
            new String[] {"tmp"});
        ocle.addConstraintText (
            new String[] {"constraint name"},
            ": ",
            new String[] {"constraint expression"});
      }
    });
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // Self
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            null,
            "self.",
            new String[] {"attribute, association or query method"});
      }
    });
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // @pre
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"attribute or association"},
            "@pre ",
            null);
      }
    });
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // result
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            null,
            "result ",
            null);
      }
    });
    
    jcb = new JComboBox (new ActionItem[] {
      new ActionItem ("Basic Operators", -1),
      new ActionItem ("=", 4),
      new ActionItem ("<>", 4),
      new ActionItem ("<", 4),
      new ActionItem (">", 4),
      new ActionItem ("<=", 4),
      new ActionItem (">=", 4),
      new ActionItem ("()", 5)
    });
    jcb.addActionListener (this);
    add (jcb);

    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // simple ops
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"expression"},
            " " + sOCLText + " ",
            new String[] {"expression"});
      }
    });
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // parens
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"tmp"},
            ")",
            null);
        ocle.addConstraintText (
            null,
            "(",
            new String[] {"expression"});
      }
    });
    
    jcb = new JComboBox (new ActionItem[] {
      new ActionItem ("Numbers", -1),
      new ActionItem ("+", 6),
      new ActionItem ("-", 6),
      new ActionItem ("*", 6),
      new ActionItem ("/", 6),
      new ActionItem ("mod", 7),
      new ActionItem ("div", 7),
      new ActionItem ("abs", 8),
      new ActionItem ("max", 7),
      new ActionItem ("min", 7),
      new ActionItem ("round", 8),
      new ActionItem ("floor", 8)
    });
    jcb.addActionListener (this);
    add (jcb);

    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // numeric ops
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"numeric expression"},
            " " + sOCLText + " ",
            new String[] {"numeric expression"});
      }
    });
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // mod, div, max, min
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"tmp"},
            ")",
            null);
        ocle.addConstraintText (
            new String[] {"numeric expression"},
            "." + sOCLText + " (",
            new String[] {"numeric expression"});
      }
    });
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // abs, round, floor
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"numeric expression"},
            "." + sOCLText + " ",
            null);
      }
    });
    
    jcb = new JComboBox (new ActionItem[] {
      new ActionItem ("Strings", -1),
      new ActionItem ("concat", 9),
      new ActionItem ("size", 10),
      new ActionItem ("toLower", 10),
      new ActionItem ("toUpper", 10),
      new ActionItem ("substring", 11)
    });
    jcb.addActionListener (this);
    add (jcb);
    
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // concat
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"tmp"},
            ")",
            null);
        ocle.addConstraintText (
            new String[] {"string expression"},
            "." + sOCLText + " (",
            new String[] {"string expression"});
      }
    });
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // size, toLower, toUpper
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"string expression"},
            "." + sOCLText + " ",
            null);
      }
    });
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // substring
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"tmp"},
            ")",
            null);
        ocle.addConstraintText (
            new String[] {"tmp"},
            ", ",
            new String[] {"integer expression"});
        ocle.addConstraintText (
            new String[] {"string expression"},
            "." + sOCLText + " (",
            new String[] {"integer expression"});
      }
    });
    
    jcb = new JComboBox (new ActionItem[] {
      new ActionItem ("Booleans", -1),
      new ActionItem ("or", 12),
      new ActionItem ("and", 12),
      new ActionItem ("xor", 12),
      new ActionItem ("not", 13),
      new ActionItem ("implies", 12),
      new ActionItem ("if then else", 14)
    });
    jcb.addActionListener (this);
    add (jcb);
    
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // basic ops
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"boolean expression"},
            " " + sOCLText + " ",
            new String[] {"boolean expression"});
      }
    });
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // not
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            null,
            sOCLText + " ",
            new String[] {"boolean expression"});
      }
    });
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // if then else
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"tmp"},
            " endif",
            null);
        ocle.addConstraintText (
            new String[] {"tmp"},
            " else ",
            new String[] {"expression"});
        ocle.addConstraintText (
            new String[] {"tmp"},
            " then ",
            new String[] {"expression"});
        ocle.addConstraintText (
            null,
            "if ",
            new String[] {"boolean expression"});
      }
    });
    
    jcb = new JComboBox (new ActionItem[] {
      new ActionItem ("Collections", -1),
      new ActionItem ("-----------", ActionItem.DUMMY_ACTION),
      new ActionItem ("General", ActionItem.DUMMY_ACTION),
      new ActionItem ("-----------", ActionItem.DUMMY_ACTION),
      new ActionItem ("Collection {}", "Collection", 15),
      new ActionItem ("Set {}", "Set", 15),
      new ActionItem ("Bag {}", "Bag", 15),
      new ActionItem ("Sequence {}", "Sequence", 15),
      new ActionItem ("size", 16),
      new ActionItem ("count", 17),
      new ActionItem ("isEmpty", 16),
      new ActionItem ("notEmpty", 16),
      new ActionItem ("includes", 17),
      new ActionItem ("includesAll", 18),
      new ActionItem ("iterate", 19),
      new ActionItem ("exists", 19),
      new ActionItem ("forAll", 19),
      new ActionItem ("collect", 19),
      new ActionItem ("select", 19),
      new ActionItem ("reject", 19),
      new ActionItem ("union", 18),
      new ActionItem ("intersection", 18),
      new ActionItem ("including", 17),
      new ActionItem ("excluding", 17),
      new ActionItem ("sum", 20),
      new ActionItem ("-----------", ActionItem.DUMMY_ACTION),
      new ActionItem ("Sets", ActionItem.DUMMY_ACTION),
      new ActionItem ("-----------", ActionItem.DUMMY_ACTION),
      new ActionItem ("-", 21),
      new ActionItem ("symmetricDifference", 22),
      new ActionItem ("-----------", ActionItem.DUMMY_ACTION),
      new ActionItem ("Sequences", ActionItem.DUMMY_ACTION),
      new ActionItem ("-----------", ActionItem.DUMMY_ACTION),
      new ActionItem ("first", 23),
      new ActionItem ("last", 23),
      new ActionItem ("at", 24),
      new ActionItem ("append", 25),
      new ActionItem ("prepend", 25),
      new ActionItem ("subSequence", 26)
    });
    jcb.addActionListener (this);
    add (jcb);
    
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // collection constructors
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"tmp"},
            "}",
            null);
        ocle.addConstraintText (
            null,
            sOCLText + " {",
            new String[] {"element list"});
      }
    });
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // size, isEmpty, notEmpty
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"collection expression"},
            "->" + sOCLText,
            null);
      }
    });
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // count, includes, including, excluding
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"tmp"},
            ")",
            null);
        ocle.addConstraintText (
            new String[] {"collection expression"},
            "->" + sOCLText + " (",
            new String[] {"element expression"});
      }
    });
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // includesAll, union, intersection
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"tmp"},
            ")",
            null);
        ocle.addConstraintText (
            new String[] {"collection expression"},
            "->" + sOCLText + " (",
            new String[] {"collection expression"});
      }
    });
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // iterate etc
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"tmp"},
            ")",
            null);
        ocle.addConstraintText (
            new String[] {"tmp"},
            " | ",
            new String[] {"expression"});
        ocle.addConstraintText (
            new String[] {"tmp"},
            " : ",
            new String[] {"type"});
        ocle.addConstraintText (
            new String[] {"collection expression "},
            "->" + sOCLText + " (",
            new String[] {"element variable"});
      }
    });
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // sum
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"collection expression"},
            "->" + sOCLText + "()",
            null);
      }
    });
    
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // set minus
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"set expression"},
            " " + sOCLText + " ",
            new String[] {"set expression"});
      }
    });
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // symmetricDifference
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"tmp"},
            ")",
            null);
        ocle.addConstraintText (
            new String[] {"set expression"},
            "->" + sOCLText + " (",
            new String[] {"set expression"});
      }
    });
    
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // first, last
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"sequence expression"},
            "->" + sOCLText,
            null);
      }
    });
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // at
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"tmp"},
            ")",
            null);
        ocle.addConstraintText (
            new String[] {"sequence expression"},
            "->" + sOCLText + " (",
            new String[] {"integer expression"});
      }
    });
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // append, prepend
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"tmp"},
            ")",
            null);
        ocle.addConstraintText (
            new String[] {"sequence expression"},
            "->" + sOCLText + " (",
            new String[] {"element expression"});
      }
    });
    ActionItem.addShortCut (new ActionItem.ShortCutAction() { // subSequence
      public void performShortCut (String sOCLText, OCLEditor ocle) {
        ocle.addConstraintText (
            new String[] {"tmp"},
            ")",
            null);
        ocle.addConstraintText (
            new String[] {"tmp"},
            ", ",
            new String[] {"integer expression"});
        ocle.addConstraintText (
            new String[] {"sequence expression"},
            "->" + sOCLText + " (",
            new String[] {"integer expression"});
      }
    });
  }
  
  public void actionPerformed(final ActionEvent e) {
    JComboBox jcb = (JComboBox) e.getSource();
    ActionItem ai = (ActionItem) jcb.getSelectedItem();
    
    if (ai != null) {
      ai.perform (jcb, getEditor());
    }
  }

  private static final class ActionItem {
    public static final int DUMMY_ACTION = Integer.MAX_VALUE;
    
    public static interface ShortCutAction {
      public void performShortCut (String sOCLText, OCLEditor ocle);
    }
    
    private static ArrayList s_aalActions = new ArrayList();
    
    private String m_sCaption;
    private String m_sOCLText;
    private int m_nActionIdx;
    
    public ActionItem (String sCaption, String sOCLText, int nActionIdx) {
      super();
      
      m_sCaption = sCaption;
      m_sOCLText = sOCLText;
      m_nActionIdx = nActionIdx;
    }
    
    public ActionItem (String sOCLText, int nActionID) {
      this (sOCLText, sOCLText, nActionID);
    }
    
    public String toString() {
      return getCaption();
    }
    
    public String getCaption() {
      return m_sCaption;
    }
    
    public String getOCLText() {
      return m_sOCLText;
    }

    public void perform (JComboBox jcb, OCLEditor ocle) {
      if (m_nActionIdx >= 0) {
        // React
        if ((m_nActionIdx != DUMMY_ACTION) &&
             (ocle != null)) {
          ShortCutAction sca = (ShortCutAction) s_aalActions.get (m_nActionIdx);
          
          sca.performShortCut (getOCLText(), ocle);
        }
        
        // Reset combo box, assuming that 0 is the index with no associated action
        jcb.setSelectedIndex (0);
      }
    }
    
    public static void addShortCut (ShortCutAction sca) {
      s_aalActions.add (sca);
    }
  }
}