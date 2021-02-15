package guitemplate;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.image.BufferStrategy;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Component;

public abstract class MainFrame extends JFrame
{
	public static final long serialVersionUID = 1L;
	protected static int WIDTH = 500, HEIGHT = 500;

	protected JPanel mainPanel;
	protected MyCanvas canvas;

	public MainFrame()
	{
		this(500, 500);
	}


	public MainFrame(int width, int height)
	{
		super();
		WIDTH = width;
		HEIGHT = height;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);

		mainPanel = new JPanel();
		mainPanel.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		mainPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		mainPanel.setLayout(null);
		add(mainPanel);

		canvas = new MyCanvas(this);
		canvas.setBounds(0, 0, WIDTH, HEIGHT);
		canvas.setVisible(true);
		canvas.setFocusable(false);
		mainPanel.add(canvas);
		pack();
	}


	public BufferStrategy getBufferStrategy()
	{
		if (canvas.getBufferStrategy() == null)
			canvas.createBufferStrategy(2);
		return canvas.getBufferStrategy();
	}


	// add mouse click listener using lambda expression
	public interface ClickListener
	{
		void onMouseClicked();
	}

	public static void addMouseClickListener(Component component, ClickListener listener) {
		component.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				listener.onMouseClicked();
			}

			public void mouseEntered(MouseEvent e) { }

			public void mouseExited(MouseEvent e) { }

			public void mousePressed(MouseEvent e) { }

			public void mouseReleased(MouseEvent e) { }
		});
	}


	public abstract void draw();
}
