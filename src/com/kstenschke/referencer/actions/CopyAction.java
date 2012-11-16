/*
 * Copyright 2012 Kay Stenschke
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
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.vfs.VirtualFile;
import com.kstenschke.referencer.DividedList.DividedListCellKeyListener;
import com.kstenschke.referencer.DividedList.DividedListCellRenderer;
import com.kstenschke.referencer.DividedList.DividedListSelectionListener;
import com.kstenschke.referencer.parser.Parser;
import com.kstenschke.referencer.Preferences;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;


/**
 * Copy Action
 */
public class CopyAction extends AnAction {

	/**
	 * Show list with possible references and copy chosen item to clipboard
	 *
	 * @param	e	Action system event
	 */
	public void actionPerformed(final AnActionEvent e) {
		final Project project	= e.getData(PlatformDataKeys.PROJECT);
		Editor editor			= e.getData(PlatformDataKeys.EDITOR);

		if( project != null && editor != null ) {
			final Object[] refArr = Parser.getItems(e);
			if( refArr != null ) {
				final JList referencesList = new JList(refArr);
				referencesList.setCellRenderer(new DividedListCellRenderer() );

				DividedListCellKeyListener keyListener	= new DividedListCellKeyListener();
				referencesList.addKeyListener(keyListener);
				referencesList.addListSelectionListener(new DividedListSelectionListener(keyListener));

				final Document document = editor.getDocument();
				VirtualFile file		= FileDocumentManager.getInstance().getFile(document);
				final String fileExtension	= (file != null) ? file.getExtension() : "";

					// Preselect item from preferences
				Integer selectedIndex	= Preferences.getSelectedIndex(fileExtension);
				if( selectedIndex > refArr.length ) selectedIndex	= 0;
				referencesList.setSelectedIndex(selectedIndex);

				PopupChooserBuilder popup = JBPopupFactory.getInstance().createListPopupBuilder(referencesList);

				popup.setTitle("Select reference to be copied").setItemChoosenCallback(new Runnable() {
					public void run() {

						// Callback when item chosen
					CommandProcessor.getInstance().executeCommand(project, new Runnable() {
						public void run() {
						final int index = referencesList.getSelectedIndex();

							// Store preferences
						Preferences.saveSelectedIndex(fileExtension, index);

							// Copy item to clipboard
						StringSelection clipString	= new StringSelection( Parser.fixReferenceValue(project, refArr[index].toString()) );
						Clipboard clipboard			= Toolkit.getDefaultToolkit().getSystemClipboard();

						clipboard.setContents(clipString, null);
						}
					},
				null, null);
				}
			}).setMovable(true).createPopup().showCenteredInCurrentWindow(project);

			}
		}

	}

}
