package nemosofts.driving.exam.utils;

import android.content.Context;
import com.yakivmospan.scytale.Crypto;
import com.yakivmospan.scytale.Options;
import com.yakivmospan.scytale.Store;
import javax.crypto.SecretKey;

/* loaded from: library.aar:classes.jar:androidx/nemosofts/lk/utils/Encrypted.class */
public class Encrypted {
    private final SecretKey key;

    public Encrypted(Context ctx) {
        Store store = new Store(ctx);
        if (!store.hasKey("envatoenc")) {
            this.key = store.generateSymmetricKey("envatoenc", (char[]) null);
        } else {
            this.key = store.getSymmetricKey("envatoenc", (char[]) null);
        }
    }

    public String encrypt(String value) {
        try {
            Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);
            return crypto.encrypt(value, this.key);
        } catch (Exception e) {
            Crypto crypto2 = new Crypto(Options.TRANSFORMATION_SYMMETRIC);
            return crypto2.encrypt("null", this.key);
        }
    }

    public String decrypt(String value) {
        try {
            Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);
            return crypto.decrypt(value, this.key);
        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }
    }
}
