package com.example.gcls.prj_demo.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gcls.prj_demo.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lenovo on 2016/12/24.
 */

public class myBluetoothList extends Dialog{

    public myBluetoothList(Context context) {
        super(context);
    }

    public myBluetoothList(Context context, int theme) {
        super(context, theme);
    }


    public static class Builder{
        private Context context;
        private ListView listView;
        private TextView headView;
        private int selectionItem;
        private Button[] taskList;
        private int length;
        private String title;
        public Builder(Context context,Button[] tl,int lt,String ttl) {
            this.context = context;
            this.taskList = tl;
            this.length = lt;
            this.title = ttl;


        }
        public myBluetoothList create() {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final myBluetoothList dialog = new myBluetoothList (context);
            View layout = inflater.inflate(R.layout.mlist, null);
            layout.setBackgroundColor(Color.rgb(144,144,144));
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            headView = (Button)layout.findViewById(R.id.mylistTitle);
            headView.setText(title);
            headView.setClickable(false);
            listView = (ListView)layout.findViewById(R.id.mylistView);
            dialog.setContentView(layout);
             /*List<Map<String, Object>> myData = getData(this.taskList,this.length);
             SimpleAdapter adapter = new SimpleAdapter(context,myData,R.layout.mlist,
                     new String[]{"context"},
                     new int[]{R.id.activity31textView1});*/
            ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
            for(int i = 0;i < length; i++){
                HashMap<String,Object> map = new HashMap<String,Object>();
                map.put("Item1",taskList[i].getText());
                listItem.add(map);
                listView.addFooterView(taskList[i]);
            }
          /* SimpleAdapter listItemAdapter = new SimpleAdapter(context,listItem,//数据源
                    R.layout.listviewitem,//ListItem的XML实现
                    //动态数组与ImageItem对应的子项
                    new String[] {"Item1"},
                    //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                    new int[] {R.id.listbutton}
            );*/

            //添加并且显示
            listView.setAdapter(null);
          /*
          listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    View view = listView.getChildAt(arg2);
                    selectionItem = arg2;
                    Button btnFinish = (Button) view.findViewById(R.id.listbutton);
                    System.out.println("在这里");
                    dialog.dismiss();
                    btnFinish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //taskList[selectionItem].callOnClick();
                            System.out.println("在这里");
                            dialog.dismiss();
                        }
                    });

                }
            });*/
            dialog.setContentView(layout);
            return dialog;
        }


    }

}
