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

package com.kstenschke.referencer.dividedlist;

import com.intellij.ui.JBColor;
import com.kstenschke.referencer.StaticTexts;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * List cell renderer with a separator, items with "_" as text are displayed as separators dividing item groups
 */
public class DividedListCellRenderer extends DefaultListCellRenderer { //implements ListCellRenderer {

    private final Border separatorBorder          = BorderFactory.createMatteBorder(1, 0, 1, 0, new JBColor(JBColor.LIGHT_GRAY, JBColor.DARK_GRAY) );
    private final Border separatorBorderTopMost   = BorderFactory.createMatteBorder(0, 0, 1, 0, new JBColor(JBColor.LIGHT_GRAY, JBColor.DARK_GRAY) );
    private Font   separatorFont   = null;

	/**
	 * @param	list				List of reference items
	 * @param	value               Value of reference item
	 * @param	index               Item index
	 * @param	isSelected			Item currently selected?
	 * @param	cellHasFocus		Item currently has focus?
	 * @return  The rendered cell
	 */
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if( value != null ) {

            if( this.separatorFont == null ) {
                Font defaultFont    = list.getFont();
                this.separatorFont  = new Font( defaultFont.getName(), Font.BOLD, defaultFont.getSize() );
            }

			String valueStr	= value.toString();

			if( valueStr.startsWith(StaticTexts.POPUP_ITEM_PREFIX_SECTION_TITLE) ) {
				JLabel lblSectionLabel = new JLabel( valueStr.replace(StaticTexts.POPUP_ITEM_PREFIX_SECTION_TITLE + " ", "") + ":" );
                setLabelUI(lblSectionLabel, index == 0);

				return lblSectionLabel ;
			}
		}
			// Non-separator item
		return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	}

    /**
     * @param   label
     */
    private void setLabelUI(JLabel label, Boolean topMost) {
        label.setFont(this.separatorFont);
        label.setBorder(topMost ? this.separatorBorderTopMost : this.separatorBorder);
        label.setEnabled(false);
        label.setFocusable(false);
        label.setVisible(true);
    }

}
