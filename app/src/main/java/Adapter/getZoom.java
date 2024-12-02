package Adapter;

public class getZoom {
    private String id;
    private String nama_judul;
    private String link_zoom;
    private String status_zoom;
    private String tanggal;
    private String waktu;

    public getZoom(String id,String nama_judul,String link_zoom,String status_zoom,String tanggal,String waktu){
        this.id = id;
        this.nama_judul = nama_judul;
        this.link_zoom = link_zoom;
        this.status_zoom = status_zoom;
        this.tanggal = tanggal;
        this.waktu = waktu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_judul() {
        return nama_judul;
    }

    public void setNama_judul(String nama_judul) {
        this.nama_judul = nama_judul;
    }

    public String getLink_zoom() {
        return link_zoom;
    }

    public void setLink_zoom(String link_zoom) {
        this.link_zoom = link_zoom;
    }

    public String getStatus_zoom() {
        return status_zoom;
    }

    public void setStatus_zoom(String status_zoom) {
        this.status_zoom = status_zoom;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
