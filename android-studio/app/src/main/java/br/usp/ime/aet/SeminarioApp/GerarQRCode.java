// Referência:
// http://www.mysamplecode.com/2012/09/android-generate-qr-code-using-zxing.html

package br.usp.ime.aet.SeminarioApp;

import android.graphics.Bitmap;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.view.WindowManager;
import android.view.Display;
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
            Bitmap bitmap = encodeAsBitmap(getIntent().getStringExtra("id_seminario"));
            imageView.setImageBitmap(bitmap);
        }
        catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private Bitmap encodeAsBitmap(String str) throws WriterException {
        // Tamanho da imagem
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int larg = display.getHeight();
        int alt = display.getWidth();
        int lado = larg < alt ? larg : alt;

        BitMatrix matrix;
        try {
            matrix = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, lado, lado, null);
        }
        catch (IllegalArgumentException iae) {
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

