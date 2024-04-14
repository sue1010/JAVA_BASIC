import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

// 소스를 입력하고 Ctrl+Shift+O를 눌러서 필요한 파일을 포함한다. 

class MyPanel extends JPanel implements ActionListener {

	private final int WIDTH = 700;
	private final int HEIGHT = 700;
	private final int START_X = 0;
	private final int START_Y = 250;
	private BufferedImage image;
	private int direction[] = new int[10];	//방향키
	private Timer timer;
	private int x[] = new int[10], y[] = new int[10];
	private int count = 0;

	public MyPanel() {
		Random r = new Random();
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setDoubleBuffered(true);

		File input = new File("ship.jpg");
		try {
			image = ImageIO.read(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i <10; i++) {
			x[i] = r.nextInt(700);
			y[i] = r.nextInt(700);
		}
	
		
		timer = new Timer(20, this);
		timer.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int i = 0; i <10; i++) {
			g.drawImage(image, x[i], y[i], this);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Random rand = new Random();
		for(int i = 0; i <10; i++) {
			direction[i] = rand.nextInt(4)+1;
			count++;
			if(count < 700) {
				if(direction[i] == 1) {
					x[i] += 1; 
				}
				else if(direction[i] == 2) {
					y[i] -= 1;
				}
				else if(direction[i] == 3) {
					x[i] -= 1;
				}
				else {
					y[i] += 1;
				}
			}
			else if(count < 1400) {
				if(direction[i] == 1) {
					x[i] -= 1;
				}
				else if(direction[i] == 2) {
					y[i] += 1;
				}
				else if(direction[i] == 3) {
					x[i] += 1;
				}
				else {
					y[i] -= 1;
				}
			}
			
			else {
				count = 0;
			}
		}
		repaint();
	}
}

public class MyFrame extends JFrame {

	public MyFrame() {
		add(new MyPanel());
		setTitle("애니메이션 테스트");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 700);
		setVisible(true);
	}

	public static void main(String[] args) {
		new MyFrame();
	}
}
