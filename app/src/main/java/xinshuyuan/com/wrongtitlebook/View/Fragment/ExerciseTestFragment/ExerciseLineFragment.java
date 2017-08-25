package xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.AnswerEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xinshuyuan.com.wrongtitlebook.Model.Common.LineButton;
import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.Model.Common.answers;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.KeyBord.DigitKeyPadUtil;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.ExerciseHandler;
import xinshuyuan.com.wrongtitlebook.Persenter.evenListener.LineOnClickListener;
import xinshuyuan.com.wrongtitlebook.Persenter.evenListener.LineXOnTouchListener;
import xinshuyuan.com.wrongtitlebook.Persenter.evenListener.LineXiaoChuAllOnClickListener;
import xinshuyuan.com.wrongtitlebook.Persenter.evenListener.LineXiaoChuOnClickListener;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Activity.ExerciseTestActivity;
import xinshuyuan.com.wrongtitlebook.View.Fragment.BaseFragment;

/**
 * Created by Administrator on 2017/6/21.
 */

public class ExerciseLineFragment extends BaseFragment {
    private ExerciseHandler exerciseHandler;
    private ExerciseTestActivity exerciseTestActivity;

    private ScrollView lineScroview;
    private Integer scrollLen;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            lineScroview.scrollTo(0,scrollLen);// 改变滚动条的位置
        }
    };
    private Integer itemType;
    private TestEntity testEntity=null;
    private View mView;
    private  List<AnswerEntity> list;
    private Document document;
    private answers ann;
    private Canvas canvas;
    private Paint paint;
    private ImageView ib;
    private ImageButton xiaochuall;
    private  ImageButton xiaochu;
    private static Bitmap baseBitmap2;
    private int ID_BTN1=0;
    private int width;
    private int height;
    private Map<String, LineButton> answerMap;
    private Bitmap bitMap;
    private float[] aa;
    private LinearLayout line_zhu;
    private Drawable drawableaone;
    //储存原始按钮的集合
    //private List<LineButton> listlist_start=new ArrayList<LineButton>();
    private List<LineButton> listlist2=new ArrayList<LineButton>();
    public ExerciseLineFragment(ExerciseHandler exerciseHandler, ExerciseTestActivity exerciseTestActivity) {
        this.exerciseHandler=exerciseHandler;
        this.exerciseTestActivity=exerciseTestActivity;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        DigitKeyPadUtil.hidePopupWindow();
        //得到javabean对象
        testEntity=(TestEntity) getArguments().getSerializable("testEntity");
        list=testEntity.getAnswerList();
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.layout_exercise_linefragment, container, false);
        line_zhu = (LinearLayout) mView.findViewById(R.id.line_main);
        initTest();
        initView();
        //擦出按钮
        initeraser();
        //初始化bitmap
        initBitmap();
        //滚动条
        return mView;
    }
    //判断这道题是否有答案，有的话放入缓存
//    private void setLineAnswer() {
//        for(int i=0;i<AnswerStore.Linekeylist().size();i++){
//            if(AnswerStore.Linekeylist().get(i).equals(testEntity.getTestId())){
//                System.out.println("AnswerStore.getAllLineAnsMap().get(testEntity.getTestId())"+AnswerStore.getAllLineAnsMap().get(testEntity.getTestId()));
//                List<LineButton>listww=AnswerStore.getAllLineAnsMap().get(testEntity.getTestId());
//                setListButton(AnswerStore.getAllLineAnsMap().get(testEntity.getTestId()));
//            }
//        }

