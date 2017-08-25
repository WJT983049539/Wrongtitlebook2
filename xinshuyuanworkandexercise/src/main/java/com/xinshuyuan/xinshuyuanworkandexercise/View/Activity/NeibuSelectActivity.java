package com.xinshuyuan.xinshuyuanworkandexercise.View.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.twiceyuan.dropdownmenu.ArrayDropdownAdapter;
import com.twiceyuan.dropdownmenu.DropdownMenu;
import com.twiceyuan.dropdownmenu.OnDropdownItemClickListener;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.PerferenceService;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XsyMap;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.javabean.OneKnowInfo;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.presenter.Handler.NeibuSelectHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;
import work.HomeWorkConstantClass;

/**
 * Created by Administrator on 2017/8/14.
 */

public class NeibuSelectActivity extends BaseActivity{
    private DropdownMenu subject_menu;
    private DropdownMenu different_menu;
    private DropdownMenu jiaocai_menu;
    private DropdownMenu fence_menu;
    private TextView neibu_select_know;
    private TextView start_select_info;
    private PopupWindow popupWindow=null;
    private Long UserId;

    //现在选择的难度
    private String NOWdifferent="";
    final String[] differentarray = new String[]{"简单","容易","一般","较难","难"};

    private View PopView;
    //pop弹出菜单一级栏目listView
    private ListView pop_know_one;
    //pop弹出菜单二级栏目listView
    private ListView pop_know_two;

    //现在选择的学科id
    private String NOWprojectId="";
    private String NowProjectName="";

    //现在选择的教材id
    private String NOWbookId="";
    private String NowBookName="";


    //现在选择的分册id
    private String NOWfenceId="";
    private String NowfenceName="";

    //存有科目名和id的集合
    private Map<String,Long> projectinfoMap;
    private List<String> projectNameList;

    //存有教材版本的集合
    private  Map<String,Long> bookinfoMap;
    private List<String> bookinfolist;

    //存有上下册的集合
    private Map<String,Long> fenceinfoMap;
    private List<String> fenceinfolist;

    //一级二级知识点的集合和适配器
    private List<OneKnowInfo> oneList;
    private List<OneKnowInfo> twoList;
    private OneKnowAdaper oneKnowAdaper;
    private OneKnowAdaper twoKnowAdaper;
    private String NOWoneknowid;
    private String NOWoneknowName;

