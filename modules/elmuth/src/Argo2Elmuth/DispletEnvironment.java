/**
 * DispletEnvironment.java
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.Enumeration;

/**
 * <code>DispletEnvironment</code> class sets up the environment (main files and directories)
 * for a project to be exported into ELmuth.
 */
public class DispletEnvironment {

   /**
	* Constructor.
	*/
	public DispletEnvironment() { }
	
   /**
	* Creates all the needed directories.
	* Main directory has the name of the System property called "projectName".
	* Three subdirectories are created: "xml" (for xml documents), "lib" (for Elmuth libs) and "menu/images" (for storing menu images).
	*/
	public void prepareDirs() throws IOException {
		File projectDir = new File(System.getProperty("projectFullPath"));
		if (projectDir.exists()  &&  projectDir.isDirectory())
			util.deleteDir(projectDir);
		if (!projectDir.mkdir()) throw new IOException("error creating dir " + projectDir.getName());
		File d = new File(projectDir.getPath()+System.getProperty("file.separator")+"lib");
		if (!d.mkdir()) throw new IOException("error creating dir " + d.getName());
		d = new File(projectDir.getPath()+System.getProperty("file.separator")+"xml");
		if (!d.mkdir()) throw new IOException("error creating dir " + d.getName());
		d = new File(projectDir.getPath()+System.getProperty("file.separator")+"menu");
		if (!d.mkdir()) throw new IOException("error creating dir " + d.getName());
		d = new File(projectDir.getPath()+System.getProperty("file.separator")+"menu"+System.getProperty("file.separator")+"images");
		if (!d.mkdir()) throw new IOException("error creating dir " + d.getName());
	}

   /**
	* Copies some files, getting them from Argo2Elmuth jar archive: Elmuth libs, menu images and some static html files.
	*/
	public void copyFiles() throws IOException {
		ZipFile zipFile = new ZipFile(System.getProperty("JAR_FILE_NAME"));
		for (Enumeration e=zipFile.entries(); e.hasMoreElements();) {
			ZipEntry entry = (ZipEntry)e.nextElement();
			String entryName = entry.getName();
				if (entryName.startsWith(System.getProperty("DISPLETS_LIB_DIR"))  &&  entryName.endsWith(".jar")) {
				String jarName = entryName.substring(System.getProperty("DISPLETS_LIB_DIR").length()+1,entryName.length());
				FileOutputStream out = new FileOutputStream(System.getProperty("projectFullPath")+System.getProperty("file.separator")+"lib"+System.getProperty("file.separator")+jarName);
				util.copyInputStream(zipFile.getInputStream(entry),new BufferedOutputStream(out));
			}
			else if (entryName.endsWith(".html")  &&  !entryName.startsWith("elmuth_files/templates")) {
				FileOutputStream out = new FileOutputStream(System.getProperty("projectFullPath")+System.getProperty("file.separator")+entryName);
				util.copyInputStream(zipFile.getInputStream(entry),new BufferedOutputStream(out));	
			}
			else if (entryName.startsWith(System.getProperty("MENU_IMAGES_DIR"))  &&  entryName.endsWith(".gif")) {
				String gifName = entryName.substring(System.getProperty("MENU_IMAGES_DIR").length()+1,entryName.length());
				FileOutputStream out = new FileOutputStream(System.getProperty("projectFullPath")+System.getProperty("file.separator")+"menu"+System.getProperty("file.separator")+"images"+System.getProperty("file.separator")+gifName);			
				util.copyInputStream(zipFile.getInputStream(entry),new BufferedOutputStream(out));
			}

		}
		zipFile.close();
	}
}