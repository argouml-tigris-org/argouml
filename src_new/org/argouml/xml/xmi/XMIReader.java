package org.argouml.xml.xmi;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.argouml.application.api.Argo;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.model_management.MModel;

/**
 * Parses an XMI file. Extended from the NSUML XMIReader since this
 * reader does not handle errorhandling very well and is not very well
 * programmed at all. This led to issues loading xmi files and hanging ArgoUML
 * by doing that. 
 * 
 * @author Jaap Branderhorst
 * @see ru.novosoft.uml.xmi.XMIReader
 */
public class XMIReader extends ru.novosoft.uml.xmi.XMIReader {
    private boolean errors = false;
    private org.xml.sax.Parser parser = null;

    /**
     * Constructor for XMIReader.
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public XMIReader() throws SAXException, ParserConfigurationException {
        super();
        SAXParserFactory saxpf = SAXParserFactory.newInstance();
        saxpf.setValidating(false);
        saxpf.setNamespaceAware(false);

        setParser(saxpf.newSAXParser().getParser());

        getParser().setErrorHandler(this);
        getParser().setDocumentHandler(this);
        getParser().setEntityResolver(this);

    }

    /**
     * Constructor for XMIReader.
     * @param p_factory
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public XMIReader(MFactory p_factory)
        throws SAXException, ParserConfigurationException {
        super(p_factory);
        SAXParserFactory saxpf = SAXParserFactory.newInstance();
        saxpf.setValidating(false);
        saxpf.setNamespaceAware(false);

        setParser(saxpf.newSAXParser().getParser());

        getParser().setErrorHandler(this);
        getParser().setDocumentHandler(this);
        getParser().setEntityResolver(this);

    }

    /**
     * Parses an xmi inputsource. Sets errors to true if an exception is 
     * thrown. Could not change the API from the superclass. Therefore this
     * strange construction.
     * @see ru.novosoft.uml.xmi.XMIReader#parseStream(InputSource)
     */
    protected void parseStream(InputSource p_is) {

        cleanup();

        try {
            getParser().parse(p_is);
            performLinking();
        }
        catch (Exception ex) {
            Argo.log.error("The model file loaded is corrupted.");
            Argo.log.error(ex);
            setErrors(true);
        }

    }

    public void setErrors(boolean errors) {
        this.errors = errors;
    }

    public boolean getErrors() {
        return errors;
    }

    public void setParser(org.xml.sax.Parser parser) {
        this.parser = parser;
    }

    public org.xml.sax.Parser getParser() {
        return parser;
    }
}