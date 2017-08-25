package xinshuyuan.com.wrongtitlebook.Model.CustomView.PreViewIncludeCustomLayout;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.FixGridLayout;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.TestEditText;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Fragment.IncludeFragment;

/**
 * 嵌套题中的多选题自定义插件， 或叫自定义布局
 * @author Administrator
 *
 */
public class FillBlankLayout extends IncludeLinearLayout {
	/*
	 * testId,这里的ID其实就是嵌套题中的小题的ID了
	 */
	private Activity activity;
	private int main_with;
	private View digitView;
	private Context content ;
	private Context context;
	private LinearLayout mainLayout=null;
	private LinearLayout itemLayout=null;
	private LinearLayout optionsLayout=null;
	private Map<String, String> answerMap=new HashMap<String,String>();
	private IncludeFragment fragment=null;
	private LinearLayout rLayoutMainActivity;
	private View mView=null;
	public FillBlankLayout(Context context,IncludeFragment fragment,View mView) {
		super(context);
		this.context=context;
		this.fragment=fragment;
		this.mView=mView;
		mainLayout=new LinearLayout(context);
		initMainLayout();
		activity=(Activity)context;

	}

//	private void panduan() {
//		LinearLayout lin = fragment.getlll();
//
//		// 如果是输入法启动
//		if (fragment.getActivity().getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
//			Display display = fragment.getActivity().getWindowManager().getDefaultDisplay();
//			// 如果键盘是弹出状态，隐藏其他的layout,否则不隐藏
//			List<View> lise = XSYTools.getAllChildViewss(lin);
//			for (int i = 0; i < lise.size(); i++) {
//				if (lise.get(i) instanceof IncludeLinearLayout) {
//					if (lise.get(i) instanceof FillBlankLayout) {
//
//					} else {
//						lise.get(i).setVisibility(View.GONE);
//					}
//
//				}
//			}
//
//		}else{
//			// 如果键盘是BU弹出状态，BU隐藏其他的layout,
//			List<View> lise = XSYTools.getAllChildViewss(lin);
//			for (int i = 0; i < lise.size(); i++) {
//				if (lise.get(i) instanceof IncludeLinearLayout) {
//					if(lise.get(i).getVisibility()==View.GONE){
//						lise.get(i).setVisibility(View.VISIBLE);
//					}
//				}
//			}
//
//		}
//	}

	private void initMainLayout(){
		rLayoutMainActivity=(LinearLayout) fragment.getActivity().findViewById(R.id.include_ding);
		if(mainLayout==null)
			return;
		mainLayout.setOrientation(LinearLayout.VERTICAL);//主布局采用多行排列，然后在主布局中分别实现第一行 题干，第二行可选项
		LinearLayout.LayoutParams lpq=new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		lpq.topMargin=100;

		itemLayout=new LinearLayout(context);
		itemLayout.setWeightSum(8);
		itemLayout.setLayoutParams(lpq);
		optionsLayout=new LinearLayout(context);
		optionsLayout.setWeightSum(2);
		mainLayout.addView(itemLayout);
		mainLayout.addView(optionsLayout);

		this.addView(mainLayout);

	}
	public void initOptions(TestEntity te) throws JSONException {
		//填空题的特殊之处在于，其选项就在题干当中，需要分析题干，找出可选项，然后渲染为指定控件，并绑定事件，完成数据交互，最后将答案保存到指定位置
		String ans="";
		Document document = Jsoup.parse(te.getPoint());
		Elements imgs=document.select("img");
		FixGridLayout entitysLayout=new FixGridLayout(context);

		entitysLayout.setmCellHeight(XSYTools.dip2px(context, 100));
		entitysLayout.setmCellWidth(XSYTools.dip2px(context, 200));
		for(Element ele:imgs){
			String group=ele.attr("group");
			String tp=ele.attr("tp");//类别
			String id=ele.attr("_id");//对应的id
			TestEditText et=new TestEditText(this.context);
			et.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			et.setClickable(false);
			et.setFocusable(false);

			//他是影响框变成自适应框吗？先标记一下
			et.setSingleLine(false);
			et.setHorizontallyScrolling(false);
			et.setCursorVisible(true);
			et.setAnsBlankId(id);
			//小方框
			if(tp.toLowerCase().equals("block")){
				et.setImeOptions(EditorInfo.IME_ACTION_SEND);
				et.setWidth(XSYTools.dip2px(context, 40));
				et.setHeight(XSYTools.dip2px(context, 40));
				et.setBackgroundResource(R.drawable.fillblank_square);
				//et.setFocusable(true);
				// requestFocus();

//				et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//					@Override
//					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//						if (actionId==EditorInfo.IME_ACTION_SEND ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER))
//						{
//							return true;
//						}
//						return false;
//					}
//				});







				//圆
			}else if(tp.trim().toLowerCase().equals("circle")){
				et.setImeOptions(EditorInfo.IME_ACTION_SEND);
				//et.setFocusable(true);
				// requestFocus();

//				//设置里面只能输入特殊指定的字符
//				et.setKeyListener(new NumberKeyListener(){
//					@Override
//					protected char[] getAcceptedChars() {
//						char[]numberChars={'1','2','3','4','5','6','7','8','9','0'};
//
//								return numberChars;
//					}
//					@Override
//					public int getInputType() {
//						// TODO Auto-generated method stub
//						return 0;
//					}});

				et.setWidth(XSYTools.dip2px(context, 40));
				et.setHeight(XSYTools.dip2px(context, 40));
				et.setBackgroundResource(R.drawable.fillblank_allshape);

//				et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//					@Override
//					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//						if (actionId==EditorInfo.IME_ACTION_SEND ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER))
//						{
//							return true;
//						}
//						return false;
//					}
//				});

				//如果做了题找出那个选项，遍历试题id
				//得到答案id集合

				//大院
			}else if(tp.trim().toLowerCase().equals("bracket")){
				et.setWidth(XSYTools.dip2px(context, 120));
				et.setHeight(XSYTools.dip2px(context, 40));
				et.setBackgroundResource(R.drawable.fillblank_smallshape);


			}else{
				et=null;

			}
			if(et==null){

				continue;
			}
			//如果做了题找出那个选项，遍历试题id


			et.setText(ans);
//			// 点击空白隐藏键盘
//			rLayoutMainActivity.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//					View view = fragment.getActivity().getWindow().peekDecorView();
//					if (view != null && view.getWindowToken() != null) {
//
//						InputMethodManager inputmanger = (InputMethodManager) fragment.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);//input_method_service
//						inputmanger.hideSoftInputFromWindow(view.getWindowToken(),0);
//
//					}
//
//
//				}
//			});
			valuation(ans,et,fragment);
			entitysLayout.addView(et);
		}
		optionsLayout.addView(entitysLayout);

	}
	//放入答案
	private void valuation(String ans, TestEditText et, IncludeFragment fragment2) {
		if(ans!=null)
			this.addAnswer(et.getAnsBlankId(), et.getText().toString());
	}

