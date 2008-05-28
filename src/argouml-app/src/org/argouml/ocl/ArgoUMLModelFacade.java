
package org.argouml.ocl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.argouml.model.Facade;
import org.argouml.model.Model;

import tudresden.ocl20.core.jmi.uml15.core.Association;
import tudresden.ocl20.core.jmi.uml15.core.Classifier;
import tudresden.ocl20.core.jmi.uml15.core.Enumeration;
import tudresden.ocl20.core.jmi.uml15.core.GeneralizableElement;
import tudresden.ocl20.core.jmi.uml15.core.Namespace;
import tudresden.ocl20.core.jmi.uml15.datatypes.Multiplicity;
import tudresden.ocl20.core.jmi.uml15.datatypes.OrderingKind;
import tudresden.ocl20.core.jmi.uml15.datatypes.ParameterDirectionKind;
import tudresden.ocl20.core.jmi.uml15.datatypes.ScopeKind;
import tudresden.ocl20.integration.Classes;
import tudresden.ocl20.integration.ModelFacade;

/**
 * 
 * TODO: This file was received without any copyright or license information. It
 * needs intellectual property clearance before it can be used.
 * <p>
 * Originally in package ch.ethz.infsec.ocl2Argo. Moved to package
 * org.argouml.ocl since that's where previous facade was.
 * 
 * @author Raphi
 * 
 */
public class ArgoUMLModelFacade extends ModelFacade {
    
    private static final Logger LOG = 
        Logger.getLogger(ArgoUMLModelFacade.class);
    
    private Facade argoFacade = null;
   
    /**
     * Constructor
     */
    public ArgoUMLModelFacade() {
        
        LOG.debug("ArgoUMLModelFacade");
        
        this.argoFacade = Model.getFacade();
        // Add refObjects with mofID ?
        // how to get the root package from argoUML?
        /*
        Project project = ProjectManager.getManager().getCurrentProject();
        MemberList members = project.getMembers();
        for (int i=0; i<members.size(); i++) {
            Object member = members.get(i);
            LOG.debug(member.getClass().getName());
            this.addRefObject(Integer.toString(i), member);
        }
        */
    }

    @Override
    /**
     * Identify the association of an association end.
     */
    public Association getAssociation(String mofID) {

        LOG.debug(getCallerDepth() + "getAssociation");        
        Object argoElement = this.getRefObject(mofID);
        Association oclAssociation = null;
        if (argoElement instanceof org.omg.uml.foundation.core.AssociationEnd) {
            org.omg.uml.foundation.core.UmlAssociation argoAssociation = 
                ((org.omg.uml.foundation.core.AssociationEnd)argoElement).getAssociation();
            oclAssociation = 
                (Association) this.getElement(Classes.association, argoAssociation);
        } else {
            LOG.debug("getAssociation " 
                    + argoElement.getClass().getName());
            LOG.debug("WARNING: please check the model facade "
                    + "implementation at getAssociation!");
        }
        return oclAssociation;
    }

    @Override
    /**
     * Identify all association ends of a classifier.
     */
    public Collection getAssociationEnds(String mofID) {
        
//            LOG.debug(getCallerDepth() + "getAssociationEnds of " + this.getName(mofID));
        LOG.debug(getCallerDepth() + "getAssociationEnds");
        
        Object argoElement = this.getRefObject(mofID);
        Collection argoAssociationEnds = 
            this.argoFacade.getAssociationEnds(argoElement);
        //Collection argoAssociationEnds = this.argoFacade.getAssociationRoles(argoElement);
        Iterator i = argoAssociationEnds.iterator();
        
        Collection oclAssociationEnds = new ArrayList();
        while (i.hasNext()) {
            Object next = i.next();
            
//                LOG.debug(next.getClass().getName());
//                LOG.debug(((org.omg.uml.foundation.core.ModelElement)next).getName());
            
            Object element = this.getElement(Classes.associationend, next);
            oclAssociationEnds.add(element);
        }
        return oclAssociationEnds;
    }

