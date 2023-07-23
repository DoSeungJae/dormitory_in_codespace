package com.taxiWithBack.domain.user.repository;


import com.taxiWithBack.domain.user.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long>{

}