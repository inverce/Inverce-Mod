package com.example.test;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.inverce.mod.integrations.support.recycler.MultiRecyclerAdapter;

public class MyAdapter extends MultiRecyclerAdapter<User> {
    public MyAdapter() {
        register(p -> p.type == 2, UserHolder2::new, R.layout.support_simple_spinner_dropdown_item);
    }


    private static class UserHolder extends RecyclerView.ViewHolder {
        public UserHolder(View itemView) {
            super(itemView);
        }
    }

    private static class UserHolder2 extends RecyclerView.ViewHolder implements IBinder<User> {
        public UserHolder2(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(User item, int position) {

        }
    }
}
