import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EnterPanel extends JFrame {
	private JLabel user; 
	private JTextField userName; //user name�� �Է� ���� text field
	private ImageIcon backImgIcon = new ImageIcon("image/enterImg.jpg"); //��� �̹����� �����´�.
	private Image backImg = backImgIcon.getImage(); 
	
	public EnterPanel() {
		setTitle("�Ǵ� ���� Ÿ���� ����");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,600);
		setContentPane(new FirstPanel()); //����Ʈ�� ����
		
		//userName field�� �̸��� �Է��ϰ� ���͸� ������ ���� ȭ���� GameFrame���� �̵�
		userName.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				GameFrame f = new GameFrame(userName.getText());
				setVisible(false); //���� �г��� ������ �ʵ��� �Ѵ�.
			}
		});
		
		setResizable(false); //â ũ�� ���� �Ұ�
		setVisible(true);
	}
	
	private class FirstPanel extends JPanel {
		public FirstPanel() {
			setLayout(null);
			user = new JLabel("ID");
			user.setForeground(Color.WHITE);
			user.setBounds(70,250,100,50);
			user.setFont(new Font("Gothic", Font.BOLD, 30));

			userName = new JTextField(); //userName�� �Է��� ����
			userName.setBounds(150,250,200,50);
			userName.setFont(new Font("Gothic", Font.BOLD, 30));
			
			add(user);
			add(userName);
		}
		
		//���� �г� ��ü�� ��� �̹����� �׸���.
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backImg,0,0,this.getWidth(), this.getHeight(), null);
		}
	}
	
	public static void main(String[] args) {
		new EnterPanel();
	}
}



