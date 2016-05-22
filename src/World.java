import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

/**
 * World contains:
 * - 1 maze
 * - Unlimited beings
 * - Unlimited items
 * 
 * @author John
 *
 */
public class World {
    
    private String name;
    private Queue<Command> commands;
    private boolean updateFlag;
    
    // Maze data
    private ArrayList<ArrayList<Node>> nodes;
    private Node start;
    private Node finish;
    private int width;
    private int height;
    
    // Copying in from merge
    private Node doorStart;
    private Node doorFinish;
    private Node key;
    
    
    // Multiple beings in a world
    // Beings are things that can move/make decisions
    private Map<String, Being> beings;
    
    // Multiple items in a world.
    // Items are stationary objects that can be interacted with by 
    private ArrayList<Item> items;
    
    public World (Queue<Command> commands, String name, int width, int height) {
        this.commands = commands;
        this.name = name;
        this.updateFlag = false;
        
        this.nodes = new ArrayList<ArrayList<Node>>();
        this.beings = new HashMap<String, Being>();
        this.items = new ArrayList<Item>();
        
        this.width = width;
        this.height = height;
        
        for (int w = 0; w < width; w++) {
            this.nodes.add(new ArrayList<Node>());
            for (int h = 0; h < height; h++) {
                this.nodes.get(w).add(new Node(w, h));
            }
        }
        
        this.doorStart = null;
        this.doorFinish = null;
        this.key = null;
        
        // Maze generator connects nodes together and sets start/finish.
        mazeGenerator();
        generateCoins();
        doorAndKeyGenerator();
    }
    
    public void addPlayer(String name) {
        Being player = new Being(getStartNode(), name);
        beings.put(name, player);
    }
    
    public void generateCoins() {
        float h = (float)getHeight();
        float w = (float)getWidth();
        float r = (float)15;
        int numberOfCoins = (int)(h*w*(r/100));
        
        Random rand = new Random();
        int xC = rand.nextInt(getWidth());
        int yC = rand.nextInt(getHeight());
        int coinValue = 50;
        
        for (int x = 0; x < numberOfCoins; x++) {
            xC = rand.nextInt(getWidth());
            yC = rand.nextInt(getHeight());
            Node n = getNode(xC, yC);
            if (start.equals(n)) continue;
            if (finish.equals(n)) continue;
            for (Item i : items) {
                if (i.getNode().equals(n)) continue;
            }
            Coins coins = new Coins(n,coinValue);
            items.add(coins);
        }
    }

    public int getPlayerCoins(String id) {
        return beings.get(id).getCoins();
    }
    
    public ArrayList<Node> getEntityNodes() {
        ArrayList<Node> coords = new ArrayList<Node>();
        for (Item i : items) {
            coords.add(i.getNode());
        }
        return coords;
    }
    
    public void moveBeing(String id, String dir) {
        Being b = beings.get(id);
        Node n = b.getNode();
        if (b != null) {
            if (dir == "up" && n.getUp() != null) {
                b.setNode(n.getUp()); 
                updateFlag = true;
            } else if (dir == "down" && n.getDown() != null) {
                b.setNode(n.getDown()); 
                updateFlag = true;
            } else if (dir == "left" && n.getLeft() != null) {
                b.setNode(n.getLeft());
                updateFlag = true;
            } else if (dir == "right" && n.getRight() != null) {
                b.setNode(n.getRight());
                updateFlag = true;
            }
            if (updateFlag) {
                addCommand(new Command(Com.SOUND_MSG, new String[]{"play", "step"}));
                updateFlag = false;
            }
        }
        update();
    }    

