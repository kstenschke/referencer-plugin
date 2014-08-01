package com.kstenschke.referencer.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @todo    change into PSI based parser one day
 */
public class ParserPhp {

    /**
     * Get all PHP variable names in the order of their appearance in the given text, but each item only once
     *
     * @param	text	Source code to be searched
     * @return			All found PHP variables
     */
    public static List<String> getAllVariablesInText(String text) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("\\$[a-zA-Z0-9_]+").matcher(text);

        while (m.find()) {
            if( !allMatches.contains(m.group())) {
                allMatches.add(m.group());
            }
        }

        return allMatches;
    }

    /**
     * Get all PHP method names in the order of their appearance in the given text, but each item only once
     *
     * @param	text	Source code to be searched
     * @return			All found PHP method names
     */
    public static List<String> getAllMethodsInText(String text) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("function.*[a-zA-Z0-9_]+\\(").matcher(text);

        while (m.find()) {
            if( !allMatches.contains(m.group())) {
                allMatches.add(m.group());
            }
        }

        return allMatches;
    }

    /**
     * Get all PHP class names in the order of their appearance in the given text, but each item only once
     *
     * @param	text	Source code to be searched
     * @return			All found PHP class names
     */
    public static List<String> getAllClassNamesInText(String text) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("class.*[a-zA-Z0-9_]+").matcher(text);

        while (m.find()) {
            if( !allMatches.contains(m.group())) {
                allMatches.add(m.group());
            }
        }

        return allMatches;
    }
}
