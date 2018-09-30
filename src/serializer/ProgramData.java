package serializer;

import java.io.Serializable;
import java.util.ArrayList;

import world.User;

class ProgramData implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5682097095965992856L;
	ArrayList<User> playerDatas;

	public ProgramData()
	{
		playerDatas = User.all;
	}
	public void updateProgram()
	{
		if (playerDatas.size()>0)
		for (int i=0; i<playerDatas.size(); i++)
		{
			User p=playerDatas.get(i);
			new User(p.getUsername(), p.getPassword(), p.getName(), p.getColor(), p.getImage());
		}
	}
}
