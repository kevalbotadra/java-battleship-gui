package battleship;

import javax.print.attribute.standard.MediaSize.Other;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.DimensionUIResource;

import org.w3c.dom.events.MouseEvent;


import java.awt.*;
import java.awt.event.*;

public class Game implements ActionListener {
    private JFrame frame;

    private PlayerBoard playerBoard;
    private OtherBoard otherBoard;
    private SideButtons sideButtons;
    private RotateButton rotateButton;
    private EraseButton eraseButton;
    private ConfirmButton confirmButton;


    private int rotateCounter = 1;
    private int eraseCounter = 0;

    private Phase phase;

    private Direction direction;

    private int lastTappedTileX;
    private int lastTappedTileY;

    // set whos turn it is
    // 0 player turn
    // 1 robot turn
    private int turn = 0;

    private boolean placeable = true;

    private Ship carrier;
    private Ship battleship;
    private Ship submarine;
    private Ship destroyer;
    private Ship cruiser;

    private Ship[] ships;

    private int shipPlace = 0;
    // 0 represents a reset settings (don't change ship)
    // 1 represents the carrier
    // 2 represetnst the battleship
    // 3 represents the submarine
    // 4 represents the cruiser
    // 4 represents the destroyer

    private JLabel shipPlacementDirectionLabel;
    private JLabel directionLabel;
    private JLabel gamePhaseLabel;
    private JLabel turnLabel;

    public Game(){
        frame = new JFrame();

        // set the phase to set-up
        phase = Phase.SETUP;

        frame.setLayout(null);
        frame.setSize(1050, 900);

        carrier = new Ship("Carrier", 5, 0, Color.GREEN);
        battleship = new Ship("Battleship", 4, 1, Color.BLACK);
        submarine = new Ship("Submarine", 3, 2, Color.PINK);
        cruiser = new Ship("Cruiser", 3, 3, Color.BLUE);
        destroyer = new Ship("Destroyer", 2, 4, Color.RED);

        // set the ships list to all the ships
        ships = new Ship[5];
        ships[0] = carrier;
        ships[1] = battleship;
        ships[2] = submarine;
        ships[3] = cruiser;
        ships[4] = destroyer;


        playerBoard = new PlayerBoard(this, carrier, battleship, submarine, cruiser, destroyer);
        frame.add(playerBoard);


        otherBoard = new OtherBoard(this);
        // otherBoard.disableButtons();
        frame.add(otherBoard);

        sideButtons = new SideButtons(this);
        frame.add(sideButtons);

        rotateButton = new RotateButton(this);
        direction = Direction.HORIZONTAL;
        frame.add(rotateButton);

        eraseButton = new EraseButton(this);
        frame.add(eraseButton);

        confirmButton = new ConfirmButton(this);
        confirmButton.setVisible(false);
        frame.add(confirmButton);

        directionLabel = new JLabel("Ship Direction: Horizontal");
        directionLabel.setFont(directionLabel.getFont().deriveFont(directionLabel.getFont().getStyle() | Font.BOLD));
        directionLabel.setBounds(845, 50, 200, 50);
        frame.add(directionLabel);
        
        shipPlacementDirectionLabel = new JLabel();
        shipPlacementDirectionLabel.setText("<html>Tap anywhere on the grid to place your ships on the board. Note that the position you tap will be the end of the ship. If you are horizontal, the ship will start at the clicked position and be placed rightwards. If you are vertical, the ship will placed upwards.</html>");
        shipPlacementDirectionLabel.setBounds(430, 0, 400, 100);
        frame.add(shipPlacementDirectionLabel);

        gamePhaseLabel = new JLabel();
        gamePhaseLabel.setText("<html>When it's your turn, click anywhere on the small board, and cofirm it to make a guess. If you get a hit, the board will turn the color of the boat (which will be anything but green), and if you get a miss the board will turn green. In order to win, destroy all the computer's ships, before it destroys yours.</html>");
        gamePhaseLabel.setBounds(430, 0, 400, 100);
        gamePhaseLabel.setVisible(false);
        frame.add(gamePhaseLabel);

        turnLabel = new JLabel("Turn: Yours");
        turnLabel.setFont(directionLabel.getFont().deriveFont(directionLabel.getFont().getStyle() | Font.BOLD));
        turnLabel.setBounds(860, 50, 200, 50);
        turnLabel.setVisible(false);
        frame.add(turnLabel);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void setupVisbility(boolean choice){
        rotateButton.setVisible(choice);
        eraseButton.setVisible(choice);
        shipPlacementDirectionLabel.setVisible(choice);
        directionLabel.setVisible(choice);
    }

    public void gameVisibility(boolean choice){
        turnLabel.setVisible(choice);
        confirmButton.setVisible(choice);
        gamePhaseLabel.setVisible(choice);
    }

    public void setTurn(int turn){
        if (turn == 0){
            turnLabel.setText("Turn: Yours");
            playerBoard.disableButtons();
            otherBoard.enableButtons();
        } else {
            turnLabel.setText("Turn: Computers");
            otherBoard.disableButtons();
            playerBoard.enableButtons();
        }
    }

    public void redoLastShip(){
        Ship ship = ships[shipPlace - 1];
    }

    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand() == "restart/finish"){
            if (phase == Phase.SETUP){
                if (shipPlace == 5){
                    phase = Phase.GAME;
                    shipPlace = 0;
                    setupVisbility(false);
                    gameVisibility(true);
                    playerBoard.gatherShipPlacements();
                    sideButtons.changeFinishToRestart();
                    JOptionPane.showMessageDialog(frame, "Now that you are all setup, let's start the game.\nIf your ship's are not placed how you want them to be, please tap the 'Restart' button.");
                } else {
                    JOptionPane.showMessageDialog(frame, "You can only finish once you have placed all ships.");
                }
                
            } else if (phase == Phase.GAME){
                phase = Phase.SETUP;
                shipPlace = 0;
                placeable = true;
                setupVisbility(true);
                gameVisibility(false);
                sideButtons.changeRestartToFinish();
                otherBoard.resetBoard();
                playerBoard.resetBoard();
            }
            
        }

