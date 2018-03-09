package Zad2;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.*;

public class FrameForTextEditor extends JFrame{
	
	private JFileChooser chooser = new JFileChooser(new File("."));
	private JTextArea textArea = new JTextArea();
	private Color[] colors = {Color.BLUE, Color.YELLOW, Color.ORANGE, Color.RED, Color.WHITE, Color.BLACK, Color.GREEN};
	
	private static HashMap<String, String> adresy = new HashMap<String, String>();
	private static HashMap<String, Color> colorsFromTextToColor = new HashMap<String, Color>();
	
	static {
		adresy.put("Praca", "adres pracy");
		adresy.put("Szkoła", "adres szkoły");
		adresy.put("Dom", "adres domu");
	}
	
	static {
		colorsFromTextToColor.put("Blue", Color.BLUE);
		colorsFromTextToColor.put("Yellow", Color.YELLOW);
		colorsFromTextToColor.put("Orange", Color.ORANGE);
		colorsFromTextToColor.put("Red", Color.RED);
		colorsFromTextToColor.put("White", Color.WHITE);
		colorsFromTextToColor.put("Black", Color.BLACK);
		colorsFromTextToColor.put("Green", Color.GREEN);
	}
	
	//======================================================================================================
	//Listenery
	
	private ActionListener open = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			int i = chooser.showOpenDialog(null);
			if(i == 0 ) { //zero oznacza wybranie pliku, 1 anulowanie
				setTitle("Prosty edytor - " + chooser.getSelectedFile().getAbsolutePath());
				textArea.setText(loadFile(chooser.getSelectedFile()));
				textArea.setCaretPosition(0);
			}
		}
		
	};
	
	private ActionListener save = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(chooser.getName(chooser.getSelectedFile()) != null) {
				String text = textArea.getText();
				String path = chooser.getSelectedFile().getAbsolutePath();
				try {
					saveFile(text, new FileOutputStream(path));
				} catch (FileNotFoundException exc) {
					exc.getMessage();
				}
				
			} else {
				int i = chooser.showSaveDialog(null);
				if(i == 0) {
					setTitle("Prosty edytor - " + chooser.getSelectedFile().getAbsolutePath());
				}
			}
		}
		
	};
	
	private ActionListener saveAs = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			int i = chooser.showSaveDialog(null);
			if(i == 0) {
				setTitle("Prosty edytor - " + chooser.getSelectedFile().getAbsolutePath());
			}
		}
		
	};
	
	private ActionListener exit = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
			System.exit(0);
		}
		
	};
	
	private ActionListener wpiszAdres = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			String typAdresu = e.getActionCommand();
		    textArea.insert(adresy.get(typAdresu), textArea.getCaretPosition());
		}
		
	};
	
	private ActionListener changeFont = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			Scanner scan = new Scanner(e.getActionCommand());
			float size = (float)scan.nextInt();
			textArea.setFont(getFont().deriveFont(size));
			scan.close();
		}
		
	};
	
	
	private ActionListener changeForeground = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			String color = e.getActionCommand();
			textArea.setForeground(colorsFromTextToColor.get(color));
		}
	};
	
	private ActionListener changeBackground = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			String color = e.getActionCommand();
			textArea.setBackground(colorsFromTextToColor.get(color));
		}
	};
	
	//=======================================================================================
	//konstruktor i createFrame
	
	public FrameForTextEditor() {
		setTitle("Prosty edytor - Bez tytułu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(100, 100);
		setPreferredSize(new Dimension(800, 600));
		setResizable(true);
		
		
		JScrollPane scroll = new JScrollPane(textArea);
		add(scroll);
		JMenuBar menuBar = new JMenuBar();
		
		ArrayList<JMenu> menuFirstLevel = createMenu();
		for(JMenu jm : menuFirstLevel) {
			menuBar.add(jm);		//tu sie dodaja te pierwszopoziomowe menu jak file itp, wraz ze swoja zawartoscia
		}
		
		setJMenuBar(menuBar);
		
		pack( );
		setVisible(true);   
	}
	
	
	//===================================================================================================
	//metody tworzace menu
	
	
	private ArrayList<JMenu> createMenu() {		//createMenu tworzy cale nasze menu ktore potem wystarczy po kolei dodac do JMenuBar
		ArrayList<JMenu> list = new ArrayList<JMenu>();
		
		int licznikFirstLevelMenu = 0;		//okresla z ktorym JMenu mamy doczynienia, 
											//potrzebne do nadania textu odpowiedniego textu i okreslenia czy mamy stworzyc JMenu czy JMenuItem
		
		//text do kazdej opcji menu
		
		String[] menuFirstLevelText = {"File", "Edit", "Options"};
		
		String[][] menuSecondLevelText = {
				{"Open", "Save", "Save as", "Exit"},
				{"Adresy"},
				{"Foreground", "Background", "Font size"}
		};
		
		String[][] thirdLevelText = {
				{"Praca", "Szkoła", "Dom"}, 
				{"Blue", "Yellow", "Orange", "Red", "White", "Black", "Green"},
				{"Blue", "Yellow", "Orange", "Red", "White", "Black", "Green"},
				{"8 pts", "10 pts", "12 pts", "14 pts", "16 pts", "18 pts", "20 pts", "22 pts", "24 pts"}
		};
		
		//actionListenery
		ActionListener[] editAndOptionsListeners = {wpiszAdres, changeForeground,  changeBackground, changeFont};
		ActionListener[] fileListeners = {open, save, saveAs, exit};
		
		
		//acceleratory
		KeyStroke[] acceleratorFile = {KeyStroke.getKeyStroke("control O"), KeyStroke.getKeyStroke("control S"), KeyStroke.getKeyStroke("ctrl A"), KeyStroke.getKeyStroke("control X")};
		KeyStroke[] acceleratorEdit = {KeyStroke.getKeyStroke("control shift P"), KeyStroke.getKeyStroke("control shift S"), KeyStroke.getKeyStroke("control shift D")};
		
		//mnemoniki
		int[] mnemonicFile = {KeyEvent.VK_O, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_X};
		int[] mnemonicEdit = {KeyEvent.VK_P, KeyEvent.VK_S, KeyEvent.VK_D};
		
		//jak w nazwie, okresla czy ma zostac stworzone JMenu czy JMenuItem
		boolean[] menuOrMenuItem = {false, true, true};
		
		//okresla czy dodawac ikone, bedaca kolorowym kolkiem
		boolean[] addColor = {false, true, true, false};
		
		int licznikThirdLevelMenu = 0;		//okresla z ktorej tabeli mamy nadawac texty

		for(String m : menuFirstLevelText) {		//ten for tworzy wszystkie JMenu pierwszego poziomu, czyli file itp
			JMenu jm = new JMenu(m);
			if(menuOrMenuItem[licznikFirstLevelMenu]) { //dodaje Edit i Options
				ArrayList<JMenu> menuSecondLevel = createJMenuLevel(menuSecondLevelText[licznikFirstLevelMenu]);
				for(JMenu jm2 : menuSecondLevel) {
					ArrayList<JMenuItem> menuThirdLevel = createJMenuItemLevel(thirdLevelText[licznikThirdLevelMenu]);
					int licznikAcceleratorEdit = 0;		//do nadawania odpowiednich acceleratorow do JMenuItemow w JMenu Edit
					int licznikColor = 0;		//do nadawania po kolei odpowiednich kolorow
					for(JMenuItem jmi : menuThirdLevel) {
						if(licznikThirdLevelMenu == 0) {	//teraz jestesmy w trakcie dodawania opcji do JMenu Adresy, wiec dodaje acceleratory i mnemoniki
							jmi.setAccelerator(acceleratorEdit[licznikAcceleratorEdit]);
							jmi.setMnemonic(mnemonicEdit[licznikAcceleratorEdit]);
							licznikAcceleratorEdit++;
						}
						
						jmi.addActionListener(editAndOptionsListeners[licznikThirdLevelMenu]);
						
						if(addColor[licznikThirdLevelMenu]) {
							jmi.setIcon(new MenuColorIcon(colors[licznikColor]));
							licznikColor++;
						}
						
						jm2.add(jmi);
					}
					jm.add(jm2);
					licznikThirdLevelMenu++;
				}
				list.add(jm);
				licznikFirstLevelMenu++;
			} else {		//dodaje File
				ArrayList<JMenuItem> menuSecondLevel = createJMenuItemLevel(menuSecondLevelText[licznikFirstLevelMenu]);
				int licznikJMenuItem = 0;		//do dodawania kolejnych listenerów itp. dla każdego JMenuItem File
				for(JMenuItem jmi2 : menuSecondLevel) {
					if(licznikJMenuItem == 3) {
						JSeparator js = new JSeparator(SwingConstants.HORIZONTAL);
						js.setForeground(Color.RED);
						js.setBackground(Color.RED);
						jm.add(js);
					}
						jmi2.addActionListener(fileListeners[licznikJMenuItem]);
						jmi2.setAccelerator(acceleratorFile[licznikJMenuItem]);
						jmi2.setMnemonic(mnemonicFile[licznikJMenuItem]);
						jm.add(jmi2);
						licznikJMenuItem++;
				}
				list.add(jm);
				licznikFirstLevelMenu++;
			}
		}
		return list;
	}
	
	private ArrayList<JMenu> createJMenuLevel(String ... menu) {
		ArrayList<JMenu> list = new ArrayList<JMenu>();
		
		for(String m : menu) {
				JMenu jm = new JMenu(m);
				jm.setBorder(BorderFactory.createRaisedBevelBorder());
				list.add(jm);
		}
		return list;
	}
	
	private ArrayList<JMenuItem> createJMenuItemLevel(String ... menu) {
		ArrayList<JMenuItem> list = new ArrayList<JMenuItem>();
		
		for(String m : menu) {
			JMenuItem jmi = new JMenuItem(m);
			jmi.setBorder(BorderFactory.createRaisedBevelBorder());
			list.add(jmi);
		}
		return list;
	}
	
	//==========================================================
	//metody ladujace i zapisujace plik
	
	private String loadFile(File file) {
		StringBuffer sb = new StringBuffer();
		try {
			Scanner scan = new Scanner(file);
			while(scan.hasNextLine()){
				sb.append(scan.nextLine() + System.getProperty("line.separator"));
			}
			scan.close();
			return new String(sb);
		} catch (FileNotFoundException e) {
			e.getMessage();
			return null;
		}
	}
	
	private void saveFile(String in, OutputStream out) {
		try (BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(out))) {
			Scanner scan = new Scanner(in);
			while(scan.hasNextLine()) {
				buffer.write(scan.nextLine());
				buffer.newLine();
			}
			scan.close();
		} catch (IOException e) {
			e.getMessage();
		}
		      
	}
	
}
