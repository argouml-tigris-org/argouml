package uci.argo.kernel;

public interface ToDoListListener extends java.util.EventListener {
  void toDoListChanged(ToDoListEvent tde);
  void toDoItemAdded(ToDoListEvent tde);
  void toDoItemRemoved(ToDoListEvent tde);
} /* end interface ToDoListListener */
