/*
 * Created on 25.08.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tudresden.ocl20.integration;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.reflect.RefPackage;

import tudresden.ocl20.core.MetaModelConst;
import tudresden.ocl20.core.MetaModelIntegrator;
import tudresden.ocl20.core.MetaModelUtil;
import tudresden.ocl20.core.ModelManager;
import tudresden.ocl20.core.ModelManagerException;
import tudresden.ocl20.core.OclModel;
import tudresden.ocl20.core.OclModelException;
import tudresden.ocl20.core.Repository;
import tudresden.ocl20.core.RepositoryManager;
import tudresden.ocl20.core.MetaModelConst.MetaModelInfo;
import tudresden.ocl20.core.jmi.uml15.core.Association;
import tudresden.ocl20.core.jmi.uml15.core.AssociationEnd;
import tudresden.ocl20.core.jmi.uml15.core.CorePackage;
import tudresden.ocl20.core.jmi.uml15.core.Namespace;
import tudresden.ocl20.core.jmi.uml15.core.UmlClass;
import tudresden.ocl20.core.jmi.uml15.datatypes.Multiplicity;
import tudresden.ocl20.core.jmi.uml15.datatypes.MultiplicityRange;
import tudresden.ocl20.core.jmi.uml15.impl.core.ModelElementImpl;
import tudresden.ocl20.core.jmi.uml15.impl.modelmanagement.ModelHelper;
import tudresden.ocl20.core.jmi.uml15.impl.modelmanagement.PackageImpl;
import tudresden.ocl20.core.jmi.uml15.modelmanagement.Model;
import tudresden.ocl20.core.jmi.uml15.modelmanagement.Package;
import tudresden.ocl20.core.jmi.uml15.uml15.Uml15Package;
import tudresden.ocl20.core.parser.astgen.Heritage;
import tudresden.ocl20.core.parser.astgen.LAttrAstGenerator;
import tudresden.ocl20.core.parser.sablecc.analysis.AttrEvalException;
import tudresden.ocl20.core.parser.sablecc.lexer.Lexer;
import tudresden.ocl20.core.parser.sablecc.lexer.LexerException;
import tudresden.ocl20.core.parser.sablecc.node.Start;
import tudresden.ocl20.core.parser.sablecc.parser.Parser;
import tudresden.ocl20.core.parser.sablecc.parser.ParserException;

/**
 * This class can be use to start the validation of a constraint.
 * @author Mirko 
 */
public class OCLChecker 
{
	public static HashMap instances = new HashMap();
	
	/**
	 * Returns the existing OCLChecker instance for the given object or creates a new instance. 
	 */
	public static OCLChecker getInstance(Object rootPackage)
	{
		if (instances.get(rootPackage) == null)
		{
			try 
			{
				OCLChecker instance =  new OCLChecker();	
				instance.initModel();
				instances.put(rootPackage, instance);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return (OCLChecker) instances.get(rootPackage);         
	}
	
	/**
	 * Deletes all models in the MDR.
	 *
	 */
	private static void deleteModels() 
	{
		for (int i = 0; i < instances.size(); i++)
		{	
			((OCLChecker)instances.get(i)).deleteModel();
		}			
	}

	private static ModelManager mm = ModelManager.getInstance();   
    private Uml15Package model = null;
    
    private OCLChecker()
    {
    	
    }	
	
    /**
     * Creates a new UML model in the MDR with one Model instance. 
     */
	public void initModel() throws Exception
    {     
		//deleteModel();
		System.out.println("Initialize Model");
		mm.beginTrans(true);
		model = (Uml15Package) mm.createOclModel(MetaModelConst.UML15, this.getUniqueName());
		Model rootModel = model.getModelManagement().getModel().createModel();
	    rootModel.setNameA("rootPackage");	 
	    
	    mm.endTrans(false);    	
    } 
	
	/**
	 * Sets the ModelFacade for the model of the OCLChecker instance.
	 */
	public void setModelFacade(ModelFacade facade) throws Exception
	{
		ModelFacade.addModelFacade(this.model.refMofId(), facade);
		ModelFacade instance = ModelFacade.getInstance(this.model.refMofId());
		Model topPackage = ModelHelper.getInstance(this.model).getTopPackage();
		
		ArrayList result = new ArrayList();
		Iterator it = this.instances.keySet().iterator();
		while (it.hasNext())
		{
			Object caseTop = it.next();
			if (this.instances.get(caseTop) != null)
				if (this.instances.get(caseTop).equals(this))
						instance.addRefObject(topPackage.refMofId(), caseTop);					
		}
	}
	
	/**
	 * Validates the given constraint. 
	 */
	public void validate(String constraint) throws OclModelException, ParserException, LexerException, IOException, AttrEvalException
	{
		if (this.model != null)
		{
			OclModel oclModel =  new OclModel(MetaModelConst.UML15, model);
			System.out.println("Syntaxprüfung");
			Lexer lexer = new Lexer (new PushbackReader(new StringReader(constraint)));
			System.out.println("CSTErstellung");
			Parser parser = new Parser(lexer);			
			Start cst = parser.parse();
			System.out.println("Konsistenzprüfung");
			LAttrAstGenerator astgen = new LAttrAstGenerator(oclModel);
			Heritage hrtg = new Heritage();
			oclModel.beginTrans(true);
			cst.apply(astgen, hrtg);
			oclModel.endTrans(false);			
		}
		else
			System.out.println("Please initialize the model first!");
	}
	
	/**
	 * Deletes the models of all OCLChecker instances.
	 */
	protected void finalize() 
	{
		Iterator it = instances.values().iterator();
		while (it.hasNext())
			((OCLChecker)it.next()).deleteModel();	
	}
	
	/**
	 * Deletes the element in the MDR assoziated to the given object.
	 */
	public void deleteElements(Object refObject) 
	{
		ModelFacade instance = ModelFacade.getInstance(this.model.refMofId());
		instance.deleteElements(refObject);
	}
	
	/**
	 * Deletes the model of the OCLChecker instance.
	 */
	private void deleteModel() 
	{
		mm.beginTrans(true);
			if (this.model != null)
				mm.deleteModel(model);
		mm.endTrans(false);
	}
	
	/**
	 * Returns an unique name for a new model.
	 */
	private String getUniqueName()
	{
		try 
		{
			boolean found = false;
			Collection col = mm.getAllModelNames();
			String name = "WorkingModel";
			int i = col.size()+1;
			while (true)
			{
				name = "WorkingModel" + i;
				if (!col.contains(name))
					return name;
				i++;
			}			
		} 
		catch (ModelManagerException e) 
		{
			e.printStackTrace();
			return "";
		}
		
	}
}