    @Override
    /**
     * Identify the subclass of a generalization.
     */
    public GeneralizableElement getChild(String mofID) {
        
        LOG.debug(getCallerDepth() + "getChild");
        
        Object argoElement = this.getRefObject(mofID);
        org.omg.uml.foundation.core.GeneralizableElement argoChild = 
            (org.omg.uml.foundation.core.GeneralizableElement) this.argoFacade.getChild(argoElement);
        GeneralizableElement oclChild = (GeneralizableElement) this.getElement(Classes.umlClass, argoChild);
        return oclChild;
    }

    @Override
    /**
     * Identify the association ends of an association.
     */
    public List getConnection(String mofID) {
        
        LOG.debug(getCallerDepth() + "getConnection");
        
        Object argoElement = this.getRefObject(mofID);
        Collection argoConnection = this.argoFacade.getConnections(argoElement);
        ArrayList oclConnection = new ArrayList();
        
        Iterator i = argoConnection.iterator();
        int testCounter = 0;
        while (i.hasNext()) {
            testCounter++;
            Object argoAE = i.next();
            Object oclAE = this.getElement(Classes.associationend, argoAE);
            oclConnection.add(oclAE);
        }
        
        if (testCounter > 2) {
            LOG.debug("WARNING at getConnection: There were more than 2 association ends found!");
        }
        return oclConnection;
    }

    @Override
    /**
     * Identify the enumeration of an enumeration literal.
     */
    public Enumeration getEnumeration(String mofID) {
        
        LOG.debug(getCallerDepth() + "getEnumeration");

        Object argoElement = this.getRefObject(mofID);
        org.omg.uml.foundation.core.Enumeration argoEnumeration = 
            (org.omg.uml.foundation.core.Enumeration) this.argoFacade.getEnumeration(argoElement);
        Enumeration oclEnumeration = 
            (Enumeration) this.getElement(Classes.enumeration, argoEnumeration);
        return oclEnumeration;
    }

    @Override
    /**
     * Identify all attributes and operations of a classifier.
     */
    public List getFeature(String mofID) {
        
        LOG.debug(getCallerDepth() + "getFeature");
        
        Object argoElement = this.getRefObject(mofID);
        Collection argoFeatures = this.argoFacade.getFeatures(argoElement);
        
        ArrayList oclFeatures = new ArrayList();
        Iterator i = argoFeatures.iterator();
        while (i.hasNext()) {
            Object argoFeature = i.next();
            if (argoFeature instanceof org.omg.uml.foundation.core.Attribute) {
                // Attribute extends StructuralFeature extends Feature
                oclFeatures.add(this.getElement(Classes.attribute, argoFeature));
//            } else if (argoFeature instanceof org.omg.uml.foundation.core.Method) {
//                // Method extends BehavioralFeature extends Feature
//                oclFeatures.add(this.getElement(Classes.method, argoFeature));
            } else if (argoFeature instanceof org.omg.uml.foundation.core.Operation) {
                // Operation extends BehavioralFeature extends Feature
                oclFeatures.add(this.getElement(Classes.method, argoFeature));  
            } else {
                LOG.debug("getFeature " 
                        + argoFeature.getClass().getName());
                LOG.debug("WARNING: please check the model "
                        + "facade implementation at getFeature!");
            }
        }
        return oclFeatures;
    }

    @Override
    /**
     * Identify the generalization of a classifier.
     */
    public Collection getGeneralization(String mofID) {
        
        LOG.debug(getCallerDepth() + "getGeneralization");
        
        Object argoElement = this.getRefObject(mofID);
        Collection argoGeneralization = 
            this.argoFacade.getGeneralizations(argoElement);
        Collection oclGeneralization = new ArrayList();
        
        Iterator i = argoGeneralization.iterator();
        while (i.hasNext()) {
            Object argoG = i.next();
            Object oclG = this.getElement(Classes.generalization, argoG);
            oclGeneralization.add(oclG);
        }
        return oclGeneralization;
    }

