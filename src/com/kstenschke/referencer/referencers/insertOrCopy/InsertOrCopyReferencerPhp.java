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
import com.intellij.openapi.project.Project;
import com.kstenschke.referencer.parsers.ParserPhp;
import com.kstenschke.referencer.resources.StaticTexts;
import com.kstenschke.referencer.utils.UtilsString;

import java.util.ArrayList;
import java.util.List;

class InsertOrCopyReferencerPhp {

    /**
     * Parse document of given event for PHP references and return them
     *
     * @param    e    Action system event
     * @return List of PHP items
     */
    public static List<String> getReferenceItems(AnActionEvent e) {
        List<String> referenceItems = new ArrayList<>();

        final Project project = e.getData(PlatformDataKeys.PROJECT);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        if (project != null && editor != null) {
            final Document document = editor.getDocument();

            // Get line number the caret is in
            int caretOffset = editor.getCaretModel().getOffset();
            String textFull = document.getText();
            String textBeforeCaret = textFull.substring(0, caretOffset);
            String textAfterCaret = textFull.substring(caretOffset);

            // Add class before caret
            String classBefore = null;
            List<String> allClassBeforeCaret = ParserPhp.getAllClassNamesInText(textBeforeCaret);
            if (!allClassBeforeCaret.isEmpty()) {
                classBefore = cleanupClassName(allClassBeforeCaret.get(allClassBeforeCaret.size() - 1));
                referenceItems.add(classBefore);
            }
            // Add class after caret
            String classAfter;
            List<String> allClassAfterCaret = ParserPhp.getAllClassNamesInText(textAfterCaret);
            if (!allClassAfterCaret.isEmpty()) {
                classAfter = cleanupClassName(allClassAfterCaret.get(0));
                referenceItems.add(classAfter);
            }
            // Add item for all methods in file, newline-separated
            referenceItems.add(StaticTexts.POPUP_ITEM_METHODS_IN_FILE);

            // Add method before caret
            String methodBefore = null;
            List<String> allMethodsBeforeCaret = ParserPhp.getAllMethodsInText(textBeforeCaret);
            if (!allMethodsBeforeCaret.isEmpty()) {
                methodBefore = cleanupMethodName(allMethodsBeforeCaret.get(allMethodsBeforeCaret.size() - 1));
                referenceItems.add(methodBefore);
            }
            // Add method after caret
            String methodAfter;
            List<String> allMethodsAfterCaret = ParserPhp.getAllMethodsInText(textAfterCaret);
            if (!allMethodsAfterCaret.isEmpty()) {
                methodAfter = cleanupMethodName(allMethodsAfterCaret.get(0));
                referenceItems.add(methodAfter);
            }
            // Add method before caret / prev. variable
            String varBefore = null;
            List<String> allVarsBeforeCaret = ParserPhp.getAllVariablesInText(textBeforeCaret);
            if (!allVarsBeforeCaret.isEmpty()) {
                varBefore = allVarsBeforeCaret.get(allVarsBeforeCaret.size() - 1);
                if (methodBefore != null && varBefore != null) {
                    referenceItems.add(methodBefore + " / " + varBefore);
                }
            }

            // Add classBefore::methodBefore caret
            if (classBefore != null && methodBefore != null) {
                referenceItems.add(classBefore + "::" + methodBefore);
            }

            // Add variable before caret
            if (varBefore != null) {
                referenceItems.add(varBefore);
            }
            // Add variable after caret
            String varAfter;
            List<String> allVarsAfterCaret = ParserPhp.getAllVariablesInText(textAfterCaret);
            if (!allVarsAfterCaret.isEmpty()) {
                varAfter = allVarsAfterCaret.get(0);
                if (!varAfter.equals(varBefore)) {
                    referenceItems.add(varAfter);
                }
            }
        }

        return referenceItems;
    }

    /**
     * Clean up method name
     *
     * @return Cleaned method name
     * @param    methodName    Method name to be cleaned
     */
    private static String cleanupMethodName(String methodName) {
        String[] removeEachStrs = {"("};

        return UtilsString.cleanReference(methodName, "function", removeEachStrs, "();");
    }

    /**
     * Clean up method name
     *
     * @param    className    Class name to be cleaned
     * @return Cleaned class name
     */
    private static String cleanupClassName(String className) {
        return UtilsString.cleanReference(className, "class");
    }
}
