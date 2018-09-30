package objects;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

import world.Map;

public class PowerManager
{
	@SuppressWarnings("rawtypes")
	public static ArrayList<Class> allClasses = new ArrayList<Class>();
	public static ArrayList<Power> all = new ArrayList<Power>();
	
	@SuppressWarnings("unchecked")
	public PowerManager()
	{
		if (allClasses.size()>0)
		{
			Random r = new Random();
			r.setSeed((long) (Math.random()*100000));
			int randomType = r.nextInt(allClasses.size());
			Constructor<Power> cons=null;
			Power p = null;
			try
			{
				cons = allClasses.get(randomType).getDeclaredConstructor(double.class, double.class);
				p= cons.newInstance(Map.width*Math.random(), Map.height*Math.random());
			}
			catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				e.printStackTrace();
			}
			all.add(p);
		}
	}
	public static void addPower(@SuppressWarnings("rawtypes") Class c)
	{
		allClasses.add(c);
	}
	@SuppressWarnings("rawtypes")
	public static ArrayList<Class> getPowers()
	{
		return allClasses;
	}
	
}
