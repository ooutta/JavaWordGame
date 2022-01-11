import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

//������ �߾ӿ��� �Ǵ��� ��ǰ �̹����� �����ִ� �г�
public class ShowPartsPanel extends JPanel {
	private int level;
	private File dir; //������ ���� ������ �Ǵ� ��ǰ �̹��� ������ ��� ���丮 
	private File [] partsFile; //dir ��ο� �ִ� ��� �̹��� ������ �������� File �迭
	private ArrayList<File> partsFileList = new ArrayList<File>(); //ShowPartsPanel�� ����� �̹��� ����Ʈ
	private boolean once = true; //���� ���� �� ����Ʈ�� �̹��� ���ϵ��� �ҷ����� ���� ����

	public ShowPartsPanel() {
		this.setBackground(Color.BLACK);
		setLayout(null);
	}

	//������ ���� �ٸ� ���丮�� �ִ� �̹������� �����ͼ� ��ƾ� �� ��ǰ ����Ʈ�� partsFileList�� �߰��Ѵ�.
	public void setFile() {
		partsFileList.clear();
		if(level == 0) dir = new File("image/level1");
		else if (level == 1) dir = new File("image/level2");
		else dir = new File("image/level3");
		partsFile =  dir.listFiles();
		for(int i=0;i<partsFile.length;i++)
			partsFileList.add(partsFile[i]);
	}
	
	//������ ���� �ٸ��� ������ �̹��� ����Ʈ�� ���� �гο� �ٽ� �׸���.
	public void setLevel(int level) {
		this.level = level;
		setFile();
		repaint();
	}

	//���� ���� ��ư�� ���� ��� �̹��� ������ �ٽ� �����ͼ� ����Ѵ�.
	public ArrayList<File> firstGetImageFiles() {
		setFile();
		return partsFileList;
	}
	
	//ShowPartsPanel�� ��µ� �̹��� ���� ����Ʈ ����
	public ArrayList<File> imageFiles() {
		return partsFileList;
	}
	
	//ShowPartsPanel�� �ִ� ��� ��ǰ�� ������� true, ��ƾ� �� ��ǰ�� �������� false ����
	public boolean collectEntireParts() {
		if(partsFileList.size() == 0) {
			setFile();
			return true;
		}
		else return false;
	}

	//����ڰ� �̹��� ���̺��� ���� �ܾ ���缭 ������ ��� showPartsPanel������ �ش� �̹��� ���̺��� �����ǵ��� �Ѵ�.
	public void deleteImage(String path) {
		//������ �̹��� ������ ������ ������ �̹��� ������ ������ �ӽ� �̹��� ���� ����Ʈ
		ArrayList<File> temporaryFile = new ArrayList<File>(); 
		
		for(int i=0;i<partsFileList.size();i++) {
			//��ƾ� �� ��ǰ ����Ʈ�� partsFileList�� ����ڰ� ���� �̹����� path�� ��ΰ� ���� �̹����� ������ 
			//�ش� �̹����� �����ϰ� �ӽ� ���� ����Ʈ temporaryFile�� ����.
			if(!partsFileList.get(i).getPath().equals(path)) {
				temporaryFile.add(new File(partsFileList.get(i).getPath()));
			}
		}
		//partsFileList�� clear()�ϰ�, ������ �̹����� �����ϰ� ����� temporaryFile�� ��� �����Ѵ�.
		partsFileList.clear();
		partsFileList.addAll(temporaryFile);
		repaint();
	}
	
	//���� �гο� ��ƾ� �� ��ǰ�� (60x60) ũ��� ���
	public void drawImages(Graphics g) {
		int x = 0, y = 0;

		for(int i=0;i<partsFileList.size();i++) {
			File f = partsFileList.get(i);
			Image img = new ImageIcon(f.getPath()).getImage();
			if(x+60>this.getWidth()) {
				x = 0;
				y += 65;
			}
			g.drawImage(img, x, y, 60, 60, null);
			x+=70;
		}
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(once) { //������ �������� �ʾ��� ��� ����Ʈ�� �ѹ��� �̹��� ������ �����´�.
			setFile();
			once = false;
		}
		drawImages(g);
	}
}
