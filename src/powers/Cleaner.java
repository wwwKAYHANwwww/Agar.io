package powers;

import java.awt.Color;
import objects.Power;
import world.Player;

public class Cleaner extends Power
{
	private static final long serialVersionUID = 1471126768296450406L;
	public Cleaner(double x, double y)
	{
		super(x, y);
		
		radius=20;
		bgColor=Color.white;
		borderColor=Color.red;
		bgImage=Power.powerDown09;
		
		msg="Cleaner";

		show();
	}
	
	@Override
	public void operation(Player p)
	{
		if (p.godMode || p.speed)
		{
			p.unsetGodMode();
			p.setColor(p.tmpColor);
			p.setName(p.tmpName);
			p.unsetSpeedMode();
			p.clearBoard();
		}		
	}

}
