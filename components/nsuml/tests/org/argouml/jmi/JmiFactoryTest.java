package org.argouml.jmi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.MalformedXMIException;
import javax.jmi.xmi.XmiReader;
import javax.jmi.xmi.XmiWriter;
import javax.naming.Context;
import javax.naming.InitialContext;

public class JmiFactoryTest {

	public static void main(String[] args) {
		JmiFactoryTest t = new JmiFactoryTest();
		t.loadModel("file:example.xmi", "uml13");
	}
	
	public void loadModel(String metamodel, String uri) {
		try {
			// Obtain the JmiFactory from JNDI
			Context initCtx = new InitialContext();

			// Look up the metamodel uml13
			JmiFactory jf = (JmiFactory) initCtx.lookup("jmi:" + metamodel);

			XmiReader xmiReader = jf.getXmiReader();
			RefPackage extent = jf.getTopLevelPackage();
			InputStream is = null;
			try {
				is = new FileInputStream(new File(uri));
			} catch (FileNotFoundException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			try {
				xmiReader.read(is, uri, extent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedXMIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            XmiWriter xmiWriter = jf.getXmiWriter();
            OutputStream os = null;
			try {
				os = new FileOutputStream(new File("changed-example.xml"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            try {
				xmiWriter.write(os, extent, "2.0");
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
            

		} //try
		catch (javax.naming.NamingException ne) {
			ne.printStackTrace();
		}
	}
}

