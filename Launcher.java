import java.awt.EventQueue;

public class Launcher
{
	public static void main(String[] args)
	{
		EventQueue.invokeLater(() -> {
			MyFrame mainFrame = new MyFrame();
			mainFrame.setVisible(true);
			mainFrame.draw();
		});

	}
}
