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
        // 受け取った日付文字列をLocalDateに変換
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(date, formatter);

        // 日付に基づいた処理を行う（例: DBから該当するデータを取得）
        model.addAttribute("date", parsedDate);

        // 結果を表示するビュー名を返す
        return "create";  // create.html というテンプレートに遷移
    }
	
	// Postリクエストでフォームから送信されたデータを受け取るメソッド
    @PostMapping("/main/create")
    public String handleSubmit(
    		Model model, TaskForm form, @AuthenticationPrincipal AccountUserDetails user
            ) {
        

        // データの処理
        Tasks task = new Tasks();
        
        repo.save(task);
        
        // /main へリダイレクト
        return "redirect:/main";
        
    }
}
