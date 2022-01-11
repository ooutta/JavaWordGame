import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GamePanel extends JPanel {
	private JTextField input = new JTextField(40); //���� �ܾ� �Է��� �ʵ�
	private ScorePanel scorePanel = null; //������ ��� �г�. �̸�, ����, ������ �����ش�.
	private ShowPartsPanel showPartsPanel = null; //������ �߾� �г�. ��ƾ� �� ��ǰ �̹����� �����ش�.
	private GameGroundPanel ground = null; //�߾� �г�. �ܾ �������� ����.
	private TextSource textSource = null; //������ �ܾ� ���͸� ����.

	private Vector<JLabel> labelVector = new Vector<JLabel>(); //�������� �ܾ �ϳ��� ���Ϳ� ��´�.
	private Vector<FallingThread> fallVector = new Vector<FallingThread>(); //�������� �ܾ� �ϳ��� ������ �ϳ��� ���� ����.
	private ArrayList<File> partsImageFiles; //�Ǵ� ��ǰ �̹��� ������ ���� ����Ʈ

	private MakeLabelThread lbThread = null; //������ �ܾ� ������ �����ϴ� ������
	private InputThread inputThread = null; //�������� �ܾ ���߱� ���� �ܾ� �Է� ������
	private String userName; //����� �̸�
	private FileWriter fWriter; //��ŷ ����� ���� ��ü

	private String fallingWord = null; //�������� �ܾ�
	private int x; //�ܾ� ���̺��� x ��ǥ
	private long delay; //������ �ܾ �����Ǵ� �ð� ���� 
	private int point; //���� ����
	private int speed = 5; //�ܾ �������� �ӵ�
	private int life; //���� ����
	private boolean deleteOrNot; //�ܾ� ���̺��� ���������� �ƴ����� �Ǵ��� ����

	public GamePanel(ScorePanel scorePanel, ShowPartsPanel showPartsPanel) {
		this.scorePanel = scorePanel;
		this.showPartsPanel = showPartsPanel;

		ground = new GameGroundPanel(); //�ܾ �������� �г� ����
		setLayout(new BorderLayout());
		add(ground, BorderLayout.CENTER); //�ܾ �������� �г��� ground�� ���Ϳ� ����
		add(new InputPanel() ,BorderLayout.SOUTH); //�ܾ �Է��ϴ� InputPanel�� �Ʒ��� ����

		textSource = new TextSource("word.txt"); //�ܾ �о�� ������ word.txt�� ����
	}

	public void startGame() { //GameFrame���� startBtn ��ư�� ������ �Ҹ���.
		Container c = ground;
		c.removeAll();
		c.repaint(); //�ܾ ������ ground �гο� �ִ� ��� ���� ����
		deleteOrNot = false; //�ܾ� ���� ���θ� �Ǵ��� ������ default�� false�� ����

		partsImageFiles = showPartsPanel.firstGetImageFiles(); //�������� �ܾ� ���̺� ���ʿ� ���� ��ǰ �̹����� �����´�.
		lbThread = new MakeLabelThread(); // ������ �ܾ� ���̺� ���� ������ ����
		lbThread.start(); //������ �ܾ� ���̺� ���� ������ ����
		inputThread = new InputThread(); //�ܾ� �Է¹޴� ������ ����
		inputThread.start(); //�ܾ� �Է¹޴� ������ ����
	}

	public void stopGame() { //GameFrame���� stopBtn ��ư�� �����ų� life�� 0�� �Ǵ� ��� ����
		JLabel endLabel = new JLabel("**Game Over**");
		endLabel.setSize(400,50);
		endLabel.setLocation(100, 100);
		endLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		endLabel.setForeground(Color.MAGENTA);
		ground.add(endLabel);
		ground.repaint(); //������ ����Ǿ��ٴ� ���̺� ����

		lbThread.interrupt(); //�ܾ� ���� ������ ����
		inputThread.interrupt(); //�ܾ� �Է� �޴� ������ ����
		if(scorePanel.runningClearThreadOrStop()) //��� ��ǰ�� ��� scorePanel�� �����Ÿ��� �����尡 �������̶�� 
			scorePanel.stopSuccessThread(); //�ش� �����带 �����Ų��.

		for(int j=0;j<fallVector.size();j++) { //�������� �������� �����带 ��� ����
			fallVector.get(j).interrupt();
		}

		//��ũ ��Ͽ� userName�� ���� ���
		writeRank(userName + "\t" +scorePanel.getScore());

		scorePanel.reset(); //scorePanel �������� �ʱ�ȭ
	}

	public void hitFloor() { // �����尡 InputPanel�� �ִ� �ٴڿ� ��Ƽ� ������ �� ȣ��
		scorePanel.decrease(point); //������ ���� ������ŭ ����
		scorePanel.decreseLife(); //���� �ϳ� ����
	}

	public void setUserName(String userName) { //����� �̸� ����
		this.userName = userName;
	}

	//������ ���� �������� �ܾ� ���� �ӵ��� �����Ǵ� ������ �ٸ��� ����
	public void setDelayPoint(int chooseLevel) { 
		if(chooseLevel == 0)  { //����1
			delay = 4000;
			point = 10;
		}
		else if (chooseLevel == 1) { //����2
			delay =  2000;
			point = 15;
		}
		else { //����3
			delay =  1000;
			point = 20;
		}
	}
	
	//����� �̸��� ������ ���
	public void writeRank(String rank) {
		try {
			fWriter = new FileWriter("ranking.txt", true);
			fWriter.write(rank);
			fWriter.write("\r\n");
			fWriter.close();
		}
		catch(IOException e) {
			System.exit(0);
		}
	}

	//�������� �ܾ� ���̺� ���ʿ� �پ� �Բ� ������ �Ǵ� ��ǰ�� �̹��� ũ�� ����
	public ImageIcon changeImageSize(String path) {
		ImageIcon partsImg;
		partsImg = new ImageIcon(path);
		Image img = partsImg.getImage();
		Image chageImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		ImageIcon changeIcon = new ImageIcon(chageImg);
		return changeIcon;
	}

	//�������� �ܾ ���� ������
	class MakeLabelThread extends Thread {
		private String path = "";
		private int imgFileIndex; //�Ǵ� ��ǰ �ε���
		private int imgCount = 0; //�̹��� ���̺� ���� �ֱ�
		
		@Override
		public void run() {
			while(true) {
				try {
					//��� ��ǰ���� �� ������� 100�� �߰��� scorePanel�� �����Ÿ����� �Ѵ�.
					if(showPartsPanel.collectEntireParts()) { 
						scorePanel.increase(100);
						scorePanel.success();
					}
					
					life = scorePanel.getLife(); //scorePanel�� ��ϵ� ���� ���� ����

					if(life == 0) { //������ 0�̸� ���� ����
						stopGame();
						break;
					}

					fallingWord = textSource.getRandomWord(); //������ �ܾ �������� �ϳ� �����´�.
					JLabel label = new JLabel();
					label.setText(fallingWord);
					label.setSize(200, 30);
					x = (int)(Math.random()*(getWidth()-label.getWidth())); //���̺��� x ��ġ�� �����ϰ�.
					label.setLocation(x+30, 0); // ���̺� ��ġ				
					label.setForeground(Color.WHITE); //���̺��� ���� ���� �����Ѵ�.				
					label.setFont(new Font("Tahoma", Font.PLAIN, 20));
					ground.add(label); //ground �гο� �ܾ� ���̺��� �����ؼ� ���������� �Ѵ�.
					labelVector.add(label); //�������� �ܾ���� ���Ϳ� ����

					JLabel imgLabel = null;
					if(imgCount % 3 == 0) { //�ܾ� ���̺��� 3�� ������ �� �̹��� ���̺��� 1�� ���������� �Ѵ�.
						partsImageFiles = showPartsPanel.imageFiles(); //������ ���� ��ǰ �̹��� ��ü ������ �����´�.

						imgFileIndex = (int)(Math.random()*partsImageFiles.size()); //��ǰ�� �� �ϳ��� �������� ����

						path = partsImageFiles.get(imgFileIndex).getPath();

						imgLabel = new JLabel(changeImageSize(path)); //�̹����� ũ�⸦ ������ �� ���̺� ����
						imgLabel.setSize(30,30);
						imgLabel.setLocation(x, 0); //�̹��� ���̺��� ��ġ�� ũ�� ����
						ground.add(imgLabel); //ground �гο� �̹��� ���̺��� �����ؼ� ���������� �Ѵ�.
					}

					FallingThread thread = new FallingThread(ground, label , imgLabel, path);
					fallVector.add(thread); 
					thread.start(); //������ �ܾ� ���̺�� �̹��� ���̺��� delay �ֱ�� ���������� �ϴ� ������ ���� �� ���Ϳ� ���� ���� ����

					imgCount++;

					sleep(delay); //����1������ 4��, ����2�� 2��, ����3�� 1�� �������� �ܾ� ������ �����ȴ�.
				} catch (InterruptedException e) {
					return;
				}

			}
		}
	}

	//MakeLabelThread���� ������ ���̺��� speed��ŭ ���������� �ϴ� ������
	class FallingThread extends Thread {
		private GameGroundPanel ground; //�ܾ �������� �г��� GameGroundPanel�� ����
		private JLabel label; //�������� �ܾ ����ϴ� ���̺�
		private JLabel imgLabel; //�������� �̹����� ����ϴ� ���̺�
		private String path; //imgLabel�� ������ �̹����� ���

		public FallingThread(GameGroundPanel ground, JLabel label, JLabel imgJLabel, String path) {
			this.ground = ground;
			this.label = label;
			this.imgLabel = imgJLabel;
			this.path = path;
		}

		@Override
		public void run() {
			while(true) {
				try {
					int score = scorePanel.getScore(); //scorePanel���� ���� ������ �����´�.

					//score�� ���� �ܾ �������� y ��ǥ ���� ����
					if(score >= 240) speed = 20;
					else if (score >= 120) speed = 15;
					else speed = 10;
					
					int y = label.getY() + speed; //speed �ȼ��� �Ʒ��� �̵�
					if(y >= ground.getHeight()-label.getHeight()) { //�ܾ� ���̺��� �ٴڿ� ������
						Container c = label.getParent();
						c.remove(label); //���̺��� �θ� �����̳ʿ��� ����
						if(imgLabel != null) { //�̹��� ���̺��� �ִٸ�
							c.remove(imgLabel); //�̹��� ���̺� ����
							repaint(); //�ٽ� �׸���
						}
						hitFloor(); //�ٴڿ� �꿴���Ƿ� ������ ���� ����
						break; // ������ ����
					}

					label.setLocation(label.getX(), y); //���̺� ��ġ�� y�� ����
					if(imgLabel != null)   //�̹��� ���̺��� �ִٸ� �̹��� ���̺��� y ��ġ�� ����
						imgLabel.setLocation(label.getX()-30, y);
					GamePanel.this.repaint(); //�г� �ٽ� �׸���.

					
					sleep(500);
				} catch (InterruptedException e) { 
					//InputThead���� �ܾ �ùٸ��� ���� ��� �ܾ� ���̺�� �̹��� ���̺� ����
					Container c = label.getParent();
					c.remove(label);
					if(imgLabel != null) {
						c.remove(imgLabel);
						
						//�ܾ ���缭 interrupt�� �߻��� ��쿡 �Ǵ� ��ǰ�� �����ִ� showPartsPanel���� �ش� ����� �̹��� ����
						if(deleteOrNot) { 
							showPartsPanel.deleteImage(path);
						}
					}
					c.repaint(); 
					return;
				}

			}
		}	
	}

	//����ڰ� �ܾ �Է��� ground �гο��� �������� �ܾ �����ϴ� ������
	class InputThread extends Thread {
		private JLabel label;
		@Override
		public void run() {
			//input JTextField���� ���͸� �Է��ϸ� ����
			input.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JTextField t = (JTextField)e.getSource();
					String inWord = t.getText(); //����ڰ� �Է��� �ܾ�
					for(int j=0; j < labelVector.size() ; j++) {
						label = labelVector.get(j);
						//�������� �ܾ ����� labelVector���� ����ڰ� �Է��� �ܾ� inWord�� ������
						//������ ������Ű�� labelVector���� ���� �� �ش� FallingThread ����
						if(label.getText().equals(inWord)) {
							scorePanel.increase(point);
							t.setText("");
							labelVector.remove(label.getText());
							fallVector.get(j).interrupt();
							deleteOrNot = true; //�������� �ܾ ���� ����̹Ƿ� true
							return;
						}
					}

				}		
			});
		}
	}

	class GameGroundPanel extends JPanel { //�ܾ ����ϴ� ��. CENTER
		//��� �̹��� ����
		ImageIcon gamePanelIcon = new ImageIcon("image/gamePanelImg.jpg");
		Image gamePanelImg = gamePanelIcon.getImage();
		public GameGroundPanel() {
			setLayout(null);
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			//GameGroundPanel ũ��� ��� �̹��� ���
			g.drawImage(gamePanelImg,0,0,this.getWidth(), this.getHeight(), null);
		}
	}

	class InputPanel extends JPanel { //�ܾ� �Է��ϴ� ��. SOUTH
		public InputPanel() {
			setLayout(new FlowLayout());
			this.setBackground(Color.BLACK);
			add(input); //JTextField ����
		}
	}
}
