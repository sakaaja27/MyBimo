package Adapter;

public class MateriSub {
//    buat inisiasi var apa yg kmu perlukan contoh
//    id,nama,upload fie
    private String id;
    private String judul_materi;
    private String nama;
    private String fotoIcon;
    private String upload_file;



    //    buat parameter dan isi value dari private inisiasi var tdi dengan value parameter
    public MateriSub(String id,String judul_materi,String nama,String fotoIcon,String upload_file) {
        this.id = id;
        this.judul_materi = judul_materi;
        this.nama = nama;
        this.upload_file = upload_file;
    }

//    program getter setter untuk nrima dan mengirimkan data

    public String getFotoIcon() {
        return fotoIcon;
    }

    public void setFotoIcon(String fotoIcon) {
        this.fotoIcon = fotoIcon;
    }

    public String getJudul_materi() {
        return judul_materi;
    }

    public void setJudul_materi(String judul_materi) {
        this.judul_materi = judul_materi;
    }

    public String getNama() {
        return nama;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpload_file() {
        return upload_file;
    }

    public void setUpload_file(String upload_file) {
        this.upload_file = upload_file;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
