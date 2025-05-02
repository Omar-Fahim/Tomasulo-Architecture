package micro;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.*;

import java.util.HashMap;

public class main {
	static ReservationStation[] addSubRes = null;
	static ReservationStation[] mulDivRes = null;
	static ReservationStation[] loadRes = null;
	static ReservationStation[] storeRes = null;

	static double[] cache = new double[100];

	static RegisterI[] registerFieldsI = null;
	static RegisterF[] registerFieldsF = null;
	static ArrayList<Instruction> instructionQueue = new ArrayList<Instruction>();
	static int PC = 0;
	static Bus bus = new Bus();
	static String stallRes = "";

	// static HashMap<String, Integer> LatencyForInstructions = new HashMap<>();
	static int addLatency;
	static int subLatency;
	static int mulLatency;
	static int divLatency;
	static int storeLatency;
	static int loadLatency;

	static boolean stallBranch = false;

	static boolean stallPc = false;

	static Queue<Pair<String, Double>> toBeWrittenOnBus = new LinkedList<Pair<String, Double>>();

	public static void readFile(String program) throws FileNotFoundException, IOException {

		BufferedReader reader = new BufferedReader(new FileReader(program));
		String line = "";

		String[] input;
		while ((line = reader.readLine()) != null) {
			input = line.split(" ");

			convertToInstruction(input);
			// System.out.println(Arrays.toString(input));
		}
	}

	public static boolean checkAllResAreEmptyAndPC() {

		for (int i = 0; i < addSubRes.length; i++) {

			if (addSubRes[i].busy == true) {
				return false;
			}

		}

		for (int i = 0; i < mulDivRes.length; i++) {

			if (mulDivRes[i].busy == true) {
				return false;
			}

		}
		for (int i = 0; i < loadRes.length; i++) {

			if (loadRes[i].busy == true) {
				return false;
			}

		}

		for (int i = 0; i < storeRes.length; i++) {

			if (storeRes[i].busy == true) {
				return false;
			}

		}

		return true && PC >= instructionQueue.size();

	}

	public static void convertToInstruction(String[] input) {
		// TODO Auto-generated method stub
		if(input[0].equals("LOOP")|| input[0].equals("LABEL")) {
			String [] newArray = new String[input.length-1];
			for(int i = 0;i<newArray.length;i++) {
				newArray[i] = input[i+1];
			}
			input= newArray;
			
		}
			
		Instruction i = new Instruction();
		i.name = input[0];
		i.destination = input[1];
		
		// if(i.name =="BNEZ")
		// System.out.println("Name: "+i.name+" "+" Destination: "+input[1]);

		switch (i.name) {
			case "ADDI":
			case "SUBI":
				// case "ADDI.D":
				// case "SUBI.D":

				i.sourceOne = input[2];
				i.imm = Double.parseDouble(input[3]);
				break;

			// case "LD":
			// case "SD":
			case "L.D":
			case "S.D":
				// case "BNEZ":
				i.imm = Double.parseDouble(input[2]);
				break;

			case "DADD":
				// case "SUB":
			case "ADD.D":
			case "SUB.D":
				// case "MUL":
				// case "DIV":
			case "MUL.D":
			case "DIV.D":
				i.sourceOne = input[2];
				i.sourceTwo = input[3];
				break;

		}

		if (i.name.equals("SD") || i.name.equals("S.D")) {
			i.sourceOne = input[1];
			i.destination = "";
			i.sourceTwo = "-3";
		}

		instructionQueue.add(i);

	}

