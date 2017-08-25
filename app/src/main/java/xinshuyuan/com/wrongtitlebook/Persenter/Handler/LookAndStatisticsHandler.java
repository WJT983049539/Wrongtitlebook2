//package xinshuyuan.com.wrongtitlebook.Persenter.Handler;
//
//import android.app.FragmentTransaction;
//import android.os.Handler;
//import android.os.Message;
//
//import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
//
//import xinshuyuan.com.wrongtitlebook.R;
//import xinshuyuan.com.wrongtitlebook.View.Activity.CommentLookAndStatisticsActivity;
//import xinshuyuan.com.wrongtitlebook.View.Fragment.EvaluateFragment.GetGradeFragment;
//import xinshuyuan.com.wrongtitlebook.View.Fragment.EvaluateFragment.ShowCommentfragment;
//import xinshuyuan.com.wrongtitlebook.View.Fragment.EvaluateFragment.ShowlanmucommentInfo;
//
///**
// * 查询统计handler
// * Created by wjt on 2017/8/10.
// * Created by Administrator on 2017/8/21.
// */
//
//public class LookAndStatisticsHandler extends Handler {
//    CommentLookAndStatisticsActivity context;
//    public LookAndStatisticsHandler(CommentLookAndStatisticsActivity commentLookAndStatisticsActivity) {
//        context=commentLookAndStatisticsActivity;
//    }
//
//    @Override
//    public void handleMessage(Message msg) {
//        super.handleMessage(msg);
//        FragmentTransaction fragmetnTransaction= context.getFragmentManager().beginTransaction();
//        switch (msg.what){
//            //转到得到成绩页面
//            case Common.GET_GRADE:
//                GetGradeFragment getGrqradefragment=new GetGradeFragment(context,this);
//                fragmetnTransaction.replace(R.id.whattt,getGrqradefragment,"getGrade");
//                fragmetnTransaction.addToBackStack(null);
//                fragmetnTransaction.commitAllowingStateLoss();
//                break;
//
//            //显示各种类型的评论页面
//            case Common.SHOW_COMMENT_FRAGMETN:
//                ShowCommentfragment showCommentfragment=new ShowCommentfragment(context,this);
//                showCommentfragment.setInfo(msg.obj,msg.arg1);
//                fragmetnTransaction.replace(R.id.whattt,showCommentfragment,"showCommentfragment");
//                fragmetnTransaction.addToBackStack(null);
//                fragmetnTransaction.commitAllowingStateLoss();
//
//
//                break;
//            //搜索显示各栏目信息
//            case Common.SHOWLANMUCOMMENTINFO:
//
//                ShowlanmucommentInfo showlanmucommentInfo=new ShowlanmucommentInfo(context,this);
//                fragmetnTransaction.replace(R.id.whattt,showlanmucommentInfo,"showlanmucommentInfo");
//                fragmetnTransaction.addToBackStack(null);
//                fragmetnTransaction.commitAllowingStateLoss();
//
//                break;
//
//
//
//        }
//
//    }
//}