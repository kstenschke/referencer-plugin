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
 */// Popup titles
package com.kstenschke.referencer.referencers.goTo;

import com.intellij.openapi.editor.Document;
import com.kstenschke.referencer.parsers.ParserMarkdown;
import com.kstenschke.referencer.resources.StaticTexts;
import com.kstenschke.referencer.utils.UtilsArray;
import com.kstenschke.referencer.utils.UtilsFile;
import com.kstenschke.referencer.utils.UtilsString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoToReferencerHeadlines extends GoToReferencer {

    public static String[] getItems(Document document, String fileExtension) {
        if (!UtilsFile.isMarkdownFileExtension(fileExtension)) {
            return null;
        }

        String documentText = document.getText();
        List<String> headlines = ParserMarkdown.getAllHeadlinesInText(documentText);
        List<Integer> headlineLineNumbers = collectLineNumbers(documentText, headlines);

        return buildReferencesArray(document, documentText, headlineLineNumbers);
    }

    private static String[] buildReferencesArray(Document document, String documentText,
                                                 List<Integer> methodLineNumbers) {
        List<String> headlineItems = new ArrayList<>();

        if (methodLineNumbers.isEmpty()) {
            return null;
        }

        String[] referencesArr;
        int digits = Collections.max(methodLineNumbers).toString().length();

        Integer[] lineNumbersArr = methodLineNumbers.toArray(new Integer[methodLineNumbers.size()]);

        /* Assemble items with line summary, and post-fixed with line number */
        int index = 0;
        for (Integer currentLineNum : lineNumbersArr) {
            int offsetLineStart = currentLineNum == 0 ? 0 : document.getLineStartOffset(currentLineNum);
            int offsetLineEnd = document.getLineEndOffset(currentLineNum);

            String lineSummary = GoToReferencer.getLineSummary(documentText.substring(offsetLineStart, offsetLineEnd));
            headlineItems.add(index,
                    lineSummary + ":" + UtilsString.makeMinLen(Integer.toString(currentLineNum + 1), digits));
            index++;
        }

        referencesArr = headlineItems.toArray(new String[headlineItems.size()]);
        reformItemsMovePostfixToFront(referencesArr);                                   /* Move line numbers to front */

        /* Add section header */
        return UtilsArray.addToBeginning(referencesArr, StaticTexts.POPUP_SECTION_TITLE_MARKDOWN);
    }

}