	public static void setLatencyForReady() {
		for (int i = 0; i < addSubRes.length; i++) {
			if (addSubRes[i].busy == true && addSubRes[i].time == -1 && addSubRes[i].qj.equals("0")
					&& addSubRes[i].qk.equals("0")) {
				 if (addSubRes[i].operation.equals("ADDI") ||addSubRes[i].operation.equals("SUBI") ||addSubRes[i].operation.equals("BNEZ")) 
					addSubRes[i].time = 1;
				else if (addSubRes[i].operation.charAt(0) == 'A' || addSubRes[i].operation.charAt(0) == 'D') {
					addSubRes[i].time = addLatency;
				}  
				else
					addSubRes[i].time = subLatency;
			}
		}

		for (int i = 0; i < mulDivRes.length; i++) {
			if (mulDivRes[i].busy == true && mulDivRes[i].time == -1 && mulDivRes[i].qj.equals("0")
					&& mulDivRes[i].qk.equals("0")) {
				if (mulDivRes[i].operation.charAt(0) == 'M') {
					mulDivRes[i].time = mulLatency;
				} else
					mulDivRes[i].time = divLatency;
			}
		}

		for (int i = 0; i < loadRes.length; i++) {
			if (loadRes[i].busy == true && loadRes[i].time == -1) {
				loadRes[i].time = loadLatency;
			}
		}

		for (int i = 0; i < storeRes.length; i++) {
			if (storeRes[i].busy == true && storeRes[i].time == -1 && storeRes[i].qj.equals("0")) {
				storeRes[i].time = storeLatency;
			}
		}
	}

	public static void addToResStation(ReservationStation[] res, int i) {

		Instruction ins = instructionQueue.get(PC);

		if (ins.name.equals("LD") || ins.name.equals("L.D")) {

			res[i].busy = true;
			res[i].address = ins.imm.intValue();
			res[i].operation = ins.name;
			if (ins.destination.charAt(0) == 'R') {

				for (int j = 0; j < registerFieldsI.length; j++) {
					if (registerFieldsI[j].name.equals(ins.destination)) {

						registerFieldsI[j].q = res[i].tag;

					}
				}

			} else {

				for (int j = 0; j < registerFieldsF.length; j++) {
					if (registerFieldsF[j].name.equals(ins.destination)) {

						registerFieldsF[j].q = res[i].tag;

					}
				}

			}
		} else if (ins.name.equals("SD") || ins.name.equals("S.D")) {
			res[i].busy = true;
			res[i].address = ins.imm.intValue();
			res[i].operation = ins.name;
			

			if (ins.sourceOne.charAt(0) == 'R') {

				for (int j = 0; j < registerFieldsI.length; j++) {
					if (registerFieldsI[j].name.equals(ins.sourceOne)) {

						res[i].vj = registerFieldsI[j].value;
						res[i].qj = registerFieldsI[j].q;
						// registerFieldsI[j].q = res[i].tag;

					}
				}
			} else {

				for (int j = 0; j < registerFieldsF.length; j++) {
					if (registerFieldsF[j].name.equals(ins.sourceOne)) {

						res[i].vj = registerFieldsF[j].value;
						res[i].qj = registerFieldsF[j].q;

					}
				}

			}

			return;

		} else if (ins.name.equals("BNEZ")) {
			res[i].operation = ins.name;
			res[i].busy = true;

			// res[i]. = ins.destination;
			for (int j = 0; j < registerFieldsI.length; j++) {

				if (registerFieldsI[j].name.equals(ins.destination)) {

					if (registerFieldsI[j].q.equals("0")) {
						res[i].vj = registerFieldsI[j].value;
						res[i].qj = "0";
					} else {
						res[i].vj = registerFieldsI[j].value;
						res[i].qj = registerFieldsI[j].q;
					}

				}
			}
		}

		else {
			// String name;
			// String destination;
			// String sourceOne;
			// String sourceTwo ;
			//
			// boolean busy;
			// String operation ;
			// float vj;
			// float vk;
			// String qj;
			// String qk;
			// int address;

			if (ins.sourceOne.charAt(0) == 'F') {
				for (int j = 0; j < registerFieldsF.length; j++) {

					// System.out.println("Source One: "+ins.sourceTwo);

					if (registerFieldsF[j].name.equals(ins.sourceOne)) {
						// System.out.println("RegField: "+registerFieldsF[j].name+"Source One:
						// "+ins.sourceOne);
						if (registerFieldsF[j].q.equals("0")) {
							res[i].vj = registerFieldsF[j].value;
							res[i].qj = "0";
						} else {
							res[i].vj = registerFieldsF[j].value;
							res[i].qj = registerFieldsF[j].q;
						}

					}
					if (registerFieldsF[j].name.equals(ins.sourceTwo)) {

						if (registerFieldsF[j].q.equals("0")) {
							res[i].vk = registerFieldsF[j].value;
							res[i].qk = "0";
						} else {
							res[i].vk = registerFieldsF[j].value;
							res[i].qk = registerFieldsF[j].q;
						}

					}

					if (registerFieldsF[j].name.equals(ins.destination)) {

						registerFieldsF[j].q = res[i].tag;
					}

				}

			}

			else if (ins.sourceOne.charAt(0) == 'R') {
				for (int j = 0; j < registerFieldsI.length; j++) {

					if (registerFieldsI[j].name.equals(ins.sourceOne)) {

						if (registerFieldsI[j].q.equals("0")) {
							res[i].vj = registerFieldsI[j].value;
							res[i].qj = "0";
						} else {
							res[i].vj = registerFieldsI[j].value;
							res[i].qj = registerFieldsI[j].q;
						}

					}
					if (registerFieldsI[j].name.equals(ins.sourceTwo)) {

						if (registerFieldsI[j].q.equals("0")) {
							res[i].vk = registerFieldsI[j].value;
							res[i].qk = "0";
						} else {
							res[i].vk = registerFieldsI[j].value;
							res[i].qk = registerFieldsI[j].q;
						}

					}

					if (registerFieldsI[j].name.equals(ins.destination)) {

						registerFieldsI[j].q = res[i].tag;
					}

				}

			}

			res[i].busy = true;
			res[i].operation = ins.name;

			if (ins.imm != null) {
				res[i].vk = ins.imm;
				res[i].qk = "0";

			}

		}

	}

