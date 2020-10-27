import com.sun.istack.internal.NotNull;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import java.util.concurrent.TimeUnit;


/**
 * |--------------------------------------------------------------------------------------------------------------|
 * |                                          Object Header(128bits)                                              |
 * |--------------------------------------------------------------------------------------------------------------|
 * |                                Mark Word(64bits)               |  Klass Word(64bits)    |      State         |
 * |--------------------------------------------------------------------------------------------------------------|
 * | unused:25|identity_hashcode:31|unused:1|age:4|biase_lock:0| 01 | OOP to metadata object |      Nomal         |
 * |--------------------------------------------------------------------------------------------------------------|
 * | thread:54|      epoch:2       |unused:1|age:4|biase_lock:1| 01 | OOP to metadata object |      Biased        |
 * |--------------------------------------------------------------------------------------------------------------|
 * |                     ptr_to_lock_record:62                 | 00 | OOP to metadata object | Lightweight Locked |
 * |--------------------------------------------------------------------------------------------------------------|
 * |                    ptr_to_heavyweight_monitor:62          | 10 | OOP to metadata object | Heavyweight Locked |
 * |--------------------------------------------------------------------------------------------------------------|
 * |                                                           | 11 | OOP to metadata object |    Marked for GC   |
 * |--------------------------------------------------------------------------------------------------------------|
 */
public class ClassLayoutDemo {


    private User user;

    public ClassLayoutDemo(@NotNull User user) {
        this.user = user;
    }

    public void incrementAge() {
        System.out.println(Thread.currentThread().getName() + " before");
        printUser(user);
        synchronized (user) {
            System.out.println(Thread.currentThread().getName() + " in");
            printUser(user);
            this.user.incrementAge();
        }
        System.out.println(Thread.currentThread().getName() + " after");
        printUser(user);
    }


    public static void main(String[] args) throws Exception {
        System.out.println(VM.current().details());

        User user = new User();
        final ClassLayoutDemo layoutDemo = new ClassLayoutDemo(user);
        layoutDemo.incrementAge();
        TimeUnit.SECONDS.sleep(10);
        new Thread() {
            @Override
            public void run() {
                layoutDemo.incrementAge();
            }
        }.start();

        TimeUnit.SECONDS.sleep(10);
    }

    public static void printUser(User user) {
        System.out.println("User hash : " + user.hashCode());
        System.out.println(ClassLayout.parseInstance(user).toPrintable());
    }

    static class User {
        private String name;
        private Integer age = 0;
        private boolean sex;

        public User() {
        }

        public User(String name, Integer age, boolean sex) {
            this.name = name;
            this.age = age;
            this.sex = sex;
        }

        public void incrementAge() {
            this.age++;
        }

    }
}
