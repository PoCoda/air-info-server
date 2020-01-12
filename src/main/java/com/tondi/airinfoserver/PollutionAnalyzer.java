package com.tondi.airinfoserver;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tondi.airinfoserver.db.DbConnector;
import com.tondi.airinfoserver.model.status.StatusModel;

@Service
public class PollutionAnalyzer {
	@Autowired
	DbConnector dbConnector;

	public Integer getDaysMatchingNormsStreak() {
		return calculateDaysStreakOf((StatusModel status) -> status.getMatchesNorms());
	}

	public Integer getDaysExceedingNormsStreak() {
		return calculateDaysStreakOf((StatusModel status) -> !status.getMatchesNorms());
	}

	private Integer calculateDaysStreakOf(Function<StatusModel, Boolean> predicate) {
		LocalDate day = LocalDate.now();
		Integer daysStreak = 0;

		StatusModel queryResult = dbConnector.getAverageStatusForDay(day);
		while (queryResult != null && predicate.apply(queryResult)) {
			daysStreak++;
			queryResult = dbConnector.getAverageStatusForDay(day.minusDays(daysStreak));
		}

		return daysStreak;
	};

	/**
	 * Returns number of days not worse than today, including present day. For
	 * example, for few days measured from present to past: 98 75 30 100 method
	 * returns 3. It means there is no worse day than today in the last 3 days.
	 * 
	 * @return
	 */
	public Integer getWorstSinceDays() {
		return this.getDaysFulfillingCondition((Double current, Double last) -> current >= last);
	}

	public Integer getBestSinceDays() {
		return this.getDaysFulfillingCondition((Double current, Double last) -> current <= last);
	}

	private Integer getDaysFulfillingCondition(BiFunction<Double, Double, Boolean> predicate) {
		LocalDate day = LocalDate.now();
		Integer daysSince = 1;

		StatusModel queryResult = dbConnector.getAverageStatusForDay(day);
		Double harmFactor = queryResult.calculateHarmFactor();
		while (queryResult != null) {
			System.out.println(harmFactor);
			System.out.println(day.minusDays(daysSince));

			queryResult = dbConnector.getAverageStatusForDay(day.minusDays(daysSince));
			Double newHarmFactor = Double.MAX_VALUE;
			if (queryResult != null) {
				newHarmFactor = queryResult.calculateHarmFactor();
			}

			System.out.println(newHarmFactor);

			if (predicate.apply(newHarmFactor, harmFactor)) {
				break;
			}

			harmFactor = newHarmFactor;
			daysSince++;
		}

		return daysSince;
	}
	
	// TODO add to AirInfoController
	// TODO check if works
	public Double getThisWeekAverage() {
		List<LocalDate> thisWeek = getDaysOfThisWeek();

		DoubleSummaryStatistics summary = thisWeek.stream()
			.map((LocalDate date) -> dbConnector.getAverageStatusForDay(date))
			.mapToDouble((StatusModel dayAverage) -> dayAverage.calculateHarmFactorPercentage())
			.summaryStatistics();
		
		return summary.getAverage();
	}
	
	// TODO check if works
	private List<LocalDate> getDaysOfThisWeek() {
		ArrayList<LocalDate> week = new ArrayList<LocalDate>(); 
		LocalDate day = LocalDate.now();

		DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
		LocalDate startOfCurrentWeek = day.with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
		
		Integer days = 1;
		while(day.isAfter(startOfCurrentWeek) || day.equals(startOfCurrentWeek)) {
			week.add(day);
			day = day.minusDays(days);
			days++;
		}
		
		return week;
	}
}
