package org.argouml.util;

public class Tools {

    static String packageList[] = new String[]{"org.argouml.application","ru.novosoft.uml","org.tigris.gef.base","org.xml.sax","java.lang"};

    public static String getVersionInfo()
    {
      try{

	// class preloading, so packages are there...
	Class cls = Class.forName("ru.novosoft.uml.MBase");
	cls = Class.forName("org.tigris.gef.base.Editor");
	cls = Class.forName("ru.novosoft.uml.MBase");
	cls = Class.forName("org.xml.sax.AttributeList");

	String in = "";
	StringBuffer sb = new StringBuffer();
	for(int i=0;i<packageList.length;i++)
	    {
		sb.append("Package: ");
		sb.append(packageList[i]);
		sb.append('\n');
		Package pkg = Package.getPackage(packageList[i]);
		if(pkg == null)
		    {
			sb.append("-- No Versioning Information --\nMaybe you don't use the jar?\n\n");
			continue;
		    }
		in = pkg.getImplementationTitle();
		if(in!=null)
		    {
			sb.append("Component: ");
			sb.append(in);
		    }
		in = pkg.getImplementationVendor();
		if(in!=null)
		    {
			sb.append(", by: ");
			sb.append(in);
		    }
		in = pkg.getImplementationVersion();
		if(in!=null)
		    {
			sb.append(", version: ");
			sb.append(in);
			sb.append('\n');
		    }
		sb.append('\n');
	    }

	sb.append("Operation System is: ");
	sb.append(System.getProperty("os.name", "unknown"));
	sb.append('\n');
	sb.append("Operation System Version: ");
	sb.append(System.getProperty("os.version", "unknown"));
	sb.append('\n');
	sb.append("Language: ");
	sb.append(System.getProperty("user.language", "unknown"));
	sb.append('\n');
	sb.append("Region: ");
	sb.append(System.getProperty("user.region", "unknown"));
	sb.append('\n');


	String saxFactory = System.getProperty("javax.xml.parsers.SAXParserFactory");
	if(saxFactory != null) {
	    System.out.println("SAX Parser Factory " + saxFactory+ " specified using system property\n");
	}
	try {
	    System.out.println("SAX Parser Factory " +
			       javax.xml.parsers.SAXParserFactory.newInstance().getClass().getName() + " will be used.\n");
	}
	catch(Exception ex) {
	    System.out.println("Error determining SAX Parser Factory\n.");
	}


	return sb.toString();

      } catch (Exception e) { return e.toString();}

    }
}
