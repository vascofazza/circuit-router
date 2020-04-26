package it.insidecode.main;

import it.insidecode.parser.BinaryParser;
import it.insidecode.parser.InputParser;
import it.insidecode.parser.OutputParser;
import it.insidecode.parser.ParsingException;
import it.insidecode.parser.Sequence;
import it.insidecode.parser.Test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class Main {
	
	public static boolean DEBUG = false;
	public static boolean verify = false;
	public static boolean black = false;
	
	private static Color[] colors = new Color[50];
	static
	{
		Random r = new Random();
		for (int i = 0; i < 50; i++)
		{
			//to get rainbow, pastel colors
			final float hue = r.nextFloat();
			final float saturation = Math.min(.8f, r.nextFloat() + .4f);//0.7f;//1.0 for brilliant, 0.0 for dull
			final float luminance = .9f; //1.0 for brighter, 0.0 for black
			Color color = Color.getHSBColor(hue, saturation, luminance);
			colors[i] = color;
		}
	}
	
	public static void main(String[] args) throws InterruptedException, ParsingException, IOException 
	{
		boolean draw = false;
		boolean step = false;
		long time = System.nanoTime();
		InputParser ip = new InputParser();
		OutputParser op = new OutputParser();
		for (String s: args)
			{
				if (s.equals("-draw")) draw = true;
				else if (s.equals("-debug")) DEBUG = true;
				else if (s.equals("-step")) step = true;
				else if (s.equals("-verify")) verify = true;
				else if (s.equals("-black")) black = true;
			}
		if (verify)
		{
			int counter = 0;
			List<Test> tests = ip.parse(args[0]);
			BinaryParser bp = new BinaryParser("output.bin");
			//List<Sequence> seq = bp.parse("output.bin", tests.get(0).getHeight());
			for (int i = 0; i < tests.size(); i++)
			{
				Test t = tests.get(i);
				int size = t.run().size();
				List<Sequence> s = bp.parse(size, t.getHeight());
				{
					saveImage(args[0], "test "+ (i+1)+" verify", draw(t.getWidth(), t.getHeight(), s, t.getFigures()));
				}
			}
		}
		else
		{
			List<Test> tests = ip.parse(args[0]);
			List<Sequence> s;
			for (int x = 0; x < tests.size(); x++)
			{
				Test t = tests.get(x);
				s = t.run();
				op.parse(s);
				if (draw)
					{
					if(step)
						{
							for (int i = 0; i < s.size(); i++)
								saveImage(args[0], "test " + (x+1) + " - step " + (i+1), draw(t.getWidth(), t.getHeight(), s.subList(0, i+1), t.getFigures()));
						}
					else saveImage(args[0], "test "+ (x+1), draw(t.getWidth(), t.getHeight(), s, t.getFigures()));
						
					}
				System.gc();
				if (DEBUG) System.out.println("OK test "+ (x+1));
			}
			op.write("output.bin");
		}
		System.out.println((System.nanoTime()- time)/1000000000f + " Seconds");
	}
	
	public static BufferedImage draw(int width, int height, List<Sequence> lines, List<Sequence> figures)
	{
		BufferedImage image=new BufferedImage(width*5, height*5, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2=(Graphics2D)image.getGraphics();
		
		g2.setBackground(Color.white);
		g2.clearRect(0, 0, width*5, height*5);
		
		g2.setColor(Color.BLACK);
		for (Sequence s : figures)
		{
			int node;
			for (int x : s)
			{
				node = x;
				g2.fillRect(((node/4)/(height))*5, ((node/4)%(height))*5, 5, 5);
			}
		}
		int colorCounter = 0;
		for (Sequence s : lines)
		{
			g2.setColor(colors[(colorCounter = colorCounter + 1) % colors.length]);
			if (black || verify) g2.setColor(Color.BLACK);
			//Iterator<Node> i = s.iterator();
			//Node node;
			int node;
			for (int x : s)
			{
				node = x;
				if(verify) g2.fillRect(((node/4)/(height))*5, ((node/4)%(height))*5, 5, 5);
				else g2.fillRect((((node & 0xFFFFFFFC)/4)/(height))*5, (((node & 0xFFFFFFFC)/4)%(height))*5, 5, 5);
			}
		}
		return image;
	}
	
	private static void saveImage(String dirName, String fileName, BufferedImage bi)
	{
		try {
			File file = new File("Input file - " + dirName + "/" + fileName + ".png");
			file.mkdirs();
			ImageIO.write(bi, "png", file);
		} catch (Exception e) {}
	}
}
