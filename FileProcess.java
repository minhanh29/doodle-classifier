import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

public class FileProcess
{
	private byte[] data;
	private int size;

	public FileProcess(int size)
	{
		this.size = size;
		loadByte("data/door.npy");
		writeByte("data/door" + size + ".npy");
		loadByte("data/basketball.npy");
		writeByte("data/basketball" + size + ".npy");
		loadByte("data/snowflake.npy");
		writeByte("data/snowflake" + size + ".npy");
	}


	private void loadByte(String file)
	{
		// read the file
		Path path = Paths.get(file);
		try {
			data = Files.readAllBytes(path);
		}
		catch (IOException ex)
		{
			System.out.println("Cannot read file");
		}

	}


	private void writeByte(String file)
	{
		int end = 784 * size;
		byte[] bytes = new byte[end];
		for (int i = 0; i < end; i++)
			bytes[i] = data[80 + i];

		Path path = Paths.get(file);
		try {
			Files.write(path, bytes);
		}
		catch (IOException ex)
		{
			System.out.println("Cannot write file");
		}
	}
}
