/*
 * OldToSpecConverter.java
 *
 * Created on 10. Dezember 2002, 09:56
 */

package org.argouml.uml.diagram.sequence.specification_level;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import org.argouml.kernel.ProjectManager;
import org.argouml.uml.ProjectMemberModel;

import org.argouml.model.uml.UmlFactory;
import org.argouml.uml.diagram.sequence.specification_level.ui.FigSeqAsRole;
import org.argouml.uml.diagram.sequence.specification_level.ui.FigSeqClRole;
import org.argouml.uml.diagram.sequence.specification_level.ui.FigSeqInteraction;
import org.argouml.uml.diagram.sequence.specification_level.ui.FigSeqMessage;
import org.argouml.uml.diagram.sequence.specification_level.ui.SequenceDiagramLayout;
import org.argouml.uml.diagram.sequence.specification_level.SequenceDiagramGraphModel;
import org.argouml.uml.diagram.sequence.ui.FigSeqLink;
import org.argouml.uml.diagram.sequence.ui.FigSeqObject;
import org.argouml.uml.diagram.sequence.ui.FigSeqStimulus;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;

import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigPoly;

import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.behavior.collaborations.MAssociationEndRole;
import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.behavior.collaborations.MCollaboration;
import ru.novosoft.uml.behavior.collaborations.MInteraction;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MInstance;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MObject;
import ru.novosoft.uml.behavior.common_behavior.MStimulus;

import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MModelImpl;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.behavior.common_behavior.MAction;

/**
 *
 * @author  Administrator
 */
public final class OldToSpecConverter {

    /** Creates a new instance of OldToSpecConverter */
    private OldToSpecConverter() {
    }

