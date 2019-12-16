package learning.shinescdev.jetpack.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import learning.shinescdev.jetpack.data.source.MovieRepository;
import learning.shinescdev.jetpack.data.source.TVRepository;
import learning.shinescdev.jetpack.di.Injection;
import learning.shinescdev.jetpack.ui.movie.MovieViewModel;
import learning.shinescdev.jetpack.ui.tv.TVViewModel;

public class ViewModelTVShowFactory extends ViewModelProvider.NewInstanceFactory {
    private static volatile ViewModelTVShowFactory INSTANCE;
    private TVRepository mTVRepository;

    private ViewModelTVShowFactory(TVRepository tvRepository){
        this.mTVRepository = tvRepository;
    }

    public static ViewModelTVShowFactory getInstanceTVShow(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelTVShowFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelTVShowFactory(Injection.provideTVRepository(application));
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if(modelClass.isAssignableFrom(TVViewModel.class)) {
            return  (T) new TVViewModel(mTVRepository);
        /*else if (modelClass.isAssignableFrom(DetailCourseViewModel.class)) {
            //noinspection unchecked
            return (T) new DetailCourseViewModel(mAcademyRepository);
        } else if (modelClass.isAssignableFrom(BookmarkViewModel.class)) {
            //noinspection unchecked
            return (T) new BookmarkViewModel(mAcademyRepository);
        } else if (modelClass.isAssignableFrom(CourseReaderViewModel.class)) {
            //noinspection unchecked
            return (T) new CourseReaderViewModel(mAcademyRepository);
        }*/
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
