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

import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.kstenschke.referencer.Preferences;
import com.kstenschke.referencer.resources.StaticTexts;
import com.kstenschke.referencer.utils.UtilsEnvironment;
import com.kstenschke.referencer.utils.UtilsFile;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PluginConfiguration {

    private JTextArea textAreaGoToPatterns;
    private JPanel rootPanel;
    private JTextArea textAreaReplacePatterns;
    private JButton buttonImportSettings;
    private JButton buttonExportSettings;

    public PluginConfiguration() {
        textAreaGoToPatterns.setText(Preferences.getGoToPatterns());
        textAreaReplacePatterns.setText(Preferences.getReplacePatterns());

        buttonImportSettings.addActionListener(e -> importSettingsFromJson());
        buttonExportSettings.addActionListener(e -> exportSettingsToJson());
    }

    private void importSettingsFromJson() {
        Project project = UtilsEnvironment.getOpenProject();

        if (null == project) {
            UtilsEnvironment.notify(StaticTexts.NOTIFY_NO_PROJECT_OPEN);
            return;
        }

        String pathReferencerJson = getPathReferencerJson(project);
        File jsonFile = new File(pathReferencerJson);

        if (!jsonFile.exists()) {
            UtilsEnvironment.notify(StaticTexts.NOTIFY_REFERENCER_JSON_DOESNT_EXIST);
            return;
        }

        String jsonStr = UtilsFile.getFileContents(pathReferencerJson);

        try {
            StringBuilder gotoPatterns = new StringBuilder();
            StringBuilder replacePatterns = new StringBuilder();

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject)parser.parse(jsonStr);

            for (Object o : json.keySet()) {
                String key = (String) o;
                String value = (String) json.get(key);

                JSONObject innerJson = (JSONObject) parser.parse(value);

                for (Object item : innerJson.keySet()) {
                    String innerKey = (String) item;
                    String innerValue = (String) innerJson.get(key);

                    if (key.equals("goto")) {
                        if (gotoPatterns.length() == 0) {
                            gotoPatterns.append("\n");
                        }

                        gotoPatterns.append(innerKey).append("\t").append(innerValue);
                    } else {
                        if (replacePatterns.length() == 0) {
                            replacePatterns.append("\n");
                        }

                        replacePatterns.append(innerKey).append("\t").append(innerValue);
                    }
                }

                textAreaGoToPatterns.setText(gotoPatterns.toString());
                textAreaReplacePatterns.setText(replacePatterns.toString());

                UtilsEnvironment.notify(StaticTexts.NOTIFY_REFERENCER_JSON_LOADED);
            }
        } catch (ParseException e) {
            UtilsEnvironment.notify(StaticTexts.NOTIFY_REFERENCER_JSON_FAILED_PARSE);
        }
    }

    private void exportSettingsToJson() {
        Project project = UtilsEnvironment.getOpenProject();

        if (null == project) {
            UtilsEnvironment.notify(StaticTexts.NOTIFY_NO_PROJECT_OPEN);
            return;
        }

        String pathReferencerJson = getPathReferencerJson(project);
        File jsonFile = new File(pathReferencerJson);

        if ((jsonFile.exists() && jsonFile.setWritable(true)) && !jsonFile.delete()) {
            UtilsEnvironment.notify(StaticTexts.NOTIFY_REFERENCER_JSON_FAILED_SAVE);
            return;
        }

        try {
            FileWriter myWriter = new FileWriter(pathReferencerJson);
            myWriter.write("{\n"
                + "    \"goto\": " + Preferences.getGoToPatternsAsJson() +",\n"
                + "    \"replace\": " + Preferences.getReplacePatternsAsJson() + "\n}");
            myWriter.close();

            UtilsEnvironment.notify(StaticTexts.NOTIFY_REFERENCER_JSON_SAVED);
        } catch (IOException exception) {
            UtilsEnvironment.notify(StaticTexts.NOTIFY_REFERENCER_JSON_FAILED_SAVE);
        }
    }

    @NotNull
    private String getPathReferencerJson(Project project) {
        return ModuleRootManager.getInstance(
            ModuleManager.getInstance(project).getModules()[0]
        ).getContentRoots()[0].getPath() + "/referencer.json";
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
