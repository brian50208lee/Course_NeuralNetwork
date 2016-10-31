package network.data_struct;

import java.util.ArrayList;
import java.util.Iterator;

public class PattenSet {
	private ArrayList<Patten> pattenList;
	
	public PattenSet(){
		pattenList = new ArrayList<Patten>();
	}
	
	public ArrayList<Patten> getPattenList(){
		return pattenList;
	}
	
	public int size(){
		return pattenList.size();
	}
	
	public void add(Patten patten){
		pattenList.add(patten);
	}
	
	public Iterator<Patten> getIterator(){
		return pattenList.iterator();
	}
}
