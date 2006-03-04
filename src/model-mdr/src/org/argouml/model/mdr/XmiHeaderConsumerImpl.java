// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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
package org.argouml.model.mdr;

import java.io.IOException;
import java.io.InputStream;

import org.netbeans.lib.jmi.xmi.XMIHeaderConsumer;

/**
 * This class is unused.  If it's ever reintroduced to determine
 * between UML 1.3 (not just Novosoft files) and UML 1.4 files
 * it needs to be changed to not be Novosoft specific (and not
 * scan the entire file)
 */
public class XmiHeaderConsumerImpl implements XMIHeaderConsumer {
    
    private boolean novosoft = false;
    
    public XmiHeaderConsumerImpl() {
        super();
    }

    public void consumeHeader(InputStream in) {
        StringBuffer sb = new StringBuffer();
        try {
            // TODO: I hope that this is not this which prevent the
            // repositioning of the stream. Haven't tried to reposition
            // without following lines commented - ludo
            byte[] buf = new byte[512];

            while (in.read(buf) > 0) {
                sb.append(new String(buf));
            }
            //TODO: Improve the test if needed
            if (sb.indexOf("Novosoft") > -1) {
                novosoft = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isNovosoft() {
        return novosoft;
    }
}
