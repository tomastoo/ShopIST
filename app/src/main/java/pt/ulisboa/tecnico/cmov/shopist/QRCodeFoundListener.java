package pt.ulisboa.tecnico.cmov.shopist;

public interface QRCodeFoundListener {
    void onQRCodeFound(String qrCode);
    void qrCodeNotFound();
}
