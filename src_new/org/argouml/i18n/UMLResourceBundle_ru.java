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
 *   This class provides strings for UML related PropPanels in Russian.
 *
 *   @author Alexey Aphanasyev (Alexey@tigris.org)
 *   @see org.argouml.i18n.UMLResourceBundle
 */
public class UMLResourceBundle_ru extends ListResourceBundle {

    private static final Object[][] _contents = {
        { "Add_Menu_Actor" , "Актант..." } ,
        { "Add_Menu_Class" , "Класс..." } ,
        { "Add_Menu_Datatype" , "Тип данных..." } ,
        { "Add_Menu_Exception" , "Исключение..." } ,
        { "Add_Menu_Interface" , "Интерфейс..." } ,
        { "Add_Menu_Signal" , "Сигнал..." } ,
        { "Add_Menu_UseCase" , "Вариант использования..." },
        { "Add association" , "Добавить ассоциацию" },
        { "Add attribute" , "Добавить атрибут" },
        { "Add datatype" , "Добавить тип данных" },
        { "Add inner class" , "Добавить внутренний класс" },
        { "Add operation" , "Добавить операцию" },
        { "Add parameter" , "Добавить параметр" },
        { "Add use case" , "Добавить вариант использования" },
        { "Association:" , "Ассоциация:" },
        { "Associations:" , "Ассоциации:" },
        { "Attributes:" , "Атрибуты:" },
        { "Base Class:" , "Базовый класс:" },
        { "Class" , "Класс" },
        { "changeable" , "изменяемый" },
        { "Components:" , "Компоненты:" },
        { "Connections:" , "Связи:" },
        { "Delete actor" , "Удалить актант" },
        { "Delete association" , "Удалить ассоциацию" },
        { "Delete association end" , "Удалить полюс ассоциации" },
        { "Delete attribute" , "Удалить атрибут" },
        { "Delete class" , "Удалить класс" },
        { "Delete interface" , "Удалить интерфейс" },
        { "Delete operation" , "Удалить операцию" },
        { "Delete package" , "Удалить пакет" },
        { "Delete parameter" , "Удалить параметр" },
        { "Derived:" , "Производные:" },
        { "Expression:" , "Выражение:" },
        { "Extends:" , "Расширяет:" },
        { "Go back" , "Назад" },
        { "Go forward" , "Вперед" },
        { "Implements:" , "Реализует:" },
        { "Incoming:" , "Входящие:" },
        { "Language:" , "Язык:" },
        { "Literals:" , "Литералы:" },
        { "Multiplicity:" , "Множественность:" },
        { "Name:" , "Имя:" },
        { "Namespace:" , "Пространство имен:" },
        { "New actor" , "Новый актант" },
        { "New association" , "Новая ассоциация" },
        { "New attribute" , "Новый атрибут" },
        { "New class" , "Новый класс" },
        { "New data type" , "Новый тип данных" },
        { "New interface" , "Новый интерфейс" },
        { "New operation" , "Новая операция" },
        { "New signal" , "Новый сигнал" },
        { "none" , "ничто" },
        { "Operations:" , "Операции:" },
        { "Ordering:" , "Упорядоченность:" },
        { "Outgoing:" , "Исходящие:" },
        { "Owner:" , "Владелец:" },
        { "Receiver:" , "Получатель:" },
        { "Receives:" , "Получает:" },
        { "Sends:" , "Посылает:" },
        { "sorted" , "отсортированно" },
        { "Transition" , "Переход" },
        { "Trigger:" , "Триггер:" },
        { "Type:" , "Тип:" },
        { "Visibility:" , "Видимость:" },
        {"public", "public" },
        {"abstract", "abstract" },
        {"final", "final" },
        {"root", "root" },
        {"Stereotype:", "Стереотип:" },
        {"Modifiers:", "Модификаторы:" },
        {"active", "active" },
        {"Owned Elements:", "Принадлежащие элементы:" },
        {"Go up", "Вверх" },
        {"Add generalization", "Добавить обобщение" },  
        {"Add realization", "Add реализацию" },  
        {"Owned Elements", "Принадлежащие элементы" },  
        {"Add class", "Добавсить класс" },  
        {"Add interface", "Добавить интерфейс" },  
        {"Add stereotype", "Добавить стереотип" },  
        {"Add actor", "Добавить актант" },  
        {"Add subpackage", "Добавить субпакет" },  
        {"Abstract", "Абстракный" },  
        {"Final", "Конечный" },  
        {"Root", "Корневой" },  
        {"Association Ends:", "Полюса ассоциации:" },  
        {"Add association end", "Добавить полюс ассоциации" },  
        {"Public", "Публичный" },  
        {"Implementations:", "Реализации:" },  
        {"New stereotype", "Новый стереотип" },  
        {"Add enumeration literal", "Добавить перечисление" },  
        {"NavStereo", "Перейти к выбранному стереотипу" },
        {"NavClass", "Перейти к выбранному классификатору" }
    };

    public Object[][] getContents() {
        return _contents;
    }


}
