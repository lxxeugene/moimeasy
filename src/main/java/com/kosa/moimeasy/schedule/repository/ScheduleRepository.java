package com.kosa.moimeasy.schedule.repository;

import com.kosa.moimeasy.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    //모임 id로 스케쥴 이벤트 가져오기
    List<Schedule> findByMoeim_MoeimId(Long moeimId);
}
