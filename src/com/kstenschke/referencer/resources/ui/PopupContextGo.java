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

package com.kstenschke.referencer.resources.ui;

import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.vfs.VirtualFile;
import com.kstenschke.referencer.resources.StaticTexts;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PopupContextGo {

    private final JPopupMenu popup;

    /**
     * Constructor
     */
    public PopupContextGo(final JBPopup popupGo, final Project curProject) {
        this.popup = new JPopupMenu();

            // Remove all bookmarks from current file
        JMenuItem menuItemSelectedBookmarkAdd    = new JMenuItem(StaticTexts.POPUP_GO_REMOVE_ALL_BOOKMARKS);
        menuItemSelectedBookmarkAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BookmarkManager bookmarkManager = BookmarkManager.getInstance(curProject);
                List<Bookmark> bookmarks    = bookmarkManager.getValidBookmarks();
                if( !bookmarks.isEmpty() ) {
                    FileEditorManager fileEditorManager = FileEditorManager.getInstance(curProject);
                    Editor editor   = fileEditorManager.getSelectedTextEditor();
                    if( editor != null ) {
                        VirtualFile currentFile	= FileDocumentManager.getInstance().getFile(editor.getDocument());
                        for(Bookmark curBookmark : bookmarks) {
                            if( curBookmark.getFile().equals(currentFile) ) {
                                bookmarkManager.removeBookmark(curBookmark);
                            }
                        }

                        popupGo.cancel();
                    }
                }
            }
        });
        this.popup.add(menuItemSelectedBookmarkAdd);
    }

    /**
     * @return  PopupListener
     */
    public PopupListener getPopupListener() {
        return new PopupListener();
    }



    /**
     * PopupListener
     */
    class PopupListener extends MouseAdapter {
        /**
         * @param   e
         */
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        /**
         * @param   e
         */
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        /**
         * @param   e
         */
        private void maybeShowPopup(MouseEvent e) {
            if( e.isPopupTrigger() ) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
}
