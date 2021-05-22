package imitate.ioc;

public class CreateTest {

    public static void main(String[] args) {
        BeanFactory factory = BeanFactory.getInstance();
        User user = factory.create("imitate.ioc.User", User.class);
        System.out.println(user);
    }
}
