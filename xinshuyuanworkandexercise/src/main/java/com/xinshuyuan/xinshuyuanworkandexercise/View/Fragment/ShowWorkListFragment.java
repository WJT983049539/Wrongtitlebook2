package com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.PerferenceService;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.WorkInfoBean;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XsyMap;
import com.xinshuyuan.xinshuyuanworkandexercise.R;
import com.xinshuyuan.xinshuyuanworkandexercise.presenter.Handler.ProjectWorkShowActivityHandler;

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
 *作业列表
 * Created by Administrator on 2017/7/5.
 */

public class ShowWorkListFragment extends Fragment{
    private View Mview;
    private String info;
    private ListView listView;
    private  String UserId;
    private String projectId;
    private List<WorkInfoBean> workInfoList;
    private ShowWorkListAdapter myadapter;
    private ProjectWorkShowActivityHandler projectWorkShowActivityHandler;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                String[] aa=info.split(",");
                projectId=aa[0];
                UserId=aa[1];

            PerferenceService service=new PerferenceService(getActivity());
            service.save("projectId",projectId);

        workInfoList=new ArrayList<WorkInfoBean>();

    }
    //得到数据
    private void getData() {
        Map map= XsyMap.getInterface();
        map.put(HomeWorkConstantClass.PARAM_STUDENTID,UserId);
        map.put(HomeWorkConstantClass.PARAM_SUBJECTID,projectId);

        OkGo.post(XSYTools.getWorkUrl(HomeWorkConstantClass.WORKLIST_URL,getActivity())).params(map).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i(s);
                workInfoList.clear();

                try {
                    JSONObject object=new JSONObject(s);
                    JSONArray array=object.getJSONArray("workList");
                    for(int i=0;i<array.length();i++){

                        JSONObject oo=array.getJSONObject(i);
                            //作业名称
                            String workName=oo.getString("workName");
                            //作业id
                            long workId=oo.getLong("id");
                            //作业状态
                            int work_state=oo.getInt("homeTestState");
                        JSONObject publitimeobject=oo.getJSONObject("publishTime");
                        String year=String.valueOf(publitimeobject.getInt("year"));
                        String month=String.valueOf(publitimeobject.getInt("month"));
                        String day=String.valueOf(publitimeobject.getInt("day"));
                        String hours=String.valueOf(publitimeobject.getInt("hours"));
                        String minutes=String.valueOf(publitimeobject.getInt("minutes"));
                        String seconds=String.valueOf(publitimeobject.getInt("seconds"));
                        //放入javabean
                        WorkInfoBean workInfoBean=new WorkInfoBean();
                        workInfoBean.setWorkId(workId);
                        workInfoBean.setWorkName(workName);
                        workInfoBean.setWorkState(work_state);
                        workInfoBean.setPubllicTime((year+"-"+month+"-"+day+" | "+hours+"-"+minutes+"-"+seconds));
                        workInfoList.add(workInfoBean);
                    }
                    myadapter=new ShowWorkListAdapter(workInfoList);
                    listView.setAdapter(myadapter);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Mview=inflater.inflate(R.layout.layout_fragment_show_worklist,container,false);
        inint();
        return Mview;
    }

    private void inint() {
        listView= (ListView) Mview.findViewById(R.id.listview_showwork_list);
        getData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WorkInfoBean workInfobean=workInfoList.get(position);
                Long workId=workInfobean.getWorkId();
                PerferenceService service=new PerferenceService(getActivity());
                service.save("NowWorkId",String.valueOf(workId));
                XSYTools.i("选择的workId为"+String.valueOf(workId));
               if(workInfobean.getWorkState()==0){
                   //没有完成，点击以后进入试题列表页面

                   projectWorkShowActivityHandler.sendMessage(XSYTools.makeNewMessage(Common.TEST_LIST_SHOW,workInfobean));

               }else if(workInfobean.getWorkState()==2){
                   //已经完成进入统计界面
                   projectWorkShowActivityHandler.sendMessage(XSYTools.makeNewMessage(Common.STATISTICE_FTAGMENT,workInfobean));
               }else if(workInfobean.getWorkState()==1){
                    //未批阅
               }
            }
        });
    }

    public void setInfo(String s, ProjectWorkShowActivityHandler projectWorkShowActivityHandler) {
        //这个是xueshengid和学科id
        info=s;
        this.projectWorkShowActivityHandler=projectWorkShowActivityHandler;
    }


    private class ShowWorkListAdapter extends BaseAdapter{
        List<WorkInfoBean> workInfoList;
        public ShowWorkListAdapter(List<WorkInfoBean> LL) {
            workInfoList=LL;
        }

        @Override
        public int getCount() {
            return workInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return workInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflate=LayoutInflater.from(getActivity());
            ViewHolder viewHolder;
            if(convertView==null){
                viewHolder=new ViewHolder();
                convertView=inflate.inflate(R.layout.show_work_item,parent,false);
                viewHolder.status= (TextView) convertView.findViewById(R.id.text_status);
                viewHolder.workinfo= (TextView) convertView.findViewById(R.id.text_info);
                viewHolder.timeText=(TextView) convertView.findViewById(R.id.text_time);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            WorkInfoBean workInfoBean=workInfoList.get(position);
            viewHolder.workinfo.setText(workInfoBean.getWorkName());
            viewHolder.timeText.setText(workInfoBean.getPubllicTime());
            String ff="";

            if(workInfoBean.getWorkState()==0){
                ff="未完成";
                viewHolder.status.setBackgroundColor(0xffec6941);
            }else if(workInfoBean.getWorkState()==1){
                viewHolder.status.setBackgroundColor(0xfbbb13);
                ff="未批阅";
            }else if(workInfoBean.getWorkState()==2){
                viewHolder.status.setBackgroundColor(0x32b16c);
                ff="已完成";
            }

            viewHolder.status.setText(ff);

            return convertView;
        }
    }

     class ViewHolder {
         TextView status;
         TextView workinfo;
         TextView timeText;
    }
}
