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
package com.kstenschke.referencer.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.IPopupChooserBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBList;
import com.intellij.util.Consumer;
import com.kstenschke.referencer.Preferences;
import com.kstenschke.referencer.referencers.insertOrCopy.InsertOrCopyReferencer;
import com.kstenschke.referencer.resources.StaticTexts;
import com.kstenschke.referencer.resources.ui.DividedListCellRenderer;
import com.kstenschke.referencer.utils.UtilsFile;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;

abstract class ReferencePopupAbleAction extends AnAction {

    public static final int ACTION_COPY = 0;
    public static final int ACTION_INSERT = 1;
    public static final int ACTION_GO = 2;

    private int actionType_;

    protected void launchPopup(@Nullable final Object[] references, Project project, Editor editor, int actionType) {
        if (references == null) {
            return;
        }

        final Document document = editor.getDocument();
        final String fileExtension = UtilsFile.getExtensionByDocument(document);

        final JBList<Object> referencesList = new JBList<>(references);
        buildPopup(project, editor, references, referencesList, fileExtension, true, actionType);
    }

    public ReferencePopupAbleAction() {
        super();
    }

    protected @NotNull JBPopup buildPopup(final Project project, final Editor editor,
                                          final Object[] references, final JBList<Object> referencesList,
                                          final String fileExtension, final boolean show,
                                          @Nullable Integer actionType) {
        if (actionType != null) {
            actionType_ = actionType;
        }

        ArrayList<String> rList = new ArrayList<>();
        int index = 0;
        for (Object reference : references) {
            rList.add(index + "=>" + reference.toString());
            ++index;
        }

        final IPopupChooserBuilder<String> popupChooserBuilder =
            JBPopupFactory.getInstance().createPopupChooserBuilder(rList);

        popupChooserBuilder.setRenderer(new DividedListCellRenderer(referencesList));
        popupChooserBuilder.setTitle(getPopupTitle());

        popupChooserBuilder.setItemSelectedCallback((Consumer<Object>) o -> 
            popupItemSelectedCallback(fileExtension, rList, popupChooserBuilder, o));

        popupChooserBuilder.setItemChosenCallback((Consumer<Object>) item ->
            popupItemChosenCallback(project, editor, references, item));

        JBPopup popup = popupChooserBuilder.createPopup();

        if (show) {
            popup.showCenteredInCurrentWindow(project);
        }

        return popup;
    }

    private void popupItemChosenCallback(Project project, Editor editor, Object[] references, Object item) {
        if (item == null) {
            return;
        }

        String value = item.toString();
        if (value != null && value.contains("=>")) {
            int selectedIndex = Integer.parseInt(value.substring(0, value.indexOf("=>")));
            fireSelectPopupItemAction(project, editor, references, selectedIndex);
        }

        editor.getContentComponent().grabFocus();
    }

    private void popupItemSelectedCallback(String fileExtension, ArrayList<String> rList, 
                                           IPopupChooserBuilder<String> popupChooserBuilder, Object item) {
        if (item == null) {
            return;
        }

        String value = item.toString();
        if (value != null && value.contains(StaticTexts.POPUP_ITEM_PREFIX_SECTION_TITLE)) {
            int prevSelectedIndex = Preferences.getSelectedIndex(fileExtension);
            int selectedIndex = Integer.parseInt(value.substring(0, value.indexOf("=>")));
            if (prevSelectedIndex < selectedIndex) {
                ++selectedIndex;
                if (selectedIndex == rList.size()) {
                    selectedIndex = 1;
                }
            } else {
                --selectedIndex;
                if (selectedIndex < 2) {
                    selectedIndex = rList.size() - 1;
                }
            }
            popupChooserBuilder.setSelectedValue(rList.get(selectedIndex), true);
            Preferences.saveSelectedIndex(fileExtension, selectedIndex);
        }
    }

    private void fireSelectPopupItemAction(Project project, Editor editor, Object[] references, int index) {
        switch (actionType_) {
            case ACTION_COPY:
                StringSelection clipString = new StringSelection(InsertOrCopyReferencer.fixReferenceValue(
                        project, editor, references[index].toString()));
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(clipString, null);
                break;
            case ACTION_INSERT:
                int caretOffset = editor.getCaretModel().getOffset();

                String insertString = InsertOrCopyReferencer.fixReferenceValue(
                        project, editor, references[index].toString());

                WriteCommandAction.runWriteCommandAction(project, () ->
                    editor.getDocument().insertString(caretOffset, insertString));

                editor.getCaretModel().moveToOffset(caretOffset + insertString.length());
                break;
            case ACTION_GO:
                int lineNumber = Integer.parseInt(references[index].toString().split(":")[0]);
                editor.getCaretModel().moveToOffset(editor.getDocument().getLineStartOffset(lineNumber - 1), true);
                editor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
            default:
        }
    }

    @NotNull
    private String getPopupTitle() {
        switch (actionType_) {
            case ACTION_COPY:
                return StaticTexts.POPUP_TITLE_ACTION_COPY;
            case ACTION_INSERT:
                return StaticTexts.POPUP_TITLE_ACTION_INSERT;
            case ACTION_GO:
            default:
                return StaticTexts.POPUP_TITLE_ACTION_GO;
        }
    }
}
