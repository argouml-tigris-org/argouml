package org.argouml.persistence;

import java.sql.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import ru.novosoft.uml.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.use_cases.*;

import org.apache.log4j.Category;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlHelper;
import org.argouml.uml.*;

/**
 * This class contains the functionality to write a model into a
 * mysql database. At the moment the DB must have the name "uml",
 * otherwise it won't work. Additionally, you need a "db.ini" file
 * in the Argo root-directory or as parameter -Dargo.dbconfig="c:\db.ini"
 *
 * @author Toby Baier <Toby.Baier@gmx.net>
 * @version 1.0
 */

public class DBWriter
{
        protected static Category cat = Category.getInstance(DBWriter.class);
    String configFile = null;
    String stmtString = "";
    Properties props = null;
    static Connection Conn = null;

    /**
	 * The default constructor reads the config file (db.ini) and connects to the db.
	 */

    public DBWriter ()
    {
	props = new Properties();
	configFile =  System.getProperty("argo.dbconfig", "/db.ini");
	try {
	    InputStream is = new FileInputStream(configFile);
	    props.load(is);
	}	
	catch (IOException e) {
        cat.error("Could not load DB properties from " + configFile, e);
	    errorMessage("Could not load DB properties from " + configFile, e);
	}

	try {
	    Class.forName(props.getProperty("driver")).newInstance();	    
	}
	catch (Exception e) {
            cat.error("Could not load the database driver!", e);
	    errorMessage("Could not load the database driver!",e);
	}

	String dbURL = "jdbc:mysql://";
	dbURL += props.getProperty("host") + ":";
	dbURL += props.getProperty("port") + "/";
	dbURL += props.getProperty("db");
	String dbUser = props.getProperty("user");
	String dbPassword = props.getProperty("password");
	String dbConnectFormat = props.getProperty("dbConnectFormat");


 
	try {
	    if (dbConnectFormat.equals("1")) {
		Conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
	    } else if (dbConnectFormat.equals("2")) {
		String connectString = dbURL + "?user=" + dbUser + ";password=" + dbPassword;
		Conn = DriverManager.getConnection(connectString);
	    } else if (dbConnectFormat.equals("3")) {
		Properties connprops = new Properties();
		connprops.put("user", dbUser);
		connprops.put("password", dbPassword);
		Conn = DriverManager.getConnection(dbURL, connprops);
	    } else if (dbConnectFormat.equals("4")) {
		String connectString = dbURL + "?user=" + dbUser + "&password=" + dbPassword;
		Conn = DriverManager.getConnection(connectString);
	    } else {
		errorMessage("Unknown dbConnectFormat choice:" + dbConnectFormat, null);
	    }
	}

	catch (Exception e) {
            cat.error("Could not connect to database!", e);
	    errorMessage("Could not connect to database!",e);
	}
    }

    /** test whether we can use this DBWriter */
    public boolean hasConnection() {
	return (Conn != null);
    }


    private void errorMessage(String msg, Exception e) {
	JOptionPane.showMessageDialog(null, msg, "Database error", JOptionPane.ERROR_MESSAGE);
    }

	/**
	 * This method is called from org.argouml.ui.ActionStoreProjectToDb to store the current namespace (which should be a MModel) into the database.
	 *
	 * @param model This is the model which will be stored using its name.
	 */
    public void store(MModel model) {
	
	Statement stmt = null;
	// this is TEMP CODE until UUIDs are set when obj is created !!!!!
	UUIDManager.SINGLETON.createModelUUIDS((MNamespace)model);

	try {
	    cat.info("Storing model: "+model.getName() + " to database.");
		// this statement is reused in all the "store" methods
	    stmt = Conn.createStatement();
		store(model,stmt);
	}
	catch (Exception e) {
            cat.error("Error while storing the model to the database!", e);
	    errorMessage("Error while storing the model to the database!",e);
	}

	finally {
	    if (stmt != null) {
		try { stmt.close();}
		catch (SQLException SQLE) {}
	    }

	    if (Conn != null) {
		try { Conn.close();}
		catch (SQLException SQLE) {}
	    }
	}
    }

