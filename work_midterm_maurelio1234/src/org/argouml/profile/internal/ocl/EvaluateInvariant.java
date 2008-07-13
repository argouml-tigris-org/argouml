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
import java.util.Vector;

import org.apache.log4j.Logger;

import tudresden.ocl.parser.analysis.DepthFirstAdapter;
import tudresden.ocl.parser.node.AActualParameterList;
import tudresden.ocl.parser.node.AAdditiveExpressionTail;
import tudresden.ocl.parser.node.AAndLogicalOperator;
import tudresden.ocl.parser.node.ABooleanLiteral;
import tudresden.ocl.parser.node.AConstraint;
import tudresden.ocl.parser.node.ADeclaratorTail;
import tudresden.ocl.parser.node.ADivMultiplyOperator;
import tudresden.ocl.parser.node.AEmptyFeatureCallParameters;
import tudresden.ocl.parser.node.AEnumLiteral;
import tudresden.ocl.parser.node.AEqualRelationalOperator;
import tudresden.ocl.parser.node.AFeatureCall;
import tudresden.ocl.parser.node.AFeatureCallParameters;
import tudresden.ocl.parser.node.AFeaturePrimaryExpression;
import tudresden.ocl.parser.node.AGtRelationalOperator;
import tudresden.ocl.parser.node.AGteqRelationalOperator;
import tudresden.ocl.parser.node.AIfExpression;
import tudresden.ocl.parser.node.AImpliesLogicalOperator;
import tudresden.ocl.parser.node.AIntegerLiteral;
import tudresden.ocl.parser.node.AIterateDeclarator;
import tudresden.ocl.parser.node.ALetExpression;
import tudresden.ocl.parser.node.ALiteralCollection;
import tudresden.ocl.parser.node.ALogicalExpressionTail;
import tudresden.ocl.parser.node.ALtRelationalOperator;
import tudresden.ocl.parser.node.ALteqRelationalOperator;
import tudresden.ocl.parser.node.AMinusAddOperator;
import tudresden.ocl.parser.node.AMinusUnaryOperator;
import tudresden.ocl.parser.node.AMultMultiplyOperator;
import tudresden.ocl.parser.node.AMultiplicativeExpressionTail;
import tudresden.ocl.parser.node.ANEqualRelationalOperator;
import tudresden.ocl.parser.node.ANotUnaryOperator;
import tudresden.ocl.parser.node.AOrLogicalOperator;
import tudresden.ocl.parser.node.APlusAddOperator;
import tudresden.ocl.parser.node.APostfixExpressionTail;
import tudresden.ocl.parser.node.ARealLiteral;
import tudresden.ocl.parser.node.ARelationalExpressionTail;
import tudresden.ocl.parser.node.AStandardDeclarator;
import tudresden.ocl.parser.node.AStringLiteral;
import tudresden.ocl.parser.node.AUnaryUnaryExpression;
import tudresden.ocl.parser.node.AXorLogicalOperator;
import tudresden.ocl.parser.node.Node;
import tudresden.ocl.parser.node.PActualParameterListTail;
import tudresden.ocl.parser.node.PConstraintBody;
import tudresden.ocl.parser.node.PDeclaratorTail;
import tudresden.ocl.parser.node.PExpression;
import tudresden.ocl.parser.node.Start;

/**
 * Evaluates OCL invariants, this class should not depend on the model
 * subsystem. 
 * 
 * @author maurelio1234
 */
public class EvaluateInvariant extends DepthFirstAdapter {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(EvaluateInvariant.class);
    
    /**
     * The Variable Table
     */
    private HashMap<String, Object> vt = new HashMap<String, Object>();
    
    /**
     * Is this invariant satified?
     */
    private boolean ok = true;
    
    /**
     * Keeps the return value of the visitor 
     */
    private Object val = null;
    
    /**
     * Keeps a forward propagated value
     */
    private Object fwd = null;    
    
    /**
     * The model interpreter
     */
    private ModelInterpreter interp = null;
    
    /**
     * Constructor 
     * 
     * @param modelElement self
     * @param mi model interpreter
     */
    public EvaluateInvariant(Object modelElement, ModelInterpreter mi) {
        this.interp = mi;
        vt.put("self", modelElement);
    }

    /**
     * @return is the invariant ok?
     */
    public boolean isOK() {
        return ok;
    }
  
