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
public class AnimationChooser extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;

	private Thread timer;
	private final int DELAY = 25;
	private boolean threadSuspended = false;

	private Map<String, CharacterAnimation> animations;
	private String[] keys;
	private int currentAnimation;

	private int width, height;

	public AnimationChooser(int width, int height) {
		addKeyListener(new AnimAdapter());
		setFocusable(true);
		setBackground(Color.WHITE);
		setDoubleBuffered(true);

		currentAnimation = 0;

		this.width = width;
		this.height = height;
	}

	public void setAnimations(Map<String, CharacterAnimation> animations) {
		this.animations = animations;
		this.keys = animations.keySet().toArray(new String[0]);

	}

	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);

		g2d.drawImage(animations.get(keys[currentAnimation]).getImage(), 300, 50, this);

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
		animations.get(keys[currentAnimation]).update();
	}

	@Override
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
						while (isThreadSuspended())
							wait();
					}
				}
			} catch (InterruptedException e) {
				System.out.println("Interrupted");
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

		public void keyPressed(KeyEvent e) {
			System.out.print("down");
		}

		public void keyReleased(KeyEvent e) {
			System.out.println("up");
		}

	
		public void keyTyped(KeyEvent e) {
			System.out.println("typed");
		}
	}
}
