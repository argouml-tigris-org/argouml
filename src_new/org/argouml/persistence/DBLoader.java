package org.argouml.persistence;

import org.argouml.model.uml.UmlFactory;

import java.sql.*;
import java.io.*;
import java.util.Properties;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import java.util.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.use_cases.*;



/**
 * This class contains the functionality to load a model from a
 * mysql database. At the moment the DB must have the name "uml",
 * otherwise it won't work. Additionally, you need a "db.ini" file
 * in the Argo root-directory or as parameter -Dargo.dbconfig="c:\db.ini"
 *
 * Model information for static structure and use case diagrams can now be stored
 * to a mySQL database. The adaption to any other JDBC capable database should be
 * easy by adjusting the DB-scheme, since ArgoUML contains clean JDBC code. Graphical
 * information is not supported yet.
 *
 * @author Toby Baier <Toby.Baier@gmx.net>
 * @version 1.0
 */

public class DBLoader
{
    String DBUrl = "jdbc:mysql://";
    String DBName = "";
    String configFile = null;
    Properties props = null;
    static Connection Conn = null;

	HashMap uuid2element = new HashMap();
	HashMap uuid2ascend = new HashMap();

    //  default constructor, reads config file and connects to db.
    public DBLoader ()
    {
	props = new Properties();
	configFile =  System.getProperty("argo.dbconfig", "/db.ini");
	try {
	    InputStream is = new FileInputStream(configFile);
	    props.load(is);
	}	
	catch (IOException e) {
	    System.out.println("Could not load DB properties from " + configFile);
	    System.out.println(e);
	    errorMessage("Could not load DB properties from " + configFile, e);
	}

	try {
	    Class.forName(props.getProperty("driver")).newInstance();	    
	}
	catch (Exception e) {
	    System.out.println("Could not load the database driver!");
	    System.out.println(e);
	    errorMessage("Could not load the database driver!",e);
	}

	String dbURL = "jdbc:mysql://";
	dbURL += props.getProperty("host") + "/";
	dbURL += props.getProperty("db");
	String dbUser = props.getProperty("user");
	String dbPassword = props.getProperty("password");
	String dbConnectFormat = props.getProperty("dbConnectFormat");


 
	try {
	    if (dbConnectFormat.equals("1")) {
		Conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
	    } else if (dbConnectFormat.equals("2")) {
		Conn = DriverManager.getConnection(dbURL + "?user=" + dbUser + ";password=" + dbPassword);
	    } else if (dbConnectFormat.equals("3")) {
		Properties connprops = new Properties();
		connprops.put("user", dbUser);
		connprops.put("password", dbPassword);
		Conn = DriverManager.getConnection(dbURL, connprops);
	    } else if (dbConnectFormat.equals("4")) {
		Conn = DriverManager.getConnection(dbURL + "?user="
                                                + dbUser + "&password=" + dbPassword);
	    } else {
		errorMessage("Unknown dbConnectFormat choice:" + dbConnectFormat, null);
	    }
	}

	catch (Exception e) {
	    System.out.println("Could not connect to database!");
	    System.out.println(e);
	    errorMessage("Could not connect to database!",e);
	}
    }

    /** test whether we can use this DBLoader */
    public boolean hasConnection() {
	return (Conn != null);
    }
	
    private void errorMessage(String msg, Exception e) {
	JOptionPane.showMessageDialog(null, msg, "Database error", JOptionPane.ERROR_MESSAGE);
    }
	/**
	 * This method is called from ActionLoadModelFromDb. It's the only 
	 * public method, the only one you actually need to call to get a MModel
	 *
	 * @param modelName The name of the model you want to read from the database
	 * @return the constructed model
	 */
    public MModel read(String modelName) {
		
		ResultSet rs = null;
		Statement stmt = null;
		String uuid = "";
		
		try {
			System.out.println("Loading model: "+modelName);
			stmt = Conn.createStatement();
            rs = stmt.executeQuery("SELECT uuid FROM tModelElement WHERE name = '" + modelName + "'");
			if (rs.next())
				uuid = rs.getString("uuid");

			return readModel(uuid, modelName, stmt);
		}
		catch (SQLException E) {
			System.out.println("error while executing!");
			System.out.println(E);
			errorMessage("Error while loading the model from the database!",E);
		}
		finally {
			if (rs != null) { try {rs.close();} catch (SQLException SQLE) {} }	    
			if (stmt != null) {	try {stmt.close();} catch (SQLException SQLE) {}}
			if (Conn != null) {	try {Conn.close();}	catch (SQLException SQLE) {}}
		}
		return null;
    }

