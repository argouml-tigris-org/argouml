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

package org.argouml.uml.ui.behavior.collaborations;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

import javax.swing.*;

import org.argouml.application.api.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.model.uml.UmlFactory;
import org.argouml.uml.MMUtil;

import java.awt.*;
import java.util.*;

public class PropPanelAssociationRole extends PropPanelModelElement {

  ////////////////////////////////////////////////////////////////
  // attributes
    protected JComboBox _baseField;

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelAssociationRole() {
    super("Association Role",_associationRoleIcon, 2);

   //
    //   this will cause the components on this page to be notified
    //      anytime a stereotype, namespace, operation, etc
    //      has its name changed or is removed anywhere in the model
    Class[] namesToWatch = { MStereotype.class,MNamespace.class,MAssociation.class, MMessage.class, MAssociationEndRole.class, MClassifierRole.class, MClassifier.class};
    setNameEventListening(namesToWatch); 
    
    Class mclass = MAssociationRole.class;

    addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
    addField(nameField,1,0,0);

    _baseField = new UMLAssociationComboBox(this);
    addCaption(Argo.localize("UMLMenu", "label.association"), 2, 0, 0);
    addField(_baseField, 2, 0, 0);
    
    addCaption(Argo.localize("UMLMenu", "label.stereotype"),3,0,0);
    addField(stereotypeBox,3,0,0);

    addCaption(Argo.localize("UMLMenu", "label.namespace"),4,0,1);
    addField(namespaceScroll,4,0,0);

    addCaption("Messages:",0,1,0);
    JList messageList = new UMLList(new UMLMessagesListModel(this,"message",true), true);
    messageList.setBackground(getBackground());
    messageList.setForeground(Color.blue);
    addField(new JScrollPane(messageList),0,1,0.75);

    addCaption("AssociationRole Ends:",1,1,0);
    JList assocEndList = new UMLList(new UMLReflectionListModel(this,"connection",true,"getAssociationEnds","setAssociationEnds",null,null),true);
    assocEndList.setBackground(getBackground());
    assocEndList.setForeground(Color.blue);
    addField(new JScrollPane(assocEndList),1,1,0.25);


    new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateNamespace",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete"),"removeElement",null);

  }

    public Collection getAssociationEnds() {
        Collection ends = null;
        Object target = getTarget();
        if(target instanceof MAssociationRole) {
            ends = ((MAssociationRole) target).getConnections();
        }
        return ends;
    }

    public void setAssociationEnds(Collection ends) {
        Object target = getTarget();
        if(target instanceof MAssociationRole) {
            java.util.List list = null;
            if(ends instanceof java.util.List) {
                list = (java.util.List) ends;
            }
            else {
                list = new ArrayList(ends);
            }
            ((MAssociationRole) target).setConnections(list);
        }
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return (baseClass.equals("AssociationRole") || 
                baseClass.equals("Association"));
    }
    
    /**
     * <p> sets the base association of the associationRole </p>
     * @param the association to set as the base
     **/
    public void setAssociation(MAssociation association){
        Object target=getTarget();
        if(target instanceof MAssociationRole){
            MAssociationRole role=(MAssociationRole) target;
            role.setBase(association);
        }
    }
    /**
     * @return the base association of the association role
     **/
    public MAssociation getAssociation(){
        MAssociation assoc=null;
        Object target=getTarget();
        if(target instanceof MAssociationRole){
            MAssociationRole role=(MAssociationRole) target;
            assoc=role.getBase();
        }
        return assoc;
    }
    /**
     * <p> tests if the association is acceptible in this list: </p>
     * <p> it tests if all the classifier of the given association are included 
     * in the list of classifierRoles' classifiers from the current associationRole </p>
     *
     * @param element the association to test
     * @return true if the association can be put in the list
     **/
    public boolean isAcceptibleAssociation(MModelElement element){
        boolean isAcceptible = false;
        if(element instanceof MAssociation) {
            MAssociation assoc=(MAssociation) element;
            Vector classifiers=new Vector();
            //get the classifiers from the associationEndRoles
            Object target=getTarget();
            if(target instanceof MAssociationRole){
                MAssociationRole role=(MAssociationRole) target;
                java.util.List list=role.getConnections();
                Iterator it=list.iterator();
                while(it.hasNext()){
                    MAssociationEndRole endRole=(MAssociationEndRole) it.next();
                    MClassifierRole clRole=(MClassifierRole) endRole.getType();
                    Collection col=clRole.getBases();
                    classifiers.addAll(col);
                }
                //compare with the base association classifiers
                Vector assocClassifiers=new Vector();
                java.util.List list2=assoc.getConnections();
                if(list2!=null){
                    Iterator it2=list2.iterator();
                    while(it2.hasNext()){
                        MAssociationEnd end=(MAssociationEnd) it2.next();
                        MClassifier type=end.getType();
                        assocClassifiers.add(type);
                        }
                }
                if(classifiers.containsAll(assocClassifiers))
                isAcceptible=true;
            }
        }
        return isAcceptible;
    }

} /* end class PropPanelAssociationRole */
