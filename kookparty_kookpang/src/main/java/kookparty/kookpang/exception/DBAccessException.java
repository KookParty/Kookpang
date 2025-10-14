package kookparty.kookpang.exception;

public class DBAccessException extends Exception {
	public DBAccessException() {
		super("데이터베이스 접근 중 요류가 발생했습니다.");
	}
	
	public DBAccessException(String message) {
		super(message);
	}
}
