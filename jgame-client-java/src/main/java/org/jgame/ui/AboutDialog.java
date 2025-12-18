/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * Enhanced with AI assistance from Google Gemini (Antigravity)
 */

package org.jgame.ui;

import javax.swing.*;
import java.awt.*;

/**
 * About dialog displaying application information, credits, and license.
 * 
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class AboutDialog extends JDialog {

    /**
     * Creates a new About dialog.
     *
     * @param parent the parent frame
     */
    public AboutDialog(JFrame parent) {
        super(parent, "About JGame", true);
        initComponents();
        setLocationRelativeTo(parent);
    }

    /**
     * Initializes the dialog components.
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setSize(400, 300);

        // Title panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("JGame Framework");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        infoPanel.add(createInfoLabel("Version: 1.0-SNAPSHOT"));
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(createInfoLabel("Author: Silvere Martin-Michiellot"));
        infoPanel.add(createInfoLabel("Email: silvere.martin@gmail.com"));
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(createInfoLabel("License: MIT License"));
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(createInfoLabel("Enhanced with AI assistance from Google Gemini"));

        add(infoPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates a centered info label.
     *
     * @param text the label text
     * @return the configured label
     */
    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
}
