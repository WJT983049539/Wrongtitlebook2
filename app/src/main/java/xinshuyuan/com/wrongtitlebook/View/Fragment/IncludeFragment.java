package xinshuyuan.com.wrongtitlebook.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.ibpd.xsy.varDefine.GlobalVarDefine;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.AnswerEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.PreViewIncludeCustomLayout.FillBlankLayout;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.PreViewIncludeCustomLayout.IncludeLinearLayout;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.PreViewIncludeCustomLayout.MultiSelectLayout;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.PreViewIncludeCustomLayout.SingleSelectLayout;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.PreViewIncludeCustomLayout.YesOrNoSelectLayout;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.TestEditText;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.PreViewTestHandler;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * 嵌套題碎片
 * @author wjt
 *
 */
public class IncludeFragment extends BaseFragment {
	PreViewTestHandler preViewTestHandler;
	private ScrollView includeScroview;
	private Integer scrollLen;
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// 改变滚动条的位置
			includeScroview.scrollTo(0,scrollLen);
		}
	};
	private Handler handler;


	private  static int  options=1;
	private Integer itemType;
	private TestEntity testEntity=null;
	private View mView;
	public View getmView() {
		return mView;
	}
	public void setmView(View mView) {
		this.mView = mView;
	}
	private Map<String, String> answerMap=new HashMap<String,String>();
	private Document document;
	/**
	 * mslList的作用是存放本页面当中所有的多选题小题，等需要归结答案的时候，可以用这个list来归结答案
	 */
	private List<MultiSelectLayout> mslList=new ArrayList<MultiSelectLayout>();
	/**
	 * sslList的作用是存放本页面当中所有的单选题小题，等需要归结答案的时候，可以用这个list来归结答案
	 */
	private List<SingleSelectLayout> sslList=new ArrayList<SingleSelectLayout>();
	/**
	 * yonslList的作用是存放本页面当中所有的选择题小题，等需要归结答案的时候，可以用这个list来归结答案
	 */
	private List<YesOrNoSelectLayout> yonslList=new ArrayList<YesOrNoSelectLayout>();
	/**
	 * yonslList的作用是存放本页面当中所有的选择题小题，等需要归结答案的时候，可以用这个list来归结答案
	 */
	private List<FillBlankLayout> fblList=new ArrayList<FillBlankLayout>();
	public IncludeFragment(){}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//得到答题类型
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mView= inflater.inflate(R.layout.layout_include, container,false);
		try {
			initTest();
			//滚动条


		} catch (Exception e) {
			e.printStackTrace();
		}
		return mView;
	}

	private void initTest() throws JSONException{
		//赋值正常，继续
		//集中式和分布式的区别是：1、集中式不显示题干，仅显示可选项或答案填写区，分布式则得显示题干；
		//2、集中式无法切换到下一题或上一题，全靠服务器推送，分布式可自主选择题目
		String itemPoint=testEntity.getPoint();

		document = Jsoup.parse(itemPoint);
		itemPoint=document.html();
		itemPoint="<span style=\"color:red;font-weight:bold;\">[嵌套题]</span>"+itemPoint;

		Log.d("html", itemPoint);
		//设置题干显示相关属性，以及相关node的格式化操作
		WebView wv=this.getViewById(mView, R.id.include_itemPoint);

		// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
		wv.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				HitTestResult hit = view.getHitTestResult();
				if(hit!=null){
					int hitType=hit.getType();
					//如果是一个url超链接转到默认浏览器
					if (hitType == HitTestResult.SRC_ANCHOR_TYPE||hitType == HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
//							 Intent i = new Intent(Intent.ACTION_VIEW);
//							 i.setData(Uri.parse(url));
//							 startActivity(i);
						//转到activity播放
//						Intent intent=new Intent(getActivity(),WebViewActivity.class);
//						intent.putExtra("data", url);
//						getActivity().startActivity(intent);


					}else{
						// view.loadUrl(url);
					}
				}
				return true;
			}
		});



		wv.loadDataWithBaseURL(null, itemPoint, "text/html", "utf-8", null);
		initWebView(wv);
		//题干显示完成
