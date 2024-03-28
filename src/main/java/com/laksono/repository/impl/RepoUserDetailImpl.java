package com.laksono.repository.impl;

import com.laksono.entity.User;
import com.laksono.entity.UserDetails;
import com.laksono.repository.RepoUserDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.util.List;

public class RepoUserDetailImpl implements RepoUserDetails {
    private final EntityManager em;
    private final RepoUserImpl repoUser;

    public RepoUserDetailImpl(EntityManager em, RepoUserImpl repoUser){
        this.em = em;
        this.repoUser = repoUser;
    }
    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public void insertUserDetail(UserDetails userDetails) {
        em.getTransaction().begin();
        em.createNativeQuery("INSERT INTO userDetails (id, first_name, last_name, address, date_of_birth) VALUES (?, ?, ?, ?, ?)")
                .setParameter(1, userDetails.getId())
                .setParameter(2, userDetails.getFirstName())
                .setParameter(3, userDetails.getLastName())
                .setParameter(4, userDetails.getAddress())
                .setParameter(5, userDetails.getDateOfBirth())
                .executeUpdate();
        em.getTransaction().commit();
        System.out.println("Data inserted with native query!");
    }

    @Override
    public void updateUserDetail(String username, UserDetails userDetails) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            User user = repoUser.getUserByUsername(username);
            Query preQuery = em.createNativeQuery("SELECT * FROM userDetails WHERE id = ?", UserDetails.class);
            preQuery.setParameter(1, user.getUserDetails().getId());


            UserDetails findUserDetail = (UserDetails) preQuery.getSingleResult();
            Query query = em.createNativeQuery("UPDATE userDetails SET first_name = :firstName, last_name = :lastName, address = :address, date_of_birth = :dateOfBirth WHERE id = :id");
            query.setParameter("firstName", userDetails.getFirstName());
            query.setParameter("lastName", userDetails.getLastName());
            query.setParameter("address", userDetails.getAddress());
            query.setParameter("dateOfBirth", userDetails.getDateOfBirth());
            query.setParameter("id", findUserDetail.getId());
            query.executeUpdate();

            em.merge(userDetails);
            transaction.commit();
            System.out.println(userDetails);
        } catch (RuntimeException e) {
            // Rollback the transaction if an exception occurs
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e; // Re-throw the exception
        }
    }

    @Override
    public void deletedUserDetail(int id) {

    }

    @Override
    public void getUserDetailByUsername(String username) {
        User user = repoUser.getUserByUsername(username);
        Query query = em.createNativeQuery("SELECT * FROM userDetails WHERE id = ?", UserDetails.class);
        query.setParameter(1, user.getUserDetails().getId());
        UserDetails userDetails = new UserDetails();
        List<UserDetails> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            userDetails.setId(resultList.get(0).getId());
            userDetails.setFirstName(resultList.get(0).getFirstName());
            userDetails.setLastName(resultList.get(0).getLastName());
            userDetails.setDateOfBirth(resultList.get(0).getDateOfBirth());
            userDetails.setAddress(resultList.get(0).getAddress());
        } else {
            System.out.println("No UserDetails found for the given user.");
        }

        System.out.println(userDetails.toStringView());

    }
}
