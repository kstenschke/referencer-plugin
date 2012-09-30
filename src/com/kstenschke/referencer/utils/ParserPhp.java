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
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserPhp {

	/**
	 * Parse document of given event for PHP references and return them
	 *
	 * @param	e	Action system event
	 * @return		List of PHP items
	 */
	public static List<String> getReferenceItems(AnActionEvent e) {
		List<String> referenceItems = new ArrayList<String>();


		final Project project	= e.getData(PlatformDataKeys.PROJECT);
		Editor editor			= e.getData(DataKeys.EDITOR);

		if( project != null && editor != null ) {
			final Document document = editor.getDocument();


				// Get line number the caret is in
			int caretOffset	= editor.getCaretModel().getOffset();
			String textFull = document.getText();
			String textBeforeCaret	= textFull.substring(0, caretOffset);
			String textAfterCaret	= textFull.substring(caretOffset);


				// Add class before caret
			String classBefore = null;
			List<String> allClassBeforeCaret	= getAllClassInText(textBeforeCaret);
			if( allClassBeforeCaret.size() > 0 ) {
				classBefore	= cleanupClassName(allClassBeforeCaret.get(allClassBeforeCaret.size() - 1));
				referenceItems.add(classBefore);
			}
				// Add class after caret
			String classAfter = null;
			List<String> allClassAfterCaret	= getAllClassInText(textAfterCaret);
			if( allClassAfterCaret.size() > 0 ) {
				classAfter	= cleanupClassName(allClassAfterCaret.get( 0 ));
				referenceItems.add(classAfter);
			}
				// Add method before caret
			String methodBefore = null;
			List<String> allMethodsBeforeCaret	= getAllMethodsInText(textBeforeCaret);
			if( allMethodsBeforeCaret.size() > 0 ) {
				methodBefore	= cleanupMethodName(allMethodsBeforeCaret.get( allMethodsBeforeCaret.size()-1 ));
				referenceItems.add(methodBefore);
			}
				// Add method after caret
			String methodAfter = null;
			List<String> allMethodsAfterCaret	= getAllMethodsInText(textAfterCaret);
			if( allMethodsAfterCaret.size() > 0 ) {
				methodAfter	= cleanupMethodName(allMethodsAfterCaret.get(0));
				referenceItems.add( methodAfter );
			}

				// Add method before caret / prev. variable
			String varBefore = null;
			List<String> allVarsBeforeCaret	= getAllVariablesInText(textBeforeCaret);
			if( allVarsBeforeCaret.size() > 0 ) {
				varBefore	= allVarsBeforeCaret.get( allVarsBeforeCaret.size()-1 );
				if( methodBefore != null && varBefore != null ) {
					referenceItems.add( methodBefore + " / " + varBefore );
				}
			}

				// Add classBefore::methodBefore caret
			if( classBefore != null && methodBefore != null ) {
				referenceItems.add( classBefore + "::" + methodBefore );
			}

				// Add variable before caret
			if( varBefore != null ) {
				referenceItems.add(varBefore);
			}
				// Add variable after caret
			String varAfter	= null;
			List<String> allVarsAfterCaret	= getAllVariablesInText(textAfterCaret);
			if( allVarsAfterCaret.size() > 0 ) {
				varAfter	= allVarsAfterCaret.get( 0 );
				if( !varAfter.equals(varBefore) ) {
					referenceItems.add(varAfter);
				}
			}
		}

		return referenceItems;
	}



	/**
	 * Get all PHP variable names in the order of their appearance in the given text, but each item only once
	 *
	 * @param	text	Source code to be searched
	 * @return			All found PHP variables
	 */
	private static List<String> getAllVariablesInText(String text) {
		List<String> allMatches = new ArrayList<String>();
		Matcher m = Pattern.compile("\\$[a-zA-Z0-9_]+").matcher(text);

		while (m.find()) {
			if( !allMatches.contains(m.group())) {
				allMatches.add(m.group());
			}
		}

		return allMatches;
	}



	/**
	 * Get all PHP method names in the order of their appearance in the given text, but each item only once
	 *
	 * @param	text	Source code to be searched
	 * @return			All found PHP method names
	 */
	private static List<String> getAllMethodsInText(String text) {
		List<String> allMatches = new ArrayList<String>();
		Matcher m = Pattern.compile("function.*[a-zA-Z0-9_]+\\(").matcher(text);

		while (m.find()) {
			if( !allMatches.contains(m.group())) {
				allMatches.add(m.group());
			}
		}

		return allMatches;
	}



	/**
	 * Get all PHP class names in the order of their appearance in the given text, but each item only once
	 *
	 * @param	text	Source code to be searched
	 * @return			All found PHP class names
	 */
	private static List<String> getAllClassInText(String text) {
		List<String> allMatches = new ArrayList<String>();
		Matcher m = Pattern.compile("class.*[a-zA-Z0-9_]+").matcher(text);

		while (m.find()) {
			if( !allMatches.contains(m.group())) {
				allMatches.add(m.group());
			}
		}

		return allMatches;
	}

	
	
	/**
	 * Clean up method name
	 *
	 * @param	methodName	Method name to be cleaned
	 * @return              Cleaned method name
	 */
	private static String cleanupMethodName(String methodName) {
		String[] removeEachStrs	= {"("};

		return StringUtils.cleanReference(methodName, "function", removeEachStrs, "();");
	}



	/**
	 * Clean up method name
	 *
	 * @param	className	Class name to be cleaned
	 * @return				Cleaned class name
	 */
	private static String cleanupClassName(String className) {
		return StringUtils.cleanReference(className, "class");
	}

}
