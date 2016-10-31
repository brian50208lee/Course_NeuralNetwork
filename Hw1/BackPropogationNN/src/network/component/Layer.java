package network.component;
import java.util.Random;

public class Layer {
	public Neural neural[];
	public Layer(int neuralNum) {
		this.neural = new Neural[neuralNum];
		for (int i = 0; i < neuralNum; i++) {
			neural[i] = new Neural();
		}
	}

	public static void linkNeural(Layer layer1, Layer layer2) {
		int neuralNum1 = layer1.neural.length;
		int neuralNum2 = layer2.neural.length;
		for (int i = 0; i < neuralNum1; i++) {
			for (int j = 0; j<neuralNum2;j++) {
				Neural neural1 = layer1.neural[i];
				Neural neural2 = layer2.neural[j];
				Link link = new Link(neural1 , neural2 , new Random().nextDouble() );
				if (neural1.outLink == null)neural1.outLink = new Link[neuralNum2];
				if (neural2.inLink == null)neural2.inLink = new Link[neuralNum1];
				neural1.outLink[j] = link;
				neural2.inLink[i] = link;
			}
		}
	}
}
