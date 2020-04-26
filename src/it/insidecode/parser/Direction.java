package it.insidecode.parser;

/**
 * Enum delle direzioni codificate nel file di input/output binario
 * 
 * @author fscozzafava
 *
 */
public enum Direction {

	LEFT_UP(-1,-1), 
	LEFT_DOWN(-1,1), 
	LEFT_CENTER(-1,0), 
	RIGHT_UP(1,-1), 
	RIGHT_DOWN(1,1), 
	RIGHT_CENTER(1,0), 
	CENTER_UP(0,-1), 
	CENTER_DOWN(0,1);
	
	private int dx;
	private int dy;
	
	private Direction(int dx, int dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
	
	public int getDx(){ return dx;}
	
	public int getDy() { return dy;}
	
	/**
	 * Date le coordinate di due nodi nella griglia ritorna la direzione dell'arco che li collega
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return la direzione
	 */
	public static Direction getDirection(int x1, int y1, int x2, int y2)
	{
		int x = x2 - x1;
		int y = y2 - y1;
		for(Direction d : Direction.values())
		{
			if (d.dx == x && d.dy == y) return d;
		}
		return null;
	}
}
