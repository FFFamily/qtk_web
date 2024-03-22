package web.factory;

public abstract class ObjFactory {
    public abstract <T> T createObj(Class<T> tClass);
}
