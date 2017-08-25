package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ibpd.xsy.varDefine.TestVarDefine;
import com.ibpd.xsy.varDefine.WrongQuestionConstantClass;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.twiceyuan.dropdownmenu.ArrayDropdownAdapter;
import com.twiceyuan.dropdownmenu.DropdownMenu;
import com.twiceyuan.dropdownmenu.MenuManager;
import com.twiceyuan.dropdownmenu.OnDropdownItemClickListener;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.GetUserInfoClass;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import okhttp3.Call;
import okhttp3.Response;
import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.SelectWrongTest;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.SuspendWindowsUtil;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.ShowWrongHandler;
import xinshuyuan.com.wrongtitlebook.Persenter.workRunnable.CheckLoginRunnable;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * 显示错题列表的界面
 * Created by Administrator on 2017/6/9.
 */

public class ShowWrongBookslistActivity extends Activity{
  private ShowWrongHandler showWrongHandler;
    private  String Project;
    private LinearLayout ContentFragment;
    //难度参数
    private  String ff;
    private String time_order;
    private String DC_Order;
    private   TextView textView;
    //难度选择
    private    DropdownMenu menu;
    //时间排序
    private    DropdownMenu menu2;
    //排序
    private    DropdownMenu menu3;

    //知识点按钮
    private TextView knowtext;
    //返回箭头
    private ImageView show_wrng_list_retuen;
    /**
     * 默认识空的
     */
    //现在选择的难度
    private String NowDifferent;
    //现在选择的时间排序
    private String NowTimeOrder;
    //现在选择的排序
    private String NowOrder;

    final String[] different = new String[]{"简单","容易","一般","较难","难"};
    final String[] timetOrder = new String[]{"今天","昨天","一周之内","一月之内","全部"};
    final String[] Order = new String[]{"时间降序","时间倒序","难度降序","难度倒序"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i("ShowWrongBooklist","ShowWrongBooklistActivity开始了！");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window=getWindow();
        WindowManager.LayoutParams layoutParams= window.getAttributes();
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏底部键盘，一直不会弹出
        layoutParams.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(layoutParams);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.show_wronglist_layout);
        Project=getIntent().getStringExtra("project");
        Common.SubjectId=Common.getMap().get(Project);
        getData();
        
