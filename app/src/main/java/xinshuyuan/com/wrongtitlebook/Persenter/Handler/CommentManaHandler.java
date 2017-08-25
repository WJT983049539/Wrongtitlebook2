package xinshuyuan.com.wrongtitlebook.Persenter.Handler;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;

import xinshuyuan.com.wrongtitlebook.Model.CommentInfo;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Activity.CommentManageActivity;
import xinshuyuan.com.wrongtitlebook.View.Fragment.EvaluateFragment.AddCommentFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.EvaluateFragment.CommentManagerFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.EvaluateFragment.CommentManagerUpdateFragment;


/**
 * 评论管理界面的 Handler
 * Created by Administrator on 2017/7/31.
 */

public class CommentManaHandler extends Handler{
    private Activity context;
    private FragmentTransaction fragmentTransaction;
    public CommentManaHandler(CommentManageActivity commentManageActivity) {
        context=commentManageActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        fragmentTransaction =context.getFragmentManager().beginTransaction();
        switch (msg.what){
            //转到评论管理碎片页面
            case Common.COMMENTMANAGE:
                CommentManagerFragment commentManagerFragment=new CommentManagerFragment(context,this);
                fragmentTransaction.replace(R.id.commentmanage_showcontent,commentManagerFragment,"commentmanagerFragment");
                fragmentTransaction.addToBackStack(null);//将fragment加入返回栈
                fragmentTransaction.commitAllowingStateLoss();
                break;
            //评论修改界面
            case Common.UPDATECOMMENT:
                Object object=msg.obj;
                if(msg.obj!=null){

                if(object instanceof CommentInfo){
                    CommentInfo commentInfo= (CommentInfo) msg.obj;
                    CommentManagerUpdateFragment commentManagerUpdateFragment=new CommentManagerUpdateFragment(context,this);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("CommentInfo",commentInfo);
                    commentManagerUpdateFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.commentmanage_showcontent,commentManagerUpdateFragment,"commentManagerUpdateFragment");
                    fragmentTransaction.addToBackStack(null);//将fragment加入返回栈
                    fragmentTransaction.commitAllowingStateLoss();
                }
                }
                break;
            //评论添加页面
            case Common.ADDCOMMENT:
                AddCommentFragment addCommentFragment=new AddCommentFragment(context,this);
                fragmentTransaction.replace(R.id.commentmanage_showcontent,addCommentFragment,"addCommentFragment");
                fragmentTransaction.addToBackStack(null);//将fragment加入返回栈
                fragmentTransaction.commitAllowingStateLoss();
                break;

        }
    }
}
