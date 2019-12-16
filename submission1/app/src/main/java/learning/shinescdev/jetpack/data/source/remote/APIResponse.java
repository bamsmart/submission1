package learning.shinescdev.jetpack.data.source.remote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class APIResponse<T> {

    @NonNull
    public final StatusResponse status;

    @NonNull
    public final String message;

    @NonNull
    public final T body;


    public APIResponse(@NonNull StatusResponse status, @NonNull T body, @NonNull String message) {
        this.status = status;
        this.message = message;
        this.body = body;
    }

    public static <T> APIResponse<T> success(@Nullable T body){
        return new APIResponse<>(StatusResponse.SUCCESS, body, null);
    }

    public static <T> APIResponse<T> empty(String msg, @Nullable T body){
        return new APIResponse<>(StatusResponse.EMPTY, body, msg);
    }

    public static <T> APIResponse<T> error(String msg, @Nullable T body){
        return new APIResponse<>(StatusResponse.ERROR, body, msg);
    }
}
