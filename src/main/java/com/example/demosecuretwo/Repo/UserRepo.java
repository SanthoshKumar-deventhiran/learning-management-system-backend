package com.example.demosecuretwo.Repo;

import com.example.demosecuretwo.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users,Integer> {

     // public Users findByUsername(String username);
      public Users findByEmail(String email);
      Optional<Users> findByUsername(String username);

}
