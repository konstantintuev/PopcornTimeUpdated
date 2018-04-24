package tu.netfix_n_chill.mobile.model.content;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import tu.netfix_n_chill.base.model.video.info.TvShowsInfo;
import tu.netfix_n_chill.base.model.video.info.VideoInfo;
import tu.netfix_n_chill.model.content.IDetailsProvider;
import tu.netfix_n_chill.model.content.ISubtitlesProvider;
import tu.netfix_n_chill.model.filter.IFilter;

public final class TvShowsProvider<T extends TvShowsInfo> extends ContentProvider<T> {

    public TvShowsProvider(@NonNull ContentType<T> type,
                           @NonNull ContentRepository repository,
                           @NonNull IFilter[] filters,
                           @Nullable IDetailsProvider[] detailsProviders,
                           @Nullable ISubtitlesProvider subtitlesProvider) {
        super(type, repository, filters, detailsProviders, subtitlesProvider);
    }

    @NonNull
    @Override
    public Iterator<Observable<List<? extends VideoInfo>>> getContentIterator(@Nullable String keywords) {
        return new TvShowsIterator(keywords);
    }

    private final class TvShowsIterator extends ContentIterator {

        private TvShowsIterator(@Nullable String keywords) {
            super(keywords);
        }

        @NonNull
        @Override
        protected Observable<List<? extends VideoInfo>> getVideos(@Nullable String keywords, int page) {
            return repository.getTVShows(filters, keywords, page, type.getTypeToken()).map(new Function<List<T>, List<? extends VideoInfo>>() {

                @Override
                public List<? extends VideoInfo> apply(@io.reactivex.annotations.NonNull List<T> tvShows) throws Exception {
                    return tvShows;
                }
            });
        }
    }
}
