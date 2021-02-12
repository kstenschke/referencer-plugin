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

import com.kstenschke.referencer.utils.UtilsString;

import java.util.ArrayList;
import java.util.List;

class GoToReferencer {

    static List<Integer> collectLineNumbers(String documentText, List<String> items) {
        List<Integer> lineNumbers = new ArrayList<>();

        if (items != null) {
            Integer lineOffset = 0;
            for (String currentItem : items) {
                Integer curLineNumber = UtilsString.getLineNumberOfString(documentText, currentItem, lineOffset);
                if (curLineNumber != null) {
                    lineNumbers.add(curLineNumber);
                }
                lineOffset = curLineNumber;
            }
        }

        return lineNumbers;
    }

    static void reformItemsMovePostfixToFront(String[] references) {
        int index = 0;
        for (String item : references) {
            int splitPos = item.lastIndexOf(':');
            String beginning = item.substring(0, splitPos);
            String ending = item.substring(splitPos + 1);

            references[index] = ending + ": " + beginning;
            index++;
        }
    }

    static String getLineSummary(String lineText) {
        return lineText.isEmpty()
            ? ""
            : UtilsString.crop(lineText.trim().replace("\t", " ").replace("  ", " "), 80);
    }
}
