package world;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;

import objects.Circle;

public class Player implements Serializable
{
	private static final long serialVersionUID = 9132657220015784009L;
	public static int C=250;
	public static ArrayList<Player> all = new ArrayList<Player>();
	public ArrayList<Circle> myParts = new ArrayList<Circle>();
	private String name="";
	private ImageIcon image=null;
	private	Color color;
	private double V;
	public boolean speed=false;
	public boolean godMode=false;
	private int motionX=0, motionY=0;
	private double vx, vy;
	public static String name1 = "Player 1";
	public static ImageIcon image1=null;
	public static Color c1=Color.red;
	public Mover moveMe = new Mover();
	Updater updateMe = new Updater();
	public double ang = -1;
	private String username="";
	private String board = "";
	private double coeff=1;
	public boolean changesVisibleForAll=true;
	public String tmpName="";
	public ImageIcon tmpImage=null;
	public Color tmpColor=null;
	
	public Player(String name, String username, double x, double y, Color color, ImageIcon img)
	{
		this.name=name;
		this.color=color;
		this.image=img;
		this.username=username;
		myParts.add(new Circle(this, x, y, (double)20));
		moveMe.start();
		updateMe.start();
		V=C/(this.theBiggestPart().getArea());
		all.add(this);	
	}
	public String readBoard()
	{
		return board;
	}
	public void writeBoard(String msg)
	{
		board+=msg;
	}
	public void clearBoard()
	{
		board="";
	}

	public String getName()
	{
		return name;
	}
	public Color getColor()
	{
		return color;
	}
	public ImageIcon getImage()
	{
		return image;
	}
	public void goTo(double x, double y)
	{
		double dx = x - this.myParts.get(0).getCenter().getX();
		double dy = -y + this.myParts.get(0).getCenter().getY();
		ang = Math.atan(dy/dx)+(2*Math.PI);
		if (dx<0)
		{
			ang+=Math.PI;
		}
	}
	private Circle theBiggestPart()
	{
		if (myParts.size()==0)
			return null;
		Iterator<Circle> it = myParts.iterator();
		Circle c;
		Circle c2=null;
		double area=0;
		while (it.hasNext())
		{
			c=it.next();
			if (c.getArea()>area)
			{
				c2=c;
				area=c.getArea();
			}
		}
		return c2;
	}
	public void Render (Graphics G)
	{
		Iterator<Circle> it = myParts.iterator();
		while (it.hasNext())
			it.next().Render(G);
		
		G.setColor(Color.DARK_GRAY);
		Font f = G.getFont();
		G.setFont(new Font("Arial", 20, 50));
		if (mainWindow.MainWindow.me==this)
			G.drawString(this.readBoard(), 5, Map.height-10);
		G.setFont(f);
	}
	public void RenderShadow (Graphics G)
	{
		Iterator<Circle> it = myParts.iterator();
		while (it.hasNext())
			it.next().RenderShadow(G);
		
		G.setColor(Color.DARK_GRAY);
		Font f = G.getFont();
		G.setFont(new Font("Arial", 20, 50));
		if (mainWindow.MainWindow.me==this)
			G.drawString(this.readBoard(), 5, Map.height-10);
		G.setFont(f);
	}
	public class Mover extends Thread implements Serializable
	{
		private static final long serialVersionUID = -8539844052083376859L;

		@Override
		public void run()
		{
			while (true)
			{
				for (int i = myParts.size()-1; i>=0; i--)
				{
					myParts.get(i).moveCenter(vy/2, -vx/2);
				}
				try
				{
					TimeUnit.MILLISECONDS.sleep(10);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}

			}
		}
	}
	public class Updater extends Thread implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -2403755383845408687L;

