// $Id$
// Copyright (c) 2002 The Regents of the University of California. All
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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.uml.diagram.ui.UMLDiagram;
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
 * 
 * @author jaap.branderhorst@xs4all.nl
 */
public final class TargetManager {

    /**
     * The manager of the history of targets. Everytimes the user (or the program)
     * selects a new target, this is recorded in the history. Via navigateBack and
     * navigateForward, the user can browse through the history just like in an 
     * ordinary internet browser.
     * @author jaap.branderhorst@xs4all.nl
     */
    private class HistoryManager implements TargetListener {
        
        private final static int MAX_SIZE = 100;
        
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
         * Puts some target into the history (if needed). Updates both the history
         * as the pointer to indicate the target.
         * @param target The target to put into the history
         */
        private void putInHistory(Object target) {
            // only targets we didn't have allready count
            target = target instanceof Fig ? ((Fig)target).getOwner() : target;            
            if (_currentTarget > -1) {
                Object oldTarget = ((WeakReference) _history.get(_currentTarget)).get();   
                if (oldTarget == target) return;             
            }
            if (target != null && !_navigateBackward) {                
                if (_currentTarget+1 == _history.size()) {                
                    _history.add(new WeakReference(target));
                    _currentTarget++;
                    resize();
                }
                else {                
                    WeakReference ref =
                        (WeakReference) _history.get(_currentTarget);
                    if (!ref.get().equals(target)) {
                        int size = _history.size();
                        for (int i = _currentTarget+1;
                            i < size;
                            i++) {
                            _history.remove(_currentTarget+1);
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
                int halfsize = size/2;
                if (_currentTarget > halfsize && oversize < halfsize) {
                    for (int i = 0; i < oversize; i++) {
                        _history.remove(0);                        
                    }
                    _currentTarget -= oversize;
                }
            }
        }
        
        /**
         * Navigate one target forward in history. Throws an illegalstateException
         * if not possible.
         *
         */
        private void navigateForward() {
            if (_currentTarget >= _history.size()-1) 
                throw new IllegalStateException("NavigateForward is not allowed " +
                    "since the targetpointer is pointing at the upper boundary " +
                    "of the history");
            setTarget(((WeakReference)_history.get(++_currentTarget)).get());
        }
        
        /**
         * Navigate one step back in history. Throws an illegalstateexception if
         * not possible.
         *
         */
        private void navigateBackward() {
            if (_currentTarget == 0) {
                throw new IllegalStateException("NavigateBackward is not allowed " +
                   "since the targetpointer is pointing at the lower boundary " +
                   "of the history");
            }
            _navigateBackward = true;
            setTarget(((WeakReference)_history.get(--_currentTarget)).get());
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
            return _currentTarget < _history.size()-1;
        }

        /**
         * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
         */
        public void targetAdded(TargetEvent e) {
        }

        /**
         * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
         */
        public void targetRemoved(TargetEvent e) {
            // comparable to targetReasserted in this respect.
            // putInHistory(e.getNewTargets()[0]);

        }

        /**
         * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
         */
        public void targetSet(TargetEvent e) {
            putInHistory(e.getNewTargets()[0]);
        }
        
        /**
         * Cleans the history in total.
         *
         */
        private void clean() {
            _history = new ArrayList();
            _currentTarget = -1;
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
     * The targets stored in an object array to improve performance
     */
    private Object[] _targets = new Object[0];

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
     * While firing events, the list with targets is not updated yet. Therefore
     * getTarget() will return the old target. This can get nasty for classes that
     * do not use the event mechanism yet. The newTarget is a variable that is 
     * temporarily filled with the new target. When the targets are set, the new 
     * target is nullified.
     */
    private Object _newTarget;

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
     * @param o The new target
     */
    public synchronized void setTarget(Object o) {
        if (!isInTargetTransaction()) {
            startTargetTransaction();
            Object[] targets = new Object[] { o };
            if (!targets.equals(_targets)) {
                _newTarget = o;
                _figTarget = determineFigTarget(_newTarget);
                _modelTarget = determineModelTarget(_newTarget);
                fireTargetSet(targets);
                _targets = new Object[] { o };
                _newTarget = null;         
            }
            endTargetTransaction();
        }
    }

    /**
     * Returns the current target. 
     * @return The current target
     * @throws TargetException if there are more then 1 target.
     */
    public synchronized Object getTarget() throws TargetException {
        if (_targets.length == 0) {
            _log.warn("Returning null as target. No target was selected.");
        }
        return _newTarget != null
            ? _newTarget
            : (_targets.length >= 1 ? _targets[0] : null);
    }

    /**
     * Sets the given collection to the current targets. If the collection 
     * equals the current targets, the targets will not be (re)set. When setting
     * the targets, a TargetEvent will be fired to each interested listener.
     * @param targetsList The new targets list.
     */
    public synchronized void setTargets(Collection targetsList) {
        if (!isInTargetTransaction()) {
            startTargetTransaction();
            if (targetsList != null && !targetsList.isEmpty()) {
                Object[] targets = targetsList.toArray();
                if (!targets.equals(_targets)) {
                    _newTarget = targets[0];
                    _figTarget = determineFigTarget(_newTarget);
                    _modelTarget = determineModelTarget(_newTarget);
                    fireTargetSet(targets);
                    _targets = targets;
                    _newTarget = null;
                }
            } else {
                Object[] targets = new Object[] { null };
                fireTargetSet(targets);
                _targets = targets;
                _modelTarget = null;
                _figTarget = null;
            }
            endTargetTransaction();
        }
    }

    /**
     * Adds a target to the targets list. If the target is allready in the targets
     * list no (re)setting will take place. Otherwise the target will be added
     * and an appropriate TargetEvent will be fired to all interested listeners.
     * @param target the target to be added.
     */
    public synchronized void addTarget(Object target) {
        if (target != null && !isInTargetTransaction()) {
            startTargetTransaction();
            Object[] targets = new Object[_targets.length + 1];
            System.arraycopy(_targets, 0, targets, 0, _targets.length);
            targets[_targets.length] = target;
            fireTargetAdded(target);
            _targets = targets;
            endTargetTransaction();
        }
    }

    /**
     * Removes the target from the targets list. Does do nothing if the target
     * does not exist in the targets list. Fires an appropriate TargetEvent to 
     * all interested listeners.
     * @param target The target to remove.
     */
    public synchronized void removeTarget(Object target) {
        if (target != null && !isInTargetTransaction()) {
            startTargetTransaction();
            boolean found = false;
            for (int i = 0; i < _targets.length; i++) {
                if (_targets[i] == target) {
                    Object[] targets = new Object[_targets.length - 1];
                    // Copy the list up to index
                    System.arraycopy(_targets, 0, targets, 0, i);
                    // Copy from two past the index, up to
                    // the end of tmp (which is two elements
                    // shorter than the old list)
                    if (i < targets.length)
                        System.arraycopy(
                            _targets,
                            i + 1,
                            targets,
                            i,
                            targets.length - i);
                    // set the listener array to the new array or null
                    fireTargetRemoved(target);
                    _targets = (targets.length == 0) ? new Object[0] : targets;

                }
            }
            endTargetTransaction();
        }
    }

    /**
     * Returns a collection with all targets. Returns null if there are no
     * targets.
     * @return A collection with all targets.
     */
    public synchronized Collection getTargets() {
        return _targets.length > 0 ? Arrays.asList(_targets) : null;
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

    private void fireTargetSet(Object[] newTargets) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        TargetEvent targetEvent =
            new TargetEvent(this, TargetEvent.TARGET_SET, _targets, newTargets);
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TargetListener.class) {
                // Lazily create the event:                     
                 ((TargetListener) listeners[i + 1]).targetSet(targetEvent);
            }
        }
    }

    private void fireTargetAdded(Object targetAdded) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        TargetEvent targetEvent =
            new TargetEvent(
                this,
                TargetEvent.TARGET_SET,
                _targets,
                new Object[] { targetAdded });

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TargetListener.class) {
                // Lazily create the event:                     
                 ((TargetListener) listeners[i + 1]).targetAdded(targetEvent);
            }
        }
    }

    private void fireTargetRemoved(Object targetRemoved) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        TargetEvent targetEvent =
            new TargetEvent(
                this,
                TargetEvent.TARGET_SET,
                _targets,
                new Object[] { targetRemoved });

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TargetListener.class) {
                // Lazily create the event:                     
                ((TargetListener) listeners[i + 1]).targetRemoved(targetEvent);
            }
        }
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
     * Convenience method to return the target as fig. If the current target 
     * (retrieved by getTarget) is either a fig itself or the owner of a fig this
     * fig will be returned. Otherwise null will be returned.
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
     * Returns the target in it's 'modelform'. If the target retrieved by getTarget
     * is an UMLDiagram or a modelelement the target will be returned. If the target
     * is a fig but owned by a modelelement that modelelement will be returned. 
     * Otherwise null will be returned.
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
     * Navigates the target pointer one target backward. This implements together
     * with navigateForward browser like functionality
     * @throws IllegalStateException If the target pointer is at the beginning of the 
     * history.
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
     * Cleans the history. Needed for the JUnit tests.
     *
     */
    void cleanHistory() {
        _historyManager.clean();
    }
    
    

}
