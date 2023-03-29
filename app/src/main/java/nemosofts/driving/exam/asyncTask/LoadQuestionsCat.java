package nemosofts.driving.exam.asyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import nemosofts.driving.exam.callback.Callback;
import nemosofts.driving.exam.interfaces.CategoryListener;
import nemosofts.driving.exam.interfaces.QuestionsCatListener;
import nemosofts.driving.exam.interfaces.VideosListener;
import nemosofts.driving.exam.item.ItemCat;
import nemosofts.driving.exam.item.ItemQuestionsCat;
import nemosofts.driving.exam.item.ItemVideo;
import nemosofts.driving.exam.utils.ApplicationUtil;
import nemosofts.driving.exam.utils.DBHelper;
import okhttp3.RequestBody;

public class LoadQuestionsCat extends AsyncTask<String, String, String> {

    private final RequestBody requestBody;
    private final QuestionsCatListener qCatListener;
    private final ArrayList<ItemQuestionsCat> arrayList = new ArrayList<>();
    private final DBHelper dbHelper;
    private String verifyStatus = "0", message = "";

    public LoadQuestionsCat(Context context, QuestionsCatListener qCatListener, RequestBody requestBody) {
        this.qCatListener = qCatListener;
        this.requestBody = requestBody;
        dbHelper = new DBHelper(context);
    }

    @Override
    protected void onPreExecute() {
        dbHelper.removeAllQCat();
        qCatListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String json = ApplicationUtil.responsePost(Callback.API_URL, requestBody);
            Log.d("dbg",json);
            JSONObject mainJson = new JSONObject(json);
            JSONArray jsonArray = mainJson.getJSONArray(Callback.TAG_ROOT);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objJson = jsonArray.getJSONObject(i);

                if (!objJson.has(Callback.TAG_SUCCESS)) {
                    String q_cat_id = objJson.getString(Callback.TAG_Q_CAT_ID);
                    String q_cat_name = objJson.getString(Callback.TAG_Q_CAT_NAME);


                    ItemQuestionsCat objItem = new ItemQuestionsCat(q_cat_id, q_cat_name);
                    arrayList.add(objItem);
                    dbHelper.addedQCatList(objItem);
                } else {
                    verifyStatus = objJson.getString(Callback.TAG_SUCCESS);
                    message = objJson.getString(Callback.TAG_MSG);
                }
            }
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        qCatListener.onEnd(s, verifyStatus, message, arrayList);
        super.onPostExecute(s);
    }
}