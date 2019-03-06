package exceptions;

import java.nio.file.NoSuchFileException;

public class NoTableFoundException extends NoSuchFileException {
	private final String MESSAGE = "Файл кодировочной таблицы не найден";
	
	
	public NoTableFoundException(String path) {
		super(path);
	}


	public String getMessage() {
		return MESSAGE;
	}
}
