package nn.demo;

import java.io.IOException;

import nn.dataset.PattenSet;
import nn.network.som.SOM;
import nn.view.GUI;
public class DemoSOM {
	
	private static String inputFileName = "hw2.dat";
	private static int[] networkDeclare = new int[]{2,5,5,5,5,5};
	private static int dataDim = 2;
	private static int tagDim = 1;
	private static int interation = 5000;
	private static double learningRateAtt = 0.1;
	private static double learningRateRep = 100;
	
	public static void main(String args[]) throws InterruptedException, NumberFormatException, IOException  {
		/* read training data */
		PattenSet pattenSet = new PattenSet(inputFileName, dataDim, tagDim);
		
		/* initial network */
		SOM som = new SOM(networkDeclare, interation, learningRateAtt, learningRateRep);
		
		/* View for 2D data */
		if (networkDeclare[0] == 2 ) {
			GUI gui = new GUI(som.getSubject());
			gui.drawPatten(pattenSet);
		}
		
		/* start training */
		System.out.println("Trainning ...");
		som.training(pattenSet);
		System.out.println("Done traning");
		
		/* test */
		som.printWeight();
		som.test(new double[]{+1, +0});
		som.test(new double[]{+0, -1});
		som.test(new double[]{-1, +0});
		som.test(new double[]{+0, +1});
	}
	
	
	
}
