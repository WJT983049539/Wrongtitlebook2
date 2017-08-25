package xinshuyuan.com.wrongtitlebook.Model.Common;

import android.webkit.JavascriptInterface;

/**
 * 填空题 webview中点击‘空’调用该回调，在外部显示出具体的‘空’以及初始化相应逻辑-
 * 该函数暂时停用
 *
 */

public class FillBlankJSInterface {

    @JavascriptInterface
    public void showBlank(String type,String id,String val){
//		val=val==null?"":val;
//		id=id==null?"":id;
//		type=type==null?"":type;
//		comm.toast(type+"   "+id+"   "+val);
//		Handler handler=XsyApplication.getCurrentHandler();
//		if(handler!=null && handler instanceof MainHandler){
//			JSONObject o=null;
//			try {
//				o=new JSONObject();
//				o.put(VarDefine.TESTTYPE, type);
//				o.put(VarDefine.TESTVIEWID, id);
//				o.put(VarDefine.TESTVIEWVALUE, val);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//			if(o==null){
//				comm.toast("参数初始化错误");
//				return;
//			}else{
//				Message msg=new Message();
//				msg.what=VarDefine.UPDATE_FILLBLANK;
//				msg.obj=o;
//				handler.sendMessage(msg);
//			}
//		}else{
//			comm.toast("无法在非答题页面展现试题");
//		}
    }

}
