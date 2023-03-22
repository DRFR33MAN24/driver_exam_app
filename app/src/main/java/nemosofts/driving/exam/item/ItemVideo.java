package nemosofts.driving.exam.item;

public class ItemVideo {

    private final String id;
    private final String title;
    private final String url;
    private final String thumb;

    public ItemVideo(String id, String title,String url, String thumb) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.thumb = thumb;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getThumb() {
        return thumb;
    }

}
