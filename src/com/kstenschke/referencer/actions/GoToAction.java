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
import com.kstenschke.referencer.referencers.goTo.BookmarkReferencer;
import com.kstenschke.referencer.referencers.insertOrCopy.JavascriptReferencer;
import com.kstenschke.referencer.resources.ui.DividedListCellRenderer;
import com.kstenschke.referencer.listeners.DividedListSelectionListener;
import com.kstenschke.referencer.resources.StaticTexts;
import com.kstenschke.referencer.resources.ui.PopupContextGo;
import com.kstenschke.referencer.utils.UtilsEnvironment;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.List;


public class GoToAction extends AnAction {

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
            String content              = document.getText();

            Object[] refArr = BookmarkReferencer.getBookmarkItems(this.project, this.document, this.file);
            refArr  = ArrayUtils.addAll( refArr, this.getFunctionItems() );

            if( refArr != null && refArr.length > 0) {
                final JBList referencesList = new JBList(refArr);
                referencesList.setCellRenderer(new DividedListCellRenderer() );
                referencesList.addListSelectionListener( new DividedListSelectionListener() );

                    // Preselect item from preferences
                Integer selectedIndex	= Preferences.getSelectedIndex(fileExtension);
                if( selectedIndex > refArr.length ) selectedIndex	= 0;

                referencesList.setSelectedIndex(selectedIndex);

                    // Build and show popup
                PopupChooserBuilder popup = JBPopupFactory.getInstance().createListPopupBuilder(referencesList);

                JBPopup popupGo = popup.setTitle(StaticTexts.POPUP_TITLE_ACTION_GO).setItemChoosenCallback(new Runnable() {
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
                }).setMovable(true).createPopup();

                    // Add context menu
                PopupContextGo contextMenu = new PopupContextGo(popupGo, this.project);
                referencesList.addMouseListener( contextMenu.getPopupListener() );

                popupGo.showCenteredInCurrentWindow(project);
            }

//            List<String> refArrFunctions = ParserJavascript.getAllMethodsInText(content);

            if( (refArr == null || refArr.length == 0) /*|| refArrFunctions.isEmpty()*/ ) {
                UtilsEnvironment.notify(StaticTexts.NOTIFY_BOOKMARK_NONE_FOUND);
            }
        }
    }

    /**
     * @return  String[]
     */
    private String[] getFunctionItems() {
        String[] referencesArr = null;

//        BookmarkManager bookmarkManager = BookmarkManager.getInstance(this.project);
//
        List<String> methodItems= new ArrayList<String>();
        methodItems.add(StaticTexts.POPUP_SECTION_FUNCTIONS);

//        List<Bookmark> bookmarks  = bookmarkManager.getValidBookmarks();
        String documentText = this.document.getText();

        List<String> functions = JavascriptReferencer.getAllMethodsInText(documentText);

        List<Integer> methodLineNumbers = new ArrayList<Integer>();
        for( String functionName : functions ) {
//            if( curBookmark.getFile().equals(this.file) ) {
//                bookmarkLineNumbers.add(curBookmark.getLine());
//            }
            methodItems.add( functionName );
        }

        if( methodLineNumbers.size() > 0 || true ) {
//            int digits  = Collections.max(methodLineNumbers).toString().length();
//            Integer[] lineNumbersArr= methodLineNumbers.toArray( new Integer[methodLineNumbers.size()] );
//            Arrays.sort(lineNumbersArr);
//
//            int index   = 1;
//            for( Integer curLineNum : lineNumbersArr ) {
//                if( curLineNum > 0 ) {
//                    int offsetLineStart = this.document.getLineStartOffset(curLineNum);
//                    int offsetLineEnd   = this.document.getLineEndOffset(curLineNum);
//                    String lineSummary = this.getLineSummary(documentText.substring(offsetLineStart, offsetLineEnd));
//                    methodItems.add(index, UtilsString.makeMinLen(Integer.toString(curLineNum), digits) + ": " + lineSummary);
//                    index++;
//                }
//            }

            referencesArr = methodItems.toArray( new String[methodItems.size()] );
        }

        return referencesArr;
    }

}
