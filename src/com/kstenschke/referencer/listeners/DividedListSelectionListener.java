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
package com.kstenschke.referencer.listeners;

import com.kstenschke.referencer.resources.StaticTexts;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DividedListSelectionListener extends JFrame implements ListSelectionListener {

    private int prevSelectedIndex = 0;

    /**
     * Constructor
     */
    public DividedListSelectionListener() {

    }

    /**
     * Detect when a separator is selected via cursor keys, select previous/next item than
     *
     * @param	e	Selection event
     */
    public void valueChanged(ListSelectionEvent e) {
        JList list	            = (JList) e.getSource();

        if( list.getSelectedValue() != null && list.isVisible() ) {
            int selectedIndex   = list.getSelectedIndex();

            if( isSeparatorOrSection(list.getSelectedValue().toString()) ) {
                if( selectedIndex > this.prevSelectedIndex ) {
                    list.setSelectedIndex( list.getSelectedIndex() + 1 );
                } else {
                    list.setSelectedIndex( list.getSelectedIndex() - 1 );
                }
            }

            this.prevSelectedIndex  = list.getSelectedIndex();
        }
    }

    /**
     * @param   valueStr
     * @return  Boolean
     */
    private boolean isSeparatorOrSection(String valueStr) {
        return valueStr.equals("_") || valueStr.startsWith(StaticTexts.POPUP_ITEM_PREFIX_SECTION_TITLE);
    }
}