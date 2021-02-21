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

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBList;
import com.kstenschke.referencer.*;
import com.kstenschke.referencer.listeners.DividedListSelectionListener;
import com.kstenschke.referencer.referencers.goTo.*;
import com.kstenschke.referencer.resources.StaticTexts;
import com.kstenschke.referencer.resources.ui.DividedListCellRenderer;
import com.kstenschke.referencer.resources.ui.PopupContextGo;
import com.kstenschke.referencer.utils.UtilsEnvironment;
import com.kstenschke.referencer.utils.UtilsFile;
import org.apache.commons.lang.ArrayUtils;

public class GoToAction extends ReferencePopupableAction {

    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        if (project == null || editor == null) {
            return;
        }

        Document document = editor.getDocument();
        VirtualFile file = FileDocumentManager.getInstance().getFile(document);
        String fileExtension = UtilsFile.getExtensionByDocument(document);

        Object[] references = GoToReferencerBookmarks.getItems(project, document, file);

        String[] headlineItems = GoToReferencerHeadlines.getItems(document, fileExtension);
        if (headlineItems != null && headlineItems.length > 1) {
            references = ArrayUtils.addAll(references, headlineItems);
        }

        String[] methodItems = GoToReferencerMethods.getItems(document, fileExtension);   /* Add JS or PHP methods */
        if (methodItems != null && methodItems.length > 1) {
            references = ArrayUtils.addAll(references, methodItems);
        }

        String[] regionItems = GoToReferencerRegions.getItems(document);  /* Add region blocks */
        if (regionItems != null && regionItems.length > 1) {
            references = ArrayUtils.addAll(references, regionItems);
        }

        if (GoToReferencerPatterns.hasPatternDefinitions()) {       /* Add dynamical jump destination patterns */
            String[] patternDefinitions = GoToReferencerPatterns.getPatternDefinitions();
            for (String curPatterDefinition : patternDefinitions) {
                String[] curPatternItems = GoToReferencerPatterns.getItems(document, curPatterDefinition);
                if (curPatternItems != null && curPatternItems.length > 1) {
                    references = ArrayUtils.addAll(references, curPatternItems);
                }
            }
        }

        if (references != null && references.length > 0) {
            final JBList<Object> referencesList = new JBList<>(references);
            referencesList.setCellRenderer(new DividedListCellRenderer(referencesList));
            referencesList.addListSelectionListener(new DividedListSelectionListener());

            /* Preselect item from preferences */
            int selectedIndex = Preferences.getSelectedIndex(fileExtension);
            if (selectedIndex > references.length || selectedIndex == 0) {
                selectedIndex = 1;
            }
            referencesList.setSelectedIndex(selectedIndex);

            PopupChooserBuilder<Object> popupGo =
                    buildPopup(project, editor, references, referencesList, fileExtension, false, MODE_GO);

            /* Add context menu */
            PopupContextGo contextMenu = new PopupContextGo(project);
            referencesList.addMouseListener(contextMenu.getPopupListener());

            popupGo.setMovable(true).createPopup().showCenteredInCurrentWindow(project);
        }

        if (references == null || references.length == 0) {
            UtilsEnvironment.notify(StaticTexts.NOTIFY_GOTO_NONE_FOUND);
        }
    }
}
