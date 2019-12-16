package learning.shinescdev.jetpack.di;

import android.app.Application;

import learning.shinescdev.jetpack.data.source.MovieRepository;
import learning.shinescdev.jetpack.data.source.TVRepository;
import learning.shinescdev.jetpack.data.source.local.LocalRepository;
import learning.shinescdev.jetpack.data.source.local.room.LocalDatabase;
import learning.shinescdev.jetpack.data.source.remote.RemoteRepository;
import learning.shinescdev.jetpack.utils.AppExecutors;
import learning.shinescdev.jetpack.utils.JsonHelper;

public class Injection {

    public static MovieRepository provideMovieRepository(Application application) {

        LocalDatabase database = LocalDatabase.getInstance(application);

        LocalRepository localRepository = LocalRepository.getInstance(database.movieDao(), database.tvDao());
        RemoteRepository remoteRepository = RemoteRepository.getInstance(new JsonHelper(application));
        AppExecutors appExecutors = new AppExecutors();

        return MovieRepository.getInstance(localRepository, remoteRepository, appExecutors);
    }

    public static TVRepository provideTVRepository(Application application){
        LocalDatabase database = LocalDatabase.getInstance(application);

        LocalRepository localRepository = LocalRepository.getInstance(database.movieDao(), database.tvDao());
        RemoteRepository remoteRepository = RemoteRepository.getInstance(new JsonHelper(application));
        AppExecutors appExecutors = new AppExecutors();

        return TVRepository.getInstanceTV(localRepository, remoteRepository, appExecutors);
    }
}
