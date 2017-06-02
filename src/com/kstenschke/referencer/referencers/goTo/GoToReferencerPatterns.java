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
package com.kstenschke.referencer.referencers.goTo;

import com.intellij.openapi.editor.Document;
import com.kstenschke.referencer.Preferences;
import com.kstenschke.referencer.parsers.ParserPattern;
import com.kstenschke.referencer.resources.StaticTexts;
import com.kstenschke.referencer.utils.UtilsArray;
import com.kstenschke.referencer.utils.UtilsString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GoToReferencerPatterns extends GoToReferencer {

    /**
     * @return Any patterns defined?
     */
    public static boolean hasPatternDefinitions() {
        return Preferences.getGoToPatterns().length() > 0;
    }

    /**
     * @return String[]    Pattern definitions (containing label+pattern each)
     */
    public static String[] getPatternDefinitions() {
        return Preferences.getGoToPatterns().split("\n");
    }

    /**
     * @param pattern
     * @return String      The label, or the whole definition if no colon-separated label prefix is found
     */
    private static String getLabelFromPatternDefinition(String pattern) {
        int splitPosition = pattern.indexOf(':');
        String label = splitPosition == -1 ? pattern : pattern.substring(0, splitPosition);

        return label.trim();
    }

    /**
     * @param pattern
     * @return String      The label, or the whole definition if no colon-separated label prefix is found
     */
    private static String getPatternFromPatternDefinition(String pattern) {
        int splitPosition = pattern.indexOf(':');

        return splitPosition == -1 ? pattern : pattern.substring(splitPosition + 1);
    }

    /**
     * @param document
     * @param patternDefinition
     * @return String[]
     */
    public static String[] getItems(Document document, String patternDefinition) {
        String label = getLabelFromPatternDefinition(patternDefinition);
        String pattern = getPatternFromPatternDefinition(patternDefinition);

        String documentText = document.getText();

        List<String> occurrences = ParserPattern.getAllOccurrencesInText(documentText, pattern);
        List<Integer> occurrenceLineNumbers = collectLineNumbers(documentText, occurrences);

        return buildReferencesArray(document, documentText, occurrenceLineNumbers, label);
    }

    /**
     * @param document
     * @param documentText
     * @param methodLineNumbers
     * @return String[]
     */
    private static String[] buildReferencesArray(Document document, String documentText, List<Integer> methodLineNumbers, String label) {
        List<String> methodItems = new ArrayList<String>();
        String[] referencesArr = null;
        if (!methodLineNumbers.isEmpty()) {
            int digits = Collections.max(methodLineNumbers).toString().length();
            Integer[] lineNumbersArr = methodLineNumbers.toArray(new Integer[methodLineNumbers.size()]);
            Arrays.sort(lineNumbersArr);

            // Assemble items with line summary, and post-fixed with line number
            int index = 0;
            for (Integer curLineNum : lineNumbersArr) {
                if (curLineNum > 0) {
                    int offsetLineStart = document.getLineStartOffset(curLineNum);
                    int offsetLineEnd = document.getLineEndOffset(curLineNum);

                    String lineSummary = GoToReferencer.getLineSummary(documentText.substring(offsetLineStart, offsetLineEnd));
                    methodItems.add(index, lineSummary + ":" + UtilsString.makeMinLen(Integer.toString(curLineNum + 1), digits));
                    index++;
                }
            }
            // Sort alphabetical
            referencesArr = methodItems.toArray(new String[methodItems.size()]);
            Arrays.sort(referencesArr, String.CASE_INSENSITIVE_ORDER);
            // Move line numbers to front
            ReformItemsMovePostfixToFront(referencesArr);
            // Add section header
            referencesArr = UtilsArray.addToBeginning(referencesArr, StaticTexts.POPUP_ITEM_PREFIX_SECTION_TITLE + " " + label);
        }

        return referencesArr;
    }

}