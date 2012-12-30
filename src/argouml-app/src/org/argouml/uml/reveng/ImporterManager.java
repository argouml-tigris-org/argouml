/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2007 The Regents of the University of California. All
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

package org.argouml.uml.reveng;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Keeps an instance of each ImportInterface implementation module registered.
 * ImporterManager is a singleton.
 */
public final class ImporterManager {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ImporterManager.class.getName());

    /**
     * The instance.
     */
    private static final ImporterManager INSTANCE =
        new ImporterManager();

    /**
     * @return The singleton instance of the importer manager.
     */
    public static ImporterManager getInstance() {
        return INSTANCE;
    }

    private Set<ImportInterface> importers = new HashSet<ImportInterface>();

    /**
     * The constructor.
     */
    private ImporterManager() {
        // private constructor to enforce singleton
    }


    /**
     * Register a new source language importer.
     *
     * @param importer The ImportInterface object to register.
     */
    public void addImporter(ImportInterface importer) {
        importers.add(importer);
//        ArgoEventPump.fireEvent(
//                new ArgoImporterEvent(ArgoEventTypes.IMPORTER_ADDED, gen));
        LOG.log(Level.FINE, "Added importer {0}", importer );
    }

    /**
     * Removes a importer.
     *
     * @param importer
     *            the importer to be removed
     *
     * @return false if no matching importer had been registered
     */
    public boolean removeImporter(ImportInterface importer) {
        boolean status = importers.remove(importer);
//        if (status) {
//            ArgoEventPump.fireEvent(
//                    new ArgoImporterEvent(
//                            ArgoEventTypes.IMPORTER_REMOVED, old));
//        }
        LOG.log(Level.FINE, "Removed importer {0}", importer );
        return status;
    }

    /**
     * @return A copy of the set of importers.
     */
    public Set<ImportInterface> getImporters() {
        return Collections.unmodifiableSet(importers);
    }


    /**
     * @return true, if at least one importer exists.
     */
    public boolean hasImporters() {
        return !importers.isEmpty();
    }
}
