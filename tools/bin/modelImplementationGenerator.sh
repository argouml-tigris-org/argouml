#!/bin/sh

# Generates a new model implementation in the current directory.
#
# The first argument is the model implementation name.
#
# This just takes care of some tedious typing. After this is run
# you will have to create a whole lot of methods to fulfil the interfaces.
# Eclipse can help you create those. Eclipse will also help you create
# the needed import statements if you need those.

MINAME=$1

INTERFACES="ActivityGraphsFactory ActivityGraphsHelper
    AggregationKind
    ChangeableKind
    CollaborationsFactory CollaborationsHelper
    CommonBehaviorFactory CommonBehaviorHelper
    ConcurrencyKind
    CopyHelper
    CoreFactory CoreHelper
    DataTypesFactory DataTypesHelper
    DirectionKind
    ExtensionMechanismsFactory ExtensionMechanismsHelper
    Facade
    MetaTypes
    ModelEventPump
    ModelManagementFactory ModelManagementHelper
    OrderingKind
    PseudostateKind
    ScopeKind
    UmlFactory UmlHelper
    UseCasesFactory UseCasesHelper
    VisibilityKind"

miname=`echo $MINAME | tr 'A-Z' 'a-z'`

fileHeader() {
    cat <<EOF
// \$Id\$
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.model.$miname;

EOF
}


# Create the ModelImplementation file.
FILENAME=${MINAME}ModelImplementation.java

fileHeader > $FILENAME
cat >>$FILENAME <<EOF
/**
 * ModelImplementation for the $MINAME Implementation.
 */
public class ${MINAME}ModelImplementation implements ModelImplementation {
EOF

for i in $INTERFACES
do
  echo "    private $i the$i;" >> $FILENAME
done

cat >>$FILENAME <<EOF

    /**
     * Constructor.
     */
    public ${MINAME}ModelImplementation() {
        // 1. Do initialization of the data base.

        // 2. If any factories or helpers depend on each other and need
        //    to be initialized in a special order, do the initialization
        //    here in order:
        theCoreFactory = new CoreFactory${MINAME}Impl(this);
    }

EOF

for i in $INTERFACES
do
    cat >>$FILENAME <<EOF
    public $i get$i() {
        if (the$i == null) {
            the$i = new ${i}${MINAME}Impl(this);
        }

        return the$i;
    }

EOF
done

cat >>$FILENAME <<EOF
    public XmiReader getXmiReader() throws UmlException {
        return new XmiReader${MINAME}Impl(this);
    }

    public XmiWriter getXmiWriter(Object model, Writer writer, String version)
        throws UmlException {
        return new XmiWriter${MINAME}Impl(this, model, writer, version);
    }
}
EOF


for i in $INTERFACES XmiReader
do
  CLASSNAME="${i}${MINAME}Impl"
  FILENAME="${CLASSNAME}.java"

  fileHeader > $FILENAME
  cat >>$FILENAME <<EOF
/**
 * The implementation of the ${i} for ${MINAME}.
 */
public class $CLASSNAME implements $i {

    /**
     * The model implementation.
     */
    private ${MINAME}ModelImplementation modelImpl;

    /**
     * Constructor.
     *
     * @param implementation The ModelImplementation.
     */
    public $CLASSNAME(${MINAME}ModelImplementation implementation) {
        modelImpl = implementation;
    }


}
EOF
done


CLASSNAME=XmiWriter${MINAME}Impl
FILENAME=$CLASSNAME.java

fileHeader > $FILENAME
cat >>$FILENAME <<EOF
/**
 * The implementation of the XmiWriter for ${MINAME}.
 */
public class $CLASSNAME implements XmiWriter {

    /**
     * The model implementation.
     */
    private ${MINAME}ModelImplementation modelImpl;

    /**
     * Constructor.
     *
     * @param implementation The ModelImplementation.
     * @param model The project member model.
     * @param writer The writer.
     * @param version The version of ArgoUML.
     */
    public $CLASSNAME(${MINAME}ModelImplementation implementation,
        Object model, Writer writer, String version) {
        modelImpl = implementation;
    }


}
EOF
