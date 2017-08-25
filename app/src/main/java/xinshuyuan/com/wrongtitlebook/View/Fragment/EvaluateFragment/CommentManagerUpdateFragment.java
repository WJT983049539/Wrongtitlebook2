package xinshuyuan.com.wrongtitlebook.View.Fragment.EvaluateFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lzy.imagepickeruu.ImagePicker;
import com.lzy.imagepickeruu.bean.ImageItem;
import com.lzy.imagepickeruu.ui.ImageGridActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.Common;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.PerferenceService;
import com.xinshuyuan.xinshuyuanworkandexercise.Model.XsyMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.richeditor.RichEditor;
import okhttp3.Call;
import okhttp3.Response;
import work.StudentCommentConstantClass;
import xinshuyuan.com.wrongtitlebook.Model.CommentInfo;
import xinshuyuan.com.wrongtitlebook.Model.Common.XSYTools;
import xinshuyuan.com.wrongtitlebook.Model.StudentInfo;
import xinshuyuan.com.wrongtitlebook.Persenter.Handler.CommentManaHandler;
import xinshuyuan.com.wrongtitlebook.R;

import static android.app.Activity.RESULT_OK;

/**
 * 修改评论的界面
 * Created by WJT on 2017/8/2.
 */

public class CommentManagerUpdateFragment extends BaseFragment{
    private final static String upPictureUrll="apache-tomcat-6.0.39\\webapps\\evaluation\\upload\\img";
    private final static String upvideoUrl="apache-tomcat-6.0.39\\webapps\\evaluation\\upload\\video";
    private final static int IMAGE_PICKER=99;
    public static final int REQUEST_CODE_SELECT = 100;

    private Activity context;
    private CommentManaHandler commentManaHandler;
    private CommentInfo commentinfo;
    //id
    private int comment_id;
    //被评论人
    private String conmentorName;
    //一级的评论栏目
    private String Commentary;
    //评论内容
    private String CommentContent;
    //评论人
    private String studentName;
    //学期
    private String semesterName;
    //二级栏目
    private String secondItemName;
    //评论时间
    private String commentTime;
    //内容edit
    private EditText Edit_content;
    //评价人
    private TextView textView_pingjiaren;
    //本机用户
    private TextView textView_user;
    //时间
    private TextView textView_time;

    private String Timee;
    private Button button_commit;
    //选择学生
    private Spinner spinner_pingjiaren;
    private EditText update_edit_score;

    private RichEditor mEditor;
    private TextView mPreview;
    //本机xueshengid;
    Long StudentId;
    private Long NowBPLRId;
    private String NowBPLRName="";

    //的分
    private Double Nowscore;
    //储存全部学生的集合
    private List<StudentInfo> AllStudent=null;

    private List<String> allstudentName=null;


    //全体学生适配器
    private ArrayAdapter arrayAdapter;

    public CommentManagerUpdateFragment(Activity context, CommentManaHandler commentManaHandler) {
        this.context=context;
        this.commentManaHandler=commentManaHandler;
    }

