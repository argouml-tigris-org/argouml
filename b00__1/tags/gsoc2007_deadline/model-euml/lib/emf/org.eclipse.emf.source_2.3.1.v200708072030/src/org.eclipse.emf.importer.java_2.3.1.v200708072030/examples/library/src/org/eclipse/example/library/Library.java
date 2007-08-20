package org.eclipse.example.library;

import java.util.List;
import java.util.Map;

/**
 * @model
 */
public interface Library
{
  /**
   * @model
   */
  String getName();

  /**
   * @model type="Writer" containment="true"
   */
  List getWriters();

  /**
   * @model type="Book" containment="true"
   */
  List getBooks();
  
  /**
   * @model type="Book"
   */
  List getSpecialBooks();
  
  /**
   * @model keyType="String" valueType="Book"
   */
  Map getBookByTitleMap(); 

  /**
   * @model keyType="String" valueType="Writer"
   */
  Map getWriterByNameMap();
  
  /**
   * @model
   */
  Map getOptions();
  
  /**
   * @model dataType="MyMapOfIntegersAndStrings"
   */  
  Map getWriterByIDMap();  
}