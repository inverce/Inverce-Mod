//package com.example.test;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import com.inverce.mod.integrations.support.annotations.IBinder;
//import com.inverce.mod.integrations.support.recycler.MultiRecyclerAdapter;
//
//public class MyAdapter extends MultiRecyclerAdapter<Object> {
//    public MyAdapter() {
//        register(p -> p instanceof Product, Holder::new, R.layout.support_simple_spinner_dropdown_item);
//        register(p -> p instanceof Category, Holder2::new, R.layout.support_simple_spinner_dropdown_item);
//    }
//
//    private static class Holder extends RecyclerView.ViewHolder implements IBinder<Product> {
//        public Holder(View itemView) {
//            super(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(Product item, int position) {
//
//        }
//    }
//
//    private static class Holder2 extends RecyclerView.ViewHolder implements IBinder<Category> {
//        public Holder2(View itemView) {
//            super(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(Category item, int position) {
//
//        }
//    }
//}
