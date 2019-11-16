package com.fitcrew.FitCrewAppTrainings.domains;

import lombok.*;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;

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

	@Column(name = "TrainingName", nullable = false, length = 100, unique = true)
	private String trainingName;

	@Column(nullable = false, length = 400)
	private String description;

	@Column(nullable = false)
	private String training;

	@Column(name = "TrainerEmail", nullable = false, length = 50)
	private String trainerEmail;

	@Column(name = "Clients")
	@ElementCollection(targetClass=String.class)
	private List<String> clients;
}