    public CommentManagerUpdateFragment(){}
    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_updatecomment_layout;
    }

    @Override
    protected void initData(Bundle arguments) {
        super.initData(arguments);
        commentinfo=(CommentInfo)getArguments().getSerializable("CommentInfo");
        comment_id=commentinfo.getComment_id();
        conmentorName=commentinfo.getConmentorName();
        Commentary=commentinfo.getCommentary();
        CommentContent=commentinfo.getCommentContent();
        studentName=commentinfo.getStudentName();
        semesterName=commentinfo.getSemesterName();
        secondItemName=commentinfo.getSecondItemName();
        commentTime=commentinfo.getCommentTime();
        Nowscore= commentinfo.getScore();

        PerferenceService service=new PerferenceService(getActivity());
        StudentId=service.getsharedPre().getLong("EvaluateId",0);

    }

    @Override
    protected void initView() {
        super.initView();
        update_edit_score=fVB(R.id.update_edit_score);
        update_edit_score.setText(String.valueOf(Nowscore));
        AllStudent=new ArrayList<StudentInfo>();
        allstudentName=new ArrayList<String>();
//        Edit_content=fVB(R.id.Edit_content);
//        textView_pingjiaren=fVB(R.id.textView_pingjiaren);
        textView_user=fVB(R.id.textView_user);
        textView_time=fVB(R.id.textView_time);
        //提交按钮
        button_commit=fVB(R.id.button_commit);
//        Edit_content.setText(CommentContent);
//        textView_pingjiaren.setText(conmentorName);
        textView_user.setText(studentName);
        //选择评价人的spinner
        spinner_pingjiaren=fVB(R.id.spinner_pingjiaren);

        getAllStudent();




        try {
            JSONObject object=new JSONObject(commentTime);
            String year=object.getString("year");
            String month=object.getString("month");
            String day=object.getString("day");
            String hours=object.getString("hours");
            String minutes=object.getString("minutes");
            String seconds=object.getString("seconds");
             Timee=year+"/"+month+"/"+day+" "+hours+":"+minutes+":"+seconds;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        textView_time.setText(Timee);
    }


    @Override
    protected void setListener() {
        super.setListener();



        spinner_pingjiaren.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NowBPLRId= AllStudent.get(position).getId();
                NowBPLRName=AllStudent.get(position).getStudentName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        mEditor = fVB(R.id.editor);
        mEditor.setHtml(CommentContent);
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.RED);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("Insert text here...");
        //mEditor.setInputEnabled(false);
        mPreview = fVB(R.id.preview);
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override public void onTextChange(String text) {
                mPreview.setText(text);
            }
        });

        fVB(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.undo();
            }
        });

        fVB(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.redo();
            }
        });

        fVB(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBold();
            }
        });

        fVB(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        fVB(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        fVB(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });

        fVB(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){
                mEditor.setStrikeThrough();
            }
        });

        fVB(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        fVB(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        fVB(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        fVB(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        fVB(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        fVB(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        fVB(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });

        fVB(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        fVB(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.argb(0,255,255,255) : Color.BLUE);
                isChanged = !isChanged;
            }
        });

        fVB(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        fVB(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        fVB(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        fVB(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        fVB(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        fVB(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBlockquote();
            }
        });

        fVB(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {


                mEditor.setBullets();
            }
        });

        fVB(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

        fVB(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                //先上传选择的图片,调用选择器
                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT);

            }
        });
        //视频选择器
        fVB(R.id.action_insert_video).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                XSYTools.delayalertMsg(getActivity(),"提示","本activity会销毁，稍后再解决");


                //和 图片一样先上传到服务器，然后在web上加载出来
                //调出视频选择器
                // 进入相册 以下是例子：用不到的api可以不写
//                PictureSelector.create(context)
//                        .openGallery(PictureMimeType.ofVideo())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
//                        //.theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
//                        //.maxSelectNum()// 最大图片选择数量 int
//                        .minSelectNum(1)// 最小选择数量 int
//                        .imageSpanCount(4)// 每行显示个数 int
//                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
////                        .previewImage()// 是否可预览图片 true or false
////                        .previewVideo()// 是否可预览视频 true or false
//                        //.enablePreviewAudio(true) // 是否可播放音频 true or false
//                        .compressGrade(Luban.CUSTOM_GEAR)// luban压缩档次，默认3档 Luban.THIRD_GEAR、Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
////                        .isCamera()// 是否显示拍照按钮 true or false
//                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
//                        .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
//                        .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
////                        .enableCrop()// 是否裁剪 true or false
////                        .compress()// 是否压缩 true or false
//                        .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
////                        .glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
////                        .withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
////                        .hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
//                        .isGif(true)// 是否显示gif图片 true or false
////                        .freeStyleCropEnabled()// 裁剪框是否可拖拽 true or false
////                        .circleDimmedLayer()// 是否圆形裁剪 true or false
////                        .showCropFrame()// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
////                        .showCropGrid()// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
////                        .openClickSound()// 是否开启点击声音 true or false
////                        .selectionMedia()// 是否传入已选图片 List<LocalMedia> list
////                        .previewEggs()// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
////                        .cropCompressQuality()// 裁剪压缩质量 默认90 int
////                        .compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效 int
////                        .compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效  int
////                        .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
////                        .rotateEnabled() // 裁剪是否可旋转图片 true or false
////                        .scaleEnabled()// 裁剪是否可放大缩小图片 true or false
////                        .videoQuality()// 视频录制质量 0 or 1 int
////                        .videoSecond()// 显示多少秒以内的视频or音频也可适用 int
////                        .recordVideoSecond()//视频秒数录制 默认60s int
//                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code





            }
        });

        fVB(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
            }
        });
        fVB(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.insertTodo();
            }
        });





            //提交啦
        button_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,String> commitUpdate= XsyMap.getInterface();
                //评论信息Id：id
                commitUpdate.put(StudentCommentConstantClass.PARAM_ID,String.valueOf(comment_id));
                //内容
                commitUpdate.put(StudentCommentConstantClass.PARAM_CONTENT,mEditor.getHtml());
                //被评论人
                commitUpdate.put(StudentCommentConstantClass.PARAM_STUDENTID,String.valueOf(NowBPLRId));
                //评论人他自己
                commitUpdate.put(StudentCommentConstantClass.PARAM_CREATORID,String.valueOf(StudentId));
                //得分
                commitUpdate.put(StudentCommentConstantClass.PARAM_SCORE,update_edit_score.getText().toString());
                OkGo.post(com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools.getEvaluateUrl(StudentCommentConstantClass.COMMENTEDIT_URL,getActivity())).params(commitUpdate).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        XSYTools.i("更新评论成功返回的数据"+s);
                        XSYTools.showToastmsg(getActivity(),"更新成功");

                        //转到评论管理页面
                        commentManaHandler.sendMessage(XSYTools.makeNewMessage(Common.COMMENTMANAGE,""));
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        XSYTools.i("更新评论报错数据"+e.toString());
                    }
                });
            }
        });

    }









    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {

            if (data != null && requestCode == REQUEST_CODE_SELECT) {

                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                ArrayList<File> imageFileList=new ArrayList<File>();
                imageFileList.clear();
                for(ImageItem imageItem:images){
                    File file=new File(imageItem.path);
                    imageFileList.add(file);
                }


                //得到图片路径
                String ImagePath=images.get(0).path;
                String upPictureUrl= com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools.getEvaluateUrl(StudentCommentConstantClass.ADDFILES_URL,getActivity());
                OkGo.post(upPictureUrl).addFileParams("ImageFile",imageFileList).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        XSYTools.i("上传图片成功"+s);


                        try {
                            JSONObject object=new JSONObject(s);
                            String fileAddress=object.getString("msg");
                            String rellyAddress= com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools.getEvaluateUrl(fileAddress,getActivity());
                            XSYTools.i(rellyAddress);


                            //上传成功以后网页显示图片
                            mEditor.insertImage(rellyAddress,"dachshund");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        XSYTools.i("上传图片失败"+e.toString());
                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        super.upProgress(currentSize, totalSize, progress, networkSpeed);

                        XSYTools.i("进度="+currentSize/totalSize*100+"%");

                    }
                });

            }

        //这个是视频
        }
        else  if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case PictureConfig.CHOOSE_REQUEST:
