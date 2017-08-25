package com.xinshuyuan.xinshuyuanworkandexercise.presenter.Handler;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.PerferenceService;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.VarDefine;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.WorkInfoBean;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Activity.ProjectWorkShowActivity;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Activity.TestStatisticeFragment;
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
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.SelectStatisticeFragment;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ShowTestListFragment;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ShowWorkListFragment;

import work.HomeWorkConstantClass;

/**
 * Created by Administrator on 2017/7/5.
 */

public class ProjectWorkShowActivityHandler extends Handler {
    private ProjectWorkShowActivity projectWorkShowActivity;

    public ProjectWorkShowActivityHandler(ProjectWorkShowActivity projectWorkShowActivity) {
        this.projectWorkShowActivity = projectWorkShowActivity;
    }

    @Override

    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            //主界面显示作业列表
            case Common.SHOW_WORKLIST_INFO_FRAGMENT:

                FragmentManager managet = projectWorkShowActivity.getFragmentManager();
                ShowWorkListFragment showWorkListFragment = new ShowWorkListFragment();
                showWorkListFragment.setInfo(msg.obj.toString(), this);
                FragmentTransaction transaction = managet.beginTransaction();
                transaction.replace(R.id.showproject_info_list_fragment, showWorkListFragment);
                transaction.commitAllowingStateLoss();
                break;
            //显示试题列表
            case Common.TEST_LIST_SHOW:
                Object object = msg.obj;
                if (object instanceof WorkInfoBean) {
                    WorkInfoBean workinfobean = (WorkInfoBean) object;
                    FragmentManager managet2 = projectWorkShowActivity.getFragmentManager();
                    ShowTestListFragment showTestListFragment = new ShowTestListFragment();
                    showTestListFragment.setInfo(this);
                    FragmentTransaction transaction2 = managet2.beginTransaction();
                    transaction2.replace(R.id.showproject_info_list_fragment, showTestListFragment);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("workinfobean", workinfobean);
                    showTestListFragment.setArguments(bundle);
                    transaction2.commitAllowingStateLoss();
                }
                break;
            //进入统计界面
            case Common.STATISTICE_FTAGMENT:
                Object object2 = msg.obj;
                if (object2 instanceof WorkInfoBean) {
                    WorkInfoBean workinfobean = (WorkInfoBean) object2;
                    FragmentManager manager = projectWorkShowActivity.getFragmentManager();

                    SelectStatisticeFragment selectStatisticeFragment = new SelectStatisticeFragment();
                    selectStatisticeFragment.setInfo(this);
                    FragmentTransaction transaction3 = manager.beginTransaction();
                    transaction3.replace(R.id.showproject_info_list_fragment, selectStatisticeFragment);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("workinfobean", workinfobean);
                    selectStatisticeFragment.setArguments(bundle);
                    transaction3.commitAllowingStateLoss();
                }
                break;
            case Common.SHOW_SING_TEST:
                //显示详细信息
                Object oo=msg.obj;
                if(oo instanceof TestEntity){
                    TestEntity testEntity= (TestEntity)oo;
                    //该展现试题了，根据试题类型，转到不同个处理页面当中
                    Fragment newFragment=null;
                    String tag="";
                    switch(testEntity.getTestType()){
                        //单选题
                        case HomeWorkConstantClass.TESTTYPE_SINGLE_SELECT:
                            newFragment = new WorkExerciseSelsctFragment(this,projectWorkShowActivity);
                            tag= VarDefine.FRAGMENT_TAG_SINGLESELECT;
                            break;
                        //多选题
                        case HomeWorkConstantClass.TESTTYPE_MULTI_SELECT:
                            newFragment=new WorkExerciseMultSelectFragment(this,projectWorkShowActivity);
                            tag= VarDefine.FRAGMENT_TAG_MULTISELECT;
                            break;
                        //判断题
                        case HomeWorkConstantClass.TESTTYPE_YESORNO_SELECT:
                            newFragment=new WorkExerciseJudgeFragment(this,projectWorkShowActivity);
                            tag=VarDefine.FRAGMENT_TAG_JUDGE;
                            break;
                            //涂抹题
                        case HomeWorkConstantClass.TESTTYPE_PAINT:
                            newFragment=new WorkExerciseDaubFragment(this,projectWorkShowActivity);
                            tag=VarDefine.FRAGMENT_TAG_DAUB;
                            break;
                        //填空题
                        case HomeWorkConstantClass.TESTTYPE_FILL_BLANK:
                            newFragment=new WorkExerciseFillBlankFragment(this,projectWorkShowActivity);
                            tag=VarDefine.FRAGMENT_TAG_FLBANK;
                            break;
                        //连线题
                        case HomeWorkConstantClass.TESTTYPE_LINE:

                            newFragment=new WorkExerciseLineFragment(this,projectWorkShowActivity);
                            tag=VarDefine.FRAGMENT_TAG_LINE;
                            break;
                        //操作题
                        case HomeWorkConstantClass.TESTTYPE_OPRATE:
                            newFragment=new WorkExerciseOPRAFragment(this,projectWorkShowActivity);
                            tag= VarDefine.FRAGMENT_TAG_OPRATE;
                            break;
                        //嵌套题
                        case HomeWorkConstantClass.TESTTYPE_NEST:
                            newFragment=new WorkExerciseNESTFragment(this,projectWorkShowActivity);
                            tag=VarDefine.FRAGMENT_TAG_INCLUDE;
                            break;

                    }
                    PerferenceService service=new PerferenceService(projectWorkShowActivity);
                    Long userId=service.getsharedPre().getLong("WorkId",0);
                    Bundle bundle=new Bundle();
                    bundle.putString("testId",String.valueOf(userId));
                    bundle.putString("Model","work");//作业模式
                    bundle.putSerializable("testEntity", testEntity);
                    newFragment.setArguments(bundle);//第二种传值方式
                    FragmentTransaction transaction2 =projectWorkShowActivity.getFragmentManager().beginTransaction();
                    transaction2.replace(R.id.showproject_info_list_fragment,newFragment,tag);
                    transaction2.commitAllowingStateLoss();
                }
                break;

