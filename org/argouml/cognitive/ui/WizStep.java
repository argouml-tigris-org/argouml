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

package org.argouml.cognitive.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.cognitive.ToDoItem;
import org.argouml.kernel.Wizard;
import org.argouml.ui.SpacerPanel;
import org.argouml.util.osdep.StartBrowser;

/** Each Critic may provide a Wizard to help fix the problem it
 *  identifies.  The "Next>" button will advance through the steps of
 *  the wizard, and increase the blue progress bar on the ToDoItem
 *  "sticky note" icon in ToDo tree pane.
 *
 * @see org.argouml.cognitive.critics.Critic
 * @see org.argouml.kernel.Wizard
 */

public class WizStep extends JPanel
implements TabToDoTarget, ActionListener, DocumentListener {
    protected static Category cat = Category.getInstance(WizStep.class);

  ////////////////////////////////////////////////////////////////
  // constants
  private static final String BUNDLE = "Cognitive";
  public static final ImageIcon WIZ_ICON = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Wiz", "Wiz");

  ////////////////////////////////////////////////////////////////
  // instance variables

  JPanel  _mainPanel = new JPanel();
  JButton _backButton = new JButton(Argo.localize(BUNDLE, "button.back"));
  JButton _nextButton = new JButton(Argo.localize(BUNDLE, "button.next"));
  JButton _finishButton = new JButton(Argo.localize(BUNDLE, "button.finish"));
  JButton _helpButton = new JButton(Argo.localize(BUNDLE, "button.help"));
  JPanel  _buttonPanel = new JPanel();

  Object _target;

    static final protected void setMnemonic(JButton b, String key) {
	String m = Argo.localize(BUNDLE, key);
	if (m == null)
	    return;
	if (m.length() == 1) {
	    b.setMnemonic(m.charAt(0));
	}
    }   

  ////////////////////////////////////////////////////////////////
  // constructor
  public WizStep() {
    setMnemonic(_backButton, "mnemonic.button.back");
    setMnemonic(_nextButton, "mnemonic.button.next");
    setMnemonic(_finishButton, "mnemonic.bundle.finish");
    setMnemonic(_helpButton, "mnemonic.bundle.help");
    _buttonPanel.setLayout(new GridLayout(1, 5));
    _buttonPanel.add(_backButton);
    _buttonPanel.add(_nextButton);
    _buttonPanel.add(new SpacerPanel());
    _buttonPanel.add(_finishButton);
    _buttonPanel.add(new SpacerPanel());
    _buttonPanel.add(_helpButton);

    _backButton.setMargin(new Insets(0, 0, 0, 0));
    _nextButton.setMargin(new Insets(0, 0, 0, 0));
    _finishButton.setMargin(new Insets(0, 0, 0, 0));
    _helpButton.setMargin(new Insets(0, 0, 0, 0));

    JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    southPanel.add(_buttonPanel);

    setLayout(new BorderLayout());
    add(_mainPanel, BorderLayout.CENTER);
    add(southPanel, BorderLayout.SOUTH);

    _backButton.addActionListener(this);
    _nextButton.addActionListener(this);
    _finishButton.addActionListener(this);
    _helpButton.addActionListener(this);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setTarget(Object item) {
    _target = item;
    enableButtons();
  }

  public void enableButtons() {
    if (_target == null) {
      _backButton.setEnabled(false);
      _nextButton.setEnabled(false);
      _finishButton.setEnabled(false);
      _helpButton.setEnabled(false);
    }
    else if (_target instanceof ToDoItem) {
      ToDoItem tdi = (ToDoItem) _target;
      Wizard w = getWizard();
      _backButton.setEnabled(w != null ? w.canGoBack() : false);
      _nextButton.setEnabled(w != null ? w.canGoNext() : false);
      _finishButton.setEnabled(w != null ? w.canFinish() : false);
      _helpButton.setEnabled(true);
    }
    else {
      //_description.setText("TODO");
      return;
    }
  }

  public Object getTarget() { return _target; }

  public void refresh() { setTarget(_target); }

  public Wizard getWizard() {
    if (_target instanceof ToDoItem) {
      return ((ToDoItem)_target).getWizard();
    }
    return null;
  }

  ////////////////////////////////////////////////////////////////
  // actions

  public void doBack() {
    Wizard w = getWizard();
    if (w != null) {
      w.back();
      updateTabToDo();
    }
  }
  public void doNext() {
    Wizard w = getWizard();
    if (w != null) {
      w.next();
      updateTabToDo();
    }
  }
  public void doFinsh() {
    Wizard w = getWizard();
    if (w != null) {
      w.finish();
      updateTabToDo();
    }
  }
  public void doHelp() {
    if (!(_target instanceof ToDoItem)) return;
    ToDoItem item = (ToDoItem) _target;
    String urlString = item.getMoreInfoURL();
    StartBrowser.openUrl(urlString);
  }

  protected void updateTabToDo() {
    // awkward: relying on getParent() is fragile.
    TabToDo ttd = (TabToDo) getParent(); //???
    JPanel ws = getWizard().getCurrentPanel();
    if (ws instanceof WizStep) ((WizStep)ws).setTarget(_target);
    ttd.showStep(ws);
  }

  ////////////////////////////////////////////////////////////////
  // ActionListener implementation

  public void actionPerformed(ActionEvent ae) {
    Object src = ae.getSource();
    if (src == _backButton) doBack();
    else if (src == _nextButton) doNext();
    else if (src == _finishButton) doFinsh();
    else if (src == _helpButton) doHelp();
  }

  ////////////////////////////////////////////////////////////////
  // DocumentListener implementation

  public void insertUpdate(DocumentEvent e) {
    enableButtons();
  }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

  public void changedUpdate(DocumentEvent e) {
    // Apparently, this method is never called.
  }

} /* end class WizStep */
