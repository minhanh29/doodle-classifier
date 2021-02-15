import neuralnetwork.Data;
import java.util.List;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

public class Utils
{
	public static final int PIECE = 784;
	public static final int SIZE = 10000;

	public static final int DOOR = 0;
	public static final int BASKETBALL = 1;
	public static final int SNOW = 2;

	private static byte[] basketballData, doorData, snowData;
	private static List<Data> trainingData, testingData;

	// convert output to label
	public static int outputToLabel(double[] output)
	{
		int index = 0;
		for (int i = 0; i < output.length; i++)
		{
			if (output[i] > output[index])
				index = i;
		}

		return index;
	}


	// convert array to list
	public static List<Data> arrayToList(byte[] data, int label)
	{
		int size = data.length / PIECE;
		List<Data> result = new ArrayList<Data>();
		for (int n = 0; n < size; n++)
		{
			byte[] input = new byte[PIECE];
			int start = n * PIECE;

			for (int i = 0; i < PIECE; i++)
				input[i] = data[start+i];

			Data d = new Data(input, label);
			result.add(d);
		}
		return result;
	}


	// convert Label int to String
	public static String getLabelString(int label)
	{
		switch(label)
		{
			case DOOR:
				return "Door";
			case BASKETBALL:
				return "Basketball";
			case SNOW:
				return "Snow";
			default:
				return "Undefined";
		}
	}


	public void displayImageData(Graphics g, byte[] data)
	{
		// show the first 10 images
		int res = 2;
		int size = data.length / PIECE;
		for (int n = 0; n < size; n++)
		{
			int start = n * PIECE;
			int startX = 28 * (n % 10) * res;
			int startY = 28 * (n / 10) * res;
			// int startY = 0;
			for (int i = 0; i < PIECE; i++)
			{
				int value = 255 - Byte.toUnsignedInt(data[start + i]);
				Color color = new Color(value, value, value);
				g.setColor(color);
				int x = i % 28;
				int y = i / 28;
				g.fillRect(startX + x * res, startY + y * res, res, res);
			}
		}
	}


	// return the training data
	public static List<Data> getTrainingData()
	{
		if (trainingData == null)
			initData();
		return trainingData;
	}


	// return the testing data
	public static List<Data> getTestingData()
	{
		if (testingData == null)
			initData();
		return testingData;
	}

	private static void initData()
	{
		// read the file
		readData("data/door" + SIZE + ".npy", Utils.DOOR);
		readData("data/basketball" + SIZE + ".npy", Utils.BASKETBALL);
		readData("data/snowflake" + SIZE + ".npy", Utils.SNOW);

		// split data to training and testing
		splitData();
	}


	// convert to lists
	private static void splitData()
	{
		// convert data to list
		List<Data> door = Utils.arrayToList(doorData, Utils.DOOR);
		List<Data> basketball = Utils.arrayToList(basketballData, Utils.BASKETBALL);
		List<Data> snow = Utils.arrayToList(snowData, Utils.SNOW);

		// split to training and testing
		int limit = (int) (door.size() * 0.8);
		List<Data> doorTrain = door.subList(0, limit);
		List<Data> doorTest = door.subList(limit, door.size());

		List<Data> basketballTrain = basketball.subList(0, limit);
		List<Data> basketballTest = basketball.subList(limit, basketball.size());

		List<Data> snowTrain = snow.subList(0, limit);
		List<Data> snowTest = snow.subList(limit, snow.size());

		// add all data to one place
		// training
		trainingData = new ArrayList<Data>();
		trainingData.addAll(doorTrain);
		trainingData.addAll(basketballTrain);
		trainingData.addAll(snowTrain);

		// testing
		testingData = new ArrayList<Data>();
		testingData.addAll(doorTest);
		testingData.addAll(basketballTest);
		testingData.addAll(snowTest);
	}


	// read data from files
	private static void readData(String filename, int label)
	{
		Path path = Paths.get(filename);
		try {
			switch (label)
			{
				case Utils.DOOR:
					doorData = Files.readAllBytes(path);
					break;
				case Utils.BASKETBALL:
					basketballData = Files.readAllBytes(path);
					break;
				case Utils.SNOW:
					snowData = Files.readAllBytes(path);
					break;
			}
		}
		catch (IOException ex)
		{
			System.out.println("Cannot read file");
		}
	}


}
