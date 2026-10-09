package com.entity;

import java.time.LocalDate;
import java.util.List;

import com.enums.ProjectStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
   
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String description;
	
	private LocalDate startsDate;
	
	private LocalDate endsDate;
	
	@Enumerated(EnumType.STRING)
	private ProjectStatus status;
	
	@ManyToOne
	@JoinColumn(name = "created_by")
	private User createdBy;
	
	@OneToMany(mappedBy = "project",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true)
	private List<Bug> bugs;
	
	@OneToMany(mappedBy = "project",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true)
	private List<ProjectMember> members;

	
	
}
