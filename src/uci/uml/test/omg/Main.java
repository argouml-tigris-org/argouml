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


package uci.uml.test.omg;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Extension_Mechanisms.*;

/** Constructs all the examples in this directory.  The purpose of
/** this code is to test the UML Meta-model implementation. */


public class Main {

  public static void main(String args[]) {
    System.out.println("\n\nCONSTRUCTING WINDOW EXAMPLE");
    WindowExample we = new WindowExample();
    we.print();
    
    System.out.println("\n\nCONSTRUCTING MATHPACK EXAMPLE");
    MathPackExample mpe = new MathPackExample();
    mpe.print();
    
    System.out.println("\n\nCONSTRUCTING GRAPHICS EXAMPLE");
    GraphicsExample ge = new GraphicsExample();
    ge.print();
    
    System.out.println("\n\nCONSTRUCTING HUMAN RESOURCES EXAMPLE");
    HumanResourcesExample hre = new HumanResourcesExample();
    hre.print();
    
    System.out.println("\n\nCONSTRUCTING SHAPES EXAMPLE");
    ShapesExample she = new ShapesExample();
    she.print();
    
    System.out.println("\n\nCONSTRUCTING PASSWORD EXAMPLE");
    PasswordExample pwe = new PasswordExample();
    pwe.print();
    
    System.out.println("\n\nCONSTRUCTING DIALING EXAMPLE");
    DialingExample de = new DialingExample();
    de.print();
    
    System.out.println("\n\nCONSTRUCTING CONCURRENT STATE EXAMPLE");
    ConcurrentSubstatesExample cse = new ConcurrentSubstatesExample();
    cse.print();
    
    System.out.println("\n\nCONSTRUCTING STUBBED TRANSITIONS EXAMPLE");
    StubbedTransExample ste = new StubbedTransExample();
    ste.print();
    
    System.out.println("\n\nCONSTRUCTING COMPLEX TRANSITIONS EXAMPLE");
    ComplexTransExample cte = new ComplexTransExample();
    cte.print();
    
    System.out.println("\n\nCONSTRUCTING IMPLEMENTATION EXAMPLE");
    ImplementationExample ie = new ImplementationExample();
    ie.print();
    
    System.out.println("\n\nCONSTRUCTING TELEPHONE CATALOG EXAMPLE");
    TelephoneCatalogExample tce = new TelephoneCatalogExample();
    tce.print();
    
    System.out.println("\n\nCONSTRUCTING PACKAGE EXAMPLE");
    PackageExample pke = new PackageExample();
    pke.print();
    
    System.out.println("\n\nSUCCESSFULLY CONSTRUCTED ALL EXAMPLES");
  }

} /* end class Main */
