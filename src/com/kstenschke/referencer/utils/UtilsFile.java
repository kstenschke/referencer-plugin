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
package com.kstenschke.referencer.utils;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UtilsFile {

    /**
     * Compare given extension against known PHP extensions
     *
     * @param  fileExtension    Extension to be checked
     * @return PHP extension?
     */
    public static Boolean isPhpFileExtension(String fileExtension) {
        if (fileExtension == null) {
            return false;
        }

        fileExtension = fileExtension.toLowerCase();

        return "func".equals(fileExtension)
                || "phpsh".equals(fileExtension)
                || "php".equals(fileExtension)
                || "php3".equals(fileExtension)
                || "php4".equals(fileExtension)
                || "php5".equals(fileExtension)
                || "phtml".equals(fileExtension);
    }

    /**
     * Compare given extension against known JavaScript extensions
     *
     * @param  fileExtension    Extension to be checked
     * @return JavaScript extension?
     */
    public static Boolean isJavaScriptFileExtension(String fileExtension) {
        return "js".equalsIgnoreCase(fileExtension);
    }

    public static Boolean isMarkdownFileExtension(String fileExtension) {
        return "md".equalsIgnoreCase(fileExtension);
    }

    public static String getExtensionByDocument(Document document) {
        VirtualFile file = FileDocumentManager.getInstance().getFile(document);
        String fileExtension = file != null ? file.getExtension() : "";

        return fileExtension != null && !fileExtension.isEmpty()
            ? fileExtension.toLowerCase()
            : fileExtension;
    }

    public static String getFileContents(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        } catch (IOException e) {
            return null;
        }

        return contentBuilder.toString();
    }
}
