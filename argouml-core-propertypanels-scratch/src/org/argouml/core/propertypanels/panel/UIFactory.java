// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.core.propertypanels.panel;

import java.io.DataInputStream;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.argouml.core.propertypanels.xml.XMLPropertyPanelsHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

// this import is weird, but it will be removed (just a workaround
// until the problem with loading XMLs from jars is solved.
import com.sun.org.apache.bcel.internal.util.ByteSequence;

// TODO: This class will be an interface or abstract class
// and will be implemented by SwingUIFactory and SwtUIFactory.
public class UIFactory {
    
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(UIFactory.class);
    
    private static UIFactory INSTANCE = new UIFactory();
    
    public UIFactory() {
        
    }
    
    public static UIFactory getInstance() {        
        return INSTANCE;
    }
    
    // TODO: This will be a template method, where there will be two
    // implementations, for Swing and for SWT.
    public JPanel createGUI (Object target) throws Exception {
        String filename = getXMLFileName(target);
        LOG.info("[XMLPP] filename is:" + filename);
        JPanel panel = parseXML(filename);
        return panel;       
    }
    
    private String getXMLFileName(Object target) {
        String classname = target.getClass().getSimpleName();
        // TODO: I don't like this hack, it may exist a better way.
        return classname.replace("$Impl", "");
    }

    public JPanel parseXML(String filename) 
        throws Exception {
        JPanel panel = new JPanel();
        
        // TODO: I have to investigate how to read the XML.
        // There are some different APIs available, but
        // I'll choose SAX because it's the one API used in
        // PGML, so we don't have different APIs in Argo.
        XMLReader parser = XMLReaderFactory.createXMLReader(
                "org.apache.xerces.parsers.SAXParser"
              );
        parser.setContentHandler(new XMLPropertyPanelsHandler(panel));

        String file = "/org/argouml/core/propertypanels/xml/"
            + filename + ".xml";
        LOG.info("File = "+ file);
        // URL url = ClassLoader.getSystemResource(file);
        // ClassLoader loader = UIFactory.class.getClassLoader();
        // URL url = loader.getResource(file);
        // LOG.info("URL = "+ url.toString());
//        Enumeration<URL> urls = loader.getResources(file);        
//        while (urls.hasMoreElements()) {
//            URL u = (URL) urls.nextElement();
//            LOG.info("URL = "+ u.toString());
//        }
//        URL url = loader.getResource(file);
//        LOG.info("URL = "+ url.toString());
        DataInputStream stream = getStream();
//        FileInputStream stream = new FileInputStream(file);
        
        InputSource source = new InputSource(stream);
        parser.parse(source);        
        
        return panel; 
    }

    private DataInputStream getStream() {
        String x = "<?xml version='1.0' encoding='UTF-8'?><panel title='Prueba de titutlo'>     <label text='This is a test from XML' />        <property name='name' value='hey'       /></panel>";
        return new ByteSequence(x.getBytes());
    }
}
