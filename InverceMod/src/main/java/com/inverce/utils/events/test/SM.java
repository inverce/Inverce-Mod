package com.inverce.utils.events.test;

import com.inverce.utils.events.StateMachineBase;
import com.inverce.utils.events.annotations.State;
import com.inverce.utils.events.annotations.StateMachineMeta;
import com.inverce.utils.events.core.ThreadPolicy;
import com.inverce.utils.logging.Log;

@StateMachineMeta(threadPolicy = ThreadPolicy.BgThread)
public class SM extends StateMachineBase<DD, States, SMObject> implements DD {
    public SM() {
        Log.e("ctor");
        event().setListener(this);
        toState(States.Hide, new SMObject());
    }

    @State("*->Show")
    public void preShow(SMObject context) {
        Log.w("preShow");
    }

    @State("Show")
    public void onShow(SMObject context) {
        Log.w("onShow");
    }

    @State("Show->*")
    public void postShow(SMObject context) {
        Log.w("postShow");
    }

    @State("Hide")
    public void onHide(SMObject context) {
        Log.w("onHide");
    }

    @State("Show->Hide")
    public void hiding(SMObject context) {
        Log.w("hiding");
    }

    @Override
    public void testMe() {
        Log.w("testMe");
        toState(States.Show, new SMObject());
        toState(States.Hide, new SMObject());
    }

//    Usage
//    SM sm = new SM();
//    sm.testMe();

//    Sample Outputs
//    E/||: ctor
//    W/||: Going to state: Hide
//    W/||: onHide
//    E/||: testMe
//    W/||: Going to state: Show
//    W/||: preShow
//    W/||: onShow
//    W/||: Going to state: Hide
//    W/||: postShow
//    W/||: hiding
//    W/||: onHide
}
