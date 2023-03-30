
package nemosofts.driving.exam.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import nemosofts.driving.exam.ProCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import nemosofts.driving.exam.R;
import nemosofts.driving.exam.adapter.AdapterCategory;
import nemosofts.driving.exam.adapter.AdapterQuestionsCategories;
import nemosofts.driving.exam.adapter.AdapterVideos;
import nemosofts.driving.exam.asyncTask.LoadCat;
import nemosofts.driving.exam.asyncTask.LoadQuestionsCat;
import nemosofts.driving.exam.asyncTask.LoadVideos;
import nemosofts.driving.exam.callback.Callback;
import nemosofts.driving.exam.ifSupported.IsRTL;
import nemosofts.driving.exam.ifSupported.IsScreenshot;
import nemosofts.driving.exam.interfaces.CategoryListener;
import nemosofts.driving.exam.interfaces.QuestionsCatListener;
import nemosofts.driving.exam.interfaces.VideosListener;
import nemosofts.driving.exam.item.ItemCat;
import nemosofts.driving.exam.item.ItemQuestionsCat;
import nemosofts.driving.exam.item.ItemVideo;
import nemosofts.driving.exam.utils.ApplicationUtil;
import nemosofts.driving.exam.utils.DBHelper;
import nemosofts.driving.exam.utils.Helper;


public class QuestionsCategoriesActivity extends ProCompatActivity {

    private Helper helper;
    private DBHelper dbHelper;
    private RecyclerView rv;
    private ArrayList<ItemQuestionsCat> arrayList;
    private GridLayoutManager glm;
    private ProgressBar pb;
    private FrameLayout frameLayout;
    private String error_msg;
    private FloatingActionButton fab;
    private String mode = "questions";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IsRTL.ifSupported(this);
        IsScreenshot.ifSupported(this);

        dbHelper = new DBHelper(this);
        helper = new Helper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.mode = extras.getString("key");

            //The key argument here must match that used in the other activity
        }
        helper = new Helper(this, (position, type) -> {
            if (mode =="questions"){
                Intent intent = new Intent(QuestionsCategoriesActivity.this, QuizPaperActivity.class);
                intent.putExtra("q_cat_id", arrayList.get(position).getId());
                intent.putExtra("name", arrayList.get(position).getName());
            }
            else{
                Intent intent = new Intent(QuestionsCategoriesActivity.this, QuizDetails.class);
                intent.putExtra("q_cat_id", arrayList.get(position).getId());
                intent.putExtra("name", arrayList.get(position).getName());
            }



        });

        arrayList = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        pb = findViewById(R.id.pb);
        rv = findViewById(R.id.rv);
        frameLayout = findViewById(R.id.fl_empty);

        glm = new GridLayoutManager(QuestionsCategoriesActivity.this, 1);
        rv.setLayoutManager(glm);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);

        fab = findViewById(R.id.fab);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = glm.findFirstVisibleItemPosition();

                if (firstVisibleItem > 6) {
                    fab.show();
                } else {
                    fab.hide();
                }
            }
        });

        fab.setOnClickListener(v -> rv.smoothScrollToPosition(0));

        arrayList = dbHelper.getQCat();
        if (!arrayList.isEmpty()){
            setAdapterToListview();
        } else {
            getData();
        }

        findViewById(R.id.iv_refresh).setOnClickListener(view -> getData());

        LinearLayout adView = findViewById(R.id.ll_adView);
        helper.showBannerAd(adView);
    }

    private void getData() {
        if (helper.isNetworkAvailable()){
            LoadQuestionsCat startLoad = new LoadQuestionsCat(this, new QuestionsCatListener() {
                @Override
                public void onStart() {
                    if (!arrayList.isEmpty()){
                        arrayList.clear();
                    }
                    rv.setVisibility(View.GONE);
                    pb.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.GONE);
                }

                @Override
                public void onEnd(String success, String verifyStatus, String message, ArrayList<ItemQuestionsCat> questionsCatArrayList) {
                    if (success.equals("1")) {
                        if (!verifyStatus.equals("-1")) {
                            if (questionsCatArrayList.isEmpty()) {
                                error_msg = getString(R.string.error_no_data_found);
                                setEmpty();
                            } else {
                                arrayList = dbHelper.getQCat();
                                if (!arrayList.isEmpty()){
                                    setAdapterToListview();
                                }else {
                                    error_msg = getString(R.string.error_no_data_found);
                                    setEmpty();
                                }
                            }
                        } else {
                            helper.getVerifyDialog(getString(R.string.error_unauthorized_access), message);
                        }
                    }else {
                        error_msg = getString(R.string.error_server_not_connected);
                        setEmpty();
                    }
                }
            },helper.callAPI(Callback.METHOD_Q_CAT, 0, "","", "", "", "", "", "", "", "", "", "", "", "", null));
            startLoad.execute();
        }else {
            error_msg = getString(R.string.error_internet_not_connected);
            setEmpty();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setAdapterToListview() {
        AdapterQuestionsCategories adapter = new AdapterQuestionsCategories(arrayList, (itemQ_Cat, position) -> helper.showInter(position,""));
        rv.setAdapter(adapter);
        setEmpty();
    }

    private void setEmpty() {
        if (arrayList.size() > 0) {
            rv.setVisibility(View.VISIBLE);
            pb.setVisibility(View.INVISIBLE);
            frameLayout.setVisibility(View.GONE);
        } else {
            pb.setVisibility(View.INVISIBLE);
            rv.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);

            frameLayout.removeAllViews();

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View myView = inflater.inflate(R.layout.layout_empty, null);

            TextView textView = myView.findViewById(R.id.tv_empty_msg);
            textView.setText(error_msg);

            myView.findViewById(R.id.btn_empty_try).setOnClickListener(v -> getData());

            frameLayout.addView(myView);
        }
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_questions_categories;
    }

    @Override
    protected int setApplicationThemes() {
        return ApplicationUtil.isTheme();
    }
}