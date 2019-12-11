package learning.shinescdev.myjetpackpack1.model;

public class CourseEntity {
    private String CourseId;
    private String title;
    private String description;
    private String deadline;
    private boolean bookmark;
    private String imagePath;

    public CourseEntity(String courseId, String title, String description, String deadline, boolean bookmark, String imagePath) {
        CourseId = courseId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.bookmark = bookmark;
        this.imagePath = imagePath;
    }

    public String getCourseId() {
        return CourseId;
    }

    public void setCourseId(String courseId) {
        CourseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public boolean isBookmark() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
