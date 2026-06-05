package com.company.clothingerp.service;

import com.company.clothingerp.model.Report;
import com.company.clothingerp.model.SystemLog;
import com.company.clothingerp.repository.ReportRepository;
import com.company.clothingerp.repository.SystemLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private SystemLogRepository systemLogRepository;

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report saveReport(Report report) {
        if (report.getGeneratedDate() == null) {
            report.setGeneratedDate(LocalDateTime.now());
        }
        return reportRepository.save(report);
    }

    // System audit logs
    public List<SystemLog> getAllSystemLogs() {
        return systemLogRepository.findAllByOrderByTimestampDesc();
    }

    public SystemLog logAction(String action, String details, String performedBy) {
        SystemLog log = SystemLog.builder()
                .action(action)
                .details(details)
                .performedBy(performedBy)
                .timestamp(LocalDateTime.now())
                .build();
        return systemLogRepository.save(log);
    }
}
