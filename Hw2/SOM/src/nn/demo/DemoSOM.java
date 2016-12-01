package nn.demo;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import nn.dataset.PattenSet;
import nn.network.som.SOM;
import nn.view.GUI;
public class DemoSOM {
	
	private static String inputFileName = "circle.dat";
	private static int[] networkDeclare = new int[]{2,10,10,10,10,1};
	private static int dataDim = 2;
	private static int tagDim = 1;
	private static int interation = 5000;
	private static double learningRateAtt = 0.01;
	private static double learningRateRep = 10;
	
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
		
		/* weight */
		System.out.println("Done Traning");
		som.printWeight();
		
		/* test data */
		System.out.println("Test Data");
		som.test(new double[]{+1, +0});
		som.test(new double[]{+0, -1});
		som.test(new double[]{-1, +0});
		som.test(new double[]{+0, +1});
		
		BufferedImage classImg = som.get2DClassImage(pattenSet, 500, 500, new double[]{-1,1}, new double[]{1,-1});
		showImg(classImg, "class image");
	}
	
	public static void showImg(BufferedImage bi, String frameTitle ){
		ImageIcon img= new ImageIcon(bi);
		JFrame frame = new JFrame();
		JLabel lb = new JLabel(img);
		frame.getContentPane().add(lb, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(img.getIconWidth(), img.getIconHeight()+20);
		frame.setTitle(frameTitle);
		frame.setVisible(true);
	}
	
	
	
}
