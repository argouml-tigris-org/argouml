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

/**
 *   This class is the default member of a resource bundle that
 *   provides strings for UML related critiques and check lists
 *
 *   @author Curt Arnold
 *   @since 0.9
 *   @see java.util.ResourceBundle
 *   @see org.argouml.uml.cognitive.UMLCognitiveResourceBundle_de
 *   @see org.argouml.util.CheckResourceBundle
 *   @see org.argouml.uml.cognitive.critics.CrUML
 */
public class UMLCognitiveResourceBundle_fr extends ListResourceBundle {

    private static final Object[][] _contents = {
	// General stuff
        {"decision.behavior", "Comportement"},
        {"decision.class-selection", "S\u00e9lection de classe"},
        {"decision.code-generation", "G\u00e9n\u00e9ration du code"},
        {"decision.containment", "Conteneur"},
        {"decision.design-patterns", "Mod\u00e8les de conception"},
        {"decision.expected-usage", "Emploi attendu"},
        {"decision.inheritance", "H\u00e9ritage"},
        {"decision.instantiation", "Instanciation"},
        {"decision.methods", "M\u00e9thodes"},
        {"decision.modularity", "Modularit\u00e9"},
        {"decision.mstate-machines", "Automates d'\u00e9tats finis"},
        {"decision.naming", "Affectation de nom"},
        {"decision.planned-extensions", "Extensions pr\u00e9vues"},
        {"decision.relationships", "Relations"},
        {"decision.stereotypes", "St\u00e9r\u00e9otypes"},
        {"decision.storage", "Stockage"},
        {"decision.uncategorized", "Non class\u00e9es"},

        {"goal.unspecified", "ind\u00e9termin\u00e9"},

        {"knowledge.completeness", "Compl\u00e9tude"},
        {"knowledge.consistency", "Coh\u00e9rence"},
        {"knowledge.correctness", "Justesse"},
        {"knowledge.designers", "des concepteurs"},
        {"knowledge.experiential", "Exp\u00e9rimental"},
        {"knowledge.optimization", "Optimisation"},
        {"knowledge.organizational", "d'organisation"},
        {"knowledge.presentation", "Pr\u00e9sentation"},
        {"knowledge.semantics", "S\u00e9mantique"},
        {"knowledge.syntax", "Syntaxe"},
        {"knowledge.tool", "Outil"},

        {"todopane.label.no-items", " Pas d'\u00c9l\u00e9ments"},
        {"todopane.label.item", " {0} \u00c9l\u00e9ment "},
        {"todopane.label.items", " {0} \u00c9l\u00e9ments "},

        {"docpane.label.documentation", "Documentation"},
        {"docpane.label.author", "Auteur"},
        {"docpane.label.version", "Version"},
        {"docpane.label.since", "Depuis"},
        {"docpane.label.deprecated", "D\u00e9conseill\u00e9"},
        {"docpane.label.see", "Voir"},

        {"stylepane.label.bounds", "Limites"},
        {"stylepane.label.fill", "Remplissage"},
        {"stylepane.label.no-fill", "Pas de remplissage"},
        {"stylepane.label.line", "Trait"},
        {"stylepane.label.no-line", "Pas de trait"},
        {"stylepane.label.shadow", "Ombrage"},
        {"stylepane.label.no-shadow", "Pas d'ombrage"},
        {"stylepane.label.custom", "Personnaliser"},

        {"taggedvaluespane.label.tag", "Marqueur"},
        {"taggedvaluespane.label.value", "Valeur"},

        {"button.ok", "OK"},
        {"button.cancel", "Annuler"},
        {"button.open", "Ouvrir"},
        {"button.back", "Pr\u00e9c\u00e9dent"},
        {"button.next", "Suivant"},
        {"button.finish", "Fin"},
        {"button.help", "Aide"},

        {"mnemonic.button.back", "P"},
        {"mnemonic.button.next", "S"},
        {"mnemonic.button.finish", "F"},
        {"mnemonic.button.help", "A"},

        {"level.low", "Basse"},
        {"level.medium", "Moyenne"},
        {"level.high", "\u00c9lev\u00e9e"},

        {"message.no-item-selected",
                "Aucun \u00e9l\u00e9ment \u00e0 corriger s\u00e9lectionn\u00e9"},
        {"message.branch-critic",
                "Cette section contient des \u00e9l\u00e9ments \"\u00e0 corriger\" g\u00e9n\u00e9r\u00e9s par la critique:\n{0}."},
        {"message.branch-decision",
                "Cette section contient des \u00e9l\u00e9ments \"\u00e0 corriger\" relatifs \u00e0 la d\u00e9cision:\n{0}."},
        {"message.branch-goal",
                "Cette section contient des \u00e9l\u00e9ments \"\u00e0 corriger\" relatifs \u00e0 l objectif:\n{0}."},
        {"message.branch-knowledge",
                "Cette section contient des \u00e9l\u00e9ments \"\u00e0 corriger\" relatifs aux connaissances de type {0}."},
        {"message.branch-model",
                "Cette section contient des \u00e9l\u00e9ments \"\u00e0 corriger\" relatifs \u00e0 l \u00e9l\u00e9ment du mod\u00e8le:\n{0}."},
        {"message.branch-priority",
                "Cette section contient des \u00e9l\u00e9ments \"\u00e0 corriger\" de priorit\u00e9 {0}."},

        { "CrAssocNameConflict_head" ,
                "Supprimez les conflits de noms d'associations" },
        { "CrAssocNameConflict_desc" ,
                "Chacun des \u00e9l\u00e9ments d'un espace de nommage doit avoir un nom unique.\n\nUne d\u00e9nomination claire et sans ambigu\u00eft\u00e9 est indispensable \u00e0 la g\u00e9n\u00e9ration de code et \u00e0 la maintenabilit\u00e9 de la conception et facilite sa compr\u00e9hension.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement les \u00e9l\u00e9ments et changez leurs noms \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s." },
        { "CrAttrNameConflict_head" ,
                "Supprimez les conflits de noms d'attributs" },
        { "CrAttrNameConflict_desc" ,
                "Les attributs doivent avoir des noms distincts les uns des autres. Le probl\u00e8me peut provenir d'un attribut h\u00e9rit\u00e9.\n\nUne d\u00e9nomination claire et sans ambigu\u00eft\u00e9 est indispensable \u00e0 la g\u00e9n\u00e9ration de code et \u00e0 la maintenabilit\u00e9 de la conception et facilite sa compr\u00e9hension.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement l'un des attributs en conflit de la classe et changez son nom" },
        { "CrOperNameConflict_head" ,
                "Changez les noms ou les signatures dans <ocl>self</ocl>" },
        { "CrOperNameConflict_desc" ,
                "Deux op\u00e9rations ont exactement la m\u00eame signature. Les op\u00e9rations doivent avoir des signatures distinctes les unes des autres. Une signature est la combinaison du nom de l'op\u00e9ration et des types de ses param\u00e8tres.\n\n\u00c9viter les conflits de signatures est indispensable \u00e0 la g\u00e9n\u00e9ration de code et \u00e0 la maintenabilit\u00e9 de la conception et facilite sa compr\u00e9hension.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement l'une des op\u00e9rations en conflit et changez son nom ou ses param\u00e8tres." },
        { "CrCircularAssocClass_head" ,
                "Supprimez l'association circulaire" },
        { "CrCircularAssocClass_desc" ,
                "Les classes-associations ne peuvent comporter des r\u00f4les qui les r\u00e9f\u00e9rencent directement." },
        { "CrCircularInheritance_head" ,
                "Supprimez le cycle d'h\u00e9ritage de <ocl>self</ocl>" },
        { "CrCircularInheritance_desc" ,
                "Les relations d'h\u00e9ritage ne peuvent pas comporter de cycle.\n\nUne hi\u00e9rarchie d'h\u00e9ritage correcte est n\u00e9cessaire \u00e0 la g\u00e9n\u00e9ration de code et pour la justesse de la conception.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb o\u00f9 s\u00e9lectionnez manuellement l'un des liens de g\u00e9n\u00e9ralisation et supprimez-le." },
        { "CrCircularComposition_head" ,
                "Supprimez le cycle de composition" },
        { "CrCircularComposition_desc" ,
                "Les relations de composition (losanges noirs) ne peuvent pas comporter de cycle.\n\nUne hi\u00e9rarchie d'agr\u00e9gation correcte est n\u00e9cessaire \u00e0 la g\u00e9n\u00e9ration de code et pour la justesse de la conception.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb o\u00f9 s\u00e9lectionnez manuellement l'une des associations et supprimez-la ou changez sa cat\u00e9gorie en autre chose que composite." },
        { "CrCrossNamespaceAssoc_head" ,
                "Agr\u00e9gation de r\u00f4les dans une association n-aire" },
        { "CrCrossNamespaceAssoc_desc" ,
                "Toutes les classes et interfaces impliqu\u00e9es dans une association doivent appartenir au m\u00eame espace de nommage que l'association." },
        { "CrDupParamName_head" ,
                "Supprimez les conflits de noms de param\u00e8tres" },
        { "CrDupParamName_desc" ,
                "Les param\u00e8tres d'une op\u00e9ration doivent avoir des noms distincts les uns des autres.\n\nUne d\u00e9nomination claire et sans ambigu\u00eft\u00e9 est indispensable \u00e0 la g\u00e9n\u00e9ration de code et \u00e0 la maintenabilit\u00e9 de la conception et facilite sa compr\u00e9hension.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement l'un des param\u00e8tres en conflit de la l'op\u00e9ration et changez son nom" },
        { "CrDupRoleNames_head" ,
                "Changez les noms de r\u00f4les de <ocl>self</ocl>" },
        { "CrDupRoleNames_desc" ,
                "L'association <ocl>self</ocl> a deux r\u00f4les aux noms conflictuels.\n\nUne d\u00e9nomination claire et sans ambigu\u00eft\u00e9 est indispensable \u00e0 la g\u00e9n\u00e9ration de code et \u00e0 la maintenabilit\u00e9 de la conception et facilite sa compr\u00e9hension.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement <ocl>self</ocl> et changez le nom de l'un des r\u00f4les." },
        { "CrFinalSubclassed_head" ,
                "Supprimez le mot-clef \u00ab final \u00bb ou supprimez les sous-classes" },
        { "CrFinalSubclassed_desc" ,
                "Dans le langage Java, le mot-clef \u00ab final \u00bb sp\u00e9cifie que la classe ne doit pas avoir de sous-classe. Cette classe est \u00e9tiquet\u00e9e avec ce mot-clef et elle a des sous-classes.\n\nUne hi\u00e9rarchie d'h\u00e9ritage bien pens\u00e9e qui supporte et indique clairement des extensions pr\u00e9visibles est une \u00e9tape importante vers une conception maintenable et compr\u00e9hensible.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement la classe et changez sa classe de base ou s\u00e9lectionnez la classe de base et supprimez le mot-clef \u00ab final \u00bb \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s." },
        { "CrIllegalGeneralization_head" ,
                "Supprimez la g\u00e9n\u00e9ralisation non valide" },
        { "CrIllegalGeneralization_desc" ,
                "L'h\u00e9ritage n'est possible qu'entre \u00e9l\u00e9ments de mod\u00e8le de m\u00eame type.\n\nUne hi\u00e9rarchie d'h\u00e9ritage correcte est n\u00e9cessaire \u00e0 la g\u00e9n\u00e9ration de code et pour la justesse de la conception.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb o\u00f9 s\u00e9lectionnez manuellement le lien de g\u00e9n\u00e9ralisation et supprimez-le." },
        { "CrAlreadyRealizes_head" ,
                "Supprimez les r\u00e9alisations inutiles de <ocl>self</ocl>" },
        { "CrAlreadyRealizes_desc" ,
                "La classe s\u00e9lectionn\u00e9e r\u00e9alise d\u00e9j\u00e0 la sp\u00e9cification {item.extra}. Il est inutile de la r\u00e9aliser directement en plus.\n\nIl est toujours bon de simplifier la conception. Vous pouvez ignorer cette entr\u00e9e \u00ab \u00c0 Faire \u00bb si vous pr\u00e9f\u00e9rez faire appara\u00eetre explicitement que la classe s\u00e9lectionn\u00e9e r\u00e9alise la sp\u00e9cification.\n\nPour corriger, s\u00e9lectionnez le lien de r\u00e9alisation (ligne pointill\u00e9e termin\u00e9e par un triangle blanc) et supprimez-le avec la touche \u00ab Suppr \u00bb." },
        { "CrInterfaceAllPublic_head" ,
                "Rendez publiques les op\u00e9rations des interfaces" },
        { "CrInterfaceAllPublic_desc" ,
                "Les interfaces ont pour objectif de sp\u00e9cifier des ensembles d'op\u00e9rations que d'autres classes impl\u00e9mentent. Ces op\u00e9rations doivent \u00eatre publiques.\n\nUn ensemble d'interfaces bien con\u00e7u constitue un bon moyen de d\u00e9finir les extensions possible d'une architecture.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement les op\u00e9rations de l'interface et rendez-les publiques \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s." },
        { "CrInterfaceOperOnly_head" ,
                "Supprimez les attributs de l'interface" },
        { "CrInterfaceOperOnly_desc" ,
                "Les interfaces ont pour objectif de sp\u00e9cifier des ensembles d'op\u00e9rations que d'autres classes impl\u00e9mentent. Elles n'impl\u00e9mentent pas ces op\u00e9rations par elles-m\u00eames et ne peuvent pas avoir d'attributs.\n\nUn ensemble d'interfaces bien con\u00e7u constitue un bon moyen de d\u00e9finir les extensions possible d'une architecture.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement l'interface et supprimez tous ses attributs \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s." },
        { "CrMultipleAgg_head" ,
                "Supprimez l'agr\u00e9gation surnum\u00e9raire de l'un des r\u00f4les" },
        { "CrMultipleAgg_desc" ,
                "Seul l'un des r\u00f4les d'une association peut \u00eatre une agr\u00e9gation ou une composition.\n\nUne hi\u00e9rarchie d'appartenance claire et coh\u00e9rente est n\u00e9cessaire \u00e0 la clart\u00e9 de la conception, \u00e0 la facilit\u00e9 de gestion du stockage des objets et \u00e0 l'impl\u00e9mentation des m\u00e9thodes r\u00e9cursives.\nPour corriger, s\u00e9lectionnez l'association et changez l'agr\u00e9gation d'un de ses r\u00f4les en \u00ab Aucun \u00bb" },
        { "CrNWayAgg_head" ,
                "Supprimez l'agr\u00e9gation des r\u00f4les d'une association n-aire" },
        { "CrNWayAgg_desc" ,
                "Les association ternaires (ou plus) ne peuvent pas \u00eatre des agr\u00e9gations.\n\nUne hi\u00e9rarchie d'appartenance claire et coh\u00e9rente est n\u00e9cessaire \u00e0 la clart\u00e9 de la conception, \u00e0 la facilit\u00e9 de gestion du stockage des objets et \u00e0 l'impl\u00e9mentation des m\u00e9thodes r\u00e9cursives.\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez l'association manuellement et changez toutes les agr\u00e9gations de ses r\u00f4les en \u00ab Aucun \u00bb." },
        { "CrNavFromInterface_head" ,
                "Supprimez la navigation en provenance de l'interface <ocl>self</ocl>" },
        { "CrNavFromInterface_desc" ,
                "Les associations impliquant une interface ne peuvent pas \u00eatre navigables depuis l'interface. Ceci est d\u00fb au fait que les interfaces ne contiennent que des d\u00e9clarations d'op\u00e9rations et ne peuvent pas contenir de pointeurs vers d'autres objets.\n\nCette partie de la conception doit \u00eatre chang\u00e9e avant de pouvoir \u00eatre utilis\u00e9e pour g\u00e9n\u00e9rer du code. Si vous tentez de g\u00e9n\u00e9rer du code avant de r\u00e9soudre ce probl\u00e8me, le code ne sera pas coh\u00e9rent avec la conception.\n\nPour corriger, s\u00e9lectionnez l'association et d\u00e9cochez la case de navigabilit\u00e9 du c\u00f4t\u00e9 de l'interface dans l'onglet des propri\u00e9t\u00e9s. Une fl\u00e8che doit alors appara\u00eetre sur l'extr\u00e9mit\u00e9 de l'association vers l'interface." },
        { "CrUnnavigableAssoc_head" ,
                "Rendez l'association <ocl>self</ocl> navigable" },
        { "CrUnnavigableAssoc_desc" ,
          "L'association <ocl>self</ocl> n'est navigable dans aucune direction. Toutes les associations doivent \u00eatre navigables au moins dans un sens.\n\nParam\u00e9trer la navigabilit\u00e9 des associations permet \u00e0 votre code d'acc\u00e9der aux donn\u00e9es en suivant des pointeurs.\n\nPour corriger, s\u00e9lectionnez l'association \u00ab <ocl>self</ocl> \u00bb et changez la navigabilit\u00e9 \u00e0 l'aide des cases \u00e0 cocher de l'onglet des propri\u00e9t\u00e9s." },
        { "CrNameConflictAC_head" ,
                "Supprimez le conflit de nom entre un r\u00f4le et un membre" },
        { "CrNameConflictAC_desc" ,
                "Les noms de r\u00f4le des associations d'une classe-association ne doivent pas entrer en conflit avec les \u00e9l\u00e9ments structuraux (par exemple les variables d'instance) de la classe." },
        { "CrMissingClassName_head" ,
                "Choisissez un nom" },
        { "CrMissingClassName_desc" ,
                "Toutes les classes et toutes les interfaces d'un paquetage doivent avoir un nom.\n\nUne d\u00e9nomination claire et sans ambigu\u00eft\u00e9 est indispensable \u00e0 la g\u00e9n\u00e9ration de code et \u00e0 la maintenabilit\u00e9 de la conception et facilite sa compr\u00e9hension.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement la classe et donnez-lui un nom \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s." },
        { "CrMissingAttrName_head" ,
                "Choisissez un nom" },
        { "CrMissingAttrName_desc" ,
                "Tous les attributs doivent avoir un nom.\n\nUne d\u00e9nomination claire et sans ambigu\u00eft\u00e9 est indispensable \u00e0 la g\u00e9n\u00e9ration de code et \u00e0 la maintenabilit\u00e9 de la conception et facilite sa compr\u00e9hension.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement l'attribut et donnez-lui un nom \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s." },
        { "CrMissingOperName_head" ,
                "Choisissez un nom" },
        { "CrMissingOperName_desc" ,
                "Toutes les op\u00e9rations doivent avoir un nom.\n\nUne d\u00e9nomination claire et sans ambigu\u00eft\u00e9 est indispensable \u00e0 la g\u00e9n\u00e9ration de code et \u00e0 la maintenabilit\u00e9 de la conception et facilite sa compr\u00e9hension.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement l'op\u00e9ration et donnez-lui un nom \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s." },
        { "CrMissingStateName_head" ,
                "Choisissez un nom" },
        { "CrMissingStateName_desc" ,
                "Tous les \u00e9tats d'un automate \u00e0 \u00e9tats doivent avoir un nom.\n\nUne d\u00e9nomination claire et sans ambigu\u00eft\u00e9 est indispensable \u00e0 la g\u00e9n\u00e9ration de code et \u00e0 la maintenabilit\u00e9 de la conception et facilite sa compr\u00e9hension.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement l'\u00e9tat et donnez-lui un nom \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s." },
        { "CrNoInstanceVariables_head" ,
                "Ajoutez des variables d'instance \u00e0 <ocl>self</ocl>" },
        { "CrNoInstanceVariables_desc" ,
                "Vous n'avez pas encore sp\u00e9cifi\u00e9 de variable d'instance pour <ocl>self</ocl>. Normalement, les classes ont des variables d'instance qui stockent les informations d'\u00e9tat des instances. Les classes qui n'ont que des attributs et des m\u00e9thodes statiques doivent \u00eatre qualifi\u00e9es avec le st\u00e9r\u00e9otype \u00ab utility \u00bb.\n\nVous devez d\u00e9finir des variables d'instance pour compl\u00e9ter la partie \u00ab repr\u00e9sentation de l'information \u00bb de votre conception.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou ajoutez des variables d'instance en double-cliquant sur <ocl>self</ocl> dans l'explorateur et en utilisant le menu \u00ab Cr\u00e9er \u00bb pour construire un nouvel attribut." },
        { "CrNoAssociations_head" ,
                "Ajoutez des associations \u00e0 <ocl>self</ocl>" },
        { "CrNoAssociations_desc" ,
                "Vous n'avez pas encore sp\u00e9cifi\u00e9 d'association pour <ocl>self</ocl>. Normalement, les classes, les acteurs et les cas d'utilisation sont reli\u00e9s par des associations.\n\nLa d\u00e9finition d'associations entre objets est une partie importante de votre conception\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou ajoutez des associations manuellement en cliquant sur l'outil d'association dans la barre d'outils puis en faisant un glisser-d\u00e9poser entre <ocl>self</ocl> et un autre n\u0153ud." },
        { "CrNonAggDataType_head" ,
                "Encapsulez le type de donn\u00e9e" },
        { "CrNonAggDataType_desc" ,
                "Les types de donn\u00e9e (DataTypes) ne sont pas des classes compl\u00e8tes et ne peuvent pas \u00eatre associ\u00e9s avec des classes, sauf par des agr\u00e9gations de type composition (losanges noirs).\n\nUne bonne conception orient\u00e9e objet d\u00e9pend du soin apport\u00e9 au choix des entit\u00e9s qui doivent \u00eatre des objets complets et des entit\u00e9s qui doivent \u00eatre des attributs d'objets.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb, remplacez manuellement le type de donn\u00e9e par une classe compl\u00e8te, ou changez l'association en appartenance \u00e0 une classe compl\u00e8te." },
        { "CrOppEndConflict_head" ,
                "Renommez les noms de r\u00f4les de l'association" },
        { "CrOppEndConflict_desc" ,
                "Deux r\u00f4les de <ocl>self</ocl> ont le m\u00eame nom. Les r\u00f4les doivent avoir des noms distincts.\n\nUne d\u00e9nomination claire et sans ambigu\u00eft\u00e9 est indispensable \u00e0 la g\u00e9n\u00e9ration de code et \u00e0 la maintenabilit\u00e9 de la conception et facilite sa compr\u00e9hension.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement l'un des r\u00f4les et changez son nom \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s." },
        { "CrParamTypeNotImported_head" ,
                "Importez le type de param\u00e8tre dans la classe" },
        { "CrParamTypeNotImported_desc" ,
          "Le type de chacun des param\u00e8tres d'une op\u00e9ration doit \u00eatre visible et import\u00e9 dans la classe qui contient l'op\u00e9ration.\n\nImporter les classes est n\u00e9cessaire \u00e0 la g\u00e9n\u00e9ration de code. Une bonne modularisation des classes en paquetages est n\u00e9cessaire \u00e0 une conception compr\u00e9hensibles.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb, ou ajoutez manuellement une directive d'importation dans la classe qui contient l'op\u00e9ration." },
        { "CrUselessAbstract_head" ,
                "D\u00e9finissez des sous-classes concr\u00e8tes" },
        { "CrUselessAbstract_desc" ,
                "La classe <ocl>self</ocl> n'a aucun effet sur le syst\u00e8me en cours d'ex\u00e9cution car ni elle ni ses sous-classes ne peuvent \u00eatre instanci\u00e9es.\n\nPour corriger, d\u00e9finissez des sous-classes concr\u00e8tes qui impl\u00e9mentent l'interface de cette classe, ou rendez <ocl>self</ocl> ou une de ses sous-classes concr\u00e8te." },
        { "CrUselessInterface_head" ,
                "D\u00e9finissez des classes impl\u00e9mentant <ocl>self</ocl>" },
        { "CrUselessInterface_desc" ,
                "<ocl>self</ocl> ne peut pas \u00eatre utilis\u00e9e car aucune classe ne l'impl\u00e9mente.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou utilisez le bouton \u00ab Class \u00bb de la barre d'outils pour d\u00e9finir des classes et le bouton \u00ab Realizes \u00bb pour mettre en relation ces classes et l'interface concern\u00e9e." },
        { "CrDisambigClassName_head" ,
                "Choisissez un nom unique pour <ocl>self</ocl>" },
        { "CrDisambigClassName_desc" ,
                "Chaque classe et chaque interface d'un paquetage doit avoir un nom unique. Il y a au moins deux \u00e9l\u00e9ments de ce paquetage d\u00e9nomm\u00e9s \u00ab <ocl>self</ocl> \u00bb.\n\nUne d\u00e9nomination claire et sans ambigu\u00eft\u00e9 est indispensable \u00e0 la g\u00e9n\u00e9ration de code et \u00e0 la maintenabilit\u00e9 de la conception et facilite sa compr\u00e9hension.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement les \u00e9l\u00e9ments et changez leurs noms \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s." },
        { "CrDisambigStateName_head" ,
                "Choisissez un nom unique pour <ocl>self</ocl>" },
        { "CrDisambigStateName_desc" ,
                "Chaque \u00e9tat dans un automate doit avoir un nom unique. Il y a au moins deux \u00e9tats de cet automate d\u00e9nomm\u00e9s \u00ab <ocl>self</ocl> \u00bb.\n\nUne d\u00e9nomination claire et sans ambigu\u00eft\u00e9 est indispensable \u00e0 la g\u00e9n\u00e9ration de code et \u00e0 la maintenabilit\u00e9 de la conception et facilite sa compr\u00e9hension.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement l'un des \u00e9tats et changez son nom \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s." },
        { "CrConflictingComposites_head" ,
                "Supprimez les relations de composition conflictuelles" },
        { "CrConflictingComposites_desc" ,
                "Le r\u00f4le de composition (losange noir) d'une association indique que les instances de cette classe contiennent des instances des classes associ\u00e9es. Comme chaque instance ne peut \u00eatre contenue que dans un seul autre objet, elle ne peut pas avoir de r\u00f4le de composition portant sur plus d'une instance.\n\nUne hi\u00e9rarchie d'agr\u00e9gation correcte est n\u00e9cessaire \u00e0 la g\u00e9n\u00e9ration de code et pour la justesse de la conception.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb o\u00f9 s\u00e9lectionnez manuellement l'association et changez sa multiplicit\u00e9 en 0..1 ou 1..1 ou son type (un losange par exemple est moins strict) ou encore supprimez l'une des associations." },
        { "CrTooManyAssoc_head" ,
                "R\u00e9duisez le nombre d'associations sur <ocl>self</ocl>" },
        { "CrTooManyAssoc_desc" ,
                "Il y a trop d'associations sur la classe <ocl>self</ocl>. Lorsqu'une classe devient un \u00e9l\u00e9ment trop central d'une conception, elle peut devenir un goulot d'\u00e9tranglement qui doit \u00eatre mis \u00e0 jour souvent lors de la maintenance.\n\nLa d\u00e9finition d'associations entre objets est une partie importante de votre conception\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou supprimez des associations manuellement en cliquant sur une association dans l'explorateur ou le diagramme et en appuyant sur la touche \u00ab Suppr \u00bb." },
        { "CrTooManyAttr_head" ,
                "R\u00e9duisez le nombre d'attribut de <ocl>self</ocl>" },
        { "CrTooManyAttr_desc" ,
                "Il y a trop d'attributs dans la classe <ocl>self</ocl>. Lorsqu'une classe devient un \u00e9l\u00e9ment trop central d'une conception, elle peut devenir un goulot d'\u00e9tranglement qui doit \u00eatre mis \u00e0 jour souvent lors de la maintenance.\n\nLa d\u00e9finition des attributs des objets est une partie importante de votre conception\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou supprimez des attributs manuellement en double-cliquant dans le compartiment des attributs de la classe concern\u00e9e et en supprimant la ligne de texte de l'attribut." },
        { "CrTooManyOper_head" ,
                "R\u00e9duisez le nombre d'op\u00e9rations de <ocl>self</ocl>" },
        { "CrTooManyOper_desc" ,
                "Il y a trop d'op\u00e9rations dans la classe <ocl>self</ocl>. Lorsqu'une classe devient un \u00e9l\u00e9ment trop central d'une conception, elle peut devenir un goulot d'\u00e9tranglement qui doit \u00eatre mis \u00e0 jour souvent lors de la maintenance.\n\nLa d\u00e9finition des op\u00e9rations des objets est une partie importante de votre conception\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou supprimez des op\u00e9rations manuellement en double-cliquant dans le compartiment des op\u00e9rations de la classe concern\u00e9e et en supprimant la ligne de texte de l'op\u00e9ration." },
        { "CrTooManyStates_head" ,
                "R\u00e9duisez le nombre d'\u00e9tats de <ocl>self</ocl>" },
        { "CrTooManyStates_desc" ,
                "Il y a trop d'\u00e9tats dans <ocl>self</ocl>. Si un automate comporte trop d'\u00e9tats il devient humainement difficile de le comprendre.\n\nLa d\u00e9finition d'un ensemble d'\u00e9tats compr\u00e9hensible est une partie importante de votre conception.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou supprimez des \u00e9tats en cliquant sur un \u00e9tat dans l'explorateur ou dans le diagramme et en appuyant sur la touche \u00ab Suppr \u00bb. Vous pouvez \u00e9galement utiliser des super et des sous-\u00e9tats." },
        { "CrTooManyTransitions_head" ,
                "R\u00e9duisez le nombre de transitions de <ocl>self</ocl>" },
        { "CrTooManyTransitions_desc" ,
                "Il y a trop de transitions sur l'\u00e9tat <ocl>self</ocl>. Si \u00e9tat devient trop central dans un automate, il peut devenir un goulot d'\u00e9tranglement qui doit \u00eatre mis \u00e0 jour souvent lors de la maintenance\n\nLa d\u00e9finition des transitions entre \u00e9tats est une partie importante de votre conception.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou supprimez des transitions en cliquant sur une transition dans l'explorateur ou dans le diagramme et en appuyant sur la touche \u00ab Suppr \u00bb." },
        { "CrTooManyClasses_head" ,
                "R\u00e9duisez le nombre de classes du diagramme <ocl>self</ocl>" },
        { "CrTooManyClasses_desc" ,
                "Il y a trop de classes dans <ocl>self</ocl>. Si un diagramme de classes contient trop de classes il devient humainement difficile de le comprendre.\n\nLa d\u00e9finition d'un ensemble de diagrammes de classes compr\u00e9hensible est une partie importante de votre conception.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb et supprimez des classes en cliquant sur une classe dans l'explorateur ou le diagramme et en appuyant sur la touche \u00ab Suppr \u00bb. Vous pouvez \u00e9galement cr\u00e9er un nouveau diagramme." },
        { "CrNoTransitions_head" ,
                "Ajoutez des transitions \u00e0 <ocl>self</ocl>" },
        { "CrNoTransitions_desc" ,
                "L'\u00e9tat <ocl>self</ocl> n'a pas de transition entrante ou sortante. Les \u00e9tats doivent avoir \u00e0 la fois des transitions entrantes et des transitions sortantes.\n\nLa d\u00e9finition de transitions compl\u00e8tes est indispensable \u00e0 la sp\u00e9cification du comportement de votre conception.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou ajoutez des transitions manuellement en cliquant sur l'outil des transitions dans la barre d'outils puis en faisant un glisser-d\u00e9poser entre un autre \u00e9tat et <ocl>self</ocl>." },
        { "CrNoIncomingTransitions_head" ,
                "Ajoutez des transitions entrantes dans <ocl>self</ocl>" },
        { "CrNoIncomingTransitions_desc" ,
                "L'\u00e9tat <ocl>self</ocl> n'a pas de transition entrante. Les \u00e9tats doivent avoir \u00e0 la fois des transitions entrantes et des transitions sortantes.\n\nLa d\u00e9finition de transitions compl\u00e8tes est indispensable \u00e0 la sp\u00e9cification du comportement de votre conception. Sans transition entrante, un \u00e9tat ne peut pas \u00eatre atteint.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou ajoutez des transitions manuellement en cliquant sur l'outil des transitions dans la barre d'outils puis en faisant un glisser-d\u00e9poser d'un autre \u00e9tat vers <ocl>self</ocl>." },
        { "CrNoOutgoingTransitions_head" ,
                "Ajoutez des transitions sortantes de <ocl>self</ocl>" },
        { "CrNoOutgoingTransitions_desc" ,
                "L'\u00e9tat <ocl>self</ocl> n'a pas de transition sortante. Les \u00e9tats doivent avoir \u00e0 la fois des transitions entrantes et des transitions sortantes.\n\nLa d\u00e9finition de transitions compl\u00e8tes est indispensable \u00e0 la sp\u00e9cification du comportement de votre conception. Sans transition sortante, un \u00e9tat devient un \u00e9tat \u00ab mort \u00bb dont on ne peut plus sortir.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou ajoutez des transitions manuellement en cliquant sur l'outil des transitions dans la barre d'outils puis en faisant un glisser-d\u00e9poser de <ocl>self</ocl> vers un autre \u00e9tat." },
        { "CrMultipleInitialStates_head" ,
                "Supprimez les \u00e9tats initiaux surnum\u00e9raires" },
        { "CrMultipleInitialStates_desc" ,
                "Il y a plusieurs \u00e9tats initiaux ambigus dans cet automate ou \u00e9tat composite. Un automate ou un \u00e9tat composite doivent normalement avoir un seul \u00e9tat initial.\n\nLa d\u00e9finition d'\u00e9tats non ambigus est indispensable \u00e0 la sp\u00e9cification du comportement de votre conception.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement les \u00e9tats initiaux surnum\u00e9raires et supprimez-les." },
        { "CrNoInitialState_head" ,
                "Mettez un \u00e9tat initial" },
        { "CrNoInitialState_desc" ,
                "Il n'y a aucun \u00e9tat initial dans cet automate ou \u00e9tat composite. Un automate ou un \u00e9tat composite doivent normalement avoir un \u00e9tat initial.\n\nLa d\u00e9finition d'\u00e9tats non ambigus est indispensable \u00e0 la sp\u00e9cification du comportement de votre conception.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez un \u00e9tat initial dans la barre d'outils et placez-le dans le diagramme." },
        { "CrNoTriggerOrGuard_head" ,
                "Ajoutez un \u00e9v\u00e9nement ou une garde" },
        { "CrNoTriggerOrGuard_desc" ,
                "La transition est incompl\u00e8te car elle n'a ni \u00e9v\u00e9nement ni condition de garde. Les \u00e9v\u00e9nements sont les d\u00e9clencheurs du passage par une transition. Les conditions de garde doivent \u00eatre vraies pour qu'une transition puisse \u00eatre faite. S'il n'y a qu'une condition de garde sans \u00e9v\u00e9nement, la transition est faite d\u00e8s que la condition est vraie.\n\nLa r\u00e9solution de ce probl\u00e8me est indispensable \u00e0 la justesse de l'automate.\n\nPour corriger, s\u00e9lectionnez la transition et utilisez l'onglet des propri\u00e9t\u00e9s ou s\u00e9lectionnez-la et saisissez du texte sous la forme\n\u00c9V\u00c9NEMENT [GARDE] / ACTION\n o\u00f9 \u00c9V\u00c9NEMENT est un nom d'\u00e9v\u00e9nement, GARDE est une expression bool\u00e9enne et ACTION est l'action \u00e0 r\u00e9aliser lorsque la transition est faite. Ces trois parties sont optionnelles." },
        { "CrNoGuard_head" ,
                "Ajoutez une garde \u00e0 la transition" },
        { "CrNoGuard_desc" ,
                "La transition est incompl\u00e8te car elle n'a pas de condition de garde. Les conditions de garde doivent \u00eatre vraies pour qu'une transition puisse \u00eatre faite. S'il n'y a qu'une condition de garde sans \u00e9v\u00e9nement, la transition est faite d\u00e8s que la condition est vraie.\n\nLa r\u00e9solution de ce probl\u00e8me est indispensable \u00e0 la justesse de l'automate.\n\nPour corriger, s\u00e9lectionnez la transition et utilisez l'onglet des propri\u00e9t\u00e9s ou s\u00e9lectionnez-la et saisissez du texte sous la forme\n[GARDE]\no\u00f9 GARDE est une expression bool\u00e9enne." },
        { "CrInvalidFork_head" ,
                "Changez les transitions de la bifurcation" },
        { "CrInvalidFork_desc" ,
                "Cet \u00e9tat de bifurcation a un nombre de transitions incorrect. Les \u00e9tats de bifurcation doivent normalement avoir une transition d'entr\u00e9e et plusieurs transitions de sortie.\n\nLa d\u00e9finition de transitions correctes est n\u00e9cessaire \u00e0 la sp\u00e9cification du comportement de votre conception.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou enlevez les transitions surnum\u00e9raires manuellement en cliquant dessus et en appuyant sur la touche \u00ab Suppr \u00bb." },
        { "CrInvalidJoin_head" ,
                "Changez les transitions de la jonction" },
        { "CrInvalidJoin_desc" ,
                "Cet \u00e9tat de jonction a un nombre de transitions incorrect. Les \u00e9tats de jonction doivent normalement avoir plusieurs transitions d'entr\u00e9e et une transition de sortie.\n\nLa d\u00e9finition de transitions correctes est n\u00e9cessaire \u00e0 la sp\u00e9cification du comportement de votre conception.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou enlevez les transitions surnum\u00e9raires manuellement en cliquant dessus et en appuyant sur la touche \u00ab Suppr \u00bb." },
        { "CrInvalidBranch_head" ,
                "Changez les transitions de la branche" },
        { "CrInvalidBranch_desc" ,
                "Cet \u00e9tat de branchement a un nombre de transitions incorrect. Les \u00e9tats de branchement doivent normalement avoir une transition d'entr\u00e9e et plusieurs transitions de sortie.\n\nLa d\u00e9finition de transitions correctes est n\u00e9cessaire \u00e0 la sp\u00e9cification du comportement de votre conception.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou selon le cas enlevez les transitions surnum\u00e9raires manuellement en cliquant dessus et en appuyant sur la touche \u00ab Suppr \u00bb ou ajoutez la transition manquante en utilisant l'outil transitions de la barre d'outils." },
        { "CrEmptyPackage_head" ,
                "Ajoutez des \u00e9l\u00e9ments au paquetage <ocl>self</ocl>" },
        { "CrEmptyPackage_desc" ,
                "Vous n'avez pas encore mis d'\u00e9lement dans le paquetage <ocl>self</ocl>. Les paquetages contiennent normalement des groupes de classes corr\u00e9l\u00e9es.\n\nLa d\u00e9finition et l'utilisation des paquetages est importante pour la maintenabilit\u00e9 de la conception.\n\nPour corriger, s\u00e9lectionnez le paquetage <ocl>self</ocl> dans l'explorateur et ajoutez des diagrammes ou des \u00e9l\u00e9ments de mod\u00e9lisation comme des classes ou des cas d'utilisation." },
        { "CrNoOperations_head" ,
                "Ajoutez des op\u00e9rations \u00e0 <ocl>self</ocl>" },
        { "CrNoOperations_desc" ,
                "Vous n'avez pas encore mis d'op\u00e9ration dans la classe <ocl>self</ocl>. Les classes offrent des op\u00e9rations qui d\u00e9finissent leur comportement.\n\nLa d\u00e9finition des op\u00e9rations est indispensable \u00e0 la sp\u00e9cification du comportement de votre conception.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou ajoutez des op\u00e9rations manuellement en cliquant sur <ocl>self</ocl> dans l'explorateur et en utilisant le menu de cr\u00e9ation pour faire de nouvelles op\u00e9rations." },
        { "CrConstructorNeeded_head" ,
                "Ajouter un constructeur \u00e0 <ocl>self</ocl>" },
        { "CrConstructorNeeded_desc" ,
          "Vous n'avez pas encore mis de constructeur dans la classe"
              + " <ocl>self</ocl>. Les constructeurs initialisent les"
              + " nouvelles instances afin que leurs attributs aient"
              + " des valeurs valides. Cette classe a probablement"
              + " besoin d'un constructeur car certains de ses attributs"
              + " n'ont pas de valeur initiale.\n"
              + "\n"
              + "La d\u00e9finition de constructeurs est n\u00e9cessaire"
              + " \u00e0 l'\u00e9tablissement d'invariants de classes et"
              + " les invariants de classes sont une aide pr\u00e9cieuse"
              + " \u00e0 l'\u00e9criture de code robuste.\n"
              + "\n"
              + "Pour corriger, ajoutez un constructeur manuellement en"
              + " cliquant sur <ocl>self</ocl> dans l'explorateur et en"
              + " utilisant le menu contextuel de l'onglet des"
              + " propri\u00e9t\u00e9s, ou s\u00e9lectionnez"
              + " <ocl>self</ocl> dans le diagramme des classes et utilisez"
              + " l'outil d'ajout d'op\u00e9ration.\n"
              + "\n"
              + "Un constructeur est une op\u00e9ration ayant le"
              + " st\u00e9r\u00e9otype \u00ab Create \u00bb.\n"
              + "\n"
              + "Par convention (Java, C++) un constructeur porte le nom de"
              + " la classe, n'est pas statique, et ne retourne aucune"
              + " valeur (ce qui signifie que vous devez \u00e9liminer le"
              + " param\u00e8tre de retour qu'ArgoUML met par d\u00e9faut)."
              + " ArgoUML acceptera toute op\u00e9ration respectant ces"
              + " conventions comme un constructeur m\u00eame si elle n'a"
              + " pas le st\u00e9r\u00e9otype \u00ab Create \u00bb." },
        { "CrNameConfusion_head" ,
                "Modifiez les noms pour \u00e9viter des confusions" },
        { "CrNameConfusion_desc" ,
                "Les noms doivent \u00eatre clairement distincts les uns des autres. Ces deux noms sont tellement similaires que les lecteurs peuvent les confondre.\n\nUne d\u00e9nomination claire et sans ambigu\u00eft\u00e9 est indispensable \u00e0 la g\u00e9n\u00e9ration de code et \u00e0 la maintenabilit\u00e9 de la conception et facilite sa compr\u00e9hension.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou selectionnez manuellement les \u00e9l\u00e9ments et changez leur nom \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s. \u00c9vitez les noms qui ne diff\u00e8rent les uns des autres que par les majuscules, l'utilisation du blanc soulign\u00e9 ou par un seul caract\u00e8re." },
        { "CrMergeClasses_head" ,
                "\u00c9tudiez la possibilit\u00e9 de combiner les classes" },
        { "CrMergeClasses_desc" ,
                "La classe <ocl>self</ocl> ne participe qu'a une seule association et cette association est bijective. Les instances des deux classes devant \u00eatre cr\u00e9\u00e9es et d\u00e9truites simultan\u00e9ment, combiner ces classes peut simplifier votre conception sans r\u00e9duire la repr\u00e9sentativit\u00e9. Il est cependant possible que vous trouviez la classe combin\u00e9e trop grosse et trop complexe, auquel cas la s\u00e9paration est effectivement une meilleure solution.\n\nL'organisation des classes de fa\u00e7on \u00e0 ma\u00eetriser la complexit\u00e9 de la conception est toujours importante, en particulier quand cette conception est d\u00e9j\u00e0 complexe.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou ajoutez manuellement les attributs et les op\u00e9rations de la classe \u00e0 l'autre classe et supprimez la classe du projet." },
        { "CrSubclassReference_head" ,
                "Supprimez les r\u00e9f\u00e9rences \u00e0 une sous-classe sp\u00e9cifique" },
        { "CrSubclassReference_desc" ,
                "La classe <ocl>self</ocl> a une r\u00e9f\u00e9rence \u00e0 l'une de ses sous-classes. Les sous-classes doivent normalement \u00eatre trait\u00e9es de fa\u00e7on \u00e9galitaire par la classe de base. Ceci permet d'ajouter de nouvelles sous-classes sans modifier la classe de base.\n\nLa d\u00e9finition des associations entre objets est une partie importante de votre conception. Certains mod\u00e8les d'associations sont plus faciles \u00e0 maintenir que d'autres, en fonction de la nature des changements futurs.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou supprimez l'association manuellement et cliquant desssus et en appuyant sur la touche \u00ab Suppr \u00bb." },
        { "CrComponentWithoutNode_head" ,
                "Mettez les composants dans les n\u0153uds" },
        { "CrComponentWithoutNode_desc" ,
                "Il y a des n\u0153uds dans ce diagramme, il s'agit donc d'un vrai diagramme de d\u00e9ploiement et dans les diagrammes de d\u00e9ploiement les composants appartiennent normalement \u00e0 des n\u0153uds." },
        { "CrCompInstanceWithoutNode_head" ,
                "Mettez les instances de composants dans les n\u0153uds" },
        { "CrCompInstanceWithoutNode_desc" ,
                "Il y a des instances de n\u0153uds dans ce diagramme, il s'agit donc d'un vrai diagramme de d\u00e9ploiement et dans les diagrammes de d\u00e9ploiement les instances de composants appartiennent normalement \u00e0 des instances de n\u0153uds." },
        { "CrClassWithoutComponent_head" ,
                "Mettez les classes dans les composants" },
        { "CrClassWithoutComponent_desc" ,
                "Dans les diagrammes de d\u00e9ploiement, les classes sont normalement dans des composants." },
        { "CrInterfaceWithoutComponent_head" ,
                "Mettez les interfaces dans les composants" },
        { "CrInterfaceWithoutComponent_desc" ,
                "Dans les diagrammes de d\u00e9ploiement, les interfaces sont normalement dans des composants." },
        { "CrObjectWithoutComponent_head" ,
                "Mettez les objets dans les composants" },
        { "CrObjectWithoutComponent_desc" ,
                "Dans les diagrammes de d\u00e9ploiement, les objets sont normalement dans des composants ou des instances de composants." },
        { "CrNodeInsideElement_head" ,
                "Otez les n\u0153uds inclus dans d'autres \u00e9l\u00e9ments" },
        { "CrNodeInsideElement_desc" ,
                "Les n\u0153uds ne sont normalement pas dans d'autres \u00e9l\u00e9ments. Ils repr\u00e9sentent des objets physiques ayant acc\u00e8s \u00e0 des ressources de calcul, ils poss\u00e8dent au moins de la m\u00e9moire et g\u00e9n\u00e9ralement des possibilit\u00e9s de calculs." },
        { "CrNodeInstanceInsideElement_head" ,
                "Otez les instances de n\u0153uds incluses dans d'autres \u00e9l\u00e9ments" },
        { "CrNodeInstanceInsideElement_desc" ,
                "Les instances n\u0153uds ne sont normalement pas dans d'autres \u00e9l\u00e9ments. Elles repr\u00e9sentent des objets physiques ayant acc\u00e8s \u00e0 des ressources de calcul, elles poss\u00e8dent au moins de la m\u00e9moire et g\u00e9n\u00e9ralement des possibilit\u00e9s de calculs." },
        { "CrWrongLinkEnds_head" ,
                "Corrigez les extr\u00e9mit\u00e9s de liens qui ne sont pas au m\u00eame endroit" },
        { "CrWrongLinkEnds_desc" ,
                "Dans les diagrammes de d\u00e9ploiement, les objets peuvent appartenir soit dans des composants soit dans des instances de composants. Il n'est donc pas possible d'avoir deux objets interconnect\u00e9s lorsque l'un des objets r\u00e9side dans un composant et l'autre dans une instance de composant.\n\nPour corriger, d\u00e9placez l'un des objets interconnect\u00e9s vers un \u00e9l\u00e9ment qui a le m\u00eame type que celui dans lequel r\u00e9side l'autre objet." },
        { "CrInstanceWithoutClassifier_head" ,
                "Mettez un qualificateur" },
        { "CrInstanceWithoutClassifier_desc" ,
                "Les instances doivent avoir des qualificateurs." },
        { "CrCallWithoutReturn_head" ,
                "Ajoutez une action en retour" },
        { "CrCallWithoutReturn_desc" ,
                "Chaque action d'appel ou d'envoi n\u00e9cessite une action en retour, mais ce lien n'a pas d'action en retour correspondante." },
        { "CrReturnWithoutCall_head" ,
                "Ajouter une action d'appel ou d'envoi" },
        { "CrReturnWithoutCall_desc" ,
                "Chaque action en retour n\u00e9cessite une action d'appel ou d'envoi, mais ce lien n'a pas d'action en retour correspondante." },
        { "CrLinkWithoutStimulus_head" ,
                "Ajoutez un message au lien" },
        { "CrLinkWithoutStimulus_desc" ,
                "Dans les diagrammes de s\u00e9quence, les objets \u00e9metteurs envoient des stimuli aux objets r\u00e9cepteurs \u00e0 travers un lien. Ce lien ne repr\u00e9sente que la communication/connexion, un stimulus est n\u00e9cessaire." },
        { "CrSeqInstanceWithoutClassifier_head" ,
                "Ajoutez un qualificateur" },
        { "CrSeqInstanceWithoutClassifier_desc" ,
                "Les instances doivent avoir un qualificateur." },
        { "CrStimulusWithWrongPosition_head" ,
                "Changez la position des messages" },
        { "CrStimulusWithWrongPosition_desc" ,
                "Dans les diagrammes de s\u00e9quence, le c\u00f4t\u00e9 \u00e9metteur de la communication/connexion des stimuli est connect\u00e9 au d\u00e9but de l'activation. Pour pouvoir \u00eatre un \u00e9metteur, un objet doit d'abord avoir la main." },
        { "CrUnconventionalOperName_head" ,
                "Choisissez un meilleur nom d'op\u00e9ration" },
        { "CrUnconventionalOperName_desc" ,
                "Les noms d'op\u00e9rations commencent normalement par une lettre minuscule. Le nom \u00ab <ocl>self</ocl> \u00bb ne respecte pas cette convention.\n\nSuivre de bonnes conventions de nommage am\u00e9liore la maintenabilit\u00e9 de la conception et facilite sa compr\u00e9hension\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement <ocl>self</ocl> et changez son nom \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s." },
        { "CrUnconventionalAttrName_head" ,
                "Choisissez un meilleur nom d'attribut" },
        { "CrUnconventionalAttrName_desc" ,
                "Les noms d'attributs commencent normalement par une lettre minuscule. Le nom \u00ab <ocl>self</ocl> \u00bb ne respecte pas cette convention.\n\nSuivre de bonnes conventions de nommage am\u00e9liore la maintenabilit\u00e9 de la conception et facilite sa compr\u00e9hension\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement <ocl>self</ocl> et changez son nom \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s." },
        { "CrUnconventionalClassName_head" ,
                "Capitalisez le nom de la classe <ocl>self</ocl>" },
        { "CrUnconventionalClassName_desc" ,
                "Les noms de classes commencent normalement par une lettre majuscule. Le nom \u00ab <ocl>self</ocl> \u00bb ne respecte pas cette convention.\n\nSuivre de bonnes conventions de nommage am\u00e9liore la maintenabilit\u00e9 de la conception et facilite sa compr\u00e9hension\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement <ocl>self</ocl> et changez son nom \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s." },
        { "CrUnconventionalPackName_head" ,
                "Modifiez le nom du paquetage <ocl>self</ocl>" },
        { "CrUnconventionalPackName_desc" ,
                "Les noms de paquetages sont normalement \u00e9crits enti\u00e8rement en minuscules avec de points pour indiquer la hi\u00e9rarchie des paquetages. Le nom \u00ab <ocl>self</ocl> \u00bb ne respecte pas cette convention.\n\nSuivre de bonnes conventions de nommage am\u00e9liore la maintenabilit\u00e9 de la conception et facilite sa compr\u00e9hension\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement <ocl>self</ocl> et changez son nom \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s." },
        { "CrClassMustBeAbstract_head" ,
                "Qualifiez la classe d'abstraite" },
        { "CrClassMustBeAbstract_desc" ,
                "Les classes qui incluent ou h\u00e9ritent de m\u00e9thodes abstraites de leur classe de base ou d'interfaces doivent \u00eatre d\u00e9clar\u00e9es abstraites.\n\nLe choix des classes abstraites ou concr\u00e8tes est une partie importante de la conception d'une hi\u00e9rarchie.\n\nPour corriger, utilisez le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement la classe et qualifiez-l\u00e0 d'abstraite \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s ou impl\u00e9mentez chcune des op\u00e9rations abstraites h\u00e9rit\u00e9es de la classe de base ou de l'inteface." },
        { "CrReservedName_head" ,
                "Changez <ocl>self</ocl> en un mot non r\u00e9serv\u00e9" },
        { "CrReservedName_desc" ,
                "\u00ab <ocl>self</ocl> \u00bb est un mot-clef r\u00e9serv\u00e9 ou en est tr\u00e8s proche. Les noms des \u00e9l\u00e9ments du mod\u00e8le ne doivent pas entrer en conflit avec les mots r\u00e9serv\u00e9s du langage de programmation ou d'UML.\n\nL'utilisation de noms autoris\u00e9s est n\u00e9cessaire \u00e0 la g\u00e9n\u00e9ration de code compilable.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement l'\u00e9l\u00e9ment concern\u00e9 et changez son nom \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s." },
        { "CrMultipleInheritance_head" ,
                "Remplacez l'h\u00e9ritage multiple par des interfaces" },
        { "CrMultipleInheritance_desc" ,
                "<ocl>self</ocl> a plusieurs classes de base, mais la langage Java ne supporte pas l'h\u00e9ritage multiple. Vous devez plut\u00f4t utiliser des interfaces.\n\nVous ne pourrez g\u00e9n\u00e9rer de code Java qu'apr\u00e8s avoir corrig\u00e9 ce probl\u00e8me.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou supprimez manuellement l'une des classes de base et d\u00e9finissez une nouvelle interface avec les m\u00eame m\u00e9thodes, d\u00e9clarez que la classe <ocl>self</ocl> l'impl\u00e9mente et d\u00e9placez le corps des m\u00e9thodes de l'ancienne classe de base vers <ocl>self</ocl>." },
        { "CrIllegalName_head" ,
                "Choisissez un nom autoris\u00e9" },
        { "CrIllegalName_desc" ,
                "Les noms des \u00e9l\u00e9ments de mod\u00e9lisation doivent \u00eatre compos\u00e9s de lettres, de chiffres et de blancs soulign\u00e9s. Ils ne peuvent pas contenir de caract\u00e8res de ponctuation.\n\nLa g\u00e9n\u00e9ration de code compilable n'est possible qu'avec des noms autoris\u00e9s.\n\nPour corriger, appuyez sur le bouton \u00ab Next> \u00bb ou s\u00e9lectionnez manuellement l'\u00e9l\u00e9ment concern\u00e9 et changez son nom \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s." },
        { "CrConsiderSingleton_head" ,
                "\u00c9tudiez la possibilit\u00e9 d'utiliser le mod\u00e8le de conception du singleton pour <ocl>self</ocl>" },
        { "CrConsiderSingleton_desc" ,
                "<ocl>self</ocl> n'a ni attribut ni association qui soit"
              + " navigable \u00e0 partir d'elle. Ceci implique que toutes"
              + " les instances de cette classe seront indicernables les unes"
              + " des autres car aucune variable d'instance ne permettra de"
              + " les diff\u00e9rencier.\n"
              + "\n"
              + "Dans ces conditions, vous devriez envisager de rendre"
              + " explicite le fait que vous n'avez qu'une seule instance en"
              + " utilisant le mod\u00e8le de conception du singleton."
              + " L'utilisation du mod\u00e8le de conception du singleton peut"
              + " \u00e9conomiser du temps et de la place m\u00e9moire. Avec"
              + " ArgoUML, ceci peut \u00eatre fait en utilisant le"
              + " st\u00e9r\u00e9otype \u00ab Singleton \u00bb pour la classe.\n"
              + "\n"
              + "Si vous avez l'intention d'avoir plusieurs instances, vous"
              + " devez d\u00e9finir des variables d'instances (c'est \u00e0"
              + " dire des attributs non-statiques) et/ou des associations qui"
              + " soient navigables \u00e0 partir d'elle, ceci repr\u00e9sentant"
              + " les diff\u00e9rences entre instances.\n"
              + "\n"
              + "Si vous sp\u00e9cifiez que <ocl>self</ocl> est un singleton,"
              + " vous devez d\u00e9finir la classe de telle sorte qu'elle ne"
              + " puisse avoir qu'une seule instance. Ceci compl\u00e8tera la"
              + " repr\u00e9sentativit\u00e9 de votre conception. Pour ce faire,"
              + " vous devez suivre les \u00e9tapes suivantes :\n"
              + "\n"
              + "1. D\u00e9finissez une variable statique (une variable de classe)"
              + " contenant l'instance. Cette variable doit donc avoir pour type :"
              + " <ocl>self</ocl>.\n"
              + "\n"
              + "2. Rendez tous les constructeurs priv\u00e9s de sorte qu'aucune"
              + " autre instance ne puisse \u00eatre cr\u00e9\u00e9e dans d'autres"
              + " parties du code. La cr\u00e9ation de l'instance unique peut se"
              + " faire \u00e0 l'aide d'une fonction utilitaire qui appelle le"
              + " constructeur une seule fois.\n"
              + "\n"
              + "3. D\u00e9finissez au moins un constructeur qui \u00e9crase le"
              + " constructeur par d\u00e9faut, pour emp\u00eacher la cr\u00e9ation"
              + " d'instances par le constructeur par d\u00e9faut.\n"
              + "\n"
              + "Dans le standard UML 1.3, un constructeur est une op\u00e9ration qui"
              + " a le st\u00e9r\u00e9otype \u00ab Create \u00bb. bien qu'il ne soit"
              + " pas compl\u00e8tement standard, ArgoUML acceptera \u00e9galement"
              + " \u00ab create \u00bb comme st\u00e9r\u00e9otype pour les constructeurs.\n"
              + "\n"
              + "Par convention (Java, C++) un constructeur porte le nom de la classe,"
              + " n'est pas statique, et ne retourne aucune valeur (ce qui signifie que"
              + " vous devez \u00e9liminer le param\u00e8tre de retour qu'ArgoUML met"
              + " par d\u00e9faut). ArgoUML acceptera toute op\u00e9ration respectant"
              + " ces conventions comme un constructeur m\u00eame si elle n'a pas le"
              + " st\u00e9r\u00e9otype \u00ab Create \u00bb.\n"
              + "\n"
              + "\u00ab Singleton \u00bb ne fait pas partie des st\u00e9r\u00e9otypes"
              + " par d\u00e9faut d'ArgoUML, vous devrez donc le cr\u00e9er la"
              + " premi\u00e8re fois que vous l'utiliserez. Ceci peut \u00eatre fait"
              + " \u00e0 l'aide de l'onglet des propri\u00e9t\u00e9s des"
              + " st\u00e9r\u00e9otypes, qui est accessible \u00e0 l'aide du bouton"
              + " NavStereo de l'onglet des propri\u00e9t\u00e9s des classes. La"
              + " classe de base doit \u00eatre Class." },
        { "CrSingletonViolated_head" ,
                "Corrigez l'utilisation du st\u00e9r\u00e9otype du singleton pour <ocl>self</ocl>" },
        { "CrSingletonViolated_desc" ,
                "<ocl>self</ocl> est marqu\u00e9e avec le st\u00e9r\u00e9otype"
              + " \u00ab Singleton \u00bb, mais elle ne respecte pas les contraintes"
              + " impos\u00e9es aux singletons (ArgoUML accepte également \u00ab"
              + " singleton \u00bb comme st\u00e9r\u00e9otype pour les singletons).\n"
              + "\n"
              + "Une classe singleton ne peut avoir qu'une seule instance. Ceci se"
              + " traduit par le fait que la classe doit respecter les crit\u00e8res"
              + " de conception des singletons.\n"
              + "\n"
              + "1. Elle doit avoir un attribut statique (une variable de classe) pour"
              + " contenir l'instance.\n"
              + "\n"
              + "2. Elle ne doit avoir que des constructeurs priv\u00e9s afin"
              + " qu'aucune nouvelle instance ne puisse \u00eatre cr\u00e9\u00e9e"
              + " dans d'autres parties du code.\n"
              + "\n"
              + "3. Elle doit avoir au moins un constructeur pour \u00e9craser le"
              + " constructeur par d\u00e9faut, pour emp\u00eacher la cr\u00e9ation"
              + " d'instances par le constructeur par d\u00e9faut.\n"
              + "\n"
              + "Lorsque vous marquez une classe avec un st\u00e9r\u00e9otype, cette"
              + " classe doit satisfaire toutes les contraintes du st\u00e9r\u00e9otype."
              + " Ceci est important pour avoir une conception coh\u00e9rente et"
              + " compr\u00e9hensible. Utiliser le mod\u00e8le de conception du singleton"
              + " peut \u00e9conomiser du temps et de la place m\u00e9moire.\n"
              + "\n"
              + "Si vous ne voulez plus que cette classe soit un singleton, enlevez le"
              + " st\u00e9r\u00e9otype \u00ab Singleton \u00bb ou le st\u00e9r\u00e9otype"
              + " \u00ab singleton \u00bb en cliquant sur la classe et en faisant une"
              + " s\u00e9lection vide dans la liste d\u00e9roulante de"
              + " st\u00e9r\u00e9otypes de l'onglet des propri\u00e9t\u00e9s.\n"
              + "\n"
              + "Si vous voulez que la classe respecte les contraintes du"
              + " st\u00e9r\u00e9otype \u00ab Singleton \u00bb vous devez suivre les"
              + " \u00e9tapes suivantes :\n"
              + "\n"
              + "1. D\u00e9finissez une variable statique (une variable de classe)"
              + " contenant l'instance. Cette variable doit donc avoir pour type :"
              + " <ocl>self</ocl>.\n"
              + "\n"
              + "2. Rendez tous les constructeurs priv\u00e9s de sorte qu'aucune"
              + " autre instance ne puisse \u00eatre cr\u00e9\u00e9es dans d'autres"
              + " parties du code. La cr\u00e9ation de l'instance unique peut se"
              + " faire \u00e0 l'aide d'une fonction utilitaire qui appelle le"
              + " constructeur une seule fois.\n"
              + "\n"
              + "3. D\u00e9finissez au moins un constructeur qui \u00e9crase le"
              + " constructeur par d\u00e9faut, pour emp\u00eacher la cr\u00e9ation"
              + " d'instances par le constructeur par d\u00e9faut.\n"
              + "\n"
              + "Dans le standard UML 1.3, un constructeur est une op\u00e9ration qui"
              + " a le st\u00e9r\u00e9otype \u00ab Create \u00bb. bien qu'il ne soit"
              + " pas compl\u00e8tement standard, ArgoUML acceptera \u00e9galement"
              + " \u00ab create \u00bb comme st\u00e9r\u00e9otype pour les constructeurs.\n"
              + "\n"
              + "Par convention (Java, C++) un constructeur porte le nom de la classe,"
              + " n'est pas statique, et ne retourne aucune valeur (ce qui signifie que"
              + " vous devez \u00e9liminer le param\u00e8tre de retour qu'ArgoUML met"
              + " par d\u00e9faut). ArgoUML acceptera toute op\u00e9ration respectant"
              + " ces conventions comme un constructeur m\u00eame si elle n'a pas le"
              + " st\u00e9r\u00e9otype \u00ab Create \u00bb." },
        { "CrNodesOverlap_head" ,
                "R\u00e9organisez votre diagramme" },
        { "CrNodesOverlap_desc" ,
                "Certains des objets de ce diagramme se recouvrent mutuellement. Ceci peut masquer des informations importantes et le rendre difficile \u00e0 comprendre. Une apparence claire est \u00e9galement un moyen de persuasion pour les autres concepteurs, les personnes charg\u00e9es de l'impl\u00e9mentation et les d\u00e9cisionnaires.\n\nLa construction d'un ensemble de diagrammes compr\u00e9hensible est une partie importante de votre conception.\n\nPour corriger, d\u00e9placez les n\u0153uds concern\u00e9s dans le diagramme." },
        { "CrZeroLengthEdge_head" ,
                "Allongez cette ligne" },
        { "CrZeroLengthEdge_desc" ,
                "Cette ligne est trop courte pour \u00eatre visible facilement. Ceci peut masquer des informations importantes et le rendre difficile \u00e0 comprendre. Une apparence claire est \u00e9galement un moyen de persuasion pour les autres concepteurs, les personnes charg\u00e9es de l'impl\u00e9mentation et les d\u00e9cisionnaires.\n\nLa construction d'un ensemble de diagrammes compr\u00e9hensible est une partie importante de votre conception.\n\nPour corriger, d\u00e9placez un ou plusieurs n\u0153uds afin d'allonger la ligne ou cliquez au milieu ligne et faites un glisser-d\u00e9poser pour cr\u00e9er un nouveau sommet." },
        //
        //   these phrases should be localized here
        //      not in the following check list section
        { "Naming", "Nommage" },
        { "Encoding", "Encodage" },
        { "Value", "Valeur" },
        { "Location", "Position" },
        { "Updates", "Mises \u00e0 jour" },
        { "General", "G\u00e9n\u00e9ral" },
        { "Actions" , "Actions" },
        { "Transitions", "Transitions" },
        { "Structure", "Structure" },
        { "Trigger", "\u00c9v\u00e9nements" },
        { "MGuard", "Garde" },
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
                  "Le nom \u00ab <ocl>self</ocl> \u00bb d\u00e9crit-il clairement la classe ?",
                  "\u00ab <ocl>self</ocl> \u00bb est-il un nom ou une phrase nominale ?",
                  "Le nom \u00ab <ocl>self</ocl> \u00bb pourrait-il \u00eatre mal interpr\u00e9t\u00e9 ou signifier quelque chose d'autre ?"
                },
                new String[] { "Encoding",
                  "<ocl>self</ocl> doit-elle \u00eatre sa propre classe ou un simple attribut d'une autre classe ?",
                  "La classe <ocl>self</ocl> fait-elle exactement une chose et la fait-elle bien ?",
                  "<ocl>self</ocl> pourrait-elle \u00eatre scind\u00e9e en deux classes ou plus ?"
                },
                new String[] { "Value",
                  "Tous les attributs de <ocl>self</ocl> ont-ils des valeurs initiales significatives ?",
                  "Pourriez-vous \u00e9crire un invariant pour cette classe ?",
                  "Tous les constructeurs \u00e9tablissent-ils l'invariant de classe ?",
                  "Toutes les op\u00e9rations maintiennent-elles l'invariant de classe ?"
                },
                new String[] { "Location",
                  "La classe <ocl>self</ocl> pourrait-elle \u00eatre positionn\u00e9e diff\u00e9remment dans la hi\u00e9rarchie de classes ?",
                  "Avez-vous pr\u00e9vu d'avoir des sous-classes de <ocl>self</ocl> ?",
                  "La classe <ocl>self</ocl> pourrait-elle \u00eatre \u00e9limin\u00e9e du mod\u00e8le ?",
                  "Existe-t-il une autre classe du mod\u00e8le qui pourrait \u00eatre revue ou \u00e9limin\u00e9e car elle a la m\u00eame fonction que <ocl>self</ocl> ?"
                },
                new String[] { "Updates",
                  "\u00c0 quelles occasions les instances de <ocl>self</ocl> doivent-elles \u00eatre mises \u00e0 jour ?",
                  "Y a-t-il d'autres objets qui doivent \u00eatre mis \u00e0 jour lorsque <ocl>self</ocl> est mise \u00e0 jour ?"
                }
            }
        },
        { "ChAttribute",
            new String[][] {
                new String[] { "Naming",
                  "Le nom \u00ab <ocl>self</ocl> \u00bb d\u00e9crit-il clairement l'attribut ?",
                  "\u00ab <ocl>self</ocl> \u00bb est-il un nom ou une phrase nominale ?",
                  "Le nom \u00ab <ocl>self</ocl> \u00bb pourrait-il \u00eatre mal interpr\u00e9t\u00e9 ou signifier quelque chose d'autre ?"
                },
                new String[] { "Encoding",
                  "Le type <ocl>self.type</ocl> est-il trop restrictif pour repr\u00e9senter toutes les valeurs possibles de <ocl>self</ocl>?",
                  "Le type <ocl>self.type</ocl> permet-il \u00e0 <ocl>self</ocl> de prendre des valeurs qui ne pourront jamais \u00eatre correctes ?",
                  "L'attribut <ocl>self</ocl> pourrait-il \u00eatre combin\u00e9 avec un autre attribut de <ocl>self.owner</ocl> (par exemple {owner.structuralFeature}) ?",
                  "L'attribut <ocl>self</ocl> pourrait-il \u00eatre scind\u00e9 en deux parties ou plus (par exemple un num\u00e9ro de t\u00e9l\u00e9phone peut \u00eatre s\u00e9par\u00e9 en code r\u00e9gional, pr\u00e9fixe et num\u00e9ro) ?",
                  "L'attribut <ocl>self</ocl> pourrait-il \u00eatre calcul\u00e9 \u00e0 partir d'autres attributs au lieu d'\u00eatre stock\u00e9 ?"
                },
                new String[] { "Value",
                  "L'attribut <ocl>self</ocl> a-t-il une valeur initiale ou par d\u00e9faut ?",
                  "La valeur initiale <ocl>self.initialValue</ocl> est-elle correcte ?",
                  "Pourriez-vous \u00e9crire une expression v\u00e9rifiant si <ocl>self</ocl> est correcte ou plausible ?"
                },
                new String[] { "Location",
                  "L'attribut <ocl>self</ocl> pourrait-il \u00eatre d\u00e9fini dans une autre classe associ\u00e9e \u00e0 <ocl>self.owner</ocl> ?",
                  "L'attribut <ocl>self</ocl> pourrait-il \u00eatre remont\u00e9 dans la hi\u00e9rarchie et s'appliquer \u00e0 <ocl>owner.name</ocl> et \u00e0 d'autres classes ?",
                  "L'attribut <ocl>self</ocl> s'applique-t-il \u00e0 toutes les instances de la classe <ocl>self.owner</ocl> y compris les instances de sous-classes ?",
                  "L'attribut <ocl>self</ocl> pourrait-il \u00eatre supprim\u00e9 du mod\u00e8le ?",
                  "Existe-t-il un autre attribut du mod\u00e8le qui pourrait \u00eatre revu ou \u00e9limin\u00e9 car il a la m\u00eame fonction que <ocl>self</ocl> ?"
                },
                new String[] { "Updates",
                  "\u00c0 quelles occasions l'attribut <ocl>self</ocl> doit-il \u00eatre mis \u00e0 jour ?",
                  "Y a-t-il d'autres attributs qui doivent \u00eatre mis \u00e0 jour lorsque <ocl>self</ocl> est mis \u00e0 jour ?",
                  "Y a-t-il une m\u00e9thode qui doivent \u00eatre appel\u00e9e lorsque <ocl>self</ocl> est mis \u00e0 jour ?",
                  "Y a-t-il une m\u00e9thode qui doivent \u00eatre appel\u00e9e lorsque <ocl>self</ocl> prend certaines valeurs ?"
                }
            }
        },
        { "ChOperation",
            new String[][] {
                new String[] { "Naming",
                  "Le nom \u00ab <ocl>self</ocl> \u00bb d\u00e9crit-il clairement l'op\u00e9ration ?",
                  "\u00ab <ocl>self</ocl> \u00bb est-il un verbe ou une phrase verbale ?",
                  "Le nom \u00ab <ocl>self</ocl> \u00bb pourrait-il \u00eatre mal interpr\u00e9t\u00e9 ou signifier quelque chose d'autre ?",
                  "L'op\u00e9ration <ocl>self</ocl> fait-elle exactement une chose et la fait-elle bien ?",
                },
                new String[] { "Encoding",
                  "Le type de retour <ocl>self.returnType</ocl> est-il trop restrictif pour repre\u00e9senter toutes les valeurs retourn\u00e9es par <ocl>self</ocl> ?",
                  "Le type de retour <ocl>self.returnType</ocl> permet-il de repr\u00e9senter des valeurs qui ne pourront jamais \u00eatre correctes ?",
                  "L'op\u00e9ration <ocl>self</ocl> peut-elle \u00eatre combin\u00e9e avec d'autres op\u00e9rations de <ocl>self.owner</ocl> (par exemple <ocl sep=', '>self.owner.behavioralFeature</ocl>) ?",
                  "L'op\u00e9ration<ocl>self</ocl> pourrait-elle \u00eatre scind\u00e9e en deux op\u00e9rations ou plus (par exemple un pr\u00e9-traitement, le traitement et un post-traitement) ?",
                  "L'op\u00e9ration <ocl>self</ocl> pourrait-elle \u00eatre remplac\u00e9e par une s\u00e9rie d'appels par le client \u00e0 des op\u00e9rations plus simples ?",
                  "L'op\u00e9ration <ocl>self</ocl> pourrait-elle \u00eatre combin\u00e9e avec d'autres op\u00e9rations pour r\u00e9duire le nombre d'appels que le client doit faire ?"
                },
                new String[] { "Value",
                  "L'op\u00e9ration <ocl>self</ocl> peut-elle traiter toutes les entr\u00e9es possibles ?",
                  "Y a-t-il des cas d'entr\u00e9es qui doivent \u00eatre trait\u00e9s s\u00e9par\u00e9ments ?",
                  "Pourriez-vous \u00e9crire une expression v\u00e9rifiant si les arguments de <ocl>self</ocl> sont corrects ou plausibles ?",
                  "Pourriez-vous exprimer les pr\u00e9-conditions de <ocl>self</ocl>?",
                  "Pourriez-vous exprimer les post-conditions de <ocl>self</ocl>?",
                  "Comment l'op\u00e9ration <ocl>self</ocl> r\u00e9agit-elle lorsque les pr\u00e9-conditions sont viol\u00e9es ?",
                  "Comment l'op\u00e9ration <ocl>self</ocl> r\u00e9agit-elle lorsque les post-conditions sont viol\u00e9es ?",
                },
                new String[] { "Location",
                  "L'op\u00e9ration <ocl>self</ocl> pourrait-elle \u00eatre d\u00e9finie dans une autre classe associ\u00e9e \u00e0 <ocl>self.owner</ocl> ?",
                  "L'op\u00e9ration <ocl>self</ocl> pourrait-elle \u00eatre remont\u00e9e dans la hi\u00e9rarchie et s'appliquer \u00e0 <ocl>self.owner</ocl> et \u00e0 d'autres classes ?",
                  "L'op\u00e9ration <ocl>self</ocl> s'applique-t-elle \u00e0 toutes les instances de la classe <ocl>self.owner</ocl> y compris les instances de sous-classes ?",
                  "L'op\u00e9ration <ocl>self</ocl> pourrait-elle \u00eatre supprim\u00e9e du mod\u00e8le ?",
                  "Existe-t-il une autre op\u00e9ration du mod\u00e8le qui pourrait \u00eatre revue ou \u00e9limin\u00e9e car elle a la m\u00eame fonction que <ocl>self</ocl> ?"
                }
            }
        },
        { "ChAssociation",
            new String[][] {
                new String[] { "Naming",
                  "Le nom \u00ab <ocl>self</ocl> \u00bb d\u00e9crit-il clairement la classe ?",
                  "\u00ab <ocl>self</ocl> \u00bb est-il un nom ou une phrase nominale ?",
                  "Le nom \u00ab <ocl>self</ocl> \u00bb pourrait-il \u00eatre mal interpr\u00e9t\u00e9 ou signifier quelque chose d'autre ?"
                },
                new String[] { "Encoding",
                  "<ocl>self</ocl> doit-elle \u00eatre sa propre classe ou un simple attribut d'une autre classe ?",
                  "La classe <ocl>self</ocl> fait-elle exactement une chose et la fait-elle bien ?",
                  "<ocl>self</ocl> pourrait-elle \u00eatre scind\u00e9e en deux classes ou plus ?"
                },
                new String[] { "Value",
                  "Tous les attributs de <ocl>self</ocl> ont-ils des valeurs initiales significatives ?",
                  "Pourriez-vous \u00e9crire un invariant pour cette classe ?",
                  "Tous les constructeurs \u00e9tablissent-ils l'invariant de classe ?",
                  "Toutes les op\u00e9rations maintiennent-elles l'invariant de classe ?"
                },
                new String[] { "Location",
                  "La classe <ocl>self</ocl> pourrait-elle \u00eatre positionn\u00e9e diff\u00e9remment dans la hi\u00e9rarchie de classes ?",
                  "Avez-vous pr\u00e9vu d'avoir des sous-classes de <ocl>self</ocl> ?",
                  "La classe <ocl>self</ocl> pourrait-elle \u00eatre \u00e9limin\u00e9e du mod\u00e8le ?",
                  "Existe-t-il une autre classe du mod\u00e8le qui pourrait \u00eatre revue ou \u00e9limin\u00e9e car elle a la m\u00eame fonction que <ocl>self</ocl> ?"
                },
                new String[] { "Updates",
                  "\u00c0 quelles occasions les instances de <ocl>self</ocl> doivent-elles \u00eatre mises \u00e0 jour ?",
                  "Y a-t-il d'autres objets qui doivent \u00eatre mis \u00e0 jour lorsque <ocl>self</ocl> est mise \u00e0 jour ?"
                }
            }
        },
        { "ChInterface",
            new String[][] {
                new String[] { "Naming",
                  "Le nom \u00ab <ocl>self</ocl> \u00bb d\u00e9crit-il clairement la classe ?",
                  "\u00ab <ocl>self</ocl> \u00bb est-il un nom ou une phrase nominale ?",
                  "Le nom \u00ab <ocl>self</ocl> \u00bb pourrait-il \u00eatre mal interpr\u00e9t\u00e9 ou signifier quelque chose d'autre ?"
                },
                new String[] { "Encoding",
                  "<ocl>self</ocl> doit-elle \u00eatre sa propre classe ou un simple attribut d'une autre classe ?",
                  "La classe <ocl>self</ocl> fait-elle exactement une chose et la fait-elle bien ?",
                  "<ocl>self</ocl> pourrait-elle \u00eatre scind\u00e9e en deux classes ou plus ?"
                },
                new String[] { "Value",
                  "Tous les attributs de <ocl>self</ocl> ont-ils des valeurs initiales significatives ?",
                  "Pourriez-vous \u00e9crire un invariant pour cette classe ?",
                  "Tous les constructeurs \u00e9tablissent-ils l'invariant de classe ?",
                  "Toutes les op\u00e9rations maintiennent-elles l'invariant de classe ?"
                },
                new String[] { "Location",
                  "La classe <ocl>self</ocl> pourrait-elle \u00eatre positionn\u00e9e diff\u00e9remment dans la hi\u00e9rarchie de classes ?",
                  "Avez-vous pr\u00e9vu d'avoir des sous-classes de <ocl>self</ocl> ?",
                  "La classe <ocl>self</ocl> pourrait-elle \u00eatre \u00e9limin\u00e9e du mod\u00e8le ?",
                  "Existe-t-il une autre classe du mod\u00e8le qui pourrait \u00eatre revue ou \u00e9limin\u00e9e car elle a la m\u00eame fonction que <ocl>self</ocl> ?"
                },
                new String[] { "Updates",
                  "\u00c0 quelles occasions les instances de <ocl>self</ocl> doivent-elles \u00eatre mises \u00e0 jour ?",
                  "Y a-t-il d'autres objets qui doivent \u00eatre mis \u00e0 jour lorsque <ocl>self</ocl> est mise \u00e0 jour ?"
                }
            }

        },
        { "ChInstance",
            new String[][] {
                new String[] { "General",
                  "L'instance <ocl>self</ocl> d\u00e9crit-elle clairement ce qu'elle est ?"
                },
                new String[] { "Naming",
                  "Le nom \u00ab <ocl>self</ocl> \u00bb d\u00e9crit-il clairement l'instance ?",
                  "Le terme \u00ab <ocl>self</ocl> \u00bb d\u00e9note-t-il bien un \u00e9tat plut\u00f4t qu'une activit\u00e9 ?",
                  "Le nom \u00ab <ocl>self</ocl> \u00bb pourrait-il \u00eatre mal interpr\u00e9t\u00e9 ou signifier quelque chose d'autre ?"
                },
                new String[] { "Structure",
                  "L'\u00e9tat <ocl>self</ocl> est-il bien ind\u00e9pendant ou doit-il \u00eatre combin\u00e9 avec un autre \u00e9tat ?",
                  "L'\u00e9tat <ocl>self</ocl> fait-il une chose et la fait-il bien ?",
                  "L'\u00e9tat <ocl>self</ocl> pourrait-il \u00eatre scind\u00e9 en deux \u00e9tats ou plus ?",
                  "Pourriez-vous \u00e9crire une \u00e9quation caract\u00e9ristique pour <ocl>self</ocl>?",
                  "L'\u00e9tat <ocl>self</ocl> appartient-il bien \u00e0 cet automate ou \u00e0 un autre ?",
                  "L'\u00e9tat <ocl>self</ocl> devrait-il \u00eatre un \u00e9tat initial ?",
                  "Y a-t-il un autre \u00e9tat d'un autre automate qui soit exclusif de <ocl>self</ocl>?"
                },
                new String[] { "Actions",
                  "Quelles actions doivent-elles \u00eatre r\u00e9alis\u00e9es \u00e0 l'entr\u00e9e de <ocl>self</ocl> ?",
                  "Quels attributs doivent \u00eatre mis \u00e0 jour \u00e0 l'entr\u00e9e de <ocl>self</ocl> ?",
                  "Quelles actions doivent-elles \u00eatre r\u00e9alis\u00e9es \u00e0 la sortie de <ocl>self</ocl> ?",
                  "Quels attributs doivent \u00eatre mis \u00e0 jour \u00e0 la sortie de <ocl>self</ocl> ?",
                  "Quelles actions doivent-elles \u00eatre r\u00e9alis\u00e9es \u00e0 l'int\u00e9rieur de <ocl>self</ocl> ?",
                  "Les actions-\u00e9tats maintiennent-elles <ocl>self</ocl> comme \u00e9tat courant ?"
                },
                new String[] { "Transitions",
                  "D'autres transitions vers <ocl>self</ocl> devraient-elles \u00eatre ajout\u00e9es ?",
                  "Toutes les transitions vers <ocl>self</ocl> sont-elles utilisables ?",
                  "Certaines transitions entrantes doivent-elles \u00eatre combin\u00e9es ?",
                  "D'autres transitions hors de <ocl>self</ocl> devraient-elles \u00eatre ajout\u00e9es ?",
                  "Toutes les transitions hors de <ocl>self</ocl> sont-elles utilisables ?",
                  "Toutes les transitions sortantes sont-elles exclusives ?",
                  "Certaines transitions sortantes doivent-elles \u00eatre combin\u00e9es ?",
                }
            }
        },
        { "ChLink",
            new String[][] {
                new String[] { "Naming",
                  "Le nom \u00ab <ocl>self</ocl> \u00bb d\u00e9crit-il clairement la classe ?",
                  "\u00ab <ocl>self</ocl> \u00bb est-il un nom ou une phrase nominale ?",
                  "Le nom \u00ab <ocl>self</ocl> \u00bb pourrait-il \u00eatre mal interpr\u00e9t\u00e9 ou signifier quelque chose d'autre ?"
                },
                new String[] { "Encoding",
                  "<ocl>self</ocl> doit-elle \u00eatre sa propre classe ou un simple attribut d'une autre classe ?",
                  "La classe <ocl>self</ocl> fait-elle exactement une chose et la fait-elle bien ?",
                  "<ocl>self</ocl> pourrait-elle \u00eatre scind\u00e9e en deux classes ou plus ?"
                },
                new String[] { "Value",
                  "Tous les attributs de <ocl>self</ocl> ont-ils des valeurs initiales significatives ?",
                  "Pourriez-vous \u00e9crire un invariant pour cette classe ?",
                  "Tous les constructeurs \u00e9tablissent-ils l'invariant de classe ?",
                  "Toutes les op\u00e9rations maintiennent-elles l'invariant de classe ?"
                },
                new String[] { "Location",
                  "La classe <ocl>self</ocl> pourrait-elle \u00eatre positionn\u00e9e diff\u00e9remment dans la hi\u00e9rarchie de classes ?",
                  "Avez-vous pr\u00e9vu d'avoir des sous-classes de <ocl>self</ocl> ?",
                  "La classe <ocl>self</ocl> pourrait-elle \u00eatre \u00e9limin\u00e9e du mod\u00e8le ?",
                  "Existe-t-il une autre classe du mod\u00e8le qui pourrait \u00eatre revue ou \u00e9limin\u00e9e car elle a la m\u00eame fonction que <ocl>self</ocl> ?"
                },
                new String[] { "Updates",
                  "\u00c0 quelles occasions les instances de <ocl>self</ocl> doivent-elles \u00eatre mises \u00e0 jour ?",
                  "Y a-t-il d'autres objets qui doivent \u00eatre mis \u00e0 jour lorsque <ocl>self</ocl> est mise \u00e0 jour ?"
                }
            }

        },
        { "ChState",
            new String[][] {
                new String[] { "Naming",
                  "Le nom \u00ab <ocl>self</ocl> \u00bb d\u00e9crit-il clairement l'\u00e9tat ?",
                  "Le terme \u00ab <ocl>self</ocl> \u00bb d\u00e9note-t-il bien un \u00e9tat plut\u00f4t qu'une activit\u00e9 ?",
                  "Le nom \u00ab <ocl>self</ocl> \u00bb pourrait-il \u00eatre mal interpr\u00e9t\u00e9 ou signifier quelque chose d'autre ?"
                },
                new String[] { "Structure",
                  "L'\u00e9tat <ocl>self</ocl> est-il bien ind\u00e9pendant ou doit-il \u00eatre combin\u00e9 avec un autre \u00e9tat ?",
                  "L'\u00e9tat <ocl>self</ocl> fait-il une chose et la fait-il bien ?",
                  "L'\u00e9tat <ocl>self</ocl> pourrait-il \u00eatre scind\u00e9 en deux \u00e9tats ou plus ?",
                  "Pourriez-vous \u00e9crire une \u00e9quation caract\u00e9ristique pour <ocl>self</ocl>?",
                  "L'\u00e9tat <ocl>self</ocl> appartient-il bien \u00e0 cet automate ou \u00e0 un autre ?",
                  "L'\u00e9tat <ocl>self</ocl> devrait-il \u00eatre un \u00e9tat initial ?",
                  "Y a-t-il un autre \u00e9tat d'un autre automate qui soit exclusif de <ocl>self</ocl>?"
                },
                new String[] { "Actions",
                  "Quelles actions doivent-elles \u00eatre r\u00e9alis\u00e9es \u00e0 l'entr\u00e9e de <ocl>self</ocl> ?",
                  "Quels attributs doivent \u00eatre mis \u00e0 jour \u00e0 l'entr\u00e9e de <ocl>self</ocl> ?",
                  "Quelles actions doivent-elles \u00eatre r\u00e9alis\u00e9es \u00e0 la sortie de <ocl>self</ocl> ?",
                  "Quels attributs doivent \u00eatre mis \u00e0 jour \u00e0 la sortie de <ocl>self</ocl> ?",
                  "Quelles actions doivent-elles \u00eatre r\u00e9alis\u00e9es \u00e0 l'int\u00e9rieur de <ocl>self</ocl> ?",
                  "Les actions-\u00e9tats maintiennent-elles <ocl>self</ocl> comme \u00e9tat courant ?"
                },
                new String[] { "Transitions",
                  "D'autres transitions vers <ocl>self</ocl> devraient-elles \u00eatre ajout\u00e9es ?",
                  "Toutes les transitions vers <ocl>self</ocl> sont-elles utilisables ?",
                  "Certaines transitions entrantes doivent-elles \u00eatre combin\u00e9es ?",
                  "D'autres transitions hors de <ocl>self</ocl> devraient-elles \u00eatre ajout\u00e9es ?",
                  "Toutes les transitions hors de <ocl>self</ocl> sont-elles utilisables ?",
                  "Toutes les transitions sortantes sont-elles exclusives ?",
                  "Certaines transitions sortantes doivent-elles \u00eatre combin\u00e9es ?",
                }
            }

        },
        { "ChTransition",
            new String[][] {
                new String[] { "Structure",
                  "Cette transition devrait-elle partir d'une autre source ?",
                  "Cette transition devrait-elle arriver \u00e0 une autre destination ?",
                  "Devrait-il y avoir une autre transitions \u00ab similaire \u00bb \u00e0 celle-ci ?",
                  "Une autre transition devient-elle inutile \u00e0 caus e de celle-ci ?"
                },
                new String[] { "Trigger",
                  "Cette transition a-t-elle besoin d'un d\u00e9clencheur ?",
                  "Le d\u00e9clencheur s'active-t-il trop fr\u00e9quemment ?",
                  "Le d\u00e9clencheur s'active-t-il trop rarement ?",
                },
                new String[] { "MGuard",
                  "Cette transition peut-elle \u00eatre franchie trop fr\u00e9quemment ?",
                  "Les conditions sur cette transition sont-elles trop restrictives ?",
                  "Cette transition peut-elle \u00eatre scind\u00e9e en deux transitions ou plus ?"
                },
                new String[] { "Actions",
                  "Cette transition devrait-elle \u00eatre associ\u00e9e \u00e0 une action ?",
                  "L'action de cette transition doit-elle \u00eatre une action de sortie ?",
                  "L'action de cette transition doit-elle \u00eatre une action d'entr\u00e9e ?",
                  "La pr\u00e9condition de cette action est-elle toujours v\u00e9rifi\u00e9e ?",
                  "La post-condition de cette action est-elle coh\u00e9rente avec la destination ?"
                }
            }
        },
        { "ChUseCase",
            new String[][] {
                new String[] { "Naming",
                  "Le nom \u00ab <ocl>self</ocl> \u00bb d\u00e9crit-il clairement la classe ?",
                  "\u00ab <ocl>self</ocl> \u00bb est-il un nom ou une phrase nominale ?",
                  "Le nom \u00ab <ocl>self</ocl> \u00bb pourrait-il \u00eatre mal interpr\u00e9t\u00e9 ou signifier quelque chose d'autre ?"
                },
                new String[] { "Encoding",
                  "<ocl>self</ocl> doit-elle \u00eatre sa propre classe ou un simple attribut d'une autre classe ?",
                  "La classe <ocl>self</ocl> fait-elle exactement une chose et la fait-elle bien ?",
                  "<ocl>self</ocl> pourrait-elle \u00eatre scind\u00e9e en deux classes ou plus ?"
                },
                new String[] { "Value",
                  "Tous les attributs de <ocl>self</ocl> ont-ils des valeurs initiales significatives ?",
                  "Pourriez-vous \u00e9crire un invariant pour cette classe ?",
                  "Tous les constructeurs \u00e9tablissent-ils l'invariant de classe ?",
                  "Toutes les op\u00e9rations maintiennent-elles l'invariant de classe ?"
                },
                new String[] { "Location",
                  "La classe <ocl>self</ocl> pourrait-elle \u00eatre positionn\u00e9e diff\u00e9remment dans la hi\u00e9rarchie de classes ?",
                  "Avez-vous pr\u00e9vu d'avoir des sous-classes de <ocl>self</ocl> ?",
                  "La classe <ocl>self</ocl> pourrait-elle \u00eatre \u00e9limin\u00e9e du mod\u00e8le ?",
                  "Existe-t-il une autre classe du mod\u00e8le qui pourrait \u00eatre revue ou \u00e9limin\u00e9e car elle a la m\u00eame fonction que <ocl>self</ocl> ?"
                },
                new String[] { "Updates",
                  "\u00c0 quelles occasions les instances de <ocl>self</ocl> doivent-elles \u00eatre mises \u00e0 jour ?",
                  "Y a-t-il d'autres objets qui doivent \u00eatre mis \u00e0 jour lorsque <ocl>self</ocl> est mise \u00e0 jour ?"
                }
            }

        },
        { "ChActor",
            new String[][] {
                new String[] { "Naming",
                  "Le nom \u00ab <ocl>self</ocl> \u00bb d\u00e9crit-il clairement la classe ?",
                  "\u00ab <ocl>self</ocl> \u00bb est-il un nom ou une phrase nominale ?",
                  "Le nom \u00ab <ocl>self</ocl> \u00bb pourrait-il \u00eatre mal interpr\u00e9t\u00e9 ou signifier quelque chose d'autre ?"
                },
                new String[] { "Encoding",
                  "<ocl>self</ocl> doit-elle \u00eatre sa propre classe ou un simple attribut d'une autre classe ?",
                  "La classe <ocl>self</ocl> fait-elle exactement une chose et la fait-elle bien ?",
                  "<ocl>self</ocl> pourrait-elle \u00eatre scind\u00e9e en deux classes ou plus ?"
                },
                new String[] { "Value",
                  "Tous les attributs de <ocl>self</ocl> ont-ils des valeurs initiales significatives ?",
                  "Pourriez-vous \u00e9crire un invariant pour cette classe ?",
                  "Tous les constructeurs \u00e9tablissent-ils l'invariant de classe ?",
                  "Toutes les op\u00e9rations maintiennent-elles l'invariant de classe ?"
                },
                new String[] { "Location",
                  "La classe <ocl>self</ocl> pourrait-elle \u00eatre positionn\u00e9e diff\u00e9remment dans la hi\u00e9rarchie de classes ?",
                  "Avez-vous pr\u00e9vu d'avoir des sous-classes de <ocl>self</ocl> ?",
                  "La classe <ocl>self</ocl> pourrait-elle \u00eatre \u00e9limin\u00e9e du mod\u00e8le ?",
                  "Existe-t-il une autre classe du mod\u00e8le qui pourrait \u00eatre revue ou \u00e9limin\u00e9e car elle a la m\u00eame fonction que <ocl>self</ocl> ?"
                },
                new String[] { "Updates",
                  "\u00c0 quelles occasions les instances de <ocl>self</ocl> doivent-elles \u00eatre mises \u00e0 jour ?",
                  "Y a-t-il d'autres objets qui doivent \u00eatre mis \u00e0 jour lorsque <ocl>self</ocl> est mise \u00e0 jour ?"
                }
            }

        }
    };

    public Object[][] getContents() {
        return _contents;
    }
}
