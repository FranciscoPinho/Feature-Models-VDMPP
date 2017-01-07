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
    
  
    /**
     * Instantiates a new mainframe and initializes all the containers as well as their needed listeners.
     */
    mainframe() {
    	FeatureModel fm = new FeatureModel("root");
		fm.addMandatorySub("0", "root");
		fm.addXorSub("1", "root");
		fm.addXorSub("2", "root");
		fm.addOrSub("3", "root");
		fm.addOrSub("4", "root");
		fm.addOptionalSub("5", "root");
		fm.addOptionalSub("6", "5");
		fm.addXorSub("7", "0");
		fm.addXorSub("8", "0");
    	
        final JFrame frame = new JFrame();
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(1000, 400));
        frame.setPreferredSize(new Dimension(1300, 800));
        frame.setTitle("Feature Tree Model Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //FRAME CONTENT PANE
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        //CONTENT PANELS
        JPanel console = new JPanel();
        contentPane.add(console, BorderLayout.CENTER);
        console.setLayout(new BorderLayout(0, 0));
        
        JPanel optionsPane = new JPanel();
        console.add(optionsPane, BorderLayout.NORTH);
        optionsPane.setLayout(new BorderLayout(0, 0));


        JPanel buttonsPane = new JPanel();
        optionsPane.add(buttonsPane, BorderLayout.EAST);

        JButton button = new JButton("New Model");
        buttonsPane.add(button);

        JButton button_1 = new JButton("Add Mandatory Feature");
        buttonsPane.add(button_1);

        JButton button_2 = new JButton("Print Model");
        buttonsPane.add(button_2);

      
     

        consoleText = new JTextArea();
        consoleText.setTabSize(2);
        consoleText.setEditable(false);
        consoleText.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

        JScrollPane consoleScroll = new JScrollPane(consoleText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        consoleScroll.setPreferredSize(new Dimension(300, 600));
        console.add(consoleScroll);

        button.addActionListener(e -> {
        	  consoleText.setText("----------------------------------------------------\n"); 
        });

        button_1.addActionListener(e -> {
        	  consoleText.setText("----------------------------------------------------\n");
        });

        button_2.addActionListener(e -> {
        	consoleText.setText("Printing Model\n----------------------------------------------------\n");
        	fm.printModel(fm.get(fm.getRootName()),"root",consoleText);
        	consoleText.setText(consoleText.getText()+"----------------------------------------------------\n");
        });

        //FINALIZE THE FRAME
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * The main method.
     *
     * @param args the arguments
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