    public static void convertDiagramContents(UMLSequenceDiagram diagram) {
        MInteraction _interaction;
        SequenceDiagramGraphModel gm = (SequenceDiagramGraphModel)diagram.getGraphModel();
        if (gm == null) {
            throw new RuntimeException("GraphModel required");
        }
        Vector v  = diagram.getLayer().getContents();
        Vector vo = new Vector();
        Vector vs = new Vector();
        Vector vl = new Vector();
        int ci = 0;     // Count of Interactions/FigSeqInteraction
        int cc = 0;     // Count of ClassifierRoles/FigSeqClRole
        int cm = 0;     // Count of Messages/FigSeqMessage
        int ca = 0;     // Count of AssociationRoles/FigSeqAsRole
        int co = 0;     // Count of MObjects (old model)
        int cs = 0;     // Count of MStimuli (old model)
        int cl = 0;     // Count of MLinks (old model)
        Iterator it = v.iterator();
        Object obj = null;
        while (it.hasNext()) {
            obj = it.next();
            if (obj instanceof FigSeqObject) {
                vo.add(obj);
                co++;
            } else if (obj instanceof FigSeqStimulus) {
                vs.add(obj);
                cs++;
            } else if (obj instanceof FigSeqLink) {
                vl.add(obj);
                cl++;
            } else if (obj instanceof FigSeqClRole) {
                cc++;
            } else if (obj instanceof FigSeqMessage) {
                cm++;
            } else if (obj instanceof FigSeqAsRole) {
                ca++;
            } else if (obj instanceof FigSeqInteraction) {
                ci++;
                if (ci > 1) throw new RuntimeException("More than one interaction in diagram");
            } else {
                throw new RuntimeException("Unknown figure type in diagram: " + obj.getClass().getName());
            }
        }
        if (co == 0 && cl == 0 && cs == 0) return;
        if (cc != 0 || ca != 0 || cm != 0 || ci != 0) throw new RuntimeException("Mixture of old and new figure types in this diagram");
        _interaction = UmlFactory.getFactory().getCollaborations().buildInteraction((MCollaboration)diagram.getNamespace());
        _interaction.setName(diagram.getName() + " interaction");
        FigSeqInteraction fi = new FigSeqInteraction(gm, _interaction);
        gm.addNode(fi);
        diagram.add(fi);
        FigSeqObject[]   ao = (FigSeqObject[])vo.toArray(new FigSeqObject[0]);
        FigSeqStimulus[] as = (FigSeqStimulus[])vs.toArray(new FigSeqStimulus[0]);
        FigSeqLink[]     al = (FigSeqLink[])vl.toArray(new FigSeqLink[0]);
        FigSeqClRole[]   ac = new FigSeqClRole[ao.length];
        FigSeqMessage[]  am = new FigSeqMessage[as.length];
        FigSeqAsRole[]   aa = new FigSeqAsRole[al.length];
        int i = 0;
        for(; i < ao.length; i++) {
            ac[i] = (FigSeqClRole)convertFigure(ao[i], null, diagram, _interaction);
            gm.addNode(ac[i]);
            diagram.add(ac[i]);
        }
        for(i = 0; i < as.length; i++) {
            am[i] = (FigSeqMessage)convertFigure(as[i], new Fig[][]{ao, ac}, diagram, _interaction);
            addToInteraction(_interaction, (MMessage)am[i].getOwner());
            gm.addNode(am[i]);
            diagram.add(am[i]);
            MessageSupervisor.addMessageFig((SequenceDiagramLayout)diagram.getLayer(), am[i]);
        }
        TreeMap sortedMessages = new TreeMap();
        int y;
        Collection hc;
        MMessage[] hm;
        for(i = 0; i < al.length; i++) {
            aa[i] = (FigSeqAsRole)convertFigure(al[i], new Fig[][]{ao, as, ac, am}, diagram, _interaction);
            y = al[i].getFirstPoint().y;
            if (sortedMessages.containsKey(new Integer(y))) {
                throw new RuntimeException("Cannot convert sequence diagram, two messages with same y-value");
            } else {
                hc = ((MAssociationRole)aa[i].getOwner()).getMessages();
                hm = (MMessage[])hc.toArray(new MMessage[0]);
                sortedMessages.put(new Integer(y), hm[0]);
            }
            gm.addEdge(aa[i]);
            diagram.add(aa[i]);
        }
        Vector   imv = new Vector();  // Initial messages vector to initialize the MessageSupervisor
        Iterator imi = sortedMessages.keySet().iterator();
        while (imi.hasNext()) {
            imv.add(sortedMessages.get(imi.next()));
        }
        MessageSupervisor.createContainerFromConvertedMessages((SequenceDiagramLayout)diagram.getLayer(), imv);
        for(i = 0; i < al.length; i++) {
            diagram.remove(al[i]);
            gm.getNamespace().removeOwnedElement((MModelElement)al[i].getOwner());
            gm.removeEdge(al[i]);
        }
        for(i = 0; i < ao.length; i++) {
            diagram.remove(ao[i]);
            gm.getNamespace().removeOwnedElement((MModelElement)ao[i].getOwner());
            gm.removeNode(ao[i]);
        }

        for(i = 0; i < as.length; i++) {
            diagram.remove(as[i]);
System.out.println("MStimulus.namespace = " + ((MModelElement)as[i].getOwner()).getNamespace().getName());
            gm.getNamespace().removeOwnedElement((MModelElement)as[i].getOwner());
            gm.removeNode(as[i]);
        }

        MModel m = ((ProjectMemberModel)ProjectManager.getManager().getCurrentProject().getMembers().get(0)).getModel();
        debugProjectMembers();
    }

    public static Fig convertFigure(Fig figOld, Fig[][] figs, UMLSequenceDiagram diagram, MInteraction inter) {
        if (figOld instanceof FigSeqClRole) return figOld;
        if (figOld instanceof FigSeqAsRole) return figOld;
        if (figOld instanceof FigSeqMessage) return figOld;
        MModelElement element = (MModelElement)figOld.getOwner();
        Fig figNew = null;
        if (figOld instanceof FigSeqObject) {
            MClassifierRole ne = cvObjectToClassifierRole((MObject)element);
            ne.setNamespace(element.getNamespace());
            figNew = new FigSeqClRole(ne);
        } else if (figOld instanceof FigSeqLink) {
            MAssociationRole ne = cvLinkToAssociationRole((MLink)element, (FigSeqObject[])figs[0], (FigSeqStimulus[])figs[1], (FigSeqClRole[])figs[2], (FigSeqMessage[])figs[3]);
            ne.setNamespace(element.getNamespace());
            figNew = new FigSeqAsRole(ne);
        } else if (figOld instanceof FigSeqStimulus) {
            MMessage ne = cvStimulusToMessage((MStimulus)element, (FigSeqObject[])figs[0], (FigSeqClRole[])figs[1]);
            ne.setInteraction(inter);
            inter.addMessage(ne);
            MNamespace nsp = inter.getNamespace();
//            ne.setNamespace(inter.getNamespace());
System.out.println("Created Message from Stimulus, old Namespace = "
                 + ((MStimulus)figOld.getOwner()).getNamespace().getName());
//                 + ", new Namespace = "
//                 + ne.getNamespace().getName());
            SequenceDiagramGraphModel gm = (SequenceDiagramGraphModel)diagram.getGraphModel();
            figNew = new FigSeqMessage(gm, ne);
        }
        if (figNew == null) {
            throw new IllegalArgumentException("Cannot convert figure");
        } else {
System.out.println("figOld.getLocation() = " + figOld.getLocation());
System.out.println("figOld.getBounds() = " + figOld.getBounds());
//            figNew.setBounds(figOld.getBounds());
            figNew.setLocation(figOld.getLocation());
            return figNew;
        }
    }