	private MModel readModel(String modelUUID, String modelName, Statement stmt) throws SQLException {
		System.out.println("Loading model with uuid: "+ modelUUID);

		MModel model = UmlFactory.getFactory().getModelManagement().createModel();
		model.setName(modelName);
		model.setUUID(modelUUID);

		ResultSet rs = null;

		//rs = stmt.executeQuery("SELECT * FROM tModelElement WHERE namespace = '" + modelUUID + "'");
		rs = stmt.executeQuery("SELECT * FROM tModelElement");

		// first, load all classifiers
		while (rs.next()) {
			if (rs.getString("UMLClassName").equals("Interface"))
				readInterface(model, rs.getString("uuid"), rs.getString("name"), rs.getString("namespace"), rs.getString("stereotype"), rs.getString("package"));
			if (rs.getString("UMLClassName").equals("Class"))
				readClass(model, rs.getString("uuid"), rs.getString("name"), rs.getString("namespace"), rs.getString("stereotype"), rs.getString("package"));
			if (rs.getString("UMLClassName").equals("Actor"))
				readActor(model, rs.getString("uuid"), rs.getString("name"), rs.getString("namespace"), rs.getString("stereotype"), rs.getString("package"));
			if (rs.getString("UMLClassName").equals("UseCase"))
				readUseCase(model, rs.getString("uuid"), rs.getString("name"), rs.getString("namespace"), rs.getString("stereotype"), rs.getString("package"));
			if (rs.getString("UMLClassName").equals("DataType"))
				readDataType(rs.getString("uuid"), rs.getString("name"));
		}

		// now add attributes, operations and constraints to classifiers
		Iterator iter = uuid2element.values().iterator();
		while (iter.hasNext()) {
			MClassifier cls = (MClassifier)iter.next();
			addAttributes(cls, cls.getUUID());
			addOperations(cls, cls.getUUID());
		}

		// now construct the AssociationEnds, so the associations can be 
		// built more easily
		rs = stmt.executeQuery("SELECT * FROM tModelElement");
			while (rs.next()) {
			if (rs.getString("UMLClassName").equals("AssociationEnd"))
				readAssociationEnd(model, rs.getString("uuid"), rs.getString("name"), rs.getString("namespace"), rs.getString("stereotype"), rs.getString("package"));
		}

		// last not least add relationships
		rs = stmt.executeQuery("SELECT * FROM tModelElement");
			while (rs.next()) {
			if (rs.getString("UMLClassName").equals("Association"))
				readAssociation(model, rs.getString("uuid"), rs.getString("name"), rs.getString("namespace"), rs.getString("stereotype"), rs.getString("package"));
			if (rs.getString("UMLClassName").equals("Generalization"))
				readGeneralization(model, rs.getString("uuid"), rs.getString("name"), rs.getString("namespace"), rs.getString("stereotype"), rs.getString("package"));
			if (rs.getString("UMLClassName").equals("Abstraction"))
				readAbstraction(model, rs.getString("uuid"), rs.getString("name"), rs.getString("namespace"), rs.getString("stereotype"), rs.getString("package"));
			if (rs.getString("UMLClassName").equals("Usage"))
				readUsage(model, rs.getString("uuid"), rs.getString("name"), rs.getString("namespace"), rs.getString("stereotype"), rs.getString("package"));
		}

		return model;
	}

	private MStereotype readStereotype(String uuid) {return null;}

	private void readClass(MModel model, String classUUID, String name, String ns, String stereotypeUUID, String packageUUID) throws SQLException {

		MClass cls = UmlFactory.getFactory().getCore().createClass();
		cls.setName(name);
		cls.setUUID(classUUID);
		if (ns.equals(model.getUUID()))
			cls.setNamespace(model);
		cls.setStereotype(readStereotype(stereotypeUUID));

		uuid2element.put(classUUID, cls);		
	}

	private void readInterface(MModel model, String interfaceUUID, String name, String ns, String stereotypeUUID, String packageUUID) throws SQLException {

		MInterface me = UmlFactory.getFactory().getCore().createInterface();
		me.setName(name);
		me.setUUID(interfaceUUID);
		if (ns.equals(model.getUUID()))
			me.setNamespace(model);
		me.setStereotype(readStereotype(stereotypeUUID));

		uuid2element.put(interfaceUUID, me);
	}

