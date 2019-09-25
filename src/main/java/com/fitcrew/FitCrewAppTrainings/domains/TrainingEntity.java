package com.fitcrew.FitCrewAppTrainings.domains;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
public class TrainingEntity implements Serializable {

    private static final long serialVersionUID = -3255126230685615683L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 400)
    private String description;

    @Column(nullable = false)
    private String training;

    @Column(name = "CreatedBy", nullable = false, length = 50)
    private String createdBy;
}
