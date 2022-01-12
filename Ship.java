package battleship;


public class Ship{
    String name;
    int length;
    Direction direction;

    int startingX;
    int startingY;
    
    public Ship(String name, int length){
        this.name = name;
        this.length = length;

        this.direction = Direction.UNSET;
    }
    
    public void setDirection(Direction direction){
        this.direction = direction;
    }

    public void setXAndY(int x, int y){
        startingX = x;
        startingY = y;
    }

    public Direction getDirection(){
        return direction;
    }

    public int getLength(){
        return length;
    }

    //public String directionToString(){
            
    //// }
}