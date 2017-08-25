package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibpd.xsy.varDefine.WrongQuestionConstantClass;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.AnswerEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.GetUserInfoClass;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;
import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.ExerciseParmas;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.ExerciseHandler;
import xinshuyuan.com.wrongtitlebook.R;


/**练习在这个Activity
 * Created by Administrator on 2017/6/21.
 */

public class ExerciseTestActivity extends Activity{
    //知识点id
    private Long knowledgePointId;
    //难度
    private String different;
    //试题id
    private long ShiTiId;
    //学生id
    private Integer studentId;
    //学科id
    private Integer subjectId;
    //年级id
    private Integer gradeId;

    private TestEntity testEntity;

    private ExerciseHandler exerciseHandler;
    private ImageView xingxing;
    private TextView exercise_knowledgePointName;
    private String knowledgePointName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window=getWindow();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager.LayoutParams layoutParams= window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏底部键盘，一直不会弹出
        layoutParams.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(layoutParams);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        knowledgePointId=bundle.getLong("knowledgePointId");
        different=bundle.getString("difficulty");
        ShiTiId=bundle.getLong("ShiTiId");
        studentId=bundle.getInt("studentId");
        subjectId=bundle.getInt("subjectId");
        gradeId=bundle.getInt("gradeId");
        knowledgePointName=bundle.getString("knowledgePointName");
        //保存获取试题的参数以便继续答题获取试题使用
        ExerciseParmas exerciseParmas=new ExerciseParmas();
        exerciseParmas.setSubjectId(subjectId);
        exerciseParmas.setGradeId(gradeId);
        exerciseParmas.setKnowledgePointId(knowledgePointId);
        exerciseParmas.setDifferent(different);
        //保存参数
        Common.setExerciseParmas(exerciseParmas);
        //实例化Handler
        exerciseHandler=new ExerciseHandler(this);

        setContentView(R.layout.exercise_activity_layout);
        xingxing= (ImageView) findViewById(R.id.exercise_xingxingimageView);
        Double longdifferent=Double.valueOf(different);

        if(0.9<=longdifferent&&longdifferent<1){
            xingxing.setImageResource(R.drawable.yikexing);
        }else if(0.75<=longdifferent&&longdifferent<0.9){
            xingxing.setImageResource(R.drawable.liangkexing);
        }else if(0.5<=longdifferent&&longdifferent<0.75){
            xingxing.setImageResource(R.drawable.sankexing);
        }else if(0.35<=longdifferent&&longdifferent<0.5){
            xingxing.setImageResource(R.drawable.sikexing);
        }else if(0.15<=longdifferent&&longdifferent<0.35){
            xingxing.setImageResource(R.drawable.wukexing);
        }else if(longdifferent<0.15){
            xingxing.setImageResource(R.drawable.wukexing);
        }

        exercise_knowledgePointName=(TextView)findViewById(R.id.exercise_knowledgePointName);
        //得到试题数据
        exercise_knowledgePointName.setText(knowledgePointName);
         getTestDate();
    }

    private void getTestDate() {
        String getTestInfoUrl= XSYTools.getWrongUrl(WrongQuestionConstantClass.PRACTICE_URL,ExerciseTestActivity.this);
        XsyMap map=XsyMap.getInterface();

        Long studId= new GetUserInfoClass(ExerciseTestActivity.this).getUserId();
        map.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));//学生id
        map.put(WrongQuestionConstantClass.PARAM_SUBJECTID, String.valueOf(subjectId));//学科id
        map.put(WrongQuestionConstantClass.PARAM_GRADEID,String.valueOf(gradeId));//年级id
        map.put(WrongQuestionConstantClass.PARAM_KNOWLEDGEID,String.valueOf(knowledgePointId));
        map.put(WrongQuestionConstantClass.PARAM_DIFFICULT,different);
        OkGo.post(getTestInfoUrl).params(map).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i(s);

                try {
                    //得到试题信息
                    JSONObject object=new JSONObject(s);
                    if(object.has("status")){
                        String aa=object.getString("status");
                        if(aa.equals("-3")){
                            XSYTools.showToastmsg(ExerciseTestActivity.this,"获取失败，试题不存在");
                            //返回到错题列表展示的view
//                            Intent intent=new Intent(,ShowWrongBookslistActivity.class);
//                            intent.putExtra("project",msg.obj.toString());
//                            mainActivity.startActivity(intent);
                            ExerciseTestActivity.this.finish();
                            return;
                        }else{
                            exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.VERY_GOOD,""));
                            return;
                        }
                    }
                    //本题类型
                    Integer TestType = object.getInt("itemType");
                    //得到题干
                   String point = object.getString("itemPoint");
                    //试题id
                   long testId=object.getLong("id");
                    String answerAnalysis=object.getString("answerAnalysis");

                   String Subject=object.getString("subjectName");
                    //装入javabean
                    testEntity =new TestEntity();
                    testEntity.setTestId(testId);
                    testEntity.setTestType(TestType);
                    testEntity.setPoint(point);
                    testEntity.setSubject(Subject);
                    testEntity.setAnswerAnalysis(answerAnalysis);
                    JSONArray jsonarray = object.getJSONArray("answerList");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obb = jsonarray.getJSONObject(i);
                        long answerid = obb.getLong("id");
                        long answertestId = obb.getLong("testId");
                        String optionTitle = obb.getString("optionTitle");
                        String optionAnswer = obb.getString("optionAnswer");
                        Boolean isRealAnswer = obb.getBoolean("isRealAnswer");

                        AnswerEntity answerEntity = new AnswerEntity();
                        answerEntity.setId(answerid);
                        answerEntity.setTestId(answertestId);
                        answerEntity.setIsRealAnswer(isRealAnswer);
                        answerEntity.setOptionAnswer(optionAnswer);
                        answerEntity.setOptionTitle(optionTitle);
                        testEntity.fillAnser(answerEntity);
                    }
                    exerciseHandler.sendMessage(XSYTools.makeNewMessage(Common.EXERCISE_TEST_SHOW,testEntity));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                XSYTools.showToastmsg(ExerciseTestActivity.this,"服务器数据异常！");
            }
        });

    }
    //为了在fragment里面实现接口
    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(10);

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public interface MyOnTouchListener {

         boolean onTouch(MotionEvent ev);
    }

    /**
     * 改变星星
     * @param different
     */
                     public void gbDifferent(Double different){

                         if(0.9<=different&&different<1){
                             xingxing.setImageResource(R.drawable.yikexing);
                         }else if(0.75<=different&&different<0.9){
                             xingxing.setImageResource(R.drawable.liangkexing);
                         }else if(0.5<=different&&different<0.75){
                             xingxing.setImageResource(R.drawable.sankexing);
                         }else if(0.35<=different&&different<0.5){
                             xingxing.setImageResource(R.drawable.sikexing);
                         }else if(0.15<=different&&different<0.35){
                             xingxing.setImageResource(R.drawable.wukexing);
                         }else if(different<0.15){
                             xingxing.setImageResource(R.drawable.wukexing);
                         }

                      }


}