    /**
     * Run this after any changes in the maze. It checks for anything
     * that needs to be updated. This includes:
     * - win conditions
     * - entity collisions
     *     - player picks up coins
     *     - player dies
     */
    public void update() {
        
        // Check if there are any winnders
        beingCollision();
        itemCollision();
    }
    private void beingCollision() {
        Iterator<String> iter = beings.keySet().iterator();
        while (iter.hasNext()) {
            Being b = beings.get(iter.next());
            if (b.getName().equals(start)) {
                // winner winner chicken dinner
                addCommand(new Command(Com.SOUND_MSG, new String[]{"play", "finish"}));
                
            }
        }
    }
    private void itemCollision () {
        Iterator<Item> iter = items.iterator();
        while (iter.hasNext()) {
            Entity e = iter.next();
            
            for(Map.Entry<String,Being> entry : beings.entrySet()) {
                Being b = entry.getValue();
                if (b.getNode().equals(e.getNode())) {
                    if (e instanceof Coins) {
                        b.addCoins(((Coins)e).getValue());
                        iter.remove();
                        addCommand(new Command(Com.SOUND_MSG, new String[]{"play", "coin"}));
                    }
                }
            }
        }
    }
    
    private void addCommand (Command c) {
        commands.add(c);
    }
    
    /**
     * Return the coordinates of the player
     * 
     * @return x coordinate of the player
     */
    public Node getPlayerCoordinate () {
        return beings.get("Moneymaker").getNode();
    }

    public String getName() {
        return this.name;
    }
    public Node getStartNode() {
        return this.start;
    }
    public Node getFinishNode() {
        return this.finish;
    }
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
    public Node getKeyNode() {
        return key;
    }

    public Node getBeingCoordinate(String id) {
        return beings.get(id).getNode();
    }
    public Node getPlayerNode() {
        return beings.get("Moneymaker").getNode();
    }
    
    /**
     * Get the node at the x and y coordinates given.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return Node at x and y.
     */
    public Node getNode(int x, int y) {
        return this.nodes.get(x).get(y);
    }
    
    /**
     * Checks if the x and y coordinates given are corrected to each other.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public boolean isConnected(int x1, int y1, int x2, int y2) {
        return getNode(x1,y1).isConnected(getNode(x2,y2));
    }
    
    public boolean isDoor(int xA, int yA, int xB, int yB) {
        if((this.getNode(xA, yA).equals(this.doorStart) &&
                this.getNode(xB, yB).equals(this.doorFinish)) ||
                (this.getNode(xA, yA).equals(this.doorFinish) &&
                this.getNode(xB, yB).equals(this.doorStart))) {
            return true;
        } else {
            return false;
        }
    }      
    
    public void makePath(int xA, int yA, int xB, int yB) {
        if (xA == xB || yA == yB) {
            if ((xA + 1) == xB) {
                this.getNode(xA, yA).setRight(this.getNode(xB, yB));
                this.getNode(xB, yB).setLeft(this.getNode(xA, yA));
            } else if ((xB + 1) == xA) {
                this.getNode(xA, yA).setLeft(this.getNode(xB, yB));
                this.getNode(xB, yB).setRight(this.getNode(xA, yA));
            } else if ((yA + 1) == yB) {
                this.getNode(xA, yA).setDown(this.getNode(xB, yB));
                this.getNode(xB, yB).setUp(this.getNode(xA, yA));
            } else if ((yB + 1) == yA) {
                this.getNode(xA, yA).setUp(this.getNode(xB, yB));
                this.getNode(xB, yB).setDown(this.getNode(xA, yA));
            } else {
                //Illegal
            }
        } else {
            //Illegal
        }
    }
    
    /**
     * Generates a maze
     */
    public void mazeGenerator() {
        Stack<Node> explore = new Stack<Node>();
        ArrayList<Node> visited = new ArrayList<Node>();
        Random rand = new Random();
        
        this.start = this.getNode(rand.nextInt(this.width), rand.nextInt(this.height));
        Node currNode = this.getStartNode();
        explore.add(currNode);
        visited.add(currNode);
        
        while(!explore.isEmpty()) {
            ArrayList<Node> neighbourList = unvisitNeighbour(currNode, visited);
            
            if(!neighbourList.isEmpty()) {
                int randomNum = rand.nextInt(neighbourList.size());
                Node chosen = neighbourList.get(randomNum);
                
                explore.push(currNode);
                connectNodes(currNode, chosen);
                currNode = chosen;
                visited.add(chosen);
            } else {
                currNode = explore.pop();
            }
        }
        this.findAndSetFinish();
    }
    
