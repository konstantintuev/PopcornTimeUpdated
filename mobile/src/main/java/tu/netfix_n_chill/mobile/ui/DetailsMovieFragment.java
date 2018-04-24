package tu.netfix_n_chill.mobile.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import tu.netfix_n_chill.IUseCaseManager;
import tu.netfix_n_chill.base.database.tables.History;
import tu.netfix_n_chill.base.model.video.info.MoviesInfo;
import tu.netfix_n_chill.base.model.video.info.VideoInfo;
import tu.netfix_n_chill.mobile.R;
import tu.netfix_n_chill.ui.details.DetailsMoviePresenter;
import tu.netfix_n_chill.ui.details.IDetailsMoviePresenter;
import tu.netfix_n_chill.ui.details.IDetailsMovieView;

public final class DetailsMovieFragment extends DetailsVideoFragment<MoviesInfo, IDetailsMovieView, IDetailsMoviePresenter> implements IDetailsMovieView {

    @NonNull
    @Override
    protected IDetailsMoviePresenter onCreateDetailsPresenter(@NonNull IUseCaseManager useCaseManager) {
        return new DetailsMoviePresenter(useCaseManager.getContentUseCase(), useCaseManager.getDetailsUseCase());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_details_movie, container, false);
    }

    @Override
    public void onVideoInfo(@NonNull MoviesInfo videoInfo) {
        super.onVideoInfo(videoInfo);
        watchedButton.setOnCheckedChangeListener(null);
        watchedButton.setChecked(History.isWatched(getContext(), videoInfo.getImdb(), 0, 0));
        watchedButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final VideoInfo info = detailsUseCase.getVideoInfoProperty().getValue();
                if (isChecked) {
                    History.insert(getContext(), info.getImdb(), 0, 0);
                    Toast.makeText(getContext(), R.string.movie_marked_as_watched, Toast.LENGTH_SHORT).show();
                } else {
                    History.delete(getContext(), info.getImdb(), 0, 0);
                    Toast.makeText(getContext(), R.string.movie_marked_as_unwatched, Toast.LENGTH_SHORT).show();
                }
            }
        });
        description.setVisibility(View.GONE);
        additionalDescription.setVisibility(View.VISIBLE);
        additionalDescription.setText(Html.fromHtml(videoInfo.getDescription()));
        additionalReleaseDate.setVisibility(View.GONE);
    }
}
