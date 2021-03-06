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

public class ParserMarkdown {

    /**
     * Get all Markdown headline in the order of their appearance
     *
     * @param  text    Source code to be searched
     * @return All found Markdown headlines
     */
    public static List<String> getAllHeadlinesInText(String text) {
        List<String> allMatches = new ArrayList<>();

        int offsetNewline = text.indexOf("\n");
        if (offsetNewline != -1 && text.startsWith("#")) {
            allMatches.add(text.substring(0, offsetNewline).trim());
        }

        Matcher m = Pattern.compile("\n#.*\n").matcher(text);

        while (m.find()) {
            if (!allMatches.contains(m.group())) {
                allMatches.add(m.group().trim());
            }
        }

        return allMatches;
    }
}
