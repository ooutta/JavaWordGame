import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

//������ ��� �г�. �̸�, ����, ���� ǥ��
public class ScorePanel extends JPanel {
	private int score = 0; //������ 0���� �ʱ�ȭ
	private int life = 5; //������ 5�� �ʱ�ȭ
	private JLabel nameLabel = new JLabel("�̸�"); 
	private JLabel scoreLabel = new JLabel("���� : "+Integer.toString(score)); //���� ���̺�
	private JLabel lifeLabel = new JLabel("���� : " + life);
	private JLabel success = new JLabel("��� ��ǰ ������ ����!!"); //��� ��ǰ�� ���� ��� scorePanel�� ��µǴ� ���̺�
	
	private SuccessThread th; //��� ��ǰ �����⿡ ������ ��� ����Ǵ� ������

	public ScorePanel() {
		setLayout(null);
		makeLabels();
	}

	//scorePanel�� ���̺���� ����
	public void makeLabels() {
		nameLabel.setBounds(10,10,70,20);
		nameLabel.setForeground(Color.WHITE);
		add(nameLabel);

		scoreLabel.setText("���� : "+Integer.toString(score));
		scoreLabel.setSize(100,20);
		scoreLabel.setLocation(10,30);
		scoreLabel.setForeground(Color.WHITE);
		add(scoreLabel);
		
		lifeLabel.setText("���� : " + life);
		lifeLabel.setSize(100,20);
		lifeLabel.setLocation(10,50);
		lifeLabel.setForeground(Color.WHITE);
		add(lifeLabel);
		
		setBackground(Color.BLACK);
	}

	public void increase(int point) { //���� ����
		score+=point;
		scoreLabel.setText("���� : "+Integer.toString(score));
	}

	public void decrease(int point) { //���� ����
		score-=point;
		scoreLabel.setText("���� : "+Integer.toString(score));
	}

	public void reset() { //score�� life�� �ʱ�ȭ
		score = 0;
		scoreLabel.setText("���� : "+Integer.toString(score));
		life = 5;
		lifeLabel.setText("���� : " + life);
	}

	public int getScore() { 
		return score;
	}
	
	public void decreseLife() { //life�� 1�� ����
		life--;
		lifeLabel.setText("���� : " + life);
	}
	
	public int getLife() {
		return life;
	}

	//GameFrame���� �޾ƿ� userName���� nameLabel�� �ؽ�Ʈ ����
	public void setUserName(String userName) { 
		nameLabel.setText("�̸� : "+userName);
	}

	//��� ��ǰ �����⿡ ������ ��� �����带 �����ϰ� �����Ѵ�.
	public void success() {
		th = new SuccessThread();
		th.start();
	}

	//�������� scorePanel�� �����Ÿ��� ������ ����
	public void stopSuccessThread() {
		th.interrupt();
	}

	//�����尡 ������ ���������� ����� ���������� ���� true, false return.
	public boolean runningClearThreadOrStop() {
		if(th == null) { //�����尡 ���� ����
			return false;
		}
		else {
			Thread.State state = th.getState();
			if(state == Thread.State.TERMINATED
					|| state == Thread.State.NEW
					||  state == Thread.State.RUNNABLE) { //�������� �ƴ� ����
				return false;
			}
			else { //������
				return true;
			}
		}
	}

	//��� ��ǰ �����⿡ �����ϸ� ����Ǵ� ������
	class SuccessThread extends Thread {
		public void run() {
			//���� srocePanel�� �ִ� ���̺��� ��� ����� success ���̺� ����
			remove(nameLabel);
			remove(scoreLabel);
			remove(lifeLabel);
			
			success.setSize(300,40);
			success.setLocation(10,10);
			success.setForeground(Color.WHITE);
			success.setFont(new Font("Gothic", Font.BOLD, 20));
			add(success);

			int n = 0, i = 0;
			while(true) {
				if(i == 4) { //success ���̺��� �� �� �����̰� ����
					remove(success);
					makeLabels();
					repaint();
					interrupt();
				}
				i++;

				if(n == 0) 
					setBackground(Color.BLACK);
				else
					setBackground(Color.YELLOW);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					return;
				}
				if(n==0) n=1;
				else n=0;
			}
		}
	}
}
