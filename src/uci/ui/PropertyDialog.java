// from Sun's Beanbox
// Support for PropertyEditor with custom editors.

package uci.ui;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;

class PropertyDialog extends Dialog implements ActionListener {

    private Button doneButton;
    private Component body;
    private final static int vPad = 5;
    private final static int hPad = 4;

    PropertyDialog(Frame frame, PropertyEditor pe, int x, int y) {
	super(frame, pe.getClass().getName(), true);
	//new WindowCloser(this);
	setLayout(null);

	body = pe.getCustomEditor();
	add(body);

	doneButton = new Button("Done");
	doneButton.addActionListener(this);
	add(doneButton);

	setLocation(x, y);
	show();
    }

    public void actionPerformed(ActionEvent evt) {
        // Button down.
	dispose();
    }

    public void doLayout() {
        Insets ins = getInsets();
	Dimension bodySize = body.getPreferredSize();
	Dimension buttonSize = doneButton.getPreferredSize();

	int width = ins.left + 2*hPad + ins.right + bodySize.width;
	int height = ins.top + 3*vPad + ins.bottom + bodySize.height +
							buttonSize.height;

        body.setBounds(ins.left+hPad, ins.top+vPad,
				bodySize.width, bodySize.height);

	doneButton.setBounds((width-buttonSize.width)/2,
				ins.top+(2*hPad) + bodySize.height,
				buttonSize.width, buttonSize.height);

	setSize(width, height);

    }

}

