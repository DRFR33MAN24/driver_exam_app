package nemosofts.driving.exam;

import android.content.Context;
import android.content.SharedPreferences;
import nemosofts.driving.exam.callback.SerialKey;
import nemosofts.driving.exam.utils.Encrypted;

/* loaded from: library.aar:classes.jar:androidx/nemosofts/lk/Envato.class */
public class Envato {
    private final Encrypted encrypted;
    private final SharedPreferences sharedPrefEnvato;
    private final SharedPreferences.Editor editor;

    public Envato(Context context) {
        this.encrypted = new Encrypted(context);
        this.sharedPrefEnvato = context.getSharedPreferences(SerialKey.TAG_DATA, 0);
        this.editor = this.sharedPrefEnvato.edit();
    }

    public void setEnvatoKEY(String apikey) {
        this.editor.putString(SerialKey.TAG_API_KEY, this.encrypted.encrypt(apikey));
        this.editor.apply();
    }

    public Boolean isNetworkAvailable() {
        return Boolean.valueOf(this.sharedPrefEnvato.getBoolean(SerialKey.TAG_FIRST_TIME, true));
    }

    public Boolean getIsEnvato() {
        boolean verifyStatus;
        String envato_key = this.encrypted.decrypt(this.sharedPrefEnvato.getString(SerialKey.TAG_API_KEY, ""));
        String api_key = this.encrypted.decrypt(this.sharedPrefEnvato.getString(SerialKey.TAG_API_KEY_TEST, ""));
        if (envato_key.isEmpty()) {
            verifyStatus = false;
        } else if (api_key.isEmpty()) {
            verifyStatus = false;
        } else {
            verifyStatus = envato_key.equals(api_key);
        }
        if (!verifyStatus) {
            this.editor.putBoolean(SerialKey.TAG_FIRST_TIME, true);
            this.editor.apply();
        }
        return Boolean.valueOf(verifyStatus);
    }
}
