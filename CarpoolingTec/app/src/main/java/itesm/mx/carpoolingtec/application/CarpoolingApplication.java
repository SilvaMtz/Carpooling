package itesm.mx.carpoolingtec.application;

import android.app.Application;

import jonathanfinerty.once.Once;

public class CarpoolingApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Once.initialise(this);
    }
}
