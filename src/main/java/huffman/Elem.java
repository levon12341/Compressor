package huffman;

public class Elem {
	private String code;
	private long frequence;
	private char letter;

	public Elem() {
		
	}

	public Elem(char c) {
		letter = c;
	}
	
	public void setCode(String str) {
		code = str;
	}
	
	public void incrementFrequence() {
		frequence++;
	}
	
	public long getFrequence() {
		return frequence;
	}

	public char getLetter() {
		return letter;
	}

	public void setFrequence(long freq) {
		frequence = freq;
	}
	
	public String getCode() {
		return code;
	}
}