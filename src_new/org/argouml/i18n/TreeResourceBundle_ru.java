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
import javax.swing.*;
import java.awt.event.*;


/** Russian Resource bundle
 *
 *   @author Alexey Aphanasyev (Alexey@tigris.org)
 *   @see org.argouml.i18n.TreeResourceBundle
 */
public class TreeResourceBundle_ru extends ListResourceBundle {


   static final Object[][] _contents = {
        { "Package-centric", "По пакетам" },
        { "Diagram-centric", "По диаграммам" },
        { "Inheritance-centric", "По наследованию" },
        { "Class Associations", "Ассоциации классов" },
        { "Navigable Associations", "Ассоциации, допускающие навигацию" },
        { "Association-centric", "По ассоциациям" },
        { "Aggregate-centric", "По агрегатам" },
        { "Composite-centric", "По композициям" },
        { "Class states", "Состояния класса" },
        { "State-centric", "По состояниям" },
        { "State-transitions", "Состояние-переходы" },
        { "Transitions-centric", "По переходам" },
        { "Transitions paths", "Пути переходов" },
        { "UseCase-centric", "UseCase-centric" },
        { "Dependency-centric", "По зависимостям" },
        { "Features of Class", "Черты класса" },
        { "Methods of Class", "Методы класса" },
        { "Attributes of Class", "Атрибуты класса" },
        { "States of Class", "Состояния класса" },
        { "Transitions of Class", "Переходы класса" },

        { "Package->Subpackages", "Пакет->Подпакеты" },
        { "Package->Classifiers", "Пакет->Классификаторы" },
        { "Package->Associations", "Пакет->Ассоциации" },
        { "Package->Instances", "Пакет->Экземпляры" },
        { "Package->Links", "Пакет->Связи" },
        { "Package->Collaborations", "Пакет->Кооперации" },
        { "State Machine->Final States", "Конечный автомат->Конечные состояния" },
        { "State Machine->Initial States", "Конечный автомат->Исходные состояния" },
        { "State->Final Substates", "Состояние->Конечные подсостояния" },
        { "State->Initial Substates", "Состояние->Исходные подсотояния" },

        { "Namespace->Owned Element", "Пространство имен->Принадлежащие элементы" },
        { "Project->Package", "Проект->Пакет" },
        { "Package->Diagram", "Пакет->Диаграмма" },
        { "Class->Attribute", "Класс->Атрибут" },
        { "Class->Operation", "Класс->Операция" },
        { "Diagram->Edge", "Диаграмма->Бордюр" },
        { "Package->Base Class", "Пакет->Базовый класс" },
        { "Element->Dependent Element", "Элемент->Зависимый элемент" },
        { "Class->State Machine", "Класс->Конечный автомат" },
        { "Element->Required Element", "Элемент->Необходимый элемент" },
        { "Class->Subclass", "Класс->Подкласс" },
        { "Interaction->Messages", "Взаимодействие->Сообщения" },
        { "Project->Diagram", "Проект->Диаграмма" },
        { "Link->Stimuli", "Связь->Стимулы" },
        { "Stimulus->Action", "Стимул->Действие" },
	{ "Properties", "Свойства" },
	{ "Add to Diagram", "Добавить в диаграмму"},
	{ "Click on diagram to add ", "Щелкние по диаграме чтобы добавить "},

	// For the ToDoItem tree
	{ "todo.perspective.type", "По виду знания" },
	{ "todo.perspective.decision", "По решению" },
	{ "todo.perspective.offender", "По обидчику" },
	{ "todo.perspective.priority", "По приоритету" },
	{ "todo.perspective.goal", "По цели" },
	{ "todo.perspective.poster", "По афише" }
   };

     public Object[][] getContents() {
        return _contents;
     }

}
