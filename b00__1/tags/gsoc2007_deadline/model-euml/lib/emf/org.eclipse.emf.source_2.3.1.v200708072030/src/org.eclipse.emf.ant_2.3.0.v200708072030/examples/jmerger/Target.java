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
 */
package org.eclipse.emf.test.tools.merger;

import source.ClassA;
import target.ClassA;


/**
 * Target javadoc 1
 * Target javadoc 2
 * <!-- begin-user-doc -->
 * Target user javadoc 3
 * Target user javadoc 4
 * <!-- end-user-doc -->
 * 
 * @author EMF Team (target)
 * @generated
 */
public class MergerExample
{
  /**
   * Target javadoc 5
   * @generated
   */
  private int a1 = -1;
  
  /**
   * Target javadoc 6
   * @generated
   */
  private int a2 = -2;

  /**
   * Target javadoc 7
   * @generated
   */
  private int a3 = -3;

  /**
   * Target javadoc 8
   * <!-- begin-user-doc -->
   * Target user javadoc 9 
   * <!-- end-user-doc -->
   * @generated
   */
  private int a4 = -4;

  /**
   * Target javadoc 10
   * <!-- begin-user-doc -->
   * Target user javadoc 11
   * <!-- end-user-doc -->
   * @generated
   */
  private int a5 = -5;
  
  
  
  
  
  /**
   * Target javadoc 12
   * <!-- begin-user-doc -->
   * Target user javadoc 13
   * <!-- end-user-doc -->
   * Target javadoc 14
   * @generated
   */
  protected String id = "target";

  /**
   * Target javadoc 15
   * <!-- begin-user-doc -->
   * Target user javadoc 16
   * Target user javadoc 17
   * <!-- end-user-doc -->
   * Target javadoc 18
   * @return target
   * @generated
   */
  public boolean isID()
  {
    // begin-user-code
    System.out.println("Target user code 2");
    // end-user-code
    System.out.println("Target code 3");
    return id == "target";
  }

  /**
   * Target javadoc 19
   * <!-- begin-user-doc -->
   * Target user javadoc 20
   * Target user javadoc 21
   * <!-- end-user-doc -->
   * Target javadoc 22
   * @param target
   * @generated NOT
   */
  public void setID(boolean value)
  {
     id = value ? "target" : "source";
     System.out.println("Target code 4");
  }
  
  /**
   * Target javadoc 23
   * <!-- begin-user-doc -->
   * Target user javadoc 24
   * <!-- end-user-doc -->
   * Target javadoc 25
   * @param target
   */  
  protected Object sourceRequired()
  {
    System.out.println("Target code 5");
  }    
  
  /**
   * Target javadoc 26
   * @generated
   */
  private void methodWithTargetWithoutUserSections()
  {
    System.out.println("Target code 6");
  }
  
  /**
   * Target javadoc 27
   * <!-- begin-user-doc -->
   * Target user javadoc 28
   * <!-- end-user-doc -->
   * Target javadoc 29
   * @generated
   */
  private void methodWithSourceWithoutUserSections()
  {
    // begin-user-code
    System.out.println("Target user code 7");
    // end-user-code        
    System.out.println("Target code 8");
  }
  
  /**
   * Target javadoc 30
   * @generated
   */
  private void methodWithoutUserSections()
  {
    System.out.println("Target code 9");
  }
}