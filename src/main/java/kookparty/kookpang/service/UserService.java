package kookparty.kookpang.service;

import java.util.regex.Pattern;
import kookparty.kookpang.dao.UserDAO;
import kookparty.kookpang.dto.UserDTO;

public class UserService {
    private final UserDAO dao = new UserDAO();
    private static final Pattern EMAIL =
        Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public boolean emailTaken(String email) { return email != null && dao.existsByEmail(email); }
    public boolean nickTaken (String nick)  { return nick  != null && dao.existsByNickname(nick); }
    public boolean phoneTaken(String phone) { return phone != null && dao.existsByPhone(phone); }
    public boolean isValidEmail(String email) {
        return email != null && EMAIL.matcher(email).matches();
    }


    /** 회원가입 */
    public UserDTO register(String email, String password, String name,
                            String nickname, String phone, String address) {
        if (email == null || password == null || name == null || nickname == null || phone == null)
            throw new IllegalArgumentException("필수 항목 누락");
        if (!EMAIL.matcher(email).matches())
            throw new IllegalArgumentException("이메일 형식 오류");
        if (emailTaken(email)) throw new IllegalArgumentException("이미 사용 중인 이메일");
        if (nickTaken(nickname)) throw new IllegalArgumentException("이미 사용 중인 닉네임");
        if (phoneTaken(phone)) throw new IllegalArgumentException("이미 등록된 전화번호");

        UserDTO u = new UserDTO();
        u.setEmail(email);
        u.setPassword(password); // 평문(임시) — 추후 해시로 교체
        u.setName(name);
        u.setNickname(nickname);
        u.setPhone(phone);
        u.setAddress(address);
        return dao.insert(u);
    }

    /** 로그인: 이메일+비밀번호 */
    public UserDTO login(String email, String password) {
        if (email == null || password == null || email.isBlank() || password.isBlank())
            throw new IllegalArgumentException("이메일/비밀번호를 입력해주세요.");
        if (!EMAIL.matcher(email).matches())
            throw new IllegalArgumentException("이메일 형식 오류");

        return dao.findByEmailAndPassword(email, password)
                  .orElseThrow(() -> new IllegalStateException("이메일 또는 비밀번호가 올바르지 않습니다."));
    }
}
