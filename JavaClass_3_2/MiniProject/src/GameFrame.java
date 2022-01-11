import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

public class GameFrame extends JFrame {
	//�ܾ�� ���õ� �޴�
	private JMenuItem addWord = new JMenuItem("�߰�");
	private JMenuItem deleteWord = new JMenuItem("����");
	private JMenuItem searchWord = new JMenuItem("�˻�");

	//������ ���õ� �޴�
	private JMenuItem allRank = new JMenuItem("TOP 10");
	private JMenuItem myRank = new JMenuItem("�� ���");

	//���ٿ� ������ ������ ���õ� �޺� �ڽ�
	private JLabel lbLevel = new JLabel("����");
	private String [] level = {"Level1", "Level2", "Level3"};
	private JComboBox<String> levelCombo = new JComboBox<String>(level);
	
	//���� ���� ��ư��
	private JButton homeBtn = new JButton(new ImageIcon("image/homeBtn.jpg"));
	private JButton startBtn = new JButton(new ImageIcon("image/startBtn.jpg"));
	private JButton stopBtn = new JButton(new ImageIcon("image/stopBtn.jpg"));

	//�ʿ��� �г� ��ü��
	private ScorePanel scorePanel = new ScorePanel(); 
	private ShowEntirePanel showEntirePanel = new ShowEntirePanel();
	private ShowPartsPanel showPartsPanel = new ShowPartsPanel();
	private GamePanel gamePanel = new GamePanel(scorePanel, showPartsPanel);
	private JFrame frame;
	
	private String userName; //����ڰ� �Է��� �̸��� ������ ����
	private int chooseLevel = 0; //����ڰ� ���� �޺��ڽ����� ������ ������ ������ ���� 

	public GameFrame(String userName) {
		setTitle("�Ǵ� ���� Ÿ���� ����");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,600);
		
		frame = this; //���� frame�� ������ ����
		this.userName = userName; //EnterPanel�κ��� ������ userName�� GameFrame������ ����� �� �ֵ��� ����

		scorePanel.setUserName(userName); //ScorePanel���� userName ����� �� �ֵ��� ����
		gamePanel.setUserName(userName); //GamePanel���� userName ����� �� �ֵ��� ����
		gamePanel.setDelayPoint(chooseLevel); //�޺��ڽ����� ���õ� ������ ���� ���� �÷��̰� �ٸ��� �ǵ��� ������ ����

