package battleship;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.DimensionUIResource;

import org.w3c.dom.events.MouseEvent;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PlayerBoard extends JPanel {
    private static int num = 11;

    // variables to keep track of turns
    private boolean turn = false;

    private static GameTile[][] gameTiles;

    private boolean[][] shipPlacements;

    private boolean[][] hitOrMiss;

    private Direction shipDirection = Direction.HORIZONTAL;

    private Ship carrier;
    private Ship battleship;
    private Ship submarine;
    private Ship destroyer;
    private Ship cruiser;

    private Ship[] ships;

    public PlayerBoard(ActionListener listener) {
        setLayout(new GridLayout(num, num));
        setBounds(375, 75, 650, 650);

        shipPlacements = new boolean[10][10];

        // define an array of GameTiles equal to 100 (the amount of game tiles that
        // should exist)
        ArrayList<GameTile> listGameTiles = new ArrayList<GameTile>();

        // iterate over the amount of tiles in this case 121
        for (int i = 0; i < num * num; i++) {
            // defines a string array that contains characters uptil J
            String[] alphabet = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };

            // if i is a multiple of 11 and is not 0
            // add a new tile with the letter corresponding to the box
            // for example 1 is A, 2 is B, so on and so forth
            // gets our vertical A-J letter labels
            if (i % 11 == 0 && i != 0) {
                this.add(new Tile(alphabet[(i / 11) - 1]));
            } else if (i <= 10 && i != 0) {
                // if the number is less than or equal to 10 and not equal to 0
                // place a tile with the number of the column
                // this just gets the column numbers
                this.add(new Tile(Integer.toString(i)));
            } else if (i == 0) {
                // if we are at the first box just add a blank tile
                this.add(new Tile(""));
            } else {
                // create a new tile, add it to the gameTiles list, and place it at the i value
                // in the gridlayout
                GameTile gameTile = new GameTile("playerBoardTile", true);
                gameTile.addActionListener(listener);
                listGameTiles.add(gameTile);
                this.add(gameTile);
            }
        }

        // reshape the gameTiles arraylist so it is 2D
        GameTile[][] arrayGameTiles = new GameTile[num][num];

        // create a counter for the tiles
        int tileCounter = 0;

        // loops over every row
        for (int i = 0; i < 10; i++) {
            // loop over every column
            for (int k = 0; k < 10; k++) {
                // get the tile at the counter value
                GameTile tile = listGameTiles.get(tileCounter);

                // assign the row value and the column value to the obtained tile
                arrayGameTiles[i][k] = tile;

                tile.row = i;
                tile.column = k;

                // increment the tile counter
                tileCounter++;
            }
        }

        // assign the newly created 2d Game Tile array to the class's game tile array
        gameTiles = arrayGameTiles;

        // create our 5 ships
        carrier = new Ship("Carrier", 5);
        battleship = new Ship("Battleship", 4);
        submarine = new Ship("Submarine", 3);
        cruiser = new Ship("Cruiser", 3);
        destroyer = new Ship("Destroyer", 2);

        // set the ships list to all the ships
        ships = new Ship[5];
        ships[0] = carrier;
        ships[1] = battleship;
        ships[2] = submarine;
        ships[3] = cruiser;
        ships[4] = destroyer;

        
        this.setFocusable(true);
        String T = "t";
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0), T);
        this.getActionMap().put(T, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shipDirection = Direction.VERTICAL;
            }
        });
        this.requestFocus();
    }


    public void disableButtons() {
        for (int i = 0; i < 10; i++) {
            for (int k = 0; k < 10; k++) {
                gameTiles[i][k].setEnabled(false);
            }
        }
    }

    public void enableButtons() {
        for (int i = 0; i < 10; i++) {
            for (int k = 0; k < 10; k++) {
                gameTiles[i][k].setEnabled(true);
            }
        }
    }

    public void clearBoard() {
        // loops over every row
        for (int i = 0; i < 10; i++) {
            // loop over every column
            for (int k = 0; k < 10; k++) {
                gameTiles[i][k].setIcon(null);
                gameTiles[i][k].revalidate();
            }
        }
    }

    public void placeShip(int xCord, int yCord) {
        Direction direction = shipDirection;
        Ship ship = carrier;
        if (direction == Direction.HORIZONTAL) {
            if ((yCord + ship.length) > 11) {
                System.out.println("here");
                return;
            }
        } else if (direction == Direction.VERTICAL) {
            if ((xCord + ship.length) > 11) {
                System.out.println("here");
                return;
            }
        }

        int counter = 0;
        for (int i = 0; i < ship.length; i++) {
            if (direction == Direction.HORIZONTAL) {
                ImageIcon imageIcon = new ImageIcon("BattleshipImages/Battleship/" + i + ".png"); // load the image to a
                                                                                                  // imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg); // transform it back
                gameTiles[xCord][(yCord + counter) - 1].setIcon(imageIcon);

                counter++;
            } else if (direction == Direction.VERTICAL) {

            }
        }
    }

    public void eraseCurrentShip(int xCord, int yCord, Direction direction) {
        Ship ship = carrier;
        int counter = 0;
        for (int i = 0; i < ship.length; i++) {
            if (direction == Direction.HORIZONTAL) {
                ImageIcon imageIcon = new ImageIcon("BattleshipImages/Battleship/" + i + ".png"); // load the image to a
                                                                                                  // imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg); // transform it back
                gameTiles[xCord][(yCord + counter) - 1].setIcon(null);
                gameTiles[xCord][(yCord + counter) - 1].revalidate();

                counter++;
            } else if (direction == Direction.VERTICAL) {

            }
        }
    }
}
