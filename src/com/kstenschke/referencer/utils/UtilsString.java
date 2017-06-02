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
package com.kstenschke.referencer.utils;

import org.jetbrains.annotations.Nullable;

/**
 * String utility methods
 */
public class UtilsString {

    /**
     * @param text
     * @param subStr
     * @param lineOffset Optional line offset where to begin searching
     * @return Number of line containing the given subString, or null if not contained
     */
    public static Integer getLineNumberOfString(String text, String subStr, @Nullable Integer lineOffset) {
        if (lineOffset == null) {
            lineOffset = 0;
        }

        String[] lines = text.split("\n");

        int curLineNumber = 0;
        for (String curLine : lines) {
            if (curLineNumber >= lineOffset && curLine.contains(subStr)) {
                return curLineNumber;
            }
            curLineNumber++;
        }

        return null;
    }

    /**
     * Remove all of the given sub string from the given string
     *
     * @param    haystack    String to be modified
     * @param    needles        Sub strings to be removed
     * @return The modified string
     */
    private static String removeSubStrings(String haystack, String[] needles) {
        for (String needle : needles) {
            haystack = haystack.replace(needle, "");
        }

        return haystack;
    }

    /**
     * @param string
     * @param toReplace
     * @param replacement
     * @return String
     */
    public static String replaceLast(String string, String toReplace, String replacement) {
        int pos = string.lastIndexOf(toReplace);
        if (pos > -1) {
            return string.substring(0, pos)
                    + replacement
                    + string.substring(pos + toReplace.length(), string.length());
        } else {
            return string;
        }
    }

    /**
     * Get word to the left of caret offset out of given text
     *
     * @param text         The full text
     * @param cursorOffset Character offset of caret
     * @return The extracted word or null
     */
    public static String getWordLeftOfOffset(CharSequence text, int cursorOffset) {
        return grabWord(text, cursorOffset - 1, false);
    }

    /**
     * Get word at caret offset out of given text
     *
     * @param text                    The full text
     * @param cursorOffset            Character offset of caret
     * @param expandWordBoundaryRight Detect string boundary by expanding to the right?
     * @return The extracted word or null
     */
    private static String grabWord(CharSequence text, int cursorOffset, boolean expandWordBoundaryRight) {
        if (text.length() == 0 || cursorOffset >= text.length()) return null;

        while (cursorOffset > 0 && cursorOffset < text.length() - 1
                && !Character.isJavaIdentifierPart(text.charAt(cursorOffset))
                && Character.isJavaIdentifierPart(text.charAt(cursorOffset - 1))
                ) {
            cursorOffset--;
        }

        if (cursorOffset >= 0 && Character.isJavaIdentifierPart(text.charAt(cursorOffset))) {
            int start = cursorOffset;
            int end = cursorOffset;


            while (start > 0 && Character.isJavaIdentifierPart(text.charAt(start - 1))) {
                start--;
            }

            if (expandWordBoundaryRight) {
                // Find ending of word by expanding boundary until first non-whitespace to the right
                while (end < text.length() && !Character.isWhitespace(text.charAt(end))) {
                    end++;
                }
            }

            return text.subSequence(start, end).toString();
        }

        return null;
    }

    /**
     * Get "string" at caret offset out of given text - string-boundary: white-space characters
     *
     * @param text         The full text
     * @param cursorOffset Character offset of caret
     * @return The extracted word or null
     */
    public static String getStringLeftOfOffset(CharSequence text, int cursorOffset) {
        return grabString(text, cursorOffset - 1, false);
    }

    /**
     * Get "string" at caret offset out of given text - string-boundary: white-space characters
     *
     * @param text                    The full text
     * @param cursorOffset            Character offset of caret
     * @param expandWordBoundaryRight Detect string boundary by expanding to the right?
     * @return The extracted word or null
     */
    private static String grabString(CharSequence text, int cursorOffset, boolean expandWordBoundaryRight) {
        if (text.length() == 0 || cursorOffset >= text.length()) return null;

        while (cursorOffset > 0 && cursorOffset < (text.length() - 1)
                && !Character.isWhitespace(text.charAt(cursorOffset))
                && Character.isWhitespace(text.charAt(cursorOffset - 1))
                ) {
            cursorOffset--;
        }

        if (cursorOffset >= 0 && Character.isWhitespace(text.charAt(cursorOffset))) {
            int start = cursorOffset;
            int end = cursorOffset;


            while (start > 0 && Character.isWhitespace(text.charAt(start - 1))) {
                start--;
            }

            if (expandWordBoundaryRight) {
                while (end < text.length() && !Character.isWhitespace(text.charAt(end))) {
                    end++;
                }
            }

            return text.subSequence(start, end).toString();
        }

        return null;
    }

    /**
     * @param str
     * @param maxLen
     */
    public static String crop(String str, Integer maxLen) {
        if (str.length() > maxLen) {
            str = str.substring(0, maxLen) + "...";
        }

        return str;
    }

    /**
     * @param str
     * @param len
     * @param fillChar
     */
    private static String makeMinLen(String str, Integer len, String fillChar) {
        while (str.length() < len) {
            str = fillChar + str;
        }

        return str;
    }

    public static String makeMinLen(String str, Integer len) {
        return makeMinLen(str, len, "0");
    }

    /**
     * @param    referenceStr    String to be cleaned
     * @param    removeOnceStr    Sub string to be removed only once (first occurrence)
     * @return The cleaned string
     */
    public static String cleanReference(String referenceStr, String removeOnceStr) {
        if (!removeOnceStr.equals("") && referenceStr.contains(removeOnceStr)) {
            return referenceStr.replaceFirst(removeOnceStr, "").trim();
        }

        return referenceStr.trim();
    }

    /**
     * @param    referenceStr    String to be cleaned
     * @param    removeOnceStr    Sub string to be removed only once (first occurrence)
     * @param    removeEachStrs    Sub strings to be removed allover
     * @return The cleaned string
     */
    public static String cleanReference(String referenceStr, String removeOnceStr, String[] removeEachStrs) {
        referenceStr = cleanReference(referenceStr, removeOnceStr);
        referenceStr = UtilsString.removeSubStrings(referenceStr, removeEachStrs);

        return referenceStr.trim();
    }

    /**
     * Cleanup and concatenate given postfix
     *
     * @param    referenceStr    String to be cleaned
     * @param    removeOnceStr    Sub string to be removed only once (first occurrence)
     * @param    removeEachStr    Sub strings to be removed allover
     * @param    postfix            String to concatenated at end
     * @return The cleaned string
     */
    public static String cleanReference(String referenceStr, String removeOnceStr, String[] removeEachStr, String postfix) {
        return cleanReference(referenceStr, removeOnceStr, removeEachStr) + postfix;
    }
}
