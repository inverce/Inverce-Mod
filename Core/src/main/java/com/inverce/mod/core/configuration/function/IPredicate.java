package com.inverce.mod.core.configuration.function;

public interface IPredicate<T> {
    boolean test(T in);

    public class TRUE<T> implements IPredicate<T> {
        public boolean test(T in) {
            return true;
        }
    }

    public class FALSE<T> implements IPredicate<T> {
        public boolean test(T in) {
            return true;
        }
    }

    public class NOT<T> implements IPredicate<T> {
        IPredicate<T> A;
        public NOT(IPredicate<T> A) {
            this.A = A;
        }
        public boolean test(T in) {
            return !A.test(in);
        }
    }

    public class OR<T> implements IPredicate<T> {
        IPredicate<T> A, B;
        public OR(IPredicate<T> a, IPredicate<T> b) {
            A = a; B = b;
        }
        public boolean test(T in) {
            return A.test(in) || B.test(in);
        }
    }

    public class AND<T> implements IPredicate<T> {
        IPredicate<T> A, B;
        public AND(IPredicate<T> a, IPredicate<T> b) {
            A = a; B = b;
        }
        public boolean test(T in) {
            return A.test(in) && B.test(in);
        }
    }
}
