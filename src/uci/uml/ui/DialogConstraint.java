

package uci.uml.ui;

import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import com.sun.java.util.collections.*;
import uci.util.*;
import uci.uml.ocl.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.model_management.*;

import tudresden.ocl.*;

public class DialogConstraint extends JDialog implements ActionListener{
    
    JTextArea _expressionField;
    JButton _okButton;
    JButton _cancelButton;
    JLabel _message;

    String resultingExpression = null;

    public DialogConstraint(JFrame parentFrame) {
	super(parentFrame, true);
	setTitle("Enter new OCL constraint");
	_expressionField = new JTextArea(4,20);
	_okButton = new JButton("OK");
	_okButton.addActionListener(this);
	_cancelButton = new JButton("Cancel");
	_cancelButton.addActionListener(this);
	_message = new JLabel(" ");
	JPanel buttonPanel = new JPanel();
	buttonPanel.add(_okButton);
	buttonPanel.add(_cancelButton);
	JPanel inputPanel = new JPanel(new BorderLayout());
	inputPanel.add(_expressionField);
	inputPanel.add(buttonPanel,BorderLayout.SOUTH);

	JPanel content = new JPanel(new BorderLayout());
	content.add(inputPanel);
	content.add(_message, BorderLayout.SOUTH);
	getContentPane().add(content);

	pack();
    }

    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == _okButton) {
	    try {
		OclTree tree = OclTree.createTree(_expressionField.getText(), new ArgoFacade());
		tree.assureTypes();
		resultingExpression = _expressionField.getText();
		dispose();
	    }
	    catch (Exception ex) {
		_message.setText(ex.getMessage());
	    }
	}

	if (e.getSource() == _cancelButton) {
	    dispose();
	}
    }

    public String getResultingExpression() {
	return resultingExpression;
    }
}
    
