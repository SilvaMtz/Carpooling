package itesm.mx.carpoolingtec.data;

import java.util.List;

import io.reactivex.Single;
import itesm.mx.carpoolingtec.model.Ride;
import itesm.mx.carpoolingtec.model.User;
import retrofit2.http.GET;

public interface CarpoolingService {

    @GET("users/A00513173/contacts.json")
    Single<List<User>> getUsers();

    @GET("rides.json")
    Single<List<Ride>> getRides();
}
