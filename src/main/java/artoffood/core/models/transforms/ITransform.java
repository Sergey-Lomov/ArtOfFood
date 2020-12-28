package artoffood.core.models.transforms;

public interface ITransform<T> {
    void update(T in);
}