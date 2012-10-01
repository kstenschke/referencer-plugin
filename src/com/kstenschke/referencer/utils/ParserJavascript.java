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
import com.intellij.openapi.project.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserJavascript {

	/**
	 * Parse document of given event for JavaScript references and return them
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
			String textFull = document.getText();
			String textBeforeCaret	= textFull.substring(0, caretOffset);
			String textAfterCaret	= textFull.substring(caretOffset);


				// Add namespace
			String namespaceBefore = null;
			List<String> allNamespaces	= getAllNamespaceInText(textBeforeCaret);
			if( allNamespaces.size() > 0 ) {
				namespaceBefore	= cleanupNamespaceName(allNamespaces.get(allNamespaces.size() - 1));
				referenceItems.add(namespaceBefore);
			}

				// Add classname
			String classnameBefore = null;
			List<String> allClassnames	= getAllClassnamesInText(textBeforeCaret);
			if( allClassnames.size() > 0 ) {
				classnameBefore	= cleanupClassname(allClassnames.get(allClassnames.size() - 1));
				referenceItems.add(classnameBefore);
			}
				// Add namespace + classname
			String namespacedClassname	= null;
			if( namespaceBefore != null && classnameBefore != null ) {
				namespacedClassname	= namespaceBefore + "." + classnameBefore;
				referenceItems.add(namespacedClassname);
			}


				// Add method before caret
			String methodBefore = null;
			List<String> allMethodsBeforeCaret	= getAllMethodsInText(textBeforeCaret);
			if( allMethodsBeforeCaret.size() > 0 ) {
				methodBefore	= cleanupMethodName(allMethodsBeforeCaret.get( allMethodsBeforeCaret.size()-1 ));
			}
				// Add method after caret
			String methodAfter = null;
			List<String> allMethodsAfterCaret	= getAllMethodsInText(textAfterCaret);
			if( allMethodsAfterCaret.size() > 0 ) {
				methodAfter	= cleanupMethodName(allMethodsAfterCaret.get(0));
			}

				// Add namespace.classname.methodBefore
				// -or classname.methodBefore if no namespace found
			if( classnameBefore != null && methodBefore != null ) {
				if( namespacedClassname != null ) {
					referenceItems.add( namespacedClassname + "." + methodBefore );
				}
					// Classname.method
				referenceItems.add( classnameBefore + "." + methodBefore );
			}


			if(methodBefore != null ) referenceItems.add(methodBefore);
			if(methodAfter != null ) referenceItems.add(methodAfter);
		}

		return referenceItems;
	}



	/**
	 * Get all JavaScript method names in the order of their appearance in the given text, but each item only once
	 *
	 * @param	text	Source code to be searched
	 * @return			All found PHP method names
	 */
	private static List<String> getAllMethodsInText(String text) {
			// 1. Find matches ala: "function methodname(", if any found return it
		List<String> allMatches = new ArrayList<String>();
		Matcher m = Pattern.compile("function.*[a-zA-Z0-9_]+\\(").matcher(text);

		while (m.find()) {
			if( !allMatches.contains(m.group())) {
				allMatches.add(m.group());
			}
		}

			// No matches found? look for OOP style methods, ala: "methodname: function("
		if( allMatches.size() == 0) {
			m = Pattern.compile("[a-zA-Z0-9_]+:(\\s)*function.*\\(").matcher(text);

			while (m.find()) {
				if( !allMatches.contains(m.group())) {
					allMatches.add(m.group());
				}
			}
		}

		return allMatches;
	}



	/**
	 * @param	text	Source code to be searched
	 * @return			All found PHP class names
	 */
	private static List<String> getAllClassnamesInText(String text) {
			// Look for "@class" annotations
		List<String> allMatches = new ArrayList<String>();
		Matcher m = Pattern.compile("@class(\\s)*([a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*)").matcher(text);

		while (m.find()) {
			if( !allMatches.contains(m.group())) {
				allMatches.add(m.group());
			}
		}
			// Nothing found? look for "...Class.create("
		if( allMatches.size() == 0 ) {
			m = Pattern.compile("[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\\s*=\\s*Class\\.create\\(").matcher(text);

			while (m.find()) {
				if( !allMatches.contains(m.group())) {
					allMatches.add(m.group());
				}
			}
		}

		return allMatches;
	}



	/**
	 * Get all JS namespace in the order of their appearance, defined as doc-annotations in the given text, but each item only once
	 *
	 * @param	text	Source code to be searched
	 * @return			All found PHP class names
	 */
	private static List<String> getAllNamespaceInText(String text) {
		List<String> allMatches = new ArrayList<String>();
		Matcher m = Pattern.compile("@namespace(\\s)*([a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*)").matcher(text);

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
	 * @return				Cleaned method name
	 */
	private static String cleanupMethodName(String methodName) {
		String[] removeEachStrs	= {"(", ":"};

		return StringUtils.cleanReference(methodName, "function", removeEachStrs, "();");
	}



	/**
	 * Clean up namespace name
	 *
	 * @param	namespaceName	Namespace name to be cleaned
	 * @return					Cleaned namespace name
	 */
	private static String cleanupNamespaceName(String namespaceName) {
		String[] removeEachStrs	= {"\t"};

		return StringUtils.cleanReference(namespaceName, "@namespace", removeEachStrs);
	}



	/**
	 * Clean up class name
	 *
	 * @param	className	Class name to be cleaned
	 * @return				Cleaned class name
	 */
	private static String cleanupClassname(String className) {
		String[] removeEachStrs	= {"\t", " ", "=", "("};
		className	= StringUtils.cleanReference(className, "@class", removeEachStrs);

//		className	= StringUtils.cleanReference(className, "Class\\.create");
		className	= className.replace("Class.create", "");

		return className.trim();
	}

}
