/*
 * Created on 21.10.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.argouml.cognitive.ui;

import javax.swing.tree.TreeModel;

import org.tigris.gef.util.Predicate;
import org.tigris.gef.util.PredicateTrue;

/**
 * @author MarkusK
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractGoList implements TreeModel {
    
    private Predicate listPredicate = new PredicateTrue();
    
    public void setListPredicate(Predicate newPredicate) {
        listPredicate = newPredicate;
    }

    public Predicate getListPredicate() {
        return listPredicate;
    }
    
    /**
     * @see javax.swing.tree.TreeModel#getRoot()
     */
    public Object getRoot() {
    throw new UnsupportedOperationException();
    } 
    
    /**
     * @param r ignored
     */
    public void setRoot(Object r) { }
    
}
