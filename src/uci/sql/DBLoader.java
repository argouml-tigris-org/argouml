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
			if (rs.next())
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

		//rs = stmt.executeQuery("SELECT * FROM tModelElement WHERE namespace = '" + modelUUID + "'");
		rs = stmt.executeQuery("SELECT * FROM tModelElement");

		// first, load all classifiers
		while (rs.next()) {
			if (rs.getString("UMLClassName").equals("Interface"))
				readInterface(model, rs.getString("uuid"), rs.getString("name"), rs.getString("namespace"), rs.getString("stereotype"), rs.getString("package"));
			if (rs.getString("UMLClassName").equals("Class"))
				readClass(model, rs.getString("uuid"), rs.getString("name"), rs.getString("namespace"), rs.getString("stereotype"), rs.getString("package"));
			if (rs.getString("UMLClassName").equals("DataType"))
				readDataType(rs.getString("uuid"), rs.getString("name"));
		}

		// now add attributes and operations to classifiers
		Iterator iter = uuid2element.values().iterator();
		while (iter.hasNext()) {
			MClassifier cls = (MClassifier)iter.next();
			addAttributes(cls, cls.getUUID());
			addOperations(cls, cls.getUUID());
		}

		// last not least add relationships
		

		return model;
	}

	private MStereotype readStereotype(String uuid) {return null;}

	private void readClass(MModel model, String classUUID, String name, String ns, String stereotypeUUID, String packageUUID) throws SQLException {

		MClass cls = new MClassImpl();
		cls.setName(name);
		cls.setUUID(classUUID);
		if (ns.equals(model.getUUID()))
			cls.setNamespace(model);
		cls.setStereotype(readStereotype(stereotypeUUID));

		uuid2element.put(classUUID, cls);		
	}

	private void readInterface(MModel model, String interfaceUUID, String name, String ns, String stereotypeUUID, String packageUUID) throws SQLException {

		MInterface me = new MInterfaceImpl();
		me.setName(name);
		me.setUUID(interfaceUUID);
		if (ns.equals(model.getUUID()))
			me.setNamespace(model);
		me.setStereotype(readStereotype(stereotypeUUID));

		uuid2element.put(interfaceUUID, me);
	}

	private void readDataType(String dtUUID, String name) {

		MDataType me = new MDataTypeImpl();
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
			MAttribute attr = new MAttributeImpl();
			attr.setUUID(attributes.getString("uuid"));
			if (! attributes.getString("ownerScope").equals("-1"))
				attr.setOwnerScope(MScopeKind.forValue(Integer.parseInt(attributes.getString("ownerScope"))));
			if (! attributes.getString("visibility").equals("-1"))
				attr.setVisibility(MVisibilityKind.forValue(Integer.parseInt(attributes.getString("visibility"))));
			if (! attributes.getString("multiplicity").equals("-1"))
				attr.setMultiplicity(new MMultiplicity(attributes.getString("multiplicity")));
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
			MOperation oper = new MOperationImpl();
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
			MParameter param = new MParameterImpl();
			param.setUUID(parameters.getString("uuid"));
			if (! ((parameters.getString("defaultValue") == null) || (parameters.getString("defaultValue").equals(""))))
				param.setDefaultValue(new MExpression("",parameters.getString("defaultValue")));
			if (! parameters.getString("kind").equals("-1"))
				param.setKind(MParameterDirectionKind.forValue(Integer.parseInt(parameters.getString("kind"))));
			param.setType((MClassifier)uuid2element.get(parameters.getString("type")));
			param.setName(parameters.getString("name"));
			param.setNamespace((MNamespace)uuid2element.get(parameters.getString("namespace")));

			oper.addParameter(param);
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

	/** Don't use main(), it's only for testing! */
    public static void main(String[] Args) throws Exception {
		MModel mymodel = new MModelImpl();
		DBLoader writer = new DBLoader();
    }
};
