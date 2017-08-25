package xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.ibpd.xsy.varDefine.GlobalVarDefine;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.GetUserInfoClass;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;

import okhttp3.Call;
import okhttp3.Response;
import work.HomeWorkConstantClass;
import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.PerferenceService;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.ExerciseHandler;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Activity.ExerciseTestActivity;
import xinshuyuan.com.wrongtitlebook.View.Activity.WebViewActivity;
import xinshuyuan.com.wrongtitlebook.View.Fragment.BaseFragment;

/**
 * Created by Administrator on 2017/6/21.
 */

public class ExerciseOPRAFragment extends BaseFragment {


    private ScrollView oprateScroview;
    private Integer scrollLen;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            oprateScroview.scrollTo(0,scrollLen);// 改变滚动条的位置
        }
    };
    private String AnsWerAndFenxi;
    private Integer itemType;
    private TestEntity testEntity = null;
    private View mView;
    private Document document;
    public Bitmap bitmap;
    private ImageButton imageButton;
    private ExerciseHandler exerciseHandler;
    private ExerciseTestActivity exerciseTestActivity;
    private File file;

    public ExerciseOPRAFragment(ExerciseHandler exerciseHandler, ExerciseTestActivity exerciseTestActivity) {
        super();
        this.exerciseHandler=exerciseHandler;
        this.exerciseTestActivity=exerciseTestActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 得到答题类型
//        DigitKeyPadUtil.hidePopupWindow();
        testEntity = (TestEntity) getArguments().getSerializable("testEntity");
        AnsWerAndFenxi=testEntity.getAnswerAnalysis();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.layout_exercise_oprate, container, false);
        imageButton=(ImageButton)mView.findViewById(R.id.takephone_button);

        try {
            initTest();
            initbutton();
            //拍照点击事件
            initbutton();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mView;
    }

    private void initbutton() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file = new File(Common.getCacheDirPath()+"/"+"123456.jpg");
                Intent inn = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
                Uri photoUri = Uri.fromFile(file); // 传递路径
                inn.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);// 更改系统默认存储路径
                startActivityForResult(inn, 2);


            }
        });
        imageButton.setBackgroundResource(R.drawable.caozuotakephone);
    }

    private void initTest() throws JSONException {
        if ( XSYTools.isNull(testEntity)) {
            XSYTools.showToastmsg(getActivity(),"初始化错误");
            return;
        }

        // 赋值正常，继续
        // 集中式和分布式的区别是：1、集中式不显示题干，仅显示可选项或答案填写区，分布式则得显示题干；
        // 2、集中式无法切换到下一题或上一题，全靠服务器推送，分布式可自主选择题目
        String itemPoint = testEntity.getPoint();
        document = Jsoup.parse(itemPoint);
        itemPoint = document.html();
        itemPoint="<span style=\"color:red;font-weight:bold;\">[操作题]</span>"+itemPoint;

        Log.d("html", itemPoint);
        // 设置题干显示相关属性，以及相关node的格式化操作
        WebView wv = this.getViewById(mView, R.id.oprate_itemPoint);
        // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebView.HitTestResult hit = view.getHitTestResult();
                if(hit!=null){
                    int hitType=hit.getType();
                    //如果是一个url超链接转到默认浏览器
                    if (hitType == WebView.HitTestResult.SRC_ANCHOR_TYPE||hitType == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
//					 Intent i = new Intent(Intent.ACTION_VIEW);
//					 i.setData(Uri.parse(url));
//					 startActivity(i);
                        //转到activity播放
                        Intent intent=new Intent(getActivity(),WebViewActivity.class);
                        intent.putExtra("data", url);
                        getActivity().startActivity(intent);

                        //转到dialog
//					 UriView_dialog uri_dialog=new UriView_dialog(getActivity());
//					 uri_dialog.StartUri(url);
//					 uri_dialog.show();

                    }else{
                        // view.loadUrl(url);
                    }
                }
                return true;
            }
        });
        wv.getSettings().setDefaultFontSize(25);
        wv.loadDataWithBaseURL(null, itemPoint, "text/html", "utf-8", null);
        initWebView(wv);
        // 题干显示完成
    }

    // 初始化浏览器
    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    private void initWebView(WebView wv) {
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebChromeClient(new WebChromeClient());
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int scale = dm.densityDpi;
        if (scale == 240) { //
            wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (scale == 160) {
            wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else {
            wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }
        // 下面的这段是为了防止网页中的部分按钮点击无响应而设置的,原因不详
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 2) {
                File file = new File(Common.getCacheDirPath() + File.separator + "123456.jpg");
                //返回图片
                FileInputStream fis = null;
                fis = new FileInputStream(file.toString()); // 根据路径获取数据
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                // 压缩保存
                bitmap = XSYTools.comp(bitmap);
                XSYTools.saveBitmap(bitmap);
                //开始提交
                JSONObject j = new JSONObject();
                if (testEntity != null) {
                    try {
                        j.put("testId", testEntity.getTestId());
                        System.out.println("组合完成");
                        // 参数组合完成，准备发送！！！！！！！
                        XsyMap<String, String> map = XsyMap.getInterface();


                        PerferenceService service=new PerferenceService(getActivity());
                        String subjectIdd=service.getsharedPre().getString("WrongProjectId","");
                        //学生id
                        Long studId= new GetUserInfoClass(getActivity()).getUserId();
                        map.put(GlobalVarDefine.PARAM_USER_ID, String.valueOf(studId));
                        //试题id
                        map.put(GlobalVarDefine.PARAM_TESTID, testEntity.getTestId().toString());
                        //提交试题的json数据
                        map.put(GlobalVarDefine.PARAM_TESTANS_JSON, j.toString());
                        //答题类型
                        map.put(GlobalVarDefine.PARAM_ITEMTYPE, testEntity.getTestType().toString());
                        map.put(HomeWorkConstantClass.PARAM_SUBJECTID,subjectIdd);

                      String Opraurl= XSYTools.getWrongUrl(GlobalVarDefine.TEST_OPERTEST_FILEUPLOAD_URL,getActivity());

                        String pms="";
                        if(map.size()>0){
                            for(String name:map.keySet()){
                                pms+=name+"="+map.get(name)+"&";
                            }
                            if(!XSYTools.isEmpty(pms)){
                                pms="?"+pms;
                            }
                            Opraurl=Opraurl+pms;
                        }


                        OkGo.post(Opraurl).tag(this)
                                .params("file1", file).execute(new StringCallback() {
                                @Override
                            public void onSuccess(String s, Call call, Response response) {

                                try {

//                                    JSONArray array = new JSONArray(s);
//                                    JSONObject object = array.getJSONObject(0);
//                                    Boolean flag = object.getBoolean("msg");
//                                    if (flag) {
//                                        exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.ANS_OK, AnsWerAndFenxi));
//
//                                    } else {
                                        exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.ONLY_ANSWER, AnsWerAndFenxi));
//                                     }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                            }

                            @Override
                            public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                                super.upProgress(currentSize, totalSize, progress, networkSpeed);
                            }
                        });


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }


            }
        }catch (Exception e){
            e.printStackTrace();

        }



    }

    public void onDestroy() {
        super.onDestroy();
    }



    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }






}
