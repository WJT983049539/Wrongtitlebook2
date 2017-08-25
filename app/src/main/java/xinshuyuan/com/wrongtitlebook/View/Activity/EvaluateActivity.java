package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Response;
import work.StudentCommentConstantClass;
import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.PerferenceService;
import xinshuyuan.com.wrongtitlebook.Model.Common.UserEntity;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * 综合素质评价首页
 * Created by wjt on 2017/8/21.
 */

public class EvaluateActivity extends EvaluateBaseActivity {
    int c[] = {R.color.pinglunlisto,R.color.pinglunlistp,R.color.pinglunlistq,R.color.pinglunlistr,R.color.pinglunlists,R.color.pinglunlistt,R.color.pinglunlistu};
    //头像
    private ImageView avatar;
    private TextView imageView_plgl;
    private TextView imageView_plckytj;

    private TextView NameTextView;
    private TextView textView_stucode;
    private TextView textView_login_count;
    private TextView textView_login_lasttime;
    private ListView CommentsNews;
    //作业动态的适配器
    private ListInfoShowAdapter myadapter;
    //共享参数
    private PerferenceService service;

    //储存作业动态的集合
    private List<String> infolist=null;
    private Timer timer=null;
    private Long studentId;
    private Document document;


    @Override
    protected int setLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        //主页头像
        avatar=fvbi(R.id.avatar);
        imageView_plgl=fvbi(R.id.textview_plgl);
        imageView_plckytj=fvbi(R.id.textview_plckytj);
        imageView_plckytj.setOnClickListener(this);
        imageView_plgl.setOnClickListener(this);
        NameTextView= (TextView) findViewById(R.id.NameTextView);
        textView_stucode= (TextView) findViewById(R.id.textView_stucode);
        textView_login_count= (TextView) findViewById(R.id.textView_login_count);
        textView_login_lasttime= (TextView) findViewById(R.id.textView_login_lasttime);
        service=new PerferenceService(this);
        studentId=service.getsharedPre().getLong("EvaluateId",0);
        CommentsNews=fvbi(R.id.dynamic);
        timer=new Timer();
        infolist=new ArrayList<String>();
    }

    @Override
    protected void initData() {
        //的到学生信息
        HashMap<String,String> getAcatar= XsyMap.getInterface();
        PerferenceService service=new PerferenceService(this);
        Long ssoid=service.getsharedPre().getLong("SSOID",0);
        getAcatar.put(StudentCommentConstantClass.PARAM_STUDENTID,String.valueOf(ssoid));
        OkGo.post(XSYTools.getEvaluateUrl(StudentCommentConstantClass.STUDENTINFO_URL,this)).params(getAcatar).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i("的到学生信息："+s);

                try {
                    JSONObject objects=new JSONObject(s);
                    String studentinfo= objects.getString("student");
                    JSONObject object=new JSONObject(studentinfo);
                    //学生姓名
                    String studentName=object.getString("studName");

                    NameTextView.setText(studentName);

                    //学号
                    String stuNum=object.getString("studNum");
                    textView_stucode.setText(stuNum);
                    //登录次数
                    int loginCount=object.getInt("longinCount");
                    textView_login_count.setText(String.valueOf(loginCount));

                    //最后一次登录时间
                    String lastLoginTime=object.getString("lastLoginTime");
                    JSONObject object2=new JSONObject(lastLoginTime);

                    String year=String.valueOf(object2.getInt("year"));
                    String month=String.valueOf(object2.getInt("month"));
                    String day=String.valueOf(object2.getInt("day"));
                    String hours=String.valueOf(object2.getInt("hours"));
                    String minutes=String.valueOf(object2.getInt("minutes"));
                    String seconds=String.valueOf(object2.getInt("seconds"));
                    Long teme=object2.getLong("time");
                    Date dat=new Date(teme);
                    SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String nowTime=sim.format(dat);
                    textView_login_lasttime.setText(nowTime);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                XSYTools.i("的到学生信息错误信息："+e.toString());
            }
        });
        //的到学生头像

        HashMap<String,String> getTouxiang= XsyMap.getInterface();
        getTouxiang.put(StudentCommentConstantClass.PARAM_STUDENTID,String.valueOf(studentId));
        OkGo.post(XSYTools.getEvaluateUrl(StudentCommentConstantClass.STUDIMG_URL,this)).params(getTouxiang).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i("的到学生头像信息："+s);

                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String url=jsonObject.getString("fieldName");
                    url=XSYTools.getEvaluateUrl(url,EvaluateActivity.this);
                    Glide.with(EvaluateActivity.this).load(url).bitmapTransform(new CropCircleTransformation(EvaluateActivity.this)).crossFade(1000).thumbnail(0.1f).into(avatar);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                XSYTools.i("的到学生头像错误信息："+e.toString());
            }
        });


        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //得到评论动态的数据
                HashMap<String,String> map=XsyMap.getInterface();
                map.put(StudentCommentConstantClass.PARAM_STUDENTID,String.valueOf(studentId));
                OkGo.post(XSYTools.getEvaluateUrl(StudentCommentConstantClass.NEWLIST_URL,EvaluateActivity.this)).params(map).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        XSYTools.i("得到评论动态的Json："+s);
                        infolist.clear();
                        try{
                            if(s.equals("")){
                                XSYTools.i("没有动态更新");
                                infolist.add("没有动态更新");

                            }else {

                                JSONObject oo = new JSONObject(s);
                                String rows = oo.getString("list");
                                if (rows.equals("")) {
                                    XSYTools.i("没有动态更新");
                                    infolist.add("没有动态更新");
                                } else {

                                    JSONArray jsonArray = new JSONArray(rows);

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject aaa = jsonArray.getJSONObject(i);
                                        String workName = aaa.getString("content");
                                        //发布作业的时间
                                        String publishTime = aaa.getString("commentTime");
                                        String creatorName=aaa.getString("creatorName");
                                        JSONObject publishTimeobject = new JSONObject(publishTime);
                                        String year = String.valueOf(publishTimeobject.getInt("year"));
                                        String month = String.valueOf(publishTimeobject.getInt("month"));
                                        String day = String.valueOf(publishTimeobject.getInt("day"));
                                        String hours = String.valueOf(publishTimeobject.getInt("hours"));
                                        String minutes = String.valueOf(publishTimeobject.getInt("minutes"));
                                        String seconds = String.valueOf(publishTimeobject.getInt("seconds"));
                                        Long time=publishTimeobject.getLong("time");
                                        Date dat=new Date(time);
                                        SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String nowTime=sim.format(dat);

                                        String infoList = creatorName+"在"+nowTime+ "发表评论 : "+workName;
                                        infolist.add(infoList);
                                    }
                                }
                                Collections.reverse(infolist);
                                myadapter = new ListInfoShowAdapter(infolist);
                                CommentsNews.setAdapter(myadapter);
                            }
                        }catch (Exception e)
                        {e.printStackTrace();}
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        XSYTools.i("得到评论动态的Json的错误数据："+e.toString());
                    }
                });
            }
        },200,10000);




    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            //评论查看与统计
            case R.id.textview_plckytj:
                openActicity(CommentLookAndStatisticsActivity.class);
                this.finish();
                break;
            //评论管理
            case R.id.textview_plgl:
                openActicity(CommentManageActivity.class);
                break;

        }

    }


    private class ListInfoShowAdapter extends BaseAdapter {
        private List<String>list;
        public ListInfoShowAdapter(List<String> infolist) {
            list=infolist;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=LayoutInflater.from(EvaluateActivity.this);
            ViewHolder holder1;
            if(convertView==null){
                convertView=inflater.inflate(R.layout.info_show_stuat_item,parent,false);
                holder1=new ViewHolder();
//                holder1.tt= (TextView) convertView.findViewById(R.id.infoshow_item_textView);
                holder1.home_item_web=(WebView) convertView.findViewById(R.id.webview_why);
                convertView.setTag(holder1);
            }else {
                holder1= (ViewHolder) convertView.getTag();
            }
            initotherWebView(holder1.home_item_web,list.get(position));

            return convertView;
        }
    }

    private class ViewHolder {
//        TextView tt;
        WebView home_item_web;
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }


    private void initotherWebView(WebView webviewaa, String title) {

        //解析网页
        document = Jsoup.parse(title);
        String itemPoint=document.html();
        // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
        webviewaa.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

//        webviewaa.setBackgroundColor(0); // 设置背景色
        WebSettings settings=webviewaa.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        webviewaa.setHorizontalScrollBarEnabled(false);//水平不显示
        settings.setTextSize(WebSettings.TextSize.LARGER);
        webviewaa.setBackgroundColor(0);
        Random r=new Random();
        int nowint=r.nextInt(6);
        webviewaa.setBackgroundColor(getResources().getColor(c[nowint]));
        webviewaa.loadDataWithBaseURL(null, itemPoint, "text/html", "utf-8", null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        UserEntity userEntity=Common.getUserEntity();
        Intent intent=new Intent(this,SelectSystemActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("userEntity",userEntity);
        intent.putExtras(bundle);
        startActivity(intent);
        return true;
    }
}