    private void findAndSetFinish() {
        Queue<Node> newExplore = new LinkedList<Node>();
        LinkedList<Node> newVisited = new LinkedList<Node>();
        
        newExplore.add(getStartNode());
        while (!newExplore.isEmpty()){
            Node n = newExplore.remove();
            newVisited.add(n);
            
            ArrayList<Node> reachable = new ArrayList<Node>();
            if (n.getLeft() != null) reachable.add(n.getLeft());
            if (n.getDown() != null) reachable.add(n.getDown());
            if (n.getRight() != null) reachable.add(n.getRight());
            if (n.getUp() != null) reachable.add(n.getUp());
            
            for(Node neighbour: reachable){
                if (!newVisited.contains(neighbour)){
                    newExplore.add(neighbour);
                }
            }
        }
        
        Node node = newVisited.getLast();
        this.finish = this.getNode(node.getX(), node.getY());
    }
    
    /**
     * Returns a list of unvisit neighbors from the given node, 
     * if there is no unvisit neighbors, an empty list will be returned
     * @param node the node for visiting its neighbors
     * @param visited a list that contains the nodes that have been visited
     * @return a list of unvisit neighbors from the given node
     */
    private ArrayList<Node> unvisitNeighbour(Node node, ArrayList<Node> visited) {
        ArrayList<Node> unvisit = new ArrayList<Node>();
        int x = node.getX();
        int y = node.getY();
        
        // Looks for upper neighbour
        if(y + 1 < this.height) {
            if(!visited.contains(this.getNode(x, y + 1))) {
                unvisit.add(this.getNode(x, y + 1));
            }
        }
        
        // Looks for lower neighbour
        if(y - 1 >= 0) {
            if(!visited.contains(this.getNode(x, y - 1))) {
                unvisit.add(this.getNode(x, y - 1));
            }
        }
        
        // Looks for left neighbour
        if(x - 1 >= 0) {
            if(!visited.contains(this.getNode(x - 1, y))) {
                unvisit.add(this.getNode(x - 1, y));
            }
        }
        
        // Looks for right neighbour
        if(x + 1 < this.width) {
            if(!visited.contains(this.getNode(x + 1, y))) {
                unvisit.add(this.getNode(x + 1, y));
            }
        }
        
        return unvisit;
    }
    
    /**
     * Connects the two given nodes by comparing their coordinates
     * @param nodeA the first node to be connected
     * @param nodeB the second node to be connected
     */
    private void connectNodes(Node nodeA, Node nodeB) {
        int xA = nodeA.getX();
        int yA = nodeA.getY();
        int xB = nodeB.getX();
        int yB = nodeB.getY();
        
        // If nodeA is above nodeB
        if(xA == xB && yA == yB - 1) {
            nodeA.setDown(nodeB);
            nodeB.setUp(nodeA);
        } 
        
        // If nodeA is below nodeB
        else if(xA == xB && yA == yB + 1) {
            nodeA.setUp(nodeB);
            nodeB.setDown(nodeA);
        } 
        
        // If nodeA is left to nodeB
        else if(xA == xB - 1 && yA == yB) {
            nodeA.setRight(nodeB);
            nodeB.setLeft(nodeA);
        } 
        
        // If nodeA is right to nodeB
        else if(xA == xB + 1 && yA == yB) {
            nodeA.setLeft(nodeB);
            nodeB.setRight(nodeA);
        }
    } 

    
    /**
     * Fills in the shortest path with the given start and destination
     * @param shortestPath the List that will be storing the shortest path
     * @param path the path created by bfs
     * @param start the starting point
     * @param dest the destination
     */
    private void processPath(ArrayList<Node> shortestPath, ArrayList<Node> path, 
            Node start, Node dest){
        int i = path.indexOf(dest);
        Node source = path.get(i + 1);
        
        shortestPath.add(0, dest);
        if (source.equals(start)) {
            shortestPath.add(0, start);
            return;
        } else {
            processPath(shortestPath, path, start, source);
        }
    }

