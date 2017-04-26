package itesm.mx.carpoolingtec.login;

public interface LoginView {

    void setLoadingIndicator(boolean active);

    void startMainActivity();

    void startPedirInfoActivity(String userId, String name);

    void showLoginErrorMessage();

    void showInvalidCredentialsMessage();

    void showMissingFieldsMessage();

    void showToast(String message);
}
