/**
 * Copyright (C) futuretek AG 2016
 * All Rights Reserved
 *
 * @author Artan Veliju
 */
package survey.android.futuretek.ch.ft_survey;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SkillsActivity extends BaseActivity {
    private Button btn_add;
    private ListView listview;
    private String newSkill = null;
    public List<String> _productlist = new ArrayList<String>();
    private ListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        listview = (ListView) findViewById(R.id.listView);
        View mainTextView = findViewById(R.id.textLayout);
        mainTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((ViewGroup)findViewById(R.id.textLayout)).removeAllViews();
        List<String> textArray = new ArrayList<String>(1);
        textArray.add("Please add a developer skill");
        animateText(textArray);
        _productlist.clear();
        _productlist = getDatabase().getAllSkills();
        adapter = new ListAdapter(this);
        listview.setAdapter(adapter);
    }

    private class ListAdapter extends BaseAdapter {
        LayoutInflater inflater;
        ViewHolder viewHolder;

        public ListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return _productlist.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_row, null);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) convertView.findViewById(R.id.textView1);
                viewHolder.delBtn = (Button) convertView.findViewById(R.id.deleteBtn);
                viewHolder.delBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ViewGroup row = ((ViewGroup)v.getParent());
                        String id = ((TextView)row.findViewById(R.id.textView1)).getText().toString();
                        getDatabase().deleteSkill(id);
                        _productlist.remove(id);
                        adapter.notifyDataSetChanged();
                    }
                });

                /*
                //button to add new skill
                viewHolder.btnAdd = (Button) convertView.findViewById(R.id.addButton);
                viewHolder.btnAdd.setEnabled(true);
                viewHolder.btnAdd.setVisibility(View.VISIBLE);
                viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        requestNewSkill();

                        adapter.notifyDataSetChanged();
                    }
                });
*/
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(_productlist.get(position));
            return convertView;
        }
    }

    private class ViewHolder {
        TextView textView;
        Button delBtn;
        Button btnAdd;
    }

    private void requestNewSkill(){

            openInputDialog(new View.OnClickListener() {
                public void onClick(View v) {
                    EditText userInput = ((EditText) v.findViewById(R.id.userInput));
                    newSkill = userInput.getText().toString();
                    if (newSkill == null || newSkill.isEmpty()) {
                        List<String> textArray = new ArrayList<String>(1);
                        textArray.add("The text is empty..");
                        animateText(textArray, new AnimationListDone() {
                            public void done() {
                                //activateNextButton();
                            }
                        });
                    } else {
                        insertSkill(newSkill);
                    }
                }
            });

    }

    private void insertSkill(String skill){
        try {
            getDatabase().putSkill(skill);
            _productlist.clear();
            _productlist = getDatabase().getAllSkills();
            adapter = new ListAdapter(this);
            listview.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}