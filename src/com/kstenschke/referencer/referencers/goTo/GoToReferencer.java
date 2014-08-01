package com.kstenschke.referencer.referencers.goTo;

import com.kstenschke.referencer.utils.UtilsString;

public class GoToReferencer {


    /**
     * @param   lineText
     * @return  String
     */
    protected static String getLineSummary(String lineText) {
        return lineText.isEmpty() ? "" : UtilsString.crop(lineText.trim().replace("\t", " ").replace("  ", " "), 80);
    }
}
