package kookparty.kookpang.controller.service;

import kookparty.kookpang.controller.dao.UserDao;
import kookparty.kookpang.dto.User;
import java.util.regex.Pattern;

public class UserService {
    private final UserDao dao = new UserDao();
    private static final Pattern EMAIL =
        Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public boolean emailTaken(String email) { return email != null && dao.existsByEmail(email); }
    public boolean nickTaken (String nick)  { return nick  != null && dao.existsByNickname(nick); }
    public boolean phoneTaken(String phone) { return phone != null && dao.existsByPhone(phone); } // 있으면 사용

    public User register(String email, String name, String nickname, String phone, String address) {
        if (email == null || name == null || nickname == null || phone == null)
            throw new IllegalArgumentException("필수값 누락");
        if (email.isBlank() || name.isBlank() || nickname.isBlank() || phone.isBlank())
            throw new IllegalArgumentException("필수값 누락");
        if (!EMAIL.matcher(email).matches())
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        if (dao.existsByEmail(email))
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        if (dao.existsByNickname(nickname))
            throw new IllegalStateException("이미 사용 중인 닉네임입니다.");
        // 전화 중복 체크도 쓸 거면 주석 해제
        // if (dao.existsByPhone(phone)) throw new IllegalStateException("이미 등록된 전화번호입니다.");

        User u = new User();
        u.setEmail(email);
        u.setName(name);
        u.setNickname(nickname);
        u.setPhone(phone);
        u.setAddress(address);
        return dao.insert(u);
    }
}
