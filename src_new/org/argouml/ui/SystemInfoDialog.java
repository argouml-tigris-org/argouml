/*
 * SystemInfoDialog.java
 */

package org.argouml.ui;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * Display System Information (JDK Version, JDK Vendor, etc).
 * A Copy to System Clipboard button is provided to help generate bug reports.
 *
 * @author Eugenio Alvarez
 */
public class SystemInfoDialog extends JDialog {

  ////////////////////////////////////////////////////////////////
  // instance varaibles

  JTabbedPane _tabs = new JTabbedPane();
  JTextArea   _info = new JTextArea();
  JButton     _runGCButton = new JButton("Run GC");
  JButton     _copyButton = new JButton("Copy Information to System Clipboard");
  JButton     _cancelButton = new JButton("Cancel");
  JPanel      _buttons = new JPanel();

  ////////////////////////////////////////////////////////////////
  // constructors

  public SystemInfoDialog() {
    this((Frame)null,false);
  }

  public SystemInfoDialog(Frame owner) {
    this(owner,false);
  }

  public SystemInfoDialog(Frame owner, boolean modal) {
    super(owner,modal);
    this.setTitle("System Information");
    this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

    _info.setEditable(false);

    _tabs.addTab("System Information", new JScrollPane(_info));

    _runGCButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        runGC_actionPerformed(e);
      }
    });
    _copyButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        copy_actionPerformed(e);
      }
    });
    _cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cancel_actionPerformed(e);
      }
    });
    getContentPane().setLayout(new BorderLayout(0, 0));
    getContentPane().add(_tabs, BorderLayout.CENTER);
    _buttons.add(_runGCButton);
    _buttons.add(_copyButton);
    _buttons.add(_cancelButton);
    getContentPane().add(_buttons, BorderLayout.SOUTH);
    updateInfo();
    addWindowListener(new WindowAdapter() {
        public void windowActivated(WindowEvent e) {
            updateInfo();
        } // end windowActivated()
    });
    pack();
  } // end SystemInfoDialog()

  void runGC_actionPerformed(ActionEvent e) {
    Runtime.getRuntime().gc();
    updateInfo();
  } // end runGC_actionPerformed()

  void copy_actionPerformed(ActionEvent e) {
    String info = _info.getText();
    StringSelection contents = new StringSelection(info);
    Clipboard clipboard = getToolkit().getSystemClipboard();
    clipboard.setContents(contents,defaultClipboardOwner);
  } // end copy_actionPerformed()

  void cancel_actionPerformed(ActionEvent e) {
    this.setVisible(false);
  } // end cancel_actionPerformed()

  void updateInfo() {
    StringBuffer info = new StringBuffer();
    info.append("Java Version		: ");
    info.append(System.getProperty("java.version","") + "\n" );
    info.append("Java Vendor		: ");
    info.append(System.getProperty("java.vendor","") + "\n" );
    info.append("Java Vendor URL	: ");
    info.append(System.getProperty("java.vendor.url","") + "\n" );
    info.append("Java Home Directory	: ");
    info.append(System.getProperty("java.home","") + "\n" );
    info.append("Java Classpath		: ");
    info.append(System.getProperty("java.class.path","") + "\n" );
    info.append("Operation System	: ");
    info.append(System.getProperty("os.name",""));
    info.append(", Version ");
    info.append(System.getProperty("os.version","") + "\n" );
    info.append("Architecture		: ");
    info.append(System.getProperty("os.arch","") + "\n" );
    info.append("User Name		: ");
    info.append(System.getProperty("user.name","") + "\n" );
    info.append("User Home Directory	: ");
    info.append(System.getProperty("user.home","") + "\n" );
    info.append("Current Directory	: ");
    info.append(System.getProperty("user.dir","") + "\n" );
    info.append("JVM Total Memory	: ");
    info.append(String.valueOf(Runtime.getRuntime().totalMemory()) + "\n" );
    info.append("JVM Free Memory	: ");
    info.append(String.valueOf(Runtime.getRuntime().freeMemory())+ "\n" );
    
    _info.setText(info.toString());
  } //end updateInfo()

  private static ClipboardOwner defaultClipboardOwner = new ClipboardObserver();

  static class ClipboardObserver implements ClipboardOwner {
     public void lostOwnership(Clipboard clipboard, Transferable contents) {
     }
  }

} /* end class SystemInfoDialog */
