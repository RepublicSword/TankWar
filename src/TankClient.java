/*
	This version I start the game,the important module I creat is how the computer tank fire and move.
 */
import java.awt.*;
import java.awt.event.*;

public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	int x=50,y=50;
	int j=500,k=500;
	boolean start=false;
	Tank myTank=new Tank(x,y,true,this);	
	Tank enemyTank=new Tank(j,k,false,this);	
	public void launchFrame() {
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setLocation(400,300);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		this.setBackground(Color.GREEN);
		this.setResizable(false);
		this.addKeyListener(new KeyMonitor());
		this.setVisible(true);
		new Thread(new PaintThread()).start();
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub

		g.drawString("Missile count: "+myTank.m.size(), 60, 60);
		if(myTank.isAlive)myTank.draw(g);
		if(enemyTank.isAlive)enemyTank.draw(g);
	}

	private class PaintThread extends Thread {
		//重画单开线程
		@Override
		public void run() {
			while (true) {
				if(start)repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	private class KeyMonitor extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			//System.out.print("ok");
			start=true;
			myTank.keyPressed(e);
			}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			myTank.keyReleased(e);
		}
		
		}

	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TankClient tc = new TankClient();
		tc.launchFrame();
	}

}
