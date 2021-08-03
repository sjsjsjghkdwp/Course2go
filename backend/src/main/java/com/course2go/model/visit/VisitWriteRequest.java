package com.course2go.model.visit;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitWriteRequest {
    private Integer visitPid;
    private String visitContent;
    private Integer visitTime;
    private Integer visitCost;
    private MultipartFile visitImage1;
    private MultipartFile visitImage2;
    private MultipartFile visitImage3;
}
