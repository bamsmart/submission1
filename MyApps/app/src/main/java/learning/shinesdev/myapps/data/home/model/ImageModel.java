package learning.shinesdev.myapps.data.home.model;

public class ImageModel {
    private String imgUrl;
    private String imgName;

    public  ImageModel(String name , String url){
     this.imgName = name;
     this.imgUrl = url ;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
}
