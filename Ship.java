package battleship;


public class Ship{
    String name;
    int length;
    Direction direction;
    
    public Ship(String name, int length){
        this.name = name;
        this.length = length;

        this.direction = Direction.UNSET;
    }

    public boolean isDirectionSet(){
        if(direction == Direction.UNSET){
            return false;
        }
        else{
            return true;
        }
    }
    
    public void setDirection(Direction direction){
        if(direction != Direction.UNSET && direction != Direction.HORIZONTAL && direction != Direction.VERTICAL){
            this.direction = direction;
        }
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