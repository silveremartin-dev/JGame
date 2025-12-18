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
 * Test pane for development and testing purposes.
 * 
 * <p>
 * Provides a simple UI for testing game components and
 * verifying UI functionality during development.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class TestPane extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JLabel statusLabel;
    private final JTextArea logArea;

    /**
     * Creates a new test pane with basic test controls.
     */
    public TestPane() {
        super(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header panel with title
        JLabel titleLabel = new JLabel("JGame Test Panel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // Center panel with log area
        logArea = new JTextArea(10, 40);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Test Log"));
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton testButton = new JButton("Run Test");
        testButton.addActionListener(e -> log("Test executed successfully"));
        buttonPanel.add(testButton);

        JButton clearButton = new JButton("Clear Log");
        clearButton.addActionListener(e -> logArea.setText(""));
        buttonPanel.add(clearButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Status label
        statusLabel = new JLabel("Ready");
        add(statusLabel, BorderLayout.SOUTH);

        log("Test pane initialized");
    }

    /**
     * Logs a message to the test log area.
     * 
     * @param message the message to log
     */
    public void log(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    /**
     * Sets the status message.
     * 
     * @param status the status message
     */
    public void setStatus(String status) {
        statusLabel.setText(status);
    }
}
