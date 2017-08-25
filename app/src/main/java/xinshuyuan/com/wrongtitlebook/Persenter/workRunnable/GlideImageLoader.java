package xinshuyuan.com.wrongtitlebook.Persenter.workRunnable;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.lzy.imagepickeruu.loader.ImageLoader;
import java.io.File;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * Created by Administrator on 2017/6/17.
 */

public class GlideImageLoader implements ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity)                             //配置上下文
                .load(Uri.fromFile(new File(path)))
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH)
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {

    }
}