    @Override
    /**
     * Identify the direction kind of a parameter.
     * @return Either IN, OUT, INOUT or RETURN.
     */
    public ParameterDirectionKind getKind(String mofID) {
        
        LOG.debug(getCallerDepth() + "getKind");
        
        Object argoElement = this.getRefObject(mofID);
        Object argoKind = this.argoFacade.getKind(argoElement);
        
        ParameterDirectionKind oclKind = null;
        if (argoKind instanceof org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum) {
            argoKind = (org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum) argoKind;
            if (argoKind == org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum.PDK_IN) {
                oclKind = (ParameterDirectionKind) this.getElement(
                        Classes.parameterDirectionKind_In, argoKind);
            } else if (argoKind == org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum.PDK_INOUT) {
                oclKind = (ParameterDirectionKind) this.getElement(
                        Classes.parameterDirectionKind_InOut, argoKind);
            } else if (argoKind == org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum.PDK_OUT) {
                oclKind = (ParameterDirectionKind) this.getElement(
                        Classes.parameterDirectionKind_Out, argoKind);
            } else if (argoKind == org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum.PDK_RETURN) {
                oclKind = (ParameterDirectionKind) this.getElement(
                        Classes.parameterDirectionKind_Return, argoKind);
            } else {
                LOG.debug("getKind "
                        + argoKind.getClass().getName());
                LOG.debug("WARNING: please check the model "
                        + "facade implementation at getKind!");
            }
        } else {
            LOG.debug("getKind " + argoKind.getClass().getName());
            LOG.debug("WARNING: please check the model facade "
                    + "implementation at getKind!");
        }
        return oclKind;
    }

    @Override
    /**
     * Identify the literals of an enumeration.
     */
    public List getLiteral(String mofID) {
        
        LOG.debug(getCallerDepth() + "getLiteral");
        
        Object argoElement = this.getRefObject(mofID);
        ArrayList oclLiteral = new ArrayList();
        if (argoElement instanceof org.omg.uml.foundation.core.Enumeration) {
            List argoLiteral = 
                ((org.omg.uml.foundation.core.Enumeration)argoElement).getLiteral();
            Iterator i = argoLiteral.iterator();
            while (i.hasNext()) {
                Object argoL = i.next();
                Object oclL = this.getElement(Classes.enumerationLiteral, argoL);
                oclLiteral.add(oclL);
            }
        } else {
            
                LOG.debug("getLiteral "
                        + argoElement.getClass().getName());
                LOG.debug("WARNING: please check the model facade "
                        + "implementation at getLiteral!");
            
        }
        return oclLiteral;
    }

    @Override
    /**
     * Identify the multiplicity of an attribute or of an associationend.
     */
    public Multiplicity getMultiplicity(String mofID) {
        
            LOG.debug(getCallerDepth() + "getMultiplicity");
        
        Object argoElement = this.getRefObject(mofID);
        Object argoMultiplicity = this.argoFacade.getMultiplicity(argoElement);
        Multiplicity oclMultiplicity = null;
        oclMultiplicity = (Multiplicity) this.getElement(Classes.multiplicity,
                argoMultiplicity);
        return oclMultiplicity;
    }

    @Override
    /**
     * Identify the name of a model element.
     */
    public String getName(String mofID) {
        
        LOG.debug(getCallerDepth() + "getName");
        
        Object argoElement = this.getRefObject(mofID);
        String name = this.argoFacade.getName(argoElement);
        
        LOG.debug(getCallerDepth() + name);
        return name;
    }

