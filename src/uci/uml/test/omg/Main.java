package uci.uml.test.omg;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Extension_Mechanisms.*;

/** Constructs all the examples in this directory.  The purpose of
/** this code is to test the UML Meta-model implementation. */


public class Main {

  public static void main(String args[]) {
    System.out.println("\n\nCONSTRUCTING WINDOW EXAMPLE");
    WindowExample we = new WindowExample();

    System.out.println("\n\nCONSTRUCTING MATHPACK EXAMPLE");
    MathPackExample mpe = new MathPackExample();

    System.out.println("\n\nCONSTRUCTING GRAPHICS EXAMPLE");
    GraphicsExample ge = new GraphicsExample();

    System.out.println("\n\nCONSTRUCTING HUMAN RESOURCES EXAMPLE");
    HumanResourcesExample hre = new HumanResourcesExample();

    System.out.println("\n\nCONSTRUCTING SHAPES EXAMPLE");
    ShapesExample she = new ShapesExample();

    System.out.println("\n\nSUCCESSFULLY CONSTRUCTED ALL EXAMPLES");
  }

} /* end class Main */
