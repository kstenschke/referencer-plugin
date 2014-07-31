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
