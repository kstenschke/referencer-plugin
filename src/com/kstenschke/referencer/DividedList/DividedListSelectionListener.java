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

	DividedListCellKeyListener keyListener;

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

		if ( val != null && val.toString().equals("_") ) {
			int selectedIndex	= list.getSelectedIndex();

			if( keyListener.isGoingDown)	list.setSelectedIndex(selectedIndex + 1);
			if( keyListener.isGoingUp)	list.setSelectedIndex(selectedIndex - 1);
		}
	}

}