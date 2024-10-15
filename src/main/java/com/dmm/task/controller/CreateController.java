package com.dmm.task.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TasksRepository;
import com.dmm.task.form.TaskForm;
import com.dmm.task.service.AccountUserDetails;

@Controller
public class CreateController {

	@Autowired
	private TasksRepository repo;
	

	@GetMapping("/main/create/{date}")
	public String create(Model model, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
		return "create";
	}

	@PostMapping("/main/create")
	public String createPost(Model model, TaskForm form, @AuthenticationPrincipal AccountUserDetails user) {
	    Tasks task = new Tasks();
	    task.setId(task.getId());
		task.setName(user.getName());
		task.setTitle(form.getTitle()); 
		task.setDate(form.getDate());
	    task.setDone(false); // 新しいタスクは未完了であるため、falseを設定
	    task.setText(form.getText()); // 内容を設定
	    
		repo.save(task);
	

		// /main へリダイレクト
		return "redirect:/main";

	}
}
