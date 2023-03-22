package nemosofts.driving.exam.interfaces;

/* loaded from: library.aar:classes.jar:androidx/nemosofts/lk/interfaces/ProductListener.class */
public interface ProductListener {
    void onStartPairing();

    void onConnected();

    void onUnauthorized(String str);

    void onError();
}
