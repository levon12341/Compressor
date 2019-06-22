package exceptions;

public class CompressException extends Exception {
	private final String msg;
	
	public CompressException (String msg) {
		this.msg = msg;
	}
	
	public String getMessage() {
		return msg;
	}
}