    /**
     * Generates a door and a key in the maze
     */
    public void doorAndKeyGenerator() {
        ArrayList<Node> path = new ArrayList<Node>();
        ArrayList<Node> shortestPath = new ArrayList<Node>();
        boolean pathFound = false;
        
        Queue<Node> explore = new LinkedList<Node>();
        LinkedList<Node> visited = new LinkedList<Node>();
        
        // finds the shortest path from start to finish
        explore.add(start);
        while (!explore.isEmpty()){
            Node n = explore.remove();
            visited.add(n);
            
            ArrayList<Node> reachable = new ArrayList<Node>();
            if (n.getLeft() != null) reachable.add(n.getLeft());
            if (n.getDown() != null) reachable.add(n.getDown());
            if (n.getRight() != null) reachable.add(n.getRight());
            if (n.getUp() != null) reachable.add(n.getUp());
            
            int i = 0;
            while(i != reachable.size()){
                Node neighbour = reachable.get(i);
                path.add(neighbour);
                path.add(n);
                
                if (neighbour.equals(this.finish)){
                    processPath(shortestPath, path, start, this.finish);
                    pathFound = true;
                    break;
                } else if (!visited.contains(neighbour)){
                    explore.add(neighbour);
                }
                i++;
            }
            
            if(pathFound) {
                break;
            }
        }
        
        // Sets the middle point of the shortest path as a door
        // to block the path
        int halfPoint = shortestPath.size() / 2;
        this.doorStart = shortestPath.get(halfPoint);
        this.doorFinish = shortestPath.get(halfPoint + 1);
        this.disconnectNodes(this.doorStart, this.doorFinish);
        
        // Sets the cost of each of the nodes of the starting side
        this.resetNodeCost();
        this.setNodeCost(this.start);
        this.setNodeCost(this.doorStart);
        
        // Finds the node with the greatest cost from start
        // and from the door and set this as the location of the key
        for(ArrayList<Node> aList: this.nodes) {
            for(Node node: aList) {
                if(this.key == null) {
                    this.key = node;
                } else if(node.getCost() > this.key.getCost()) {
                    this.key = node;
                }
            }
        }
    }
    
    /**
     * Disconnects the given two nodes, ie make a wall between them
     * @param nodeA the first node to be disconnected
     * @param nodeB the second node to be disconnected
     */
    private void disconnectNodes(Node nodeA, Node nodeB) {
        int xA = nodeA.getX();
        int yA = nodeA.getY();
        int xB = nodeB.getX();
        int yB = nodeB.getY();
        
        // If nodeA is above nodeB
        if(xA == xB && yA == yB - 1) {
            nodeA.setDown(null);
            nodeB.setUp(null);
        } 
        
        // If nodeA is below nodeB
        else if(xA == xB && yA == yB + 1) {
            nodeA.setUp(null);
            nodeB.setDown(null);
        } 
        
        // If nodeA is left to nodeB
        else if(xA == xB - 1 && yA == yB) {
            nodeA.setRight(null);
            nodeB.setLeft(null);
        } 
        
        // If nodeA is right to nodeB
        else if(xA == xB + 1 && yA == yB) {
            nodeA.setLeft(null);
            nodeB.setRight(null);
        }
    }
    

    
    /**
     * Sets the cost of the nodes from the given node by doing a bfs
     * @param node the starting node
     */
    private void setNodeCost(Node node) {
        Queue<Node> explore = new LinkedList<Node>();
        LinkedList<Node> visited = new LinkedList<Node>();
        int i = 1;
        
        explore.add(node);
        while (!explore.isEmpty()){
            Node n = explore.remove();
            visited.add(n);
            
            ArrayList<Node> reachable = new ArrayList<Node>();
            if (n.getLeft() != null) reachable.add(n.getLeft());
            if (n.getDown() != null) reachable.add(n.getDown());
            if (n.getRight() != null) reachable.add(n.getRight());
            if (n.getUp() != null) reachable.add(n.getUp());
            
            for(Node neighbour: reachable){
                if (!visited.contains(neighbour)){
                    neighbour.addCost(i);
                    explore.add(neighbour);
                }
            }
            
            i++;
        }
    }
    /**
     * Resets the cost of all the nodes
     */
    private void resetNodeCost() {
        for(ArrayList<Node> aList: this.nodes) {
            for(Node node: aList) {
                node.resetCost();
            }
        }
    }
}