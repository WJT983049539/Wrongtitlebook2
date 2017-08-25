package xinshuyuan.com.wrongtitlebook.View.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.lang.reflect.Field;

import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * 操作题的fragment
 * @author wjt
 *
 */
public class OPRATEFragment extends BaseFragment {
	
	private ScrollView oprateScroview;
	private Integer scrollLen;
	private Runnable runnable = new Runnable() {
	    @Override
	    public void run() {  
	    	oprateScroview.scrollTo(0,scrollLen);// 改变滚动条的位置  
	    }  
	};
	private Handler handler;
	
	private Integer itemType;
	private TestEntity testEntity = null;
	private View mView;
	private Document document;
	public Bitmap bitmap;
	private ImageButton imageButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		    mView = inflater.inflate(R.layout.layout_oprate, container, false);
		    imageButton=(ImageButton)mView.findViewById(R.id.takephone_button);

		try {
			initTest();

		} catch (Exception e) {



			e.printStackTrace();
		}

		return mView;
	}

	private void initTest() throws JSONException {
		if ( XSYTools.isNull(testEntity)) {
			XSYTools.showToastmsg(getActivity(),"数据有误");
			return;
		}

		// 赋值正常，继续
		// 集中式和分布式的区别是：1、集中式不显示题干，仅显示可选项或答案填写区，分布式则得显示题干；
		// 2、集中式无法切换到下一题或上一题，全靠服务器推送，分布式可自主选择题目
		String itemPoint = testEntity.getPoint();
		document = Jsoup.parse(itemPoint);
    	itemPoint = document.html();
		itemPoint="<span style=\"color:red;font-weight:bold;\">[操作题]</span>"+itemPoint;
		
		Log.d("html", itemPoint);
		// 设置题干显示相关属性，以及相关node的格式化操作
		WebView wv = this.getViewById(mView, R.id.oprate_itemPoint);
		// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
		 wv.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
			HitTestResult hit = view.getHitTestResult();
			if(hit!=null){
				int hitType=hit.getType();
				//如果是一个url超链接转到默认浏览器
				 if (hitType == HitTestResult.SRC_ANCHOR_TYPE||hitType == HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {

				 }else{
					// view.loadUrl(url);
				 }
			}
			return true;
			}
			});
		wv.getSettings().setDefaultFontSize(25);
		wv.loadDataWithBaseURL(null, itemPoint, "text/html", "utf-8", null);
		initWebView(wv);
		// 题干显示完成
	}

	// 初始化浏览器
	@SuppressLint("SetJavaScriptEnabled")
	@SuppressWarnings("deprecation")
	private void initWebView(WebView wv) {
		wv.getSettings().setDefaultTextEncodingName("utf-8") ;
		wv.setBackgroundColor(0); // 设置背景色
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
		// 下面的这段是为了防止网页中的部分按钮点击无响应而设置的,原因不详
		wv.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});

	}




	
	@Override
	public void onDetach() {
		super.onDetach();
		 try {
		        Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
		        childFragmentManager.setAccessible(true);
		        childFragmentManager.set(this, null);

		    } catch (Exception e) {
		        e.printStackTrace();
		    }
	}

	/**
	 * 放入数据
	 * @param testEntity
	 */
	public void setInfo(TestEntity testEntity) {
		this.testEntity=testEntity;
	}

}
