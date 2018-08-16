package com.coldstoragecoins;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.coldstoragecoins.db.DatabaseHelper;
import com.coldstoragecoins.models.Wallet;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

public class QRActivity extends BaseActivity implements QRCodeReaderView.OnQRCodeReadListener, View.OnClickListener {

    private QRCodeReaderView qrCodeReaderView;
    private TextView tvCancel;
    private DatabaseHelper db;
    public static boolean isScanned = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        db = new DatabaseHelper(this);
        setContentView(R.layout.activity_qr);
        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
        //openWalletActivity("1PpBQWG8XZoMZ5aoEZ1SjeEemxjU1XrsPy");
        //openWalletActivity("0x5fd847fb4993b8dac0eb43f3ecac13b47f151a57");
    }

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed in View
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Log.d("Scanned :", text);
        // Toast.makeText(this, "Scanned :" + text, Toast.LENGTH_SHORT).show();
        if (!text.equalsIgnoreCase("") && isScanned) {
            openWalletActivity(text);
            isScanned = false;
        }
        //  resultTextView.setText(text);
    }

    private void openWalletActivity(String text) {
        Wallet wallet = new Wallet(text, "", "", "", "", "", "", "");
        Intent intent = new Intent(QRActivity.this, WalletActivity.class);
        intent.putExtra("Wallet", wallet);
        startActivity(intent);
        finish();
        Log.d("", "");
    }


    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
