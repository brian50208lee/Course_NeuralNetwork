package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import network.bpn.*;
import network.data_struct.Patten;
import network.data_struct.PattenSet;
import network.observer.BPNSubject;
import network.view.GUI;
public class TestBPN {
	
	private static String inputFileName = "./hw1data.dat";
	private static int[] networkDeclare = new int[]{2,8,6,1};
	private static int interation = 1000;
	
	public static void main(String args[]) throws InterruptedException  {
		/* args to filename*/
		if (args.length > 0) {inputFileName = args[0];}
		if (args.length > 1) {
			String declare[] = args[1].split(",");
			networkDeclare = new int[declare.length];
			for(int i = 0 ; i < declare.length ;i++){
				networkDeclare[i]=Integer.parseInt(declare[i]);
			}
		}
		if (args.length > 2) {interation = Integer.parseInt(args[2]);}
		
		/* read training data */
		PattenSet pattenSet = readPatten(inputFileName);
		
		/* initial network */
		BPN bpn = new BPN(networkDeclare, 0.5);
		bpn.setSubject(new BPNSubject());
		
		/* set weight */
		
		/*
		bpn.setWeight(1, 0, new double[]{-0.00,5.50,-10.00});
		bpn.setWeight(1, 1, new double[]{4.25,-0.03,-7.54});
		bpn.setWeight(1, 2, new double[]{4.05,0.00,-15.73});
		bpn.setWeight(1, 3, new double[]{0.01,4.10,-16.06});
		
		bpn.setWeight(2, 0, new double[]{10.78000,0.94760,10.00843,11.59637,-15.37324});
		bpn.setWeight(2, 1, new double[]{5.43259,11.55113,-3.48058,6.71319,-16.78150});
		bpn.setWeight(2, 2, new double[]{5.68836,-9.41941,8.70487,8.60202,4.75489});
		
		bpn.setWeight(3, 0, new double[]{-17.06591,17.35151,14.37711,-7.09429});
		*/
		
		
		/* View for 2D data */
		if (networkDeclare[0] == 2 ) {
			GUI gui = new GUI(bpn.getSubject());
			gui.drawPatten(pattenSet);
		}
		
		/* start training */
		System.out.println("Trainning ...");
		for (int i = 0; i < interation; i++) {
			bpn.train(pattenSet);
			/* slow motion */
			//Thread.sleep(1000);
		}
		System.out.println("Done traning");
		bpn.printErrorRecord();
		bpn.printWeight();
		bpn.printHiddenTree(pattenSet);
		
		/* test */
		System.out.println("Test ...");
		/*
		bpn.test(new double[]{0,0,0});
		bpn.test(new double[]{0,0,1});
		bpn.test(new double[]{0,1,0});
		bpn.test(new double[]{0,1,1});
		bpn.test(new double[]{1,0,0});
		bpn.test(new double[]{1,0,1});
		bpn.test(new double[]{1,1,0});
		bpn.test(new double[]{1,1,1});
		*/
		
		
		/*
		bpn.printOutputPath(new double[]{0.2,0.2});
		bpn.printOutputPath(new double[]{0.5,0.2});
		bpn.printOutputPath(new double[]{0.8,0.2});
		bpn.printOutputPath(new double[]{0.2,0.5});
		bpn.printOutputPath(new double[]{0.5,0.5});
		bpn.printOutputPath(new double[]{0.8,0.5});
		bpn.printOutputPath(new double[]{0.2,0.8});
		bpn.printOutputPath(new double[]{0.5,0.8});
		bpn.printOutputPath(new double[]{0.8,0.8});
		*/
		/*
		bpn.printOutputPath(new double[]{1,1});
		bpn.printOutputPath(new double[]{3,1});
		bpn.printOutputPath(new double[]{5,1});
		bpn.printOutputPath(new double[]{1,3});
		bpn.printOutputPath(new double[]{3,3});
		bpn.printOutputPath(new double[]{5,3});
		bpn.printOutputPath(new double[]{1,5});
		bpn.printOutputPath(new double[]{3,5});
		bpn.printOutputPath(new double[]{5,5});
		*/
		System.out.println("done");
	}
	
	
	
	
	
	
	
	public static PattenSet readPatten(String inputFileName) {
		PattenSet tPattenSet = new PattenSet();
		BufferedReader br = null;
		
		try {
			br = br = new BufferedReader(new FileReader(new File(inputFileName)));
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String line="";
		try {
			while ((line = br.readLine())!= null) {
				/* change to double tyoe */
				String strPT[] = line.split("\\s+");
				double dbPT[] = new double[strPT.length];
				for (int i = 0; i < dbPT.length; i++) {
					dbPT[i] = Double.parseDouble(strPT[i]);
				}
				
				/* normalize target value to 1 and 0 */
				for (int i = 0; i < networkDeclare[networkDeclare.length-1]; i++) {
					dbPT[dbPT.length-1 - i] =dbPT[dbPT.length-1 - i] > 0 ? 1: 0;
				}
				tPattenSet.add(new Patten(networkDeclare[0] , dbPT));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tPattenSet;
	}
}
