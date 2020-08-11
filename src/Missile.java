import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;


public class Missile {
	int x,y;
	boolean misLife=true;
	Tank.Direction dir;
	public static final int MXSPEED = 10;
	public static final int MYSPEED = 10;
	public static final int MWIDTH = 10;
	public static final int MHEIGHT = 10;
	public Missile(int x, int y, Tank.Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillOval(x+Tank.WIDTH / 3, y+Tank.HEIGHT/3, MWIDTH, MHEIGHT);
		g.setColor(c);
		move(dir);
	}
	public void move(Tank.Direction dir) {
		switch(dir) {
		case stop:break;
		case left:
			x -= MXSPEED;
			break;// ���û��break,�᲻�����ж϶�ֱ��˳��ִ��
		case up:
			y -= MYSPEED;
			break;
		case right:
			x += MXSPEED;
			break;
		case down:
			y += MYSPEED;
			break;
		case left_up:
			x-=MXSPEED;
			y-=MYSPEED;
			break;
		case left_down:
			x-=MXSPEED;
			y+=MYSPEED;
			break;//����������break����bug����������޷��ƶ�
		case right_up:
			x+=MXSPEED;
			y-=MYSPEED;
			break;
		case right_down:
			x+=MXSPEED;
			y+=MYSPEED;
			break;
		}
	}
	public boolean isAlive() {
		if(x<=0||y<=0||x>=800||y>=600)misLife=false;
		return misLife;
	}
	public Rectangle getRect() {
		return new Rectangle(x,y,MWIDTH, MHEIGHT);
	}
	public boolean hitTank(Tank t) {
		return this.getRect().intersects(t.getRect())&&t.isAlive;//��ֻ֤����̹�˻��ŵ�ʱ�������ײ���
	}
}
