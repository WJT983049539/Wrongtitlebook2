package xinshuyuan.com.wrongtitlebook.View.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.AnswerEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

import xinshuyuan.com.wrongtitlebook.Model.Common.LineButton;
import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * 连线题fragment
 * @author wjt
 *
 */
@SuppressLint("NewApi")
public class LINEFragment extends BaseFragment {


	private Integer itemType;
	private TestEntity testEntity = null;
	private View mView;
	private Document document;
	private Canvas canvas;
	private Paint paint;
	private ImageView ib;
	private ImageButton xiaochuall;
	private ImageButton xiaochu;
	private static Bitmap baseBitmap2;
	private int ID_BTN1 = 0;
	private LinearLayout line_zhu;
	//储存原始按钮的集合
	List<AnswerEntity> answerList = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		answerList = testEntity.getAnswerList();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.layout_linefragment, container, false);
		line_zhu = (LinearLayout) mView.findViewById(R.id.line_main);
		initTest();
		//擦出按钮
		return mView;
	}
	private void initTest() {
		String itemPoint=testEntity.getPoint();
		document = Jsoup.parse(itemPoint);


		//
		itemPoint=document.html();
		itemPoint="<span style=\"color:red;font-weight:bold;\">[连线题]</span>"+itemPoint;
		Log.d("html", itemPoint);
		//设置题干显示相关属性，以及相关node的格式化操作
		WebView wv=this.getViewById(mView, R.id.line_webView1);
		//设置字体
		WebSettings settings=wv.getSettings();
		settings.setTextSize(TextSize.LARGER);
		wv.loadDataWithBaseURL(null, itemPoint, "text/html", "utf-8", null);
		initWebView(wv);
		//题干显示完成
		try{
			loadOption();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void initWebView(WebView wv) {
		wv.getSettings().setBlockNetworkImage(false);//解决图片不显示
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setDefaultTextEncodingName("utf-8") ;
		wv.setBackgroundColor(0); // 设置背景色
		wv.getSettings().setDefaultFontSize(19);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setWebChromeClient(new WebChromeClient());
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int scale = dm.densityDpi;
		if (scale == 240) { //
			wv.getSettings().setDefaultZoom(ZoomDensity.FAR);
		} else if (scale == 160) {
			wv.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
		} else {
			wv.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
		}
		//下面的这段是为了防止网页中的部分按钮点击无响应而设置的,原因不详
		wv.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
									 JsResult result)
			{
				return super.onJsAlert(view, url, message, result);
			}
		});

	}


	private void loadOption() throws JSONException {
		LinearLayout left=this.getViewById(mView, R.id.line_left_showqq);//左排
		if(left.getChildCount()>0){
			left.removeAllViews();
		}

		LinearLayout right=this.getViewById(mView, R.id.line_right_layout);//右排
		if(right.getChildCount()>0){
			right.removeAllViews();
		}
		// 得到集合
		List<AnswerEntity> ansList=this.testEntity.getAnswerList();
		if(ansList==null || ansList.size()==0){
			XSYTools.showToastmsg(getActivity(),"没有东西");
			return;
		}
		//遍历得到AnswerEntity每个对象
		for(AnswerEntity ans:ansList){


			String optionAnswer=ans.getOptionAnswer();
			//转换成json对象
			//JSONObject  jsonObject = new JSONJSONArray(optionAnswer);
			//得到数组对象
			JSONArray jsonArray = new JSONArray(optionAnswer);
			for(int i=0;i<jsonArray.length();i++) {
				ID_BTN1=ID_BTN1+1;
				JSONObject jsonOb = (JSONObject)jsonArray.opt(i);
				String type= jsonOb.getString("type");
				String text=jsonOb.getString("text");
				String pair=jsonOb.getString("pair");
				String order=jsonOb.getString("order");
				System.out.println("type="+type+"pair=="+pair);
				if(type.equals("left")){
					String html = "<html><head><style type='text/css'>.cont{text-align:right;}p{text-align:right;}</style></head><body><div class='cont'>"
							+text+ "</div></body></html>";
					//弄一些竖排的linearLayout来显示选项、后面加小圆圈用来连接,在左边显示
					RelativeLayout subll=new RelativeLayout(this.getActivity());
					RelativeLayout.LayoutParams lp3 = new  RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					subll.setGravity(Gravity.RIGHT);
					//new出按钮
					LineButton linebutton=new LineButton(getActivity());

					linebutton.setId(ID_BTN1);
					linebutton.setLineButtonId(String.valueOf(ID_BTN1));
					linebutton.setPair(pair);
					linebutton.setOder(order);
					linebutton.setType(type);
					//放入样式
					linebutton.setBackgroundResource(R.drawable.selectlinebutton);
					linebutton.setWidth(XSYTools.dip2px(this.getActivity(), 40));
					linebutton.setHeight(XSYTools.dip2px(this.getActivity(), 45));
					//把按钮添加到原始数据集合
					// listlist_start.add(linebutton);
					//添加参数
					RelativeLayout.LayoutParams lp1 = new  RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					lp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					lp1.setMargins(0, 10, 0, 0);
					//加载button
					subll.addView(linebutton,lp1);
					//newWeb浏览器

					WebView ansWv=new WebView(this.getActivity());
					//ansWv.loadUrl("file:///android_asset/web.js.js");
					WebSettings webSettings = ansWv.getSettings();
					webSettings.setSavePassword(false);
					webSettings.setSaveFormData(false);
					webSettings.setJavaScriptEnabled(true);
					ansWv.getSettings().setJavaScriptEnabled(true);
					ansWv.getSettings().setDefaultTextEncodingName("utf-8") ;
					ansWv.setBackgroundColor(0); // 设置背景色
					webSettings.setSupportZoom(false);
					ansWv.setWebChromeClient(new WebChromeClient());
					ansWv.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
					//ansWv.loadDataWithBaseURL(null, text,"text/html", "utf-8", null);
					//滚动条没有
					ansWv.setVerticalScrollBarEnabled(false);
					initWebView(ansWv);
					//？？
					RelativeLayout.LayoutParams lp_topheader = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					//RelativeLayout.LayoutParams lp2 = new  RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					//lp2.addRule(RelativeLayout.ALIGN_BASELINE,ID_BTN1);
					lp_topheader.addRule(RelativeLayout.ALIGN_BOTTOM, ID_BTN1);
					lp_topheader.addRule(RelativeLayout.LEFT_OF, ID_BTN1);
					lp_topheader.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					lp_topheader.setMargins(20, 0, 10, 0);
					//lp_topheader.addRule(RelativeLayout.ALIGN_LEFT, ID_BTN1);
					subll.addView(ansWv,lp_topheader);
					left.addView(subll);

				}else if(type.equals("right")){
					//弄一些竖排的linearLayout来显示选项、后面加小圆圈用来连接,在左边显示
					RelativeLayout subll=new RelativeLayout(this.getActivity());
					RelativeLayout.LayoutParams lp3 = new  RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					//subll.setGravity(Gravity.RIGHT);
					//new出按钮
					LineButton linebutton=new LineButton(getActivity());
					linebutton.setId(ID_BTN1);
					linebutton.setLineButtonId(String.valueOf(ID_BTN1));
					linebutton.setPair(pair);
					linebutton.setOder(order);
					linebutton.setType(type);
					linebutton.setBackgroundResource(R.drawable.selectlinebutton);
					linebutton.setWidth(XSYTools.dip2px(this.getActivity(), 20));
					linebutton.setHeight(XSYTools.dip2px(this.getActivity(), 26));
					//把按钮添加到原始数据集合
					// listlist_start.add(linebutton);

					//添加参数
					RelativeLayout.LayoutParams lp1 = new  RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);//align_parent_right
					lp1.setMargins(0, 15, 0, 0);
					//加载button
					subll.addView(linebutton,lp1);
					//newWeb浏览器
					WebView ansWv=new WebView(this.getActivity());
					ansWv.getSettings().setJavaScriptEnabled(true);
					ansWv.getSettings().setDefaultTextEncodingName("utf-8") ;
					ansWv.setBackgroundColor(0); // 设置背景色
					ansWv.loadDataWithBaseURL(null, text,"text/html", "utf-8", null);
					//滚动条没有
					ansWv.setVerticalScrollBarEnabled(false);
					initWebView(ansWv);
					//？？
					RelativeLayout.LayoutParams lp_topheader = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					lp_topheader.addRule(RelativeLayout.ALIGN_BOTTOM, ID_BTN1);
					lp_topheader.addRule(RelativeLayout.RIGHT_OF, ID_BTN1);
					lp_topheader.setMargins(20, 0, 10, 0);
					subll.addView(ansWv,lp_topheader);
					right.addView(subll);
				}
			}
		}
	}



	public void setInfo(TestEntity testEntity) {
  		this.testEntity=testEntity;
	}
}
