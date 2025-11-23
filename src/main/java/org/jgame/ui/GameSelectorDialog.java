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

import org.jgame.plugin.GameDescriptor;
import org.jgame.plugin.GamePluginRegistry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Dialog for selecting a game to play.
 * 
 * <p>
 * Displays all available games from the plugin registry and allows
 * the user to select one.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class GameSelectorDialog extends JDialog {

    private String selectedGameId = null;
    private final GamePluginRegistry registry;

    /**
     * Creates a new game selector dialog.
     * 
     * @param parent parent frame
     */
    public GameSelectorDialog(Frame parent) {
        super(parent, "Select Game", true);
        this.registry = GamePluginRegistry.getInstance();

        initComponents();
        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Initializes UI components.
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel = new JLabel("Choose a game to play:", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Game list
        List<GameDescriptor> games = registry.getAvailableGames();
        JPanel gamesPanel = new JPanel();
        gamesPanel.setLayout(new BoxLayout(gamesPanel, BoxLayout.Y_AXIS));
        gamesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ButtonGroup group = new ButtonGroup();
        for (GameDescriptor descriptor : games) {
            JRadioButton radioButton = new JRadioButton(createGameLabel(descriptor));
            radioButton.setActionCommand(descriptor.id());
            group.add(radioButton);
            gamesPanel.add(radioButton);
            gamesPanel.add(Box.createVerticalStrut(5));

            if (selectedGameId == null) {
                radioButton.setSelected(true);
                selectedGameId = descriptor.id();
            }

            radioButton.addActionListener(e -> selectedGameId = e.getActionCommand());
        }

        JScrollPane scrollPane = new JScrollPane(gamesPanel);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> dispose());
        cancelButton.addActionListener(e -> {
            selectedGameId = null;
            dispose();
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates display label for a game.
     */
    private String createGameLabel(GameDescriptor descriptor) {
        return String.format("<html><b>%s</b><br/><i>%s</i><br/>%d-%d players</html>",
                descriptor.name(),
                descriptor.description(),
                descriptor.minPlayers(),
                descriptor.maxPlayers());
    }

    /**
     * Gets the selected game ID.
     * 
     * @return game ID, or null if cancelled
     */
    public String getSelectedGameId() {
        return selectedGameId;
    }

    /**
     * Shows the dialog and returns selected game ID.
     * 
     * @param parent parent frame
     * @return selected game ID, or null if cancelled
     */
    public static String showDialog(Frame parent) {
        GameSelectorDialog dialog = new GameSelectorDialog(parent);
        dialog.setVisible(true);
        return dialog.getSelectedGameId();
    }
}