    @Override
    /**
     * Identify the namespace of a model element.
     */
    public Namespace getNamespace(String mofID) {
        
        LOG.debug(getCallerDepth() + "getNamespace");
        
        Object argoElement = this.getRefObject(mofID);
        Object argoNamespace = this.argoFacade.getNamespace(argoElement);
        Namespace oclNamespace = null;
        
        if (argoElement instanceof org.omg.uml.modelmanagement.UmlPackage) {
            oclNamespace = (Namespace) this.getElement(Classes.umlPackage,
                    argoNamespace);
        } else if (argoElement instanceof org.omg.uml.foundation.core.UmlClass) {
            oclNamespace = (Namespace) this.getElement(Classes.umlPackage,
                    argoNamespace);
        } else if (argoElement instanceof org.omg.uml.foundation.core.Interface) {
            oclNamespace = (Namespace) this.getElement(Classes.umlPackage,
                    argoNamespace);
        } else if (argoElement instanceof org.omg.uml.foundation.core.Enumeration) {
            oclNamespace = (Namespace) this.getElement(Classes.umlPackage,
                    argoNamespace);
        } else if (argoElement instanceof org.omg.uml.foundation.core.DataType) {
            oclNamespace = (Namespace) this.getElement(Classes.umlClass,
                    argoNamespace);
        } else if (argoElement instanceof org.omg.uml.foundation.core.Attribute) {
            // Attribute extends StructuralFeature extends Feature
            oclNamespace = (Namespace) this.getElement(Classes.umlClass,
                    argoNamespace);
        } else if (argoElement instanceof org.omg.uml.foundation.core.Method) {
            // Method extends BehavioralFeature extends Feature
            oclNamespace = (Namespace) this.getElement(Classes.umlClass,
                    argoNamespace);
        } else if (argoElement instanceof org.omg.uml.foundation.core.Operation) {
            // Operation extends BehavioralFeature extends Feature
            oclNamespace = (Namespace) this.getElement(Classes.umlClass,
                    argoNamespace);
        } else {
            
                LOG.debug("getNamespace "
                        + argoElement.getClass().getName());
                LOG.debug("WARNING: please check the model facade"
                        + " implementation at getNamespace!");
            
        }
        return oclNamespace;
    }

    @Override
    /**
     * Identify the OrderingKind of an Attribute or of an AssociationEnd
     * @return ORDERED or UNORDERED
     */
    public OrderingKind getOrdering(String mofID) {
        
            LOG.debug(getCallerDepth() + "getOrdering");
        
        Object argoElement = this.getRefObject(mofID);
        Object argoOrdering = null;
        if (argoElement instanceof org.omg.uml.foundation.core.AssociationEnd) {
            argoOrdering = this.argoFacade.getOrdering(argoElement);
        } else if (argoElement instanceof org.omg.uml.foundation.core.Attribute) {
            argoOrdering = ((org.omg.uml.foundation.core.Attribute) argoElement)
                    .getOrdering();
        } else {
            
                LOG.debug("getOrdering "
                        + argoElement.getClass().getName());
                LOG.debug("WARNING: please check the model facade"
                        + " implementation at getOrdering!");
            
        }
        
        OrderingKind oclOrdering = null;
        if (((org.omg.uml.foundation.datatypes.OrderingKindEnum) argoOrdering) == org.omg.uml.foundation.datatypes.OrderingKindEnum.OK_ORDERED) {
            oclOrdering = (OrderingKind) this.getElement(
                    Classes.orderingKind_ordered, argoOrdering);
        } else if (((org.omg.uml.foundation.datatypes.OrderingKindEnum) argoOrdering) == org.omg.uml.foundation.datatypes.OrderingKindEnum.OK_UNORDERED) {
            oclOrdering = (OrderingKind) this.getElement(
                    Classes.orderingKind_unordered, argoOrdering);
        } else {
            
                LOG.debug("getOrdering "
                        + argoOrdering.getClass().getName());
                LOG.debug("WARNING: please check the model facade"
                        + " implementation at getOrdering!");
            
        }
        return oclOrdering;
    }

