package xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.ibpd.xsy.varDefine.GlobalVarDefine;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.GetUserInfoClass;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;
import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.ExerciseHandler;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Activity.ExerciseTestActivity;
import xinshuyuan.com.wrongtitlebook.View.Fragment.BaseFragment;

/**
 * 自定义的拍照的题
 * Created by Administrator on 2017/6/21.
 */

public class ExerciseCustomFragment extends BaseFragment {
    private ExerciseHandler exerciseHandler;
    private ExerciseTestActivity exerciseTestActivity;
    private View mView;
    private WebView webview;
    private TestEntity testEntity;
    private String impoint;
    Button serJiexi;
    private String AnsWerAndFenxi;
    public ExerciseCustomFragment(ExerciseHandler exerciseHandler, ExerciseTestActivity exerciseTestActivity) {
        super();
        this.exerciseHandler=exerciseHandler;
        this.exerciseTestActivity=exerciseTestActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testEntity = (TestEntity) getArguments().getSerializable("testEntity");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.exercise_custom_up_layout,container,false);
        inint();
        return mView;
    }
    private void inint() {
        impoint=testEntity.getPoint();
        webview=(WebView)mView.findViewById(R.id.exercise_custom_point_webView);
        serJiexi= (Button) mView.findViewById(R.id.serJiexi);
        initWebView();
        webview.loadUrl(impoint);

        serJiexi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject j=new JSONObject();
                AnsWerAndFenxi= testEntity.getAnswerAnalysis();
                if(testEntity!=null){
                    try {
                        j.put("testId", testEntity.getTestId());
                        XsyMap<String,String> commitmap=XsyMap.getInterface();
                        //列表名
                        Long studId= new GetUserInfoClass(getActivity()).getUserId();
                        commitmap.put(GlobalVarDefine.PARAM_USER_ID,String.valueOf(studId));
                        //试题id
                        commitmap.put(GlobalVarDefine.PARAM_TESTID,testEntity.getTestId().toString());
                        //提交试题的json数据
                        commitmap.put(GlobalVarDefine.PARAM_TESTANS_JSON,j.toString());
                        //答题类型
                        commitmap.put(GlobalVarDefine.PARAM_ITEMTYPE,testEntity.getTestType().toString());
                        String commotUrl= XSYTools.getWrongUrl(GlobalVarDefine.TEST_SUBMIT_ANSWER_URL,getActivity());
                        OkGo.post(commotUrl).params(commitmap).execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {

                                XSYTools.i(s);
                                exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.EXERCISE_TEST_SHOW,testEntity));
//                                try {
//                                    JSONArray array=new JSONArray(s);
//                                    JSONObject object=array.getJSONObject(0);
//                                    Boolean flag=object.getBoolean("msg");
//                                    if(flag){
//                                        exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.ANS_OK,AnsWerAndFenxi));
//
//                                    }else {
//                                        exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.ANS_NO,AnsWerAndFenxi));
//                                    }
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }

                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                XSYTools.i(e.toString());
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    private void initWebView() {
        webview.getSettings().setBlockNetworkImage(false);//解决图片不显示
        webview.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });

        webview.getSettings().setDefaultTextEncodingName("utf-8") ;
        webview.setBackgroundColor(0); // 设置背景色
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setInitialScale(56);
        //支持屏幕缩放
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        //不显示webview缩放按钮
        webview.getSettings().setDisplayZoomControls(false);
        webview.setWebChromeClient(new WebChromeClient());
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int scale = dm.densityDpi;
        if (scale == 240) { //
            webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (scale == 160) {
            webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else {
            webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }
        //下面的这段是为了防止网页中的部分按钮点击无响应而设置的,原因不详
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result)
            {
                return super.onJsAlert(view, url, message, result);
            }
        });
    }

}
