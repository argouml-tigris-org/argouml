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


package org.argouml.i18n;
import java.util.*;
import org.argouml.util.*;

/* 
 * SPECIAL CHARACTER CODES //////////////////////////////////////////
 *
 * To avoid breaking the build, high characters must be Unicode-encoded
 * (\udddd notation). Just use the native2ascii tool (included with 
 * Java2). Example for Latin-1 to Unicode: 
 *     java sun.tools.native2ascii.Main -encoding ISO8859_1 native.java unicode.java
 *
 * To perform the reverse operation add -reverse. 
 *
 * See native2ascii documentation: <jdk-path>/docs/tooldocs/win32/native2ascii.html
 * See supported encodings: <jdk-path>/docs/guide/intl/encoding.doc.html 
 *
 *
 * LITTLE DICTIONARY ////////////////////////////////////////////////
 *
 * This translation is based on "El Lenguaje Unificado de Modelado", Booch, 
 * Rumbaugh, Jacobson. Almost every term is a direct translation. This is a 
 * sample..
 *
 * activity diagram: diagrama de actividades
 * aggregation (white diamond): agregaci\u00f3n (diamante blanco)
 * association: asociaci\u00f3n
 * attribute: atributo
 * branch transitions: transiciones de divisi\u00f3n
 * call-action: evento de llamada
 * class diagram: diagrama de clases
 * classifier: clasificador
 * collaboration diagram: diagrama de colaboraci\u00f3n
 * component diagram: diagrama de componentes
 * composite (black diamond): composici\u00f3n o agregaci\u00f3n compuesta (diamante negro)
 * connection: conexi\u00f3n
 * component: componente
 * deployment diagram: diagrama de despliegue
 * fork transitions: transiciones de bifurcaci\u00f3n
 * framework: framework
 * guard: guarda 
 * join transitions: transiciones de uni\u00f3n
 * node: nodo
 * object diagram: diagrama de objetos
 * operation: operaci\u00f3n
 * rol: papel
 * send-action: se\u00f1al 
 * sequence diagram: diagrama de secuencia
 * state diagram: diagrama de estados
 * trigger: disparador
 * use cases: casos de uso
 */

/**
 *   This class is the default member of a resource bundle that
 *   provides strings for UML related critiques and check lists.
 *   Source code contains some notes about the translation.
 *
 *   @author Curt Arnold
 *   @author Alejandro Ramirez
 *   @since 0.9
 *   @see java.util.ResourceBundle
 *   @see org.argouml.uml.cognitive.UMLCognitiveResourceBundle
 *   @see org.argouml.util.CheckResourceBundle
 *   @see org.argouml.uml.cognitive.critics.CrUML
 */
public class UMLCognitiveResourceBundle_es extends ListResourceBundle {

