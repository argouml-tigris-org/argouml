
package uci.argo.kernel;

public class HistoryEvent {
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected History _source;
  protected HistoryItem _item;
  protected int _index;

  ////////////////////////////////////////////////////////////////
  // contructors
  public HistoryEvent(History src, HistoryItem item, int index) {
    _source = src;
    _item = item;
    _index = index;
  }

  ////////////////////////////////////////////////////////////////
  // accessors
  //needs-more-work

  public int getIndex() { return _index; }

} /* end class HistoryEvent */
