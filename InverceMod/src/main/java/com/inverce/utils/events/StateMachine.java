package com.inverce.utils.events;

import android.util.SparseArray;

import com.inverce.utils.events.interfaces.Listener;
import com.inverce.utils.events.annotations.State;
import com.inverce.utils.events.interfaces.StateMachineInterface;
import com.inverce.utils.events.core.ThreadPolicy;

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
