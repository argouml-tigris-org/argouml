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



// from Sun's Beanbox
// Support for PropertyEditor with custom editors.

package uci.ui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.beans.*;

class PropertyDialog extends JFrame implements ActionListener {

    private JButton doneButton;
    private Component body;
    private final static int vPad = 5;
    private final static int hPad = 4;

  public PropertyDialog(JFrame frame, PropertyEditor pe, int x, int y) {
    super(pe.getClass().getName());
    //new WindowCloser(this);
    getContentPane().setLayout(new BorderLayout());
    
    body = pe.getCustomEditor();
    //System.out.println("got custom editor!!");
    getContentPane().add(body, BorderLayout.CENTER);
    
    doneButton = new JButton("Done");
    doneButton.addActionListener(this);
    getContentPane().add(doneButton, BorderLayout.SOUTH);
    
    pack();
    setLocation(x, y);
    setVisible(true);
    doneButton.addActionListener(this);
  }

  public void actionPerformed(ActionEvent evt) {
    // Button down.
    dispose();
  }
  
//   public void doLayout() {
//     Insets ins = getInsets();
//     Dimension bodySize = body.getPreferredSize();
//     Dimension buttonSize = doneButton.getPreferredSize();
    
//     int width = ins.left + 2*hPad + ins.right + bodySize.width;
//     int height = ins.top + 3*vPad + ins.bottom + bodySize.height +
//       buttonSize.height;
    
//     body.setBounds(ins.left+hPad, ins.top+vPad,
// 		   bodySize.width, bodySize.height);
    
//     doneButton.setBounds((width-buttonSize.width)/2,
// 			 ins.top+(2*hPad) + bodySize.height,
// 			 buttonSize.width, buttonSize.height);
    
//     setSize(width, height);
    
//   }

}

