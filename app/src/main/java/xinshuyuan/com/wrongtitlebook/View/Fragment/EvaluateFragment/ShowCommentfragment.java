package xinshuyuan.com.wrongtitlebook.View.Fragment.EvaluateFragment;

import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xinshuyuan.com.wrongtitlebook.Model.CommentListBean;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.SHOWCommentFragmentHandler;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Activity.ShenSuActivity;

/**
 * 各个评论展示
 * Created by Administrator on 2017/8/10.
 */

public class ShowCommentfragment extends BaseFragment{
    private Activity context;
    private SHOWCommentFragmentHandler lookAndStatisticsHandler;
    //得到的json数据
    private String Info;
    //类型
    int type;
    private List<CommentListBean> listbean=null;

    private ListView showcommentfragment_listview;
    private TextView show_fragment_type_textView;
    private Myadapter myadapter;

    public ShowCommentfragment(){};
    public ShowCommentfragment(Activity context, SHOWCommentFragmentHandler lookAndStatisticsHandler) {
        this.context=context;
        this.lookAndStatisticsHandler=lookAndStatisticsHandler;
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_layout_show_comment;
    }
    //得到数据
    public void setInfo(Object obj, int arg1) {
        Info=obj.toString();
        type=arg1;
    }

    @Override
    protected void initView() {
        super.initView();
        listbean= new ArrayList<CommentListBean>();
        showcommentfragment_listview=fVB(R.id.showcommentfragment_listview);
        show_fragment_type_textView=fVB(R.id.show_fragment_type_textView);
        if(type==1){
            show_fragment_type_textView.setText("老师评语列表");
        }else if(type==2){
            show_fragment_type_textView.setText("家长评语列表");
        }else if(type==3){
            show_fragment_type_textView.setText("同学评语列表");
        }else if(type==4){
            show_fragment_type_textView.setText("自评评语列表");
        }else if(type==0){
            show_fragment_type_textView.setText("综合评语列表");
        }

        try {
            JSONObject jsonObject=new JSONObject(Info);
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
                //是否申诉
                Boolean appeal=OO.getBoolean("appeal");
                //申诉状态
                int audit=OO.getInt("audit");
                CommentListBean commentListBean=new CommentListBean();
                commentListBean.setId(id);
                commentListBean.setCreatorId(creatorId);
                commentListBean.setCreatorName(creatorName);
                commentListBean.setCreatorType(creatorType);
                commentListBean.setStudentId(studentId);
                commentListBean.setStudentName(studentName);
                commentListBean.setContent(content);
                commentListBean.setAppeal(appeal);
                commentListBean.setAudit(audit);
                listbean.add(commentListBean);
            }
            Collections.reverse(listbean);
            //开始放数据到list中
            showListView();


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void showListView() {
        myadapter=new Myadapter();
        showcommentfragment_listview.setAdapter(myadapter);
    }

    @Override
    protected void setListener() {
        super.setListener();
    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();

    }
    //这个是评论列表的适配器
    private class Myadapter extends BaseAdapter {
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
            ViewHodler myviewHolder=null;
            if(convertView==null){
                myviewHolder=new ViewHodler();
                convertView=inflater.inflate(R.layout.item_pingluninfo,parent,false);
                myviewHolder.myweb= (WebView) convertView.findViewById(R.id.show_comment_item_id);
                myviewHolder.shensuText= (TextView) convertView.findViewById(R.id.item_shensu_id);
                myviewHolder.textview_audit=(TextView)convertView.findViewById(R.id.textview_audit);
                convertView.setTag(myviewHolder);
            }else {
                myviewHolder= (ViewHodler) convertView.getTag();
            }

            String content= listbean.get(position).getContent();

            inintWebvView(myviewHolder.myweb,content);


            int audit=listbean.get(position).getAudit();
            if(audit==-1){
                myviewHolder.textview_audit.setText("未审核");
            }if(audit==0){
                myviewHolder.textview_audit.setText("审核失败   ");
            }if(audit==1){
                myviewHolder.textview_audit.setText("审核成功");
            }
            //如果是true的话，正在申诉,把申诉按钮隐藏掉。false 的话未申诉,
            if(listbean.get(position).getAppeal()){

                if(myviewHolder.shensuText.getVisibility()==View.VISIBLE){
                    myviewHolder.shensuText.setVisibility(View.GONE);
                }

            }else{
                if(myviewHolder.textview_audit.getVisibility()==View.VISIBLE){
                    myviewHolder.textview_audit.setVisibility(View.GONE);
                }

//                myviewHolder.myweb.loadUrl(listbean.get(position).getContent());
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
            }

            return convertView;
        }
    }
    class ViewHodler {

        WebView myweb;
        TextView shensuText;
        TextView textview_audit;
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
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        webviewaa.setHorizontalScrollBarEnabled(false);//水平不显示
//        settings.setTextSize(WebSettings.TextSize.LARGER);
        webviewaa.setBackgroundColor(0);
        webviewaa.setWebChromeClient(new WebChromeClient());
        webviewaa.setWebViewClient(new webViewClient());
        webviewaa.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        Random r=new Random();
//        int nowint=r.nextInt(2);
//        webviewaa.setBackgroundColor(getResources().getColor(c[nowint]));
//        webviewaa.loadUrl(itemPoint);
        webviewaa.loadDataWithBaseURL(null, itemPoint, "text/html", "utf-8", null);
    }

    private class webViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();  // 接受所有网站的证书
            super.onReceivedSslError(view, handler, error);
        }
    }
}

