// $Id: SnoozeOrder.java 10203 2006-03-23 22:01:24Z linus $
// Copyright (c) 1996-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.cognitive.critics;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * A Critic can be disabled for a certain amount of time by giving it
 * the snooze command.  Whereas most ControlMech's activate or deactivate
 * Critic's based on evidence of the Designer's state of mind, this
 * command allows the Designer to disable Critic's without stating any
 * reason.  However, after a period of time, the critic may become
 * active again.  We think this will often be convienent because
 * Designer's have a lot of tacit knowledge about their own state of
 * mind that is not worth making explicit.
 *
 * @author Jason Robbins
 */
public class SnoozeOrder implements Serializable {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(SnoozeOrder.class);

    ////////////////////////////////////////////////////////////////
    // constants
    /**
     * The initial sleeping time.
     *
     * Ten minutes.
     */
    private static final long INITIAL_INTERVAL_MS = 1000 * 60 * 10;

    ////////////////////////////////////////////////////////////////
    // instance variables

    /**
     * Critic should sleep until this time.
     */
    private Date snoozeUntil;

    /**
     * If the designer snoozees the critics again before this time, then
     * go to sleep for even longer.
     */
    private Date snoozeAgain;

    /**
     * The sleeping time, including the effects of repeated snoozeing.
     */
    private long interval;

    private Date now = new Date();
    private Date getNow() {
	now.setTime(System.currentTimeMillis());
	return now;
    }

    /**
     * The constructor.
     *
     */
    public SnoozeOrder() {
	/* in the past, 0 milliseconds after January 1, 1970, 00:00:00 GMT. */
	snoozeUntil =  new Date(0);
	snoozeAgain =  new Date(0);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @return true if snoozed
     */
    public boolean getSnoozed() {
	return snoozeUntil.after(getNow());
    }

    /**
     * @param h if true, then snooze, else unsnooze
     */
    public void setSnoozed(boolean h) {
	if (h) {
	    snooze();
	} else {
	    unsnooze();
	}
    }

    ////////////////////////////////////////////////////////////////
    // criticism control

    /**
     * Snooze the critic.
     */
    public void snooze() {
	if (snoozeAgain.after(getNow())) {
	    interval = nextInterval(interval);
	} else {
	    interval = INITIAL_INTERVAL_MS;
	}
	long n = (getNow()).getTime();
	snoozeUntil.setTime(n + interval);
	snoozeAgain.setTime(n + interval + INITIAL_INTERVAL_MS);
	LOG.info("Setting snooze order to: " + snoozeUntil.toString());
    }

    /**
     * Unsnooze the critic.
     */
    public void unsnooze() {
	/* in the past, 0 milliseconds after January 1, 1970, 00:00:00 GMT. */
	snoozeUntil =  new Date(0);
    }

    /**
     * @param last the previous interval
     * @return the next longer interval
     */
    protected long nextInterval(long last) {
	/* by default, double the snooze interval each time */
	return last * 2;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -7133285313405407967L;
} /* end class SnoozeOrder */
