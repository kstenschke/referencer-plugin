/*
 * Copyright 2012-2014 Kay Stenschke
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
package com.kstenschke.referencer.resources.ui;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.kstenschke.referencer.resources.StaticTexts;
import com.kstenschke.referencer.resources.forms.PluginConfiguration;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ReferencerSettingsComponent implements ProjectComponent, Configurable {

    private PluginConfiguration settingsPanel = null;

    @Nls
    @Override
    public String getDisplayName() {
        return StaticTexts.SETTINGS_DISPLAY_NAME;
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (settingsPanel == null) {
            settingsPanel = new PluginConfiguration();
        }

        reset();

        return settingsPanel.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return settingsPanel != null && settingsPanel.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
//        if (settingsPanel != null) {
//            ShifterPreferences.saveSortingMode(settingsPanel.getSelectedSortingMode());
//            ShifterPreferences.saveIsActivePreserveCase(settingsPanel.getIsActivePreserveCase());
//
//            String dictionary	= settingsPanel.getData();
//            if( dictionary != null ) {
//                ShifterPreferences.saveDictionary(dictionary);
//            }
//
//            applyGlobalSettings();
//        }
    }

    @Override
    public void reset() {

    }

    @Override
    public void disposeUIResources() {

    }

    public ReferencerSettingsComponent(Project project) {
    }

    public void initComponent() {
        // TODO: insert component initialization logic here
    }

    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @NotNull
    public String getComponentName() {
        return StaticTexts.SETTINGS_COMPONENT_NAME;
    }

    public void projectOpened() {
        // called when project is opened
    }

    public void projectClosed() {
        // called when project is being closed
    }
}
