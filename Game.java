package battleship;

import javax.print.attribute.standard.MediaSize.Other;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.DimensionUIResource;

import org.w3c.dom.events.MouseEvent;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

//This holds the game's main functions
public class Game implements ActionListener {
    //initialzing variables and objects
    private JFrame frame;

    private PlayerBoard playerBoard;
    private OtherBoard otherBoard;
    private SideButtons sideButtons;
    private RotateButton rotateButton;
    private EraseButton eraseButton;
    private Confirm confirmPanel;
    private JButton toggleConfirm;


    private int rotateCounter = 1;
    private int eraseCounter = 0;

    private Phase phase;

    private Direction direction;

    private int lastTappedTileX;
    private int lastTappedTileY;

    private int confirmTileX = 0;
    private int confirmTileY = 0;

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

    //these hold the images of each ship for the computer
    private JLabel computerHitShipLabel;
    private ShipInfo computerCarrierInfo;
    private ShipInfo computerBattleshipInfo;
    private ShipInfo computerSubmarineInfo;
    private ShipInfo computerCruiserInfo;
    private ShipInfo computerDestroyerInfo;
    private ShipInfo[] computerHitShipInfos;

    //these hold the images of each ship for the player
    private JLabel playerHitShipLabel;
    private ShipInfo playerCarrierInfo;
    private ShipInfo playerBattleshipInfo;
    private ShipInfo playerSubmarineInfo;
    private ShipInfo playerCruiserInfo;
    private ShipInfo playerDestroyerInfo;
    private ShipInfo[] playerHitShipInfos;

    //counts how many ships have been shit for the player and computer
    private int totalComputerHitShips = 0;
    private int totalPlayerHitShips = 0;

    private boolean resetColor = true;

    private boolean useConfirm = true;

