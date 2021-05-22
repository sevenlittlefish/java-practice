package imitate.aop;

import imitate.ioc.User;

public class UserDao implements IUserDao {

    @Override
    public void create(User user) {
        System.out.println("保存user成功："+user);
    }
}
