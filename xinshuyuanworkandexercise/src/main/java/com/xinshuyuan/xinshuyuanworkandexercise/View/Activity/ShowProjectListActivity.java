package com.xinshuyuan.xinshuyuanworkandexercise.View.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.PerferenceService;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XsyMap;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.javabean.ProjectInfo;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.TopFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;
import work.HomeWorkConstantClass;

/**
 * 科目选择的Activity
 * Created by Administrator on 2017/7/5.
 */

public class ShowProjectListActivity extends Activity{
    private  long userId;
    private String UserName;
    //保存 学科信息的集合
    private List<ProjectInfo> projectlist;
    private GridView projectGridView;
    private TopFragment topFragment=null;
    private FragmentManager fragmentManager;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
        setContentView(R.layout.work_project_list);
        Intent intent=getIntent();
        userId=intent.getLongExtra("userId",0);
        UserName=intent.getStringExtra("userName");
        projectlist=new ArrayList<ProjectInfo>();

        inint();
        setData();
    }

    private void inint() {

        projectGridView= (GridView) findViewById(R.id.meungridViewgridView);
        fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        topFragment=new TopFragment();
        topFragment.setInfo(UserName);
        fragmentTransaction.replace(R.id.maintop_fragment,topFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void setData() {

          Map<String,String> map= XsyMap.getInterface();
           map.put(HomeWorkConstantClass.PARAM_STUDENTID,String.valueOf(userId));
          OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.SUBJECT_URL,ShowProjectListActivity.this)).params(map).execute(new StringCallback() {
              @Override
              public void onSuccess(String s, Call call, Response response) {
                  projectlist.clear();
                  XSYTools.i(s);
                  try {
                      JSONObject object=new JSONObject(s);
                     JSONArray array= object.getJSONArray("subject");

                      for(int i=0;i<array.length();i++){

                        JSONObject OO=array.getJSONObject(i);
                        String name=OO.getString("subjectName");
                          long id=OO.getLong("id");
                          ProjectInfo projectInfo=new ProjectInfo();
                          projectInfo.setSubjectId(id);
                          projectInfo.setSubjectName(name);
                          projectlist.add(projectInfo);
                      }
                      projectGridView.setAdapter(new GridAdapter());
                      projectGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                          @Override
                          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                              XSYTools.i("科目选择："+projectlist.get(position).getSubjectName());

                              //转到显示作业列表的界面
                                Intent intent=new Intent(ShowProjectListActivity.this,ProjectWorkShowActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putLong("userId",userId);
                                bundle.putSerializable("SelectProject",projectlist.get(position));
                                //把选择的科目存入共享参数
                                PerferenceService service=new PerferenceService(ShowProjectListActivity.this);
                                Long projectId=projectlist.get(position).getSubjectId();
                                service.save("ProjectId",projectId);
                                intent.putExtras(bundle);
                               ShowProjectListActivity.this.startActivity(intent);
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
    class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public GridAdapter() {
            inflater = LayoutInflater.from(ShowProjectListActivity.this);
        }

        @Override
        public int getCount() {
            return projectlist.size();
        }

        @Override
        public Object getItem(int position) {
            return projectlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder viewHolder;
            if (convertView == null){

                convertView = inflater.inflate(R.layout.project_item, null);
                viewHolder = new ViewHolder();
                viewHolder.item_layout=(LinearLayout)convertView.findViewById(R.id.item_layout);
                convertView.setTag(viewHolder);
            } else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
//            viewHolder.title.setText(projectList.get(position));
//            viewHolder.image.setImageResource(R.drawable.ic_launcher);
            if(projectlist.get(position).getSubjectName().equals("语文")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.yuwen);
            }else if(projectlist.get(position).getSubjectName().equals("数学")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.shuxue);
            }else if(projectlist.get(position).getSubjectName().equals("自然")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.ziran);
            }else if(projectlist.get(position).getSubjectName().equals("科学")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.kexue);
            }else if(projectlist.get(position).getSubjectName().equals("品德与社会")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.pinde);
            }else if(projectlist.get(position).getSubjectName().equals("体育")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.tiyu);
            }else if(projectlist.get(position).getSubjectName().equals("美术")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.meishu);
            }else if(projectlist.get(position).getSubjectName().equals("音乐")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.yinyue);
            }else if(projectlist.get(position).getSubjectName().equals("信息")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.xinxi);
            }else if(projectlist.get(position).getSubjectName().equals("英语")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.yingyu);
            }


            return convertView;
        }
    }
    class ViewHolder
    {
        public LinearLayout item_layout;
    }
}