package com.webtech.efurniture.userRepository;

import com.webtech.efurniture.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    /**
     * Retrieve the 10 most recent activity logs, ordered by timestamp in descending order.
     *
     * @return a list of the top 10 recent activity logs
     */
    List<ActivityLog> findTop10ByOrderByTimestampDesc();
}
