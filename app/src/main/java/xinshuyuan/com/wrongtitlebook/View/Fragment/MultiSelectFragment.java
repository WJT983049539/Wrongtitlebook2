package xinshuyuan.com.wrongtitlebook.View.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.AnswerEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.Model.CustomView.SelectButton;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * 对选题的fragment
 * @author wjt
 *
 */
public class MultiSelectFragment extends BaseFragment {

	private ScrollView multiScroview;
	private Handler handler;
	private Integer itemType;
	private TestEntity testEntity=null;
	private View mView;
	private Map<String, String> answerMap=new HashMap<String,String>();
	private Document document;
	private Map<Long,Integer> indexMap=new LinkedHashMap<Long,Integer>();
	private Integer scrollLen;
	
	private Runnable runnable = new Runnable() {
	    @Override
	    public void run() {  
	    	multiScroview.scrollTo(0,scrollLen);// 改变滚动条的位置  
	    }  
	};
	
	public MultiSelectFragment(){}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		mView= inflater.inflate(R.layout.layout_multiselect, container,false);
		try {
			initTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mView;
	}
	



	private void initTest() throws JSONException {
		//赋值正常，继续
		//集中式和分布式的区别是：1、集中式不显示题干，仅显示可选项或答案填写区，分布式则得显示题干；
		//2、集中式无法切换到下一题或上一题，全靠服务器推送，分布式可自主选择题目
		String itemPoint=testEntity.getPoint();
		document = Jsoup.parse(itemPoint);
		itemPoint=document.html();
		itemPoint="<span style=\"color:red;font-weight:bold;\">[多选题]</span>"+itemPoint;
		Log.d("html", itemPoint);
		//设置题干显示相关属性，以及相关node的格式化操作
		WebView wv=this.getViewById(mView, R.id.multiselect_itemPoint);

		// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
	   wv.setWebViewClient(new WebViewClient(){
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
			return true;
		}
	});



		wv.loadDataWithBaseURL(null, itemPoint, "text/html", "utf-8", null);
		//设置字体
		wv.getSettings().setDefaultFontSize(25);
		initWebView(wv);
		loadOption();
		//题干显示完成
		//该显示选项了
	}
	private void initWebView(WebView wv){
		wv.getSettings().setBlockNetworkImage(false);//解决图片不显示
		wv.getSettings().setJavaScriptEnabled(true);
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
	private void loadOption() throws JSONException {
		LinearLayout ll=this.getViewById(mView, R.id.multiselect_blank_container);//横排
		Log.d("viewCount", String.valueOf(ll.getChildCount()));
		if(ll.getChildCount()>0){
			ll.removeAllViews();
		}
		List<AnswerEntity> ansList=this.testEntity.getAnswerList();
		if(ansList==null || ansList.size()==0){
			XSYTools.showToastmsg(getActivity(),"没有答案");
			return;
		}
		for(AnswerEntity ans:ansList){
			Long answerId = (long) 0;
			//弄一些竖排的linearLayout来显示选项
			LinearLayout subll=new LinearLayout(this.getActivity());
			subll.setOrientation(LinearLayout.HORIZONTAL);
			//选项分两部分，一部分是选项标题（如A,B,C,D），一部分是答案，选项标题用button实现，答案用webview实现
			//就这么愉快的定了
			//左右放置 textview 和屏幕隔开
			TextView text=new TextView(this.getActivity());
			text.setWidth(XSYTools.dip2px(this.getActivity(), 30));
			
			SelectButton btn=new SelectButton(this.getActivity());
			btn.setText(ans.getOptionTitle());
			btn.setOptionId(ans.getId());
			btn.setBackgroundResource(R.drawable.sing_startbuttonbackground);
			btn.setWidth(XSYTools.dip2px(this.getActivity(), 45));
			btn.setHeight(XSYTools.dip2px(this.getActivity(), 45));
			//初始化事件,稍后再做
			//绑定事件
//			btn.setOnClickListener(new MultiSelectOptionClickListener(this));
			WebView ansWv=new WebView(this.getActivity());
			//设置字体
			ansWv.getSettings().setDefaultFontSize(19);
			initWebView(ansWv);
			String ansString=ans.getOptionAnswer();
			ansString=ansString.toLowerCase().trim().replace("<br />", "&nbsp;").replace("<br/>", "&nbsp;").replace("<br>", "&nbsp;");
			ansWv.loadDataWithBaseURL(null, ansString,"text/html", "utf-8", null);
			
			//初始化事件,稍后再做
			
			
			TextView text3=new TextView(this.getActivity());
			text3.setWidth(XSYTools.dip2px(this.getActivity(), 15));
			//如果做了题找出那个选项，遍历试题id
			//得到答案id集合
//			List<Long> ansidList =AnswerStore.keylist();
//			for(int ii=0;ii<ansidList.size();ii++){
//				Long testIddd=ansidList.get(ii);
//				//如果试题id相同，说明有答案
//				if(this.testEntity.getTestId().equals(testIddd)){
//					String ansjson=AnswerStore.getAllTestanswerMap().get(testIddd);
//					//得到jsonshuju
//					JSONObject a = new JSONObject(ansjson);
//					JSONArray array=a.getJSONArray("answers");
//				     for(int j=0;j<array.length();j++){
//				    	 String ss=array.getJSONObject(j).getString("id");
//				    	 ss=ss.replace(GlobalVarDefine.SUBMIT_TEST_ID_PREFIX,"");
//				    	 ss=ss.trim();
//				    	 //得到那个已经做了的选项id
//				    	 answerId= Long.valueOf(ss);
//				    	 //如果选项id相同则放入状态和
//				    	 if(answerId.equals(btn.getOptionId())){
//								btn.setBackgroundResource(R.drawable.multi_select_option_button_shape_selected);
//								btn.setState(btn.getState()+1);
//								fillAnswerToMap(btn.getOptionId().toString(), "");
//
//
//				    	 }
//				     }
//				}
//			}
			
			//没有事件
			subll.addView(text);
			subll.addView(btn);
			subll.addView(text3);
			subll.addView(ansWv);
			ll.addView(subll);
			//上下放置textview把他们隔开
			TextView text2=new TextView(this.getActivity());
			text2.setHeight(XSYTools.dip2px(this.getActivity(), 10));
			ll.addView(text2);
		}
	}
	public void clearAnswerMap(){
		this.getAnswerMap().clear();
	}
	//返回答按
	public Map<String, String> getAnswerMap() {
		return answerMap;
	}
	public void fillAnswerToMap(String id, String value) {
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
	public TestEntity getTestEntity() {
		return testEntity;
	}
	public Integer getItemType() {
		return itemType;
	}
     //放入数据
	public void setInfo(TestEntity testEntity) {
		this.testEntity=testEntity;

	}
}
