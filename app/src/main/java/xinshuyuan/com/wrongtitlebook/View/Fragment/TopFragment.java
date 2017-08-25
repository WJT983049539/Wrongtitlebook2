package xinshuyuan.com.wrongtitlebook.View.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Activity.SelectProjectUpPaper;

/**
 * mainActivity 顶部布局
 * Created by Administrator on 2017/5/23.
 */
public class TopFragment extends Fragment{
    private View mView;
    private Button UptakePhone;
    private TextView name_textview;
    private String userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView=inflater.inflate(R.layout.fragment_top_layout,container,false);
        UptakePhone=(Button)mView.findViewById(R.id.updatetakephone);

        name_textview=(TextView)mView.findViewById(R.id.name_textView);
        name_textview.setText(userName);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {

        //拍照上传的按钮
        UptakePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //转到上传试题信息的Activity
                getActivity().startActivity(new Intent(getActivity(),SelectProjectUpPaper.class));
            }
        });

    }

    public void setInfo(String userName) {
        this.userName=userName;
    }
}
