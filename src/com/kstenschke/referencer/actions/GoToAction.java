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
package com.kstenschke.referencer.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBList;
import com.kstenschke.referencer.*;
import com.kstenschke.referencer.referencers.goTo.GoToReferencerBookmarks;
import com.kstenschke.referencer.referencers.goTo.GoToReferencerMethods;
import com.kstenschke.referencer.referencers.goTo.GoToReferencerPatterns;
import com.kstenschke.referencer.resources.ui.DividedListCellRenderer;
import com.kstenschke.referencer.listeners.DividedListSelectionListener;
import com.kstenschke.referencer.resources.StaticTexts;
import com.kstenschke.referencer.resources.ui.PopupContextGo;
import com.kstenschke.referencer.utils.UtilsEnvironment;
import com.kstenschke.referencer.utils.UtilsFile;
import org.apache.commons.lang.ArrayUtils;

public class GoToAction extends AnAction {

    private Project project;
    private Editor editor;
    private Document document;

    /**
     * Show list of bookmarks in current document to go to
     *
     * @param	e	Action system event
     */
    public void actionPerformed(AnActionEvent e) {
        this.project	= e.getData(PlatformDataKeys.PROJECT);
        this.editor		= e.getData(PlatformDataKeys.EDITOR);

        if( this.project != null && this.editor != null ) {
            this.document   = this.editor.getDocument();
            VirtualFile file = FileDocumentManager.getInstance().getFile(document);
            String fileExtension = UtilsFile.getExtensionByDocument(this.document);

                // Get bookmarks
            Object[] refArr = GoToReferencerBookmarks.getItems(this.project, this.document, file);
                // Add JS or PHP methods
            String[] methodItems = GoToReferencerMethods.getItems(this.document, fileExtension);
            if( methodItems != null && methodItems.length > 1 ) {
                refArr = ArrayUtils.addAll( refArr, methodItems );
            }
                // Add dynamical jump destination patterns
            String[] patternDefinitions = GoToReferencerPatterns.getPatternDefinitions();
            for(String curPatterDefinition : patternDefinitions) {
                String[] curPatternItems = GoToReferencerPatterns.getItems(this.document, curPatterDefinition);
                if( curPatternItems != null && curPatternItems.length > 1 ) {
                    refArr  = ArrayUtils.addAll( refArr, curPatternItems );
                }
            }

            if( refArr != null && refArr.length > 0) {
                final JBList referencesList = new JBList(refArr);
                referencesList.setCellRenderer(new DividedListCellRenderer() );
                referencesList.addListSelectionListener( new DividedListSelectionListener() );

                    // Preselect item from preferences
                Integer selectedIndex	= Preferences.getSelectedIndex(fileExtension);
                if( selectedIndex > refArr.length ) selectedIndex	= 0;

                referencesList.setSelectedIndex(selectedIndex);

                    // Build and show popup
                JBPopup popupGo = buildPopup(fileExtension, referencesList);

                    // Add context menu
                PopupContextGo contextMenu = new PopupContextGo(popupGo, this.project);
                referencesList.addMouseListener( contextMenu.getPopupListener() );

                popupGo.showCenteredInCurrentWindow(project);
            }

            if( (refArr == null || refArr.length == 0) ) {
                UtilsEnvironment.notify(StaticTexts.NOTIFY_GOTO_NONE_FOUND);
            }
        }
    }

    /**
     * @param   fileExtension
     * @param   referencesList
     * @return  JBPopup
     */
    private JBPopup buildPopup(final String fileExtension, final JBList referencesList) {
        PopupChooserBuilder popup = JBPopupFactory.getInstance().createListPopupBuilder(referencesList);

        return popup.setTitle(StaticTexts.POPUP_TITLE_ACTION_GO).setItemChoosenCallback(new Runnable() {
            public void run() {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    public void run() {
                            // Callback when item chosen
                        CommandProcessor.getInstance().executeCommand(project, new Runnable() {
                            public void run() {
                                Preferences.saveSelectedIndex(fileExtension, referencesList.getSelectedIndex());

                                Integer lineNumber  = Integer.parseInt( referencesList.getSelectedValue().toString().split(":")[0] );
                                editor.getCaretModel().moveToOffset( document.getLineStartOffset(lineNumber-1), true );
                                editor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
                            }
                        }, null, null);
                    }
                });

            }
        }).setMovable(true).createPopup();
    }

}
