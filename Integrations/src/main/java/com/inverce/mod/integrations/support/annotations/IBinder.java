package com.inverce.mod.integrations.support.annotations;

public interface IBinder<I> {
    void onBindViewHolder(I item, int position);
}
