package neuralnetwork;

import java.util.Arrays;
import java.util.Random;

public class Matrix
{
	private int row, col;
	private double[][] matrix;

	public Matrix(int row, int col)
	{
		this.row = row;
		this.col = col;
		matrix = new double[row][col];
	}


	public void randomize()
	{
		Random rand = new Random();
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[i].length; j++)
				matrix[i][j] = rand.nextDouble() * 2 - 1;
	}


	/* GETTERS, SETTERS */
	public double[][] getArray() { return matrix; }

	public int getRow() { return row; }

	public int getCol() { return col; }

	public void setValue(int row, int col, double value)
	{
		if (row < 0 || row >= this.row || col < 0 || col >= this.col)
		{
			System.out.println("Invalid index");
			return;
		}

		matrix[row][col] = value;
	}

	public String toString()
	{
		String result = "";
		for (int i = 0; i < matrix.length; i++)
		{
			result += Arrays.toString(matrix[i]) + "\n";
		}
		return result;
	}

	// return 1D array
	public double[] toVector()
	{
		double[] result = new double[row * col];
		int index = 0;
		for (int i = 0 ; i < matrix.length; i++)
		{
			for (int j = 0; j < matrix[i].length; j++)
			{
				result[index] = matrix[i][j];
				index++;
			}
		}

		return result;
	}


	/* SCALAR OPERATIONS */

	// method to pass
	public interface PassedMethod {
		double operate(double x);
	}


	// apply passing method to all elements in the matrix
	public void map(PassedMethod operation)
	{
		for (int i = 0; i < matrix.length; i++)
		{
			for (int j = 0; j < matrix[i].length; j++)
			{
				matrix[i][j] = operation.operate(matrix[i][j]);
			}
		}
	}


	// add n to each element
	public void add(double n)
	{
		map(element -> element + n);
	}


	// Hadamard multiplication
	public void multiply(double n)
	{
		map(element -> element * n);
	}


	// normalize each row
	public void rowNormalize()
	{
		for (int i = 0; i < matrix.length; i++)
		{
			double sum = 0;
			for (double element : matrix[i])
				sum += element;

			for (int j = 0; j < matrix[i].length; j++)
				matrix[i][j] = matrix[i][j] / sum;
		}
	}


	/* STATIC OPERATION */

	public static Matrix arrayToMatrix(double[] array)
	{
		Matrix result = new Matrix(array.length, 1);
		double[][] r = result.getArray();
		for (int i = 0; i < array.length; i++)
			r[i][0] = array[i];

		return result;
	}

	// element wise operations

	public interface ElementWiseOp
	{
		double performElementWise(double x, double y);
	}


	public static Matrix map(Matrix m1, Matrix m2, ElementWiseOp operation)
	{
		if (m1.getRow() != m2.getRow() || m1.getCol() != m2.getCol())
			return null;

		Matrix result = new Matrix(m1.getRow(), m1.getCol());
		double[][] a1 = m1.getArray();
		double[][] a2 = m2.getArray();
		double[][] r = result.getArray();

		for (int i = 0; i < r.length; i++)
			for (int j = 0; j < r[0].length; j++)
				r[i][j] = operation.performElementWise(a1[i][j], a2[i][j]);

		return result;
	}


	public static Matrix add(Matrix m1, Matrix m2)
	{
		return map(m1, m2, (x, y) -> x + y);
	}


	public static Matrix subtract(Matrix m1, Matrix m2)
	{
		return map(m1, m2, (x, y) -> x - y);
	}


	public static Matrix pairMultiply(Matrix m1, Matrix m2)
	{
		return map(m1, m2, (x, y) -> x * y);
	}


	/* MULTIPLICATION */
	public static Matrix multiply(Matrix m1, Matrix m2)
	{
		if (m1.getCol() != m2.getRow())
			return null;

		Matrix result = new Matrix(m1.getRow(), m2.getCol());
		double[][] a1 = m1.getArray();
		double[][] a2 = m2.getArray();
		double[][] r = result.getArray();

		for (int i = 0; i < r.length; i++)
		{
			for (int j = 0; j < r[i].length; j++)
			{
				double sum = 0;
				for (int k = 0; k < a2.length; k++)
					sum += a1[i][k] * a2[k][j];
				r[i][j] = sum;
			}
		}

		return result;
	}


	// static scalar operations
	public interface ScalarOperation
	{
		double performScalar(double x);
	}


	public static Matrix map(Matrix m, ScalarOperation operation)
	{
		Matrix result = new Matrix(m.getRow(), m.getCol());
		double[][] r = result.getArray();
		double[][] a = m.getArray();

		for (int i = 0; i < r.length; i++)
			for (int j = 0; j < r[i].length; j++)
				r[i][j] = operation.performScalar(a[i][j]);

		return result;
	}


	// Hadamard multiplication
	public static Matrix multiply(Matrix m, double n)
	{
		return map(m, element -> element * n);
	}


	public static Matrix add(Matrix m, double n)
	{
		return map(m, element -> element + n);
	}


	// transpose
	public static Matrix transpose(Matrix m)
	{
		Matrix result = new Matrix(m.getCol(), m.getRow());
		double[][] r = result.getArray();
		double[][] a = m.getArray();

		for (int i = 0; i < result.getRow(); i++)
			for (int j = 0; j < result.getCol(); j++)
				r[i][j] = a[j][i];

		return result;
	}
}
