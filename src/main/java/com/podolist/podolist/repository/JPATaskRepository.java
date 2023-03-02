package com.podolist.podolist.repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.podolist.podolist.Entity.Task;

public interface JPATaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByStartedAtBetweenAndDeletedIs(LocalDateTime d1, LocalDateTime d2, boolean deleted);

    default List<Task> getAllTasksAtDate(LocalDateTime date) {
        var startedDate = date.with(LocalTime.of(0, 0, 0, 0));
        var endDate = date.with(LocalTime.of(23, 59, 59, 999));
        // Calendar startDateCalendar=Calendar.getInstance();
        // Calendar endDateCalendar=Calendar.getInstance();
        
        // startDateCalendar.setTime(date);
        // endDateCalendar.setTime(date);
        // startDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
        // endDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
        // endDateCalendar.add(Calendar.DATE, 1);
        // Date startDate=startDateCalendar.getTime();
        // Date endDate=endDateCalendar.getTime();
        return findByStartedAtBetweenAndDeletedIs(startedDate, endDate,false);
    }
}
