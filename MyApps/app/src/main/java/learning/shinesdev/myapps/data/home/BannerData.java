package learning.shinesdev.myapps.data.home;

import android.media.Image;

import java.util.ArrayList;
import java.util.List;

import learning.shinesdev.myapps.data.home.model.ImageModel;

public class BannerData {

    public ArrayList<ImageModel> getImageList(){
        ArrayList<ImageModel> image = new ArrayList<>();

        image.add(new ImageModel("Mandalorian","https://image.tmdb.org/t/p/w1000_and_h563_face/o7qi2v4uWQ8bZ1tW3KI0Ztn2epk.jpg"));
        image.add(new ImageModel("Frozen II","https://image.tmdb.org/t/p/w1000_and_h563_face/xJWPZIYOEFIjZpBL7SVBGnzRYXp.jpg"));

        return image;
    }

}
