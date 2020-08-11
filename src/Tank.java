import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.*;

public class Tank {
	int x;
	int y;
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	public static final int GUN_LENGTH = 10;
	boolean bL = false, bR = false, bU = false, bD = false;
	boolean isGood;
	boolean isAlive=true;
	TankClient tc;
	
	enum Direction {
		left, right, up, down, stop, left_up, left_down, right_up, right_down
	}
	private Random r=new Random();
	private int step=r.nextInt(12)+3;
	Direction dir = Direction.stop;
	Direction gunDir = Direction.left_up;// 添加炮口方向
	List<Missile> m = new ArrayList<>();
	int gunTime=r.nextInt(5)+2;
	public Tank(int x, int y,boolean isGood,TankClient tc) {
		super();
		this.x = x;
		this.y = y;
		this.isGood=isGood;
		this.tc= tc;
	}
	
	public void move() {
		switch (dir) {
		case stop:
			break;
		case left:
			x-=XSPEED;
			break;// 如果没有break,会不再做判断而直接顺序执行
		case up:
			y -= YSPEED;
			break;
		case right:
			x += XSPEED;
			break;
		case down:
			y += YSPEED;
			break;
		case left_up:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case left_down:
			x -= XSPEED;
			y += YSPEED;
			break;// 代码中少了break导致bug，即此情况无法移动
		case right_up:
			x += XSPEED;
			y -= YSPEED;
			break;
		case right_down:
			x += XSPEED;
			y += YSPEED;
			break;
		}
		if(x<0)x=0;//关系到坦克不能开出边界
		if(y<30)y=30;
		if(x+Tank.WIDTH>TankClient.GAME_WIDTH)x=TankClient.GAME_WIDTH-Tank.WIDTH;
		if(y+Tank.HEIGHT>TankClient.GAME_HEIGHT)y=TankClient.GAME_HEIGHT-Tank.HEIGHT;
		if (dir != Direction.stop)
			gunDir = dir;// 炮口方向与车身方向一致，这是比较简单的解决方案
		if(!isGood) {

			if(step==0) {

				Direction[] dirs=Direction.values();
				dir=dirs[r.nextInt(dirs.length)];
				step=r.nextInt(dirs.length)+3;
			}
			step--;
		}
	}

	public void draw(Graphics g) {
		// direction();将此方法放在键盘处理程序后
		// move(dir);此方法在重画后执行，更合乎逻辑，而非在执行后重画。
		Color c = g.getColor();
		if(isGood)g.setColor(Color.pink);
		else g.setColor(Color.BLUE);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(Color.BLUE);
		switch (gunDir) {
		case left:
			g.drawLine(x, y + HEIGHT / 2, x + WIDTH / 2, y + HEIGHT / 2);// 画炮管
			break;// 如果没有break,会不再做判断而直接顺序执行
		case up:
			g.drawLine(x + WIDTH / 2, y, x + WIDTH / 2, y + HEIGHT / 2);// 画炮管
			break;
		case right:
			g.drawLine(x + WIDTH, y + HEIGHT / 2, x + WIDTH / 2, y + HEIGHT / 2);// 画炮管
			break;
		case down:
			g.drawLine(x + WIDTH / 2, y + HEIGHT, x + WIDTH / 2, y + HEIGHT / 2);// 画炮管
			break;
		case left_up:
			g.drawLine(x, y, x + WIDTH / 2, y + HEIGHT / 2);// 画炮管
			break;
		case left_down:
			g.drawLine(x, y + HEIGHT, x + WIDTH / 2, y + HEIGHT / 2);// 画炮管
			break;// 代码中少了break导致bug，即此情况无法移动
		case right_up:
			g.drawLine(x + WIDTH, y, x + WIDTH / 2, y + HEIGHT / 2);// 画炮管
			break;
		case right_down:
			g.drawLine(x + WIDTH, y + HEIGHT, x + WIDTH / 2, y + HEIGHT / 2);// 画炮管
			break;
		}

		g.setColor(c);

		for (int i = 0; i < m.size();) {
			if (m.get(i).isAlive()) {
				if(this!=tc.enemyTank) {
					if(m.get(i).hitTank(tc.enemyTank)) {
						m.remove(i);
						tc.enemyTank.isAlive=false;
					}
					else {
						m.get(i).draw(g);
						i++;
					}
				}
				else {
					if(m.get(i).hitTank(tc.myTank)) {
						m.remove(i);
						tc.myTank.isAlive=false;
					}
					else {
						m.get(i).draw(g);
						i++;
					}
				}
			} else {
				m.remove(i);
			}
		}
		move();
		if(!isGood) {
			if(gunTime==0) {
				m.add(fire());
				gunTime=r.nextInt(5)+5;
			}
			gunTime--;
		}
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {

		case KeyEvent.VK_LEFT:// 如何重置方向的判断值，否则会出现全部为true无法移动的bug；
			bL = true;
			break;// 如果没有break,会不再做判断而直接顺序执行
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		}
		direction();
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_CONTROL:
			/*
			 * 此处为与教程的不同之处，我认为绘画子弹功能应该与坦克更加贴近，所以将子弹引用设在
			 * 坦克内，坦克的fire方法负责新建子弹对象，当引用不为空时，坦克的draw方法内会调用子
			 * 弹的draw方法。教程中将子弹的引用放在Client中，我认为从逻辑上不够妥当。
			 */
			m.add(fire());
			break;
		case KeyEvent.VK_LEFT:// 如何重置方向的判断值，否则会出现全部为true无法移动的bug；
			bL = false;
			break;// 如果没有break,会不再做判断而直接顺序执行
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		}
		direction();
	}

	public void direction() {
		if (bL || bR || bU || bD == false)
			dir = Direction.stop;
		if (bL && bU && !(bR || bD) == true)
			dir = Direction.left_up;
		if (bL && bD && !(bR || bU) == true)
			dir = Direction.left_down;
		if (bR && bU && !(bL || bD) == true)
			dir = Direction.right_up;
		if (bR && bD && !(bL || bU) == true)
			dir = Direction.right_down;
		if (bL && !(bU || bR || bD) == true)
			dir = Direction.left;
		if (bR && !(bU || bL || bD) == true)
			dir = Direction.right;
		if (bU && !(bL || bR || bD) == true)
			dir = Direction.up;
		if (bD && !(bU || bR || bL) == true)
			dir = Direction.down;
	}

	public Missile fire() {
		return new Missile(x, y, gunDir);
	}
	public Rectangle getRect() {
		return new Rectangle(x,y,WIDTH, HEIGHT);
	}
}