		splitPane(); //JSplitPane�� �����Ͽ� ����Ʈ���� CENTER�� ����
		makeMenu(); //�޴� ���� �Լ�
		makeToolBar(); //���� ���� �Լ�
 		setResizable(false); //â ũ�� ���� �Ұ�
		setVisible(true);
	}

	private void splitPane() { //GameFrame�� ȭ�� ����
		JSplitPane hPane = new JSplitPane();
		getContentPane().add(hPane, BorderLayout.CENTER);
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT); //���η� �����Ѵ�.
		hPane.setDividerLocation(500); //500px ��ġ�� ���μ����� ����
		hPane.setEnabled(false); //�и��� �������̰�
		hPane.setLeftComponent(gamePanel); //���� 500px���� ���ʿ� gamePanel ����

		JSplitPane p2Pane = new JSplitPane();
		p2Pane.setOrientation(JSplitPane.VERTICAL_SPLIT); //���η� �����Ѵ�.
		p2Pane.setDividerLocation(70); //70px ��ġ�� ���μ����� �����Ѵ�.
		p2Pane.setTopComponent(scorePanel); //70px ���� ���� scorePanel ����
		p2Pane.setBottomComponent(showPartsPanel); //70px ���� �Ʒ��� showPartsPanel ����
		hPane.setRightComponent(p2Pane); //hPane�� �����ʿ� p2Pane ����

		JSplitPane pPane = new JSplitPane();
		pPane.setOrientation(JSplitPane.VERTICAL_SPLIT); //���η� �����Ѵ�.
		pPane.setDividerLocation(270); //270px ��ġ�� ���μ����� �����Ѵ�.
		pPane.setTopComponent(p2Pane); //270px ���� ���� p2Pane ����
		pPane.setBottomComponent(showEntirePanel); //270px ���� �Ʒ��� showEntirePanel ����
		hPane.setRightComponent(pPane); //hPane�� �����ʿ� pPane ����
	}

	private void makeMenu() { //�޴� ���� �Լ�
		JMenuBar mBar = new JMenuBar();
		this.setJMenuBar(mBar); //���� frame�� mBar �޴� ����

		mBar.setBackground(Color.BLACK); 

		JMenu wordMenu = new JMenu("�ܾ�����"); //�ܾ����� �޴� ����
		wordMenu.setForeground(Color.WHITE);
		//wordMenu �޴��� �޴������� ���� ����
		wordMenu.add(addWord);
		wordMenu.add(deleteWord);
		wordMenu.add(searchWord);
		mBar.add(wordMenu);

		//��ŷ �޴� �����ϰ� �޴������� ����
		JMenu rankingMenu = new JMenu("����");
		rankingMenu.setForeground(Color.WHITE);
		rankingMenu.add(allRank);
		rankingMenu.add(myRank);
		mBar.add(rankingMenu);

		//�� �޴� ������ �����ϸ� �۵��ϴ� �׼� �ڵ� �ۼ�
		addWord.addActionListener(new InputWord());
		deleteWord.addActionListener(new DeleteWord());
		searchWord.addActionListener(new SearchWord());

		allRank.addActionListener(new AllRank());
		myRank.addActionListener(new MyRank());
	}

	private void makeToolBar() { //���� ���� �Լ�
		JToolBar tBar = new JToolBar();
		tBar.setBackground(Color.BLACK);
		tBar.add(homeBtn); //Ȩ��ư �����ϰ� ���ٿ� ����

		//homeBtn ������ ���α׷� ù ȭ���� EnterPanel�� ȭ�� ��ȯ
		homeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EnterPanel f = new EnterPanel();
				setVisible(false);
			}
		});
		tBar.addSeparator();
		tBar.addSeparator();

		//"����"�� �����ִ� ���̺��� ���ڻ��� �Ͼ������ ����
		lbLevel.setForeground(Color.WHITE);
		tBar.add(lbLevel); //���� ���̺� �����ϰ� ���ٿ� ����
		tBar.add(levelCombo); //���� �޺��ڽ� �����ϰ� ���ٿ� ����

		levelCombo.addActionListener(new ChoosedLevel());

		JLabel empty = new JLabel("                 "); //������Ʈ ������ ������ ����ֱ� ���� ����
		JLabel empty2 = new JLabel("                                                                                                                                  ");
		tBar.add(empty);
		tBar.add(startBtn); //���� ��ư �����ϰ� ���ٿ� ����
		tBar.add(stopBtn); //�ߴ� ��ư �����ϰ� ���ٿ� ����
		tBar.add(empty2);

		getContentPane().add(tBar, BorderLayout.NORTH); //���ʿ� tBar ���� ����

		startBtn.addActionListener(new StartAction()); //starBtn�� ������ ������ �׼�
		stopBtn.addActionListener(new StopAction()); //stopBtn�� ������ ������ �׼�

	}

	//startBtn ��ư�� ������ gamePanel�� startGame() ����
	private class StartAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			gamePanel.startGame();
		}
	}

	//stopBtn ��ư�� ������ gamePanel�� stopGame() ����
	private class StopAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			gamePanel.stopGame();
		}
	}

	//addWord �޴� �������� ���õǸ� ������ �׼�
	private class InputWord implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//word.txt�� ������ �ܾ �Է� �޴� ���̾�α� ���
			String inputWord = JOptionPane.showInputDialog("�߰��� �ܾ �Է��ϼ���.");
			try {
				FileWriter wordFiles = new FileWriter("word.txt", true);
				wordFiles.write(inputWord); //wordFiles�� ���̾�α׿��� �Է� ���� inputWord �߰�
				wordFiles.write("\r\n");
				wordFiles.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	//deleteWord �޴� �������� ���õǸ� ������ �׼�
	private class DeleteWord implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//word.txt���� ������ �ܾ �Է� �޴� ���̾�α� ���
			String deleteWord = JOptionPane.showInputDialog("������ �ܾ �Է��ϼ���.");
			File src = new File("word.txt"); //������ ����� �ܾ� ����
			File dest = new File("word2.txt"); //word.txt���� deleteWord�� ������ �ܾ ������ ����
			boolean contain = false; //deleteWord�� src���Ͽ� ������ true, ������ false.
			
			try {
				FileInputStream fi = new FileInputStream(src); //word.txt ���Ͽ��� �о�´�.
				FileOutputStream fo = new FileOutputStream(dest); //word2.txt ���Ͽ� �����Ѵ�.

				Scanner scanner = new Scanner(fi);
				while(scanner.hasNext()) { // ���� ������ �� �ܾ �д´�.
					String word = scanner.nextLine(); // �� ������ �а� '\n'�� ���� ������ ���ڿ��� ����
					if(word.equals(deleteWord)) { //deleteWord�� word.txt ���Ͽ��� �о�� word�� ��ġ
						contain = true;
						break;
					}
					else { 
						//deleteWord�� word.txt ���Ͽ��� �о�� word�� ��ġ���� ������ word2.txt���Ͽ� word ����
						word+="\r\n";
						fo.write(word.getBytes());
					}
				}
				scanner.close();

				fi.close();
				fo.close();

				if(contain) { //������ �ܾ word.txt�� ������ ����
					Path oldpath = Paths.get("word.txt");
					Path newpath = Paths.get("word2.txt");
					Files.move(newpath, oldpath, StandardCopyOption.REPLACE_EXISTING); //word2.txt ������ word.txt�� �̸� ����
					dest.delete(); //word2.txt ����

					JOptionPane.showMessageDialog(null, "������ �����߽��ϴ�.", "���� ����", JOptionPane.INFORMATION_MESSAGE);
				}
				else 
					JOptionPane.showMessageDialog(null, "������ �ܾ �����ϴ�.", "���� ����", JOptionPane.WARNING_MESSAGE);

			} catch(IOException e1) {
				return;
			}
		}
	}

	private class SearchWord implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//word.txt���� �˻��� �ܾ �Է� �޴� ���̾�α� ���
			String searchWord = JOptionPane.showInputDialog("�˻��� �ܾ �Է��ϼ���.");
			File src = new File("word.txt");
			boolean contain = false;
			try {
				FileInputStream fi = new FileInputStream(src);
				Scanner scanner = new Scanner(fi);
				while(scanner.hasNext()) { // ���� ������ ����
					String word = scanner.nextLine(); // �� ������ �а� '\n'�� ���� ������ ���ڿ��� ����
					if(word.equals(searchWord)) { //ã�� �ܾ� searchWord�� word.txt�� �� �ܾ�� ��ġ
						contain = true;
						break;
					}
				}
				scanner.close();

				if(contain) //ã�� �ܾ ������ ����
					JOptionPane.showMessageDialog(null, searchWord+"��/�� �ֽ��ϴ�.", "�˻� ����", JOptionPane.INFORMATION_MESSAGE);
				else //ã�� �ܾ ������ ����
					JOptionPane.showMessageDialog(null, searchWord+"��/�� �����ϴ�.", "�˻� ����", JOptionPane.WARNING_MESSAGE);

			} catch(IOException e1) {
				return;
			}
		}
	}

	//��ŷ�� ������ ������������ �����ϱ� ���� Ŭ����
	private class Ranking implements Comparable<Ranking> {
		private String name;
		private int score;

		public Ranking(String name, int score) {
			this.name = name;
			this.score = score;
		}
		
		@Override //score�� ������������ �����ϵ��� �޼ҵ� ������
		public int compareTo(Ranking o) {
			if(this.score < o.score) { 
				return 1;
			}
			else if (this.score == o.score) {
				return 0;
			}
			else {
				return -1;
			}
		}
	}
	
	//��ŷ�� ������ ���̾�α� ���� Ŭ����
	private class RankingDialog extends JDialog {
		private JScrollPane rankJScrollPane; //��ŷ�� ������ JScrollPane
		private JList<String> scrollList; //���ĵ� ��ŷ ����Ʈ�� ���� ����Ʈ

		public RankingDialog(JFrame frame, String title, Vector<String> rankingVector) {
			super(frame, title);
			setLayout(null);
			setBounds(1000, 100, 300, 350);
			
			scrollList = new JList<String>(rankingVector); //��ŷ�� ��ϵ� ���͸� ��ŷ ����Ʈ�� ����
			rankJScrollPane = new JScrollPane(scrollList); //��ŷ ����Ʈ�� JScrollPane�� ���� 
			
			scrollList.setFont(new Font("Gothic", Font.PLAIN, 15));
			rankJScrollPane.setBounds(20,30,250,250);
			
			add(rankJScrollPane); //���̾�α׿� rankJScrollPane ����
		}
	}
	
	//allRank �޴� �������� ���õǸ� ����. ��ü ��Ͽ��� 1������ 10������ ������ �����ִ� Ŭ����
	private class AllRank implements ActionListener {
		private List<Ranking> myRankingList = new ArrayList<Ranking>(); //Ranking��ü�� ������ ����Ʈ ����
		private Vector<String> rankingVector = new Vector<String>(); //myRankingList�� score �������� ������ ������ ���� ���� 
		private RankingDialog rankingDialog;
		
		public void actionPerformed(ActionEvent e) {
			File src = new File("ranking.txt"); //��ŷ�� ����� ����
			try {
				//��ŷ�� name�� score�� �и��ؼ� Ranking��ü�� �����ϰ� List�� ��´�.
				FileInputStream fi = new FileInputStream(src);
				Scanner scanner = new Scanner(fi);
				while(scanner.hasNext()) {
					String word = scanner.nextLine(); 
					String [] splitWord = new String[2];
					splitWord = word.split("\t");
					for(int i=0;i<2;i++) {
						splitWord[i] = splitWord[i].trim();
					}
					
					myRankingList.add(new Ranking(splitWord[0], Integer.parseInt(splitWord[1])));

				}
				scanner.close();

			} catch(IOException e1) {
				return;
			}
			
			Collections.sort(myRankingList); //score ���� ������������ ����
			
			//���� 10���� ���Ϳ� ����
			for(int i=0;i<10;i++) {
				rankingVector.add("[" + (i+1) + "]  " + "ID : " +myRankingList.get(i).name + "   ,   score : "+ myRankingList.get(i).score);
			}
			
			//Top 10 ����� ���̾�α׿� ����.
			rankingDialog = new RankingDialog(frame, "TOP 10", rankingVector);
			rankingDialog.setVisible(true);
		}
	}
	
	//myRank �޴� �������� ���õǸ� ����. ���� ���� �������� ������� ���� ����� �����ִ� Ŭ����
	private class MyRank implements ActionListener {
		private List<Ranking> myRankingList = new ArrayList<>(); //Ranking��ü�� ������ ����Ʈ ����
		private Vector<String> rankingVector = new Vector<String>(); //myRankingList�� score �������� ������ ������ ���� ���� 
		private RankingDialog rankingDialog;
		
		public void actionPerformed(ActionEvent e) {
			File src = new File("ranking.txt");
			try {
				FileInputStream fi = new FileInputStream(src);
				Scanner scanner = new Scanner(fi);
				while(scanner.hasNext()) { 
					String word = scanner.nextLine(); 
					String [] splitWord = new String[2];
					splitWord = word.split("\t");
					for(int i=0;i<2;i++) {
						splitWord[i] = splitWord[i].trim();
					}
					
					//��ŷ ���Ͽ� ��ϵ� ����� �̸��� ���� ���� �����ڿ� ������ ��ŷ ����Ʈ�� ����
					if(splitWord[0].equals(userName))
						myRankingList.add(new Ranking(splitWord[0], Integer.parseInt(splitWord[1])));

				}
				scanner.close();

			} catch(IOException e1) {
				return;
			}
			
			Collections.sort(myRankingList); //score ���� �������� ����
			
			for(int i=0;i<myRankingList.size();i++) { //��ŷ ���Ͽ� ��ϵ� ���� ����ڿ� �̸��� ���� ��� ����� ���Ϳ� ����
				rankingVector.add("[" + (i+1) + "]  " + "ID : " +myRankingList.get(i).name + "   ,   score : "+ myRankingList.get(i).score);
			}
			
			//���� ������� ����� ���̾�α׿� ����.
			rankingDialog = new RankingDialog(frame, "�� ����", rankingVector);
			rankingDialog.setVisible(true);
		}
		
	}

	//JComboBox���� ������ ���õǸ� ������ Ŭ����
	private class ChoosedLevel implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JComboBox<String> cb = (JComboBox<String>)e.getSource();
			chooseLevel = cb.getSelectedIndex(); //�޺��ڽ����� ���õ� ����
			gamePanel.setDelayPoint(chooseLevel); //gamePanel�� ���õ� ���� ����
			showPartsPanel.setLevel(chooseLevel); //showPartsPanel�� ���õ� ���� ����
			showEntirePanel.setLevel(chooseLevel); //showEntirePanel�� ���õ� ���� ����
		}
	}

}
