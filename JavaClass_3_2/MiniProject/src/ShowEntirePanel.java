import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

//������ �ϴ� �гο� ������ ���� ��ƾ� �� �Ǵ� ��ǰ�� �ϼ�ǰ �̹����� ����Ѵ�.
public class ShowEntirePanel extends JPanel {
	private int level;
	private int width, height; //�ϼ�ǰ�� ����, ���� ũ��
	private Image new_img; //�̹��� ũ�� ���� ���� �̹���
	private ImageIcon icon = null; // ������ ���� ������ �̹��� ������
	private int x = 0, y = 0; //�̹��� �������� ����� x,y ��ǥ
	
	//������ ���� �ٸ� �Ǵ� �ϼ�ǰ�� �����ͼ� ���
	public void setLevel(int level) {
		this.level = level;
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.BLACK);
		//������ ���� �ϼ��ؾ� �� �Ǵ��� �ϼ�ǰ�� �ٸ��� �����ش�.
		if(level == 0) //����1
			icon = new ImageIcon("image/level1.jpg");
		else if (level == 1) //����2
			icon = new ImageIcon("image/level2.jpg");
		else if (level == 2) //����3
			icon = new ImageIcon("image/level3.jpg");
		
		resizeImg(icon); //icon ũ�� ����
		g.drawImage(icon.getImage(), x, y, width, height, null);
	}
	
	//showEntirePanel�� ũ�⿡ �°� �̹��� ũ��� ��� ��ǥ�� ����
	public void resizeImg(ImageIcon icon) {
		Image ori_img=icon.getImage();
		double ratio;
		width = icon.getIconWidth();
		height = icon.getIconHeight();
		
		//�̹����� ũ�Ⱑ �������� ũ�ٸ� ũ�� ����
		if (width > 250 || height > 220) {
			if (width > height) { //�ʺ� ���̺��� ū ����. �ʺ� �������� ���� ���.
				ratio = (double) height / width;
				width = 250;
				height = (int) (width * ratio);
			} else { //���̰� �ʺ񺸴� ū �̹���. ���� �������� �ʺ� ���.
				ratio = (double) width / height;
				height = 220;
				width = (int) (height * ratio);
			}
			new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		}
		else {
			new_img = ori_img;
		}
		//�г��� �߾ӿ� ������ ��ǥ ����
		x = (this.getWidth()-width)/2;
		y = (this.getHeight()-height)/2;
	}
}