    private NeibuSelectHandler neibuselectHandler;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window=getWindow();
        WindowManager.LayoutParams layoutParams= window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.neibuselect_layout);
        PerferenceService service=new PerferenceService(this);
        UserId=service.getsharedPre().getLong("WorkId",0);
        neibuselectHandler=new NeibuSelectHandler(this);
        inint();
        getData();
    }


    private void inint() {

        oneList=new ArrayList<OneKnowInfo>();
        twoList=new ArrayList<OneKnowInfo>();

        bookinfolist=new ArrayList<String>();
        bookinfoMap=new HashMap<String,Long>();

        fenceinfoMap=new HashMap<String,Long>();
        fenceinfolist=new ArrayList<String>();

        projectinfoMap=new HashMap<String,Long>();
        projectNameList=new ArrayList<String>();
        subject_menu= (DropdownMenu) findViewById(R.id.project_menu);
        different_menu= (DropdownMenu) findViewById(R.id.different_menu);
        jiaocai_menu= (DropdownMenu) findViewById(R.id.jiaocai_menu);
        fence_menu= (DropdownMenu) findViewById(R.id.fence_menu);


        neibu_select_know= (TextView) findViewById(R.id.neibu_select_know);
        start_select_info= (TextView) findViewById(R.id.start_select_info);

        //获取列表信息

        start_select_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHOWListFragment();
            }
        });




        different_menu.setAdapter(new ArrayDropdownAdapter(NeibuSelectActivity.this, R.layout.dropdown_light_item_1line, differentarray));

        different_menu.setOnItemClickListener(new OnDropdownItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NOWdifferent=differentarray[position];
            }
        });

        neibu_select_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NOWprojectId.equals("") || NOWbookId.equals("") || NOWfenceId.equals("")) {

                    XSYTools.showToastmsg(NeibuSelectActivity.this, "请把条件选择完整！");

                } else {

                    LayoutInflater layoutInflater = (LayoutInflater) NeibuSelectActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    PopView = layoutInflater.inflate(R.layout.layout_popupwindow_style01, null);
                    pop_know_one = (ListView) PopView.findViewById(R.id.neibuselsect_know_one);
                    pop_know_two = (ListView) PopView.findViewById(R.id.neibuselsect_know_two);

                    //加载数据，获取一级知识点list
                    HashMap<String, String> getlanmuMap = XsyMap.getInterface();
                    getlanmuMap.put(HomeWorkConstantClass.PARAM_BOOKSECTIONID,NOWfenceId);
                    getlanmuMap.put(HomeWorkConstantClass.PARAM_KNOWLEDGEID,"");
                    OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.KNOWLEDGEPOINT_URL,NeibuSelectActivity.this)).params(getlanmuMap).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            XSYTools.i("得到一级知识点",s);
                            if (s.equals("[]")) {
                                XSYTools.i("没有一级知识点");
                            }else {
                                try {
                                    JSONArray array = new JSONArray(s);
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        String oneKnowId = String.valueOf(object.getLong("id"));
                                        String oneKnowString = object.getString("name");
                                        OneKnowInfo oneKnowInfo = new OneKnowInfo();
                                        oneKnowInfo.setOneKnowId(oneKnowId);
                                        oneKnowInfo.setOneKnowString(oneKnowString);
                                        oneList.add(oneKnowInfo);
                                    }
                                    oneKnowAdaper = new OneKnowAdaper(oneList);
                                    pop_know_one.setAdapter(oneKnowAdaper);

                                    pop_know_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            NOWoneknowid = oneList.get(position).getOneKnowId();
                                            NOWoneknowName = oneList.get(position).getOneKnowString();
                                            XsyMap<String, String> gettwoKnow = XsyMap.getInterface();
                                            gettwoKnow.put(HomeWorkConstantClass.PARAM_BOOKSECTIONID, NOWfenceId);
                                            gettwoKnow.put(HomeWorkConstantClass.ERROR_TEST_PARENTID, NOWoneknowid);
                                            OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.KNOWLEDGEPOINT_URL,NeibuSelectActivity.this)).params(gettwoKnow).execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(String s, Call call, Response response) {
                                                    twoList.clear();
                                                    XSYTools.i("得到的二级知识点json" + s);

                                                    if (s.equals("[]")) {
                                                        XSYTools.i("没有二级知识点");

                                                        neibu_select_know.setText(NOWoneknowName);
                                                        popupWindow.dismiss();

                                                    } else {

                                                        try {
                                                            JSONArray array = new JSONArray(s);

                                                            for (int i = 0; i < array.length(); i++) {
                                                                JSONObject object2 = array.getJSONObject(i);
                                                                String twoKnowId = String.valueOf(object2.getLong("id"));
                                                                String twoKnowString = object2.getString("name");
                                                                OneKnowInfo twoKnowInfo = new OneKnowInfo();
                                                                twoKnowInfo.setOneKnowId(twoKnowId);
                                                                twoKnowInfo.setOneKnowString(twoKnowString);
                                                                twoList.add(twoKnowInfo);
                                                            }
                                                            twoKnowAdaper = new OneKnowAdaper(twoList);
                                                            pop_know_two.setAdapter(twoKnowAdaper);


                                                            pop_know_two.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                    NOWoneknowid = twoList.get(position).getOneKnowId();
                                                                    NOWoneknowName = twoList.get(position).getOneKnowString();
                                                                    neibu_select_know.setText(NOWoneknowName);
                                                                    popupWindow.dismiss();
                                                                }
                                                            });

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }


                                                }

                                                @Override
                                                public void onError(Call call, Response response, Exception e) {
                                                    super.onError(call, response, e);
                                                    XSYTools.i("得到的二级知识点错误json" + e);
                                                    popupWindow.dismiss();
                                                }
                                            });

                                        }
                                    });











                                }catch (Exception e){e.printStackTrace();}

                            }


                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            XSYTools.i("一级知识点获取失败"+e.toString());
                            popupWindow.dismiss();
                        }
                    });


                    popupWindow=new PopupWindow(PopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    // 使其聚集
                    popupWindow.setFocusable(true);
                    // 设置允许在外点击消失
                    PopView.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                popupWindow.dismiss();
                                return true;
                            }
                            return false;
                        }
                    });

                    popupWindow.setOutsideTouchable(true);
