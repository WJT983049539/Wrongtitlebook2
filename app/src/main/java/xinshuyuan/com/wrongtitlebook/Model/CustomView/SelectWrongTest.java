package xinshuyuan.com.wrongtitlebook.Model.CustomView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import xinshuyuan.com.wrongtitlebook.Model.Common.BookInfo;
import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.MenuInfo;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.Model.FaciInfo;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.ShowWrongHandler;
import xinshuyuan.com.wrongtitlebook.R;

/**构造放在悬浮窗里面的view
 * Created by Administrator on 2017/6/9.
 */

public class SelectWrongTest {

    //储存教材版本 id和名称的集合
    private List<BookInfo> Bookbanbenlist=new LinkedList<BookInfo>();
    //储存教材分册的id和名称的集合
    private List<FaciInfo>  Facilist=new ArrayList<FaciInfo>();
    //储存一级菜单数据的集合
    private List<MenuInfo> oneMenulist=new ArrayList<MenuInfo>();
    //储存二级菜单数据的集合
    private List<MenuInfo> twoMenulist=new ArrayList<MenuInfo>();



     private ListView list_teacherbok;
     private ListView list_fascicle;
     private ListView list_oneknowloage;
     private ListView list_twoknowloage;

     private Context myContext;
     private String project;
     private ShowWrongHandler showWrongHandler;


     private MyAdapter adapter;
     //分册的适配器
     private  BookSectionAdapter bsAdapeter;
    //已经选择的教材id
    private  String SubjectId;
    //已经选择的教材版本
    private  String selectBookId;
    //已经选择的分册
    private String bookSection;
    //已经选择的一级知识点id
    private String oneKnowleageId;

    /**
     * 需要 context , 选择的学科 ，和Handler
     * @param context
     * @param project
     */
    public SelectWrongTest(Context context,String project,ShowWrongHandler showWrongHandler){
        this.myContext=context;
        this.project=project;
        this.showWrongHandler=showWrongHandler;
    }


    public View BuilderView (){
        LayoutInflater inflater=LayoutInflater.from(myContext);
        View mView=inflater.inflate(R.layout.show_wrong_list_layout,null);
        list_teacherbok= (ListView) mView.findViewById(R.id.list_teacherbok);
        list_fascicle= (ListView) mView.findViewById(R.id.list_fascicle);
        list_oneknowloage= (ListView) mView.findViewById(R.id.list_oneknowloage);
        list_twoknowloage= (ListView) mView.findViewById(R.id.list_twoknowloage);
        //得到选中学科的ID
        Common.SubjectId= Common.getMap().get(project);

        inint();
        return mView;
    }

    private void inint() {
        //得到教材版本的数据
         getData();

    }

