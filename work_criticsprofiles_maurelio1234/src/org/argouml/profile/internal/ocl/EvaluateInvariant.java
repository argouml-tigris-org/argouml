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

package org.argouml.profile.internal.ocl;

import java.util.HashMap;
import java.util.Stack;

import org.apache.log4j.Logger;

import tudresden.ocl.parser.analysis.DepthFirstAdapter;
import tudresden.ocl.parser.node.AAdditiveExpression;
import tudresden.ocl.parser.node.AConstraintBody;
import tudresden.ocl.parser.node.AExpression;
import tudresden.ocl.parser.node.AIfExpression;
import tudresden.ocl.parser.node.ALogicalExpression;
import tudresden.ocl.parser.node.ARelationalExpression;
import tudresden.ocl.parser.node.Node;
import tudresden.ocl.parser.node.PAdditiveExpressionTail;
import tudresden.ocl.parser.node.PLetExpression;
import tudresden.ocl.parser.node.PLogicalExpressionTail;

/**
 * Evaluates the OCL invariant 
 * 
 * @author maurelio1234
 */
public class EvaluateInvariant extends DepthFirstAdapter {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(EvaluateInvariant.class);
    
    private boolean ok = true;
    private Stack<Object> st = new Stack<Object>();
    private HashMap<String, Object> vt = new HashMap<String,Object>();
    private Object currentValue = null;
    
    /**
     * Constructor 
     * 
     * @param modelElement self
     */
    public EvaluateInvariant(Object modelElement) {
        vt.put("self", modelElement);
    }

    /**
     * @return is the invariant ok?
     */
    public boolean isOK() {
        return ok;
    }
  
    /**
     * @param node
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#caseAConstraintBody(tudresden.ocl.parser.node.AConstraintBody)
     */
    public void caseAConstraintBody(AConstraintBody node)
    {
        inAConstraintBody(node);
        if(node.getStereotype() != null)
        {
            node.getStereotype().apply(this);
        }
        if(node.getName() != null)
        {
            node.getName().apply(this);
        }
        if(node.getColon() != null)
        {
            node.getColon().apply(this);
        }
        if(node.getExpression() != null)
        {            
            st.removeAllElements();
            node.getExpression().apply(this);
            Object top = st.pop();
            if (top instanceof Boolean) {
                ok = (Boolean)top;
            } else {
                LOG.debug("Invariant does not evaluate to a Boolean! st = "
                        + st);                
                ok = false;
            }
        }
        outAConstraintBody(node);
    }    

    public void caseAIfExpression(AIfExpression node)
    {            
        inAIfExpression(node);
        if(node.getTIf() != null)
        {
            node.getTIf().apply(this);
        }
        if(node.getIfBranch() != null)
        {
            node.getIfBranch().apply(this);
        }        

        boolean iftest = (Boolean)st.pop();
        
        if(node.getTThen() != null)
        {
            node.getTThen().apply(this);
        }
        if(node.getThenBranch() != null)
        {
            node.getThenBranch().apply(this);
            if (!iftest) // if "if" wasn't ok, forget if
                st.pop();
        }
        if(node.getTElse() != null)
        {
            node.getTElse().apply(this);
        }
        if(node.getElseBranch() != null)
        {
            node.getElseBranch().apply(this);
            if (iftest) // if "if" was ok, forget else
                st.pop();
        }
        if(node.getEndif() != null)
        {
            node.getEndif().apply(this);
        }
        outAIfExpression(node);
    }
    
    public void caseAExpression(AExpression node)
    {
        inAExpression(node);
        {
            Object temp[] = node.getLetExpression().toArray();
            for(int i = 0; i < temp.length; i++)
            {
                ((PLetExpression) temp[i]).apply(this);
            }
        }
        if(node.getLogicalExpression() != null)
        {
            node.getLogicalExpression().apply(this);
        }
        outAExpression(node);
    }
    
    public void caseALogicalExpression(ALogicalExpression node)
    {
        inALogicalExpression(node);
        if(node.getRelationalExpression() != null)
        {
            node.getRelationalExpression().apply(this);
        }
        {
            Object temp[] = node.getLogicalExpressionTail().toArray();
            for(int i = 0; i < temp.length; i++)
            {
                ((PLogicalExpressionTail) temp[i]).apply(this);
            }
        }
        outALogicalExpression(node);
    }
    
    public void caseARelationalExpression(ARelationalExpression node)
    {
        inARelationalExpression(node);
        if(node.getAdditiveExpression() != null)
        {
            node.getAdditiveExpression().apply(this);
        }
        if(node.getRelationalExpressionTail() != null)
        {
            node.getRelationalExpressionTail().apply(this);
        }
        outARelationalExpression(node);
    }
    
    public void caseAAdditiveExpression(AAdditiveExpression node)
    {
        inAAdditiveExpression(node);
        if(node.getMultiplicativeExpression() != null)
        {
            node.getMultiplicativeExpression().apply(this);
        }
        {
            Object temp[] = node.getAdditiveExpressionTail().toArray();
            for(int i = 0; i < temp.length; i++)
            {
                ((PAdditiveExpressionTail) temp[i]).apply(this);
            }
        }
        outAAdditiveExpression(node);
    }
    
    public void defaultIn(Node node)
    {
        LOG.debug("Entering: " + node.getClass() + "\n VALUE: " + node);
    }
}
