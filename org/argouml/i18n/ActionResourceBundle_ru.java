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

/** Russian resource bundle
 *
 *   @author Alexey Aphanasyev (Alexey@tigris.org)
 *   @see org.argouml.i18n.ActionResourceBundle
 */
public class ActionResourceBundle_ru extends ListResourceBundle {

  static final Object[][] _contents = {
    {"template.save_project.confirm_overwrite", "Файл \"{0\"} уже существует. Перезаписать?"},
    {"template.save_project.status_writing", "Записывается файл \"{0\"}..."},
    {"template.save_project.status_wrote", "Запись файла \"{0}\" завершена."},
    {"template.save_project.file_not_found",
       "Файл \"{0}\" не найден.\n" +
       "Файл возможно поврежден."},
    {"template.save_project.io_exception",
       "Ошибка при попытке записать файл \"{0}\".\n" +
       "Файл возможно поврежден."},
    {"text.save_project.confirm_overwrite_title", "Файл уже существует"},
    {"text.save_project.file_not_found_title", "Файл не найден"},
    {"text.save_project.io_exception_title", "Ошибка во время созранения файла"},
    {"text.save_as_project.unstable_release",
       "Это отладочный выпуск ArgoUML, предназначенный только для разработчиков. \n" +
       "Не рекомендуется использовать его для производственных целей, так как \n" +
       "сохраненные Вами модели возможно не смогут быть прочитаны будущими \n" +
       "выпусками ArgoUML. Для производственных целей используйте \"устойчивый\"\n" +
       "(stable) выпуск, который вы можете найти на нашем сайте во всемирной \n" +
       "паутине: www.argouml.org\n" +
       "Благодарим Вас за интерес, проявленный к нашему продутку."},
    {"text.save_as_project.unstable_release_title", "Внимание!!!"},
    {"text.save_as_project.chooser_title", "Сохранить проект: "},
    {"template.new_project.save_changes_to", "Сохранить изменения в {0}?"},
    {"template.open_project.save_changes_to", "Сохранить изменения в {0}?"},
    {"text.open_project.chooser_title", "Открыть проект"},
    {"template.open_project.status_read", "Читать {0}."},
    {"text.remove_from_model.will_remove_from_diagrams",
       "\nЭто будет удалено из диаграмм."},
    {"text.remove_from_model.will_remove_subdiagram",
       "\nПоддиаграммы также будут удалены."},
    {"text.remove_from_model.anon_element_name", "этот элемент"},
    {"template.remove_from_model.confirm_delete", "Вы уверены что хотите удалить {0}?{1}"},
    {"text.remove_from_model.confirm_delete_title", "Вы уверены?"},
    {"template.exit.save_changes_to", "Сохранить изменения в {0}?"}
  };

  public Object[][] getContents() {
    return _contents;
  }
}
