package xinshuyuan.com.wrongtitlebook.View.Fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import xinshuyuan.com.wrongtitlebook.Model.Common.SideIndexGestureListener;
import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.FixGridLayout;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.TestEditText;
import xinshuyuan.com.wrongtitlebook.R;

@SuppressLint("SetJavaScriptEnabled")
/**
 *填空fragment
 * @author wjt
 *
 */
public class FillBlankFragment extends BaseFragment {
	private LinearLayout rLayoutMainActivity;
	private Integer itemType;
	private TestEntity testEntity;
	private View mView;
	private Elements imgs;
	private Map<String, String> answerMap = new HashMap<String, String>();
	private Document document;
	private Map<Long, Integer> indexMap = new LinkedHashMap<Long, Integer>();
	private String zhuangtai = "";
	private ScrollView FillblankScroview;
	private Handler handler;
	private Integer scrollLen;
	private LinearLayout subll;
	private GestureDetector mGestureDetector;
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			FillblankScroview.scrollTo(0, scrollLen);// 改变滚动条的位置
		}
	};
	private Context act;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.layout_fillblank, container, false);
		try {
			 mGestureDetector = new GestureDetector(getActivity(),new SideIndexGestureListener());
			
			initTest();
			showkong();


		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mView;
	}

     //显示填空
	private void showkong() throws JSONException {
		// 开始显示填空
		int tt = 1;
		for (Element ele : imgs) {
			String id = ele.attr("_id");
			String group = ele.attr("group");
			String tp = ele.attr("tp");
			if (XSYTools.isEmpty(id) || XSYTools.isEmpty(group) || XSYTools.isEmpty(tp)) {
				continue;
			}
			if (group.toLowerCase().trim().equals("blank")) {
				showBlank(tp, id, "", tt);
				tt++;
			}
		}
		// 填空显示完成
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}



	@SuppressLint("JavascriptInterface")
	private void initTest() throws JSONException {
		// 赋值正常，继续
		// 集中式和分布式的区别是：1、集中式不显示题干，仅显示可选项或答案填写区，分布式则得显示题干；
		// 2、集中式无法切换到下一题或上一题，全靠服务器推送，分布式可自主选择题目
		String itemPoint = testEntity.getPoint();
		document = Jsoup.parse(itemPoint);
		document.body().attr("onload", "javascript:myjss.over();");
		imgs = document.select("img");
		//attr 挑选节点信息 
		for (Element ele : imgs) {
			Log.d("jsoup", "tp=" + ele.attr("tp"));
			String group = ele.attr("group");
			if (group.toLowerCase().trim().equals("blank")) {
				ele.attr("src", "file:///android_asset/" + ele.attr("tp") + ".png");
				ele.attr("onClick", "javascript:showBlank(this);");
			}
			
		}

		// 加入执行js函数
		Element script = document.head().appendElement("script");
		script.attr("type", "text/javascript");
		script.attr("language", "javascript");
		script.attr("src", "file:///android_asset/fillBlank_js.js");
		itemPoint = document.html();
		itemPoint = "<span style=\"color:red;font-weight:bold;\">[填空题]</span>"
				+ itemPoint;
		Log.d("html", itemPoint);
		if (imgs == null || imgs.size() == 0) {
			XSYTools.showToastmsg(getActivity(),"原始数据不正确");
			return;
		}
		// 设置题干显示相关属性，以及相关node的格式化操作
		WebView wv = this.getViewById(mView, R.id.fillblank_itemPoint);
		inintwebview(wv);
//		wv.addJavascriptInterface(new myjs(this, act, testEntity,(WebView) this.getViewById(mView, R.id.fillblank_itemPoint)),"myjss");
		// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
		wv.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				HitTestResult hit = view.getHitTestResult();
				if (hit != null) {
					int hitType = hit.getType();
					// 如果是一个url超链接转到默认浏览器
					if (hitType == HitTestResult.SRC_ANCHOR_TYPE
							|| hitType == HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {

					} else {
						// view.loadUrl(url);
					}
				}
				return true;
			}
		});
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setDefaultTextEncodingName("utf-8") ;
		wv.setBackgroundColor(0); // 设置背景色
		wv.getSettings().setDefaultFontSize(25);
		wv.loadDataWithBaseURL(null, itemPoint, "text/html", "utf-8", null);

		// wv.setWebChromeClient(new WebChromeClient());

		// 题干显示完成

	}

	private void inintwebview(WebView wv22) {
		wv22.getSettings().setJavaScriptEnabled(true);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int scale = dm.densityDpi;
		if (scale == 240) { //
			wv22.getSettings().setDefaultZoom(ZoomDensity.FAR);
		} else if (scale == 160) {   
			wv22.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
		} else {
			wv22.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
		}
		// 设置回调java接口,名字为tool
//		wv22.addJavascriptInterface(new FillBlankJSInterface(), "tool");
		// 下面的这段是为了防止网页中的部分按钮点击无响应而设置的,原因不详
		wv22.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});

	}

	// 这里的id是上面的空的id
	public void showBlank(String tp, String id, String val, int number)throws JSONException {
		String ans = "";
		Long answerId = (long) 0;
		rLayoutMainActivity = this.getViewById(mView,R.id.fillblank__linear_lef_top_max);
		// 自动换行的layout布局
		FixGridLayout ll = this.getViewById(mView,R.id.fillblank_blank_container);
		ll.setmCellHeight(XSYTools.dip2px(getActivity(), 100));
		ll.setmCellWidth(XSYTools.dip2px(getActivity(), 190));
		Log.d("viewCount", String.valueOf(ll.getChildCount()));
		subll = new LinearLayout(this.getActivity());
		subll.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.setMargins(20, 10, 10, 10);
		// 就是这个，标记一下
		TestEditText te = new TestEditText(this.getActivity());
		te.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		te.setSingleLine(false);
		te.setHorizontallyScrolling(false);
		te.setCursorVisible(true);
		te.clearFocus();
		te.setFocusable(false);
		TextView tv = new TextView(this.getActivity());
		tv.setLayoutParams(lp);
		te.setAnsBlankId(id);
		System.out.println("id" + id);
		if (tp.trim().toLowerCase().equals("block")) {
			//te.setImeOptions(EditorInfo.IME_ACTION_SEND);

			te.setWidth(XSYTools.dip2px(this.getActivity(), 40));
			te.setHeight(XSYTools.dip2px(this.getActivity(), 40));
			te.setBackgroundResource(R.drawable.fillblank_square);

			// 找出这个题，然后从答案里面找答案
			// 得到答案id集合

		} else if (tp.trim().toLowerCase().equals("circle")) {
			te.setWidth(XSYTools.dip2px(this.getActivity(), 40));
			te.setHeight(XSYTools.dip2px(this.getActivity(), 40));
			te.setBackgroundResource(R.drawable.fillblank_allshape);

			// 如果做了题找出那个选项，遍历试题id
			// 得到答案id集合

		} else if (tp.trim().toLowerCase().equals("bracket")) {
			te.setWidth(XSYTools.dip2px(this.getActivity(), 120));
			te.setHeight(XSYTools.dip2px(this.getActivity(), 40));
			te.setBackgroundResource(R.drawable.fillblank_smallshape);

			// 如果做了题找出那个选项，遍历试题id
			// 得到答案id集合

		}else{
			te=null;
		}
		if(te==null){
			
		}else{
		tv.setText("填空" + number + ":");
		tv.setTextSize(20f);
		tv.setTextColor(Color.RED);
		te.setTp(tp.trim().toLowerCase());
		tv.setTextColor(Color.BLACK);
		te.setText(ans);

		TextView text = new TextView(this.getActivity());
		text.setWidth(XSYTools.dip2px(this.getActivity(), 10));
		text.setHeight(XSYTools.dip2px(this.getActivity(), 40));

		TextView text2 = new TextView(this.getActivity());
		text2.setWidth(XSYTools.dip2px(this.getActivity(), 40));
		text2.setHeight(XSYTools.dip2px(this.getActivity(), 40));
		// 给每个空得到，失去一下焦点
		System.out.println(ans);

		subll.addView(text2);
		subll.addView(tv);
		subll.addView(text);
		subll.addView(te);

		if (ll == null) {
		} else {
			ll.addView(subll);
		}

	}
	}
	// 显示答案，有个问题，存进去的会重复
	private void valuation(final TestEditText tee, final WebView webView, final String id) {
		fillAnswerToMap(tee.getAnsBlankId(), tee.getText().toString());
	}



	public void clearAnswerMap() {
		this.getAnswerMap().clear();
	}

	public Map<String, String> getAnswerMap() {
		return answerMap;
	}

	public void getway() {

	}

	public void fillAnswerToMap(String id, String value) {
		Log.d("fillAnswer", "添加试题答案@" + id + "  " + value);
		this.getAnswerMap().put(id, value);
	}

	public Integer getItemType() {
		return itemType;
	}

	public TestEntity getTestEntity() {
		return testEntity;
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		clearAnswerMap();
	}

	// 判断状 态
	public Elements getimgs() {
		return this.imgs;
	}

    // 隐藏软键盘
	private void HideSoftInput(IBinder token) {
		if (token != null) {
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
	}
	// 判定是否需要隐藏
	 private boolean isHideInput(View v, MotionEvent ev) {
	        if (v != null && (v instanceof TestEditText)) {
	            int[] l = { 0, 0 };
	            v.getLocationInWindow(l);
	            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
	                    + v.getWidth();
	            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
	                    && ev.getY() < bottom) {
	                return false;
	            } else {
	                return true;
	            }
	        }
	        return false;
	    }


	public void setInfo(TestEntity testEntity) {
		this.testEntity=testEntity;
	}
}
