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
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class ParserDateTime {

	/**
	 * Get date / time items
	 *
	 * @param	e	Action system event
	 * @return		List of PHP items
	 */
	public static List<String> getReferenceItems(AnActionEvent e) {
		List<String> referenceItems = new ArrayList<String>();

		Date date = new Date();

		referenceItems.add(new SimpleDateFormat("yyyy-MM-dd").format(date));
		referenceItems.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
			// Current UNIX timestamp
		referenceItems.add( Long.toString( System.currentTimeMillis() ) );
		referenceItems.add( Long.toString( System.currentTimeMillis() / 1000) );

		return referenceItems;
	}

}