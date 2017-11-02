import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Tank {
	private int x, y;
	private int a, b;// 用于在move方法中存储移动之前的位置
	public static final int TANKSIZEX = 30;
	public static final int TANKSIZEY = 30;
	private int tankSpeedX = 5, TankSpeedY = 5;

	public enum Direction {
		U, D, L, R, LU, LD, RU, RD, STOP;
	}

	private int life = 100;
	Direction dir = Direction.D;
	private Direction barrelDir = Direction.D;
	private boolean isEnemy = true;
	private BloodBar bloodBar = null;
	private TankClient tankClient = null;
	private Random random = new Random();
	private int step = random.nextInt(12) + 3;
	private boolean BL = false, BR = false, BU = false, BD = false;
	private boolean shouldStop = true;

	public Tank(int x, int y, boolean isEnemy, TankClient tankClient) {
		this.x = x;
		this.y = y;
		this.isEnemy = isEnemy;
		this.tankClient = tankClient;
		if (!isEnemy) {
			this.dir = Direction.STOP;
			bloodBar = new BloodBar();
		}
	}

	public void drawTank(Graphics g) {
		Color color = g.getColor();// 保存画笔的初始颜色
		if (this.isEnemy) {
			g.setColor(Color.RED);
			g.fillOval(x, y, TANKSIZEX, TANKSIZEY);
			g.setColor(color);
		} else {
			g.setColor(Color.PINK);
			g.fillOval(x, y, TANKSIZEX, TANKSIZEY);
			g.setColor(color);
		}
		switch (barrelDir) {
		case U:
			g.setColor(Color.WHITE);
			g.drawLine(x + TANKSIZEX / 2, y + TANKSIZEY / 2, x + TANKSIZEX / 2, y);
			g.setColor(color);
			break;
		case D:
			g.setColor(Color.WHITE);
			g.drawLine(x + TANKSIZEX / 2, y + TANKSIZEY / 2, x + TANKSIZEX / 2, y + TANKSIZEY);
			g.setColor(color);
			break;
		case L:
			g.setColor(Color.WHITE);
			g.drawLine(x + TANKSIZEX / 2, y + TANKSIZEY / 2, x, y + TANKSIZEY / 2);
			g.setColor(color);
			break;
		case R:
			g.setColor(Color.WHITE);
			g.drawLine(x + TANKSIZEX / 2, y + TANKSIZEY / 2, x + TANKSIZEX, y + TANKSIZEY / 2);
			g.setColor(color);
			break;
		case LU:
			g.setColor(Color.WHITE);
			g.drawLine(x + TANKSIZEX / 2, y + TANKSIZEY / 2, x, y);
			g.setColor(color);
			break;
		case LD:
			g.setColor(Color.WHITE);
			g.drawLine(x + TANKSIZEX / 2, y + TANKSIZEY / 2, x, y + TANKSIZEY / 2);
			g.setColor(color);
			break;
		case RU:
			g.setColor(Color.WHITE);
			g.drawLine(x + TANKSIZEX / 2, y + TANKSIZEY / 2, x + TANKSIZEX / 2, y);
			g.setColor(color);
			break;
		case RD:
			g.setColor(Color.WHITE);
			g.drawLine(x + TANKSIZEX / 2, y + TANKSIZEY / 2, x + TANKSIZEX, y + TANKSIZEY);
			g.setColor(color);
			break;
		case STOP:
			break;
		default:
			return;
		}
		if (!isEnemy) {
			bloodBar.drawBloodBar(g);
		}
		if (isEnemy) {
			move();
		}
	}

	public void move() {
		a = x;
		b = y;
		switch (dir) {
		case U:
			y -= TankSpeedY;
			break;
		case D:
			y += TankSpeedY;
			break;
		case L:
			x -= tankSpeedX;
			break;
		case R:
			x += tankSpeedX;
			break;
		case LU:
			x -= tankSpeedX;
			y -= TankSpeedY;
			break;
		case LD:
			x -= tankSpeedX;
			y += TankSpeedY;
			break;
		case RU:
			x += tankSpeedX;
			y -= TankSpeedY;
			break;
		case RD:
			x += tankSpeedX;
			y += TankSpeedY;
			break;
		case STOP:
			return;
		default:
			return;
		}
		if (hitWall() || hitTank() || outBounds()) {
			x = a;
			y = b;
		}
		if (hitFood() && (!isEnemy)) {
			life = 100;
			tankClient.food.eatten();
		}
		int i = hitBuliet();
		if (i == 1) {
			life -= 20;
			if (life == 0) {
				tankClient.myTank = null;
			}
			tankClient.explosions.add(new Explosion(x, y, this.tankClient));
		}
		if (i == 2) {
			tankClient.killEnemyCount++;
			life = 0;
			tankClient.explosions.add(new Explosion(x, y, this.tankClient));
			tankClient.tanks.remove(this);
		}
		if (isEnemy) {
			step--;
			if (random.nextInt(30) > 27) {
				fire();
			}
			if (step == 0) {
				step = random.nextInt(12) + 3;
				Direction[] directions = Direction.values();
				int length = directions.length;
				do {
					dir = directions[random.nextInt(length)];
				} while (dir == Direction.STOP);
			}
			if (barrelDir != dir) {
				barrelDir = dir;
			}
		}
	}

	public Rectangle getTankRectangle() {
		return new Rectangle(x, y, TANKSIZEX, TANKSIZEY);
	}

	public void fire() {
		tankClient.buliets.add(new Buliet(x + TANKSIZEX / 2 - Buliet.BULIETSIZEX / 2,
				y + TANKSIZEY / 2 - Buliet.BULIETSIZEY / 2, barrelDir, isEnemy, this, tankClient));
	}

	public void superfire() {
		tankClient.buliets.add(new Buliet(x + TANKSIZEX / 2 - Buliet.BULIETSIZEX / 2,
				y + TANKSIZEY / 2 - Buliet.BULIETSIZEY / 2, Direction.LU, isEnemy, this, tankClient));
		tankClient.buliets.add(new Buliet(x + TANKSIZEX / 2 - Buliet.BULIETSIZEX / 2,
				y + TANKSIZEY / 2 - Buliet.BULIETSIZEY / 2, Direction.LD, isEnemy, this, tankClient));
		tankClient.buliets.add(new Buliet(x + TANKSIZEX / 2 - Buliet.BULIETSIZEX / 2,
				y + TANKSIZEY / 2 - Buliet.BULIETSIZEY / 2, Direction.RU, isEnemy, this, tankClient));
		tankClient.buliets.add(new Buliet(x + TANKSIZEX / 2 - Buliet.BULIETSIZEX / 2,
				y + TANKSIZEY / 2 - Buliet.BULIETSIZEY / 2, Direction.RD, isEnemy, this, tankClient));
	}

	public boolean hitWall() {
		for (int i = 0; i < tankClient.walls.size(); i++) {
			if (this.getTankRectangle().intersects(tankClient.walls.get(i).getWallRectangle())) {
				return true;
			}
		}
		return false;
	}

	public boolean hitFood() {
		if (this.getTankRectangle().intersects(tankClient.food.getFoodRectangle())) {
			return true;
		} else {
			return false;
		}
	}

	public boolean hitTank() {
		if ((isEnemy) && tankClient.myTank != null
				&& this.getTankRectangle().intersects(tankClient.myTank.getTankRectangle())) {
			return true;
		}
		/* 我方或敌方碰到敌方时 */
		for (int i = 0; i < tankClient.tanks.size(); i++) {
			if (this.getTankRectangle().intersects(tankClient.tanks.get(i).getTankRectangle())
					&& this != tankClient.tanks.get(i)) {
				return true;
			}
		}
		return false;
	}

	public boolean outBounds() {
		if (x < 0 || x > TankClient.GAMEWIDTH || y < 0 || y > TankClient.GAMEHEIGHT) {
			return true;
		} else {
			return false;
		}
	}

	public int hitBuliet() {
		/* 我方碰到敌方的子弹 */
		if (!isEnemy) {
			for (int i = 0; i < tankClient.buliets.size(); i++) {
				if (this.getTankRectangle().intersects(tankClient.buliets.get(i).getBulietRectangle())
						&& tankClient.buliets.get(i).getIsenemy()) {
					tankClient.buliets.remove(i);
					return 1;
				}
				if (this.getTankRectangle().intersects(tankClient.buliets.get(i).getBulietRectangle())
						&& !tankClient.buliets.get(i).getIsenemy() && tankClient.buliets.get(i).getTank() != this) {
					tankClient.buliets.remove(i);
					return 3;
				}
			}
		}
		if (isEnemy) {
			for (int i = 0; i < tankClient.buliets.size(); i++) {
				if (this.getTankRectangle().intersects(tankClient.buliets.get(i).getBulietRectangle())
						&& !tankClient.buliets.get(i).getIsenemy()) {
					tankClient.buliets.remove(i);
					return 2;
				}
				if (this.getTankRectangle().intersects(tankClient.buliets.get(i).getBulietRectangle())
						&& tankClient.buliets.get(i).getTank() != this && tankClient.buliets.get(i).getIsenemy()) {
					tankClient.buliets.remove(i);
					return 3;
				}
			}
		}
		return 4;
	}

	public void locateDirection() {
		if (BL && !BR && !BU && !BD) {
			dir = Direction.L;
		}
		if (!BL && BR && !BU && !BD) {
			dir = Direction.R;
		}
		if (!BL && !BR && BU && !BD) {
			dir = Direction.U;
		}
		if (!BL && !BR && !BU && BD) {
			dir = Direction.D;
		}
		if (BL && !BR && BU && !BD) {
			dir = Direction.LU;
		}
		if (BL && !BR && !BU && BD) {
			dir = Direction.LD;
		}
		if (!BL && BR && BU && !BD) {
			dir = Direction.RU;
		}
		if (!BL && BR && !BU && BD) {
			dir = Direction.RD;
		}
		if (barrelDir != dir) {
			barrelDir = dir;
		}
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_Q:
			fire();
			return;
		case KeyEvent.VK_W:
			superfire();
			return;
		case KeyEvent.VK_UP:
			BU = true;
			break;
		case KeyEvent.VK_DOWN:
			BD = true;
			break;
		case KeyEvent.VK_LEFT:
			BL = true;
			break;
		case KeyEvent.VK_RIGHT:
			BR = true;
			break;
		case KeyEvent.VK_ENTER:
			tankClient.initalize();
			return;
		default:
			break;
		}
		locateDirection();
		if (BU || BD || BL || BR) {
			shouldStop = false;
		}
	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			BU = false;
			break;
		case KeyEvent.VK_DOWN:
			BD = false;
			break;
		case KeyEvent.VK_LEFT:
			BL = false;
			break;
		case KeyEvent.VK_RIGHT:
			BR = false;
			break;
		default:
			break;
		}
		locateDirection();
		if (!BU && !BD && !BL && !BR) {
			shouldStop = true;
		}
	}

	public int getLife() {
		return this.life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public void setSpeed(int speedX, int speedY) {
		this.tankSpeedX = speedX;
		this.TankSpeedY = speedY;
	}

	public boolean getShouldStop() {
		return this.shouldStop;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	private class BloodBar {
		void drawBloodBar(Graphics g) {
			Color color = g.getColor();
			g.setColor(Color.GREEN);
			int width = 30 * life / 100;
			g.drawRect(x, y - 10, 30, 10);
			g.fillRect(x, y - 10, width, 10);
			g.setColor(color);
		}
	}
}
