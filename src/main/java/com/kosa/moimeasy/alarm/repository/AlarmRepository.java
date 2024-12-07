package com.kosa.moimeasy.alarm.repository;

import com.kosa.moimeasy.alarm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
