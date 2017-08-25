//package com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.ExerciseTestFragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.xinshuyuan.xinshuyuanworkandexercise.Model.PerferenceService;
//import com.xinshuyuan.xinshuyuanworkandexercise.R;
//import com.xinshuyuan.xinshuyuanworkandexercise.View.Fragment.BaseFragment;
//
//
///**已经掌握了本知识点
// * Created by Administrator on 2017/6/28.
// */
//
//public class VERY_GOODFragment extends BaseFragment {
//    private View Mview;
//    private String info;
//    private TextView y_good_textview;
//    private Button very_good_button;
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        Mview= inflater.inflate(R.layout.verygood_layout,container,false);
//         y_good_textview= (TextView) Mview.findViewById(R.id.very_good_textview);
//        y_good_textview.setText(info);
//        very_good_button=(Button)Mview.findViewById(R.id.very_good_button);
//        very_good_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                new Thread(new CheckLoginRunnable(getActivity())).start();
//                //转到错题列表界面
//                PerferenceService service=new PerferenceService(getActivity());
//                String Project=service.getsharedPre().getString("Project","");
//                Intent intent=new Intent(getActivity(),ShowWrongBookslistActivity.class);
//                intent.putExtra("project",Project);
//                getActivity().startActivity(intent);
//            }
//        });
//        return Mview;
//    }
//
//    public void setInfo(String info) {
//        this.info = info;
//    }
//}
