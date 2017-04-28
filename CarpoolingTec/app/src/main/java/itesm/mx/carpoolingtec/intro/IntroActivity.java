package itesm.mx.carpoolingtec.intro;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import itesm.mx.carpoolingtec.data.AppRepository;
import itesm.mx.carpoolingtec.data.MySharedPreferences;
import itesm.mx.carpoolingtec.data.Repository;
import itesm.mx.carpoolingtec.login.LoginActivity;
import itesm.mx.carpoolingtec.main.MainActivity;

public class IntroActivity extends Activity {

    private static final String TAG = "IntroActivity";

    @Override
    protected void onStart() {
        super.onStart();
        setVisible(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Repository repository = AppRepository.getInstance(getSharedPreferences(
                MySharedPreferences.MY_PREFERENCES, MODE_PRIVATE));

        Intent intent;

        if (repository.getMyId() != null) {
            Log.d(TAG, "Already logged in, launching MainActivity");
            intent = new Intent(this, MainActivity.class);
        } else {
            Log.d(TAG, "Not logged in, launching LoginActivity");
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
