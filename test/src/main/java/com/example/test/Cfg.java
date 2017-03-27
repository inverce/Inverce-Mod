//package com.example.test;
//
//
//public class Cfg extends Config<Cfg.CfgValues> {
//    public static final CfgValues I = config().proxy();
//
//    private static Cfg config() {
//        return getConfig(Cfg.class);
//    }
//
//    public Preset<CfgValues> root() {
//        return Preset.create("Default");
//    }
//
//    public interface CfgValues {
//        ValuePreference<Integer> timeLimit();
//        ValuePreference<Integer> restRetries();
//    }
//}