	public static boolean isThereFreeRes(ReservationStation[] res) {

		for (int i = 0; i < res.length; i++) {
			System.out.println("Reservation Station Inners: " + res[i]);

			if (res[i].busy == false && !res[i].tag.equals(bus.tag)) {
				
				addToResStation(res, i);
				return true;
				
			}
		}
		return false;
	}

	public static void initializeRegistersInRegisterFile() {

		for (int i = 0; i < registerFieldsI.length; i++) {
			String name = "R" + i;
			registerFieldsI[i] = new RegisterI(name);
		}

		for (int i = 0; i < registerFieldsF.length; i++) {
			String name = "F" + i;
			registerFieldsF[i] = new RegisterF(name);
		}
	}

	public static void issue() {

		String name = instructionQueue.get(PC).name;
        
		switch (name) {

			case "ADDI":
			case "SUBI":
				// case "ADDI.D":
				// case "SUBI.D":
				// case "ADD":
				// case "SUB":
			case "ADD.D":
			case "SUB.D":
			case "DADD":
			case "BNEZ":

				if (!isThereFreeRes(addSubRes)) {
					stallPc = true;
					stallRes = "addSubRes";
					
					
					return;
				}
				if (name.equals("BNEZ"))
					stallBranch = true;

				break;

			// case "LD":
			case "L.D":

				if (!isThereFreeRes(loadRes)) {
					stallPc = true;
					stallRes = "loadRes";
					return;
				}
				break;

			// case "SD":
			case "S.D":

				if (!isThereFreeRes(storeRes)) {
					stallPc = true;
					stallRes = "storeRes";
					return;
				}
				break;
			// case "MUL":
			// case "DIV":
			case "MUL.D":
			case "DIV.D":
				if (!isThereFreeRes(mulDivRes)) {
					stallPc = true;
					stallRes = "mulDivRes";
					return;
				}
				break;

		}
		// stallPc = false;

	}

