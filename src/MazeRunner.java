
import java.util.ArrayList;
import java.util.Scanner;

public class MazeRunner {

	public static void main (String[] args) {
		char move;
		boolean endGame = false;
		
		Scanner input = new Scanner(System.in);

		System.out.println(System.lineSeparator() + "	    A-MAZE-ING MAZE GAME" + System.lineSeparator());
		System.out.println("  Use WASD to move, R to re-start, Q to quit," + System.lineSeparator());
		System.out.println("	and N to generate a new maze." + System.lineSeparator());
		System.out.println("please enter the maze size (n*n) then press ENTER" + System.lineSeparator());
		
		int n = Integer.parseInt(input.next());
		
		Maze maze = new Maze(n, n);
		maze.mazeGenerator();
		maze.setStart(-1, 0);
		maze.setFinish(n, n-1);
		Player player = new Player(maze.getNode(0, 0));
		maze.printMaze(player);
		
		move = input.next().charAt(0);
		while (endGame != true){
			if (move == 'a'){
				Node position = player.getPosition().getLeft();
				if (position != null && !position.equals(maze.getStart())){
					player.setPosition(player.getPosition().getLeft());
					maze.printMaze(player);
				}
			}
			if (move == 's'){
				Node position = player.getPosition().getDown();
				if (position != null && !position.equals(maze.getStart())){
					player.setPosition(player.getPosition().getDown());
					maze.printMaze(player);
				}
				
			}
			if (move == 'd'){
				Node position = player.getPosition().getRight();
				if (position != null && !position.equals(maze.getStart())){
					player.setPosition(player.getPosition().getRight());
					maze.printMaze(player);
				}
			}
			if (move == 'w'){
				Node position = player.getPosition().getUp();
				if (position != null && !position.equals(maze.getStart())){
					player.setPosition(player.getPosition().getUp());
					maze.printMaze(player);
				}
				
			}
			if (move == 'r'){
				player = new Player(maze.getNode(0, 0));
				maze.printMaze(player);
			}
			if (move == 'n'){ 
				System.out.println("	Enter a new maze size.");
				n = Integer.parseInt(input.next());
				maze = new Maze(n, n);
				maze.mazeGenerator();
				maze.setStart(-1, 0);
				maze.setFinish(n, n-1);
				player = new Player(maze.getNode(0, 0));
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
				n = Integer.parseInt(input.next());
				maze = new Maze(n, n);
				maze.mazeGenerator();
				maze.setStart(-1, 0);
				maze.setFinish(n, n-1);
				player = new Player(maze.getNode(0, 0));
				maze.printMaze(player);
			}
			move = input.next().charAt(0);			
		}
		if (input != null){
			input.close();
		}
	}
}
