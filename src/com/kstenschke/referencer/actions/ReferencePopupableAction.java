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
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.ui.components.JBList;
import com.kstenschke.referencer.Preferences;
import com.kstenschke.referencer.listeners.DividedListSelectionListener;
import com.kstenschke.referencer.referencers.insertOrCopy.InsertOrCopyReferencer;
import com.kstenschke.referencer.resources.StaticTexts;
import com.kstenschke.referencer.resources.ui.DividedListCellRenderer;
import com.kstenschke.referencer.utils.UtilsFile;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

abstract class ReferencePopupableAction extends AnAction {

    public static final int MODE_COPY = 0;
    public static final int MODE_INSERT = 1;
    public static final int MODE_GO = 2;

    private int mode_;

    protected void launchPopup(@Nullable final Object[] references, Project project, Editor editor, int mode) {
        if (references == null) {
            return;
        }

        final JBList<Object> referencesList = new JBList<>(references);
        referencesList.setCellRenderer(new DividedListCellRenderer(referencesList));
        referencesList.addListSelectionListener(new DividedListSelectionListener());

        final Document document = editor.getDocument();
        final String fileExtension = UtilsFile.getExtensionByDocument(document);

        int selectedIndex = Preferences.getSelectedIndex(fileExtension);
        if (selectedIndex > references.length || selectedIndex == 0) {      /* Preselect item from preferences */
            selectedIndex = 1;
        }
        referencesList.setSelectedIndex(selectedIndex);

        buildPopup(project, editor, references, referencesList, fileExtension, true, mode);
    }

    protected PopupChooserBuilder<Object> buildPopup(final Project project, final Editor editor,
                                                   final Object[] references, final JList<Object> referencesList,
                                                   final String fileExtension, final boolean show,
                                                   @Nullable Integer mode) {
        if (mode != null) {
            mode_ = mode;
        }

        PopupChooserBuilder<Object> popup = JBPopupFactory.getInstance().createListPopupBuilder(referencesList);

        popup.setTitle(getPopupTitle()).setItemChoosenCallback(() ->
                CommandProcessor.getInstance().executeCommand(project, () -> {      /* Callback when item chosen */
                    final int index = referencesList.getSelectedIndex();
                    Preferences.saveSelectedIndex(fileExtension, index);
                    fireSelectPopupItemAction(project, editor, references, referencesList, index);
                    editor.getContentComponent().grabFocus();
                }, null, null));

        if (show) {
            popup.setMovable(true).createPopup().showCenteredInCurrentWindow(project);
        }

        return popup;
    }

    private void fireSelectPopupItemAction(Project project, Editor editor, Object[] references,
                                           JList<Object> referencesList, int index) {
        switch (mode_) {
            case MODE_COPY:
                StringSelection clipString = new StringSelection(InsertOrCopyReferencer.fixReferenceValue(
                        project, editor, references[index].toString()));
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(clipString, null);
                break;
            case MODE_INSERT:
                int caretOffset = editor.getCaretModel().getOffset();

                String insertString = InsertOrCopyReferencer.fixReferenceValue(
                        project, editor, references[index].toString());

                editor.getDocument().insertString(caretOffset, insertString);
                editor.getCaretModel().moveToOffset(caretOffset + insertString.length());
                break;
            case MODE_GO:
                int lineNumber = Integer.parseInt(referencesList.getSelectedValue().toString().split(":")[0]);
                editor.getCaretModel().moveToOffset(editor.getDocument().getLineStartOffset(lineNumber - 1), true);
                editor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
            default:
        }
    }

    @NotNull
    private String getPopupTitle() {
        switch (mode_) {
            case MODE_COPY:
                return StaticTexts.POPUP_TITLE_ACTION_COPY;
            case MODE_INSERT:
                return StaticTexts.POPUP_TITLE_ACTION_INSERT;
            case MODE_GO:
            default:
                return StaticTexts.POPUP_TITLE_ACTION_GO;
        }
    }
}
