/**
 * Class for readng UML models from a MySQL database
 */

package uci.sql;

import java.sql.*;
import java.io.*;
import java.util.Properties;

import com.sun.java.util.collections.*;

import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.use_cases.*;

public class DBLoader
{
    String DBUrl = "jdbc:mysql://";
    String DBName = "";
    String configFile = null;
    Properties props = null;
    static Connection Conn = null;

	HashMap uuid2element = new HashMap();

    //  default constructor, reads config file and connects to db.
    public DBLoader ()
    {
	props = new Properties();
	configFile =  System.getProperty("argo.dbconfig", "/db.ini");
	try {
	    InputStream is = DBLoader.class.getResourceAsStream(configFile);
	    props.load(is);
	}	
	catch (IOException e) {
	    System.out.println("Could not load DB properties from uci/sql/db.ini");
	    System.out.println(e);
	}

	DBName = props.getProperty("db");
	DBUrl += props.getProperty("host") + "/";
	DBUrl += DBName;
	DBUrl += "?" + "user=" + props.getProperty("user");
	DBUrl += "&" + "password=" + props.getProperty("password");

	try { Class.forName("org.gjt.mm.mysql.Driver").newInstance();}
	catch (Exception e) {
	    System.out.println("Could not load the database driver!");
	    System.out.println(e);
	}
	try {Conn = DriverManager.getConnection(DBUrl);}
	catch (SQLException e) {
	    System.out.println("Could not connect to database!");
	    System.out.println(e.getMessage());
	    System.out.println(e.getSQLState());
	}
    }
	
    public MModel read(String modelName) {
		
		ResultSet rs = null;
		Statement stmt = null;
		String uuid = "";
		
		try {
			System.out.println("Loading model: "+modelName);
			stmt = Conn.createStatement();
            rs = stmt.executeQuery("SELECT uuid FROM tModelElement WHERE name = '" + modelName + "'");
			if (rs.first())
				uuid = rs.getString("uuid");

			return readModel(uuid, modelName, stmt);
		}
		catch (SQLException E) {
			System.out.println("error while executing!");
			System.out.println(E);
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

		MModel model = new MModelImpl();
		model.setName(modelName);
		model.setUUID(modelUUID);

		ResultSet rs = null;

		rs = stmt.executeQuery("SELECT * FROM tModelElement WHERE namespace = '" + modelUUID + "'");

		// first, add all classes, interfaces, use cases and actors to the model
		while (rs.next()) {
			if (rs.getString("UMLClassName").equals("Interface"))
				readInterface(rs.getString("uuid"), rs.getString("name"), model, rs.getString("stereotype"), rs.getString("package"));
			if (rs.getString("UMLClassName").equals("Class"))
				readClass(rs.getString("uuid"), rs.getString("name"), model, rs.getString("stereotype"), rs.getString("package"), stmt);
		}

		// now add attributes and operations to classifiers
		Iterator iter = uuid2element.values().iterator();
		while (iter.hasNext()) {
			MClassifier cls = (MClassifier)iter.next();
			addAttributes(cls, cls.getUUID());
			//addOperations(cls);
		}

		// last not least add relationships
		

		return model;
	}

	private MStereotype readStereotype(String uuid) {return null;}

	private void readClass(String classUUID, String name, MNamespace ns, String stereotypeUUID, String packageUUID, Statement stmt) throws SQLException {

		MClass cls = new MClassImpl();
		cls.setName(name);
		cls.setUUID(classUUID);
		cls.setNamespace(ns);
		cls.setStereotype(readStereotype(stereotypeUUID));

		uuid2element.put(classUUID, cls);		
	}

	private void readInterface(String interfaceUUID, String name, MNamespace ns, String stereotypeUUID, String packageUUID) throws SQLException {

		MInterface me = new MInterfaceImpl();
		me.setName(name);
		me.setUUID(interfaceUUID);
		me.setNamespace(ns);
		me.setStereotype(readStereotype(stereotypeUUID));

		uuid2element.put(interfaceUUID, me);
	}

	private void addAttributes(MClassifier me, String uuid) throws SQLException{

		MAttribute attr = new MAttributeImpl();
		
		String query = "SELECT f.ownerScope, f.visibility, ";
		query += "sf.multiplicity, sf.changeability, sf.targetScope, sf.type ";
		query += "FROM tFeature f, tStructuralFeature sf ";
		query += "WHERE f.owner = '" + uuid +"' ";
		query += "AND f.uuid = sf.uuid";
		Statement stmtCl = Conn.createStatement();
		ResultSet attributes = stmtCl.executeQuery(query);
		if (attributes.first()) {
			if (! attributes.getString("ownerScope").equals("-1"))
				attr.setOwnerScope(MScopeKind.forValue(Integer.parseInt(attributes.getString("ownerScope"))));
			if (! attributes.getString("visibility").equals("-1"))
				attr.setVisibility(MVisibilityKind.forValue(Integer.parseInt(attributes.getString("visibility"))));
			if (! attributes.getString("multiplicity").equals("-1"))
				attr.setMultiplicity(new MMultiplicity(attributes.getString("multiplicity")));
			if (! attributes.getString("changeablibity").equals("-1"))
				attr.setChangeability(MChangeableKind.forValue(Integer.parseInt(attributes.getString("changeablibity"))));
			if (! attributes.getString("targetScope").equals("-1"))
				attr.setTargetScope(MScopeKind.forValue(Integer.parseInt(attributes.getString("targetScope"))));
			attr.setType((MClassifier)uuid2element.get(attributes.getString("type")));
		}
	}


	private void read(MGeneralizableElement me, String uuid) throws SQLException{
		String query = "SELECT * FROM tGeneralizableElement WHERE uuid = '" + uuid +"'";
		Statement stmtGE = Conn.createStatement();
		ResultSet rsGE = stmtGE.executeQuery(query);
		if (rsGE.first()) {
			me.setRoot(rsGE.getBoolean("isRoot"));
			me.setLeaf(rsGE.getBoolean("isLeaf"));
			me.setAbstract(rsGE.getBoolean("isAbstract"));
		}
	}

    public static void main(String[] Args) throws Exception {
		MModel mymodel = new MModelImpl();
		DBLoader writer = new DBLoader();
    }
};
