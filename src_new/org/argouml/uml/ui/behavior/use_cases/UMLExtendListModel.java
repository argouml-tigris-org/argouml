// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

// File: UMLExtendListModel.java
// Classes: UMLExtendListModel
// Original Author: not known
// $$

// 26 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Tidied up, as part of
// getting the Extends stuff to work. add() method tidied up to put the new
// relationship at the correct place in the list. buildPopup removed, since the
// parent implementation is fine.

// 3 May 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to mark the
// project as needing saving if an extend relationship is added, deleted,
// changed or moved.


package org.argouml.uml.ui.behavior.use_cases;

import java.util.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.use_cases.*;

import org.argouml.ui.*;
import org.argouml.uml.ui.UMLBinaryRelationListModel;
import org.argouml.uml.ui.UMLUserInterfaceContainer;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.kernel.*;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.behavioralelements.usecases.UseCasesFactory;
import org.argouml.model.uml.behavioralelements.usecases.UseCasesHelper;
import org.argouml.model.uml.foundation.extensionmechanisms.ExtensionMechanismsHelper;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;


/**
 * <p>A list model for the extend relationship on use case and extension point
 *   property panels.</p>
 *
 * <p>Supports the full menu (Open, Add, Delete, Move Up, Move Down). Provides
 *   its own formatElement routine, to use the name of the base use case (where
 *   the container is a use case) or the extension use case (where the
 *   container is an extension point, rather than name of the extend
 *   relationship itself.</p>
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by ?,
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */

public class UMLExtendListModel extends UMLBinaryRelationListModel  {
    protected static Category cat = Category.getInstance(UMLExtendListModel.class);

    /**
     * <p>The default text when there is no base class for the extend
     *   relationship.</p>
     */
 
    final private static String _nullLabel = "(anon)";


    /**
     * <p>Create a new list model.<p>
     *
     * <p>Implementation is just an invocation of the parent constructor.</p>
     *
     * @param container  The container for this list - the use case property
     *                   panel.
     *
     * @param property   The name associated with the NSUML {@link
     *                   MElementEvent} that we are tracking or
     *                   <code>null</code> if we track them all. We 
     *                   probably want to just track the "extend" event.
     *
     * @param showNone   True if an empty list is represented by the keyword
     *                   "none"
     */

    public UMLExtendListModel(UMLUserInterfaceContainer container,
                              String property, boolean showNone) {

        super(container, property, showNone);
    }


    /**
     * <p>Compute the size of the list model. This method must be provided to
     *   override the abstract method in the parent.</p>
     *
     * @return the number of elements the list model (0 if there are none).
     */
    /*
      protected int recalcModelElementSize() {
      int        size   = 0;
      Collection xtends = getExtends();

      if(xtends != null) {
      size = xtends.size();
      }

      return size;
      }
    */
    
    /**
     * <p>Get the element at a given offset in the model This method must be
     *   provided to override the abstract method in the parent.</p>
     *
     * <p>The implementation makes use of the {@link #elementAtUtil} method,
     *   which takes care of all unusual cases.</p>
     *
     * @param   the index of the desired element.
     *
     * @return  the element at that index if there is one, otherwise
     *          <code>null</code>.
     */
    /*
      protected MModelElement getModelElementAt(int index) {

      return elementAtUtil(getExtends(), index, MExtend.class);
      }
    */            
        
    /**
     * <p>A private utility to get the list of extends relationships for this
     *   use case.</p>
     *
     * @return the list of extends relationships for this use case.
     */
    /*
      private Collection getExtends() {

      Collection xtends = null;
      Object     target = getTarget();

      if(target instanceof MUseCase) {
      MUseCase useCase = (MUseCase) target;

      xtends = useCase.getExtends();
      }
      else if (target instanceof MExtensionPoint) {
      MExtensionPoint extensionPoint = (MExtensionPoint) target;

      xtends = extensionPoint.getExtends();
      }

      return xtends;
      }
    */    

    /**
     * <p>Format a given model element.</p>
     *
     * <p>If this is invoked on a use case property panel and there is no base
     *   use case, use the default text ("(anon)"). Otherwise use the parent
     *   formatElement on the use case attached as base to the extend
     *   relationship , which will ultimately invoke the format element method
     *   of {@link PropPanel}.</p>
     *
     * <p>If this is invoked on an extension point property panel and there is
     *   * no extension use case, use the default text ("(anon)"). Otherwise
     *   use the parent formatElement on the use case attached as extension to
     *   the extend relationship , which will ultimately invoke the format
     *   element method of {@link PropPanel}.</p>
     *
     * <p>In this current implementation, more rigorously checks it is
     *   formatting an extend relationship.</p>
     *
     * @param element  the model element to format
     *
     * @return an object (typically a string) representing the element.
     */
    /*
      public Object formatElement(MModelElement element) {

      Object value = _nullLabel;

      if (element instanceof MExtend) {
      MExtend  extend = (MExtend) element;
      MUseCase target;

      // Which end to use depends on the nature of the container. For a
      // use case we

      if (getTarget() instanceof MUseCase) {
      target = extend.getBase();
      }
      else {
      target = extend.getExtension();
      }

      if(target != null) {
      value = super.formatElement(target);
      }
      }
      else {
      if (element != null) {
      cat.warn("UMLExtendListModel." +
      "formatElement(): Can't format " +
      element.getClass().toString());
      }
      }

      return value;
      }
    */

