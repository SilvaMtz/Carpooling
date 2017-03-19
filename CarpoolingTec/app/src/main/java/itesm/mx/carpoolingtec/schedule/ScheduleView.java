package itesm.mx.carpoolingtec.schedule;

public interface ScheduleView {

    void setLoadingIndicator(boolean active);

    void showItems();

    void hideItems();

    void showNoItemsToast();

    void showErrorLoadingItemsToast();
}
