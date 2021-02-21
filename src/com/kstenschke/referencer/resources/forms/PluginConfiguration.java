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
package com.kstenschke.referencer.resources.forms;

import com.kstenschke.referencer.Preferences;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class PluginConfiguration {

    private JTextArea textAreaGoToPatterns;
    private JPanel rootPanel;
    private JTextArea textAreaReplacePatterns;

    public PluginConfiguration() {
        textAreaGoToPatterns.setText(Preferences.getGoToPatterns());
        textAreaReplacePatterns.setText(Preferences.getReplacePatterns());
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public boolean isModified() {
        return !getGoToPatterns().equals(Preferences.getGoToPatterns())
            || !getReplacePatterns().equals(Preferences.getReplacePatterns());
    }

    public String getGoToPatterns() {
        return getPatternTrimmedFromTextarea(textAreaGoToPatterns);
    }

    public String getReplacePatterns() {
        return getPatternTrimmedFromTextarea(textAreaReplacePatterns);
    }

    @NotNull
    private String getPatternTrimmedFromTextarea(JTextArea textArea) {
        String text = textArea.getText();
        String trimmed = text.trim();

        if (!trimmed.equals(text)) {
            int caretPosition = textArea.getCaretPosition();
            textArea.setText(text);
            textArea.setCaretPosition(caretPosition);
        }

        return trimmed;
    }
}
