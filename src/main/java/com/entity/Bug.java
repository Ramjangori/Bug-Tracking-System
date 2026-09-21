package com.entity;

import java.util.List;

import com.enums.BugPriority;
import com.enums.BugStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="bugs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bug {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	
	private String description;
	
	@ManyToOne
	private Project project;
	
	@Enumerated(EnumType.STRING)
	private BugPriority priority;
	
	@Enumerated(EnumType.STRING)
	private BugStatus status;
	
	@ManyToOne
	private User assignedTo;
	
	@ManyToOne
	private User createdBy;
	
	@OneToMany(mappedBy = "bug" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments;
	
	@OneToMany(mappedBy = "bug" , cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Attachment> attachments;
	
}
