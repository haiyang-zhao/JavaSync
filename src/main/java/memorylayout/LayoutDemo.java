package memorylayout;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

public class LayoutDemo {
    public static void main(String[] args) {
//        System.out.println(VM.current().details());
//        System.out.println(ClassLayout.parseClass(SimpleInt.class).toPrintable());

//        SimpleInt instance = new SimpleInt();
//        System.out.println("The identity hash code is " + System.identityHashCode(instance));
//        System.out.println(ClassLayout.parseInstance(instance).toPrintable());

//        System.out.println(ClassLayout.parseClass(FieldsArrangement.class).toPrintable());
//        Lock lock = new Lock();
//        System.out.println(ClassLayout.parseInstance(lock).toPrintable());
//        synchronized (lock) {
//            System.out.println(ClassLayout.parseInstance(lock).toPrintable());
//        }

//        new LayoutDemo().testGcAge();

        boolean[] booleans = new boolean[3];
        System.out.println(ClassLayout.parseInstance(booleans).toPrintable());

    }


    volatile Object consumer;

    void testGcAge() {

        Object instance = new Object();
        long lastAddr = VM.current().addressOf(instance);
        ClassLayout layout = ClassLayout.parseInstance(instance);

        for (int i = 0; i < 10000; i++) {
            long currentAddr = VM.current().addressOf(instance);
            if (currentAddr != lastAddr) {
                System.out.println(layout.toPrintable());
            }

            for (int j = 0; j < 10000; j++) {
                consumer = new Object();
            }

            lastAddr = currentAddr;
        }

    }
}
