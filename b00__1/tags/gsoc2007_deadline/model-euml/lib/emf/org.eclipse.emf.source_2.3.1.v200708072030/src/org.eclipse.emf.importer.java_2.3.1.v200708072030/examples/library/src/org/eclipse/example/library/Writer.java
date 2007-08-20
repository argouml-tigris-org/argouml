package org.eclipse.example.library;

/**
 * @model
 */
public interface Writer
{
  /**
   * @model
   */
  String getName();

  /**
   * @model type="Book" opposite="author"
   */
  java.util.List getBooks();
}