package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
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

import com.ibpd.xsy.varDefine.TestVarDefine;
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
import xinshuyuan.com.wrongtitlebook.Model.Common.BookInfo;
import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.MenuInfo;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.PriviewHanaler;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * 检索条件的Activity
 * Created by Administrator on 2017/5/24.
 */
public class CheckActivity extends Activity{
    //已经选择的教材id
    private  String SubjectId;
    //已经选择的学科id
    private String BookId;
    //已经选择的难度
    private String  different;
    //难度参数
   private  Float ff;
    //时间排序
    private String time_order;
    private Integer time_int_order;
    //正序，倒序
    private  String DC_Order;
    private  Integer Int_DC_Order;

    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private Spinner spinner4;
    private Spinner spinner5;

    private ListView listone_know;
    private ListView listtwo_know;
    //上下册保存到全局变量
    private  String bookSectionId;

    //储存一级菜单数据的集合
    private List<MenuInfo> oneMenulist=new ArrayList<MenuInfo>();
    //储存二级菜单数据的集合
    private List<MenuInfo> twoMenulist=new ArrayList<MenuInfo>();
    //储存三级菜单数据集合

    private String Project;
    //    private MyAdapter adapter;
    //储存教材 id和名称的集合
    private Map<String,String> Bookmap=new HashMap<String, String>();
    //储存 分册 id和名称的集合
    private Map<String,String> Fascimap=new HashMap<String, String>();


    //教材适配器
    private ArrayAdapter<String> aadapter;
    //分册适配器
    private ArrayAdapter<String> aadapter2;
    //难度适配器
    private ArrayAdapter<String> aadapter3;
    //时间适配器
    private ArrayAdapter<String> aadapter4;
    //排序适配器
    private ArrayAdapter<String> aadapter5;




    //难度下拉列表
    private static final String[] diffcutly = {"简单","容易","一般","较难","难"};
    //时间下拉列表
    private static final String[] timetOrder = {"今天","昨天","一周之内","一月之内","全部"};
    //排序下拉列表
    private static final String[] Order = {"时间降序","时间倒序","难度降序","难度倒序"};
    //    private List<String> allCountries;
    private List<BookInfo> bookinfoList;

