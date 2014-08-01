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

import java.util.ArrayList;
import java.util.List;

public class GoToReferencerMethods {

    /**
     * @return  String[]
     */
    public static String[] getMethodItems(Document document, String fileExtension) {
        String[] referencesArr;
        List<String> methodItems= new ArrayList<String>();
        methodItems.add(StaticTexts.POPUP_SECTION_FUNCTIONS);

        String documentText = document.getText();

        List<String> functions = null;
        if( UtilsFile.isJavaScriptFileExtension(fileExtension) ) {
            functions = ParserJavaScript.getAllMethodsInText(documentText);
        } else if( UtilsFile.isPhpFileExtension(fileExtension) ) {
            functions = ParserPhp.getAllMethodsInText(documentText);
        }

        List<Integer> methodLineNumbers = new ArrayList<Integer>();
        if( functions != null ) {
            for( String functionName : functions ) {
//            if( curBookmark.getFile().equals(this.file) ) {
//                bookmarkLineNumbers.add(curBookmark.getLine());
//            }
                methodItems.add( functionName );
            }
        }

        if( methodLineNumbers.size() > 0 || true ) {
//            int digits  = Collections.max(methodLineNumbers).toString().length();
//            Integer[] lineNumbersArr= methodLineNumbers.toArray( new Integer[methodLineNumbers.size()] );
//            Arrays.sort(lineNumbersArr);
//
//            int index   = 1;
//            for( Integer curLineNum : lineNumbersArr ) {
//                if( curLineNum > 0 ) {
//                    int offsetLineStart = this.document.getLineStartOffset(curLineNum);
//                    int offsetLineEnd   = this.document.getLineEndOffset(curLineNum);
//                    String lineSummary = this.getLineSummary(documentText.substring(offsetLineStart, offsetLineEnd));
//                    methodItems.add(index, UtilsString.makeMinLen(Integer.toString(curLineNum), digits) + ": " + lineSummary);
//                    index++;
//                }
//            }

            referencesArr = methodItems.toArray( new String[methodItems.size()] );
        }

        return referencesArr;
    }

}