// Copyright (c) 1996-2001 The Regents of the University of California. All

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



package org.argouml.cognitive;

import junit.framework.*;



import org.argouml.cognitive.critics.*;

import org.tigris.gef.util.*;







public class TestToDoItem extends TestCase {

    public TestToDoItem(String name) {

	super(name);

    }



    public void testConstructors() {

	ToDoItem t;



	t = new ToDoItem(new Designer(), "headline",

			 1, 

			 "description", "moreinfoUrl",

			 new VectorSet());

	assert("headline", "headline".equals(t.getHeadline()));

	assert("description", "description".equals(t.getDescription()));

	assert("infourl", "moreinfoUrl".equals(t.getMoreInfoURL()));

	assert("priority", 1 == t.getPriority());



	t = new ToDoItem(new Critic(), new Object(), new Designer());



	VectorSet vs = new VectorSet();

	vs.addElement(new Object());

	t = new ToDoItem(new Critic(), vs, new Designer());



	t = new ToDoItem(new Critic());

    }



    public void testEquals() {

	ToDoItem t1;

	ToDoItem t2;



	Designer d1 = new Designer();

	Designer d2 = new Designer();

	t1 = new ToDoItem(d1, "headline",

			  1, 

			  "description", "moreinfoUrl",

			  new VectorSet());

	assert("same", t1.equals(t1));



	t2 = new ToDoItem(d1, "headline",

			  1, 

			  "description", "moreinfoUrl",

			  new VectorSet());

	assert("similar1", t1.equals(t2));

	assert("similar2", t2.equals(t1));



	t2 = new ToDoItem(d2, "headline",

			  1, 

			  "description", "moreinfoUrl",

			  new VectorSet());

	assert("designer differ1", !t1.equals(t2));

	assert("designer differ2", !t2.equals(t1));



	t2 = new ToDoItem(d1, "new headline",

			  1, 

			  "description", "moreinfoUrl",

			  new VectorSet());

	assert("headline differ1", !t1.equals(t2));

	assert("headline differ2", !t2.equals(t1));



	t2 = new ToDoItem(d1, "headline",

			  2, 

			  "description", "moreinfoUrl",

			  new VectorSet());

	assert("priority differ1", t1.equals(t2));

	assert("priority differ2", t2.equals(t1));

	

	t2 = new ToDoItem(d1, "headline",

			  1,

			  "new description", "moreinfoUrl",

			  new VectorSet());

	assert("description differ1", t1.equals(t2));

	assert("description differ2", t2.equals(t1));

	

	t2 = new ToDoItem(d1, "headline",

			  1,

			  "description", "new moreinfoUrl",

			  new VectorSet());

	assert("moreinfourl differ1", t1.equals(t2));

	assert("moreinfourl differ2", t2.equals(t1));



	VectorSet vs1 = new VectorSet();

	Object o1 = new Object();

	vs1.addElement(o1);



	t2 = new ToDoItem(d1, "headline",

			  1,

			  "description", "moreinfoUrl",

			  vs1);

	assert("offenderamount differ1", !t1.equals(t2));

	assert("offenderamount differ2", !t2.equals(t1));



	t1 = new ToDoItem(d1, "headline",

			  1, 

			  "description", "moreinfoUrl",

			  vs1);

	assert("offenderlist same1", t1.equals(t2));

	assert("offenderlist same2", t2.equals(t1));

	

	VectorSet vs2 = new VectorSet();

	vs2.addElement(o1);



	t2 = new ToDoItem(d1, "headline",

			  1,

			  "description", "moreinfoUrl",

			  vs2);

	assert("offenderlist differ1", t1.equals(t2));

	assert("offenderlist differ2", t2.equals(t1));



	vs2 = new VectorSet();

	Object o2 = new Object();

	vs2.addElement(o2);



	t2 = new ToDoItem(d1, "headline",

			  1,

			  "description", "moreinfoUrl",

			  vs2);

	assert("offender differ1", !t1.equals(t2));

	assert("offender differ2", !t2.equals(t1));



	vs2 = new VectorSet();

	vs2.addElement(o1);

	vs2.addElement(o2);



	t2 = new ToDoItem(d1, "headline",

			  1,

			  "description", "moreinfoUrl",

			  vs2);

	assert("offenderamount differ1", !t1.equals(t2));

	assert("offenderamount differ2", !t2.equals(t1));

    }





    /** Function never actually called. Provided in order to make

     *  sure that the static interface has not changed.

     */

    private void compileTestStatics() {

	int i;

	i = ToDoItem.HIGH_PRIORITY;

	i = ToDoItem.MED_PRIORITY;

	i = ToDoItem.LOW_PRIORITY;

    }



    /** Function never actually called. Provided in order to make

     *  sure that the public interface has not changed.

     */

    private void compileTestConstructors() {

	new ToDoItem(new Designer(), "hejsan", 1, "hoppsan", "gurka",

		     new VectorSet());

	new ToDoItem(new Critic(), new Object(), new Designer());

	new ToDoItem(new Critic(), new VectorSet(), new Designer());

	new ToDoItem(new Critic());

    }

}

