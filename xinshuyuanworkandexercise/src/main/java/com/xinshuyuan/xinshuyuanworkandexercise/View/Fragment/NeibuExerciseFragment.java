//package com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment;
//
//import android.app.Fragment;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.xinshuyuan.xinshuyuanworkandexercise.R;
//import com.xinshuyuan.xinshuyuanworkandexercise.View.Activity.ProjectWorkShowActivity;
//import com.xinshuyuan.xinshuyuanworkandexercise.presenter.Handler.ProjectWorkShowActivityHandler;
//
///**
// * 内部选择条件来获取试题的fragment
// * Created by wjt on 2017/8/14.
// */
//
//public class NeibuExerciseFragment extends Fragment{
//
//    private ProjectWorkShowActivityHandler projectWorkShowActivityHandler;
//    private ProjectWorkShowActivity projectWorkShowActivity;
//    private String work;
//    private View Mview;
//
//    public NeibuExerciseFragment(ProjectWorkShowActivityHandler projectWorkShowActivityHandler, ProjectWorkShowActivity projectWorkShowActivity, String work) {
//        this.projectWorkShowActivity=projectWorkShowActivity;
//        this.projectWorkShowActivityHandler=projectWorkShowActivityHandler;
//        this.work=work;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//
//        Mview=inflater.inflate(R.layout.neibu_exercise_selsect_layout,container,false);
//
//
//        return Mview;
//    }
//
//}
