/*
 * Copyright Kay Stenschke
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

    public DividedListSelectionListener() {
    }

    /**
     * Detect when a separator is selected via cursor keys, select previous/next item than
     *
     * @param e Selection event
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        var list = (JList) e.getSource();

        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex >= 0 && list.isVisible()) {
            if (isSeparatorOrSection(list.getSelectedValue().toString(), selectedIndex)) {
                boolean isMovingDown = selectedIndex > this.prevSelectedIndex;
                int newIndex = isMovingDown
                    ? selectedIndex + 1
                    : selectedIndex - 1;

                list.setSelectedIndex(newIndex);
            }

            this.prevSelectedIndex = list.getSelectedIndex();
        }
    }

    private boolean isSeparatorOrSection(String valueStr, Integer index) {
        return index == 0
            || "_".equals(valueStr) || valueStr.startsWith(StaticTexts.POPUP_ITEM_PREFIX_SECTION_TITLE);
    }
}
