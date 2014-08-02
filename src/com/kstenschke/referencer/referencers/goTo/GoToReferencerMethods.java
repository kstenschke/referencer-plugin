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
import com.kstenschke.referencer.parsers.ParserJavaScript;
import com.kstenschke.referencer.parsers.ParserPhp;
import com.kstenschke.referencer.resources.StaticTexts;
import com.kstenschke.referencer.utils.UtilsFile;
import com.kstenschke.referencer.utils.UtilsString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GoToReferencerMethods {

    /**
     * @return  String[]
     */
    public static String[] getMethodItems(Document document, String fileExtension) {
        boolean isJavaScript = UtilsFile.isJavaScriptFileExtension(fileExtension);
        boolean isPhp = UtilsFile.isPhpFileExtension(fileExtension);

        if( ! isJavaScript && ! isPhp) {
            return null;
        }

        String[] referencesArr  = null;

        List<String> methodItems= new ArrayList<String>();
        methodItems.add(StaticTexts.POPUP_SECTION_FUNCTIONS);

        String documentText = document.getText();

        List<Integer> methodLineNumbers = new ArrayList<Integer>();
        List<String> methods = isJavaScript
            ? ParserJavaScript.getAllMethodsInText(documentText)
            : ParserPhp.getAllMethodsInText(documentText);

        Integer lineOffset = 0;
        if( methods != null ) {
            for( String curMethodName : methods ) {
                Integer curLineNumber   = UtilsString.getLineNumberOfString(documentText, curMethodName, lineOffset);
                if( curLineNumber != null ) {
                    methodLineNumbers.add( curLineNumber );
                }
                lineOffset = curLineNumber;
            }
        }

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
                    methodItems.add(index, UtilsString.makeMinLen(Integer.toString(curLineNum+1), digits) + ": " + lineSummary);
                    index++;
                }
            }

            referencesArr = methodItems.toArray( new String[methodItems.size()] );
        }

        return referencesArr;
    }

}