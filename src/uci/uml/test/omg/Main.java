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
