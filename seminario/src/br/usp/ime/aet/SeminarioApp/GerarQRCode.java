package br.usp.ime.aet.SeminarioApp;

import android.graphics.Bitmap;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import android.view.WindowManager;
import android.view.Display;
import android.graphics.Point;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class GerarQRCode extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gerar_qrcode);
        ImageView imageView = (ImageView) findViewById(R.id.qrCode);
        try {
            Bitmap bitmap = encodeAsBitmap("hello world");
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {}
    }

    private Bitmap encodeAsBitmap(String str) throws WriterException {
        // Tamanho da imagem
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point dimensoes = new Point();
        display.getSize(dimensoes);
        int larg = dimensoes.x;
        int alt = dimensoes.y;
        int lado = larg < alt ? larg : alt;
         
        BitMatrix matrix;
        try {
            matrix = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, lado, lado, null);
        } catch (IllegalArgumentException iae) {
            return null;
        }
        int w = matrix.getWidth();
        int h = matrix.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, lado, 0, 0, w, h);
        return bitmap;
    }

}

