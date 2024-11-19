package Adapter;

public class GetTransaksi {
   private String id;
   private String id_user;
   private String id_pembayaran;
   private String status;
   private String upload_bukti;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_pembayaran() {
        return id_pembayaran;
    }

    public void setId_pembayaran(String id_pembayaran) {
        this.id_pembayaran = id_pembayaran;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpload_bukti() {
        return upload_bukti;
    }

    public void setUpload_bukti(String upload_bukti) {
        this.upload_bukti = upload_bukti;
    }

    public GetTransaksi(String id, String id_user, String id_pembayaran, String status, String upload_bukti) {
       this.id = id;
       this.id_user = id_user;
       this.id_pembayaran = id_pembayaran;
       this.status = status;
       this.upload_bukti = upload_bukti;


   }
}
