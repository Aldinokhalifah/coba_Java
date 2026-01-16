package Sistem_Subcription_Digital.repository;

import Sistem_Subcription_Digital.model.User;
import java.util.Optional;

public interface  UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    void save(User user);

}
