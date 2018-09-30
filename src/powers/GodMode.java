package powers;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import objects.Power;
import world.Player;

public class GodMode extends Power
{
	private static final long serialVersionUID = -51155254546800875L;

	public GodMode(double x, double y)
	{
		super(x, y);
		
		radius=20;
		bgColor=Color.white;
		borderColor=Color.GREEN;
		bgImage=Power.powerUp03;
		
		changesVisibleForAll=true;

		msg="GodMode";

		show();
	}

	@Override
	public void operation(Player p)
	{
		if (!p.godMode)
		{
			p.tmpColor = p.getColor();
			p.tmpName = p.getName();
			p.setColor(Color.gray);
			p.setName(p.getName()+" (GOD)");
			p.setGodMode();
			try
			{
				TimeUnit.MILLISECONDS.sleep(3000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			p.unsetGodMode();
			p.setName(p.tmpName);
			p.setColor(p.tmpColor);
		}
		else
		{
			try {
				TimeUnit.MILLISECONDS.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
