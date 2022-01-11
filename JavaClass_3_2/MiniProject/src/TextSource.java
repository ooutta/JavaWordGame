import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;

//words.txt ������ �а� ���Ϳ� �����ϰ� ���ͷκ��� �����ϰ� �ܾ �����ϴ� Ŭ����
public class TextSource {
	private Vector<String> wordVector = new Vector<String>();
	
	public TextSource(String fileName) { //fileName ���Ͽ��� �ܾ� �ϳ��� �о�´�.
		try {
			Scanner scanner = new Scanner(new FileReader(fileName));
			while(scanner.hasNext()) { // ���� ������ �д´�.
				String word = scanner.nextLine(); // �� ������ �а� '\n'�� ���� ������ ���ڿ��� �����Ѵ�.
				wordVector.add(word); // ���ڿ��� ���Ϳ� ����
			}
			scanner.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("file not found error");
			System.exit(0);
		}
	}
	
	//wordVector�� ����� �ܾ� �� �ϳ��� �������� ����
	public String getRandomWord() {
		final int WORDMAX = wordVector.size(); // �� �ܾ��� ����
		int index = (int)(Math.random()*WORDMAX); 
		return wordVector.get(index);
	}
}



