package network.component;

import java.util.Random;

public class Link {
	public Neural inNeural;
	public Neural outNeural;
	public double weight = new Random().nextDouble();
	
	public Link(Neural inNeural, Neural outNeural) {
		this.inNeural = inNeural;
		this.outNeural = outNeural;
	}
}
