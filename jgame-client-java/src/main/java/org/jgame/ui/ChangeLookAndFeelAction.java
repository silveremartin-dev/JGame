package org.jgame.ui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Action for changing the application's Look and Feel.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class ChangeLookAndFeelAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    private final JFrame frame;
    private final LookAndFeelData lafData;

    /**
     * Creates a new change L&F action.
     *
     * @param frame   the application frame
     * @param lafData the Look and Feel data
     */
    public ChangeLookAndFeelAction(JFrame frame, LookAndFeelData lafData) {
        super(lafData.label);
        this.frame = frame;
        this.lafData = lafData;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            UIManager.setLookAndFeel(lafData.className);
            SwingUtilities.updateComponentTreeUI(frame);
            frame.pack();
        } catch (Exception ex) {
            System.err.println("Failed to set Look and Feel: " + ex.getMessage());
        }
    }
}
