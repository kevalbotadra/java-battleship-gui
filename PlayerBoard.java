package battleship;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.DimensionUIResource;

import org.w3c.dom.events.MouseEvent;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayerBoard extends JPanel {
    private static int num = 11;

    // variables to keep track of turns
    private boolean turn = false;

    private static GameTile[][] gameTiles;

    private boolean[][] shipPlacements;

    private boolean[][] hitOrMiss;

    private Ship carrier;
    private Ship battleship;
    private Ship submarine;
    private Ship destroyer;
    private Ship cruiser;

    private Ship[] ships;

    public PlayerBoard(ActionListener listener, Ship carrier, Ship battleship, Ship submarine, Ship destroyer, Ship cruiser) {
        setLayout(new GridLayout(num, num));
        setBounds(375, 90, 650, 650);

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

        // set the ships list to all the ships
        ships = new Ship[5];
        ships[0] = carrier;
        ships[1] = battleship;
        ships[2] = submarine;
        ships[3] = cruiser;
        ships[4] = destroyer;

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

    public void resetBoard() {
        // loops over every row
        for (int i = 0; i < 10; i++) {
            // loop over every column
            for (int k = 0; k < 10; k++) {
                shipPlacements[i][k] = false;
                gameTiles[i][k].setIcon(null);
                gameTiles[i][k].revalidate();
            }
        }
    }

    public int placeShip(Ship ship, int xCord, int yCord, Direction direction) {
        System.out.println(ship.name + direction);
        ship.setDirection(direction);
        if (direction == Direction.HORIZONTAL) {
            if ((yCord + ship.length) > 11) {
                return 0;
            }
        } else if (direction == Direction.VERTICAL) {
            if ((xCord - ship.length) < 0) {
                return 0;
            }
        }

        int checkCounter = 0;
        for (int i = 0; i < ship.length; i++) {
            if (direction == Direction.HORIZONTAL) {
                if(gameTiles[xCord][(yCord + checkCounter)].getIcon() != null){
                    return 1;
                }

                checkCounter++;
            } else if (direction == Direction.VERTICAL) {
                if(gameTiles[(xCord - checkCounter)][yCord].getIcon() != null){
                    return 1;
                }

                checkCounter++;
            }
        }


        int counter = 0;
        for (int i = 0; i < ship.length; i++) {
            if (direction == Direction.HORIZONTAL) {
                ship.setXAndY(yCord, xCord);
                ImageIcon imageIcon = new ImageIcon("BattleshipImages/" + ship.name + "/" + (i + 1) + ".png"); // load the image to a
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg); // transform it back
                System.out.println(xCord + ", " +(yCord + counter));
                gameTiles[xCord][yCord + counter].setIcon(imageIcon);

                counter++;
            } else if (direction == Direction.VERTICAL) {
                ship.setXAndY(yCord, xCord);
                ImageIcon imageIcon = new ImageIcon("BattleshipImages/" + ship.name + "/" + (i + 1) + "_flip.png"); // load the image to a
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg); // transform it back
                gameTiles[(xCord - counter)][yCord].setIcon(imageIcon);

                counter++;
            }
        }

        if(ship.name.equals("Destroyer")){return 3;}

        return 2;
    }

    public void gatherShipPlacements(){
        // loops over every row
        for (int i = 0; i < 10; i++) {
            // loop over every column
            for (int k = 0; k < 10; k++) {
                if(gameTiles[i][k].getIcon() != null){
                    shipPlacements[i][k] = true;
                } else {
                    shipPlacements[i][k] = false;
                }
            }
        }
    }

    public void randomizeBoard(){
        resetBoard();
        // instantate a random class
        Random rand = new Random();

        // iterate over every ship
        for (Ship ship : ships) {
            while (true) {
                // generate a random x and y coordinate
                int x = rand.nextInt(10);
                int y = rand.nextInt(10);

                // generate a random direction (horizontal : 0 or vertical : 1)
                int directionIdx = rand.nextInt(2);

                Direction direction;
                if(directionIdx == 0){
                    direction = Direction.HORIZONTAL;
                } else {
                    direction = Direction.VERTICAL;
                }

                // overlap boolean indicates if overlap exists
                boolean overlap = false;

                if (direction == Direction.HORIZONTAL) {
                    if (y > (10 - ship.length)) {
                        y = y - ship.length;
                    }

                    for (int i = 0; i < ship.length; i++) {
                        if (gameTiles[x][y + i].getIcon() != null) {
                            System.out.println("overlap " + x + ", " + y);
                            overlap = true;
                        }
                    }
                }

                if (direction == Direction.VERTICAL) {
                    if (x < (10 - ship.length)) {
                        x = x + ship.length;
                    }

                    for (int i = 0; i < ship.length; i++) {
                        if (gameTiles[x - i][y].getIcon() != null) {
                            System.out.println("overlap " + x + ", " + y);
                            overlap = true;
                        }
                    }
                }

                if (!overlap) {
                    if (direction == Direction.HORIZONTAL) {
                        ship.direction = direction;
                        if (y > (10 - ship.length)) {
                            y = y - ship.length;
                        }

                        for (int i = 0; i < ship.length; i++) {
                            ImageIcon imageIcon = new ImageIcon("BattleshipImages/" + ship.name + "/" + (i + 1) + ".png"); // load the image to a
                            Image image = imageIcon.getImage(); // transform it
                            Image newimg = image.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                            imageIcon = new ImageIcon(newimg); // transform it back
                            gameTiles[x][y + i].setIcon(imageIcon);
                        }
                    }

                    if (direction == Direction.VERTICAL) {
                        ship.direction = direction;
                        if (x < (10 - ship.length)) {
                            x = x + ship.length;
                        }

                        for (int i = 0; i < ship.length; i++) {
                            ImageIcon imageIcon = new ImageIcon("BattleshipImages/" + ship.name + "/" + (i + 1) + "_flip.png"); // load the image to a
                            Image image = imageIcon.getImage(); // transform it
                            Image newimg = image.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                            imageIcon = new ImageIcon(newimg); // transform it back
                            gameTiles[x - i][y].setIcon(imageIcon);
                        }
                    }
                    break;
                }
            }
        }
    }

    public void eraseShip(Ship ship, int xCord, int yCord) {
        System.out.println(ship.name + ship.direction);
        int counter = 0;
        for (int i = 0; i < ship.length; i++) {
            if (ship.direction == Direction.HORIZONTAL) {
                gameTiles[xCord][(yCord + counter)].setIcon(null);
                gameTiles[xCord][(yCord + counter)].revalidate();

                counter++;
            } else if (ship.direction == Direction.VERTICAL) {
                gameTiles[(xCord - counter)][yCord].setIcon(null);
                gameTiles[(xCord - counter)][yCord].revalidate();

                counter++;
            }
        }
    }

}
