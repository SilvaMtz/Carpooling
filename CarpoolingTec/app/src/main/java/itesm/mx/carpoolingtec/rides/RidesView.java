package itesm.mx.carpoolingtec.rides;

public interface RidesView {

    void setLoadingIndicator(boolean active);

    void showRides();

    void hideRides();

    void showNoRidesAvailableToast();

    void showErrorLoadingRidesToast();
}
