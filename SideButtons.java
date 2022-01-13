package battleship;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.plaf.DimensionUIResource;

import java.awt.*;
import java.awt.event.*;

public class SideButtons extends JPanel {
    private JButton[] buttons;

    public SideButtons(ActionListener listener){
        setLayout(new GridLayout(1, 2));
        setBounds(50, 650, 300, 100);
        
        buttons = new JButton[2];

        JButton restartFinish = new JButton("Finish");
        restartFinish.setFocusPainted(false);
        restartFinish.setActionCommand("restart/finish");
        restartFinish.addActionListener(listener);
        buttons[0] = restartFinish;
        this.add(restartFinish);

        JButton randomize = new JButton("Randomize"); 
        randomize.setFocusPainted(false);
        randomize.setActionCommand("randomize");
        randomize.addActionListener(listener);
        buttons[1] = randomize;
        this.add(randomize);
    }

    public void changeFinishToRestart(){
        buttons[0].setText("Restart");
    }

    public void changeRestartToFinish(){
        buttons[0].setText("Finish");
    }
}   
