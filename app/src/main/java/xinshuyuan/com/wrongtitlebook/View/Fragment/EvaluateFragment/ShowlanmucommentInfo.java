package xinshuyuan.com.wrongtitlebook.View.Fragment.EvaluateFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import work.StudentCommentConstantClass;
import xinshuyuan.com.wrongtitlebook.Model.ColumnInfo;
import xinshuyuan.com.wrongtitlebook.Model.CommentListBean;
import xinshuyuan.com.wrongtitlebook.Model.Common.PerferenceService;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.SHOWCommentFragmentHandler;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Activity.ShenSuActivity;

/**
 * 栏目评语列表
 * Created by wjt on 2017/8/10.
 */

public class ShowlanmucommentInfo extends BaseFragment{
    private Activity context;
    private SHOWCommentFragmentHandler lookAndStatisticsHandler;
    private PopupWindow popupWindow=null;
    private View PopView;
    //pop弹出菜单一级栏目listView
    private ListView lanmu_one;
    //pop弹出菜单二级栏目listView
    private ListView lanmu_two;

    //储存栏目一级的list
    private List<ColumnInfo> oneLanmulist=null;
    //一级栏目适配器
    private MyOneAdapter myOneAdapter=null;

    //储存栏目二级的list
    private List<ColumnInfo> twoLanmulist=null;
    //=二级栏目适配器
    private MyOneAdapter mytwoAdapter=null;
    private Long SelectedOneLanmuid;
    private String SelectedOnelanmuName;
    private Long SelectedTwoLanmuid;
    private String SelectedTwolanmuName;
    private Long UserId;

    private List<CommentListBean> listbean=null;
    private Myadapter myadapter;


    private TextView fragment_lanmu_show_textview;
    private ListView fragment_lanmu_show_listview;
    public ShowlanmucommentInfo(){};
    public ShowlanmucommentInfo(Activity context, SHOWCommentFragmentHandler lookAndStatisticsHandler) {
        this.context=context;
        this.lookAndStatisticsHandler=lookAndStatisticsHandler;
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_layout_lanmucomment;
    }

    @Override
    protected void initView() {
        super.initView();
        PerferenceService servie=new PerferenceService(getActivity());
        UserId=servie.getsharedPre().getLong("EvaluateId",0);
        //栏目显示框
        fragment_lanmu_show_textview=fVB(R.id.fragment_lanmu_show_textview);
        fragment_lanmu_show_listview=fVB(R.id.fragment_lanmu_show_listview);
        listbean=new ArrayList<CommentListBean>();
        oneLanmulist=new ArrayList<ColumnInfo>();
        twoLanmulist=new ArrayList<ColumnInfo>();



    }

    @Override
    protected void setListener() {
        super.setListener();
        fragment_lanmu_show_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //初始化悬浮窗
                inintpup();
            }
        });
    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();
    }



    private void inintpup() {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PopView = layoutInflater.inflate(R.layout.popwindows_item_layout, null);
        lanmu_one= (ListView) PopView.findViewById(R.id.lanmu_one);
        lanmu_two= (ListView) PopView.findViewById(R.id.lanmu_two);
        //加载数据，获取一级栏目list
        HashMap<String,String> getlanmuMap= XsyMap.getInterface();
        getlanmuMap.put(StudentCommentConstantClass.PARAM_ITEMID,"0");
        OkGo.post(XSYTools.getEvaluateUrl(StudentCommentConstantClass.GETITEMINFO_URL,context)).params(getlanmuMap).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i("得到一级栏目"+s);
                oneLanmulist.clear();
                twoLanmulist.clear();
                try {

                    JSONArray array=new JSONArray(s);
                    for(int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);
                        Long id=object.getLong("id");
                        String lanmuName=object.getString("itemName");
                        ColumnInfo columnInfo=new ColumnInfo();
                        columnInfo.setLanmuId(id);
                        columnInfo.setLanmuName(lanmuName);
                        oneLanmulist.add(columnInfo);
                    }
                    myOneAdapter=new MyOneAdapter(oneLanmulist);
                    lanmu_one.setAdapter(myOneAdapter);
                    lanmu_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            twoLanmulist.clear();
                            SelectedOneLanmuid = oneLanmulist.get(position).getLanmuId();
                            SelectedOnelanmuName=oneLanmulist.get(position).getLanmuName();
                            XSYTools.i("已经选择的栏目id="+ SelectedOneLanmuid);
                            if(SelectedOneLanmuid !=null){
                                HashMap<String,String> getTwolanmu= XsyMap.getInterface();
                                getTwolanmu.put(StudentCommentConstantClass.PARAM_ITEMID,String.valueOf(SelectedOneLanmuid));
                                OkGo.post(XSYTools.getEvaluateUrl(StudentCommentConstantClass.GETITEMINFO_URL,context)).params(getTwolanmu).execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        XSYTools.i("二级栏目返回json="+s);
                                        try {
                                            JSONArray array=new JSONArray(s);
                                            if(array.length()==0){
                                                fragment_lanmu_show_textview.setText(SelectedOnelanmuName);
                                                popupWindow.dismiss();
                                                showOneData(SelectedOneLanmuid);





                                            }else{
                                                for(int i=0;i<array.length();i++){
                                                    JSONObject object=array.getJSONObject(i);
                                                    Long id=object.getLong("id");
                                                    String lanmuName=object.getString("itemName");
                                                    ColumnInfo columnInfo=new ColumnInfo();
                                                    columnInfo.setLanmuId(id);
                                                    columnInfo.setLanmuName(lanmuName);
                                                    twoLanmulist.add(columnInfo);
                                                }
                                                mytwoAdapter=new MyOneAdapter(twoLanmulist);
                                                lanmu_two.setAdapter(mytwoAdapter);
                                                lanmu_two.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        SelectedTwoLanmuid=twoLanmulist.get(position).getLanmuId();
                                                        SelectedTwolanmuName=twoLanmulist.get(position).getLanmuName();
                                                        fragment_lanmu_show_textview.setText(SelectedTwolanmuName);
//                                                        hipepopView();
                                                        popupWindow.dismiss();

                                                        showTwoData(SelectedOneLanmuid,SelectedTwoLanmuid);
                                                    }
                                                });
                                            }
                                        }catch (Exception e){e.printStackTrace();}

                                    }

                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        super.onError(call, response, e);
                                        XSYTools.i("二级栏目返回错误="+e.toString());
                                        popupWindow.dismiss();
