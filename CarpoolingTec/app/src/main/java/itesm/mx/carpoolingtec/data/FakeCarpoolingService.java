package itesm.mx.carpoolingtec.data;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import itesm.mx.carpoolingtec.model.Ride;
import itesm.mx.carpoolingtec.model.User;


public class FakeCarpoolingService implements CarpoolingService {
    @Override
    public Single<List<User>> getUsers() {
        User user1 = new User("Jorge Gil", "http://orig04.deviantart.net/aded/f/2013/066/c/2/profile_picture_by_naivety_stock-d5x8lbn.jpg");
        User user2 = new User("Karen Jimenez", "http://orig10.deviantart.net/b1f3/f/2011/258/1/8/profile_picture_by_ff_stock-d49yyse.jpg");
        User user3 = new User("Lucia Hernandez", "http://skateparkoftampa.com/spot/headshots/2585.jpg");

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        return Single.just(users);
    }

    @Override
    public Single<List<Ride>> getRides() {
        User user1 = new User("Jorge", "http://orig04.deviantart.net/aded/f/2013/066/c/2/profile_picture_by_naivety_stock-d5x8lbn.jpg");
        User user2 = new User("Karen", "http://orig10.deviantart.net/b1f3/f/2011/258/1/8/profile_picture_by_ff_stock-d49yyse.jpg");
        User user3 = new User("Lucia", "http://skateparkoftampa.com/spot/headshots/2585.jpg");

        List<Ride> rides = new ArrayList<>();
        rides.add(new Ride(user1));
        rides.add(new Ride(user2));
        rides.add(new Ride(user3));

        return Single.just(rides);
    }
}
