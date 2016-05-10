
import java.util.ArrayList;
import java.util.Scanner;

public class MazeRunner {

	public static void main (String[] args) throws Exception {
		char move;
		boolean endGame = false;
		
		Scanner input = new Scanner(System.in);

		System.out.println(System.lineSeparator() + "	    A-MAZE-ING MAZE GAME" + System.lineSeparator());
		System.out.println("  Use WASD to move, R to re-start, Q to quit," + System.lineSeparator());
		System.out.println("             Z to solve the maze," + System.lineSeparator());
		System.out.println("	and N to generate a new maze." + System.lineSeparator());
		System.out.println("Please enter the width of the maze and press ENTER," + System.lineSeparator());
		System.out.println("Followed by the height of the maze and press ENTER." + System.lineSeparator());
		System.out.println("               Please note that" + System.lineSeparator());
		System.out.println("The width and the height have to be greater than 1" + System.lineSeparator());
		
		int width = Integer.parseInt(input.next());
		int height = Integer.parseInt(input.next());
		
		if(width <= 1 || height <= 1) {
			if(input != null) {
				input.close();
			}
			throw new Exception("Invalid width or height");
		}
		
		Maze maze = new Maze(width, height);
		maze.mazeGenerator();
		Player player = new Player(maze.getPlayerStart());
		maze.printMaze(player);
		
		move = input.next().charAt(0);
		while (endGame != true){
			if (move == 'a'){
				Node position = player.getPosition().getLeft();
				if (position != null && !position.equals(maze.getStart())){
					player.setPosition(player.getPosition().getLeft());
				}
				maze.printMaze(player);
			}
			if (move == 's'){
				Node position = player.getPosition().getDown();
				if (position != null && !position.equals(maze.getStart())){
					player.setPosition(player.getPosition().getDown());
				}
				maze.printMaze(player);
			}
			if (move == 'd'){
				Node position = player.getPosition().getRight();
				if (position != null && !position.equals(maze.getStart())){
					player.setPosition(player.getPosition().getRight());
				}
				maze.printMaze(player);
			}
			if (move == 'w'){
				Node position = player.getPosition().getUp();
				if (position != null && !position.equals(maze.getStart())){
					player.setPosition(player.getPosition().getUp());
				}
				maze.printMaze(player);
			}
			if (move == 'r'){
				player = new Player(maze.getPlayerStart());
				maze.printMaze(player);
			}
			if (move == 'n'){ 
				System.out.println("	Please Enter a new maze size.");
				System.out.println("Please enter the width of the maze and press ENTER,");
				System.out.println("Followed by the height of the maze and press ENTER.");
				
				width = Integer.parseInt(input.next());
				height = Integer.parseInt(input.next());
				
				if(width <= 1 || height <= 1) {
					if(input != null) {
						input.close();
					}
					throw new Exception("Invalid width or height");
				}
				
				maze = new Maze(width, height);
				maze.mazeGenerator();
				player = new Player(maze.getPlayerStart());
				maze.printMaze(player);
			}
			if (move == 'q'){
				endGame = true;
				System.out.println("	Quitting. Thanks for playing!");
			}
			if (move == 'z'){
				AI ai = new AI();
				ArrayList<Node> path = ai.traverseMaze(maze, player.getPosition());
				Node next = path.remove(0);
				while(!path.isEmpty()){
					next = path.remove(0);
					player.setPosition(next);
					maze.printMaze(player);
					try {
					    Thread.sleep(150);
					} catch(InterruptedException ex) {
					    Thread.currentThread().interrupt();
					}
				}
			}
			if (player.getPosition().equals(maze.getFinish())){
				System.out.println("	Congratulations!!! Enter a new maze size to play again.");
				System.out.println("Please enter the width of the maze and press ENTER,");
				System.out.println("Followed by the height of the maze and press ENTER.");
				
				width = Integer.parseInt(input.next());
				height = Integer.parseInt(input.next());
				
				if(width <= 1 || height <= 1) {
					if(input != null) {
						input.close();
					}
					throw new Exception("Invalid width or height");
				}
				
				maze = new Maze(width, height);
				maze.mazeGenerator();
				player = new Player(maze.getPlayerStart());
				maze.printMaze(player);
			}
			move = input.next().charAt(0);			
		}
		if (input != null){
			input.close();
		}
	}
}
