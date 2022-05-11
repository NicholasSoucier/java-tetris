package jinji.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class TetrisGame extends ApplicationAdapter implements ApplicationListener, InputProcessor {

	// List of Colors that corresponds to brick types
	private Color brickColors[] = { new Color(0f, 1f, 1f, 1f), new Color(1f, 1f, 0f, 1f), new Color(1f, 0f, 1f, 1f),
			new Color(0f, 1f, 0f, 1f), new Color(1f, 0f, 0f, 1f), new Color(0f, 0f, 1f, 1f),
			new Color(1f, 0.5f, 0f, 1f) };

	// Random object
	Random rand = new Random();

	// Render control objects
	SpriteBatch batch;
	private BitmapFont font;
	private Texture grid;

	// Game control objects
	private Boolean playing;
	private ArrayList<Brick> bricks;
	private Boolean gridVal[][] = new Boolean[10][20];
	private int dropTimer;
	private int maxDropTimer;
	private boolean isHolding;
	private boolean hasSwappedHold;
	private Shape pieceInPlay;
	private Shape holdShape;
	private Shape nextShape;
	private ArrayList<Brick> playBricks;
	private ArrayList<Brick> holdBricks;
	private ArrayList<Brick> nextBricks;
	private int playX;
	private int playY;
	private int playRot;
	private int linesCleared;

	// Score
	private long score;

	// On game initialization, perform these tasks
	@Override
	public void create() {
		batch = new SpriteBatch();
		grid = new Texture(Gdx.files.internal("grid.png"));
		bricks = new ArrayList<Brick>();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 20; j++) {
				gridVal[i][j] = false;
			}
		}
		playing = true;
		score = 0;
		pieceInPlay = new Shape(0);
		isHolding = false;
		hasSwappedHold = false;
		holdShape = new Shape(0);
		holdBricks = new ArrayList<Brick>();
		for (int i = 0; i < 4; i++) {
			holdBricks.add(new Brick(0, 0));
		}
		nextShape = new Shape(0);
		nextBricks = new ArrayList<Brick>();
		for (int i = 0; i < 4; i++) {
			nextBricks.add(new Brick(0, 0));
		}
		dropTimer = 50;
		maxDropTimer = 50;
		playX = 3;
		playY = 20;
		playBricks = new ArrayList<Brick>();
		font = new BitmapFont();
		for (int i = 0; i < 4; i++) {
			playBricks.add(new Brick(playX, playY));
		}

		Gdx.input.setInputProcessor(this);

		startPiece();
		adjustBrickLoc();

	}

	// Render each game frame
	@Override
	public void render() {
		tick();
		ScreenUtils.clear(0.21f, 0.21f, 0.21f, 1f);
		batch.begin();
		// Draw Grid
		batch.draw(grid, (Gdx.graphics.getWidth() / 2 - 200), (Gdx.graphics.getHeight() / 2 - 400));

		if (playing) {
			// Draw piece in play
			batch.setColor(brickColors[pieceInPlay.getShape()]);
			for (int i = 0; i < playBricks.size(); i++) {
				int drawX = playBricks.get(i).getXPos() * 40 + (Gdx.graphics.getWidth() / 2 - 200);
				int drawY = playBricks.get(i).getYPos() * 40 + (Gdx.graphics.getHeight() / 2 - 400);
				// playBricks.get(i).draw(batch);
				if (inGrid(playBricks.get(i).getXPos(), playBricks.get(i).getYPos())) {
					batch.draw(playBricks.get(i).getTexture(), drawX, drawY);
				}
			}

			// Draw hold piece
			batch.setColor(brickColors[holdShape.getShape()]);
			for (int i = 0; i < holdBricks.size(); i++) {
				int drawX = (int) (holdBricks.get(i).getX());
				int drawY = (int) (holdBricks.get(i).getY());
				if (isHolding) {
					batch.draw(holdBricks.get(i).getTexture(), drawX, drawY);
				}
			}

			// Draw next piece
			batch.setColor(brickColors[nextShape.getShape()]);
			for (int i = 0; i < nextBricks.size(); i++) {
				int drawX = (int) (nextBricks.get(i).getX());
				int drawY = (int) (nextBricks.get(i).getY());
				batch.draw(nextBricks.get(i).getTexture(), drawX, drawY);
			}

			// Draw bricks in grid
			batch.setColor(Color.WHITE);
			for (int i = 0; i < bricks.size(); i++) {
				batch.setColor(bricks.get(i).getColor());
				bricks.get(i).draw(batch);
			}
		}

		// Draw on-screen text
		batch.setColor(Color.WHITE);
		font.draw(batch, "HOLD", (Gdx.graphics.getWidth() / 2 - 300), (Gdx.graphics.getHeight() / 2 + 300));
		font.draw(batch, "NEXT", (Gdx.graphics.getWidth() / 2 + 300), (Gdx.graphics.getHeight() / 2 + 300));
		font.draw(batch, "Score: " + score, (Gdx.graphics.getWidth() / 2 - 200), (Gdx.graphics.getHeight() - 25));

		if (!playing) {
			font.draw(batch, "GAME OVER", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		}

		batch.end();
	}

	// Dispose of render 50objects
	@Override
	public void dispose() {
		batch.dispose();
		grid.dispose();
	}

	// Test to see if a coordinate position is bounded in the grid
	private Boolean inGrid(int x, int y) {
		if (x <= 9 && x >= 0) {
			if (y <= 19 && y >= 0) {
				return true;
			}
		}
		return false;
	}

	// Game tick
	public void tick() {
		if (playing) {
			if (dropTimer > 0) {
				dropTimer--;
			}
			if (dropTimer <= 0) {
				dropPiece(1);
				dropTimer = maxDropTimer;
			}
		}
	}

	// Set a new piece in play
	private void startPiece() {
		// printOutGrid();
		int randPiece = rand.nextInt(7);
		playX = 3;
		playRot = 0;
		pieceInPlay.setRotation(0);
		pieceInPlay.setShape(randPiece);
		playY = 19;
		randPiece = rand.nextInt(7);
		nextShape.setShape(randPiece);
		adjustBrickLoc();
	}

	// Set a new piece in50 play, but define the shape
	private void startPiece(int shape) {
		playX = 3;
		playRot = 0;
		pieceInPlay.setRotation(0);
		pieceInPlay.setShape(shape);
		playY = 19;
		int randPiece = rand.nextInt(7);
		nextShape.setShape(randPiece);
		adjustBrickLoc();
	}

	// Translate inPlayPiece location to Brick Sprite location.
	private void adjustBrickLoc() {
		int bricksIter = 0;
		int holdBricksIter = 0;
		int nextBricksIter = 0;
		for (int shapeY = 0; shapeY < 4; shapeY++) {
			for (int shapeX = 0; shapeX < 4; shapeX++) {
				if (playY + shapeY > 19) {
					return;
				}
				if (pieceInPlay.getShapeMap()[(shapeY * 4) + shapeX] == true) {
					playBricks.get(bricksIter).setGridPosition((playX + shapeX), (playY + shapeY));
					playBricks.get(bricksIter)
							.setX((playBricks.get(bricksIter).getXPos() * 40) + (Gdx.graphics.getWidth() / 2 - 200));
					playBricks.get(bricksIter)
							.setY((playBricks.get(bricksIter).getYPos() * 40) + (Gdx.graphics.getHeight() / 2 - 400));
					bricksIter++;
				}
				if (holdShape.getShapeMap()[(shapeY * 4) + shapeX] == true) {
					holdBricks.get(holdBricksIter).setXYPos((shapeX * 40 + (Gdx.graphics.getWidth() / 2 - 360)),
							(shapeY * 40 + (Gdx.graphics.getHeight() / 2 + 150)));
					holdBricksIter++;
				}
				if (nextShape.getShapeMap()[(shapeY * 4) + shapeX] == true) {
					nextBricks.get(nextBricksIter).setXYPos((shapeX * 40 + (Gdx.graphics.getWidth() / 2 + 240)),
							(shapeY * 40 + (Gdx.graphics.getHeight() / 2 + 150)));
					nextBricksIter++;
				}
			}
		}
	}

	// Move a piece down a row
	// Place a piece in the grid if
	private void dropPiece(int amt) {
		for (int i = 0; i < amt; i++) {
			if (checkBelow()) {
				placePiece();
			} else {
				playY--;
				adjustBrickLoc();
			}
		}
	}

	// Check to see if piece is touching another brick below or at the bottom of
	// grid
	// Return TRUE if touching, FALSE otherwise
	private Boolean checkBelow() {
		for (int shapeY = 0; shapeY < 4; shapeY++) {
			for (int shapeX = 0; shapeX < 4; shapeX++) {
				if (playY + shapeY > 19) {
					return false;
				}
				if (pieceInPlay.getShapeMap()[(shapeY * 4) + shapeX] == true) {
					if (playY + shapeY == 0) {
						return true;
					}
					if (gridVal[playX + shapeX][(playY + shapeY) - 1]) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// Check for full rows and queue to destroy them if full
	private void checkRows() {
		int rowsCleared = 0;
		for (int i = 0; i < 20; i++) {
			Boolean isFull = true;
			for (int j = 0; j < 10; j++) {
				if (gridVal[j][i] == false) {
					isFull = false;
				}
			}
			if (isFull) {
				removeRow(i);
				rowsCleared++;
				i--;
			}
		}
		switch (rowsCleared) {
		case 1:
			score += 100;
			break;
		case 2:
			score += 500;
			break;
		case 3:
			score += 1500;
			break;
		case 4:
			score += 3500;
			break;
		}
		linesCleared += rowsCleared;
	}

	// Remove row and push down all rows above
	private void removeRow(int rowNum) {
		for (int i = 0; i < 10; i++) {
			gridVal[i][rowNum] = false;
			for (int b = 0; b < bricks.size(); b++) {
				if (bricks.get(b).getXPos() == i && bricks.get(b).getYPos() == rowNum) {
					bricks.remove(b);
					b--;
				}
			}
		}
		int rowiter = rowNum;
		while (rowiter < 19) {
			for (int i = 0; i < 10; i++) {
				gridVal[i][rowiter] = gridVal[i][rowiter + 1];
				for (Brick brick : bricks) {
					if (brick.getXPos() == i && brick.getYPos() == rowiter + 1) {
						brick.setGridPosition(i, rowiter);
					}
				}
			}
			rowiter++;
		}
	}

	// Place a piece on the board if checkBelow() fails
	private void placePiece() {
		if (playY >= 19) {
			playing = false;
			for (int i = 0; i < gridVal.length; i++) {
				for (int j = 0; j < gridVal[0].length; j++) {
					gridVal[i][j] = false;
				}
			}
			for (int i = 0; i < bricks.size(); i++) {
				bricks.remove(0);
			}
		} else {
			for (int shapeY = 0; shapeY < 4; shapeY++) {
				for (int shapeX = 0; shapeX < 4; shapeX++) {
					if (pieceInPlay.getShapeMap()[(shapeY * 4) + shapeX] == true) {
						gridVal[playX + shapeX][playY + shapeY] = true;
						Brick newBrick = new Brick(playX + shapeX, playY + shapeY, brickColors[pieceInPlay.getShape()]);
						newBrick.setX((newBrick.getXPos() * 40) + (Gdx.graphics.getWidth() / 2 - 200));
						newBrick.setY((newBrick.getYPos() * 40) + (Gdx.graphics.getHeight() / 2 - 400));
						bricks.add(newBrick);
					}
				}
			}
		}
		for (Brick brick : playBricks) {
			brick.setGridPosition(-1, -1);
		}
		hasSwappedHold = false;
		score += 10;
		checkRows();
		startPiece(nextShape.getShape());
		adjustBrickLoc();
		maxDropTimer = 70 - (int) (linesCleared / 2);
		System.out.println(maxDropTimer);
	}

	// Rotate the piece in play
	int rotationAttempts = 0;

	private void rotatePiece() {
		Boolean collision = false;
		Boolean[] compareShape = pieceInPlay.getShapeMap(pieceInPlay.getShape(), pieceInPlay.getNextRotCW());
		for (int shapeY = 0; shapeY < 4; shapeY++) {
			for (int shapeX = 0; shapeX < 4; shapeX++) {
				if (compareShape[(shapeY * 4) + shapeX] == true) {
					if (playX + shapeX < 0) {
						return;
					} else if (playX + shapeX > 9) {
						return;
					} else if (playY + shapeY > 19) {
						return;
					} else if (playY + shapeY < 0) {
						return;
					} else if (gridVal[playX + shapeX][playY + shapeY] == true) {
						rotationAttempts++;
						if (rotationAttempts <= 8) {
							playRot = pieceInPlay.getNextRotCW();
							rotatePiece();
							collision = true;
						}
					}
				}
			}
		}
		if (!collision) {
			playRot = pieceInPlay.getNextRotCW();
			pieceInPlay.setRotation(playRot);
			adjustBrickLoc();
		}
		rotationAttempts = 0;
	}

	// Check to see if moving a piece left-right would violate the boundaries
	private void checkBoundary(Boolean isRightWall) {
		Boolean canMove = true;
		for (int shapeY = 0; shapeY < 4; shapeY++) {
			for (int shapeX = 0; shapeX < 4; shapeX++) {
				if (pieceInPlay.getShapeMap()[(shapeY * 4) + shapeX] == true) {
					if (playY + shapeY > 19) {
						canMove = false;
					} else if (isRightWall) {
						if (playX + shapeX + 1 > 9) {
							canMove = false;
						} else if (gridVal[playX + shapeX + 1][playY + shapeY] == true) {
							canMove = false;
						}
					} else {
						if (playX + shapeX - 1 < 0) {
							canMove = false;
						} else if (gridVal[playX + shapeX - 1][playY + shapeY] == true) {
							canMove = false;
						}
					}
				}
			}
		}
		if (isRightWall && canMove) {
			playX++;
			adjustBrickLoc();
		} else if (!isRightWall && canMove) {
			playX--;
			adjustBrickLoc();
		}
	}

	// Function to hold a piece
	private void holdPiece() {
		if (isHolding && !hasSwappedHold) {
			int tempHold = holdShape.getShape();
			holdShape.setShape(pieceInPlay.getShape());
			hasSwappedHold = true;
			startPiece(tempHold);
			dropTimer = 1;
		} else if (!hasSwappedHold) {
			isHolding = true;
			hasSwappedHold = true;
			holdShape.setShape(pieceInPlay.getShape());
			startPiece(nextShape.getShape());
			dropTimer = 0;
		}
		adjustBrickLoc();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.W) {
			rotatePiece();
		}
		if (keycode == Keys.S) {
			maxDropTimer = 3;
			dropTimer = 3;
		}
		if (keycode == Keys.A) {
			checkBoundary(false);
		}
		if (keycode == Keys.D) {
			checkBoundary(true);
		}
		if (keycode == Keys.E) {
			holdPiece();
		}
		if (keycode == Keys.SPACE) {
			dropTimer = 0;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.S) {
			maxDropTimer = 70 - (int) (linesCleared / 2);
			dropTimer = 70 - (int) (linesCleared / 2);
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}

	private void printOutGrid() {
		for (int i = 19; i >= 0; i--) {
			for (int j = 0; j < 10; j++) {
				if (gridVal[j][i] == true) {
					System.out.print("#");
				} else {
					System.out.print("-");
				}
			}
			System.out.print("\n");
		}
	}

}
