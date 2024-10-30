package Adapter;

public class Materi {
    private String id;
    private String fotoIcon;
    private String nama;

    public Materi(String id,String fotoIcon, String nama) {
        this.id = id;
        this.fotoIcon = fotoIcon;
        this.nama = nama;
    }
    public String getId() {
        return id;
    }
    public String getIcon() {
        return fotoIcon;
    }

    public String getNama() {
        return nama;
    }
}
