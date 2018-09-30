package powers;

import java.awt.Color;

import objects.Power;
import world.Player;

public class Collector extends Power
{
	private static final long serialVersionUID = -8802692855041882972L;

	public Collector(double x, double y)
	{
		super(x, y);
		
		radius=20;
		bgColor=Color.white;
		borderColor=Color.GREEN;
		bgImage=Power.powerUp05;
		
		msg="Collector";

		show();
	}

	@Override
	public void operation(Player p)
	{
		if (p.myParts.size()>1)
			p.collectParts();
	}
}
