package mertgerdan.tictac.toe;

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

public class Game extends Application {
	Scene scene;
	int squareSize = 200;
	int width = 600;
	int height = 600;
	int squaresInX = width / squareSize;
	int squaresInY = height / squareSize;
	Tile[][] board = new Tile[squaresInX][squaresInY];
	boolean isX = true; // starts the game with X placing
	boolean isOver = false;

	int xWins = 0;
	int oWins = 0;
	int gamesPlayed = 0;
	
	Text winText = new Text();
	
	public class Tile extends StackPane {
		int x, y;
		boolean isOpen = false;
		private Rectangle rect = new Rectangle(squareSize - 5, squareSize - 5);
		private Text text = new Text();

		public Tile(int x, int y) {
			this.x = x;
			this.y = y;
			text.setFont(Font.font(48));
			text.setFill(Color.WHITE);
			text.setStroke(Color.BLACK);
			text.setStrokeWidth(2.5);
			text.setVisible(false);
			rect.setFill(Color.GREEN);
			rect.setStroke(Color.BLACK);
			rect.setStrokeWidth(2.5);

			setTranslateX(x * squareSize); // places the mine on the scene
			setTranslateY(y * squareSize);
			getChildren().addAll(rect, text); // add it to the stack pane
			setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					MouseButton button = event.getButton();
					if (button == MouseButton.PRIMARY) {
						setMark(); // first, open the square
						checkWin(); // then, check win
						checkTie();
						printStats();
						isX = !isX;
					}
				}
			});
		}

		private void setMark() {
			if (isOpen || isOver) {
				return;
			}
			if (isX) {
				text.setText("X");
				text.setVisible(true);
				isOpen = true;
			} else {
				text.setText("O");
				text.setVisible(true);
				isOpen = true;
			}
		}

		public void printStats() {
			if (isOver) {
				System.out.print("Out of " + gamesPlayed + " games played, X has " + xWins + " wins while O has "
						+ oWins + " wins.");
			} else {
				return;
			}
		}

		public void checkTie() {

			if (isOver) {
				return;
			}

			int isClosedCounter = 0;
			for (int x = 0; x < 3; x++) {
				for (int y = 0; y < 3; y++) {
					if (board[x][y].text.getText() == "") {
						isClosedCounter++;
					}
				}
			}

			if (isClosedCounter == 0) {
				System.out.println("It's a tie!");
				isOver = true;
				gamesPlayed++;
			}

		}

		public void checkWin() {
			if (isOver) {
				return;
			}
			// o o o
			// o o o
			// o o o

			// 00 10 20
			// 01 11 21
			// 02 12 22

			// 00 01 02
			// 10 11 12
			// 20 21 22

			// 00 11 22
			// 20 11 02

			int openedX = x;
			int openedY = y;

			if (isX) {

				if (openedX == 0) {
					if (openedY == 0) {
						if (board[1][0].text.getText() == "X" && board[2][0].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}
						if (board[0][1].text.getText() == "X" && board[0][2].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}
						if (board[1][1].text.getText() == "X" && board[2][2].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}
					}
					if (openedY == 1) {
						if (board[0][0].text.getText() == "X" && board[0][2].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}
						if (board[1][1].text.getText() == "X" && board[2][1].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}
					}
					if (openedY == 2) {
						if (board[1][2].text.getText() == "X" && board[2][2].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}
						if (board[0][0].text.getText() == "X" && board[0][1].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}
						if (board[1][1].text.getText() == "X" && board[2][0].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}

					}
				}
				if (openedX == 1) {
					if (openedY == 0) {
						if (board[0][0].text.getText() == "X" && board[2][0].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}
						if (board[1][1].text.getText() == "X" && board[1][2].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}
					}
					if (openedY == 1) {
						if (board[0][1].text.getText() == "X" && board[2][1].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}
						if (board[0][0].text.getText() == "X" && board[2][2].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}
						if (board[0][2].text.getText() == "X" && board[2][0].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}
						if (board[1][0].text.getText() == "X" && board[1][2].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}

					}
					if (openedY == 2) {
						if (board[0][2].text.getText() == "X" && board[2][2].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}
						if (board[1][0].text.getText() == "X" && board[1][1].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}
					}
				}
				if (openedX == 2) {
					if (openedY == 0) {
						if (board[0][0].text.getText() == "X" && board[1][0].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}
						if (board[2][1].text.getText() == "X" && board[2][2].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}
						if (board[1][1].text.getText() == "X" && board[0][2].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}

					}
					if (openedY == 1) {
						if (board[2][0].text.getText() == "X" && board[2][2].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}
						if (board[0][1].text.getText() == "X" && board[1][1].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}

					}
					if (openedY == 2) {
						if (board[2][0].text.getText() == "X" && board[2][1].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}
						if (board[0][2].text.getText() == "X" && board[1][2].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}
						if (board[0][0].text.getText() == "X" && board[1][1].text.getText() == "X") {
							System.out.println("X wins!");
							isOver = true;
							xWins++;
							gamesPlayed++;
						}

					}
				}
			} else if (!isX) {
				if (openedX == 0) {
					if (openedY == 0) {
						if (board[1][0].text.getText() == "O" && board[2][0].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}
						if (board[0][1].text.getText() == "O" && board[0][2].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}
						if (board[1][1].text.getText() == "O" && board[2][2].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}
					}
					if (openedY == 1) {
						if (board[0][0].text.getText() == "O" && board[0][2].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}
						if (board[1][1].text.getText() == "O" && board[2][1].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}
					}
					if (openedY == 2) {
						if (board[1][2].text.getText() == "O" && board[2][2].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}
						if (board[0][0].text.getText() == "O" && board[0][1].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}
						if (board[1][1].text.getText() == "O" && board[2][0].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}

					}
				}
				if (openedX == 1) {
					if (openedY == 0) {
						if (board[0][0].text.getText() == "O" && board[2][0].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}
						if (board[1][1].text.getText() == "O" && board[1][2].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}
					}
					if (openedY == 1) {
						if (board[0][1].text.getText() == "O" && board[2][1].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}
						if (board[0][0].text.getText() == "O" && board[2][2].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}
						if (board[0][2].text.getText() == "O" && board[2][0].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}
						if (board[1][0].text.getText() == "O" && board[1][2].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}

					}
					if (openedY == 2) {
						if (board[0][2].text.getText() == "O" && board[2][2].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}
						if (board[1][0].text.getText() == "O" && board[1][1].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}
					}
				}
				if (openedX == 2) {
					if (openedY == 0) {
						if (board[0][0].text.getText() == "O" && board[1][0].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}
						if (board[2][1].text.getText() == "O" && board[2][2].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}
						if (board[1][1].text.getText() == "O" && board[0][2].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}

					}
					if (openedY == 1) {
						if (board[2][0].text.getText() == "O" && board[2][2].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}
						if (board[0][1].text.getText() == "O" && board[1][1].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}

					}
					if (openedY == 2) {
						if (board[2][0].text.getText() == "O" && board[2][1].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}
						if (board[0][2].text.getText() == "O" && board[1][2].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}
						if (board[0][0].text.getText() == "O" && board[1][1].text.getText() == "O") {
							System.out.println("O wins!");
							isOver = true;
							oWins++;
							gamesPlayed++;
						}

					}
				}

			}

		}

	}

	Parent fillBoard() {
		isOver = false;
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				Tile tile = new Tile(x, y);
				board[x][y] = tile;
				board[x][y].text.setText(null);
				isX = true;
			}
		}
		
		Pane root = new Pane();
		root.setPrefSize(width, height+100);
		
		Rectangle win = new Rectangle(580,80); //the blue rectangle at the bottom that restarts the game
		win.setTranslateX(10);
		win.setTranslateY(height+10);
		win.setFill(Color.AQUAMARINE);
		win.setStroke(Color.BLACK);
		win.setStrokeWidth(2.5);
		winText.setFont(Font.font(28));
		winText.setText("Restart");
		winText.setVisible(true);
		winText.setTranslateX(width/2.0-50);
		winText.setTranslateY(height+60);
		win.	setOnMouseClicked(e -> restartGame());
		
		root.getChildren().addAll(win, winText);
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				Tile tile = new Tile(x, y);
				board[x][y] = tile;
				root.getChildren().add(tile);
			}
		}
		return root;

	}
	
	public void restartGame() {
		scene.setRoot(fillBoard());
		return;
	}

	@Override
	public void start(Stage stage) throws Exception {
		scene = new Scene(fillBoard());

		stage.setScene(scene);
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
