package com.example.digimart;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheets extends BottomSheetDialogFragment  {

    View view;
    Button okButton;
    RecyclerView recyclerView;
    BottomRecyclerview bottomRecyclerview;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bottom_sheets, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        okButton = view.findViewById(R.id.btnOK);
        okButton.setOnClickListener(v ->
        {
            int centerPosition = calculateCenterPosition();
            int selectedImageResId = getImageList().get(centerPosition);
            if (getActivity() instanceof OnOkButtonClickListener) {
                OnOkButtonClickListener listener = (OnOkButtonClickListener) getActivity();
                listener.onOkButtonClick(selectedImageResId);
            }

            dismiss(); // Close the bottom sheet
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),  LinearLayoutManager.HORIZONTAL, false));
        BottomRecyclerview bottomRecyclerview = new BottomRecyclerview(getImageList(), recyclerView);
        recyclerView.setAdapter(bottomRecyclerview);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                        // Determine which item is centered on the screen
                        int centerPosition = (lastVisibleItemPosition - firstVisibleItemPosition) / 2 + firstVisibleItemPosition;

                        // Update the selected item
                        bottomRecyclerview.setSelectedItem(centerPosition);
                    }
                }
            }
        });
        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, // Swipe left and right
                0
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    private List<Integer> getImageList() {
        List<Integer> imageList = new ArrayList<>();
        // Add your image resource IDs here
        imageList.add(R.drawable.prof);
        imageList.add(R.drawable.man);
        imageList.add(R.drawable.man_2);
        imageList.add(R.drawable.human);
        imageList.add(R.drawable.business);

        return imageList;
    }

    private int calculateCenterPosition() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (layoutManager != null) {
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

            // Determine which item is centered on the screen
            return (lastVisibleItemPosition - firstVisibleItemPosition) / 2 + firstVisibleItemPosition;
        }
        return -1; // Return a default value if the calculation fails
    }

    public interface OnOkButtonClickListener {
        void onOkButtonClick(int imageResId);
    }
}