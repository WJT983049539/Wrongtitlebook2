package xinshuyuan.com.wrongtitlebook.View.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *基础碎片类
 */
    public abstract class BaseFragment extends Fragment {
        public <T extends View> T getViewById(View v, int id){
                if(v==null)
                    return null;
                return (T) v.findViewById(id);




   }


}