    @Override
    /**
     * Ermittelt sämtliche im Paket oder Classifier enthaltene Elemente.
     */
    public Collection getOwnedElements(String mofID) {
        
            LOG.debug(getCallerDepth() + "getOwnedElements");
            //LOG.debug("getOwnedElements " + mofID);
        
    
        ArrayList ownedElements = new ArrayList();
        Object argoElement = this.getRefObject(mofID);

        if (argoElement instanceof org.omg.uml.foundation.core.Namespace) {
            // UmlPackage, UmlClass, Interface, Enumeration and DataType are subtypes of Namespace!
            
            org.omg.uml.foundation.core.Namespace argoNamespace = (org.omg.uml.foundation.core.Namespace)argoElement;
            
//                LOG.debug(argoElement.getClass().getName());
//                LOG.debug(argoNamespace.getName());
            
            Collection argoCollection = this.argoFacade.getOwnedElements(argoNamespace);
            this.addOwnedElements(argoCollection, ownedElements);
        } else {
            
                LOG.debug("getOwnedElements " + argoElement.getClass().getName());
                LOG.debug("WARNING: please check the model facade implementation at getOwnedElements!");
            
        }
        return ownedElements;
        
        
        /*
        // Ist die instanceof Unterscheidung nötig?? UmlClass etc. sind Subklassen von Namespace!!!
        if (o instanceof org.omg.uml.foundation.core.UmlClass) {
            org.omg.uml.foundation.core.UmlClass argoUmlClass = (org.omg.uml.foundation.core.UmlClass)o;
            LOG.debug(argoUmlClass.getName());
            Collection argoCollection = this.argoFacade.getOwnedElements(argoUmlClass);
            this.addOwnedElements(argoCollection, ownedElements);
        }        
        else if (o instanceof org.omg.uml.foundation.core.Enumeration) {
            org.omg.uml.foundation.core.Enumeration argoEnumeration = (org.omg.uml.foundation.core.Enumeration)o;
            LOG.debug(argoEnumeration.getName());
            Collection argoCollection = this.argoFacade.getOwnedElements(argoEnumeration);
            this.addOwnedElements(argoCollection, ownedElements);
        }
        else if (o instanceof org.omg.uml.foundation.core.Interface) {
            org.omg.uml.foundation.core.Interface argoUmlInterface = (org.omg.uml.foundation.core.Interface)o;
            LOG.debug(argoUmlInterface.getName());
            Collection argoCollection = this.argoFacade.getOwnedElements(argoUmlInterface);
            this.addOwnedElements(argoCollection, ownedElements);
        } 
        else if (o instanceof org.omg.uml.modelmanagement.UmlPackage)    {
            // Model extends UmlPackage extends Namespace 
            org.omg.uml.modelmanagement.UmlPackage argoUmlPackage = (org.omg.uml.modelmanagement.UmlPackage)o;
            LOG.debug(argoUmlPackage.getName());
            Collection argoCollection = this.argoFacade.getOwnedElements(argoUmlPackage);
            // alternative:
            //Collection argoCollection = argoNamespace.getOwnedElement();
            //ownedElements.add(this.getElement(Classes.umlPackage, argoNamespace));
            this.addOwnedElements(argoCollection, ownedElements);
        } else {
            LOG.debug("WARNING: ERROR IN MODELFACADE IMPLEMENTATION AT getOwnedElements!!!");
        }
        */
    }
    