		@Override
		public void run()
		{
			while (true)
			{
				synchronized(Color.black)
				{
					speedUpdated();
				}
			}
		}
	}
	
	private void speedUpdated()
	{
		Circle theBiggestPart = theBiggestPart();
		if (theBiggestPart==null)
		{
			V=0;
		}
		else
		{
			if (speed)
				V=C*coeff/Math.sqrt(theBiggestPart.getArea())+1.4;	
			else
				V=(C/Math.sqrt(theBiggestPart.getArea())+1.4);	

		}
		if (ang==-1)
		{
			if (motionX*motionY==0)
			{
				vx=(motionX*V);
				vy=(motionY*V);
			}
			else
			{
				vx= (0.65*V*motionX);
				vy= (0.65*V*motionY);
			}
		}
		else
		{
			vx = V*Math.cos(-ang+Math.PI/2);
			vy = V*Math.sin(-ang+Math.PI/2);
		}
	}
	public void setName(String name)
	{
		this.name=name;
		Iterator<Circle> it = myParts.iterator();
		if (this.changesVisibleForAll)
			while (it.hasNext())
				it.next().setName(name);
		else
			while (it.hasNext())
				it.next().setNameShadow(name);
	}
	public void setColor(Color c)
	{
		this.color=c;
		Iterator<Circle> it = myParts.iterator();
		if (this.changesVisibleForAll)
			while (it.hasNext())
				it.next().setColor(c);
		else
			while (it.hasNext())
				it.next().setColorShadow(c);
	}
	public void setImage(ImageIcon img)
	{
		this.image=img;
		Iterator<Circle> it = myParts.iterator();
		if (this.changesVisibleForAll)
			while (it.hasNext())
				it.next().setImage(img);
		else
			while (it.hasNext())
				it.next().setImageShadow(img);
	}
	public void setGodMode()
	{
		unsetGodMode();
		godMode=true;
	}
	public void unsetGodMode()
	{
		godMode=false;
	}
	public void collectParts()
	{
		if (myParts.size()<2)
			return;
		synchronized(Color.black)
		{
			int size = myParts.size();
			for(int i=size-1; i>0; i--)
			{
				myParts.get(0).increaseArea(myParts.get(i).getArea());
				myParts.get(i).destroy();
			}
		}
	}
	public void setSpeedMode(double coefff)
	{
		unsetSpeedMode();
		speed=true;
		coeff=coefff;
	}
	public void unsetSpeedMode()
	{
		speed=false;
		coeff=1;
	}

	public void setGoingUp()
	{
		synchronized(Color.black)
		{
			Iterator<Circle> it = myParts.iterator();
			while (it.hasNext())
				it.next().moveCenter(0, -3);
		}
		motionX=1;
	}
	public void unsetGoingUp()
	{
		motionX=0;
	}
	public void setGoingDown()
	{
		synchronized(Color.black)
		{
			Iterator<Circle> it = myParts.iterator();
			while (it.hasNext())
				it.next().moveCenter(0, 3);
		}
		motionX=-1;
	}
	public void unsetGoingDown()
	{
		motionX=0;
	}
	public void setGoingRight()
	{
		synchronized(Color.black)
		{
			Iterator<Circle> it = myParts.iterator();
			while (it.hasNext())
				it.next().moveCenter(3, 0);
		}
		motionY=1;
	}
	public void unsetGoingRight()
	{
		motionY=0;
	}
	public void setGoingLeft()
	{
		synchronized(Color.black)
		{
			Iterator<Circle> it = myParts.iterator();
			while (it.hasNext())
				it.next().moveCenter(-3, 0);
		}
		motionY=-1;
	}

	public void devide(int ms)
	{
		int size = myParts.size();
		if (size>=4)
			return;
		for (int i=size-1; i>=0; i--)
		{
			myParts.get(i).devide(ms);
		}
	}
	public void unsetGoingLeft()
	{
		motionY=0;
	}
	public String getUsername()
	{
		return this.username;
	}
	public static int indexOf(String username)
	{
		int result=-1;
		synchronized(Color.GREEN)
		{
			for (int i=0; i<all.size(); i++)
			{
				if (all.get(i).getUsername().equals(username))
				{
					result=i;
					break;
				}
			}
		}
		return result;
	}
}
