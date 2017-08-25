package xinshuyuan.com.wrongtitlebook.Model.Common;

import android.content.Context;

import java.io.File;

/**、缓存到sd卡的实体类
 * Created by Administrator on 2017/6/28.
 */

public class FileCache {
    private File cacheDir;

    public FileCache(Context context) {
        // 找到保存缓存的图片目录
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(
                    android.os.Environment.getExternalStorageDirectory(),
                    "newnews");
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public File getFile(String url) {
        String filename = String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        return f;

    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        for (File f : files)
            f.delete();
    }

}
