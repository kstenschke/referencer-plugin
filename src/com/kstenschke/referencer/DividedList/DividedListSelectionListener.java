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

package com.kstenschke.referencer.DividedList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DividedListSelectionListener extends JFrame implements ListSelectionListener {

	private final DividedListCellKeyListener keyListener;



	public DividedListSelectionListener(DividedListCellKeyListener keyListener) {
		this.keyListener	= keyListener;
	}



	/**
	 * Detect when a separator is selected via cursor keys, select previous/next item than
	 *
	 * @param	e	Selection event
	 */
	public void valueChanged(ListSelectionEvent e) {
		JList list	= (JList) e.getSource();
		Object val	= list.getSelectedValue();

		if ( val != null ) {
			 if ( keyListener.isVkControlDown ) {
					// CTRL+DOWN: 1st item of next section
				 if( keyListener.isGoingDown ) {
					while( 		list.getSelectedIndex() < (list.getModel().getSize() - 1)
							&&	!isSeparatorOrSection( list.getSelectedValue().toString() )
					) {
						list.setSelectedIndex( list.getSelectedIndex() + 1);
					}
					//if( list.getSelectedIndex() < (list.getModel().getSize() - 1)) {
					//	list.setSelectedIndex( list.getSelectedIndex() + 1);
					//}
				 }
			 }


				// Navigating item selection up/down
				// If the selected item is a section title or divider - forward to the next item in that direction
			if ( isSeparatorOrSection( list.getSelectedValue().toString() )) {
				if( keyListener.isGoingDown || keyListener.isGoingUp) {
					int offset			= keyListener.isGoingDown ? 1 : -1;

					list.setSelectedIndex(list.getSelectedIndex() + offset);
				}
			}

		}
	}



	private boolean isSeparatorOrSection(String valStr) {
		return valStr.equals("_") || valStr.startsWith("SECTIONTITLE:");
	}
}