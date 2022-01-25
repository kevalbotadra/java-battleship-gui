package battleship;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.plaf.DimensionUIResource;

import java.awt.*;
import java.awt.event.*;

public class SideButtons extends JPanel {
    private JButton[] buttons;

    //Function to make different buttons
    public SideButtons(ActionListener listener){
        setLayout(new GridLayout(1, 2));
        setBounds(50, 650, 300, 100);
        
        buttons = new JButton[2];

        //Makes button to either restart the game or finish placing your ships
        JButton restartFinish = new JButton("Finish");
        restartFinish.setFocusPainted(false);
        restartFinish.setActionCommand("restart/finish");
        restartFinish.addActionListener(listener);
        buttons[0] = restartFinish;
        this.add(restartFinish);

        //Makes button to randomize ship placements
        JButton randomize = new JButton("Randomize"); 
        randomize.setFocusPainted(false);
        randomize.setActionCommand("randomize");
        randomize.addActionListener(listener);
        buttons[1] = randomize;
        this.add(randomize);
    }

    //Function to change the finish button to the restart button
    public void changeFinishToRestart(){
        buttons[0].setText("Restart");
    }

    //Function to change the restart button to the finish button
    public void changeRestartToFinish(){
        buttons[0].setText("Finish");
    }
}   
