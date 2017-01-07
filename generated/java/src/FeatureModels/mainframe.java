package FeatureModels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.io.*;
import java.util.Map;
import java.util.Scanner;

/**
 * The Class mainframe - Main GUI.
 */
public class mainframe extends JFrame {

	/** The console text. */
	private JTextArea consoleText;

	private FeatureModel fm = null;

	/**
	 * Instantiates a new mainframe and initializes all the containers as well
	 * as their needed listeners.
	 */
	mainframe() {

		/*
		 * fm.addMandatorySub("0", "root"); fm.addXorSub("1", "root");
		 * fm.addXorSub("2", "root"); fm.addOrSub("3", "root"); fm.addOrSub("4",
		 * "root"); fm.addOptionalSub("5", "root"); fm.addOptionalSub("6", "5");
		 * fm.addXorSub("7", "0"); fm.addXorSub("8", "0");
		 */

		final JFrame frame = new JFrame();
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setMinimumSize(new Dimension(1000, 400));
		frame.setPreferredSize(new Dimension(1300, 800));
		frame.setTitle("Feature Tree Model Viewer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// FRAME CONTENT PANE
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		// CONTENT PANELS
		JPanel console = new JPanel();
		contentPane.add(console, BorderLayout.CENTER);
		console.setLayout(new BorderLayout(0, 0));

		JPanel optionsPane = new JPanel();
		console.add(optionsPane, BorderLayout.NORTH);
		optionsPane.setLayout(new BorderLayout(0, 0));

		JPanel buttonsPane = new JPanel();
		optionsPane.add(buttonsPane, BorderLayout.EAST);

		JButton newModel = new JButton("New Model");
		buttonsPane.add(newModel);

		JButton addM = new JButton("Add Mandatory");
		buttonsPane.add(addM);

		JButton addO = new JButton("Add Optional");
		buttonsPane.add(addO);

		JButton addOr = new JButton("Add Or");
		buttonsPane.add(addOr);

		JButton addXor = new JButton("Add Xor");
		buttonsPane.add(addXor);

		JButton button_6 = new JButton("Add Requires");
		buttonsPane.add(button_6);

		JButton button_7 = new JButton("Add Excludes");
		buttonsPane.add(button_7);

		JButton button_8 = new JButton("Remove Requires");
		buttonsPane.add(button_8);

		JButton button_9 = new JButton("Remove Excludes");
		buttonsPane.add(button_9);

		JButton button_10 = new JButton("Gen Configurations");
		buttonsPane.add(button_10);

		JButton button_11 = new JButton("Make Config");
		buttonsPane.add(button_11);

		JButton button_2 = new JButton("Print Model");
		buttonsPane.add(button_2);

		consoleText = new JTextArea();
		consoleText.setTabSize(2);
		consoleText.setEditable(false);
		consoleText
				.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JScrollPane consoleScroll = new JScrollPane(consoleText,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		consoleScroll.setPreferredSize(new Dimension(300, 600));
		console.add(consoleScroll);

		newModel.addActionListener(e -> {
			String rootName = JOptionPane.showInputDialog(this,
					"Input name of the Feature Model\n\n", "Make Model",
					JOptionPane.INFORMATION_MESSAGE);
			if (rootName != "")
				fm = new FeatureModel(rootName);
		});

		addM.addActionListener(e -> {
			if (fm == null)
				return;
			addSubFeature("m");
		});

		button_2.addActionListener(e -> {
			consoleText
					.setText("Printing Model\n----------------------------------------------------\n");
			fm.printModel(fm.get(fm.getRootName()), "root", consoleText);
			consoleText.setText(consoleText.getText()
					+ "----------------------------------------------------\n");
		});

		addO.addActionListener(e -> {
			if (fm == null)
				return;
			addSubFeature("o");
		});

		addOr.addActionListener(e -> {
			if (fm == null)
				return;
			addSubFeature("or");
		});

		addXor.addActionListener(e -> {
			if (fm == null)
				return;
			addSubFeature("xor");
		});

		button_6.addActionListener(e -> {
			consoleText
					.setText("----------------------------------------------------\n");
		});

		// FINALIZE THE FRAME
		frame.pack();
		frame.setVisible(true);
	}

	void addSubFeature(String c) {
		JTextField newF = new JTextField(5);
		JTextField parentF = new JTextField(5);
		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("new Feature Name:"));
		myPanel.add(newF);
		myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		myPanel.add(new JLabel("Parent Feature:"));
		myPanel.add(parentF);
		int result = JOptionPane.showConfirmDialog(null, myPanel,
				"Please Enter new feature name and parent feature",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			if (fm.get(parentF.getText()) != null
					&& fm.get(newF.getText()) == null) {
				switch (c) {
				case "m":
					fm.addMandatorySub(newF.getText(), parentF.getText());
					break;
				case "o":
					fm.addOptionalSub(newF.getText(), parentF.getText());
					break;
				case "or":
					fm.addOrSub(newF.getText(), parentF.getText());
					break;
				case "xor":
					fm.addXorSub(newF.getText(), parentF.getText());
					break;
				}
				consoleText
						.setText("Added new feature\n----------------------------------------------------\n");
				fm.printModel(fm.get(fm.getRootName()), "root", consoleText);
				consoleText
						.setText(consoleText.getText()
								+ "----------------------------------------------------\n");

			}
		}
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				new mainframe();

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}