    private PriviewHanaler privuewHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window=getWindow();
        WindowManager.LayoutParams layoutParams= window.getAttributes();
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏底部键盘，一直不会弹出
        layoutParams.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(layoutParams);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        privuewHandler=new PriviewHanaler(this);
        Intent intent=getIntent();
        setContentView(R.layout.checklayout);
        Project=intent.getStringExtra("project");
        init();


    }

       private void init() {
//           allCountries=new ArrayList<String>();
           //储存得到的教材信息
           bookinfoList=new ArrayList<BookInfo>();
           aadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
           aadapter3=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,diffcutly);
           aadapter4=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,timetOrder);
           aadapter5=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,Order);
           getData();
           spinner1= (Spinner) findViewById(R.id.spinner1);
           spinner2= (Spinner) findViewById(R.id.spinner2);
           spinner3= (Spinner) findViewById(R.id.spinner3);
           spinner4= (Spinner) findViewById(R.id.spinner4);
           spinner5= (Spinner) findViewById(R.id.spinner5);

           spinner5.setAdapter(aadapter5);
           spinner4.setAdapter(aadapter4);
           spinner3.setAdapter(aadapter3);
            //难易度
           spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   different=aadapter3.getItem(position);
               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {
               }
           });
           //时间排序
           spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   time_order=aadapter4.getItem(position);               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {

               }
           });

           //正序，倒序排序
           spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   DC_Order=aadapter5.getItem(position);
               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {

               }
           });



           listone_know=(ListView)findViewById(R.id.list_know_one);
           listtwo_know=(ListView)findViewById(R.id.list_know_two);

           spinner1.setAdapter(aadapter);
           spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                   BookId=Bookmap.get(aadapter.getItem(position));
                   getBookSectiondata(BookId);

               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {
                   XSYTools.i("sda");
               }
           });

    }
    //得到分册信息
    private void getBookSectiondata(final String bookId) {
        String getBookSelectUrl= XSYTools.getWrongUrl(WrongQuestionConstantClass.BOOKSECTION_URL,CheckActivity.this);
        XsyMap map=XsyMap.getInterface();
        Long studId= new GetUserInfoClass(CheckActivity.this).getUserId();
        map.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
        map.put(WrongQuestionConstantClass.PARAM_BOOKID, bookId);
        OkGo.post(getBookSelectUrl).params(map).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i(s);
                aadapter2 = new ArrayAdapter<String>(CheckActivity.this, android.R.layout.simple_spinner_item);
                try {
                    JSONArray array=new JSONArray(s);
                    for(int i=0;i<array.length();i++){
                        JSONObject object=new JSONObject(array.get(i).toString());
                         String FasciId=String.valueOf(object.get("id"));
                         String FasciName=object.getString("name");
                        //放入集合
                         Fascimap.put(FasciName,FasciId);
                         aadapter2.add(FasciName);
                    }

                    spinner2.setAdapter(aadapter2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, final long id) {

                            XSYTools.i(aadapter2.getItem(position));
                            //这里选择了上下册，开始显示上下列表
//                            String BookId=Bookmap.get(aadapter.getItem(position));
                            //得到教材上下册id,通过上下册id获取一级知识点
                            bookSectionId= Fascimap.get(aadapter2.getItem(position));
                            //一级列表url
                            String getoneListUrl= XSYTools.getWrongUrl(WrongQuestionConstantClass.KNOWLEDGEPOINT_URL,CheckActivity.this);
                            XsyMap map=XsyMap.getInterface();
                            Long studId= new GetUserInfoClass(CheckActivity.this).getUserId();
                            map.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
                            map.put(WrongQuestionConstantClass.PARAM_BOOKSECTIONID, bookSectionId);

                            OkGo.post(getoneListUrl).params(map).execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    XSYTools.i(s);
                                    oneMenulist.clear();
                                    try {
                                        JSONArray array=new JSONArray(s);
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

                                        }

                                        OneMenulistadapter oneMenulistadapter=new OneMenulistadapter(oneMenulist);

                                        listone_know.setAdapter(oneMenulistadapter);


                                        listone_know.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                //一级菜单的知识点id
                                                String knowledgePointId=oneMenulist.get(position).getId();

                                                String TwomenuknowUrl=XSYTools.getWrongUrl(WrongQuestionConstantClass.KNOWLEDGEPOINT_URL,CheckActivity.this);
                                                XsyMap map=XsyMap.getInterface();
                                                Long studId= new GetUserInfoClass(CheckActivity.this).getUserId();
                                                map.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
                                                map.put(WrongQuestionConstantClass.PARAM_BOOKSECTIONID, bookSectionId);
                                                map.put(WrongQuestionConstantClass.PARAM_KNOWLEDGEID,knowledgePointId);
                                                OkGo.post(TwomenuknowUrl).params(map).execute(new StringCallback() {
                                                    @Override
                                                    public void onSuccess(String s, Call call, Response response) {
                                                        XSYTools.i(s);
                                                        twoMenulist.clear();
                                                        try {
                                                            JSONArray array=new JSONArray(s);
                                                            for(int i=0;i<array.length();i++){

                                                                JSONObject object=new JSONObject(array.get(i).toString());
                                                                //二级知识点id
                                                                String twomenuknowId=object.getString("id");
                                                                String Content=object.getString("text");
                                                                MenuInfo twomenuinfo=new MenuInfo();
                                                                twomenuinfo.setContent(Content);
                                                                twomenuinfo.setId(twomenuknowId);
                                                                twoMenulist.add(twomenuinfo);
                                                            }
                                                            //公用一个适配器，显示二级菜单显示二级菜单显示二级菜单显示二级菜单
                                                            OneMenulistadapter twoMenulistadapter=new OneMenulistadapter(twoMenulist);
                                                            listtwo_know.setAdapter(twoMenulistadapter);

                                                            //设置二级知识点菜单的监听事件
                                                            listtwo_know.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                    //得到二级菜单的id
                                                                    String TwoMenuID=twoMenulist.get(position).getId();
                                                                    //获取详细试题的url
                                                                    String detailedUrl=XSYTools.getWrongUrl(WrongQuestionConstantClass.LIST_URL,CheckActivity.this);
                                                                    XsyMap map=XsyMap.getInterface();
                                                                     //学生id
                                                                    Long studId= new GetUserInfoClass(CheckActivity.this).getUserId();
                                                                    map.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
                                                                    //学科id
                                                                    map.put(WrongQuestionConstantClass.PARAM_SUBJECTID,SubjectId);
                                                                    //教材id
                                                                    map.put(WrongQuestionConstantClass.PARAM_BOOKID,bookId);
                                                                    //分册id
                                                                    map.put(WrongQuestionConstantClass.PARAM_BOOKSECTIONID,bookSectionId);
                                                                    //知识点id
                                                                    map.put(WrongQuestionConstantClass.PARAM_KNOWLEDGEID,TwoMenuID);
                                                                    //难度
                                                                    if(different.equals("简单")){
                                                                       ff= TestVarDefine.TEST_DIFFICULTY_EAISER_GENERAL;
                                                                    }else if(different.equals("容易")){
                                                                        ff= TestVarDefine.TEST_DIFFICULTY_EASY_GENERAL;
                                                                    }else if(different.equals("一般")){
                                                                        ff= TestVarDefine.TEST_DIFFICULTY_GENERAL_GENERAL;
                                                                    }else if(different.equals("较难")){
                                                                        ff= TestVarDefine.TEST_DISCRIMINATION_SMALL_MAX;
                                                                    }else if(different.equals("难")){
                                                                        ff= TestVarDefine.TEST_DIFFICULTY_VERYDIFFICULTY_GENERAL;
                                                                    }

                                                                    map.put(WrongQuestionConstantClass.PARAM_DIFFICULT,String.valueOf(ff));
                                                                    //排序
                                                                    if(time_order.equals("今天")){

                                                                        time_int_order=WrongQuestionConstantClass.PARAM_TODAY;

                                                                    }else if(time_order.equals("昨天")){

                                                                        time_int_order=WrongQuestionConstantClass.PARAM_YESTODAY;
                                                                    }else if(time_order.equals("一周之内")){

                                                                        time_int_order=WrongQuestionConstantClass.PARAM_WEEK;
                                                                    }else if(time_order.equals("一月之内")){

                                                                        time_int_order=WrongQuestionConstantClass.PARAM_MONTH;
                                                                    }else if(time_order.equals("全部")){
                                                                        time_int_order=WrongQuestionConstantClass.PARAM_ALL;
                                                                    }
                                                                    map.put(WrongQuestionConstantClass.PARAM_TIMEAREA,String.valueOf(time_int_order));


                                                                    //看是正序还是倒序

                                                                    if(DC_Order.equals("时间降序")){

                                                                        Int_DC_Order=WrongQuestionConstantClass.ORDER_TYPE_TIME_DESC;
                                                                    }else if(DC_Order.equals("时间倒序")){
                                                                        Int_DC_Order=WrongQuestionConstantClass.ORDER_TYPE_TIME_ASC;
                                                                    }else if(DC_Order.equals("难度降序")){
                                                                        Int_DC_Order=WrongQuestionConstantClass.ORDER_TYPE_DIFFICULTY_DESC;
                                                                    }else if(DC_Order.equals("难度倒序")){
                                                                        Int_DC_Order=WrongQuestionConstantClass.ORDER_TYPE_DIFFICULTY_ASC;
                                                                    }
                                                                    map.put(WrongQuestionConstantClass.PARAM_DATE,String.valueOf(Int_DC_Order));


                                                                    OkGo.post(detailedUrl).params(map).execute(new StringCallback() {
                                                                        @Override
                                                                        public void onSuccess(String s, Call call, Response response) {
                                                                            XSYTools.i(s);
                                                                            privuewHandler.sendMessage(XSYTools.makeNewMessage(Common.SHOWWRONG_LIST,s));


//                                                                            //获取到详细试题的信息，跳转页面== ,转到展现试题的界面
//                                                                            Intent intent=new Intent(CheckActivity.this,PreviewTestQuestionActivity.class);
//                                                                            intent.putExtra("testQuestionInfo",s);
//                                                                            startActivity(intent);

                                                                            //{"answerAnalysis":"","answerCount":0,"bookId":3,"bookSectionId":5,"classId":1,"createTime":{"date":25,"day":4,"hours":18,"minutes":24,"month":4,"nanos":0,"seconds":58,"time":1495707898000,"timezoneOffset":-480,"year":117},"difficulty":0.9,"discrimination":0.15,"gradeId":1,"id":24,"knowledgePointId":2,"knowledgePointName":"\u500d\u7684\u8ba4\u8bc6\uff081\uff09","lastTime":{"date":25,"day":4,"hours":18,"minutes":56,"month":4,"nanos":0,"seconds":5,"time":1495709765000,"timezoneOffset":-480,"year":117},"problem":"3*6=","realAnswer":"","rightCount":0,"score":0,"sourceId":1,"sourceType":2,"stageId":1,"state":false,"studAnswer":"B   ","studentId":2,"subjectId":2,"testId":84,"type":1,"wrongCount":0,"answerList":[{"id":163,"isRealAnswer":true,"optionAnswer":"18<br/>","optionTitle":"A","order":0,"testId":84},{"id":164,"isRealAnswer":false,"optionAnswer":"21<br/>","optionTitle":"B","order":1,"testId":84}]}
                                                                            XSYTools.i(s);
                                                                        }

                                                                        @Override
                                                                        public void onError(Call call, Response response, Exception e) {
                                                                            super.onError(call, response, e);
                                                                        }
                                                                    });

                                                                }
                                                            });


                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }


                                                    }
                                                });
                                            }
                                        });


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
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
                XSYTools.i(e.toString());
            }
        });


    }

    //根据学科得到教材信息
    public void  getData() {
        //得到选中学科的ID
        SubjectId= Common.getMap().get(Project);
        String getBookUrl= XSYTools.getWrongUrl(WrongQuestionConstantClass.BOOK_URL,CheckActivity.this);
        XsyMap map=XsyMap.getInterface();
        Long studId= new GetUserInfoClass(CheckActivity.this).getUserId();
        map.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
        map.put(WrongQuestionConstantClass.PARAM_SUBJECTID, SubjectId);
        OkGo.post(getBookUrl).params(map).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i(s);
                try {
                    JSONArray array=new JSONArray(s);
                    for(int i=0;i<array.length();i++){
                        JSONObject jsonObject=new JSONObject(array.get(i).toString());
                        BookInfo bookInfo=new BookInfo();
                        //得到教材ID
                        String BookId=jsonObject.getString("id");
                        //得到教材名字
                        String BookName=jsonObject.getString("name");


                        aadapter.add(BookName);
                        Bookmap.put(BookName,BookId);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });

    }


    private class MyAdapter extends BaseAdapter{
        List<BookInfo> bookinfoList;
        public MyAdapter(List<BookInfo> bookinfoList) {
            this.bookinfoList=bookinfoList;
        }

        @Override
        public int getCount() {
            return bookinfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return bookinfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater _LayoutInflater=LayoutInflater.from(CheckActivity.this);
            convertView=_LayoutInflater.inflate(R.layout.item_book, null);
            if(convertView!=null) {
                TextView textView= (TextView) convertView.findViewById(R.id.book_text_item);
                textView.setText(bookinfoList.get(position).getBookName());
            }
            return convertView;
        }
    }



    //一级菜单适配器
    private class OneMenulistadapter extends BaseAdapter{

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
            LayoutInflater inflater=LayoutInflater.from(CheckActivity.this);
            if(convertView==null){
                convertView=inflater.inflate(R.layout.onemenulayout,null);
                holder=new ViewHolder();
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
