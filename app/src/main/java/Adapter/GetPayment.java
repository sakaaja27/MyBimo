package Adapter;

public class GetPayment {
    private String id;
    private String nama_pembayaran;
    private String harga;
    private String nomor_bank;

    public GetPayment(String id, String nama_pembayaran, String harga, String nomor_bank){
        this.id = id;
        this.nama_pembayaran = nama_pembayaran;
        this.harga = harga;
        this.nomor_bank = nomor_bank;
    }

//    Getter Setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_pembayaran() {
        return nama_pembayaran;
    }

    public void setNama_pembayaran(String nama_pembayaran) {
        this.nama_pembayaran = nama_pembayaran;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getNomor_bank() {
        return nomor_bank;
    }

    public void setNomor_bank(String nomor_bank) {
        this.nomor_bank = nomor_bank;
    }
}
