package carousel;

public class Materi {
    private String fotoIcon;
    private String nama;

    public Materi(String fotoIcon, String nama) {
        this.fotoIcon = fotoIcon;
        this.nama = nama;
    }

    public String getIcon() {
        return fotoIcon;
    }

    public String getNama() {
        return nama;
    }
}
