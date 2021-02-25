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

import com.intellij.openapi.editor.Document;
import com.kstenschke.referencer.resources.StaticTexts;
import com.kstenschke.referencer.utils.UtilsArray;
import com.kstenschke.referencer.utils.UtilsString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoToReferencerRegions extends GoToReferencer {

    public static String[] getItems(Document document) {
        String documentText = document.getText();

        if ((!documentText.contains("// region ") && !documentText.contains("//region "))
            || !documentText.contains("endregion")
        ) {
            return null;
        }

        List<String> regions = getAllRegionsInText(documentText);

        List<Integer> methodLineNumbers = collectLineNumbers(documentText, regions);

        return buildReferencesArray(document, documentText, methodLineNumbers);
    }

    private static List<String> getAllRegionsInText(String text) {
        /* 1. Find matches ala: "// region ", if any found return it */
        List<String> allMatches = new ArrayList<>();
        Matcher m = Pattern.compile("//\\s*region\\s*[^\\n ]*").matcher(text);

        while (m.find()) {
            if (!allMatches.contains(m.group())) {
                allMatches.add(m.group());
            }
        }

        return allMatches;
    }

    private static String[] buildReferencesArray(Document document, String documentText,
                                                 List<Integer> regionLineNumbers) {
        if (regionLineNumbers.isEmpty()) {
            return null;
        }

        List<String> methodItems = new ArrayList<>();

        String[] referencesArr;
        int digits = Collections.max(regionLineNumbers).toString().length();
        Integer[] lineNumbersArr = regionLineNumbers.toArray(new Integer[0]);
        Arrays.sort(lineNumbersArr);

        /* Assemble items with line summary, and post-fixed with line number */
        int index = 0;
        for (Integer curLineNum : lineNumbersArr) {
            if (curLineNum > 0) {
                int offsetLineStart = document.getLineStartOffset(curLineNum);
                int offsetLineEnd = document.getLineEndOffset(curLineNum);

                String lineSummary = GoToReferencer.getLineSummary(documentText.substring(offsetLineStart, offsetLineEnd));
                methodItems.add(index,
                        lineSummary + ":" + UtilsString.makeMinLen(Integer.toString(curLineNum + 1), digits));
                index++;
            }
        }

        referencesArr = methodItems.toArray(new String[0]);
        reformItemsMovePostfixToFront(referencesArr);                                   /* Move line numbers to front */

        return UtilsArray.addToBeginning(referencesArr, StaticTexts.POPUP_SECTION_REGIONS);   /* Add section header */
    }

}