	// This method stores the model. While doing so it iterates throw its owned Elements
	// and uses the other private store() methods to store these.
	private void store(MModel model, Statement stmt) throws SQLException {

		// first store the model itself 
		stmtString = "REPLACE INTO tModel (uuid) VALUES ('";
		stmtString += model.getUUID()+ "')";
		stmt.executeUpdate(stmtString);

		store((MPackage)model, stmt);
	}

	private void store(MNamespace ns, Statement stmt) throws SQLException {
		stmtString = "REPLACE INTO tNamespace (uuid) VALUES ('";
		stmtString += ns.getUUID() + "')";
		stmt.executeUpdate(stmtString);

		// now iterate through the Namespace's owned elements and write them
		Iterator ownedElements = ns.getOwnedElements().iterator();
		while (ownedElements.hasNext()) {
			MModelElement me = (MModelElement)ownedElements.next();
			cat.debug("Processing " + me.toString());
			if (me instanceof MClass) store((MClass)me,stmt);
			if (me instanceof MInterface) store((MInterface)me,stmt);
			if (me instanceof MDataType) store((MDataType)me,stmt);
			if (me instanceof MAssociation) store((MAssociation)me,stmt);
			if (me instanceof MGeneralization) store((MGeneralization)me,stmt);
			if (me instanceof MUseCase) store((MUseCase)me,stmt);
			if (me instanceof MActor) store((MActor)me,stmt);
			if (me instanceof MAbstraction) store((MAbstraction)me,stmt);
			if (me instanceof MUsage) store((MUsage)me,stmt);
			if (me instanceof MBinding) store((MBinding)me,stmt);
			if (me instanceof MPermission) store((MPermission)me,stmt);
		}

		store((MModelElement)ns,stmt);
	}

	//
	// Here the uninteresting part starts. ;-)
	// There's one store() method for each modelelement I want to store,
	// so you can just call store(thisElement, myStatement) to store thisElement
	//

	private void store(MPackage me, Statement stmt) throws SQLException {
		stmtString = "REPLACE INTO tPackage (uuid) VALUES ('";
		stmtString += me.getUUID() + "')";
		stmt.executeUpdate(stmtString);

		store((MNamespace)me,stmt);
	}

	private void store(MModelElement me, Statement stmt) throws SQLException {
		boolean hasStereotype = false;
		stmtString = "REPLACE INTO tModelElement (uuid, name, namespace, stereotype, UMLClassName) VALUES ('";
		stmtString += me.getUUID() + "','";
		if (me.getName() != null)
			stmtString += me.getName() + "','" ;
		else stmtString += "','" ;
		if (me.getNamespace() != null) stmtString += me.getNamespace().getUUID() + "','";
		else stmtString += "','";
		if (me.getStereotype() != null) {
			stmtString += me.getStereotype().getUUID() + "','";
			hasStereotype = true;
		}
		else stmtString += "','";
		stmtString += me.getUMLClassName() + "')";

		stmt.executeUpdate(stmtString);
		if (hasStereotype) store((MStereotype)me.getStereotype(),stmt);

		Iterator constraints = me.getConstraints().iterator();
		while (constraints.hasNext()) {
		    store((MConstraint)constraints.next(),stmt);
		}

	}


    private void store(MConstraint me, Statement stmt) throws SQLException {
		stmtString = "REPLACE INTO tConstraint (uuid, body, constrainedElement) VALUES ('";
		stmtString += me.getUUID() + "','";
		if (me.getBody() != null) stmtString += me.getBody() + "','";
		else stmtString += "','";
		if (me.getConstrainedElement(0) != null) stmtString += me.getConstrainedElement(0).getUUID() + "')";
		else stmtString += "')";

		stmt.executeUpdate(stmtString);

		store((MModelElement)me,stmt);
	}


    private void store(MClass cls, Statement stmt) throws SQLException {
		String bool = "0";
		stmtString = "REPLACE INTO tClass (uuid, isActive) VALUES ('";
		stmtString += cls.getUUID()+ "', ";
		if (cls.isActive()) bool = "1"; else bool = "0";
		stmtString += bool + ")";
		stmt.executeUpdate(stmtString);

		store((MClassifier)cls, stmt);
    }

