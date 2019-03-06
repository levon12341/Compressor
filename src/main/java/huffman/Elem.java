package huffman;

public class Elem {
	private String code;
	private int frequence;
	
	public void setCode(String str) {
		code = str;
	}
	
	public void incrementFrequence() {
		frequence++;
	}
	
	public int getFrequence() {
		return frequence;
	}
	
	public String getCode() {
		return code;
	}
}