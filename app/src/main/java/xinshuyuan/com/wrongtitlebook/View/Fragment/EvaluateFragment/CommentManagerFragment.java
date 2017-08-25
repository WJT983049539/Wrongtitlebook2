package xinshuyuan.com.wrongtitlebook.View.Fragment.EvaluateFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import work.StudentCommentConstantClass;
import xinshuyuan.com.wrongtitlebook.Model.CommentInfo;
import xinshuyuan.com.wrongtitlebook.Model.Common.PerferenceService;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.CommentManaHandler;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Activity.Dialog_commentActivity;

/**
 * 评论管理页面
 * Created by wjt on 2017/7/31.
 */

public class CommentManagerFragment extends BaseFragment {
    private Activity context;
    private ListView commentmana_listview;
    private List<CommentInfo>commentInfoList=null;
    private ArrayList<String> studentNameList=null;
    private CommentAdapter commentAdapter;
    private Button AddDatabutton;
    private CommentManaHandler commentManaHandler;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private String SelectName;
    private Long UserId;
    private String UserName;
    //自动提示框
    private AutoCompleteTextView acTextView ;
    public CommentManagerFragment(){}
    private InputMethodManager m;
    private  ArrayAdapter<String> autoCompleteAdapter;


    public CommentManagerFragment(Activity context, CommentManaHandler commentManaHandler) {
        this.context=context;
        this.commentManaHandler=commentManaHandler;
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_comment_manager;
    }


    @Override
    protected void initData(Bundle arguments) {
        super.initData(arguments);
    }

    @Override
    protected void initView() {
        super.initView();
        PerferenceService service=new PerferenceService(getActivity());
        UserId=service.getsharedPre().getLong("EvaluateId",0);
        UserName=service.getsharedPre().getString("UserName","");
        adapter=new ArrayAdapter(context,android.R.layout.simple_list_item_1,list);
        commentInfoList=new ArrayList<CommentInfo>();
        studentNameList=new ArrayList<String>();
        getStudentName();
        commentmana_listview=fVB(R.id.commentmana_listview);
        AddDatabutton=fVB(R.id.AddDatabutton);
        acTextView=fVB(R.id.acTextView);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("选择");
        menu.add(0,1,0,"修改");
        menu.add(0,2,0,"删除");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        //这个是list所选择的potion
        final int position=info.position;
        //这个是item 所选择的
        int itemId=item.getItemId();
        int comment_id=commentInfoList.get(position).getComment_id();
        //修改
        if(itemId==1){
            CommentInfo commentInfo=commentInfoList.get(position);
            commentManaHandler.sendMessage(XSYTools.makeNewMessage(Common.UPDATECOMMENT,commentInfo));
            //删除
        }else if(itemId==2){
            HashMap<String,String> DeleteMap= XsyMap.getInterface();
            DeleteMap.put(StudentCommentConstantClass.PARAM_ID,String.valueOf(comment_id));
            OkGo.post(XSYTools.getEvaluateUrl(StudentCommentConstantClass.COMMENTDELETE_URL,context)).params(DeleteMap).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    XSYTools.showToastmsg(getActivity(),"删除成功！");
                    commentInfoList.clear();
                    XSYTools.i("删除请求成功返回的信息："+s);
                    try {
                        JSONObject object=new JSONObject(s);
                        JSONArray arry=object.getJSONArray("list");
                        for(int i=0;i<arry.length();i++){
                            JSONObject OO=arry.getJSONObject(i);
                            int id=OO.getInt("id");
                            String content=OO.getString("content");
                            //被评论人，也就是别人，
                            String studentName=OO.getString("studentName");
                            //学期
                            String semesterName=OO.getString("semesterName");
                            //一级栏目
                            String itemName=OO.getString("itemName");
                            //二级栏目
                            String secondItemName=OO.getString("secondItemName");
                            //评论人是自己或者别人
                            String creatorName=UserName;
                            //评论时间
                            String commentTime=OO.getString("commentTime");

                            CommentInfo commentinfo=new CommentInfo();
                            commentinfo.setComment_id(id);
                            commentinfo.setCommentContent(content);
                            commentinfo.setStudentName(studentName);
                            commentinfo.setSemesterName(semesterName);
                            commentinfo.setCommentary(itemName);
                            commentinfo.setSecondItemName(secondItemName);
                            commentinfo.setConmentorName(creatorName);
                            commentinfo.setCommentTime(commentTime);
                            //添加进数据库
                            commentInfoList.add(commentinfo);
                            //数据库不用啦
                        }
                        //相同的引用list的话，list如果改变了，使用adapter.notifyDataSetChanged().改变数据
                        commentAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    XSYTools.i("删除请求失败信息："+e.toString());

                }
            });
        }
        return super.onContextItemSelected(item);
    }


    @Override
    protected void setListener() {
        super.setListener();
        registerForContextMenu(commentmana_listview);


        //添加数据按钮
        AddDatabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentManaHandler.sendMessage(XSYTools.makeNewMessage(Common.ADDCOMMENT,""));
            }
        });

    }

    /**
     * 当用户可见view 的时候用
     */
    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();

        getData("");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,studentNameList);

        autoCompleteAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, studentNameList);
        acTextView.setAdapter(adapter);
