package com.example.big_work;


import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ByListFragment extends Fragment implements  AdapterView.OnItemClickListener{

    ListView listView;

    // 2.1 定义用来与外部activity交互，获取到宿主activity
    private FragmentInteraction listterner;

    // 1 定义了所有activity必须实现的接口方法
    public interface FragmentInteraction {
        void process(int i);
    }

    // 当FRagmen被加载到activity的时候会被回调
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof FragmentInteraction) {
            listterner = (FragmentInteraction)activity; // 2.2 获取到宿主activity并赋值
        } else{
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }
    }


    public ByListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_by_list, container, false);

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/testmusic");
        String[] aa = file.list();
        listView = view.findViewById(R.id.et);
        ListAdapter adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),android.R.layout.simple_list_item_1,aa);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(this);


        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Object itemAtPosition = listView.getItemAtPosition(position);
        //String str = (String) itemAtPosition;
        listterner.process(position);
    }

    //把传递进来的activity对象释放掉
    @Override
    public void onDetach() {
        super.onDetach();
        listterner = null;
    }
}
