/**
 * <copyright>
 *
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *
 * </copyright>
 * 
 * $Id$
 */
package org.eclipse.emf.test.tools.merger;

import source.ClassA;
import source.ClassB;

/**
 * Source javadoc 1
 * Source javadoc 2
 * <!-- begin-user-doc -->
 * Source user javadoc 3
 * Source user javadoc 4
 * <!-- end-user-doc -->
 * 
 * @author EMF Team (source)
 * @generated
 */
public class MergerExample
{
  private int a1 = 1;
  
  /**
   * Source javadoc 5
   */
  int a2 = 2;

  /**
   * Source javadoc 6
   * @generated
   */
  protected int a3 = 3;

  /**
   * Source javadoc 7
   * <!-- begin-user-doc -->
   * Source user javadoc 8
   * <!-- end-user-doc -->
   */
  public int a4 = 4;

  /**
   * Source javadoc 9
   * <!-- begin-user-doc -->
   * Source user javadoc 10
   * <!-- end-user-doc -->
   * @generated
   */
  protected int a5 = 5;
  
  
  
  
  /**
   * Source javadoc 11
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected String id = "source";

  /**
   * Source javadoc 12
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected boolean newAttribute = true;  
  
  /**
   * Source javadoc 13
   * <!-- begin-user-doc -->
   * Source user javadoc 14
   * <!-- end-user-doc -->
   * Source javadoc 15
   * @return source
   * @generated
   */
  public boolean isID()
  {
    // begin-user-code
    System.out.println("Source user code 1");
    // end-user-code    
    System.out.println("Source code 2");
    return id == "source";
  }

  /**
   * Source javadoc 16
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * Source javadoc 17
   * @param source
   * @generated
   */
  public void setID(boolean value)
  {
     System.out.println("Source code 3");
     id = value ? "source" : "target";
  }  
  
  /**
   * Source javadoc 17
   * <!-- begin-user-doc -->
   * Source user javadoc 18
   * <!-- end-user-doc -->
   * Source javadoc 19
   * @param source
   * @unmodifiable
   */
  protected Object sourceRequired()
  {
    System.out.println("Source code 4");
  }
  
  /**
   * Source javadoc 20
   * <!-- begin-user-doc -->
   * Source user javadoc 21
   * <!-- end-user-doc -->
   * Source javadoc 22
   * @generated
   */
  private void methodWithTargetWithoutUserSections()
  {
    // begin-user-code
    System.out.println("Source user code 5");
    // end-user-code        
    System.out.println("Source code 6");
  }
  
  /**
   * Source javadoc 23
   * @generated
   */
  private void methodWithSourceWithoutUserSections()
  {
    System.out.println("Source code 7");
  }
  
  /**
   * Source javadoc 24
   * @generated
   */
  private void methodWithoutUserSections()
  {
    System.out.println("Source code 8");
  }
}