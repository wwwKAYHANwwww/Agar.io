package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class PowerShower implements Serializable
{
	private static final long serialVersionUID = 5519082695079571949L;
	public static ArrayList<PowerShower> all = new ArrayList<PowerShower>();
	
	private double x, y, r;
	private Color bgColor, borderColor;
	private ImageIcon bgImage;
	
	public PowerShower(Power p)
	{
		x=p.getCenter().getX();
		y=p.getCenter().getY();
		r=p.getRadius();
		bgColor=p.bgColor;
		borderColor=p.borderColor;
		bgImage=p.bgImage;
		all.add(this);
	}
	
	public void Render(Graphics G)
	{
		G.setColor(bgColor);
		G.fillOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
		
		G.setColor(borderColor);
		G.drawOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));

		G.drawImage(bgImage.getImage(), (int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r), null);
	}

}