        if(phase == Phase.SETUP){
            if (placeable){
                if (e.getActionCommand() == "playerBoardTile"){
                    Ship ship = ships[shipPlace];
    
                    GameTile tappedTile = (GameTile)e.getSource();
                    int placed = playerBoard.placeShip(ship, tappedTile.row, tappedTile.column, direction);
                    lastTappedTileX = tappedTile.row;
                    lastTappedTileY = tappedTile.column;
                    
                    if(placed == 0){
                        JOptionPane.showMessageDialog(frame, "This ship will not fit within the bounds at this tile.\nPlease try another tile.");
                    } else if (placed == 1){
                        JOptionPane.showMessageDialog(frame, "This ship will interfere with another ship.\nPlease try another tile.");
                    } else if (placed == 2){
                        shipPlace++;
                    } else if (placed == 3){
                        shipPlace = 5;
                        placeable = false;
                    }
                }
            }

            if (e.getActionCommand() == "randomize"){
                shipPlace = 5;
                playerBoard.randomizeBoard();
                playerBoard.gatherShipPlacements();
            }

            if(e.getActionCommand() == "rotate"){
                if(!(rotateCounter % 2 == 0)){
                    direction = Direction.VERTICAL;
                    directionLabel.setText("Ship Direction: Vertical");
                } else {
                    direction = Direction.HORIZONTAL;
                    directionLabel.setText("Ship Direction: Horizontal");
                }

                Ship ship = !(shipPlace == 5) ? ships[shipPlace - 1] : ships[4];

                playerBoard.eraseShip(ship, lastTappedTileX, lastTappedTileY);
                int placed = playerBoard.placeShip(ship, lastTappedTileX, lastTappedTileY, direction);

                if(placed == 0){
                    JOptionPane.showMessageDialog(frame, "This ship will not fit within the bounds at this tile.\nPlease try rotating it another way.");
                    shipPlace --;
                } else if (placed == 1){
                    JOptionPane.showMessageDialog(frame, "This ship will interfere with another ship.\nPlease try rotating it somewhere else.");
                    shipPlace--;
                }

                
                rotateCounter ++;
            }

            if(e.getActionCommand() == "erase"){
                if(eraseCounter == 0){
                    if (shipPlace != 5){
                        playerBoard.eraseShip(ships[shipPlace - 1], lastTappedTileX, lastTappedTileY);
                        shipPlace = shipPlace - 1;
                    } else {
                        playerBoard.eraseShip(ships[4], lastTappedTileX, lastTappedTileY);
                    }
                    placeable = true;
                    eraseButton.setText("<html><center>Erase<br>All Ships</center></html>");
                    eraseCounter ++;
                } else if (eraseCounter == 1){
                    phase = Phase.SETUP;
                    placeable = true;
                    shipPlace = 0;
                    setupVisbility(true);
                    otherBoard.resetBoard();
                    playerBoard.resetBoard();
                    eraseButton.setText("<html><center>Erase<br>Last Ship</center></html>");
                    eraseCounter --;
                }
                
            }
        }

        if (phase == Phase.GAME){
            if (e.getActionCommand() == "otherBoardTile"){
                GameTile tappedTile = (GameTile)e.getSource();
                int result = otherBoard.checkHitOrMiss(tappedTile.row, tappedTile.column);
                if (result == 0){
                    setTurn(1);
                    while(true){
                        int error = playerBoard.randomHitOrMiss();
                        if (error != 400){
                            break;
                        }
                    }
                    
                    setTurn(0);
                }
                
            }
        }
        
    }

}