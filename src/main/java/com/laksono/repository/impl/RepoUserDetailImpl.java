package com.laksono.repository.impl;

import com.laksono.entity.User;
import com.laksono.entity.UserDetails;
import com.laksono.repository.RepoUserDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        String[] headers = {"First Name", "Last Name", "Address", "Date of Birth"};
        String userDetailData = Stream.of(userDetails)
                .map(ud -> new String[]{ud.getFirstName(), ud.getLastName(), ud.getAddress(), String.valueOf(ud.getDateOfBirth())})
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            int[] columnWidths = new int[headers.length];
                            list.forEach(data -> {
                                for (int i = 0; i < data.length; i++) {
                                    columnWidths[i] = Math.max(columnWidths[i], data[i].length());
                                }
                            });

                            StringBuilder sb = new StringBuilder();
                            sb.append("Data Detail user inserted successfully:\n");

                            // Print header row
                            printRow(sb, headers, columnWidths);

                            // Print separator
                            printSeparator(sb, columnWidths);

                            // Print data rows
                            list.forEach(data -> printRow(sb, data, columnWidths));

                            // Print bottom separator
                            printSeparator(sb, columnWidths);

                            return sb.toString();
                        }));
        System.out.println(userDetailData);
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

    private static void printRow(StringBuilder sb, String[] rowData, int[] columnWidths) {
        sb.append("|");
        for (int i = 0; i < rowData.length; i++) {
            sb.append(" ");
            sb.append(String.format("%-" + columnWidths[i] + "s", rowData[i]));
            sb.append(" |");
        }
        sb.append("\n");
    }

    private static void printSeparator(StringBuilder sb, int[] columnWidths) {
        sb.append("+");
        for (int width : columnWidths) {
            sb.append("-".repeat(width + 2)).append("+");
        }
        sb.append("\n");
    }
}
