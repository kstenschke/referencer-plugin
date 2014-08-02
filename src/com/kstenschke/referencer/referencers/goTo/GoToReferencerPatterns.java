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
package com.kstenschke.referencer.referencers.goTo;

import com.intellij.openapi.editor.Document;
import com.kstenschke.referencer.Preferences;
import com.kstenschke.referencer.parsers.ParserPattern;
import com.kstenschke.referencer.utils.UtilsString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GoToReferencerPatterns {

    /**
     * @return  String[]
     */
    public static String[] getPatterns() {
        return Preferences.getGoToPatterns().split("\n");
    }

    /**
     * @param   pattern
     * @return  String      The label, or the whole definition if no colon-separated label prefix is found
     */
    private static String getLabelFromPatternDefinition(String pattern) {
        int splitPosition = pattern.indexOf(":");
        String label = splitPosition == -1 ? pattern : pattern.substring(0, splitPosition);
        label   = label.trim();

        return label.endsWith(":") ? label : label + ":";
    }

    /**
     * @param   pattern
     * @return  String      The label, or the whole definition if no colon-separated label prefix is found
     */
    private static String getPatternFromPatternDefinition(String pattern) {
        int splitPosition = pattern.indexOf(":");

        return splitPosition == -1 ? pattern : pattern.substring(splitPosition+1);
    }

    /**
     * @param   document
     * @param   patternDefinition
     * @return  String[]
     */
    public static String[] getItems(Document document, String patternDefinition) {
        String label    = getLabelFromPatternDefinition(patternDefinition);
        String pattern  = getPatternFromPatternDefinition(patternDefinition);

        String documentText = document.getText();

        if( !documentText.contains(pattern) ) {
            return null;
        }

        List<String> patternItems= new ArrayList<String>();
        patternItems.add( label + ":" );

        List<Integer> patternLineNumbers = new ArrayList<Integer>();
        List<String> patterns = ParserPattern.getAllOccurrencesInText(documentText, pattern);

        Integer lineOffset = 0;
        if( patterns != null ) {
            for( String curPattern : patterns ) {
                Integer curLineNumber   = UtilsString.getLineNumberOfString(documentText, curPattern, lineOffset);
                if( curLineNumber != null ) {
                    patternLineNumbers.add(curLineNumber);
                }
                lineOffset = curLineNumber;
            }
        }

        return buildReferencesArray(document, patternItems, documentText, patternLineNumbers);
    }

    /**
     * @param   document
     * @param   methodItems
     * @param   documentText
     * @param   methodLineNumbers
     * @return  String[]
     */
    private static String[] buildReferencesArray(Document document, List<String> methodItems, String documentText, List<Integer> methodLineNumbers) {
        String[] referencesArr  = null;
        if( methodLineNumbers.size() > 0 ) {
            int digits  = Collections.max(methodLineNumbers).toString().length();
            Integer[] lineNumbersArr= methodLineNumbers.toArray( new Integer[methodLineNumbers.size()] );
            Arrays.sort(lineNumbersArr);

            int index   = 1;
            for( Integer curLineNum : lineNumbersArr ) {
                if( curLineNum > 0 ) {
                    int offsetLineStart = document.getLineStartOffset(curLineNum);
                    int offsetLineEnd   = document.getLineEndOffset(curLineNum);

                    String lineSummary = GoToReferencer.getLineSummary(documentText.substring(offsetLineStart, offsetLineEnd));
                    methodItems.add(index, UtilsString.makeMinLen(Integer.toString(curLineNum + 1), digits) + ": " + lineSummary);
                    index++;
                }
            }

            referencesArr = methodItems.toArray( new String[methodItems.size()] );
        }
        return referencesArr;
    }

}