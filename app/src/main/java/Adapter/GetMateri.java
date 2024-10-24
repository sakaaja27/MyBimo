package Adapter;

public class GetMateri {
    private String id;
    private String id_materi;
    private String materi;
    private String namaSub;
    private String pdfData;

    public GetMateri(String id, String idMateri, String namaSub, String pdfData){
        this.id = id;
        this.namaSub = namaSub;
        this.pdfData = pdfData;

    }



    public String getId(){
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getId_materi(){return id_materi;}
    public void  setId_materi(String id_materi){this.id_materi = id_materi;}
    public String getMateri(){
        return materi;
    }
    public void setMateri(String materi){
        this.materi = materi;
    }
    public String getNamaSub(){
        return namaSub;
    }
    public void setNamaSub(String nama_sub){
        this.namaSub = nama_sub;
    }
    public String getPdfData(){
        return pdfData;
    }



}
