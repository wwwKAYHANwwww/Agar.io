package powers;

import java.awt.Color;

import objects.Power;
import world.Player;

public class Divider extends Power
{
	private static final long serialVersionUID = 4747930766160084061L;

	public Divider(double x, double y)
	{
		super(x, y);
		
		radius=20;
		bgColor=Color.white;
		borderColor=Color.red;
		bgImage=Power.powerDown05;
		
		msg="Divider";
		
		show();
	}

	@Override
	public void operation(Player p)
	{
		int k=p.myParts.size();
		for (int j=0; j<k; j++)
		{
			p.myParts.get(j).devide(7000);
		}
	}
}
