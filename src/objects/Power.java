package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import world.Player;

public abstract class Power extends Obj
{
	private static final long serialVersionUID = -3490122557241903723L;
	protected double radius=20;
	protected Color bgColor = Color.white;
	protected ImageIcon bgImage=null;
	protected Color borderColor = Color.white;
	
	public static ImageIcon powerUp01=null, powerUp02=null, powerUp03=null, powerUp04=null, powerUp05=null, powerUp06=null, powerUp07=null,
							powerUp08=null, powerUp09=null, powerUp10=null;
	
	public static ImageIcon powerDown01=null, powerDown02=null, powerDown03=null, powerDown04=null, powerDown05=null, powerDown06=null,
							powerDown07=null, powerDown08=null, powerDown09=null, powerDown10=null;

	private static boolean firstTime=true;
	protected boolean changesVisibleForAll=true;
	private PowerShower ps=null;
	protected String msg="";
	public Power(double x, double y)
	{
		super(x, y);
		if (firstTime)
		{
			Image img=null;
			try
			{
				File pathToFile = new File("powerUp01.png");
				img = ImageIO.read(pathToFile);
				powerUp01 = new ImageIcon(img);
				
				pathToFile = new File("powerUp02.png");
				img = ImageIO.read(pathToFile);
				powerUp02 = new ImageIcon(img);
				
				pathToFile = new File("powerUp03.png");
				img = ImageIO.read(pathToFile);
				powerUp03 = new ImageIcon(img);

				pathToFile = new File("powerUp04.png");
				img = ImageIO.read(pathToFile);
				powerUp04 = new ImageIcon(img);
				
				pathToFile = new File("powerUp05.png");
				img = ImageIO.read(pathToFile);
				powerUp05 = new ImageIcon(img);
				
				pathToFile = new File("powerUp06.png");
				img = ImageIO.read(pathToFile);
				powerUp06 = new ImageIcon(img);
				
				pathToFile = new File("powerUp07.png");
				img = ImageIO.read(pathToFile);
				powerUp07 = new ImageIcon(img);
				
				pathToFile = new File("powerUp08.png");
				img = ImageIO.read(pathToFile);
				powerUp08 = new ImageIcon(img);
				
				pathToFile = new File("powerUp09.png");
				img = ImageIO.read(pathToFile);
				powerUp09 = new ImageIcon(img);
				
				pathToFile = new File("powerUp10.png");
				img = ImageIO.read(pathToFile);
				powerUp10 = new ImageIcon(img);
				
				pathToFile = new File("powerDown01.png");
				img = ImageIO.read(pathToFile);
				powerDown01 = new ImageIcon(img);
				
				pathToFile = new File("powerDown02.png");
				img = ImageIO.read(pathToFile);
				powerDown02 = new ImageIcon(img);
				
				pathToFile = new File("powerDown03.png");
				img = ImageIO.read(pathToFile);
				powerDown03 = new ImageIcon(img);
				
				pathToFile = new File("powerDown04.png");
				img = ImageIO.read(pathToFile);
				powerDown04 = new ImageIcon(img);
				
				pathToFile = new File("powerDown05.png");
				img = ImageIO.read(pathToFile);
				powerDown05 = new ImageIcon(img);
				
				pathToFile = new File("powerDown06.png");
				img = ImageIO.read(pathToFile);
				powerDown06 = new ImageIcon(img);
				
				pathToFile = new File("powerDown07.png");
				img = ImageIO.read(pathToFile);
				powerDown07 = new ImageIcon(img);
				
				pathToFile = new File("powerDown08.png");
				img = ImageIO.read(pathToFile);
				powerDown08 = new ImageIcon(img);
				
				pathToFile = new File("powerDown09.png");
				img = ImageIO.read(pathToFile);
				powerDown09 = new ImageIcon(img);
				
				pathToFile = new File("powerDown10.png");
				img = ImageIO.read(pathToFile);
				powerDown10 = new ImageIcon(img);	
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		firstTime=false;
		bgImage=powerUp01;
	}
	
	@Override
	public double getRadius()
	{
		return radius;
	}

	@Override
	public void destroy()
	{
		synchronized(Color.black)
		{
			if (Obj.all.contains(this))
				Obj.all.remove(this);
			if (PowerManager.all.contains(this))
				PowerManager.all.remove(this);
			if (PowerShower.all.contains(ps))
				PowerShower.all.remove(ps);
		}
	}

	@Override
	public int getType()
	{
		return 3;
	}

	@Override
	public double getArea()
	{
		return Math.PI*radius*radius;
	}

	@Override
	public boolean isIn(Point p)
	{
		if (Point.distance(p.getX(), p.getY(), this.center.getX(), this.center.getY())<=radius)
			return true;
		else
			return false;
	}
	
	@Override
	public void Render(Graphics G)
	{
		G.setColor(bgColor);
		G.fillOval((int)(this.getCenter().getX()-radius), (int)(this.getCenter().getY()-radius), (int)(2*radius), (int)(2*radius));
		
		G.setColor(borderColor);
		G.drawOval((int)(this.getCenter().getX()-radius), (int)(this.getCenter().getY()-radius), (int)(2*radius), (int)(2*radius));

		G.drawImage(bgImage.getImage(), (int)(this.getCenter().getX()-radius), (int)(this.getCenter().getY()-radius), (int)(2*radius), (int)(2*radius), null);

	}
	public void show()
	{
		ps=new PowerShower(this);
	}
	public void act(Player p)
	{
		p.changesVisibleForAll=changesVisibleForAll;
		String s = p.readBoard();
		if (s.indexOf(msg)<0)
			p.writeBoard("|"+ msg);
		operation(p);
		synchronized(Color.gray)
		{
			s=p.readBoard();
			s=s.replaceAll(msg, "");
			if (s.startsWith("||"))
				s=s.substring(2);
			if (s.startsWith("|"))
				s=s.substring(1);
			p.clearBoard();
			p.writeBoard(s);
		}
		p.changesVisibleForAll=true;
	}
	
	public abstract void operation (Player p);
}
