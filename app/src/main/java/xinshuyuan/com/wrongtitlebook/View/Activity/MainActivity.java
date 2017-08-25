package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import xinshuyuan.com.wrongtitlebook.Model.Common.Common;
import xinshuyuan.com.wrongtitlebook.Model.Common.PerferenceService;
import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.MainHandler;
import xinshuyuan.com.wrongtitlebook.R;
import xinshuyuan.com.wrongtitlebook.View.Fragment.LanguageFragment;
import xinshuyuan.com.wrongtitlebook.View.Fragment.TopFragment;

/**
 * 主要显示的区域
 * Created by Administrator on 2017/5/22.
 */

public class MainActivity extends Activity {

    private MainHandler mainHandler;

    private GridView  menuGridView;
    private GridAdapter mGridAdapter;
    private TopFragment topFragment=null;
    private FragmentManager fragmentManager;
    private ArrayList<String > projectList;
    private String userName;
    private String projectId;
    private LanguageFragment languageFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        setContentView(R.layout.mainactivity);
        Intent intent=getIntent();
        projectList=intent.getStringArrayListExtra("subjectList");
        userName=intent.getStringExtra("userName");
        mainHandler=new MainHandler(this);
        //获取科目信息
         init();
    }

    private void init() {
        fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        topFragment=new TopFragment();
        topFragment.setInfo(userName);
        fragmentTransaction.replace(R.id.maintop_fragment,topFragment);
        fragmentTransaction.commitAllowingStateLoss();
        menuGridView= (GridView) findViewById (R.id.meungridViewgridView);

        //注册选项的监听事件
        menuGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Toast.makeText(MainActivity.this, projectList.get(position), Toast.LENGTH_SHORT).show();
                     XSYTools.i("科目选择："+projectList.get(position));
                    //把选择的科目保存起来，在后面跳转到试题列表显示
                    PerferenceService service=new PerferenceService(MainActivity.this);
                    service.save("Project",projectList.get(position));

                String nowprojectName=projectList.get(position);
                String subjectId=Common.getMap().get("nowprojectName");
                service.save("WrongProjectId",subjectId);
                mainHandler.sendMessage(XSYTools.makeNewMessage(Common.LANGUAGE_FRAGMENT,projectList.get(position)));
            }
        });

        menuGridView.setAdapter(new GridAdapter(this));
    }


    class GridAdapter extends BaseAdapter{
        private LayoutInflater inflater;

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return projectList.size();
        }

        @Override
        public Object getItem(int position) {
            return projectList.get(position);
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
//                viewHolder.title = (TextView) convertView.findViewById(R.id.project_item_text);
//                viewHolder.image = (ImageView) convertView.findViewById(R.id.project_item_iamge);
                convertView.setTag(viewHolder);
            } else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
//            viewHolder.title.setText(projectList.get(position));
//            viewHolder.image.setImageResource(R.drawable.ic_launcher);
            if(projectList.get(position).equals("语文")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.yuwen);
            }else if(projectList.get(position).equals("数学")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.shuxue);
            }else if(projectList.get(position).equals("自然")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.ziran);
            }else if(projectList.get(position).equals("科学")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.kexue);
            }else if(projectList.get(position).equals("品德与社会")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.pinde);
            }else if(projectList.get(position).equals("体育")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.tiyu);
            }else if(projectList.get(position).equals("美术")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.meishu);
            }else if(projectList.get(position).equals("音乐")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.yinyue);
            }else if(projectList.get(position).equals("信息")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.xinxi);
            }else if(projectList.get(position).equals("英语")){
                viewHolder.item_layout.setBackgroundResource(R.drawable.yingyu);
            }


            return convertView;
        }
    }
    class ViewHolder
    {
//        public TextView title;
//        public ImageView image;
        public LinearLayout item_layout;
    }

}