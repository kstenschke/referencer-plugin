/*
 * Copyright Kay Stenschke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kstenschke.referencer.referencers.insertOrCopy;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.kstenschke.referencer.parsers.ParserPhp;
import com.kstenschke.referencer.resources.StaticTexts;
import com.kstenschke.referencer.utils.UtilsArray;
import com.kstenschke.referencer.utils.UtilsFile;
import com.kstenschke.referencer.utils.UtilsString;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class InsertOrCopyReferencer {

    /**
     * @param e Action system event
     * @return Items of the events context
     */
    @Nullable public static String[] getItems(AnActionEvent e) {
        final Project project = e.getData(PlatformDataKeys.PROJECT);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        if (project == null || editor == null) {
            return null;
        }

        final Document document = editor.getDocument();

        /* File path and name */
        VirtualFile file = FileDocumentManager.getInstance().getFile(document);
        String filePath = file != null ? file.getPath() : "";
        String fileExtension = UtilsFile.getExtensionByDocument(document);

        int caretOffset = editor.getCaretModel().getOffset();                   /* Get line number the caret is in */

        SelectionModel selectionModel = editor.getSelectionModel();
        int selStart = selectionModel.getSelectionStart();
        int selEnd = selectionModel.getSelectionEnd();

        int lineSelStart = document.getLineNumber(selStart);
        int lineSelEnd = document.getLineNumber(selEnd);

        /* Grab the "word" to the left of the caret - all java identifier characters */
        String wordLeftOfCaret = UtilsString.getWordLeftOfOffset(document.getCharsSequence(), caretOffset);
        /* Grab the "string" to the left of the caret - all non white-space characters */
        String stringLeftOfCaret = UtilsString.getStringLeftOfOffset(document.getCharsSequence(), caretOffset);

        /* Setup list of items */
        List<String> referenceItems = new ArrayList<>();

        /* Add date/timestamps */
        referenceItems.add(StaticTexts.POPUP_SECTION_TITLE_DATE_TIME);
        referenceItems.addAll(InsertOrCopyReferencerDateTime.getReferenceItems());

        /* Add file / path items */
        referenceItems.add(StaticTexts.POPUP_SECTION_TITLE_FILES_PATHS);
        referenceItems.addAll(InsertOrCopyReferencerFilesFolders.getReferenceItems(e));

        /* Add selection info */
        if (selectionModel.hasSelection() && lineSelEnd > lineSelStart) {
            referenceItems.add(filePath + " / Selection: " + (lineSelStart + 1) + " - " + (lineSelEnd + 1));
        }

        if (UtilsFile.isJavaScriptFileExtension(fileExtension)) {                   /* Add JavaScript items */
            referenceItems.add(StaticTexts.POPUP_SECTION_TITLE_JAVASCRIPT);
            referenceItems.addAll(InsertOrCopyReferencerJavascript.getReferenceItems(e));
        } else if (UtilsFile.isPhpFileExtension(fileExtension)) {                   /* Add PHP items */
            referenceItems.add(StaticTexts.POPUP_SECTION_TITLE_PHP);
            referenceItems.addAll(InsertOrCopyReferencerPhp.getReferenceItems(e));
        }

        /* Add all line-parts in current document that begin the same as the word at the caret */
        if (wordLeftOfCaret != null && !wordLeftOfCaret.isEmpty() && wordLeftOfCaret.length() > 1
         || (stringLeftOfCaret != null && !stringLeftOfCaret.isEmpty() && stringLeftOfCaret.length() > 2)
        ) {
            String docText = document.getText();
            String[] wordLineParts = null;
            if (wordLeftOfCaret != null && !wordLeftOfCaret.isEmpty()) {
                wordLineParts = docText.split(wordLeftOfCaret);
            }

            String[] allLineParts;
            if (stringLeftOfCaret != null && !stringLeftOfCaret.isEmpty()) {
                String[] stringLineParts = docText.split(Pattern.quote(stringLeftOfCaret));
                allLineParts = UtilsArray.merge(wordLineParts, stringLineParts);
            } else {
                allLineParts = wordLineParts;
            }

            if (allLineParts != null && allLineParts.length > 0) {
                List<String> listLineParts = Arrays.asList(allLineParts);
                addReferenceItems(referenceItems, listLineParts);
            }
        }

        return referenceItems.toArray(new String[0]);
    }

    private static void addReferenceItems(List<String> referenceItems, List<String> listLineParts) {
        int count = 0;
        for (String listLinePart : listLineParts) {
            if (count > 0) {
                listLinePart = listLinePart.split("\\n")[0];

                if (listLinePart.length() > 1) {
                    listLinePart = listLinePart.substring(1);

                    if (listLinePart.trim().length() > 1 && !referenceItems.contains(listLinePart)) {
                        if (!referenceItems.contains(StaticTexts.POPUP_SECTION_TITLE_TEXT_COMPLETIONS)) {
                            referenceItems.add(StaticTexts.POPUP_SECTION_TITLE_TEXT_COMPLETIONS);
                        }

                        referenceItems.add(listLinePart);
                    }
                }
            }
            count++;
        }
    }

    /**
     * Process given reference item - if it's a type description "translate" to the respective value
     *
     * @param project    IDEA project
     * @param itemString Selected references list item value
     * @return String to be inserted/copied
     */
    public static String fixReferenceValue(Project project, Editor editor, String itemString) {
        if (StaticTexts.POPUP_ITEM_METHODS_IN_FILE.equals(itemString)) {
            List<String> methods = ParserPhp.getAllMethodsInText(editor.getDocument().getText());
            String methodsList = StringUtils.join(methods, "\n");

            return methodsList.replaceAll("function ", "").replaceAll("\\(", "").replaceAll(" ", "");
        }
        return StaticTexts.POPUP_ITEM_OPEN_FILES.equals(itemString)
                ? InsertOrCopyReferencerFilesFolders.getOpenFiles(FileEditorManager.getInstance(project))
                : itemString;
    }

}
