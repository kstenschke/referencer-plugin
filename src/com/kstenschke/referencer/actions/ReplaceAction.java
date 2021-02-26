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
package com.kstenschke.referencer.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.kstenschke.referencer.Preferences;
import com.kstenschke.referencer.resources.StaticTexts;
import com.kstenschke.referencer.utils.UtilsEnvironment;
import com.kstenschke.referencer.utils.UtilsString;
import org.jetbrains.annotations.NotNull;

public class ReplaceAction extends AnAction {

    public void actionPerformed(final AnActionEvent e) {
        final Project project = e.getData(PlatformDataKeys.PROJECT);
        final Editor editor = e.getData(PlatformDataKeys.EDITOR);

        if (project == null || editor == null) {
            return;
        }

        final String replacePatterns = Preferences.getReplacePatterns();

        if (replacePatterns.isEmpty()) {
            UtilsEnvironment.notify(StaticTexts.NOTIFY_REPLACE_NONE_CONFIGURED);
            return;
        }

        final Document document = editor.getDocument();
        final String documentText = document.getText();
        final int amountLines = documentText.split("\n").length;

        CommandProcessor.getInstance().executeCommand(project, () -> {      /* Replace undoable */
            String replacedText = documentText;
            String[] replaceTuples = replacePatterns.split("\n");
            int amountReplaced = 0;

            for (String replaceTuple : replaceTuples) {
                if (!replaceTuple.contains("\t")) {
                    continue;
                }

                String[] parts = replaceTuple.split("\t");
                int amountOccurrences = UtilsString.regexCount(replacedText, parts[0]);
                if (amountOccurrences == 0) {
                    continue;
                }

                replacedText = replacedText.replaceAll(parts[0], parts[1]);
                amountReplaced += amountOccurrences;
            }

            final String replaceText = replacedText;
            WriteCommandAction.runWriteCommandAction(project, () -> document.setText(replaceText));

            final int amountLinesAfterReplace = replacedText.split("\n").length;
            UtilsEnvironment.notify(renderSuccessMessage(amountLines, amountReplaced, amountLinesAfterReplace));
            editor.getContentComponent().grabFocus();
        }, null, null);
    }

    @NotNull
    private String renderSuccessMessage(int amountLines, int amountReplaced, int amountLinesAfterReplace) {
        String message = "Replaced " + amountReplaced + " strings";

        if (amountLines > amountLinesAfterReplace) {
            return message + ",\nreduced code by " + (amountLines - amountLinesAfterReplace) + " lines.";
        }
        if (amountLines < amountLinesAfterReplace) {
            return message + ",\nreformatting added " + (amountLinesAfterReplace - amountLines) + " lines.";
        }

        return message + ".";
    }
}
