package nn.demo;

import java.awt.image.BufferedImage;
import java.io.IOException;

import nn.dataset.PattenSet;
import nn.network.som.SIR_SOM;
import nn.view.GUI;
import nn.view.ImageUtil;
public class DemoSIR_SOM {
	
	public static void main(String args[]) throws InterruptedException, NumberFormatException, IOException  {
		/* parsing argument */
		ArgumentChecker argument = new ArgumentChecker(args);
		
		/* read training data */
		PattenSet pattenSet = new PattenSet(argument.train_data, argument.network[0], argument.network[argument.network.length-1]);
		
		/* initial network */
		SIR_SOM som = new SIR_SOM(argument.network, argument.iter, argument.learn_att, argument.learn_rep);
		
		/* View for 2D data */
		if (argument.network[0] == 2 ) {
			GUI gui = new GUI(som.getSubject());
			gui.drawPatten(pattenSet);
		}
		
		/* start training */
		som.train(pattenSet);
		som.printWeight();
		
		/* show class image */
		BufferedImage classImg = som.get2DClassImage(pattenSet, 500, 500, new double[]{-1.1,1.1}, new double[]{1.1,-1.1});
		ImageUtil.showImg(classImg, "class image");
	}
	

}
