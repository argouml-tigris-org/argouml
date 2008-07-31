// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.profile.internal.ocl.uml14;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.argouml.model.Facade;
import org.argouml.model.Model;
import org.argouml.profile.internal.ocl.ModelInterpreter;

/**
 * Model Access
 * 
 * @author maurelio1234
 */
public class ModelAccessModelInterpreter implements ModelInterpreter {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger
            .getLogger(ModelAccessModelInterpreter.class);

    /*
     * @see org.argouml.profile.internal.ocl.ModelInterpreter#invokeFeature(java.util.HashMap,
     *      java.lang.Object, java.lang.String, java.lang.String,
     *      java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
    public Object invokeFeature(HashMap<String, Object> vt, Object subject,
            String feature, String type, Object[] parameters) {

        if (subject == null) {
            subject = vt.get("self");
        }

        if (Model.getFacade().isAModelElement(subject)) {
            if (type.equals(".")) {
                if (feature.equals("name")) {
                    return Model.getFacade().getName(subject);
                }
            }
        }

        /* 4.5.2.1 Abstraction */  
        // TODO investigate: Abstraction.mapping is not in the Model Sybsystem

        /* 4.5.2.3 Association */  
        
        if (Model.getFacade().isAAssociation(subject)) {
            if (type.equals(".")) {
                if (feature.equals("connection")) {
                    return new ArrayList<Object>(Model.getFacade()
                            .getConnections(subject));
                }
            }
        }

        /* 4.5.2.5 AssociationEnd */  
        
        if (Model.getFacade().isAAssociationEnd(subject)) {
            if (type.equals(".")) {
                if (feature.equals("aggregation")) {
                    return Model.getFacade().getAggregation(subject);
                }
                if (feature.equals("changeability")) {
                    return Model.getFacade().getChangeability(subject);
                }
                if (feature.equals("ordering")) {
                    return Model.getFacade().getOrdering(subject);
                }
                if (feature.equals("isNavigable")) {
                    return Model.getFacade().isNavigable(subject);
                }
                if (feature.equals("multiplicity")) {
                    return Model.getFacade().getMultiplicity(subject);
                }
                if (feature.equals("targetScope")) {
                    return Model.getFacade().getTargetScope(subject);
                }
                if (feature.equals("visibility")) {
                    return Model.getFacade().getVisibility(subject);
                }
                if (feature.equals("qualifier")) {
                    return Model.getFacade().getQualifiers(subject);
                }
                if (feature.equals("specification")) {
                    return Model.getFacade().getSpecification(subject);
                }                
                if (feature.equals("participant")) {
                    return Model.getFacade().getClassifier(subject);
                }
                
                // TODO investigate the "unnamed opposite end"
            }
        }

        /* 4.5.2.6 Attribute */  
        
        if (Model.getFacade().isAAttribute(subject)) {
            if (type.equals(".")) {
                if (feature.equals("initialValue")) {
                    return Model.getFacade().getInitialValue(subject);
                }
                if (feature.equals("associationEnd")) {
                    return new ArrayList<Object>(Model.getFacade()
                            .getAssociationEnds(subject));
                }
            }
        }

        /* 4.5.2.7 BehavioralFeature */  
        
        if (Model.getFacade().isABehavioralFeature(subject)) {
            if (type.equals(".")) {
                if (feature.equals("isQuery")) {
                    return Model.getFacade().isQuery(subject);
                }
                if (feature.equals("parameter")) {
                    return new ArrayList<Object>(Model.getFacade()
                            .getParameters(subject));
                }
            }
        }

        /* 4.5.2.8 Binding */  
        
        if (Model.getFacade().isABinding(subject)) {
            if (type.equals(".")) {
                if (feature.equals("argument")) {
                    return Model.getFacade().getArguments(subject);
                }
            }
        }        

        /* 4.5.2.9 Class */  
        
        if (Model.getFacade().isAClass(subject)) {
            if (type.equals(".")) {
                if (feature.equals("isActive")) {
                    return Model.getFacade().isActive(subject);
                }
            }
        }        

        /* 4.5.2.10 Classifier */  
        
