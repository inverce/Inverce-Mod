package com.inverce.mod.stateless;

import android.util.SparseArray;

import com.inverce.mod.events.annotation.Listener;
import com.inverce.mod.stateless.annotations.State;
import com.inverce.mod.stateless.annotations.StateMachineInterface;
import com.inverce.mod.events.annotation.ThreadPolicy;

import java.util.HashMap;

@SuppressWarnings("unchecked")
public class StateMachine {
    static SparseArray<HashMap<Class<? extends StateMachineBase<?, ?, ?>>, StateMachineBase<?, ?, ?>>> myMachines;

    @State(threadPolicy = ThreadPolicy.CallingThread)
    static void init() {
        myMachines = new SparseArray<>();
    }

    private static <T extends Listener, Y extends Enum<?>, O> StateMachineBase<T,Y,O> machine(Class<? extends StateMachineBase<T,Y,O>> clazz) {
        HashMap<Class<? extends StateMachineBase<?, ?, ?>>, StateMachineBase<?, ?, ?>> map = myMachines.get(0);
        if (map == null) {
            myMachines.put(0, map = new HashMap<>());
        }
        if (!map.containsKey(clazz)) {
            try {
                map.put(clazz, clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (StateMachineBase<T,Y, O>)map.get(clazz);
    }

    public static <T extends Listener, Y extends Enum<?>, O> StateMachineInterface<T,Y,O> get(Class<? extends StateMachineBase<T,Y,O>> clazz) {
        return machine(clazz);
    }
}
