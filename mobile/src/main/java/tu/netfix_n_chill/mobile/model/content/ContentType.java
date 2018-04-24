package tu.netfix_n_chill.mobile.model.content;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;

import java.util.List;

import tu.netfix_n_chill.base.model.video.info.VideoInfo;

public final class ContentType<T extends VideoInfo> {

    private final String type;
    private final TypeToken<List<T>> typeToken;

    public ContentType(@NonNull String type, @NonNull TypeToken<List<T>> typeToken) {
        this.type = type;
        this.typeToken = typeToken;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public TypeToken<List<T>> getTypeToken() {
        return typeToken;
    }
}
