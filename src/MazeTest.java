
public class MazeTest {

	public static void main(String[] args) {
		Maze maze = new Maze(10, 10);
		maze.mazeGenerator();
		maze.KeyAndDoorGenerator();
		maze.printMaze();
	}
}