    /**
     * <p>Implement the "add" function of the pop up menu.</p>
     *
     * <p>Creates a dialog in which the user can select usecases this usecase should extend</p>
     *
     * @param index  Offset in the list of the element at which the pop-up was
     *               invoked.
     */
    /*
      public void add(int index) {
      Object target = getTarget();
      boolean extensionPoint = false;
      MExtensionPoint point = null;
      if (target instanceof MExtensionPoint) {
      point = (MExtensionPoint)target;
      target = point.getUseCase();
      extensionPoint = true;
      }
      if (target instanceof MUseCase) {
      MUseCase usecase = (MUseCase)target;	
      Vector choices = new Vector();
      Vector selected = new Vector();
      choices.addAll(UseCasesHelper.getHelper().getAllUseCases());
      choices.remove(usecase);
      selected.addAll(UseCasesHelper.getHelper().getExtendedUseCases(usecase));
      UMLAddDialog dialog = new UMLAddDialog(choices, selected, Argo.localize("UMLMenu", "dialog.title.add-extended-usecases"), true, true);
      int returnValue = dialog.showDialog(ProjectBrowser.TheInstance);
      if (returnValue == JOptionPane.OK_OPTION) {
      Iterator it = dialog.getSelected().iterator();
      while (it.hasNext()) {
      MUseCase eusecase = (MUseCase)it.next();
      if (!selected.contains(eusecase)) {
      ProjectBrowser pb = ProjectBrowser.TheInstance;
      ArgoDiagram diagram = pb.getActiveDiagram();
      Fig figclass = diagram.getLayer().presentationFor(usecase);
      Fig figeusecase = diagram.getLayer().presentationFor(eusecase);
      if (figclass != null && figeusecase != null) {
      GraphModel gm = diagram.getGraphModel();
      if (gm instanceof MutableGraphModel) {
      if (!extensionPoint)
      ((MutableGraphModel)gm).connect(usecase, eusecase, MExtend.class);
      else {
      ((MutableGraphModel)gm).connect(eusecase, usecase, MExtend.class);
      List list = new ArrayList();
      list.add(point);
      MExtend e = UseCasesHelper.getHelper().getExtends(usecase, eusecase);
      UmlFactory.getFactory().delete(e.getExtensionPoint(0));
      e.setExtensionPoints(list);
      }
      }
      } else {
      if (!extensionPoint)
      UseCasesFactory.getFactory().buildExtend(eusecase, usecase);
      else {
      UseCasesFactory.getFactory().buildExtend(usecase, eusecase);
      List list = new ArrayList();
      list.add(point);
      MExtend e = UseCasesHelper.getHelper().getExtends(usecase, eusecase);
      UmlFactory.getFactory().delete(e.getExtensionPoint(0));
      e.setExtensionPoints(list);
      }
                            
      }
      }
      }
      it = selected.iterator();
      while (it.hasNext()) {
      MUseCase eusecase = (MUseCase)it.next();
      if (!dialog.getSelected().contains(eusecase)) {
      MExtend extend = UseCasesHelper.getHelper().getExtends(eusecase, usecase);
      Object pt = ProjectBrowser.TheInstance.getTarget();
      ProjectBrowser.TheInstance.setTarget(extend);
      ActionEvent event = new ActionEvent(this, 1, "delete");
      ActionRemoveFromModel.SINGLETON.actionPerformed(event);
      ProjectBrowser.TheInstance.setTarget(pt);
      }
      }
      }
      }
      }

    */
    /**
     * <p>Implement the "delete" function of the pop up menu. Delete the
     *   element at the given index.</p>
     *
     * <p>Find the use cases at each end (note that NSUML uses the name
     *   "extend2" for the use case doing the extended, since it is unnamed in
     *   the standard). Delete their references to this extend
     *   relationship. Also remove from the namespace.</p>
     *
     * <p><em>Note</em>. We don't actually need to check the target PropPanel
     *   is a use case&mdash;given an extend relationship we can delete it.</p>
     *
     * @param index  Offset in the list of the element at which the pop-up was
     *               invoked and which is to be deleted.
     */
    /*
      public void delete(int index) {
      Object target = getTarget();
      if (target instanceof MExtensionPoint) {
      target = ((MExtensionPoint)target).getUseCase();
      }
      if (target instanceof MUseCase) {
      MUseCase usecase = (MUseCase)target;
      MUseCase eusecase = (MUseCase)UMLModelElementListModel.elementAtUtil(UseCasesHelper.getHelper().getExtendedUseCases(usecase), index, null);
      MExtend gen = UseCasesHelper.getHelper().getExtends(eusecase, usecase);
      Object pt = ProjectBrowser.TheInstance.getTarget();
      ProjectBrowser.TheInstance.setTarget(gen);
      ActionEvent event = new ActionEvent(this, 1, "delete");
      ActionRemoveFromModel.SINGLETON.actionPerformed(event);
      ProjectBrowser.TheInstance.setTarget(pt);
      fireIntervalRemoved(this,index,index);
      }
      }
    */