	private void readActor(MModel model, String actorUUID, String name, String ns, String stereotypeUUID, String packageUUID) throws SQLException {

		MActor actor = UmlFactory.getFactory().getUseCases().createActor();
		actor.setName(name);
		actor.setUUID(actorUUID);
		if (ns.equals(model.getUUID()))
			actor.setNamespace(model);
		actor.setStereotype(readStereotype(stereotypeUUID));

		uuid2element.put(actorUUID, actor);		
	}

	private void readUseCase(MModel model, String ucUUID, String name, String ns, String stereotypeUUID, String packageUUID) throws SQLException {

		MUseCase usecase = UmlFactory.getFactory().getUseCases().createUseCase();
		usecase.setName(name);
		usecase.setUUID(ucUUID);
		if (ns.equals(model.getUUID()))
			usecase.setNamespace(model);
		usecase.setStereotype(readStereotype(stereotypeUUID));

		uuid2element.put(ucUUID, usecase);		
	}

	private void readDataType(String dtUUID, String name) {

		MDataType me = UmlFactory.getFactory().getCore().createDataType();
		me.setName(name);
		me.setUUID(dtUUID);

		uuid2element.put(dtUUID, me);
	}

	private void addAttributes(MClassifier me, String uuid) throws SQLException{
		
		String query = "SELECT f.uuid, f.ownerScope, f.visibility, ";
		query += "sf.multiplicity, sf.changeability, sf.targetScope, sf.type, ";
		query += "me.name, me.namespace ";
		query += "FROM tFeature f, tStructuralFeature sf, tModelElement me ";
		query += "WHERE f.owner = '" + uuid +"' ";
		query += "AND f.uuid = sf.uuid ";
		query += "AND f.uuid = me.uuid";
		Statement stmtCl = Conn.createStatement();
		ResultSet attributes = stmtCl.executeQuery(query);
		while (attributes.next()) {
			MAttribute attr = UmlFactory.getFactory().getCore().createAttribute();
			attr.setUUID(attributes.getString("uuid"));
			if (! attributes.getString("ownerScope").equals("-1"))
				attr.setOwnerScope(MScopeKind.forValue(Integer.parseInt(attributes.getString("ownerScope"))));
			if (! attributes.getString("visibility").equals("-1"))
				attr.setVisibility(MVisibilityKind.forValue(Integer.parseInt(attributes.getString("visibility"))));
			if (! attributes.getString("multiplicity").equals("-1"))
				attr.setMultiplicity(UmlFactory.getFactory().getDataTypes().createMultiplicity(attributes.getString("multiplicity")));
			if (! attributes.getString("changeability").equals("-1"))
				attr.setChangeability(MChangeableKind.forValue(Integer.parseInt(attributes.getString("changeablibity"))));
			if (! attributes.getString("targetScope").equals("-1"))
				attr.setTargetScope(MScopeKind.forValue(Integer.parseInt(attributes.getString("targetScope"))));
			attr.setType((MClassifier)uuid2element.get(attributes.getString("type")));
			attr.setName(attributes.getString("name"));
			attr.setNamespace((MNamespace)uuid2element.get(attributes.getString("namespace")));
			me.addFeature(attr);
		}
	}

	private void addOperations(MClassifier me, String uuid) throws SQLException{
		
		String query = "SELECT f.uuid, f.ownerScope, f.visibility, ";
		query += "bf.isQuery, ";
		query += "me.name, me.namespace ";
		query += "FROM tFeature f, tBehavioralFeature bf, tModelElement me ";
		query += "WHERE f.owner = '" + uuid +"' ";
		query += "AND f.uuid = bf.uuid ";
		query += "AND f.uuid = me.uuid";
		Statement stmtCl = Conn.createStatement();
		ResultSet operations = stmtCl.executeQuery(query);
		while (operations.next()) {
			MOperation oper = UmlFactory.getFactory().getCore().createOperation();
			oper.setUUID(operations.getString("uuid"));
			if (! operations.getString("ownerScope").equals("-1"))
				oper.setOwnerScope(MScopeKind.forValue(Integer.parseInt(operations.getString("ownerScope"))));
			if (! operations.getString("visibility").equals("-1"))
				oper.setVisibility(MVisibilityKind.forValue(Integer.parseInt(operations.getString("visibility"))));
			oper.setQuery(operations.getBoolean("isQuery"));
			oper.setName(operations.getString("name"));
			oper.setNamespace((MNamespace)uuid2element.get(operations.getString("namespace")));

			addParameters(oper);

			me.addFeature(oper);
		}
	}

