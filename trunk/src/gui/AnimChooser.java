package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

import javax.swing.JPanel;

import animation.CharacterAnimation;

/**
 * 
 * @author Richard Jenkin
 * @version 0.5
 *
 */
public class AnimChooser extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;

	private Thread timer;
	private final int DELAY = 25;
	private boolean threadSuspended;

	private Map<String, CharacterAnimation> animations;

	private int width, height;


	public AnimChooser(int width, int height, Map<String, CharacterAnimation> animations) {
		addKeyListener(new AnimAdapter());
		setFocusable(true);
		setBackground(Color.BLUE);
		setDoubleBuffered(true);

		this.animations = animations;

		this.width = width;
		this.height = height;
	}

	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.fillRect(0, 0, width, height);

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	public void addNotify() {
		super.addNotify();
		timer = new Thread(this);
		timer.start();
	}

	public void cycle() {
		// TODO: Anything to call routinely goes here
	}

	public void run() {
		long beforeTime, timeDiff, sleep;

		beforeTime = System.currentTimeMillis();

		while (true) {
			cycle();
			repaint();

			timeDiff = System.currentTimeMillis() - beforeTime;
			sleep = DELAY - timeDiff;

			if (sleep < 0) {
				sleep = 2;
			}

			try {
				Thread.sleep(sleep);

				if (isThreadSuspended()) {
					synchronized (this) {
						while (isThreadSuspended()) {
							wait();
						}
					}
				}
			} catch (InterruptedException e) {
				System.out.println("Interrupted!");
			}

			beforeTime = System.currentTimeMillis();
		}
	}


	public void setThreadSuspended(boolean threadSuspended) {
		this.threadSuspended = threadSuspended;
	}

	public boolean isThreadSuspended() {
		return threadSuspended;
	}

	private class AnimAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {

		}

		@Override
		public void keyReleased(KeyEvent e) {

		}

		@Override
		public void keyTyped(KeyEvent e) {

		}
	}

}