//            popupWindow.showAsDropDown(PopView);
                    // popupWindow.showAtLocation(, Gravity.BOTTOM, 0, 0);
                    popupWindow.setAnimationStyle(R.style.contextMenuAnim);
                    popupWindow.showAsDropDown(neibu_select_know);

                }
            }
        });

    }


    /**
     * 填充数据，把科目和难度放进去
     */
    private void getData() {
        XsyMap<String,String> projectMap=XsyMap.getInterface();
        projectMap.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(UserId));
        OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.SUBJECT_URL,NeibuSelectActivity.this)).params(projectMap).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i("得到科目列表"+s);
                projectNameList.clear();
                projectinfoMap.clear();
                try {
                    JSONObject obje=new JSONObject(s);

                    JSONArray array=obje.getJSONArray("subject");

                    for(int i=0;i<array.length();i++){

                        JSONObject OO=array.getJSONObject(i);
                        Long subjectId=OO.getLong("id");
                        String subjectName=OO.getString("subjectName");
                        projectNameList.add(subjectName);
                        projectinfoMap.put(subjectName,subjectId);
                    }
                    //放入学科数据
                    subject_menu.setAdapter(new ArrayDropdownAdapter(NeibuSelectActivity.this, R.layout.dropdown_light_item_1line, projectNameList));
                    subject_menu.setOnItemClickListener(new OnDropdownItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            NOWprojectId=String.valueOf(projectinfoMap.get(projectNameList.get(position)));
                            //现在要显示的String保存到缓存里面
                            NowProjectName=projectNameList.get(position);
                            XsyMap<String,String> JiaoCaibookMap=XsyMap.getInterface();
                            JiaoCaibookMap.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(UserId));
                            JiaoCaibookMap.put(HomeWorkConstantClass.PARAM_SUBJECTID,NOWprojectId);

                            OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.BOOK_URL,NeibuSelectActivity.this)).params(JiaoCaibookMap).execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    bookinfolist.clear();
                                    bookinfoMap.clear();
                                    XSYTools.i("教材json"+s);

                                    try {
                                        JSONArray array=new JSONArray(s);
                                        for(int i=0;i<array.length();i++){
                                            JSONObject OO=array.getJSONObject(i);
                                            long bookId=OO.getLong("id");
                                            String bookName=OO.getString("name");
                                            bookinfolist.add(bookName);
                                            bookinfoMap.put(bookName,bookId);
                                        }
                                        jiaocai_menu.setAdapter(new ArrayDropdownAdapter(NeibuSelectActivity.this, R.layout.dropdown_light_item_1line, bookinfolist));
                                        jiaocai_menu.setOnItemClickListener(new OnDropdownItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                NOWbookId=String.valueOf(bookinfoMap.get(bookinfolist.get(position)));
                                                NowBookName=bookinfolist.get(position);
                                                XsyMap<String,String> getFenceMap=XsyMap.getInterface();
                                                getFenceMap.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(UserId));
                                                getFenceMap.put(HomeWorkConstantClass.PARAM_BOOKID,NOWbookId);

                                                OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.BOOKSECTION_URL,NeibuSelectActivity.this)).params(getFenceMap).execute(new StringCallback() {
                                                    @Override
                                                    public void onSuccess(String s, Call call, Response response) {
                                                        fenceinfoMap.clear();
                                                        fenceinfolist.clear();


                                                        XSYTools.i("分册Json"+s);

                                                        try {
                                                            JSONArray array=new JSONArray(s);

                                                            for(int i=0;i<array.length();i++){
                                                                JSONObject oo=array.getJSONObject(i);
                                                                long Id=oo.getLong("id");
                                                                String Name=oo.getString("name");
                                                                fenceinfoMap.put(Name,Id);
                                                                fenceinfolist.add(Name);
                                                            }
                                                            fence_menu.setAdapter(new ArrayDropdownAdapter(NeibuSelectActivity.this, R.layout.dropdown_light_item_1line, fenceinfolist));
                                                            fence_menu.setOnItemClickListener(new OnDropdownItemClickListener() {
                                                                @Override
                                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                    NOWfenceId=String.valueOf(fenceinfoMap.get(fenceinfolist.get(position)));
                                                                    NowfenceName=fenceinfolist.get(position);
                                                                }
                                                            });
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(Call call, Response response, Exception e) {
                                                        super.onError(call, response, e);
                                                        XSYTools.i("分册Json报错"+e.toString());
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
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                XSYTools.i("得到科目列表请求错误"+e.toString());
            }
        });
    }

    private class OneKnowAdaper extends BaseAdapter {
        private List<OneKnowInfo> list;
        public OneKnowAdaper(List<OneKnowInfo> oneList) {
            list=oneList;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutinflater=LayoutInflater.from(NeibuSelectActivity.this);
            convertView= layoutinflater.inflate(R.layout.layout_item_select_know,parent,false);
            TextView textview= (TextView) convertView.findViewById(R.id.select_know_item_textview);
            textview.setText(list.get(position).getOneKnowString());
            return convertView;
        }
    }


    //获取所有符合的列表

    private void SHOWListFragment() {

        if(NOWprojectId.equals("")||NOWprojectId.equals("")||NOWbookId.equals("")||NOWfenceId.equals("")||NOWoneknowid.equals("")){
            XSYTools.showToastmsg(NeibuSelectActivity.this,"请把条件补充完整");
        }else{
            PerferenceService service=new PerferenceService(NeibuSelectActivity.this);
            service.save("NOWprojectId",NOWprojectId);
            service.save("NOWbookId",NOWbookId);
            service.save("NOWfenceId",NOWfenceId);
            service.save("KnowId",NOWoneknowid);

            //保存到共享参数里面方便调用，在缓存里面在存一次

            Common.setNOWprojectId(NOWprojectId);
            Common.setNOWbookId(NOWbookId);
            Common.setNOWfenceId(NOWfenceId);
            Common.setKnowId(NOWoneknowid);

            Common.setNowKnowName(NOWoneknowName);
            Common.setNowProjectName(NowProjectName);
            Common.setNowBookName(NowBookName);
            Common.setNowfenceName(NowfenceName);



            XsyMap<String,String> exerciseMap=XsyMap.getInterface();
            String differentParmes="";
            if(NOWdifferent.equals("简单")){
                differentParmes=String.valueOf(HomeWorkConstantClass.TEST_DIFFICULTY_EAISER_GENERAL);
            }else if(NOWdifferent.equals("容易")){
                differentParmes=String.valueOf(HomeWorkConstantClass.TEST_DIFFICULTY_EASY_GENERAL);
            }else if(NOWdifferent.equals("一般")){
                differentParmes=String.valueOf(HomeWorkConstantClass.TEST_DIFFICULTY_GENERAL_GENERAL);
            }else if(NOWdifferent.equals("较难")){
                differentParmes=String.valueOf(HomeWorkConstantClass.TEST_DISCRIMINATION_SMALL_MAX);
            }else if(NOWdifferent.equals("困难")){
                differentParmes=String.valueOf(HomeWorkConstantClass.TEST_DIFFICULTY_VERYDIFFICULTY_GENERAL);
            }
            exerciseMap.put(HomeWorkConstantClass.PARAM_DIFFICULT,differentParmes);
            exerciseMap.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(UserId));
            exerciseMap.put(HomeWorkConstantClass.PARAM_SUBJECTID,NOWprojectId);
            exerciseMap.put(HomeWorkConstantClass.PARAM_BOOKID,NOWbookId);
            exerciseMap.put(HomeWorkConstantClass.PARAM_BOOKSECTIONID,NOWfenceId);
            exerciseMap.put(HomeWorkConstantClass.PARAM_KNOWLEDGEID,NOWoneknowid);

            OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.PRACTICES_URL,NeibuSelectActivity.this)).params(exerciseMap).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    XSYTools.i("得到内部练习的试题json"+s);
                    //缓存下来
                    Common.setListJson(s);
                    neibuselectHandler.sendMessage(XSYTools.makeNewMessage(Common.WORK_SHOW_LIST,s));
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    XSYTools.i("得到内部练习请求报错"+e.toString());
                }
            });

        }
    }

}
