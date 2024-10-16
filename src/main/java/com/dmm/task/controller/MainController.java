package com.dmm.task.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TasksRepository;
import com.dmm.task.service.AccountUserDetails;

@Controller
public class MainController {

	@Autowired
	private TasksRepository repo;

	/**
	 * 投稿の一覧表示.
	 * 
	 * @param model モデル
	 * @return 遷移先
	 */
	@GetMapping("/main")
	public String main(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
			@AuthenticationPrincipal AccountUserDetails userDetails,
			Model model) throws Exception {

		//2次元表になるので、ListのListを用意する
		List<List<LocalDate>> month = new ArrayList<>();

		//1週間分のLocalDateを格納するListを用意する
		List<LocalDate> week = new ArrayList<>();

		//日にちを格納
		LocalDate day;

		LocalDate now = LocalDate.now(); //現在の日付

		//nullなら今月を代入
		if (date != null) {
			now = date;
		} else {
			now = LocalDate.now();
		}

		LocalDate firstDayOfMonth = now.withDayOfMonth(1); //今月の1日
		LocalDate previousMonth = firstDayOfMonth.minusMonths(1); //前月の1日
		LocalDate nextMonth = firstDayOfMonth.plusMonths(1); //翌月の1

		// モデルに前月と翌月のリンク情報を追加
		model.addAttribute("prev", previousMonth);
		model.addAttribute("next", nextMonth);

		//曜日を表すDayOfWeekを取得し、
		//上で取得したLocalDateに曜日の値（DayOfWeek#getValue)をマイナスして前月分のLocalDateを求める
		DayOfWeek startWeek = firstDayOfMonth.getDayOfWeek();
		int weekValue = startWeek.getValue() % 7;
		LocalDate prevMonthStart = firstDayOfMonth.minusDays(weekValue);
		day = prevMonthStart;

		int lastDayOfMonth = now.lengthOfMonth(); //今月の月末までの日数を取得

		//1日ずつ増やしてLocalDateを求めめる
		for (int i = 0; i < 7; i++) {
			week.add(day); // 週のリストへ格納
			day = day.plusDays(1); // 1日進める

		}

		month.add(week); // 1週目のリストを、月のリストへ格納する
		week = new ArrayList<>(); // 次週のリストを新しくつくる

		int currentMonth = day.getMonthValue(); //今月を保管

		//2週目以降
		for (int i = 1; i <= lastDayOfMonth; i++) {
			week.add(day); // 続きの日付を追加
			day = day.plusDays(1); // 1日進める

			// 週が終わったらリストをカレンダーに追加し、oneWeekをリセット
			if (week.size() == 7) {
				month.add(week); // リストをカレンダーに追加
				week = new ArrayList<>(); // 次週のリストを新しくつくる
			}

			// 翌月になったらループを抜ける
			if (currentMonth != day.getMonthValue()) {
				break;
			}
		}

		// 最終週の翌月分
		DayOfWeek w = day.getDayOfWeek();
		for (int i = 0; i < 7 - w.getValue(); i++) {
			week.add(day);
			day = day.plusDays(1);
		}

		if (!week.isEmpty()) {
			month.add(week);
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月");
		String formatted = firstDayOfMonth.format(formatter);

		model.addAttribute("matrix", month);
		model.addAttribute("month", formatted);
		model.addAttribute("week", week);

		//タスクを表示させる
		MultiValueMap<LocalDate, Tasks> tasks = new LinkedMultiValueMap<LocalDate, Tasks>();

		List<Tasks> tasksList;

		if (userDetails.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
			tasksList = repo.findAll();

		} else {
			tasksList = repo.findByName(userDetails.getName());
		}
		for (Tasks task : tasksList) {
			LocalDate taskDate = task.getDate();
			tasks.add(taskDate, task);
		}

		model.addAttribute("tasks", tasks);

		return "/main";

	}

}
