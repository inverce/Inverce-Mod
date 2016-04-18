package com.inverce.utils.tools;

@SuppressWarnings("unused")
public class Metric {
    public static class Bytes {
        public static final int B = 1, KB = B * 1024, MB = KB * 1024, GB = MB * 1024;
    }

    public static class Millis {
        public static final int MS = 1, S = MS * 1000, M = S * 60, H = M * 60, D = H * 24;
    }
}
