package nemosofts.driving.exam.interfaces;

public interface SocialLoginListener {
    void onStart();
    void onEnd(String success, String registerSuccess, String message, String user_id, String user_name, String email,String id_num, String auth_id);
}