    /**
     * <p>Implement the action that occurs with the "MoveUp" pop up.</p>
     *
     * <p>Move the extend relationship at the given index in the list up one
     *   (unless it is already at the top). Since we use {@link #moveUpUtil}
     *   there is no need to test for unusual cases.</p>
     *
     * @param index  the index in the list of the extend relationship to move
     *               up.
     */
    /*
      public void moveUp(int index) {

      // Only do this if we are an extension point or use case

      Object target = getTarget();

      if ((!(target instanceof MExtensionPoint)) &&
      (!(target instanceof MUseCase))) {
      return;
      }

      // Handle according to the type of target

      if (target instanceof MExtensionPoint) {
      MExtensionPoint extensionPoint = (MExtensionPoint) target;
      extensionPoint.setExtends(moveUpUtil(extensionPoint.getExtends(),
      index));
      }
      else {
      MUseCase useCase = (MUseCase) target;
      useCase.setExtends(moveUpUtil(useCase.getExtends(), index));
      }

      // Having moved an extend relationship, mark as needing saving

      Project p = ProjectBrowser.TheInstance.getProject();
      p.setNeedsSave(true);

      // Tell Swing

      fireContentsChanged(this, index - 1, index);
      }
    
    */
    /**
     * <p>The action that occurs with the "MoveDown" pop up.</p>
     *
     * <p>Move the extend relationship at the given index in the list down one
     *   (unless it is already at the bottom). Since we use {@link #moveUpUtil}
     *   there is no need to test for unusual cases.</p>
     *
     * @param index  the index in the list of the extend relationship to move
     *               down.
     */
    /*
      public void moveDown(int index) {

      // Only do this if we are an extension point or use case

      Object target = getTarget();

      if ((!(target instanceof MExtensionPoint)) &&
      (!(target instanceof MUseCase))) {
      return;
      }

      // Handle according to the type of target

      if (target instanceof MExtensionPoint) {
      MExtensionPoint extensionPoint = (MExtensionPoint) target;
      extensionPoint.setExtends(moveDownUtil(extensionPoint.getExtends(),
      index));
      }
      else {
      MUseCase useCase = (MUseCase) target;
      useCase.setExtends(moveDownUtil(useCase.getExtends(), index));
      }

      // Having moved an extend relationship, mark as needing saving

      Project p = ProjectBrowser.TheInstance.getProject();
      p.setNeedsSave(true);

      // Tell Swing

      fireContentsChanged(this,index,index+1);
      }
    */

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel#buildPopup(JPopupMenu, int)
     */
    /*
      public boolean buildPopup(JPopupMenu popup, int index) {
      UMLUserInterfaceContainer container = getContainer();
      UMLListMenuItem open = new UMLListMenuItem(container.localize("Open"),this,"open",index);
      UMLListMenuItem delete = new UMLListMenuItem(container.localize("Delete"),this,"delete",index);
      if(getModelElementSize() <= 0) {
      open.setEnabled(false);
      delete.setEnabled(false);
      }

      popup.add(open);
      UMLListMenuItem add =new UMLListMenuItem(container.localize("Add"),this,"add",index);
      if(_upper >= 0 && getModelElementSize() >= _upper) {
      add.setEnabled(false);
      }
      popup.add(add);
      popup.add(delete);

      return true;
      }
    */
    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#build(MModelElement, MModelElement)
     */
    protected void build(MModelElement from, MModelElement to) {
        if (from instanceof MUseCase && to instanceof MUseCase) {
            UseCasesFactory.getFactory().buildExtend((MUseCase) to, (MUseCase) from);
        } else
            throw new IllegalArgumentException("In build of UMLExtendListModel: either the arguments are null or not instanceof MUseCase");
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#connect(MutableGraphModel, MModelElement, MModelElement)
     */
    protected void connect(
			   MutableGraphModel gm,
			   MModelElement from,
			   MModelElement to) {
	gm.connect(from, to, MExtend.class);
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getAddDialogTitle()
     */
    protected String getAddDialogTitle() {
        return Argo.localize("UMLMenu", "dialog.title.add-extended-usecases");
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getChoices()
     */
    protected Collection getChoices() {
        return ModelManagementHelper.getHelper().getAllModelElementsOfKind(getTarget().getClass());
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getRelation(MModelElement, MModelElement)
     */
    protected MModelElement getRelation(MModelElement from, MModelElement to) {
        return UseCasesHelper.getHelper().getExtends((MUseCase) to, (MUseCase) from);
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getSelected()
     */
    protected Collection getSelected() {
        if (getTarget() instanceof MUseCase) {
            return UseCasesHelper.getHelper().getExtendedUseCases((MUseCase) getTarget());
        } else
            throw new IllegalStateException("In getSelected of UMLExtendListModel: target is not an instanceof MUseCase");
    }

} /* End of class UMLExtendListModel */
