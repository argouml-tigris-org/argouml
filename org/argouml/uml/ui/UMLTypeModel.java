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

package org.argouml.uml.ui;

import java.util.Collection;
import java.util.Iterator;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

/**
 * Broken out into its own file due to a need to reference it both in
 * PropPanelAttribute and PropPanelParameter.
 *
 * d00mst / 2002-09-10
 */
public class UMLTypeModel extends UMLComboBoxModel {
	private Class _elementClass;

	/**
	 * Constructor for UMLTypeModel.
	 * @param container
	 * @param filter
	 * @param property
	 * @param getMethod
	 * @param setMethod
	 * @param allowVoid
	 * @param elementType
	 * @param elementClass
	 * @param addElementsFromProfileModel
	 */
	public UMLTypeModel(
		UMLUserInterfaceContainer container,
		String filter,
		String property,
		String getMethod,
		String setMethod,
		boolean allowVoid,
		Class elementType,
		Class elementClass,
		boolean addElementsFromProfileModel) {
		super(
			container,
			filter,
			property,
			getMethod,
			setMethod,
			allowVoid,
			elementType,
			addElementsFromProfileModel);
		_elementClass = elementClass;
	}

	/**
	 * @see ru.novosoft.uml.MElementListener#propertySet(MElementEvent)
	 */
	public void propertySet(MElementEvent event) {
		// only react to type
		if (event.getName() == null || !"type".equals(event.getName())) {
			return;
		}
		
		// only accept attribute as source
		if (event.getSource() == null ||
		    !_elementClass.isAssignableFrom(
				event.getSource().getClass())) {
			return;
		}
		
		// is it acceptible?
		if (!isAcceptible((MModelElement)event.getNewValue())) {
			return;
		}
		
		if (!event.getNewValue().equals(event.getOldValue())) {
			setSelectedItem(new UMLComboBoxEntry((MModelElement)event.getNewValue(), _container.getProfile(), false));
		}
		
	}

	/**
	 * @see org.argouml.uml.ui.UMLComboBoxModel#isAcceptible(MModelElement)
	 */
	public boolean isAcceptible(MModelElement element) {
		if (_filter != null) return super.isAcceptible(element);
		return element instanceof MClassifier;
	}

	/**
	 * @see ru.novosoft.uml.MElementListener#roleAdded(MElementEvent)
	 */
	public void roleAdded(MElementEvent e) {
		if (e.getName().equals("ownedElement") && !e.getOldValue().equals(e.getNewValue())) {
			Iterator it = ((Collection)e.getNewValue()).iterator();
			while (it.hasNext()) {
				MModelElement elem = (MModelElement)it.next();
				if (!((Collection)e.getOldValue()).contains(elem)) {
					updateElement(elem);
					break;
				}
			}
		}	
		super.roleAdded(e);
	}

}

