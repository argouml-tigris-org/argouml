package uci.uml.ui;

import java.beans.*;

public class DelayedChangeNotify implements Runnable {
  DelayedVetoableChangeListener _listener;
  PropertyChangeEvent _pce;

  public DelayedChangeNotify(DelayedVetoableChangeListener list,
			     PropertyChangeEvent pce) {
    _listener = list;
    _pce = pce;
  }
  
  public void run() { _listener.delayedVetoableChange(_pce); }

} /* end class DelayedChangeNotify */
