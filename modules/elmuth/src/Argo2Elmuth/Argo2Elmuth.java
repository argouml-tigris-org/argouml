package Argo2Elmuth;

import java.io.IOException;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Enumeration;


public class Argo2Elmuth {
	private final String DISPLETS_LIB_DIR = "elmuth_files/DispletsLib";
	private final String MENU_IMAGES_DIR = "elmuth_files/images/menu";
	private String ElmuthArchive;
	private String projectPath;
	private String projectName;
	private String error = "";

     /**
	* Constructor.
	*/
	public Argo2Elmuth(String ElmuthArchive,String projectPath, String projectName) {
		this.ElmuthArchive = ElmuthArchive;
		this.projectPath = projectPath;
		this.projectName = projectName;
	}

     /**
	* sets some global variables
	*/	
	public void loadConfiguration() {
		System.setProperty("DISPLETS_LIB_DIR",DISPLETS_LIB_DIR);
		System.setProperty("MENU_IMAGES_DIR",MENU_IMAGES_DIR);
		System.setProperty("JAR_FILE_NAME",ElmuthArchive);
		System.setProperty("projectName",projectName);
		System.setProperty("projectPath",projectPath);
		System.setProperty("projectFullPath",projectPath+System.getProperty("file.separator")+projectName);
	}

     /**
	* Main class: exports an ArgoUML project into Elmuth
	*/		
	public boolean exportToWeb() {
		System.out.print("Loading Elmuth configuration...");
		loadConfiguration();
		System.out.println("done.");		
		try {
			System.out.print("Setting up displets environment...");
			DispletEnvironment de = new DispletEnvironment();
			de.prepareDirs();
			de.copyFiles();
			System.out.println("done.");

			
			XMLGenerator xmlGenerator = new XMLGenerator();
			
			xmlGenerator.openJar();
			System.out.print("Creating XML document for data dictionary...");
			FileOutputStream XMLDictionary = new FileOutputStream(System.getProperty("projectFullPath")+System.getProperty("file.separator")+"xml"+System.getProperty("file.separator")+"dictionary.xml");
			xmlGenerator.makeDictionary(XMLDictionary);
			XMLDictionary.close();
			System.out.println("done.");
			xmlGenerator.closeJar();
		
			xmlGenerator.openJar();
			System.out.print("Creating XML document for code generator...");
			FileOutputStream XMLCode = new FileOutputStream(System.getProperty("projectFullPath")+System.getProperty("file.separator")+"xml"+System.getProperty("file.separator")+"code.xml");
			xmlGenerator.makeCode(XMLCode);
			XMLCode.close();
			System.out.println("done.");
			xmlGenerator.closeJar();
		
			System.out.print("Creating XML documents for menu and svg displet...");
			xmlGenerator.openJar();
			Hashtable diagramsInfo = xmlGenerator.makeMenuAndSVG();
			for (Enumeration en=diagramsInfo.keys(); en.hasMoreElements();) {
				xmlGenerator.addXlinks(((Integer)en.nextElement()).intValue(),diagramsInfo);
			}
			xmlGenerator.closeJar();
			System.out.println("done.");
			
			System.out.print("Creating HTML documents...");
			HTMLGenerator hg = new HTMLGenerator();
			hg.computeHeights(diagramsInfo);
			hg.openJar();
			util.copyString(hg.makeIndex(),System.getProperty("projectFullPath")+System.getProperty("file.separator")+"index.html");
			util.copyString(hg.makeDictionary(),System.getProperty("projectFullPath")+System.getProperty("file.separator")+"dictionary.html");
			util.copyString(hg.makeCode(),System.getProperty("projectFullPath")+System.getProperty("file.separator")+"code.html");
			util.copyString(hg.makeMenu(),System.getProperty("projectFullPath")+System.getProperty("file.separator")+"menu0.html");
			util.copyString(hg.makeModel(),System.getProperty("projectFullPath")+System.getProperty("file.separator")+"diagram0.html");
			for (Enumeration en=diagramsInfo.keys(); en.hasMoreElements();) {
				Integer key = (Integer)en.nextElement();
				util.copyString(hg.makeHtml(key.intValue()),System.getProperty("projectFullPath")+System.getProperty("file.separator")+"html"+key+".html");
			}
			hg.closeJar();
			System.out.println("done.");
		}
		catch (Exception exc) {
			error = exc.getMessage();
			System.out.println("got an Exception");
			//exc.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String getErrorMessage() {
		return error;
	}
}