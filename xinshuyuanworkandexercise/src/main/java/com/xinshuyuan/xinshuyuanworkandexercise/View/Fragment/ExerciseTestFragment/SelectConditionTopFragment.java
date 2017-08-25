package com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.xinshuyuan.xinshuyuanworkandexercise.Model.javabean.ProjectInfo;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Activity.selectKnowActivity;

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

/**顶部显示选择条件的fragment
 * Created by Administrator on 2017/7/26.
 */

public class SelectConditionTopFragment extends Fragment {
    private String jsonInfo;
    private Handler layering_practiceHandler;
    private View Mview;
    private DropdownMenu topsubject_dm_dropdown;
    private DropdownMenu topjiaocai_dm_dropdown;
    private DropdownMenu topfence_dm_dropdown2;
    //    private DropdownMenu know_dm_dropdown3;
    private TextView topwork_xuanti;
    private TextView topknow_selsct;

    //现在选择的id
    private String NOWprojectId="";
    //现在选择的教材id
    private String NOWbookId="";
    //现在选择的分册id
    private String NOWfenceId="";
    //现在选择的知识点id
    private String KnowId="";
    //现在选择的难度
    private String KnowDifferent="";

    private String NowProjectName="";
    private String NowBookName="";
    private String NowfenceName="";
    private String NowKnowName="";

    private String userId;



    //存有科目名和id的集合
    private Map<String,Long> projectinfoMap;
    private List<String> projectNameList;

    //存有教材版本的集合
    private  Map<String,Long> bookinfoMap;
    private List<String> bookinfolist;

    //存有上下册的集合
    private Map<String,Long> fenceinfoMap;
    private List<String> fenceinfolist;




    private Activity layering_practice_activity;

    public SelectConditionTopFragment(Activity layering_practice_activity) {
       this.layering_practice_activity=layering_practice_activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Mview=inflater.inflate(R.layout.layout_topfragment,container,false);
        inintData();
        inint();
        setDate();
        return Mview;
    }

    private void setDate() {
        XsyMap<String,String> projectMap=XsyMap.getInterface();
        projectMap.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(userId));

        OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.SUBJECT_URL,layering_practice_activity)).params(projectMap).execute(new StringCallback() {
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
                    topsubject_dm_dropdown.setAdapter(new ArrayDropdownAdapter(layering_practice_activity, R.layout.dropdown_light_item_1line, projectNameList));
                    topsubject_dm_dropdown.setOnItemClickListener(new OnDropdownItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            NOWprojectId=String.valueOf(projectinfoMap.get(projectNameList.get(position)));
                            //现在要显示的String保存到缓存里面
                            NowProjectName=projectNameList.get(position);
                            XsyMap<String,String> JiaoCaibookMap=XsyMap.getInterface();
                            JiaoCaibookMap.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(userId));
                            JiaoCaibookMap.put(HomeWorkConstantClass.PARAM_SUBJECTID,NOWprojectId);

                            OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.BOOK_URL,layering_practice_activity)).params(JiaoCaibookMap).execute(new StringCallback() {
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
                                        topjiaocai_dm_dropdown.setAdapter(new ArrayDropdownAdapter(layering_practice_activity, R.layout.dropdown_light_item_1line, bookinfolist));
                                        topjiaocai_dm_dropdown.setOnItemClickListener(new OnDropdownItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                NOWbookId=String.valueOf(bookinfoMap.get(bookinfolist.get(position)));
                                                NowBookName=bookinfolist.get(position);
                                                XsyMap<String,String> getFenceMap=XsyMap.getInterface();
                                                getFenceMap.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(userId));
                                                getFenceMap.put(HomeWorkConstantClass.PARAM_BOOKID,NOWbookId);

                                                OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.BOOKSECTION_URL,layering_practice_activity)).params(getFenceMap).execute(new StringCallback() {
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
                                                            topfence_dm_dropdown2.setAdapter(new ArrayDropdownAdapter(layering_practice_activity, R.layout.dropdown_light_item_1line, fenceinfolist));
                                                            topfence_dm_dropdown2.setOnItemClickListener(new OnDropdownItemClickListener() {
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

    //初始化数据
    private void inintData() {
        PerferenceService service =new PerferenceService(layering_practice_activity);
        //得到userId
        userId=String.valueOf(service.getsharedPre().getLong("WORKuserId",0));
        //得到选择的参数

        NOWprojectId=Common.getNOWprojectId();
        NOWbookId=Common.getNOWbookId();
        NOWfenceId=Common.getNOWfenceId();
        KnowId=Common.getknowId();
        NowProjectName=Common.getnowProjectName();
        NowBookName=Common.getnowBookName();
        NowfenceName=Common.getNowfenceName();
        NowKnowName=Common.getNowKnowName();
        KnowDifferent=Common.getnowDifferent();


        if("".equals(NOWprojectId)||"".equals(NOWbookId)||"".equals(NOWfenceId)||"".equals(KnowId)){

            NOWprojectId= service.getsharedPre().getString("NOWprojectId","");
            NOWbookId= service.getsharedPre().getString("NOWbookId","");
            NOWfenceId= service.getsharedPre().getString("NOWfenceId","");
            KnowId= service.getsharedPre().getString("KnowId","");

        }


    }

    private void inint() {
        projectinfoMap=new HashMap<String,Long>();
        projectNameList=new ArrayList<String>();

        bookinfolist=new ArrayList<String>();
        bookinfoMap=new HashMap<String,Long>();

        fenceinfoMap=new HashMap<String,Long>();
        fenceinfolist=new ArrayList<String>();
        //选题按钮
        topwork_xuanti= (TextView) Mview.findViewById(R.id.top_work_xuanti);
        //学科下拉
        topsubject_dm_dropdown= (DropdownMenu) Mview.findViewById(R.id.top_subject_dm_dropdown);
        topsubject_dm_dropdown.setTitle(NowProjectName);
        //教材下拉
        topjiaocai_dm_dropdown= (DropdownMenu) Mview.findViewById(R.id.top_jiaocai_dm_dropdown);
        topjiaocai_dm_dropdown.setTitle(NowBookName);
        //分册下拉
        topfence_dm_dropdown2= (DropdownMenu) Mview.findViewById(R.id.top_fence_dm_dropdown2);
        topfence_dm_dropdown2.setTitle(NowfenceName);
        //知识点选择
        topknow_selsct=(TextView)Mview.findViewById(R.id.top_know_selsct);
        topknow_selsct.setText(NowKnowName);

        topknow_selsct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NOWprojectId.equals("")||NOWbookId.equals("")||NOWfenceId.equals("")){

                    XSYTools.showToastmsg(layering_practice_activity,"请把条件选择完整！");

                }else{

                    Intent intent=new Intent(layering_practice_activity,selectKnowActivity.class);
                    intent.putExtra("userId",String.valueOf(userId));
                    intent.putExtra("projectId",NOWprojectId);
                    intent.putExtra("bookId",NOWbookId);
                    intent.putExtra("fenceId",NOWfenceId);
                    intent.putExtra("UserName","");
                    startActivityForResult(intent,6);
                }
            }
        });



        //随机选题的按钮
        topwork_xuanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //现在不做以后看做不做成随机选题
                XSYTools.showToastmsg(getActivity(),"随机叫题，以后再说");
            }
        });

    }

    public void setInfo(String s , Handler layering_practiceHandler) {
        jsonInfo=s;
        this.layering_practiceHandler=layering_practiceHandler;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bb= data.getExtras();
        KnowId=bb.getString("KnowId");
        String KnowString=bb.getString("knowName");
        NowKnowName=KnowString;

        topknow_selsct.setText(KnowString);
        //显示试题列表
        SHOWListFragment();
    }

    private void SHOWListFragment() {

        if(NOWprojectId.equals("")||NOWprojectId.equals("")||NOWbookId.equals("")||NOWfenceId.equals("")||KnowId.equals("")){
            XSYTools.showToastmsg(layering_practice_activity,"请把条件补充完整");
        }else{

            PerferenceService service=new PerferenceService(layering_practice_activity);
            service.save("NOWprojectId",NOWprojectId);
            service.save("NOWbookId",NOWbookId);
            service.save("NOWfenceId",NOWfenceId);
            service.save("KnowId",KnowId);

            //保存到共享参数里面方便调用，在缓存里面在存一次

            Common.setNOWprojectId(NOWprojectId);
            Common.setNOWbookId(NOWbookId);
            Common.setNOWfenceId(NOWfenceId);
            Common.setKnowId(KnowId);

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

            OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.PRACTICE_URL,layering_practice_activity)).params(exerciseMap).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    XSYTools.i(s);
//                    PerferenceService service=new PerferenceService(Layering_practice_Activity.this);
//                    service.save("listJson",s);
                    //缓存下来
                    Common.setListJson(s);
                    layering_practiceHandler.sendMessage(XSYTools.makeNewMessage(Common.WORK_SHOW_LIST,s));

                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                }
            });

        }
    }}
