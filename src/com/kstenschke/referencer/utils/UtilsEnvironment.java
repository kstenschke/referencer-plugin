/*
 * Copyright 2012-2017 Kay Stenschke
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
package com.kstenschke.referencer.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;

import javax.swing.*;
import java.awt.*;

public class UtilsEnvironment {

    /**
     * @return The currently opened project
     */
    private static Project getOpenProject() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();

        return (projects.length > 0) ? projects[0] : null;
    }

    /**
     * @param message
     */
    public static void notify(String message) {
        Project project = getOpenProject();
        final StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);

        if (statusBar != null) {
            // Create notification message UI
            final JPanel panel = new JPanel();
            panel.setOpaque(false);

            if (message.contains("\n")) {
                String[] messageLines = message.split("\n");
                for (String curLine : messageLines) {
                    panel.add(new JBLabel(curLine));
                }
                panel.setSize(new Dimension(panel.getWidth(), messageLines.length * 16));
            } else {
                JBLabel label = new JBLabel();
                label.setText(message);

                panel.add(label);
                panel.setSize(new Dimension(panel.getWidth(), 16));
            }

            // Run notification thread
            Thread statusBarNotifyThread = new Thread() {
                @Override
                public void run() {
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                statusBar.fireNotificationPopup(panel, JBColor.WHITE);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            statusBarNotifyThread.start();
        }
    }
}
