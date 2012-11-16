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

package com.kstenschke.referencer.parser;


import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.kstenschke.referencer.FileUtils;
import com.kstenschke.referencer.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class Parser {

	/**
	 * @param	e	Action system event
	 * @return		Items of the events context
	 */
	public static Object[] getItems(AnActionEvent e) {
		Object[] refArr;

		final Project project	= e.getData(PlatformDataKeys.PROJECT);
		Editor editor			= e.getData(PlatformDataKeys.EDITOR);

		if( project != null && editor != null ) {
			final Document document = editor.getDocument();

				// File path and name
			VirtualFile file	= FileDocumentManager.getInstance().getFile(document);
			String filePath		= (file != null) ? file.getPath() : "";
			String fileExtension= (file != null) ? file.getExtension() : "";
			if( fileExtension != null && fileExtension.length() > 0) fileExtension = fileExtension.toLowerCase();

				// Get line number the caret is in
			int caretOffset	= editor.getCaretModel().getOffset();

			SelectionModel selectionModel	= editor.getSelectionModel();
			int selStart= selectionModel.getSelectionStart();
			int selEnd	= selectionModel.getSelectionEnd();

			int lineSelStart	= document.getLineNumber(selStart);
			int lineSelEnd		= document.getLineNumber(selEnd);

				// Grab the "word" before the caret
			String wordAtCaret	= StringUtils.getWordAtOffset(document.getCharsSequence(), caretOffset);



				// Setup list of items
			List<String> referenceItems = new ArrayList<String>();

				// Add date/timestamps
			referenceItems.add("SECTIONTITLE: Date / Time");
			referenceItems.addAll(ParserDateTime.getReferenceItems(e));

				// Add file / path items
			referenceItems.add("SECTIONTITLE: Files / Paths");
//			referenceItems.add("_");
			referenceItems.addAll(ParserFilesFolders.getReferenceItems(e));

				// Add selection info
			if( selectionModel.hasSelection() && lineSelEnd > lineSelStart ) {
				referenceItems.add(filePath + " / Selection: " + (lineSelStart+1) + " - " + (lineSelEnd+1));
			}

				// Add JavaScript items
			if( fileExtension != null && fileExtension.equals("js") ) {
//				referenceItems.add("_");
				referenceItems.add("SECTIONTITLE: JavaScript");
				referenceItems.addAll(ParserJavascript.getReferenceItems(e));
			}

				// Add PHP items
			else if( FileUtils.isPhpFileExtension(fileExtension) ) {
//				referenceItems.add("_");
				referenceItems.add("SECTIONTITLE: PHP");
				referenceItems.addAll(ParserPhp.getReferenceItems(e));
			}

				// Add all line-parts in current document that begin the same as the word at the caret
			if( wordAtCaret!= null && !wordAtCaret.isEmpty() && wordAtCaret.length() > 2 ) {
				String docText	= document.getText();
				String[] lineParts	= docText.split( wordAtCaret );

				if( lineParts.length > 0 ) {
//					referenceItems.add("_");

					List<String> listLineParts = Arrays.asList(lineParts);

					int count	= 0;
					for (String listLinePart : listLineParts) {
						if(count > 0 ) {
							listLinePart	= listLinePart.split("\\n")[0];

							if( !listLinePart.equals(wordAtCaret) && !listLinePart.trim().isEmpty()
								&& !referenceItems.contains(listLinePart)
							) {
								if( count == 1) referenceItems.add("SECTIONTITLE: Text Completions");
								referenceItems.add(listLinePart);
							}
						}
						count++;
					}
				}
			}

			refArr = referenceItems.toArray();
		} else {
			refArr	= null;
		}

		return refArr;
	}



	/**
	 * Process given reference item - if it's a type description "translate" to the respective value
	 *
	 * @param	project							IDEA project
	 * @param	itemString						Selected references list item value
	 * @return  String to be inserted/copied
	 */
	public static String fixReferenceValue(Project project, String itemString) {
		if( itemString.equals("List of currently opened files") ) {
			return ParserFilesFolders.getAllOpenedFiles(FileEditorManager.getInstance(project));
		}


		return itemString;
	}

}
