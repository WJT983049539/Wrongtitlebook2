package com.xinshuyuan.xinshuyuanworkandexercise.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.twiceyuan.dropdownmenu.ArrayDropdownAdapter;
import com.twiceyuan.dropdownmenu.DropdownMenu;
import com.twiceyuan.dropdownmenu.OnDropdownItemClickListener;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.AnswerEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.PerferenceService;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XsyMap;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.javabean.ProjectInfo;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.presenter.Handler.Layering_practiceHandler;

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
 * 分册练习的Activity
 * Created by wjt on 2017/7/8.
 */

public class Layering_practice_Activity extends BaseActivity{

    Layering_practiceHandler layering_practicehandler;

    private TextView work_xuanti;
    private DropdownMenu subject_dm_dropdown;
    private DropdownMenu jiaocai_dm_dropdown;
    private DropdownMenu fence_dm_dropdown2;
    private DropdownMenu diffent_dm_dropdown2;
//    private DropdownMenu know_dm_dropdown3;
    private TextView know_selsct;

    private Long userId;
    private String UserName;

    //现在选择的知识点id
    private String KnowId="";
    private String NowKnowName="";

    //现在选择的学科id
    private String NOWprojectId="";
    private String NowProjectName="";

    //现在选择的教材id
    private String NOWbookId="";
    private String NowBookName="";


    //现在选择的分册id
    private String NOWfenceId="";
    private String NowfenceName="";


    //private List<ProjectInfo> projectInfoList;
    //存有科目名和id的集合
    private Map<String,Long> projectinfoMap;
    private List <String> projectNameList;

    //存有教材版本的集合
    private  Map<String,Long> bookinfoMap;
    private List<String> bookinfolist;

    //存有上下册的集合
    private Map<String,Long> fenceinfoMap;
    private List<String> fenceinfolist;

    //现在选择的难度
    private String NOWdifferent="";
    private Float different;

