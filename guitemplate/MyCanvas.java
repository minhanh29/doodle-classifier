package guitemplate;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Point;
import java.awt.Color;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

public class MyCanvas extends Canvas implements MouseListener, MouseMotionListener
{
	public static final long serialVersionUID = 2L;
	private MainFrame frame;
	private Point p;
	private int strokeSize = 15;
	private BufferedImage img;

	public MyCanvas(MainFrame frame)
	{
		this.frame = frame;
		p = new Point(0, 0);  // cursor position
		addMouseListener(this);
		addMouseMotionListener(this);
	}


	public void paint(Graphics g)
	{
		g.clearRect(0, 0, getWidth(), getHeight());
		frame.draw();
	}


	// Mouse Listener
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

	public void mousePressed(MouseEvent e)
	{
		setForeground(Color.BLACK);
		p.move(e.getX(), e.getY());
	}


	// Mouse Motion Listener
	public void mouseMoved(MouseEvent e) {}

	public void mouseDragged(MouseEvent e)
	{
		BufferStrategy bs = getBufferStrategy();
		Graphics2D g = (Graphics2D)bs.getDrawGraphics();
		g.setStroke(new BasicStroke(strokeSize));
		g.drawLine(p.x, p.y, e.getX(), e.getY());
		bs.show();

		// draw on image
		g = (Graphics2D)getImgGraphics();
		g.setStroke(new BasicStroke(strokeSize));
		g.drawLine(p.x, p.y, e.getX(), e.getY());

		p.move(e.getX(), e.getY());
	}

	public Graphics getImgGraphics()
	{
		return getBufferedImage().getGraphics();
	}


	public BufferedImage getBufferedImage()
	{
		if (img == null)
			img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		return img;
	}
}