    private static MClassifierRole cvObjectToClassifierRole(MObject mo) {
        MClassifierRole obj = UmlFactory.getFactory().getCollaborations().createClassifierRole();
        obj.setName(mo.getName());
        obj.setBases(mo.getClassifiers());
        return obj;
    }
    private static MMessage cvStimulusToMessage(MStimulus ms, FigSeqObject[] fo, FigSeqClRole[] fc) {
        MMessage obj = UmlFactory.getFactory().getCollaborations().createMessage();
        int p = -1;
        for(int i = 0; i < fo.length; i++) {
            if (ms.getSender() == (MInstance)fo[i].getOwner()) {
                p = i;
                break;
            }
        }
        if (p >= 0) obj.setSender((MClassifierRole)fc[p].getOwner());
        p = -1;
        for(int i = 0; i < fo.length; i++) {
            if (ms.getReceiver() == (MInstance)fo[i].getOwner()) {
                p = i;
                break;
            }
        }
        if (p >= 0) obj.setReceiver((MClassifierRole)fc[p].getOwner());
        obj.setName(ms.getName());
        MAction action = ms.getDispatchAction();
        ms.setDispatchAction(null);
        Collection col = action.getStimuli();
        obj.setAction(action);
        return obj;
    }
    private static MAssociationRole cvLinkToAssociationRole(MLink ml, FigSeqObject[] fo, FigSeqStimulus[] fs, FigSeqClRole[] fc, FigSeqMessage[] fm) {
        MAssociationRole obj = UmlFactory.getFactory().getCollaborations().createAssociationRole();
        MAssociationEndRole con;
        obj.setName(ml.getName());
        obj.setBase(ml.getAssociation());
        Collection cm = ml.getStimuli();
        MStimulus  ms = null;
        MObject    mo = null;
        MMessage   mm = null;
        int i;
        int j;
        int k;
        if (cm.isEmpty()) {
            // a dummy message needs to be generated if there is no stimulus
            // TODO: needs more work
            mm = UmlFactory.getFactory().getCollaborations().createMessage();
            mm.setName(ml.getName());
            mo = (MObject)(ml.getAssociation().getConnections().toArray()[0]);
            j  = findObject(mo, fo);
            mo = (MObject)(ml.getAssociation().getConnections().toArray()[1]);
            k  = findObject(mo, fo);
            mm.setSender((MClassifierRole)fc[j].getOwner());
            mm.setReceiver((MClassifierRole)fc[k].getOwner());
            obj.addMessage(mm);
        } else {
            Iterator it = cm.iterator();
            while (it.hasNext()) {
                ms = (MStimulus)it.next();
                i  = findStimulus(ms, fs);
                mo = (MObject)ms.getSender();
                j  = findObject(mo, fo);
                mo = (MObject)ms.getReceiver();
                k  = findObject(mo, fo);
                if (i >= 0 && j >= 0 && k >= 0) {
                    mm = (MMessage)fm[i].getOwner();
                    mm.setName(ms.getName());
                    mm.setSender((MClassifierRole)fc[j].getOwner());
                    mm.setReceiver((MClassifierRole)fc[k].getOwner());
                    obj.addMessage(mm);
                }
    //            ms.setCommunicationLink(null);
                ms.setDispatchAction(null);
                ms.setSender(null);
                ms.setReceiver(null);
                ml.removeStimulus(ms);
            }
        }
        return obj;
    }