            //试题的统计
            case Common.WORK_TEST_STATISTICE:
                Object object3 = msg.obj;
                if (object3 instanceof WorkInfoBean) {
                    WorkInfoBean workinfobean = (WorkInfoBean) object3;
                    FragmentManager manager = projectWorkShowActivity.getFragmentManager();
                    TestStatisticeFragment testStatisticeFragment=new TestStatisticeFragment(projectWorkShowActivity);
                    testStatisticeFragment.setInfo(this);
                    FragmentTransaction transaction4 = manager.beginTransaction();
                    transaction4.replace(R.id.showproject_info_list_fragment, testStatisticeFragment);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("workinfobean", workinfobean);
                    testStatisticeFragment.setArguments(bundle);
                    transaction4.commitAllowingStateLoss();
                }
                break;
            case Common.VIEW_INCLUDTEST_SUBTEST:
                Fragment fragment_include=projectWorkShowActivity.getFragmentManager().findFragmentByTag(Common.FRAGMENT_TAG_INCLUDE);
                if(fragment_include!=null && fragment_include instanceof WorkExerciseNESTFragment){
                    WorkExerciseNESTFragment f=(WorkExerciseNESTFragment) fragment_include;
                    if(msg.obj instanceof TestEntity){
                        TestEntity te=(TestEntity) msg.obj;
                        f.doInitAnsInfo(te);
                    }
                }
                break;
            //完成答题
            case Common.ANS_OK:

                FragmentTransaction transaction4= projectWorkShowActivity.getFragmentManager().beginTransaction();
                WorkANS_ok_Fragment newFragment1=new WorkANS_ok_Fragment(projectWorkShowActivity,this,"work");
                newFragment1.setInfo(msg.obj.toString());
                transaction4.replace(R.id.showproject_info_list_fragment,newFragment1);
                transaction4.commitAllowingStateLoss();
                break;
            //未完成答题
            case Common.ANS_NO:
                FragmentTransaction transaction5= projectWorkShowActivity.getFragmentManager().beginTransaction();
                WorkANS_no_Fragment newFragment2=new WorkANS_no_Fragment(this,projectWorkShowActivity,"work");
                newFragment2.setInfo(msg.obj.toString());
                transaction5.replace(R.id.showproject_info_list_fragment,newFragment2);
                 transaction5.commitAllowingStateLoss();
                break;
            //客观题完成界面
            case  Common.ONLY_ANSWER:

