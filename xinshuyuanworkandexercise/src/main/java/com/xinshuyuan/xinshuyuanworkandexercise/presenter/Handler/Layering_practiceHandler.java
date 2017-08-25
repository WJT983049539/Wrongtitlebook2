package com.xinshuyuan.xinshuyuanworkandexercise.presenter.Handler;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.VarDefine;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Activity.Layering_practice_Activity;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.SelectConditionTopFragment;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkANS_no_Fragment;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkANS_ok_Fragment;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkExerciseDaubFragment;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkExerciseFillBlankFragment;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkExerciseJudgeFragment;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkExerciseLineFragment;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkExerciseMultSelectFragment;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkExerciseNESTFragment;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkExerciseOPRAFragment;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkExerciseSelsctFragment;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment.WorkOnlyAnswerFragment;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.Work_ShowtestListFragment;

import work.HomeWorkConstantClass;

/**
 * 分册练习的Handler
 * Created by wjt on 2017/7/11.
 */

public class Layering_practiceHandler extends Handler{
    LinearLayout topLinear;
    private Layering_practice_Activity layering_practice_activity;
    private Long userId;
    public Layering_practiceHandler(Layering_practice_Activity layering_practice_activity, Long userId) {
        this.layering_practice_activity=layering_practice_activity;
        this.userId=userId;
        topLinear = (LinearLayout) layering_practice_activity.findViewById(R.id.topshowfragment);
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){

            //完成答题
            case Common.ANS_OK:

                FragmentTransaction transaction4= layering_practice_activity.getFragmentManager().beginTransaction();
                WorkANS_ok_Fragment newFragment1=new WorkANS_ok_Fragment(layering_practice_activity,this,"exercise");
                newFragment1.setInfo(msg.obj.toString());
                transaction4.replace(R.id.show_wrong_content,newFragment1);//showTestInfo_layout
                transaction4.commitAllowingStateLoss();
                break;
            //未完成答题
            case Common.ANS_NO:
                FragmentTransaction transaction5= layering_practice_activity.getFragmentManager().beginTransaction();
                WorkANS_no_Fragment newFragment2=new WorkANS_no_Fragment(this,layering_practice_activity,"exercise");
                newFragment2.setInfo(msg.obj.toString());
                transaction5.replace(R.id.show_wrong_content,newFragment2);
                transaction5.commitAllowingStateLoss();
                break;
            //客观题完成界面
            case  Common.ONLY_ANSWER:

                FragmentTransaction transaction6= layering_practice_activity.getFragmentManager().beginTransaction();
                WorkOnlyAnswerFragment newFragment3=new WorkOnlyAnswerFragment(this,layering_practice_activity,"exercise");
                newFragment3.setInfo(msg.obj.toString());
                transaction6.replace(R.id.show_wrong_content,newFragment3);
                transaction6.commitAllowingStateLoss();
                break;
            case Common.VIEW_INCLUDTEST_SUBTEST:
                Fragment fragment_include=layering_practice_activity.getFragmentManager().findFragmentByTag(Common.FRAGMENT_TAG_INCLUDE);
                if(fragment_include!=null && fragment_include instanceof WorkExerciseNESTFragment){
                    WorkExerciseNESTFragment f=(WorkExerciseNESTFragment) fragment_include;
                    if(msg.obj instanceof TestEntity){
                        TestEntity te=(TestEntity) msg.obj;
                        f.doInitAnsInfo(te);
                    }
                }
                break;

            case Common.WORK_SHOW_LIST:
                //把上面显示
                if(topLinear.getVisibility()==View.GONE){
                    topLinear.setVisibility(View.VISIBLE);
                }

                FragmentTransaction transaction= layering_practice_activity.getFragmentManager().beginTransaction();
                Work_ShowtestListFragment showtestlistfragment=new Work_ShowtestListFragment();
                showtestlistfragment.setInfo(msg.obj.toString(),this);
                transaction.replace(R.id.show_wrong_content,showtestlistfragment);
                transaction.commitAllowingStateLoss();
                break;
                //显示单个详细信息,转到练习的试题界面


            case Common.WORK_SHOW_TOP:
                FragmentTransaction transaction9= layering_practice_activity.getFragmentManager().beginTransaction();
                SelectConditionTopFragment selectConditionTopFragment=new SelectConditionTopFragment(layering_practice_activity);
                selectConditionTopFragment.setInfo(msg.obj.toString(),this);
                transaction9.replace(R.id.topshowfragment,selectConditionTopFragment);
                transaction9.commitAllowingStateLoss();
                break;

            case Common.PRIVIEW_SHOW_TEXT:
                //把上面隐藏掉
                topLinear.setVisibility(View.GONE);

                    Object object=msg.obj;
                if(object!=null){
                    TestEntity testEntity=(TestEntity)object;
                    //该展现试题了，根据试题类型，转到不同个处理页面当中
                    Fragment newFragment=null;
                    String tag="";
                    switch(testEntity.getTestType()){
                        //单选题
                        case HomeWorkConstantClass.TESTTYPE_SINGLE_SELECT:
                            newFragment = new WorkExerciseSelsctFragment(this,layering_practice_activity);
                            tag= VarDefine.FRAGMENT_TAG_SINGLESELECT;
                            break;
                        //多选题
                        case HomeWorkConstantClass.TESTTYPE_MULTI_SELECT:
                            newFragment=new WorkExerciseMultSelectFragment(this,layering_practice_activity);
                            tag= VarDefine.FRAGMENT_TAG_MULTISELECT;
                            break;
                        //判断题
                        case HomeWorkConstantClass.TESTTYPE_YESORNO_SELECT:
                            newFragment=new WorkExerciseJudgeFragment(this,layering_practice_activity);
                            tag=VarDefine.FRAGMENT_TAG_JUDGE;
                        //涂抹题
                        case HomeWorkConstantClass.TESTTYPE_PAINT:
                            newFragment=new WorkExerciseDaubFragment(this,layering_practice_activity);
                            tag=VarDefine.FRAGMENT_TAG_DAUB;
                            break;
                        //填空题
                        case HomeWorkConstantClass.TESTTYPE_FILL_BLANK:
                            newFragment=new WorkExerciseFillBlankFragment(this,layering_practice_activity);
                            tag=VarDefine.FRAGMENT_TAG_FLBANK;
                            break;
                        //连线题
                        case HomeWorkConstantClass.TESTTYPE_LINE:

                            newFragment=new WorkExerciseLineFragment(this,layering_practice_activity);
                            tag=VarDefine.FRAGMENT_TAG_LINE;
                            break;
                        //操作题
                        case HomeWorkConstantClass.TESTTYPE_OPRATE:
                            newFragment=new WorkExerciseOPRAFragment(this,layering_practice_activity);
                            tag=VarDefine.FRAGMENT_TAG_OPRATE;
                            break;
                        //嵌套题
                        case HomeWorkConstantClass.TESTTYPE_NEST:
                            newFragment=new WorkExerciseNESTFragment(this,layering_practice_activity);
                            tag=VarDefine.FRAGMENT_TAG_INCLUDE;
                            break;

                    }

                    Bundle bundle=new Bundle();
                    bundle.putString("testId",String.valueOf(userId));
                    bundle.putSerializable("testEntity", testEntity);
                    bundle.putSerializable("Model","exercise");//练习模式
                    newFragment.setArguments(bundle);//第二种传值方式
                    FragmentTransaction transaction2 =layering_practice_activity.getFragmentManager().beginTransaction();
                    transaction2.replace(R.id.show_wrong_content,newFragment,tag);
                    transaction2.commitAllowingStateLoss();
                }
                break;

        }
    }
}
