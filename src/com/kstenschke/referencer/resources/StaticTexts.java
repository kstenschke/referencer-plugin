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
package com.kstenschke.referencer.resources;

import org.jetbrains.annotations.NonNls;

public class StaticTexts {

    @NonNls public static final String SETTINGS_DISPLAY_NAME = "Referencer";

    // Popup titles
    @NonNls public static final String POPUP_TITLE_ACTION_COPY = "Select what to copy";
    @NonNls public static final String POPUP_TITLE_ACTION_INSERT = "Select Insertion";
    @NonNls public static final String POPUP_TITLE_ACTION_GO = "Select where to go";

    // Popup items
    @NonNls public static final String POPUP_ITEM_METHODS_IN_FILE = "List of methods in current file";
    public static final String POPUP_ITEM_OPEN_FILES = "List of currently opened files";

    // Popup section titles
    @NonNls public static final String POPUP_ITEM_PREFIX_SECTION_TITLE = "SECTIONTITLE:";
    @NonNls public static final String POPUP_SECTION_TITLE_DATE_TIME = "SECTIONTITLE: Date / Time";
    @NonNls public static final String POPUP_SECTION_TITLE_FILES_PATHS = "SECTIONTITLE: Files / Paths";
    @NonNls public static final String POPUP_SECTION_TITLE_JAVASCRIPT = "SECTIONTITLE: JavaScript";
    @NonNls public static final String POPUP_SECTION_TITLE_MARKDOWN = "SECTIONTITLE: Markdown";
    @NonNls public static final String POPUP_SECTION_TITLE_PHP = "SECTIONTITLE: PHP";
    @NonNls public static final String POPUP_SECTION_TITLE_TEXT_COMPLETIONS = "SECTIONTITLE: Text Completions";
    @NonNls public static final String POPUP_SECTION_BOOKMARKS = "SECTIONTITLE: Bookmarks";
    @NonNls public static final String POPUP_SECTION_FUNCTIONS = "SECTIONTITLE: Methods";

    // Go action context menu
    @NonNls public static final String POPUP_GO_REMOVE_ALL_BOOKMARKS = "Remove all Bookmarks from this File";

    // Notifications
    @NonNls public static final String NOTIFY_GOTO_NONE_FOUND = "No \"Go To...\" destinations found";
}