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

package org.argouml.kernel;

public interface ArgoConstants {

  ////////////////////////////////////////////////////////////////
  // domain-independent types of knowledge

  //TODO: what if there are more than 31 of these?

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
