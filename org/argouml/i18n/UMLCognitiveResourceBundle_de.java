// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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

// File: UMLCognitiveResourceBundle_de.java
// Classes: UMLCognitiveResourceBundle_de
// Original Author: Curt Arnold

package org.argouml.i18n;
import java.util.*;

/**
 *   This class is the default member of a resource bundle that
 *   provides strings for UML related critiques and check lists.
 *   UMLCognitiveResourceBundle_de provides the german translations.
 *
 *   <b>Note:</b> do not use german umlaut characters directly.
 *
 *   @author Curt Arnold
 *   @see java.util.ResourceBundle
 *   @see org.argouml.i18n.UMLCognitiveResourceBundle
 *   @see org.argouml.util.CheckResourceBundle
 *   @see org.argouml.uml.cognitive.critics.CrUML
 */
public class UMLCognitiveResourceBundle_de extends ListResourceBundle {

    private static final Object[][] _contents = {
	// General stuff
        {"decision.behavior", "Verhalten"},
        {"decision.class-selection", "Klassen ausw\u00e4hlen"},
        {"decision.code-generation", "Code generieren"},
        {"decision.containment", "Festhalten"},
        {"decision.design-patterns", "Entwurfsmuster"},
        {"decision.expected-usage", "Erwartete Aussage"},
        {"decision.inheritance", "Vererbung"},
        {"decision.instantiation", "Instanz bilden"},
        {"decision.methods", "Methoden"},
        {"decision.modularity", "Modularit\u00e4t"},
        {"decision.mstate-machines", "Zustandsautomaten"},
        {"decision.naming", "Benennen"},
        {"decision.planned-extensions", "Geplante Erweiterungen"},
        {"decision.relationships", "Beziehungen"},
        {"decision.stereotypes", "Stereotypen"},
        {"decision.storage", "Speicher"},
        {"decision.uncategorized", "Nicht kategorisiert"},

        {"goal.unspecified", "Nicht spezifiziert"},

        {"knowledge.completeness", "Vollst\u00e4ndigkeit"},
        {"knowledge.consistency", "Konsistenz"},
        {"knowledge.correctness", "Fehlerfreiheit"},
        {"knowledge.designers", "Designer's"},
        {"knowledge.experiential", "Experimentell"},
        {"knowledge.optimization", "Optimierung"},
        {"knowledge.organizational", "organisatorisch"},
        {"knowledge.presentation", "Darstellung"},
        {"knowledge.semantics", "Semantik"},
        {"knowledge.syntax", "Syntax"},
        {"knowledge.tool", "Werkzeug"},

        {"todopane.label.no-items", " Keine Elemente "},
        {"todopane.label.item", " {0} Element "},
        {"todopane.label.items", " {0} Elemente "},

        {"docpane.label.documentation", "Dokumentation"},
        {"docpane.label.author", "Autor"},
        {"docpane.label.version", "Version"},
        {"docpane.label.since", "Seit"},
        {"docpane.label.deprecated", "veraltet"},
        {"docpane.label.see", "Siehe"},

        {"stylepane.label.bounds", "Grenzen"},
        {"stylepane.label.fill", "F\u00fcllen"},
        {"stylepane.label.no-fill", "Nicht f\u00fcllen"},
        {"stylepane.label.line", "Zeile"},
        {"stylepane.label.no-line", "Keine Zeile"},
        {"stylepane.label.shadow", "Schatten"},
        {"stylepane.label.no-shadow", "Kein Schatten"},
        {"stylepane.label.custom", "Angepasst"},

        {"taggedvaluespane.label.tag", "Markierung"},
        {"taggedvaluespane.label.value", "Wert"},

        {"button.open", "\u00d6ffnen"},
        {"button.back", "<< Vorheriges"},
        {"button.next", "N\u00e4chstes >>"},
        {"button.finish", "Fertig"},
        {"button.help", "Hilfe"},

        {"mnemonic.button.back", "V"},
        {"mnemonic.button.next", "N"},
        {"mnemonic.button.finish", "F"},
        {"mnemonic.button.help", "H"},

        {"level.low", "Niedrig"},
        {"level.medium", "Mittel"},
        {"level.high", "Hoch"},

        {"message.no-item-selected", "Keine \"Noch zu bearbeiten\"-Elemente ausgew\u00e4hlt"},
        {"message.branch-critic", "Dieser Zweig enth\u00e4lt durch den Hinweis: \n{0} " +
        "generierte \"Noch zu bearbeiten\"-Elemente."},
        {"message.branch-decision", "Dieser Zweig enth\u00e4lt \"Noch zu bearbeiten\"" +
        "-Elemente bez\u00fcglich der Entscheidung:\n{0}."},
        {"message.branch-goal", "Dieser Zweig enth\u00e4lt \"Noch zu bearbeiten\"-" +
        "Elemente bez\u00fcglich des Zieles:\n{0}."},
        {"message.branch-knowledge", "Dieser Zweig enth\u00e4lt \"Noch zu bearbeiten\"" +
        "-Elemente, die {0} verkn\u00fcpfte Kenntnisse liefern."},
        {"message.branch-model", "Dieser Zweig enth\u00e4lt \"Noch zu bearbeiten\"" +
        "-Elemente bez\u00fcglich des Modellelementes:\n{0}."},
        {"message.branch-priority", "Dieser Zweig enth\u00e4lt {0} priorisierte " +
        "\"Noch zu bearbeiten\"-Elemente."},

	// Critics text
        { "CrAssocNameConflict_head" ,
	        	"L\u00f6sen Sie den Konflikt bei den Assoziationsnamen auf" },
        { "CrAssocNameConflict_desc" ,
				"Jedes Element eines Namensraumes mu\u00df einen eindeutigen Namen haben. \n" +
				"\n" +
				"Eine klare und unzweideutige Namensgebung ist ein Schl\u00fcsselelement für die " +
				"Codegenerierung, die Verst\u00e4ndlichkeit und die Wartbarkeit des Entwurfes. \n" +
				"\n" +
				"Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
				"Oder, Sie markieren die Elemente und \u00e4ndern die Namen im Registerblatt " +
				"\"Eigenschaften\"." },
        { "CrAttrNameConflict_head" ,
                "Um einen Konflikt zu vermeiden, \u00c4ndern Sie den Namen des Attributes."},
        { "CrAttrNameConflict_desc" ,
				"Attribute m\u00fcssen unterschiedliche Namen haben.  Dieses Problem kann auch " +
				"durch vererbte Attribute entstehen. \n" +
				"\n" +
				"Klare und eindeutige Namen sind ein Schl\u00fcsselelement bei der Codegenerierung und " +
				"beim erstellen eines verst\u00e4ndlichen und pflegbaren Entwurfes.\n" +
				"\n" +
				"Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
				"Oder, Sie markieren eines der betroffenen Attribute dieser Klasse und " +
				"\u00e4ndern seinen Namen."},

        // Updated following bug fix

        { "CrOperNameConflict_head" ,
	        	"\u00c4ndern Sie  in \"<ocl>self</ocl>\" Namen oder Signaturen." },
        { "CrOperNameConflict_desc" ,
				"Zwei Methoden haben exakt die gleiche Signatur.  Methoden m\u00fcssen " +
				"unterschiedliche Signaturen haben. Eine Signatur ist die Kombination eines Namens" +
				"mit den Parametertypen der Methode. \n" +
				"\n" +
				"Das Vermeiden von identischen Signaturen ist ein Schl\u00fcsselelement bei der " +
				"Codegenerierung und beim Erstellen eines verst\u00e4ndlichen und wartbaren Entwurfes.\n" +
				"\n" +
				"Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
				"Oder, Sie markieren eine der betroffenen Methoden dieser Klasse und \u00e4ndern deren " +
				"Namen." },
        { "CrCircularAssocClass_head" ,
	        	"Schleife bei Assoziation" },
        { "CrCircularAssocClass_desc" ,
				"Assoziierte Klassen k\u00f6nnen keine Rolle enthalten, die sich wiederum " +
				"direkt auf die Klasse bezieht" },
        { "CrCircularInheritance_head" ,
                "Entfernen Sie die \"<ocl>self</ocl>\"-zirkulare Vererbung." },
        { "CrCircularInheritance_desc" ,
                "Vererbungsbeziehungen d\u00fcrfen keine Schleifen bilden. \n" +
                "\n" +
                "f\u00fcr die Code-Generierung und die Richtigkeit des Entwurfes ist eine zul\u00e4ssige " +
                "Klassen-Vererbungshierarchie erforderlich. \n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
                "Oder, Sie w\u00e4hlen manuell einen der Vererbungspfeile in der Schleife aus " +
                "und entfernen diesen." },
        { "CrCircularComposition_head" ,
                "Entfernen Sie die zirkulare Komposition." },
        { "CrCircularComposition_desc" ,
                "Kompositionsbeziehungen (schwarze Diamanten) d\u00fcrfen keine Schleifen bilden. \n" +
                "\n" +
                "F\u00fcr die Code-Generierung und f\u00fcr die Richtigkeit des Entwurfes wird eine zul\u00e4ssige " +
                "Aggregationshierarchie ben\u00f6tigt. \n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
                "Oder, Sie w\u00e4hlen manuell eine der Assoziationen in der Schleife aus " +
                "und entfernen diese." },

        // Updated following bug fix

        { "CrCrossNamespaceAssoc_head" ,
                "Die Klassifizierung \"<ocl>self</ocl>\" befindet sich nicht im Namensraum " +
                "dieser Assoziation." },
        { "CrCrossNamespaceAssoc_desc" ,
				"Jede Klasse, jede Schnittstelle oder andere Klassifizierungen " +
				"(<ocl>self</ocl>), die Teil einer Assoziation ist, sollte sich " +
				"innerhalb des Namensraumes der Assoziation befinden.\n" +
				"\n" +
				"Wenn dies nicht der Fall ist, gibt es für die Klassen, Schnittstellen " +
				"oder anderen Klassifizierungen keine M\u00f6glichkeit, die Referenz " +
				"zu den anderen mit Hilfe der Assoziation zu benennen.\n" +
				"\n" +
				"Beachten Sie, da\u00df dieser Hinweis derzeit nicht den hierarchischen " +
				"Namensraum interpretiert. Er wird ausgel\u00f6st, wenn die abschliessenden " +
				"Komponenten des Namensraumes unterschiedlich sind, auch wenn sie sich eine " +
				"gemeinsame Wurzel teilen. Diesen Hinweis sollten Sie in diesem Licht " +
				"interpretieren.\n" +
				"Zur Problembeseitigung l\u00f6schen Sie die Assoziation und erzeugen Sie " +
				"diese erneut in einem Diagramm, dessen Namensraum die Klassen, Schnittstellen " +
				"und Klassifizierungen einschlie\u00dft."},
        { "CrDupParamName_head" ,
	        	"Der Parametername kommt doppelt vor." },
        { "CrDupParamName_desc" ,
				"Jeder Parameter einer Methode mu\u00df einen eindeutigen Namen haben. \n" +
				"\n" +
				"Die klare und unzweideutige Namensgebung ist für die Codegenerierung " +
				"und zum Erreichen eines klaren und wartbaren Entwurfes erforderlich.\n" +
				"\n" +
				"Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
				"Oder, Sie \u00e4ndern den Namen eines Parameters von Hand." },

        // Updated following bug fix

        { "CrDupRoleNames_head" ,
	        	"Duplizieren Sie die Namen der Enden (Rollen) f\u00fcr \"<ocl>self</ocl>\"." },
        { "CrDupRoleNames_desc" ,
				"Die Assoziation \"<ocl>self</ocl>\" hat zwei Rollen mit den gleichen Namen. \n" +
				"\n" +
				"Die klare und unzweideutige Namensgebung ist ein Schl\u00fcsselelement bei der " +
				"Codegenerierung, bei der Verst\u00e4ndlichkeit und Wartbarkeit des Entwurfes. \n" +
				"\n" +
				"Um dies manuell zu beseitigen, markieren Sie \"<ocl>self</ocl>\" und " +
				"verwenden Sie das Registerblatt \"Eigenschaften\", um einen oder mehrere " +
				"der konfliktausl\u00f6senden Rollennamen zu \u00e4ndern." },
        { "CrFinalSubclassed_head" ,
	        	"Entfernen Sie das  Schl\u00fcsselwort \"Blatt\" oder entfernen Sie die " +
	        	"Unterklassen" },
        { "CrFinalSubclassed_desc" ,
				"Das Schl\u00fcsselwort \"Blatt\" gibt an, dass eine Klasse keine " +
				"Unterklassen haben soll. Diese Klasse oder Schnittstelle ist als \"Blatt\" " +
				"markiert und hat Unterklassen.\n" +
				"\n" +
				"Eine gut durchdachte Klassenhierachie, die potentielle Erweiterungen " +
				"transportiert und unterst\u00fctzt, ist ein wichtiger Teil eines " +
				"verst\u00e4ndlichen und wartbaren Entwurfes.\n" +
				"\n" +
				"Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
				"Oder, Sie markieren die Klasse von Hand und \u00e4ndern die Basisklasse. " +
				"Oder, Sie markieren die Basisklasse und benutzen das Registerblatt \"Eigenschaften\", um das " +
				"Schl\u00fcsselwort \"Blatt\" zu entfernen."},
        { "CrIllegalGeneralization_head" ,
	       		"Unerlaubte Vererbung" },
        { "CrIllegalGeneralization_desc" ,
				"Modell-Elemente k\u00f6nnen nur von Elementen des gleichen Typs erben. \n" +
				"\n" +
				"Eine g\u00fcltige Vererbungshierachie ist f\u00fcr die Codegenerierung und " +
				"f\u00fcr die Korrektheit des Entwurfes erforderlich. \n" +
				"\n" +
				"Zur Problembeseitigung benutzen Sie die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
				"Oder, Sie markieren den Vererbungspfeil und entfernen ihn von Hand." },
        { "CrAlreadyRealizes_head" ,
	        	"Entfernen Sie die unn\u00f6tige Implementierung von \"<ocl>self</ocl>\"" },
        { "CrAlreadyRealizes_desc" ,
				"Die markierte Klasse implementiert bereits indirekt die Schnittstelle {item.extra}. " +
				"Es gibt keinen Grund, diese nochmals direkt zu implementieren. \n" +
				"\n" +
				"Es ist immer eine gute Idee, den Entwurf zu vereinfachen. Sie m\u00f6chten vielleicht " +
				"dieses \"Noch zu bearbeiten\"-Element unbearbeitet lassen, wenn Sie es offensichtlich machen wollen, " +
				"da\u00df die markierte Klasse diese Schnittstelle implementiert.\n" +
				"\n" +
				"Um dieses Problem zu beheben, markieren Sie die Implementierung (die punktierte " +
				"Linie mit der weissen, dreieckigen, Pfeilspitze) und dr\u00fccken die " +
				" Taste \"Entf\"." },
        { "CrInterfaceAllPublic_head" ,
	        	"Die Methoden in einer Schnittstelle m\u00fcssen \"public\" sein" },
        { "CrInterfaceAllPublic_desc" ,
				"Schnittstellen sind dazu gedacht, alle Methoden zu spezifizieren, die " +
				"von den anderen Klassen implementiert werden m\u00fcssen. Sie m\u00fcssen " +
				"\"public\" sein. \n" +
				"\n" +
				"Eine wohldurchdachte Sammlung von Schnittstellen ist ein guter Weg, die denkbaren " +
				"Erweiterungen eines Klassen-Frameworks zu spezifizieren. \n" +
				"\n" +
				"Zur Problembeseitigung benutzen Sie die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
				"Oder, Sie markieren die Methoden der Schnittstellen von Hand und benutzen " +
				"das Registerblatt \"Eigenschaften\", um sie \"public\" zu machen." },
        { "CrInterfaceOperOnly_head" ,
	        	"Schnittstellen d\u00fcrfen nur Methoden haben" },
        { "CrInterfaceOperOnly_desc" ,
				"Schnittstellen sind dazu gedacht, alle Methoden zu spezifizieren, die " +
				"von den anderen Klassen implementiert werden m\u00fcssen. Sie implementieren diese " +
				"Methoden nicht selbst und d\u00fcfen keine Attribute haben.\n" +
				"\n" +
				"Eine wohldurchdachte Sammlung von Schnittstellen ist ein guter Weg, die denkbaren " +
				"Erweiterungen eines Klassen-Frameworks zu definieren. \n" +
				"\n" +
				"Zur Problembeseitigung benutzen Sie die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
				"Oder, Sie markieren die Schnittstelle und benutzen das Registerblatt \"Eigenschaften\", " +
				"um alle Attribute zu entfernen." },
        { "CrMultipleAgg_head" ,
	        	"Doppelt vorkommende Aggregation"	},
        { "CrMultipleAgg_desc" ,
                "Nur eine Funktion einer Assoziation kann vom Typ Aggregation oder vom Typ " +
                "Komposition sein.\n" +
                "\n" +
                "Eine klare und konsistente Ist-Teil-von-Hierarchie ist ein Schl\u00fcsselelement " +
                "f\u00fcr einen klaren Entwurf, \n" +
                "handhabbarem Objektspeicher und der Implementierung rekursiver Methoden.\n" +
                "Zur Problembeseitigung markieren Sie die Assoziation und setzen einige der \n" +
                "Aggregationen auf \"Keine\"." },
        { "CrNWayAgg_head" ,
                "Aggregatfunktion in N-fach Assoziation." },
        { "CrNWayAgg_desc" ,
                "Dreifach- oder Mehrfach-Assoziationen d\u00fcrfen keine Aggregationsenden aufweisen.\n" +
                "\n" +
                "Eine klare und konsistente Ist-Teil-von-Hierarchie ist ein Schl\u00fcsselelement " +
                "f\u00fcr einen klaren Entwurf, \n" +
                "handhabbarem Objektspeicher und der Implementierung rekursiver Methoden.\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
                "Oder, Sie markieren manuell die Assoziation und \n" +
                "setzen alle Aggregatfunktionen auf \"Keine\"." },
        { "CrNavFromInterface_head" ,
                "Entfernen Sie die Steuerung aus der Schnittstelle \u00fcber die Assoziation \"<ocl>self</ocl>\"" },
        { "CrNavFromInterface_desc" ,
                "Assoziationen, die eine Schnittstelle enthalten, k\u00f6nnen  von der Schnittstelle nicht " +
                "gesteuert werden. Dies ist so, weil Schnittstellen nur Methodendeklarationen " +
                "und keine Zeiger auf andere Objekte enthalten k\u00f6nnen.\n" +
                "\n" +
                "Dieser Teil des Entwurfes sollte ge\u00e4ndert werden, bevor Sie aus diesem " +
                "Entwurf Code generieren. Wenn Sie den Code generieren, bevor Sie dieses Problem " +
                "behoben haben, wird der Code nicht mit dem Entwurf \u00fcbereinstimmen.\n" +
                "\n" +
                "Zur Problembeseitigung markieren Sie die Assoziation und verwenden Sie das " +
                "Registerblatt \"Eigenschaften\", um alle Assoziationsenden zu markieren, die " +
                "NICHT mit der Schnittstelle verbunden sind. Entfernen Sie die Eigenschaft " +
                "\"Steuerbar\" f\u00fcr jedes dieser Enden. \n" +
                "\n" +
                "Die Assoziation sollte dann als d\u00fcnne Pfeilspitze " +
                "auf die Schnittstelle erscheinen.\n" +
                "\n" +
                "WARNUNG: Die aktuelle Version von ArgoUML enth\u00e4lt einen bekannten " +
                "Fehler. Es erzeugt eine Assoziation zwischen einer Klasse und einer " +
                "Schnittstelle, die entweder in beiden Richtungen steuerbar, oder nur " +
                "von der Schnittstelle zur Klasse steuerbar ist. Die L\u00f6sung liegt " +
                "im Entfernen der Eigenschaft \"steuerbar\" aus dem Klassenende der " +
                "Assoziation und, sofern notwendig, dem Hinzuf\u00fcgen der Eigenschaft " +
                "\"steuerbar\" zum Schnittstellenende (steuerbar bezieht sich auf die " +
                "Steuerbarkeit in RICHTUNG des Endes), wenn dieser Hinweis ausgel\u00f6st " +
                "wird." },
        { "CrUnnavigableAssoc_head" ,
                "Machen Sie \"<ocl>self</ocl>\" steuerbar." },
        { "CrUnnavigableAssoc_desc" ,
                "Die Assoziation \"<ocl>self</ocl>\" ist in keine Richtung steuerbar. Alle " +
                "Assoziationen sollten mindestens in einer Richtung steuerbar sein.\n" +
                "\n" +
                "Die Einstellung der Steuerbarkeit von Assoziationen erlaubt es Ihrem Code " +
                "auf Daten durch nachfolgenden Zeiger zuzugreifen. \n" +
                "\n" +
                "Zur Problembeseitigung markieren Sie im Diagramm oder im Navigationsfenster " +
                "die Assoziation \"<ocl>self</ocl>\", und klicken auf das Registerblatt \"Eigenschaften\". " +
                "Dann verwenden Sie die unten befindlichen Checkboxen des Eigenschaftsfensters " +
                "und schalten die Steuerbarkeit ein. " },
        { "CrNameConflictAC_head" ,
	        	"Der Rollenname widerspricht einem Klassenmerkmal" },
        { "CrNameConflictAC_desc" ,
				"Der Rollenname einer Assoziationsklasse darf nicht in Konflikt mit den " +
				"Namen von Klassenmerkmalen (z.B. Klassenvariablen) stehen.\n" },
        { "CrMissingClassName_head" ,
	        	"W\u00e4hlen Sie einen Namen aus." },
        { "CrMissingClassName_desc" ,
				"Jede Klasse und jede Schnittstelle innerhalb eines Pakets mu\u00df einen " +
				"Namen haben. \n" +
				"\n" +
				"Die klare und unzweideutige Namensgebung ist ein Schl\u00fcsselelement " +
				"f\u00fcr die Codegenerierung, " +
				"sowie f\u00fcr die Verst\u00e4ndlichkeit und Wartbarkeit des Entwurfes. \n" +
				"\n" +
				"Zur Problembeseitigung benutzen Sie die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
				"Oder, Sie markieren die Klasse und benutzen das Registerblatt " +
				"\"Eigenschaften\", um ihr einen Namen zu geben." },
        { "CrMissingAttrName_head" ,
	        	"W\u00e4hlen Sie einen Namen aus." },
        { "CrMissingAttrName_desc" ,
				"Jedes Attribut mu\u00df einen Namen haben. \n" +
				"\n" +
				"Die klare und unzweideutige Namensgebung ist ein Schl\u00fcsselelement " +
				"f\u00fcr die Codegenerierung, " +
				"f\u00fcr die Verst\u00e4ndlichkeit und Wartbarkeit des Entwurfes. \n" +
				"\n" +
				"Zur Problembeseitigung benutzen Sie die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
				"Oder, Sie markieren das Attribut von Hand und benutzen Sie das Registerblatt " +
				"\"Eigenschaften\", um ihm einen Namen zu geben" },
        { "CrMissingOperName_head" ,
	        	"W\u00e4hlen Sie einen Namen aus." },
        { "CrMissingOperName_desc" ,
				"Jede Methode mu\u00df einen Namen haben. \n" +
				"\n" +
				"Die klare und unzweideutige Namensgebung ist ein Schl\u00fcsselelement " +
				"f\u00fcr die Codegenerierung, " +
				"Verst\u00e4ndlichkeit und Wartbarkeit des Entwurfes. \n" +
				"\n" +
				"Zur Problembeseitigung benutzen Sie die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
				"Oder, Sie markieren die Methode von Hand und benutzen das Registerblatt " +
				"\"Eigenschaften\", um ihr einen Namen zu geben" },
        { "CrMissingStateName_head" ,
	        	"W\u00e4hlen Sie einen Namen aus." },
        { "CrMissingStateName_desc" ,
				"Jeder Zustand in einem Zustandsautomaten sollte einen Namen haben. \n" +
				"\n" +
				"Die klare und unzweideutige Namensgebung ist ein Schl\u00fcsselelement " +
				"f\u00fcr die Verst\u00e4ndlichkeit und Wartbarkeit des Entwurfes. \n" +
				"\n" +
				"Zur Problembeseitigung benutzen Sie die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
				"Oder, Sie markieren den Zustand von Hand und benutzen das Registerblatt " +
				"\"Eigenschaften\", um ihm einen Namen zu geben. Oder, Sie markieren den Zustand " +
				"und tippen einen Namen ein." },
        { "CrNoInstanceVariables_head" ,
	        	"F\u00fcgen Sie in \"<ocl>self</ocl>\" Klassenvariablen ein." },
        { "CrNoInstanceVariables_desc" ,
                "Sie haben bis jetzt in \"<ocl>self</ocl>\" noch keine Klassenvariablen " +
                "spezifiziert. Normalerweise haben Klassen Klassenvariablen, die " +
                "Zustandsinformationen \u00fcber jede Instanz speichern. Klassen, die " +
                "nur statische Attribute und Methoden enthalten, sollten das Stereotyp " +
                "<<utility>> erhalten.\n" +
                "\n" +
                "Das Definieren von Klassenvariablen ist erforderlich, um den " +
                "Informationsteil Ihres Entwurfes zu vervollst\u00e4ndigen. \n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
                "Oder, Sie f\u00fcgen Klassenvariable durch einen Doppelklick auf \"<ocl>self</ocl>\" " +
                "im Navigationsfenster hinzu und verwenden das Pop-up-Men\u00fc \"Hinzuf\u00fcgen\", um " +
                "ein neues Attribut zu erstellen. " },
        { "CrNoAssociations_head" ,
	        	"F\u00fcgen Sie Assoziationen zu \"<ocl>self</ocl>\" hinzu" },
        { "CrNoAssociations_desc" ,
				"Sie haben bis jetzt f\u00fcr \"<ocl>self</ocl>\" keine Assoziationen spezifiziert. " +
				"Normalerweise sind Klassen, Akteure und Anwendungsf\u00e4lle mit anderen " +
				"assoziiert. \n" +
				"\n" +
				"Das Definieren von Assoziationen zwischen Objekten ist ein wichtiger Teil Ihres " +
				"Entwurfes. \n" +
				"\n" +
				"Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
				"Oder, Sie f\u00fcgen die Assoziationen von Hand hinzu. Dazu klicken Sie auf " +
				"das Symbol \"Assoziation\" in der Werkzeugleiste und erzeugen die Assoziationen von und " +
				"zu \"<ocl>self</ocl>\" per Drag und Drop." },
        { "CrNonAggDataType_head" ,
                "Verpacke Datentyp" },
        { "CrNonAggDataType_desc" ,
                "Datentypen sind keine vollst\u00e4ndigen Klassen und d\u00fcrfen nicht " +
                "mit Klassen assoziiert werden, es sei denn, der Datentyp ist Teil " +
                "einer Komposition (schwarzer Diamand). \n" +
                "\n" +
                "Ein gutes OO-Design h\u00e4ngt davon ab, \u00fcber welche " +
                "Entit\u00e4ten vollst\u00e4ndige Objekte abgebildet, und " +
                "wie die Attribute von Objekten dargestellt werden.\n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
                "Oder, Sie ersetzen den Datentyp von Hand durch eine vollst\u00e4ndige Klasse. " +
                "Oder, Sie \u00e4ndern die Assoziationsaggregation, um eine vollst\u00e4ndige Klasse " +
                "zur\u00fcckzuerhalten.\n" },
        { "CrOppEndConflict_head" ,
	        	"Benennen Sie die Assozationsfunktionen um" },
        { "CrOppEndConflict_desc" ,
				"In \"<ocl>self</ocl>\" haben zwei Funktionen den gleichen Namen. Funktionen " +
				"m\u00fcssen unterschiedliche Namen haben.\n" +
				"\n" +
				"Die klare und unzweideutige Namensgebung ist ein Schl\u00fcsselelement " +
				"f\u00fcr die Codegenerierung " +
				"und f\u00fcr das Erstellen eines verst\u00e4ndlichen und wartbaren Entwurfes.\n" +
				"\n" +
				"Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
				"Oder, Sie markieren die Funktion am anderen Ende der Assoziation dieser Klasse " +
				"und \u00e4ndern ihren Namen." },
        { "CrParamTypeNotImported_head" ,
	        	"Importieren Sie den Parametertyp in die Klasse" },
        { "CrParamTypeNotImported_desc" ,
				"Der Typ eines jeden Methodenparameters mu\u00df sichtbar sein und in die " +
				"Klasse importiert werden, welche die Methode beinhaltet.\n" +
				"\n" +
				"Das Importieren von Klassen ist f\u00fcr die Codegenerierung erforderlich. " +
				"Eine gute Modularisierung von Klassen in Pakete ist ein Schl\u00fcsselelement " +
				"f\u00fcr einen verst\u00e4ndlichen Entwurf.\n" +
				"\n" +
				"Zur Problembeseitigung benutzen Sie die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
				"Oder, Sie f\u00fcgen der Klasse, welche die Methode enth\u00e4lt, von Hand " +
				"eine \"import\"-Anweisung hinzu." },
        { "CrUselessAbstract_head" ,
                "Definieren Sie eine konkrete (Unter-)Klasse" },
        { "CrUselessAbstract_desc" ,
                "\"<ocl>self</ocl>\" kann das laufende System niemals beeinflussen, " +
                "da es selbst und auch keine seiner Unterklassen jemals Instanzen bilden " +
                "k\u00f6nnen. \n" +
                "\n" +
                "Problembeseitigung: (1) Sie definieren konkrete Unterklassen, " +
                "welche die Schnittstelle der Klasse implementieren; oder (2) " +
                "Sie konkretisieren \"<ocl>self</ocl>\" oder eine seiner existierenden Unterklassen." },
        { "CrUselessInterface_head" ,
	        	"Definieren Sie eine Klasse, um \"<ocl>self</ocl>\" zu implementieren" },
        { "CrUselessInterface_desc" ,
                "\"<ocl>self</ocl>\" kann niemals verwendet werden, da es keine Klasse " +
                "implementiert.\n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie verwenden zum definieren von Klassen " +
                "in der Werkzeugleiste die Schaltfl\u00e4che \"Klasse\" und " +
                "die Schaltfl\u00e4che \"Realisieren\", um eine Beziehung von der Klasse " +
                "zu der hervorgehobenen Schnittstelle herzustellen." },
        { "CrDisambigClassName_head" ,
	        	"W\u00e4hlen Sie einen eindeutigen Namen f\u00fcr \"<ocl>self</ocl>\" aus." },
        { "CrDisambigClassName_desc" ,
				"Jede Klasse und jede Schnittstelle innerhalb eines Paketes mu\u00df einen " +
				"eindeutigen Namen haben. Es gibt mindestens zwei Elemente in diesem Paket " +
				"mit dem Namen \"<ocl>self</ocl>\".\n" +
				"\n" +
				"Die klare und unzweideutige Namensgebung ist ein Schl\u00fcsselelement " +
				" f\u00fcr die Codegenerierung, f\u00fcr die Verst\u00e4ndlichkeit und " +
				"Wartbarkeit des Entwurfes. \n" +
				"\n" +
				"Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
				"Oder, Sie markieren eine der betroffenen Klassen und benutzen das " +
				"Registerblatt \"Eigenschaften\", um ihren Namen zu \u00e4ndern" },
        { "CrDisambigStateName_head" ,
	        	"W\u00e4hlen Sie einen eindeutigen Namen f\u00fcr \"<ocl>self</ocl>\"." },
        { "CrDisambigStateName_desc" ,
                "Jeder Zustand in einer Zustandsmaschine mu\u00df einen eindeutigen Namen haben. " +
                "Es sind mindestens zwei Zust\u00e4nde in dieser Maschine mit \"<ocl>self</ocl>\" " +
                "benannt.\n" +
                "\n" +
                "Die klare und unzweideutige Benennung ist das Schl\u00fcsselelement f\u00fcr die " +
                "Codegenerierung, sowie f\u00fcr die Verst\u00e4ndlichkeit und Pflegbarkeit des Entwurfes. \n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
                "Oder, Sie markieren von Hand einen der konflikttr\u00e4chtigen Zust\u00e4nde " +
                "und verwenden das Registerblatt \"Eigenschaften\", um " +
                "den Namen zu \u00e4ndern." },
        { "CrConflictingComposites_head" ,
                "Entfernen Sie die konflikttr\u00e4chtige Komposition" },
        { "CrConflictingComposites_desc" ,
                "Die Kompositionsfunktion (schwarzer Diamand) einer Assoziation " +
                "zeigt, da\u00df Instanzen dieser Klasse Instanzen der " +
                "assoziierten Klasse enthalten k\u00f6nnen. Da jede Instanz " +
                "h\u00f6chstens in einem anderen Projekt enthalten sein darf, darf jedes Objekt " +
                "h\u00f6chstens \"Teil\" einer Ist-Teil-von-Beziehung sein.\n" +
                "\n" +
                "Gutes OO-Design h\u00e4ngt von der Bildung guter Ist-Teil-von-Beziehungen " +
                "ab.\n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie \u00e4ndern von Hand eine Assoziation, um " +
                "eine Kardinalit\u00e4t von 0..1 oder 1..1 zu erhalten. Oder, Sie verwenden " +
                "eine andere Art von Aggregation (z.B.: ist ein wei\u00dfer Diamant weniger streng). " +
                "Oder, Sie entfernen eine der Assoziationen" },
        { "CrTooManyAssoc_head" ,
                "Reduzieren Sie die Assoziationen auf \"<ocl>self</ocl>\"" },
        { "CrTooManyAssoc_desc" ,
                "Es gibt zu viele Assoziationen auf die Klasse \"<ocl>self</ocl>\".  " +
                "Immer, wenn eine Klasse im Entwurf zu zentral wird, wird sie zu " +
                "einem Engpass bei der Pflege und mu\u00df h\u00e4ufig ver\u00e4ndert werden. \n" +
                "\n" +
                "Die Definition von Assoziationen zwischen Objekten ist ein wichtiger " +
                "Teil Ihres Entwurfes. \n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie entfernen die Assoziationen von Hand " +
                "durch anklicken einer Assoziation im Navigationsfenster oder Diagramm und dr\u00fccken " +
                "der Taste \"Entf\". " },
        { "CrTooManyAttr_head" ,
	        	"Reduzieren Sie die Anzahl der Attribute in \"<ocl>self</ocl>\"" },
        { "CrTooManyAttr_desc" ,
                "In der Klasse \"<ocl>self</ocl>\" gibt es zu viele Attribute.  " +
                "Immer, wenn eine Klasse im Entwurf zu m\u00e4chtig wird, wird sie zu einem " +
                "Engpass bei der Pflege und mu\u00df h\u00e4ufig ver\u00e4ndert werden. \n" +
                "\n" +
                "Das Definieren von Attributen in Objekten ist ein wichtiger Teil Ihres Entwurfes. \n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie entfernen Attribute im Diagramm von Hand, " +
                "durch einen Doppel-Klick auf den Attributbereich der hervorgehobenen Klasse " +
                "und entfernen der Textzeile des Attributes. " },
        { "CrTooManyOper_head" ,
	        	"Reduzieren Sie die Anzahl der Methoden in \"<ocl>self</ocl>\"." },
        { "CrTooManyOper_desc" ,
                "In der Klasse \"<ocl>self</ocl>\" gibt es zu viele Methoden.  " +
                "Immer, wenn eine Klasse in Ihrem Entwurf zu m\u00e4chtig wird, wird sie zu einem " +
                "Pflegeengpass und mu\u00df h\u00e4ufig ver\u00e4ndert werden. \n" +
                "\n" +
                "Das Definieren der Objektmethoden ist ein wichtiger Teil Ihres Entwurfes. \n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie entfernen Methoden im Diagramm von Hand " +
                "durch einen Doppel-Klick auf den Methodenbereich der hervorgehobenen Klasse und " +
                "entfernen der Textzeile der Methode. " },
        { "CrTooManyStates_head" ,
                "Reduzieren Sie die Zust\u00e4nde im Zustandsautomaten \"<ocl>self</ocl>\"" },
        { "CrTooManyStates_desc" ,
                "Es gibt zu viele Zust\u00e4nde in \"<ocl>self</ocl>\".  Wenn ein Zustandsautomat " +
                "zu viele Zust\u00e4nde enth\u00e4lt, wird er f\u00fcr Menschen unverst\u00e4ndlich. \n" +
                "\n" +
                "Das Definieren eines verst\u00e4ndlichen Satzes von Zust\u00e4nden ist ein wichtiger " +
                "Teil Ihres Entwurfes. \n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie entfernen Zust\u00e4nde von Hand durch anklicken des " +
                "Zustandes im Navigationsfenster oder im Diagramm und dr\u00fccken der Taste " +
                "\"Entf\".  Oder, Sie k\u00f6nnen Zust\u00e4nde verschachteln..." },
        { "CrTooManyTransitions_head" ,
                "Reduzieren Sie Zustands\u00fcberg\u00e4nge (Transitionen) bei \"<ocl>self</ocl>\"" },
        { "CrTooManyTransitions_desc" ,
                "Es gibt zu viele Zustands\u00fcberg\u00e4nge (Transitionen) im Zustand " +
                "\"<ocl>self</ocl>\". Immer wenn ein Zustand im Zustandsautomaten zu m\u00e4chtig wird, " +
                "wird er zu einem Pflegeengpass und mu\u00df h\u00e4ufig ver\u00e4ndert werden. \n" +
                "\n" +
                "Das Definieren von Zustands\u00fcberg\u00e4nge (Transitionen) zwischen " +
                "Zust\u00e4nden ist ein wichtiger Teil Ihres Entwurfes. \n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie entfernen Zustands\u00fcberg\u00e4nge " +
                "(Transitionen) von Hand durch anklicken eines Zustands\u00fcberganges " +
                "im Navigationsfenster oder im Diagramm und dr\u00fccken " +
                "der Taste \"Entf\". " },
        { "CrTooManyClasses_head" ,
	        	"Reduzieren Sie die Anzahl der Klassen im Diagramm \"<ocl>self</ocl>\"." },
        { "CrTooManyClasses_desc" ,
                "In \"<ocl>self</ocl>\" gibt es zu viele Klassen.  Wenn ein Klassendiagramm " +
                "zu viele Klassen aufweist, wird es f\u00fcr Menschen unverst\u00e4ndlich. \n" +
                "\n" +
                "Das Definieren eines verst\u00e4ndlichen Satzes von Klassendiagrammen ist " +
                "ein wichtiger Teil Ihres Entwurfes. \n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie entfernen Klassen von Hand durch das Anklicken " +
                "einer Klasse im Navigationsfenster oder im Diagramm und dr\u00fccken der " +
                "Taste \"Entf\".  Oder, Sie k\u00f6nnen ein neues Diagramm erstellen..." },
        { "CrNoTransitions_head" ,
	        	"Zustands\u00fcberg\u00e4nge zu <ocl>self</ocl hinzuf\u00fcgen" },
        { "CrNoTransitions_desc" ,
				"Der Zustand \"<ocl>self</ocl>\" hat keine ein- oder ausgehenden " +
				"Zustands\u00fcberg\u00e4nge. Normalerweise haben Zust\u00e4nde ein- und " +
				"ausgehende Zustands\u00fcberg\u00e4nge. \n" +
				"\n" +
				"Die Definition von vollst\u00e4ndigen Zust\u00e4nden und Zustands\u00fcberg\u00e4ngen " +
				"ist erforderlich, um das Verhalten Ihres Entwurfs vollst\u00e4ndig zu beschreiben.\n" +
				"\n" +
				"Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che \"N\u00e4chstes >>\". " +
				"Oder, Sie f\u00fcgen die Zustands\u00fcberg\u00e4nge per Hand hinzu. Dazu klicken " +
				"Sie auf das Zustands\u00fcbergangssymbol in der Werkzeugleiste und erzeugen die " +
				"Zustands\u00fcberg\u00e4nge von und zu \"<ocl>self</ocl>\" per Drag und Drop." },
        { "CrNoIncomingTransitions_head" ,
	        	"Kommende Zustands\u00fcberg\u00e4nge zu \"<ocl>self</ocl>\" hinzuf\u00fcgen." },
        { "CrNoIncomingTransitions_desc" ,
                "Zustand \"<ocl>self</ocl>\" hat keinen kommende Zustands\u00fcbergang. Normalerweise " +
                "haben Zust\u00e4nde kommende und gehende Zustands\u00fcberg\u00e4nge. \n" +
                "\n" +
                "Das Definieren vollst\u00e4ndiger Zustands\u00fcberg\u00e4nge wird f\u00fcr die " +
                "Spezifikation des Verhaltens Ihres Entwurfes ben\u00f6tigt. Ohne kommenden " +
                "Zustands\u00fcbergang kann dieser Zustand niemals erreicht werden.\n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie f\u00fcgen Transitionen von Hand durch anklicken des " +
                "Transitionswerkzeuges in der Werkzeugleiste und ziehen von einem anderen " +
                "Zustand zu \"<ocl>self</ocl>\" hinzu. " },
        { "CrNoOutgoingTransitions_head" ,
	        	"Gehende Zustands\u00fcberg\u00e4nge zu \"<ocl>self</ocl>\" hinzuf\u00fcgen." },
        { "CrNoOutgoingTransitions_desc" ,
                "Der Zustand \"<ocl>self</ocl>\" hat keine gehende Zustands\u00fcberg\u00e4nge " +
                "(Transitionen). Normalerweise haben Zust\u00e4nde kommende und gehende " +
                "Zustands\u00fcberg\u00e4nge (Transitionen). \n" +
                "\n" +
                "Das Definieren vollst\u00e4ndiger Transitionen wird f\u00fcr die Spezifikation " +
                "des Verhaltens Ihres Entwurfes ben\u00f6tigt. Ohne gehende Transition ist dieser " +
                "Zustand ein \"toter\" Zustand, der niemals verlassen werden kann.\n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie f\u00fcgen Transitionen von Hand durch anklicken des " +
                "Transitionswerkzeuges in der Werkzeugleiste und ziehen von einem anderen " +
                "Zustand zu \"<ocl>self</ocl>\" hinzu. " },
        { "CrMultipleInitialStates_head" ,
                "Entfernen Sie zus\u00e4tzliche Anfangszust\u00e4nde" },
        { "CrMultipleInitialStates_desc" ,
                "Es gibt mehrere, mehrdeutige Anfangszust\u00e4nde in diesem Zustandsautomaten. " +
                "Normalerweise hat jeder Zustandsautomat oder jede Komposition einen " +
                "Anfangszustand. \n" +
                "\n" +
                "Das Definieren eindeutiger Zust\u00e4nde wird ben\u00f6tigt, um die Spezifikation " +
                "des Verhaltens Ihres Entwurfes zu vervollst\u00e4ndigen.\n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie markieren von Hand einen der zus\u00e4tzlichen " +
                "Anfangszust\u00e4nd und entfernen ihn. " },
        { "CrNoInitialState_head" ,
	        	"F\u00fcgen Sie einen Anfangszustand hinzu" },
        { "CrNoInitialState_desc" ,
                "In diesem Zustandsautomaten oder in dieser Komposition gibt es keinen " +
                "Anfangszustand. Normalerweise hat jeder Zustandsautomat oder jede " +
                "Komposition einen Anfangszustand. \n" +
                "\n" +
                "Das Definieren eindeutiger Zust\u00e4nde wird ben\u00f6tigt, um das Verhalten Ihres " +
                "Entwurfes zu spezifizieren.\n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie f\u00fcgen einen Anfangszustand von Hand aus der " +
                "Werkzeugleiste ein und plazieren diesen im Diagramm. " },
        { "CrNoTriggerOrGuard_head" ,
	        	"Dem Zustands\u00fcbergang ein Signal oder einen W\u00e4chter hinzuf\u00fcgen." },
        { "CrNoTriggerOrGuard_desc" ,
                "Der hervorgehobene Zustands\u00fcbergang (Transition) ist unvollst\u00e4ndig, weil " +
                "er kein Signal- oder keinen W\u00e4chter aufweist.  Signale sind Ereignisse, die " +
                "einen Zustands\u00fcbergang ausl\u00f6sen.  W\u00e4chter m\u00fcssen f\u00fcr " +
                "den Zustands\u00fcbergang wahr sein, damit er ausgef\u00fchrt werden kann.  " +
                "Wird nur ein W\u00e4chter verwendet, wird der Zustands\u00fcbergang " +
                "ausgel\u00f6st, wenn die die Bedingung wahr wird.\n" +
                "\n" +
                "Dieses Problem mu\u00df gel\u00f6st werden, um den Zustandsautomaten zu " +
                "vervollst\u00e4ndigen.\n" +
                "\n" +
                "Zur Problembeseitigung markieren Sie den Zustands\u00fcbergang und verwenden " +
                "das Registerblatt \"Eigenschaften\". Oder, Sie markieren den Zustands\u00fcbergang " +
                "und geben den folgenden Text ein:\nTRIGGER [GUARD] / ACTION\n" +
                "Dabei ist TRIGGER ein Ereignisname, GUARD ein Ausdruck vom Typ boolean und " +
                "ACTION eine Aktion, die ausgef\u00fchrt wird, wenn die Transition ausgel\u00f6st " +
                "wurde.  Alle drei Teile sind optional." },
        { "CrNoGuard_head" ,
	        	"F\u00fcgen Sie dem Zustands\u00fcbergang einen W\u00e4chter hinzu" },
        { "CrNoGuard_desc" ,
                "Der hervorgehobene Zustands\u00fcbergang (Transition) ist unvollst\u00e4ndig, weil " +
                "er keinen W\u00e4chter aufweist.  W\u00e4chter-Bedingungen m\u00fcssen erf\u00fcllt sein, " +
                "bevor der Zustands\u00fcbergang erfolgen kann.  Wenn nur ein W\u00e4chter " +
                "verwendet wird, erfolgt der Zustands\u00fcbergang, wenn die Bedingung wahr wird.\n" +
                "\n" +
                "Dieses Problem mu\u00df gel\u00f6st werden, um den Zustandsautomaten zu " +
                "vervollst\u00e4ndigen.\n" +
                "\n" +
                "Zur Problembeseitigung markieren Sie den Zustands\u00fcbergang und verwenden " +
                "das Registerblatt \"Eigenschaften\". Oder, Sie markieren den Zustands\u00fcbergang " +
                "und geben den folgenden Text ein:\nGUARD\n" +
                 "Dabei ist GUARD ein Ausdruck vom Typ \"boolean\"." },
        { "CrInvalidFork_head" ,
                "\u00c4ndere Verzweigungs-Zustands\u00fcberg\u00e4nge" },
        { "CrInvalidFork_desc" ,
                "Dieser Verzweigungszustand weist eine ung\u00fcltige Anzahl von Zustands\u00fcberg\u00e4ngen " +
                "auf. Normalerweise haben Verzweigungszust\u00e4nde einen kommenden und zwei " +
                "oder mehrere gehende Zustands\u00fcberg\u00e4nge. \n" +
                "\n" +
                "Das Definieren korrekter Zustands\u00fcberg\u00e4nge wird ben\u00f6tigt, " +
                "um das Verhalten Ihres Entwurfes zu vervollst\u00e4ndigen. \n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie entfernen Zustands\u00fcberg\u00e4nge von " +
                "Hand durch Anklicken des Zustands\u00fcberganges im Diagramm und " +
                "dr\u00fccken der Taste \"Entf\". " },
        { "CrInvalidJoin_head" ,
                "\u00c4ndere Verkn\u00fcpfungs-Zustands\u00fcberg\u00e4nge" },
        { "CrInvalidJoin_desc" ,
                "Dieser Verkn\u00fcpfungs-Zustand weist eine ung\u00fcltige Anzahl von " +
                "Zustands\u00fcberg\u00e4ngen auf. Normalerweise haben Verkn\u00fcpfungszust\u00e4nde " +
                "zwei oder mehrere kommende und einen gehenden Zustands\u00fcbergang. \n" +
                "\n" +
                "Das Definieren korrekter Zustands\u00fcberg\u00e4nge wird ben\u00f6tigt, um das Verhalten " +
                "Ihres Entwurfes zu vervollst\u00e4ndigen. \n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie entfernen Zustands\u00fcberg\u00e4nge von Hand durch " +
                "Anklicken des Zustands\u00fcberganges im Diagramm und dr\u00fccken der Taste \"Entf\". " },
        { "CrInvalidBranch_head" ,
                "\u00c4ndere Verzweigungs-Zustands\u00fcberg\u00e4nge" },
        { "CrInvalidBranch_desc" ,
                "Dieser Verzweigungszustand weist eine ung\u00fcltige Anzahl von Zustands\u00fcberg\u00e4ngen " +
                "auf. Normalerweise haben Verzweigungszust\u00e4nde einen kommenden und zwei " +
                "oder mehrere gehende Zustands\u00fcberg\u00e4nge. \n" +
                "\n" +
                "Das Definieren korrekter Zustands\u00fcberg\u00e4nge wird ben\u00f6tigt, um das Verhalten " +
                "Ihres Entwurfes zu vervollst\u00e4ndigen. \n" +
                "\n" +
                "Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie entfernen Zustands\u00fcberg\u00e4nge von Hand durch " +
                "Anklicken des Zustands\u00fcberganges im Diagramm und dr\u00fccken der Taste \"Entf\". " +
                "Oder, Sie f\u00fcgen Zustands\u00fcberg\u00e4nge mit dem Werkzeug Zustands\u00fcberg\u00e4nge " +
                "hinzu. " },
        { "CrEmptyPackage_head" ,
	        "F\u00fcgen Sie dem Paket \"<ocl>self</ocl>\" Elemente hinzu" },
        { "CrEmptyPackage_desc" ,
                "Sie haben bis jetzt noch keine Elemente in das Paket \"<ocl>self</ocl>\" " +
                "eingef\u00fcgt. Normalerweise enthalten Pakete Gruppen " +
                "zusammengeh\u00f6render Klassen.\n" +
                "\n" +
                "Das Definieren und Verwenden von Paketen ist ein Schl\u00fcsselelement " +
                "beim Erstellen eines pflegbaren Entwurfes. \n" +
                "\n" +
                "Um dieses Problem zu l\u00f6sen, markieren Sie im Navigationsfenster das " +
                "Paket \"<ocl>self</ocl>\" und f\u00fcgen Diagramme oder Modellelemente, wie " +
                "Klassen oder Anwendungsf\u00e4lle hinzu. " },
        { "CrNoOperations_head" ,
	        	"Definieren Sie Methoden f\u00fcr die Klasse \"<ocl>self</ocl>\"." },
        { "CrNoOperations_desc" ,
                "Sie haben bis jetzt keine Methoden f\u00fcr \"<ocl>self</ocl>\" spezifiziert. " +
                "Normalerweise enthalten Klassen Methoden, die deren Verhalten definieren.\n" +
                "\n" +
                "Das Definieren von Methoden wird ben\u00f6tigt, um das Verhalten Ihres Entwurfes " +
                "zu vervollst\u00e4ndigen. \n" +
                "\n" +
                "Zur Problembeseitigung dr\u00fccken Sie die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie f\u00fcgen Methoden von Hand durch Klicken auf " +
                "\"<ocl>self</ocl>\" im Navigationsfenster und verwenden des Pop-up-Men\u00fcs " +
                "\"Hinzuf\u00fcgen\" hinzu." },
        { "CrConstructorNeeded_head" ,
	        	"Definieren Sie einen Konstruktor f\u00fcr die Klasse \"<ocl>self</ocl>\"." },
        { "CrConstructorNeeded_desc" ,
                "Sie haben bis jetzt keinen Konstruktor f\u00fcr die Klasse \"<ocl>self</ocl>\" " +
                "definiert. Konstruktoren initialisieren neue Instanzen, soda\u00df ihre " +
                "Attribute g\u00fcltige Werte aufweisen.  Diese Klasse ben\u00f6tigt " +
                "wahrscheinlich einen Konstruktor, weil nicht alle seine Attribute " +
                "Anfangswerte aufweisen. \n" +
                "\n" +
                "Das Definieren guter Konstruktoren ist der Schl\u00fcssel f\u00fcr die " +
                "Etablierung unver\u00e4nderlicher Klasseninstanzen. Unver\u00e4nderliche " +
                "Klasseninstanzen sind eine gro\u00dfe Hilfe beim Schreiben stabilen Codes. \n" +
                "\n" +
                "Zur Problembeseitigung dr\u00fccken Sie die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\" und f\u00fcgen einen Konstruktor mit Hilfe des " +
                "kontextsensitiven Pop-Up-Men\u00fcs des Registerblattes \"Eigenschaften\" " +
                "hinzu. Oder, Sie markieren im Klassendiagramm \"<ocl>self</ocl>\" und " +
                "verwenden den Men\u00fcpunkt \"Methode hinzuf\u00fcgen\".\n" +
                "\n" +
                "Ein Konstruktor ist eine Methode mit dem Stereotypen <<create>>.\n" +
                "\n" +
                "Ein Konstruktor hat per Konvention (Java, C++) den gleichen Namen wie die " +
                "Klasse, ist nicht statisch und gibt keinen R\u00fcckgabewert zur\u00fcck " +
                "(das bedeutet, Sie müssen den return-Parameter, den ArgoUML " +
                "standardm\u00e4ssig hinzuf\u00fcgt, entfernen). ArgoUML wird jede " +
                "Methode akzeptieren, die diesen Konventionen folgt, auch wenn er nicht " +
                "den Sterotyp <<create>> aufweist."},
        { "CrNameConfusion_head" ,
	        	"\u00c4ndern Sie den Namen um Verwirrung zu vermeiden" },
        { "CrNameConfusion_desc" ,
                "Namen sollten klar voneinander unterschieden werden k\u00f6nnen. Diese " +
                "beiden Namen sind so \u00e4hnlich, da\u00df andere Leser verwirrt sein k\u00f6nnten.\n" +
                "\n" +
                "Eine klare und eindeutige Namensvergabe ist ein Schl\u00fcsselelement " +
                "f\u00fcr die Code-Generierung und f\u00fcr die Verst\u00e4ndlichkeit " +
                "und Pflegbarkeit des Entwurfes. \n" +
                "\n" +
                "Zur Problembeseitigung verwenden Sie die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie markieren die Elemente von Hand und verwenden " +
                "das Registerblatt \"Eigenschaften\", um deren Namen zu \u00e4ndern.  " +
                "Vermeiden Sie Namen, die sich von anderen nur durch Gro\u00df-/Kleinschreibung, " +
                "den Gebrauch von Unterstrichen, oder nur durch ein Zeichen unterscheiden." },
        { "CrMergeClasses_head" ,
	        	"Sie sollten erw\u00e4gen, die Klassen zusammenzufassen" },
        { "CrMergeClasses_desc" ,
                "Die hervorgehobene Klasse \"<ocl>self</ocl>\" nimmt nur an einer Assoziation " +
                "teil. Und diese Assoziation ist eins-zu-eins mit einer anderen Klasse " +
                "verbunden.  Da Instanzen dieser beiden Klassen immer gemeinsam erzeugt und " +
                "gel\u00f6scht werden m\u00fcssen, k\u00f6nnte das Kombinieren dieser Klassen " +
                "Ihren Entwurf vereinfachen, ohne an Klarheit zu verlieren.  Sie k\u00f6nnten " +
                "jedoch die kombinierte Klasse als zu gro\u00df und zu komplex empfinden. In " +
                "diesem Fall ist die Trennung wahrscheinlich besser.\n" +
                "\n" +
                "Die Organisation von Klassen zum Managen der Komplexit\u00e4t des Entwurfes " +
                "ist immer wichtig. Speziell dann, wenn das Design bereits komplex ist. \n" +
                "\n" +
                "Um dieses Problem zu l\u00f6sen, klicken Sie auf die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\", oder f\u00fcgen Sie die Attribute und Methoden der hervorgehobenen " +
                "Klasse manuell der anderen Klasse hinzu. Dann entfernen Sie die " +
                "hervorgehobene Klasse aus dem Projekt. " },
        { "CrSubclassReference_head" ,
                "Entfernen Sie die Referenz auf die spezielle Unterklasse" },
        { "CrSubclassReference_desc" ,
                "Die Klasse \"<ocl>self</ocl>\" hat eine Referenz auf eine seiner Unterklassen. " +
                "Normalerweise sollten alle Unterklassen durch Ihre Superklasse \"gleich\" " +
                "behandelt werden.  Dieses erlaubt das Hinzuf\u00fcgen von Unterklassen ohne " +
                "Ver\u00e4nderung der Superklasse. \n" +
                "\n" +
                "Das Definieren von Assoziationen zwischen Objekten ist ein wichtiger Teil " +
                "Ihres Entwurfes. Einige Assoziationensmuster sind leichter zu handhaben " +
                "als andere, je nach Art der k\u00fcnftigen \u00c4nderungen. \n" +
                "\n" +
                "Zur Problembeseitigung dr\u00fccken Sie die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie entfernen die Assoziation von Hand durch Anklicken " +
                "im Diagramm und dr\u00fccken der Taste \"Entf\". " },
        { "CrComponentWithoutNode_head" ,
	        	"Komponenten befinden sich normalerweise innerhalb von Knoten" },
        { "CrComponentWithoutNode_desc" ,
                "Es gibt Knoten im Diagramm. So haben Sie ein reelles\n Verteilungsdiagramm " +
                "erhalten. In Verteilungsdiagrammen befinden sich Komponenten\n " +
                "normalerweise innerhalb von Knoten." },
        { "CrCompInstanceWithoutNode_head" ,
	        	"Instanzen von Komponenten befinden sich normalerweise innerhalb von Knoten" },
        { "CrCompInstanceWithoutNode_desc" ,
                "Es gibt Knoten-Instanzen im Diagramm. So haben Sie ein reelles\n " +
                "Verteilungsdiagramm erhalten. In Verteilungsdiagrammen befinden sich " +
                "Komponenten\n normalerweise innerhalb Knoten-Instanzen." },
        { "CrClassWithoutComponent_head" ,
	        	"Klassen befinden sich normalerweise innerhalb von Komponenten" },
        { "CrClassWithoutComponent_desc" ,
	        	"In Verteilungsdiagrammen befinden sich Klassen normalerweise innerhalb " +
	        	"von Komponenten" },
        { "CrInterfaceWithoutComponent_head" ,
	        	"Schnittstellen befinden sich normalerweise innerhalb von Komponenten" },
        { "CrInterfaceWithoutComponent_desc" ,
	        	"In Verteilungsdiagrammen befinden sich Schnittstellen normalerweise " +
	        	"innerhalb von Komponenten" },
        { "CrObjectWithoutComponent_head" ,
	        	"Objekte befinden sich normalerweise innerhalb von Komponenten" },
        { "CrObjectWithoutComponent_desc" ,
	        	"In Verteilungsdiagrammen befinden sich Objekte normalerweise innerhalb von " +
	        	"Komponenten oder Instanzen von Komponenten" },
        { "CrNodeInsideElement_head" ,
                "Knoten haben normalerweise keine Begrenzungen" },
        { "CrNodeInsideElement_desc" ,
                "Knoten befinden sich normalerweise nicht innerhalb anderer Elemente. " +
                "Sie sind zur Laufzeit physische Objekte mit einem Verarbeitungsteil, " +
                "haben mindestens einen Speicher und oft auch Verarbeitungsf\u00e4higkeiten." },
        { "CrNodeInstanceInsideElement_head" ,
                "Knoten-Instanzen haben normalerweise keine Begrenzungen" },
        { "CrNodeInstanceInsideElement_desc" ,
                "Knoten-Instanzen befinden sich normalerweise nicht innerhalb anderer Elemente. " +
                "Sie sind zur Laufzeit physische Objekte mit einem Verarbeitungsteil, " +
                "haben mindestens einen Speicher und oft auch Verarbeitungsf\u00e4higkeiten." },
        { "CrWrongLinkEnds_head" ,
                "Die Enden der Verbindungen haben nicht die gleichen Positionen." },
        { "CrWrongLinkEnds_desc" ,
                "In Verteilungsdiagrammen k\u00f6nnen sich Objekte entweder in Komponenten\n " +
                "oder in Komponenten-Instanzen befinden. Daher ist es nicht m\u00f6glich, " +
                "zwei Objekte zu haben, die \u00fcber einen Link verbunden sind, w\u00e4hrend ein " +
                "sich ein Objekt in einer Komponente und \n das andere Objekt in einer " +
                "Komponenten-Instanz befindet.\n" +
                "\n" +
                "\n" +
                "Zur Problembeseitigung entfernen Sie eines der beiden verbundenen Objekte an " +
                "dieser Stelle und verbinden Sie es mit einem Element, welches den gleichen " +
                "Typ aufweist wie das andere Objekt." },
        { "CrInstanceWithoutClassifier_head" ,
                "Setze Klassifizierung." },
        { "CrInstanceWithoutClassifier_desc" ,
                "Instanzen haben eine Klassifizierung." },
        { "CrCallWithoutReturn_head" ,
                "Vermisse return-Aktionen" },
        { "CrCallWithoutReturn_desc" ,
                "Jede call- oder send-Aktion erfordert eine return-Aktion.\n " +
                "Aber diese Verbindung hat keine return-Aktion.\n" },
        { "CrReturnWithoutCall_head" ,
                "Vermisse call(send)-Aktion" },
        { "CrReturnWithoutCall_desc" ,
                "Jede return-Aktion erfordert eine call- oder send-Aktion.\n " +
                "Aber diese Verbindung hat keine entsprechende call- or send-Aktion.\n" },
        { "CrLinkWithoutStimulus_head" ,
                "Es gibt keine Botschaften f\u00fcr diese Verbindungen" },
        { "CrLinkWithoutStimulus_desc" ,
                "In einem Sequenzdiagramm sendet ein Senderobjekt Botschaften\n an " +
                "ein empfangendes Objekt oder eine empfangende Verbindung. Der Link ist nur " +
                "die Kommunikationsverbindung, soda\u00df eine Botschaft ben\u00f6tigt wird. "},
        { "CrSeqInstanceWithoutClassifier_head" ,
                "Setze Klassifizierung." },
        { "CrSeqInstanceWithoutClassifier_desc" ,
                "Instanzen haben eine Klassifizierung." },
        { "CrStimulusWithWrongPosition_head" ,
                "Falsche Position dieser Botschaft" },
        { "CrStimulusWithWrongPosition_desc" ,
                "In Sequenzdiagrammen ist die Senderseite der Kommunikationsverbindung dieser " +
                "Botschaft mit dem Beginn einer Aktivit\u00e4t verbunden. Um ein Sender sein zu " +
                "k\u00f6nnen, mu\u00df ein Objekt zuerst den Fokus erhalten." },
        { "CrUnconventionalOperName_head" ,
	        	"W\u00e4hlen Sie f\u00fcr diese Methode einen besseren Namen." },
        { "CrUnconventionalOperName_desc" ,
				"Normalerweise beginnen die Namen von Methoden mit einem Kleinbuchstaben. " +
				"Der Name \"<ocl>self</ocl>\" ist ungew\u00f6hnlich, da er nicht mit einem " +
				"Kleinbuchstaben beginnt.\n" +
				"\n" +
				"Das Einhalten von sinnvollen Konventionen zur Namensgebung hilft, " +
				"die Verst\u00e4ndlichkeit und Wartbarkeit des Entwurfs zu verbessern. \n" +
				"\n" +
				"Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
				"\"N\u00e4chstes >>\". Oder, Sie markieren \"<ocl>self</ocl>\" von Hand und " +
				"benutzen das Registerblatt \"Eigenschaften\", um einen anderen " +
				"Namen zu vergeben." },
        { "CrUnconventionalAttrName_head" ,
	        	"W\u00e4hlen Sie f\u00fcr das Attribut einen besseren Namen." },
        { "CrUnconventionalAttrName_desc" ,
				"Normalerweise beginnen Namen von Attributen mit einem Kleinbuchstaben. " +
				"Der Name \"<ocl>self</ocl>\" ist ungew\u00f6hnlich, da es nicht mit einem " +
				"Kleinbuchstaben beginnt.\n" +
				"\n" +
				"Das Einhalten von sinnvollen Konventionen zur Namensgebung hilft, " +
				"die Verst\u00e4ndlichkeit und Wartbarkeit des Entwurfs zu verbessern. \n" +
				"\n" +
				"Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
				"\"N\u00e4chstes >>\". Oder, Sie markieren \"<ocl>self</ocl>\" von Hand und " +
				"benutzen das Registerblatt \"Eigenschaften\" um einen anderen " +
				"Namen zu vergeben." },
        { "CrUnconventionalClassName_head" ,
	        	"Beginnen Sie den Klassennamen \"<ocl>self</ocl>\" mit einem Gro\u00dfbuchstaben." },
        { "CrUnconventionalClassName_desc" ,
				"Normalerweise beginnen Klassennamen mit einem Gro\u00dfbuchstaben. " +
				"Der Name \"<ocl>self</ocl>\" ist ungew\u00f6hnlich, da er nicht mit einem " +
				"Gro\u00dfbuchstaben beginnt.\n" +
				"\n" +
				"Das Einhalten von sinnvollen Konventionen zur Namensgebung hilft, die " +
				"Verst\u00e4ndlichkeit und Wartbarkeit des Entwurfs zu verbessern. \n" +
				"\n" +
				"Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
				"\"N\u00e4chstes >>\". Oder, Sie markieren \"<ocl>self</ocl>\" von Hand und " +
				"benutzen das Registerblatt \"Eigenschaften\", um einen anderen Namen zu " +
				"vergeben." },
        { "CrUnconventionalPackName_head" ,
	        	"W\u00e4hlen Sie einen anderen Paketnamen f\u00fcr \"<ocl>self</ocl>\"" },
        { "CrUnconventionalPackName_desc" ,
				"Normalerweise werden Paketnamen durchg\u00e4ngig klein geschrieben. Mit " +
				"Punkten werden \"geschachtelte\" Pakete dargestellt. Der Name \"<ocl>self</ocl>\" " +
				"ist ungew\u00f6hnlich, da er nicht aus kleinen Buchstaben und Punkten " +
				"besteht.\n" +
				"\n" +
				"Das Einhalten von sinnvollen Konventionen zur Namensgebung hilft die " +
				"Verst\u00e4ndlichkeit und Wartbarkeit des Entwurfs zu verbessern. \n" +
				"\n" +
				"Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
				"\"N\u00e4chstes >>\". Oder, Sie markieren \"<ocl>self</ocl>\" von Hand und " +
				"benutzen das Registerblatt \"Eigenschaften\", um einen anderen Namen zu " +
				"vergeben." },
        { "CrClassMustBeAbstract_head" ,
                "Die Klasse mu\u00df abstrakt sein" },
        { "CrClassMustBeAbstract_desc" ,
                "Klassen, die abstrakte Methoden von Basisklassen oder Schnittstellen " +
                "enthalten oder vererben, m\u00fcssen als abstrakt gekennzeichnet werden.\n" +
                "\n" +
                "Die Entscheidung, welche Klassen abstrakt oder konkret sind, ist ein " +
                "Schl\u00fcsselelement f\u00fcr den Entwurf der Klassenhierarchie.\n" +
                "\n" +
                "Zur Problembeseitigung verwenden Sie die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie markieren die Klasse von Hand und verwenden " +
                "das Registerblatt \"Eigenschaften\", um das Schl\u00fcsselwort \"abstract\" " +
                "hinzuzuf\u00fcgen. Oder, Sie \u00fcberschreiben jede abstrakte Methode, die " +
                "von der Basisklasse oder der Schnittstelle vererbt wurde." },
        { "CrReservedName_head" ,
                "\u00c4ndern Sie \"<ocl>self</ocl>\" in ein nicht reserviertes Wort" },
        { "CrReservedName_desc" ,
                "\"<ocl>self</ocl>\" ist ein reserviertes Wort, oder einem reservierten Wort " +
                "sehr \u00e4hnlich. Die Namen von Modellelementen d\u00fcrfen nicht mit " +
                "reservierten W\u00f6rtern aus Programmiersprachen oder der UML in Konflikt " +
                "stehen.\n" +
                "\n" +
                "Die Verwendung legaler Namen wird ben\u00f6tigt, um kompatiblen Code " +
                "generieren zu k\u00f6nnen. \n" +
                "\n" +
                "Zur Problembeseitigung verwenden Sie die Schaltfl\u00e4che " +
                "\"N\u00e4chstes >>\". Oder, Sie markieren das hervorgehobene Element von Hand " +
                "und verwenden das Registerblatt \"Eigenschaften\", um einen unterscheidbaren " +
                "Namen einzugeben." },
        { "CrMultipleInheritance_head" ,
	        	"\u00c4ndern Sie Mehrfachvererbungen in Schnittstellen" },
        { "CrMultipleInheritance_desc" ,
				"\"<ocl>self</ocl>\" hat mehrere Basisklassen. Java unterst\u00fctzt aber keine " +
				"Mehrfachvererbung. Sie m\u00fcssen stattdessen Schnittstellen benutzen. \n" +
				"\n" +
				"Diese \u00c4nderung ist erforderlich, bevor Sie Java Code generieren " +
				"k\u00f6nnen.\n" +
				"\n" +
				"Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
				"\"N\u00e4chstes >>\". Oder, Sie entfernen (1) eine der Basisklassen von Hand, " +
				"und (2) definieren optional eine neue Schnittstelle mit den selben " +
				"Methodendeklarationen und (3) f\u00fcgen diese als Schnittstelle von " +
				"\"<ocl>self</ocl>\" hinzu, und (4) verschieben Sie die Methodenr\u00fcmpfe " +
				"der alten Basisklasse nach unten in \"<ocl>self</ocl>\"." },
        { "CrMultipleRealization_head" ,
                "\u00c4ndern Sie die Mehrfach-Realisierung in <ocl>self</ocl> in eine Vererbungsbeziehung." },
        { "CrMultipleRealization_desc" ,
                "<ocl>self</ocl> implementiert mehrere Schnittstellen. Obwohl dies in UML " +
                "legal ist, wird dieser Code nicht in Java \u00fcbersetzt werden können. " +
                "Ersetzen Sie die Mehrfach-Realisierung durch Vererbungen, wenn Sie " +
                "kompatiblen und fehlerfreien Code erzeugen wollen." },
        { "CrIllegalName_head" ,
	        	"W\u00e4hlen Sie einen erlaubten Namen f\u00fcr \"<ocl>self</ocl>\"" },
        { "CrIllegalName_desc" ,
				"Die Namen von Modellelementen m\u00fcssen aus Folgen von Buchstaben, " +
				"Ziffern und Unterstrichen bestehen. Sie d\u00fcrfen keine Satzzeichen " +
				"enthalten.\n" +
				"\n" +
				"Zur Generierung von \u00fcbersetzbarem Code sind g\u00fcltige Namen " +
				"erforderlich. \n" +
				"\n" +
				"Zur Problembeseitigung klicken Sie auf die Schaltfl\u00e4che " +
				"\"N\u00e4chstes >>\". Oder, Sie markieren das hervorgehobene Element und " +
				"verwenden das Registerblatt \"Eigenschaften\", um einen anderen Namen zu vergeben." },
		{ "CrUtilityViolated_head",
		  		"Das Sterotyp <<utility>> wurde verletzt, Instanzen k\u00f6nnen erzeugt werden." },
		{ "CrUtilityViolated_desc",
				"<ocl>self</ocl> wurde mit dem Stereotyp <<utility>> gekennzeichnet, " +
				"aber sie stimmt nicht mit den f\u00fcr <<utility>> geltenden Restriktionen " +
				"\u00fcberein.\n" +
				"Sie enth\u00e4lt Instanz-Attribute oder -Variablen.\n" +
				"\n" +
				"Wenn Sie wollen, da\u00df diese Klasse nicht länger ein <<utility>> ist, " +
				"entfernen Sie das Stereotyp <<utility>>, indem Sie auf die Klasse klicken " +
				"und im Registerblatt \"Eigenschaften\" die leere Auswahl in der " +
				"Stereotyp-Drop-Down-Liste markieren.\n" },


        // Updated to bring in line with CrSingletonViolated

        { "CrConsiderSingleton_head" ,
	        	"Sie sollten dar\u00fcber nachdenken, f\u00fcr \"<ocl>self</ocl>\" das " +
	        	"\"Singleton\"-Entwurfsmuster zu verwenden."},
        { "CrConsiderSingleton_desc" ,
                "Diese Klasse hat weder nicht-statische Attribute noch irgendwelche " +
                "Assoziationen, die von Instanzen dieser Klasse ferngesteuert werden " +
                "k\u00f6nnen. Das bedeutet, da\u00df jede Instanz dieser Klasse " +
                "mit jeder anderen Instanz \u00fcbereinstimmen wird (gleich sein), da es " +
                "nichts \u00fcber die Instanzen geben wird, die sie von anderen " +
                "Unterscheidbar macht.\n" +
                "\n" +
                "Unter diesen Umst\u00e4nden sollten Sie dar\u00fcber nachdenken, da\u00df " +
                "Sie genau eine Instanz dieser Klasse haben und dies mit Hilfe des " +
                "\"Singleton\"-Musters ausdr\u00fccklich festlegen sollten. Die Verwendung " +
                "des \"Singleton\"-Entwurfsmusters kann Zeit und Speicherplatz sparen. " +
                "In ArgoUML k\u00f6nnen Sie dies erreichen, indem Sie das Stereotyp " +
                "<<singleton>> f\u00fcr diese Klasse verwenden.\n" +
                "\n" +
                "Wenn Sie nicht nur eine einzige Instanz haben wollen, sollten " +
                "Sie Instanzvariablen (z.B. nicht-statische Attribute) und/oder " +
                "gehende Assoziationen definieren, welche die Unterschiede zwischen den " +
                "Instanzen repr\u00e4sentieren.\n" +
                "\n" +
                "Wenn Sie \"<ocl>self</ocl>\" als Singleton spezifiziert haben, m\u00fcssen " +
                "Sie die Klasse so definieren, da\u00df es nur eine einzige Instanz geben " +
                "kann. Dies vervollst\u00e4ndigt den Informationsteil Ihres Entwurfes. Um " +
                "dies zu erreichen, m\u00fcssen Sie folgendes tun.\n" +
                "\n" +
                "1. Ein statisches Attribut (eine Klassenvariable) definieren, welches die " +
                "Instanz aufnimmt. Dieses mu\u00df daher den Typ \"<ocl>self</ocl>\" " +
                "aufweisen.\n" +
                "\n" +
                "2. Erlauben Sie in \"<ocl>self</ocl>\" nur private Konstruktoren, damit " +
                "von anderem Code keine neuen Instanzen erzeugt werden k\u00f6nnen. Die " +
                "Erzeugung der einzelnen Instanz k\u00f6nnte durch eine geeignete Hilfs-" +
                "Operation erfolgen, die diesen privaten Konstruktor genau einmal aufruft.\n" +
                "\n" +
                "3. Sorgen Sie daf\u00fcr, da\u00df mindestens ein Konstruktor den Standard-" +
                "Konstruktor \u00fcberschreibt, soda\u00df der Standardkonstruktor nicht " +
                "dazu verwendet werden kann, mehrere Instanzen zu erzeugen.\n" +
                "\n" +
                "In Java und C++ haben Konstruktoren per Konvention die gleichen Namen " +
                "wie die Klasse, sind nicht statisch und geben keinen R\u00fcckgabewert " +
                "zur\u00fcck. Beachten Sie, da\u00df dies in ArgoUML bedeutet, da\u00df " +
                "Sie den standardm\u00e4ssig f\u00fcr eine Methode erzeugten R\u00fcckgabewert " +
                "entfernen m\u00fcssen. ArgoUML wird jede Methode akzeptieren, die diesen " +
                "Konventionen eines Konstruktors folgen, auch wenn sie nicht mit dem " +
                "Sterotyp <<create>> oder <<Create>> versehen wurde."},

        // Updated to reflect use of <<create>> stereotype for constructors and
        // lack of a wizard at this stage

        { "CrSingletonViolatedMissingStaticAttr_head",
          		"Sterotyp \"Singleton\" verletzt. In \"<ocl>self</ocl>\" wird ein " +
          		"statisches Attribut vermisst." },

        { "CrSingletonViolatedMissingStaticAttr_desc",
				"\"<ocl>self</ocl>\" wurde mit dem Stereotyp <<singleton>> gekennzeichnet, " +
				"aber es erf\u00fcllt nicht die für \"Singletons\" geltenden Restriktionen." +
				"\n" +
				"Sie hat kein statisches Attribut (eine Klassenvariable), welches die " +
				"die Instanz aufnimmt.\n" +
				"\n" +
				"Immer, wenn Sie eine Klasse mit einem Stereotypen kennzeichnen, sollte die " +
				"Klasse allen Restriktionen dieses Stereotyps entsprechen. Dies ist ein " +
				"wichtiger Teil bei der Erstellung eines in-sich-konsistenten und " +
				"verst\u00e4ndlichen Entwurfes. Die Verwendung von \"Singleton\"-Entwurfsmustern " +
				"kann Zeit und Speicherplatz sparen.\n" +
				"\n" +
				"Wenn Sie nicht l\u00e4nger wollen, da\u00df diese Klasse ein \"Singleton\" " +
				"ist, entfernen Sie das Stereotyp <<singleton>>, indem Sie auf die Klasse " +
				"klicken und im Registerblatt \"Eigenschaften\" die leere Auswahl in der " +
				"Stereotypen-Drop-Down-Liste markieren.\n" },

        { "CrSingletonViolatedOnlyPrivateConstructors_head",
          		"Stereotyp \"Singleton\" verletzt, \"<ocl>self</ocl>\" enth\u00e4lt " +
          		"Konstruktor, der nicht mit \"private\" gekennzeichnet ist." },

        { "CrSingletonViolatedOnlyPrivateConstructors_desc",
				"\"<ocl>self</ocl>\" wurde mit dem Stereotyp <<singleton>> gekennzeichnet, " +
				"aber es erf\u00fcllt nicht die für \"Singletons\" geltenden Restriktionen." +
				"\n" +
				"Sie darf nur mit \"private\" gekennzeichnete Konstruktoren aufweisen, damit " +
				"neue Instanzen nicht durch anderen Code erzeugt werden kann.\n" +
				"\n" +
				"Immer, wenn Sie eine Klasse mit einem Stereotypen kennzeichnen, sollte die " +
				"Klasse allen Restriktionen dieses Stereotyps entsprechen. Dies ist ein " +
				"wichtiger Teil bei der Erstellung eines in-sich-konsistenten und " +
				"verst\u00e4ndlichen Entwurfes. Die Verwendung von \"Singleton\"-Entwurfsmustern " +
				"kann Zeit und Speicherplatz sparen.\n" +
				"\n" +
				"Wenn Sie nicht l\u00e4nger wollen, da\u00df diese Klasse ein \"Singleton\" " +
				"ist, entfernen Sie das Stereotyp <<singleton>>, indem Sie auf die Klasse " +
				"klicken und im Registerblatt \"Eigenschaften\" die leere Auswahl in der " +
				"Stereotypen-Drop-Down-Liste markieren.\n" },

        { "CrNodesOverlap_head" ,
                "Diagramm \"<ocl>self</ocl>\" aufr\u00e4umen" },
        { "CrNodesOverlap_desc" ,
                "Einige Objekte in diesem Diagramm \u00fcberlappen und verdunkeln andere Objekte. " +
                "Dies kann wichtige Informationen verdecken und es f\u00fcr Menschen schwer " +
                "verst\u00e4ndlich machen.  Eine ordentliche Erscheinung macht Ihr Diagramm " +
                "auch f\u00fcr andere Designer, Implementierer und Entscheidungstr\u00e4ger " +
                "wirkungsvoller. \n" +
                "\n" +
                "Das Konstruieren eines verst\u00e4ndlichen Satzes von Klassendiagrammen " +
                "ist ein wichtiger Teil Ihres Entwurfes.  \n" +
                "\n" +
                "Um dieses Problem zu beheben, verschieben Sie die hervorgehobenen " +
                "Knoten im Diagramm." },
        { "CrZeroLengthEdge_head" ,
                "Ecke sichtbarer machen" },
        { "CrZeroLengthEdge_desc" ,
                "Diese Ecke ist zu klein, um leicht gesehen zu werden. Dies kann wichtige " +
                "Informationen verecken und es f\u00fcr Menschen schwer verst\u00e4ndlich machen. " +
                "Eine ordentliche Erscheinung macht Ihr Diagramm " +
                "auch f\u00fcr andere Designer, Implementierer und Entscheidungstr\u00e4ger " +
                "wirkungsvoller. \n" +
                "\n" +
                "Das Konstruieren eines verst\u00e4ndlichen Satzes von Diagrammen " +
                "ist ein wichtiger Teil Ihres Entwurfes.  \n" +
                "\n" +
                "Um dieses Problem zu beheben, verschieben Sie eine oder mehrere Knoten, " +
                "soda\u00df die hervorgehobenen Ecken l\u00e4nger werden. Oder, Sie klicken in " +
                "die Mitte der Ecke und ziehen diese, um einem neuen Schnittpunkt zu erzeugen." },
        //
        //   these phrases should be localized here
        //      not in the following check list section
        { "Naming", "Namensgebung" },
        { "Encoding", "Kodierung" },
        { "Value", "Wert" },
        { "Location", "Ort" },
        { "Updates", "Aktualisierungen" },
        { "General", "Allgemein" },
        { "Actions" , "Aktionen" },
        { "Transitions", "Zustands\u00dcberg\u00e4nge" },
        { "Structure", "Struktur" },
        { "Trigger", "Signal" },
        { "MGuard", "MW\u00e4chter" },
        //
        //   The following blocks define the UML related
        //      Checklists.  The key is the name of
        //      the non-deprecated implementing class,
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
                  "Gibt der Name \"<ocl>self</ocl>\" eine klare Beschreibung der Klasse?",
                  "Ist \"<ocl>self</ocl>\" ein Substantiv oder ein substantiv\u00e4hnlicher Ausdruck?",
                  "Kann der Name \"<ocl>self</ocl>\" falsch interpretiert werden und etwas " +
                  "anderes bedeuten?"
                },
                new String[] { "Encoding",
                  "Sollte \"<ocl>self</ocl>\" eine eigene Klasse bilden, oder nur ein " +
                  "einfaches Attribut einer anderen Klasse sein?",
                  "Tut \"<ocl>self</ocl>\" exakt eine einzige Sache und tut sie es richtig?",
                  "Kann \"<ocl>self</ocl>\" in zwei oder mehrere Klassen heruntergebrochen werden?"
                },
                new String[] { "Value",
                  "Starten alle Attribute von \"<ocl>self</ocl>\" mit sinnvollen Werten?",
                  "K\u00f6nnen Sie eine Invariante f\u00fcr diese Klasse schreiben?",
                  "Etablieren alle Konstruktoren die Klassen-Invariante?",
                  "Behandeln alle Methoden die Klassen-Invariante?"
                },
                new String[] { "Location",
                  "Kann \"<ocl>self</ocl>\" an einer anderen Stelle in der Klassenhierarchie " +
                  "definiert werden?",
                  "Haben Sie eine Unterklasse von \"<ocl>self</ocl>\" geplant?",
                  "Kann \"<ocl>self</ocl>\" aus dem Modell entfernt werden?",                  "Gibt es im Modell eine andere Klasse, die \u00fcberarbeitet oder entfernt " +
                  "werden sollte, weil sie dem gleichen Zweck dient wie \"<ocl>self</ocl>\"?"
                },
                new String[] { "Updates",
                  "In welchen Fall wird eine Instanz von \"<ocl>self</ocl>\" aufgefrischt?",
                  "Gibt es andere Objekte, die immer aufgefrischt werden m\u00fcssen, wenn " +
                  "\"<ocl>self</ocl>\" aufgefrischt wurde?"
                }
            }
        },
        { "ChAttribute",
            new String[][] {
                new String[] { "Naming",
                  "Gibt der Name \"<ocl>self</ocl>\" eine klare Beschreibung des Attributes?",
                  "Ist \"<ocl>self</ocl>\" ein Substantiv oder ein substantiv\u00e4hnlicher Ausdruck?",
                  "Kann der Name \"<ocl>self</ocl>\" falsch interpretiert werden und etwas " +
                  "anderes bedeuten?"
                },
                new String[] { "Encoding",
                  "Ist der Typ <ocl>self.type</ocl> zu restriktiv, um alle m\u00f6glichen Werte von " + "\"<ocl>self</ocl>\" darzustellen?",
                  "Erlaubt der Typ <ocl>self.type</ocl> Werte f\u00fcr \"<ocl>self</ocl>\", die niemals " + "richtig sein k\u00f6nnen?",
                  "Kann \"<ocl>self</ocl>\" mit einigen anderen Attributen des " +
                  "<ocl>self.owner</ocl> kombiniert werden (z.B.: {owner.structuralFeature})?",
                  "Kann \"<ocl>self</ocl>\" in zwei oder mehrere Teile aufgeteilt werden " +
                  "(z.B.: eine Telefonnummer kann in den Bereichscode, den Pr\u00e4fix und die " +
                  "Nummer unterteilt werden)?",
                  "Kann \"<ocl>self</ocl>\" von anderen Attributen verarbeitet werden, ohne " +
                  "da\u00df es gespeichert werden mu\u00df?"
                },
                new String[] { "Value",
                  "Sollte \"<ocl>self</ocl>\" einen Anfangs- (oder Standard-) Wert haben?",
                  "Ist der Anfangswert <ocl>self.initialValue</ocl> richtig?",
                  "K\u00f6nnen Sie einen Ausdruck schreiben, um zu pr\u00fcfen, ob \"<ocl>self</ocl>\" " +
                  "richtig ist? Plausibel?"
                },
                new String[] { "Location",
				  "Kann \"<ocl>self</ocl>\" in einer anderen Klasse definiert werden, die " +
				  "mit <ocl>self.owner</ocl> assoziiert ist?",
                  "Kann \"<ocl>self</ocl>\" die Vererbungshierarchie hochsteigen, um es f\u00fcr " +
                  "den owner.name und mit anderen Klassen zu verwenden?",
                  "Kann \"<ocl>self</ocl>\" auf alle Instanzen der Klasse " +
                  "<ocl>self.owner</ocl>, einschlie\u00dflich der Instanzen der Unterklassen " +
                  "angewendet werden?",
                  "Kann \"<ocl>self</ocl>\" aus dem Modell entfernt werden?",
                  "Gibt es ein anderes Attribut im Modell, das \u00fcberarbeitet oder entfernt " +
                  "werden sollte, weil es dem gleichen Zweck wie \"<ocl>self</ocl>\" dient?"
                },
                new String[] { "Updates",
                  "In welchen F\u00e4llen wird \"<ocl>self</ocl>\" aktualisiert?",
                  "Gibt es einige andere Attribute, die immer aktualisiert werden m\u00fcssen, " +
                  "wenn \"<ocl>self</ocl>\" aktualisiert wird?",
                  "Gibt es eine Methode, die aufgerufen werden sollte, wenn " +
                  "\"<ocl>self</ocl>\" aktualisiert wird?",
                  "Gibt es eine Methode, die aufgerufen werden sollte, wenn \"<ocl>self</ocl>\" " +
                  "einen bestimmten Wert erh\u00e4lt?"
                }
            }
        },
        { "ChOperation",
            new String[][] {
                new String[] { "Naming",
                  "Gibt der Name \"<ocl>self</ocl>\" eine klare Beschreibung der Methode?",
                  "Ist \"<ocl>self</ocl>\" ein Verb oder ein verbaler Ausdruck?",
                  "Kann der Name \"<ocl>self</ocl>\" falsch interpretiert werden und etwas " +
                  "anderes bedeuten?",
                  "Tut \"<ocl>self</ocl>\" eine Sache und tut sie es richtig?"
                },
                new String[] { "Encoding",
                  "Ist der Typ des R\u00fcckgabewertes '<ocl>self.returnType</ocl>' zu restriktiv, " +
                  "um alle m\u00f6glichen, von \"<ocl>self</ocl>\" zur\u00fcckgegebenen Werte darzustellen?",
                  "Erlaubt '<ocl>self.returnType</ocl>' R\u00fcckgabewerte, die niemals korrekt " +
                  "sein k\u00f6nnen?",
                  "Kann \"<ocl>self</ocl>\" mit einigen anderen Methoden von <ocl>self.owner</ocl> " +
                  "kombiniert werden (z.B.: <ocl sep=', '>self.owner.behavioralFeature</ocl>)?",
                  "Kann \"<ocl>self</ocl>\" in zwei oder mehrere Teile aufgeteilt werden " +
                  "(z.B.: Vorverarbeitung, Hauptverarbeitung und Nachverarbeitung)?",
                  "Kann \"<ocl>self</ocl>\" durch eine Reihe von Clientaufrufen in einfachere " +
                  "Methoden aufgeteilt werden?",
                  "Kann \"<ocl>self</ocl>\" mit anderen Methoden kombiniert werden, um die Anzahl " +
                  "der Clientaufrufe zu reduzieren?"
                },
                new String[] { "Value",
                  "Kann \"<ocl>self</ocl>\" alle denkbaren Eingaben verarbeiten?",
                  "Gibt es Spezialeingaben, die separat verarbeitet werden m\u00fcssen?",
                  "K\u00f6nnen Sie einen Ausdruck schreiben, um zu pr\u00fcfen, ob die Argumente " +
                  "f\u00fcr  \"<ocl>self</ocl>\" richtig sind? Plausibel?",
                  "K\u00f6nnen Sie die Vorbedingungen von \"<ocl>self</ocl>\" ausdr\u00fccken?",
                  "K\u00f6nnen Sie die Nachbedingungen von \"<ocl>self</ocl>\" ausdr\u00fccken?",
                  "Wie wird sich \"<ocl>self</ocl>\" verhalten, wenn die Vorbedingungen " +
                  "verletzt werden?",
                  "Wie wird sich \"<ocl>self</ocl>\" verhalten, wenn die Nachbedingungen " +
                  "nicht erreicht werden?"
                },
                new String[] { "Location",
                  "Kann \"<ocl>self</ocl>\" in einer anderen Klasse definiert werden, die mit " +
                  "<ocl>self.owner</ocl> assoziiert ist?",
                  "Kann \"<ocl>self</ocl>\" die Vererbungshierarchie hochsteigen, um es f\u00fcr " +
                  "den owner.name und mit anderen Klassen zu verwenden?",
                  "Kann \"<ocl>self</ocl>\" auf alle Instanzen der Klasse " +
                  "<ocl>self.owner</ocl>, einschlie\u00dflich der Instanzen der Unterklassen " +
                  "angewendet werden?",
                  "Kann \"<ocl>self</ocl>\" aus dem Modell entfernt werden?",
                  "Gibt es eine andere Methode im Modell, die \u00fcberarbeitet oder entfernt " +
                  "werden sollte, weil sie dem gleichen Zweck wie \"<ocl>self</ocl>\" dient?"
                }
            }
        },
        { "ChAssociation",
            new String[][] {
                new String[] { "Naming",
                  "Gibt der Name \"<ocl>self</ocl>\" eine klare Beschreibung der Klasse?",
                  "Ist \"<ocl>self</ocl>\" ein Substantiv oder ein substantiv\u00e4hnlicher Ausdruck?",
                  "Kann der Name \"<ocl>self</ocl>\" falsch interpretiert werden und etwas " +
                  "anderes bedeuten?"
                },
                new String[] { "Encoding",
                  "Sollte \"<ocl>self</ocl>\" eine eigene Klasse bilden, oder nur ein " +
                  "einfaches Attribut einer anderen Klasse sein?",
                  "Tut \"<ocl>self</ocl>\" exakt eine einzige Sache und tut sie es richtig?",
                  "Kann \"<ocl>self</ocl>\" in zwei oder mehrere Klassen heruntergebrochen werden?"
                },
                new String[] { "Value",
                  "Starten alle Attribute von \"<ocl>self</ocl>\" mit sinnvollen Werten?",
                  "K\u00f6nnen Sie eine Invariante f\u00fcr diese Klasse schreiben?",
                  "Etablieren alle Konstruktoren die Klassen-Invariante?",
                  "Behandeln alle Methoden die Klassen-Invariante?"
                },
                new String[] { "Location",
                  "Kann \"<ocl>self</ocl>\" an einer anderen Stelle in der Klassenhierarchie " +
                  "definiert werden?",
                  "Haben Sie eine Unterklasse von \"<ocl>self</ocl>\" geplant?",
                  "Kann \"<ocl>self</ocl>\" aus dem Modell entfernt werden?",                  "Gibt es im Modell eine andere Klasse, die \u00fcberarbeitet oder entfernt " +
                  "werden sollte, weil sie dem gleichen Zweck dient wie \"<ocl>self</ocl>\"?"
                },
                new String[] { "Updates",
                  "In welchen Fall wird eine Instanz von \"<ocl>self</ocl>\" aufgefrischt?",
                  "Gibt es andere Objekte, die immer aufgefrischt werden m\u00fcssen, wenn " +
                  "\"<ocl>self</ocl>\" aufgefrischt wurde?"
                }
            }
        },
        { "ChInterface",
            new String[][] {
                new String[] { "Naming",
                  "Gibt der Name \"<ocl>self</ocl>\" eine klare Beschreibung der Klasse?",
                  "Ist \"<ocl>self</ocl>\" ein Substantiv oder ein substantiv\u00e4hnlicher Ausdruck?",
                  "Kann der Name \"<ocl>self</ocl>\" falsch interpretiert werden und etwas " +
                  "anderes bedeuten?"
                },
                new String[] { "Encoding",
                  "Sollte \"<ocl>self</ocl>\" eine eigene Klasse bilden, oder nur ein " +
                  "einfaches Attribut einer anderen Klasse sein?",
                  "Tut \"<ocl>self</ocl>\" exakt eine einzige Sache und tut sie es richtig?",
                  "Kann \"<ocl>self</ocl>\" in zwei oder mehrere Klassen heruntergebrochen werden?"
                },
                new String[] { "Value",
                  "Starten alle Attribute von \"<ocl>self</ocl>\" mit sinnvollen Werten?",
                  "K\u00f6nnen Sie eine Invariante f\u00fcr diese Klasse schreiben?",
                  "Etablieren alle Konstruktoren die Klassen-Invariante?",
                  "Behandeln alle Methoden die Klassen-Invariante?"
                },
                new String[] { "Location",
                  "Kann \"<ocl>self</ocl>\" an einer anderen Stelle in der Klassenhierarchie " +
                  "definiert werden?",
                  "Haben Sie eine Unterklasse von \"<ocl>self</ocl>\" geplant?",
                  "Kann \"<ocl>self</ocl>\" aus dem Modell entfernt werden?",
                  "Gibt es im Modell eine andere Klasse, die \u00fcberarbeitet oder entfernt " +
                  "werden sollte, weil sie dem gleichen Zweck dient wie \"<ocl>self</ocl>\"?"
                },
                new String[] { "Updates",
                  "In welchen Fall wird eine Instanz von \"<ocl>self</ocl>\" aufgefrischt?",
                  "Gibt es andere Objekte, die immer aufgefrischt werden m\u00fcssen, wenn " +
                  "\"<ocl>self</ocl>\" aufgefrischt wurde?"
                }
            }
        },
        { "ChInstance",
            new String[][] {
                new String[] { "General",
                  "Ist diese Instanz \"<ocl>self</ocl>\" klar beschrieben?"
                },
                new String[] { "Naming",
                  "Gibt der Name \"<ocl>self</ocl>\" eine klare Beschreibung der Instanz?",
                  "Beschreibt \"<ocl>self</ocl>\" mehr einen Zustand als eine Aktivit\u00e4t?",
                  "Kann der Name \"<ocl>self</ocl>\" falsch interpretiert werden und etwas " +
                  "anderes bedeuten?"
                },
                new String[] { "Structure",
                  "Sollte \"<ocl>self</ocl>\" seinen eigenen Zustand darstellen, oder kann " +
                  "er mit einem anderen Zustand gemischt werden?",
                  "Tut \"<ocl>self</ocl>\" exakt eine einzige Sache und tut sie es richtig?",
                  "Kann \"<ocl>self</ocl>\" in zwei oder mehrere Zust\u00e4nde unterteilt werden?",
                  "K\u00f6nnen Sie eine charakteristische Gleichung f\u00fcr \"<ocl>self</ocl>\" schreiben?",
                  "Geh\u00f6rt \"<ocl>self</ocl>\" zu diesem oder zu einem anderen Zustandsautomaten?",
                  "Sollte \"<ocl>self</ocl>\" ein Anfangszustand sein?",
                  "Geh\u00f6ren einige Zust\u00e4nde im anderen Zustandsautomaten exklusiv zu " +
                  "\"<ocl>self</ocl>\"?"
                },
                new String[] { "Actions",
                  "Welche Aktion soll beim Eintritt in \"<ocl>self</ocl>\" ausgef\u00fchrt werden?",
                  "Sollten einige Attribute beim Eintritt in \"<ocl>self</ocl>\" aktualisiert werden?",
                  "Welche Aktion sollte beim Verlassen von \"<ocl>self</ocl>\" ausgef\u00fchrt werden?",
                  "Sollten einige Attribute beim Verlassen von \"<ocl>self</ocl>\" " +
                  "aktualisiert werden?",
                  "Welche Aktionen sollen innerhalb von \"<ocl>self</ocl>\" ausgef\u00fchrt werden?",
                  "Verarbeiten Zustandsaktionen \"<ocl>self</ocl>\" als aktuellen Zustand?"
                },
                new String[] { "Transitions",
                  "Sollte es einen anderen Zustands\u00fcbergang zu \"<ocl>self</ocl>\" geben?",
                  "K\u00f6nnen alle Zustands\u00fcberg\u00e4nge in \"<ocl>self</ocl>\" verwendet werden?",
                  "K\u00f6nnen einige kommende Zustands\u00fcberg\u00e4nge kombiniert werden?",
                  "Sollte es einen anderen Zustands\u00fcbergang gehend von \"<ocl>self</ocl>\" geben?",
                  "K\u00f6nnen alle gehenden Zustands\u00fcberg\u00e4nge von \"<ocl>self</ocl>\" verwendet werden?",
                  "Ist jeder gehende Zustands\u00fcbergang exklusiv?",
                  "K\u00f6nnen einige gehende Zustands\u00fcberg\u00e4nge kombiniert werden?"
                }
            }
        },
        { "ChLink",
            new String[][] {
                new String[] { "Naming",
                  "Gibt der Name \"<ocl>self</ocl>\" eine klare Beschreibung der Klasse?",
                  "Ist \"<ocl>self</ocl>\" ein Substantiv oder ein substantiv\u00e4hnlicher Ausdruck?",
                  "Kann der Name \"<ocl>self</ocl>\" falsch interpretiert werden und etwas " +
                  "anderes bedeuten?"
                },
                new String[] { "Encoding",
                  "Sollte \"<ocl>self</ocl>\" eine eigene Klasse bilden, oder nur ein " +
                  "einfaches Attribut einer anderen Klasse sein?",
                  "Tut \"<ocl>self</ocl>\" exakt eine einzige Sache und tut sie es richtig?",
                  "Kann \"<ocl>self</ocl>\" in zwei oder mehrere Klassen heruntergebrochen werden?"
                },
                new String[] { "Value",
                  "Starten alle Attribute von \"<ocl>self</ocl>\" mit sinnvollen Werten?",
                  "K\u00f6nnen Sie eine Invariante f\u00fcr diese Klasse schreiben?",
                  "Etablieren alle Konstruktoren die Klassen-Invariante?",
                  "Behandeln alle Methoden die Klassen-Invariante?"
                },
                new String[] { "Location",
                  "Kann \"<ocl>self</ocl>\" an einer anderen Stelle in der Klassenhierarchie " +
                  "definiert werden?",
                  "Haben Sie eine Subklasse von \"<ocl>self</ocl>\" geplant?",
                  "Kann \"<ocl>self</ocl>\" aus dem Modell entfernt werden?",                  "Gibt es im Modell eine andere Klasse, die \u00fcberarbeitet oder entfernt " +
                  "werden sollte, weil sie dem gleichen Zweck dient wie \"<ocl>self</ocl>\"?"
                }
            }
        },
        { "ChState",
            new String[][] {
                new String[] { "Naming",
                  "Gibt der Name \"<ocl>self</ocl>\" eine klare Beschreibung des Zustandes?",
                  "Beschreibt \"<ocl>self</ocl>\" mehr einen Zustand als eine Aktivit\u00e4t?",
                  "Kann der Name \"<ocl>self</ocl>\" falsch interpretiert werden und etwas " +
                  "anderes bedeuten?"
                },
                new String[] { "Structure",
                  "Sollte \"<ocl>self</ocl>\" seinen eigenen Zustand darstellen, oder kann " +
                  "er mit einem anderen Zustand gemischt werden?",
                  "Tut \"<ocl>self</ocl>\" exakt eine einzige Sache und tut sie es richtig?",
                  "Kann \"<ocl>self</ocl>\" in zwei oder mehrere Zust\u00e4nde unterteilt werden?",
                  "K\u00f6nnen Sie eine charakteristische Gleichung f\u00fcr \"<ocl>self</ocl>\" schreiben?",
                  "Geh\u00f6rt \"<ocl>self</ocl>\" zu diesem oder zu einem anderen Zustandsautomaten?",
                  "Sollte \"<ocl>self</ocl>\" ein Anfangszustand sein?",
                  "Geh\u00f6ren einige Zust\u00e4nde im anderen Zustandsautomaten exklusiv zu " +
                  "\"<ocl>self</ocl>\"?"
                },
                new String[] { "Actions",
                  "Welche Aktion soll beim Eintritt in \"<ocl>self</ocl>\" ausgef\u00fchrt werden?",
                  "Sollten einige Attribute beim Eintritt in \"<ocl>self</ocl>\" aktualisiert werden?",
                  "Welche Aktion sollte beim Verlassen von \"<ocl>self</ocl>\" ausgef\u00fchrt werden?",
                  "Sollten einige Attribute beim Verlassen von \"<ocl>self</ocl>\" " +
                  "aktualisiert werden?",
                  "Welche Aktionen sollen innerhalb von \"<ocl>self</ocl>\" ausgef\u00fchrt werden?",
                  "Verarbeiten Zustandsaktionen \"<ocl>self</ocl>\" als aktuellen Zustand?"
                },
                new String[] { "Transitions",
                  "Sollte es einen anderen Zustands\u00fcbergang zu \"<ocl>self</ocl>\" geben?",
                  "K\u00f6nnen alle Zustands\u00fcberg\u00e4nge in \"<ocl>self</ocl>\" verwendet werden?",
                  "K\u00f6nnen einige kommende Zustands\u00fcberg\u00e4nge kombiniert werden?",
                  "Sollte es einen anderen Zustands\u00fcbergang gehend von \"<ocl>self</ocl>\" geben?",
                  "K\u00f6nnen alle gehenden Zustands\u00fcberg\u00e4nge von \"<ocl>self</ocl>\" verwendet werden?",
                  "Ist jeder gehende Zustands\u00fcbergang exklusiv?",
                  "K\u00f6nnen einige gehende Zustands\u00fcberg\u00e4nge kombiniert werden?"
                }
            }
        },
        { "ChTransition",
            new String[][] {
                new String[] { "Structure",
                  "Sollte dieser Zustands\u00fcbergang eine andere Quelle starten?",
                  "Sollte dieser Zustands\u00fcbergang an einem anderen Ziel enden?",
                  "Sollte es einen anderen Zustands\u00fcbergang \"wie\" diesen geben?",
                  "Ist ein anderer Zustands\u00fcbergang wegen diesem hier unn\u00f6tig?"
                },
                new String[] { "Trigger",
                  "Ben\u00f6tigt dieser Zustands\u00fcbergang ein Signal?",
                  "Werden zu oft Signale ausgel\u00f6st?",
                  "Werden zu selten Signale ausgel\u00f6st?"
                },
                new String[] { "MGuard",
                  "K\u00f6nnte dieser Zustands\u00fcbergang zu oft verwendet werden?",
                  "Ist die Bedingung dieses Zustands\u00fcberganges zu streng?",
                  "Kann er in zwei oder mehrere Zustands\u00fcberg\u00e4nge aufgeteilt werden?"
                },
                new String[] { "Actions",
                  "Sollte dieser Zustands\u00fcbergang eine Aktion haben?",
                  "Sollte diese Zustands\u00fcbergangs-Aktion eine Verlassen-Aktion sein?",
                  "Sollte diese Zustands\u00fcbergangs-Aktion eine Eintritt-Aktion sein?",
                  "Tritt die Vorbedingung der Aktion immer ein?",
                  "Ist die Nachbedingung der Aktion mit dem Ziel konsistent?"
                }
            }
        },
        { "ChUseCase",
            new String[][] {
                new String[] { "Naming",
                  "Gibt der Name \"<ocl>self</ocl>\" eine klare Beschreibung der Klasse?",
                  "Ist \"<ocl>self</ocl>\" ein Substantiv oder ein substantiv\u00e4hnlicher Ausdruck?",
                  "Kann der Name \"<ocl>self</ocl>\" falsch interpretiert werden und etwas " +
                  "anderes bedeuten?"
                },
                new String[] { "Encoding",
                  "Sollte \"<ocl>self</ocl>\" eine eigene Klasse bilden, oder nur ein " +
                  "einfaches Attribut einer anderen Klasse sein?",
                  "Tut \"<ocl>self</ocl>\" exakt eine einzige Sache und tut sie es richtig?",
                  "Kann \"<ocl>self</ocl>\" in zwei oder mehrere Klassen heruntergebrochen werden?"
                },
                new String[] { "Value",
                  "Starten alle Attribute von \"<ocl>self</ocl>\" mit sinnvollen Werten?",
                  "K\u00f6nnen Sie eine Invariante f\u00fcr diese Klasse schreiben?",
                  "Etablieren alle Konstruktoren die Klassen-Invariante?",
                  "Behandeln alle Methoden die Klassen-Invariante?"
                },
                new String[] { "Location",
                  "Kann \"<ocl>self</ocl>\" an einer anderen Stelle in der Klassenhierarchie " +
                  "definiert werden?",
                  "Haben Sie eine Unterklasse von \"<ocl>self</ocl>\" geplant?",
                  "Kann \"<ocl>self</ocl>\" aus dem Modell entfernt werden?",                  "Gibt es im Modell eine andere Klasse, die \u00fcberarbeitet oder entfernt " +
                  "werden sollte, weil sie dem gleichen Zweck dient wie \"<ocl>self</ocl>\"?"
                },
                new String[] { "Updates",
                  "In welchen Fall wird eine Instanz von \"<ocl>self</ocl>\" aufgefrischt?",
                  "Gibt es andere Objekte, die immer aufgefrischt werden m\u00fcssen, wenn " +
                  "\"<ocl>self</ocl>\" aufgefrischt wurde?"
                }
            }
        },
        { "ChActor",
            new String[][] {
                new String[] { "Naming",
                  "Gibt der Name \"<ocl>self</ocl>\" eine klare Beschreibung der Klasse?",
                  "Ist \"<ocl>self</ocl>\" ein Substantiv oder ein substantiv\u00e4hnlicher Ausdruck?",
                  "Kann der Name \"<ocl>self</ocl>\" falsch interpretiert werden und etwas " +
                  "anderes bedeuten?"
                },
                new String[] { "Encoding",
                  "Sollte \"<ocl>self</ocl>\" eine eigene Klasse bilden, oder nur ein " +
                  "einfaches Attribut einer anderen Klasse sein?",
                  "Tut \"<ocl>self</ocl>\" exakt eine einzige Sache und tut sie es richtig?",
                  "Kann \"<ocl>self</ocl>\" in zwei oder mehrere Klassen heruntergebrochen werden?"
                },
                new String[] { "Value",
                  "Starten alle Attribute von \"<ocl>self</ocl>\" mit sinnvollen Werten?",
                  "K\u00f6nnen Sie eine Invariante f\u00fcr diese Klasse schreiben?",
                  "Etablieren alle Konstruktoren die Klassen-Invariante?",
                  "Behandeln alle Methoden die Klassen-Invariante?"
                },
                new String[] { "Location",
                  "Kann \"<ocl>self</ocl>\" an einer anderen Stelle in der Klassenhierarchie " +
                  "definiert werden?",
                  "Haben Sie eine Unterklasse von \"<ocl>self</ocl>\" geplant?",
                  "Kann \"<ocl>self</ocl>\" aus dem Modell entfernt werden?",                  "Gibt es im Modell eine andere Klasse, die \u00fcberarbeitet oder entfernt " +
                  "werden sollte, weil sie dem gleichen Zweck dient wie \"<ocl>self</ocl>\"?"
                },
                new String[] { "Updates",
                  "In welchen Fall wird eine Instanz von \"<ocl>self</ocl>\" aufgefrischt?",
                  "Gibt es andere Objekte, die immer aufgefrischt werden m\u00fcssen, wenn " +
                  "\"<ocl>self</ocl>\" aufgefrischt wurde?"
                }
            }
        }
    };

    public Object[][] getContents() {
        return _contents;
    }
}
