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
package com.kstenschke.referencer.resources.ui;

import com.intellij.ui.Gray;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.UIUtil;
import com.kstenschke.referencer.resources.StaticTexts;

import javax.swing.*;
import java.awt.*;

/**
 * List cell renderer with a separator, items with "_" as text are displayed as separators dividing item groups
 */
public class DividedListCellRenderer extends DefaultListCellRenderer {

    private final Font separatorFont;
    private final Color separatorColorBackground;
    private final Color separatorColorForeground;

    public DividedListCellRenderer(JBList<Object> list) {
        boolean isUnderDarcula = UIUtil.isUnderDarcula();

        Font defaultFont = list.getFont();
        this.separatorFont = new Font(defaultFont.getName(), defaultFont.getStyle(), defaultFont.getSize());
        this.separatorColorBackground = isUnderDarcula ? Gray._79 : Gray._243;
        this.separatorColorForeground = isUnderDarcula ? Gray._250 : Gray._79;
    }

    /**
     * @return The rendered cell
     * @param    list                List of reference items
     * @param    value Value of reference item
     * @param    index Item index
     * @param    isSelected            Item currently selected?
     * @param    cellHasFocus        Item currently has focus?
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus) {
        String valueStr = null;

        if (value != null) {
            valueStr = value.toString();
            valueStr = valueStr.substring(valueStr.indexOf("=>") + 2);  /* Do not output item index prefix */

            /* Apply section title styling */
            if (valueStr.startsWith(StaticTexts.POPUP_ITEM_PREFIX_SECTION_TITLE)) {
                String labelText = valueStr.replace(StaticTexts.POPUP_ITEM_PREFIX_SECTION_TITLE + " ", "");
                if (!labelText.endsWith(":")) {
                    labelText += ":";
                }

                return setLabelUI(new JBLabel(labelText));
            }
        }
        /* Non-separator item */
        return super.getListCellRendererComponent(list, valueStr, index, isSelected, cellHasFocus);
    }

    private JLabel setLabelUI(JBLabel label) {
        label.setOpaque(true);
        label.setFont(separatorFont);
        label.setBackground(separatorColorBackground);
        label.setForeground(separatorColorForeground);
        label.setEnabled(false);
        label.setFocusable(false);
        label.setVisible(true);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        return label;
    }
}
