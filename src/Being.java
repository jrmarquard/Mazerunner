
public class Being extends Entity {

    private String name;
    private boolean AIControl;
    private int coins;
    
    public Being(Node node, String name) {
        super(node);        
        this.AIControl = false;
        this.name = name;
        this.coins = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isAIControl() {
        return AIControl;
    }

    public void setAIControl(boolean aiControl) {
        this.AIControl = aiControl;
    }
    public void toggleAIControl() {
        if (AIControl==false) {
            AIControl = true;
        } else {
            AIControl = false;
        }
    }

    public int getCoins() {
        return coins;
    }
    public void removeCoins(int coins) {
        this.coins -= coins;
    }

    public void addCoins(int coins) {
        this.coins += coins;
    } 
}