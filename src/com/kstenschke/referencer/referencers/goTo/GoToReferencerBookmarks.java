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
package com.kstenschke.referencer.referencers.goTo;

import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.kstenschke.referencer.resources.StaticTexts;
import com.kstenschke.referencer.utils.UtilsString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GoToReferencerBookmarks extends GoToReferencer {

    public static String[] getItems(Project project, Document document, VirtualFile file) {
        com.intellij.ide.bookmarks.BookmarkManager bookmarkManager =
                com.intellij.ide.bookmarks.BookmarkManager.getInstance(project);

        List<String> bookmarkItems = new ArrayList<>();
        bookmarkItems.add(StaticTexts.POPUP_SECTION_BOOKMARKS);

        List<Bookmark> bookmarks = bookmarkManager.getValidBookmarks();
        String documentText = document.getText();

        List<Integer> bookmarkLineNumbers = new ArrayList<>();
        for (Bookmark curBookmark : bookmarks) {
            if (curBookmark.getFile().equals(file)) {
                bookmarkLineNumbers.add(curBookmark.getLine());
            }
        }

        return buildReferencesArray(document, bookmarkItems, documentText, bookmarkLineNumbers);
    }

    private static String[] buildReferencesArray(Document document, List<String> bookmarkItems, String documentText,
                                                 List<Integer> bookmarkLineNumbers) {
        String[] referencesArr = null;
        if (!bookmarkLineNumbers.isEmpty()) {
            int digits = Collections.max(bookmarkLineNumbers).toString().length();
            Integer[] lineNumbersArr = bookmarkLineNumbers.toArray(new Integer[bookmarkLineNumbers.size()]);
            Arrays.sort(lineNumbersArr);

            int index = 1;
            for (Integer curLineNum : lineNumbersArr) {
                if (curLineNum > 0) {
                    int offsetLineStart = document.getLineStartOffset(curLineNum);
                    int offsetLineEnd = document.getLineEndOffset(curLineNum);
                    String lineSummary = getLineSummary(documentText.substring(offsetLineStart, offsetLineEnd));
                    bookmarkItems.add(index,
                            UtilsString.makeMinLen(Integer.toString(curLineNum + 1), digits) + ": " + lineSummary);
                    index++;
                }
            }

            referencesArr = bookmarkItems.toArray(new String[bookmarkItems.size()]);
        }

        return referencesArr;
    }
}