    private static int findObject(MObject mo, FigSeqObject[] fo) {
        for( int i = 0; i < fo.length; i++) {
            if (mo == fo[i].getOwner()) return i;
        }
        return -1;
    }
    private static int findStimulus(MStimulus ms, FigSeqStimulus[] fs) {
        for( int i = 0; i < fs.length; i++) {
            if (ms == fs[i].getOwner()) return i;
        }
        return -1;
    }

    private static void addToInteraction(MInteraction inter, MMessage msg) {
        inter.addMessage(msg);
    }

    private static void debugUserModels() {
        System.out.println("Count of user models: " + ProjectManager.getManager().getCurrentProject().getUserDefinedModels().size());
        MNamespace model = (MNamespace)ProjectManager.getManager().getCurrentProject().getUserDefinedModels().get(0);
        Collection c = model.getOwnedElements();
        Iterator ci = c.iterator();
        Object o = null;
        while (ci.hasNext()) {
            o = ci.next();
            System.out.println("Element in MModel: " + o);
            if (o instanceof MNamespace) {
                System.out.println("   '--MNamespace: " + ((MNamespace)o).getOwnedElements().size());
                Iterator cn = ((MNamespace)o).getOwnedElements().iterator();
                while (cn.hasNext()) {
                    o = cn.next();
                    System.out.println("         '--Element in MNamespace = " + o);
                }
            }
        }
    }

    private static void debugProjectMembers() {
        Vector v = ProjectManager.getManager().getCurrentProject().getMembers();
        System.out.println("Count of project members: " + v.size());
        MModel m = null;
        for(int i = 0; i < v.size(); i++) {
            System.out.println("Project member has type: " + v.get(i).getClass().getName());
            if (v.get(i) instanceof ProjectMemberModel) {
                m = ((ProjectMemberModel)v.get(i)).getModel();
                System.out.println("MModel.name = " + m.getName());
                System.out.println("MModel.namespace = " + m.getNamespace());
                System.out.println("MModel.Behaviors.count = " +((MModelImpl)m).getBehaviors().size());
                System.out.println("MModel.Bindings.count = " +((MModelImpl)m).getBindings().size());
                System.out.println("MModel.Children.count = " +((MModelImpl)m).getChildren().size());
                System.out.println("MModel.ClassifierRoles1.count = " +((MModelImpl)m).getClassifierRoles1().size());
                System.out.println("MModel.ClientDependencies.count = " +((MModelImpl)m).getClientDependencies().size());
                System.out.println("MModel.Collaborations1.count = " +((MModelImpl)m).getCollaborations1().size());
                System.out.println("MModel.Constraints.count = " +((MModelImpl)m).getConstraints().size());
                System.out.println("MModel.ElementImports.count = " +((MModelImpl)m).getElementImports().size());
                System.out.println("MModel.ElementImports2.count = " +((MModelImpl)m).getElementImports2().size());
                System.out.println("MModel.ElementResidences.count = " +((MModelImpl)m).getElementResidences().size());
                System.out.println("MModel.Extensions.count = " +((MModelImpl)m).getExtensions().size());
                System.out.println("MModel.Generalizations.count = " +((MModelImpl)m).getGeneralizations().size());
                System.out.println("MModel.ModelElementContents.count = " +((MModelImpl)m).getModelElementContents().size());
                System.out.println("MModel.OwnedElements.count = " +((MModelImpl)m).getOwnedElements().size());
                System.out.println("MModel.Parents.count = " +((MModelImpl)m).getParents().size());
                System.out.println("MModel.Partitions1.count = " +((MModelImpl)m).getPartitions1().size());
                System.out.println("MModel.Presentations.count = " +((MModelImpl)m).getPresentations().size());
                Collection col = ((MModelImpl)m).getBindings();
                Iterator it = col.iterator();
                Object o = null;
                while (it.hasNext()) {
                    o = it.next();
                    System.out.println("Element in Model: " + o.getClass().getName());
                }
            }
//            m = (MModel)v.get(i);
        }
    }
}
