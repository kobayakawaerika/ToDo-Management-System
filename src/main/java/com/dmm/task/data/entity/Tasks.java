package com.dmm.task.data.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Tasks {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主キーとなるフィールド
    
    private String taskName;
    private LocalDate dueDate;
    private boolean completed;

    
    
}
