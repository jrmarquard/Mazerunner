import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class Maze {
	private ArrayList<ArrayList<Node>> nodes;
	
	private Node start;
	private Node finish;
	private Node doorStart;
	private Node doorFinish;
	
	private int width;
	private int height;
	
	public Maze(int width, int height) {
		this.nodes = new ArrayList<ArrayList<Node>>();
		
		int counterW = 0;
		int counterH = 0;
		
		while (counterW < width) {
			this.nodes.add(new ArrayList<Node>());
			while (counterH < height) {
				this.nodes.get(counterW).add(new Node(counterW, counterH));
				counterH++;
			}
			counterH = 0;
			counterW++;
		}
		
		this.start = null;
		this.finish = null;
		
		this.width = width;
		this.height = height;
	}
	
	public Node getStart() {
		return this.start;
	}
	public Coordinate getStartCoordinate() {
	    return start.getCoordinate();
	}
	
	public Node getFinish() {
		return this.finish;
	}
    public Coordinate getFinishCoordinate() {
        return finish.getCoordinate();
    }
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public Node getNode(int x, int y) {
		return this.nodes.get(x).get(y);
	}
	public Node getNode(Coordinate c) {
	    return this.nodes.get(c.getX()).get(c.getY());
	}
	
	public boolean isAdjacent(int x1, int y1, int x2, int y2) {
	    return getNode(x1,y1).isAdjacent(getNode(x2,y2));
	}
	
	public boolean isDown(Coordinate c) {
        if (getNode(c).getDown() == null) {
            return false;
        } else {
            return true;
        }
	}
	
	public boolean isUp(Coordinate c) {
        if (getNode(c).getUp() == null) {
            return false;
        } else {
            return true;
        }
	}

    public boolean isRight(Coordinate c) {
        if (getNode(c).getRight() == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isLeft(Coordinate c) {
        if (getNode(c).getLeft() == null) {
            return false;
        } else {
            return true;
        }
    }
	
	public boolean isStart(int x, int y) {
	    return getStart().equals(new Node(x,y));
	}

    public boolean isFinish(int x, int y) {
        return getFinish().equals(new Node(x,y));
    }
    
    public boolean isDoor(Coordinate coorA, Coordinate coorB) {
    	if((this.doorStart.getCoordinate().equals(coorA) && 
    			this.doorFinish.getCoordinate().equals(coorB)) ||
    			(this.doorStart.getCoordinate().equals(coorB) && 
    			this.doorFinish.getCoordinate().equals(coorA))) {
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
	
	public void printMaze() {
	   int width = 0;
	   int height = 0;
	   System.out.print("  ");
	   while (width < this.width) {
		   if (this.getNode(width, height).getUp() == null) {
			   System.out.print("___ ");
		   } else if (this.getNode(width, height).getUp() == this.getFinish()){
			   System.out.print(" F  ");
		   } else if (this.getNode(width, height).getUp() == this.getStart()){
			   System.out.print(" S  ");
		   }
		   width++;
	   }
	   
	   System.out.print("\n");
	   
	   while (height < this.height) {
		   width = 0;
		   if (this.getNode(width, height).getLeft() == null) {
			   System.out.print(" |");
		   } else if (this.getNode(width, height).getLeft() == this.getFinish()){
			   System.out.print("F ");
		   } else if (this.getNode(width, height).getLeft() == this.getStart()){
			   System.out.print("S ");
		   }
		   
		   while (width < this.width) {
		       System.out.print("   ");
			   if (this.getNode(width, height).getRight() == null) {
				   System.out.print("|");
			   } else {
				   System.out.print(" ");
			   }
			   width++;
	   	   }
		   width--;
		   
		   if (this.getNode(width, height).getRight() == this.getFinish()){
			   System.out.print("F ");
		   } else if (this.getNode(width, height).getRight() == this.getStart()){
			   System.out.print("S ");
		   }
		   System.out.print("\n");
		   width = 0;
		   
		   if (this.getNode(width, height).getLeft() == null) {
			   System.out.print(" |");
		   } else {
			   System.out.print("  ");
		   }
		   
		   while (width < this.width) {
			   if (this.getNode(width, height).getDown() == null) {
				   System.out.print("___");
			   } else {
				   System.out.print("   ");
			   }
			   if (this.getNode(width, height).getRight() == null) {
				   System.out.print("|");
			   } else {
				   System.out.print(" ");
			   }
			   
			   width++;
		   }
		   
		   System.out.print("\n");
		   height++;
	   }
	   height--;
	   width = 0;
	   System.out.print("   ");
	   
	   while (width < this.width) {
		   if (this.getNode(width, height).getDown() == this.getFinish()) {
			   System.out.print("F   ");
		   } else if (this.getNode(width, height).getDown() == this.getStart()) {
			   System.out.print("S   ");
		   } else {
			   System.out.print("    ");
		   }
		   width++;
	   }
	   System.out.print("\n");
	}
	
	/**
	 * Generates a maze
	 */
	public void mazeGenerator() {
		Stack<Node> explore = new Stack<Node>();
		ArrayList<Node> visited = new ArrayList<Node>();
		Random rand = new Random();
		
		this.start = this.getNode(rand.nextInt(this.width), rand.nextInt(this.height));
		Node currNode = this.getStart();
		explore.add(currNode);
		visited.add(currNode);
		
		while(!explore.isEmpty()) {
			ArrayList<Node> neighbourList = unvisitNeighbour(currNode, visited);
			
			if(!neighbourList.isEmpty()) {
				int	randomNum = rand.nextInt(neighbourList.size());
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
	
	public void KeyAndDoorGenerator() {
		ArrayList<Node> path = new ArrayList<Node>();
		ArrayList<Node> shortestPath = new ArrayList<Node>();
		Node deadEnd = null;
		boolean pathFound = false;
		boolean deadEndFound = false;
		
		Queue<Node> q = new LinkedList<Node>();
		Queue<Node> visited = new LinkedList<Node>();
		
		q.add(start);
		while (!q.isEmpty()){
			Node n = q.remove();
			visited.add(n);
			
			ArrayList<Node> reachable = new ArrayList<Node>();
			if (n.getLeft() != null) reachable.add(n.getLeft());
			if (n.getDown() != null) reachable.add(n.getDown());
			if (n.getRight() != null) reachable.add(n.getRight());
			if (n.getUp() != null) reachable.add(n.getUp());
			
			if(!deadEndFound && !n.equals(this.start)) {
				if(reachable.size() == 1) {
					deadEnd = n;
					deadEndFound = true;
				}
			}
			
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
					q.add(neighbour);
				}
				i++;
			}
			
			if(pathFound) {
				break;
			}
		}
		
		int halfPoint = shortestPath.size() / 2;
		Node nodeA = shortestPath.get(halfPoint);
		Node nodeB = shortestPath.get(halfPoint + 1);
		this.doorStart = nodeA;
		this.doorFinish = nodeB;
		System.out.print("Start ");
		this.start.print();
		System.out.print("Finish ");
		this.finish.print();
		System.out.print("Door ");
		nodeA.print();
		System.out.print("Door ");
		nodeB.print();
		System.out.print("Key ");
		deadEnd.print();
		
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
	
	private void findAndSetFinish() {
		Queue<Node> explore = new LinkedList<Node>();
		LinkedList<Node> visited = new LinkedList<Node>();
		
		explore.add(getStart());
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
					explore.add(neighbour);
				}
			}
		}
		
		Node node = visited.getLast();
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

    public boolean isNorthWall(Coordinate coord) {
        return !getNode(coord).isUp();
    }
    public boolean isEastWall(Coordinate coord) {
        return !getNode(coord).isRight();
    }
    public boolean isSouthWall(Coordinate coord) {
        return !getNode(coord).isDown();
    }
    public boolean isWestWall(Coordinate coord) {
        return !getNode(coord).isLeft();
    }
}
