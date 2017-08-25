package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;
import work.StudentCommentConstantClass;
import xinshuyuan.com.wrongtitlebook.Model.CommentListBean;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.R;

public class ShenSuActivity extends Activity {
    private CommentListBean commentListBean;
    private Long CommentId;
    private EditText edit_shensu_content;
    private Button shensu_commit;
    private Button shensu_nativebutton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置窗口的大小及透明度
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.alpha = 0.9f;
        window.setAttributes(layoutParams);
        Intent intent = getIntent();
        commentListBean= (CommentListBean) intent.getSerializableExtra("commentListBean");
        setContentView(R.layout.layout_dialog_shensu);
        initView();
        initData();
        super.onCreate(savedInstanceState);
    }
    private void initView() {
        edit_shensu_content= (EditText) findViewById(R.id.edit_shensu_content);
        shensu_commit=(Button)findViewById(R.id.shensu_commit);
        shensu_nativebutton=(Button)findViewById(R.id.shensu_nativebutton);
        CommentId=commentListBean.getId();

        shensu_nativebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShenSuActivity.this.finish();
            }
        });
    }
    private void initData() {
        shensu_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> map= XsyMap.getInterface();
                map.put(StudentCommentConstantClass.PARAM_ID,String.valueOf(CommentId));
                map.put(StudentCommentConstantClass.PARAM_CONTENT,edit_shensu_content.getText().toString());
                OkGo.post(XSYTools.getEvaluateUrl(StudentCommentConstantClass.DOAPPEAL_URL,ShenSuActivity.this)).params(map).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        XSYTools.i("申诉成功返回"+s);
                        ShenSuActivity.this.finish();
                        XSYTools.showToastmsg(ShenSuActivity.this,"申诉成功！");
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        XSYTools.i("申诉请求失败返回"+e.toString());
                    }
                });

            }
        });
    }



}
