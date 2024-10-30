package Adapter;

public class MateriSub {
//    buat inisiasi var apa yg kmu perlukan contoh
//    id,nama,upload fie
    private String id;
    private String nama;
    private String upload_file;

//    buat parameter dan isi value dari private inisiasi var tdi dengan value parameter
    public MateriSub(String id,String nama,String upload_file) {
        this.id = id;
        this.nama = nama;
        this.upload_file = upload_file;
    }

//    program getter setter untuk nrima dan mengirimkan data
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
