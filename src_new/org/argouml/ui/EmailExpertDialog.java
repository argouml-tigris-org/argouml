// Copyright (c) 1996-99 The Regents of the University of California. All
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

package org.argouml.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Category;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Poster;
import org.argouml.cognitive.ToDoItem;
import org.argouml.swingext.LabelledLayout;

/** The email expert dialog does not work and is in
 * desperate need of some attention.
 *
 * Ideally, this would allow users to directly
 * contact the developers responsible for a piece
 * of code.
 *
 * Enabling this feature would go along way
 * to developing a fully collaborative environment
 * within argo.
 */
public class EmailExpertDialog extends ArgoDialog {
    protected static Category cat = Category.getInstance(EmailExpertDialog.class);

  ////////////////////////////////////////////////////////////////
  // instance variables
    /** This field sets the email of the recipient.
     * As yet this doesn not work, nor can the
     * user access a list of contributors to a
     * particular argo project.
     */    
  protected JTextField _to;
  protected JTextField _cc;
  /** The subject line should be automatically
   * generated based on the class or the
   * diagram.
   */  
  protected JTextField _subject;
  protected JTextArea  _body;
  /** Does not work.
   */  
  /**
   */  
  protected ToDoItem _target;

  ////////////////////////////////////////////////////////////////
  // constructors

    public EmailExpertDialog() {
        super(ProjectBrowser.getInstance(), "Send Email to an Expert", true);
        
        getOkButton().setText("Send");
        
        _to = new JTextField(30);
        _cc = new JTextField(30);
        _subject = new JTextField(30);
        _body = new JTextArea(10,30);
    
        JLabel toLabel = new JLabel("To:");
        JLabel ccLabel = new JLabel("Cc:");
        JLabel subjectLabel = new JLabel("Subject:");
    
        JPanel panel = new JPanel(new LabelledLayout(labelGap, componentGap));

        toLabel.setLabelFor(_to);
        panel.add(toLabel);
        panel.add(_to);

        ccLabel.setLabelFor(_cc);
        panel.add(ccLabel);
        panel.add(_cc);
    
        subjectLabel.setLabelFor(_subject);
        panel.add(subjectLabel);
        panel.add(_subject);

        JScrollPane bodyScroller = new JScrollPane(_body);
        bodyScroller.setPreferredSize(new Dimension(100,50));
        panel.add(bodyScroller);
        
        setContent(panel);
        setButtons(new JButton[] { getOkButton(), getCancelButton() }, getOkButton());
    }
    
  public void setTarget(Object t) {
    _target = (ToDoItem) t;
    Poster p = _target.getPoster();
    _to.setText(p.getExpertEmail());
    _subject.setText(_target.getHeadline());
  }

  ////////////////////////////////////////////////////////////////
  // event handlers
  public void actionPerformed(ActionEvent e) {
      super.actionPerformed(e);   
      if (e.getSource() == getOkButton()) {
          Designer dsgr = Designer.TheDesigner;
          String to = _to.getText();
          String cc = _cc.getText();
          String subject = _subject.getText();
          cat.debug("sending email!");
      }
      else if (e.getSource() == getCancelButton()) {
        cat.debug("cancel");
      }
  }  
} /* end class EmailExpertDialog */
