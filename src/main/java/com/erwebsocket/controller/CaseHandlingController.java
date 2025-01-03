package com.erwebsocket.controller;

import com.erwebsocket.model.Admin;
import com.erwebsocket.model.CaseHandling;
import com.erwebsocket.model.CaseLog;
import com.erwebsocket.model.Emergency;
import com.erwebsocket.repository.AdminRepository;
import com.erwebsocket.repository.CaseHandlingRepository;
import com.erwebsocket.repository.CaseLogRepository;
import com.erwebsocket.repository.EmergencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cases")
public class CaseHandlingController {

    @Autowired
    private CaseHandlingRepository caseHandlingRepository;

    @Autowired
    private EmergencyRepository emergencyRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CaseLogRepository caseLogRepository;

    @GetMapping("/{caseid}")
    public ResponseEntity<CaseHandling> findByCaseid(@PathVariable Long caseid) {
        // Fetch the CaseHandling entity
        CaseHandling caseHandling = caseHandlingRepository.findByCaseid(caseid)
                .orElseThrow(() -> new IllegalArgumentException("CaseHandling not found with id: " + caseid));

        // Return the DTO
        return ResponseEntity.ok(caseHandling);
    }

    @GetMapping("/unhandled")
    public ResponseEntity<List<Emergency>> getUnhandledCases() {
        List<Emergency> unhandledEmergencies = emergencyRepository.findUnhandledReports();
        return ResponseEntity.ok(unhandledEmergencies);
    }

    @GetMapping("/admin/{adminid}/active")
    public ResponseEntity<List<CaseHandling>> getCasesByAdminAndActiveEmergency(@PathVariable Long adminid) {
        List<CaseHandling> cases = caseHandlingRepository.findByAdminidAndActiveEmergency(adminid);
        return ResponseEntity.ok(cases);
    }

    @GetMapping("/admin/{adminid}/closed")
    public ResponseEntity<List<CaseHandling>> getCasesByAdminAndClosedEmergency(@PathVariable Long adminid) {
        List<CaseHandling> cases = caseHandlingRepository.findByAdminidAndClosedEmergency(adminid);
        return ResponseEntity.ok(cases);
    }

    @GetMapping("/case-details/{caseid}")
    public ResponseEntity<CaseHandling> getCaseById(@PathVariable Long caseid) {
        CaseHandling caseHandling = caseHandlingRepository.findBycaseidWithUser(caseid);
        if (caseHandling != null) {
            return ResponseEntity.ok(caseHandling);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCaseHandling(@RequestBody CreateCaseHandlingRequest request) {
        try {
            // Check if the emergency exists
            Optional<Emergency> emergencyOptional = emergencyRepository.findById(request.getEmergencyId());
            if (emergencyOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Emergency with the given ID does not exist.");
            }

            // Check if the admin exists
            Optional<Admin> adminOptional = adminRepository.findById(request.getAdminId());
            if (adminOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin with the given ID does not exist.");
            }

            // Create new CaseHandling
            CaseHandling caseHandling = new CaseHandling();
            caseHandling.setEmergency(emergencyOptional.get());
            caseHandling.setAdmin(adminOptional.get());
            caseHandling.setTimestamp(LocalDateTime.now());

            // Save the CaseHandling
            CaseHandling savedCaseHandling = caseHandlingRepository.save(caseHandling);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedCaseHandling);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // DTO for API request
    public static class CreateCaseHandlingRequest {
        private Long emergencyId;
        private Long adminId;

        // Getters and setters
        public Long getEmergencyId() {
            return emergencyId;
        }

        public void setEmergencyId(Long emergencyId) {
            this.emergencyId = emergencyId;
        }

        public Long getAdminId() {
            return adminId;
        }

        public void setAdminId(Long adminId) {
            this.adminId = adminId;
        }
    }

    @PostMapping("/add-caselog")
    public ResponseEntity<CaseLog> createCaseLog(@RequestBody CaseLogRequest request) {
        // Fetch CaseHandling by caseId
        CaseHandling caseHandling = caseHandlingRepository.findByCaseid(request.getCaseId())
                .orElseThrow(() -> new IllegalArgumentException("Case not found with id: " + request.getCaseId()));

        CaseLog caseLog = new CaseLog();
        caseLog.setCaseHandling(caseHandling);
        caseLog.setActionTaken(request.getActionTaken());
        caseLog.setTimestamp(LocalDateTime.now());

        caseLogRepository.save(caseLog);

        return ResponseEntity.ok(caseLog);
    }

    public static class CaseLogRequest {
        private Long caseId;
        private String actionTaken;

        // Getters and Setters
        public Long getCaseId() {
            return caseId;
        }

        public void setCaseId(Long caseId) {
            this.caseId = caseId;
        }

        public String getActionTaken() {
            return actionTaken;
        }

        public void setActionTaken(String actionTaken) {
            this.actionTaken = actionTaken;
        }
    }


    // API to set isFalseMessage to true
    @PutMapping("/{id}/mark-false")
    public ResponseEntity<String> markAsFalseMessage(@PathVariable("id") Long emergencyId) {
        return emergencyRepository.findById(emergencyId).map(emergency -> {
            emergency.setIsFalseMessage(true);
            emergency.setStatus("closed");
            emergency.setCloseTimestamp(LocalDateTime.now());
            emergencyRepository.save(emergency);
            return ResponseEntity.ok("Emergency marked as false message successfully.");
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Emergency not found."));
    }

    // API to set isPublish to true
    @PutMapping("/{id}/publish")
    public ResponseEntity<String> PublishEmergency (@PathVariable("id") Long emergencyId) {
        return emergencyRepository.findById(emergencyId).map(emergency -> {
            emergency.setIsPublish(true);
            emergencyRepository.save(emergency);
            return ResponseEntity.ok("Emergency published successfully.");
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Emergency not found."));
    }

    // API to set isResolve to true
    @PutMapping("/{id}/resolve")
    public ResponseEntity<String> ResolveEmergency (@PathVariable("id") Long emergencyId) {
        return emergencyRepository.findById(emergencyId).map(emergency -> {
            emergency.setStatus("closed");
            emergency.setCloseTimestamp(LocalDateTime.now());
            emergencyRepository.save(emergency);
            return ResponseEntity.ok("Emergency has resolved.");
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Emergency not found."));
    }

    // API to update status to closed and isPublish
    @PutMapping("/{id}/update-status")
    public ResponseEntity<String> ResolveEmergencyWithPublishStatus(
            @PathVariable("id") Long emergencyId,
            @RequestBody Boolean isPublish
    ) {
        return emergencyRepository.findById(emergencyId).map(emergency -> {
            emergency.setStatus("closed");
            emergency.setIsPublish(isPublish);
            emergency.setCloseTimestamp(LocalDateTime.now());
            emergencyRepository.save(emergency);
            return ResponseEntity.ok("Emergency status and publish state updated successfully.");
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Emergency not found."));
    }
}
