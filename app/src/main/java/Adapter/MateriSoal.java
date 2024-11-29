package Adapter;

public class MateriSoal {
    private int id;
    private String nama_soal;
    private String jawaban_a;
    private String jawaban_b;
    private String jawaban_c;
    ;

    public MateriSoal(int id, String nama_soal, String jawaban_a, String jawaban_b, String jawaban_c) {
        this.id = id;
        this.nama_soal = nama_soal;
        this.jawaban_a = jawaban_a;
        this.jawaban_b = jawaban_b;
        this.jawaban_c = jawaban_c;

    }

    public Integer getId(){
        return id;
    }
    public String getNama_soal(){
        return nama_soal;
    }
    public String getJawaban_a(){return jawaban_a;}
    public String getJawaban_b(){return jawaban_b;}
    public String getJawaban_c(){return jawaban_c;}



}
