package xinshuyuan.com.wrongtitlebook.View.Fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ibpd.xsy.varDefine.WrongQuestionConstantClass;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.AsyncImageLoader;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.Model.WrongListMenu;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.ShowWrongHandler;
import xinshuyuan.com.wrongtitlebook.R;

/**展现试题列表的fragment
 * Created by Administrator on 2017/6/7.
 */

public class ShowWrongListFragment extends Fragment {
    private LinearLayout scrollView_showrong;
    private LinearLayout sisi_showrong;
    private View mView;
    private Document document;
    private Elements imgs;
    private Elements body;
    private ShowWrongHandler showWrongHandler;
    //题型
    private Integer TestType;
    //展示题型
    private String StringTestType;
    //把要显示的错题列表储存起来
    List<WrongListMenu> wrongList = new ArrayList<WrongListMenu>();
    //题干
    private String problem;
    private ListView wrong_list;
    //需要显示的数据
    private String Value="";

    //放入错题列表的适配器
    ShowWrongAdapter showWrongAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!((Value.equals("")||Value==null))) {
            wrongList.clear();

            try {
                JSONArray jsonArray = new JSONArray(Value);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    WrongListMenu wlm = new WrongListMenu();

                    Integer type = object.getInt("type");
                    String title = object.getString("problem");
                    Long testId = object.getLong("id");

                    wlm.setTestType(type);
                    wlm.setTestId(testId);
                    wlm.setTitle(title);
                    //添加到集合
                    wrongList.add(wlm);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        //刷新listview
        showWrongAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.layout_wronglist, container, false);
        //错题显示的listView
        showWrongAdapter = new ShowWrongAdapter();
        wrong_list = (ListView) mView.findViewById(R.id.wrong_list);
        wrong_list.setAdapter(showWrongAdapter);
        wrong_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Long TestId = wrongList.get(position).getTestId();
                //获取单个详细信息的url
                String getWrongQuestionInfoUrl = XSYTools.getWrongUrl(WrongQuestionConstantClass.PREVIEW_URL,getActivity());
                XsyMap testoInfoMap = XsyMap.getInterface();
                testoInfoMap.put(WrongQuestionConstantClass.PARAM_ID, String.valueOf(TestId));


                OkGo.post(getWrongQuestionInfoUrl).params(testoInfoMap).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        XSYTools.i(s);
                        showWrongHandler.sendMessage(XSYTools.makeNewMessage(Common.PRIVIEW_SHOW_TEXT, s));
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        XSYTools.i("获取单个试题失败"+e.toString());
                    }
                });


            }
        });
        return mView;
    }

    /**
     * 得到数据
     *
     * @param obj
     * @param showWrongHandler
     */
    public void setInfo(String obj, ShowWrongHandler showWrongHandler) {
        this.Value = obj;
        this.showWrongHandler = showWrongHandler;
    }

    //适配器
    class ShowWrongAdapter extends BaseAdapter {
        private AsyncImageLoader asyncImageLoader;
        //为三种布局定义一个标识
        private final int TYPE1 = 0;
        private final int TYPE2 = 1;
        private final int TYPE3 = 2;
        private LayoutInflater inflater;

        public ShowWrongAdapter() {
            inflater = LayoutInflater.from(getActivity());

        }

        @Override
        public int getCount() {
            return wrongList.size();
        }

        @Override
        public Object getItem(int position) {
            return wrongList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //这个方法重写看有几种类型，它返回了有几种不同的布局
        @Override
        public int getViewTypeCount() {
            return 3;
        }

        // 每个convertView都会调用此方法，获得当前应该加载的布局样式
        @Override
        public int getItemViewType(int position) {
            WrongListMenu wrongMenu = wrongList.get(position);
            Integer Type = wrongMenu.getTestType();

            //获取当前布局的数据
            //哪个字段不为空就说明这是哪个布局
            //比如第一个布局只有item1_str这个字段，那么就判断这个字段是不是为空，
            //如果不为空就表明这是第一个布局的数据
            //根据字段是不是为空，判断当前应该加载的布局
            //填空题
            if (Type == 5) {
                return TYPE1;
                //自定义题
            } else if (Type == 0) {
                return TYPE2;
            } else {
                return TYPE3;
            }
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //初始化每个holder
            ViewHolder1 holder1 = null;
            ViewHolder2 holder2 = null;
            ViewHolder3 holder3 = null;
            final String Title=wrongList.get(position).getTitle();
            Integer testType=wrongList.get(position).getTestType();


            LayoutInflater inflater = LayoutInflater.from(getActivity());

            int type = getItemViewType(position);
            if (convertView == null) {

                switch (type) {
                    //填空题
                    case TYPE1:
                        convertView = inflater.inflate(R.layout.show_wrong_layout_t, null, false);
                        holder1 = new ViewHolder1();
                        holder1.item1_tv = (TextView) convertView.findViewById(R.id.SW_tile_type);
                        holder1.webview=(WebView)convertView.findViewById(R.id.SW_tile_testTile_web);
                        holder1.lin1=(LinearLayout) convertView.findViewById(R.id.liuliu_showrong);
                        holder1.lin2=(LinearLayout) convertView.findViewById(R.id.sisi_showrong);
                        convertView.setTag(holder1);
                        break;
                    //自定义上传的题
                    case TYPE2:

                        convertView = inflater.inflate(R.layout.custom_takephone, null, false);
                        holder2 = new ViewHolder2();
                        holder2.type= (TextView) convertView.findViewById(R.id.type);
                        holder2.image= (ImageView) convertView.findViewById(R.id.imageView2);
                        convertView.setTag(holder2);
                        break;
                    //其他的题
                    case TYPE3:

                        convertView = inflater.inflate(R.layout.show_wrong_layout, null);
                        holder3 = new ViewHolder3();
                        holder3.SW_tile_type = (TextView) convertView.findViewById(R.id.SW_tile_type);
                        holder3.SW_tile_testTile = (TextView) convertView.findViewById(R.id.SW_tile_testTile);
                        convertView.setTag(holder3);

                        break;


                }



            } else {
                switch (type) {
                    case TYPE1:
                        holder1 = (ViewHolder1) convertView.getTag();
                        break;
                    case TYPE2:
                        holder2 = (ViewHolder2) convertView.getTag();
                        break;
                    case TYPE3:
                        holder3 = (ViewHolder3) convertView.getTag();
                        break;
                }
            }

            //为布局设置数据
            switch (type) {


                case TYPE1:
                    if (Title == null) {
                        return convertView;
                    } else {
                        holder1.item1_tv.setText("  填空题");
                        holder1.item1_tv.setBackgroundResource(R.drawable.flanksinbg);
                        initWebView(holder1.webview, Title);
                        final ViewHolder1 finalHolder1 = holder1;
                        holder1.lin1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                v.setBackgroundColor(Color.parseColor("#6699cc"));

                                finalHolder1.lin2.setBackgroundColor(Color.parseColor("#6699cc"));

                                Long TestId = wrongList.get(position).getTestId();
                                //获取单个详细信息的url
                                String getWrongQuestionInfoUrl = XSYTools.joinUrl(WrongQuestionConstantClass.PREVIEW_URL);

                                XsyMap testoInfoMap = XsyMap.getInterface();
                                testoInfoMap.put(WrongQuestionConstantClass.PARAM_ID, String.valueOf(TestId));


                                OkGo.post(getWrongQuestionInfoUrl).params(testoInfoMap).execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        XSYTools.i(s);
                                        showWrongHandler.sendMessage(XSYTools.makeNewMessage(Common.PRIVIEW_SHOW_TEXT, s));
                                    }
                                });
                            }
                        });
                    }
                    break;
                case TYPE2:
                    if (Title == null) {
                        return convertView;
                    } else {
                        holder2.type.setText("  拍照上传");
                        holder2.type.setBackgroundResource(R.drawable.upphonesinbg);
//                    holder2.image.setImageDrawable();
//                    final ViewHolder2 finalHolder = holder2;
//                    new Thread(new Runnable(){
//
//                        @Override
//                        public void run(){
//                            final Drawable drawable = loadImageFromNetwork(Title);
//                            if(drawable!=null){
//                             getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    finalHolder.image.setImageDrawable(drawable);
//                                }
//                            });
//
//                        }
//                        }
//
//                    }).start();  //线程启动
                        holder2.image.setTag(Title);//为ImageView设置tag
                        new AsyncImageLoader().loadImgByAsyncTask(holder2.image, Title);
                        break;
                    }

                case TYPE3:
                    if (Title == null) {
                        return convertView;
                    } else {
                        holder3.SW_tile_testTile.setText(Title);

                        if (testType == 1) {
                            holder3.SW_tile_type.setText("  选择题");
                            holder3.SW_tile_type.setBackgroundResource(R.drawable.wokaosingsign);
                        } else if (testType == 2) {
                            holder3.SW_tile_type.setText("  多选题");
                            holder3.SW_tile_type.setBackgroundResource(R.drawable.multsingsinbg);
                        } else if (testType == 3) {
                            holder3.SW_tile_type.setText("  判断题");
                            holder3.SW_tile_type.setBackgroundResource(R.drawable.wokaosingsign);
                        } else if (testType == 4) {
                            holder3.SW_tile_type.setText("  涂抹题");
                            holder3.SW_tile_type.setBackgroundResource(R.drawable.oprasinbg);
                        } else if (testType == 6) {
                            holder3.SW_tile_type.setText("  连线题");
                            holder3.SW_tile_type.setBackgroundResource(R.drawable.linsinbg);
                        } else if (testType == 7) {
                            holder3.SW_tile_type.setText("  操作题");
                            holder3.SW_tile_type.setBackgroundResource(R.drawable.oprasinbg);
                        } else if (testType == 8) {
                            holder3.SW_tile_type.setBackgroundResource(R.drawable.flanksinbg);
                            holder3.SW_tile_type.setText("  嵌套题");
                        } else if (testType == 9) {
                            holder3.SW_tile_type.setBackgroundResource(R.drawable.flanksinbg);
                            holder3.SW_tile_type.setText("  自定义单选题");
                        } else if (testType == 10) {
                            holder3.SW_tile_type.setBackgroundResource(R.drawable.wokaosingsign);
                            holder3.SW_tile_type.setText("  自定义多选题");
                        }
                        break;
                    }
            }
            return convertView;

        }
    }
        private void initWebView2(WebView titleWebvie, String title) {
            {
                titleWebvie.getSettings().setBlockNetworkImage(false);//解决图片不显示
                titleWebvie.getSettings().setJavaScriptEnabled(true);
                titleWebvie.getSettings().setLoadWithOverviewMode(true);
                titleWebvie.getSettings().setUseWideViewPort(true);

                //添加回调，,Webv
//		wv.addJavascriptInterface(new DaubJSInterface(), "tool");
                titleWebvie.setWebChromeClient(new WebChromeClient());
                DisplayMetrics dm = getResources().getDisplayMetrics();
                int scale = dm.densityDpi;
                if (scale == 240) { //
                    titleWebvie.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
                } else if (scale == 160) {
                    titleWebvie.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
                } else {
                    titleWebvie.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
                }
                titleWebvie.getSettings().setJavaScriptEnabled(true);
                titleWebvie.getSettings().setDefaultTextEncodingName("utf-8");
                titleWebvie.setBackgroundColor(0); // 设置背景色
                //设置载入页面自适应手机屏幕，居中显示
                WebSettings mWebSettings = titleWebvie.getSettings();
                mWebSettings.setUseWideViewPort(true);
                mWebSettings.setLoadWithOverviewMode(true);
                mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

                titleWebvie.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public boolean onJsAlert(WebView view, String url, String message,
                                             JsResult result) {
                        return super.onJsAlert(view, url, message, result);
                    }
                });
                titleWebvie.getSettings().setDefaultFontSize(20);
                titleWebvie.loadUrl(title);
                XSYTools.i(title);
            }

        }

        /**
         * 初始化WebView
         *
         * @param titleWebvie
         * @param title
         */
        private void initWebView(WebView titleWebvie, String title) {
            titleWebvie.getSettings().setJavaScriptEnabled(true);
            titleWebvie.getSettings().setDefaultTextEncodingName("utf-8");
            titleWebvie.setBackgroundColor(0); // 设置背景色
            //设置载入页面自适应手机屏幕，居中显示
            WebSettings mWebSettings = titleWebvie.getSettings();
            mWebSettings.setUseWideViewPort(true);
            mWebSettings.setLoadWithOverviewMode(true);
            mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

            titleWebvie.setWebChromeClient(new WebChromeClient() {
                @Override
                public boolean onJsAlert(WebView view, String url, String message,
                                         JsResult result) {
                    return super.onJsAlert(view, url, message, result);
                }
            });

            document = Jsoup.parse(title);
            body = document.select("body");
            body.attr("align", "center");
            imgs = document.select("img");

            //attr 挑选节点信息
            for (Element ele : imgs) {
                String group = ele.attr("group");
                if (group.toLowerCase().trim().equals("blank")) {
                    ele.attr("src", "file:///android_asset/" + ele.attr("tp") + ".png");
                }
            }
            Element script = document.head().appendElement("script");
            script.attr("type", "text/javascript");
            script.attr("language", "javascript");
            script.attr("src", "file:///android_asset/fillBlank_js.js");
            String title2 = title = document.html();
            titleWebvie.getSettings().setJavaScriptEnabled(true);
            titleWebvie.getSettings().setDefaultFontSize(20);
            titleWebvie.loadDataWithBaseURL(null, title2, "text/html", "utf-8", null);
            XSYTools.i(title2);
        }

    //为每种布局定义自己的ViewHolder
                 class ViewHolder1 {
                    TextView item1_tv;
                    WebView webview;
                    LinearLayout  lin1;
                    LinearLayout  lin2;
                }

                class ViewHolder2 {
                    TextView type;
                    ImageView image;
                }

                 class ViewHolder3 {
                    TextView SW_tile_type;
                    TextView SW_tile_testTile;
                }


     private Drawable loadImageFromNetwork(String imageUrl)
     {
         Drawable drawable = null;
         try {
             // 可以在这里通过文件名来判断，是否本地有此图片
             drawable = Drawable.createFromStream(
                     new URL(imageUrl).openStream(), "image.jpg");
         } catch (IOException e) {
             Log.d("test", e.getMessage());
         }
         if (drawable == null) {
             Log.d("test", "null drawable");
         } else {
             Log.d("test", "not null drawable");
         }

         return drawable ;
     }
 }