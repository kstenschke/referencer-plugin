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
package com.kstenschke.referencer;

import com.intellij.openapi.options.Configurable;
import com.kstenschke.referencer.resources.StaticTexts;
import com.kstenschke.referencer.resources.forms.PluginConfiguration;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ReferencerConfigurable implements Configurable {

    private PluginConfiguration settingsPanel = null;

    @Nullable @NonNls public String getHelpTopic() {
        return null;
    }

    @Nls @Override public String getDisplayName() {
        return StaticTexts.SETTINGS_DISPLAY_NAME;
    }

    @Nullable @Override public JComponent createComponent() {
        if (settingsPanel == null) {
            settingsPanel = new PluginConfiguration();
        }

        reset();

        return settingsPanel.getRootPanel();
    }

    @Override public boolean isModified() {
        return settingsPanel != null && settingsPanel.isModified();
    }

    @Override public void apply() {
        if (settingsPanel != null) {
            Preferences.saveGoToPatterns(settingsPanel.getGoToPatterns());
        }
    }

    @Override public void reset() {
    }

    @Override public void disposeUIResources() {
        settingsPanel = null;
    }
}
