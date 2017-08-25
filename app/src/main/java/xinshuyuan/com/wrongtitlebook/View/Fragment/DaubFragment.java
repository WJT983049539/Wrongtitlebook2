package xinshuyuan.com.wrongtitlebook.View.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;

import com.ibpd.xsy.varDefine.GlobalVarDefine;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.AnswerEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

import xinshuyuan.com.wrongtitlebook.R;

//import com.xsy.student.eventListener.DaubOnClickListener;

/**
 * 涂抹题fragment
 * @author wjt
 *
 */
public class DaubFragment extends BaseFragment {
	private TestEntity testEntity;
	private Integer itemType;
	private WebView ansWv;
	private Handler handler;
	private Integer scrollLen;
	public Integer getItemType() {
		return itemType;
	}
	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public TestEntity getTestEntity() {
		return testEntity;
	}
	public void setTestEntity(TestEntity testEntity) {
		this.testEntity = testEntity;
	}
	private View mView;
	public View getmView() {
		return mView;
	}
	private Document document;
	private String ansValue="";
	public DaubFragment(){}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		mView= inflater.inflate(R.layout.layout_daub, container,false);
		ansWv=this.getViewById(mView, R.id.daub_ans);
		try {
			initTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mView;
	}

	private void initTest() throws JSONException {
		if( XSYTools.isNull(testEntity)){
			XSYTools.showToastmsg(getActivity(),"数据有误");
			return;
		}
		//赋值正常，继续
		//集中式和分布式的区别是：1、集中式不显示题干，仅显示可选项或答案填写区，分布式则得显示题干；
		//2、集中式无法切换到下一题或上一题，全靠服务器推送，分布式可自主选择题目
		String itemPoint=testEntity.getPoint();
		document = Jsoup.parse(itemPoint);
		itemPoint=document.html();
		itemPoint="<span style=\"color:red;font-weight:bold;\">[涂抹题]</span>"+itemPoint;
		Log.d("html", itemPoint);
		//设置题干显示相关属性，以及相关node的格式化操作
		WebView wv=this.getViewById(mView, R.id.daub_itemPoint);
		// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
		 wv.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
			HitTestResult hit = view.getHitTestResult();
			if(hit!=null){
				int hitType=hit.getType();
				//如果是一个url超链接转到默认浏览器
				 if (hitType == HitTestResult.SRC_ANCHOR_TYPE||hitType == HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
				 }else{
				 }
			}
			return true;
			}
			});
		
		wv.loadDataWithBaseURL(null, itemPoint, "text/html", "utf-8", null);
		wv.getSettings().setDefaultFontSize(45);
		initWebView(wv);
		//题干显示完成
		//该显示选项了
		loadOption();
	}
	private void initWebView(WebView wv){
		wv.getSettings().setBlockNetworkImage(false);//解决图片不显示
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.getSettings().setDomStorageEnabled(true);
		wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
		wv.getSettings().setLoadsImagesAutomatically(true);  //支持自动加载图片
		wv.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		//添加回调，,Webv
//		wv.addJavascriptInterface(new DaubJSInterface(), "tool");
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
		
		
//		//没有点击到图片上的提示框
//		wv.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public boolean onJsAlert(final WebView view, String url, String message,
//                                     JsResult result)
//            {
//
//            	CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
//            	  builder.setMessage(message);
//            	  builder.setTitle("提示");
//            	  builder.setPositiveButton("确定",
//      					new DialogInterface.OnClickListener() {
//      						public void onClick(DialogInterface dialog, int which) {
//      							//view.reload();//重写刷新页面
//      							dialog.dismiss();
//      						}
//      					});
//            		builder.create(true).show();
//            		result.confirm();
//                return true;
//              //super.onJsAlert(view, url, message, result);
//            }
//        });
	}
	//加载涂抹试题
	private void loadOption(){
		
		List<AnswerEntity> ansList=this.testEntity.getAnswerList();
		if(ansList==null || ansList.size()==0){
			XSYTools.showToastmsg(getActivity(),"没有数据");
			return;
		}
		AnswerEntity ans=ansList.get(0);
		initWebView(ansWv);
		String daubpicUrl=XSYTools.getWrongUrl(GlobalVarDefine.DOTEST_DAUB_URL,getActivity())+"?"+ GlobalVarDefine.PARAM_TESTID+"="+ans.getTestId();
//		ansWv.pauseTimers();
		ansWv.loadUrl(daubpicUrl);
	}


	public String getAnsValue() {
		return ansValue;
	}
	public void setAnsValue(String ansValue) {
		this.ansValue = ansValue;
	}

	public void setInfo(TestEntity testEntity) {
		this.testEntity=testEntity;
	}
	@Override
	public void onPause() {
		super.onPause();
		ansWv.pauseTimers();
	}

	@Override
	public void onResume() {
		super.onResume();
		ansWv.resumeTimers();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ansWv.resumeTimers();
	}
}
