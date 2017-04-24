package itesm.mx.carpoolingtec.base;

import android.support.annotation.NonNull;

public abstract class BasePresenter<V> {

    protected V view;

    public final void attachView(@NonNull V view){
        this.view = view;
    }

    public final void detachView() {
        this.view = null;
    }

    protected final boolean isViewAttached() {
        return view != null;
    }
}
