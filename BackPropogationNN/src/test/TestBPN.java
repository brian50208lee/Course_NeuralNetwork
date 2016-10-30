package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import network.bpn.*;
import network.data_struct.Patten;
import network.data_struct.PattenSet;
import network.observer.BPNSubject;
import network.view.GUI;
public class TestBPN {
	
	public static String inputFileName = "./hw1data.dat";
	
	public static void main(String args[]) throws Exception {
		
		/* read training data */
		PattenSet pattenSet = readPatten(inputFileName);

		
		
		/* initial network */
		BPN bpn = new BPN(2, 5, 1, 0.5);
		bpn.setSubject(new BPNSubject());
		GUI gui = new GUI(bpn.getSubject());
		
		
		
		/* draw tranning data */
		gui.drawPatten(pattenSet);
		
		
		
		/* start training */
		System.out.println("Trainning ...");
		for (int i = 0; i < 10000; i++) {
			
			bpn.train(pattenSet);
			/* slow motion */
			//Thread.sleep(50);
		}
		System.out.println("Done traning");
		
		
		
		/* test */
		System.out.println("Test ...");
		System.out.printf("{0.2,0.2}  ->\t%f\n",bpn.test(new double[]{0.2,0.2}));
		System.out.printf("{0.5,0.2}  ->\t%f\n",bpn.test(new double[]{0.5,0.2}));
		System.out.printf("{0.8,0.2}  ->\t%f\n",bpn.test(new double[]{0.8,0.2}));
		System.out.printf("{0.2,0.5}  ->\t%f\n",bpn.test(new double[]{0.2,0.5}));
		System.out.printf("{0.5,0.5}  ->\t%f\n",bpn.test(new double[]{0.5,0.5}));
		System.out.printf("{0.8,0.5}  ->\t%f\n",bpn.test(new double[]{0.8,0.5}));
		System.out.printf("{0.2,0.8}  ->\t%f\n",bpn.test(new double[]{0.2,0.8}));
		System.out.printf("{0.5,0.8}  ->\t%f\n",bpn.test(new double[]{0.5,0.8}));
		System.out.printf("{0.8,0.8}  ->\t%f\n",bpn.test(new double[]{0.8,0.8}));

	}
	
	
	
	
	
	
	
	public static PattenSet readPatten(String inputFileName) throws Exception{
		
		PattenSet tPattenSet = new PattenSet();
		BufferedReader br = br = new BufferedReader(new FileReader(new File(inputFileName)));

		String line="";
			while ((line = br.readLine())!= null) {
				String strPT[] = line.split("\\s+");
				double dbPT[] = new double[strPT.length];
				for (int i = 0; i < dbPT.length; i++) {
					dbPT[i] = Double.parseDouble(strPT[i]);
				}
				dbPT[dbPT.length-1] =dbPT[dbPT.length-1] <0 ? 0: 1;
				tPattenSet.add(new Patten(dbPT));
				//System.out.printf("%f,%f,%f\n",dbPT[0],dbPT[1],dbPT[2]);
			}
		return tPattenSet;
	}
}
