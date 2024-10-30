package Adapter;

// Kelas untuk menyimpan dan mengelola data pengguna
public class GetUser {
    // Deklarasi variabel instance untuk menyimpan data pengguna
    private String id;          // Untuk menyimpan ID pengguna
    private String username;    // Untuk menyimpan nama pengguna
    private String email;       // Untuk menyimpan email pengguna
    private String phone;       // Untuk menyimpan nomor telepon pengguna
    private String uploadImage; // Untuk menyimpan path/URL gambar profil
    private String password;    // Untuk menyimpan password pengguna

    // Constructor untuk inisialisasi objek GetUser dengan semua atribut
    public GetUser(String id, String username, String email, String phone, String uploadImage, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.uploadImage = uploadImage;
        this.password = password;
    }

    // Getter dan Setter untuk setiap atribut
    // Getter untuk mengambil nilai uploadImage
    public String getUploadImage() {
        return uploadImage;
    }

    // Setter untuk mengubah nilai uploadImage
    public void setUploadImage(String uploadImage) {
        this.uploadImage = uploadImage;
    }

    // Getter untuk mengambil nilai id
    public String getId() {
        return id;
    }

    // Setter untuk mengubah nilai id
    public void setId(String id) {
        this.id = id;
    }

    // Getter untuk mengambil nilai username
    public String getUsername() {
        return username;
    }

    // Setter untuk mengubah nilai username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter untuk mengambil nilai email
    public String getEmail() {
        return email;
    }

    // Setter untuk mengubah nilai email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter untuk mengambil nilai phone
    public String getPhone() {
        return phone;
    }

    // Setter untuk mengubah nilai phone
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter untuk mengambil nilai password
    public String getPassword() {
        return password;
    }

    // Setter untuk mengubah nilai password
    public void setPassword(String password) {
        this.password = password;
    }
}
