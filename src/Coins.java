
public class Coins extends Item {
    
    private int value;
    
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Coins(Coordinate coord, int value) {
        super(coord);
        this.value = value;
    }

}
