package xinshuyuan.com.wrongtitlebook.Persenter.Handler;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Activity.PreviewTestQuestionActivity;
import xinshuyuan.com.wrongtitlebook.View.Activity.ShowWrongBookslistActivity;
import xinshuyuan.com.wrongtitlebook.View.Fragment.ShowWrongListFragment;

/**
 * Created by Administrator on 2017/6/9.
 */

public class ShowWrongHandler extends Handler{
    private ShowWrongBookslistActivity showWrongBookslistActivity;
    public ShowWrongHandler(ShowWrongBookslistActivity showWrongBookslistActivity) {
        this.showWrongBookslistActivity=showWrongBookslistActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            //得到数据然后直接在，ShowWrongBookslistActivity的Content上展现出来
            case Common.SHOWWRONG_LIST:
                FragmentTransaction transaction= showWrongBookslistActivity.getFragmentManager().beginTransaction();
                ShowWrongListFragment showWrongfragment=new ShowWrongListFragment();
                showWrongfragment.setInfo(msg.obj.toString(),this);
                transaction.replace(R.id.show_wrong_content,showWrongfragment);
                transaction.commitAllowingStateLoss();
                break;
            //根据数据展现选中的单个错题
            case Common.PRIVIEW_SHOW_TEXT:
                Intent intent=new Intent(showWrongBookslistActivity,PreviewTestQuestionActivity.class);
                intent.putExtra("testQuestionInfo",msg.obj.toString());
                showWrongBookslistActivity.startActivity(intent);
                break;
        }
    }
}
