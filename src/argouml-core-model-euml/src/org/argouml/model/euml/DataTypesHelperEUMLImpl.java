// $Id$
/***************************************************************************
 * Copyright (c) 2007,2010 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial implementation
 *    Thomas Neustupny
 *    Laurent Braud 
 ***************************************************************************/
package org.argouml.model.euml;

import java.util.ArrayList;
import java.util.List;

import org.argouml.model.DataTypesHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.LiteralBoolean;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.LiteralNull;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValueSpecification;

/**
 * The implementation of the DataTypesHelper for EUML2.
 */
class DataTypesHelperEUMLImpl implements DataTypesHelper {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     *
     * @param implementation The ModelImplementation.
     */
    public DataTypesHelperEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public boolean equalsCHOICEKind(Object kind) {
        return PseudostateKind.CHOICE_LITERAL.equals(kind);
    }

    public boolean equalsDeepHistoryKind(Object kind) {
        return PseudostateKind.DEEP_HISTORY_LITERAL.equals(kind);
    }

    public boolean equalsFORKKind(Object kind) {
        return PseudostateKind.FORK_LITERAL.equals(kind);
    }

    public boolean equalsINITIALKind(Object kind) {
        return PseudostateKind.INITIAL_LITERAL.equals(kind);
    }

    public boolean equalsJOINKind(Object kind) {
        return PseudostateKind.JOIN_LITERAL.equals(kind);
    }

    public boolean equalsJUNCTIONKind(Object kind) {
        return PseudostateKind.JUNCTION_LITERAL.equals(kind);
    }

    public boolean equalsShallowHistoryKind(Object kind) {
        return PseudostateKind.SHALLOW_HISTORY_LITERAL.equals(kind);
    }

    public String getBody(Object handle) {
        EList<String> bodies = null;
        if (handle instanceof OpaqueExpression) {
            bodies = ((OpaqueExpression) handle).getBodies();
        } else if (handle instanceof OpaqueBehavior) {
            bodies = ((OpaqueBehavior) handle).getBodies();
        } else {
            throw new IllegalArgumentException(
                    "handle must be instance "
                    + "of OpaqueExpression or OpaqueBehavior"); //$NON-NLS-1$
        }
        return (bodies.size() < 1) ? null : bodies.get(0);
    }

    public String getLanguage(Object handle) {
        EList<String> langs = null;
        if (handle instanceof OpaqueExpression) {
            langs = ((OpaqueExpression) handle).getLanguages();
        } else if (handle instanceof OpaqueBehavior) {
            langs = ((OpaqueBehavior) handle).getLanguages();
        } else {
            throw new IllegalArgumentException(
                    "handle must be instance of OpaqueExpression" //$NON-NLS-1$
                    + " or OpaqueBehavior. We got " + handle); //$NON-NLS-1$
        }
        return (langs.size() < 1) ? null : langs.get(0);
    }

    public String multiplicityToString(Object multiplicity) {
        if (!(multiplicity instanceof MultiplicityElement)) {
            throw new IllegalArgumentException(
                    "multiplicity must be instance "
                    + "of MultiplicityElement"); //$NON-NLS-1$
        }
        MultiplicityElement mult = (MultiplicityElement) multiplicity;
        if (mult.getLower() == mult.getUpper()) {
            return DataTypesFactoryEUMLImpl.boundToString(mult.getLower());
        } else {
            return DataTypesFactoryEUMLImpl.boundToString(
                    mult.getLower())
                    + ".." //$NON-NLS-1$
                    + DataTypesFactoryEUMLImpl.boundToString(mult.getUpper());
        }
    }
    
