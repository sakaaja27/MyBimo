package Adapter;

public class GetNamaMateri {
    private String id;
    private String judul_materi;
    private String foto_icon;
    private String status_materi;


    public GetNamaMateri(String id, String judul_materi, String foto_icon, String status_materi) {
        this.id = id;
        this.judul_materi = judul_materi;
        this.foto_icon = foto_icon;
        this.status_materi = status_materi;

    }

    // Getter dan Setter untuk setiap atribut


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul_materi() {
        return judul_materi;
    }

    public void setJudul_materi(String judul_materi) {
        this.judul_materi = judul_materi;
    }

    public String getFoto_icon() {
        return foto_icon;
    }

    public void setFoto_icon(String foto_icon) {
        this.foto_icon = foto_icon;
    }

    public String getStatus_materi() {
        return status_materi;
    }

    public void setStatus_materi(String status_materi) {
        this.status_materi = status_materi;
    }

}

