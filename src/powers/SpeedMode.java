package powers;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import objects.Power;
import world.Player;

public class SpeedMode extends Power
{
	private static final long serialVersionUID = 1325771851827103039L;

	public SpeedMode(double x, double y)
	{
		super(x, y);
		
		radius=20;
		bgColor=Color.white;
		borderColor=Color.GREEN;
		bgImage=Power.powerUp10;
		
		changesVisibleForAll=false;
		
		msg="SpeedMode";

		show();
	}

	@Override
	public void operation(Player p)
	{
		if (!p.speed)
		{
			p.setSpeedMode(3);
			try
			{
				TimeUnit.MILLISECONDS.sleep(10000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			p.unsetSpeedMode();
		}
	}
}