    public Object setBody(Object handle, String body) {
        List<String> bodies = null;
        if (handle instanceof OpaqueExpression) {
            bodies = ((OpaqueExpression) handle).getBodies();
        } else if (handle instanceof OpaqueBehavior) {
            bodies = ((OpaqueBehavior) handle).getBodies();
        } else {
            throw new IllegalArgumentException(
                    "handle must be instance "
                    + "of OpaqueExpression or OpaqueBehavior"); //$NON-NLS-1$
        }
        // TODO: Support more than one body/language
        if (bodies.size() > 1) {
            throw new IllegalStateException(
                    "Only one body/lang supported"); //$NON-NLS-1$
        }
        bodies.clear();
        bodies.add(body);
        return handle;
    }

    public Object setLanguage(Object handle, String language) {
        List<String> langs = null;
        if (handle instanceof OpaqueExpression) {
            langs = ((OpaqueExpression) handle).getLanguages();
        } else if (handle instanceof OpaqueBehavior) {
            langs = ((OpaqueBehavior) handle).getLanguages();
        } else {
            throw new IllegalArgumentException(
                    "handle must be instance "
                    + "of OpaqueExpression or OpaqueBehavior"); //$NON-NLS-1$
        }
        // TODO: Support more than one body/language
        if (langs.size() > 1) {
            throw new IllegalStateException(
                    "Only one body/lang supported"); //$NON-NLS-1$
        }
        langs.clear(); 
        langs.add(language);
        return handle;
    }

    /**
     * @see org.argouml.model.DataTypesHelper#getValueSpecifications()
     */
    public ArrayList<String> getValueSpecifications() {
        ArrayList<String> list = new ArrayList<String>(); 
        //
        list.add("OpaqueExpression");
        //list.add("OpaqueExpression");
        
        //LiteralSpecification
        //list.add("LiteralNull");//When other ok
        list.add("LiteralString");
        //list.add("LiteralInteger");//When other ok
        list.add("LiteralBoolean");
        //list.add("LiteralUnlimitedNatural");//When other ok
        
        //list.add("LiteralReal");//UML 2.4
        //End LiteralSpecification     
        
        //list.add("InstanceValue");
        
        return list;
    }
    
    /**
     * @see org.argouml.model.DataTypesHelper#createValueValueSpecification(java.lang.String, java.lang.Object)
     */
    public Object createValueSpecification(Object handle,
            String type) {
        
        ValueSpecification newValueSpecification = null;
        if (handle instanceof Property) {
            Property property = (Property) handle;
            
            Object[] tabValues = null;
            if (type.equals("LiteralBoolean")) {
                newValueSpecification = 
                    (ValueSpecification) UMLFactory.eINSTANCE.create(
                            UMLPackage.eINSTANCE.getLiteralBoolean());
            } else if (type.equals("LiteralNull")) {
        	newValueSpecification = 
        	    (ValueSpecification) UMLFactory.eINSTANCE.create(
        	            UMLPackage.eINSTANCE.getLiteralNull());
            } else if (type.equals("LiteralString")) {
                newValueSpecification = 
                    (ValueSpecification) UMLFactory.eINSTANCE.create(
                            UMLPackage.eINSTANCE.getLiteralString());
                tabValues = new String[]{""};
            } else if (type.equals("LiteralUnlimitedNatural")) {
                newValueSpecification = 
                    (ValueSpecification) UMLFactory.eINSTANCE.create(
                            UMLPackage.eINSTANCE.getLiteralUnlimitedNatural());
                tabValues = new String[]{"0"};
            } else if (type.equals("LiteralInteger")) {
                newValueSpecification = 
                    (ValueSpecification) UMLFactory.eINSTANCE.create(
                            UMLPackage.eINSTANCE.getLiteralInteger());
                tabValues = new String[]{"0"};
            } else if (type.equals("OpaqueExpression")) {
                newValueSpecification =
                    (ValueSpecification) UMLFactory.eINSTANCE.create(
                            UMLPackage.eINSTANCE.getOpaqueExpression());
                setLanguage(newValueSpecification, "");
                setBody(newValueSpecification, "");
            }
            if (tabValues != null) {
                modifyValueSpecification(newValueSpecification, tabValues);
            }
            
            if (newValueSpecification != null) {
                property.setDefaultValue(newValueSpecification);
            }
        } // TODO else => Exception !

        return newValueSpecification;
        
    }
    
