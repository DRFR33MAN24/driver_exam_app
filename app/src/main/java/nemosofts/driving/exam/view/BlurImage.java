package nemosofts.driving.exam.view;

import android.graphics.Bitmap;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* loaded from: library.aar:classes.jar:androidx/nemosofts/lk/view/BlurImage.class */
public class BlurImage {
    @Nullable
    public static Bitmap fastBlur(@NonNull Bitmap sentBitmap, float scale, int radius) {
        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        Bitmap sentBitmap2 = Bitmap.createScaledBitmap(sentBitmap, width, height, false);
        Bitmap bitmap = sentBitmap2.copy(sentBitmap2.getConfig(), true);
        if (radius < 1) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;
        int[] r = new int[wh];
        int[] g = new int[wh];
        int[] b = new int[wh];
        int[] vmin = new int[Math.max(w, h)];
        int divsum = (div + 1) >> 1;
        int divsum2 = divsum * divsum;
        int[] dv = new int[256 * divsum2];
        for (int i = 0; i < 256 * divsum2; i++) {
            dv[i] = i / divsum2;
        }
        int yi = 0;
        int yw = 0;
        int[][] stack = new int[div][3];
        int r1 = radius + 1;
        for (int y = 0; y < h; y++) {
            int bsum = 0;
            int gsum = 0;
            int rsum = 0;
            int boutsum = 0;
            int goutsum = 0;
            int routsum = 0;
            int binsum = 0;
            int ginsum = 0;
            int rinsum = 0;
            for (int i2 = -radius; i2 <= radius; i2++) {
                int p = pix[yi + Math.min(wm, Math.max(i2, 0))];
                int[] sir = stack[i2 + radius];
                sir[0] = (p & 16711680) >> 16;
                sir[1] = (p & 65280) >> 8;
                sir[2] = p & 255;
                int rbs = r1 - Math.abs(i2);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i2 > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            int stackpointer = radius;
            for (int x = 0; x < w; x++) {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];
                int rsum2 = rsum - routsum;
                int gsum2 = gsum - goutsum;
                int bsum2 = bsum - boutsum;
                int stackstart = (stackpointer - radius) + div;
                int[] sir2 = stack[stackstart % div];
                int routsum2 = routsum - sir2[0];
                int goutsum2 = goutsum - sir2[1];
                int boutsum2 = boutsum - sir2[2];
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                int p2 = pix[yw + vmin[x]];
                sir2[0] = (p2 & 16711680) >> 16;
                sir2[1] = (p2 & 65280) >> 8;
                sir2[2] = p2 & 255;
                int rinsum2 = rinsum + sir2[0];
                int ginsum2 = ginsum + sir2[1];
                int binsum2 = binsum + sir2[2];
                rsum = rsum2 + rinsum2;
                gsum = gsum2 + ginsum2;
                bsum = bsum2 + binsum2;
                stackpointer = (stackpointer + 1) % div;
                int[] sir3 = stack[stackpointer % div];
                routsum = routsum2 + sir3[0];
                goutsum = goutsum2 + sir3[1];
                boutsum = boutsum2 + sir3[2];
                rinsum = rinsum2 - sir3[0];
                ginsum = ginsum2 - sir3[1];
                binsum = binsum2 - sir3[2];
                yi++;
            }
            yw += w;
        }
        for (int x2 = 0; x2 < w; x2++) {
            int bsum3 = 0;
            int gsum3 = 0;
            int rsum3 = 0;
            int boutsum3 = 0;
            int goutsum3 = 0;
            int routsum3 = 0;
            int binsum3 = 0;
            int ginsum3 = 0;
            int rinsum3 = 0;
            int yp = (-radius) * w;
            for (int i3 = -radius; i3 <= radius; i3++) {
                int yi2 = Math.max(0, yp) + x2;
                int[] sir4 = stack[i3 + radius];
                sir4[0] = r[yi2];
                sir4[1] = g[yi2];
                sir4[2] = b[yi2];
                int rbs2 = r1 - Math.abs(i3);
                rsum3 += r[yi2] * rbs2;
                gsum3 += g[yi2] * rbs2;
                bsum3 += b[yi2] * rbs2;
                if (i3 > 0) {
                    rinsum3 += sir4[0];
                    ginsum3 += sir4[1];
                    binsum3 += sir4[2];
                } else {
                    routsum3 += sir4[0];
                    goutsum3 += sir4[1];
                    boutsum3 += sir4[2];
                }
                if (i3 < hm) {
                    yp += w;
                }
            }
            int yi3 = x2;
            int stackpointer2 = radius;
            for (int y2 = 0; y2 < h; y2++) {
                pix[yi3] = ((-16777216) & pix[yi3]) | (dv[rsum3] << 16) | (dv[gsum3] << 8) | dv[bsum3];
                int rsum4 = rsum3 - routsum3;
                int gsum4 = gsum3 - goutsum3;
                int bsum4 = bsum3 - boutsum3;
                int stackstart2 = (stackpointer2 - radius) + div;
                int[] sir5 = stack[stackstart2 % div];
                int routsum4 = routsum3 - sir5[0];
                int goutsum4 = goutsum3 - sir5[1];
                int boutsum4 = boutsum3 - sir5[2];
                if (x2 == 0) {
                    vmin[y2] = Math.min(y2 + r1, hm) * w;
                }
                int p3 = x2 + vmin[y2];
                sir5[0] = r[p3];
                sir5[1] = g[p3];
                sir5[2] = b[p3];
                int rinsum4 = rinsum3 + sir5[0];
                int ginsum4 = ginsum3 + sir5[1];
                int binsum4 = binsum3 + sir5[2];
                rsum3 = rsum4 + rinsum4;
                gsum3 = gsum4 + ginsum4;
                bsum3 = bsum4 + binsum4;
                stackpointer2 = (stackpointer2 + 1) % div;
                int[] sir6 = stack[stackpointer2];
                routsum3 = routsum4 + sir6[0];
                goutsum3 = goutsum4 + sir6[1];
                boutsum3 = boutsum4 + sir6[2];
                rinsum3 = rinsum4 - sir6[0];
                ginsum3 = ginsum4 - sir6[1];
                binsum3 = binsum4 - sir6[2];
                yi3 += w;
            }
        }
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return bitmap;
    }
}