    /**
     * Converts a collection of ArgoUML elements into a collection of OCL elements. 
     * @param argoCollection The ArgoUML elements.
     * @param ownedElements The OCL elements. 
     */
    private void addOwnedElements(Collection argoCollection, Collection ownedElements) {
        
        LOG.debug(getCallerDepth() + "addOwnedElements");
        
        Iterator i = argoCollection.iterator();
        while (i.hasNext()) {
            Object argoElement = i.next();
            //LOG.debug("addOwnedElements " + argoElement.getClass().getName());
            
            if (argoElement instanceof org.omg.uml.modelmanagement.UmlPackage) {
                //LOG.debug(((org.omg.uml.foundation.core.Namespace)argoElement).getName());
                Object oclElement = this.getElement(Classes.umlPackage, argoElement); 
                ownedElements.add(oclElement);
            } else if (argoElement instanceof org.omg.uml.foundation.core.UmlClass) {
                //LOG.debug(((org.omg.uml.foundation.core.UmlClass)argoElement).getName());
                Object oclElement = this.getElement(Classes.umlClass, argoElement); 
                ownedElements.add(oclElement);
            } else if (argoElement instanceof org.omg.uml.foundation.core.Interface) {
                //LOG.debug(((org.omg.uml.foundation.core.Interface)argoElement).getName());
                Object oclElement = this.getElement(Classes.umlinterface, argoElement); 
                ownedElements.add(oclElement);
            } else if (argoElement instanceof org.omg.uml.foundation.core.Enumeration) {
                //LOG.debug(((org.omg.uml.foundation.core.Enumeration)argoElement).getName());
                Object oclElement = this.getElement(Classes.enumeration, argoElement); 
                ownedElements.add(oclElement);
            } else if (argoElement instanceof org.omg.uml.foundation.core.DataType) {
                //LOG.debug(((org.omg.uml.foundation.core.DataType)argoElement).getName());
                Object oclElement = this.getElement(Classes.dataType, argoElement); 
                ownedElements.add(oclElement);
            } else if (argoElement instanceof org.omg.uml.foundation.core.UmlAssociation) {
                Object oclElement = this.getElement(Classes.association, argoElement); 
                ownedElements.add(oclElement);
            } else if (argoElement instanceof org.omg.uml.foundation.core.Generalization) {
                Object oclElement = this.getElement(Classes.generalization, argoElement); 
                ownedElements.add(oclElement);
            } else {
                // org.omg.uml.foundation.core.Comment
                // org.omg.uml.foundation.core.Constraint
                // org.omg.uml.foundation.core.Stereotype
//                
                    LOG.debug("At addOwnedElements: " + argoElement.getClass().getName() + " is not supported");
//                }
            }
        }            
    }

    @Override
    /**
     * Identify the owner of a method or an attribute.
     */
    public Classifier getOwner(String mofID) {
        
        LOG.debug(getCallerDepth() + "getOwner");
        
        Object argoElement = this.getRefObject(mofID);
        org.omg.uml.foundation.core.Classifier argoOwner = (org.omg.uml.foundation.core.Classifier) this.argoFacade.getOwner(argoElement); 
        
        Object oclOwner = null;
        
        if (argoOwner instanceof org.omg.uml.foundation.core.UmlClass) {
            oclOwner = this.getElement(Classes.umlClass, argoOwner); 
        } else if (argoOwner instanceof org.omg.uml.foundation.core.Interface) {
            oclOwner = this.getElement(Classes.umlinterface, argoOwner); 
        } else if (argoOwner instanceof org.omg.uml.foundation.core.Enumeration) {
            oclOwner = this.getElement(Classes.enumeration, argoOwner); 
        } else if (argoOwner instanceof org.omg.uml.foundation.core.DataType) {
            oclOwner = this.getElement(Classes.dataType, argoOwner); 
        } else {
            // org.omg.uml.foundation.core.Primitive and org.omg.uml.foundation.core.ProgrammingLanguageDataType are included in org.omg.uml.foundation.core.DataType??
            
                LOG.debug("getOwner " + argoOwner.getClass().getName());
                LOG.debug("WARNING: please check the model facade implementation at getOwner!");
         
        }
        return (Classifier) oclOwner;        
    }

    @Override
    /**
     * Identify the owner-scope of a feature.
     * @return Instance or Classifier. 
     */
    public ScopeKind getOwnerScope(String mofID) {
        
        LOG.debug(getCallerDepth() + "getOwnerScope");
        
        Object argoElement = this.getRefObject(mofID);
        org.omg.uml.foundation.datatypes.ScopeKind argoScopeKind = (org.omg.uml.foundation.datatypes.ScopeKind) this.argoFacade.getOwnerScope(argoElement); 
        ScopeKind oclScopeKind = null;
        
        if (argoScopeKind == org.omg.uml.foundation.datatypes.ScopeKindEnum.SK_CLASSIFIER) {
            oclScopeKind = (ScopeKind) this.getElement(Classes.scopeKind_classifier, argoScopeKind);
        } else if (argoScopeKind == org.omg.uml.foundation.datatypes.ScopeKindEnum.SK_INSTANCE) {
            oclScopeKind = (ScopeKind) this.getElement(Classes.scopeKind_instance, argoScopeKind);
        }
        return oclScopeKind;
    }

