package com.example.clothingstore.entities;

import com.example.clothingstore.custom.Unique;
import com.example.clothingstore.custom.ValidPassword;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "customer")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "name", nullable = false)
    @NotNull
    @Size(min = 2, max = 25)
    String name;

    @Column(name = "surname", nullable = false)
    @NotNull
    @Size(min = 3, max = 25)
    String surname;

    @Column(name = "email", nullable = false)
    @NotNull
    @NotBlank
    @Email
    @Size(min = 10, max = 30)
    String email;

    @Column(name = "username", nullable = false)
    @NotNull
    @NotBlank
    @Size(min = 4, max = 25)
    @Unique
    String username;

    @Column(name = "password", nullable = false)
    @NotNull
    @ValidPassword
    String password;


    @Column(name = "date_registration")
    LocalDate dateRegistration;

    @ManyToMany
    @JoinTable(name = "orders",
                joinColumns = @JoinColumn(name = "customer_id"),
                inverseJoinColumns = @JoinColumn(name = "clothes_id"))
    private List<Clothes> clothes;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
