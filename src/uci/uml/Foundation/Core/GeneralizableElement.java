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




package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.PropertyVetoException;

public interface GeneralizableElement extends Namespace {
  //    public boolean _isRoot;
  //    public boolean _isLeaf;
  //    public boolean _isAbstract;
  //    public Generalization _generalization[];
  //    public Generalization _specialization[];

  public boolean getIsRoot();
  public void setIsRoot(boolean x) throws PropertyVetoException;

  public boolean getIsLeaf();
  public void setIsLeaf(boolean x) throws PropertyVetoException;

  public boolean getIsAbstract();
  public void setIsAbstract(boolean x) throws PropertyVetoException;

  public Vector getGeneralization();
  public void setGeneralization(Vector x) throws PropertyVetoException;
  public void addGeneralization(Generalization x) throws PropertyVetoException;
  public void removeGeneralization(Generalization x) throws PropertyVetoException;

  public Vector getSpecialization();
  public void setSpecialization(Vector x) throws PropertyVetoException;
  public void addSpecialization(Generalization x) throws PropertyVetoException;
  public void removeSpecialization(Generalization x) throws PropertyVetoException;
  static final long serialVersionUID = -4743108433739796567L;
}
