import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*
 * Mert Gerdan
 * 05/01/2018
 * Minesweeper JavaFX
 * 
 */

public class Minesweeper extends Application {

	Scanner input = new Scanner(System.in);
	int squareSize = customizeBoard("square size?"); // these can be changed, one tile's size. lower to get more tiles.
	int width = customizeBoard("width?");
	int height = customizeBoard("height?");
	int mineCount = customizeBoard("how many mines?");
	int flagCount = mineCount;
	boolean isOver = false;
	int time = 999;
	
	Text winText = new Text();
	Text flagsCount = new Text();
	Text timeText = new Text();
	
	List<Integer> winPercentageList = new ArrayList<Integer>();
	ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
	
	int squaresInX = width / squareSize; // determines how many squares can fit by dividing width or height's dims. to
											// squareSize pixels.
	int squaresInY = height / squareSize;

	Tile[][] board = new Tile[squaresInX][squaresInY]; //creates the board
	Scene scene;

	public int customizeBoard(String question) { //gets an input from the user
		System.out.println(question);
		return input.nextInt();
	}

	public class Tile extends StackPane { // baska bir class acmayi internetten buldum kurtardi resmen burasi
		int x, y; 
		boolean hasMine;
		boolean isOpen, hasFlag = false;

		private Rectangle border = new Rectangle(squareSize - 2, squareSize - 2); // 
		private Text text = new Text();
		private Text flagText = new Text();

