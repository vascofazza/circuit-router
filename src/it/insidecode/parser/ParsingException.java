package it.insidecode.parser;

/**
 * Rappresenta un'eccezione nel parsing del file binario
 * 
 * @author fscozzafava
 *
 */
public class ParsingException extends Exception {

	private static final long serialVersionUID = 1L;

	public ParsingException(String message) {
		super("Error parsing input file: "+message);
	}
	
	

}
