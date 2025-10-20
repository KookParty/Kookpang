package kookparty.kookpang.dto;

public class UserDTO {
    private long userId;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String phone;
    private String address;
    private String role;
    private int status;

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }        // ← 추가
    public void setPassword(String password) { this.password = password; } // ← 추가

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}
