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

package uci.uml.Foundation.Extension_Mechanisms;

import java.util.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.generate.*;


public class TaggedValue extends ElementImpl {
  public Name _tag;
  public Uninterpreted _value;

  public TaggedValue() { }
  public TaggedValue(Name tag, Uninterpreted value) {
    super(tag);  // a bit redundant
    setTag(tag);
    setValue(value);
  }
  public TaggedValue(String tagStr, String valueStr) {
    super(new Name(tagStr));  // a bit redundant
    setTag(getName());
    setValue(new Uninterpreted(valueStr));
  }

  public Name getTag() { return _tag; }
  public void setTag(Name x) { _tag = x; }

  public Uninterpreted getValue() { return _value; }
  public void setValue(Uninterpreted x) { _value = x; }

  ////////////////////////////////////////////////////////////////
  // debugging

  public String dbgString() {
    String s = getOCLTypeStr() + "{" +
      GeneratorDisplay.Generate(this) +
      "}";
    return s;
  }

  static final long serialVersionUID = 3466399101350652592L;
}
