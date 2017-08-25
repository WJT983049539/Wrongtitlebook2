package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;
import work.StudentCommentConstantClass;
import xinshuyuan.com.wrongtitlebook.Model.Common.PerferenceService;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * 评论查看与统计
 * Created by wjt on 2017/8/10.
 */

public class CommentLookAndStatisticsActivity extends EvaluateBaseActivity {
    private GridView gridView;
    private String NowSelected;
    private Long UserId;
//    private LookAndStatisticsHandler lookAndStaticsHandler;
    private static final String aa[]={"学生成绩查询","自我评语查询","教师评语查询","同学评语查询","综合评语查询","各栏目评语查询","家长评语查询"};

    @Override
    protected int setLayout() {
        return R.layout.comment_look_stticits_layout;
    }

    @Override
    protected void initView() {
//        lookAndStaticsHandler=new LookAndStatisticsHandler(this);
        gridView=fvbi(R.id.classify_greidlist);
        gridView.setAdapter(new MyAdapter(this));
        PerferenceService service=new PerferenceService(this);
        //得到综合素质的学生id
        UserId=service.getsharedPre().getLong("EvaluateId",0);
    }

    @Override
    protected void initData() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NowSelected=aa[position];
                if(NowSelected.equals("学生成绩查询")){
                    getStudentGradeList();
                }else if(NowSelected.equals("自我评语查询")){
                    getCommentforType(4);
                }else if(NowSelected.equals("家长评语查询")){
                    getCommentforType(2);
                }else if(NowSelected.equals("教师评语查询")){
                    getCommentforType(1);
                }else if(NowSelected.equals("同学评语查询")){
                    getCommentforType(3);
                }else if(NowSelected.equals("综合评语查询")){
                    getCommentforType(0);
                }else if(NowSelected.equals("各栏目评语查询")){
                    getLanmuComment();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {

    }
    //得到栏目评论信息
    private void getLanmuComment() {
//        lookAndStaticsHandler.sendMessage(XSYTools.makeNewMessage(Common.SHOWLANMUCOMMENTINFO,""));
        Intent intent =new Intent(CommentLookAndStatisticsActivity.this,SHOWCommentFragmentActivity.class);
        intent.putExtra("jsondate","getLanmu");
        CommentLookAndStatisticsActivity.this.startActivity(intent);
    }

    private void getCommentforType(final int i) {

        HashMap<String,String> getCommentforTypeMap= XsyMap.getInterface();
        getCommentforTypeMap.put(StudentCommentConstantClass.PARAM_STUDENTID,String.valueOf(UserId));
        getCommentforTypeMap.put(StudentCommentConstantClass.PARAM_CREATORTYPE,String.valueOf(i));
        OkGo.post(XSYTools.getEvaluateUrl(StudentCommentConstantClass.LIST_URL,this)).params(getCommentforTypeMap).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i("得到评论信息"+s);
//                Message message=new Message();
//                message.what=Common.SHOW_COMMENT_FRAGMETN;
//                message.obj=s;
//                message.arg1=i;
//                lookAndStaticsHandler.sendMessage(message);

                Intent intent =new Intent(CommentLookAndStatisticsActivity.this,SHOWCommentFragmentActivity.class);
                intent.putExtra("jsondate",s);
                CommentLookAndStatisticsActivity.this.startActivity(intent);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                XSYTools.i("得到评论信息报错"+e.toString());
            }
        });


    }

    /**
     * 查询学生成绩
     */
    private void getStudentGradeList() {
        Intent intent =new Intent(CommentLookAndStatisticsActivity.this,SHOWCommentFragmentActivity.class);
        intent.putExtra("jsondate","getGrade");
        CommentLookAndStatisticsActivity.this.startActivity(intent);
//        lookAndStaticsHandler.sendMessage(XSYTools.makeNewMessage(Common.GET_GRADE,""));
    }

    private class MyAdapter extends BaseAdapter {

        CommentLookAndStatisticsActivity context;
        public MyAdapter(CommentLookAndStatisticsActivity commentLookAndStatisticsActivity) {
            context=commentLookAndStatisticsActivity;
        }

        @Override
        public int getCount() {
            return aa.length;
        }

        @Override
        public Object getItem(int position) {
            return aa[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView itemTextView;
            LayoutInflater inflater=LayoutInflater.from(CommentLookAndStatisticsActivity.this);
            convertView=inflater.inflate(R.layout.layout_gridview_item,parent,false);
            itemTextView= (TextView) convertView.findViewById(R.id.gridview_item_textview);
            itemTextView.setText(aa[position]);
            return convertView;
        }
    }
    //返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        startActivity(new Intent(this,EvaluateActivity.class));
        return true;
    }

}
