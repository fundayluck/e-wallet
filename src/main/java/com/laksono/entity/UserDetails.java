package com.laksono.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "userDetails")
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "address")
    private String address;
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;



    public UserDetails(String first_name, String last_name, String address, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public UserDetails() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }

    public String toStringView() {
        Map<String, String> infoMap = Stream.of(
                        new String[]{"First Name", firstName},
                        new String[]{"Last Name", lastName},
                        new String[]{"Address", address},
                        new String[]{"Date of Birth", String.valueOf(dateOfBirth)})
                .collect(Collectors.toMap(data -> data[0], data -> data[1]));

        int maxLengthField = infoMap.keySet().stream().mapToInt(String::length).max().orElse(0);
        int maxLengthValue = infoMap.values().stream().mapToInt(String::length).max().orElse(0);

        int fieldWidth = Math.max(maxLengthField, 10);
        int valueWidth = Math.max(maxLengthValue, 10);

        StringBuilder sb = new StringBuilder();
        String line = "+-" + "-".repeat(fieldWidth) + "-+-" + "-".repeat(valueWidth) + "-+\n";

        sb.append(line);
        sb.append(String.format("| %-" + fieldWidth + "s | %-" + valueWidth + "s |\n", "Field", "Value"));
        sb.append(line);

        infoMap.forEach((field, value) ->
                sb.append(String.format("| %-" + fieldWidth + "s | %-" + valueWidth + "s |\n", field, value)));

        sb.append(line);
        return sb.toString();
    }
}
