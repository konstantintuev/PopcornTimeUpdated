package tu.netfix_n_chill.mobile.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import tu.netfix_n_chill.base.model.video.info.VideoInfo;
import tu.netfix_n_chill.mobile.ui.DetailsActivity;

public class VideoItemListener implements OnClickListener {

    private Context context;
    private VideoInfo info;

    public VideoItemListener(Context context, VideoInfo info) {
        this.context = context;
        this.info = info;
    }

    @Override
    public void onClick(View v) {
        DetailsActivity.start(context, info);
    }
}