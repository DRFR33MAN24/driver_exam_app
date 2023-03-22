package nemosofts.driving.exam.interfaces;

import java.util.ArrayList;


import nemosofts.driving.exam.item.ItemVideo;

public interface VideosListener {
    void onStart();
    void onEnd(String success, String verifyStatus, String message, ArrayList<ItemVideo> arrayVidCat);
}