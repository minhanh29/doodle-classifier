import guitemplate.*;
import neuralnetwork.*;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.Box;

import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

import java.util.Collections;
import java.util.List;

import java.io.FileNotFoundException;

public class MyFrame extends MainFrame
{
	public static final long serialVersionUID = 3L;

	private JPanel controlPanel;
	private JButton saveBtn, loadBtn, trainBtn, predictBtn, clearBtn;
	private JLabel answerLabel;

	private List<Data> trainingData, testingData;

	private NeuralNetWork brain;
	private int epoch;

	public MyFrame()
	{
		super(676, 476);

		initButtons();

		// answer label
		answerLabel = new JLabel("Answer:");
		answerLabel.setFont(new Font(null, Font.BOLD, 15));
		controlPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		controlPanel.add(answerLabel);

		// get the data
		trainingData = Utils.getTrainingData();
		testingData = Utils.getTestingData();

		// create Neural Network
		brain = new NeuralNetWork(Utils.PIECE, 64, 3);
		epoch = 1;
	}


	public void draw()
	{
		BufferStrategy bs = getBufferStrategy();
		Graphics g = bs.getDrawGraphics();

		g.clearRect(0, 0, WIDTH, HEIGHT);

		/*
		 * draw here
		 */

		bs.show();
		g.dispose();
	}


	// train the data
	private void train()
	{
		Collections.shuffle(trainingData);

		int count = 0;
		int size = trainingData.size();
		int end = (int)(size / 11.0);

		// clear screen
		if (epoch == 1)
			Macros.cls();

		Macros.locate(epoch * 2 -1, 1);
		System.out.print("Epoch " + epoch + ": [");
		// go to the end of line
		Macros.locate(epoch * 2 -1, 25);
		System.out.print("0%");
		Macros.locate(epoch * 2 -1, 22);
		System.out.println("]");
		int cursor = 11;

		for (int i = 0; i < size; i++)
		{
			// training
			Data d = trainingData.get(i);
			brain.train(d.getInputs(), d.getLabels());

			// printing
			int percent = (int)Math.floor(i * 100.0 /size) + 1;
			Macros.locate(epoch * 2 -1, 25);
			System.out.print("" + percent + "%");
			if (count >= end)
			{
				count = 0;
				Macros.locate(epoch * 2 -1, cursor);
				System.out.print("=");
				cursor++;
			}
			count++;
		}

		System.out.println();
		System.out.printf("Accuracy: %.2f%%\n", test() * 100);
		epoch++;
	}


	// test the neural network
	private double test()
	{
		int correct = 0;
		for (int i = 0; i < testingData.size(); i++)
		{
			Data d = testingData.get(i);
			double[] output = brain.predict(d.getInputs());
			if (Utils.outputToLabel(output) == Utils.outputToLabel(d.getLabels()))
				correct++;
		}

		return (double) correct / testingData.size();
	}


	// predict the image
	private void predict()
	{
		// convert img to int[]
		BufferedImage img = canvas.getBufferedImage();

		int res = (int)(canvas.getWidth() / 28.0);
		byte[] values = new byte[Utils.PIECE];
		for (int i = 0; i < Utils.PIECE; i++)
		{
			int x = res * (i % 28);
			int y = res * (i / 28);
			Color color = new Color(img.getRGB(x, y));
			values[i] = (byte) color.getRed();
		}

		Data data = new Data(values, 1);
		double[] output = brain.predict(data.getInputs());
		String text = "Answer: " + Utils.getLabelString(Utils.outputToLabel(output));
		answerLabel.setText(text);
	}


	// initialze the buttons
	private void initButtons()
	{
		// add space for buttons
		int width = 200;
		WIDTH -= width;
		canvas.setBounds(width, 0, WIDTH, HEIGHT);

		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.setBounds(25, 0, width-25, HEIGHT);
		mainPanel.add(controlPanel);

		// add buttons
		saveBtn = new JButton("Save Model");
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				brain.save("my_model.txt");
				JOptionPane.showMessageDialog(null, "Model is saved to my_model.txt", "Save Model", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		controlPanel.add(saveBtn);

		loadBtn = new JButton("Load Model");
		loadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				try {
					brain.load("my_model.txt");
					JOptionPane.showMessageDialog(null, "Model is loaded successfully!", "Load Model", JOptionPane.INFORMATION_MESSAGE);
				}
				catch(FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, "Cannot find my_model.txt!", "Load Model Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		controlPanel.add(loadBtn);

		trainBtn = new JButton("Train Model");
		trainBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				train();
				JOptionPane.showMessageDialog(null, "Training model completed", "Train Model", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		controlPanel.add(trainBtn);

		predictBtn = new JButton("Predict");
		predictBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				predict();
			}
		});
		controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		controlPanel.add(predictBtn);

		// clear the canvas
		clearBtn = new JButton("Clear Screen");
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				BufferStrategy bs = getBufferStrategy();
				Graphics g = bs.getDrawGraphics();
				g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
				bs.show();
				g.dispose();

				g = canvas.getImgGraphics();
				g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
				g.dispose();
			}
		});
		controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		controlPanel.add(clearBtn);

	}
}
