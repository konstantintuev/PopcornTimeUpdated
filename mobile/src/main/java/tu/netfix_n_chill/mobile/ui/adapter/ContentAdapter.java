package tu.netfix_n_chill.mobile.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;
import java.util.Locale;

import tu.netfix_n_chill.IUseCaseManager;
import tu.netfix_n_chill.VibrantUtils;
import tu.netfix_n_chill.base.database.tables.Favorites;
import tu.netfix_n_chill.base.model.video.info.VideoInfo;
import tu.netfix_n_chill.base.utils.Logger;
import tu.netfix_n_chill.mobile.R;
import tu.netfix_n_chill.mobile.ui.DetailsActivity;

public final class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {

    private List<VideoInfo> content;

    private int width = 356;
    private int height = 534;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.onBind(content.get(position), width, height);
    }

    @Override
    public int getItemCount() {
        return content != null ? content.size() : 0;
    }

    public void setContent(@Nullable List<VideoInfo> content) {
        this.content = content;
        notifyDataSetChanged();
    }

    public List<VideoInfo> getContent() {
        return content;
    }

    public void setItemSize(Display display, int columnCount, int spacingPixels) {
        final DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        width = (metrics.widthPixels - spacingPixels * (columnCount - 1)) / columnCount;
        height = (int) (width * 1.5f);
    }

    static final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        private final ImageView poster;
        private final TextView rating;
        private final CompoundButton favorite;
        private final TextView year;

        private VideoInfo info;

        ViewHolder(@NonNull ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_content, parent, false));
            itemView.setOnClickListener(this);
            poster = itemView.findViewById(R.id.poster);
            rating = itemView.findViewById(R.id.rating);
            favorite = itemView.findViewById(R.id.favorite);
            year = itemView.findViewById(R.id.year);
        }

        void onBind(@NonNull VideoInfo info, int width, int height) {
            this.info = info;
            final ViewGroup.LayoutParams params = itemView.getLayoutParams();
            params.width = width;
            params.height = height;
            itemView.setLayoutParams(params);
            Picasso.with(itemView.getContext()).load(info.getPoster()).placeholder(R.drawable.poster).into(poster);
            rating.setText(String.format(Locale.ENGLISH, "%.1f", info.getRating()));
            favorite.setOnCheckedChangeListener(null);
            final Cursor cursor = Favorites.query(itemView.getContext(), null, Favorites._IMDB + "=\"" + info.getImdb() + "\"", null, null);
            if (cursor != null) {
                favorite.setChecked(cursor.getCount() > 0);
                cursor.close();
            }
            favorite.setOnCheckedChangeListener(this);
            year.setText(String.format(Locale.ENGLISH, "%d", info.getYear()));
        }

        @Override
        public void onClick(View v) {
            VibrantUtils.setAccentColor(((BitmapDrawable) poster.getDrawable()).getBitmap(), ContextCompat.getColor(itemView.getContext(), R.color.v3_accent), accentColor -> {
                if (Configuration.ORIENTATION_LANDSCAPE == itemView.getContext().getResources().getConfiguration().orientation) {
                    DetailsActivity.start(v.getContext(), info);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation((Activity) v.getContext(),
                                        poster,
                                        "image_transition");
                        ((IUseCaseManager) v.getContext().getApplicationContext()).getDetailsUseCase().getVideoInfoProperty().setValue(info);
                        v.getContext().startActivity(intent, options.toBundle());
                    } else {
                        DetailsActivity.start(v.getContext(), info);
                    }
                }
            });
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                Favorites.insert(buttonView.getContext(), info);
            } else {
                Favorites.delete(buttonView.getContext(), info);
            }
        }
    }
}
