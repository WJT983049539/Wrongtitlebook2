//<editor-fold desc="Description">
package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ibpd.xsy.varDefine.WrongQuestionConstantClass;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.GetUserInfoClass;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;
import xinshuyuan.com.wrongtitlebook.Model.Common.MenuInfo;
import xinshuyuan.com.wrongtitlebook.Model.Common.Subjectbean;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * 选择条件
 * Created by Administrator on 2017/6/16.
 */

public class SelscCondictiontActivity extends Activity{
    Map<String,String> xuekeMap;
    //储存教材 id和名称的集合
    private Map<String,String> Bookmap;
    //储存上下册 id和名称的集合
    private Map<String,String> Fascimap;
    private Spinner  spinner_subject;
    private Spinner  spinner_jiaocai;
    private Spinner  spinner_fence;
    private ListView selsct_list_know_one;
    private ListView selsct_list_know_two;

    //储存一级菜单数据的集合
    private List<MenuInfo> oneMenulist;
    //储存二级菜单数据的集合
    private List<MenuInfo> twoMenulist;
    //选中的学科
    private String SubjectId;
    //选中的教材版本
    private String selectBookId;
    //选中的上下册
    private String selectSXC;
    //最终选择知识点内容
    private String finalKnowContent;
    //最终选择知识点id
   private String finalKnowID;
    //学科适配器
    private ArrayAdapter<String> aadapter;
    //教材适配器
    private ArrayAdapter<String> aadapter2;
    //分册适配器
    private ArrayAdapter<String> aadapter3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window=getWindow();
        WindowManager.LayoutParams layoutParams= window.getAttributes();
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏底部键盘，一直不会弹出
        layoutParams.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(layoutParams);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.selectactivity_layout);
        inint();
    }

    private void inint() {
        xuekeMap=new HashMap<String, String>();
        Fascimap=new HashMap<String, String>();
        Bookmap=new HashMap<String, String>();
        oneMenulist=new ArrayList<MenuInfo>();
        twoMenulist=new ArrayList<MenuInfo>();
        spinner_subject= (Spinner) findViewById(R.id.spinner_subject);
        spinner_jiaocai= (Spinner) findViewById(R.id.spinner_jiaocai);
        spinner_fence= (Spinner) findViewById(R.id.spinner_fence);
        selsct_list_know_one= (ListView) findViewById(R.id.selsct_list_know_one);
        selsct_list_know_two= (ListView) findViewById(R.id.selsct_list_know_two);
        aadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        aadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aadapter2=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        aadapter2.setDropDownViewResource(android.R.layout.select_dialog_item);

        aadapter3=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        aadapter3.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        //获取数据
        getSubjectDate();
    }

    /**
     * 获取学科数据
     */
    private void getSubjectDate() {
        String getSubjectUrl= XSYTools.getWrongUrl(WrongQuestionConstantClass.SUBJECT_URL,SelscCondictiontActivity.this);
        XsyMap map= XsyMap.getInterface();
        Long studId= new GetUserInfoClass(SelscCondictiontActivity.this).getUserId();
        map.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
        OkGo.post(getSubjectUrl).params(map).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i("选择学科列表="+s);
                ArrayList<Subjectbean> list2=new ArrayList<Subjectbean>();
                if(list2.size()>0){ list2.clear();}
                aadapter.clear();
                aadapter2.clear();
                aadapter3.clear();

                JSONObject object= null;
                try {
                    object = new JSONObject(s);

                String jaonarray=object.getString("subject");
                JSONArray array=new JSONArray(jaonarray);
                for(int i=0;i<array.length();i++){
                    Subjectbean subjectbean=new Subjectbean();
                    String subjectName=new JSONObject(array.get(i).toString()).getString("subjectName");
                    String subjectId=String.valueOf(new JSONObject(array.get(i).toString()).getString("id"));
                    subjectbean.setSubject(subjectName);
                    subjectbean.setSubjectId(subjectId);
                    list2.add(subjectbean);
                    xuekeMap.put(subjectName,subjectId);
                    aadapter.add(subjectName);
                }
                    spinner_subject.setAdapter(aadapter);

                    spinner_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            SubjectId =xuekeMap.get(aadapter.getItem(position));

                                    getjiaocaidata(SubjectId);


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });







                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                XSYTools.i("获取科目失败！！！");
            }
        });


    }

    /**
     * 得到教材
     * @param subjectId
     */
    private void getjiaocaidata(String subjectId) {

        String getBookUrl= XSYTools.getWrongUrl(WrongQuestionConstantClass.BOOK_URL,SelscCondictiontActivity.this);
        XsyMap map=XsyMap.getInterface();
        Long studId= new GetUserInfoClass(SelscCondictiontActivity.this).getUserId();
        map.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
        map.put(WrongQuestionConstantClass.PARAM_SUBJECTID, subjectId);
        OkGo.post(getBookUrl).params(map).execute(new StringCallback() {

            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i(s);
                aadapter2.clear();
                aadapter3.clear();

                if(s.equals("[]")){
                    oneMenulist.clear();
                    OneMenulistadapter oneMenulistadapter=new OneMenulistadapter(oneMenulist);
                    selsct_list_know_one.setAdapter(oneMenulistadapter);

                    twoMenulist.clear();
                    OneMenulistadapter oneMenulistadapter2=new OneMenulistadapter(oneMenulist);
                    selsct_list_know_two.setAdapter(oneMenulistadapter2);
                }

                try {
                    JSONArray array=new JSONArray(s);
                    for(int i=0;i<array.length();i++){
                        JSONObject jsonObject=new JSONObject(array.get(i).toString());
                        //得到教材ID
                        String BookId=jsonObject.getString("id");
                        //得到教材名字
                        String BookName=jsonObject.getString("name");
                        aadapter2.add(BookName);
                        Bookmap.put(BookName,BookId);

                    }
                    spinner_jiaocai.setAdapter(aadapter2);
                    spinner_jiaocai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectBookId =Bookmap.get( aadapter2.getItem(position));
                            aadapter3.clear();
                             //得上下册
                            String getBookSelectUrl= XSYTools.getWrongUrl(WrongQuestionConstantClass.BOOKSECTION_URL,SelscCondictiontActivity.this);
                            XsyMap map=XsyMap.getInterface();
                            Long studId= new GetUserInfoClass(SelscCondictiontActivity.this).getUserId();
                            map.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
                            map.put(WrongQuestionConstantClass.PARAM_BOOKID, selectBookId);
                            OkGo.post(getBookSelectUrl).params(map).execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    XSYTools.i(s);

                                    if(s.equals("[]")){
                                        oneMenulist.clear();
                                        OneMenulistadapter oneMenulistadapter=new OneMenulistadapter(oneMenulist);
                                        selsct_list_know_one.setAdapter(oneMenulistadapter);

                                        twoMenulist.clear();
                                        OneMenulistadapter oneMenulistadapter2=new OneMenulistadapter(oneMenulist);
                                        selsct_list_know_two.setAdapter(oneMenulistadapter2);
                                    }

                                    JSONArray array= null;
                                    try {
                                        array = new JSONArray(s);

                                    for(int i=0;i<array.length();i++){
                                        JSONObject object=new JSONObject(array.get(i).toString());
                                        String FasciId=String.valueOf(object.get("id"));
                                        String FasciName=object.getString("name");
                                        //放入集合
                                        Fascimap.put(FasciName,FasciId);
                                        aadapter3.add(FasciName);
                                    }

                                        spinner_fence.setAdapter(aadapter3);

                                        spinner_fence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                selectSXC = Fascimap.get(aadapter3.getItem(position));

                                                //一级列表url
                                                String getoneListUrl = XSYTools.getWrongUrl(WrongQuestionConstantClass.KNOWLEDGEPOINT_URL,SelscCondictiontActivity.this);
                                                XsyMap map = XsyMap.getInterface();
                                                Long studId= new GetUserInfoClass(SelscCondictiontActivity.this).getUserId();
                                                map.put(WrongQuestionConstantClass.PARAM_STUDID, String.valueOf(studId));
                                                map.put(WrongQuestionConstantClass.PARAM_BOOKSECTIONID, selectSXC);

                                                    OkGo.post(getoneListUrl).params(map).execute(new StringCallback() {
                                                        @Override
                                                        public void onSuccess(String s, Call call, Response response) {
                                                                XSYTools.i(s);
                                                            if(s.equals("[]")){
                                                                oneMenulist.clear();
                                                                OneMenulistadapter oneMenulistadapter=new OneMenulistadapter(oneMenulist);
                                                                selsct_list_know_one.setAdapter(oneMenulistadapter);

                                                                twoMenulist.clear();
                                                                OneMenulistadapter oneMenulistadapter2=new OneMenulistadapter(oneMenulist);
                                                                selsct_list_know_two.setAdapter(oneMenulistadapter2);
                                                            }


                                                                if(oneMenulist.size()>0){ oneMenulist.clear();}


                                                            try {
                                                            JSONArray array= null;

                                                                array = new JSONArray(s);

                                                            for(int i=0;i<array.length();i++){

                                                                JSONObject object=new JSONObject(array.get(i).toString());
                                                                //得到父知识点的id
                                                                String parentKnowId=String.valueOf(object.get("id"));
                                                                //得到父知识点的内容
                                                                String Content=object.getString("text");
                                                                MenuInfo menuInfo=new MenuInfo();
                                                                menuInfo.setId(parentKnowId);
                                                                menuInfo.setContent(Content);
                                                                //把一级菜单数据全部装到集合
                                                                oneMenulist.add(menuInfo);

                                                                OneMenulistadapter oneMenulistadapter=new OneMenulistadapter(oneMenulist);
                                                                selsct_list_know_one.setAdapter(oneMenulistadapter);
                                                                selsct_list_know_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                    @Override
                                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                        final String TwoMenuID=oneMenulist.get(position).getId();
                                                                        final String TWOContent= oneMenulist.get(position).getContent();
                                                                        String detailedUrl=XSYTools.getWrongUrl(WrongQuestionConstantClass.KNOWLEDGEPOINT_URL,SelscCondictiontActivity.this);
                                                                        XsyMap map=XsyMap.getInterface();
                                                                        //学生id
                                                                        Long studId= new GetUserInfoClass(SelscCondictiontActivity.this).getUserId();
                                                                        map.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
                                                                        //分册id
                                                                        map.put(WrongQuestionConstantClass.PARAM_BOOKSECTIONID,selectSXC);
                                                                        //知识点id
                                                                        map.put(WrongQuestionConstantClass.PARAM_KNOWLEDGEID,TwoMenuID);

                                                                        OkGo.post(detailedUrl).params(map).execute(new StringCallback() {
                                                                            @Override
                                                                            public void onSuccess(String s, Call call, Response response) {

                                                                                XSYTools.i(s);
                                                                                //如果二级知识点没有值

                                                                                if(s.equals("[]")){

                                                                                    XSYTools.i("没有值了");

                                                                                    finalKnowID=TwoMenuID;
                                                                                    finalKnowContent=TWOContent;
                                                                                    Intent intent=new Intent(SelscCondictiontActivity.this,SelectProjectUpPaper.class);
                                                                                    intent.putExtra("finalKnowID",finalKnowID);
                                                                                    intent.putExtra("finalKnowContent",finalKnowContent);
                                                                                    intent.putExtra("finalSubjectID",SubjectId);
                                                                                    intent.putExtra("finalJiaocaiID",selectBookId);
                                                                                    intent.putExtra("finalfenceID",selectSXC);

                                                                                    setResult(6,intent);
                                                                                    finish();

                                                                                }else{
                                                                                if(twoMenulist.size()>0){  twoMenulist.clear();}

                                                                                try {
                                                                                    JSONArray array = null;

                                                                                    array = new JSONArray(s);

                                                                                    for (int i = 0; i < array.length(); i++) {

                                                                                        JSONObject object = new JSONObject(array.get(i).toString());
                                                                                        //得到父知识点的id
                                                                                        String parentKnowId = String.valueOf(object.get("id"));
                                                                                        //得到父知识点的内容
                                                                                        String Content = object.getString("text");
                                                                                        MenuInfo menuInfo = new MenuInfo();
                                                                                        menuInfo.setId(parentKnowId);
                                                                                        menuInfo.setContent(Content);
                                                                                        //把一级菜单数据全部装到集合
                                                                                        twoMenulist.add(menuInfo);
                                                                                        OneMenulistadapter oneMenulistadapter = new OneMenulistadapter(twoMenulist);
                                                                                        selsct_list_know_two.setAdapter(oneMenulistadapter);
                                                                                        selsct_list_know_two.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                                            @Override
                                                                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                                                finalKnowContent = twoMenulist.get(position).getContent();
                                                                                                finalKnowID = twoMenulist.get(position).getId();

                                                                                                Intent intent=new Intent(SelscCondictiontActivity.this,SelectProjectUpPaper.class);
                                                                                                intent.putExtra("finalKnowID",finalKnowID);
                                                                                                intent.putExtra("finalKnowContent",finalKnowContent);
                                                                                                setResult(6,intent);
                                                                                                finish();

                                                                                            }
                                                                                        });
                                                                                    }
                                                                                }catch (Exception e){
                                                                                    e.printStackTrace();
                                                                                }

                                                                            }}
                                                                        });
                                                                    }
                                                                });


                                                            }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        @Override
                                                        public void onError(Call call, Response response, Exception e) {
                                                            super.onError(call, response, e);
                                                            XSYTools.i("获取一级知识点失败");
                                                        }
                                                    });


                                            }
                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                XSYTools.i("获取教材版本失败！！！");
            }
        });
    }

    //一级菜单适配器
    private class OneMenulistadapter extends BaseAdapter {

        private List<MenuInfo> oneMenulist;
        public OneMenulistadapter(List<MenuInfo> oneMenulist) {

            this.oneMenulist=oneMenulist;

        }

        @Override
        public int getCount() {
            return oneMenulist.size();
        }

        @Override
        public Object getItem(int position) {
            return oneMenulist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            LayoutInflater inflater=LayoutInflater.from(SelscCondictiontActivity.this);
            if(convertView==null){
                convertView=inflater.inflate(R.layout.onemenulayout,null);
                holder= new ViewHolder();
                holder.textview= (TextView) convertView.findViewById(R.id.menu_one_item);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            String pp= oneMenulist.get(position).getContent();
            if(pp!=null){
                holder.textview.setText(pp);
            }else {
                holder.textview.setText("");
            }


            return convertView;
        }
    }

    public class ViewHolder {
        public TextView textview;

    }
}
//</editor-fold>