    /**
     * 得到一级知识点
     */
    private void getOneKnowledgepoint() {
        //一级列表url
        String getoneListUrl= XSYTools.getWrongUrl(WrongQuestionConstantClass.KNOWLEDGEPOINT_URL,myContext);
        XsyMap map=XsyMap.getInterface();
        Long studId= new GetUserInfoClass(myContext).getUserId();
        map.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
        map.put(WrongQuestionConstantClass.PARAM_BOOKSECTIONID, Common.bookSection);

        OkGo.post(getoneListUrl).params(map).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i("一级知识点"+s);
                oneMenulist.clear();
                twoMenulist.clear();
                JSONArray array= null;
                try {
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
                    //一级菜单显示
                    OneMenulistadapter twoMenulistadapter=new OneMenulistadapter(oneMenulist);
                    list_oneknowloage.setAdapter(twoMenulistadapter);
                    //一级菜单点击事件，如果有值的话，显示二级菜单，如果没有值的话直接显示错题
                    list_oneknowloage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Common.oneKnowleageId=oneMenulist.get(position).getId();
                            //看看有没有二级知识点
                            String TwomenuknowUrl=XSYTools.getWrongUrl(WrongQuestionConstantClass.KNOWLEDGEPOINT_URL,myContext);
                            XsyMap OneKnowMap=XsyMap.getInterface();
                            Long studId= new GetUserInfoClass(myContext).getUserId();
                            OneKnowMap.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
                            OneKnowMap.put(WrongQuestionConstantClass.PARAM_BOOKSECTIONID, Common.bookSection);
                            OneKnowMap.put(WrongQuestionConstantClass.PARAM_KNOWLEDGEID,Common.oneKnowleageId);

                            OkGo.post(TwomenuknowUrl).params(OneKnowMap).execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    XSYTools.i("二级知识点="+s);
                                    if(s.equals("[]")){
                                        list_twoknowloage.setAdapter(null);
                                        XSYTools.i("二级知识点没有值");
                                        //直接获取错题列表
                                        //获取详细试题的url
                                        String detailedUrl=XSYTools.getWrongUrl(WrongQuestionConstantClass.LIST_URL,myContext);
                                        XsyMap oneKnowMap=XsyMap.getInterface();
                                        //学生id
                                        Long studId= new GetUserInfoClass(myContext).getUserId();
                                        oneKnowMap.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
                                        //学科id
                                        oneKnowMap.put(WrongQuestionConstantClass.PARAM_SUBJECTID,Common.SubjectId);
                                        //教材id
                                        oneKnowMap.put(WrongQuestionConstantClass.PARAM_BOOKID,Common.selectBookId);
                                        //分册id
                                        oneKnowMap.put(WrongQuestionConstantClass.PARAM_BOOKSECTIONID,Common.bookSection);
                                        //知识点id
                                        oneKnowMap.put(WrongQuestionConstantClass.PARAM_KNOWLEDGEID, Common.oneKnowleageId);

                                        OkGo.post(detailedUrl).params(oneKnowMap).execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(String s, Call call, Response response) {
                                                XSYTools.i("一级菜单="+s);

                                                showWrongHandler.sendMessage(XSYTools.makeNewMessage(Common.SHOWWRONG_LIST,s));
                                                SuspendWindowsUtil.HideWindow();
                                            }
                                        });

                                    }else{
                                        twoMenulist.clear();
                                        JSONArray array= null;
                                        try {
                                            array = new JSONArray(s);
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


                                            OneMenulistadapter twoMenulistadapter=new OneMenulistadapter(twoMenulist);
                                            list_twoknowloage.setAdapter(twoMenulistadapter);
                                            list_twoknowloage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    //得到二级菜单的id
                                                   Common.twoKnowleageId=twoMenulist.get(position).getId();
                                                    //获取详细试题的url
                                                    String detailedUrl=XSYTools.getWrongUrl(WrongQuestionConstantClass.LIST_URL,myContext);
                                                    XsyMap twoknwoMap=XsyMap.getInterface();
                                                    //学生id
                                                    Long studId= new GetUserInfoClass(myContext).getUserId();
                                                    twoknwoMap.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
                                                    //学科id
                                                    twoknwoMap.put(WrongQuestionConstantClass.PARAM_SUBJECTID,Common.SubjectId);
                                                    //教材id
                                                    twoknwoMap.put(WrongQuestionConstantClass.PARAM_BOOKID,Common.selectBookId);
                                                    //分册id
                                                    twoknwoMap.put(WrongQuestionConstantClass.PARAM_BOOKSECTIONID,Common.bookSection);
                                                    //知识点id
                                                    twoknwoMap.put(WrongQuestionConstantClass.PARAM_KNOWLEDGEID, Common.twoKnowleageId);

                                                    OkGo.post(detailedUrl).params(twoknwoMap).execute(new StringCallback() {
                                                        @Override
                                                        public void onSuccess(String s, Call call, Response response) {
                                                            XSYTools.i("二级菜单="+s);

                                                            showWrongHandler.sendMessage(XSYTools.makeNewMessage(Common.SHOWWRONG_LIST,s));
                                                            SuspendWindowsUtil.HideWindow();
                                                        }
                                                        @Override
                                                        public void onError(Call call, Response response, Exception e) {
                                                            super.onError(call, response, e);
                                                            XSYTools.i(e.toString());
                                                        }
                                                    });






                                                }
                                            });

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }


                                }
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
                XSYTools.i(e.toString());
            }
        });

    }

    /**
     *得到分册信息
     */
    private void getBookSection() {
        String getBookSelectUrl= XSYTools.getWrongUrl(WrongQuestionConstantClass.BOOKSECTION_URL,myContext);
        XsyMap map=XsyMap.getInterface();
        Long studId= new GetUserInfoClass(myContext).getUserId();
        map.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
        map.put(WrongQuestionConstantClass.PARAM_BOOKID,Common.selectBookId);

        OkGo.post(getBookSelectUrl).params(map).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i(s);
                Facilist.clear();
                try {
                    JSONArray array=new JSONArray(s);
                    for(int i=0;i<array.length();i++){
                        JSONObject object=new JSONObject(array.get(i).toString());
                        String FasciId=String.valueOf(object.get("id"));
                        String FasciName=object.getString("name");
                        //放入
                        FaciInfo faciInfo=new FaciInfo();
                        faciInfo.setFasciId(FasciId);
                        faciInfo.setFasciName(FasciName);
                        Facilist.add(faciInfo);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }


    //根据学科得到教材信息
    public void  getData() {
        //得到选中学科的ID
        Common.SubjectId= Common.getMap().get(project);
        String getBookUrl= XSYTools.getWrongUrl(WrongQuestionConstantClass.BOOK_URL,myContext);
        XsyMap map=XsyMap.getInterface();

        Long studId= new GetUserInfoClass(myContext).getUserId();
        XSYTools.i("得到学生id"+studId);

        map.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
        map.put(WrongQuestionConstantClass.PARAM_SUBJECTID, Common.SubjectId);
        OkGo.post(getBookUrl).params(map).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i(s);
                Bookbanbenlist.clear();
                oneMenulist.clear();
                twoMenulist.clear();
                try {
                    JSONArray array=new JSONArray(s);
                    for(int i=0;i<array.length();i++){
                        JSONObject jsonObject=new JSONObject(array.get(i).toString());
                        BookInfo bookInfo=new BookInfo();
                        //得到教材版本ID
                        String BookId=jsonObject.getString("id");
                        //得到教材版本名字
                        String BookName=jsonObject.getString("name");
                        //把信息储存到集合
                        bookInfo.setBookId(BookId);
                        bookInfo.setBookName(BookName);
                        Bookbanbenlist.add(bookInfo);
                    }
                    //创建教材版本的适配器
                    adapter=new MyAdapter();
                    list_teacherbok.setAdapter(adapter);
                    //更新数据
                    adapter.notifyDataSetChanged();

                    list_teacherbok.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //清空数据
                            list_fascicle.setAdapter(null);
                            list_oneknowloage.setAdapter(null);
                            list_twoknowloage.setAdapter(null);

                            BookInfo bookinfo= Bookbanbenlist.get(position);
                            //得到已经选择的教材版本
                            Common.selectBookId=bookinfo.getBookId();
//                            //根据教材版本得到分册
//                            getBookSection();

                            String getBookSelectUrl= XSYTools.getWrongUrl(WrongQuestionConstantClass.BOOKSECTION_URL,myContext);
                            XsyMap map=XsyMap.getInterface();
                            Long studId= new GetUserInfoClass(myContext).getUserId();
                            map.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
                            map.put(WrongQuestionConstantClass.PARAM_BOOKID,Common.selectBookId);

                            OkGo.post(getBookSelectUrl).params(map).execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    XSYTools.i(s);
                                    Facilist.clear();
                                    try {
                                        JSONArray array=new JSONArray(s);
                                        for(int i=0;i<array.length();i++){
                                            JSONObject object=new JSONObject(array.get(i).toString());
                                            String FasciId=String.valueOf(object.get("id"));
                                            String FasciName=object.getString("name");
                                            //放入
                                            FaciInfo faciInfo=new FaciInfo();
                                            faciInfo.setFasciId(FasciId);
                                            faciInfo.setFasciName(FasciName);
                                            Facilist.add(faciInfo);
                                        }
                                        bsAdapeter=new BookSectionAdapter();
                                        list_fascicle.setAdapter(bsAdapeter);
                                        //更新数据
                                        bsAdapeter.notifyDataSetChanged();
                                        list_fascicle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                                list_oneknowloage.setAdapter(null);
                                                list_twoknowloage.setAdapter(null);
                                                //已经选择的分册id
                                                Common.bookSection =Facilist.get(position).getFasciId();
                                                //得到一级知识点
                                                getOneKnowledgepoint();

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
                XSYTools.i(e.toString());
            }
        });

    }
    //Book教材版本适配器
    private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return Bookbanbenlist.size();
        }

        @Override
        public Object getItem(int position) {
            return Bookbanbenlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater _LayoutInflater=LayoutInflater.from(myContext);
            convertView=_LayoutInflater.inflate(R.layout.item_book, null);
            if(convertView!=null) {
                TextView textView= (TextView) convertView.findViewById(R.id.book_text_item);
                textView.setText(Bookbanbenlist.get(position).getBookName());
            }
            return convertView;
        }
    }

    /**
     * 显示上下册的适配器
     */
    private class BookSectionAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return Facilist.size();
        }

        @Override
        public Object getItem(int position) {
            return Facilist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater _LayoutInflater=LayoutInflater.from(myContext);
            convertView=_LayoutInflater.inflate(R.layout.item_book, null);
            if(convertView!=null) {
                TextView textView= (TextView) convertView.findViewById(R.id.book_text_item);
                textView.setText(Facilist.get(position).getFasciName());
            }
            return convertView;
        }
    }

    /**
     * 一级菜单适配器
     */
    class OneMenulistadapter extends BaseAdapter{
       private List<MenuInfo> Menulist;
        public OneMenulistadapter(List<MenuInfo> oneMenulist) {
            Menulist=oneMenulist;
        }

        @Override
          public int getCount() {
              return Menulist.size();
          }

          @Override
          public Object getItem(int position) {
              return Menulist.get(position);
          }

          @Override
          public long getItemId(int position) {
              return position;
          }

          @Override
          public View getView(int position, View convertView, ViewGroup parent) {
              ViewHolder holder;
                LayoutInflater inflater=LayoutInflater.from(myContext);
                if(convertView==null){
                    convertView=inflater.inflate(R.layout.onemenulayout,null);
                    holder=new ViewHolder();
                    holder.textview= (TextView) convertView.findViewById(R.id.menu_one_item);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
                }
              String content=Menulist.get(position).getContent();
              if(content!=null){
                  holder.textview.setText(content);
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
