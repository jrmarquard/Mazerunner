import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.Random;


public class MazeWorld {
    private Queue<Command> commands;
    private Preferences pref;
    private Maze maze;
    private Character player;
    private AIControl ai;
    private ArrayList<Entity> entities;
    private boolean lockPlayerControl;
    private boolean winStatus;
    private boolean updated;
    
    public MazeWorld (Queue<Command> commands, Preferences pref) {
        this.commands = commands;
        this.pref = pref;
        generateWorld(pref.getValue("defaultMapWidth"), pref.getValue("defaultMapHeight"));
    }
    
    /**
     * generatWorld resets the mazeWorld. It needs to intiliase everything: 
     * - create a new maze
     * - creates new entities
     *    - player
     *    - coins
     *    - enemies .. etc
     * - sets flags to defaults
     *  
     * @param height height of the maze to be generated
     * @param width width of the maze to be generated
     */
    public void generateWorld(int width, int height) {
        maze = new Maze(width, height);
        ai = new AI();
        entities = new ArrayList<Entity>();
        maze.mazeGenerator();
        player = new Character(new Coordinate(maze.getStart().getX(), maze.getStart().getY()), pref.getText("playerName"));
        
        float h = (float)maze.getHeight();
        float w = (float)maze.getWidth();
        float r = (float)pref.getValue("defaultCoinRatio");
        
        int numberOfCoins = (int)(h*w*(r/100));
        generateCoins(numberOfCoins);
        winStatus = false;
        lockPlayerControl = false;
        updated = false;
    }
    
    private void generateCoins(int instances) {

        Random rand = new Random();
        int xC = rand.nextInt(maze.getWidth());
        int yC = rand.nextInt(maze.getHeight());
        int coinValue = 5+rand.nextInt(80);
        
        for (int x = 0; x < instances; x++) {
            while (!uniqueCoordinates(xC, yC)) {
                xC = rand.nextInt(maze.getWidth());
                yC = rand.nextInt(maze.getHeight());
                coinValue = 5+rand.nextInt(80);
            }
            Coins coins = new Coins(new Coordinate(xC,yC),coinValue);
            entities.add(coins);
        }
    }
    
    private boolean uniqueCoordinates(int x, int y) {
        if (maze.isStart(x,y)) return false;
        if (maze.isFinish(x,y)) return false;
        for (Entity e : entities) {
            if (e.getX() == x && e.getY() == y) return false;
        }
        return true;
    }
    /**
     * gets the maze
     * 
     * @return the maze
     */
    public Maze getMaze() {
        return maze;
    }
    
    /**
     * Returns true if the game has been won.
     * 
     * @return the winStatus boolean
     */
    public boolean getWinStatus () {
        return winStatus;
    }
    
    /**
     * Run this after any changes in the maze. It checks for anything
     * that needs to be updated. This includes:
     * - win conditions
     * - entity collisions
     *     - player picks up coins
     *     - player dies
     *     
     * If something has happened, ask the GUI to redraw the world.
     */
    public void update() {
        // Things the mazeWorld needs to do/check
        if (hasCharacterWon()) {
            winStatus = true;
            lockPlayerControl = true;
            updated = true;
        }
        entityCollision();
        
        if (updated) {
            updated = false;
            addCommand(new Command(Com.DRAW));
        }
    }
    
    private void entityCollision () {
        Iterator<Entity> iter = entities.iterator();
        while (iter.hasNext()) {
            Entity e = iter.next();
            if (player.getCoordinate().equals(e.getCoordinate())) {
                if (e instanceof Coins) {
                    player.addCoins(((Coins)e).getValue());
                    iter.remove();
                    updated = true;
                }
            }
        }
    }
    /**
     * Return the coordinates of the player
     * 
     * @return x coordinate of the player
     */
    public Coordinate getPlayerCoordinate () {
        return player.getCoordinate();
    }
    
    /**
     * Gets the name of the character
     * 
     * @return character's name
     */
    public String getCharacterName() {
        return player.getName();
    }
    
    public void moveCharacterDown() {
        if (lockPlayerControl) {
            return;
        }
        if (maze.isDown(player.getCoordinate())) {
            player.setY(player.getY()+1);
        }
        update();
    }
    public void moveCharacterLeft() {
        if (lockPlayerControl) {
            return;
        }
        if (maze.isLeft(player.getCoordinate())) {
            player.setX(player.getX()-1);
        }
        update();
    }
    public void moveCharacterRight() {
        if (lockPlayerControl) {
            return;
        }
        if (maze.isRight(player.getCoordinate())) {
            player.setX(player.getX()+1);
        }
        update();
    }
    public void moveCharacterUp() {
        if (lockPlayerControl) {
            return;
        }
        
        if (maze.isUp(player.getCoordinate())) {
            player.setY(player.getY()-1);
        }
        update();
    }

    public boolean hasCharacterWon() {
        return maze.getFinishCoordinate().equals(getPlayerCoordinate());
    }
    public void addCommand (Command c) {
        commands.add(c);
    }
    
    public void solveCharacter() {
        // Ask AI to make a move and add that to the command queue
        try {
            addCommand(ai.makeMove(this));
            Thread.sleep(50);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        
        // if the player has reached the end
        if (winStatus) {
            pref.toggleBool("autoComplete");
        }
        
        // if the player is set to auto complete, send another solve command
        if (pref.getBool("autoComplete")) {
            addCommand(new Command(Com.SOLVE));
        }
        update();
    }

    public int getPlayerCoins() {
        return player.getCoins();
    }
    
    public ArrayList<Coordinate> getEntityCoordinates() {
        ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
        for (Entity e : entities) {
            coords.add(e.getCoordinate());
        }
        return coords;
    }
    
    public Coordinate getStart() {
        return new Coordinate(maze.getStart().getX(), maze.getStart().getY());
    }
    public Coordinate getFinish() {
        return new Coordinate(maze.getFinish().getX(), maze.getFinish().getY());
    }
    
}