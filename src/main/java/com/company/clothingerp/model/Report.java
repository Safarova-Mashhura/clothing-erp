package com.company.clothingerp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String type; // SALES, INVENTORY, CRM, FINANCIAL

    @Column(name = "generated_date", nullable = false)
    @Builder.Default
    private LocalDateTime generatedDate = LocalDateTime.now();

    @Column(name = "generated_by", nullable = false)
    private String generatedBy;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "summary_data", length = 2000)
    private String summaryData;
}
