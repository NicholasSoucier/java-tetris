package jinji.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Brick extends Sprite{
	private Texture blockTexture;
	private Color tintColor;
	private int xPos;
	private int yPos;
	
	public Brick(int x, int y) {
		super(new Texture(Gdx.files.internal("block.png")));
		xPos = x;
		yPos = y;
		blockTexture = new Texture(Gdx.files.internal("block.png"));
		this.setTexture(blockTexture);
		
		Color defaultColor = new Color(1, 0, 0, 1);
		this.setColor(defaultColor);
		tintColor = defaultColor;
	}
	
	public Brick(int x, int y, Color tint) {
		super(new Texture(Gdx.files.internal("block.png")));
		xPos = x;
		yPos = y;
		this.setColor(tint);
		tintColor = tint;
		blockTexture = new Texture(Gdx.files.internal("block.png"));
		this.setTexture(blockTexture);
	}
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public void setXYPos(int x, int y) {
		this.setX(x);
		this.setY(y);
	}
	
	public void setGridPosition(int x, int y) {
		xPos = x;
		yPos = y;
		//Set display position.
		this.setX((xPos*40) + (Gdx.graphics.getWidth() / 2 - 200));
		this.setY((yPos*40) + (Gdx.graphics.getHeight() / 2 - 400));
	}
	
	public void setBlockColor(Color tint) {
		this.setColor(tint);
		tintColor = tint;
	}
	
	public Color getColor() {
		return tintColor;
	}
	
}