    /** Interpreter Code **/   
    public void outStart(Start node)
    {
        ok = asBoolean(val, node);
    }
    
    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#defaultIn(tudresden.ocl.parser.node.Node)
     */
    public void defaultIn(Node node)
    {
        //LOG.debug("\nEntering: " + node.getClass() + "\n NODE: " + node + "\n VAL: " + val + " fwd: "+ fwd);
    }

    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#caseAConstraint(tudresden.ocl.parser.node.AConstraint)
     */
    public void caseAConstraint(AConstraint node)
    {
        inAConstraint(node);
        if(node.getContextDeclaration() != null)
        {
            node.getContextDeclaration().apply(this);
        }
        {
            boolean localOk = true;
            
            Object temp[] = node.getConstraintBody().toArray();
            for(int i = 0; i < temp.length; i++)
            {
                ((PConstraintBody) temp[i]).apply(this);

                localOk &= asBoolean(val, temp[i]);
            }
            
            val = localOk;
        }
        outAConstraint(node);
    }

    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#caseAIfExpression(tudresden.ocl.parser.node.AIfExpression)
     */
    public void caseAIfExpression(AIfExpression node)
    {
        boolean test = false;
        boolean ret = false;
        
        inAIfExpression(node);
        if(node.getTIf() != null)
        {
            node.getTIf().apply(this);
        }
        if(node.getIfBranch() != null)
        {
            node.getIfBranch().apply(this);            
            test = asBoolean(val, node.getIfBranch());
            val = null;
        }
        if(node.getTThen() != null)
        {
            node.getTThen().apply(this);
        }
        if(node.getThenBranch() != null)
        {
            node.getThenBranch().apply(this);
            if (test) {
                ret = asBoolean(val, node.getThenBranch());
                val = null;
            }
        }
        if(node.getTElse() != null)
        {
            node.getTElse().apply(this);
        }
        if(node.getElseBranch() != null)
        {
            node.getElseBranch().apply(this);
            if (!test) {
                ret = asBoolean(val, node.getThenBranch());                
                val = null;
            }
        }
        if(node.getEndif() != null)
        {
            node.getEndif().apply(this);
        }
        
        val = ret;
        outAIfExpression(node);
    }

    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#caseALogicalExpressionTail(tudresden.ocl.parser.node.ALogicalExpressionTail)
     */
    public void caseALogicalExpressionTail(ALogicalExpressionTail node)
    {
        Object left = val;
        val = null;

        inALogicalExpressionTail(node);
        if(node.getLogicalOperator() != null)
        {
            node.getLogicalOperator().apply(this);
        }
        if(node.getRelationalExpression() != null)
        {
            node.getRelationalExpression().apply(this);
        }

        Object op = node.getLogicalOperator();
        Object right = val;
        val = null;
        
        if (left != null && op!= null && right != null) {
            if (op instanceof AAndLogicalOperator) {
                val = asBoolean(left, node) && asBoolean(right, node);
            } else if (op instanceof AImpliesLogicalOperator) {
                val = !asBoolean(left, node) || asBoolean(right, node);
            } else if (op instanceof AOrLogicalOperator) {
                val = asBoolean(left, node) || asBoolean(right, node);
            } else if (op instanceof AXorLogicalOperator) {
                val = !asBoolean(left, node) ^ asBoolean(right, node);
            } else {
                error(node);
            }           
        } else {
            error(node);
        }
        outALogicalExpressionTail(node);
    }
    
    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#caseARelationalExpressionTail(tudresden.ocl.parser.node.ARelationalExpressionTail)
     */
    public void caseARelationalExpressionTail(ARelationalExpressionTail node)
    {
        Object left = val;
        val = null;
        
        inARelationalExpressionTail(node);
        if(node.getRelationalOperator() != null)
        {
            node.getRelationalOperator().apply(this);                       
        }
        if(node.getAdditiveExpression() != null)
        {
            node.getAdditiveExpression().apply(this);
        }

        Object op = node.getRelationalOperator();
        Object right = val;
        val = null;
        
        if (left != null && op!= null && right != null) {
            if (op instanceof AEqualRelationalOperator) {
                val = left.equals(right);                
            } else if (op instanceof AGteqRelationalOperator) {
                val = asInteger(left, node) >= asInteger(right, node);
            } else if (op instanceof AGtRelationalOperator) {
                val = asInteger(left, node) > asInteger(right, node);
            } else if (op instanceof ALteqRelationalOperator) {
                val = asInteger(left, node) <= asInteger(right, node);
            } else if (op instanceof ALtRelationalOperator) {
                val = asInteger(left, node) < asInteger(right, node);
            } else if (op instanceof ANEqualRelationalOperator) {
                val = !left.equals(right);                
            } else {
                error(node);                
            }
        } else {
            error(node);
        }
        outARelationalExpressionTail(node);
    }

    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#caseAAdditiveExpressionTail(tudresden.ocl.parser.node.AAdditiveExpressionTail)
     */
    public void caseAAdditiveExpressionTail(AAdditiveExpressionTail node)
    {
        Object left = val;
        val = null;

        inAAdditiveExpressionTail(node);
        if(node.getAddOperator() != null)
        {
            node.getAddOperator().apply(this);
        }
        if(node.getMultiplicativeExpression() != null)
        {
            node.getMultiplicativeExpression().apply(this);
        }

        Object op = node.getAddOperator();
        Object right = val;
        val = null;
        
        if (left != null && op!= null && right != null) {
            if (op instanceof AMinusAddOperator) {
                val = asInteger(left, node) - asInteger(right, node);
            } else if (op instanceof APlusAddOperator) {
                val = asInteger(left, node) + asInteger(right, node);
            } else {
                error(node);
            }           
        } else {
            error(node);
        }
        
        outAAdditiveExpressionTail(node);
    }
    
    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#caseAMultiplicativeExpressionTail(tudresden.ocl.parser.node.AMultiplicativeExpressionTail)
     */
    public void caseAMultiplicativeExpressionTail(AMultiplicativeExpressionTail node)
    {
        Object left = val;
        val = null;

        inAMultiplicativeExpressionTail(node);
        if(node.getMultiplyOperator() != null)
        {
            node.getMultiplyOperator().apply(this);
        }
        if(node.getUnaryExpression() != null)
        {
            node.getUnaryExpression().apply(this);
        }

        Object op = node.getMultiplyOperator();
        Object right = val;
        val = null;
        
        if (left != null && op!= null && right != null) {
            if (op instanceof ADivMultiplyOperator) {
                val = asInteger(left, node) / asInteger(right, node);
            } else if (op instanceof AMultMultiplyOperator) {
                val = asInteger(left, node) * asInteger(right, node);
            } else {
                error(node);
            }           
        } else {
            error(node);
        }
        
        outAMultiplicativeExpressionTail(node);
    }
        
    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#caseAUnaryUnaryExpression(tudresden.ocl.parser.node.AUnaryUnaryExpression)
     */
    public void caseAUnaryUnaryExpression(AUnaryUnaryExpression node)
    {
        inAUnaryUnaryExpression(node);
        if(node.getUnaryOperator() != null)
        {
            node.getUnaryOperator().apply(this);
        }
        if(node.getPostfixExpression() != null)
        {
            val = null;
            node.getPostfixExpression().apply(this);
        }
        
        Object op = node.getUnaryOperator();
        if (op instanceof AMinusUnaryOperator) {
            val = -asInteger(val, node);
        } else if (op instanceof ANotUnaryOperator) {
            val = !asBoolean(val, node);
        }
        
        outAUnaryUnaryExpression(node);
    }
    
    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#caseAPostfixExpressionTail(tudresden.ocl.parser.node.APostfixExpressionTail)
     */
    public void caseAPostfixExpressionTail(APostfixExpressionTail node)
    {
        inAPostfixExpressionTail(node);
        if(node.getPostfixExpressionTailBegin() != null)
        {
            node.getPostfixExpressionTailBegin().apply(this);
        }
        if(node.getFeatureCall() != null)
        {
            fwd = node.getPostfixExpressionTailBegin();
            node.getFeatureCall().apply(this);
            
            // XXX: hypotheses for AFeatureCall: fwd = op, val = head
        }
        outAPostfixExpressionTail(node);
    }
    
    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#caseAFeaturePrimaryExpression(tudresden.ocl.parser.node.AFeaturePrimaryExpression)
     */
    @SuppressWarnings("unchecked")
    public void caseAFeaturePrimaryExpression(AFeaturePrimaryExpression node)
    {
        Object subject = val;
        Object feature = null;
        Vector<Object> parameters = null;
        
        inAFeaturePrimaryExpression(node);
        if(node.getPathName() != null)
        {            
            // TODO support other name kinds             
            node.getPathName().apply(this);
            feature = node.getPathName().toString().trim();
        }
        if(node.getTimeExpression() != null)
        {
            // hypotheses no time expression (only invariants)
            node.getTimeExpression().apply(this);
        }
        if(node.getQualifiers() != null)
        {
            // XXX: hypotheses no qualifiers (I don't know)
            node.getQualifiers().apply(this);
        }
        if(node.getFeatureCallParameters() != null)
        {
            val = null;
            node.getFeatureCallParameters().apply(this);            
            parameters = (Vector<Object>) val;
        }

        if (subject == null) {
            val = vt.get(feature);
        } else {
            val = runFeatureCall(subject, feature,fwd,parameters);            
        }
        outAFeaturePrimaryExpression(node);
    }       

    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#outAEmptyFeatureCallParameters(tudresden.ocl.parser.node.AEmptyFeatureCallParameters)
     */
    public void outAEmptyFeatureCallParameters(AEmptyFeatureCallParameters node)
    {
        val = new Vector<Object>();
        defaultOut(node);
    }
    
    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#caseAFeatureCallParameters(tudresden.ocl.parser.node.AFeatureCallParameters)
     */
    @SuppressWarnings("unchecked")
    public void caseAFeatureCallParameters(AFeatureCallParameters node)
    {
        inAFeatureCallParameters(node);
        if(node.getLPar() != null)
        {
            node.getLPar().apply(this);
        }
        
        boolean hasDeclarator = false;
        if(node.getDeclarator() != null)
        {
            node.getDeclarator().apply(this);
            hasDeclarator = true;
        }
        if (node.getActualParameterList() != null) {
            Vector<String> vars = null;
            if (hasDeclarator) {
                Vector<Object> ret = new Vector<Object>();
                vars = (Vector<String>) val;
                final PExpression exp = ((AActualParameterList) node
                        .getActualParameterList()).getExpression();
                
                /*
                 * For a iterator call we should provide:
                 *  (a) the variables
                 *  (b) the expression to be evaluated on each step
                 *  (c) the lambda-evaluator to evaluate it
                 */
                
                ret.add(vars);
                ret.add(exp);
                ret.add(new LambdaEvaluator() {

                    /**
                     * @see org.argouml.profile.internal.ocl.LambdaEvaluator#evaluate(java.util.HashMap, java.lang.Object)
                     */
                    public Object evaluate(HashMap<String, Object> vti, Object expi) {
                        Object old_val = EvaluateInvariant.this.val;
                        EvaluateInvariant.this.val = null;
                        ((PExpression)expi).apply(EvaluateInvariant.this);
                        
                        Object reti = EvaluateInvariant.this.val;
                        EvaluateInvariant.this.val = old_val;
                        
                        return reti;
                    }
                    
                });                
                
                val = ret;
            } else {
                node.getActualParameterList().apply(this);
            }

        }
        if (node.getRPar() != null) {
            node.getRPar().apply(this);
        }
        outAFeatureCallParameters(node);
    }
    
