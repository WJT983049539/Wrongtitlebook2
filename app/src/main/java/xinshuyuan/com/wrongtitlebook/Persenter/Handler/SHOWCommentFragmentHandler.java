package xinshuyuan.com.wrongtitlebook.Persenter.Handler;

import android.app.FragmentTransaction;
import android.os.Handler;
import android.os.Message;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;

import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Activity.SHOWCommentFragmentActivity;
import xinshuyuan.com.wrongtitlebook.View.Fragment.EvaluateFragment.GetGradeFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.EvaluateFragment.ShowCommentfragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.EvaluateFragment.ShowlanmucommentInfo;

/**
 * 专门显示各种评论de activity 的handler
 * Created by wjt on 2017/8/24.
 */

public class SHOWCommentFragmentHandler extends Handler {
    private SHOWCommentFragmentActivity showCommentFragmentActivity;

    public SHOWCommentFragmentHandler(SHOWCommentFragmentActivity showCommentFragmentActivityS) {
        showCommentFragmentActivity = showCommentFragmentActivityS;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        FragmentTransaction fragmetnTransaction = showCommentFragmentActivity.getFragmentManager().beginTransaction();
        switch (msg.what) {
            //转到得到成绩页面
            case Common.GET_GRADE:
                GetGradeFragment getGrqradefragment = new GetGradeFragment(showCommentFragmentActivity, this);
                fragmetnTransaction.replace(R.id.showfragment_lin, getGrqradefragment, "getGrade");
                fragmetnTransaction.addToBackStack(null);
                fragmetnTransaction.commitAllowingStateLoss();
                break;

            //显示各种类型的评论页面
            case Common.SHOW_COMMENT_FRAGMETN:
                ShowCommentfragment showCommentfragment = new ShowCommentfragment(showCommentFragmentActivity, this);
                showCommentfragment.setInfo(msg.obj, msg.arg1);
                fragmetnTransaction.replace(R.id.showfragment_lin, showCommentfragment, "showCommentfragment");
                fragmetnTransaction.addToBackStack(null);
                fragmetnTransaction.commitAllowingStateLoss();


                break;
            //搜索显示各栏目信息
            case Common.SHOWLANMUCOMMENTINFO:

                ShowlanmucommentInfo showlanmucommentInfo = new ShowlanmucommentInfo(showCommentFragmentActivity, this);
                fragmetnTransaction.replace(R.id.showfragment_lin, showlanmucommentInfo, "showlanmucommentInfo");
                fragmetnTransaction.addToBackStack(null);
                fragmetnTransaction.commitAllowingStateLoss();

                break;

        }
    }
}
