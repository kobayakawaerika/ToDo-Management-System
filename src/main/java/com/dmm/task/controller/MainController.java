package com.dmm.task.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.dmm.task.data.entity.Tasks;

@Controller
public class MainController {

	@GetMapping("/main")
	public String main(Model model) throws Exception {

		//2次元表になるので、ListのListを用意する
		List<List<LocalDate>> month = new ArrayList<>();

		//1週間分のLocalDateを格納するListを用意する
		List<LocalDate> week = new ArrayList<>();

		//日にちを格納
		LocalDate day;

		//その月の1日のLocalDateを取得する
		LocalDate now = LocalDate.now();
		LocalDate firstDayOfMonth = now.withDayOfMonth(1);

		//曜日を表すDayOfWeekを取得し、
		//上で取得したLocalDateに曜日の値（DayOfWeek#getValue)をマイナスして前月分のLocalDateを求める
		DayOfWeek startWeek = firstDayOfMonth.getDayOfWeek();
		int weekValue = startWeek.getValue();
		LocalDate prevMonth = firstDayOfMonth.minusDays(weekValue);
		day = prevMonth;

		int lastDayOfMonth = now.lengthOfMonth(); //今月の月末までの日数を取得

		//1日ずつ増やしてLocalDateを求めめる
		for (int i = 1; i <= 7; i++) {
			week.add(day); // 週のリストへ格納
			day = day.plusDays(1); // 1日進める
		}
		month.add(week); // 1週目のリストを、月のリストへ格納する
		week = new ArrayList<>(); // 次週のリストを新しくつくる

		//2週目以降
		for (int i = 1; i <= lastDayOfMonth; i++) {
			week.add(day); // 続きの日付を追加
			day = day.plusDays(1); // 1日進める

			// 週が終わったらリストをカレンダーに追加し、oneWeekをリセット
			if (week.size() == 7) {
				month.add(week); // リストをカレンダーに追加
				week = new ArrayList<>(); // 次週のリストを新しくつくる
			}
		}

		// 最終週の翌月分
		DayOfWeek w = day.getDayOfWeek();
		for (int i = 1; i < 7 - w.getValue(); i++) {
			week.add(day);
			day = day.plusDays(1);
		}
		
		MultiValueMap<LocalDate, Tasks> tasks = new LinkedMultiValueMap<LocalDate, Tasks>();
		model.addAttribute("matrix", month);
		model.addAttribute("month", day);
		model.addAttribute("week", week);
		model.addAttribute("tasks", tasks);
		
		return "/main";
	}	
}