    /**
     * @param node
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#caseAStandardDeclarator(tudresden.ocl.parser.node.AStandardDeclarator)
     */
    public void caseAStandardDeclarator(AStandardDeclarator node)
    {        
        inAStandardDeclarator(node);
        
        Vector<String> vars = new Vector<String>();
        
        if(node.getName() != null)
        {
            node.getName().apply(this);
            
            vars.add(node.getName().toString().trim());
        }
        {            
            Object temp[] = node.getDeclaratorTail().toArray();
            for (int i = 0; i < temp.length; i++) {
                ((PDeclaratorTail) temp[i]).apply(this);

                vars.add(((ADeclaratorTail) temp[i]).getName()

                .toString().trim());                
            }
            
            val = vars;
        }
        if(node.getDeclaratorTypeDeclaration() != null)
        {
            // TODO check types!
            node.getDeclaratorTypeDeclaration().apply(this);
        }
        if(node.getBar() != null)
        {
            node.getBar().apply(this);
        }
        outAStandardDeclarator(node);
    }

    public void outAIterateDeclarator(AIterateDeclarator node)
    {
        //TODO support iterate declarator
        val = new Vector<String>();
        defaultOut(node);
    }

    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#caseALetExpression(tudresden.ocl.parser.node.ALetExpression)
     */
    public void caseALetExpression(ALetExpression node)
    {
        // TODO support nested let expressions !

        Object name = null;
        Object value = null;
        
        inALetExpression(node);
        if(node.getTLet() != null)
        {
            node.getTLet().apply(this);
        }
        if(node.getName() != null)
        {
            node.getName().apply(this);
            name = node.getName().toString().trim();
        }
        if(node.getLetExpressionTypeDeclaration() != null)
        {
            // TODO: check type!
            node.getLetExpressionTypeDeclaration().apply(this);
        }
        if(node.getEqual() != null)
        {
            node.getEqual().apply(this);
        }
        if(node.getExpression() != null)
        {
            node.getExpression().apply(this);
        }
        if(node.getTIn() != null)
        {
            node.getTIn().apply(this);
        }
        
        vt.put((""+name).trim(), value);
        val = value;
        outALetExpression(node);
    }
    
    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#outAStringLiteral(tudresden.ocl.parser.node.AStringLiteral)
     */
    public void outAStringLiteral(AStringLiteral node)
    {
        val = node.getStringLit().getText();
        defaultOut(node);
    }

    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#outARealLiteral(tudresden.ocl.parser.node.ARealLiteral)
     */
    public void outARealLiteral(ARealLiteral node)
    {
        // TODO support real types
        val = (int)Double.parseDouble(node.getReal().getText());
        defaultOut(node);
    }
    
    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#outAIntegerLiteral(tudresden.ocl.parser.node.AIntegerLiteral)
     */
    public void outAIntegerLiteral(AIntegerLiteral node)
    {
        val = Integer.parseInt(node.getInt().getText());
        defaultOut(node);
    }
    
    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#outABooleanLiteral(tudresden.ocl.parser.node.ABooleanLiteral)
     */
    public void outABooleanLiteral(ABooleanLiteral node)
    {
        val = Boolean.parseBoolean(node.getBool().getText());
        defaultOut(node);
    }

    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#outAEnumLiteral(tudresden.ocl.parser.node.AEnumLiteral)
     */
    public void outAEnumLiteral(AEnumLiteral node)
    {
        // TODO support enums!
        val = null;
        defaultOut(node);
    }
    
    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#outALiteralCollection(tudresden.ocl.parser.node.ALiteralCollection)
     */
    public void outALiteralCollection(ALiteralCollection node)
    {
        // support collections
        val = new Vector<Object>();
        defaultOut(node);
    }

