package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ibpd.xsy.varDefine.GlobalVarDefine;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.AnswerEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.PerferenceService;
import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.PreViewTestHandler;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Fragment.AnswerAnalysisFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.CustomUpTestFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.DaubFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.FillBlankFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.IncludeFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.JudgeFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.LINEFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.MultiSelectFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.OPRATEFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.SingleSelectFragment;


/**
 * 展示试题的界面，有预览试题，和答案解析，下面有关闭和练习按钮 展示试题和错题，右面滑动有答案解析
 * Created by wjt on 2017/5/26.
 */
public class PreviewTestQuestionActivity extends Activity {

    private  Button closeButton;

    private Button exerciseButton;
    //答案解析
    private String answer_annlysis;

    //试题实体
    private TestEntity testEntity;
    //选项或者答案的javabean
    private AnswerEntity answerEntity;
    //题型
    private TextView test_type;
    //题干
    private TextView test_title;
    //知识点
    private TextView knowledgePointNametext;
    //难度显示的星星
    private ImageView diffecth_image;
    private PreViewTestHandler preViewTestHandler;
    //Handler处理的类
    //PriviewTestHandler Phandler;
    //主要内容区
    private LinearLayout showContentlinear;
    //开始显示答案与解析的布局
    private LinearLayout startanswerexplanation;
    //答案与解析的布局
    private LinearLayout answerexplanation;
    //预览试题显示的碎片放置区域
    private LinearLayout preiview_test_show_fragment;
    //滑动菜单

    //题型
    private Integer TestType;
    //知识点
    private String knowledgePointName;
    //题干
    private String problem;
    //难度
    private String difficultyreal;
    //试题id
    private Long testsId;

    //answer的javabean
    private Boolean isRealAnswer;
    private String optionTitle;
    private String optionAnswer;
    private Long answertestId;
    private Long answerid;

    private DrawerLayout drawer_layout;

    //JSOn数据
    String jsonData;
    //知识点id
    private Integer knowledgePointId;
    private long ShiTiId;
    private Integer studentId;
    private Integer subjectId;
    private Integer gradeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Window window=getWindow();
        WindowManager.LayoutParams layoutParams= window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏底部键盘，一直不会弹出
        layoutParams.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(layoutParams);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        preViewTestHandler=new PreViewTestHandler(this);
        Intent intent = getIntent();

        //得到Json数据

