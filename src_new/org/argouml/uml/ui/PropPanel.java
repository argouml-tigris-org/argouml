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
import org.argouml.ui.*;
import org.argouml.uml.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

import org.tigris.gef.util.*;

/**
 *   This abstract class provides the basic layout and event dispatching
 *   support for all Property Panels.  The property panel is layed out
 *   as a number (specified in the constructor) of equally sized panels 
 *   that split the available space.  Each panel has a column of 
 *   "captions" and matching column of "fields" which are laid out
 *   indepently from the other panels.
 */
abstract public class PropPanel extends TabSpawnable
implements TabModelTarget, MElementListener, UMLUserInterfaceContainer {
  ////////////////////////////////////////////////////////////////
  // instance vars
  private Object _target;
  private MModelElement _modelElement;
  private Profile _profile;
  private LinkedList _navListeners = new LinkedList();

  private Vector _panels = new Vector();
  private UMLNameEventListener _nameListener;

  protected static ImageIcon _navBackIcon = Util.loadIconResource("NavigateBack");
  protected static ImageIcon _navForwardIcon = Util.loadIconResource("NavigateForward");
  protected static ImageIcon _deleteIcon = Util.loadIconResource("RedDelete");
  protected static ImageIcon _navUpIcon = Util.loadIconResource("NavigateUp");



  ////////////////////////////////////////////////////////////////
  // constructors

    /**
     *    Constructs the PropPanel.
     *    @param title Title of panel
     *    @param panelCount number of horizontal panels
     */
    public PropPanel(String title,int panelCount) {
        super(title);
        setLayout(new BorderLayout());

        JPanel center = new JPanel(new GridLayout(1,0));
        JPanel panel;
        for(long i = 0; i < panelCount; i++) {
            panel = new JPanel(new GridBagLayout());
            _panels.add(panel);
            center.add(panel);
        }
        add(center,BorderLayout.CENTER);
    }
    
    /**
     *   Adds a component to the captions of the specified panel.
     *   @param component Component to be added (typically a JLabel)
     *   @param row row index, zero-based.
     *   @param panel panel index, zero-based.
     *   @param weighty specifies how to distribute extra vertical space,
     *      see GridBagConstraint for details on usage.
     */
    public void addCaption(Component component,int row,int panel,double weighty)
    {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = row;
        gbc.weighty = weighty;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        
        JPanel pane = (JPanel) _panels.elementAt(panel);
        GridBagLayout layout = (GridBagLayout) pane.getLayout();
        layout.setConstraints(component,gbc);
        pane.add(component);
    }    
    
    /**
     *   Adds a component to the fields of the specified panel.
     *   @param component Component to be added
     *   @param row row index, zero-based.
     *   @param panel panel index, zero-based.
     *   @param weighty specifies how to distribute extra vertical space,
     *      see GridBagConstraint for details on usage.
     */
   public void addField(Component component,int row,int panel,double weighty)
    {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = row;
        gbc.weighty = weighty;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.weightx = 1;
        if(weighty == 0)
            gbc.fill = GridBagConstraints.HORIZONTAL;
        else
            gbc.fill = GridBagConstraints.BOTH;
        
        JPanel pane = (JPanel) _panels.elementAt(panel);
        GridBagLayout layout = (GridBagLayout) pane.getLayout();
        layout.setConstraints(component,gbc);
        pane.add(component);
    }    
     
    /**
     *   Adds a component to the fields of the specified panel
     *     and sets the background and color to indicate
     *     the field is a link.
     *   @param component Component to be added
     *   @param row row index, zero-based.
     *   @param panel panel index, zero-based.
     *   @param weighty specifies how to distribute extra vertical space,
     *      see GridBagConstraint for details on usage.
     */
    final public void addLinkField(Component component,int row,int panel,double weighty)
    {
        component.setBackground(getBackground());
        component.setForeground(Color.blue);
        addField(component,row,panel,weighty);
    }
    


    public Profile getProfile() {
        if(_profile == null) {
            _profile = new ProfileJava();
        }
        return _profile;
    }

    /**
        This method (and addMElementListener) can be overriden if the 
        prop panel wants to monitor additional objects.
    
        @param target target of prop panel
    
    */
    protected void removeMElementListener(MBase target) {
        target.removeMElementListener(this);
    }
    
    /**
        This method (and removeMElementListener) can be overriden if the 
        prop panel wants to monitor additional objects.  This method
        is public only since it is called from a Runnable object.
    
        @param target target of prop panel
    */
    public void addMElementListener(MBase target) {
        target.addMElementListener(this);
    }
    
    
    public void setTarget(Object t) {
        if(!t.equals(_target)) {            
            boolean removeOldPromiscuousListener = (_nameListener != null);
            if(t instanceof MBase && _nameListener != null) {
//XXX                removeOldPromiscuousListener = 
//XXX                    ((MBase) t).addPromiscuousListener(_nameListener);
            }
            
            //
            //   if the previous target was a MBase (99.999% of the time)
            //      remove the listener from it
            if ( _target instanceof MBase ) {
                removeMElementListener((MBase) _target);
                //
                //  this path shouldn't happen unless t == null
                //
//XXX                if(removeOldPromiscuousListener) {
//XXX                    ((MBase) _target).removePromiscuousListener(_nameListener);
                }
            }
            
            _target = t;
            _modelElement = null;
            
            
            if(_target instanceof MModelElement) {
                _modelElement = (MModelElement) _target;
            }
            
            //
            //   this will add listener after update is complete
            //
            SwingUtilities.invokeLater(new UMLChangeDispatch(this,-1));
        }
    }

    public Object getTarget() { return _target; }
    
    public MModelElement getModelElement() {
        return _modelElement;
    }

    public void refresh() { 
        SwingUtilities.invokeLater(new UMLChangeDispatch(this,0));
    }

    public boolean shouldBeEnabled() { return (_modelElement != null); }


    public void propertySet(MElementEvent mee) {
        UMLChangeDispatch dispatch = new UMLChangeDispatch(this,0);
        dispatch.propertySet(mee);
        SwingUtilities.invokeLater(dispatch);
    }

    public void listRoleItemSet(MElementEvent mee) {
        UMLChangeDispatch dispatch = new UMLChangeDispatch(this,0);
        dispatch.listRoleItemSet(mee);
        SwingUtilities.invokeLater(dispatch);
    }
    
    public void recovered(MElementEvent mee) {
        UMLChangeDispatch dispatch = new UMLChangeDispatch(this,0);
        dispatch.recovered(mee);
        SwingUtilities.invokeLater(dispatch);
    }
    
    public void removed(MElementEvent mee) {
        UMLChangeDispatch dispatch = new UMLChangeDispatch(this,0);
        dispatch.removed(mee);
        SwingUtilities.invokeLater(dispatch);
    }
    
    public void roleAdded(MElementEvent mee) {
        UMLChangeDispatch dispatch = new UMLChangeDispatch(this,0);
        dispatch.roleAdded(mee);
        SwingUtilities.invokeLater(dispatch);
    }
    
    public void roleRemoved(MElementEvent mee) {
        UMLChangeDispatch dispatch = new UMLChangeDispatch(this,0);
        dispatch.roleRemoved(mee);
        SwingUtilities.invokeLater(dispatch);
    }

        
    public String formatElement(MModelElement element) {
        MNamespace ns = null;
        if(_modelElement != null) {
             ns = _modelElement.getNamespace();
        }
        return getProfile().formatElement(element,ns);
    }
    
    public String formatNamespace(MNamespace ns) {
        return getProfile().formatElement(ns,null);
    }
    
    
    
    public String formatCollection(Iterator iter) {
        MNamespace ns = null;
        if(_modelElement != null) {
            ns = _modelElement.getNamespace();
        }
        return getProfile().formatCollection(iter,ns);
    }
    
    public void navigateTo(Object element) {
        Iterator iter = _navListeners.iterator();
        while(iter.hasNext()) {
            ((NavigationListener) iter.next()).navigateTo(element);
        }
    }
    public void open(Object element) {
        Iterator iter = _navListeners.iterator();
        while(iter.hasNext()) {
            ((NavigationListener) iter.next()).open(element);
        }
    }

    
    public boolean navigateBack(boolean attempt) {
        boolean navigated = false;
        Iterator iter = _navListeners.iterator();
	    while(iter.hasNext()) {
	        navigated = ((NavigationListener) iter.next()).navigateBack(attempt);
            if(navigated) attempt = false;
	    }
        return navigated;
    }

    public void navigateBackAction() {
        boolean attempt = true;
        navigateBack(attempt);
    }
        
    public boolean navigateForward(boolean attempt) {
        boolean navigated = false;
        Iterator iter = _navListeners.iterator();
	    while(iter.hasNext()) {
	        navigated = ((NavigationListener) iter.next()).navigateForward(attempt);
            if(navigated) attempt = false;
	    }
        return navigated;
    }
    
    public void navigateForwardAction() {
        boolean attempt = true;
        navigateForward(attempt);
    }

    public boolean isNavigateForwardEnabled() {
        boolean enabled = false;
        Iterator iter = _navListeners.iterator();
	    while(iter.hasNext() && !enabled) {
	        enabled = ((NavigationListener) iter.next()).isNavigateForwardEnabled();
	    }
        return enabled;
    }

    public boolean isNavigateBackEnabled() {
        boolean enabled = false;
        Iterator iter = _navListeners.iterator();
	    while(iter.hasNext() && !enabled) {
	        enabled = ((NavigationListener) iter.next()).isNavigateBackEnabled();
	    }
        return enabled;
    }

    
    
    /**    Registers a listener for navigation events.
     */
    public void addNavigationListener(NavigationListener navListener) {
        _navListeners.add(navListener);
    }
        
    public void removeNavigationListener(NavigationListener navListener) {
        _navListeners.remove(navListener);
    }

    /**
        Calling this method with an array of metaclasses 
        (for example, MClassifier.class) will result in the prop panel
        propagating any name changes or removals on any object that
        on the same event queue as the target that is assignable to one
        of the metaclasses.
    */
    public void setNameEventListening(Class[] metaclasses) {
        Object target = getTarget();
        if(target instanceof MBase) {
            MBase base = (MBase) target;
            if(_nameListener != null) {
//XXX                base.removePromiscuousListener(_nameListener);
            }
            _nameListener = new UMLNameEventListener(this,metaclasses);
//XXX            base.addPromiscuousListener(_nameListener);
        }
        else {
            _nameListener = new UMLNameEventListener(this,metaclasses);
        }
    }
    
    public void removeElement() {
        Object target = getTarget();
        if(target instanceof MBase) {
            ((MBase) target).remove();
        }
    }
    
        
    
} /* end class PropPanel */