    //Sets the game up 
    public Game(){
        frame = new JFrame();

        // set the phase to set-up
        phase = Phase.SETUP;

        //Sets of bound of Battleship board
        frame.setLayout(null);
        frame.setSize(1050, 900);

        //Makes ship objects for each ship
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

        //All of these are objects that go on the board like both of the boards and the buttons for all the different actions 
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

        confirmPanel = new Confirm(this);
        confirmPanel.setVisible(false);
        frame.add(confirmPanel);

        //States what direction the ship is 
        directionLabel = new JLabel("Ship Direction: Horizontal");
        directionLabel.setFont(directionLabel.getFont().deriveFont(directionLabel.getFont().getStyle() | Font.BOLD));
        directionLabel.setBounds(845, 50, 200, 50);
        frame.add(directionLabel);
        
        //Directions for placing ships
        shipPlacementDirectionLabel = new JLabel();
        shipPlacementDirectionLabel.setText("<html>Tap anywhere on the grid to place your ships on the board. Note that the position you tap will be the end of the ship. If you are horizontal, the ship will start at the clicked position and be placed rightwards. If you are vertical, the ship will placed upwards.</html>");
        shipPlacementDirectionLabel.setBounds(430, 0, 400, 100);
        frame.add(shipPlacementDirectionLabel);

        //Direction for guessing the computer's ships
        gamePhaseLabel = new JLabel();
        gamePhaseLabel.setText("<html>When it's your turn, click anywhere on the small board, and cofirm it to make a guess. If you get a hit, the board will turn the color of the boat (which will be anything but green), and if you get a miss the board will turn green. In order to win, destroy all the computer's ships, before it destroys yours.</html>");
        gamePhaseLabel.setBounds(430, 0, 400, 100);
        gamePhaseLabel.setVisible(false);
        frame.add(gamePhaseLabel);

        //Displays whose turn it is
        turnLabel = new JLabel("Turn: Yours");
        turnLabel.setFont(directionLabel.getFont().deriveFont(directionLabel.getFont().getStyle() | Font.BOLD));
        turnLabel.setBounds(860, 50, 200, 50);
        turnLabel.setVisible(false);
        frame.add(turnLabel);

        //This code displays the computer's ships and which ones have been hit
        int hitShipLabelYBounds = 340;
        int computerHitShipsXBounds = 40;
        computerHitShipLabel = new JLabel("Computer's Ships");
        computerHitShipLabel.setFont(directionLabel.getFont().deriveFont(directionLabel.getFont().getStyle() | Font.BOLD));
        computerHitShipLabel.setBounds(computerHitShipsXBounds, hitShipLabelYBounds, 200, 50);
        frame.add(computerHitShipLabel);

        // "\u2713" is a check mark
        computerCarrierInfo = new ShipInfo(this, computerHitShipsXBounds, hitShipLabelYBounds + 40, "BattleshipImages/Carrier/full_image.png");
        frame.add(computerCarrierInfo);

        computerBattleshipInfo = new ShipInfo(this, computerHitShipsXBounds, hitShipLabelYBounds + 90, "BattleshipImages/Battleship/full_image.png");
        frame.add(computerBattleshipInfo);

        computerSubmarineInfo = new ShipInfo(this, computerHitShipsXBounds, hitShipLabelYBounds + 140, "BattleshipImages/Submarine/full_image.png");
        frame.add(computerSubmarineInfo);

        computerCruiserInfo = new ShipInfo(this, computerHitShipsXBounds, hitShipLabelYBounds + 190, "BattleshipImages/Cruiser/full_image.png");
        frame.add(computerCruiserInfo);

        computerDestroyerInfo = new ShipInfo(this, computerHitShipsXBounds, hitShipLabelYBounds + 240, "BattleshipImages/Destroyer/full_image.png");
        frame.add(computerDestroyerInfo);

        computerHitShipInfos = new ShipInfo[5];
        computerHitShipInfos[0] = computerCarrierInfo;
        computerHitShipInfos[1] = computerBattleshipInfo;
        computerHitShipInfos[2] = computerSubmarineInfo;
        computerHitShipInfos[3] = computerCruiserInfo;
        computerHitShipInfos[4] = computerDestroyerInfo;

        //This code displays the player's ships and which ships have been hit
        int playerHitShipXBounds = 245;
        playerHitShipLabel = new JLabel("Player's Ships");
        playerHitShipLabel.setFont(directionLabel.getFont().deriveFont(directionLabel.getFont().getStyle() | Font.BOLD));
        playerHitShipLabel.setBounds(playerHitShipXBounds, hitShipLabelYBounds, 200, 50);
        frame.add(playerHitShipLabel);

        // "\u2713" is a check mark
        playerCarrierInfo = new ShipInfo(this, playerHitShipXBounds, hitShipLabelYBounds + 40, "BattleshipImages/Carrier/full_image.png");
        frame.add(playerCarrierInfo);

        playerBattleshipInfo = new ShipInfo(this, playerHitShipXBounds, hitShipLabelYBounds + 90, "BattleshipImages/Battleship/full_image.png");
        frame.add(playerBattleshipInfo);

        playerSubmarineInfo = new ShipInfo(this, playerHitShipXBounds, hitShipLabelYBounds + 140, "BattleshipImages/Submarine/full_image.png");
        frame.add(playerSubmarineInfo);

        playerCruiserInfo = new ShipInfo(this, playerHitShipXBounds, hitShipLabelYBounds + 190, "BattleshipImages/Cruiser/full_image.png");
        frame.add(playerCruiserInfo);

        playerDestroyerInfo = new ShipInfo(this, playerHitShipXBounds, hitShipLabelYBounds + 240, "BattleshipImages/Destroyer/full_image.png");
        frame.add(playerDestroyerInfo);

        playerHitShipInfos = new ShipInfo[5];
        playerHitShipInfos[0] = playerCarrierInfo;
        playerHitShipInfos[1] = playerBattleshipInfo;
        playerHitShipInfos[2] = playerSubmarineInfo;
        playerHitShipInfos[3] = playerCruiserInfo;
        playerHitShipInfos[4] = playerDestroyerInfo;

        //Button to toggle confirm so you don't have to click confirm every time you make a guess
        toggleConfirm = new JButton("Toggle Confirm : On");
        toggleConfirm.setBounds(2, 5, 200, 15);
        Border border = BorderFactory.createEmptyBorder();
        toggleConfirm.setBorder(border);
        toggleConfirm.setFocusPainted(false);
        toggleConfirm.setActionCommand("toggleConfirm");
        toggleConfirm.addActionListener(this);
        toggleConfirm.setVisible(false);
        frame.add(toggleConfirm);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    //Based on what you do in the setup phase, it will choose whether or not to display that button
    public void setupVisbility(boolean choice){
        rotateButton.setVisible(choice);
        eraseButton.setVisible(choice);
        shipPlacementDirectionLabel.setVisible(choice);
        directionLabel.setVisible(choice);
    }

    //Based on what you do in the game phase, it will choose whether or not to display that button
    public void gameVisibility(boolean choice){
        toggleConfirm.setVisible(choice);
        turnLabel.setVisible(choice);
        confirmPanel.setVisible(choice);
        gamePhaseLabel.setVisible(choice);
    }

    //Based on whose turn it is, it will disable the buttons
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

    //Checks how many ships are hit for the computer and if all the ships have been hit it will play a sound effect and display an image
    public void setComputerHitShips(Ship[] computerHitShips){
        int counter = 0;
        for (Ship ship : computerHitShips){
            if(ship != null){
                computerHitShipInfos[ship.getIdx()].setStatus(" \u2713");
                counter ++;
            }   
        }
        if (counter == 5){
            playSound("Win");
            JOptionPane.showMessageDialog(frame, "you won. i accept defeat. i am the lesser being");
            System.exit(200);
        }
    } 
    
    //Checks how many ships are hit for the playerand if all the ships have been hit it will play a sound effect and display an image
    public void setPlayerHitShips(Ship[] playerHitShips){
        int counter = 0;
        for (Ship ship : playerHitShips){
            if(ship != null){
                counter ++;
                playerHitShipInfos[ship.getIdx()].setStatus(" \u2713");
            }   
        }
        if(counter == 5){
            playSound("Lost");
            JOptionPane.showMessageDialog(frame, "YOU SUCK UR DOGWATER UNINSTALL DELETE THE GAME");
            System.exit(200);
        }
    }

    //Function to play music when a certain action occurs
    public void playSound(String fileName){
        try {
            // create an input stream for audio with the file define above
            AudioInputStream audioInputStream = AudioSystem
                    .getAudioInputStream(new File("BattleshipSounds/" + fileName + ".wav")); // change "byebye" to "lilnasx" to play industry baby

            // create clip reference
            Clip clip = AudioSystem.getClip();

            // open audioInputStream to the clip
            clip.open(audioInputStream);

            // start the audio clip 
            clip.start();

        } catch (Exception e) {
            System.out.println("RIP NO MUSICA :(");
        }
    }

    //Performs actions based on what phase it is
    public void actionPerformed(ActionEvent e){
        //Restarts game if it is in the game phase and would move to the next phase if it is the setup phase
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

        //Checks if the ship you are trying to place can't fit there
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

            //This randomizes the ship placements 
            if (e.getActionCommand() == "randomize"){
                shipPlace = 5;
                playerBoard.randomizeBoard();
                playerBoard.gatherShipPlacements();
            }

            //This rotates the ship while checking if it is valid 
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

            //This erases the last ship when you press the button at first and if you press it again it will erase all the ships
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
            //Turns toggle confirm on and off 
            if(e.getActionCommand().equals("toggleConfirm")){
                if(otherBoard.getHitOrMiss()[confirmTileX][confirmTileY].equals("None")){
                        otherBoard.getGameTiles()[confirmTileX][confirmTileY].setBackground(Color.decode("#206d99"));
                }
                if(useConfirm){
                    toggleConfirm.setText("Toggle Confirm: Off");
                    useConfirm = false;
                } else {
                    toggleConfirm.setText("Toggle Confirm: On");
                    useConfirm = true;
                }
            }

            //This is the confirm button used to confirm a tile when the player is guessing where the computer's ships are 
            if(useConfirm){
                if(e.getActionCommand() == "confirm"){
                    if(!otherBoard.getHitOrMiss()[confirmTileX][confirmTileY].equals("None")){
                        JOptionPane.showMessageDialog(frame, "Please tap a valid tile before confirming");
                    } else {
                        int result = otherBoard.checkHitOrMiss(confirmTileX, confirmTileY);
                        setComputerHitShips(otherBoard.getHitShips());
                        if (result == 0){
                            setTurn(1);
                            while(true){                    
                                int outcome = playerBoard.randomHitOrMiss();
                                setPlayerHitShips(playerBoard.getHitShips());
                                if(outcome == 0){
                                    setTurn(0);
                                    break;
                                }
    
                                if(outcome == 1 && outcome == 400){
                                    continue;
                                }
                            }
                        }
    
                        confirmPanel.setTileLocation(" ");
                    }
                }
                //Checks if the player already guessed that tile and informs user if they try to guess that tile again with the coordinate of that tile
                if (e.getActionCommand() == "otherBoardTile"){
                    if(otherBoard.getHitOrMiss()[confirmTileX][confirmTileY].equals("None")){
                        otherBoard.getGameTiles()[confirmTileX][confirmTileY].setBackground(Color.decode("#206d99"));
                    }
                    GameTile tappedTile = (GameTile)e.getSource();
                    confirmTileX = tappedTile.row;
                    confirmTileY = tappedTile.column;
                    if (!otherBoard.getHitOrMiss()[confirmTileX][confirmTileY].equals("None")){
                        JOptionPane.showMessageDialog(frame, ("You have already guessed at " + confirmTileX + ", " + confirmTileY));
                    } else {
                        tappedTile.setBackground(Color.BLUE);
                        String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "F", "H", "I", "J"};
                        confirmPanel.setTileLocation("   " + alphabet[confirmTileX] + (confirmTileY + 1));
                    }
                }
            } else {
                //This checks if the player is trying to confirm a tile without clicking a tile
                if (e.getActionCommand() == "otherBoardTile"){
                    if(otherBoard.getHitOrMiss()[confirmTileX][confirmTileY].equals("None")){
                        otherBoard.getGameTiles()[confirmTileX][confirmTileY].setBackground(Color.decode("#206d99"));
                    }
                    GameTile tappedTile = (GameTile)e.getSource();
                    if(!otherBoard.getHitOrMiss()[tappedTile.row][tappedTile.column].equals("None")){
                        JOptionPane.showMessageDialog(frame, "Please tap a valid tile before confirming");
                    } else {
                        int result = otherBoard.checkHitOrMiss(tappedTile.row , tappedTile.column);
                        setComputerHitShips(otherBoard.getHitShips());
                        if (result == 0){
                            setTurn(1);
                            while(true){                    
                                int outcome = playerBoard.randomHitOrMiss();
                                setPlayerHitShips(playerBoard.getHitShips());
                                if(outcome == 0){
                                    setTurn(0);
                                    break;
                                }
    
                                if(outcome == 1 && outcome == 400){
                                    continue;
                                }
                            }
                        }
    
                        confirmPanel.setTileLocation(" ");
                    }
                }
            }
            
        }
        
    }

}