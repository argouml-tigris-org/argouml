package uci.argo.kernel;

public interface ArgoConstants {

  ////////////////////////////////////////////////////////////////
  // domain-independent types of knowledge

  //needs-more-work: what if there are more than 31 of these?

  public static int KT_CORRECTNESS    = 1<<0;
  public static int KT_COMPLETENESS   = 1<<1;
  public static int KT_CONCISTENCY    = 1<<2;
  public static int KT_OPTIMIZATION   = 1<<3;
  public static int KT_ALTERNATIVE    = 1<<4;
  public static int KT_EVOLVABILITY   = 1<<5;
  public static int KT_PRESENTATION   = 1<<6;
  public static int KT_TOOL           = 1<<7;
  public static int KT_EXPERIENTIAL   = 1<<8;
  public static int KT_ORGANIZATIONAL = 1<<9;
  

} /* end interface ArgoConstants */
