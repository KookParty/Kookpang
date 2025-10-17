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
    public boolean takenvaildated(String email){
        if (!isValidEmail(email)) throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        return dao.existsByEmail(email);
    }

    public UserDTO register(String email, String name, String nickname, String phone, String address) {
        if (email == null || name == null || nickname == null || phone == null)
            throw new IllegalArgumentException("필수값 누락");
        if (email.isBlank() || name.isBlank() || nickname.isBlank() || phone.isBlank())
            throw new IllegalArgumentException("필수값 누락");
        if (!EMAIL.matcher(email).matches())
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        if (dao.existsByEmail(email))     throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        if (dao.existsByNickname(nickname)) throw new IllegalStateException("이미 사용 중인 닉네임입니다.");
        // if (dao.existsByPhone(phone)) throw new IllegalStateException("이미 등록된 전화번호입니다.");

        UserDTO u = new UserDTO();
        u.setEmail(email);
        u.setName(name);
        u.setNickname(nickname);
        u.setPhone(phone);
        u.setAddress(address);
        return dao.insert(u);
    }

    public UserDTO loginByEmailPhone(String email, String phone) {
        if (email == null || phone == null || email.isBlank() || phone.isBlank())
            throw new IllegalArgumentException("이메일/전화번호를 입력해주세요.");
        if (!EMAIL.matcher(email).matches())
            throw new IllegalArgumentException("이메일 형식 오류");

        return dao.findByEmailAndPhone(email, phone)
                  .orElseThrow(() -> new IllegalStateException("일치하는 회원이 없습니다."));
    }
}
