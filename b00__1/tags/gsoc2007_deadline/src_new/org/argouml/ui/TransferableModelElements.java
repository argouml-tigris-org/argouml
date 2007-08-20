// $Id:TransferableModelElements.java 11510 2006-11-24 07:37:59Z tfmorris $
// Copyright (c) 2005-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
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

package org.argouml.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Collection;

/**
 * A transferable wraps the data that is transferred
 * (in casu a collection of UML modelelements)
 * from a drag source to a drop target.
 * The initiator of a drag wraps data in a transferable,
 * and drops are handled by accessing a transferable's data.
 */
public class TransferableModelElements implements Transferable {

    /**
     * The data flavor we use for collections of UML elements.
     */
    public static final DataFlavor UML_COLLECTION_FLAVOR =
        new DataFlavor(Collection.class, "UML ModelElements Collection");

    private static DataFlavor[] flavors = {UML_COLLECTION_FLAVOR };

    private Collection theModelElements;

    /**
     * The constructor.
     *
     * @param data the collection of UML elements
     */
    public TransferableModelElements(Collection data) {

        theModelElements = data;
    }

    /*
     * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
     */
    public Object getTransferData(DataFlavor dataFlavor)
        throws UnsupportedFlavorException,
               IOException {

        if (dataFlavor.match(UML_COLLECTION_FLAVOR)) {
            return theModelElements;
        }
        /*
         * TODO: We could also support other flavors here,
         * e.g. image (then you can drag modelelements directly into
         * your wordprocessor, to be inserted as an image).
         */
        throw new UnsupportedFlavorException(dataFlavor);
    }

    /*
     * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
     */
    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    /*
     * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
     */
    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {

        return dataFlavor.match(UML_COLLECTION_FLAVOR);
    }

}
