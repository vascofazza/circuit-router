package it.insidecode.parser;

/**
 * Rappresenta un coppia di oggetti
 * 
 * @author fscozzafava
 *
 * @param <T> tipo generico della coppia
 */
public class Pair<T> {

	private T first;
	
	private T second;
	
	public Pair(T first, T second)
	{
		this.first = first;
		this.second = second;
	}
	
	/**
	 * @return il primo oggetto della coppia
	 */
	public T getFirst() { return first;}
	
	/**
	 * @return il secondo oggetto della coppia
	 */
	public T getSecond() { return second;}
	
}
