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
import com.kstenschke.referencer.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ParserFilesFolders {

	/**
	 * Get items regarding files / folder / paths
	 *
	 * @param	e	Action system event
	 * @return		List of PHP items
	 */
	public static List<String> getReferenceItems(AnActionEvent e) {
		List<String> referenceItems = new ArrayList<String>();

		final Project project	= e.getData(PlatformDataKeys.PROJECT);
		Editor editor			= e.getData(PlatformDataKeys.EDITOR);

		if( project != null && editor != null ) {
			final Document document = editor.getDocument();


			// Get line number the caret is in
			int caretOffset	= editor.getCaretModel().getOffset();
			int lineNumber	= document.getLineNumber( caretOffset );

				// File path and name
			VirtualFile file	= FileDocumentManager.getInstance().getFile(document);
			String filePath		= (file != null) ? file.getPath() : "";
			String filename		= (file != null) ? file.getName() : "";


				// Add items
			FileEditorManager fileEditorManager	= FileEditorManager.getInstance(project);
			int amountOpenFiles	= fileEditorManager.getOpenFiles().length;
			if( amountOpenFiles > 1 ) {
				referenceItems.add( "List of currently opened files" );
			}

			referenceItems.add(filePath);
			referenceItems.add(filePath + "::" + (lineNumber+1));

			referenceItems.add(filename);
			referenceItems.add(filename + "::" + (lineNumber+1));
		}

		return referenceItems;
	}



	/**
	 * @param 	fileEditorManager	FileEditorManager
	 * @return	String with concatenated list of all files that are opened currently
	 */
	public static String getAllOpenedFiles(FileEditorManager fileEditorManager) {
		String allOpenFiles	= "";

		List<VirtualFile> openFiles = Arrays.asList( fileEditorManager.getOpenFiles() );
		for (VirtualFile iteratedOpenFile : openFiles) {
			String curFilePath	= iteratedOpenFile.toString().replace("file://", "");
			allOpenFiles = allOpenFiles + "\n" + curFilePath;
		}

		return allOpenFiles;
	}

}