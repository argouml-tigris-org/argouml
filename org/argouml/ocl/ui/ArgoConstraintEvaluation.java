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

package org.argouml.ocl.ui;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;

import tudresden.ocl.*;
import tudresden.ocl.check.types.ModelFacade;

import org.argouml.ocl.ArgoFacade;

public class ArgoConstraintEvaluation extends ConstraintEvaluation {

  JDialog dialog;
  String resultConstraint=null;

  JButton cOk, cCancel;
  JButton aOk, aCancel;

  Object target;

  JTextField cContext;
  String classifierName;

  public ArgoConstraintEvaluation(JDialog dialog, Object target) {
    this.dialog=dialog;
    this.target=target;
  }

  protected void addTabs(JTabbedPane tabs) {
    tabs.addTab("Constraint", getConstraintPane());
    tabs.addTab("Lexer", getLexerPane());
    tabs.addTab("AST", getASTPane());
    tabs.addTab("Java", getJavaCodePane());
    tabs.addTab("Errors", getErrorPane());
    tabs.addTab("About", getAboutPane());
  }

  protected JPanel getConstraintPane() {
    JPanel result=new JPanel(new BorderLayout());
    JPanel buttons=new JPanel(new GridLayout(0, 1));

    cContext = new JTextField();
    cContext.setEditable(false);
    cInput=new JTextArea();
    cInput.setFont(new Font("Monospaced", Font.PLAIN, 12));

    cParse=new JButton("Parse");
    cOk=new JButton("OK");
    cCancel=new JButton("Cancel");

    cToClipboard=new JButton(getImage("images/right.gif"));
    cFromClipboard=new JButton(getImage("images/left.gif"));

    cToClipboard.setToolTipText("copy constraint to clipboard");
    cFromClipboard.setToolTipText("copy constraint from clipboard");

    cParse.addActionListener(this);
    cOk.addActionListener(this);
    cCancel.addActionListener(this);
    cToClipboard.addActionListener(this);
    cFromClipboard.addActionListener(this);

    buttons.add(cParse);
    buttons.add(cOk);
    buttons.add(cCancel);
    buttons.add(new JLabel(" "));
    buttons.add(cToClipboard);
    buttons.add(cFromClipboard);

    JPanel constraintPanel = new JPanel(new BorderLayout());
    constraintPanel.add(cContext, BorderLayout.NORTH);
    constraintPanel.add(cInput);

    result.add(new JScrollPane(constraintPanel));
    result.add(panelAround(buttons), BorderLayout.EAST);
    return result;
  }

  protected JPanel getASTPane() {
    JPanel result=new JPanel(new BorderLayout());
    JPanel buttons=new JPanel(new GridLayout(0, 1));

    aTree=new JTree(new DefaultMutableTreeNode());
    aTree.setFont(new Font("Monospaced", Font.PLAIN, 12));
    aNormalize=new JButton("Normalize");
    aOk=new JButton("OK");
    aCancel=new JButton("Cancel");
    aShowLeaves=new JButton("Show Leaves");
    aToText=new JButton("To Text");
    aToClipboard=new JButton(getImage("images/right.gif"));

    JLabel txtTypeCheck=new JLabel("Type Check:");
    JLabel txtGeneratedTests=new JLabel("Generated Tests:");
    aTypeCheck=new JLabel(imageEmpty, SwingConstants.CENTER);
    aGeneratedTests=new JLabel(imageEmpty, SwingConstants.CENTER);

    aNormalize.setToolTipText("normalize syntax tree");
    aShowLeaves.setToolTipText("show all leafs of the syntax tree");
    aToText.setToolTipText("copy the syntax tree's expression into the input field");
    aToClipboard.setToolTipText("copy text version of syntax tree to clipboard");

    aOk.addActionListener(this);
    aCancel.addActionListener(this);
    aNormalize.addActionListener(this);
    aToText.addActionListener(this);
    aShowLeaves.addActionListener(this);
    aToClipboard.addActionListener(this);

    buttons.add(aOk);
    buttons.add(aCancel);
    buttons.add(new JLabel(" "));
    buttons.add(aNormalize);
    buttons.add(aShowLeaves);
    buttons.add(new JLabel(" "));
    buttons.add(aToText);
    buttons.add(aToClipboard);
    buttons.add(new JLabel(" "));
    buttons.add(txtTypeCheck);
    buttons.add(aTypeCheck);
    buttons.add(txtGeneratedTests);
    buttons.add(aGeneratedTests);

    result.add(new JScrollPane(aTree));
    result.add(panelAround(buttons), BorderLayout.EAST);
    return result;
  }

  protected int getIndexOfErrorPane() {
    return 4;
  }

  protected int getIndexOfASTPane() {
    return 2;
  }

  protected ModelFacade getModelFacade() {
    return new ArgoFacade(target);
  }

  protected String getEnteredConstraint()
  {
    return "context " + classifierName + "\n" + super.getEnteredConstraint();
  }

  protected void doCopyTreeToText() {
    if (tree!=null) {
      String oclExpression = tree.getExpression();
      java.util.StringTokenizer st = new java.util.StringTokenizer(oclExpression);
      int startOfBodies = oclExpression.indexOf(st.nextToken());
      String classifierName = st.nextToken();
      startOfBodies = oclExpression.indexOf(classifierName, startOfBodies);
      startOfBodies = startOfBodies + classifierName.length();
      String bodies = oclExpression.substring(startOfBodies);
      cInput.setText( bodies.trim() );
      showTab(0);
    } else {
      message.setText("no syntax tree to copy...");
    }
  }

  public void setConstraint(String classifierName, String constraintBodies) {
    this.classifierName = classifierName;
    cContext.setText("context "+classifierName);
    cInput.setText(constraintBodies);
  }

  public String getResultConstraint() {
	return resultConstraint;
  }

  protected void doParse(boolean s) {
    super.doParse(s);
    resultConstraint=cInput.getText();
  }

  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);
    if (e.getSource()==cOk) {
      doParse(false); // parse without switching tabs
      closeDialog();
    } else if (e.getSource()==aOk) {
      if (resultConstraint==null) doParse(false);
      closeDialog();
    } else if (e.getSource()==cCancel || e.getSource()==aCancel) {
      resultConstraint=null;
      dialog.dispose();
    }
  }

  protected void closeDialog() {
    if (resultConstraint!=null) {
      dialog.dispose();
    }
  }
}

