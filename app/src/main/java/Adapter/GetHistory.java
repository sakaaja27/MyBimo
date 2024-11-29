package Adapter;

public class GetHistory {
    private String id;
    private String user_id;
    private String id_materi;
    private String judul_materi;
    private int jumlah_benar;
    private int jumlah_salah;
    private String tanggal;

    public GetHistory(String id, String user_id, String id_materi, String judul_materi, int jumlah_benar, int jumlah_salah, String tanggal) {
        this.id = id;
        this.user_id = user_id;
        this.id_materi = id_materi;
        this.judul_materi = judul_materi;
        this.jumlah_benar = jumlah_benar;
        this.jumlah_salah = jumlah_salah;
        this.tanggal = tanggal;
    }

    public String getId(){return  id;}
    public String getUser_id(){
        return user_id;
  }
  public  String getId_materi(){return id_materi;}
    public String getJudul_materi(){return  judul_materi;}
  public int getJumlah_benar(){
        return jumlah_benar;
  }
  public int getJumlah_salah(){
        return jumlah_salah;
      }
  public String getTanggal(){
        return tanggal;
  }

}
