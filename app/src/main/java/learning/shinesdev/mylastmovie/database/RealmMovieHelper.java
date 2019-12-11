package learning.shinesdev.mylastmovie.database;

import android.util.Log;

import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;
import learning.shinesdev.mylastmovie.model.MovieRealm;

public class RealmMovieHelper {
    private final Realm realm;

    public  RealmMovieHelper(Realm realm){
        this.realm = realm;
    }

    public void insert(final MovieRealm movie){
        realm.executeTransaction(realm -> {
            try {
                realm.copyToRealm(movie);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public List<MovieRealm> getAllMovie(){
        RealmResults<MovieRealm> results = realm.where(MovieRealm.class).findAll();

        Log.d("Load Movie List",""+results.size()+" ");
        return results;
    }

    public void update(final Integer id, final String nim, final int nama){
        realm.executeTransactionAsync(realm -> {
            MovieRealm model = realm.where(MovieRealm.class)
                    .equalTo("id", id)
                    .findFirst();
            Objects.requireNonNull(model).setTitle(nim);
            model.setFavorite(nama);
        }, () -> Log.e("pppp", "onSuccess: Update Successfully"), Throwable::printStackTrace);
    }

    public void delete(Integer id){
        final RealmResults<MovieRealm> model = realm.where(MovieRealm.class).equalTo("id", id).findAll();
        realm.executeTransaction(realm -> model.deleteFromRealm(0));
    }
}
