/*
 * Copyright 2012-2014 Kay Stenschke
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

import java.util.*;

/**
 * Array utility methods
 */
public class UtilsArray {

	/**
	 * Merge the two given arrays of strings
	 *
	 * @param	strArr1		Array 1
	 * @param	strArr2		Array 2
	 * @return	String[]	Array containing all the strings of strArr1 and strArr2
	 */
	public static String[] merge(String[] strArr1, String[] strArr2) {
		if( strArr1 == null && strArr2 != null ) return strArr2;
		if( strArr2 == null && strArr1 != null ) return strArr1;
		if( strArr1 == null ) return null;

		List<String> list = new ArrayList<String>(Arrays.asList(strArr1));
		list.addAll(Arrays.asList(strArr2));

		Object[] strObjects	= list.toArray();

		String[] merged = {};

		int count	= 0;
		for(Object strObject : strObjects) {
			merged[count]	= strObject.toString();
		    count++;
		}

		return merged;
	}

}
