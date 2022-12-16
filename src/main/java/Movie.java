public class Movie extends Content {
    
    private String videoURL;
    private long resumeTime;

    public Movie() {
        super();
    }

    @Override
    public String getVideoURL() {
        return videoURL;
    }

    @Override
    public long getResumeTime() {
        return resumeTime;
    }

    
}
