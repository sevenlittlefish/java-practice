package imitate.aop;

import imitate.ioc.BeanFactory;
import imitate.ioc.User;

public class AopTest {

    public static void main(String[] args) {
        UserDao dao = BeanFactory.getInstance().create(null, UserDao.class);
        Aop aop = BeanFactory.getInstance().create("", Aop.class);
        IUserDao proxy = (IUserDao)ProxyFacotry.getProxyInstance(dao, aop);

        User user = new User();
        user.setName("567");
        user.setAge(18);
        user.setSex(1);
        proxy.create(user);
    }
}
