/**
 * Class for writing UML models into a MySQL database
 */

package uci.sql;

import java.sql.*;
import java.io.*;
import java.util.Properties;

import uci.uml.util.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;

public class DBWriter
{
    String DBUrl = "jdbc:mysql://";
    String DBName = "";
    String configFile = null;
    Properties props = null;
    static Connection Conn = null;

    //  default constructor, reads config file and connects to db.
    public DBWriter ()
    {
	props = new Properties();
	configFile =  System.getProperty("argo.dbconfig", "/uci/sql/db.ini");
	try {
	    InputStream is = DBWriter.class.getResourceAsStream(configFile);
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

	try {
	    Class.forName("org.gjt.mm.mysql.Driver").newInstance();	    
	}
	catch (Exception e) {
	    System.out.println("Could not load the database driver!");
	    System.out.println(e);
	}
	try {
	    Conn = DriverManager.getConnection(DBUrl);
	}
	catch (Exception e) {
	    System.out.println("Could not connect to database!");
	    System.out.println(e);
	}
    }

    public void store(MModel model) {
	
	Statement stmt = null;
	// this is TEMP CODE until UUIDs are set when obj is created !!!!!
	UUIDManager.SINGLETON.createModelUUIDS((MNamespace)model);

	try {
	    System.out.println("Writing model: "+model.getName());
	    stmt = Conn.createStatement();
		store(model,stmt);
	}
	catch (SQLException E) {
	    System.out.println("error while executing!");
	    System.out.println(E);
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

	private void store(MModel model, Statement stmt) throws SQLException {
		stmt.executeUpdate("REPLACE INTO tModel (uuid) VALUES ('" +model.getUUID()+ "')");
		stmt.executeUpdate("REPLACE INTO tModelElement (uuid, name) VALUES ('" +model.getUUID()+ "','"+model.getName()+"')");
	}
		

    public void storeClass(MClass cls) throws SQLException {
    }

    public static void main(String[] Args) throws Exception {
	MModel mymodel = new MModelImpl();
	DBWriter writer = new DBWriter();
    }
};