	private void initWebView(WebView wv){
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

	public void initItemPoint(TestEntity te, int options) {
		try {
			initTest(te,options);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void initTest(TestEntity testEntity, int options) throws JSONException{

		//把序号转成字符型
		String title=String.valueOf(options);
		title=title+"、";
		String itemPoint=testEntity.getPoint();
		title=title+itemPoint;
		Document document = Jsoup.parse(title);

//		String itemPoint=testEntity.getItemPoint();
//		Document document = Jsoup.parse(itemPoint);
		Elements imgs=document.select("img");
		for(Element ele:imgs){
			ele.attr("src","file:///android_asset/"+ele.attr("tp")+".png");
			//ele.attr("onClick","javascript:showBlank(this);");
		}
		//加入执行js函数
//		Element script=document.head().appendElement("script");
//		script.attr("type","text/javascript");
//		script.attr("language","javascript");
//		script.attr("src","file:///android_asset/fillBlank_js.js");
		itemPoint=document.html();
		//设置题干显示相关属性，以及相关node的格式化操作
		WebView wv=new WebView(context);
		// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
		wv.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				HitTestResult hit = view.getHitTestResult();
				if(hit!=null){
					int hitType=hit.getType();
					//如果是一个url超链接转到默认浏览器
					if (hitType == HitTestResult.SRC_ANCHOR_TYPE||hitType == HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
//										 Intent i = new Intent(Intent.ACTION_VIEW);
//										 i.setData(Uri.parse(url));
//										 startActivity(i);
						//转到activity播放
//						Intent intent=new Intent(activity,WebViewActivity.class);
//						intent.putExtra("data", url);
//						activity.startActivity(intent);

						//转到dialog
//										 UriView_dialog uri_dialog=new UriView_dialog(getActivity());
//										 uri_dialog.StartUri(url);
//										 uri_dialog.show();

					}else{
						// view.loadUrl(url);
					}
				}
				return true;
			}
		});




		wv.loadDataWithBaseURL(null, itemPoint, "text/html", "utf-8", null);
		initWebView(wv);
		itemLayout.addView(wv);
		this.setTestId(testEntity.getTestId());
		this.setTestType(testEntity.getTestType());
		//题干显示完成
	}

	public Map<String, String> getAnswerMap() {
		return answerMap;
	}

	public void setAnswerMap(Map<String, String> answerMap) {
		this.answerMap = answerMap;
	}

	public void addAnswer(String id,String val){
		Map<String, String> map=getAnswerMap();
		if(map==null){
			map=new HashMap<String, String>();
		}
//		comm.toast("pull:+"+id+"---"+val);
		map.put(id, val);
	}
	public void removeAnswer(String testId){
		Map<String, String> map=getAnswerMap();
		if(map==null){
			return;
		}
		map.remove(testId);
	}

//	@Override
//	public void show() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void hidden() {
//		// TODO Auto-generated method stub
//		
//	}
}
