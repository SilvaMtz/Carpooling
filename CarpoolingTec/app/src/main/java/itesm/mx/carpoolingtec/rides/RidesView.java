package itesm.mx.carpoolingtec.rides;

import java.util.List;

import itesm.mx.carpoolingtec.model.Ride;

public interface RidesView {

    void setLoadingIndicator(boolean active);

    void showRides(List<Ride> rides);

    void hideRides();

    void showNoRidesAvailableToast();

    void showErrorLoadingRidesToast();
}
