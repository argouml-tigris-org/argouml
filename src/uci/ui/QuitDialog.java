package uci.ui;


import java.awt.*;

public class QuitDialog extends Dialog {
	void yesButton_Clicked(Event event) {
	    getParent().handleEvent(new Event(this, Event.WINDOW_DESTROY, null));
	}

	void noButton_Clicked(Event event) {
		//{{CONNECTION
		// Clicked from noButton Hide the Dialog
		hide();
		//}}
	}

	public QuitDialog(Frame parent, boolean modal) {

	    super(parent, modal);

		//{{INIT_CONTROLS
		setLayout(null);
		addNotify();
		resize(insets().left + insets().right + 337,insets().top + insets().bottom + 135);
		yesButton = new java.awt.Button(" Yes ");
		yesButton.reshape(insets().left + 72,insets().top + 84,79,22);
		yesButton.setFont(new Font("Dialog", Font.BOLD, 12));
		add(yesButton);
		noButton = new java.awt.Button("  No  ");
		noButton.reshape(insets().left + 180,insets().top + 84,79,22);
		noButton.setFont(new Font("Dialog", Font.BOLD, 12));
		add(noButton);
		label1 = new java.awt.Label("Do you really want to quit?");
		label1.reshape(insets().left + 60,insets().top + 36,228,23);
		add(label1);
		setTitle("Confirm Quit");
		setResizable(false);
		//}}
	}

	public QuitDialog(Frame parent, String title, boolean modal) {
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
		if (event.target == noButton && event.id == Event.ACTION_EVENT) {
			noButton_Clicked(event);
		}
		if (event.target == yesButton && event.id == Event.ACTION_EVENT) {
			yesButton_Clicked(event);
		}
		return super.handleEvent(event);
	}

	//{{DECLARE_CONTROLS
	java.awt.Button yesButton;
	java.awt.Button noButton;
	java.awt.Label label1;
	//}}
}

