package uci.ui;

import java.awt.*;
//import java.awt.*;

public class AboutDialog extends Dialog {
  void okButton_Clicked(Event event) {
    //{{CONNECTION
    // Clicked from okButton Hide the Dialog
    hide();
    //}}
  }

  public AboutDialog(Frame parent, boolean modal) {

    super(parent, modal);

    //{{INIT_CONTROLS
    setLayout(null);
    addNotify();
    resize(insets().left + insets().right + 249,insets().top + insets().bottom + 150);
    label1 = new java.awt.Label("CoRE Tool Prototype");
    label1.reshape(insets().left + 36,insets().top + 36,166,21);
    add(label1);
    okButton = new java.awt.Button("OK");
    okButton.reshape(insets().left + 96,insets().top + 84,66,27);
    add(okButton);
    setTitle("About");
    setResizable(false);
    //}}
  }

  public AboutDialog(Frame parent, String title, boolean modal) {
    this(parent, modal);
    setTitle(title);
  }

  public synchronized void show() {
    Rectangle bounds = getParent().bounds();
    Rectangle abounds = bounds();

    move(bounds.x + (bounds.width - abounds.width)/ 2,
	 bounds.y + (bounds.height - abounds.height)/2);

    super.show();
  }

  public boolean handleEvent(Event event) {
    if(event.id == Event.WINDOW_DESTROY) {
      hide();
      return true;
    }
    if (event.target == okButton && event.id == Event.ACTION_EVENT) {
      okButton_Clicked(event);
    }
    return super.handleEvent(event);
  }

  //{{DECLARE_CONTROLS
  java.awt.Label label1;
  java.awt.Button okButton;
  //}}
}

