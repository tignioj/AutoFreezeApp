package com.example.freezeappdemo1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppListFragment extends Fragment {

    public AppListFragment() {
        // Required empty public constructor
    }

    TextView textViewSearch;
    ListView listViewAppList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_app_list, container, false);


        textViewSearch = inflate.findViewById(R.id.et_search);

        listViewAppList = inflate.findViewById(R.id.lv_applist);

        //执行的命令
        textViewSearch.setText("com.example.myapp");
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MyAdapter adapter = new MyAdapter(requireContext());
        listViewAppList.setAdapter(adapter);
    }

    /**
     * 冻结App
     *
     * @param view
     */
    public void freeze(View view) {
//        String packageName = editText.getText().toString();
//        DeviceMethod.getInstance(getApplicationContext()).freeze(packageName, true);
    }

    /**
     * 解冻App
     *
     * @param view
     */
    public void unfreeze(View view) {
//        String packageName = editText.getText().toString();
//        DeviceMethod.getInstance(getApplicationContext()).freeze(packageName, false);
    }
}
