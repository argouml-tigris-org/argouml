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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Category;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Poster;
import org.argouml.cognitive.ToDoItem;

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
public class EmailExpertDialog extends JDialog implements ActionListener {
    protected static Category cat = Category.getInstance(EmailExpertDialog.class);

  ////////////////////////////////////////////////////////////////
  // instance variables
    /** This field sets the email of the recipient.
     * As yet this doesn not work, nor can the
     * user access a list of contributors to a
     * particular argo project.
     */    
  protected JTextField _to = new JTextField();
  protected JTextField _cc = new JTextField();
  /** The subject line should be automatically
   * generated based on the class or the
   * diagram.
   */  
  protected JTextField _subject = new JTextField();
  protected JTextArea  _body = new JTextArea();
  /** Does not work.
   */  
  protected JButton _sendButton = new JButton("Send");
  /**
   */  
  protected JButton _cancelButton = new JButton("Cancel");
  protected ToDoItem _target;

  ////////////////////////////////////////////////////////////////
  // constructors

  public EmailExpertDialog() {
    super(ProjectBrowser.getInstance(), "Send Email to an Expert");
    JLabel toLabel = new JLabel("To:");
    JLabel ccLabel = new JLabel("Cc:");
    JLabel subjectLabel = new JLabel("Subject:");

    setLocation(200, 100);
    setSize(new Dimension(400, 350));
    getContentPane().setLayout(new BorderLayout());
    JPanel top = new JPanel();
    GridBagLayout gb = new GridBagLayout();
    top.setLayout(gb);
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.0;
    c.ipadx = 3; c.ipady = 3;

    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 0;
    gb.setConstraints(toLabel, c);
    top.add(toLabel);
    c.gridy = 1;
    gb.setConstraints(ccLabel, c);
    top.add(ccLabel);
    c.gridy = 2;
    gb.setConstraints(subjectLabel, c);
    top.add(subjectLabel);

    c.weightx = 1.0;
    c.gridx = 1;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridy = 0;
    gb.setConstraints(_to, c);
    top.add(_to);
    c.gridy = 1;
    gb.setConstraints(_cc, c);
    top.add(_cc);
    c.gridy = 2;
    gb.setConstraints(_subject, c);
    top.add(_subject);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    JPanel buttonInner = new JPanel(new GridLayout(1, 2));
    buttonInner.add(_sendButton);
    buttonInner.add(_cancelButton);
    buttonPanel.add(buttonInner);

    getContentPane().add(top, BorderLayout.NORTH);
    getContentPane().add(new JScrollPane(_body), BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);

    getRootPane().setDefaultButton(_sendButton);
    _sendButton.addActionListener(this);
    _cancelButton.addActionListener(this);
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
    if (e.getSource() == _sendButton) {
      Designer dsgr = Designer.TheDesigner;
      String to = _to.getText();
      String cc = _cc.getText();
      String subject = _subject.getText();
      cat.debug("sending email!");
      setVisible(false);
      dispose();
    }
    if (e.getSource() == _cancelButton) {
      cat.debug("cancel");
      setVisible(false);
      dispose();
    }
  }

} /* end class EmailExpertDialog */
