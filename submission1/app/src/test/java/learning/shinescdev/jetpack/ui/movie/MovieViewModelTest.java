package learning.shinescdev.jetpack.ui.movie;

import android.view.View;
import android.widget.Toast;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.List;

import learning.shinescdev.jetpack.data.source.local.entity.MovieEntity;

import static org.junit.Assert.*;

public class MovieViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantRule = new InstantTaskExecutorRule() ;

    private MovieViewModel viewModel;


    @Before
    public void setUp() {
        viewModel = new MovieViewModel();
    }


    @Test
    public void givenDataSource_whenFetchingReuslt_thenTrue(){
        /*TestLiveDataObserver<List<Item>> corridorObserver = new TestLiveDataObserver()
        dataSource.getItems().observeForever(itemObserver);
        dataSource.getItems();
        assertEquals(itemObserver.getItems()  , Collections.emptyList()); ;*/
    }


    @Test
    public void getMovie() {
        viewModel.movies.observe(null,movies -> {

            if (movies != null) {

                switch (movies.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        assertNotNull(movies);
                        assertEquals(5, movies.data.size());
                        break;
                    case ERROR:

                        break;

                }
            }
        });

    }

}