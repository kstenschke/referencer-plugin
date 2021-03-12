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

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.kstenschke.referencer.Preferences;
import com.kstenschke.referencer.resources.StaticTexts;
import com.kstenschke.referencer.utils.UtilsEnvironment;
import com.kstenschke.referencer.utils.UtilsFile;
import org.jetbrains.annotations.NotNull;

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
    private JCheckBox checkboxListBookmarks;
    private JCheckBox checkboxListPhpMethods;
    private JCheckBox checkboxListJsMethods;

    public PluginConfiguration() {
        checkboxListBookmarks.setSelected(Preferences.getShowBookmarksInGoToList());
        checkboxListPhpMethods.setSelected(Preferences.getShowPhpMethodsInGoToList());
        checkboxListJsMethods.setSelected(Preferences.getShowJsMethodsInGoToList());

        textAreaGoToPatterns.setText(Preferences.getGoToPatterns());
        textAreaReplacePatterns.setText(Preferences.getReplacePatterns());

        buttonImportSettings.addActionListener(e -> importReplacePatterns());
        buttonExportSettings.addActionListener(e -> exportReplacePatterns());
    }

    private void importReplacePatterns() {
        Project project = UtilsEnvironment.getOpenProject();

        if (null == project) {
            UtilsEnvironment.notify(StaticTexts.NOTIFY_NO_PROJECT_OPEN);
            return;
        }

        String pathReferencerPatternsTxt = getPathReferencerPatternsTxt(project);
        File patternsFile = new File(pathReferencerPatternsTxt);

        if (!patternsFile.exists()) {
            UtilsEnvironment.notify(StaticTexts.NOTIFY_REFERENCER_TXT_DOESNT_EXIST);
            return;
        }

        String replacePatterns = UtilsFile.getFileContents(pathReferencerPatternsTxt);
        textAreaReplacePatterns.setText(replacePatterns);
        UtilsEnvironment.notify(StaticTexts.NOTIFY_REFERENCER_TXT_LOADED);
    }

    private void exportReplacePatterns() {
        Project project = UtilsEnvironment.getOpenProject();

        if (null == project) {
            UtilsEnvironment.notify(StaticTexts.NOTIFY_NO_PROJECT_OPEN);
            return;
        }

        String pathReferencerPatternsTxt = getPathReferencerPatternsTxt(project);
        File patternsFile = new File(pathReferencerPatternsTxt);

        if ((patternsFile.exists() && patternsFile.setWritable(true)) && !patternsFile.delete()) {
            UtilsEnvironment.notify(StaticTexts.NOTIFY_REFERENCER_TXT_FAILED_SAVE);
            return;
        }

        try {
            String replacePatterns = PropertiesComponent.getInstance().getValue(Preferences.PROPERTY_PATTERNS_REPLACE);
            if (null == replacePatterns) {
                UtilsEnvironment.notify(StaticTexts.NOTIFY_REFERENCER_NO_REPLACE_PATTERNS);
                return;
            }

            FileWriter myWriter = new FileWriter(pathReferencerPatternsTxt);
            myWriter.write(replacePatterns);
            myWriter.close();

            UtilsEnvironment.notify(StaticTexts.NOTIFY_REFERENCER_TXT_SAVED);
        } catch (IOException exception) {
            UtilsEnvironment.notify(StaticTexts.NOTIFY_REFERENCER_TXT_FAILED_SAVE);
        }
    }

    @NotNull
    private String getPathReferencerPatternsTxt(Project project) {
        return ModuleRootManager.getInstance(
            ModuleManager.getInstance(project).getModules()[0]
        ).getContentRoots()[0].getPath() + "/referencer_patterns.txt";
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public boolean isModified() {
        return !getGoToPatterns().equals(Preferences.getGoToPatterns())
            || !getReplacePatterns().equals(Preferences.getReplacePatterns())
            || getShowBookmarksInGotoDestinations() != Preferences.getShowBookmarksInGoToList()
            || getShowPhpMethodsInGotoDestinations() != Preferences.getShowPhpMethodsInGoToList()
            || getShowJsMethodsInGotoDestinations() != Preferences.getShowJsMethodsInGoToList();
    }

    public boolean getShowBookmarksInGotoDestinations() {
        return checkboxListBookmarks.isSelected();
    }

    public boolean getShowJsMethodsInGotoDestinations() {
        return checkboxListJsMethods.isSelected();
    }

    public boolean getShowPhpMethodsInGotoDestinations() {
        return checkboxListPhpMethods.isSelected();
    }

    public String getGoToPatterns() {
        return getPatternTrimmedFromTextarea(textAreaGoToPatterns);
    }

    public String getReplacePatterns() {
        return getPatternTrimmedFromTextarea(textAreaReplacePatterns);
    }

    @NotNull
    private String getPatternTrimmedFromTextarea(JTextArea textArea) {
        return textArea.getText();
    }
}