                FragmentTransaction transaction6= projectWorkShowActivity.getFragmentManager().beginTransaction();
                WorkOnlyAnswerFragment newFragment3=new WorkOnlyAnswerFragment(this,projectWorkShowActivity,"work");
                newFragment3.setInfo(msg.obj.toString());
                transaction6.replace(R.id.showproject_info_list_fragment,newFragment3);
                transaction6.commitAllowingStateLoss();
                break;
                //内部分层练习
            case Common.WORK_TEST_NEIBU_EXERCISE:
                //显示详细信息
                Object neibuObject=msg.obj;
                if(neibuObject instanceof TestEntity){
                    TestEntity testEntity= (TestEntity)neibuObject;
                    //该展现试题了，根据试题类型，转到不同个处理页面当中
                    Fragment newFragment=null;
                    String tag="";
                    switch(testEntity.getTestType()){
                        //单选题
                        case HomeWorkConstantClass.TESTTYPE_SINGLE_SELECT:
                            newFragment = new WorkExerciseSelsctFragment(this,projectWorkShowActivity);
                            tag= VarDefine.FRAGMENT_TAG_SINGLESELECT;
                            break;
                        //多选题
                        case HomeWorkConstantClass.TESTTYPE_MULTI_SELECT:
                            newFragment=new WorkExerciseMultSelectFragment(this,projectWorkShowActivity);
                            tag= VarDefine.FRAGMENT_TAG_MULTISELECT;
                            break;
                        //判断题
                        case HomeWorkConstantClass.TESTTYPE_YESORNO_SELECT:
                            newFragment=new WorkExerciseJudgeFragment(this,projectWorkShowActivity);
                            tag=VarDefine.FRAGMENT_TAG_JUDGE;
                            break;
                        //涂抹题
                        case HomeWorkConstantClass.TESTTYPE_PAINT:
                            newFragment=new WorkExerciseDaubFragment(this,projectWorkShowActivity);
                            tag=VarDefine.FRAGMENT_TAG_DAUB;
                            break;
                        //填空题
                        case HomeWorkConstantClass.TESTTYPE_FILL_BLANK:
                            newFragment=new WorkExerciseFillBlankFragment(this,projectWorkShowActivity);
                            tag=VarDefine.FRAGMENT_TAG_FLBANK;
                            break;
                        //连线题
                        case HomeWorkConstantClass.TESTTYPE_LINE:

                            newFragment=new WorkExerciseLineFragment(this,projectWorkShowActivity);
                            tag=VarDefine.FRAGMENT_TAG_LINE;
                            break;
                        //操作题
                        case HomeWorkConstantClass.TESTTYPE_OPRATE:
                            newFragment=new WorkExerciseOPRAFragment(this,projectWorkShowActivity);
                            tag= VarDefine.FRAGMENT_TAG_OPRATE;
                            break;
                        //嵌套题
                        case HomeWorkConstantClass.TESTTYPE_NEST:
                            newFragment=new WorkExerciseNESTFragment(this,projectWorkShowActivity);
                            tag=VarDefine.FRAGMENT_TAG_INCLUDE;
                            break;

                    }
                    PerferenceService service=new PerferenceService(projectWorkShowActivity);
                    Long userId=service.getsharedPre().getLong("UserId",0);
                    Bundle bundle=new Bundle();
                    bundle.putString("testId",String.valueOf(userId));
                    bundle.putString("Model","neibuExercise");//内部练习模式
                    bundle.putSerializable("testEntity", testEntity);
                    newFragment.setArguments(bundle);//第二种传值方式
                    FragmentTransaction transaction2 =projectWorkShowActivity.getFragmentManager().beginTransaction();
                    transaction2.replace(R.id.showproject_info_list_fragment,newFragment,tag);
                    transaction2.commitAllowingStateLoss();
                }
                break;
                //完成答题
            case Common.NEIBUANS_OK:

                FragmentTransaction transaction8= projectWorkShowActivity.getFragmentManager().beginTransaction();
                WorkANS_ok_Fragment newFragment8=new WorkANS_ok_Fragment(projectWorkShowActivity,this,"neibuExercise");
                newFragment8.setInfo(msg.obj.toString());
                transaction8.replace(R.id.showproject_info_list_fragment,newFragment8);
                transaction8.commitAllowingStateLoss();
                break;
            //未完成答题
            case Common.NEIBUANS_NO:
                FragmentTransaction transaction9= projectWorkShowActivity.getFragmentManager().beginTransaction();
                WorkANS_no_Fragment newFragment9=new WorkANS_no_Fragment(this,projectWorkShowActivity,"neibuExercise");
                newFragment9.setInfo(msg.obj.toString());
                transaction9.replace(R.id.showproject_info_list_fragment,newFragment9);
                transaction9.commitAllowingStateLoss();
                break;

            //客观题完成界面
            case  Common.NEIBUANS_ONLT:
                FragmentTransaction transactiononly= projectWorkShowActivity.getFragmentManager().beginTransaction();
                WorkOnlyAnswerFragment newFragmentonly=new WorkOnlyAnswerFragment(this,projectWorkShowActivity,"neibuExercise ");
                newFragmentonly.setInfo(msg.obj.toString());
                transactiononly.replace(R.id.showproject_info_list_fragment,newFragmentonly);
                transactiononly.commitAllowingStateLoss();
                break;
        }


    }
}