//		//该显示选项了
		loadOption();
		//选项显示完成
	}
	private void initWebView(WebView wv){
		wv.getSettings().setBlockNetworkImage(false);//解决图片不显示
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
	//显示选项
	@SuppressWarnings("unused")
	private void loadOption(){

		LinearLayout ll=this.getViewById(mView, R.id.include_linear_left_middle);//横排
		if(ll.getChildCount()>0){
			ll.removeAllViews();
		}
		List<AnswerEntity> ansList=this.testEntity.getAnswerList();
		ansList=ansList==null?new ArrayList<AnswerEntity>():ansList;
		//除了填空和操作（嵌套不考虑）,其他都是有可选项的
//		if((ansList==null || ansList.size()==0) && !testEntity.getTestType().equals(GlobalVarDefine.TESTTYPE_FILL_BLANK)  && !testEntity.getTestType().equals(GlobalVarDefine.TESTTYPE_OPRATE)){
//			comm.toast("没有答案可做");
//			return;
//		}
		if(this.testEntity==null){
			XSYTools.showToastmsg(getActivity(),"没有试题可做");
			return;
		}
		//分析答案，获取小题信息
		for(AnswerEntity ans:ansList){
			//获取试题信息，然后再根据试题类型初始化出不同的效果
			String ansVal=ans.getOptionAnswer();
			//得到选项，要传的只
			// options=ans.getOptionTitle();
			if(XSYTools.isEmpty(ansVal)){
				continue;
			}
			try {
				//对于嵌套题，答案实体中的optionTitle和id是没有意义的，只有optionAnser中的json数据是有意义的，当然optionTitle的意义在于排序，显示顺序
				JSONArray a=new JSONArray(ansVal);
				if(a==null || a.length()==0){
					continue;
				}
				JSONObject o=a.getJSONObject(0);
				if(o==null){
					continue;
				}
				//得到id
				String sId=XSYTools.getStringValFromJSONObject(o, "id");
				preViewTestHandler.sendMessage(XSYTools.makeNewMessage(Common.GETINCLUDE_ITEM_TEST,sId));

				//到这里，基本就告一段落了，剩下的工作交给回调调用的函数部分完成 函数名:doInitAnsInfo;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 供回调调用的函数，用来真正初始化可选项选区
	 * @param te
	 */
	public void doInitAnsInfo(TestEntity te){

		if(te==null){
			return;
		}
		///下面这段主要是为了防止同一试题多次出现，因为服务器端为了保证客户端能最大可能收到试题，会发送多次同一试题的广播
		boolean t=false;
		List<View> views=XSYTools.getAllChildViews(this.getActivity());
		for(View view:views){
			int i=1;
			if(view instanceof IncludeLinearLayout){
				IncludeLinearLayout sb=(IncludeLinearLayout) view;
				if(sb.getTestId().equals(te.getTestId())){
					t=true;
					break;
				}
			}
		}
		if(t){
			return;
		}
		if(te.getTestType().equals(GlobalVarDefine.TESTTYPE_MULTI_SELECT)){//多选题
			MultiSelectLayout msl=new MultiSelectLayout(this.getActivity(),this);
			msl.setWeightSum(2);
			LinearLayout ll=this.getViewById(mView, R.id.include_linear_left_middle);//横排
			if(ll==null){
				return;
			}
			ll.addView(msl);
			msl.initItemPoint(te,options);
			msl.initOptions(te.getAnswerList());
			this.addMslToList(msl);
		}else if(te.getTestType().equals(GlobalVarDefine.TESTTYPE_SINGLE_SELECT)){//单选题
			SingleSelectLayout ssl=new SingleSelectLayout(this.getActivity(),this);
			ssl.setWeightSum(2);
			LinearLayout ll=this.getViewById(mView, R.id.include_linear_left_middle);//横排
			if(ll==null){
				return;
			}
			ll.addView(ssl);
			try {
				ssl.initItemPoint(te,options);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			ssl.initOptions(te.getAnswerList());
			this.addSslToList(ssl);
		}else if(te.getTestType().equals(GlobalVarDefine.TESTTYPE_YESORNO_SELECT)){//判断题
			YesOrNoSelectLayout ssl=new YesOrNoSelectLayout(this.getActivity(),this);
			ssl.setWeightSum(2);
			LinearLayout ll=this.getViewById(mView, R.id.include_linear_left_middle);//横排
			if(ll==null){
				return;
			}
			ll.addView(ssl);
			ssl.initItemPoint(te,options);
			ssl.initOptions(te.getAnswerList());
			this.addYonslToList(ssl);
		}else if(te.getTestType().equals(GlobalVarDefine.TESTTYPE_FILL_BLANK)){//填空题
			FillBlankLayout fbl=new FillBlankLayout(this.getActivity(),this,mView);
			fbl.setWeightSum(2);
			LinearLayout ll=this.getViewById(mView, R.id.include_linear_left_middle);//横排
			if(ll==null){
				return;
			}

			ll.addView(fbl);

			fbl.initItemPoint(te,options);
			try {
				fbl.initOptions(te);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			this.addFblToList(fbl);
		}
		options=options+1;

	}
	public void clearAnswerMap(){
		this.getAnswerMap().clear();
	}
	//返回答按
	public Map<String, String> getAnswerMap() {
		return answerMap;
	}
	public void fillAnswerToMap(String id,String value) {
		Log.d("fillAnswer", "添加试题答案@"+id+"  "+value);
		this.getAnswerMap().put(id, value);
	}
	public void removeAnswerToMap(String id) {
		Log.d("fillAnswer", "移除试题答案@"+id);
		this.getAnswerMap().remove(id);
	}
	public Integer get() {
		return itemType;
	}


	//拿到装各种布局的父布局
	public LinearLayout getlll(){
		return (LinearLayout) mView.findViewById(R.id.include_linear_left_middle);
	}




	public TestEntity getTestEntity() {
		return testEntity;
	}
	public Integer getItemType() {
		// TODO Auto-generated method stub
		return itemType;
	}
	public List<MultiSelectLayout> getMslList() {
		return mslList;
	}
	public void addMslToList(MultiSelectLayout msl) {
		getMslList().add(msl);
	}
	public List<SingleSelectLayout> getSslList() {
		return sslList;
	}
	public void addSslToList(SingleSelectLayout ssl) {
		getSslList().add(ssl);
	}
	public List<YesOrNoSelectLayout> getYonslList() {
		return yonslList;
	}
	public void addYonslToList(YesOrNoSelectLayout ssl) {
		getYonslList().add(ssl);
	}
	public List<FillBlankLayout> getFblList() {
		return fblList;
	}
	public void addFblToList(FillBlankLayout fbl) {
		getFblList().add(fbl);
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


	public void setInfo(TestEntity testEntity, PreViewTestHandler preViewTestHandler) {
		this.testEntity=testEntity;
		this.preViewTestHandler=preViewTestHandler;
	}
}