//        for(int i=0;i<AnswerStore.Linezbkeylist().size();i++){
//            //如果有答案
//            if(AnswerStore.Linezbkeylist().get(i).equals(testEntity.getTestId())){
//                setFloatsorry(AnswerStore.getAllLinezbAnsMap().get(testEntity.getTestId()));
//                paint();
//            }
//
//
//        }
//
//    }
    //画出来
    private void paint() {
        initBitmap();
        Bitmap Bitmapp = GetBitmap();
        if(GetFloatsorry()!=null){
            float[] dd=new float[GetFloatsorry().length];
            dd=GetFloatsorry();
            System.out.println("得到的up数组大小："+dd.length);
            if(dd.length>=4&&dd.length%4==0){
                // 创建一张画布
                canvas = new Canvas(Bitmapp);
                canvas.drawColor(Color.WHITE);
                // 创建画笔
                paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(5);
                canvas.drawLines(dd, paint);
                Drawable drawable = new BitmapDrawable(Bitmapp);
                line_zhu.setBackground(drawable);
            }
        }

    }
    private void initeraser() {
        ib=this.getViewById(mView,R.id.line_imageview);
        xiaochu=this.getViewById(mView,R.id.xiaochu);
        xiaochuall=this.getViewById(mView,R.id.xiaochuone);
        xiaochu.setOnClickListener(new LineXiaoChuOnClickListener(this));
        xiaochu.setBackgroundResource(R.drawable.huitui_background);
        xiaochuall.setOnClickListener(new LineXiaoChuAllOnClickListener(this)) ;
        xiaochuall.setBackgroundResource(R.drawable.qingkong_background);
    }
    private void initView() {
        ImageButton ib=this.getViewById(mView,R.id.line_submit_btn);
        if(ib!=null){
            ib.setOnClickListener(new LineOnClickListener(this,exerciseHandler));
            ib.setBackgroundResource(R.drawable.commit_button_background);
        }
    }
    private void initTest() {
        if( XSYTools.isNull(testEntity)){
            XSYTools.showToastmsg(exerciseTestActivity,"初始化错误");
            return;
        }
        String itemPoint=testEntity.getPoint();
        document = Jsoup.parse(itemPoint);


        //
        itemPoint=document.html();
        itemPoint="<span style=\"color:red;font-weight:bold;\">[连线题]</span>"+itemPoint;
        Log.d("html", itemPoint);
        //设置题干显示相关属性，以及相关node的格式化操作
        WebView wv=this.getViewById(mView, R.id.line_webView1);
        //设置字体
        WebSettings settings=wv.getSettings();
        settings.setTextSize(WebSettings.TextSize.LARGER);
        wv.loadDataWithBaseURL(null, itemPoint, "text/html", "utf-8", null);
        initWebView(wv);
        //题干显示完成
        try{
            loadOption();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void initWebView(WebView wv) {
        wv.getSettings().setDefaultFontSize(19);
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
        //下面的这段是为了防止网页中的部分按钮点击无响应而设置的,原因不详
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result)
            {
                return super.onJsAlert(view, url, message, result);
            }
        });

    }


    private void loadOption() throws JSONException {
        LinearLayout left=this.getViewById(mView, R.id.line_left_showqq);//左排
        if(left.getChildCount()>0){
            left.removeAllViews();
        }

        LinearLayout right=this.getViewById(mView, R.id.line_right_layout);//右排
        if(right.getChildCount()>0){
            right.removeAllViews();
        }
        // 得到集合
        List<AnswerEntity> ansList=this.testEntity.getAnswerList();
        if(ansList==null || ansList.size()==0){
            XSYTools.showToastmsg(exerciseTestActivity,"没有东西");
            return;
        }
        //遍历得到AnswerEntity每个对象
        for(AnswerEntity ans:ansList){


            String optionAnswer=ans.getOptionAnswer();
            //转换成json对象
            //JSONObject  jsonObject = new JSONJSONArray(optionAnswer);
            //得到数组对象
            JSONArray jsonArray = new JSONArray(optionAnswer);
            for(int i=0;i<jsonArray.length();i++) {
                ID_BTN1=ID_BTN1+1;
                JSONObject jsonOb = (JSONObject)jsonArray.opt(i);
                String type= jsonOb.getString("type");
                String text=jsonOb.getString("text");
                String pair=jsonOb.getString("pair");
                String order=jsonOb.getString("order");
                System.out.println("type="+type+"pair=="+pair);
                if(type.equals("left")){
                    String html = "<html><head><style type='text/css'>.cont{text-align:right;}p{text-align:right;}</style></head><body><div class='cont'>"
                            +text+ "</div></body></html>";
                    //弄一些竖排的linearLayout来显示选项、后面加小圆圈用来连接,在左边显示
                    RelativeLayout subll=new RelativeLayout(this.getActivity());
                    RelativeLayout.LayoutParams lp3 = new  RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    subll.setGravity(Gravity.RIGHT);
                    //new出按钮
                    LineButton linebutton=new LineButton(getActivity());

                    linebutton.setId(ID_BTN1);
                    linebutton.setLineButtonId(String.valueOf(ID_BTN1));
                    linebutton.setPair(pair);
                    linebutton.setOder(order);
                    linebutton.setType(type);
                    //放入样式
                    linebutton.setBackgroundResource(R.drawable.selectlinebutton);
                    linebutton.setWidth(XSYTools.dip2px(this.getActivity(), 40));
                    linebutton.setHeight(XSYTools.dip2px(this.getActivity(), 40));
                    linebutton.setOnTouchListener(new LineXOnTouchListener(this));
                    //把按钮添加到原始数据集合
                    // listlist_start.add(linebutton);
                    //添加参数
                    RelativeLayout.LayoutParams lp1 = new  RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    lp1.setMargins(0, 15, 0, 0);
                    //加载button
                    subll.addView(linebutton,lp1);
                    //newWeb浏览器

                    WebView ansWv=new WebView(this.getActivity());
                    //ansWv.loadUrl("file:///android_asset/web.js.js");
                    WebSettings webSettings = ansWv.getSettings();
                    webSettings.setSavePassword(false);
                    webSettings.setSaveFormData(false);
                    webSettings.setJavaScriptEnabled(true);
                    webSettings.setSupportZoom(false);
                    ansWv.setWebChromeClient(new WebChromeClient());
                    ansWv.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
                    //ansWv.loadDataWithBaseURL(null, text,"text/html", "utf-8", null);
                    //滚动条没有
                    ansWv.setVerticalScrollBarEnabled(false);
                    initWebView(ansWv);
                    //？？
                    RelativeLayout.LayoutParams lp_topheader = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    //RelativeLayout.LayoutParams lp2 = new  RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    //lp2.addRule(RelativeLayout.ALIGN_BASELINE,ID_BTN1);
                    lp_topheader.addRule(RelativeLayout.ALIGN_BOTTOM, ID_BTN1);
                    lp_topheader.addRule(RelativeLayout.LEFT_OF, ID_BTN1);
                    lp_topheader.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    lp_topheader.setMargins(20, 0, 10, 0);
                    //lp_topheader.addRule(RelativeLayout.ALIGN_LEFT, ID_BTN1);
                    subll.addView(ansWv,lp_topheader);
                    left.addView(subll);

                }else if(type.equals("right")){
                    //弄一些竖排的linearLayout来显示选项、后面加小圆圈用来连接,在左边显示
                    RelativeLayout subll=new RelativeLayout(this.getActivity());
                    RelativeLayout.LayoutParams lp3 = new  RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    //subll.setGravity(Gravity.RIGHT);
                    //new出按钮
                    LineButton linebutton=new LineButton(getActivity());
                    linebutton.setOnTouchListener(new LineXOnTouchListener(this));
                    linebutton.setId(ID_BTN1);
                    linebutton.setLineButtonId(String.valueOf(ID_BTN1));
                    linebutton.setPair(pair);
                    linebutton.setOder(order);
                    linebutton.setType(type);
                    linebutton.setBackgroundResource(R.drawable.selectlinebutton);
                    linebutton.setWidth(XSYTools.dip2px(this.getActivity(), 20));
                    linebutton.setHeight(XSYTools.dip2px(this.getActivity(), 20));
                    //把按钮添加到原始数据集合
                    // listlist_start.add(linebutton);

                    //添加参数
                    RelativeLayout.LayoutParams lp1 = new  RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);//align_parent_right
                    lp1.setMargins(0, 15, 0, 0);
                    //加载button
                    subll.addView(linebutton,lp1);
                    //newWeb浏览器
                    WebView ansWv=new WebView(this.getActivity());
                    ansWv.loadDataWithBaseURL(null, text,"text/html", "utf-8", null);
                    //滚动条没有
                    ansWv.setVerticalScrollBarEnabled(false);
                    initWebView(ansWv);
                    //？？
                    RelativeLayout.LayoutParams lp_topheader = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp_topheader.addRule(RelativeLayout.ALIGN_BOTTOM, ID_BTN1);
                    lp_topheader.addRule(RelativeLayout.RIGHT_OF, ID_BTN1);
                    lp_topheader.setMargins(20, 0, 10, 0);
                    subll.addView(ansWv,lp_topheader);
                    right.addView(subll);
                }
            }
        }
    }


    public void clearAnswerMap(){
        this.getAnswerMap().clear();
    }
    //返回答按
    public Map<String, LineButton> getAnswerMap() {
        return answerMap;
    }
    public void fillAnswerToMap(String id,LineButton value) {
        //Log.d("fillAnswer", "添加试题答案@"+id+"  "+value);
        this.getAnswerMap().put(id, value);
    }
    public void removeAnswerToMap(String id) {
        Log.d("fillAnswer", "移除试题答案@"+id);
        this.getAnswerMap().remove(id);
    }
    public Integer get() {
        return itemType;
    }
    public TestEntity getTestEntity() {
        return testEntity;
    }
    public Integer getItemType() {
        return itemType;
    }
    public Paint getpaint(){
        return paint;
    }
    public Canvas getCanvas(){
        return canvas;
    }
    public void setBitMap(Bitmap b) {
        this.bitMap=b;
    }
    public Bitmap GetBitmap(){
        return this.bitMap;
    }
    public Bitmap initBitmap(){
        DisplayMetrics dm = this.getActivity().getResources().getDisplayMetrics();
        int mScreenWidth = dm.widthPixels;
        int mScreenHeight = dm.heightPixels;
        this.bitMap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_4444);
        return this.bitMap;
    }
    //放进数组，里面存的坐标
    public void setFloatsorry(float[] aa2){
        this.aa=aa2;
    }
    //得到数组，里面存的坐标
    public float[] GetFloatsorry(){
        return this.aa;
    }
    //放入按钮，里面存的按钮
    public void setListButton(List<LineButton> list){
        this.listlist2=list;
    }
    //得到存的按钮
    public List getListButton(){
        return this.listlist2;
    }
    //清空按钮
    public void inintarray(){
        this.aa=null;
    }
    /*
     * 提供原始button
    public List getList_start_button(){
        return this.listlist_start;
    }
    */
    //清除保存按钮的list
    public void cleanArrayList2(){
        this.listlist2.clear();


    }




}
