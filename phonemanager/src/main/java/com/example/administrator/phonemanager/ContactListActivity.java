package com.example.administrator.phonemanager;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.bean.Content;
import com.example.administrator.utils.ContactUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends ActionBarActivity {
    ListView lv_contactlist_showcontact;
    List<Content> contactlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        contactlist=new ArrayList<>();//
        lv_contactlist_showcontact = (ListView) findViewById(R.id.lv_contactlist_showcontact);

        contactlist= ContactUtils.getAllContact(this);//把联系人数据放入contactlist；
        lv_contactlist_showcontact.setAdapter(new MyContactAdpater());
        lv_contactlist_showcontact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Content content = contactlist.get(position);
                Intent data = new Intent();
                Log.i("哈哈哈哈哈3",""+position);
                data.putExtra("number", content.getNumber());
                setResult(1000, data);
                finish();
            }
        });
    }
    class MyContactAdpater extends BaseAdapter {
        @Override
        public int getCount() {
            return contactlist.size();
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
            Content ontcontent = contactlist.get(position);
            View v=View.inflate(ContactListActivity.this, R.layout.item_contactlist,null);  //  找到item_contactlist这个布局文件
            final TextView tv_contactlist_name = (TextView) v.findViewById(R.id.tv_contactlist_name);
            final TextView tv_contactlist_number = (TextView) v.findViewById(R.id.tv_contactlist_number);
            tv_contactlist_name.setText(ontcontent.getName());
            tv_contactlist_number.setText(ontcontent.getNumber());
            Log.i("哈哈哈哈哈4", "" + position + ontcontent.getName() + ontcontent.getNumber());
            return v;
        }
    }
}