//                    // 图片选择结果回调
//                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
//                    // 例如 LocalMedia 里面返回三种path
//                    // 1.media.getPath(); 为原图path
//                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
//                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
//                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
//                    DebugUtil.i(TAG, "onActivityResult:" + selectList.size());
//                    break;
        }
        }

                    //开始上传视频啦，上传成功后调用 PictureSelector.create(MainActivity.this).externalPictureVideo(video_path);，清除缓存
//
//                    String upVideoUrl= joinUrl(StudentCommentConstantClass.ADDFILES_URL,getActivity());
//                    OkGo.post(upVideoUrl).params("file1", new File(selectList.get(0).getPath())).execute(new StringCallback() {
//                        @Override
//                        public void onSuccess(String s, Call call, Response response) {
//                                XSYTools.i("上传视频成功"+s);
//                        }
//
//                        @Override
//                        public void onError(Call call, Response response, Exception e) {
//                            super.onError(call, response, e);
//                            XSYTools.i("上传视频失败"+e.toString());
//                        }
//
//                        @Override
//                        public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
//                            super.upProgress(currentSize, totalSize, progress, networkSpeed);
//                            XSYTools.i("上传进度"+currentSize/totalSize*100+"%");
//                        }
//                    });
//
//
//
//


    /**
     * 获得全部学生的方法
     */
    private void getAllStudent() {
        HashMap<String,String> getBPLRmap=XsyMap.getInterface();
        getBPLRmap.put(StudentCommentConstantClass.PARAM_STUDENTID,String.valueOf(StudentId));
        getBPLRmap.put(StudentCommentConstantClass.PARAM_COMMENTVAL,"");
        OkGo.post(com.xinshuyuan.xinshuyuanworkandexercise.Model.XSYTools.getEvaluateUrl(StudentCommentConstantClass.GETSTUDENTLIST_URL,context)).params(getBPLRmap).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                XSYTools.i("得到学生列表信息"+s);
                AllStudent.clear();
                allstudentName.clear();
                try {
                    JSONArray array=new JSONArray(s);

                    for(int i=0;i<array.length();i++){
                        JSONObject object= array.getJSONObject(i);
                        StudentInfo studentInfo=new StudentInfo();
                        String stuName=object.getString("studName");
                        Long id=object.getLong("id");
                        studentInfo.setId(id);
                        studentInfo.setStudentName(stuName);
                        AllStudent.add(studentInfo);
                        allstudentName.add(stuName);
                    }
                    arrayAdapter= new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, allstudentName);
                    spinner_pingjiaren.setAdapter(arrayAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                XSYTools.i("得到学生列表错误信息"+e.toString());
            }
        });
    }

    }






