// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.uml.generator;

import java.text.ParseException;

import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

import junit.framework.*;

public class TestParserDisplay extends TestCase {
	private final String attr01 = "name";
	private final String attr02 = "+name";
	private final String attr03 = "-name : void";
	private final String attr04 = "#name [1..1] : int {a=b}";
	private final String attr05 = "public name {a=b, c = d } : [1..*] int = 0";
	private final String attr06 = "private name {a=b, c = d } [*..*] : int = 15 {frozen}";
	private final String attr07 = "+name : String = \'val[15] \'";
	private final String attr08 = "  +name : String = \"a <<string>>\"";
	private final String attr09 = "+name : String = (a * (b+c) - d)";
	private final String attr10 = "+name << organization >> : String = 2 * (b+c) - 10";
	private final String attr11 = "+ <<machine>> name : String = a[15]";
	private final String attr12 = "+ name : String = a << 5";

	private final String nattr01 = "too many string in an attribute";
	private final String nattr02 = "+vis name";
	private final String nattr03 = "vis name : type : type";
	private final String nattr04 = "vis name = 0 = 1";
	private final String nattr05 = "vis name [1..1] [1..1]";
	private final String nattr06 = "vis name [1..1";
	private final String nattr07 = "vis name { a = b, cv = ";
	private final String nattr08 = "vis \"name\"";
	private final String nattr09 = "\"vis\" name";
	private final String nattr10 = "vis (name)";
	private final String nattr11 = "(vis) name";
	private final String nattr12 = "vis name : \"type\"";
	private final String nattr13 = "vis name : (type)";

	public TestParserDisplay(String str) {
		super(str);
	}

	public void testAttributeName() {
		MAttribute attr;

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkName(attr, attr01, "name");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkName(attr, attr02, "name");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkName(attr, attr03, "name");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkName(attr, attr04, "name");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkName(attr, attr05, "name");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkName(attr, attr06, "name");
	}

	private void checkName(MAttribute attr, String text, String name) {
		try {
			ParserDisplay.SINGLETON.parseAttribute(text, attr);
			assert(text + " gave wrong name: " + attr.getName(),
				name.equals(attr.getName()));
		} catch (Exception e) {
			assert(text + " threw unexpectedly: " + e, false);
		}
	}

	public void testAttributeType() {
		MAttribute attr;

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkType(attr, attr03, "void");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkType(attr, attr04, "int");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkType(attr, attr05, "int");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkType(attr, attr06, "int");
	}

	private void checkType(MAttribute attr, String text, String type) {
		try {
			ParserDisplay.SINGLETON.parseAttribute(text, attr);
			assert(text + " gave wrong type: " + (attr.getType() == null ? "(null)" : attr.getType().getName()),
				attr.getType() != null && type.equals(attr.getType().getName()));
		} catch (Exception e) {
			assert(text + " threw unexpectedly: " + e, false);
		}
	}

	public void testAttributeVisibility() {
		MAttribute attr;

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkVisibility(attr, attr02, "public");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkVisibility(attr, attr03, "private");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkVisibility(attr, attr04, "protected");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkVisibility(attr, attr05, "public");
		checkVisibility(attr, attr01, "public");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkVisibility(attr, attr06, "private");
		checkVisibility(attr, attr01, "private");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkVisibility(attr, attr08, "public");
	}

	private void checkVisibility(MAttribute attr, String text, String vis) {
		try {
			ParserDisplay.SINGLETON.parseAttribute(text, attr);
			assert(text + " gave wrong visibility: " + (attr.getVisibility() == null ? "(null)" : attr.getVisibility().getName()),
				attr.getVisibility() != null && vis.equals(attr.getVisibility().getName()));
		} catch (Exception e) {
			assert(text + " threw unexpectedly: " + e, false);
		}
	}

	public void testAttributeProperty() {
		MAttribute attr;
		String res1[] = {"a", "b"};
		String res2[] = {"a", "b", "c", "d"};
		String res3[] = {"a", "b", "c", "d", "frozen", null};

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkProperties(attr, attr04, res1);

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkProperties(attr, attr05, res2);

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkProperties(attr, attr06, res3);
	}

