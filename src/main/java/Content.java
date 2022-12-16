public abstract class Content {
    
    private int id;
    private String title;
    private String poster_path;
    private ContentType type;
    private boolean watching;

    public Content() {}

    public Content(int id, String title, ContentType type, String poster_path, boolean watching) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.poster_path = poster_path;
        this.watching = watching;
    }

    public enum ContentType { MOVIE, SERIES };

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public void setPosterPath(String poster_path) {
        this.poster_path = poster_path;
    }

    public ContentType getType() {
        return type;
    }

    public void setType(ContentType type) {
        this.type = type;
    }

    public boolean getWatching() {
        return watching;
    }

    public void setWatching(boolean watching) {
        this.watching = watching;
    }

    /**
     * If the content is a series this will be the episode title if it has one. If it is a movie it will just be the title.
     * @return Title
     */
    public String getCurrentTitle() {
        return title;
    }

    /**
     * The URL of the video that is to be played. For a movie, there is only one. For a series, this will depend on which episode is to be played.
     * @return String URL
     */
    public abstract String getVideoURL();

    public abstract long getResumeTime();

}
