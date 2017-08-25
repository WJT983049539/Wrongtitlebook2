package xinshuyuan.com.wrongtitlebook.Persenter.Handler;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.ibpd.xsy.varDefine.GlobalVarDefine;
import com.ibpd.xsy.varDefine.WrongQuestionConstantClass;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;

import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Activity.ExerciseTestActivity;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ANS_no_Fragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ANS_ok_Fragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ExerciseCustomFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ExerciseDaubFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ExerciseFillBlankFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ExerciseJudgeFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ExerciseLineFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ExerciseMultSelectFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ExerciseNESTFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ExerciseOPRAFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ExerciseSelsctFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.OnlyAnswerFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.VERY_GOODFragment;

/**练习试题主ActivityHandler
 * Created by Administrator on 2017/6/21.
 */

public class ExerciseHandler extends Handler{
    private ExerciseTestActivity exerciseTestActivity;
    public ExerciseHandler(ExerciseTestActivity exerciseTestActivity) {
        this.exerciseTestActivity=exerciseTestActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        switch (msg.what){
            //根据题型展现试题
            case Common.EXERCISE_TEST_SHOW:

                TextView TYPE= (TextView) exerciseTestActivity.findViewById(R.id.exercise_test_type);
                Object testObj=msg.obj;
                    if(testObj!=null){
                        String tag="";
                        if(testObj instanceof TestEntity){
                            TestEntity testEntity= (TestEntity) testObj;
                            //该展现试题了，根据试题类型，转到不同个处理页面当中
                            Fragment newFragment=null;
                            switch (testEntity.getTestType()){
                                //单选题
                                case GlobalVarDefine.TESTTYPE_SINGLE_SELECT:
                                    TYPE.setText("单选题");
                                    newFragment=new ExerciseSelsctFragment(this,exerciseTestActivity);
                                    tag=Common.FRAGMENT_TAG_SINGLESELECT;
                                    break;
                                //多选题
                                case GlobalVarDefine.TESTTYPE_MULTI_SELECT:
                                    TYPE.setText("多选题");
                                    newFragment=new ExerciseMultSelectFragment(this,exerciseTestActivity);
                                    tag=Common.FRAGMENT_TAG_MULTISELECT;
                                    break;
                                //判断题
                                case GlobalVarDefine.TESTTYPE_YESORNO_SELECT:
                                    TYPE.setText("判断题");
                                    newFragment=new ExerciseJudgeFragment(this,exerciseTestActivity);
                                    tag=Common.FRAGMENT_TAG_YESORNO_SELECT;
                                    break;
                                //涂抹题
                                case GlobalVarDefine.TESTTYPE_PAINT:
                                    TYPE.setText("涂抹题");
                                    newFragment=new ExerciseDaubFragment(this,exerciseTestActivity);
                                    tag=Common.FRAGMENT_TAG_DAUB;
                                    break;
                                //填空题
                                case GlobalVarDefine.TESTTYPE_FILL_BLANK:
                                    TYPE.setText("填空题");
                                    newFragment=new ExerciseFillBlankFragment(this,exerciseTestActivity);
                                    tag=Common.FRAGMENT_TAG_FILLBLANK;
                                    break;
                                //连线题
                                case GlobalVarDefine.TESTTYPE_LINE:
                                    TYPE.setText("连线题");
                                    newFragment=new ExerciseLineFragment(this,exerciseTestActivity);
                                    tag=Common.FRAGMENT_TAG_LINE;
                                    break;
                                //操作题
                                case GlobalVarDefine.TESTTYPE_OPRATE:
                                    TYPE.setText("操作题");
                                    newFragment=new ExerciseOPRAFragment(this,exerciseTestActivity);
                                    tag=Common.FRAGMENT_TAG_OPRATE;
                                    break;
                                //嵌套题
                                case GlobalVarDefine.TESTTYPE_NEST:
                                    TYPE.setText("嵌套题");
                                    newFragment=new ExerciseNESTFragment(this,exerciseTestActivity);
                                    tag=Common.FRAGMENT_TAG_INCLUDE;
                                    break;
                                //自定义拍照题
                                case 0:
                                    TYPE.setText("自定义拍照题");
                                    newFragment=new ExerciseCustomFragment(this,exerciseTestActivity);
                                    tag=Common.FRAGMENT_TAG_CUSTOM;
                                    break;

                            }
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("testEntity", testEntity);
                            newFragment.setArguments(bundle);//第二种传值方式
                            FragmentTransaction transaction =exerciseTestActivity.getFragmentManager().beginTransaction();
                            transaction.replace(R.id.show_exercise_fragment,newFragment,tag);
                            transaction.commitAllowingStateLoss();
                        }
                    }


                break;

            //回答正确，转到ok界面
            case Common.ANS_OK:

                ANS_ok_Fragment ans_ok_fragment=new ANS_ok_Fragment();
                ans_ok_fragment.setInfo(msg.obj.toString(),this);
                FragmentTransaction transaction =exerciseTestActivity.getFragmentManager().beginTransaction();
                transaction.replace(R.id.show_exercise_fragment,ans_ok_fragment);
                transaction.commitAllowingStateLoss();
                break;
            case Common.ANS_NO:
                ANS_no_Fragment ans_no_fragment=new ANS_no_Fragment();
                ans_no_fragment.setInfo(msg.obj.toString(),this);
                FragmentTransaction transaction2 =exerciseTestActivity.getFragmentManager().beginTransaction();
                transaction2.replace(R.id.show_exercise_fragment,ans_no_fragment);
                transaction2.commitAllowingStateLoss();
                break;
            //只显示答案解析的
            case  Common.ONLY_ANSWER:
                OnlyAnswerFragment onlyAnswerFragment=new OnlyAnswerFragment();
                onlyAnswerFragment.setInfo(msg.obj.toString(),this);
                FragmentTransaction transaction3 =exerciseTestActivity.getFragmentManager().beginTransaction();
                transaction3.replace(R.id.show_exercise_fragment,onlyAnswerFragment);
                transaction3.commitAllowingStateLoss();


                break;

            //嵌套题小题
            case Common.VIEW_INCLUDTEST_SUBTEST:
                Fragment fragment_include=exerciseTestActivity.getFragmentManager().findFragmentByTag(Common.FRAGMENT_TAG_INCLUDE);
                if(fragment_include!=null && fragment_include instanceof ExerciseNESTFragment){
                    ExerciseNESTFragment f=(ExerciseNESTFragment) fragment_include;
                    if(msg.obj instanceof TestEntity){
                        TestEntity te=(TestEntity) msg.obj;
                        f.doInitAnsInfo(te);
                    }
                }




                break;

            case Common.VERY_GOOD:
                VERY_GOODFragment very_goodfragment=new VERY_GOODFragment();
                FragmentTransaction transaction4 =exerciseTestActivity.getFragmentManager().beginTransaction();
                very_goodfragment.setInfo(WrongQuestionConstantClass.MSG_FINISH_MSG);
                transaction4.replace(R.id.show_exercise_fragment,very_goodfragment);
                transaction4.commitAllowingStateLoss();
                break;
            }


    }
}
