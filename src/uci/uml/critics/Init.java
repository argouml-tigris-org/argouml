package uci.uml.critics;

import uci.argo.kernel.*;

public class Init {
  // domain independent
  //public static Critic crTooManyDisabled = new CrTooManyDisabled();
  //public static Critic crTooMuchFeedback = new CrTooMuchFeedback();

  // domain specific
  public static Critic crUselessAbstract = new CrUselessAbstract();


  /** static initializer, register all appropriate critics */
  public static void init() {
//     try {
      java.lang.Class umlClass = uci.uml.Foundation.Core.Class.class;

      Agency.register(crUselessAbstract, umlClass);

      //Agency.register(crTooMuchFeedback, project);
      //Agency.register(crTooManyDisabled, project);
      //Agency.dumpRegistry();
      
//     }
//     catch (java.lang.ClassNotFoundException e) {
// 	System.out.println("!!!! Error while registering " + e.toString());
//     }
  }

      
} /* end class Init */
