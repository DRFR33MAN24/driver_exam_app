package nemosofts.driving.exam.asyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import nemosofts.driving.exam.callback.Callback;
import nemosofts.driving.exam.interfaces.CategoryListener;
import nemosofts.driving.exam.interfaces.VideosListener;
import nemosofts.driving.exam.item.ItemCat;
import nemosofts.driving.exam.item.ItemVideo;
import nemosofts.driving.exam.utils.ApplicationUtil;
import nemosofts.driving.exam.utils.DBHelper;
import okhttp3.RequestBody;

public class LoadVideos extends AsyncTask<String, String, String> {

    private final RequestBody requestBody;
    private final VideosListener videosListener;
    private final ArrayList<ItemVideo> arrayList = new ArrayList<>();
    private final DBHelper dbHelper;
    private String verifyStatus = "0", message = "";

    public LoadVideos(Context context, VideosListener videosListener, RequestBody requestBody) {
        this.videosListener = videosListener;
        this.requestBody = requestBody;
        dbHelper = new DBHelper(context);
    }

    @Override
    protected void onPreExecute() {
        dbHelper.removeAllVid();
        videosListener.onStart();
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
                    String vid = objJson.getString(Callback.TAG_VID);
                    String video_title = objJson.getString(Callback.TAG_VID_TITLE);
                    String video_url = objJson.getString(Callback.TAG_VID_URL);
                    String video_thumbnail = objJson.getString(Callback.TAG_VID_IMAGE);

                    ItemVideo objItem = new ItemVideo(vid, video_title,video_url, video_thumbnail);
                    arrayList.add(objItem);
                    dbHelper.addedVidList(objItem);
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
        videosListener.onEnd(s, verifyStatus, message, arrayList);
        super.onPostExecute(s);
    }
}