	private void checkProperties(MAttribute attr, String text, String props[]) {
		int i;
		try {
			ParserDisplay.SINGLETON.parseAttribute(text, attr);
			for (i = 0; i + 1 < props.length; i += 2) {
				if (props[i+1] == null)
					assert("TaggedValue " + props[i] + " exists!", attr.getTaggedValue(props[i]) == null);
				else
					assert("TaggedValue " + props[i] + " wrong!", props[i+1].equals(attr.getTaggedValue(props[i])));
			}
		} catch (Exception e) {
			assert(text + " threw Exception " + e, false);
		}
	}

	public void testAttributeMultiplicity() {
		MAttribute attr;

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkMultiplicity(attr, attr04, new MMultiplicity("1..1"));

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkMultiplicity(attr, attr05, new MMultiplicity("1..*"));

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkMultiplicity(attr, attr06, new MMultiplicity("*..*"));
	}

	private void checkMultiplicity(MAttribute attr, String text, MMultiplicity mult) {
		try {
			ParserDisplay.SINGLETON.parseAttribute(text, attr);
			assert(text + " gave wrong multiplicity: " + (attr.getMultiplicity() == null ? "(null)" : attr.getMultiplicity().toString()),
				mult == null && attr.getMultiplicity() == null ||
				mult != null && mult.equals(attr.getMultiplicity()));
		} catch (Exception e) {
			assert(text + " threw unexpectedly: " + e, false);
		}
	}

	public void testParseExceptions() {
		MAttribute attr;

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkThrows(attr, nattr01, true, false, false);
		checkThrows(attr, nattr02, true, false, false);
		checkThrows(attr, nattr03, true, false, false);
		checkThrows(attr, nattr04, true, false, false);
		checkThrows(attr, nattr05, true, false, false);
		checkThrows(attr, nattr06, true, false, false);
		checkThrows(attr, nattr07, true, false, false);
		checkThrows(attr, nattr08, true, false, false);
		checkThrows(attr, nattr09, true, false, false);
		checkThrows(attr, nattr10, true, false, false);
		checkThrows(attr, nattr11, true, false, false);
		checkThrows(attr, nattr12, true, false, false);
		checkThrows(attr, nattr13, true, false, false);
	}

	private void checkThrows(MAttribute attr, String text, boolean prsEx,
			boolean ex2, boolean ex3) {
		try {
			ParserDisplay.SINGLETON.parseAttribute(text, attr);
			assert("didn't throw for " + text, false);
		} catch (ParseException pe) {
			assert(text + " threw ParseException " + pe, prsEx);
		} catch (Exception e) {
			assert(text + " threw Exception " + e, !prsEx);
		}
	}

	public void testAttributeValue() {
		MAttribute attr;

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkValue(attr, attr05, "0");
		checkValue(attr, attr01, "0");
		checkValue(attr, attr06, "15");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkValue(attr, attr07, "\'val[15] \'");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkValue(attr, attr08, "\"a <<string>>\"");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkValue(attr, attr09, "(a * (b+c) - d)");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkValue(attr, attr10, "2 * (b+c) - 10");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkValue(attr, attr11, "a[15]");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkValue(attr, attr12, "a << 5");
	}

	private void checkValue(MAttribute attr, String text, String val) {
		try {
			ParserDisplay.SINGLETON.parseAttribute(text, attr);
			assert(text + " gave wrong visibility: " + (attr.getInitialValue() == null ? "(null)" : attr.getInitialValue().getBody()),
				val == null && (attr.getInitialValue() == null || "".equals(attr.getInitialValue().getBody())) ||
				val != null && attr.getInitialValue() != null && val.equals(attr.getInitialValue().getBody()));
		} catch (Exception e) {
			assert(text + " threw unexpectedly: " + e, false);
		}
	}

	public void testStereotype() {
		MAttribute attr;

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkStereotype(attr, attr01, null);

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkStereotype(attr, attr10, "organization");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		checkStereotype(attr, attr11, "machine");
		checkStereotype(attr, attr01, "machine");
	}

	private void checkStereotype(MAttribute attr, String text,
			String val) {
		try {
			ParserDisplay.SINGLETON.parseAttribute(text, attr);
			assert(text + " gave wrong stereotype " +
				(attr.getStereotype() != null ?
				 attr.getStereotype().getName() : "(null)"),
				(val == null && attr.getStereotype() == null)||
				(val != null &&
				 attr.getStereotype() != null &&
				 val.equals(attr.getStereotype().getName())));
		} catch (Exception e) {
			assert(text + " threw unexpectedly: " + e, false);
		}
	}
}

