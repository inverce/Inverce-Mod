//package com.inverce.utils.stateless.test;
//
//import com.inverce.utils.stateless.StateMachineBase;
//import com.inverce.utils.stateless.annotations.State;
//import com.inverce.utils.stateless.annotations.StateMachineMeta;
//import com.inverce.utils.events.annotation.ThreadPolicy;
//import com.inverce.logging.Log;
//
//@StateMachineMeta(threadPolicy = ThreadPolicy.BgThread)
//public class SM extends StateMachineBase<DD, States, SMObject> implements DD {
//    public SM() {
//        Log.e("ctor");
//        event().setListener(this);
//        toState(States.Hide, new SMObject());
//    }
//
//    @State("*->Show")
//    public void preShow(SMObject context) {
//        Log.w("preShow");
//    }
//
//    @State(value = "Show", threadPolicy = ThreadPolicy.UiThread)
//    public void onShow(SMObject context) {
//        Log.w("onShow");
//    }
//
//    @State("Show->*")
//    public void postShow(SMObject context) {
//        Log.w("postShow");
//    }
//
//    @State("Hide")
//    public void onHide(SMObject context) {
//        Log.w("onHide");
//    }
//
//    @State("Show->Hide")
//    public void hiding(SMObject context) {
//        Log.w("hiding");
//    }
//
//    @Override
//    public void testMe() {
//        Log.w("testMe");
//        toState(States.Show, new SMObject());
//        toState(States.Hide, new SMObject());
//    }
//
////    Usage
////    SM sm = new SM();
////    sm.testMe();
//
////    Sample Outputs
////    E/||: ctor
////    W/||: Going to state: Hide
////    W/||: onHide
////    E/||: testMe
////    W/||: Going to state: Show
////    W/||: preShow
////    W/||: onShow
////    W/||: Going to state: Hide
////    W/||: postShow
////    W/||: hiding
////    W/||: onHide
//}
