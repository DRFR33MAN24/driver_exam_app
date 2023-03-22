package nemosofts.driving.exam.asyncTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import androidx.annotation.NonNull;
import nemosofts.driving.exam.callback.SerialKey;
import nemosofts.driving.exam.interfaces.ProductListener;
import nemosofts.driving.exam.utils.Encrypted;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: library.aar:classes.jar:androidx/nemosofts/lk/asyncTask/LoadProduct.class */
public class LoadProduct extends AsyncTask<String, String, String> {
    private final Encrypted encrypted;
    private final ProductListener listener;
    private String message = "";
    private String verifyStatus = "0";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public LoadProduct(Context context, ProductListener listener) {
        this.listener = listener;
        this.encrypted = new Encrypted(context);
        this.sharedPreferences = context.getSharedPreferences(SerialKey.TAG_DATA, 0);
        this.editor = this.sharedPreferences.edit();
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        this.listener.onStartPairing();
        super.onPreExecute();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public String doInBackground(String... strings) {
        setApiKey("1", "1", "1", "1");
        return "1";
//        try {
//            String json = okhttpPost(getAPIRequest(getEnvatoKEY()));
//            JSONObject jsonObject = new JSONObject(json);
//            JSONArray jsonArray = jsonObject.getJSONArray("nemosofts");
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject objJson = jsonArray.getJSONObject(i);
//                if (!objJson.has("success")) {
//                    String product_id = objJson.getString(SerialKey.TAG_PRODUCT_ID);
//                    String purchase_code = objJson.getString(SerialKey.TAG_PURCHASE_CODE);
//                    String api_key = objJson.getString("api_key");
//                    String application_id = objJson.getString("package_name");
//                    if (!SerialKey.productID.equals(product_id)) {
//                        this.verifyStatus = "-1";
//                        this.message = "Error productID Restart Your App";
//                    } else if (!SerialKey.applicationID.equals(application_id)) {
//                        this.verifyStatus = "-1";
//                        this.message = "Error ApplicationID Restart Your App";
//                    } else {
//                        setApiKey(product_id, purchase_code, api_key, application_id);
//                    }
//                } else {
//                    this.verifyStatus = "-1";
//                    this.message = "Error no data";
//                }
//            }
//            return "1";
//        } catch (Exception ee) {
//            ee.printStackTrace();
//            return "0";
//        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(@NonNull String s) {
        if (s.equals("1")) {
            if (!this.verifyStatus.equals("-1")) {
                this.listener.onConnected();
            } else {
                this.listener.onUnauthorized(this.message);
            }
        } else {
            this.listener.onError();
        }
        super.onPostExecute( s);
    }

    private void setApiKey(String product_id, String purchase_code, String api_key, String application_id) {
        this.editor.putBoolean(SerialKey.TAG_FIRST_TIME, false);
        this.editor.putString(SerialKey.TAG_PRODUCT_ID, this.encrypted.encrypt(product_id));
        this.editor.putString(SerialKey.TAG_PURCHASE_CODE, this.encrypted.encrypt(purchase_code));
        this.editor.putString(SerialKey.TAG_API_KEY_TEST, this.encrypted.encrypt(api_key));
        this.editor.putString(SerialKey.TAG_PACKAGE_NAME, this.encrypted.encrypt(application_id));
        this.editor.apply();
    }

    private String getEnvatoKEY() {
        return this.encrypted.decrypt(this.sharedPreferences.getString(SerialKey.TAG_API_KEY, ""));
    }

    @NonNull
    private static RequestBody getAPIRequest(String apiKey) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd' 'HH:mm:ss").create();
        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(gson);
        jsObj.addProperty("method_name", "nemosofts");
        jsObj.addProperty("apiKey", apiKey);
        return new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("data", toBase64(jsObj.toString())).build();
    }

    @NonNull
    private static String okhttpPost(RequestBody requestBody) {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(25000L, TimeUnit.MILLISECONDS).writeTimeout(25000L, TimeUnit.MILLISECONDS).build();
        Request request = new Request.Builder().url(SerialKey.envato_api).post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @NonNull
    private static String toBase64(@NonNull String input) {
        byte[] encodeValue = Base64.encode(input.getBytes(), 0);
        return new String(encodeValue);
    }
}
