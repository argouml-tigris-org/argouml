

package uci.uml.ui;

import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
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

//import tudresden.ocl.*;

public class DialogConstraint extends JDialog {

    ArgoConstraintEvaluation ace;

    public DialogConstraint(MModelElement me, JFrame parentFrame) {
      super(parentFrame, true);
      setTitle("Enter new OCL constraint");
      ace=new ArgoConstraintEvaluation(this);
      ace.setConstraint("context " + me.getName());
      getContentPane().add(ace);
      pack();
    }

    public String getResultingExpression() {
      return ace.getResultConstraint();
    }
}

