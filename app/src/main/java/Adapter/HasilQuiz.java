package Adapter;

public class HasilQuiz {
    private String id;
    private String user_id;
    private int jumlah_benar;
    private int jumlah_salah;
    private String tanggal;

    public HasilQuiz(String id, String user_id, int jumlah_benar, int jumlah_salah, String tanggal) {
        this.id = id;
        this.user_id = user_id;
        this.jumlah_benar = jumlah_benar;
        this.jumlah_salah = jumlah_salah;
        this.tanggal = tanggal;
    }
    public String getId(){return  id;}
    public String getUser_id(){
        return user_id;
    }
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