    private void store(MDataType cls, Statement stmt) throws SQLException {
		String bool = "0";
		stmtString = "REPLACE INTO tDataType (uuid) VALUES ('";
		stmtString += cls.getUUID()+ "')";
		stmt.executeUpdate(stmtString);

		store((MClassifier)cls, stmt);
    }

	private void store(MClassifier cls, Statement stmt) throws SQLException {		
		stmtString = "REPLACE INTO tClassifier (uuid) VALUES ('";
		stmtString += cls.getUUID()+ "')";
		stmt.executeUpdate(stmtString);

		store((MGeneralizableElement)cls, stmt);

		// in case the Classifier has attributes or operations, store these.
		Vector attributes = new Vector(UmlHelper.getHelper().getCore().getAttributes(cls));
		for ( int i = 0; i < attributes.size(); i++) store((MAttribute)attributes.elementAt(i),stmt);

		Vector operations = new Vector(UmlHelper.getHelper().getCore().getOperations(cls));
		for ( int i = 0; i < operations.size(); i++) store((MOperation)operations.elementAt(i),stmt);
    }

	private void store(MGeneralizableElement me, Statement stmt) throws SQLException {
		String bool = "0";
		stmtString = "REPLACE INTO tGeneralizableElement (uuid, isRoot, isLeaf, isAbstract) VALUES ('";
		stmtString += me.getUUID() + "', ";
		if (me.isRoot()) bool = "1"; else bool = "0";
		stmtString += bool + ", ";
		if (me.isLeaf()) bool = "1"; else bool = "0";
		stmtString += bool + ", ";
		if (me.isAbstract()) bool = "1"; else bool = "0";
		stmtString += bool + " )";
		stmt.executeUpdate(stmtString);

		store((MModelElement)me,stmt);
	}

	private void store(MAssociation me, Statement stmt) throws SQLException {
		boolean hasConnections = true;
		stmtString = "REPLACE INTO tAssociation (uuid, connection1, connection2) VALUES ('";
		stmtString += me.getUUID()+ "','";
		if (me.getConnection(0) != null) stmtString += me.getConnection(0).getUUID() + "','";
		else { stmtString += "','"; hasConnections = false;}
		if (me.getConnection(1) != null) stmtString += me.getConnection(1).getUUID() + "')";
		else { stmtString += "')"; hasConnections = false;}
		stmt.executeUpdate(stmtString);

		if (hasConnections) {
			store(me.getConnection(0),stmt);
			store(me.getConnection(1),stmt);
		}
		
		store((MModelElement)me, stmt);
    }

	private void store(MAssociationEnd me, Statement stmt) throws SQLException {
		String bool = "0";
		stmtString = "REPLACE INTO tAssociationEnd (uuid, isNavigable, ordering, aggregation, targetScope, multiplicity, changeability, visibility, type, association) VALUES ('";
		stmtString += me.getUUID() + "', ";
		if (me.isNavigable()) bool = "1"; else bool = "0";
		stmtString += bool + ", ";
		if (me.getOrdering() != null) stmtString += me.getOrdering().getValue() + ",";
		else stmtString += "-1,";
		if (me.getAggregation() != null) stmtString += me.getAggregation().getValue() + ",";
		else stmtString += "-1,";
		if (me.getTargetScope() != null) stmtString += me.getTargetScope().getValue() + ",'";
		else stmtString += "-1,'";
		if (me.getMultiplicity() != null) stmtString += me.getMultiplicity().toString() + "',";
		else stmtString += "-1',";
		if (me.getChangeability() != null) stmtString += me.getChangeability().getValue() + ",";
		else stmtString += "-1,";
		if (me.getVisibility() != null) stmtString += me.getVisibility().getValue() + ",";
		else stmtString += "-1,'";
		if (me.getType() != null) stmtString += me.getType().getUUID() + "','";
		else stmtString += "','";
		if (me.getAssociation() != null) stmtString += me.getAssociation().getUUID() + "')";
		else stmtString += "')";

		stmt.executeUpdate(stmtString);

		store((MModelElement)me,stmt);
	}

