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

package com.kstenschke.referencer.utils;


import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.ArrayList;
import java.util.List;

public class Parser {

	/**
	 * @param	e	Action system event
	 * @return		Items of the events context
	 */
	public static Object[] getItems(AnActionEvent e) {
		Object[] refArr;

		final Project project	= e.getData(PlatformDataKeys.PROJECT);
		Editor editor			= e.getData(DataKeys.EDITOR);

		if( project != null && editor != null ) {
			final Document document = editor.getDocument();

				// File path and name
			VirtualFile file	= FileDocumentManager.getInstance().getFile(document);
			String filePath		= (file != null) ? file.getPath() : "";
			String filename		= (file != null) ? file.getName() : "";
			String fileExtension= (file != null) ? file.getExtension() : "";
			if( fileExtension != null && fileExtension.length() > 0) fileExtension = fileExtension.toLowerCase();

				// Get line number the caret is in
			int caretOffset	= editor.getCaretModel().getOffset();
			int lineNumber	= document.getLineNumber( caretOffset );

			SelectionModel selectionModel	= editor.getSelectionModel();
			int selStart= selectionModel.getSelectionStart();
			int selEnd	= selectionModel.getSelectionEnd();

			int lineSelStart	= document.getLineNumber(selStart);
			int lineSelEnd		= document.getLineNumber(selEnd);


				// Setup list of items
			List<String> referenceItems = new ArrayList<String>();

				// Add common items of all file types
			referenceItems.add(filename + "::" + (lineNumber+1));
			referenceItems.add(filePath + "::" + (lineNumber+1));

			if( selectionModel.hasSelection() && lineSelEnd > lineSelStart ) referenceItems.add(filePath + " / Selection: " + (lineSelStart+1) + " - " + (lineSelEnd+1));

				// Add JavaScript items
			if( fileExtension != null && fileExtension.equals("js") ) referenceItems.addAll(ParserJavascript.getReferenceItems(e));

				// Add PHP items
			else if( FileUtils.isPhpFileExtension(fileExtension) ) referenceItems.addAll(ParserPhp.getReferenceItems(e));


			refArr = referenceItems.toArray();
		} else {
			refArr	= null;
		}

		return refArr;
	}

}
