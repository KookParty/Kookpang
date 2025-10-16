package kookparty.kookpang.exception;

public class NotFoundException extends Exception {
	public NotFoundException() {
		super("데이터가 존재하지 않습니다.");
	}
	
	public NotFoundException(String message) {
		super(message);
	}
}