	private void store(MGeneralization me, Statement stmt) throws SQLException {
		stmtString = "REPLACE INTO tGeneralization (uuid, discriminator, parent, child) VALUES ('";
		stmtString += me.getUUID() + "','";
		stmtString += me.getDiscriminator() + "','" ;
		if (me.getParent() != null) stmtString += me.getParent().getUUID() + "','";
		else stmtString += "','";
		if (me.getChild() != null) stmtString += me.getChild().getUUID() + "')";
		else stmtString += "')";

		stmt.executeUpdate(stmtString);

		store((MModelElement)me,stmt);
	}

	private void store(MInterface me, Statement stmt) throws SQLException {		
		stmtString = "REPLACE INTO tInterface (uuid) VALUES ('";
		stmtString += me.getUUID()+ "')";
		stmt.executeUpdate(stmtString);

		store((MClassifier)me, stmt);
    }

	private void store(MUseCase me, Statement stmt) throws SQLException {		
		stmtString = "REPLACE INTO tUseCase (uuid) VALUES ('";
		stmtString += me.getUUID()+ "')";
		stmt.executeUpdate(stmtString);

		store((MClassifier)me, stmt);
    }

	private void store(MActor me, Statement stmt) throws SQLException {		
		stmtString = "REPLACE INTO tActor (uuid) VALUES ('";
		stmtString += me.getUUID()+ "')";
		stmt.executeUpdate(stmtString);

		store((MClassifier)me, stmt);
    }

	private void store(MDependency me, Statement stmt) throws SQLException {
		stmtString = "REPLACE INTO tDependency (uuid, supplier, client) VALUES ('";
		stmtString += me.getUUID() + "','";
		if (me.getSuppliers() != null) {
			Vector suppliers = new Vector(me.getSuppliers());
			MModelElement supplier = (MModelElement)suppliers.elementAt(0);
			stmtString += supplier.getUUID() + "','";
		}
		else stmtString += "','";
		if (me.getClients() != null) {
			Vector clients = new Vector(me.getClients());
			MModelElement client = (MModelElement)clients.elementAt(0);
			stmtString += client.getUUID() + "')";
		}
		else stmtString += "')";

		stmt.executeUpdate(stmtString);

		store((MModelElement)me,stmt);
	}

	private void store(MAbstraction me, Statement stmt) throws SQLException {
		stmtString = "REPLACE INTO tAbstraction (uuid, mapping) VALUES ('";
		stmtString += me.getUUID() + "','";
		if (me.getMapping() != null) stmtString += me.getMapping().getBody() + "')";
		else stmtString += "')";

		stmt.executeUpdate(stmtString);

		store((MDependency)me,stmt);
	}

	private void store(MStereotype me, Statement stmt) throws SQLException {
		stmtString = "REPLACE INTO tStereotype (uuid, icon, baseClass) VALUES ('";
		stmtString += me.getUUID() + "','";
		stmtString += me.getIcon() + "','";
		stmtString += me.getBaseClass() + "')";

		stmt.executeUpdate(stmtString);

		store((MGeneralizableElement)me,stmt);
	}

	private void store(MAttribute me, Statement stmt) throws SQLException {
		stmtString = "REPLACE INTO tAttribute (uuid, initialValue) VALUES ('";
		stmtString += me.getUUID() + "','";
		if (me.getInitialValue() != null) stmtString += me.getInitialValue().getBody() + "')";
		else stmtString += "')";

		stmt.executeUpdate(stmtString);

		store((MStructuralFeature)me,stmt);
	}

