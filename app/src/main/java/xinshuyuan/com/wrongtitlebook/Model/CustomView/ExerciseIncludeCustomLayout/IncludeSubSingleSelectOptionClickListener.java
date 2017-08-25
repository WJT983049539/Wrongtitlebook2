package xinshuyuan.com.wrongtitlebook.Model.CustomView.ExerciseIncludeCustomLayout;

import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;

import xinshuyuan.com.wrongtitlebook.Model.CustomView.SelectButton;
import xinshuyuan.com.wrongtitlebook.R;

/**
 * Created by Administrator on 2017/6/22.
 */

class IncludeSubSingleSelectOptionClickListener implements View.OnClickListener {
        private ExerciseSingleSelectLayout fragment;
    public IncludeSubSingleSelectOptionClickListener(ExerciseSingleSelectLayout exerciseSingleSelectLayout) {
        this.fragment = exerciseSingleSelectLayout;
    }

    @Override
    public void onClick(View v) {

        SelectButton btn=(SelectButton) v;
        ViewParent vp=btn.getParent().getParent();
        LinearLayout fg=(LinearLayout) vp;
        int cnt=fg.getChildCount();
        for(int i=0;i<cnt;i++){
            LinearLayout ll=(LinearLayout) fg.getChildAt(i);
            //ll下面就是真东西了，先都取消了选中再说
            for(int j=0;j<ll.getChildCount();j++){
                View btv=ll.getChildAt(j);
                if(btv instanceof SelectButton){
                    SelectButton sb=(SelectButton) btv;
                    //找到组织了
                    sb.setState(SelectButton.STATE_UNSELECTED);
                    sb.setBackgroundResource(R.drawable.multi_select_option_button_shape);
                    fragment.removeAnswer(sb.getOptionId().toString());
                }
            }
        }
        //都取消了，接下来再把当下选中的给设置为选中状态，并记录ans值
        btn.setState(btn.getState()+1);
        btn.setBackgroundResource(R.drawable.multi_select_option_button_shape_selected);
        fragment.addAnswer(btn.getOptionId().toString());

    }
}