    /**
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#caseAFeatureCall(tudresden.ocl.parser.node.AFeatureCall)
     */
    @SuppressWarnings("unchecked")
    public void caseAFeatureCall(AFeatureCall node)
    {
        Object subject = val;
        Object feature = null;
        Vector<Object> parameters = null;
        
        inAFeatureCall(node);
        if(node.getPathName() != null)
        {
            // TODO support other name kinds 
            node.getPathName().apply(this);
            
            feature = node.getPathName().toString().trim();
        }
        if(node.getTimeExpression() != null)
        {
            // XXX hypothesis: no time expression (inv)
            node.getTimeExpression().apply(this);
        }
        if(node.getQualifiers() != null)
        {
            // TODO understand qualifiers
            node.getQualifiers().apply(this);
        }
        if(node.getFeatureCallParameters() != null)
        {
            val = null;            
            node.getFeatureCallParameters().apply(this);
            
            parameters = (Vector<Object>) val;
        } else {
            parameters = new Vector<Object>();
        }
        
        val = runFeatureCall(subject, feature, fwd, parameters);
        outAFeatureCall(node);
    }
    
    public void caseAActualParameterList(AActualParameterList node)
    {
        Vector<Object> list = new Vector<Object>(); 
        inAActualParameterList(node);
        if(node.getExpression() != null)
        {
            val = null;
            node.getExpression().apply(this);
            list.add(val);
        }
        {
            
            Object temp[] = node.getActualParameterListTail().toArray();
            for(int i = 0; i < temp.length; i++)
            {
                val = null;
                ((PActualParameterListTail) temp[i]).apply(this);                
                list.add(val);
            }
        }
        
        val = list;
        outAActualParameterList(node);
    }
    
    /** HELPER METHODS **/
    private boolean asBoolean(Object value, Object node) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else {
            errorNotType(node, "Boolean", false);
            return false;
        }                
    }

    private int asInteger(Object value, Object node) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else {
            errorNotType(node, "integer", 0);
            return 0;
        }                
    }

    @SuppressWarnings("unchecked")
    private Object runFeatureCall(Object subject, Object feature, Object type, Vector<Object> parameters) {
        //LOG.debug("OCL FEATURE CALL: " + subject + ""+ type +""+ feature + "" + parameters);
        
        if (parameters == null) {
            parameters = new Vector<Object>();
        }
        
        return interp.invokeFeature(vt, subject, feature.toString().trim(),
                type.toString().trim(), parameters.toArray());
    }
    
    /** Error Handling **/
    private void errorNotType(Object node, String type, Object dft) {
        LOG.error("OCL does not evaluate to a "+type+ " expression!! Exp: "
                + node + " Val: " + val);
        val = dft;
    }

    private void error(Object node) {
        LOG.error("Unknown error processing OCL exp!! Exp: "
                + node + " Val: " + val);
        val = null;
    }
    
}
