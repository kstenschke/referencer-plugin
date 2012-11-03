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
import java.awt.*;

/**
 * List cell renderer with a separator, items with "_" as text are displayed as separators dividing item groups
 */
public class DividedListCellRenderer extends DefaultListCellRenderer implements ListCellRenderer {

	/**
	 * @param	list
	 * @param	value
	 * @param	index
	 * @param	isSelected
	 * @param	cellHasFocus
	 * @return  The rendered cell
	 */
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if ( value != null && value.toString().equals("_") ) {
				// Separator item
			JLabel lblSeparator = new JLabel();
			lblSeparator.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY) );
			//lblSeparator.setPreferredSize( new Dimension(1, 1) );

			lblSeparator.setEnabled(false);
			lblSeparator.setFocusable(false);
			lblSeparator.setVisible(false);

			return lblSeparator ;
		} else
				// Non-separator item
			return super.getListCellRendererComponent ( list, value, index, isSelected, cellHasFocus);
	}

}
