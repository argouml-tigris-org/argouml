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

import java.util.Hashtable;
//import java.util.Hashtable;

/**
 * @author Jim Holy
 */

public abstract class XMLTokenTableBase {

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected  Hashtable _tokens       = null;
  protected  boolean   _dbg          = false;
  protected  String    _openTags[]   = new String[100];
  protected  int       _openTokens[] = new int[100];
  protected  int       _numOpen      = 0;


  ////////////////////////////////////////////////////////////////
  // constructors

  public XMLTokenTableBase(int tableSize) {
    _tokens = new Hashtable(tableSize);
    setupTokens();
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public final int toToken(String s, boolean push) {
    if (push) _openTags[++_numOpen] = s;
    else if (s.equals(_openTags[_numOpen])) {
      //System.out.println("matched: " + s);
      return _openTokens[_numOpen--];
    }
    Integer i = (Integer) _tokens.get(s);
    if (i != null) {
      _openTokens[_numOpen] = i.intValue();
      return _openTokens[_numOpen];
    }
    else return -1;
  }

  public void    setDbg(boolean dbg)     { _dbg = dbg; }
  public boolean getDbg()                { return _dbg; }

  ////////////////////////////////////////////////////////////////
  // class methods

  protected void addToken(String s, Integer i) {
    boolean error = false;
    if (_dbg) {
      if (_tokens.contains(i) || _tokens.containsKey(s)) {
	System.out.println("ERROR: token table already contains " + s);
	error = true;
      }
    }
    _tokens.put(s,i);
    if (_dbg && !error) {
      System.out.println("NOTE: added '" + s + "' to token table");
    }
  }

  protected abstract void setupTokens();

} /* end class XMLTokenTableBase */