	private void addParameters(MBehavioralFeature oper) throws SQLException{
			
		String query = "SELECT p.uuid, p.defaultValue, p.kind, p.type, ";
		query += "me.name, me.namespace ";
		query += "FROM tParameter p, tModelElement me ";
		query += "WHERE p.behavioralFeature = '" + oper.getUUID() +"' ";
		query += "AND p.uuid = me.uuid";
		Statement stmtCl = Conn.createStatement();
		ResultSet parameters = stmtCl.executeQuery(query);
		while (parameters.next()) {
			MParameter param = UmlFactory.getFactory().getCore().createParameter();
			param.setUUID(parameters.getString("uuid"));
			if (! ((parameters.getString("defaultValue") == null) || (parameters.getString("defaultValue").equals(""))))
				param.setDefaultValue(UmlFactory.getFactory().getDataTypes().createExpression("",parameters.getString("defaultValue")));
			if (! parameters.getString("kind").equals("-1"))
				param.setKind(MParameterDirectionKind.forValue(Integer.parseInt(parameters.getString("kind"))));
			param.setType((MClassifier)uuid2element.get(parameters.getString("type")));
			param.setName(parameters.getString("name"));
			param.setNamespace((MNamespace)uuid2element.get(parameters.getString("namespace")));

			oper.addParameter(param);
		}
	}	

	private void addConstraints(MClassifier me, String uuid) throws SQLException{
		
	    String query = "SELECT c.uuid, c.body, ";
		query += "me.name, me.namespace ";
		query += "FROM tConstraint t, tModelElement me ";
		query += "WHERE t.constrainedElement = '" + uuid +"'";

		Statement stmtCl = Conn.createStatement();
		ResultSet constraints = stmtCl.executeQuery(query);
		while (constraints.next()) {
			MConstraint constraint = UmlFactory.getFactory().getCore().createConstraint();
			constraint.setUUID(constraints.getString("uuid"));
			if (! constraints.getString("body").equals(""))
				constraint.setBody(UmlFactory.getFactory().getDataTypes().createBooleanExpression("OCL", constraints.getString("body")));
			constraint.setName(constraints.getString("name"));
			constraint.setNamespace((MNamespace)uuid2element.get(constraints.getString("namespace")));

			me.addConstraint(constraint);
		}
	}

	private void read(MGeneralizableElement me, String uuid) throws SQLException{
		String query = "SELECT * FROM tGeneralizableElement WHERE uuid = '" + uuid +"'";
		Statement stmtGE = Conn.createStatement();
		ResultSet rsGE = stmtGE.executeQuery(query);
		if (rsGE.next()) {
			me.setRoot(rsGE.getBoolean("isRoot"));
			me.setLeaf(rsGE.getBoolean("isLeaf"));
			me.setAbstract(rsGE.getBoolean("isAbstract"));
		}
	}

	//
	// Here starts the relationship handling
	//

	private void readAssociationEnd(MModel model, String UUID, String name, String ns, String stereotypeUUID, String packageUUID) throws SQLException {
	
		MAssociationEnd ae = UmlFactory.getFactory().getCore().createAssociationEnd();
		MClassifier type = null;
		ae.setName(name);
		ae.setUUID(UUID);
		if (ns.equals(model.getUUID()))
			ae.setNamespace(model);
		ae.setStereotype(readStereotype(stereotypeUUID));

		String query = "SELECT ae.isNavigable, ae.ordering, ae.aggregation, ";
		query += "ae.targetScope, ae.multiplicity, ae.changeability, ae.visibility, ae.type ";
		query += "FROM tAssociationEnd ae ";
		query += "WHERE ae.uuid = '"+UUID+"'";

		Statement stmtAE = Conn.createStatement();
		ResultSet rsAE = stmtAE.executeQuery(query);
		if (rsAE.next()) {
			ae.setNavigable(rsAE.getBoolean("isNavigable"));
			if (! rsAE.getString("ordering").equals("-1"))
				ae.setOrdering(MOrderingKind.forValue(Integer.parseInt(rsAE.getString("ordering"))));
			if (! rsAE.getString("aggregation").equals("-1"))
				ae.setAggregation(MAggregationKind.forValue(Integer.parseInt(rsAE.getString("aggregation"))));
			if (! rsAE.getString("targetScope").equals("-1"))
				ae.setTargetScope(MScopeKind.forValue(Integer.parseInt(rsAE.getString("targetScope"))));
			if (! rsAE.getString("multiplicity").equals("-1"))
				ae.setMultiplicity(UmlFactory.getFactory().getDataTypes().createMultiplicity(rsAE.getString("multiplicity")));
			if (! rsAE.getString("changeability").equals("-1"))
				ae.setChangeability(MChangeableKind.forValue(Integer.parseInt(rsAE.getString("changeability"))));
			if (! rsAE.getString("visibility").equals("-1"))
				ae.setVisibility(MVisibilityKind.forValue(Integer.parseInt(rsAE.getString("visibility"))));
			if (rsAE.getString("type") != null)
				type = (MClassifier)uuid2element.get(rsAE.getString("type"));
			if (type != null)
				ae.setType(type);
		}

		uuid2ascend.put(UUID, ae);
	}

