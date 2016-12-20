package nn.view;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImageUtil {
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
