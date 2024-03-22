package web.factory;

import java.lang.reflect.InvocationTargetException;

public class ClassFactory extends ObjFactory{
    @Override
    public <T> T createObj(Class<T> tClass) {
        if (tClass == null) {
            return null;
        }
        try {
            return tClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}
