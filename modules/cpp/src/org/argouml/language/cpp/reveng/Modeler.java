// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

/*REMOVE_BEGIN*/
package org.argouml.language.cpp.reveng;

import java.util.List;

/**
 * The Modeler receives calls from some reverse engineering system and is able
 * to transform this information into model elements. It expects to receive
 * calls in a logic sequence:
 * <ol>
 * <li>beginClassDeclaration</li>
 * <li>derivedFromClass</li>
 * <li>beginMemberDeclaration</li>
 * <li>endMemberDeclaration</li>
 * <li>endClassDeclaration</li>
 * </ol>
 * 
 * @author Luis Sergio Oliveira
 * @version 0.00
 * @since 0.19.1
 */
public interface Modeler {

    /**
     * Signals the begin of the translation unit.
     */
    void beginTranslationUnit();

    /**
     * Signals the end of the translation unit.
     */
    void endTranslationUnit();

    /**
     * Signals that we entered the given namespace scope.
     * 
     * @param ns
     *            The namespace identifier. For unnamed namespaces it is an
     *            empty <code>String</code>.
     */
    void enterNamespaceScope(String ns);

    /**
     * Signals the exit of the namespace scope.
     */
    void exitNamespaceScope();

    /**
     * Signal a new alias for the given namespace.
     * 
     * @param ns Identifier of the namespace for which an alias will be created.
     * @param alias Identifier of the new namespace alias.
     */
    void makeNamespaceAlias(String ns, String alias);

    /**
     * Signal the begin of a new class definition
     * 
     * @param oType
     *            The type of the specifier from the CPPvariables class, it is
     *            one of the "OT_*" values, e.g., OT_CLASS.
     * @param identifier The class identifier (name).
     */
    void beginClassDefinition(String oType, String identifier);

    /**
     * Signals the end of a class definition.
     */
    void endClassDefinition();

    /**
     * Signals that the given access specifier was found.
     * 
     * @param accessSpec The access specifier, e.g., "public".
     */
    void accessSpecifier(String accessSpec);

    /**
     * Signals the begin of a function declaration.
     */
    void beginFunctionDeclaration();

    /**
     * Signals the end of a function declaration.
     */
    void endFunctionDeclaration();

    /**
     * Reports the declaration specifiers; "inline", "virtual", "friend", etc.
     * 
     * @param declSpecs
     *            The declaration specifiers for the current declaration.
     */
    void declarationSpecifiers(List declSpecs);

    /**
     * Reports the type simple type specifiers (buit-in, like int, char,
     * unsigned char, etc).
     * 
     * @param sts The simple type specifiers,
     */
    void simpleTypeSpecifier(List sts);

    /**
     * Reports the declarator, of the entity being declared.
     * @param id The identifier of the construct being declared.
     */
    void directDeclarator(String id);

    /**
     * Reports the storage class specifier.
     * @param storageClassSpec The storage class specifier.
     */
    void storageClassSpecifier(String storageClassSpec);

    /**
     * Reports the type qualifier.
     * @param typeQualifier "const" or "volatile".
     */
    void typeQualifier(String typeQualifier);

    /**
     * Signals the begin of a function definition.
     */
    void beginFunctionDefinition();

    /**
     * Signals the end of a function definition.
     */
    void endFunctionDefinition();

    /**
     * Reports the identifier of the function being declared or defined.
     * @param identifier the function identifier.
     */
    void functionDirectDeclarator(String identifier);

    /**
     * Signals the begin of a parameter declaration.
     */
    void beginParameterDeclaration();

    /**
     * Signals the end of a parameter declaration.
     */
    void endParameterDeclaration();

    /**
     * 
     */
    void beginInitializer();

    /**
     * 
     */
    void endInitializer();

    /**
     * Signals the begin of a member declaration.
     */
    void beginMemberDeclaration();

    /**
     * Signals the end of a member declaration.
     */
    void endMemberDeclaration();

    /**
     * Signals the begin of a compound statement.
     */
    void beginCompoundStatement();

    /**
     * Signals the end of a compound statement.
     */
    void endCompoundStatement();

    /**
     * Receives a pointer symbol from the ptr_operator rule. In simple 
     * cases should be a "&" or a "*".
     * 
     * @param ptrSymbol the pointer symbol found
     */
    void ptrOperator(String ptrSymbol);

    /**
     * Signals the begin of the ptr_operator rule.
     */
    void beginPtrOperator();

    /**
     * Signals the end of the ptr_operator rule.
     */
    void endPtrOperator();

    /**
     * Found a pointer to member construct. Partial results, being the rest 
     * (if existing) delivered in a typeQualifier() call.
     * @param scopedItem The scoped item, such as A::B::C::
     * @param star The "*" symbol, just that the call is clear. In the future 
     * may be removed.
     */
    void ptrToMember(String scopedItem, String star);
}