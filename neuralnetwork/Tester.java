package neuralnetwork;

import java.util.Arrays;
import java.util.Random;

public class Tester
{
	public static void main(String[] args)
	{
		// Matrix m1 = new Matrix(2, 3);
		// m1.randomize();

		// Matrix m2 = new Matrix(3, 2);
		// m2.randomize();

		// System.out.println(m1);
		// System.out.println(m2);

		// System.out.println("m1 multiply m2" + Matrix.pairMultiply(m1, m2));
		// // m1.add(1);
		// System.out.println("m2 multiply by 2" + Matrix.multiply(m2, 2));
		// m2.multiply(2);
		// System.out.println(m1);
		// System.out.println(m2);


		// System.out.println("m1 multiply m2");
		// Matrix m3 = Matrix.multiply(m1, m2);
		// System.out.println(m3);


		// XOR operation
		Data[] trainingData = {
			new Data(0, 1, 1),
			new Data(1, 0, 1),
			new Data(0, 0, 0),
			new Data(1, 1, 0),
		};

		NeuralNetWork brain = new NeuralNetWork(2, 4, 1);

		// training
		Random rand = new Random();
		for (int i = 0; i < 50000; i++)
		{
			int index = rand.nextInt(trainingData.length);
			Data data = trainingData[index];
			brain.train(data.getInputs(), data.getLabels());
		}

		// testing
		double[] inputs = {0, 1};
		double[] output = brain.feedForward(inputs);
		System.out.println(Arrays.toString(output));

		double[] input2 = {1, 0};
		output = brain.feedForward(input2);
		System.out.println(Arrays.toString(output));

		double[] input3 = {0, 0};
		output = brain.feedForward(input3);
		System.out.println(Arrays.toString(output));

		double[] input4 = {1, 1};
		output = brain.feedForward(input4);
		System.out.println(Arrays.toString(output));
	}
}
