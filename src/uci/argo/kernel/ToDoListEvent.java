package uci.argo.kernel;

public class ToDoListEvent {

  protected ToDoItem _item;

  public ToDoListEvent() { _item = null ; }
  public ToDoListEvent(ToDoItem item) { _item = item ; }

  public ToDoItem getToDoItem() { return _item; }

} /* end class ToDoListEvent */
