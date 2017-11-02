import java.awt.Color;
import java.awt.Graphics;

public class Explosion {
	private int x, y;
	private int count = 0;
	private int size[] = { 10, 20, 30, 40, 30, 20, 10 };
	private TankClient tankClient;

	public Explosion(int x, int y, TankClient tankClient) {
		this.x = x;
		this.y = y;
		this.tankClient = tankClient;
	}

	public void drawExplosion(Graphics g) {
		if (count == 7) {
			if (tankClient != null) {
				tankClient.explosions.remove(this);
			}
			return;
		}
		Color color = g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(x, y, size[count], size[count]);
		g.setColor(color);
		count++;
	}
}
