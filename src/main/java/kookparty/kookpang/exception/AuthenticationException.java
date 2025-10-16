package kookparty.kookpang.exception;
/**
 * 로그인할때 비번, 패스워드가 틀려려서 인증에 실패할때 발생하는 예외
 * */
public class AuthenticationException extends Exception {
	public AuthenticationException() {
		super("인증에 실패했습니다.");
	}
	
	public AuthenticationException(String message) {
		super(message);
	}
}