	public static void printTableI(RegisterI[] data) {
		try (PrintWriter out = new PrintWriter(new FileWriter("output.txt", true))) {
			// Print table headers
			out.printf("%-15s%-5s%-10s\n", "Name", "Q", "Value");

			// Print table data
			for (RegisterI register : data) {
				out.println(register);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printTableF(RegisterF[] data) {
		try (PrintWriter out = new PrintWriter(new FileWriter("output.txt", true))) {
			// Print table headers
			out.printf("%-15s%-5s%-10s\n", "Name", "Q", "Value");

			// Print table data
			for (RegisterF register : data) {
				out.println(register);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printResA(ReservationStation[] Res) {
		try (PrintWriter out = new PrintWriter(new FileWriter("output.txt", true))) {
			out.println(ReservationStation.getTableHeaderA());

			for (ReservationStation station : Res) {
				out.print(station.toTableRowA());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printResL(ReservationStation[] Res) {
		try (PrintWriter out = new PrintWriter(new FileWriter("output.txt", true))) {
			out.println(ReservationStation.getTableHeaderL());

			for (ReservationStation station : Res) {
				out.print(station.toTableRowL());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printResS(ReservationStation[] Res) {
		try (PrintWriter out = new PrintWriter(new FileWriter("output.txt", true))) {
			out.println(ReservationStation.getTableHeaderS());

			for (ReservationStation station : Res) {
				out.print(station.toTableRowS());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setLatency() {

		System.out.println("please enter the latency for add");
		Scanner sc = new Scanner(System.in);
		addLatency = sc.nextInt();

		System.out.println("please enter the latency for sub");
		subLatency = sc.nextInt();

		System.out.println("please enter the latency for mul");
		mulLatency = sc.nextInt();

		System.out.println("please enter the latency for Div");
		divLatency = sc.nextInt();

		System.out.println("please enter the latency for Store");
		storeLatency = sc.nextInt();

		System.out.println("please enter the latency for Load");
		loadLatency = sc.nextInt();

	}

	public static void execute() {
		for (int i = 0; i < addSubRes.length; i++) {
			if (addSubRes[i].busy == true && addSubRes[i].time > 0 && addSubRes[i].qj.equals("0")
					&& addSubRes[i].qk.equals("0")) {
				addSubRes[i].time -= 1;
				if (addSubRes[i].time == 0)
					executeADD_SUBFunctionality(addSubRes[i]);
			}
		}

		for (int i = 0; i < mulDivRes.length; i++) {
			if (mulDivRes[i].busy == true && mulDivRes[i].time > 0 && mulDivRes[i].qj.equals("0")
					&& mulDivRes[i].qk.equals("0")) {
				mulDivRes[i].time -= 1;
				if (mulDivRes[i].time == 0)
					executeMUL_DIVFunctionality(mulDivRes[i]);
			}
		}

		for (int i = 0; i < loadRes.length; i++) {
			if (loadRes[i].busy == true && loadRes[i].time > 0) {
				loadRes[i].time -= 1;
				if (loadRes[i].time == 0)
					executeLoadFunctionality(loadRes[i]);
			}
		}

		for (int i = 0; i < storeRes.length; i++) {
			if (storeRes[i].busy == true && storeRes[i].time > 0) {
				storeRes[i].time -= 1;
				if (storeRes[i].time == 0)
					executeStoreFunctionality(storeRes[i]);
			}
		}

	}

	public static void executeADD_SUBFunctionality(ReservationStation Res) {
		// TODO Auto-generated method stub
		String op = Res.operation;
		if (op.charAt(0) == 'B') {
			System.out.println("vj: " + Res.vj);
			// if((int)(Res.vj)!=0) {
			// PC = 0;
			// stallBranch = false;
			// }
			// Pair p= new Pair(Res.tag,0);
			// toBeWrittenOnBus.add(p);
		} else {
			System.out.println("Execute ADD_SUB");

			double result = 0;
			if (op.charAt(0) == 'A' || op.charAt(0) == 'D') {
				result = Res.vj + Res.vk;
			} else {
				result = Res.vj - Res.vk;

			}
			Pair p = new Pair(Res.tag, result);
			toBeWrittenOnBus.add(p);
		}
	}

	public static void executeMUL_DIVFunctionality(ReservationStation Res) {
		System.out.println("Execute MUL_DIV");

		String op = Res.operation;

		double result = 1;
		if (op.charAt(0) == 'M') {
			result = Res.vj * Res.vk;
		} else {
			result = Res.vj / Res.vk;

		}
		Pair p = new Pair(Res.tag, result);
		toBeWrittenOnBus.add(p);

	}

	public static void executeLoadFunctionality(ReservationStation res) {
		// System.out.println("Execute Load");
		int effectiveAddress = res.address;
		double value = cache[effectiveAddress];
		Pair p = new Pair(res.tag, value);
		toBeWrittenOnBus.add(p);
	}

	public static void executeStoreFunctionality(ReservationStation res) {
		int effectiveAddress = res.address;
		cache[effectiveAddress] = res.vj;
		// Pair p= new Pair(res.tag,res.vj);
		// toBeWrittenOnBus.add(p);
		// System.out.println("Execute Store");
	}

	public static void fillMem() {
		for (int i = 0; i < cache.length; i++) {
			cache[i] = i ;
		}
	}

	public static void writeBack() {
		if (toBeWrittenOnBus.size() > 0) {
			Pair p = toBeWrittenOnBus.poll();
			bus.tag = p.tagB;
			bus.value = p.valueB;

		}

		for (int i = 0; i < addSubRes.length; i++) {
			if (addSubRes[i].tag == bus.tag) {

				addSubRes[i].busy = false;

				addSubRes[i].time = -1;

			}
			if (addSubRes[i].qj.equals(bus.tag)) {
				addSubRes[i].qj = "0";
				addSubRes[i].vj = bus.value;
			}
			if (addSubRes[i].qk.equals(bus.tag)) {
				addSubRes[i].qk = "0";
				addSubRes[i].vk = bus.value;
			}
			if (addSubRes[i].operation.equals("BNEZ") && addSubRes[i].time == 0) {
				addSubRes[i].busy = false;

				if ((int) (addSubRes[i].vj) != 0) {

					PC = 0;
					
				}
				else {
					PC++;
				}
				stallBranch = false;
				addSubRes[i].time = -1;
			}

		}

		for (int i = 0; i < mulDivRes.length; i++) {
			if (mulDivRes[i].tag == bus.tag) {
				mulDivRes[i].busy = false;
				mulDivRes[i].time = -1;
			}
			if (mulDivRes[i].qj.equals(bus.tag)) {
				mulDivRes[i].qj = "0";
				mulDivRes[i].vj = bus.value;
			}
			if (mulDivRes[i].qk.equals(bus.tag)) {
				mulDivRes[i].qk = "0";
				mulDivRes[i].vk = bus.value;
			}
		}

		for (int i = 0; i < loadRes.length; i++) {
			if (loadRes[i].tag == bus.tag) {
				loadRes[i].busy = false;
				loadRes[i].time = -1;
			}
		}

		for (int i = 0; i < storeRes.length; i++) {
			
		
				
			if (storeRes[i].tag == bus.tag || storeRes[i].time == 0) {
				storeRes[i].busy = false;
				storeRes[i].time = -1;
			}
			
			if (storeRes[i].qj.equals(bus.tag)) {
				storeRes[i].qj = "0";
				storeRes[i].vj = bus.value;
			}
		}

		for (int i = 0; i < registerFieldsI.length; i++) {
			if (registerFieldsI[i].q.equals(bus.tag)) {
				registerFieldsI[i].q = "0";
				registerFieldsI[i].value = (int) bus.value;
			}
		}
		for (int i = 0; i < registerFieldsF.length; i++) {
			if (registerFieldsF[i].q.equals(bus.tag)) {
				registerFieldsF[i].q = "0";
				registerFieldsF[i].value = bus.value;
			}
		}

	}

	public static void printMem() {
		for(int i =0;i<cache.length;i++) {
			
			try (PrintWriter out = new PrintWriter(new FileWriter("output.txt", true))) {
				out.println("M["+i+"] = "+cache[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			}
		}
	
	public static void PrintInstructionQueue() {
		for(int i =0;i<instructionQueue.size();i++) {
			
			try (PrintWriter out = new PrintWriter(new FileWriter("output.txt", true))) {
				out.println("Instruction: "+i+" "+instructionQueue.get(i));
			} catch (IOException e) {
				e.printStackTrace();
			}
			}
		}
	public static void main(String[] args) {

		fillMem();

		System.out.println("please enter the numbe of reservation stations for add/sub");
		Scanner sc = new Scanner(System.in);
		int numberOfAddSubResSta = sc.nextInt();
		addSubRes = new ReservationStation[numberOfAddSubResSta];
		for (int i = 0; i < numberOfAddSubResSta; i++) {
			addSubRes[i] = new ReservationStation("A" + i);
		}

		System.out.println("please enter the numbe of reservation stations for mul/div");
		int number1 = sc.nextInt();
		mulDivRes = new ReservationStation[number1];
		for (int i = 0; i < number1; i++) {
			mulDivRes[i] = new ReservationStation("M" + i);
		}

		System.out.println("please enter the numbe of reservation stations for load");
		int number2 = sc.nextInt();
		loadRes = new ReservationStation[number2];
		for (int i = 0; i < number2; i++) {
			loadRes[i] = new ReservationStation("L" + i);
		}

		System.out.println("please enter the numbe of reservation stations for store");
		int number3 = sc.nextInt();
		storeRes = new ReservationStation[number3];
		for (int i = 0; i < number3; i++) {
			storeRes[i] = new ReservationStation("S" + i);
		
		}

		setLatency();

		try {
			readFile("program.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(instructionQueue.toString());

		registerFieldsI = new RegisterI[32];
		registerFieldsF = new RegisterF[32];
		initializeRegistersInRegisterFile();

		// printTableF(registerFieldsF);

		// System.out.println(addSubRes.length);
		// System.out.println(mulDivRes.length);

		// (!checkAllResAreEmptyAndPC())

		// registerFieldsI[6].value = 3;
		//
		// registerFieldsI[2].value = 30;
		// registerFieldsI[2].q = "M1";
		for (int i = 0; i < registerFieldsI.length; i++) {
			registerFieldsI[i].value = i;
			registerFieldsF[i].value = i ;
		}
		
		
		

		printTableI(registerFieldsI);
		printTableF(registerFieldsF);
		// (!checkAllResAreEmptyAndPC())
		for (int i = 1; (!checkAllResAreEmptyAndPC()); i++) {

			System.out.println("R1: " + registerFieldsI[1].value);
			writeBack();

			execute();

			try (PrintWriter out = new PrintWriter(new FileWriter("output.txt", true))) {
				out.println("PC: " + PC);
				out.println("Clock Cycle: " + i);
				out.println("stallPc: "+stallPc +" stallBranch: "+stallBranch);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			if(PC == 5 && stallBranch == true ) {
				System.out.println("StallBranch: "+stallBranch);
				System.out.println("StallPCM: "+stallPc);
			}
			
			
			
			if (PC != instructionQueue.size() && !stallBranch)
				issue();

			setLatencyForReady();

			// if(!stallPc)
			
			
			if (PC < instructionQueue.size() && !stallPc && !stallBranch)
				PC++;

			stallPc = false;

			printTableI(registerFieldsI);
			printTableF(registerFieldsF);
			printResA(addSubRes);
			printResA(mulDivRes);
			printResL(loadRes);
			printResS(storeRes);
			printMem();
			PrintInstructionQueue();
			// printRes(addSubRes);
			// printResS(storeRes);

			bus.tag = "omar";
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

		System.out.println(toBeWrittenOnBus.toString());

		// printTableI(registerFieldsI);
		// printTableF(registerFieldsF);
		//
		// System.out.println(cache[50]);

	}

}

