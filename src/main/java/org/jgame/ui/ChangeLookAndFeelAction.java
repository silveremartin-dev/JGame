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

import org.jgame.client.GameClient;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Action for changing the application's Look and Feel.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class ChangeLookAndFeelAction extends AbstractAction {

    private final GameClient client;
    private final LookAndFeelData lafData;

    /**
     * Creates a new change L&F action.
     *
     * @param client  the game client
     * @param lafData the Look and Feel data
     */
    public ChangeLookAndFeelAction(GameClient client, LookAndFeelData lafData) {
        super(lafData.label);
        this.client = client;
        this.lafData = lafData;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            UIManager.setLookAndFeel(lafData.className);
            SwingUtilities.updateComponentTreeUI(client);
            client.pack();
        } catch (Exception ex) {
            System.err.println("Failed to set Look and Feel: " + ex.getMessage());
        }
    }
}
