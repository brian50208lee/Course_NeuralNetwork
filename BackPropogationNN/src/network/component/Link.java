package network.component;


public class Link {
	public Neural inNeural;
	public Neural outNeural;
	public double weight;
	
	public Link(Neural inNeural, Neural outNeural, double weight) {
		this.inNeural = inNeural;
		this.outNeural = outNeural;
		this.weight = weight;
	}
}
