package BounceThread;

import java.awt.*;
import java.awt.event.*;

public class bounceThread extends Frame implements ActionListener {
	private Canvas canvas;

	public bounceThread() {
		canvas = new Canvas();
		add("Center", canvas);
		Panel p = new Panel();
		Button s = new Button("Start");
		Button c = new Button("Close");
		p.add(s);
		p.add(c);
		s.addActionListener(this);
		c.addActionListener(this);
		add("South", p);
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand() == "Start") {
			Ball b1 = new Ball(canvas, 200, 150, 20, 20, 1, 1, count_balls());
			Ball b2 = new Ball(canvas, 200, 150, 20, 20, 2, 2, count_balls());
			Ball b3 = new Ball(canvas, 200, 150, 20, 20, 3, 3, count_balls());
			Ball b4 = new Ball(canvas, 200, 150, 20, 20, -1, -1, count_balls());
			Ball b5 = new Ball(canvas, 200, 150, 20, 20, -2, -2, count_balls());
			b1.start();
			b2.start();
			b3.start();
			b4.start();
			b5.start();
		} else if (evt.getActionCommand() == "Close")
			System.exit(0);
	}
	
	public int count_balls() {
		cnt = cnt + 1;
		for (long i = 0; i < 1000000; i++);
		return cnt;
	}
	
	int[] x_temp = new int[100];
	int[] y_temp = new int[100];
	int[] XSIZE_temp = new int[100];
	int[] YSIZE_temp = new int[100];
	int[] dx_temp = new int[100];
	int[] dy_temp = new int[100];
	boolean[] check = new boolean[100];
	int cnt = -1;
	
	public static void main(String[] args) {
		Frame f = new bounceThread();
		f.setSize(400, 300);
		
		WindowDestroyer listener = new WindowDestroyer();
		f.addWindowListener(listener);
	
		f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
		});
		
		f.setVisible(true);
	}

	class Ball extends Thread {

		private Canvas box;
		private int idx;
		int i;

		public Ball(Canvas c, int x, int y, int XSIZE, int YSIZE, int dx, int dy,  int ID) {
			box = c;
			
			idx = ID;
			dx_temp[ID] = dx;
			dy_temp[ID] = dy;
			x_temp[ID] = x;
			y_temp[ID] = y;
			XSIZE_temp[ID] = XSIZE;
			YSIZE_temp[ID] = YSIZE;
			check[ID] = true;
		}
		
		public double distance(int x1, int y1, int x2, int y2) {
			return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		}
		
		public boolean meet(int j) {
			if(distance(x_temp[j], y_temp[j], x_temp[idx], y_temp[idx]) < XSIZE_temp[idx] / 2
			+ XSIZE_temp[j] / 2 - 1)
				return true;
			else
				return false;
		}

		public void draw() {
			Graphics g = box.getGraphics();
			g.fillOval(x_temp[idx], y_temp[idx], XSIZE_temp[idx], XSIZE_temp[idx]);
			g.dispose();
		}

		public void move() {
			Graphics g = box.getGraphics();
			g.setXORMode(box.getBackground());
			//g.setColor(Color.white);
			g.fillOval(x_temp[idx], y_temp[idx], XSIZE_temp[idx], XSIZE_temp[idx]);
			x_temp[idx] += dx_temp[idx];
			y_temp[idx] += dy_temp[idx];

			Dimension d = box.getSize();
			if (x_temp[idx] < 0) {
				x_temp[idx] = 0;
				dx_temp[idx] = -dx_temp[idx];
			}
			if (x_temp[idx] + XSIZE_temp[idx] >= d.width) {
				x_temp[idx] = d.width - XSIZE_temp[idx];
				dx_temp[idx] = -dx_temp[idx];
			}
			if (y_temp[idx] < 0) {
				y_temp[idx] = 0;
				dy_temp[idx] = -dy_temp[idx];
			}
			if (y_temp[idx] + XSIZE_temp[idx] >= d.height) {
				y_temp[idx] = d.height - XSIZE_temp[idx];
				dy_temp[idx] = -dy_temp[idx];
			}

			if (i >= 100) {
				for (int j = 0; j <= cnt; j++) {
					/*
					if(check[j]==0) {
						g.setColor(Color.white);
						g.fillOval(x_temp[j], y_temp[j], XSIZE_temp[j], XSIZE_temp[j]);
						g.setColor(Color.black);
					}*/
					
					if (idx != j && check[j]) { 
						
						if(meet(j)) {
						
							if (XSIZE_temp[idx]/2 > 1 && XSIZE_temp[j]/2 > 1) {
						
								Ball b1 = new Ball(canvas, x_temp[idx], y_temp[idx], XSIZE_temp[idx], XSIZE_temp[idx], dx_temp[idx], dy_temp[idx], count_balls());
								Ball b2 = new Ball(canvas, x_temp[idx], y_temp[idx], XSIZE_temp[idx], XSIZE_temp[idx], dy_temp[idx], dx_temp[idx], count_balls());
								Ball b3 = new Ball(canvas, x_temp[j], y_temp[j], XSIZE_temp[j]/2, XSIZE_temp[j]/2, dx_temp[j], dy_temp[j], count_balls());
								Ball b4 = new Ball(canvas, x_temp[j], y_temp[j], XSIZE_temp[j]/2, XSIZE_temp[j]/2, dy_temp[j], dx_temp[j], count_balls());
								b1.start();
								b2.start();
								b3.start();
								b4.start();
								
								g.fillOval(x_temp[idx], y_temp[idx], XSIZE_temp[idx], XSIZE_temp[idx]);
								g.fillOval(x_temp[j], y_temp[j], XSIZE_temp[j], XSIZE_temp[j]);
								
								check[idx] = false;
								check[j] = false;
								
								break;
							}
							else System.exit(0);
							
						}
					}
				}
			}
			//g.setColor(Color.black);
			g.fillOval(x_temp[idx], y_temp[idx], XSIZE_temp[idx], XSIZE_temp[idx]);
			g.dispose();
		}

		public void run() {
			draw();
			while(true) {
				if (check[idx]) {
					move();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
					}
				}
				else {
					break;
				}
			}
		}
	}
}