        //{"answerAnalysis":"","answerCount":0,"bookId":3,"bookSectionId":5,"classId":1,"createTime":{"date":25,"day":4,"hours":18,"minutes":24,"month":4,"nanos":0,"seconds":58,"time":1495707898000,"timezoneOffset":-480,"year":117},"difficulty":0.9,"discrimination":0.15,"gradeId":1,"id":24,"knowledgePointId":2,"knowledgePointName":"\u500d\u7684\u8ba4\u8bc6\uff081\uff09","lastTime":{"date":25,"day":4,"hours":18,"minutes":56,"month":4,"nanos":0,"seconds":5,"time":1495709765000,"timezoneOffset":-480,"year":117},"problem":"3*6=","realAnswer":"","rightCount":0,"score":0,"sourceId":1,"sourceType":2,"stageId":1,"state":false,"studAnswer":"B   ","studentId":2,"subjectId":2,"testId":84,"type":1,"wrongCount":0,"answerList":[{"id":163,"isRealAnswer":true,"optionAnswer":"18<br/>","optionTitle":"A","order":0,"testId":84},{"id":164,"isRealAnswer":false,"optionAnswer":"21<br/>","optionTitle":"B","order":1,"testId":84}]}
        jsonData = intent.getStringExtra("testQuestionInfo");
        setContentView(R.layout.preview_test_question_layout);
        inint();
    }

    //初始化
    private void inint() {
        //显示难度

        //关闭按钮
        closeButton= (Button) findViewById(R.id.close_button2);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //转到试题列表上面，防止activity异常关闭
//                finish();
                PerferenceService service=new PerferenceService(PreviewTestQuestionActivity.this);
                String Project=service.getsharedPre().getString("Project","");
                Intent intent=new Intent(PreviewTestQuestionActivity.this,ShowWrongBookslistActivity.class);
                intent.putExtra("project",Project);
                PreviewTestQuestionActivity.this.startActivity(intent);
            }
        });


        //练习按钮
        exerciseButton= (Button) findViewById(R.id.exercise);

        //点击练习按钮，就跑到新的试题练习
        exerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PreviewTestQuestionActivity.this,ExerciseTestActivity.class);
                Bundle bundle=new Bundle();
                bundle.putLong("knowledgePointId",knowledgePointId);
                bundle.putString("difficulty", difficultyreal);
                bundle.putLong("ShiTiId",ShiTiId);
                bundle.putInt("studentId",studentId);
                bundle.putInt("subjectId",subjectId);

                bundle.putInt("gradeId",gradeId);
                bundle.putString("knowledgePointName",knowledgePointName);
                intent.putExtras(bundle);
                startActivity(intent);
                PreviewTestQuestionActivity.this.finish();
            }
        });

        drawer_layout= (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置全屏高亮
        drawer_layout.setScrimColor(0x00ffffff);
        showContentlinear = (LinearLayout) findViewById(R.id.shwo_content_linear);
        startanswerexplanation = (LinearLayout) findViewById(R.id.startanswer_explanation);
        answerexplanation = (LinearLayout) findViewById(R.id.answer_explanation);
        preiview_test_show_fragment = (LinearLayout) findViewById(R.id.preiview_test_show_fragment);
        //答题类型
        test_type = (TextView) findViewById(R.id.exercise_test_type);
//        test_title=(TextView)findViewById(R.id.test_title);
        knowledgePointNametext = (TextView) findViewById(R.id.exercise_knowledgePointName);
        diffecth_image = (ImageView) findViewById(R.id.exercise_xingxingimageView);

        //滑动界面的监听事件
        drawer_layout.setDrawerListener(new DrawerLayout.DrawerListener() {


            /**
             * 当抽屉被滑动的时候调用此方法
             * arg1 表示 滑动的幅度（0-1）
             */
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                if (startanswerexplanation.getVisibility() == View.VISIBLE) {
                    startanswerexplanation.setVisibility(View.GONE);
                }


            }
            /**
             * 当一个抽屉被完全打开的时候被调用
             */
            @Override
            public void onDrawerOpened(View drawerView) {

                if (startanswerexplanation.getVisibility() == View.VISIBLE) {
                    startanswerexplanation.setVisibility(View.GONE);
                }




            }
            /**
             * 当一个抽屉被完全关闭的时候被调用
             */
            @Override
            public void onDrawerClosed(View drawerView) {
                if (startanswerexplanation.getVisibility() == View.GONE) {
                    startanswerexplanation.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        startanswerexplanation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer_layout.openDrawer(Gravity.RIGHT);
                //如果是隐藏状态就显示出来
                if (startanswerexplanation.getVisibility() == View.VISIBLE) {
                    startanswerexplanation.setVisibility(View.GONE);
                }

            }
        });

        //解析json数据

        try {
            analysis();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * 解析json
     */
    private void analysis() throws JSONException {

        //实例化试题实体的类
        testEntity = new TestEntity();

        JSONObject object = new JSONObject(jsonData);

        //难度difficulty
        Double difficulty = object.getDouble("difficulty");
        difficultyreal=String.valueOf(difficulty);

        //得到题干
        problem = object.getString("problem");
        //本题类型
        TestType = object.getInt("type");
        //知识点id
        testsId=object.getLong("id");
        //试题id
        ShiTiId=object.getLong("testId");
        //学生id
        studentId=object.getInt("studentId");
        //学科
         subjectId=object.getInt("subjectId");
        //年级id
         gradeId=object.getInt("gradeId");


        testEntity.setTestType(TestType);
        testEntity.setPoint(problem);
        testEntity.setTestId(testsId);

        //知识点and 知识点id
        knowledgePointId= object.getInt("knowledgePointId");

        knowledgePointName = object.getString("knowledgePointName");
        answer_annlysis=object.getString("answerAnalysis");
        XSYTools.i("有没有答案解析"+answer_annlysis);
        knowledgePointNametext.setText(knowledgePointName);

        if(0.9<=difficulty&&difficulty<1){
            diffecth_image.setImageResource(R.drawable.yikexing);

        }else if(0.75<=difficulty&&difficulty<0.9){
            diffecth_image.setImageResource(R.drawable.liangkexing);
        }else if(0.5<=difficulty&&difficulty<0.75){
            diffecth_image.setImageResource(R.drawable.sankexing);
        }else if(0.35<=difficulty&&difficulty<0.5){
            diffecth_image.setImageResource(R.drawable.sikexing);
        }else if(0.15<=difficulty&&difficulty<0.35){
            diffecth_image.setImageResource(R.drawable.wukexing);
        }else if(difficulty<0.15){
            diffecth_image.setImageResource(R.drawable.wukexing);
        }



        if(object.has("answerList")){

        JSONArray jsonarray = object.getJSONArray("answerList");


        for (int i = 0; i < jsonarray.length(); i++) {

            JSONObject obb = jsonarray.getJSONObject(i);
            answerid = obb.getLong("id");
            answertestId = obb.getLong("testId");
            optionTitle = obb.getString("optionTitle");
            optionAnswer = obb.getString("optionAnswer");
            isRealAnswer = obb.getBoolean("isRealAnswer");

            answerEntity = new AnswerEntity();
            answerEntity.setId(answerid);
            answerEntity.setTestId(answertestId);
            answerEntity.setIsRealAnswer(isRealAnswer);
            answerEntity.setOptionAnswer(optionAnswer);
            answerEntity.setOptionTitle(optionTitle);
            testEntity.fillAnser(answerEntity);
        }
        }
        //显示fragment
        showFragment();
    }

    //显示fragment
    private void showFragment() {
        /**
         * 判断类型，然后调用fragment
         */
        //如果是单项选择题
        if (TestType == GlobalVarDefine.TESTTYPE_SINGLE_SELECT) {
            test_type.setText("单选题");
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            AnswerAnalysisFragment answerAnalysisFragment=new AnswerAnalysisFragment();
            answerAnalysisFragment.setInfo(answer_annlysis);
            SingleSelectFragment singSelect = new SingleSelectFragment();
            singSelect.setInfo(testEntity);
            transaction.replace(R.id.preiview_test_show_fragment, singSelect,Common.FRAGMENT_TAG_SINGLESELECT);
            transaction.replace(R.id.answer_explanation, answerAnalysisFragment);
            transaction.commitAllowingStateLoss();
            //多选
        } else if (TestType == GlobalVarDefine.TESTTYPE_MULTI_SELECT) {
            test_type.setText("多选题");
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            MultiSelectFragment multiSelect = new MultiSelectFragment();
            multiSelect.setInfo(testEntity);
            transaction.replace(R.id.preiview_test_show_fragment, multiSelect,Common.FRAGMENT_TAG_MULTISELECT);
            AnswerAnalysisFragment answerAnalysisFragment=new AnswerAnalysisFragment();
            answerAnalysisFragment.setInfo(answer_annlysis);
            transaction.replace(R.id.answer_explanation, answerAnalysisFragment);
            transaction.commitAllowingStateLoss();
            //判断题
        } else if (TestType==GlobalVarDefine.TESTTYPE_YESORNO_SELECT) {
            test_type.setText("判断题");
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            JudgeFragment judgefragment = new JudgeFragment();
            judgefragment.setInfo(testEntity);
            transaction.replace(R.id.preiview_test_show_fragment, judgefragment,Common.FRAGMENT_TAG_YESORNO_SELECT);
            AnswerAnalysisFragment answerAnalysisFragment=new AnswerAnalysisFragment();
            answerAnalysisFragment.setInfo(answer_annlysis);
            transaction.replace(R.id.answer_explanation, answerAnalysisFragment);
            transaction.commitAllowingStateLoss();
          //填空题
        }else if(TestType==GlobalVarDefine.TESTTYPE_FILL_BLANK){
            test_type.setText("填空题");
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            FillBlankFragment fillblank = new FillBlankFragment();
            fillblank.setInfo(testEntity);
            transaction.replace(R.id.preiview_test_show_fragment, fillblank,Common.FRAGMENT_TAG_FILLBLANK);
            AnswerAnalysisFragment answerAnalysisFragment=new AnswerAnalysisFragment();
            answerAnalysisFragment.setInfo(answer_annlysis);
            transaction.replace(R.id.answer_explanation, answerAnalysisFragment);
            transaction.commitAllowingStateLoss();

            //操作题
        }else if(TestType==GlobalVarDefine.TESTTYPE_OPRATE){
            test_type.setText("操作题");
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            OPRATEFragment oprate = new OPRATEFragment();
            oprate.setInfo(testEntity);
            transaction.replace(R.id.preiview_test_show_fragment, oprate,Common.FRAGMENT_TAG_OPRATE);
            AnswerAnalysisFragment answerAnalysisFragment=new AnswerAnalysisFragment();
            answerAnalysisFragment.setInfo(answer_annlysis);
            transaction.replace(R.id.answer_explanation, answerAnalysisFragment);
            transaction.commitAllowingStateLoss();
            //涂抹题
        }else if(TestType==GlobalVarDefine.TESTTYPE_PAINT){
            test_type.setText("涂抹题");
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            DaubFragment daubFragment = new DaubFragment();
            daubFragment.setInfo(testEntity);
            transaction.replace(R.id.preiview_test_show_fragment, daubFragment,Common.FRAGMENT_TAG_PAINT);
            AnswerAnalysisFragment answerAnalysisFragment=new AnswerAnalysisFragment();
            answerAnalysisFragment.setInfo(answer_annlysis);
            transaction.replace(R.id.answer_explanation, answerAnalysisFragment);
            transaction.commitAllowingStateLoss();
            //连线
        }else if(TestType==GlobalVarDefine.TESTTYPE_LINE){
            test_type.setText("连线题");
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            LINEFragment lineFragment = new LINEFragment();
            lineFragment.setInfo(testEntity);
            transaction.replace(R.id.preiview_test_show_fragment, lineFragment,Common.FRAGMENT_TAG_LINE);
            AnswerAnalysisFragment answerAnalysisFragment=new AnswerAnalysisFragment();
            answerAnalysisFragment.setInfo(answer_annlysis);
            transaction.replace(R.id.answer_explanation, answerAnalysisFragment);
            transaction.commitAllowingStateLoss();

            //嵌套
        }else if(TestType==GlobalVarDefine.TESTTYPE_NEST){
            test_type.setText("嵌套题");
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            IncludeFragment includeFragment=new IncludeFragment();
            includeFragment.setInfo(testEntity,preViewTestHandler);
            transaction.replace(R.id.preiview_test_show_fragment, includeFragment, Common.FRAGMENT_TAG_INCLUDE);
            AnswerAnalysisFragment answerAnalysisFragment=new AnswerAnalysisFragment();
            answerAnalysisFragment.setInfo(answer_annlysis);
            transaction.replace(R.id.answer_explanation, answerAnalysisFragment);
            transaction.commitAllowingStateLoss();
            //自定义拍照单选
        }else if(TestType==GlobalVarDefine.TESTTYPE_NEW_SIGLE_SELECT){

//            test_type.setText("自定义拍照单选");
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            CustomSelectFragment customSelectFragment=new CustomSelectFragment();
//            customSelectFragment.setInfo(testEntity);
//            transaction.replace(R.id.preiview_test_show_fragment, customSelectFragment,Common.FRAGMENT_TAG_SINGLESELECT);
//            AnswerAnalysisFragment answerAnalysisFragment=new AnswerAnalysisFragment();
//            answerAnalysisFragment.setInfo(answer_annlysis);
//            transaction.replace(R.id.answer_explanation, answerAnalysisFragment);
//            transaction.commitAllowingStateLoss();

            //自定义拍照多选
        }else if(TestType==GlobalVarDefine.TESTTYPE_NEW_MULTI_SELECT){
//            test_type.setText("自定义拍照多选");
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            CustomSelectFragment customSelectFragment=new CustomSelectFragment();
//            customSelectFragment.setInfo(testEntity);
//            transaction.replace(R.id.preiview_test_show_fragment, customSelectFragment,Common.FRAGMENT_TAG_SINGLESELECT);
//            AnswerAnalysisFragment answerAnalysisFragment=new AnswerAnalysisFragment();
//            answerAnalysisFragment.setInfo(answer_annlysis);
//            transaction.replace(R.id.answer_explanation, answerAnalysisFragment);
//            transaction.commitAllowingStateLoss();
            //自定义上传的试题
        }else if(TestType==0){
            test_type.setText("自定义上传试题");
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            CustomUpTestFragment customUpFragment=new CustomUpTestFragment();
            customUpFragment.setInfo(testEntity);
            transaction.replace(R.id.preiview_test_show_fragment, customUpFragment);
            AnswerAnalysisFragment answerAnalysisFragment=new AnswerAnalysisFragment();
            answerAnalysisFragment.setInfo(answer_annlysis);
            transaction.replace(R.id.answer_explanation, answerAnalysisFragment);
            transaction.commitAllowingStateLoss();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        XSYTools.i("PreviewTestQuestionActivity暂停了");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XSYTools.i("PreviewTestQuestionActivity销毁了");
    }
}
