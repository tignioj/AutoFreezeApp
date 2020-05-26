package com.example.freezeappdemo1.ui.home.frozen.category;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.freezeappdemo1.R;
import com.example.freezeappdemo1.backend.entitys.FreezeApp;
import com.example.freezeappdemo1.backend.viewmodel.HomeViewModel;
import com.example.freezeappdemo1.utils.DeviceMethod;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrozenAppByCategoryFragment extends Fragment {

    public FrozenAppByCategoryFragment() {
        // Required empty public constructor
    }

    long categoryId;

    private RecyclerView recyclerView;
    private Button buttonAdd;
    EditText editTextSearch;
    HomeViewModel homeViewModel;
    private FrozenAppByCategoryAdapter adapter;
    private ImageButton imageButtonFreezeAll;
    private ProgressBar progressBarAppsByCategoryFreezeAll;
    LiveData<List<FreezeApp>> freezeAppListLive;


    public FrozenAppByCategoryAdapter getAdapter() {
        return adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frozen_app_by_category, container, false);
        Bundle arguments = getArguments();
        this.categoryId = arguments.getLong("id");
        imageButtonFreezeAll = view.findViewById(R.id.imageButtonFreezeAll);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
//        List<FreezeApp> appsByCategory = homeViewModel.getAppsByCategory(categoryId);
        freezeAppListLive = homeViewModel.getAppsByCategoryLive(categoryId);

        progressBarAppsByCategoryFreezeAll = view.findViewById(R.id.progressBarAppsByCategoryFreezeAll);
        progressBarAppsByCategoryFreezeAll.setVisibility(View.INVISIBLE);

        recyclerView = view.findViewById(R.id.rcv_applist_frozen_category);
        buttonAdd = view.findViewById(R.id.buttonAddAppToCategory);

        adapter = new FrozenAppByCategoryAdapter(homeViewModel, this);
        freezeAppListLive.observe(getViewLifecycleOwner(), new Observer<List<FreezeApp>>() {
            @Override
            public void onChanged(List<FreezeApp> freezeApps) {
                adapter.submitList(freezeApps);
                adapter.notifyDataSetChanged();
            }
        });

        editTextSearch = view.findViewById(R.id.et_search_frozen_category);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                freezeAppListLive = homeViewModel.getMutableLiveDataFreezeAppListLiveInCategoryWithPattern(categoryId,s.toString().trim());
                freezeAppListLive.removeObservers(getViewLifecycleOwner());
                freezeAppListLive.observe(getViewLifecycleOwner(), new Observer<List<FreezeApp>>() {
                    @Override
                    public void onChanged(List<FreezeApp> freezeApps) {
                        adapter.submitList(freezeApps);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAppsDialog addAppsDialog = new AddAppsDialog(FrozenAppByCategoryFragment.this);
                addAppsDialog.show(requireActivity().getSupportFragmentManager(), "add apps to category");
            }
        });

        imageButtonFreezeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarAppsByCategoryFreezeAll.setVisibility(View.VISIBLE);
                //后台处理数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        imageButtonFreezeAll.setImageResource(hasFreezeAll ? R.drawable.ic_lock_open_black_24dp : R.drawable.ic_lock_black_24dp);
                        List<FreezeApp> value = freezeAppListLive.getValue();
                        for (FreezeApp freezeApp : value) {
                            freezeApp.setFrozen(!hasFreezeAll);
                            DeviceMethod.getInstance(requireContext()).freeze(freezeApp.getPackageName(), !hasFreezeAll);
                            homeViewModel.updateFreezeApp(freezeApp);
                        }
                        hasFreezeAll = !hasFreezeAll;
                        homeViewModel.updateAllMemoryData();
                        //必须用UI线程更新UI
                        FragmentActivity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBarAppsByCategoryFreezeAll.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        return view;
    }

    private boolean hasFreezeAll;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /**
         * 0: 不支持拖动
         *ItemTouchHelper.START | ItemTouchHelper.END  支持向左划以及向右滑
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
//                Word wordToDelete = filterWords.getValue().get(viewHolder.getAdapterPosition());
                final FreezeApp currentSwipeTask = adapter.getCurrentList().get(viewHolder.getAdapterPosition());
                //注意，ViewModel从Repository拿数据，而Repository从数据库拿到数据，数据库返回的数据是一个可观察对象
                //在拿到数据的时候，我们已经对它进行监听  wordViewModel.getAllWordsLive().observe ....
                //因此我们只需要调用wordViewModel的delete方法，最终数据库更行数据，观察者发现数据变化，然后做出了变化

                switch (direction) {
                    //左滑动删除任务
                    case ItemTouchHelper.START:
                        DeviceMethod.getInstance(getContext()).freeze(currentSwipeTask.getPackageName(), false);
                        homeViewModel.deleteFreezeApp(currentSwipeTask);
                        homeViewModel.updateAllMemoryData();
                        //撤销功能
                        Snackbar.make(
                                /*参数1：显示在哪个组件上？*/
                                requireView().findViewById(R.id.frozenAppByCategoryFragmentId),
                                /*参数2：显示的文字*/
                                R.string.fragment_frozenapps_by_category_remove_snackbar_text,
                                /*参数3：显示时长*/
                                Snackbar.LENGTH_SHORT
                                /*监听按钮*/
                        ).setAction(R.string.undo_text, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currentSwipeTask.setFrozen(false);
                                homeViewModel.insertFreezeApp(currentSwipeTask);
                                homeViewModel.updateAllMemoryData();
//                                undoAction = true;
                            }
                            /*不要忘记调用show()方法*/
                        }).show();
                        break;
                }
            }

            //在滑动的时候，画出浅灰色背景和垃圾桶图标，增强删除的视觉效果
            Drawable trashIcon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_delete_forever_black_24dp);
            Drawable editIcon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_edit_black_24dp);
            Drawable background = new ColorDrawable(Color.LTGRAY);


            //删除的时候绘图
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                Drawable currentIcon;
                if (dX > 0) {
                    currentIcon = editIcon;
                } else {
                    currentIcon = trashIcon;
                }
                View itemView = viewHolder.itemView;
                int iconMargin = (itemView.getHeight() - currentIcon.getIntrinsicHeight()) / 2;

                int iconLeft, iconRight, iconTop, iconBottom;
                int backTop, backBottom, backLeft, backRight;
                backTop = itemView.getTop();
                backBottom = itemView.getBottom();
                iconTop = itemView.getTop() + (itemView.getHeight() - currentIcon.getIntrinsicHeight()) / 2;
                iconBottom = iconTop + currentIcon.getIntrinsicHeight();
                if (dX > 0) {
                    backLeft = itemView.getLeft();
                    backRight = itemView.getLeft() + (int) dX;
                    background.setBounds(backLeft, backTop, backRight, backBottom);
                    iconLeft = itemView.getLeft() + iconMargin;
                    iconRight = iconLeft + currentIcon.getIntrinsicWidth();
                    currentIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                } else if (dX < 0) {
                    backRight = itemView.getRight();
                    backLeft = itemView.getRight() + (int) dX;
                    background.setBounds(backLeft, backTop, backRight, backBottom);
                    iconRight = itemView.getRight() - iconMargin;
                    iconLeft = iconRight - currentIcon.getIntrinsicWidth();
                    currentIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                } else {
                    background.setBounds(0, 0, 0, 0);
                    currentIcon.setBounds(0, 0, 0, 0);
                }
                background.draw(c);
                currentIcon.draw(c);
            }
        }).attachToRecyclerView(recyclerView); /*将它和视图关联*/
    }
}