//        acTextView.setOnItemClickListener(new OnDropdownItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //选择的名字
//                SelectName=studentNameList.get(position);
//                XSYTools.i(studentNameList.toString());
//                XSYTools.i("取得是"+SelectName+", 第"+position+"个");
//                getData(SelectName);
//                acTextView.setText("");
//                commentAdapter.notifyDataSetChanged();
//            }
//        });
////
//        acTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //选择的名字
//                SelectName=studentNameList.get(position);
//                XSYTools.i(studentNameList.toString());
//                XSYTools.i("取得是"+SelectName+", 第"+position+"个");
//                getData(SelectName);
//                acTextView.setText("");
//                commentAdapter.notifyDataSetChanged();
//            }
//        });
    }

    /**
     * 得到评论列表数据
     * 传入搜索条件
     */
    private void getData(String contion) {
        HashMap<String,String> getCommentDataMap= XsyMap.getInterface();
        getCommentDataMap.put(StudentCommentConstantClass.PARAM_CREATORID,String.valueOf(UserId));
        getCommentDataMap.put(StudentCommentConstantClass.PARAM_CREATORNAME,UserName);
        getCommentDataMap.put(StudentCommentConstantClass.PARAM_STUDENTNAME,contion);

        OkGo.post(XSYTools.getEvaluateUrl(StudentCommentConstantClass.COMMENTLIST_URL,context)).params(getCommentDataMap).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i("得到评论列表的数据"+s);
                commentInfoList.clear();
                try {
                    JSONObject object=new JSONObject(s);
                    JSONArray arry=object.getJSONArray("list");
                    for(int i=0;i<arry.length();i++){
                        JSONObject OO=arry.getJSONObject(i);
                        int id=OO.getInt("id");
                        String content=OO.getString("content");
                        //被评论人
                        String studentName=OO.getString("studentName");
                        //学期
                        String semesterName=OO.getString("semesterName");
                        //一级栏目
                        String itemName=OO.getString("itemName");
                        //二级栏目
                        String secondItemName=OO.getString("secondItemName");
                        //评论人, 本人
                        String creatorName=UserName;
                        //评论时间
                        String commentTime=OO.getString("commentTime");
                        //评论人id
                        Long creatorId= OO.getLong("creatorId");
                        //分数
                        Double score=OO.getDouble("score");

                        CommentInfo commentinfo=new CommentInfo();
                        commentinfo.setComment_id(id);
                        commentinfo.setCommentContent(content);
                        commentinfo.setStudentName(studentName);
                        commentinfo.setSemesterName(semesterName);
                        commentinfo.setCommentary(itemName);
                        commentinfo.setSecondItemName(secondItemName);
                        commentinfo.setConmentorName(creatorName);
                        commentinfo.setCommentTime(commentTime);
                        commentinfo.setScore(score);
                        //接入评论人集合里面
                        commentInfoList.add(commentinfo);

                        //添加进数据库,暂时不用了
//                        dbOpenHelper.insert(commentinfo);
                    }
//                    commentInfoList=dbOpenHelper.ChaCunALL(-1);
                    commentAdapter=new CommentAdapter();
                    commentmana_listview.setAdapter(commentAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);

                XSYTools.i("得到评论列表的数据出现错误："+e.toString());
            }
        });



    }

    private void getStudentName() {
        HashMap<String,String> getCommentDataMap= XsyMap.getInterface();
        getCommentDataMap.put(StudentCommentConstantClass.PARAM_CREATORID,String.valueOf(UserId));
        getCommentDataMap.put(StudentCommentConstantClass.PARAM_STUDENTNAME,"");

        OkGo.post(XSYTools.getEvaluateUrl(StudentCommentConstantClass.COMMENTLIST_URL,context)).params(getCommentDataMap).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i("得到评论列表的数据"+s);
                studentNameList.clear();
                try {
                    JSONObject object=new JSONObject(s);
                    JSONArray arry=object.getJSONArray("list");
                    for(int i=0;i<arry.length();i++){
                        JSONObject OO=arry.getJSONObject(i);
                        int id=OO.getInt("id");
                        String content=OO.getString("content");
                        //被评论人
                        String studentName=OO.getString("studentName");
                        //学期
                        String semesterName=OO.getString("semesterName");
                        //一级栏目
                        String itemName=OO.getString("itemName");
                        //二级栏目
                        String secondItemName=OO.getString("secondItemName");
                        //评论人, 本人
                        String creatorName=UserName;
                        //评论时间
                        String commentTime=OO.getString("commentTime");
                        //评论人id
                        Long creatorId= OO.getLong("creatorId");
                        //分数
                        Double score=OO.getDouble("score");

                        //接入评论人集合里面
                        studentNameList.add(studentName);
                    }
                    commentAdapter=new CommentAdapter();
                    commentmana_listview.setAdapter(commentAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);

                XSYTools.i("得到评论列表的数据出现错误："+e.toString());
            }
        });



    }












    private class CommentAdapter extends BaseAdapter {
//        List<CommentInfo> commentInfoList;
//        public CommentAdapter(List<CommentInfo> commentInfoListS) {
//            commentInfoList=commentInfoListS;
//        }

        @Override
        public int getCount() {
            return commentInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return commentInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutinflater=LayoutInflater.from(context);
            ViewHolder myholder;
            if(convertView==null){
                myholder=new ViewHolder();
                convertView=layoutinflater.inflate(R.layout.layout_comment_item,parent,false);
                myholder.item_student_id= (TextView) convertView.findViewById(R.id.item_student_id);
                myholder.item_student_name= (TextView) convertView.findViewById(R.id.item_student_name);
                myholder.item_student_commentor= (TextView) convertView.findViewById(R.id.item_student_commentor);
                myholder.item_student_commentLanmu= (TextView) convertView.findViewById(R.id.item_student_commentLanmu);
                myholder.item_student_content= (TextView) convertView.findViewById(R.id.item_student_content);

                convertView.setTag(myholder);
            }else {
                myholder= (ViewHolder) convertView.getTag();

            }
            myholder.item_student_id.setText(" "+commentInfoList.get(position).getComment_id());
            myholder.item_student_name.setText(commentInfoList.get(position).getConmentorName());
            myholder.item_student_commentor.setText(commentInfoList.get(position).getStudentName());
            myholder.item_student_commentLanmu.setText(commentInfoList.get(position).getCommentary());
            myholder.item_student_content.setText("点击查看详情");
            myholder.item_student_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String content=commentInfoList.get(position).getCommentContent();
                    Intent intent=new Intent(getActivity(),Dialog_commentActivity.class);
                    intent.putExtra("Content",content);
                    getActivity().startActivity(intent);
                }
            });

            myholder.item_student_content.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return false;
                }
            });
            return convertView;
        }
    }

    public  class ViewHolder {
        TextView item_student_id;
        TextView item_student_name;
        TextView item_student_commentor;
        TextView item_student_commentLanmu;
        TextView item_student_content;
    }

    @Override
    public void onStop() {
        super.onStop();
        //在当前页面停止时关闭数据库对象
    }
}