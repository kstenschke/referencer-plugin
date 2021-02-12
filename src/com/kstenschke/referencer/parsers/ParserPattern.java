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
package com.kstenschke.referencer.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserPattern {

    /**
     * Get all occurrences of the given regex pattern
     * in the order of their appearance in the given text
     *
     * @param    text    Code to be searched in
     * @return Found pattern occurrences
     */
    public static List<String> getAllOccurrencesInText(String text, String pattern) {
        List<String> allMatches = new ArrayList<>();
        Matcher m = Pattern.compile(pattern + ".*").matcher(text);

        while (m.find()) {
            allMatches.add(m.group().trim());
        }

        return allMatches;
    }
}
