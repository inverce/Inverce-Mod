package com.inverce.mod.stateless;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.inverce.mod.core.Log;
import com.inverce.mod.events.Event;
import com.inverce.mod.events.annotation.Listener;
import com.inverce.mod.events.annotation.ThreadPolicy;
import com.inverce.mod.stateless.annotations.State;
import com.inverce.mod.stateless.annotations.StateMachineInterface;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unchecked")
public class StateMachineBase <INTERFACE extends Listener, STATES extends Enum<?>, CONTEXT> implements StateMachineInterface<INTERFACE, STATES, CONTEXT> {
    private @NonNull
    Event<INTERFACE> events;
    private @NonNull List<STATES> states;
    private @NonNull Class<STATES> statesClass;
    private @NonNull Class<INTERFACE> eventsClass;
    private @NonNull Class<CONTEXT> contextClass;

    private HashMap<String, Integer> stateToId;
    private SparseArray<ArrayList<StateCall>> onState, preState, postState;
    private HashMap<StateMatch, ArrayList<StateCall>> switchState;

    private STATES lastState = null;

    @SuppressWarnings("unchecked")
    public StateMachineBase() {
        Type[] types = EventsModule.genericTypes(this.getClass(), StateMachineBase.class);
        eventsClass = (Class<INTERFACE>)types[0];
        statesClass = (Class<STATES>)types[1];
        contextClass = (Class<CONTEXT>)types[2];

        events = new Event<>(eventsClass);
        stateToId = new HashMap<>();
        preState = new SparseArray<>();
        onState = new SparseArray<>();
        postState = new SparseArray<>();
        switchState = new HashMap<>();

        try {
            Method method = statesClass.getDeclaredMethod("values");
            method.setAccessible(true);
            states = Arrays.asList((STATES[]) method.invoke(null));
        } catch (Exception ex) {
            Log.exs(ex);
            states = new ArrayList<>();
        }

        if (eventsClass.isInstance(this)) {
            events.addListener((INTERFACE) this);
        }

        for (STATES state: states) {
            stateToId.put(state.name(), state.ordinal());
            preState.append(state.ordinal(), new ArrayList<StateCall>());
            onState.append(state.ordinal(), new ArrayList<StateCall>());
            postState.append(state.ordinal(), new ArrayList<StateCall>());
        }
        for (Method method: this.getClass().getMethods()) {
            State state = method.getAnnotation(State.class);
            if (state != null) {
                StateCall call = new StateCall(state.threadPolicy(), method, state.value());
                String [] states = state.value().split("->");
                parseCall(call, states);
            }
        }
    }

    private void parseCall(StateCall call, String[] states) {
        Log.w("Parse: " + Arrays.toString(states) + " " + call.method);
        if (states.length == 3) {
            addCall(states[0], call, preState);
            addCall(states[1], call, onState);
            addCall(states[2], call, postState);
        } else if (states.length == 2) {
            addCall(states[0], states[1], call);
        } else if (states.length == 1) {
            addCall(states[0], call, onState);
        }
    }

    private int stateOrdinal(String query) {
        Integer val = stateToId.get(query);
        return val != null ? val : -1;
    }

    private void addCall(String query, StateCall call, SparseArray<ArrayList<StateCall>> list) {
        Integer val = stateToId.get(query);
        if (val != null) {
            list.get(val).add(call);
        } else if ("*".equals(query)){
            for (int i=0; i<list.size(); i++)
                list.valueAt(i).add(call);
        } else {
            Log.w("Context state are not supported yet. Context: " + query + call.method);
        }
    }

    private void addCall(String from, String to, StateCall call) {
        StateMatch match = new StateMatch(stateOrdinal(from), stateOrdinal(to));
        if (switchState.get(match) == null)
            switchState.put(match, new ArrayList<StateCall>());
        switchState.get(match).add(call);
    }

    private synchronized List<StateCall> getSwitches(int from, int to) {
        if (from == -2)
            return null;

        ArrayList<StateCall> calls = null;
        for (StateMatch call : switchState.keySet()) {
            if ((call.to == to || call.to == -1) && (call.from == from || call.from == -1)) {
                if (calls == null)
                    calls = new ArrayList<>();
                calls.addAll(switchState.get(call));
            }
        }
        return calls;
    }

    public INTERFACE post() { return events.post(); }
    public Event<INTERFACE> event() { return events; }
    public STATES[] states() { return states.toArray((STATES[])Array.newInstance(statesClass, states.size())); }

    public void toState(STATES toState, CONTEXT context) {
        Log.w("Going to state: " + toState);
        try {
            int id = toState.ordinal();
            int prev = lastState != null ? lastState.ordinal() : -2;
            List<StateCall> stateCalls = getSwitches(prev, id);
            if (stateCalls != null) {
                for (StateCall call: stateCalls)
                    call.method.invoke(this, context);
            }
            for (StateCall call: preState.get(id))
                call.method.invoke(this, context);
            for (StateCall call: onState.get(id))
                call.method.invoke(this, context);
            for (StateCall call: postState.get(id))
                call.method.invoke(this, context);
            lastState = toState;
        } catch (Exception ex) {
            Log.exs(ex);
        }
    }


    private class StateCall {
        public ThreadPolicy threadPolicy;
        public Method method;
        public String state;

        public StateCall(ThreadPolicy threadPolicy, Method method, String state) {
            this.threadPolicy = threadPolicy;
            this.method = method;
            this.state = state;
        }
    }

    private class StateMatch {
        public int from, to;
        public StateMatch(int from, int to) { this.from = from; this.to = to; }
        public boolean equals(Object o) {
            return !(o == null || getClass() != o.getClass()) && (this == o || from == ((StateMatch) o).from && to == ((StateMatch) o).to);
        }
        public int hashCode() {
            int result = from; result = 31 * result + to; return result;
        }
    }
}

