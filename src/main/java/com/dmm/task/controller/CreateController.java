package com.dmm.task.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
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
	public String create(@PathVariable("date") String date, Model model) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate parsedDate = LocalDate.parse(date, formatter);

		model.addAttribute("date", parsedDate);

		return "create";
	}

	@PostMapping("/main/create")
	public String createPost(Model model, TaskForm form, @AuthenticationPrincipal AccountUserDetails user) {
	    Tasks task = new Tasks();

		task.setName(user.getName());
		task.setTitle(form.getTitle()); // タイトルを設定
	    task.setDate(form.getDate()); // 日付を設定
	    task.setText(form.getText()); // 内容を設定
	    
		model.addAttribute(task);
		repo.save(task);
	

		// /main へリダイレクト
		return "redirect:/main";

	}
}
