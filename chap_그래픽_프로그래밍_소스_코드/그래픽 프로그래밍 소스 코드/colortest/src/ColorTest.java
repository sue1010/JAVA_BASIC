import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

// 소스를 입력하고 Ctrl+Shift+O를 눌러서 필요한 파일을 포함한다. 


class MyPanel extends JPanel implements ActionListener {
	JButton button;
	Color color = new Color(0, 0, 0);


	public MyPanel() {
		setLayout(new BorderLayout());
		button = new JButton("색상 변경");
		button.addActionListener(this);
		add(button, BorderLayout.SOUTH);
	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(color);
		g.fillRect(10, 10, 210, 220);

	}

	public void actionPerformed(ActionEvent e) {
		color = new Color((int) (Math.random() * 255.0),
			(int) (Math.random() * 255.0), (int) (Math.random() * 255.0));

		repaint();
	}
}

public class ColorTest extends JFrame {
	public ColorTest() {
		setSize(240, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Color Test");
		setVisible(true);
		JPanel panel = new MyPanel();
		add(panel);
	}

	public static void main(String[] args) {
		ColorTest s = new ColorTest();
	}

}