    /**
     * @see org.argouml.model.DataTypesHelper#modifyValueSpecification(java.lang.Object, java.lang.Object[])
     */
    public void modifyValueSpecification(Object handle, Object[] tabValues) {
        // LiteralSpecification  
        if (handle instanceof LiteralBoolean) {
            LiteralBoolean lb = (LiteralBoolean) handle;
            if (tabValues != null && tabValues.length == 1) {
                Boolean value = (Boolean) tabValues[0];
                lb.setValue(value);
            }
        } else if (handle instanceof LiteralNull) {
            // Can't be changed
        } else if (handle instanceof LiteralString) {
            LiteralString ls = (LiteralString) handle;
            if (tabValues != null && tabValues.length == 1) {
                String value = (String) tabValues[0];
                ls.setValue(value);
            }
        } else if (handle instanceof LiteralUnlimitedNatural) {
            LiteralUnlimitedNatural ls = (LiteralUnlimitedNatural) handle;
            if (tabValues != null && tabValues.length == 1) {
                Integer value = (Integer) tabValues[0];
                ls.setValue(value);
            }
        } else if (handle instanceof LiteralInteger) {
            LiteralInteger ls = (LiteralInteger) handle;
            if (tabValues != null && tabValues.length == 1) {
                Integer value = (Integer) tabValues[0];
                ls.setValue(value);
            }
        } else if (handle instanceof OpaqueExpression) {
            //rewrite it after change setExpression ?
            OpaqueExpression oe = (OpaqueExpression) handle;
            List<String> bodies = oe.getBodies();
            List<String> langs = oe.getLanguages();
            // as write in UML specification, we have size(bodies)==size(langs)
            // we must have tabValues.length%2==0 
            bodies.clear();
            langs.clear();
            for (int i = 0; i < tabValues.length; i += 2) {
                bodies.add((String) tabValues[i]);
                langs.add((String) tabValues[i + 1]);
            }
        }
        // TODO Expression and InstanceValue (how)
        // If anyone have an example.
        
    }
    
    /**
     * @see org.argouml.model.DataTypesHelper#getValueSpecificationValues(java.lang.Object)
     */
    public Object[] getValueSpecificationValues(Object handle) {
        Object[] tabValues = null;
        // LiteralSpecification  
        if (handle instanceof LiteralBoolean) {
            LiteralBoolean lb = (LiteralBoolean) handle;
            tabValues = new Boolean[]{lb.isValue()};
            
        } else if (handle instanceof LiteralNull) {
            // Can't be set
        } else if (handle instanceof LiteralString) {
            LiteralString ls = (LiteralString) handle;
            tabValues = new String[]{ls.getValue()};
        } else if (handle instanceof LiteralUnlimitedNatural) {
            LiteralUnlimitedNatural lun = (LiteralUnlimitedNatural) handle;
            tabValues = new String[]{"" + lun.getValue()};
        } else if (handle instanceof LiteralInteger) {
            LiteralInteger li = (LiteralInteger) handle;
            tabValues = new Integer[]{li.getValue()};
        }
        else if (handle instanceof OpaqueExpression) {
            //rewrite it after change setExpression ?
            OpaqueExpression oe = (OpaqueExpression) handle;
            List<String> bodies = oe.getBodies();
            List<String> langs = oe.getLanguages();
            // as write in UML specification, we have size(bodies)==size(langs)
            // we must have tabValues.length%2==0
            if (bodies.size() > 0) {
                tabValues = new String[bodies.size() * 2];
                for (int i = 0; i < bodies.size(); i++) {
                    tabValues[i * 2] = bodies.get(i);
                    tabValues[i * 2 + 1] = langs.get(i);
                }
            }
        }
        // TODO Expression and InstanceValue (how)
        // If anyone have an example.
        
        return tabValues;
    }
}
