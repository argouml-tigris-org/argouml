// $Id$
/*******************************************************************************
 * Copyright (c) 2007-2012 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial implementation
 *    Bogdan Pistol - undo support & UUID maps for diagrams
 *    thn
 *****************************************************************************/

package org.argouml.model.euml;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.model.NotImplementedException;
import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EStructuralFeatureImpl.BasicFeatureMapEntry;
import org.eclipse.emf.ecore.impl.EStructuralFeatureImpl.SimpleFeatureMapEntry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.uml2.uml.Element;
import org.xml.sax.InputSource;

/**
 * The implementation of the XmiReader for EUML2.
 */
class XmiReaderEUMLImpl implements XmiReader {

    private static final Logger LOG =
        Logger.getLogger(XmiReaderEUMLImpl.class.getName());

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    private static Set<String> searchDirs = new HashSet<String>();

    private Resource resource;

    /**
     * Constructor.
     *
     * @param implementation
     *            The ModelImplementation.
     */
    public XmiReaderEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public int getIgnoredElementCount() {
        return 0;
    }

    public String[] getIgnoredElements() {
        // Not needed currently for UML 2
        return new String[0];
    }

    public Map<String, Object> getXMIUUIDToObjectMap() {
        if (resource == null) {
            throw new IllegalStateException();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<EObject> it = resource.getAllContents();
        while (it.hasNext()) {
            EObject o  = it.next();
            map.put(resource.getURIFragment(o), o);
        }
        return map;
    }


    public Collection parse(InputSource inputSource, boolean readOnly)
        throws UmlException {

        if (inputSource == null) {
            throw new NullPointerException(
                    "The input source must be non-null."); //$NON-NLS-1$
        }
        InputStream is = null;
        boolean needsClosing = false;
        String name = inputSource.getSystemId();
        if (name == null) {
            name = inputSource.getPublicId();
        }
        if (name == null) {
            name = inputSource.toString();
        }
        LOG.log(Level.FINE, "Parsing {0}", name); //$NON-NLS-1$

        if (inputSource.getByteStream() != null) {
            is = inputSource.getByteStream();
        } else if (inputSource.getSystemId() != null) {
            try {
                URL url = new URL(inputSource.getSystemId());
                if (url != null) {
                    LOG.log(Level.FINE, "Parsing URL {0}", url); //$NON-NLS-1$
                    is = url.openStream();
                    if (is != null) {
                        is = new BufferedInputStream(is);
                        needsClosing = true;
                    }
                }
            } catch (MalformedURLException e) {
                // do nothing
            } catch (IOException e) {
                // do nothing
            }

        }
        if (is == null) {
            throw new UnsupportedOperationException();
        }

        // TODO: Review - priority of public ID vs system ID has been reversed
        // from original implementation
        String id = inputSource.getPublicId();
        if (id == null) {
            id = inputSource.getSystemId();
            if (id != null) {
                // we only take the filename, not the whole system path
                int ix = id.lastIndexOf('/');
                if (ix != -1) {
                    id = id.substring(ix + 1);
                }
            }
        }

        Resource r = UMLUtil.getResource(modelImpl,
                URI.createURI(id), readOnly);

        try {
            modelImpl.getModelEventPump().stopPumpingEvents();
            r.unload();
            r.load(is, null);
            // TODO: Some import-only UML 2 profiles trigger this - Investigate.
//            if (!isUML2(r)) {
//                throw new UmlException("Attempted to load non-UML 2.x file");
//            }
            if (isUML14(r)) {
                throw new UmlException(
                        "Attempted to load UML 1.4 file"); //$NON-NLS-1$
            }
        } catch (IOException e) {
            throw new UmlException(e);
        } finally {
            modelImpl.getModelEventPump().startPumpingEvents();
            if (needsClosing) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        resource = r;
        LOG.log(Level.FINE,
                "Parsed resource " + resource  //$NON-NLS-1$
                + " with " + resource.getContents().size() //$NON-NLS-1$
                + " elements"); //$NON-NLS-1$
        return r.getContents();
    }

    /**
     * Test whether this is a UML2 file. Returns as soon as the first UML2
     * element is seen.
     *
     * @param r resource containing loaded UML model
     * @return true if any of the contained objects are instances of UML2
     *         Element.
     */
    private boolean isUML2(Resource r) {
        for (EObject eobj : r.getContents()) {
            if (eobj instanceof Element) {
                return true;
            }
        }
        return false;
    }

    /**
     * Attempt to detect ArgoUML/NetBeans MDR style UML 1.4 XMI files. These can
     * be loaded without complaint by EMF, but they won't do us any good.
     *
     * @param r resource containing the loaded file
     * @return true if XMI.header/XMI.metamodel contains xmi.name="UML" and
     *         xmi.version="1.4"
     */
    private boolean isUML14(Resource r) {
        for (EObject eobj : r.getContents()) {
            if ("XMI.header".equals(eobj.eClass().getName())) {
                for (Entry e1 : ((AnyType) eobj).getMixed()) {
                    if (e1 instanceof BasicFeatureMapEntry) {
                        BasicFeatureMapEntry x1 = (BasicFeatureMapEntry) e1;
                        String n1 = x1.getEStructuralFeature().getName();
                        if ("XMI.metamodel".equals(n1)) {
                            AnyType v = (AnyType) x1.getValue();
                            for (Entry e2 : v.getAnyAttribute()) {
                                if (e2 instanceof SimpleFeatureMapEntry) {
                                    SimpleFeatureMapEntry x = (SimpleFeatureMapEntry) e2;
                                    String n = x.getEStructuralFeature().getName();
                                    if ("xmi.name".equals(n)) {
                                        if (!("UML".equals((String) x.getValue()))) {
                                            LOG.log(Level.WARNING,
                                                    "Tried to parse XMI file with "
                                                     + "XMI.header/XMI.metamodel/xmi.name = "
                                                     + (String) x.getValue());
                                            return false;
                                        }
                                    } else if ("xmi.version".equals(n)) {
                                        String version = (String) x.getValue();
                                        if (version != null
                                                && version.startsWith("1.4")) {
                                            LOG.log(Level.FINE,
                                                    "Tried to parse XMI file with "
                                                    + "XMI.header/XMI.metamodel/xmi.version = "
                                                    + version);
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean setIgnoredElements(String[] elementNames) {
        if (elementNames == null) {
            return true;
        }
        throw new NotImplementedException(
                "setIgnoredElements not implemented for UML 2.x");
        // TODO: Silently ignore instead?
//        return false;
    }

    public String getTagName() {
        if (resource == null) {
            return "uml:Model"; //$NON-NLS-1$
        }
        List l = resource.getContents();
        if (!l.isEmpty()) {
            return "uml:" //$NON-NLS-1$
                    + modelImpl.getMetaTypes().getName(l.get(0));
        } else {
            return null;
        }
    }

    public void addSearchPath(String path) {
        searchDirs.add(path);
    }

    public List<String> getSearchPath() {
        return new ArrayList<String>(searchDirs);
    }

    public void removeSearchPath(String path) {
        searchDirs.remove(path);
    }

    public String getHeader() {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();
    }

}
