package xinshuyuan.com.wrongtitlebook.Model.CustomView.KeyBord;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import xinshuyuan.com.wrongtitlebook.R;

public class DigitPasswordKeyPad extends View {
    private Context ctx = null;
    private View mmView;
    private static String digitnum = "";
    private String bb="";
    private int length = 20;
    private LinearLayout lin1;
    private LinearLayout lin2;
    private Button digitkeypad_1;
    private Button digitkeypad_2;
    private Button digitkeypad_3;
    private Button digitkeypad_4;
    private Button digitkeypad_5;
    private Button digitkeypad_6;
    private Button digitkeypad_7;
    private Button digitkeypad_8;
    private Button digitkeypad_9;
    private Button digitkeypad_0;
    private Button digitkeypad_c;
    private Button digitkeypad_ok;
    private Button digitkeypad_point;
    private Button abc;
    private Button shuzizhuanhuan;
    private EditText digitkeypad_edittext;
    private EditText editt;
    private boolean isPwd;
    private Button zimujianpan_jiahao;
    private Button zimujianpan_jianhao;
    private Button zimujianpan_cheng;
    private Button zimujianpan_chu;
    private Button zimujianpan_dengyu;
    private Button zimujianpan_nodengyu;
    private Button zimujianpan_gantanhao;
    private Button zimujianpan_dayu;
    private Button zimujianpan_dayu_;
    private Button zimujianpan_xiaoyu;
    private Button zimujianpan_xiaoyu_;
    private Button zimujianpan_duihao;
    private Button zimujianpan_cuohao;
    private Button zimujianpan_xinghao;
    private Button zimujianpan_jianghao;
    private Button zimujianpan_baifenhao;
    private Button zimujianpan_shangjian;
    private Button zimujianpan_yu;
    private Button zimujianpan_zuokuohao;
    private Button zimujianpan_youkuohao;
    private Button zimujianpan_yinhao;
    private Button zimujianpan_fenhao;
    private Button zimujianpan_shuangyinhao;
    private Button zimujianpan_wenhao;
    private Button zimujianpan_juhao;
    private Button shuzishanchuanniu;
    private Button shanchuanniu;
    private EditText edit;
    public DigitPasswordKeyPad(Context ctx, EditText et) {
        super(ctx);
        this.ctx = ctx;
        this.edit=et;
    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
    }

