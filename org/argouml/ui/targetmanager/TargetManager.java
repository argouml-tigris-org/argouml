// $Id$
// Copyright (c) 2002-2003 The Regents of the University of California. All
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
package org.argouml.ui.targetmanager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.ui.Actions;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;

/**
 * <p>
 * The manager of the target of argouml. The target of argouml is the selected
 * element in the model. This can either be an instance of a meta-class (an
 * Interface or a Class for example) but it can also be a diagram or a figure 
 * on a diagram.
 * </p>
 * <p>
 * Via an event mechanism this manager makes sure that all objects interested 
 * in knowing wether the event changed are acknowledged.
 * </p>
 * <p>
 * Note in particular that null is an invalid target.
 * </p>
 * 
 * @author jaap.branderhorst@xs4all.nl
 */
public final class TargetManager {

    /**
     * The manager of the history of targets. Everytimes the user (or
     * the program) selects a new target, this is recorded in the
     * history. Via navigateBack and navigateForward, the user can
     * browse through the history just like in an ordinary internet
     * browser.
     * @author jaap.branderhorst@xs4all.nl
     */
    private class HistoryManager implements TargetListener {

        private static final int MAX_SIZE = 100;

        /**
         * The history with targets
         */
        private List _history = new ArrayList();

        /**
         * Flag to indicate if the current settarget was instantiated by a
         * navigateBack action.
         */
        private boolean _navigateBackward;

        /**
         * The pointer to the current target in the history
         */
        private int _currentTarget = -1;

        /**
         * Default constructor that registrates the history manager as target 
         * listener with the target manager.
         *
         */
        private HistoryManager() {
            addTargetListener(this);
        }

        /**
         * Puts some target into the history (if needed). Updates both
         * the history as the pointer to indicate the target.
         * @param target The target to put into the history
         */
        private void putInHistory(Object target) {
            if (_currentTarget > -1) {
                // only targets we didn't have allready count
                Object modelTarget =
                    target instanceof Fig ? ((Fig) target).getOwner() : target;
                Object oldTarget =
                    ((WeakReference) _history.get(_currentTarget)).get();
                oldTarget =
                    oldTarget instanceof Fig
		    ? ((Fig) oldTarget).getOwner()
		    : oldTarget;
                if (oldTarget == modelTarget)
                    return;
            }
            if (target != null && !_navigateBackward) {
                if (_currentTarget + 1 == _history.size()) {
                    _history.add(new WeakReference(target));
                    _currentTarget++;
                    resize();
                } else {
                    WeakReference ref =
                        _currentTarget > -1
			? (WeakReference) _history.get(_currentTarget)
			: null;
                    if (_currentTarget == -1 || !ref.get().equals(target)) {
                        int size = _history.size();
                        for (int i = _currentTarget + 1; i < size; i++) {
                            _history.remove(_currentTarget + 1);
                        }
                        _history.add(new WeakReference(target));
                        _currentTarget++;
                    }
                }

            }
        }

        /**
         * Resizes the history if it's grown too big.
         *
         */
        private void resize() {
            int size = _history.size();
            if (size > MAX_SIZE) {
                int oversize = size - MAX_SIZE;
                int halfsize = size / 2;
                if (_currentTarget > halfsize && oversize < halfsize) {
                    for (int i = 0; i < oversize; i++) {
                        _history.remove(0);
                    }
                    _currentTarget -= oversize;
                }
            }
        }

        /**
         * Navigate one target forward in history. Throws an
         * illegalstateException if not possible.
         *
         */
        private void navigateForward() {
            if (_currentTarget >= _history.size() - 1)
                throw new IllegalStateException(
			"NavigateForward is not allowed "
			+ "since the targetpointer is pointing at "
			+ "the upper boundary "
			+ "of the history");
            setTarget(((WeakReference) _history.get(++_currentTarget)).get());
        }

        /**
         * Navigate one step back in history. Throws an illegalstateexception if
         * not possible.
         *
         */
        private void navigateBackward() {
            if (_currentTarget == 0) {
                throw new IllegalStateException(
		        "NavigateBackward is not allowed "
			+ "since the targetpointer is pointing at "
			+ "the lower boundary "
			+ "of the history");
            }
            _navigateBackward = true;
            setTarget(((WeakReference) _history.get(--_currentTarget)).get());
            _navigateBackward = false;
        }

        /**
         * Checks if it's possible to navigate back.
         * @return true if it's possible to navigate back.
         */
        private boolean navigateBackPossible() {
            return _currentTarget > 0;
        }

