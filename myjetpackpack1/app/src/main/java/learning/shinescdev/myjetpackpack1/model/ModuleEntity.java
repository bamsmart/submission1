package learning.shinescdev.myjetpackpack1.model;

public class ModuleEntity {
    private ContentEntity contentEntity;
    private String moduleId;
    private String courseId;
    private String mTitle;
    private int position;
    private Boolean mRead = false;

    public ModuleEntity(String moduleId, String courseId, String mTitle, int position, Boolean mRead) {
        this.moduleId = moduleId;
        this.courseId = courseId;
        this.mTitle = mTitle;
        this.position = position;
    if(mRead != null){
        this.mRead = mRead;
        }
    }

    public ContentEntity getContentEntity() {
        return contentEntity;
    }

    public void setContentEntity(ContentEntity contentEntity) {
        this.contentEntity = contentEntity;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Boolean getmRead() {
        return mRead;
    }

    public void setmRead(Boolean mRead) {
        this.mRead = mRead;
    }
}
