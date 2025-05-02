package micro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class Screen extends JFrame{
    public JTextField addSubTextField, mulDivTextField, loadTextField, storeTextField;
    public JTextField addLatencyField, subLatencyField, mulLatencyField, divLatencyField, storeLatencyField, loadLatencyField;
    public JTextArea instructionsTextArea; // Changed to JTextArea
    public JTextArea resultsTextArea;

    public Screen() {
        // Set the layout manager
        setLayout(new BorderLayout());

        // Panel for input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Reservation Stations Configuration"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(new JLabel("Add/Sub Reservation Stations:"), gbc);
        gbc.gridx = 1;
        addSubTextField = new JTextField();
        addSubTextField.setPreferredSize(new Dimension(150, 25)); // Set preferred size
        inputPanel.add(addSubTextField, gbc);

        // Repeat the same pattern for other text fields

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Mul/Div Reservation Stations:"), gbc);
        gbc.gridx = 1;
        mulDivTextField = new JTextField();
        mulDivTextField.setPreferredSize(new Dimension(150, 25)); // Set preferred size
        inputPanel.add(mulDivTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Load Reservation Stations:"), gbc);
        gbc.gridx = 1;
        loadTextField = new JTextField();
        loadTextField.setPreferredSize(new Dimension(150, 25)); // Set preferred size
        inputPanel.add(loadTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Store Reservation Stations:"), gbc);
        gbc.gridx = 1;
        storeTextField = new JTextField();
        storeTextField.setPreferredSize(new Dimension(150, 25)); // Set preferred size
        inputPanel.add(storeTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Latency for Add:"), gbc);
        gbc.gridx = 1;
        addLatencyField = new JTextField();
        addLatencyField.setPreferredSize(new Dimension(150, 25)); // Set preferred size
        inputPanel.add(addLatencyField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(new JLabel("Latency for Sub:"), gbc);
        gbc.gridx = 1;
        subLatencyField = new JTextField();
        subLatencyField.setPreferredSize(new Dimension(150, 25)); // Set preferred size
        inputPanel.add(subLatencyField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        inputPanel.add(new JLabel("Latency for Mul:"), gbc);
        gbc.gridx = 1;
        mulLatencyField = new JTextField();
        mulLatencyField.setPreferredSize(new Dimension(150, 25)); // Set preferred size
        inputPanel.add(mulLatencyField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        inputPanel.add(new JLabel("Latency for Div:"), gbc);
        gbc.gridx = 1;
        divLatencyField = new JTextField();
        divLatencyField.setPreferredSize(new Dimension(150, 25)); // Set preferred size
        inputPanel.add(divLatencyField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        inputPanel.add(new JLabel("Latency for Store:"), gbc);
        gbc.gridx = 1;
        storeLatencyField = new JTextField();
        storeLatencyField.setPreferredSize(new Dimension(150, 25)); // Set preferred size
        inputPanel.add(storeLatencyField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        inputPanel.add(new JLabel("Latency for Load:"), gbc);
        gbc.gridx = 1;
        loadLatencyField = new JTextField();
        loadLatencyField.setPreferredSize(new Dimension(150, 25)); // Set preferred size
        inputPanel.add(loadLatencyField, gbc);


        // Panel for instructions
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        instructionsPanel.setBorder(BorderFactory.createTitledBorder("Enter Instructions"));

        instructionsTextArea = new JTextArea(8, 20); // Set rows and columns
        JScrollPane instructionsScrollPane = new JScrollPane(instructionsTextArea);
        instructionsPanel.add(instructionsScrollPane, BorderLayout.CENTER);

        // Panel for results
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Simulation Results"));

        resultsTextArea = new JTextArea();
        resultsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsTextArea);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel for the run button
        JPanel buttonPanel = new JPanel();
        JButton runButton = new JButton("Run Simulation");
        buttonPanel.add(runButton);

        // Action listener for the run button
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	resultsTextArea.setText("");
                runSimulation();
            }
        });

        // Add panels to the main frame
        add(inputPanel, BorderLayout.WEST);
        add(instructionsPanel, BorderLayout.NORTH);
        add(resultsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set frame properties
        setTitle("Tomasulo Simulator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
        }

    private void runSimulation() {
        // Implement your Tomasulo simulation logic here
        // Use the input values from text fields and instructions from instructionsTextField
        // Update the resultsTextArea with the simulation results
        // For now, let's print a simple message



        main.fillMem();

        System.out.println("please enter the number of reservation stations for add/sub");

        int numberOfAddSubResSta =Integer.parseInt(addSubTextField.getText().trim());
        main.addSubRes = new ReservationStation[numberOfAddSubResSta];
        for (int i = 0; i < numberOfAddSubResSta; i++) {
            main.addSubRes[i] = new ReservationStation("A" + i);
        }

        System.out.println("please enter the number of reservation stations for mul/div");
        int number1 = Integer.parseInt(mulDivTextField.getText().trim());
        main.mulDivRes = new ReservationStation[number1];
        for (int i = 0; i < number1; i++) {
            main.mulDivRes[i] = new ReservationStation("M" + i);
        }

        System.out.println("please enter the number of reservation stations for load");
        int number2 = Integer.parseInt(loadTextField.getText().trim());
        main.loadRes = new ReservationStation[number2];
        for (int i = 0; i < number2; i++) {
            main.loadRes[i] = new ReservationStation("L" + i);
        }

        System.out.println("please enter the numbe of reservation stations for store");
        int number3 = Integer.parseInt(storeTextField.getText().trim());
        main.storeRes = new ReservationStation[number3];
        for (int i = 0; i < number3; i++) {
            main.storeRes[i] = new ReservationStation("S" + i);
        }


        System.out.println("please enter the latency for add");

        main.addLatency =  Integer.parseInt(addLatencyField.getText().trim());

        System.out.println("please enter the latency for sub");
        main.subLatency = Integer.parseInt(subLatencyField.getText().trim());

        System.out.println("please enter the latency for mul");
        main.mulLatency =  Integer.parseInt(mulLatencyField.getText().trim());

        System.out.println("please enter the latency for Div");
        main.divLatency =  Integer.parseInt(divLatencyField.getText().trim());

        System.out.println("please enter the latency for Store");
        main.storeLatency = Integer.parseInt(storeLatencyField.getText().trim());

        System.out.println("please enter the latency for Load");
        main.loadLatency =  Integer.parseInt(loadLatencyField.getText().trim());

            String program = instructionsTextArea.getText();
            String[] lines = program.split("\\n");
            for (String line : lines) {
                String[] input = line.split(" ");
                main.convertToInstruction(input);
                // System.out.println(Arrays.toString(input));
            }




        System.out.println(main.instructionQueue.toString());

        main.registerFieldsI = new RegisterI[32];
        main.registerFieldsF = new RegisterF[32];
        main.initializeRegistersInRegisterFile();

        // printTableF(registerFieldsF);

        // System.out.println(addSubRes.length);
        // System.out.println(mulDivRes.length);

        // (!checkAllResAreEmptyAndPC())

        // registerFieldsI[6].value = 3;
        //
        // registerFieldsI[2].value = 30;
        // registerFieldsI[2].q = "M1";
        for (int i = 0; i < main.registerFieldsI.length; i++) {
            main.registerFieldsI[i].value = i;
            main.registerFieldsF[i].value = i;
        }

    
        printTableI(main.registerFieldsI,resultsTextArea);
         printTableF(main.registerFieldsF,resultsTextArea);
        // (!checkAllResAreEmptyAndPC())
        for (int i = 1; (!main.checkAllResAreEmptyAndPC()); i++) {

            System.out.println("R1: " + main.registerFieldsI[1].value);
            main.writeBack();
            main.execute();
            resultsTextArea.append("PC: " + main.PC + "\n");
            resultsTextArea.append("Clock Cycle: " + i + "\n");
            resultsTextArea.append("Stall PC: "+main.stallPc +" Stall Branch: "+main.stallBranch +"\n");


          
            if (main.PC != main.instructionQueue.size() && !main.stallBranch)
                main.issue();

            main.setLatencyForReady();

            // if(!stallPc)
            if (main.PC < main.instructionQueue.size() && !main.stallPc && !main.stallBranch)
                main.PC++;

            main.stallPc = false;

            printTableI(main.registerFieldsI,resultsTextArea);
            printTableF(main.registerFieldsF,resultsTextArea);


           // main.printTableI(main.registerFieldsI);
         //   main.printTableF(main.registerFieldsF);



            printResA(main.addSubRes,resultsTextArea);
            printResA(main.mulDivRes,resultsTextArea);
            printResL(main.loadRes,resultsTextArea);
            printResS(main.storeRes,resultsTextArea);
            printCache(main.cache,resultsTextArea);
            printInstructionQueue(main.instructionQueue,resultsTextArea);
            System.out.println("Instruction Queue: "+main.instructionQueue);
//            main.printResA(main.addSubRes);
//            main.printResA(main.mulDivRes);
//            main.printResL(main.loadRes);
//            main.printResS(main.storeRes);

            // printRes(addSubRes);
            // printResS(storeRes);

            main.bus.tag = "omar";
        }
        // printTableI(registerFieldsI);

        // System.out.println();
        // printResA(addSubRes);
        // System.out.println();
        // printResA(mulDivRes);
        // System.out.println();
        // printResL(loadRes);
        // System.out.println();
        // printResS(storeRes);

        System.out.println(main.toBeWrittenOnBus.toString());

        // printTableI(registerFieldsI);
        // printTableF(registerFieldsF);
        //
        // System.out.println(cache[50]);
       // resultsTextArea.setText("Simulation results will be displayed here.\n");
    }


    public static void printTableI(RegisterI[] data, JTextArea textArea) {
        // Print table headers
        textArea.append(String.format("%-15s%-5s%-10s\n", "Name", "Q", "Value"));

        // Print table data
        for (RegisterI register : data) {
            textArea.append(register.toString() + "\n");
        }
    }
    public static void printTableF(RegisterF[] data, JTextArea textArea) {
        // Print table headers
        textArea.append(String.format("%-15s%-5s%-10s\n", "Name", "Q", "Value"));

        // Print table data
        for (RegisterF register : data) {
            textArea.append(register.toString() + "\n");
        }
    }
    public static void printResA(ReservationStation[] Res, JTextArea textArea) {
        // Append table headers
        textArea.append(ReservationStation.getTableHeaderA() + "\n");

        // Append table data
        for (ReservationStation station : Res) {
            textArea.append(station.toTableRowA());
        }
    }

    public static void printResL(ReservationStation[] Res, JTextArea textArea) {
        // Append table headers
        textArea.append(ReservationStation.getTableHeaderL() + "\n");

        // Append table data
        for (ReservationStation station : Res) {
            textArea.append(station.toTableRowL());
        }
    }

    public static void printResS(ReservationStation[] Res, JTextArea textArea) {
        // Append table headers
        textArea.append(ReservationStation.getTableHeaderS() + "\n");

        // Append table data
        for (ReservationStation station : Res) {
            textArea.append(station.toTableRowS());
        }
    }
    
    
    
    public static void printCache(double []Memory, JTextArea textArea) {
        // Append table headers
        //textArea.append(ReservationStation.getTableHeaderS() + "\n");

        // Append table data
       for(int i = 0;i<Memory.length;i++) {
    	   textArea.append("M ["+i+"] = "+Memory[i]+"\n");
       }
    }
    
    
    public static void printInstructionQueue(ArrayList<Instruction>Instructions, JTextArea textArea) {
        // Append table headers
        //textArea.append(ReservationStation.getTableHeaderS() + "\n");

        // Append table data
       for(int i = 0;i<Instructions.size();i++) {
    	   textArea.append("Instruction "+i+": "+Instructions.get(i)+"\n");
       }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Screen();
            }
        });
    }
}