	private void readAssociation(MModel model, String UUID, String name, String ns, String stereotypeUUID, String packageUUID) throws SQLException {
	
		MAssociation me = UmlFactory.getFactory().getCore().createAssociation();
		MAssociationEnd ae1 = null;
		MAssociationEnd ae2 = null;

		me.setName(name);
		me.setUUID(UUID);
		if (ns.equals(model.getUUID()))
			me.setNamespace(model);
		me.setStereotype(readStereotype(stereotypeUUID));

		String query = "SELECT * ";
		query += "FROM tAssociation ";
		query += "WHERE uuid = '"+UUID+"'";

		Statement stmtA = Conn.createStatement();
		ResultSet rsA = stmtA.executeQuery(query);
		if (rsA.next()) {
			ae1 = (MAssociationEnd)uuid2ascend.get(rsA.getString("connection1"));
			me.addConnection(ae1);
			ae2 = (MAssociationEnd)uuid2ascend.get(rsA.getString("connection2"));
			me.addConnection(ae2);
		}
	}

	private void readGeneralization(MModel model, String UUID, String name, String ns, String stereotypeUUID, String packageUUID) throws SQLException {
	
		MGeneralization me = UmlFactory.getFactory().getCore().createGeneralization();
		MGeneralizableElement parent = null;
		MGeneralizableElement child = null;
		me.setName(name);
		me.setUUID(UUID);
		if (ns.equals(model.getUUID()))
			me.setNamespace(model);
		me.setStereotype(readStereotype(stereotypeUUID));

		String query = "SELECT * FROM tGeneralization WHERE uuid = '" + UUID +"'";
		Statement stmtG = Conn.createStatement();
		ResultSet rsG = stmtG.executeQuery(query);

		if (rsG.next()) {
			parent = (MGeneralizableElement)uuid2element.get(rsG.getString("parent"));
			child = (MGeneralizableElement)uuid2element.get(rsG.getString("child"));
		}

		if (parent != null) me.setParent(parent);
		// System.out.println("Parent: "+parent);
		if (child != null) me.setChild(child);
		// System.out.println("Child: "+child);
	}

	private void readAbstraction(MModel model, String UUID, String name, String ns, String stereotypeUUID, String packageUUID) throws SQLException {

		MAbstraction me = UmlFactory.getFactory().getCore().createAbstraction();
		me.setName(name);
		me.setUUID(UUID);
		if (ns.equals(model.getUUID()))
			me.setNamespace(model);
		me.setStereotype(readStereotype(stereotypeUUID));

		readDependency(me, UUID);
	}

	private void readUsage(MModel model, String UUID, String name, String ns, String stereotypeUUID, String packageUUID) throws SQLException {
	
		MUsage me = UmlFactory.getFactory().getCore().createUsage();
		me.setName(name);
		me.setUUID(UUID);
		if (ns.equals(model.getUUID()))
			me.setNamespace(model);
		me.setStereotype(readStereotype(stereotypeUUID));

		readDependency(me, UUID);
	}

	private void readDependency(MDependency dep, String UUID) throws SQLException {

		MModelElement supplier = null;
		MModelElement client = null;

		String query = "SELECT * FROM tDependency WHERE uuid = '" + UUID +"'";
		Statement stmtD = Conn.createStatement();
		ResultSet rsD = stmtD.executeQuery(query);

		if (rsD.next()) {
			supplier = (MModelElement)uuid2element.get(rsD.getString("supplier"));
			client = (MModelElement)uuid2element.get(rsD.getString("client"));
		}
		
		dep.addClient(client);
		dep.addSupplier(supplier);
	}

	/** Don't use main(), it's only for testing! */
    public static void main(String[] Args) throws Exception {
		MModel mymodel = UmlFactory.getFactory().getModelManagement().createModel();
		DBLoader writer = new DBLoader();
    }
}
