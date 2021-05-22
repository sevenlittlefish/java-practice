package imitate.ioc;

public class BeanFactory {

    private static final BeanFactory factory = new BeanFactory();

    private BeanFactory(){}

    public static BeanFactory getInstance(){
        return factory;
    }

    public <T> T create(String className, Class<T> clazz){
        try {
            if(className != null && !className.trim().equals("")){
                return (T) Class.forName(className).newInstance();
            }
            return (T)clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