    @Override
    /**
     * Identify the parameters of a method.
     * The return parameter is not included! getType is used to identify the return parameter.
     */
    public List getParameter(String mofID) {
        
        LOG.debug(getCallerDepth() + "getParameter");
        
        Object argoElement = this.getRefObject(mofID);
        Collection argoParameter = this.argoFacade.getParameters(argoElement); 
        List oclParameter = new ArrayList();
        
        Iterator i = argoParameter.iterator();
        while (i.hasNext()) {
            Object argoP = i.next();
            //if (!(argoP.getKind() == org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum.PDK_RETURN)) {
                // Do not inlcude the return parameter?
                Object oclP = this.getElement(Classes.parameter, argoP);
                oclParameter.add(oclP);
            //}    
        }
        return oclParameter;
    }

    @Override
    /**
     * Identify the super class of a generalization.
     */
    public GeneralizableElement getParent(String mofID) {
        
        LOG.debug(getCallerDepth() + "getParent");
        
        Object argoElement = this.getRefObject(mofID);
        org.omg.uml.foundation.core.GeneralizableElement argoParent = (org.omg.uml.foundation.core.GeneralizableElement) this.argoFacade.getParent(argoElement);
        GeneralizableElement oclParent = (GeneralizableElement) this.getElement(Classes.umlClass, argoParent);        
        return oclParent;
    }

    @Override
    /**
     * Identify the classifier of an association end.
     */
    public Classifier getParticipant(String mofID) {
        
        LOG.debug(getCallerDepth() + "getParticipant");
        
        Object argoElement = this.getRefObject(mofID);
        
        Classifier oclClassifier = null;
        if (argoElement instanceof org.omg.uml.foundation.core.AssociationEnd) {
            org.omg.uml.foundation.core.Classifier argoClassifier = ((org.omg.uml.foundation.core.AssociationEnd)argoElement).getParticipant();
            oclClassifier = (Classifier) this.getElement(Classes.umlClass, argoClassifier);
        } else {

            LOG.debug("getParticipant " + argoElement.getClass().getName());
            LOG.debug("WARNING: please check the model facade implementation at getParticipant!");

        }
        return oclClassifier;
    }

    @Override
    /**
     * Identify the qualifier of an association end.
     */
    public List getQualifier(String mofID) {
        
        LOG.debug(getCallerDepth() + "getQualifier");
        
        Object argoElement = this.getRefObject(mofID);
        Collection argoQualifiers = this.argoFacade.getQualifiers(argoElement);
        ArrayList oclQualifiers = new ArrayList();
        
        Iterator i = argoQualifiers.iterator();
        while (i.hasNext()) {
            Object argoQ = i.next();
            Object oclQ = this.getElement(Classes.attribute, argoQ);
            oclQualifiers.add(oclQ);
        }
        return oclQualifiers;
    }

    @Override
    /**
     * Identify the multiplicity-range of a multiplicity.
     */
    public Collection getRange(String mofID) {
        
        LOG.debug(getCallerDepth() + "getRange");
        
        Object argoElement = this.getRefObject(mofID);
        Iterator argoRange = this.argoFacade.getRanges(argoElement);
        Collection oclRange = new ArrayList();
        
        while (argoRange.hasNext()) {
            Object argoR = argoRange.next();
            Object oclR = this.getElement(Classes.range, argoR);
            oclRange.add(oclR);
        }
        return oclRange;
    }

