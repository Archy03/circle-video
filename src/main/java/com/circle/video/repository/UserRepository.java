package com.circle.video.repository;

import com.circle.video.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

  User findOneById(Long id);

  Long countById(Long id);

  /*@Query("select u from User u where u.email = :email")*/
  User findByEmail(String email);

  User findByUserName(String userName);

  @Query("SELECT u FROM User u WHERE concat(u.firstName, ' ', u.lastName) like %?1% or concat(u.lastName, ' ', u.firstName) like %?1% or u.email LIKE %?1% or u.userName LIKE %?1%")
  Page<User> findAll(String keyword, Pageable pageable);

}
