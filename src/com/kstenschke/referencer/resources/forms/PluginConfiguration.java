package com.kstenschke.referencer.resources.forms;

import javax.swing.*;

public class PluginConfiguration {
    private JTextArea textArea1;
    private JCheckBox regExCheckBox;
    private JPanel rootPanel;


    public JPanel getRootPanel() {
        return rootPanel;
    }

    public boolean isModified() {
        return false;
    }

}
