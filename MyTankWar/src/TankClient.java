import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TankClient extends Frame {
	public static final int GAMEWIDTH = 800;
	public static final int GAMEHEIGHT = 600;
	List<Tank> tanks = new ArrayList<>();
	List<Buliet> buliets = new ArrayList<>();
	List<Explosion> explosions = new ArrayList<>();
	List<Wall> walls = new ArrayList<>();
	Wall wall1 = new Wall(150, 150, 50, 400);
	Wall wall2 = new Wall(200, 120, 200, 60);
	Wall wall3 = new Wall(500, 300, 50, 200);
	Tank myTank = null;
	Food food = null;
	private Image gameImage = null;
	int killEnemyCount = 0;

	public TankClient() {
		super();
	}

	public void initalize() {
		if (walls.size() == 0) {
			walls.add(wall1);
			walls.add(wall2);
			walls.add(wall3);
		}
		if (tanks.size() != 0) {
			tanks.clear();
		}
		for (int i = 0; i < 6; i++) {
			if ((i & 1) == 0) {
				/* 说明i是偶数 */
				tanks.add(new Tank(80 + i / 2 * 50, 50, true, this));
			} else {
				tanks.add(new Tank(80 + i / 2 * 50, 550, true, this));
			}
		}
		if (buliets.size() != 0) {
			buliets.clear();
		}
		if (explosions.size() != 0) {
			explosions.clear();
		}
		food = new Food();
		myTank = new Tank(700, 400, false, this);
		killEnemyCount = 0;
	}

	public void launchFrame() {
		initalize();
		this.setBounds(0, 0, GAMEWIDTH, GAMEHEIGHT);
		this.setBackground(Color.BLACK);
		this.setVisible(true);
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				myTank.keyPressed(e);
			}

			public void keyReleased(KeyEvent e) {
				myTank.keyReleased(e);
			}
		});
		new Thread(new TankThread()).start();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		Color color = g.getColor();
		g.setColor(Color.WHITE);
		g.drawString("YouHaveKilled:" + String.valueOf(killEnemyCount), 10, 50);
		g.setColor(color);
		for (int i = 0; i < walls.size(); i++) {
			walls.get(i).drawWall(g);
		}
		for (int i = 0; i < tanks.size(); i++) {
			tanks.get(i).drawTank(g);
		}
		if (myTank != null) {
			myTank.drawTank(g);
		}
		food.drawFood(g);
		for (int i = 0; i < buliets.size(); i++) {
			buliets.get(i).drawBuliet(g);
		}
		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).drawExplosion(g);
		}
	}

	@Override
	public void update(Graphics g) {
		if (gameImage == null) {
			gameImage = this.createImage(GAMEWIDTH, GAMEHEIGHT);
		}
		Graphics imageG = gameImage.getGraphics();
		Color color = imageG.getColor();
		imageG.setColor(Color.BLACK);
		imageG.fillRect(0, 0, GAMEWIDTH, GAMEHEIGHT);
		imageG.setColor(color);
		paint(imageG);
		g.drawImage(gameImage, 0, 0, null);
	}

	private class TankThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				try {
					Thread.sleep(30);
					if (myTank != null && !myTank.getShouldStop()) {
						myTank.move();
					}
					repaint();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TankClient tc = new TankClient();
		tc.launchFrame();
	}

}
