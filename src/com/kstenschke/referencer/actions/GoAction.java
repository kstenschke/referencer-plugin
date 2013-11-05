/*
 * Copyright 2012-2013 Kay Stenschke
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

import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
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
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBList;
import com.kstenschke.referencer.Preferences;
import com.kstenschke.referencer.StaticTexts;
import com.kstenschke.referencer.UtilsString;
import com.kstenschke.referencer.dividedlist.DividedListCellRenderer;
import com.kstenschke.referencer.dividedlist.DividedListSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class GoAction extends AnAction {

    private Project project;
    private Editor editor;
    private VirtualFile file;
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
            this.document = this.editor.getDocument();
            this.file		= FileDocumentManager.getInstance().getFile(document);
            final String fileExtension	= (this.file != null) ? this.file.getExtension() : "";

            final String[] refArr = this.getItems();
            if( refArr != null ) {

                final JBList referencesList = new JBList(refArr);
                referencesList.setCellRenderer(new DividedListCellRenderer() );
                referencesList.addListSelectionListener(new DividedListSelectionListener());

                // Preselect item from preferences
                Integer selectedIndex	= Preferences.getSelectedIndex(fileExtension);
                if( selectedIndex > refArr.length ) selectedIndex	= 0;

                referencesList.setSelectedIndex(selectedIndex);

                PopupChooserBuilder popup = JBPopupFactory.getInstance().createListPopupBuilder(referencesList);

                popup.setTitle(StaticTexts.POPUP_TITLE_ACTION_GO).setItemChoosenCallback(new Runnable() {
                    public void run() {
                        ApplicationManager.getApplication().runWriteAction(new Runnable() {
                            public void run() {
                                    // Callback when item chosen
                                CommandProcessor.getInstance().executeCommand(project, new Runnable() {
                                    public void run() {
                                        Preferences.saveSelectedIndex(fileExtension, referencesList.getSelectedIndex());

                                        Integer lineNumber  = Integer.parseInt( referencesList.getSelectedValue().toString().split(":")[0] );
                                        editor.getCaretModel().moveToOffset( document.getLineStartOffset(lineNumber), true );
                                        editor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
                                    }
                                }, null, null);

                            }
                        });

                    }
                }).setMovable(true).createPopup().showCenteredInCurrentWindow(project);
            }
        }
    }

    /**
     * @return  String[]
     */
    private String[] getItems() {
        String[] referencesArr = null;

        BookmarkManager bookmarkManager = BookmarkManager.getInstance(this.project);

        List<String> bookmarkItems= new ArrayList<String>();
        List<Bookmark> bookmarks  = bookmarkManager.getValidBookmarks();
        String documentText = this.document.getText();

        List<Integer> bookmarkLineNums = new ArrayList<Integer>();
        for( Bookmark curBookmark : bookmarks ) {
            if( curBookmark.getFile() == this.file ) {
                bookmarkLineNums.add( curBookmark.getLine() );
            }
        }

        if( bookmarkLineNums.size() > 0 ) {
            Integer digits          = Collections.max(bookmarkLineNums).toString().length();
            Integer[] lineNumbersArr= bookmarkLineNums.toArray( new Integer[bookmarkLineNums.size()] );
            Arrays.sort(lineNumbersArr);

            Integer index   = 0;
            for( Integer curLineNum : lineNumbersArr ) {
                String lineSummary = this.getLineSummary( documentText.substring( this.document.getLineStartOffset(curLineNum), this.document.getLineEndOffset(curLineNum) ) );
                bookmarkItems.add(index, UtilsString.makeMinLen(Integer.toString(curLineNum), digits) + ": " + lineSummary);
                index++;
            }

            referencesArr = bookmarkItems.toArray( new String[bookmarkItems.size()] );
        }

        return referencesArr;
    }

    /**
     * @param   lineText
     * @return  String
     */
    private String getLineSummary(String lineText) {
        return lineText.isEmpty() ? "" : UtilsString.crop( lineText.trim().replace("\t", " ").replace("  ", " "), 80 );
    }

}