        if (Model.getFacade().isAClassifier(subject)) {
            if (type.equals(".")) {
                if (feature.equals("feature")) {
                    return new ArrayList<Object>(Model.getFacade()
                            .getFeatures(subject));
                }
                if (feature.equals("feature")) {
                    return new ArrayList<Object>(Model.getFacade()
                            .getFeatures(subject));
                }
                if (feature.equals("association")) {
                    // TODO verify if this is the corrected semantics
                    return new ArrayList<Object>(Model.getFacade()
                            .getAssociatedClasses(subject));
                }                
                if (feature.equals("powertypeRange")) {
                    return new HashSet<Object>(Model.getFacade()
                            .getPowertypeRanges(subject));
                }
                // TODO specifiedEnd??
                if (feature.equals("feature")) {
                    return new ArrayList<Object>(Model.getFacade()
                            .getFeatures(subject));
                }
            }
        }        

        /* 4.5.2.11 Comment */  
        
        if (Model.getFacade().isAComment(subject)) {
            if (type.equals(".")) {
                if (feature.equals("body")) {
                    return Model.getFacade().getBody(subject);
                }
                if (feature.equals("annotatedElement")) {
                    return new HashSet<Object>(Model.getFacade()
                            .getAnnotatedElements(subject));
                }
            }
        }
        
        /* 4.5.2.12 Component */  
        
        if (Model.getFacade().isAComponent(subject)) {
            if (type.equals(".")) {
                if (feature.equals("deploymentLocation")) {
                    return new HashSet<Object>(Model.getFacade()
                            .getDeploymentLocations(subject));
                }
                if (feature.equals("resident")) {
                    // TODO check this
                    return new HashSet<Object>(Model.getFacade()
                            .getResidents(subject));
                }
                
                // TODO implementation?
            }
        }        

        /* 4.5.2.13 Constraint */  
        
        if (Model.getFacade().isAConstraint(subject)) {
            if (type.equals(".")) {
                if (feature.equals("body")) {
                    return Model.getFacade().getBody(subject);
                }
                if (feature.equals("constrainedElement")) {
                    // TODO check this
                    return new HashSet<Object>(Model.getFacade()
                            .getConstrainedElements(subject));
                }
            }
        }        

        /* 4.5.2.14 Dependency */  
        
        if (Model.getFacade().isADependency(subject)) {
            if (type.equals(".")) {
                if (feature.equals("client")) {
                    return new HashSet<Object>(Model.getFacade()
                            .getClients(subject));
                }
                if (feature.equals("supplier")) {
                    return new HashSet<Object>(Model.getFacade()
                            .getSuppliers(subject));
                }
            }
        }        

        // TODO ElementOwnership is not in ModelSubsys!!

        /* 4.5.2.18 ElementOwnership */          
        
        if (Model.getFacade().isAElementResidence(subject)) {
            if (type.equals(".")) {
                if (feature.equals("visibility")) {
                    return Model.getFacade().getVisibility(subject);
                }
            }
        }

        /* 4.5.2.19 Enumeration */          
        
        if (Model.getFacade().isAEnumeration(subject)) {
            if (type.equals(".")) {
                if (feature.equals("literal")) {
                    return Model.getFacade().getEnumerationLiterals(subject);
                }
            }
        }

        /* 4.5.2.20 EnumerationLiteral */          
        
        if (Model.getFacade().isAEnumerationLiteral(subject)) {
            if (type.equals(".")) {
                if (feature.equals("enumeration")) {
                    return Model.getFacade().getEnumeration(subject);
                }
            }
        }
        
        /* 4.5.2.21 Feature */          
        
        if (Model.getFacade().isAEnumeration(subject)) {
            if (type.equals(".")) {
                if (feature.equals("ownerScope")) {
                    return Model.getFacade().getOwnerScope(subject);
                }
                if (feature.equals("visibility")) {
                    return Model.getFacade().getVisibility(subject);
                }
                if (feature.equals("owner")) {
                    return Model.getFacade().getOwner(subject);
                }                
            }
        }        
        return null;
    }

    /**
     * Add the metamodel-metaclasses as built-in symbols
     * @param sym the symbol
     * @return the value of the symbol
     * 
     * @see org.argouml.profile.internal.ocl.ModelInterpreter#getBuiltInSymbol(java.lang.String)
     */
    public Object getBuiltInSymbol(String sym) {
        Method m;
        try {
            m = Facade.class.getDeclaredMethod("isA" + sym,
                    new Class[] {Object.class});
            if (m != null) {
                return new OclType(sym.toString());
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return null;
    }

}
