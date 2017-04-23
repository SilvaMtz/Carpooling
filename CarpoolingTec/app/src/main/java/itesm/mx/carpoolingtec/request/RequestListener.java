package itesm.mx.carpoolingtec.request;

import itesm.mx.carpoolingtec.model.firebase.Request;

/**
 * Created by DavidMartinez on 4/23/17.
 */

public interface RequestListener {
    void onAcceptClick(Request request);
    void onDeclineClick(Request request);
}
