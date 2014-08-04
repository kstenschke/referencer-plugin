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

import com.kstenschke.referencer.utils.UtilsString;

import java.util.ArrayList;
import java.util.List;

public class GoToReferencer {

    protected static List<Integer> collectLineNumbers(String documentText, List<String> methods) {
        List<Integer> methodLineNumbers = new ArrayList<Integer>();

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

        return methodLineNumbers;
    }

    /**
     * @param referencesArr
     */
    protected static void ReformItemsMovePostfixToFront(String[] referencesArr) {
        int index;
        index = 0;
        for(String item : referencesArr) {
            int splitPos    = item.lastIndexOf(":");
            String beginning= item.substring(0, splitPos);
            String ending   = item.substring(splitPos+1);

            referencesArr[index] = ending + ": " + beginning;
            index++;
        }
    }

    /**
     * @param   lineText
     * @return  String
     */
    protected static String getLineSummary(String lineText) {
        return lineText.isEmpty() ? "" : UtilsString.crop(lineText.trim().replace("\t", " ").replace("  ", " "), 80);
    }
}