    public void setEditTextIsPwd(boolean ispwd) {
        if (ispwd) {
            digitkeypad_edittext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            digitkeypad_edittext.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        this.isPwd = ispwd;
    }

    public View setup() {
        LayoutInflater lif = LayoutInflater.from(ctx);
        Activity activity=(Activity)this.ctx;
        mmView = lif.inflate(R.layout.customkeyborada, null);
        // 初始化 对象
        zimujianpan_jiahao= (Button)mmView.findViewById(R.id.btn_jia);
        zimujianpan_jianhao=(Button)mmView.findViewById(R.id.btn_jian);
        zimujianpan_cheng=(Button)mmView.findViewById(R.id.btn_cheng);
        zimujianpan_chu=(Button)mmView.findViewById(R.id.btn_chu);
        zimujianpan_dengyu=(Button)mmView.findViewById(R.id.btn_dengyu);
        zimujianpan_nodengyu=(Button)mmView.findViewById(R.id.btn_nodengyu);
        zimujianpan_gantanhao=(Button)mmView.findViewById(R.id.btn_gantai);
        zimujianpan_dayu=(Button)mmView.findViewById(R.id.btn_dayu);
        zimujianpan_dayu_=(Button)mmView.findViewById(R.id.dayu_);
        zimujianpan_xiaoyu=(Button)mmView.findViewById(R.id.xiaoyu);
        zimujianpan_xiaoyu_=(Button)mmView.findViewById(R.id.xiaoyu_);
        zimujianpan_duihao=(Button)mmView.findViewById(R.id.duihao);
        zimujianpan_cuohao=(Button)mmView.findViewById(R.id.btn_cuohao);
        zimujianpan_xinghao=(Button)mmView.findViewById(R.id.btn_xinghao);
        zimujianpan_jianghao=(Button)mmView.findViewById(R.id.btn_jianghao);
        zimujianpan_baifenhao=(Button)mmView.findViewById(R.id.btn_baifenhao);
        zimujianpan_shangjian=(Button)mmView.findViewById(R.id.btn_ding);
        zimujianpan_yu=(Button)mmView.findViewById(R.id.btn_yu);
        zimujianpan_zuokuohao=(Button)mmView.findViewById(R.id.btn_zkh);
        zimujianpan_youkuohao=(Button)mmView.findViewById(R.id.btn_rkh);
        zimujianpan_yinhao=(Button)mmView.findViewById(R.id.btn_yinhao);
        zimujianpan_fenhao=(Button)mmView.findViewById(R.id.btn_fenhao);
        zimujianpan_shuangyinhao=(Button)mmView.findViewById(R.id.btn_s_yin);
        zimujianpan_wenhao=(Button)mmView.findViewById(R.id.btn_wenhao);
        zimujianpan_juhao=(Button)mmView.findViewById(R.id.btn_juhao);
        shanchuanniu=(Button)mmView.findViewById(R.id.btn_shanchu);
        shuzishanchuanniu=(Button)mmView.findViewById(R.id.btn_huitui);




        digitkeypad_1 = (Button) mmView.findViewById(R.id.btn_1);
        digitkeypad_2 = (Button) mmView.findViewById(R.id.btn_2);
        digitkeypad_3 = (Button) mmView.findViewById(R.id.btn_3);
        digitkeypad_4 = (Button) mmView.findViewById(R.id.btn_4);
        digitkeypad_5 = (Button) mmView.findViewById(R.id.btn_5);
        digitkeypad_6 = (Button) mmView.findViewById(R.id.btn_6);
        digitkeypad_7 = (Button) mmView.findViewById(R.id.btn_7);
        digitkeypad_8 = (Button) mmView.findViewById(R.id.btn_8);
        digitkeypad_9 = (Button) mmView.findViewById(R.id.btn_9);
        digitkeypad_0 = (Button) mmView.findViewById(R.id.btn_0);
        abc=(Button) mmView.findViewById(R.id.btn_t);
        shuzizhuanhuan=(Button) mmView.findViewById(R.id.btn_yiersan);
        digitkeypad_point = (Button) mmView.findViewById(R.id.btn_dian);
        lin1 = (LinearLayout) mmView.findViewById(R.id.table_num_1);
        //数字键盘
        lin2 = (LinearLayout) mmView.findViewById(R.id.num_2);

        digitkeypad_c = (Button) mmView.findViewById(R.id.btn_d);
        digitkeypad_ok = (Button) mmView.findViewById(R.id.bt_w);




        if(edit.getText().toString()!=null){
            //editt=edit;

            digitnum=edit.getText().toString();
            System.out.println("得到什么了？？"+digitnum);
        }
        // 添加点击事件
        DigitPasswordKeypadOnClickListener dkol = new DigitPasswordKeypadOnClickListener();
        zimujianpan_jiahao.setOnClickListener(dkol);
        zimujianpan_jianhao.setOnClickListener(dkol);
        zimujianpan_cheng.setOnClickListener(dkol);
        zimujianpan_chu.setOnClickListener(dkol);
        zimujianpan_dengyu.setOnClickListener(dkol);
        zimujianpan_nodengyu.setOnClickListener(dkol);
        zimujianpan_gantanhao.setOnClickListener(dkol);
        zimujianpan_dayu.setOnClickListener(dkol);
        zimujianpan_dayu_.setOnClickListener(dkol);
        zimujianpan_xiaoyu.setOnClickListener(dkol);
        zimujianpan_xiaoyu_.setOnClickListener(dkol);
        zimujianpan_duihao.setOnClickListener(dkol);
        zimujianpan_cuohao.setOnClickListener(dkol);
        zimujianpan_xinghao.setOnClickListener(dkol);
        zimujianpan_jianghao.setOnClickListener(dkol);
        zimujianpan_baifenhao.setOnClickListener(dkol);
        zimujianpan_shangjian.setOnClickListener(dkol);
        zimujianpan_yu.setOnClickListener(dkol);
        zimujianpan_zuokuohao.setOnClickListener(dkol);
        zimujianpan_youkuohao.setOnClickListener(dkol);
        zimujianpan_yinhao.setOnClickListener(dkol);
        zimujianpan_fenhao.setOnClickListener(dkol);
        zimujianpan_shuangyinhao.setOnClickListener(dkol);
        zimujianpan_wenhao.setOnClickListener(dkol);
        zimujianpan_juhao.setOnClickListener(dkol);
        shanchuanniu.setOnClickListener(dkol);
        shuzishanchuanniu.setOnClickListener(dkol);





        digitkeypad_1.setOnClickListener(dkol);
        digitkeypad_2.setOnClickListener(dkol);
        digitkeypad_3.setOnClickListener(dkol);
        digitkeypad_4.setOnClickListener(dkol);
        digitkeypad_5.setOnClickListener(dkol);
        digitkeypad_6.setOnClickListener(dkol);
        digitkeypad_7.setOnClickListener(dkol);
        digitkeypad_8.setOnClickListener(dkol);
        digitkeypad_9.setOnClickListener(dkol);
        digitkeypad_0.setOnClickListener(dkol);
        digitkeypad_point.setOnClickListener(dkol);
        digitkeypad_c.setOnClickListener(dkol);
        abc.setOnClickListener(dkol);
        shuzizhuanhuan.setOnClickListener(dkol);
        return mmView;
    }


    private class DigitPasswordKeypadOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            int viewId = v.getId();

            switch (viewId) {
                case R.id.btn_1:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += 1;
                    }
                    break;
                case R.id.btn_2:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += 2;
                    }
                    break;
                case R.id.btn_3:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += 3;
                    }
                    break;
                case R.id.btn_4:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += 4;
                    }
                    break;
                case R.id.btn_5:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += 5;
                    }
                    break;
                case R.id.btn_6:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += 6;
                    }
                    break;
                case R.id.btn_7:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += 7;
                    }
                    break;
                case R.id.btn_8:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += 8;
                    }
                    break;
                case R.id.btn_9:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += 9;
                    }
                    break;
                case R.id.btn_0:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += 0;
                    }

                    break;
                case R.id.btn_dian:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += ".";
                    }

                    break;
                case R.id.btn_t://转换字符键盘

                    if(lin2.getVisibility()==View.VISIBLE){
                        lin2.setVisibility(View.GONE);
                        lin1.setVisibility(View.VISIBLE);
                    }
                    digitnum=digitnum;
                    break;
                case R.id.btn_yiersan://转数字键盘
                    if(lin1.getVisibility()==View.VISIBLE){
                        lin1.setVisibility(View.GONE);
                        lin2.setVisibility(View.VISIBLE);
                    }
                    digitnum=digitnum;
                    break;
                //加号
                case R.id.btn_jia:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +="+";
                    }
                    break;

                //减号
                case R.id.btn_jian:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += "一";
                    }



                    break;
                //乘
                case R.id.btn_cheng:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += "×";
                    }

                    break;
                case R.id.btn_chu:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += "÷";
                    }



                    break;
                case R.id.btn_dengyu:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += "=";
                    }


                    break;
                case R.id.btn_nodengyu:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +="≠";
                    }


                    break;
                case R.id.btn_gantai:

                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +="!";
                    }
                    break;
                case R.id.btn_dayu:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +=">";
                    }


                    break;
                case R.id.dayu_:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +="≥";
                    }

                    break;
                case R.id.xiaoyu:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +="<";
                    }




                    break;
                case R.id.xiaoyu_:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +="≤";
                    }


                    break;

                case R.id.duihao:

                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +="√";
                    }


                    break;
                case R.id.btn_cuohao:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +="×";
                    }

                    break;



                case R.id.btn_xinghao:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +="*";
                    }


                    break;
                case R.id.btn_jianghao:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +="#";
                    }
                    break;
                case R.id.btn_baifenhao:

                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +="%";
                    }


                    break;
                case R.id.btn_ding:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +="^";
                    }


                    break;
                case R.id.btn_yu:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +="&";
                    }


                    break;
                case R.id.btn_zkh:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +="(";
                    }


                    break;
                case R.id.btn_rkh:

                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +=")";
                    }


                    break;
                case R.id.btn_yinhao:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +=":";
                    }

                    break;
                case R.id.btn_fenhao:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +=";";
                    }

                    break;
                case R.id.btn_wenhao:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +="?";
                    }


                    break;
                case R.id.btn_juhao:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +="。";
                    }


                case R.id.btn_shanchu:
                    if(digitnum.length()==0){
                        digitnum="";
                    }
//            	else{
//            	if (digitnum.length() == length) {
//            		return;
//            	}
                    else {
                        digitnum=digitnum.substring(0, digitnum.length()-1);

                    }
                    break;
                case R.id.btn_huitui:
                    if(digitnum.length()==0){
                        digitnum="";
                    }
//            	else{
//            	if (digitnum.length() == length) {
//            		return;
//            	}
                    else {
                        digitnum=digitnum.substring(0, digitnum.length()-1);

                    }
                    break;
                case R.id.btn_s_yin:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum +="\"";
                    }
                    break;
            }
            // 格式化 数据
            edit.setText(digitnum);
            edit.setSelection(null != digitnum ? digitnum.length() : 0);
        }

    }
}
