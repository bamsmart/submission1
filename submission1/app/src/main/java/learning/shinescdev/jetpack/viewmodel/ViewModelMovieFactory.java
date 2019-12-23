package learning.shinescdev.jetpack.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import learning.shinescdev.jetpack.data.source.MovieRepository;
import learning.shinescdev.jetpack.data.source.TVRepository;
import learning.shinescdev.jetpack.di.Injection;
import learning.shinescdev.jetpack.ui.movie.detail.DetailMovieViewModel;
import learning.shinescdev.jetpack.ui.movie.MovieViewModel;

public class ViewModelMovieFactory extends ViewModelProvider.NewInstanceFactory {
    private static volatile ViewModelMovieFactory INSTANCE;
    private  MovieRepository mMovieRepository;
    private TVRepository mTVRepository;

    private ViewModelMovieFactory(MovieRepository movieRepository){
        mMovieRepository = movieRepository;
    }

    public static ViewModelMovieFactory getInstanceMovie(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelMovieFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelMovieFactory(Injection.provideMovieRepository(application));
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(MovieViewModel.class)) {
            //noinspection unchecked
            return (T) new MovieViewModel(mMovieRepository);
        }else if (modelClass.isAssignableFrom(DetailMovieViewModel.class)) {
            //noinspection unchecked
            return (T) new DetailMovieViewModel(mMovieRepository);
        }/*else if (modelClass.isAssignableFrom(BookmarkViewModel.class)) {
            //noinspection unchecked
            return (T) new BookmarkViewModel(mAcademyRepository);
        } else if (modelClass.isAssignableFrom(CourseReaderViewModel.class)) {
            //noinspection unchecked
            return (T) new CourseReaderViewModel(mAcademyRepository);
        }*/

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
