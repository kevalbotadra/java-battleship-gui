package battleship;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.plaf.DimensionUIResource;

import java.awt.*;
import java.awt.event.*;

class GameTile extends JButton {
    public int row;
    public int column;

    public GameTile(String actionCommand, boolean useBorder) {
        if (useBorder){
            setBorder(BorderFactory.createLineBorder(Color.black));
            setOpaque(true);
            setBackground(Color.decode("#206d99"));

            setEnabled(true);
            setActionCommand(actionCommand);
        }
    }
}