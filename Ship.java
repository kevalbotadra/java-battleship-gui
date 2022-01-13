package battleship;

import java.awt.Color;

public class Ship{
    String name;
    int index;
    int length;
    Color color;
    
    Direction direction;

    int startingX;
    int startingY;
    
    public Ship(String name, int length, int idx, Color c){
        this.name = name;
        this.length = length;
        this.index = idx;
        this.direction = Direction.UNSET;
        this.color = c;
    }
    
    public void setDirection(Direction direction){
        this.direction = direction;
    }

    public void setXAndY(int x, int y){
        startingX = x;
        startingY = y;
    }

    public int getX(){
        return startingX;
    }

    public int getY(){
        return startingY;
    }

    public int getIdx(){
        return index;
    }

    public Direction getDirection(){
        return direction;
    }

    public int getLength(){
        return length;
    }

    public Color getColor(){
        return color;
    }

    //public String directionToString(){
            
    //// }
}