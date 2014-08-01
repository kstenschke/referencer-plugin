package com.kstenschke.referencer.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @todo    change into PSI based parser one day
 */
public class ParserJavaScript {

    /**
     * Get all JavaScript method names in the order of their appearance in the given text, but each item only once
     *
     * @param	text	Source code to be searched
     * @return			All found PHP method names
     */
    public static List<String> getAllMethodsInText(String text) {
        // 1. Find matches ala: "function methodname(", if any found return it
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("function.*[a-zA-Z0-9_]+\\(").matcher(text);

        while (m.find()) {
            if( !allMatches.contains(m.group())) {
                allMatches.add(m.group());
            }
        }

        // No matches found? look for OOP style methods, ala: "methodname: function("
        if( allMatches.size() == 0) {
            m = Pattern.compile("[a-zA-Z0-9_]+:(\\s)*function.*\\(").matcher(text);

            while (m.find()) {
                if( !allMatches.contains(m.group())) {
                    allMatches.add(m.group());
                }
            }
        }

        return allMatches;
    }

    /**
     * @param	text	Source code to be searched
     * @return			All found PHP class names
     */
    public static List<String> getAllClassNamesInText(String text) {
        // Look for "@class" annotations
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("@class(\\s)*([a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*)").matcher(text);

        while (m.find()) {
            if( !allMatches.contains(m.group())) {
                allMatches.add(m.group());
            }
        }
        // Nothing found? look for "...Class.create("
        if( allMatches.size() == 0 ) {
            m = Pattern.compile("[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\\s*=\\s*Class\\.create\\(").matcher(text);

            while (m.find()) {
                if( !allMatches.contains(m.group())) {
                    allMatches.add(m.group());
                }
            }
        }

        return allMatches;
    }

    /**
     * Get all JS namespace in the order of their appearance, defined as doc-annotations in the given text, but each item only once
     *
     * @param	text	Source code to be searched
     * @return			All found PHP class names
     */
    public static List<String> getAllNamespaceInText(String text) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("@namespace(\\s)*([a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*)").matcher(text);

        while (m.find()) {
            if( !allMatches.contains(m.group())) {
                allMatches.add(m.group());
            }
        }

        return allMatches;
    }

}
