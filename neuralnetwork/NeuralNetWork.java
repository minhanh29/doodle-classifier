package neuralnetwork;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class NeuralNetWork
{
	private Matrix weightIH, weightHO, biasH, biasO;
	private double learningRate;

	public NeuralNetWork(int numInput, int numHidden, int numOutput)
	{
		weightIH = new Matrix(numHidden, numInput);
		weightIH.randomize();
		weightHO = new Matrix(numOutput, numHidden);
		weightHO.randomize();

		learningRate = 0.1;

		biasH = new Matrix(numHidden, 1);
		biasH.randomize();
		biasO = new Matrix(numOutput, 1);
		biasH.randomize();
	}


	// return the guessing
	public double[] predict(double[] inputs)
	{
		Matrix inputMatrix = Matrix.arrayToMatrix(inputs);

		// input to hidden layer
		// compute weighted sum
		Matrix hiddenMatrix = Matrix.multiply(weightIH, inputMatrix);

		// add bias
		hiddenMatrix = Matrix.add(hiddenMatrix, biasH);

		// apply activation function
		hiddenMatrix.map(x -> sigmoid(x));


		// hidden to output layer
		// compute weighted sum
		Matrix outputMatrix = Matrix.multiply(weightHO, hiddenMatrix);

		// add bias
		outputMatrix = Matrix.add(outputMatrix, biasO);

		// apply activation function
		outputMatrix.map(x -> sigmoid(x));

		return outputMatrix.toVector();
	}


	// train the neural network
	public void train(double[] inputs, double[] labels)
	{
		Matrix inputMatrix = Matrix.arrayToMatrix(inputs);

		// input to hidden layer
		// compute weighted sum
		Matrix hiddenMatrix = Matrix.multiply(weightIH, inputMatrix);

		// add bias
		hiddenMatrix = Matrix.add(hiddenMatrix, biasH);

		// apply activation function
		hiddenMatrix.map(x -> sigmoid(x));

		// hidden to output layer
		// compute weighted sum
		Matrix outputMatrix = Matrix.multiply(weightHO, hiddenMatrix);

		// add bias
		outputMatrix = Matrix.add(outputMatrix, biasO);

		// apply activation function
		outputMatrix.map(x -> sigmoid(x));


		/* COMPUTE ERRORS */
		// convert label to matrix
		Matrix labelMatrix = Matrix.arrayToMatrix(labels);

		// the output error
		Matrix outputError = Matrix.subtract(labelMatrix, outputMatrix);

		// the hidden error
		Matrix weightHO_t = Matrix.transpose(weightHO);
		// weightHO_t.rowNormalize();
		Matrix hiddenError = Matrix.multiply(weightHO_t, outputError);


		/* COMPUTE DELTA */
		// hidden to output delta = outputError * learningRate * (output(1-output)) . hidden_t
		outputMatrix.map(x -> desigmoid(x));
		Matrix biasO_delta = Matrix.pairMultiply(Matrix.multiply(outputError, learningRate),
			outputMatrix);

		Matrix HO_delta = Matrix.multiply(biasO_delta, Matrix.transpose(hiddenMatrix));

		// similarly, input to hidden
		hiddenMatrix.map(x -> desigmoid(x));
		Matrix biasH_delta = Matrix.pairMultiply(Matrix.multiply(hiddenError, learningRate),
			hiddenMatrix);

		Matrix IH_delta  = Matrix.multiply(biasH_delta, Matrix.transpose(inputMatrix));


		/* ADD DELTA */
		// add delta to the weightHO
		weightHO = Matrix.add(weightHO, HO_delta);

		// add delta to the weightIH
		weightIH = Matrix.add(weightIH, IH_delta);

		// add bias delta
		biasO = Matrix.add(biasO, biasO_delta);
		biasH = Matrix.add(biasH, biasH_delta);
	}


	// activation function
	private double sigmoid(double x)
	{
		return 1.0 / (1.0 + Math.exp(-x));
	}


	// derivative of sigmoid
	private double desigmoid(double x)
	{
		return x * (1 - x);
	}


	// save the neural network
	public void save(String filename)
	{
		try {
			PrintWriter pw = new PrintWriter(new File(filename));

			// save weightIH
			saveMatrix(weightIH, pw);

			// save weightHO
			saveMatrix(weightHO, pw);

			// save biasH
			saveMatrix(biasH, pw);

			// save biasO
			saveMatrix(biasO, pw);

			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {}
	}


	private void saveMatrix(Matrix m, PrintWriter pw)
	{
		double[][] a = m.getArray();
		pw.println(m.getRow());  // rows
		pw.println(m.getCol());  // columns
		for (int i = 0; i < a.length; i++)
		{
			for (int j = 0; j < a[i].length; j++)
			{
				pw.print(a[i][j] + " ");
			}
			pw.println();
		}
	}


	public void load(String filename) throws FileNotFoundException
	{
		Scanner scan = new Scanner(new File(filename));

		// save weightIH
		int row = scan.nextInt();
		int col = scan.nextInt();
		weightIH = new Matrix(row, col);
		loadMatrix(weightIH, scan);

		// load weightHO
		row = scan.nextInt();
		col = scan.nextInt();
		weightHO = new Matrix(row, col);
		loadMatrix(weightHO, scan);

		// load biasH
		row = scan.nextInt();
		col = scan.nextInt();
		biasH = new Matrix(row, col);
		loadMatrix(biasH, scan);

		// load biasO
		row = scan.nextInt();
		col = scan.nextInt();
		biasO = new Matrix(row, col);
		loadMatrix(biasO, scan);

		scan.close();
	}


	private void loadMatrix(Matrix m, Scanner scan)
	{
		double[][] a = m.getArray();

		for (int i = 0; i < a.length; i++)
		{
			for (int j = 0; j < a[i].length; j++)
			{
				a[i][j] = scan.nextDouble();
			}
		}
	}
}
