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

/** Russian Resource bundle for internationalization of menu
 *
 *   @author Alexey Aphanasyev (Alexey@tigris.org)
 *   @see org.argouml.i18n.MenuResourceBundle
 */
public class MenuResourceBundle_ru extends ListResourceBundle {


   static final Object[][] _contents = {
        {"New", "Новый проект" },
        {"Open Project...", "Открыть проект..." },
        {"Save Project", "Сохранить проект" },
        {"Load model from DB", "Загрузить модель из БД" },
        {"Store model to DB", "Сохранить модель в БД" },
        {"Save Project As...", "Сохранить проект как..." },
        {"Import", "Импортировать" },
        {"Import sources...", "Импортировать исходный код..." },
        {"Print...", "Печатать..." },
        {"Save GIF...", "Сохранить как GIF..." },
        {"Save Graphics...", "Сохранить диаграмы как графику..." },
	{"Save Configuration", "Сохранить конфигурацию"},
        {"Exit", "Выход" },
        {"Undo", "Отменить" },
        {"Redo", "Повторить" },
        {"Cut", "Вырезать" },
        {"Copy", "Копировать" },
        {"Paste", "Вставить" },
	{"Settings...", "Установки..."},
        {"Set Source Path...", "Set Source Path..." },
        {"Delete From Diagram", "Удалить из диаграммы" },
        {"Erase From Model", "Удалить из модели" },
        {"Empty Trash", "Уничтожить мусор" },
        {"Navigate Back", "Продвинуться вперед" },
        {"Navigate Forward", "Вернуться назад" },
        {"NavConfig", "Конфигурирование Навигации" },
        {"Find...", "Найти..." },
        {"Goto Diagram...", "Перейти к диаграме..." },
        {"Next Editing Tab", "Следующая закладка редактирования" },
        {"Next Details Tab", "Следующая закладка деталей" },
        {"Buttons on Selection", "Buttons on Selection" },
        {"Create Multiple...", "Create Multiple..." },
        {"Add Top-Level Package", "Добавить пакет верхнего уровня" },
        {"ClassDiagram", "Диаграмма Классов" },
        {"UseCaseDiagram", "Диаграмма Вариантов использования" },
        {"StateDiagram", "Диаграмма состояний" },
        {"ActivityDiagram", "Диаграмма деятельности" },
        {"CollaborationDiagram", "Диаграмма коопераций" },
        {"DeploymentDiagram", "Диаграмма развертывания" },
        {"SequenceDiagram", "Диаграмма последовательности" },
        {"button.add-attribute", "Добавить атрибут" },
        {"button.add-operation", "Добавить операцию" },
        {"Add Message", "Добавить сообщение" },
        {"Add Internal Transition", "Добавить внутренний переход" },
        {"Generate Selected Classes", "Сгенерировать выбранные классы..." },
        {"Generate All Classes", "Сгенерировать все классы..." },
        {"Generate Code for Project", "Generate Code for Project..." },
        {"Toggle Auto-Critique", "Включить автокритику" },
        {"Design Issues...", "Спорные моменты проектирования..." },
        {"Design Goals...", "Цели проектирования..." },
        {"Browse Critics...", "Просмотр критических замечаний..." },
        {"Toggle Flat View", "Включить плоский вид" },
        {"New To Do Item...", "Новое задание..." },
        {"Resolve Item...", "Решить задание..." },
        {"Send Email To Expert...", "Послать письмо эксперту..." },
        {"More Info...", "Доп. информация..." },
        {"Snooze Critic", "Усыпить критику" },
        {"About Argo/UML", "Коротко об ArgoUML..." },
        {"Properties", "Свойства" },
        {"1", "1" },
        {"0..1", "0..1" },
        {"0..*", "0..*" },
        {"1..*", "1..*" },
        {"aggregate", "агрегат" },
        {"composite", "композитный" },
        {"none", "пусто" },
        {"Show Attribute Compartment", "Показать раздел атрибутов" },
        {"Hide Attribute Compartment", "Спрятать раздел атрибутов" },
        {"Show Operation Compartment", "показать раздел операций" },
        {"Hide Operation Compartment", "Спрятать раздел операций" },
        {"Show All Compartments", "Показать все разделы" },
        {"Hide All Compartments", "Спрятать все разделы" },
        {"File", "Файл" },
        {"Mnemonic_File", "F" },
        {"Mnemonic_New", "N" },
        {"Mnemonic_Open", "O" },
        {"Mnemonic_Save", "S" },
        {"Mnemonic_SaveAs", "A" },
        {"Mnemonic_Print", "P" },
        {"Mnemonic_SaveGraphics", "G" },
        {"Mnemonic_Exit", "X" },
        {"Edit", "Редактировать" },
        {"Mnemonic_Edit", "E" },
        {"Select", "Выбрать" },
        {"Mnemonic_Cut", "X" },
        {"Mnemonic_Copy", "C" },
        {"Mnemonic_Paste", "V" },
        {"Mnemonic_RemoveFromDiagram", "R" },
        {"Mnemonic_DeleteFromModel", "D" },
        {"View", "Просмотр" },
        {"Zoom", "Масштаб" },
        {"Mnemonic_View", "V" },
        {"Editor Tabs", "Закладки редактора" },
        {"Details Tabs", "Закладки деталей" },
        {"Create", "Создать" },
        {"Mnemonic_Create", "C" },
        {"Diagrams", "Диаграммы" },
        {"Create Diagram", "Создать диаграмму" },
        {"Arrange", "Расставить" },
        {"Mnemonic_Arrange", "A" },
        {"Align", "Выровнять" },
        {"Distribute", "Распределить" },
        {"Reorder", "Реорганизовать" },
        {"Nudge", "Подтолкнуть" },
        {"Layout", "Компоновка" },
        {"Generation", "Генерирование" },
        {"Mnemonic_Generate", "G" },
        {"Critique", "Критика" },
        {"Mnemonic_Critique", "R" },
        {"Help", "Помощь" },
        {"Mnemonic_Help", "H" },
        {"Tools", "Инструменты" },
        {"Automatic", "Автоматически" },
        {"Incremental", "Инкрементно" },
        {"As Diagram", "Как диаграмма" },
        {"As Table", "Как таблица" },
        {"As Metrics", "Как метрика" },
        {"ToDoItem", "Сделать" },
        {"Javadocs", "Javadocs" },
        {"Source", "Исходный код" },
        {"Constraints", "Ограничения" },
        {"TaggedValues", "Именованные значения" },
        {"Checklist", "Checklist" },
        {"History", "История" },

        { "Shortcut_New", KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK) },
        { "Shortcut_Open", KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK) },
        { "Shortcut_Save", KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK) },
        { "Shortcut_Print", KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK) },
        { "Shortcut_Select_All", KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK) },
        { "Shortcut_Copy", KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK) },
        { "Shortcut_Paste", KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK) },
        { "Shortcut_Cut", KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK) },
        { "Shortcut_Remove_From_Diagram", KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK) },
        { "Shortcut_Find", KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0) },
        { "Shortcut_Generate_All", KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0) },
        { "Shortcut_Exit", KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK) },
        { "Shortcut_Delete", KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0)}

   };

     public Object[][] getContents() {
        return _contents;
     }
}
