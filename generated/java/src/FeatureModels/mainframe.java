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

		JButton addReq = new JButton("Add Requires");
		buttonsPane.add(addReq);

		JButton addExc = new JButton("Add Excludes");
		buttonsPane.add(addExc);

		JButton remReq = new JButton("Remove Requires");
		buttonsPane.add(remReq);

		JButton remExc = new JButton("Remove Excludes");
		buttonsPane.add(remExc);

		JButton gen = new JButton("Gen Configurations");
		buttonsPane.add(gen);

		JButton makeConf = new JButton("Make Config");
		buttonsPane.add(makeConf);

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
			fm.printConstraints(fm.get(fm.getRootName()), consoleText);
			consoleText.setText("\n"+consoleText.getText()
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

		addReq.addActionListener(e -> {
			if (fm == null)
				return;
			addRequiresExcludes("requires");
		});
		
		addExc.addActionListener(e -> {
			if (fm == null)
				return;
			addRequiresExcludes("excludes");
		});

		remReq.addActionListener(e -> {
			if (fm == null)
				return;
			addRequiresExcludes("remrequires");
		});
		
		remExc.addActionListener(e -> {
			if (fm == null)
				return;
			addRequiresExcludes("remexcludes");
		});
		
		gen.addActionListener(e -> {
			if (fm == null)
				return;
			fm.generateValidConfigs();
			fm.printAllConfigurations();
			consoleText
			.setText("Printing Model\n----------------------------------------------------\n");
			fm.printModel(fm.get(fm.getRootName()), "root", consoleText);
			fm.printConstraints(fm.get(fm.getRootName()), consoleText);
			consoleText.setText("\n"+consoleText.getText()
			+ "----------------------------------------------------\n");
			fm.javaPrintAllConfigurations(consoleText);
		});
		
		makeConf.addActionListener(e -> {
			if (fm == null)
				return;
			addRequiresExcludes("requires");
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
				fm.printConstraints(fm.get(fm.getRootName()), consoleText);
				consoleText
						.setText("\n"+consoleText.getText()
								+ "----------------------------------------------------\n");

			}
		}
	}
	
	void addRequiresExcludes(String c) 
	{
		JTextField newF = new JTextField(5);
		JTextField parentF = new JTextField(5);
		JPanel myPanel = new JPanel();
		if(c=="requires" || c=="remrequires"){
			
			myPanel.add(new JLabel("Requirer:"));
			myPanel.add(parentF);
			myPanel.add(Box.createHorizontalStrut(15)); // a spacer
			myPanel.add(new JLabel("Requiree:"));
			myPanel.add(newF);	
		}
		else {
			myPanel.add(new JLabel("Excluder:"));
			myPanel.add(parentF);
			myPanel.add(Box.createHorizontalStrut(15)); // a spacer
			myPanel.add(new JLabel("Excludee:"));
			myPanel.add(newF);		
		}
		
		
		int result = JOptionPane.showConfirmDialog(null, myPanel,
				"Require/Exclude",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			if (fm.get(parentF.getText()) != null
					&& fm.get(newF.getText()) != null) {
				switch (c) {
				case "requires":
					fm.requires(newF.getText(), parentF.getText());
					break;
				case "excludes":
					fm.excludes(newF.getText(), parentF.getText());
					break;
				case "remrequires":
					fm.removeRequires(newF.getText(), parentF.getText());
					break;
				case "remexcludes":
					fm.removeExcludes(newF.getText(), parentF.getText());
					break;
				}
				consoleText
						.setText("Printing Updated Model\n----------------------------------------------------\n");
				fm.printModel(fm.get(fm.getRootName()), "root", consoleText);
				fm.printConstraints(fm.get(fm.getRootName()), consoleText);
				consoleText
						.setText("\n"+consoleText.getText()
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