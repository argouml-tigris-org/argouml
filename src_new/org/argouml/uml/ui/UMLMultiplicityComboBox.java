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

package org.argouml.uml.ui;
import javax.swing.event.*;
import javax.swing.*;
import java.lang.reflect.*;
import ru.novosoft.uml.*;
import java.awt.event.*;
import ru.novosoft.uml.foundation.data_types.*;

public class UMLMultiplicityComboBox extends JComboBox implements ItemListener, UMLUserInterfaceComponent {

    private UMLUserInterfaceContainer _container;
    private Method _getMethod;
    private Method _setMethod;
    private static String[] _mults = { "","0..1","0..2","0..3","0..*",
                                "1..1","1..2","1..3","1..*",
                                       "2..2","2..3","2..*",
                                              "3..3","3..*" };
    private static int[] _lower = { -1,0,0,0,0,1,1,1,1,2,2,2,3,3 };
    private static int[] _upper = { -1,1,2,3,-1,1,2,3,-1,2,3,-1,3,-1 };
    
    private static final Object[] _noArg = {};
    
    public UMLMultiplicityComboBox(UMLUserInterfaceContainer container,Class elementClass) {
        super();
        setModel(new DefaultComboBoxModel(_mults));
        _container = container;
        addItemListener(this);
        Class[] getArgs = {};
        Class[] setArgs = { MMultiplicity.class };
        try {
            _getMethod = elementClass.getMethod("getMultiplicity",getArgs);
            _setMethod = elementClass.getMethod("setMultiplicity",setArgs);
        }
        catch(Exception e) {
            setEnabled(false);
            System.out.println(e.toString() + " in UMLMultiplicityComboBox()");
        }
    }

    public void targetChanged() {
        update();
    }
    
    public void roleAdded(final MElementEvent p1) {
    }
    public void recovered(final MElementEvent p1) {
    }
    public void roleRemoved(final MElementEvent p1) {
    }
    public void listRoleItemSet(final MElementEvent p1) {
    }
    public void removed(final MElementEvent p1) {
    }
    public void propertySet(final MElementEvent event) {
        String eventProp = event.getName();
        if(eventProp == null || eventProp.equals("multiplicity")) {
            update();
        }
    }
    
    private void update() {
        int lower = -1;
        int upper = -1;
        
        try {
            Object obj = _getMethod.invoke(_container.getTarget(),_noArg);
            if(obj instanceof MMultiplicity) {
                MMultiplicity mult = (MMultiplicity) obj;
                lower = mult.getLower();
                upper = mult.getUpper();
            }
        }
        catch(Exception e) {
            System.out.println(e.toString() + " in UMLMultiplicityComboBox.update()");
        }
        boolean match = false;
        for(int i = 0; i < _mults.length; i++) {
            if(lower == _lower[i] && upper == _upper[i]) {
                setSelectedIndex(i);
                match = true;
                break;
            }
        }
        if(!match) {
            StringBuffer buf = new StringBuffer();
            if(lower <= 0) {
                buf.append("0..");
            }
            else {
                buf.append(Integer.toString(lower));
                buf.append("..");
            }
            if(upper < 0) {
                buf.append("*");
            }
            else {
                buf.append(Integer.toString(upper));
            }
            setSelectedItem(buf.toString());
        }
    }
    
    public void itemStateChanged(ItemEvent event) {
        if(event.getStateChange() == ItemEvent.SELECTED) {
            int index = getSelectedIndex();
            int lower = _lower[index];
            int upper = _upper[index];
            MMultiplicity mult = null;
            if(lower >= 0) {
                mult = new MMultiplicity(lower,upper);
            }
            try {
                _setMethod.invoke(_container.getTarget(),new Object[] { mult });
            }
            catch(Exception e) {
                System.out.println(e.toString() + " in UMLMultiplicityComboBox.itemStateChanged()");
            }
            
        }
    }
}