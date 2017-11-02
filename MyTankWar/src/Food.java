import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Food {
	public static final int FOODSIZEX = 15;
	public static final int FOODSIZEY = 15;
	private int time = 100;
	private int position[][] = { { 50, 50 }, { 50, 520 }, { 250, 250 }, { 600, 50 }, { 700, 400 } };
	private int nowPosition = 0;
	private boolean isLive = true;

	public Food() {

	}

	public void setIsLive(boolean isLive) {
		this.isLive = isLive;
	}

	public boolean getIsLive() {
		return isLive;
	}

	public void drawFood(Graphics g) {
		time--;
		if (time == 0) {
			time = 100;
			nowPosition++;
			if (nowPosition > 4) {
				nowPosition = 0;
			}
		}
		Color color = g.getColor();
		g.setColor(color.YELLOW);
		g.fillRect(position[nowPosition][0], position[nowPosition][1], FOODSIZEX, FOODSIZEY);
		g.setColor(color);
	}

	public void eatten() {
		time = 100;
		nowPosition++;
		if (nowPosition > 4) {
			nowPosition = 0;
		}
	}

	public Rectangle getFoodRectangle() {
		return new Rectangle(position[nowPosition][0], position[nowPosition][1], FOODSIZEX, FOODSIZEY);
	}

}
