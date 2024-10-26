package Adapter;

public class GetUser {
    private String id;
    private String username;
    private String email;
    private String phone;
    private String uploadImage;
    private String password;

    public GetUser(String id, String username, String email,String phone,String uploadImage, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.uploadImage = uploadImage;
        this.password = password;
        }

    // Getter dan Setter untuk setiap atribut


    public String getUploadImage() {
        return uploadImage;
    }

    public void setUploadImage(String uploadImage) {
        this.uploadImage = uploadImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