		public Tile(int x, int y, boolean hasMine, boolean hasFlag) {
			this.x = x;
			this.y = y;
			this.hasMine = hasMine;
			this.hasFlag = hasFlag;

			flagText.setFont(Font.font(18));
			flagText.setVisible(false);
			text.setFont(Font.font(18));
			text.setVisible(false);
			border.setFill(Color.GREEN);
			border.setStroke(Color.BLACK);

			
			setTranslateX(x * squareSize); //places the mine on the scene
			setTranslateY(y * squareSize);
			getChildren().addAll(border, flagText, text); //add it to the stack pane
			setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                MouseButton button = event.getButton();
                if(button==MouseButton.PRIMARY){
                    openSquare(); //first, open the square
                    checkWin(); //then, check win
                }else if(button==MouseButton.SECONDARY){
                    setFlag();
                    checkWin();
                }
            }
			});
		}
		
		public void checkWin() {
			int counter = 0;
			for (int y = 0; y < squaresInY; y++) {
				for (int x = 0; x < squaresInX; x++) {
					if (!board[x][y].isOpen) { //if it is not opened
						if (!board[x][y].hasMine) { 
							return;
						}
						else if (board[x][y].hasMine) { //and has a mine
						counter++;
						}
					}
				}
			}
			if (counter == mineCount) {
				System.out.println("You win!");
				winPercentageList.add(100); //adds a win
				System.out.println("Win Percentage: "+ getWinPercentage()); //shows the statistics
				System.out.println("Best Time: " + getBestTime() + " seconds!");
			}
		}

		public void setFlag() {
			if (flagCount == 0 || isOpen) { // if the tile is already open or flag count is down to 0, dont do anything
				return;
			}
			if (hasFlag){ //if it has a flag, remove the flag and add one to the count
				flagText.setText("");
				flagCount++;
				hasFlag = false;
				border.setFill(Color.GREEN);
				flagsCount.setText("" + flagCount);
			}
			else {
				flagText.setText("F"); //else, set a flag
				hasFlag = true;
				border.setFill(Color.LIGHTBLUE);
				flagText.setVisible(true);
				flagCount--;
				flagsCount.setText("" + flagCount);
			}
			
		}
		
		private int getWinPercentage() {
			int sum = 0;
			for (int i = 0; i < winPercentageList.size(); i++) {
				sum += winPercentageList.get(i);
			}
			return sum / winPercentageList.size();
		}
		
		private int getBestTime() {
			int newTime = Integer.parseInt(timeText.getText());
			if (newTime > time) { //if the new time is not the high score, don't look at it
				return time;
			}
			else {
				time = newTime;
				return time;
			}
		}

		public void openSquare() {
			if (hasFlag) {
				setFlag();
			}
			if (isOpen)
				return; // if it already is open, don't do anything

			if (hasMine) {
				System.out.println("Game Over"); // if it has a mine, end the game
				isOver = true;
				winText.setText(".(");
				winPercentageList.add(0);
				for (int y = 0; y < squaresInY; y++) {
					for (int x = 0; x < squaresInX; x++) { //show all mines
						if (board[x][y].hasMine) {
							board[x][y].text.setText("*");
							board[x][y].text.setVisible(true);
							board[x][y].border.setFill(Color.INDIANRED);
						}
					}
				}
				System.out.println("Win Percentage: " + getWinPercentage());
				return;
			}

			isOpen = true; // if not, open the tile
			text.setVisible(true); // make him see whats inside
			border.setFill(Color.GREENYELLOW);

			if (text.getText().isEmpty()) {
				getNeighbors(this).forEach(Tile::openSquare); // if it's empty, check its neighbors and get a value,
																
			}
			
		}
	}
	
	long startTime;
	private void setTime() {
		if (isOver) {
			return;
		}
		else {
		long elapsedTime = (System.currentTimeMillis() - startTime)/1000;
		timeText.setText("" + elapsedTime);
		}
	}



	Parent fillBoard() {
		isOver = false;
		startTime = System.currentTimeMillis();
		Pane root = new Pane();
		StackPane menuItems = new StackPane();
		root.setPrefSize(width, height+60);
		Random rand = new Random();
		flagCount = mineCount;
		
		Rectangle win = new Rectangle(100,50); //the blue rectangle at the bottom that restarts the game
			win.setTranslateX(width/2.0-50);
			win.setTranslateY(height+5);
			win.setFill(Color.AQUAMARINE);
			win.setStroke(Color.BLACK);
			winText.setFont(Font.font(18));
			winText.setText(".)");
			winText.setVisible(true);
			winText.setTranslateX(width/2.0-50);
			winText.setTranslateY(height+5);
			win.	setOnMouseClicked(e -> restartGame());
		
		Rectangle flag = new Rectangle(100,50); //the flag counting rectangle
			flag.setTranslateX(10);
			flag.setTranslateY(height+5);
			flag.setFill(Color.GRAY);
			flag.setStroke(Color.BLACK);
			flagsCount.setText("" + flagCount);
			flagsCount.setTranslateX(10);
			flagsCount.setFont(Font.font(18));
			flagsCount.setTranslateY(height+5);

		
		Rectangle time = new Rectangle(100,50); //timer on the bottom right
			time.setTranslateX(width-110);
			time.setTranslateY(height+5);
			time.setFill(Color.GRAY);
			time.setStroke(Color.BLACK);
			timeText.setTranslateX(width-110);
			timeText.setFont(Font.font(18));
			timeText.setTranslateY(height+5);
		
		timer.scheduleAtFixedRate(new Runnable() {
		    @Override
		    public void run() {
		        setTime();
		    }
		}, 0, 1, TimeUnit.SECONDS);
		
		
		menuItems.getChildren().addAll(win, winText); //add them to the menuItems stack pane
		menuItems.getChildren().addAll(flag, flagsCount);
		menuItems.getChildren().addAll(time, timeText);
		root.getChildren().add(menuItems); //add the stack pane to the root pane
		
		int minesNeededToPlace = mineCount;
		if (mineCount >= squaresInX*squaresInY) { //prevents the user from trying to enter more mines than there are tiles
			mineCount = squaresInX*squaresInY - 1;
			flagCount = mineCount;
		}
		
		for (int y = 0; y < squaresInY; y++) {
			for (int x = 0; x < squaresInX; x++) { //clear the board
				board[x][y] = null;
			}
		}

		for (int i = minesNeededToPlace; i > 0; i--) {
			int mineX = rand.nextInt(squaresInX);
			int mineY = rand.nextInt(squaresInY);
			if (board[mineX][mineY] != null) { //if the random mineX and mineY coordinate already has a mine placed, it tries another tile
				minesNeededToPlace++;
			}
			else {

				Tile tile = new Tile(mineX, mineY, true, false); 
				board[mineX][mineY] = tile; // places mines on board at random
			}
		}

		for (int y = 0; y < squaresInY; y++) {
			for (int x = 0; x < squaresInX; x++) {

				if (board[x][y] == null) { //if there isn't a mine placed, it will be null, so fill them up
					Tile tile = new Tile(x, y, false, false);
					board[x][y] = tile;
					root.getChildren().add(tile);
				}
				else if (board[x][y] != null) {
					Tile tile = board[x][y];
					root.getChildren().add(tile); //get the tile and add it to the pane
				}

			}
		}

		for (int y = 0; y < squaresInY; y++) {
			for (int x = 0; x < squaresInX; x++) {
				Tile tile = board[x][y];

				if (tile.hasMine)
					continue;

				long mines = getNeighbors(tile).stream().filter(t -> t.hasMine).count(); //get Neighbors

				if (mines > 0)
					tile.text.setText(String.valueOf(mines)); //display the value
			}
		}

		

		return root;
	}
	
	public void restartGame() {
		scene.setRoot(fillBoard());
		return;
	}

	public boolean isValidCoord(int x, int y) {
		if (x >= 0 && x <= squaresInX - 1 && y >= 0 && y <= squaresInY - 1) //checks to see if a given square lies between our board
			return true;
		else
			return false;
	}

	public List<Tile> getNeighbors(Tile tile) {
		List<Tile> neighbors = new ArrayList<>();

		int[] points = new int[] {-1, -1, -1, 0, -1, 1, 0, -1, 0, 1, 1, -1, 1, 0, 1, 1};

		for (int i = 0; i < points.length; i++) {
			int dx = points[i];
			int dy = points[++i]; // Almas Baimagambetov's minesweeper video on youtube, this is his neat solution

			int newX = tile.x + dx;
			int newY = tile.y + dy;

			if (isValidCoord(newX, newY)) {
				neighbors.add(board[newX][newY]); //if the coord is valid, add it to its neighbors.
			}
		}

		return neighbors;
	}

	@Override
	public void start(Stage stage) throws Exception { // check if a game is still running, start a new scene
		scene = new Scene(fillBoard());

		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args); //lights camera action
	}
}