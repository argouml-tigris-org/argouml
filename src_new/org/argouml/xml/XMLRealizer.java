// Copyright (c) 1996-99 The Regents of the University of California. All
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

package org.argouml.xml;

import org.w3c.dom.*;
import java.util.*;
//import java.util.*;

/**
 * @author Piotr Kaminski
 */
public class XMLRealizer {
  private final Node topNode;
  private RealizerFactory factory;
  private Hashtable cache = new Hashtable();
  private static final NodeRealizer NO_REALIZER = new NodeRealizerAdapter();

  public XMLRealizer(Node topNode) {
    this.topNode = topNode;
  }

  public synchronized void clearCache() {
    disableCache();
  }
  public synchronized void disableCache() {
    cache = null;
  }

  public synchronized void enableCache() {
    if (cache == null) cache = new Hashtable();
  }

  public boolean isCacheEnabled() {return cache != null;}


  public synchronized void realize(RealizerFactory factory) {
    this.factory = factory;
    recurse(topNode);
    this.factory = null;
  }

  protected void recurse(Node node) {
    // shouldn't need this, but the IBM parser occasionally
    // returns null children
    if (node == null) return;
    NodeRealizer r = null;
    String name = node.getNodeName();
    if (!isCacheEnabled()) r = factory.createRealizer(name);
    else {
      r = (NodeRealizer) cache.get(name);
      if (r == null) {
	r = factory.createRealizer(name);
	if (r == null) cache.put(name, NO_REALIZER);
	else cache.put(name, r);
      }
      else if (r == NO_REALIZER) r = null;
    }


    if (r != null) r.preRealize(node);
    NodeList nodes = node.getChildNodes();
    int s = nodes.getLength();
    for (int i = 0; i < s; i++) {
      recurse(nodes.item(i));
    }
    if (r != null) r.postRealize(node);
  }

} /* end class XMLRealizer */
