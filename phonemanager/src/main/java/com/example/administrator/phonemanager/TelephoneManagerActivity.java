package com.example.administrator.phonemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.dao.BlackNumberDao;
import com.example.administrator.service.BlackNumberService;

import java.util.List;

//通讯卫士的主界面
public class TelephoneManagerActivity extends Activity {

    private static   int TOTALRECORD   ;
    private Button bt_phonemanager_add;
    private BlackNumberDao dao ;
    private ListView lv_phonemanager_blacknum;

    private List<listitem> blacknumberlist;
    private MyBlacknumberListAdapter myBlacknumberListAdapter;


    private static final int LIMIT =10;
    private   int offset =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephone_manager);
        bt_phonemanager_add = (Button) findViewById(R.id.bt_phonemanager_add);
        lv_phonemanager_blacknum = (ListView) findViewById(R.id.lv_phonemanager_blacknum);
        dao = new BlackNumberDao(this);
        //这是为优化listview而做的
        TOTALRECORD = dao.getTotalRecordNumber();
        blacknumberlist =dao.getallPartBlacknumber(offset, LIMIT);
        myBlacknumberListAdapter = new MyBlacknumberListAdapter();
        lv_phonemanager_blacknum.setAdapter(myBlacknumberListAdapter);
        startService(new Intent(this, BlackNumberService.class));
        bt_phonemanager_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(TelephoneManagerActivity.this, R.layout.dialog_add_blacknum, null);
                final EditText et_blacknum_numer = (EditText) view.findViewById(R.id.et_blacknum_numer);
                final Button bt_blacknum_confirm = (Button) view.findViewById(R.id.bt_blacknum_confirm);
                final Button bt_blacknum_cancle = (Button) view.findViewById(R.id.bt_blacknum_cancle);
                final RadioGroup rg_addblacknumber_mode = (RadioGroup) view.findViewById(R.id.rg_addblacknumber_mode);
                //以自己定义的view填充dialog
                final AlertDialog alertDialog = new AlertDialog.Builder(TelephoneManagerActivity.this)
                        .setView(view)
                        .create();
                alertDialog.show();
                bt_blacknum_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String blacknumber = et_blacknum_numer.getText().toString();
                        int rb_id = rg_addblacknumber_mode.getCheckedRadioButtonId();
                        int mode = -1;
                        if (rb_id == R.id.rb_blacknum_call) {

                            mode = 1;
                        } else if (rb_id == R.id.rb_blacknum_sms) {

                            mode = 2;
                        } else if (rb_id == R.id.rb_blacknum_all) {

                            mode = 3;
                        }
                        if (-1 != dao.insertBlackNumber(blacknumber, mode)) {
                            blacknumberlist.add(new listitem(blacknumber, mode));
                            myBlacknumberListAdapter.notifyDataSetChanged();
                        }
                        alertDialog.dismiss();//在选完之后将弹窗dismiss掉
                    }
                });
                bt_blacknum_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        lv_phonemanager_blacknum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final listitem listitem = blacknumberlist.get(position);
                final int mypostion = position;
                new AlertDialog.Builder(TelephoneManagerActivity.this).setTitle("确认要删除吗？")
                        .setMessage("当前号码" + listitem.blacknum)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (1 == dao.deleteBlackNumber(listitem.blacknum)) {
                                    blacknumberlist.remove(mypostion);
                                    myBlacknumberListAdapter.notifyDataSetChanged();
                                }
                            }
                        }).setNegativeButton("取消", null)
                        .show();
            }
        });
        lv_phonemanager_blacknum.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final listitem currentlistitem = blacknumberlist.get(position);
                int myposition = position;
                new AlertDialog.Builder(TelephoneManagerActivity.this)
                        .setTitle("修改拦截模式？")
                        .setSingleChoiceItems(new String[]{"拦截电话", "拦截短信", "拦截全部"}, currentlistitem.mode - 1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (1 == dao.updateeMode(currentlistitem.blacknum, which + 1)) {
                                    currentlistitem.mode = which + 1;
                                    myBlacknumberListAdapter.notifyDataSetChanged();
                                }
                                dialog.dismiss();
                                Log.i("哈哈",dao.updateeMode(currentlistitem.blacknum, which + 1)+"");
                            }
                        }).show();
                return true;//这里返回true的话在长按的时候就不会出现setOnItemClickListener的事件 但如果返回false的话就会在longitem后执行setOnItemClickListener事件
            }
        });
        lv_phonemanager_blacknum.setOnScrollListener(new AbsListView.OnScrollListener() {
            //当有状态变化的时候
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    //表示滚动已经停止了 滑动到当前list的最后一条数据，并停止的时候去加载更多数据
                    case SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition()==blacknumberlist.size()-1&&(blacknumberlist.size()<TOTALRECORD)){
                            List<listitem> listitems = dao.getallPartBlacknumber(offset + LIMIT, LIMIT);
                            offset+=LIMIT;
                            blacknumberlist.addAll(listitems);//将一个集合全部加到另一个集合中
                            myBlacknumberListAdapter.notifyDataSetChanged();
                        }
                        break;
                    case SCROLL_STATE_FLING:
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        break;
                }
            }
            //滑动的时候执行

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                Log.i("哈哈",11+"");
            }
        });
    }

    class MyBlacknumberListAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return blacknumberlist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(TelephoneManagerActivity.this, R.layout.item_blacknumberlist, null);
            TextView tv_blacknum_number= (TextView) view.findViewById(R.id.tv_blacknum_number);
            TextView tv_blacknum_mode= (TextView)   view.findViewById(R.id.tv_blacknum_mode);
            listitem listitem = blacknumberlist.get(position);//得到listview的这一行的子控件的布局从而给这一行进行赋值
            tv_blacknum_number.setText(listitem.blacknum);
            tv_blacknum_mode.setText(listitem.mode+"");
            return view;
        }
    }

    public static class listitem{
        public String blacknum;
        public int mode;

        public listitem(String blacknum, int mode) {
            this.blacknum = blacknum;
            this.mode = mode;
        }
    }
}
