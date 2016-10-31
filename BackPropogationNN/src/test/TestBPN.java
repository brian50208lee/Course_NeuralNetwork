package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import network.bpn.*;
import network.data_struct.Patten;
import network.data_struct.PattenSet;
import network.observer.BPNSubject;
import network.view.GUI;
public class TestBPN {
	
	public static String inputFileName = "./hw1data.dat";
	public static int dataDimension = 2;
	public static int targetDimension = 1;
	public static void main(String args[]) throws InterruptedException  {
		/* args to filename*/
		if (args.length > 0) {inputFileName = args[0];}
		
		/* read training data */
		PattenSet pattenSet = readPatten(inputFileName);
		
		/* initial network */
		BPN bpn = new BPN(4, new int[]{dataDimension,8,6,targetDimension}, 0.5);
		bpn.setSubject(new BPNSubject());
		
		/* View for 2D data */
		if (dataDimension == 2 ) {
			GUI gui = new GUI(bpn.getSubject());
			gui.drawPatten(pattenSet);
		}
		
		/* start training */
		System.out.println("Trainning ...");
		for (int i = 0; i < 1000; i++) {
			bpn.train(pattenSet);
			/* slow motion */
			//Thread.sleep(50);
		}
		System.out.println("Done traning");
		
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
		
		
		bpn.test(new double[]{0.2,0.2});
		bpn.test(new double[]{0.5,0.2});
		bpn.test(new double[]{0.8,0.2});
		bpn.test(new double[]{0.2,0.5});
		bpn.test(new double[]{0.5,0.5});
		bpn.test(new double[]{0.8,0.5});
		bpn.test(new double[]{0.2,0.8});
		bpn.test(new double[]{0.5,0.8});
		bpn.test(new double[]{0.8,0.8});
		
		
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
				/* format */
				String strPT[] = line.split("\\s+");
				double dbPT[] = new double[strPT.length];
				for (int i = 0; i < dbPT.length; i++) {
					dbPT[i] = Double.parseDouble(strPT[i]);
				}
				/* normalize to 1 and 0 */
				dbPT[dbPT.length-1] =dbPT[dbPT.length-1] > 0 ? 1: 0;
				tPattenSet.add(new Patten(dataDimension , dbPT));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tPattenSet;
	}
}
