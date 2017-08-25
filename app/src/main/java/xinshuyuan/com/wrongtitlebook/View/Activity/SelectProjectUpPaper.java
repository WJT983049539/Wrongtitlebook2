package xinshuyuan.com.wrongtitlebook.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ibpd.xsy.varDefine.TestVarDefine;
import com.ibpd.xsy.varDefine.WrongQuestionConstantClass;
import com.lzy.imagepickeruu.ImagePicker;
import com.lzy.imagepickeruu.bean.ImageItem;
import com.lzy.imagepickeruu.ui.ImageGridActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.GetUserInfoClass;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import xinshuyuan.com.wrongtitlebook.Model.Common.XsyMap;
import xinshuyuan.com.wrongtitlebook.Persenter.workRunnable.GlideImageLoader;
import xinshuyuan.com.wrongtitlebook.R;

/**
     *选择上传试题类型的Activity
     * Created by Administrator on 2017/5/23.
     */

    public class SelectProjectUpPaper extends Activity {


            //判断图片是否上传成功
            private  Boolean FLAG=false;
            //知识点id
            private String finalKnowID;
            //学科id
            private String finalSubjectID;
            //教材id
            private String finalJiaocaiID;
            //上下册id
            private String finalfenceID;
            //上传题干
            private String point_title;
            private TextView up_condition;
            private ImageView phone;
            private ImagePicker imagePicker;
            private GridView gridView;
            private Button commit_button;
            private Button nativacommit_button;
            private EditText answer_edittext;
            private RatingBar ratingBar;
            private String finalKnowContent;

            private EditText pointEdit;
             private Float ff;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window=getWindow();
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager.LayoutParams layoutParams= window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏底部键盘，一直不会弹出
        layoutParams.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(layoutParams);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
            setContentView(R.layout.selectproject_layout);
            imagePicker = ImagePicker.getInstance();
            imagePicker.setImageLoader(new GlideImageLoader());
            Inint();
        }

        private void Inint() {

            ratingBar= (RatingBar) findViewById(R.id.ratingBar);
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                }
            });


            answer_edittext= (EditText) findViewById(R.id.answer_edittext);

            //提交的事件
            commit_button= (Button) findViewById(R.id.commit_button2);
            commit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String commitInfotUrl= XSYTools.getWrongUrl(WrongQuestionConstantClass.SAVEIMG_URL,SelectProjectUpPaper.this);
                    XsyMap map= XsyMap.getInterface();
                    Long studId= new GetUserInfoClass(SelectProjectUpPaper.this).getUserId();
                    map.put(WrongQuestionConstantClass.PARAM_STUDID,String.valueOf(studId));
                    //学科id
                    map.put(WrongQuestionConstantClass.PARAM_SUBJECTID,finalSubjectID);
                    //教材id
                    map.put(WrongQuestionConstantClass.PARAM_BOOKID,finalJiaocaiID);
                    //分册id
                    map.put(WrongQuestionConstantClass.PARAM_BOOKSECTIONID,finalfenceID);
                    //知识点id
                    map.put(WrongQuestionConstantClass.PARAM_KNOWLEDGEID,finalKnowID);
                    //知识点名字
                    map.put(WrongQuestionConstantClass.PARAM_KNOWNAME,finalKnowContent);
                    //答案与分析
                    map.put(WrongQuestionConstantClass.PARAM_REALANSWER,answer_edittext.getText().toString());


                    //题干
                    if(!point_title.equals("")){
                        map.put(WrongQuestionConstantClass.PARAM_ITEMPOINT,point_title);
                    }else {
                        XSYTools.showToastmsg(SelectProjectUpPaper.this,"图片上传中请稍后重新提交");
                        return;
                    }

                    //难度
                    float starNum=ratingBar.getRating();
                     if(starNum==1.0){
                         ff= TestVarDefine.TEST_DIFFICULTY_EAISER_GENERAL;
                    }else if(starNum==2.0){
                         ff= TestVarDefine.TEST_DIFFICULTY_EASY_GENERAL;
                    }else if(starNum==3.0){
                         ff= TestVarDefine.TEST_DIFFICULTY_GENERAL_GENERAL;
                    }else if(starNum==4.0){
                         ff= TestVarDefine.TEST_DISCRIMINATION_SMALL_MAX;
                    }else if(starNum==5.0){
                         ff= TestVarDefine.TEST_DIFFICULTY_VERYDIFFICULTY_GENERAL;
                    }
                    map.put(WrongQuestionConstantClass.PARAM_DIFFICULT,String.valueOf(ff));

                    OkGo.post(commitInfotUrl).params(map).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            XSYTools.i(s);
                                XSYTools.showToastmsg(SelectProjectUpPaper.this,"提交成功");
                                Intent intent=new Intent(SelectProjectUpPaper.this,FinishActivity.class);
                                 startActivity(intent);
                               SelectProjectUpPaper.this.finish();
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            XSYTools.i(e.toString());
                        }
                    });

                }
            });
            /**
             * 返回科目列表
             */
            nativacommit_button=(Button)findViewById(R.id.native_button);

            nativacommit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectProjectUpPaper.this.finish();
                }
            });

            gridView = (GridView) findViewById(R.id.gridview);
            phone=(ImageView)findViewById(R.id.phone);
            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imagePicker.setImageLoader(new GlideImageLoader());
                    imagePicker.setShowCamera(true);

                    Intent intent = new Intent(SelectProjectUpPaper.this, ImageGridActivity.class);
                    intent.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                    startActivityForResult(intent, ImagePicker.RESULT_CODE_ITEMS);
                }
            });
            up_condition=(TextView)findViewById(R.id.up_condition);
            up_condition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(SelectProjectUpPaper.this,SelscCondictiontActivity.class);
                    startActivityForResult(intent,6);
                }
            });
        }

        ArrayList<ImageItem> images = null;

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            switch (resultCode){
                //返回选择的知识点
                case 6:
                    Bundle bb= data.getExtras();
                    finalKnowID=bb.getString("finalKnowID");
                    finalKnowContent=bb.getString("finalKnowContent");
                     finalSubjectID=bb.getString("finalSubjectID");
                     finalJiaocaiID=bb.getString("finalJiaocaiID");
                     finalfenceID=bb.getString("finalfenceID");

                    up_condition.setText("选择的知识点:"+finalKnowContent);

                    break;
                //返回选中的image
                case ImagePicker.RESULT_CODE_ITEMS:
                    images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    MyAdapter adapter = new MyAdapter(images);
                    String path=images.get(0).path;
                    String upFileString= XSYTools.getWrongUrl(WrongQuestionConstantClass.UPLOAD_URL,SelectProjectUpPaper.this);
                    //上传
                    OkGo.post(upFileString).tag(this).params("file1",new File(path)).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            XSYTools.i(s);
                            FLAG=true;
                                //返回标题
                            try {
                                JSONObject object=new JSONObject(s);
                                point_title=object.getString("value");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);


                        }

                        @Override
                        public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                            super.upProgress(currentSize, totalSize, progress, networkSpeed);
                            XSYTools.i(totalSize+"");
                        }
                    });

                    gridView.setAdapter(adapter);
                    break;

            }
        }


        private class MyAdapter extends BaseAdapter {

            private List<ImageItem> items;

            public MyAdapter(List<ImageItem> items) {
                this.items = items;
            }

            public void setData(List<ImageItem> items) {
                this.items = items;
                notifyDataSetChanged();
            }

            @Override
            public int getCount() {
                return items.size();
            }

            @Override
            public ImageItem getItem(int position) {
                return items.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageView;
                int size = gridView.getWidth() / 3;
                if (convertView == null) {
                    imageView = new ImageView(SelectProjectUpPaper.this);
                    AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, size);
                    imageView.setLayoutParams(params);
                    imageView.setBackgroundColor(Color.parseColor("#88888888"));
                } else {
                    imageView = (ImageView) convertView;
                }
                imagePicker.getImageLoader().displayImage(SelectProjectUpPaper.this, getItem(position).path, imageView, size, size);
                return imageView;
            }
        }
    }