//                                            hipepopView();
                                    }
                                });

                            }

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                XSYTools.i("得到一级栏目错误信息"+e.toString());
                popupWindow.dismiss();
//                    hipepopView();
            }
        });
        popupWindow=new PopupWindow(PopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        PopView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });

        popupWindow.setOutsideTouchable(true);
//            popupWindow.showAsDropDown(PopView);
        // popupWindow.showAtLocation(, Gravity.BOTTOM, 0, 0);
        popupWindow.setAnimationStyle(R.style.contextMenuAnim);
        popupWindow.showAsDropDown(fragment_lanmu_show_textview);
    }

    /**
     * 根据一级，二级获取信息
     * @param selectedOneLanmuid
     * @param selectedTwoLanmuid
     */
    private void showTwoData(Long selectedOneLanmuid, Long selectedTwoLanmuid) {
        HashMap gettwoInfoMap= XsyMap.getInterface();
        gettwoInfoMap.put(StudentCommentConstantClass.PARAM_STUDENTID,String.valueOf(UserId));
        gettwoInfoMap.put(StudentCommentConstantClass.PARAM_ITEMID,String.valueOf(selectedOneLanmuid));
        gettwoInfoMap.put(StudentCommentConstantClass.PARAM_SECONDITEMID,String.valueOf(selectedTwoLanmuid));
        OkGo.post(XSYTools.getEvaluateUrl(StudentCommentConstantClass.ITEMLIST_URL,context)).params(gettwoInfoMap).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i("得到栏目评论信息infojson"+s);
                listbean.clear();
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    JSONArray array=jsonObject.getJSONArray("list");
                    for(int i=0;i<array.length();i++){
                        JSONObject OO=array.getJSONObject(i);
                        //评论id
                        Long id=OO.getLong("id");
                        //教师，家长，同学等别人对她的评价，评论人
                        Long creatorId=OO.getLong("creatorId");
                        //评论人
                        String creatorName=OO.getString("creatorName");
                        //评论类型
                        int creatorType=OO.getInt("creatorType");
                        //被评论人id，本人
                        Long studentId=OO.getLong("studentId");
                        //被评论人
                        String studentName=OO.getString("studentName");
                        //内容
                        String content=OO.getString("content");

                        CommentListBean commentListBean=new CommentListBean();
                        commentListBean.setId(id);
                        commentListBean.setCreatorId(creatorId);
                        commentListBean.setCreatorName(creatorName);
                        commentListBean.setCreatorType(creatorType);
                        commentListBean.setStudentId(studentId);
                        commentListBean.setStudentName(studentName);
                        commentListBean.setContent(content);
                        listbean.add(commentListBean);
                    }
                    Collections.reverse(listbean);
                    //开始放数据到list中
                    showListView();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                XSYTools.i("得到栏目评论错误信息"+e.toString());






            }
        });
    }

    /**
     * 根据一级栏目获取信息
     * @param selectedOneLanmuid
     */
    private void showOneData(Long selectedOneLanmuid) {
        HashMap getoneInfoMap= XsyMap.getInterface();
        getoneInfoMap.put(StudentCommentConstantClass.PARAM_STUDENTID,String.valueOf(UserId));
        getoneInfoMap.put(StudentCommentConstantClass.PARAM_ITEMID,String.valueOf(selectedOneLanmuid));
        getoneInfoMap.put(StudentCommentConstantClass.PARAM_SECONDITEMID,"0");
        OkGo.post(XSYTools.getEvaluateUrl(StudentCommentConstantClass.ITEMLIST_URL,context)).params(getoneInfoMap).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i("得到栏目评论信息infojson"+s);
                listbean.clear();

                try {
                    JSONArray array=new JSONArray(s);

                    for(int i=0;i<array.length();i++){
                        JSONObject OO=array.getJSONObject(i);
                        //评论id
                        Long id=OO.getLong("id");
                        //教师，家长，同学等别人对她的评价，评论人
                        Long creatorId=OO.getLong("creatorId");
                        //评论人
                        String creatorName=OO.getString("creatorName");
                        //评论类型
                        int creatorType=OO.getInt("creatorType");
                        //被评论人id，本人
                        Long studentId=OO.getLong("studentId");
                        //被评论人
                        String studentName=OO.getString("studentName");
                        //内容
                        String content=OO.getString("content");

                        CommentListBean commentListBean=new CommentListBean();
                        commentListBean.setId(id);
                        commentListBean.setCreatorId(creatorId);
                        commentListBean.setCreatorName(creatorName);
                        commentListBean.setCreatorType(creatorType);
                        commentListBean.setStudentId(studentId);
                        commentListBean.setStudentName(studentName);
                        commentListBean.setContent(content);
                        listbean.add(commentListBean);
                    }
                    //开始放数据到list中
                    showListView();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                XSYTools.i("得到栏目评论错误信息"+e.toString());

            }
        });

    }
    //填充数据
    private void showListView() {
        myadapter=new Myadapter();
        fragment_lanmu_show_listview.setAdapter(myadapter);
    }

    //一级栏目适配器
    private class MyOneAdapter extends BaseAdapter {
        List<ColumnInfo> oneLanmulist;
        public MyOneAdapter(List<ColumnInfo> oneLanmulists) {
            oneLanmulist=oneLanmulists;
        }

        @Override
        public int getCount() {
            return oneLanmulist.size();
        }

        @Override
        public Object getItem(int position) {
            return oneLanmulist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater flater=LayoutInflater.from(getActivity());
            MyViewHolder holder;
            TextView lanmuone_textview;
            if(convertView==null){
                convertView=flater.inflate(R.layout.layout_lanmu_one_item,parent,false);
                lanmuone_textview= (TextView) convertView.findViewById(R.id.textView_lanmu_item);
                holder=new MyViewHolder();
                holder.oneTextView=lanmuone_textview;
                convertView.setTag(holder);
            }else{
                holder= (MyViewHolder) convertView.getTag();
            }

            holder.oneTextView.setText(oneLanmulist.get(position).getLanmuName());
            return convertView;
        }
    }


    private class MyViewHolder {

        TextView oneTextView;

    }

    //这个是评论列表的适配器
    private class Myadapter extends BaseAdapter{
        @Override
        public int getCount() {
            return listbean.size();
        }

        @Override
        public Object getItem(int position) {
            return listbean.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=LayoutInflater.from(context);
            ViewHodler myviewHolder;
            if(convertView==null){
                myviewHolder=new ViewHodler();
                convertView=inflater.inflate(R.layout.item_pingluninfo,parent,false);
                myviewHolder.myweb= (WebView) convertView.findViewById(R.id.show_comment_item_id);
                myviewHolder.shensuText= (TextView) convertView.findViewById(R.id.item_shensu_id);
                convertView.setTag(myviewHolder);
            }else {
                myviewHolder= (ViewHodler) convertView.getTag();
            }
            inintWebvView(myviewHolder.myweb,listbean.get(position).getContent());

            myviewHolder.shensuText.setOnClickListener(new View.OnClickListener() {
                //申诉点击事件
                @Override
                public void onClick(View v) {
                    CommentListBean comm=listbean.get(position);
                    Intent intent=new Intent(context,ShenSuActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("commentListBean",comm);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
            return convertView;
        }
    }

    private void inintWebvView(WebView webviewaa,String Title) {
        //解析网页
        Document document = Jsoup.parse(Title);
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
//        Random r=new Random();
//        int nowint=r.nextInt(2);
//        webviewaa.setBackgroundColor(getResources().getColor(c[nowint]));
        webviewaa.loadDataWithBaseURL(null, itemPoint, "text/html", "utf-8", null);
    }

    public  class ViewHodler {

        WebView myweb;
        TextView shensuText;
    }

    private class webViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

