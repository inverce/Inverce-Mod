package com.example.test;

import com.inverce.mod.core.configuration.Config;
import com.inverce.mod.core.configuration.ConfigValue;
import com.inverce.mod.core.configuration.Preset;


public class Cfg extends Config<Cfg.CfgValues> {
    public static final CfgValues I = config().proxy();

    private static Cfg config() {
        return getConfig(Cfg.class);
    }

    public Preset<CfgValues> root() {
        return Preset.create("Default");
    }

    public interface CfgValues {
        ConfigValue<Integer> timeLimit();
        ConfigValue<Integer> restRetries();
    }
}
