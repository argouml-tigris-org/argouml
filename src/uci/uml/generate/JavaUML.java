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




package uci.uml.generate;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;

import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;

/** A file of information about the Java language.  This is used to
 *  fill in the offered data types in variable and operation
 *  declarations. */

public class JavaUML {

  // java.lang
  public static MMClass STRING_CLASS = new MMClass("String");

  public static DataType VOID_TYPE = new DataType("void");
  public static DataType CHAR_TYPE = new DataType("char");
  public static DataType INT_TYPE = new DataType("int");
  public static DataType BOOLEAN_TYPE = new DataType("boolean");
  public static DataType BYTE_TYPE = new DataType("byte");
  public static DataType LONG_TYPE = new DataType("long");
  public static DataType FLOAT_TYPE = new DataType("float");
  public static DataType DOUBLE_TYPE = new DataType("double");

  public static MMClass CHAR_CLASS = new MMClass("Character");
  public static MMClass INT_CLASS = new MMClass("Integer");
  public static MMClass BOOLEAN_CLASS = new MMClass("Boolean");
  public static MMClass BYTE_CLASS = new MMClass("Byte");
  public static MMClass LONG_CLASS = new MMClass("Long");
  public static MMClass FLOAT_CLASS = new MMClass("Float");
  public static MMClass DOUBLE_CLASS = new MMClass("Double");


  // java.awt
  public static MMClass RECTANGLE_CLASS = new MMClass("Rectangle");
  public static MMClass POINT_CLASS = new MMClass("Point");
  public static MMClass COLOR_CLASS = new MMClass("Color");

  
  // java.util
  public static MMClass VECTOR_CLASS = new MMClass("Vector");
  public static MMClass HASHTABLE_CLASS = new MMClass("Hashtable");
  public static MMClass STACK_CLASS = new MMClass("Stack");




  
} /* end class JavaUML */
