package learning.shinesdev.mylastmovie.database;

import android.util.Log;

import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;
import learning.shinesdev.mylastmovie.model.TVShowRealm;

public class RealmTVShowHelper {
    private final Realm realm;

    public RealmTVShowHelper(Realm realm){
        this.realm = realm;
    }

    public void insert(final TVShowRealm movie){
        realm.executeTransaction(realm -> {
                try{
                    realm.copyToRealm(movie);
                }catch (Exception e){
                    e.printStackTrace();
                }
        });
    }

    public List<TVShowRealm> getAllTVShow(){
        RealmResults<TVShowRealm> results = realm.where(TVShowRealm.class).findAll();

        Log.d("Load Movie List",""+results.size()+" ");
        return results;
    }

    public void update(final Integer id, final String nim, final int nama){
        realm.executeTransactionAsync(realm -> {
            TVShowRealm model = realm.where(TVShowRealm.class)
                    .equalTo("id", id)
                    .findFirst();
            Objects.requireNonNull(model).setTitle(nim);
            model.setFavorite(nama);
        }, () -> Log.e("pppp", "onSuccess: Update Successfully"), Throwable::printStackTrace);
    }

    public void delete(Integer id){
        final RealmResults<TVShowRealm> model = realm.where(TVShowRealm.class).equalTo("id", id).findAll();
        realm.executeTransaction(realm -> model.deleteFromRealm(0));
    }
}
