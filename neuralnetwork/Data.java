package neuralnetwork;

import java.util.Arrays;

public class Data
{
	private double[] inputs;
	private double[] labels;

	public Data(byte[] data, int label)
	{
		inputs = new double[data.length];

		// normalize the data
		for(int i = 0; i < data.length; i++)
			inputs[i] = Byte.toUnsignedInt(data[i]) / 255.0;

		labels = new double[3];
		labels[label] = 1;
	}


	public double[] getInputs() { return inputs; }

	public double[] getLabels() { return labels; }

	public String toString()
	{
		String result = "\nInput: " + Arrays.toString(inputs);
		result += "\nLabel: " + labels[0];
		return result;
	}
}
