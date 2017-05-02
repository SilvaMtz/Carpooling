package itesm.mx.carpoolingtec.request;

import itesm.mx.carpoolingtec.model.firebase.Request;

public interface RequestListener {

    void onAcceptClick(Request request, String requestKey);

    void onDeclineClick(String requestKey);
}
