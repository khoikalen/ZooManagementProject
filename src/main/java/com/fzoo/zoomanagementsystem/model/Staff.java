package com.fzoo.zoomanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;

    private String lastName;

    private String sex;

    @JsonFormat(pattern = "MM-dd-yyyy")
    private LocalDate startDay;

    private String email;

    private String phoneNumber;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Cage> cage;
}
