package com.xinshuyuan.xinshuyuanworkandexercise.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XsyMap;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.javabean.OneKnowInfo;
import com.xinshuyuan.xinshuyuanworkandexercise.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import work.HomeWorkConstantClass;

/**
 * 选择知识点的页面
 * Created by Administrator on 2017/7/10.
 */

public class selectKnowActivity extends Activity{
    private ListView one_know_id;
    private ListView two_know_id;
    private String userId;
    private String projectId;
    private String bookId;
    private String fenceId;
    private List<OneKnowInfo> oneList;
    private List<OneKnowInfo> twoList;
    private OneKnowAdaper oneKnowAdaper;
    private OneKnowAdaper twoKnowAdaper;

    private String NOWoneknowid;
    private String NOWoneknowName;
    private String UserName;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window=getWindow();
        WindowManager.LayoutParams layoutParams= window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_select_know);


        Intent intent=getIntent();
        userId=intent.getStringExtra("userId");
        projectId=intent.getStringExtra("projectId");
        bookId=intent.getStringExtra("bookId");
        fenceId=intent.getStringExtra("fenceId");
        UserName=intent.getStringExtra("UserName");
        inint();
        getData();


    }

    private void inint() {
        one_know_id= (ListView) findViewById(R.id.one_know_id);
        two_know_id= (ListView) findViewById(R.id.two_know_id);
        oneList=new ArrayList<OneKnowInfo>();
        twoList=new ArrayList<OneKnowInfo>();
    }

    //得到数据
    private void getData() {

        //开始得到一级知识点数据
        XsyMap<String,String> getOneKnow= XsyMap.getInterface();
        getOneKnow.put(HomeWorkConstantClass.PARAM_BOOKSECTIONID,fenceId);
        getOneKnow.put(HomeWorkConstantClass.PARAM_KNOWLEDGEID,"");
        OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.KNOWLEDGEPOINT_URL,selectKnowActivity.this)).params(getOneKnow).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                oneList.clear();
                XSYTools.i("一级知识点"+s);
                if(s.equals("[]")){
                    XSYTools.showToastmsg(selectKnowActivity.this,"该分册没有知识点");
                    Intent intent=new Intent(selectKnowActivity.this,Layering_practice_Activity.class);
                    intent.putExtra("userId",Long.valueOf(userId));
                    intent.putExtra("userName",UserName);
                    selectKnowActivity.this.startActivity(intent);
                    selectKnowActivity.this.finish();
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
                        one_know_id.setAdapter(oneKnowAdaper);
                        one_know_id.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                NOWoneknowid = oneList.get(position).getOneKnowId();
                                NOWoneknowName = oneList.get(position).getOneKnowString();
                                XsyMap<String, String> gettwoKnow = XsyMap.getInterface();
                                gettwoKnow.put(HomeWorkConstantClass.PARAM_BOOKSECTIONID, fenceId);
                                gettwoKnow.put(HomeWorkConstantClass.ERROR_TEST_PARENTID, NOWoneknowid);
                                OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.KNOWLEDGEPOINT_URL,selectKnowActivity.this)).params(gettwoKnow).execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        twoList.clear();
                                        XSYTools.i("得到的二级知识点json" + s);

                                        if (s.equals("[]")) {
                                            XSYTools.i("没有二级知识点");

                                            Intent intent = new Intent(selectKnowActivity.this, Layering_practice_Activity.class);
                                            intent.putExtra("KnowId", NOWoneknowid);
                                            intent.putExtra("knowName", NOWoneknowName);
                                            setResult(6, intent);
                                            selectKnowActivity.this.finish();

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
                                                two_know_id.setAdapter(twoKnowAdaper);


                                                two_know_id.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        NOWoneknowid = twoList.get(position).getOneKnowId();
                                                        NOWoneknowName = twoList.get(position).getOneKnowString();

                                                        Intent intent = new Intent(selectKnowActivity.this, Layering_practice_Activity.class);
                                                        intent.putExtra("KnowId", NOWoneknowid);
                                                        intent.putExtra("knowName", NOWoneknowName);
                                                        setResult(6, intent);
                                                        selectKnowActivity.this.finish();
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
                                    }
                                });

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
                XSYTools.i(e.toString());
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
            LayoutInflater layoutinflater=LayoutInflater.from(selectKnowActivity.this);
            convertView= layoutinflater.inflate(R.layout.layout_item_select_know,parent,false);
            TextView textview= (TextView) convertView.findViewById(R.id.select_know_item_textview);
            textview.setText(list.get(position).getOneKnowString());
            return convertView;
        }
    }
}