        /**
         * Checks if it's possible to navigate forward         
         * @return true if it's possible to navigate forward
         */
        private boolean navigateForwardPossible() {
            return _currentTarget < _history.size() - 1;
        }

        /**
         * @see
         * org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
         */
        public void targetAdded(TargetEvent e) {
        }

        /**
         * @see
         * org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
         */
        public void targetRemoved(TargetEvent e) {
            // comparable to targetReasserted in this respect.
            // putInHistory(e.getNewTarget());

        }

        /**
         * @see
         * org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
         */
        public void targetSet(TargetEvent e) {
	    putInHistory(e.getNewTarget());
        }

        /**
         * Cleans the history in total.
         *
         */
        private void clean() {
            _history = new ArrayList();
            _currentTarget = -1;
        }

        private void removeHistoryTarget(Object o) {
            if (ModelFacade.isADiagram(o)) {
                ListIterator it = ((Diagram) o).getEdges().listIterator();
                while (it.hasNext()) {
                    removeHistoryTarget(it.next());
                }
                it = ((Diagram) o).getNodes().listIterator();
                while (it.hasNext()) {
                    removeHistoryTarget(it.next());
                }
            }
            ListIterator it = _history.listIterator();
            int oldCurrentTarget = _currentTarget;
            while (it.hasNext()) {
                WeakReference ref = (WeakReference) it.next();
                Object historyObject = ref.get();
                if (ModelFacade.isAModelElement(o)) {
                    historyObject =
                        historyObject instanceof Fig
			? ((Fig) historyObject).getOwner()
			: historyObject;

                }
                if (o == historyObject) {
                    if (_history.indexOf(ref) <= _currentTarget) {
                        _currentTarget--;
                    }
                    it.remove();
                }

                // cannot break here since an object can be multiple
                // times in history
            }
            if (oldCurrentTarget != _currentTarget) {
            	/* TODO updateAllEnabled() has been deprecated, and the replacement
            	 * is to use updateAllEnabled(TargetEvent e), but what TargetEvent
            	 * would be passed?
            	 */
                Actions.updateAllEnabled();
            }
        }

    }
    /**
     * The log4j logger to log messages to
     */
    private Logger _log = Logger.getLogger(this.getClass());

    /**
     * The singleton instance
     */
    private static TargetManager instance = new TargetManager();

    /**
     * The targets
     */
    private List _targets = new ArrayList();

    /** 
     * Cache for the modeltarget. See getModelTarget
     */
    private Object _modelTarget = null;

    /**
     * Cache for the figTarget. See getFigTarget
     */
    private Fig _figTarget = null;

    /**
     * The list with targetlisteners
     */
    private EventListenerList _listenerList = new EventListenerList();

    /**
     * The history manager of argouml. Via the historymanager browser behaviour
     * is emulated
     */
    private HistoryManager _historyManager = new HistoryManager();

    /**
     * Flag to indicate that there is a setTarget method running
     */
    private boolean _inTransaction = false;

    /**
     * Singleton retrieval method
     * @return The targetmanager
     */
    public static TargetManager getInstance() {
        return instance;
    }

    /**
     * Sets the targets to the single given object. If there are targets at the 
     * moment of calling this method, these will be removed as targets. To 
     * all interested targetlisteners, a TargetEvent will be fired. If the 
     * new target o equals the current target, no events will be fired, nor will
     * the target be (re)set.
     * @param o The new target, null clears all targets.
     */
    public synchronized void setTarget(Object o) {
	RuntimeException exception;

	if (isInTargetTransaction())
	    return;

	if ((_targets.size() == 0 && o == null) ||
	    (_targets.size() == 1 && _targets.get(0).equals(o)))
	    return;

	startTargetTransaction();

	Object oldTargets[] = _targets.toArray();
	_targets.clear();
	if (o != null)
	    _targets.add(o);
	exception = internalOnSetTarget(TargetEvent.TARGET_SET, oldTargets);

        endTargetTransaction();
	if (exception != null)
	    throw new TargetException("Exception dispatching events", exception);
    }

    private RuntimeException internalOnSetTarget(String eventName, Object oldTargets[]) {
	TargetEvent event = new TargetEvent(this, eventName, oldTargets, _targets.toArray());

	if (_targets.size() > 0) {
	    _figTarget = determineFigTarget(_targets.get(0));
	    _modelTarget = determineModelTarget(_targets.get(0));
	} else {
	    _figTarget = null;
	    _modelTarget = null;
	}

	if (TargetEvent.TARGET_SET.equals(eventName))
	    return fireTargetSet(event);
	else if (TargetEvent.TARGET_ADDED.equals(eventName))
	    return fireTargetAdded(event);
	else if (TargetEvent.TARGET_REMOVED.equals(eventName))
	    return fireTargetRemoved(event);

	_log.error("Unknown eventName: " + eventName);
	return null;
    }

