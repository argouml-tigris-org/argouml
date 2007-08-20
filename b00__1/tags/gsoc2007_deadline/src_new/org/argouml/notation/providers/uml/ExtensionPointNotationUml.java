// $Id:ExtensionPointNotationUml.java 12850 2007-06-15 18:49:22Z mvw $
// Copyright (c) 2006-2007 The Regents of the University of California. All
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

package org.argouml.notation.providers.uml;

import java.util.HashMap;
import java.util.StringTokenizer;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.notation.providers.ExtensionPointNotation;

/**
 * The notation for an extension point for UML.
 * 
 * @author michiel
 */
public class ExtensionPointNotationUml extends ExtensionPointNotation {

    /**
     * The constructor.
     *
     * @param ep the represented Extension Point
     */
    public ExtensionPointNotationUml(Object ep) {
        super(ep);
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#parse(java.lang.Object, java.lang.String)
     */
    public void parse(Object modelElement, String text) {
        /* TODO: This try-catch will be needed 
         * once the code below is improved. */
//        try {
        parseExtensionPointFig(modelElement, text);
//        } catch (ParseException pe) {
//            String msg = "statusmsg.bar.error.parsing.extensionpoint";
//            Object[] args = {
//                pe.getLocalizedMessage(),
//                new Integer(pe.getErrorOffset()),
//            };
//            ProjectBrowser.getInstance().getStatusBar().showStatus(
//                    Translator.messageFormat(msg, args));
//        }
    }
    /**
     * Parse an extension point.<p>
     *
     * The syntax is "name: location", "name:", "location" or "". The fields of
     * the extension point are updated appropriately.
     *
     * @param ep      The extension point concerned
     * @param text    The text to parse
     */
    public void parseExtensionPointFig(Object ep, String text) {
        // We can do nothing if we don't have both the use case and extension
        // point.
        if (ep == null) {
            return;
        }
        Object useCase = Model.getFacade().getUseCase(ep);
        if (useCase == null) {
            return;
        }

        // Parse the string to creat a new extension point.
        Object newEp = parseExtensionPoint(text);

        // If we got back null we interpret this as meaning delete the
        // reference to the extension point from the use case, otherwise we set
        // the fields of the extension point to the values in newEp.
        if (newEp == null) {
            ProjectManager.getManager().getCurrentProject().moveToTrash(ep);
        } else {
            Model.getCoreHelper().setName(ep, Model.getFacade().getName(newEp));
            Model.getUseCasesHelper().setLocation(ep,
                    Model.getFacade().getLocation(newEp));
        }
        /* TODO: This needs more work! 
         * We simply throw the new extension point away? */
    }

    /**
     * Parse a string representing an extension point and return a new extension
     * point.<p>
     *
     * The syntax is "name: location", "name:", "location" or "".
     * <em>Note:</em> If either field is blank, it will be set to null
     * in the extension point.
     *
     * We break up the string into tokens at the ":". We must keep the ":" as a
     * token, so we can distinguish between "name:" and "location". The number
     * of tokens will distinguish our four cases.<p>
     *
     * @param text The string to parse
     *
     * @return A new extension point, with fields set appropriately, or
     *         <code>null</code> if we are given <code>null</code> or a
     *         blank string. <em>Note</em>. The string ":" can be used to set
     *         both name and location to null.
     */
    private Object parseExtensionPoint(String text) {

        // If we are given the null string, return immediately, 
        // so that the extensionpoint is removed.
        if (text == null) {
            return null;
        }

        // Build a new extension point

        // This method has insufficient information to call buildExtensionPoint.
        // Thus we'll need to create one, and pray that whomever called us knows
        // what kind of mess they got.
        Object ep =
            Model.getUseCasesFactory().createExtensionPoint();

        StringTokenizer st = new StringTokenizer(text.trim(), ":", true);
        int numTokens = st.countTokens();

        String epLocation;
        String epName;

        switch (numTokens) {

        case 0:

            // The empty string. Return null
            ep = null;

            break;

        case 1:

            // A string of the form "location". This will be confused by the
            // string ":", so we pick this out as an instruction to clear both
            // name and location.
            epLocation = st.nextToken().trim();

            if (epLocation.equals(":")) {
                Model.getCoreHelper().setName(ep, null);
                Model.getUseCasesHelper().setLocation(ep, null);
            } else {
                Model.getCoreHelper().setName(ep, null);
                Model.getUseCasesHelper().setLocation(ep, epLocation);
            }

            break;

        case 2:

            // A string of the form "name:"
            epName = st.nextToken().trim();

            Model.getCoreHelper().setName(ep, epName);
            Model.getUseCasesHelper().setLocation(ep, null);

            break;

        case 3:

            // A string of the form "name:location". Discard the middle token
            // (":")
            epName = st.nextToken().trim();
            st.nextToken(); // Read past the colon.
            epLocation = st.nextToken().trim();

            Model.getCoreHelper().setName(ep, epName);
            Model.getUseCasesHelper().setLocation(ep, epLocation);

            break;
        }

        return ep;
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.fig-extensionpoint";
    }

    /**
     * Generate the display for an extension point.<p>
     *
     * The representation is "name: location". 
     * The "name: " is omitted if there
     * is no name given.
     *
     * {@inheritDoc}
     */
    public String toString(Object modelElement, HashMap args) {
//        if (Model.getUmlFactory().isRemoved(modelElement)) {
//            /* This is a normal situation, 
//             * e.g. when an extensionpoint is removed by parsing, 
//             * see issue 4596. */
//            return "";
//        }

        if (modelElement == null) {
            return "";
        }

        // The string to build
        String s = "";

        // Get the fields we want
        String epName = Model.getFacade().getName(modelElement);
        String epLocation = Model.getFacade().getLocation(modelElement);

        // Put in the name field if it's there
        if ((epName != null) && (epName.length() > 0)) {
            s += epName + ": ";
        }

        // Put in the location field if it's there
        if ((epLocation != null) && (epLocation.length() > 0)) {
            s += epLocation;
        }

        return s;
    }

}
