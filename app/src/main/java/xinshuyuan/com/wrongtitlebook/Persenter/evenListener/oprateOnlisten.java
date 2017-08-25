package xinshuyuan.com.wrongtitlebook.Persenter.evenListener;

import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;

import com.xinshuyuan.xinshuyuanworkandexercise.Model.TestEntity;

import xinshuyuan.com.wrongtitlebook.View.Fragment.ExerciseTestFragment.ExerciseOPRAFragment;

/**
 * Created by Administrator on 2017/6/21.
 */

public class oprateOnlisten implements View.OnClickListener {
    private ExerciseOPRAFragment fragment;
    private TestEntity testEntity;
    private Integer itemType;
    Intent intent;
    public oprateOnlisten(ExerciseOPRAFragment exerciseOPRAFragment, TestEntity testEntity, Integer itemType) {
        this.fragment = exerciseOPRAFragment;
        this.testEntity=testEntity;
        this.itemType=itemType;
    }

    @Override
    public void onClick(View v) {
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        fragment.startActivityForResult(intent, 6);
    }

}
