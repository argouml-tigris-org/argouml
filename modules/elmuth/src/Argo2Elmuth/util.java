/**
 * util.java
 * @author  Paolo Pinciani (pinciani@cs.unibo.it)
 *
 * Copyright 2001-2002 Department of Computer Science
 * University of Bologna
 * Mura Anteo Zamboni 7, 40127 Bologna, ITALY
 * Tel: +39 051 35.45.16
 * Fax: +39 051 35.45.10
 * Web: http://cs.unibo.it
 */
package Argo2Elmuth;

import java.io.*;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.zip.*;

/**
 * <code>util</code> <code>static</code> class provides some generic services, like file copying, file deleting, zip entry reading, and others.
 */
public class util {
	
	
   /**
	* Deletes a directory.
	* @param <code>dir</code> the direcotry to delete.
	*/
	public static void deleteDir(File dir) throws IOException {
		String[] files = dir.list();
		for(int i=0; i<files.length; i++) {
			File f = new File(dir.getPath()+System.getProperty("file.separator")+files[i]);
			if (f.isDirectory())
				deleteDir(f);
			else
				if (!f.delete()) throw new IOException("error deleting file " + f.getName() + ".\nTry deleting by hand before proceeding");
		}
		if (!dir.delete()) throw new IOException("error deleting directory " + dir.getName() + ".\nTry deleting by hand before proceeding");
	}

   /**
	* Copies two files identified by two String.
	* @param <code>fromPath</code> Origin path.
	* @param <code>toPath</code> Destination path.
	*/	
	public static void copyFile(String fromPath, String toPath) throws IOException {
		File fromFile = new File(fromPath);
		File toFile = new File(toPath);
		if (toFile.exists())
			toFile.delete();
		byte[] buffer = new byte[512];
		int size = 0;	
		FileInputStream	in = new FileInputStream(fromFile);
		FileOutputStream out = new FileOutputStream(toFile);
		while ((size = in.read(buffer)) != -1)
			out.write(buffer,0,size);
		in.close();
		out.close();	
	}

   /**
	* Copies an <code>InputStream</code> into an <code>OutputStream</code>
	* @param <code>in</code> The <code>InputStream</code>
	* @param <code>out</code> The <code>OutputStream</code>
	*/		
	public static void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[512];
		int size = 0;
		while ((size = in.read(buffer)) != -1)
			out.write(buffer,0,size);
		in.close();
		out.close();	
	}

   /**
	* Copies an input <code>String</code> into an output <code>File</code>
	* @param <code>inputString</code> The input <code>String</code>
	* @param <code>outputFile</code> The output <code>File</code>
	*/		
	public static void copyString(String inputString, String outputFile) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
		File toFile = new File(outputFile);
		FileOutputStream out = new FileOutputStream(toFile);
		copyInputStream(in,out);
	}

   /**
	* Search a zip entry into a zip file. Returns <code>null</code> if the zip entry does not exist.
	* @param <code>zipFile</code> The zip file
	* @param <code>name</code> The entry name
	* @return <code>zipEntry</code> The zipEntry as an <code>InputStream</code>
	*/	
	public static InputStream findZipEntry(ZipFile zipFile, String name) throws IOException {
		for (Enumeration e=zipFile.entries(); e.hasMoreElements();) {
			ZipEntry entry = (ZipEntry)e.nextElement();
			if (entry.getName().equals(name))
				return zipFile.getInputStream(entry);
		}
		return null;
	}

   /**
	* Deletes the DTD reference from an XML document;
	* @param <code>XMLInput</code> The XML document as an <code>InputStream</code>
	* @return <code>XMLDocument</code> The new XML document
	*/		
	public static InputStream deleteDTD(InputStream XMLInput) throws IOException {
		ByteArrayOutputStream XMLWriterOut = new ByteArrayOutputStream();
		PrintWriter XMLOutput = new PrintWriter(XMLWriterOut);
		ByteArrayOutputStream OutDoc = new ByteArrayOutputStream();
		util.copyInputStream(XMLInput,OutDoc);
		String XMLDocument = OutDoc.toString();
		StringTokenizer st = new StringTokenizer(XMLDocument,"\r\n");
		String token;
		boolean found = false;
		while (st.hasMoreTokens()) {
			token = st.nextToken();
			if (!found) {
				if(!token.trim().startsWith("<!DOCTYPE"))
					XMLOutput.println(token);
				if (token.trim().startsWith("<pgml"))
					found = true;
			}	
			else
				XMLOutput.println(token);
		}
		if (XMLOutput.checkError())
			throw new IOException("java.io.PrintWriter error");
		XMLOutput.close();
		return new ByteArrayInputStream(XMLWriterOut.toByteArray());
	}
}