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

/**
 * String utility methods
 */
public class StringUtils {

	/**
	 * @param	referenceStr	String to be cleaned
	 * @param	removeOnceStr	Sub string to be removed only once (first occurrence)
	 * @return					The cleaned string
	 */
	public static String cleanReference(String referenceStr, String removeOnceStr) {
		if(!removeOnceStr.equals("") && referenceStr.contains(removeOnceStr)) {
			return referenceStr.replaceFirst(removeOnceStr, "").trim();
		}

		return referenceStr.trim();
	}



	/**
	 * @param	referenceStr	String to be cleaned
	 * @param	removeOnceStr	Sub string to be removed only once (first occurrence)
	 * @param	removeEachStrs	Sub strings to be removed allover
	 * @return					The cleaned string
	 */
	public static String cleanReference(String referenceStr, String removeOnceStr, String[] removeEachStrs) {
		referenceStr	= cleanReference(referenceStr, removeOnceStr);
		referenceStr	= StringUtils.removeSubStrings(referenceStr, removeEachStrs);

		return referenceStr.trim();
	}



	/**
	 * Cleanup and concatenate given postfix
	 *
	 * @param	referenceStr	String to be cleaned
	 * @param	removeOnceStr	Sub string to be removed only once (first occurrence)
	 * @param	removeEachStrs	Sub strings to be removed allover
	 * @param	postfix			String to concatenated at end
	 * @return					The cleaned string
	 */
	public static String cleanReference(String referenceStr, String removeOnceStr, String[] removeEachStrs, String postfix) {
		return cleanReference(referenceStr, removeOnceStr, removeEachStrs) + postfix;
	}



	/**
	 * Remove all of the given sub string from the given string
	 *
	 * @param	haystack	String to be modified
	 * @param	needles		Sub strings to be removed
	 * @return				The modified string
	 */
	public static String removeSubStrings(String haystack, String[] needles) {
		for (String needle : needles) {
			haystack = haystack.replace(needle, "");
		}

		return haystack;
	}

}
