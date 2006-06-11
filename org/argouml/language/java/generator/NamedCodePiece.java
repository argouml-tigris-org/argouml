// $Id$
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

package org.argouml.language.java.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Stack;


/**
 * This is a code piece that has been identified by the parser to be
 * of a specific kind. See the subclasses for further details.
 *
 * taken from:
 * JavaRE - Code generation and reverse engineering for UML and Java
 *
 * @author Marcus Andersson andersson@users.sourceforge.net
 */
public abstract class NamedCodePiece extends CodePiece {
    /**
     * Write the code this piece represents to file. The stack in the
     * parameter list contains the parser state when traversing up and
     * down in nested classes and interfaces. The code that is written
     * is generated from the model, but if no appropriate model element
     * exists, then the original code is written in order to maintain
     * additionally source code.
     *
     * @param reader Read original code from this.
     * @param writer Write code to this.
     * @param parseStateStack Information with one stack frame for each
     *                        classifier that the parser has descended into.
     * @throws IOException if we cannot write to the writer or
     *                     read from the reader.
     */
    public abstract void write(BufferedReader reader,
                               BufferedWriter writer,
                               Stack parseStateStack) throws IOException;

    /**
     * Read until the end of the code piece. As a precondition, the reader
     * must be positioned at the beginning of the code piece. If a writer
     * is given (not <tt>null</tt>), then everything that's read is written
     * to the writer. (Ususally, both reader and writer point to the same
     * file).
     *
     * @param reader Read original code from this.
     * @param writer Write code to this.
     * @throws IOException if we cannot write to the writer or
     *                     read from the reader.
     */
    public final void ffCodePiece(BufferedReader reader,
                                  BufferedWriter writer) throws IOException {
        int line = getStartLine();
        int column = getStartPosition();
        if (writer != null) {
            while (line < getEndLine()) {
                line++;
                column = 0;
                writer.write(reader.readLine());
                writer.newLine();
            }
            while (column < getEndPosition()) {
                column++;
                writer.write(reader.read());
            }
        } else {
            while (line < getEndLine()) {
                line++;
                column = 0;
                reader.readLine();
            }
            while (column < getEndPosition()) {
                column++;
                reader.read();
            }
        }
    }
}