    final String[] differentarray = new String[]{"简单","容易","一般","较难","难"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layering_pratice);
        Intent intent=getIntent();
        userId=intent.getLongExtra("userId",0);
        UserName=intent.getStringExtra("userName");
        layering_practicehandler=new Layering_practiceHandler(this,userId);
        inint();
        setData();
    }



    private void inint() {

        projectinfoMap=new HashMap<String,Long>();
        projectNameList=new ArrayList<String>();

        bookinfolist=new ArrayList<String>();
        bookinfoMap=new HashMap<String,Long>();

        fenceinfoMap=new HashMap<String,Long>();
        fenceinfolist=new ArrayList<String>();
        //难度下啦
        diffent_dm_dropdown2= (DropdownMenu) findViewById(R.id.different_dm_dropdown2);
        //选题按钮
        work_xuanti= (TextView) findViewById(R.id.work_xuanti);
        //学科下拉
        subject_dm_dropdown= (DropdownMenu) findViewById(R.id.subject_dm_dropdown);
        //教材下拉
        jiaocai_dm_dropdown= (DropdownMenu) findViewById(R.id.jiaocai_dm_dropdown);
        //分册下拉
        fence_dm_dropdown2= (DropdownMenu) findViewById(R.id.fence_dm_dropdown2);
        //知识点下拉
//        fence_dm_dropdown2= (DropdownMenu) findViewById(R.id.know_dm_dropdown3);
        know_selsct=(TextView)findViewById(R.id.know_selsct);

        diffent_dm_dropdown2.setAdapter(new ArrayDropdownAdapter(Layering_practice_Activity.this, R.layout.dropdown_light_item_1line, differentarray));

        diffent_dm_dropdown2.setOnItemClickListener(new OnDropdownItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NOWdifferent=differentarray[position];

                if(NOWdifferent.equals("简单")){
                    different=HomeWorkConstantClass.TEST_DIFFICULTY_EAISER_GENERAL;
                }else if(NOWdifferent.equals("容易")){
                    different=HomeWorkConstantClass.TEST_DIFFICULTY_EASY_GENERAL;
                }else if(NOWdifferent.equals("一般")){
                    different=HomeWorkConstantClass.TEST_DIFFICULTY_GENERAL_GENERAL;
                }else if(NOWdifferent.equals("较难")){
                    different=HomeWorkConstantClass.TEST_DISCRIMINATION_SMALL_MAX;
                }else if(NOWdifferent.equals("难")){
                    different=HomeWorkConstantClass.TEST_DIFFICULTY_VERYDIFFICULTY_GENERAL;
                }
            }
        });

        //随机选题的按钮
        work_xuanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NOWprojectId.equals("")||NOWdifferent.equals("")||NOWbookId.equals("")||NOWfenceId.equals("")||KnowId.equals("")){
                    XSYTools.showToastmsg(Layering_practice_Activity.this,"请把条件补充完整");
                }else {
                    try {

                        //得到这些参数以后用这些参数取请求试题然后显示页面
                        HashMap<String,String> map=XsyMap.getInterface();
                        map.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(userId));
                        map.put(HomeWorkConstantClass.PARAM_SUBJECTID,NOWprojectId);
                        map.put(HomeWorkConstantClass.PARAM_BOOKID,NOWbookId);
                        map.put(HomeWorkConstantClass.PARAM_BOOKSECTIONID,NOWfenceId);
                        map.put(HomeWorkConstantClass.PARAM_KNOWLEDGEID,String.valueOf(KnowId));
                        if(NOWdifferent.equals("简单")){
                            different=HomeWorkConstantClass.TEST_DIFFICULTY_EAISER_GENERAL;

                        }else if(NOWdifferent.equals("容易")){
                            different=HomeWorkConstantClass.TEST_DIFFICULTY_EASY_GENERAL;
                        }else if(NOWdifferent.equals("一般")){
                            different=HomeWorkConstantClass.TEST_DIFFICULTY_GENERAL_GENERAL;
                        }else if(NOWdifferent.equals("较难")){
                            different=HomeWorkConstantClass.TEST_DISCRIMINATION_SMALL_MAX;
                        }else if(NOWdifferent.equals("难")){
                            different=HomeWorkConstantClass.TEST_DIFFICULTY_VERYDIFFICULTY_GENERAL;
                        }

                        map.put(HomeWorkConstantClass.PARAM_DIFFICULT,String.valueOf(different));

                        OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.PRACTICES_URL,Layering_practice_Activity.this)).params(map).execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                XSYTools.i("得到一道试题的json"+s);
                                TestEntity testEntity;

                                try {
                                    JSONObject object=new JSONObject(s);
                                    Integer TestType = object.getInt("itemType");
                                    //得到题干
                                    String point = object.getString("itemPoint");
                                    //试题id
                                    long testId=object.getLong("id");
                                    String answerAnalysis=object.getString("answerAnalysis");
                                    String Subject=object.getString("subjectName");
                                    Long workId=object.getLong("stuWorkId");
                                    //装入javabean
                                    testEntity =new TestEntity();
                                    testEntity.setTestId(testId);
                                    testEntity.setTestType(TestType);
                                    testEntity.setPoint(point);
                                    testEntity.setSubject(Subject);
                                    testEntity.setWorkId(String.valueOf(workId));
                                    testEntity.setAnswerAnalysis(answerAnalysis);
                                    JSONArray jsonarray = object.getJSONArray("answerList");
                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        JSONObject obb = jsonarray.getJSONObject(i);
                                        long answerid = obb.getLong("id");
                                        long answertestId = obb.getLong("testId");
                                        String optionTitle = obb.getString("optionTitle");
                                        String optionAnswer = obb.getString("optionAnswer");
                                        Boolean isRealAnswer = obb.getBoolean("isRealAnswer");

                                        AnswerEntity answerEntity = new AnswerEntity();
                                        answerEntity.setId(answerid);
                                        answerEntity.setTestId(answertestId);
                                        answerEntity.setIsRealAnswer(isRealAnswer);
                                        answerEntity.setOptionAnswer(optionAnswer);
                                        answerEntity.setOptionTitle(optionTitle);
                                        testEntity.fillAnser(answerEntity);
                                    }
                                    layering_practicehandler.sendMessage(XSYTools.makeNewMessage(Common.PRIVIEW_SHOW_TEXT, testEntity));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                super.onError(call, response, e);
                                XSYTools.i("获取一道试题json错误"+e.toString());

                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }




                }
        });


        //知识点选择按钮
        know_selsct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NOWprojectId.equals("")||NOWbookId.equals("")||NOWfenceId.equals("")||NOWdifferent.equals("")){

                    XSYTools.showToastmsg(Layering_practice_Activity.this,"请把条件选择完整！");

                }else{

                    Intent intent=new Intent(Layering_practice_Activity.this,selectKnowActivity.class);
                    intent.putExtra("userId",String.valueOf(userId));
                    intent.putExtra("projectId",NOWprojectId);
                    intent.putExtra("bookId",NOWbookId);
                    intent.putExtra("fenceId",NOWfenceId);
                    intent.putExtra("UserName",UserName);
                    startActivityForResult(intent,6);
                }
            }
        });

    }
    //得到数据并填充上去
    private void setData() {
        XsyMap<String,String> projectMap=XsyMap.getInterface();
        projectMap.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(userId));
        OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.SUBJECT_URL,Layering_practice_Activity.this)).params(projectMap).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i("学科的json"+s);
                projectNameList.clear();
                projectinfoMap.clear();
                try {
                    JSONObject object=new JSONObject(s);
                    JSONArray array=object.getJSONArray("subject");
                    for(int i=0;i<array.length();i++){
                        JSONObject oo=array.getJSONObject(i);
                         Long id=oo.getLong("id");
                         String subjectName=oo.getString("subjectName");
                         projectNameList.add(subjectName);
                         ProjectInfo pinfo=new ProjectInfo();
                         pinfo.setSubjectId(id);
                         pinfo.setSubjectName(subjectName);
                         projectinfoMap.put(subjectName,id);
                    }
                    //放入学科数据
                    subject_dm_dropdown.setAdapter(new ArrayDropdownAdapter(Layering_practice_Activity.this, R.layout.dropdown_light_item_1line, projectNameList));
                    subject_dm_dropdown.setOnItemClickListener(new OnDropdownItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            NOWprojectId=String.valueOf(projectinfoMap.get(projectNameList.get(position)));
                            //现在要显示的String保存到缓存里面
                            NowProjectName=projectNameList.get(position);
                            XsyMap<String,String> JiaoCaibookMap=XsyMap.getInterface();
                            JiaoCaibookMap.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(userId));
                            JiaoCaibookMap.put(HomeWorkConstantClass.PARAM_SUBJECTID,NOWprojectId);

                            OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.BOOK_URL,Layering_practice_Activity.this)).params(JiaoCaibookMap).execute(new StringCallback() {
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
                                        jiaocai_dm_dropdown.setAdapter(new ArrayDropdownAdapter(Layering_practice_Activity.this, R.layout.dropdown_light_item_1line, bookinfolist));
                                        jiaocai_dm_dropdown.setOnItemClickListener(new OnDropdownItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                NOWbookId=String.valueOf(bookinfoMap.get(bookinfolist.get(position)));
                                                NowBookName=bookinfolist.get(position);
                                                XsyMap<String,String> getFenceMap=XsyMap.getInterface();
                                                getFenceMap.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(userId));
                                                getFenceMap.put(HomeWorkConstantClass.PARAM_BOOKID,NOWbookId);

                                                OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.BOOKSECTION_URL,Layering_practice_Activity.this)).params(getFenceMap).execute(new StringCallback() {
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
                                                            fence_dm_dropdown2.setAdapter(new ArrayDropdownAdapter(Layering_practice_Activity.this, R.layout.dropdown_light_item_1line, fenceinfolist));
                                                            fence_dm_dropdown2.setOnItemClickListener(new OnDropdownItemClickListener() {
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
                XSYTools.i("学科的错误"+e);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode){

            case 6:
                Bundle bb= data.getExtras();
                KnowId=bb.getString("KnowId");
                String KnowString=bb.getString("knowName");
                NowKnowName=KnowString;

                know_selsct.setText(KnowString);
                //显示试题列表
                SHOWListFragment();


                break;


        }
    }

    private void SHOWListFragment() {

        if(NOWprojectId.equals("")||String.valueOf(different).equals("")||NOWbookId.equals("")||NOWfenceId.equals("")||KnowId.equals("")){
            XSYTools.showToastmsg(Layering_practice_Activity.this,"请把条件补充完整");
        }else{

            PerferenceService service=new PerferenceService(Layering_practice_Activity.this);
            service.save("NOWprojectId",NOWprojectId);
            service.save("NOWbookId",NOWbookId);
            service.save("NOWfenceId",NOWfenceId);
            service.save("KnowId",KnowId);

            //保存到共享参数里面方便调用，在缓存里面在存一次

            Common.setNOWprojectId(NOWprojectId);
            Common.setNOWbookId(NOWbookId);
            Common.setNOWfenceId(NOWfenceId);
            Common.setKnowId(KnowId);
            Common.setnowDifferent(NOWdifferent);
            Common.setNowKnowName(NowKnowName);
            Common.setNowProjectName(NowProjectName);
            Common.setNowBookName(NowBookName);
            Common.setNowfenceName(NowfenceName);

            XsyMap<String,String> exerciseMap=XsyMap.getInterface();
            exerciseMap.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(userId));
            exerciseMap.put(HomeWorkConstantClass.PARAM_SUBJECTID,NOWprojectId);
            exerciseMap.put(HomeWorkConstantClass.PARAM_BOOKID,NOWbookId);
            exerciseMap.put(HomeWorkConstantClass.PARAM_BOOKSECTIONID,NOWfenceId);
            exerciseMap.put(HomeWorkConstantClass.PARAM_KNOWLEDGEID,KnowId);
            exerciseMap.put(HomeWorkConstantClass.PARAM_DIFFICULT,String.valueOf(different));

            OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.PRACTICE_URL,Layering_practice_Activity.this)).params(exerciseMap).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    XSYTools.i(s);
//                    PerferenceService service=new PerferenceService(Layering_practice_Activity.this);
//                    service.save("listJson",s);
                    //缓存下来
                    Common.setListJson(s);
                    layering_practicehandler.sendMessage(XSYTools.makeNewMessage(Common.WORK_SHOW_LIST,s));

                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                }
            });

        }
    }
}
