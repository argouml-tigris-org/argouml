package org.eclipse.example.library;

/**
 * @model
 */
public interface Book
{
  /**
   * @model
   */
  String getTitle();

  /**
   * @model default="100"
   */
  int getPages();

  /**
   * @model
   */
  BookCategory getCategory();

  /**
   * @model opposite="books"
   */
  Writer getAuthor();
}