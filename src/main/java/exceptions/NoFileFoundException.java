package exceptions;
import java.nio.file.NoSuchFileException;

public class NoFileFoundException extends NoSuchFileException {
	private final String MESSAGE = "Файл не найден";
	
	public NoFileFoundException (String file) {
		super(file);
	}
	
	public String getMessage() {
		return MESSAGE;
	}
}