    /**
     * Returns the current primary target, the first selected object.
     *
     * The value will be that of the new primary target during a targetSet/
     * targetAdded/targetRemoved notification, since they are just that,
     * notifications that the target(s) has just changed.
     *
     * @return The current target, or null if no target is selected
     * @throws TargetException isn't thrown, obsolete declaration.
     */
    public synchronized Object getTarget() throws TargetException {
	return _targets.size() > 0 ? _targets.get(0) : null;
    }

    /**
     * Sets the given collection to the current targets. If the collection 
     * equals the current targets, then does nothing. When setting
     * the targets, a TargetEvent will be fired to each interested listener.
     * Note that the first element returned by an Iterator on targetList
     * will be taken to be the primary target (see getTarget()), and that
     * an event will be fired also in case that that element would not equal
     * the element returned by getTarget().
     * Note also that any nulls within the Collection will be ignored.
     * @param targetsList The new targets list.
     */
    public synchronized void setTargets(Collection targetsList) {
	RuntimeException exception;
	Iterator ntarg;

	if (isInTargetTransaction())
	    return;

	if (targetsList == null)
	    targetsList = Collections.EMPTY_LIST;

	Object oldTargets[] = null;

	if (targetsList.size() == _targets.size()) {
	    boolean first = true;
	    ntarg = targetsList.iterator();

	    while (ntarg.hasNext()) {
		Object targ = ntarg.next();
		if (targ == null)
		    continue;
		if (!_targets.contains(targ)
		    || (first && targ != getTarget())) {
		    oldTargets = _targets.toArray();
		    break;
		} else
		    first = false;
	    }
	} else
	    oldTargets = _targets.toArray();

	if (oldTargets == null)
	    return;

	startTargetTransaction();

	_targets.clear();
	ntarg = targetsList.iterator();
	while (ntarg.hasNext()) {
	    Object targ = ntarg.next();
	    if (targ == null || _targets.contains(targ))
		continue;
	    _targets.add(targ);
	}

	exception = internalOnSetTarget(TargetEvent.TARGET_SET, oldTargets);

	endTargetTransaction();
	if (exception != null)
	    throw new TargetException("Exception dispatching events", exception);
    }

    /**
     * Adds a target to the targets list. If the target is already in
     * the targets list then does nothing. Otherwise the
     * target will be added and an appropriate TargetEvent will be
     * fired to all interested listeners. Since null can never be a target,
     * adding null will never do anything.
     * @param target the target to be added.
     */
    public synchronized void addTarget(Object target) {
	RuntimeException exception;

	if (isInTargetTransaction())
	    return;

	if (target == null || _targets.contains(target))
	    return;

	startTargetTransaction();

	Object[] oldTargets = _targets.toArray();
	_targets.add(target);
	exception = internalOnSetTarget(TargetEvent.TARGET_ADDED, oldTargets);

	endTargetTransaction();
	if (exception != null)
	    throw new TargetException("Exception dispatching events", exception);
    }

    /**
     * Removes the target from the targets list. Does nothing if the target
     * does not exist in the targets list. Fires an appropriate TargetEvent to 
     * all interested listeners. Since null can never be a target, removing
     * null will never do anything.
     * @param target The target to remove.
     */
    public synchronized void removeTarget(Object target) {
	RuntimeException exception;

        if (isInTargetTransaction())
	    return;

	if (target == null || !_targets.contains(target))
	    return;

	startTargetTransaction();

	Object[] oldTargets = _targets.toArray();
	_targets.remove(target);
	exception = internalOnSetTarget(TargetEvent.TARGET_REMOVED, oldTargets);

	endTargetTransaction();
	if (exception != null)
	    throw new TargetException("Exception dispatching events", exception);
    }

    /**
     * Returns a collection with all targets. Returns an empty collection
     * if there are no targets. If there are several targets then the first
     * Object by an Iterator on the returned Collection or the zero'th Object
     * in an array on this Collection is guaranteed to be the object returned
     * by getTarget.
     *
     * The value will be that of the new target(s) during a targetSet/
     * targetAdded/targetRemoved notification, since they are just that,
     * notifications that the target(s) has just changed.
     *
     * @return A collection with all targets.
     */
    public synchronized Collection getTargets() {
        return new ArrayList(_targets);
    }

    /**
     * Adds a listener.
     * @param listener the listener to add
     */
    public void addTargetListener(TargetListener listener) {
        _listenerList.add(TargetListener.class, listener);
    }

