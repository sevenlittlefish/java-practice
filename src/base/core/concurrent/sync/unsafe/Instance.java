package base.core.concurrent.sync.unsafe;

import imitate.ioc.User;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class Instance {
    public static void main(String[] args) throws InstantiationException, NoSuchFieldException {
        Unsafe unsafe = UnsafeClass.getInstance();
        System.out.println("========Unsafe instance========");
        User user = (User)unsafe.allocateInstance(User.class);
        System.out.println(user);

        System.out.println("========Constuct instance========");
        user = new User();
        System.out.println(user);

        System.out.println("========Unsafe modify filed========");
        Field field = user.getClass().getDeclaredField("age");
        unsafe.putInt(user,unsafe.objectFieldOffset(field),25);
        System.out.println(user);
    }
}
