package com.dmm.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TasksRepository;
import com.dmm.task.form.DditForm;
import com.dmm.task.service.AccountUserDetails;

@Controller
public class EditController {

	@Autowired
	private TasksRepository repo;

	/**
	 * 投稿の一覧表示.
	 * 
	 * @param model モデル
	 * @return 遷移先
	 */
	@GetMapping("/main/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {

		repo.findById(id);
		// タスクが見つかった場合はモデルに追加
		Tasks task = repo.findById(id).orElse(null);

		// タスクが見つかった場合はモデルに追加
		if (task != null) {
			model.addAttribute("task", task);
			return "edit"; // edit.htmlテンプレートを返す
		} else {
			return "redirect:/main"; // タスクが見つからない場合はメインページへリダイレクト
		}
	}

	/**
	 * 投稿を編集
	 * 
	 * @param postForm 送信データ
	 * @param user     ユーザー情報
	 * @return 遷移先
	 */
	@PostMapping("/main/edit/{id}")
	public String updateTask(Model model, DditForm form, @AuthenticationPrincipal AccountUserDetails user,
			@PathVariable Integer id) {
		Tasks task = repo.getById(id);
		task.setName(user.getName());
		task.setTitle(form.getTitle());
		task.setDate(form.getDate());
		task.setDone(form.isDone());
		task.setText(form.getText());

		repo.save(task);
		// /main へリダイレクト
		return "redirect:/main";
	}

	/**
	 * 投稿を削除する
	 * 
	 * @param id 投稿ID
	 * @return 遷移先
	 */
	@PostMapping("/main/delete/{id}")
	public String deletePost(@PathVariable Integer id) {
		Tasks task = new Tasks();
		task.setId(id);

		repo.delete(task);

		return "redirect:/main";
	}

}