        showWrongHandler=new ShowWrongHandler(this);
        show_wrng_list_retuen= (ImageView) findViewById(R.id.show_wrng_list_retuen);
        show_wrng_list_retuen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new CheckLoginRunnable(ShowWrongBookslistActivity.this)).start();

            }
        });
        ContentFragment=(LinearLayout)findViewById(R.id.show_wrong_content);
        menu = (DropdownMenu) findViewById(R.id.dm_dropdown);
        menu2 = (DropdownMenu) findViewById(R.id.dm_dropdown2);
        menu3 = (DropdownMenu) findViewById(R.id.dm_dropdown3);
        knowtext=(TextView)findViewById(R.id.know_text);
        //知识点点击事件
        knowtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectWrongTest selectWrongTest=new SelectWrongTest(ShowWrongBookslistActivity.this,Project,showWrongHandler);
                SuspendWindowsUtil.ShowSuspendWindowView(ShowWrongBookslistActivity.this,selectWrongTest.BuilderView());
            }
        });

        menu.setAdapter(new ArrayDropdownAdapter(this, R.layout.dropdown_light_item_1line, different));

        menu2.setAdapter(new ArrayDropdownAdapter(this, R.layout.dropdown_light_item_1line, timetOrder));

        menu3.setAdapter(new ArrayDropdownAdapter(this, R.layout.dropdown_light_item_1line, Order));

        menu.setOnItemClickListener(new OnDropdownItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Common.NowDifferent=(different[position]);
                //获取详细试题的url
                String detailedUrl= XSYTools.getWrongUrl(WrongQuestionConstantClass.LIST_URL,ShowWrongBookslistActivity.this);
                XsyMap oneKnowMap=XsyMap.getInterface();
                //学生id
                Long studId= new GetUserInfoClass(ShowWrongBookslistActivity.this).getUserId();
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
                //学科id
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_SUBJECTID,Common.SubjectId);
                //教材id
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_BOOKID,Common.selectBookId);
                //分册id
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_BOOKSECTIONID,Common.bookSection);
                //知识点id
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_KNOWLEDGEID, Common.oneKnowleageId);
                //难度
                //难度
                if(Common.NowDifferent.equals("简单")){
                    ff=String.valueOf(TestVarDefine.TEST_DIFFICULTY_EAISER_GENERAL);
                }else if(Common.NowDifferent.equals("容易")){
                    ff=String.valueOf(TestVarDefine.TEST_DIFFICULTY_EASY_GENERAL);
                }else if(Common.NowDifferent.equals("一般")){
                    ff=String.valueOf(TestVarDefine.TEST_DIFFICULTY_GENERAL_GENERAL);
                }else if(Common.NowDifferent.equals("较难")){
                    ff=String.valueOf(TestVarDefine.TEST_DISCRIMINATION_SMALL_MAX);
                }else if(Common.NowDifferent.equals("难")){
                    ff=String.valueOf(TestVarDefine.TEST_DIFFICULTY_VERYDIFFICULTY_GENERAL);
                }else if(Common.NowDifferent.equals("")){
                    ff="";
                }
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_DIFFICULT,ff);
                //时间排序
                if(Common.NowTimeOrder.equals("今天")){

                    time_order=String.valueOf(WrongQuestionConstantClass.PARAM_TODAY);

                }else if(Common.NowTimeOrder.equals("昨天")){

                    time_order=String.valueOf(WrongQuestionConstantClass.PARAM_YESTODAY);
                }else if(Common.NowTimeOrder.equals("一周之内")){

                    time_order=String.valueOf(WrongQuestionConstantClass.PARAM_WEEK);
                }else if(Common.NowTimeOrder.equals("一月之内")){

                    time_order=String.valueOf(WrongQuestionConstantClass.PARAM_MONTH);
                }else if(Common.NowTimeOrder.equals("全部")){
                    time_order=String.valueOf(WrongQuestionConstantClass.PARAM_ALL);
                }else if(Common.NowTimeOrder.equals("")){
                    time_order="";
                }
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_TIMEAREA,time_order);

                //看是正序还是倒序

                if(Common.NowOrder.equals("时间降序")){
                    DC_Order=String.valueOf(WrongQuestionConstantClass.ORDER_TYPE_TIME_DESC);
                }else if(Common.NowOrder.equals("时间倒序")){
                    DC_Order=String.valueOf(WrongQuestionConstantClass.ORDER_TYPE_TIME_ASC);
                }else if(Common.NowOrder.equals("难度降序")){
                    DC_Order=String.valueOf(WrongQuestionConstantClass.ORDER_TYPE_DIFFICULTY_DESC);
                }else if(Common.NowOrder.equals("难度倒序")){
                    DC_Order=String.valueOf(WrongQuestionConstantClass.ORDER_TYPE_DIFFICULTY_ASC);
                }
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_DATE,DC_Order);

                OkGo.post(detailedUrl).params(oneKnowMap).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        XSYTools.i(s);
                        showWrongHandler.sendMessage(XSYTools.makeNewMessage(Common.SHOWWRONG_LIST,s));
                    }
                });


            }

        });
        menu2.setOnItemClickListener(new OnDropdownItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Common.NowTimeOrder=(timetOrder[position]);

                //获取详细试题的url
                String detailedUrl= XSYTools.getWrongUrl(WrongQuestionConstantClass.LIST_URL,ShowWrongBookslistActivity.this);
                XsyMap oneKnowMap=XsyMap.getInterface();
                //学生id
                Long studId= new GetUserInfoClass(ShowWrongBookslistActivity.this).getUserId();
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
                //学科id
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_SUBJECTID,Common.SubjectId);
                //教材id
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_BOOKID,Common.selectBookId);
                //分册id
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_BOOKSECTIONID,Common.bookSection);
                //知识点id
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_KNOWLEDGEID, Common.oneKnowleageId);
                //难度
                //难度
                if(Common.NowDifferent.equals("简单")){
                    ff=String.valueOf(TestVarDefine.TEST_DIFFICULTY_EAISER_GENERAL);
                }else if(Common.NowDifferent.equals("容易")){
                    ff=String.valueOf(TestVarDefine.TEST_DIFFICULTY_EASY_GENERAL);
                }else if(Common.NowDifferent.equals("一般")){
                    ff=String.valueOf(TestVarDefine.TEST_DIFFICULTY_GENERAL_GENERAL);
                }else if(Common.NowDifferent.equals("较难")){
                    ff=String.valueOf(TestVarDefine.TEST_DISCRIMINATION_SMALL_MAX);
                }else if(Common.NowDifferent.equals("难")){
                    ff=String.valueOf(TestVarDefine.TEST_DIFFICULTY_VERYDIFFICULTY_GENERAL);
                }else if(Common.NowDifferent.equals("")){
                    ff="";
                }
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_DIFFICULT,ff);
                //时间排序
                if(Common.NowTimeOrder.equals("今天")){

                    time_order=String.valueOf(WrongQuestionConstantClass.PARAM_TODAY);

                }else if(Common.NowTimeOrder.equals("昨天")){

                    time_order=String.valueOf(WrongQuestionConstantClass.PARAM_YESTODAY);
                }else if(Common.NowTimeOrder.equals("一周之内")){

                    time_order=String.valueOf(WrongQuestionConstantClass.PARAM_WEEK);
                }else if(Common.NowTimeOrder.equals("一月之内")){

                    time_order=String.valueOf(WrongQuestionConstantClass.PARAM_MONTH);
                }else if(Common.NowTimeOrder.equals("全部")){
                    time_order=String.valueOf(WrongQuestionConstantClass.PARAM_ALL);
                }else if(Common.NowTimeOrder.equals("")){
                    time_order="";
                }
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_TIMEAREA,time_order);

                //看是正序还是倒序

                if(Common.NowOrder.equals("时间降序")){
                    DC_Order=String.valueOf(WrongQuestionConstantClass.ORDER_TYPE_TIME_DESC);
                }else if(Common.NowOrder.equals("时间倒序")){
                    DC_Order=String.valueOf(WrongQuestionConstantClass.ORDER_TYPE_TIME_ASC);
                }else if(Common.NowOrder.equals("难度降序")){
                    DC_Order=String.valueOf(WrongQuestionConstantClass.ORDER_TYPE_DIFFICULTY_DESC);
                }else if(Common.NowOrder.equals("难度倒序")){
                    DC_Order=String.valueOf(WrongQuestionConstantClass.ORDER_TYPE_DIFFICULTY_ASC);
                }else{
                    DC_Order="";
                }
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_DATE,DC_Order);

                OkGo.post(detailedUrl).params(oneKnowMap).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        XSYTools.i(s);

                        showWrongHandler.sendMessage(XSYTools.makeNewMessage(Common.SHOWWRONG_LIST,s));
                    }
                });




            }

        });

        menu3.setOnItemClickListener(new OnDropdownItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Common.NowOrder=(Order[position]);


                //获取详细试题的url
                String detailedUrl= XSYTools.getWrongUrl(WrongQuestionConstantClass.LIST_URL,ShowWrongBookslistActivity.this);
                XsyMap oneKnowMap=XsyMap.getInterface();
                //学生id
                Long studId= new GetUserInfoClass(ShowWrongBookslistActivity.this).getUserId();
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
                //学科id
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_SUBJECTID,Common.SubjectId);
                //教材id
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_BOOKID,Common.selectBookId);
                //分册id
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_BOOKSECTIONID,Common.bookSection);
                //知识点id
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_KNOWLEDGEID, Common.oneKnowleageId);
                //难度
                //难度
                if(Common.NowDifferent.equals("简单")){
                    ff=String.valueOf(TestVarDefine.TEST_DIFFICULTY_EAISER_GENERAL);
                }else if(Common.NowDifferent.equals("容易")){
                    ff=String.valueOf(TestVarDefine.TEST_DIFFICULTY_EASY_GENERAL);
                }else if(Common.NowDifferent.equals("一般")){
                    ff=String.valueOf(TestVarDefine.TEST_DIFFICULTY_GENERAL_GENERAL);
                }else if(Common.NowDifferent.equals("较难")){
                    ff=String.valueOf(TestVarDefine.TEST_DISCRIMINATION_SMALL_MAX);
                }else if(Common.NowDifferent.equals("难")){
                    ff=String.valueOf(TestVarDefine.TEST_DIFFICULTY_VERYDIFFICULTY_GENERAL);
                }else if(Common.NowDifferent.equals("")){
                    ff="";
                }
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_DIFFICULT,ff);
                //时间排序
                if(Common.NowTimeOrder.equals("今天")){

                    time_order=String.valueOf(WrongQuestionConstantClass.PARAM_TODAY);

                }else if(Common.NowTimeOrder.equals("昨天")){

                    time_order=String.valueOf(WrongQuestionConstantClass.PARAM_YESTODAY);
                }else if(Common.NowTimeOrder.equals("一周之内")){

                    time_order=String.valueOf(WrongQuestionConstantClass.PARAM_WEEK);
                }else if(Common.NowTimeOrder.equals("一月之内")){

                    time_order=String.valueOf(WrongQuestionConstantClass.PARAM_MONTH);
                }else if(Common.NowTimeOrder.equals("全部")){
                    time_order=String.valueOf(WrongQuestionConstantClass.PARAM_ALL);
                }else if(Common.NowTimeOrder.equals("")){
                    time_order="";
                }
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_TIMEAREA,time_order);

                //看是正序还是倒序

                if(Common.NowOrder.equals("时间降序")){
                    DC_Order=String.valueOf(WrongQuestionConstantClass.ORDER_TYPE_TIME_DESC);
                }else if(Common.NowOrder.equals("时间倒序")){
                    DC_Order=String.valueOf(WrongQuestionConstantClass.ORDER_TYPE_TIME_ASC);
                }else if(Common.NowOrder.equals("难度降序")){
                    DC_Order=String.valueOf(WrongQuestionConstantClass.ORDER_TYPE_DIFFICULTY_DESC);
                }else if(Common.NowOrder.equals("难度倒序")){
                    DC_Order=String.valueOf(WrongQuestionConstantClass.ORDER_TYPE_DIFFICULTY_ASC);
                }else{
                    DC_Order="";
                }
                oneKnowMap.put(WrongQuestionConstantClass.PARAM_DATE,DC_Order);

                OkGo.post(detailedUrl).params(oneKnowMap).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        XSYTools.i(s);

                        showWrongHandler.sendMessage(XSYTools.makeNewMessage(Common.SHOWWRONG_LIST,s));
                    }
                });
            }

        });

        // 同时只允许一个 DropdownMenu 为打开状态
        MenuManager.group(menu, menu2);
    }
    //得到全部的错题信息然后全部展示出来
    private void getData() {
        String getALLWrongTestUrl=XSYTools.getWrongUrl(WrongQuestionConstantClass.LIST_URL,ShowWrongBookslistActivity.this);;
        //学生id
        Long studId= new GetUserInfoClass(ShowWrongBookslistActivity.this).getUserId();
        XsyMap ALLKnowMap=XsyMap.getInterface();
        //学生id
        ALLKnowMap.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
        //学科id
        ALLKnowMap.put(WrongQuestionConstantClass.PARAM_SUBJECTID,Common.SubjectId);
        OkGo.post(getALLWrongTestUrl).params(ALLKnowMap).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i("showWrongList里面得到的全部试题为"+s);

                showWrongHandler.sendMessage(XSYTools.makeNewMessage(Common.SHOWWRONG_LIST,s));
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ShowWrongBooklist","ShowWrongBooklistActivity已经被暂停了！");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("ShowWrongBooklist","ShowWrongBooklistActivity已经被销毁了！");
    }
}