    @Override
    /**
     * Identify the specialization of a model element.
     */
    public Collection getSpecialization(String mofID) {
        
        LOG.debug(getCallerDepth() + "getSpecialization");
        
        Object argoElement = this.getRefObject(mofID);
        Collection argoSpecializations = this.argoFacade.getSpecializations(argoElement);
        Collection oclSpecializations = new ArrayList();
        
        Iterator i = argoSpecializations.iterator();
        while (i.hasNext()) {
            Object argoS = i.next();
            Object oclS = this.getElement(Classes.generalization, argoS);
            oclSpecializations.add(oclS);
        }
        return oclSpecializations;
    }


    /**
     * Identify the type of a parameter, an attribute, a qualifier or the type 
     * of the return value of a method.
     */
    @Override
    public Classifier getType(String mofID) {
        
        LOG.debug(getCallerDepth() + "getType");
        
        Object argoElement = this.getRefObject(mofID);
        
        org.omg.uml.foundation.core.Classifier argoClassifier = null;
        if (argoElement instanceof org.omg.uml.foundation.core.Parameter) {
            argoClassifier = (org.omg.uml.foundation.core.Classifier) this.argoFacade
                    .getType(argoElement);
        } else if (argoElement instanceof org.omg.uml.foundation.core.Attribute) {
            argoClassifier = ((org.omg.uml.foundation.core.Attribute) argoElement)
                    .getType();
            // alternative:
            // argoClassifier = (org.omg.uml.foundation.core.Classifier)
            // this.argoFacade.getType(o);
        } else if (argoElement instanceof org.omg.uml.foundation.core.AQualifierAssociationEnd) {
            argoClassifier = (org.omg.uml.foundation.core.Classifier) this.argoFacade
                    .getType(argoElement);
        }
        else if (argoElement instanceof org.omg.uml.foundation.core.Method) {
            Iterator i = this.argoFacade.getParameters(argoElement).iterator();
            while (i.hasNext()) {
                Object argoP = i.next();
                if (Model.getFacade().hasReturnParameterDirectionKind(argoP)) {
                    argoClassifier = (org.omg.uml.foundation.core.Classifier) this.argoFacade
                            .getType(argoP);
                    break;
                }
            }
        } else {

            LOG.debug(argoElement.getClass().getName());
            LOG.debug("WARNING: please check the model facade implementation at getType!");

            return null;
        }
        
        if (argoClassifier instanceof org.omg.uml.foundation.core.UmlClass) {
            return (Classifier) this.getElement(Classes.umlClass,
                    argoClassifier);
        } else if (argoClassifier instanceof org.omg.uml.foundation.core.Interface) {
            return (Classifier) this.getElement(Classes.umlinterface,
                    argoClassifier);
        } else if (argoClassifier instanceof org.omg.uml.foundation.core.Enumeration) {
            return (Classifier) this.getElement(Classes.enumeration,
                    argoClassifier);
        } else if (argoClassifier instanceof org.omg.uml.foundation.core.DataType) {
            // Primitive and ProgrammingLanguageDataType are subclasses of
            // DataType
            return (Classifier) this.getElement(Classes.dataType,
                    argoClassifier);
        } 
        else {

            LOG.debug(argoClassifier.getClass().getName());
            LOG.debug("WARNING: please check the model facade implementation at getType!");

            return null;
        }
    }

    @Override
    /**
     * Identify the upper bound of a multiplicity range.
     */
    public int getUpper(String mofID) {
        
        LOG.debug(getCallerDepth() + "getUpper");
        
        Object argoElement = this.getRefObject(mofID);
        return this.argoFacade.getUpper(argoElement);
    }

    @Override
    public boolean hasRefObject(String mofID) {
        
        LOG.debug(getCallerDepth() + "hasRefObject");

        return this.getRefObject(mofID) != null;
    }
    
    private String getCallerDepth() {
        /*
        String depth = new String();
        Exception e = new Exception();
        int d = e.getStackTrace().length;
        
        for (int i=0; i<d; i++) {
            depth = depth + " ";
        }
        
        return depth;
        */
        return new String();
    }

}