    private static final Object[][] _contents = {
        { "CrAssocNameConflict_head" ,
                "Resuelve conflicto en el nombre de la asociaci\u00f3n" },
        { "CrAssocNameConflict_desc" ,
                "Todo elemento del espacio de nombres debe tener un nombre \u00fanico. \n\nLa asignaci\u00f3n de nombres claros e inequ\u00edvocos es clave para la generaci\u00f3n de c\u00f3digo y el entendimiento y mantenibilidad del dise\u00f1o. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente los elementos y usa la solapa Propiedades para cambiar sus nombres." },
        { "CrAttrNameConflict_head" ,
                "Revisa los nombres de atributos para evitar conflictos" },
        { "CrAttrNameConflict_desc" ,
                "Los atributos deben tener nombres diferentes. Esto se debe  quiza a un atributo heredado. \n\nUsar nombres claros e inequ\u00edvocos es clave para la generaci\u00f3n de c\u00f3digo y el entendimiento y mantenibilidad del dise\u00f1o. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente el atributo en conflicto de la clase y cambia su nombre." },
        { "CrOperNameConflict_head" ,
                "Cambia los nombres o firmas en <ocl>self</ocl>" },
        { "CrOperNameConflict_desc" ,
                "Dos operaciones tienen exactamente la misma firma.  Deber\u00edan ser distintas.  Una firma es una combinaci\u00f3n del nombre de la operaci\u00f3n, y sus tipos de parametros. \n\nEvitar firmas en conflicto es clave para la generaci\u00f3n de c\u00f3digo y el entendimiento y mantenibilidad del dise\u00f1o. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente las operaciones en conflicto de esta clase y cambia su nombre o sus parametros." },
        { "CrCircularAssocClass_head" ,
                "MAssociation circular" },
        { "CrCircularAssocClass_desc" ,
                "Las AssociationClasses no pueden incluir papeles que se refieran directamente a la MAssociationClass." },
        { "CrCircularInheritance_head" ,
                "Quita la herencia circular del <ocl>self</ocl>" },
        { "CrCircularInheritance_desc" ,
                "Las relaciones de herencia no pueden tener ciclos. \n\nSe necesita una jerarquia legal de clases para la generaci\u00f3n de c\u00f3digo y la correcci\u00f3n del dise\u00f1o. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente uno de las flechas de generalizaci\u00f3n en el ciclo y quitala." },
        { "CrCircularComposition_head" ,
                "Quita la composici\u00f3n circular" },
        { "CrCircularComposition_desc" ,
                "Las relaciones de composici\u00f3n (diamantes negros) no pueden tener ciclos. \n\nUna jerarqu\u00eda legal de agregaci\u00f3n se necesita para la generaci\u00f3n de c\u00f3digo y la correcci\u00f3n del dise\u00f1o. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente una de las asociaciones en el ciclo y quitala o cambia su agregaci\u00f3n a otra que no sea composici\u00f3n." },
        { "CrCrossNamespaceAssoc_head" ,
                "Papel agregado en una MAssociation N-caminos" },
        { "CrCrossNamespaceAssoc_desc" ,
                "Cada clase o MInterface implicado en una MAssociation deber\u00eda estar en el MNamespace de la MAssociation.\n" },
        { "CrDupParamName_head" ,
                "Nombre de parametro duplicado" },
        { "CrDupParamName_desc" ,
                "Cada parametro de una operaci\u00f3n debe tener un nombre \u00fanico. \n\nUsar nombres claros e inequ\u00edvocos es clave para la generaci\u00f3n de c\u00f3digo y el entendimiento y mantenibilidad del dise\u00f1o. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o renombra manualmente uno de los parametros de esta operaci\u00f3n." },
        { "CrDupRoleNames_head" ,
                "Cambia los nombres de papel de <ocl>self</ocl>" },
        { "CrDupRoleNames_desc" ,
                "La MAssociation <ocl>self</ocl> tiene dos papeles con nombres en conflicto. \n\nUsar nombres claros e inequ\u00edvocos es clave para la generaci\u00f3n de c\u00f3digo y el entendimiento y mantenibilidad del dise\u00f1o. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente <ocl>self</ocl> y usa la solapa de propiedades para cambiar los nombres de papel." },
        { "CrFinalSubclassed_head" ,
                "Quita la palabra clave final y las subclases." },
        { "CrFinalSubclassed_desc" ,
                "En Java, la palabra clave 'final' indica que una clase no deber\u00eda tener subclases. .  Esta clase esta etiquetada final y tiene subclases.\n\nUna jerarqu\u00eda de clases meditada y que permita la adecuada subclasificaci\u00f3n es un parte importante para conseguir un dise\u00f1o comprensible y mantenible.\n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente la clase y cambia su superclase, o selecciona la superclase y usa la solapa de propiedades para quitar la palabra clave 'final'." },
        { "CrIllegalGeneralization_head" ,
                "MGeneralization ilegal" },
        { "CrIllegalGeneralization_desc" ,
                "Los elementos MModel solo pueden ser heredados por otros del mismo tipo. \n\nSe necesita una jerarqu\u00eda correcta para la generaci\u00f3n de c\u00f3digo y la correcci\u00f3n del dise\u00f1o.Se necesita una jerarqu\u00eda correcta para la generaci\u00f3n de c\u00f3digo y la correcci\u00f3n del dise\u00f1o.\n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente la flecha de generalizaci\u00f3n y quitala." },
        { "CrAlreadyRealizes_head" ,
                "Quita el realiza innecesario de <ocl>self</ocl>" },
        { "CrAlreadyRealizes_desc" ,
                "La clase seleccionada ya realiza indirectamente el MInterface {item.extra}.  No hay necesidad de realizarlo otra vez.\n\nSimplificar el dise\u00f1o siempre es bueno.Simplificar el dise\u00f1o siempre es bueno.  Puedes descartar este elemento \"por hacer\" si quieres destacar que la clase seleccionada realiza este MInterface.\n\nPara arreglar esto, selecciona la Realizaci\u00f3n (l\u00ednea punteada con una flecha blanca triangular) y pulsa la tecla \"Delete\"." },
        { "CrInterfaceAllPublic_head" ,
                "Las operaciones de los interfaces deben ser p\u00fablicas" },
        { "CrInterfaceAllPublic_desc" ,
                "Los Interfaces especifican las operaciones que otras clases deben implementar.  Deben ser p\u00fablicos. \n\nUn conjunto de interfaces bien dise\u00f1ado es un buen modo de definir las posibles extensiones de un framework de clases. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente las operaciones del interfaz y usa la solapa de propiedades para hacerlas publicas." },
        { "CrInterfaceOperOnly_head" ,
                "Los interfaces solo deben tener operaciones" },
        { "CrInterfaceOperOnly_desc" ,
                "Los Interfaces especifican las operaciones que otras clases deben implementar.  No implementan esas operaciones ellos mismos , y no pueden tener atributos. \n\nUn conjunto de interfaces bien dise\u00f1ado es un buen modo de definir las posibles extensiones de un framework de clases. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente el interfaz y usa la solapa Propiedades para quitar todos los atributos." },
        { "CrMultipleAgg_head" ,
                "Papeles agregados multiples" },
        { "CrMultipleAgg_desc" ,
                "Solo un papel de una MAssociation puede tener un agregado o compuesto.\n\nUna jerarqu\u00eda es-parte-de clara y consistente es clave para la claridad del dise\u00f1o, almacenamiento de objetos, e implementaci\u00f3n de m\u00e9todos recursivos.\nPara arreglar esto, selecciona la MAssociation y establece algunos de sus papeles de agregaci\u00f3n a Ninguno." },
        { "CrNWayAgg_head" ,
                "Papel agregado en una relaci\u00f3n de N-caminos MAssociation" },
        { "CrNWayAgg_desc" ,
                "Las asociaciones de 3 o m\u00e1s caminos no pueden tener aggregate ends.\n\nUna consistente y clara jerarquia es-parte-de es clave para la claridad del dise\u00f1o, el almacenamiento mantenible de objetos, y la implementaci\u00f3n de m\u00e9todos recursivos.\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente the MAssociation y establece todos los role aggregations a None." },
        { "CrNavFromInterface_head" ,
                "Quita la navegaci\u00f3n del MInterface <ocl>self</ocl>" },
        { "CrNavFromInterface_desc" ,
                "Las asociaciones que implican un MInterface no pueden ser navegables en la direcci\u00f3n desde el MInterface.  Esto se debe a que los interfaces contienen solo declaraciones de operaciones y no pueden contener punteros a otros objetos.\n\nEsta parte del dise\u00f1o deber\u00eda cambiarse antes de generar el c\u00f3digo de este dise\u00f1o.  Si generas c\u00f3digo antes de arreglar este problema, el c\u00f3digo no se ajustar\u00e1 al dise\u00f1o.\n\nPara arreglar esto, selecciona la MAssociation y usa la solapar \"Propiedades\" para tab to desactivar Navigable para el extremo que toca el MInterface.  La MAssociation deber\u00eda parecer entonces con una flecha apuntado fuera del MInterface." },
        { "CrUnnavigableAssoc_head" ,
                "Haz navegable a <ocl>self</ocl>" },
        { "CrUnnavigableAssoc_desc" ,
                "La MAssociation <ocl>self</ocl> no es navegable en ninguna direcci\u00f3n. Todas las asociaciones deber\u00edan ser navegables al menos en una direcci\u00f3n.\n\nEstablecer la navegabilidad de las asociaciones permite al c\u00f3digo acceder a los datos siguiendo los punteros. \n\nPara arreglar esto, selecciona la asociaci\u00f3n \"<ocl>self</ocl>\" en el diagrama o panel de navegaci\u00f3n y haz click en la solapa de propiedades.  Luego usa los checkboxes al fondo del panel de propiedades para activar la navegabilidad. " },
        { "CrNameConflictAC_head" ,
                "El nombre del papel esta en conflicto con el miembro" },
        { "CrNameConflictAC_desc" ,
                "Los nombres de papel MAssociation de la MAssociationClass no deben estar en conflicto con los nombres de las caracter\u00edsticas estructurales (por ejemplo las variables de instancia) de la clase.\n" },
        { "CrMissingClassName_head" ,
                "Escoge un nombre" },
        { "CrMissingClassName_desc" ,
                "Cada clase e interfaz de un paquete deber\u00eda tener un nombre. \n\nLa asignaci\u00f3n de nombres claros e inequ\u00edvocos es clave para la generaci\u00f3n de c\u00f3digo y el entendimiento y mantenibilidad del dise\u00f1o. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente la clase y usa la solapa Propiedades para asignarle un nombre." },
        { "CrMissingAttrName_head" ,
                "Escoge un nombre" },
        { "CrMissingAttrName_desc" ,
                "Todo atributo debe tener un nombre.. \n\nLa asignaci\u00f3n de nombres claros e inequ\u00edvocos es clave para la generaci\u00f3n de c\u00f3digo y el entendimiento y mantenibilidad del dise\u00f1o. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente el atributo y usa la solapa Propiedades para darle un nombre." },
        { "CrMissingOperName_head" ,
                "Escoge un nombre" },
        { "CrMissingOperName_desc" ,
                "EToda operaci\u00f3n debe tener un nombre.. \n\nLa asignaci\u00f3n de nombres claros e inequ\u00edvocos es clave para la generaci\u00f3n de c\u00f3digo y el entendimiento y mantenibilidad del dise\u00f1o. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente el atributo y usa la solapa Propiedades para darle un nombre." },
        { "CrMissingStateName_head" ,
                "Escoge un nombre" },
        { "CrMissingStateName_desc" ,
                "Todo estado de una m\u00e1quina de estados deber\u00eda tener un nombre. \n\nLa asignaci\u00f3n de nombres claros e inequ\u00edvocos es clave para la generaci\u00f3n de c\u00f3digo y el entendimiento y mantenibilidad del dise\u00f1o. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente el estado y usa la solapa Propiedades para darle un nombre, o selecciona el estado y escribe un nombre." },
        { "CrNoInstanceVariables_head" ,
                "A\u00f1ade variables de instancia a <ocl>self</ocl>" },
        { "CrNoInstanceVariables_desc" ,
                "Aun no has especificado variables de instancia para <ocl>self</ocl>. Normalmente las clases tienen variables de instancia que almacenan la informaci\u00f3n para cada instancia. Las clases que solo proporcionan atributos y m\u00e9todos est\u00e1ticos deber\u00edan ser estereotipadas con <<utility>>.\n\nQuiza deber\u00edas definir las variables de instancia para completar la parte de representaci\u00f3n de la informaci\u00f3n de tu dise\u00f1o. \n\nPara arreglar esto, pulsa el bot\u00f3n \"Siguiente>\", o a\u00f1ade variables de instancia haciendo doble click en <ocl>self</ocl> en el panel de navegaci\u00f3n o usando el menu Crear para crear un nuevo atributo. " },
        { "CrNoAssociations_head" ,
                "A\u00f1ade asociaciones a <ocl>self</ocl>" },
        { "CrNoAssociations_desc" ,
                "Aun no has especificado asociaciones para <ocl>self</ocl>. Normalmente las clases, actores, y casos de uso estan asociadas con otras. \n\nDefinir las asociaciones entre objetos es una parte importante del dise\u00f1o. \n\nPara arreglar esto, pulsa el bot\u00f3n \"Siguiente>\", o a\u00f1ade las asociaciones manualmente pulsando en la herramienta de asociaci\u00f3nen la barra de tareas y arrastrando desde <ocl>self</ocl> a otro nodo. " },
        { "CrNonAggDataType_head" ,
                "Envuelve MDataType" },
        { "CrNonAggDataType_desc" ,
                "Los DataTypes no son clases y no pueden ser asociadas con clases, a menos que el MDataType sea parte de una agregaci\u00f3n compuesta (diamante negro). \n\nUn buen dise\u00f1o OO depende de elecciones cuidadosas acerca de que entidades representar como objetos y cuales representar como atributos de objetos.\n\nPara arreglar esto, use el bot\u00f3n \"Siguiente>\" , o reemplace manualmente el MDataType con una clase, o cambie la asociaci\u00f3n de agregaci\u00f3n a una clase.\n" },
        { "CrOppEndConflict_head" ,
                "Renombra los papeles MAssociation" },
        { "CrOppEndConflict_desc" ,
                "Dos papeles de <ocl>self</ocl> tienen el mismo nombre. Los papeles deben tener nombres distintos.  Esto quiza sea debido a un atributo heredado. \n\nUsar nombres claros y no ambiguos es clave para la generaci\u00f3n de c\u00f3digo y la producci\u00f3n de un dise\u00f1o f\u00e1cil de entender y mantener.\n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente el papel en conflicto en el extremo de la asociaci\u00f3n de esta clase y cambia su nombre." },
        { "CrParamTypeNotImported_head" ,
                "Importa el tipo MParameter en la clase" },
        { "CrParamTypeNotImported_desc" ,
                "El tipo del parametro de cada operaci\u00f3n debe ser visible y haber sido importado en la clase que posee dicha operaci\u00f3n.\n\nImportar clases es necesario para la generaci\u00f3n de c\u00f3digo. Una buena modularizaci\u00f3n de clases en paquetes es clave para conseguir un dise\u00f1o f\u00e1cil de entender.\n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o a\u00f1ade manualmente un import a la clase que posee esta operaci\u00f3n." },
        { "CrUselessAbstract_head" ,
                "Define (Sub)Clases concretas" },
        { "CrUselessAbstract_desc" ,
                "<ocl>self</ocl> resulta superfluo en el sistema porque ni \u00e9l ni sus subclases pueden tener instancias. \n\nPara areglar este problem : (1) define subclases concretas que implementen el interface de esta clase; o (2) haz concreto <ocl>self</ocl> o una de sus subclases ." },
        { "CrUselessInterface_head" ,
                "Define una class para implementar <ocl>self</ocl>" },
        { "CrUselessInterface_desc" ,
                "<ocl>self</ocl> no es usado porque ninguna clase lo implementa.\n\nPara arreglar este problema, pulsa el bot\u00f3n \"Siguiente>\" o usa el bot\u00f3n \"Class\" de la barra de herramientas para definir clases y el bot\u00f3n \"Realizes\" para crear una relaci\u00f3n de la clase al interfaz realzado." },
        { "CrDisambigClassName_head" ,
                "Escoge un nombre \u00fanico para <ocl>self</ocl>" },
        { "CrDisambigClassName_desc" ,
                "Toda clase e interfaz de un paquete debe tener un nombre \u00fanico. Hay al menos dos elementos en este paquete llamados \"<ocl>self</ocl>\".\n\nLa asignaci\u00f3n de nombres claros e inequ\u00edvocos es clave para la generaci\u00f3n de c\u00f3digo y el entendimiento y mantenibilidad del dise\u00f1o. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente una de las clases en conflicto y usa la solapa Propiedades para cambiar sus nombres." },
        { "CrDisambigStateName_head" ,
                "Escoge un nombre \u00fanico para <ocl>self</ocl>" },
        { "CrDisambigStateName_desc" ,
                "Todo estado de una m\u00e1quina de estados debe tener un nombre \u00fanico. Hay al menos dos estados en esta m\u00e1quina llamados \"<ocl>self</ocl>\".\n\nLa asignaci\u00f3n de nombres claros e inequ\u00edvocos es clave para la generaci\u00f3n de c\u00f3digo y el entendimiento y mantenibilidad del dise\u00f1o. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente uno de los estados en conflicto y usa la solapa \"Propiedades\" para cambiar sus nombres." },
        { "CrConflictingComposites_head" ,
                "Quita las agregaciones compuestas en conflicto" },
        { "CrConflictingComposites_desc" ,
                "Un papel de composici\u00f3n (diamante negro) de una association indica que las instancias de esta clase contienen instancias de las associated classes. Puesto que cada instancia solo puede ser contenida en uno de los otros objetos, cada objeto puede ser 'parte' en al menos una relaci\u00f3n es-parte-de.\n\nUn buen dise\u00f1o OO depende de construir buenas relaciones es-parte-de.\n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o cambia manualmente una de las asociaciones para que tenga multiplicidad 0..1 o 1..1, u otra clase de agregaci\u00f3n (por ejemplo, un diamante blanco es menos estricto), o quita una de las asociaciones" },
        { "CrTooManyAssoc_head" ,
                "Reduce las asociaciones en <ocl>self</ocl>" },
        { "CrTooManyAssoc_desc" ,
                "Hay demasiadas asociaciones en la clase <ocl>self</ocl>.  Cuando una clase es centraliza demasiado el dise\u00f1o puede convertirse en un cuello de botella que debe ser acutalizado frecuentemente. \n\nDefinir las asociaciones entre los objetos es una parte importante del dise\u00f1o. \n\nPara arreglar esto, pulsa el bot\u00f3n \"Siguiente>\", o quita las asociaciones manualmente pulsando en una asociaci\u00f3n en el panel de navegaci\u00f3n o diagrama y pulsando la tecla \"Del\". " },
        { "CrTooManyAttr_head" ,
                "Reduce los atributos de <ocl>self</ocl>" },
        { "CrTooManyAttr_desc" ,
                "Hay demasiados atributos en la clase <ocl>self</ocl>.  Cuando una clase es centraliza demasiado el dise\u00f1o puede convertirse en un cuello de botella que debe ser acutalizado frecuentemente. \n\nDefinir los atributos de los objetos es una parte importante del dise\u00f1o. \n\nPara arreglar esto, pulsa el bot\u00f3n \"Siguiente>\", o quita manualmente los atributos haciendo doble click en el compartimiento de la clase realzada en el diagrama y quitando la l\u00ednea de texto para un atributo. " },
        { "CrTooManyOper_head" ,
                "Reduce las operaciones de <ocl>self</ocl>" },
        { "CrTooManyOper_desc" ,
                "Hay demasiadas operaciones en la clase <ocl>self</ocl>.  Cuando una clase es centraliza demasiado el dise\u00f1o puede convertirse en un cuello de botella que debe ser acutalizado frecuentemente. \n\nDefinir las operaciones de un objeto es una parte importante del dise\u00f1o. \n\nPara arreglar esto, pulsa el bot\u00f3n \"Siguiente>\", o quita manualmente los atributos haciendo doble click en el compartiemiento de la operaci\u00f3n de la clase realzada en el diagrama y quitando la l\u00ednea de texto para la operaci\u00f3n. " },
        { "CrTooManyStates_head" ,
                "Reduce estados en la m\u00e1quina <ocl>self</ocl>" },
        { "CrTooManyStates_desc" ,
                "Hay demasiados estados en <ocl>self</ocl>.  Si una m\u00e1quina de estados tiene demasiados estados puede resultar dif\u00edcil de entender. \n\nDefinir un conjunto de estados f\u00e1cil de entender es una parte importante del dise\u00f1o. \n\nPara arreglar esto, pulsa el bot\u00f3n \"Siguiente>\", o quita manualmente los estados pulsando en los estados en el panel de navegaci\u00f3n o el diagrama y pulsando la tecla \"Del\".  O podr\u00edas anidar estados..." },
        { "CrTooManyTransitions_head" ,
                "Reduce transitions en <ocl>self</ocl>" },
        { "CrTooManyTransitions_desc" ,
                "Hay demasiadas transiciones en el estado <ocl>self</ocl>.  Cuando un estado queda demasiado centralizado en la m\u00e1quina puede convertirse en un cuello de botella que requiera frecuentes actualizaciones. \nDefinir las transiciones entre estados es una parte importante del dise\u00f1o. \n\nPara arreglar esto, pulsa el bot\u00f3n \"Siguiente>\", o quita manualmente las transiciones pulsando en una transici\u00f3n en el panel de navegaci\u00f3n o diagramay pulsando la tecla \"Del\". " },
        { "CrTooManyClasses_head" ,
                "Reduce clases en el diagrama <ocl>self</ocl>" },
        { "CrTooManyClasses_desc" ,
                "Hay demasiados classes in <ocl>self</ocl>.  Si un diagrama de clases tiene demasiadas clases puede resultar dif\u00edcil de entender. \n\nDefinir un conjunto de diagramas de clases f\u00e1cil de entender es una parte importante del dise\u00f1o. \n\nPara arreglar esto, pulsa el bot\u00f3n \"Siguiente>\", o quita las clases manualmente pulsando en una clase en el panel de navegaci\u00f3n o diagrama y pulsando la tecla \"Del\".  O podr\u00edas hacer un nuevo diagrama..." },
        { "CrNoTransitions_head" ,
                "A\u00f1ade transiciones a <ocl>self</ocl>" },
        { "CrNoTransitions_desc" ,
                "MState <ocl>self</ocl> no tiene transiciones entrantes o salientes. Normalmente los estados tienen transiciones entrantes y salientes. \n\nDefinir transiciones de estados completas es necesario para la parte de especificaci\u00f3n de comportamiento de tu dise\u00f1o.  \n\nPara arreglar esto, pulsa el bot\u00f3n \"Siguiente>\", o a\u00f1ade transiciones manualmente pulsando en la herramienta de transiciones en la barra de herramientas y arrastrando desde otro estado a <ocl>self</ocl> o desde <ocl>self</ocl> a otro estado. " },
        { "CrNoIncomingTransitions_head" ,
                "A\u00f1ade transiciones entrantes a <ocl>self</ocl>" },
        { "CrNoIncomingTransitions_desc" ,
                "MState <ocl>self</ocl> no tiene transiciones entrantes. Normalmente los estados tienen transiciones entrantes y salientes. \n\nDefinir transiciones de estados completas es clave para completar la especificaci\u00f3n de comportamiento del dise\u00f1o. Sin transiciones entrantes, este estado nunca puede ser alcanzado.\n\nPara arreglar esto, pulsa el bot\u00f3n \"Siguiente>\", o a\u00f1ade manualmente transiciones pulsando en la herramienta de transiciones en la barra de herramientas y arrastrando esde otro estado a <ocl>self</ocl>. " },
        { "CrNoOutgoingTransitions_head" ,
                "A\u00f1ade transiciones salientes desde <ocl>self</ocl>" },
        { "CrNoOutgoingTransitions_desc" ,
                "MState <ocl>self</ocl> no tiene transacciones salientes. Normalmente los estados tienen transiciones entrantes y salientes. \n\nDefinir transacciones de estados completas es necesario para completar la especificaci\u00f3n de comportamiento del dise\u00f1o.  Sin transiciones salientes este estado esta \"muerto\" y nunca se puede salir de \u00e9l.\n\nPara arreglar esto, pulsa el bot\u00f3n \"Siguiente>\", o a\u00f1ade manualmente transiciones pulsando en la herramienta de transiciones en la barra de herramientas y arrastrando desde otro estado a <ocl>self</ocl>. " },
        { "CrMultipleInitialStates_head" ,
                "Quita los estados inicialies que sobran" },
        { "CrMultipleInitialStates_desc" ,
                "Hay multiples y ambiguos estados iniciales en esta m\u00e1quina. Normalmente cada m\u00e1quina de estados o composite state tiene un estado inicial. \n\nDefinir estados no ambiguos es necesario para completar la parte de definici\u00f3n de comportamiento del dise\u00f1o.\n\nPara arreglar esto, pulsa el bot\u00f3n \"Siguiente>\", o selecciona manualmente uno de los estados y quitalo. " },
        { "CrNoInitialState_head" ,
                "Pon un MState inicial" },
        { "CrNoInitialState_desc" ,
                "No hay estado inicial en esta m\u00e1quina o composite state. Normalmente cada m\u00e1quina o composite state tiene un estado inicial. \n\nDefinir estados no ambiguos es necesario para completar la definici\u00f3n de comportamiento del dise\u00f1o.\n\nPara arreglar esto, pulsa el bot\u00f3n \"Siguiente>\", o a\u00f1ade manualmente un estado inicial usando la barra de herramientas y a\u00f1adelo al diagrama. " },
        { "CrNoTriggerOrGuard_head" ,
                "A\u00f1ade un disparador o guarda a la transici\u00f3n" },
        { "CrNoTriggerOrGuard_desc" ,
                "La transici\u00f3n destacada es incompleta porque no tiene disparador ni condici\u00f3n de guarda.  Los disparadores son eventos que producen una transici\u00f3n. Las condiciones de guarda deben ser verdaderas para que la transici\u00f3n se produzca.  Si solo se usa una guarda, la transici\u00f3n se produce cuando la condici\u00f3n es verdadera.\n\nEste problema debe ser resuelto para completar la m\u00e1quina de estados.\n\nPara arreglar esto, selecciona la transition y usa la solapa \"Propiedades\", o selecciona la transition y escribe texto con el formato:\nDISPARADOR [GUARDA] / ACCI\u00d3N\ndonde DISPARADOR es el nombre de un evento, GUARDA es una expresi\u00f3n booleana, y ACCI\u00d3N es una acci\u00f3n a realizar cuando se produce MTransition.  Las tres partes son opcionales." },
        { "CrNoGuard_head" ,
                "A\u00f1ade MGuard a la transici\u00f3n" },
        { "CrNoGuard_desc" ,
                "La transici\u00f3n destacada esta incompleta porque no tiene guard condition.  Las condiciones MGuard deben ser verdaderas para que la transici\u00f3n se produzca.  Si solo se usa una guarda, la transici\u00f3n se produce cuando la condici\u00f3n es verdadera.\n\nEste problema debe ser resuelto para completar la m\u00e1quina de estado.\n\nPara arreglar esto, selecciona la MTransition y usa la solapa \"Propiedades\", o selecciona la MTransition y escribe algun texto con el formato:\n[GUARDA]\nDonde GUARDA es una expresi\u00f3n booleana." },
        { "CrInvalidFork_head" ,
                "Cambia las transiciones de bifurcaci\u00f3n" },
        { "CrInvalidFork_desc" ,
                "Este estado de bifurcaci\u00f3n tiene un n\u00famero invalido de transiciones. Normalmente los fork states tienen una transici\u00f3n entrante y dos o m\u00e1s salientes. \n\nDefinir transiciones de estado correctas es neceario para completar la especificaci\u00f3n de comportamiento del dise\u00f1o.  \n\nPara arreglar esto, pulsa el bot\u00f3n \"Siguiente>\", o quita manualmente las transiciones pulsando en una transici\u00f3n en el diagrama y pulsando la tecla Delete. " },
        { "CrInvalidJoin_head" ,
                "Cambia las transiciones de uni\u00f3n" },
        { "CrInvalidJoin_desc" ,
                "Este estado de uni\u00f3n tiene un n\u00famero invalido de transiciones. Normally join states tienen dos o m\u00e1s transiciones entrantes y una saliente. \n\nDefinir correctamente la transici\u00f3n de estados es necesario para completar la especificaci\u00f3n de comportamiento del dise\u00f1o.  \n\nPara arreglar esto, pulsa el bot\u00f3n \"Siguiente>\", o quita las transiciones manualmente pulsando en la transici\u00f3n en el diagrama y pulsando la tecla Delete. " },
        { "CrInvalidBranch_head" ,
                "Cambia las transiciones de divisi\u00f3n" },
        { "CrInvalidBranch_desc" ,
                "Este estadod de divisi\u00f3n tiene un n\u00famero invalido de transiciones. Normalmente los estados de divisi\u00f3n tienen una transici\u00f3n entrante y dos o m\u00e1s salientes. \n\nDefinir correctamente la transici\u00f3n de estados es necesario para completar la especificaci\u00f3n de comportamiento del dise\u00f1o.  \n\nPara arreglar esto, pulsa el bot\u00f3n \"Siguiente>\", o quita transitions manualmente pulsando en la transici\u00f3n en el diagrama y pulsando la tecla Delete, o a\u00f1ade transiciones usando la herramienta de transiciones." },
        { "CrEmptyPackage_head" ,
                "A\u00f1ade elementos al paquete <ocl>self</ocl>" },
        { "CrEmptyPackage_desc" ,
                "Aun no has puesto nada en el paquete <ocl>self</ocl>. Normalmente los paquetes contienen grupos de clases relacionadas.\n\nDefinir y usar paquetes es clave para un dise\u00f1o mantenible. \n\nPara arreglar esto, selecciona el paquete <ocl>self</ocl> en el panel de navegaci\u00f3n y a\u00f1ade los diagramas o modelos como clases o casos de uso. " },
        { "CrNoOperations_head" ,
                "A\u00f1ade operaciones a <ocl>self</ocl>" },
        { "CrNoOperations_desc" ,
                "Aun no has especificado operaciones para <ocl>self</ocl>. Normalmente las clases proporcionan operaciones que definen su comportamiento. \n\nNecesitas definir operaciones para completar la parte de especificaci\u00f3n de la comportamiento del dise\u00f1o. \n\nPara arreglar esto, pulsa el bot\u00f3n \"Siguiente>\", o a\u00f1ade operaciones manualmente pulsando en <ocl>self</ocl> en el panel de navegaci\u00f3n y usando el menu\u00fa Crear para hacer nuevas operaciones. " },
        { "CrConstructorNeeded_head" ,
                "A\u00f1ade constructor a <ocl>self</ocl>" },
        { "CrConstructorNeeded_desc" ,
                "Aun no has definido un constructor para la clase <ocl>self</ocl>. Los constructores inicializan nuevas instancias dotando de valores v\u00e1lidos a sus atributos.  Esta clase probablemente necesite un constructor porque no todos sus atributos tienen valores iniciales. \n\nDefinir buenos constructores es clave para establecer class invariants, y class invariants son una poderosa ayuda para escribir c\u00f3digo s\u00f3lido. \n\nPara arreglar esto, pulsa el bot\u00f3n \"Siguiente>\", o a\u00f1ade manualmente un constructor pulsando en <ocl>self</ocl> en el panel de navegaci\u00f3n y usando el men\u00fa Create para hacer un constructor nuevo. " },
        { "CrNameConfusion_head" ,
                "Revisa los nombres para evitar confusiones" },
        { "CrNameConfusion_desc" ,
                "Los nombres deber\u00edan distinguirse claramente unos de otros. Estos dos nombres son tan parecidos que pueden ser confundidos.\n\nLa asignaci\u00f3n de nombres claros e inequ\u00edvocos es clave para la generaci\u00f3n de c\u00f3digo y el entendimiento y mantenibilidad del dise\u00f1o. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente los elementos y usa la solapa Propiedades para cambiar sus nombres.  Evita usar nombres que solo difieren en distinciones may\u00fasculas-min\u00fasculas, o en subrayados, o en un solo car\u00e1cter." },
        { "CrMergeClasses_head" ,
                "Considera combinar las clases" },
        { "CrMergeClasses_desc" ,
                "La clase realzada, <ocl>self</ocl>, solo participa en una asociaci\u00f3n y esa asociaci\u00f3n es uno-a-uno con otra clase.  Puesto que las instancias de estas clases deben ser creadas y destruidas juntas, combinar estas clases puede simplificar el dise\u00f1o sin perdida de poder representativo.  Sin embargo, quiza la clase resultante sea demasiado grande y compleja, en cuyo caso ser\u00e1 mejor mantenerla separada.\n\nSiempre es importante organizar clases para administrar la complejidad del dise\u00f1o, especialmente cuando el dise\u00f1o ya es complejo de por s\u00ed. \n\nPara arreglar esto, pulsa en el bot\u00f3n \"Siguiente>\", o a\u00f1ade manualmente los atributos y operaciones de la clase realzada a la otra clase, luego quita la clase realzada del proyecto. " },
        { "CrSubclassReference_head" ,
                "Quita la referencia a la subclase especifica" },
        { "CrSubclassReference_desc" ,
                "La clase <ocl>self</ocl> tiene una referencia a una de sus subclases. Normalmente todas las subclases deber\u00edan ser tratadas de modo similar por la superclase.  Esto permite a\u00f1adir nuevas subclases sin modificar la superclase. \n\nDefinir las asociaciones entre objetos es una parte importante del dise\u00f1o.  Algunos patrones de asociaciones son m\u00e1s f\u00e1ciles e mantener que otros, dependiendo de la naturaleza de los cambios futuros. \n\nPara arreglar esto, pulsa el bot\u00f3n \"Siguiente>\", o quita manualmente la asociaci\u00f3n pulsando en ella en el diagrama y pulsando Delete. " },
        { "CrComponentWithoutNode_head" ,
                "Los componentes estan normalmente dentro de nodos" },
        { "CrComponentWithoutNode_desc" ,
                "Hay nodos en el diagrama. Por tanto, tienes un diagrama de despliegue, y en los diagramas de despliegue los componentes residen normalmente en nodos." },
        { "CrCompInstanceWithoutNode_head" ,
                "ComponentInstances estan normalmente dentro de nodos" },
        { "CrCompInstanceWithoutNode_desc" ,
                "Hay instancias de nodos en el diagrama. Por tanto, tienes un real diagrama de despliegue, y en los diagramas de despliegue las instancias de componentes residen normalmente en instancias de nodos." },
        { "CrClassWithoutComponent_head" ,
                "Las clases estan normalmente dentro de componentes" },
        { "CrClassWithoutComponent_desc" ,
                " En los diagramas de despliegue las clases estan normalmente dentro de componentes" },
        { "CrInterfaceWithoutComponent_head" ,
                "Los interfaces estan normalmente dentro de componentes" },
        { "CrInterfaceWithoutComponent_desc" ,
                " En Deployment-diagrams los interfaces estan normalmente dentro de componentes" },
        { "CrObjectWithoutComponent_head" ,
                "Los objetos estan normalmente dentro de componentes" },
        { "CrObjectWithoutComponent_desc" ,
                "En los diagramas de despliegue los objetos estan normalmente dentro de componentes or component-instances" },
        { "CrNodeInsideElement_head" ,
                "Los nodos normalmente no tienen contenedores" },
        { "CrNodeInsideElement_desc" ,
                " Los nodos normalmente no estan dentro de otros elementos. Los nodos representan objetos f\u00edsicos en tiempo de ejecuci\u00f3n con un recurso de procesamiento, generalmente tienen al menos memoria y capacidad de procesamiento." },
        { "CrNodeInstanceInsideElement_head" ,
                "Las NodeInstances normalmente no tienen contenedores" },
        { "CrNodeInstanceInsideElement_desc" ,
                "Las NodeInstances normalmente no estan dentro de otros elementos. Las instancias de nodos representan objetos f\u00edsicos en tiempo de ejecuci\u00f3n con un recurso de procesamiento, generalmente tienen al menos memoria y capacidad de procesamiento." },
        { "CrWrongLinkEnds_head" ,
                "Los LinkEnds no tienen el mismo emplazamiento" },
        { "CrWrongLinkEnds_desc" ,
                " En los diagramas de despliegue los objetos pueden residir en componentes o en instancias de componentes. Por tanto, no es posible tener dos objetos conectados con un Link, estando un objeto en un componente y otro en una instancia de un componente.\n\n Para arreglar esto quita un objetos de los dos conectados de su emplazamiento a un elemento que tenga el mismo tipo que el emplazamiento del otro objeto" },
        { "CrInstanceWithoutClassifier_head" ,
                "Establece un clasificador" },
        { "CrInstanceWithoutClassifier_desc" ,
                "Las instancias tienen un clasificador" },
        { "CrCallWithoutReturn_head" ,
                "Acci\u00f3n de retorno ausente" },
        { "CrCallWithoutReturn_desc" ,
                "Toda se\u00f1al o evento de llamada requiere una acci\u00f3n de retorno, pero este enlace no tiene una acci\u00f3n de retorno.\n" },
        { "CrReturnWithoutCall_head" ,
                "Ausencia de se\u00f1al o evento de llamada" },
        { "CrReturnWithoutCall_desc" ,
                "Toda acci\u00f3n de retorno requiere una se\u00f1al o evento de llamada, pero este enlace no tiene su correspondiente se\u00f1al o evento de llamada.\n" },
        { "CrLinkWithoutStimulus_head" ,
                "No hay est\u00edmulo en estos enlaces" },
        { "CrLinkWithoutStimulus_desc" ,
                "En los diagramas de secuencia un objeto emisor env\u00eda un est\u00edmulo a un objeto que lo recibe a traves de un enlace. El enlace solo es la comunicaci\u00f3n-conexi\u00f3n, asi que se necesita un est\u00edmulo." },
        { "CrSeqInstanceWithoutClassifier_head" ,
                "Establece un clasificador" },
        { "CrSeqInstanceWithoutClassifier_desc" ,
                " Las instancias tienen un clasificador" },
        { "CrStimulusWithWrongPosition_head" ,
                "Posici\u00f3n incorrecta de estos estimulos" },
        { "CrStimulusWithWrongPosition_desc" ,
                "En los diagramas de secuencia la parte de env\u00edo de la comunicaci\u00f3n-conexi\u00f3n de estos est\u00edmulos es conectada al comienzo de una activaci\u00f3n. Para ser un emisor, un objeto ha de tener primero un foco-de-control." },
        { "CrUnconventionalOperName_head" ,
                "Escoge un nombre de MOperation m\u00e1s adecuado" },
        { "CrUnconventionalOperName_desc" ,
                "Normalmente los nombres de operaci\u00f3n comienzan con una letra min\u00fascula. El nombre '<ocl>self</ocl>' no es convencional porque no comienza por min\u00fascula.\n\nSeguir buenas convenciones de nombres facilita el entendimiento y mantenibilidad del dise\u00f1o. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente <ocl>self</ocl> y usa la solapa Propiedades para darle un nombre." },
        { "CrUnconventionalAttrName_head" ,
                "Escoge un nombre de MAttribute m\u00e1s adecuado" },
        { "CrUnconventionalAttrName_desc" ,
                "Normalmente los nombres de atributos comienzan con una letra min\u00fascula. El nombre '<ocl>self</ocl>' es poco convencional porque no comienza por min\u00fascula.\n\nSeguir buenas convenciones de nombres facilita el entendimiento y mantenibilidad del dise\u00f1o. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente <ocl>self</ocl> y usa la solapa Propiedades para give it a name." },
        { "CrUnconventionalClassName_head" ,
                "Capitaliza el nombre de clase <ocl>self</ocl>" },
        { "CrUnconventionalClassName_desc" ,
                "Normalmente el nombre de las clases comienza con may\u00fascula. El nombre '<ocl>self</ocl>' es poco convencional porque no comienza con may\u00fascula.\n\nSeguir buenas convenciones de nombres facilita el entendimiento y mantenibilidad del dise\u00f1o. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente <ocl>self</ocl> y usa la solapa Propiedades para asignarle un nombre diferente." },
        { "CrUnconventionalPackName_head" ,
                "Revisa el nombre del paquete <ocl>self</ocl>" },
        { "CrUnconventionalPackName_desc" ,
                "Normalmente los nombres de paquetes estan escritos en min\u00fasculas con puntos para indicar paquetes \"anidados\".  El nombre '<ocl>self</ocl>' no es aconsejable porque no esta formado por min\u00fasculas y puntos.\n\nSeguir las convenciones para asignar nombres ayuda a entender y mantener el dise\u00f1o. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente <ocl>self</ocl> y usa la solapa Propiedades para asignarle un nombre diferente." },
        { "CrClassMustBeAbstract_head" ,
                "La clase debe ser abstracta" },
        { "CrClassMustBeAbstract_desc" ,
                "Las clases que incluyen o heredan m\u00e9todos abstractos de sus clases base o interfaces deben ser Abstract.\n\nDecidir que clases son abstractas o concretas es una parte clave del dise\u00f1o de la jerarqu\u00eda de clases.\n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente la clase y usa la solapa Propiedades para a\u00f1adir la palabra clave abstract, o manualmente sobrepon cada operaci\u00f3n abstracta que es heredada de una clase base o interface." },
        { "CrReservedName_head" ,
                "Cambia <ocl>self</ocl> a una palabra no reservada" },
        { "CrReservedName_desc" ,
                "\"<ocl>self</ocl>\" es una palabra reservada o muy parecida a una.  Los nombres de los elementos del modelo no deben estar en conflicto con palabras reservadas de lenguajes de programaci\u00f3n o del UML.\n\nEs necesario usar nombres legales para generar c\u00f3digo compilable.. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente el elemento realzado y usa la solapa Propiedades para darle un nombre diferente." },
        { "CrMultipleInheritance_head" ,
                "Sustituye la herencia multiple por interfaces" },
        { "CrMultipleInheritance_desc" ,
                "<ocl>self</ocl> tiene multiples clases base, pero Java no soporta herencia multiple.  Debes implementar interfaces en vez de usar multiples clases base. \n\nEste cambio se requiere antes de poder generar c\u00f3digo Java.\n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o manualmente (1) quita una de las clases base y luego (2) opcionalmente define un nuevo interfaz con las mismas declaraciones de m\u00e9todos y (3) a\u00f1adelo como un interface de <ocl>self</ocl>, y (4) mueve los cuerpos de los m\u00e9todos de la antigua clase base a <ocl>self</ocl>." },
        { "CrIllegalName_head" ,
                "Escoge un nombre legal" },
        { "CrIllegalName_desc" ,
                "Los nombres de los elementos del modelo deben ser secuencias de letras, n\u00fameros, y subrayados.  No pueden contener puntuaci\u00f3n.\n\nLa generaci\u00f3n de c\u00f3digo requiere nombres legales para que el c\u00f3digo resultante compile correctamente. \n\nPara arreglar esto, usa el bot\u00f3n \"Siguiente>\", o selecciona manualmente el elemento destacado y usa la solapa Propiedades para darle un nombre diferente." },
        { "CrConsiderSingleton_head" ,
                "Considera el uso del patr\u00f3n Singelton " },
        { "CrConsiderSingleton_desc" ,
                "Esta clase no tiene atributos ni asociaciones que sean navegables fuera de la instancias de esta clase.  Esto significa que cada instancia de esta clase sera igual a cualquier otra debido a que no habr\u00e1 variables de instancia para diferenciarlas. Si esta no es tu intenci\u00f3n, deber\u00edas definir atributos o asociaciones que representen diferencias entre las instancias. Si no hay atributos o asociaciones para realizar esta diferenciaci\u00f3n, deber\u00edas considerar el uso de solo una instancia de esta clase, tal como se indica en el patr\u00f3n Singelton.\n\nDefinir la multiplicidad de instancias es necesario para completar la parte de representaci\u00f3n de informaci\u00f3n del dise\u00f1o. Usar el patr\u00f3n Singelton puede ahorrar tiempo y memoria.\n\nPara aplicar autom\u00e1ticamente el patr\u00f3n Singleton, pulsa el bot\u00f3n \"Siguiente>\"; o manualmente (1) marca la clase con el estereotipo Singlton, (2) a\u00f1ade una variable est\u00e1tica que contenga la instancia de esta clase, (3) y haz privados todos los constructores.\n\nPara saber m\u00e1s del patr\u00f3n Singleton, pulsa el icono MoreInfo." },
        { "CrSingletonViolated_head" ,
                "Infracci\u00f3n del Singleton Stereotype " },
        { "CrSingletonViolated_desc" ,
                "Esta clase esta marcada con el estereotipo Singelton, pero no satisface las restricciones correspondientes.  Una clase singleton puede tener como m\u00e1ximo una instancia.  Esto significa que la clase debe tener (1) una variable static haciendo referencia a la instancia, (2) solo constructores privados de modo que no puedan crearse m\u00e1s instancias desde otras clases, y (3) debe haber al menos un constructor que sobreponga el constructor por defecto.\n\nCuando marcas una clase con un estereotipo, la clase debe satisfacer todas las restricciones del estereotipo.  Esto contribuir\u00e1 a obtener un dise\u00f1o consistente y f\u00e1cil de entender. Usar el patr\u00f3n Singleton ahorrar\u00e1 tiempo y espacio.\n\nSi ya no quieres que esta clase sea un Singelton , quita el estereotipo Singleton haciendo click sobre la clase y borrando Singleton de la solapa de propiedades. \nPara aplicar autom\u00e1ticamente el patr\u00f3n Singleton, pulsa el bot\u00f3n \"Siguiente>\"; o manualmente (1) marca la clase con el estereotipo Singelton, (2) a\u00f1ade una variable est\u00e1tica que guarde la referencia a la instancia de esta clase, (3) y haz privados todos los constructores.\n\nPara aprender m\u00e1s del patr\u00f3n Singleton, pulsa el icono MoreInfo." },
        { "CrNodesOverlap_head" ,
                "Limpia el diagrama" },
        { "CrNodesOverlap_desc" ,
                "Algunos objetos en este diagrama se solapan sobre otros. Esto puede ocultar informaci\u00f3n importante y hacer el dise\u00f1o m\u00e1s dif\u00edcil de entender. Una apariencia cuidada ayuda a otros dise\u00f1adores, implementadores, y analistas.\n\nConstruir un conjunto de diagramas de clases f\u00e1cil de entender es una parte importante del dise\u00f1o. \n\nPara arreglar esto, mueve los nodos destacados en el diagrama." },
        { "CrZeroLengthEdge_head" ,
                "Haz el borde m\u00e1s visible" },
        { "CrZeroLengthEdge_desc" ,
                "Esta l\u00ednea es demasiado peque\u00f1a para ser vista con facilidad. Esto puede ocultar informaci\u00f3n importante y hacer el dise\u00f1o m\u00e1s dif\u00edcil de entender. Una apariencia cuidada ayuda a otros dise\u00f1adores, implementadores, y analistas.\n\nConstruir un conjunto de diagramas de clases f\u00e1cil de entender es una parte importante del dise\u00f1o. \n\nPara arreglar esto, mueve uno o m\u00e1s nodos de modo que las l\u00edneas realzadas sean m\u00e1s largas, o pulsa en el centro de la l\u00ednea y arrastra para hacer un nuevo vertice." },
        //
        //   these phrases should be localized here
        //      not in the following check list section
        { "Naming", "Nombres" },
        { "Encoding", "Codificaci\u00f3n" },
        { "Value", "Valor" },
        { "Location", "Posici\u00f3n" },
        { "Updates", "Actualizaciones" },
        { "General", "General" },
        { "Actions" , "Acciones" },
        { "Transitions", "Transiciones" },
        { "Structure", "Estructura" },
        { "Trigger", "Disparador" },
        { "MGuard", "MGuard" },
        //
        //   The following blocks define the UML related
        //      Checklists.  The key is the name of
        //      the non-deprecated implmenting class,
        //      the value is an array of categories which
        //      are each an array of Strings.  The first
        //      string in each category is the name of the
        //      category and should not be localized here
        //      but should be in the immediate preceeding
        //      section
        //
        { "ChClass",
            new String[][] {
                new String[] { "Naming",
                  "\u00bfDescribe claramente la clase el nombre '<ocl>self</ocl>'?",
                  "\u00bfEs '<ocl>self</ocl>' un nombre o una sintagma nominal?",
                  "\u00bfPodr\u00eda malinterpretarse el significado de '<ocl>self</ocl>'?"
                },
                new String[] { "Encoding",
                  "\u00bfDeber\u00eda <ocl>self</ocl> estar en su propia clase o ser un atributo de otra?",
                  "\u00bfHace <ocl>self</ocl> exactamente una sola cosa y la hace bien?",
                  "\u00bfPodr\u00eda <ocl>self</ocl> ser dividido en dos o m\u00e1s clases?"
                },
                new String[] { "Value",
                  "\u00bfComienzan todos los atributos de <ocl>self</ocl> con valores significativos?",
                  "\u00bfPodr\u00edas escribir un invariante para esta clase?",
                  "\u00bfEstablecen la invariante de la clase todos los constructores?",
                  "\u00bfMantienen la invariante de la clase todas las operaciones?"
                },
                new String[] { "Location",
                  "\u00bfPodr\u00eda <ocl>self</ocl> estar definida en una posici\u00f3n diferente de esta jerarqu\u00eda de clases?",
                  "\u00bfPlaneas crear subclases de <ocl>self</ocl>?",
                  "\u00bfPodr\u00edas eliminar del modelo a <ocl>self</ocl>?",
                  "\u00bfHay alguna otra clase en el modelo que deba ser revisada o eliminada porque sirve al mismo proposito que <ocl>self</ocl>?"
                },
                new String[] { "Updates",
                  "\u00bfPorque motivos ser\u00e1 actualizada una instancia de <ocl>self</ocl>?",
                  "\u00bfExiste algun otro objeto que deba ser actualizado cuando sea actualizado <ocl>self</ocl>?"
                }
            }
        },
        { "ChAttribute",
            new String[][] {
                new String[] { "Naming",
                  "\u00bfEl nombre '<ocl>self</ocl>' describe claramente el atributo?",
                  "\u00bfEs '<ocl>self</ocl>' un nombre o un sintagma nominal?",
                  "\u00bfPodr\u00eda malinterpretarse el significado de '<ocl>self</ocl>'?"
                },
                new String[] { "Encoding",
                  "\u00bfEs el tipo <ocl>self.type</ocl> demasiado restrictivo para representar todos los posibles valores de <ocl>self</ocl>?",
                  "\u00bfEl tipo <ocl>self.type</ocl> permite valores para <ocl>self</ocl> que nunca podr\u00e1n ser correctos?",
                  "\u00bfPodr\u00eda combinarse <ocl>self</ocl> con algun otro atributo de <ocl>self.owner</ocl> (por ejemplo, {owner.structuralFeature})?",
                  "\u00bfPodr\u00eda descomponerse <ocl>self</ocl> en dos o m\u00e1s partes (por ejemplo, un n\u00famero de telefono puede descomponerse en un prefijo y un n\u00famero)?",
                  "Podr\u00eda calcularse <ocl>self</ocl> a partir de otros atributos instead of stored?"
                },
                new String[] { "Value",
                  "\u00bfDeber\u00eda tener <ocl>self</ocl> un valor inicial (o por defecto)?",
                  "\u00bfEs correcto el valor inicial <ocl>self.initialValue</ocl>?",
                  "\u00bfPodr\u00edas escribir una expresi\u00f3n para comprobar si <ocl>self</ocl> es correcto? veros\u00edmil?"
                },
                new String[] { "Location",
                  "\u00bfPodr\u00eda definirse <ocl>self</ocl> en una clase diferente que este asociada con <ocl>self.owner</ocl>?",
                  "\u00bfPodr\u00eda moverse <ocl>self</ocl> m\u00e1s arriba en la jerarqu\u00eda de herencia para aplicarla a owner.name y a otras clases?",
                  "\u00bf<ocl>self</ocl> se aplica a todas las instancias de la clase <ocl>self.owner</ocl> incluyendo instancias de sus subclases?",
                  "\u00bfPodr\u00edas eliminar del modelo a <ocl>self</ocl>?",
                  "\u00bfHay otro atributo en el modelo que deber\u00eda ser revisado o eliminado porque sirve al mismo proposito que <ocl>self</ocl>?"
                },
                new String[] { "Updates",
                  "\u00bfPorque motivos ser\u00e1 actualizado <ocl>self</ocl>?",
                  "\u00bfHay algun otro atributo que deba ser actualizado cuando sea actualizado <ocl>self</ocl>?",
                  "\u00bfHay algun m\u00e9todo que deba ser llamado cuando sea actualizado <ocl>self</ocl> ?",
                  "\u00bfHay algun m\u00e9todo que deba ser llamado cuando a <ocl>self</ocl> se le asigne un valor determinado?"
                }
            }
        },
        { "ChOperation",
            new String[][] {
                new String[] { "Naming",
                  "\u00bfDescribe claramente la operaci\u00f3n el nombre '<ocl>self</ocl>'?",
                  "\u00bf'<ocl>self</ocl>' es un verbo o una frase?",
                  "\u00bfPodr\u00eda malinterpretarse el significado de '<ocl>self</ocl>'?",
                  "\u00bf<ocl>self</ocl> hace exactamente una cosa y la hace bien?"
                },
                new String[] { "Encoding",
                  "\u00bfEl tipo de retorno '<ocl>self.returnType</ocl>' es demasiado restrictivo para representar todos los valores devueltos por <ocl>self</ocl>?",
                  "\u00bf'<ocl>self.returnType</ocl>' permite valores de retorno que nunca pueden ser correctos?",
                  "\u00bfPodr\u00eda <ocl>self</ocl> combinarse con otra operaci\u00f3n de <ocl>self.owner</ocl> (por ejemplo, <ocl sep=', '>self.owner.behavioralFeature</ocl>)?",
                  "\u00bfPodr\u00eda dividirse <ocl>self</ocl> en dos o m\u00e1s parts (por ejemplo, pre-procesamiento, procesamiento principal, y post-procesamiento)?",
                  "\u00bfPodr\u00eda <ocl>self</ocl> ser reemplazado por llamadas del cliente a operaciones m\u00e1s simples?",
                  "\u00bfPodr\u00eda <ocl>self</ocl> ser combinada con otras operaciones para redurcir el n\u00famero de llamadas que deben hacer los clientes?"
                },
                new String[] { "Value",
                  "\u00bfPuede <ocl>self</ocl> manejar todas las entradas posibles?",
                  "\u00bfHay algunas entradas especiales que deban ser manejadas por separado?",
                  "\u00bfPodr\u00edas escribir una expresi\u00f3n para comprobar si los argumentos de <ocl>self</ocl> son correctos? \u00bfy veros\u00edmiles?",
                  "\u00bfPuedes expresar las precondiciones de <ocl>self</ocl>?",
                  "\u00bfPuedes expresar las postcondiciones de <ocl>self</ocl>?",
                  "\u00bfComo se comportar\u00eda <ocl>self</ocl> si las precondiciones fueran infringidas?",
                  "\u00bfComo se comportar\u00eda <ocl>self</ocl> si las postcondiciones fueran infringidas?"
                },
                new String[] { "Location",
                  "\u00bfPodr\u00eda <ocl>self</ocl> estar definida en una clase diferente que este asociada con <ocl>self.owner</ocl>?",
                  "\u00bfPodr\u00edas mover <ocl>self</ocl> m\u00e1s arriba de la jerarqu\u00eda para aplicar <ocl>self.owner</ocl> a otras clases?",
                  "\u00bf<ocl>self</ocl> se aplica a todas las instancias de la clase <ocl>self.owner</ocl> incluyendo instancias de sus subclases?",
                  "\u00bfPodr\u00edas eliminar del modelo a <ocl>self</ocl>?",
                  "\u00bfHay otra operaci\u00f3n en el modelo que deba ser revisada o eliminada porque sirva al mismo proposito que <ocl>self</ocl>?"
                }
            }
        },
        { "ChAssociation",
            new String[][] {
                new String[] { "Naming",
                  "\u00bfEl nombre '<ocl>self</ocl>' describe claramente la clase?",
                  "\u00bfEs '<ocl>self</ocl>' un nombre o un sintagma nominal?",
                  "\u00bfPodr\u00eda malinterpretarse el significado de '<ocl>self</ocl>'?"
                },
                new String[] { "Encoding",
                  "\u00bfDeber\u00eda <ocl>self</ocl> estar en su propia clase o ser solo un atributo de una clase?",
                  "\u00bfHace <ocl>self</ocl> exactamente una sola cosa y la hace bien?",
                  "\u00bfPodr\u00eda dividirse <ocl>self</ocl> en dos o m\u00e1s classes?"
                },
                new String[] { "Value",
                  "Comienzan todos los atributos de <ocl>self</ocl> con valores significativos?",
                  "\u00bfPodr\u00edas escribir un invariante para esta clase?",
                  "\u00bfEstablecen la invariante de la clase todos los constructores?",
                  "\u00bfMantienen la invariante de la clase todas las operaciones?"
                },
                new String[] { "Location",
                  "\u00bfPodr\u00eda <ocl>self</ocl> estar definida en una posici\u00f3n diferente de esta jerarqu\u00eda de clases?",
                  "\u00bfPlaneas crear subclases de <ocl>self</ocl>?",
                  "\u00bfPodr\u00edas eliminar del modelo a <ocl>self</ocl>?",
                  "Hay alguna otra clase en el modelo que seba ser revisada o eliminada porque sirva al mismo proposito que <ocl>self</ocl>?"
                },
                new String[] { "Updates",
                  "\u00bfPorque motivos deber\u00eda ser actualizada una instancia de <ocl>self</ocl>?",
                  "\u00bfHay algun otro objeto que deba ser actualizado cuando <ocl>self</ocl> es actualizado?"
                }
            }
        },
        { "ChInterface",
            new String[][] {
                new String[] { "Naming",
                  "\u00bfEl nombre '<ocl>self</ocl>' describe claramente la clase?",
                  "\u00bfEs '<ocl>self</ocl>' un nombre o un sintagma nominal?",
                  "\u00bfPodr\u00eda malinterpretarse el significado de '<ocl>self</ocl>'?"
                },
                new String[] { "Encoding",
                  "\u00bfDeber\u00eda <ocl>self</ocl> estar en su propia clase o ser solo un atributo de una clase?",
                  "\u00bfHace <ocl>self</ocl> exactamente una sola cosa y la hace bien?",
                  "\u00bfPodr\u00eda dividirse <ocl>self</ocl> en dos o m\u00e1s classes?"
                },
                new String[] { "Value",
                  "\u00bfComienzan todos los atributos de <ocl>self</ocl> con valores significativos?",
                  "\u00bfPodr\u00edas escribir un invariante para esta clase?",
                  "\u00bfEstablecen la invariante de la clase todos los constructores?",
                  "\u00bfMantienen la invariante de la clase todas las operaciones?"
                },
                new String[] { "Location",
                  "\u00bfPodr\u00eda <ocl>self</ocl> estar definida en una posici\u00f3n diferente de esta jerarqu\u00eda de clases?",
                  "\u00bfHas planeado crear subclases de <ocl>self</ocl>?",
                  "\u00bfPodr\u00edas eliminar del modelo a <ocl>self</ocl>?",
                  "\u00bfHay otra clase en el modelo que deber\u00eda ser revisada o eliminada porque sirve al mismo proposito que <ocl>self</ocl>?"
                },
                new String[] { "Updates",
                  "\u00bfPorque motivos sera actualizada la instancia de <ocl>self</ocl>?",
                  "\u00bfHay algun otro objeto que deba ser actualizado cuando <ocl>self</ocl> es actualizado?"
                }
            }
        },
        { "ChInstance",
            new String[][] {
                new String[] { "General",
                  "\u00bfDescribe esta instancia de <ocl>self</ocl> la instancia?"
                },
                new String[] { "Naming",
                  "\u00bfEl nombre '<ocl>self</ocl>' describe claramente la instancia?",
                  "\u00bf'<ocl>self</ocl>' denota un estado en vez de una actividad?",
                  "\u00bfPodr\u00eda malinterpretarse el significado de '<ocl>self</ocl>'?"
                },
                new String[] { "Structure",
                  "\u00bfDeber\u00eda <ocl>self</ocl> ser su propio estado o podr\u00eda combinarse con otro estado?",
                  "\u00bfHace <ocl>self</ocl> exactamente una sola cosa y la hace bien?",
                  "\u00bfPodr\u00eda dividirse <ocl>self</ocl> en dos o m\u00e1s estados?",
                  "\u00bfPodr\u00edas escribir una ecuaci\u00f3n caracter\u00edstica para <ocl>self</ocl>?",
                  "\u00bf<ocl>self</ocl> pertenece a esta m\u00e1quina de estados o a otra?",
                  "\u00bfDeber\u00eda <ocl>self</ocl> ser un estado inicial?",
                  "\u00bfEs algun estado de otra m\u00e1quina esclusivo con <ocl>self</ocl>?"
                },
                new String[] { "Actions",
                  "\u00bfQue acci\u00f3n deber\u00eda realizarse al entrar en <ocl>self</ocl>?",
                  "\u00bfDeber\u00eda actualizarse algun atributo al entrar en <ocl>self</ocl>?",
                  "\u00bfQue acci\u00f3n deber\u00eda realizarse al salir de <ocl>self</ocl>?",
                  "\u00bfDeber\u00eda actualizarse algun atributo al salir de <ocl>self</ocl>?",
                  "\u00bfQue acci\u00f3n deber\u00eda realizarse mientras este en <ocl>self</ocl>?",
                  "\u00bfLas state-actions mantienen <ocl>self</ocl> como el estado actual?"
                },
                new String[] { "Transitions",
                  "\u00bfDeber\u00eda haber otra transici\u00f3n en <ocl>self</ocl>?",
                  "\u00bfPueden ser usadas todas las transiciones de <ocl>self</ocl>?",
                  "\u00bfPodr\u00edan combinarse algunas transiciones entrantes??",
                  "\u00bfDeber\u00eda haber otra transici\u00f3n fuera de <ocl>self</ocl>?",
                  "\u00bfPueden usarse todas las transiciones de <ocl>self</ocl>?",
                  "\u00bfEs exclusiva cada transici\u00f3n saliente?",
                  "\u00bfPodr\u00edan combinarse algunas transiciones salientes?"
                }
            }
        },
        { "ChLink",
            new String[][] {
                new String[] { "Naming",
                  "\u00bfEl nombre '<ocl>self</ocl>' describe claramente la clase?",
                  "\u00bfEs '<ocl>self</ocl>' un nombre o un sintagma nominal?",
                  "\u00bfPodr\u00eda malinterpretarse el significado de '<ocl>self</ocl>'?"
                },
                new String[] { "Encoding",
                  "\u00bfDeber\u00eda <ocl>self</ocl> estar en su propia clase o ser solo un atributo de una clase?",
                  "\u00bfHace <ocl>self</ocl> exactamente una sola cosa y la hace bien?",
                  "\u00bfPodr\u00eda dividirse <ocl>self</ocl> en dos o m\u00e1s classes?"
                },
                new String[] { "Value",
                  "Comienzan todos los atributos de <ocl>self</ocl> con valores significativos?",
                  "\u00bfPodr\u00edas escribir un invariante para esta clase?",
                  "\u00bfEstablecen la invariante de la clase todos los constructores?",
                  "\u00bfMantienen la invariante de la clase todas las operaciones?"
                },
                new String[] { "Location",
                  "\u00bfPodr\u00eda <ocl>self</ocl> estar definida en una posici\u00f3n diferente de esta jerarqu\u00eda de clases?",
                  "\u00bfPlaneas tener subclases de <ocl>self</ocl>?",
                  "\u00bfPodr\u00edas eliminar del modelo a <ocl>self</ocl>?",
                  "Hay alguna otra clase en el modelo que seba ser revisada o eliminada porque sirva al mismo proposito que <ocl>self</ocl>?"
                },
                new String[] { "Updates",
                  "\u00bfPorque motivos ser\u00e1 actualizada una instancia de <ocl>self</ocl>?",
                  "\u00bfHay algun otro objeto que deba ser actualizado cuando lo sea <ocl>self</ocl> ?"
                }
            }
        },
        { "ChState",
            new String[][] {
                new String[] { "Naming",
                  "\u00bfDescribe claramente el estado el nombre '<ocl>self</ocl>'?",
                  "\u00bfDenota '<ocl>self</ocl>' un estado en vez de una actividad?",
                  "\u00bfPodr\u00eda malinterpretarse el significado de '<ocl>self</ocl>'?"
                },
                new String[] { "Structure",
                  "\u00bfDeber\u00eda <ocl>self</ocl> ser su propio estado o podr\u00eda ser combinado con otro estado?",
                  "\u00bfHace <ocl>self</ocl> exactamente una sola cosa y la hace bien?",
                  "\u00bfPodr\u00eda dividirse <ocl>self</ocl> en dos o m\u00e1s states?",
                  "\u00bfPodr\u00edas escribir una ecuaci\u00f3n caracter\u00edstica para <ocl>self</ocl>?",
                  "\u00bf<ocl>self</ocl> pertence a esta m\u00e1quina de estados o a otra?",
                  "\u00bfDeber\u00eda <ocl>self</ocl> ser un estado inicial?",
                  "\u00bfHay algun estado en otra m\u00e1quina exclusivo con <ocl>self</ocl>?"
                },
                new String[] { "Actions",
                  "\u00bfQue acci\u00f3n deber\u00eda realizarse al entrar en <ocl>self</ocl>?",
                  "\u00bfDeber\u00eda actualizarse algun atributo al entrar en <ocl>self</ocl>?",
                  "\u00bfQue acci\u00f3n deber\u00eda realizarse al salir de <ocl>self</ocl>?",
                  "Deber\u00eda actualizarse algun atributo al salir de <ocl>self</ocl>?",
                  "\u00bfQue acci\u00f3n deber\u00eda realizarse mientras este en <ocl>self</ocl>?",
                  "\u00bfLas state-actions mantienen <ocl>self</ocl> como el estado actual?"
                },
                new String[] { "Transitions",
                  "\u00bfDeber\u00eda haber otra transici\u00f3n en <ocl>self</ocl>?",
                  "\u00bfPueden ser usadas todas las transiciones en <ocl>self</ocl>?",
                  "\u00bfPodr\u00edan combinarse algunas de las transiciones entrantes?",
                  "\u00bfDeber\u00eda haber otra transici\u00f3n fuera de <ocl>self</ocl>?",
                  "\u00bfPueden ser usadas todas las transiciones fuera de <ocl>self</ocl>?",
                  "\u00bfEs exclusiva cada transici\u00f3n saliente?",
                  "\u00bfPodr\u00edan combinarse algunas transiciones salientes?"
                }
            }
        },
        { "ChTransition",
            new String[][] {
                new String[] { "Structure",
                  "\u00bfDeber\u00eda comenzar en una fuente diferente esta transici\u00f3n?",
                  "\u00bfDeber\u00eda terminar esta transici\u00f3n en un destino diferente?",
                  "\u00bfDeber\u00eda haber otra transici\u00f3n \"como\" esta?",
                  "\u00bfHay otra transici\u00f3n no necesario debido a esta?"
                },
                new String[] { "Trigger",
                  "\u00bfNecesita un disparador esta transici\u00f3n?",
                  "\u00bfOcurre muy a menudo este disparador?",
                  "\u00bfOcurre muy raramente este disparador?"
                },
                new String[] { "MGuard",
                  "Could this transition be taken too often?",
                  "Es demasiado restrictiva la condici\u00f3n de esta transici\u00f3n?",
                  "\u00bfPodr\u00eda ser dividida en dos o m\u00e1s transiciones?"
                },
                new String[] { "Actions",
                  "\u00bfDeber\u00eda tener una acci\u00f3n esta transici\u00f3n?",
                  "\u00bfDeber\u00eda ser una acci\u00f3n exit la acci\u00f3n de esta transici\u00f3n?",
                  "\u00bfDeber\u00eda ser una acci\u00f3n entry la acci\u00f3n de esta transici\u00f3n?",
                  "\u00bfSe alcanza siempre la precondici\u00f3n de esta acci\u00f3n?",
                  "\u00bfEs consistente la postcondici\u00f3n de esta acci\u00f3n con el destino?"
                }
            }
        },
        { "ChUseCase",
            new String[][] {
                new String[] { "Naming",
                  "\u00bfEl nombre '<ocl>self</ocl>' describe claramente la clase?",
                  "\u00bfEs '<ocl>self</ocl>' un nombre o una sintagma nominal?",
                  "\u00bfPodr\u00eda malinterpretarse el significado de '<ocl>self</ocl>'?"
                },
                new String[] { "Encoding",
                  "\u00bfDeber\u00eda <ocl>self</ocl> estar en su propia clase o ser solo un atributo de una clase?",
                  "\u00bfHace <ocl>self</ocl> exactamente una sola cosa y la hace bien?",
                  "\u00bfPodr\u00eda dividirse <ocl>self</ocl> en dos o m\u00e1s classes?"
                },
                new String[] { "Value",
                  "\u00bfComienzan con nombres significativos todos los atributos de <ocl>self</ocl>?",
                  "\u00bfPodr\u00edas escribir un invariante para esta clase?",
                  "\u00bfEstablecen la invariante de la clase todos los constructores?",
                  "\u00bfMantienen la invariante de la clase todas las operaciones?"
                },
                new String[] { "Location",
                  "\u00bfPodr\u00eda <ocl>self</ocl> estar definida en una posici\u00f3n diferente de esta jerarqu\u00eda de clases?",
                  "\u00bfPlaneas crear subclases de <ocl>self</ocl>?",
                  "\u00bfPodr\u00edas eliminar del modelo a <ocl>self</ocl>?",
                  "\u00bfExiste alguna otra clase en el modelo que deba ser revisada o eliminada porque sirve al mismo proposito que <ocl>self</ocl>?"
                },
                new String[] { "Updates",
                  "\u00bfPorque razones sera actualizada una instancia de <ocl>self</ocl>?",
                  "\u00bfHay algun otro objeto que deba ser actualizado cuando <ocl>self</ocl> es actualizado?"
                }
            }
        },
        { "ChActor",
            new String[][] {
                new String[] { "Naming",
                  "\u00bfDescribe claramente la clase el nombre '<ocl>self</ocl>'?",
                  "\u00bf'<ocl>self</ocl>' es un nombre o una sintagma nominal?",
                  "\u00bfPodr\u00eda ser malinterpretado el significado del nombre '<ocl>self</ocl>'?"
                },
                new String[] { "Encoding",
                  "Deber\u00eda haber una clase propia para <ocl>self</ocl> o ser un atributo simple de otra clase?",
                  "\u00bfHace <ocl>self</ocl> una sola cosa y la hace bien?",
                  "Podr\u00eda dividirse <ocl>self</ocl> en dos o m\u00e1s clases?"
                },
                new String[] { "Value",
                  "\u00bfComienzan todos los atributos de <ocl>self</ocl> con valores significativos?",
                  "\u00bfPodr\u00edas escribir un invariante para esta clase?",
                  "\u00bfEstablecen la invariante de la clase todos los constructores?",
                  "\u00bfMantienen la invariante de la clase todas las operaciones?"
                },
                new String[] { "Location",
                  "\u00bfPodr\u00eda <ocl>self</ocl> estar definida en un lugar diferente de esta jerarqu\u00eda de clases?",
                  "\u00bfTienes planeado subclasificar <ocl>self</ocl>?",
                  "\u00bfPodr\u00edas eliminar <ocl>self</ocl> del modelo?",
                  "Hay alguna otra clase del modelo que deber\u00eda ser revisada o eliminada porque sirve al mismo proposito que <ocl>self</ocl>?"
                },
                new String[] { "Updates",
                  "\u00bfPorque motivos una instancia de <ocl>self</ocl> es actualizada?",
                  "Hay algun otro objeto que deba ser actualizado cuando <ocl>self</ocl> es actualizado?"
                }
            }
        }
    };

    public Object[][] getContents() {
        return _contents;
    }
}
