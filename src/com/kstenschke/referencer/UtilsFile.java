/*
 * Copyright 2012-2013 Kay Stenschke
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

package com.kstenschke.referencer;

/**
 * File helper methods
 */
public class UtilsFile {

	/**
	 * Compare given extension against known PHP extensions
	 *
	 * @param	fileExtension	Extension to be checked
	 * @return					PHP extension?
	 */
	public static Boolean isPhpFileExtension(String fileExtension) {
		if( fileExtension == null) return false;

		fileExtension	= fileExtension.toLowerCase();

		return     fileExtension.equals("phpsh")
				|| fileExtension.equals("php")
				|| fileExtension.equals("php3")
				|| fileExtension.equals("php4")
				|| fileExtension.equals("php5");
	}

	/**
	 * Compare given extension against known JavaScript extensions
	 *
	 * @param	fileExtension	Extension to be checked
	 * @return					JavaScript extension?
	 */
	public static Boolean isJavaScriptFileExtension(String fileExtension) {
		if( fileExtension == null) return false;

		fileExtension	= fileExtension.toLowerCase();

		return fileExtension.equals("js");
	}

}
