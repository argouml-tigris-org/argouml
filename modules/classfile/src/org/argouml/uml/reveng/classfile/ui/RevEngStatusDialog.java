// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.uml.reveng.classfile.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.Stack;
import javax.swing.*; 


/**
 * This window shows the current status of the reverse engineering.
 */
public class RevEngStatusDialog extends JFrame implements ActionListener {

    //////////////////////////////////////////////////////////
    // Constants

    // Size of the dialog window.
    private final int WIDTH = 400;
    private static final int HEIGHT = 350;      


    //////////////////////////////////////////////////////////
    // instance variables

    // Pass info
    private JTextField _passInfo = null;

    // A textfield to display the messages.
    private JTextField _messages = null;

    // The scoll pane for the messages.
    private JScrollPane _scrollPane = null;

    // A progress bar
    private JProgressBar _progressBar = null;

    // A button to close the window.
    private JButton _closeButton = null;

    // The current file.
    private String _file = null;

    // The current jar file.
    private String _jarfile = null;

    // The current directory tree as a stack.
    private Stack _directories = new Stack();

    // The total number of files to process.
    private int _fileCount = 0;

    // The number of files already processed.
    private int _filesProcessed = 0;

    //////////////////////////////////////////////////////////
    // Constructors

    /**
     * Create a new RevEngStatusDialog instance.
     */
    public RevEngStatusDialog() {
	super("Import progress");
	
	// Create a new layout for the status window.
	getContentPane().setLayout(new GridBagLayout());

	// Compute the constraints for the GridBag layout.
        GridBagConstraints c = new GridBagConstraints();
	c.gridwidth=GridBagConstraints.REMAINDER;
	c.insets= new Insets(5,5,5,5);
	c.fill = GridBagConstraints.HORIZONTAL;

	// Create a new textfield for the pass info
	_passInfo = new JTextField();
	_passInfo.setEditable(false);
	getContentPane().add(_passInfo, c);

	// Create a new text field for the messages.
	_messages = new JTextField();
	_messages.setEditable(false);  // This is only for the status output.
	_messages.setBackground(Color.white);

	// Create a scrollpane for the messages.
	_scrollPane = new JScrollPane(_messages, 
				      JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	_scrollPane.setMinimumSize(new Dimension(WIDTH, 250));
	_scrollPane.setPreferredSize(_scrollPane.getMinimumSize());
	c.fill = GridBagConstraints.BOTH;
	getContentPane().add(_scrollPane, c);

	// Create a new progress bar.
	_progressBar = new JProgressBar();
	_progressBar.setMinimum(0);
	_progressBar.setMinimumSize(new Dimension(WIDTH, 20));
	_progressBar.setSize(_progressBar.getMinimumSize());
	_progressBar.setPreferredSize(_progressBar.getMinimumSize());
	c.fill = GridBagConstraints.HORIZONTAL;
	getContentPane().add(_progressBar, c);

	// Create a button to close the status window
	_closeButton = new JButton("Dismiss");
	_closeButton.setEnabled(false);
	_closeButton.setMinimumSize(new Dimension(80,30));
	_closeButton.addActionListener(this);
	c.fill = GridBagConstraints.NONE;
	c.gridheight = GridBagConstraints.REMAINDER;
	getContentPane().add(_closeButton, c);
	
	pack();
    }


    //////////////////////////////////////////////////////////
    // Methods

    /**
     * Start a new pass.
     *
     * @param info Info on the pass.
     * @param fileCount The number of the files.
     */
    public void startPass(String info, int fileCount) {
	setPassInfo(info);
	_fileCount = fileCount;
	_filesProcessed = 0;
	_progressBar.setMaximum(_fileCount);
	_progressBar.setValue(0);
	update(getGraphics());
    }

    /**
     * End a pass and show some info in the status window.
     *
     * @param The info to show on the completed pass.
     */
    public void endPass(String info) {
	setPassInfo(info);
    }
    
    /**
     * Set the text in the pass info field.
     *
     * @param info The info in the pass info field.
     */
    protected void setPassInfo(String info) {
	_passInfo.setText(info);
    }

    /**
     * Enter a new directory.
     *
     * @param dirname The name of the directory.
     */
    public void enterDirectory(String dirname) {
	addMessage("Entering directory: " + dirname + "\n");
	_directories.push(dirname);
    }

    /**
     * Leave the current directory.
     */
    public void leaveDirectory() {
	String dirname = (String)(_directories.pop());
	addMessage("Leave directory: " + dirname + "\n");
    }

    /**
     * Indicate the opening of a jar file.
     *
     * @param jarname The name of the jar file.
     */
    public void openJar(String jarname) {       
	addMessage("Opening jarfile: " + jarname + "\n");
	_jarfile = jarname;  // Store the name of the jarfile.
    }

    /**
     * Close the current jar file.
     */
    public void closeJar() {
	addMessage("Closing jarfile: " + _jarfile + "\n");
	_jarfile = null;
    }

    /**
     * Indicate the start of the RE of a file.
     *
     * @param filename The name of the file.
     */
    public void startFile(String filename) {
	addMessage("Parsing file: " + filename + "...");
	_file = filename;
    }
    
    /**
     * End the import of the current file.
     */
    public void endFile() {
	_file = null;
	_filesProcessed++;

	// The progress bar is repainted after the addMessage() call,
	// so I don't do a repaint here.
	_progressBar.setValue(_filesProcessed);

	addMessage("completed\n");
    }

    /**
     * Add a message to the status window.
     *
     * @param msg The message to add.
     */
    private void addMessage(String msg) {
	_messages.setText(_messages.getText() + msg);

	// Force the message to show.
	update(getGraphics());
    }

    /**
     * Signal the end of the import, so the close button can
     * be enabled.
     */
    public void importCompleted() {
	_closeButton.setEnabled(true);  // Enable the close button.
    }

    
    /////////////////////////////////////////////////////////
    // Event handler

    /**
     * Handle the actions of the status window.
     *
     * @param e The action event.
     */
    public void actionPerformed(ActionEvent e) {
	if(e.getSource() == _closeButton) {
	    setVisible(false);
	    dispose();
	}
    }
}
