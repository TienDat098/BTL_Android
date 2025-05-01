package ntd.ntu.btl_64130297_quizz;

public class VideoItem {
    private String title;
    private  String url;
    private int image_video;

    public VideoItem(String title, String url, int image_video) {
        this.title = title;
        this.url = url;
        this.image_video = image_video;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getImage_video() {
        return image_video;
    }

    public void setImage_video(int image_video) {
        this.image_video = image_video;
    }
}
