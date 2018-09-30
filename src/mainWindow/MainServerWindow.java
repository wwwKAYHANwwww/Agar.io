package mainWindow;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import objects.PowerManager;
import world.GraphicsEngine;
import world.GraphicsSender;
import world.Map;
import world.Player;

public class MainServerWindow
{
	public static GraphicsEngine mainPanel=null;
	static JPanel contentPane;
	public static ImageIcon saveIcon = new ImageIcon("save.png");
	public static ImageIcon loadIcon = new ImageIcon("load.png");
	public static ImageIcon okIcon = new ImageIcon("ok.png");
	public static ImageIcon agarIcon = new ImageIcon("agar.png");
	public static ImageIcon startIcon = new ImageIcon("start.png");
	public static ImageIcon pauseIcon = new ImageIcon("pause.png");
	public static ImageIcon optionsIcon = new ImageIcon("options.png");
	public static ImageIcon successIcon = new ImageIcon("tick.png");
	public static ImageIcon warningIcon = new ImageIcon("warning.png");
	public static ImageIcon addIcon = new ImageIcon("add.png");

	public static String IP = null;

	
    private final static int BUFFER_SIZE = 128000;
    private static File soundFile;
    private static AudioInputStream audioStream;
    private static AudioFormat audioFormat;
    private static SourceDataLine sourceLine;
    static boolean saveMode=false;
    public static JButton start;
    public static JButton menu;
    public static JButton loadClass;
    public static Player serverPlayer=null;
    
	public void run ()
	{
		try
		{
			new ServerListener();
			new GraphicsSender();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight()-104;
	
		JFrame mainWindow = new JFrame("Agario");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setResizable(false);
		mainWindow.setBounds(0, 0, (int)width, (int)height);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainWindow.setContentPane(contentPane);
		mainWindow.setIconImage(agarIcon.getImage());
		contentPane.setLayout(null);
		
		JSeparator separator1 = new JSeparator();
		separator1.setBounds(6, (int)height-76, (int)width-12, 12);
		contentPane.add(separator1);
		
		JSeparator separator2 = new JSeparator();
		separator2.setBounds(6, 6, (int)width-12, 12);
		contentPane.add(separator2);
		
		JPanel subPanel = new JPanel();
		subPanel.setBounds(6, (int)height-70, (int)width-12, 45);
		contentPane.add(subPanel);
		subPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		start = new JButton("Start");
		start.setIcon(startIcon);
		subPanel.add(start);
		menu = new JButton("Menu");
		loadClass = new JButton("Load a PowerUp/PoweDown");
		loadClass.setIcon(addIcon);

		start.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				PowerManager.addPower(powers.GodMode.class);
				PowerManager.addPower(powers.SpeedMode.class);
				PowerManager.addPower(powers.Collector.class);
				PowerManager.addPower(powers.Divider.class);
				PowerManager.addPower(powers.Cleaner.class);

				mainPanel = new GraphicsEngine();
				world.Map.isStarted=true;
				mainPanel.setBounds(6, 17, world.Map.width, world.Map.height);
				contentPane.add(mainPanel);
				
				GraphicsEngine.started=true;
				start.setEnabled(false);
				MainWindow.me=serverPlayer=new Player(Player.name1, "Server", Map.width*Math.random(), Map.height*Math.random(), Player.c1, Player.image1);

				Thread sound = new Thread (new Runnable()
				{
					@Override
					public void run()
					{
						while (true)
						{
							playSound(world.Map.audio);
						}
					}
				});
				if (world.Map.play)
					sound.start();
				menu.setText("Save");
				menu.setIcon(saveIcon);
				saveMode=true;
				mainPanel.requestFocus();
			}
		});
		loadClass.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fc = new JFileChooser();
			    FileNameExtensionFilter filter1 = new FileNameExtensionFilter(
			            "Java Class Files", "class");
			    fc.addChoosableFileFilter(filter1);
			    fc.setApproveButtonText("Load");
			    fc.setAcceptAllFileFilterUsed(false);
				int returnVal = fc.showOpenDialog(MainServerWindow.mainPanel);
				if (returnVal==0)
				{
					File file = fc.getSelectedFile();
					File file2=null;
				    try {
						Files.copy(file.toPath(), (new File("bin/powers/"+file.getName())).toPath());
						file2 = new File("bin/powers/"+file.getName());
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					if (file.getAbsolutePath().endsWith("java")||file.getAbsolutePath().endsWith("class"))
					{
						URLClassLoader classLoader = null;
						@SuppressWarnings("rawtypes")
						Class newPower=null;
						try
						{
							classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()});
							newPower = Class.forName("powers."+file2.getName().substring(0, file2.getName().indexOf(".")), true, classLoader);
						}
						catch (ClassNotFoundException | MalformedURLException e1)
						{
							e1.printStackTrace();
						}
						PowerManager.addPower(newPower);
						try {
							Files.delete(file2.toPath());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						//
						//
						//
						//
						//
						//
						JOptionPane.showMessageDialog(MainServerWindow.mainPanel, "File loaded successfully.", "File loaded", JOptionPane.PLAIN_MESSAGE, successIcon);
					}
					else
					{
						JOptionPane.showMessageDialog(MainServerWindow.mainPanel, "File has not been loaded successfully.", "LOADING ERROR", JOptionPane.PLAIN_MESSAGE, warningIcon);
					}
					if (MainServerWindow.mainPanel!=null)
						MainServerWindow.mainPanel.requestFocus();
				}
			}
		});
		
		menu.setIcon(optionsIcon);
		subPanel.add(menu);
		subPanel.add(loadClass);

		menu.addActionListener(new ActionListener()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				if (!saveMode)
				{
					ServerOptions o = new ServerOptions();
					o.show();
				}
				else
				{
					JFileChooser fc = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Users' Data", "usersdata");
					fc.setFileFilter(filter);
					fc.setAcceptAllFileFilterUsed(false);
					int returnVal = fc.showSaveDialog(MainServerWindow.mainPanel);
					if (returnVal==0)
					{
						File file = fc.getSelectedFile();
						String file_name = file.toString();
						if (!file_name.endsWith(".usersdata"))
							file_name += ".usersdata";
						serializer.Save.saveTo(new File(file_name));
						JOptionPane.showMessageDialog(MainServerWindow.mainPanel, "The Informations of Users Saved Successfully.", "Data Exported", JOptionPane.PLAIN_MESSAGE, successIcon);
					}
					mainPanel.requestFocus();					
				}
			}
		});
		
		mainWindow.setVisible(true);
	}

	  public static void playSound(String filename)
	  {

	       String strFilename = filename;

	       try {
	           soundFile = new File(strFilename);
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.exit(1);
	        }

	        try {
	            audioStream = AudioSystem.getAudioInputStream(soundFile);
	        } catch (Exception e){
	            e.printStackTrace();
	            System.exit(1);
	        }

	        audioFormat = audioStream.getFormat();

	        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
	        try {
	            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
	            sourceLine.open(audioFormat);
	        } catch (LineUnavailableException e) {
	            e.printStackTrace();
	            System.exit(1);
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.exit(1);
	        }

	        sourceLine.start();

	        int nBytesRead = 0;
	        byte[] abData = new byte[BUFFER_SIZE];
	        while (nBytesRead != -1) {
	            try {
	                nBytesRead = audioStream.read(abData, 0, abData.length);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            if (nBytesRead >= 0) {
	                @SuppressWarnings("unused")
	                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
	            }
	        }

	        sourceLine.drain();
	        sourceLine.close();
	 }
}
