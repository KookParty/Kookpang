package kookparty.kookpang.exception;

public class ValidationException extends Exception {
	public ValidationException() {
		super("입력값 검증에 실패했습니다.");
	}
	
	public ValidationException(String message) {
		super(message);
	}
}
