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
package com.kstenschke.referencer;

import com.intellij.ide.util.PropertiesComponent;
import com.kstenschke.referencer.utils.UtilsFile;
import org.jetbrains.annotations.NonNls;

/**
 * Utility functions for preferences handling
 * All preferences of the Referencer plugin are stored on application level (not per project)
 */
public class Preferences {

    /*  @NonNls = element is not a string requiring internationalization and it does not contain such strings. */
    @NonNls public static final String PROPERTY_PATTERNS_REPLACE = "PluginReferencer.ReplacePatterns";
    @NonNls private static final String PROPERTY_PATTERNS_GOTO = "PluginReferencer.GoToPatterns";
    @NonNls private static final String PROPERTY_REFERENCER_INDEX = "PluginReferencer.SelectedIndex";
    @NonNls private static final String PROPERTY_REFERENCER_INDEX_PHP = "PluginReferencer.SelectedIndexPHP";
    @NonNls private static final String PROPERTY_REFERENCER_INDEX_JS = "PluginReferencer.SelectedIndexJS";
    @NonNls private static final String PROPERTY_GOTO_LIST_BOOKMARKS = "PluginReferencer.GotoListBookmarks";
    @NonNls private static final String PROPERTY_GOTO_LIST_PHP_METHODS = "PluginReferencer.GotoListPhpMethods";
    @NonNls private static final String PROPERTY_GOTO_LIST_JS_METHODS = "PluginReferencer.GotoListJsMethods";

    public static void saveGoToPatterns(String patterns) {
        PropertiesComponent.getInstance().setValue(PROPERTY_PATTERNS_GOTO, patterns);
    }

    public static void saveReplacePatterns(String patterns) {
        PropertiesComponent.getInstance().setValue(PROPERTY_PATTERNS_REPLACE, patterns);
    }

    public static void saveShowBookmarksInGoToList(boolean doShow) {
        PropertiesComponent.getInstance().setValue(PROPERTY_GOTO_LIST_BOOKMARKS, doShow ? "1" : "0");
    }

    public static void saveShowPhpMethodsInGoToList(boolean doShow) {
        PropertiesComponent.getInstance().setValue(PROPERTY_GOTO_LIST_PHP_METHODS, doShow ? "1" : "0");
    }

    public static void saveShowJsMethodsInGoToList(boolean doShow) {
        PropertiesComponent.getInstance().setValue(PROPERTY_GOTO_LIST_JS_METHODS, doShow ? "1" : "0");
    }

    public static String getGoToPatterns() {
        String patterns = PropertiesComponent.getInstance().getValue(PROPERTY_PATTERNS_GOTO);

        return patterns != null ? patterns.trim() : "";
    }

    public static String getReplacePatterns() {
        String patterns = PropertiesComponent.getInstance().getValue(PROPERTY_PATTERNS_REPLACE);

        return patterns != null ? patterns.trim() : "";
    }

    public static boolean getShowBookmarksInGoToList() {
        String doShow = PropertiesComponent.getInstance().getValue(PROPERTY_GOTO_LIST_BOOKMARKS);

        return doShow != null && doShow.equals("1");
    }

    public static boolean getShowPhpMethodsInGoToList() {
        String doShow = PropertiesComponent.getInstance().getValue(PROPERTY_GOTO_LIST_BOOKMARKS);

        return doShow != null && doShow.equals("1");
    }

    public static boolean getShowJsMethodsInGoToList() {
        String doShow = PropertiesComponent.getInstance().getValue(PROPERTY_GOTO_LIST_BOOKMARKS);

        return doShow != null && doShow.equals("1");
    }

    /**
     * Store referencer preferences: selected index per supported file type
     *
     * @param fileExtension File extension of file open while using referencer
     * @param selectedIndex Selected item index
     */
    public static void saveSelectedIndex(String fileExtension, Integer selectedIndex) {
        String preferenceIdentifier = getPropertyIdentifierByFileExtension(fileExtension);

        PropertiesComponent.getInstance().setValue(preferenceIdentifier, selectedIndex.toString());
    }

    /**
     * Get identifier for referencer preference of given file extension
     *
     * @param fileExtension File extension of file open while using referencer
     * @return Preference identifier
     */
    private static String getPropertyIdentifierByFileExtension(String fileExtension) {
        if (UtilsFile.isPhpFileExtension(fileExtension)) {
            return PROPERTY_REFERENCER_INDEX_PHP;
        }

        if (UtilsFile.isJavaScriptFileExtension(fileExtension)) {
            return PROPERTY_REFERENCER_INDEX_JS;
        }

        return PROPERTY_REFERENCER_INDEX;
    }

    /**
     * @param fileExtension Extension of file open while invoking referencer
     * @return Integer
     */
    public static Integer getSelectedIndex(String fileExtension) {
        String preferenceIdentifier = getPropertyIdentifierByFileExtension(fileExtension);

        String preferredIndex = PropertiesComponent.getInstance().getValue(preferenceIdentifier);
        return preferredIndex == null || "".equals(preferredIndex) ? 0 : Integer.parseInt(preferredIndex);
    }
}
