public class Macros
{
	public static int BLACK = 0;
	public static int RED = 1;
	public static int GREEN = 2;
	public static int YELLOW = 3;
	public static int BLUE = 4;
	public static int MAGENA = 5;
	public static int CYAN = 6;
	public static int WHITE = 7;

	// row x, col y
	public static void locate(int x, int y)
	{
		System.out.printf("\033[%d;%dH", x, y);
	}

	public static void cls()
	{
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static void colorText(int color)
	{
		color(color, BLACK);
	}

	public static void colorBg(int color)
	{
		color(WHITE, color);
	}

	public static void color(int foreground, int background)
	{
		System.out.printf("\033[1;3%d;4%dm", foreground, background);
	}

	public static void reset()
	{
		System.out.println("\033[0m");
	}
}
