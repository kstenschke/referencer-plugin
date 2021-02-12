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
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.kstenschke.referencer.parsers.ParserJavaScript;
import com.kstenschke.referencer.utils.UtilsString;

import java.util.ArrayList;
import java.util.List;

class InsertOrCopyReferencerJavascript {

    /**
     * Parse document of given event for JavaScript references and return them
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

            // File path and name
            VirtualFile file = FileDocumentManager.getInstance().getFile(document);
            String filePath = (file != null) ? file.getPath() : "";

            // Add namespace
            String namespaceBefore = null;
            List<String> allNamespaces = ParserJavaScript.getAllNamespaceInText(textBeforeCaret);
            if (!allNamespaces.isEmpty()) {
                namespaceBefore = cleanupNamespaceName(allNamespaces.get(allNamespaces.size() - 1));
                referenceItems.add(namespaceBefore);
            }

            // Add classname
            String classnameBefore = null;
            List<String> allClassnames = ParserJavaScript.getAllClassNamesInText(textBeforeCaret);
            if (!allClassnames.isEmpty()) {
                classnameBefore = cleanupClassname(allClassnames.get(allClassnames.size() - 1));
                referenceItems.add(classnameBefore);
            }
            // Add namespace + classname
            String namespacedClassname = null;
            if (namespaceBefore != null && classnameBefore != null) {
                namespacedClassname = namespaceBefore + "." + classnameBefore;
                referenceItems.add(namespacedClassname);
            }

            // Add method before caret
            String methodBefore = null;
            List<String> allMethodsBeforeCaret = ParserJavaScript.getAllMethodsInText(textBeforeCaret);
            if (!allMethodsBeforeCaret.isEmpty()) {
                methodBefore = cleanupMethodName(allMethodsBeforeCaret.get(allMethodsBeforeCaret.size() - 1));
            }

            // Add method after caret
            String methodAfter = null;
            List<String> allMethodsAfterCaret = ParserJavaScript.getAllMethodsInText(textAfterCaret);
            if (!allMethodsAfterCaret.isEmpty()) {
                methodAfter = cleanupMethodName(allMethodsAfterCaret.get(0));
            }

            // Add namespace.classname.methodBefore
            // -or classname.methodBefore if no namespace found
            if (classnameBefore != null && methodBefore != null) {
                if (namespacedClassname != null) {
                    referenceItems.add(namespacedClassname + "." + methodBefore);
                }
                // Classname.method
                referenceItems.add(classnameBefore + "." + methodBefore);
            }

            if (null != methodBefore) {
                referenceItems.add(methodBefore);
            }
            if (null != methodAfter) {
                referenceItems.add(methodAfter);
            }

            // Convert path to namespace (slashes to dots)
            String namespaceFromFilepath = filePath.replace("/", ".").replaceFirst("\\.", "");
            namespaceFromFilepath = UtilsString.replaceLast(namespaceFromFilepath, ".js", "");
            referenceItems.add(namespaceFromFilepath);
        }

        return referenceItems;
    }

    /**
     * Clean up method name
     *
     * @param    methodName    Method name to be cleaned
     * @return Cleaned method name
     */
    private static String cleanupMethodName(String methodName) {
        String[] removeStrings = {"(", ":"};

        return UtilsString.cleanReference(methodName, "function", removeStrings, "();");
    }

    /**
     * Clean up namespace name
     *
     * @param    namespaceName    Namespace name to be cleaned
     * @return Cleaned namespace name
     */
    private static String cleanupNamespaceName(String namespaceName) {
        String[] removeEachStrs = {"\t"};

        return UtilsString.cleanReference(namespaceName, "@namespace", removeEachStrs);
    }

    /**
     * Clean up class name
     *
     * @param    className    Class name to be cleaned
     * @return Cleaned class name
     */
    private static String cleanupClassname(String className) {
        String[] removeEachStrs = {"\t", " ", "=", "("};
        className = UtilsString.cleanReference(className, "@class", removeEachStrs);

        className = className.replace("Class.create", "");

        return className.trim();
    }
}