    /**
     * Removes a listener.
     * @param listener the listener to remove
     */
    public void removeTargetListener(TargetListener listener) {
        _listenerList.remove(TargetListener.class, listener);
    }

    private RuntimeException fireTargetSet(TargetEvent targetEvent) {
	RuntimeException exception = null;
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
	    try {
		if (listeners[i] == TargetListener.class) {
		    // Lazily create the event:                     
		    ((TargetListener) listeners[i + 1]).targetSet(targetEvent);
		}
	    } catch (RuntimeException e) {
		exception = e;
	    }
        }
	return exception;
    }

    private RuntimeException fireTargetAdded(TargetEvent targetEvent) {
	RuntimeException exception = null;
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
	    try {
		if (listeners[i] == TargetListener.class) {
		    // Lazily create the event:                     
		    ((TargetListener) listeners[i + 1]).targetAdded(targetEvent);
		}
	    } catch (RuntimeException e) {
		exception = e;
	    }
        }
	return exception;
    }

    private RuntimeException fireTargetRemoved(TargetEvent targetEvent) {
	RuntimeException exception = null;
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
	    try {
		if (listeners[i] == TargetListener.class) {
		    // Lazily create the event:                     
		    ((TargetListener) listeners[i + 1]).targetRemoved(targetEvent);
		}
	    } catch (RuntimeException e) {
		exception = e;
	    }
        }
	return exception;
    }

    private void startTargetTransaction() {
        _inTransaction = true;
    }

    private boolean isInTargetTransaction() {
        return _inTransaction;
    }

    private void endTargetTransaction() {
        _inTransaction = false;
    }

    /**
     * Convenience method to return the target as fig. If the current
     * target (retrieved by getTarget) is either a fig itself or the
     * owner of a fig this fig will be returned. Otherwise null will
     * be returned.
     * @return the target in it's 'fig-form'
     */
    public Fig getFigTarget() {
        return _figTarget;
    }

    /**
     * Calculates the most probable 'fig-form' of some target.
     * @param target the target to calculate the 'fig-form' for.
     * @return The fig-form.
     */
    private Fig determineFigTarget(Object target) {
        if (!(target instanceof Fig)) {

            Project p = ProjectManager.getManager().getCurrentProject();
            Collection col = p.findFigsForMember(target);
            if (col == null || col.isEmpty()) {
                target = null;
            } else {
                target = col.iterator().next();
            }
        }

        return target instanceof Fig ? (Fig) target : null;
    }

    /**
     * Returns the target in it's 'modelform'. If the target retrieved
     * by getTarget is an UMLDiagram or a modelelement the target will
     * be returned. If the target is a fig but owned by a modelelement
     * that modelelement will be returned.  Otherwise null will be
     * returned.
     * @return the target in it's 'modelform'.
     */
    public Object getModelTarget() {
        return _modelTarget;

    }

    /**
     * Calculates the modeltarget.
     * @param target The target to calculate the modeltarget for
     * @return The modeltarget
     */
    private Object determineModelTarget(Object target) {
        if (target instanceof Fig) {
            Object owner = ((Fig) target).getOwner();
            if (ModelFacade.isABase(owner)) {
                target = owner;
            }
        }
        return target instanceof UMLDiagram
            || ModelFacade.isABase(target) ? target : null;

    }

    /**
     * Navigates the target pointer one target forward. This implements together
     * with navigateBackward browser like functionality.
     * @throws IllegalStateException If the target pointer is at the end of the 
     * history.
     */
    public void navigateForward() throws IllegalStateException {
        _historyManager.navigateForward();
    }

    /**
     * Navigates the target pointer one target backward. This
     * implements together with navigateForward browser like
     * functionality
     * @throws IllegalStateException If the target pointer is at the
     * beginning of the history.
     */
    public void navigateBackward() throws IllegalStateException {
        _historyManager.navigateBackward();
    }

    /**
     * Checks if it's possible to navigate forward.
     * @return true if it is possible to navigate forward.
     */
    public boolean navigateForwardPossible() {
        return _historyManager.navigateForwardPossible();
    }

    /**
     * Checks if it's possible to navigate backward
     * @return true if it's possible to navigate backward
     */
    public boolean navigateBackPossible() {
        return _historyManager.navigateBackPossible();
    }

    /**
     * Cleans the history. Needed for the JUnit tests and when instantiating a 
     * new project
     *
     */
    public void cleanHistory() {
        _historyManager.clean();
    }

    public void removeHistoryElement(Object o) {
        _historyManager.removeHistoryTarget(o);
    }

}