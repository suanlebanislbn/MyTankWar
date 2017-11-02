import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Wall {
	private int x, y;
	private int wallSizeX = 0;
	private int wallSizeY = 0;

	public Wall(int x, int y, int wallSizeX, int wallSizeY) {
		this.x = x;
		this.y = y;
		this.wallSizeX = wallSizeX;
		this.wallSizeY = wallSizeY;
	}

	public void drawWall(Graphics g) {
		Color color = g.getColor();
		g.setColor(Color.BLUE);
		g.fillRect(x, y, wallSizeX, wallSizeY);
		g.setColor(color);
	}

	public Rectangle getWallRectangle() {
		return new Rectangle(x, y, wallSizeX, wallSizeY);
	}
}
