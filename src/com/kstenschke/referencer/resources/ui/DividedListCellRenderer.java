/*
 * Copyright 2012-2017 Kay Stenschke
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

import com.intellij.util.ui.UIUtil;
import com.kstenschke.referencer.resources.StaticTexts;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * List cell renderer with a separator, items with "_" as text are displayed as separators dividing item groups
 */
public class DividedListCellRenderer extends DefaultListCellRenderer {

    private final Border separatorBorder;
    private final Border separatorBorderTopMost;
    private final Font separatorFont;
    private final Color separatorColorBackground;
    private final Color separatorColorForeground;

    /**
     * Constructor
     *
     * @param list
     */
    public DividedListCellRenderer(JList list) {
        boolean isUnderDarcula = UIUtil.isUnderDarcula();

        separatorBorderTopMost = isUnderDarcula
                ? BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY)
                : BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY);

        separatorBorder = isUnderDarcula
            ? BorderFactory.createMatteBorder(1, 0, 1, 0, Color.DARK_GRAY)
            : BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY);

        Font defaultFont = list.getFont();
        this.separatorFont = new Font(defaultFont.getName(), defaultFont.getStyle(), defaultFont.getSize());

        this.separatorColorBackground = isUnderDarcula
                ? new Color(79, 79, 79)
                : new Color(243, 243, 243);

        this.separatorColorForeground = isUnderDarcula
                ? new Color(250, 250, 250)
                : new Color(79, 79, 79);
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
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            String valueStr = value.toString();

            // Apply section title styling
            if (valueStr.startsWith(StaticTexts.POPUP_ITEM_PREFIX_SECTION_TITLE)) {
                String labelText = valueStr.replace(StaticTexts.POPUP_ITEM_PREFIX_SECTION_TITLE + " ", "");
                if (!labelText.endsWith(":")) {
                    labelText += ":";
                }

                JLabel sectionLabel = new JLabel("<html><b>" + labelText + "</b></html>");
                setLabelUI(sectionLabel, index == 0);

                return sectionLabel;
            }
        }
        // Non-separator item
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }

    /**
     * @param label
     */
    private void setLabelUI(JLabel label, Boolean isTopMost) {
        label.setOpaque(true);

        label.setFont(separatorFont);

        label.setBorder(isTopMost ? separatorBorderTopMost : separatorBorder);

        label.setBackground(separatorColorBackground);

        label.setForeground(separatorColorForeground);

        label.setEnabled(false);
        label.setFocusable(false);
        label.setVisible(true);
    }
}
