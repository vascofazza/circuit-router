package it.insidecode.main;

import it.insidecode.parser.InputParser;
import it.insidecode.parser.OutputParser;
import it.insidecode.parser.ParsingException;
import it.insidecode.parser.Sequence;
import it.insidecode.parser.Test;

import java.io.IOException;
import java.util.List;

/**
 * La classe responsabile della gestione di tutte le componenti del progetto.
 * 
 * @author fscozzafava
 *
 */
public class MainClass 
{
	public static void main(String[] args) throws ParsingException, IOException 
	{
		InputParser ip = new InputParser();
		OutputParser op = new OutputParser();
		List<Test> tests = ip.parse("input.bin");
		List<Sequence> s;
		for (int x = 0; x < tests.size(); x++)
		{
			Test t = tests.get(x);
			s = t.run();
			op.parse(s);
			System.gc();
		}
		op.write("output.bin");
	}
}