	private void store(MStructuralFeature me, Statement stmt) throws SQLException {
		stmtString = "REPLACE INTO tStructuralFeature (uuid, multiplicity, changeability, targetScope, type) VALUES ('";
		stmtString += me.getUUID() + "', '";
		if (me.getMultiplicity() != null) stmtString += me.getMultiplicity().toString() + "',";
		else stmtString += "-1',";
		if (me.getChangeability() != null) stmtString += me.getChangeability().getValue() + ",";
		else stmtString += "-1,";
		if (me.getTargetScope() != null) stmtString += me.getTargetScope().getValue() + ",'";
		else stmtString += "-1,'";
		if (me.getType() != null) stmtString += me.getType().getUUID() + "')";
		else stmtString += "')";

		stmt.executeUpdate(stmtString);

		MClassifier type = me.getType();
		if (!(type.getNamespace().equals(me.getNamespace())))
			store(type, stmt);

		store((MFeature)me,stmt);
	}

	private void store(MFeature me, Statement stmt) throws SQLException {
		stmtString = "REPLACE INTO tFeature (uuid, ownerScope, visibility, owner) VALUES ('";
		stmtString += me.getUUID() + "', ";
		if (me.getOwnerScope() != null) stmtString += me.getOwnerScope().getValue() + ",";
		else stmtString += "-1,";
		if (me.getVisibility() != null) stmtString += me.getVisibility().getValue() + ",'";
		else stmtString += "-1,'";
		if (me.getOwner() != null) stmtString += me.getOwner().getUUID() + "')";
		else stmtString += "')";		
		stmt.executeUpdate(stmtString);

		store((MModelElement)me,stmt);
	}

	private void store(MOperation me, Statement stmt) throws SQLException {
		String bool = "0";
		stmtString = "REPLACE INTO tOperation (uuid, concurrency, isRoot, isLeaf, isAbstract, specification) VALUES ('";
		stmtString += me.getUUID() + "', ";
		if (me.getConcurrency() != null) stmtString += me.getConcurrency().getValue() + ",";
		else stmtString += "-1,";
		if (me.isRoot()) bool = "1"; else bool = "0";
		stmtString += bool + ", ";
		if (me.isLeaf()) bool = "1"; else bool = "0";
		stmtString += bool + ", ";
		if (me.isAbstract()) bool = "1"; else bool = "0";
		stmtString += bool + " ,'";
		if (me.getSpecification() != null) stmtString += me.getSpecification() + "')";
		else stmtString += "')";		
		stmt.executeUpdate(stmtString);

		store((MBehavioralFeature)me,stmt);
	}

	private void store(MBehavioralFeature me, Statement stmt) throws SQLException {
		String bool = "0";
		stmtString = "REPLACE INTO tBehavioralFeature (uuid, isQuery) VALUES ('";
		stmtString += me.getUUID() + "', ";
		if (me.isQuery()) bool = "1"; else bool = "0";
		stmtString += bool + ")";
		stmt.executeUpdate(stmtString);

		store((MFeature)me,stmt);

		// in case the operation has parameters, store these.
		Vector parameters = new Vector(me.getParameters());
		for ( int i = 0; i < parameters.size(); i++) store((MParameter)parameters.elementAt(i),stmt);
	}

	private void store(MParameter me, Statement stmt) throws SQLException {
		if (me.getUUID() == null)
			me.setUUID(UUIDManager.SINGLETON.getNewUUID());
		stmtString = "REPLACE INTO tParameter (uuid, defaultValue, kind, behavioralFeature, type) VALUES ('";
		stmtString += me.getUUID() + "', '";
		if (me.getDefaultValue() != null) stmtString += me.getDefaultValue().getBody() + "',";
		else stmtString += "',";
		if (me.getKind() != null) stmtString += me.getKind().getValue() + ",'";
		else stmtString += "-1,'";
		if (me.getBehavioralFeature() != null) stmtString += me.getBehavioralFeature().getUUID() + "','";
		else stmtString += "','";
		if (me.getType() != null) stmtString += me.getType().getUUID() + "')";
		else stmtString += "')";	
		stmt.executeUpdate(stmtString);

		MClassifier type = me.getType();
		if (!(type.getNamespace().equals(me.getNamespace())))
			store(type, stmt);

		store((MModelElement)me,stmt);
	}		

	/**
	 * only for testing, do not use main!
	 */
    public static void main(String[] Args) throws Exception {
	MModel mymodel = UmlFactory.getFactory().getModelManagement().createModel();
	DBWriter writer = new DBWriter();
    } 
};
