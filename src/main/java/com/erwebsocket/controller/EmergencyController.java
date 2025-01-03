package com.erwebsocket.controller;

import com.erwebsocket.model.Media;
import com.erwebsocket.model.Emergency;
import com.erwebsocket.model.EmergencyLog;
import com.erwebsocket.model.User;
import com.erwebsocket.repository.MediaRepository;
import com.erwebsocket.repository.EmergencyLogRepository;
import com.erwebsocket.repository.EmergencyRepository;
import com.erwebsocket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reports")
public class EmergencyController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmergencyRepository emergencyRepository;

    @Autowired
    private EmergencyLogRepository emergencyLogRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    //Create New Report
    @PostMapping("/create")
    public ResponseEntity<String> createReport(
            @RequestParam("emergencyType") String emergencyType,
            @RequestParam("isVictim") Boolean isVictim,
            @RequestParam("latitude") BigDecimal latitude,
            @RequestParam("longitude") BigDecimal longitude,
            @RequestParam("description") String description,
            @RequestParam(value = "detailed_address", required = false) String detailed_address,
            @RequestParam(value = "mediaFiles", required = false) List<MultipartFile> mediaFiles,
            @RequestHeader("userId") String userId // Assume userId is sent as a header for simplicity
    ) {
        try {
            //Validate User
            Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));
            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid userId: User does not exist");
            }

            User user = userOptional.get();

            // Step 1: Create a new Report
            Emergency emergency = new Emergency();
            emergency.setUser(user); // Assuming User is referenced by ID
            emergency.setEmergencyType(emergencyType);
            emergency.setIsVictim(isVictim);
            emergency.setTimestamp(LocalDateTime.now());
            emergency.setCloseTimestamp(null);
            emergency.setStatus("active");
            emergency.setIsPublish(false);
            emergency.setIsFalseMessage(false);
            emergency.setLatitude(latitude);
            emergency.setLongitude(longitude);

            emergency = emergencyRepository.save(emergency);

            // Step 2: Create a new ReportLog
            EmergencyLog emergencyLog = new EmergencyLog();
            emergencyLog.setEmergency(emergency);
            emergencyLog.setDescription(description);
            emergencyLog.setDetailedAddress(detailed_address);
            emergencyLog.setTimestamp(LocalDateTime.now());

            emergencyLog = emergencyLogRepository.save(emergencyLog);

            // Step 3: Handle Media Files
            if (mediaFiles != null && !mediaFiles.isEmpty()) {
                for (MultipartFile mediaFile : mediaFiles) {
                    // Define the public directory path (relative to the project root)
                    String publicDir = System.getProperty("user.dir") + File.separator + "public" + File.separator + "uploads" + File.separator;
                    File dir = new File(publicDir);
                    System.out.println("Saving file to: " + publicDir);

                    // Create the directory if it doesn't exist
                    if (!dir.exists()) {
                        if (!dir.mkdirs()) {
                            throw new IOException("Failed to create directory: " + publicDir);
                        }
                    }

                    try {
                        // Generate a unique filename
                        String originalFilename = mediaFile.getOriginalFilename();
                        String fileExtension = "";

                        // Extract file extension (if it exists)
                        int dotIndex = originalFilename.lastIndexOf('.');
                        if (dotIndex > 0) {
                            fileExtension = originalFilename.substring(dotIndex); // e.g., .jpg, .png
                            originalFilename = originalFilename.substring(0, dotIndex); // Remove extension from filename
                        }

                        // Append a timestamp to the filename
                        String uniqueFileName = originalFilename + "_" + System.currentTimeMillis() + fileExtension;

                        // Construct the file path
                        String filePath = publicDir + uniqueFileName;

                        // Save the file to the public/uploads directory
                        File fileToSave = new File(filePath);
                        mediaFile.transferTo(fileToSave);

                        // Create a Media entry
                        Media media = new Media();
                        media.setEmergencyLog(emergencyLog);
                        media.setMediaType(mediaFile.getContentType());
                        media.setMediaPath(uniqueFileName); // Store filename
                        mediaRepository.save(media);
                    } catch (IOException e) {
                        // Log and handle the exception if file saving fails
                        e.printStackTrace();
                        throw new RuntimeException("Failed to save the file: " + mediaFile.getOriginalFilename(), e);
                    }
                }
            }

            messagingTemplate.convertAndSend("/topic/reports", emergency);

            return ResponseEntity.ok("Report created successfully!");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }


    //Update Report Log
    @PostMapping("/update-reportlog")
    public ResponseEntity<String> updateReportLog(
            @RequestParam("emergencyId") Long emergencyId,
            @RequestParam("description") String description,
            @RequestParam(value = "detailed_address", required = false) String detailedAddress,
            @RequestParam(value = "mediaFiles", required = false) List<MultipartFile> mediaFiles
    ) {
        try {
            // Fetch the report
            Optional<Emergency> reportOptional = emergencyRepository.findById(emergencyId);
            if (reportOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid reportId: Report does not exist");
            }

            Emergency emergency = reportOptional.get();

            // Create a new ReportLog
            EmergencyLog emergencyLog = new EmergencyLog();
            emergencyLog.setEmergency(emergency);
            emergencyLog.setDescription(description);
            emergencyLog.setDetailedAddress(detailedAddress);
            emergencyLog.setTimestamp(LocalDateTime.now());

            emergencyLog = emergencyLogRepository.save(emergencyLog);

            // Handle Media Files
            if (mediaFiles != null && !mediaFiles.isEmpty()) {
                for (MultipartFile mediaFile : mediaFiles) {
                    String publicDir = System.getProperty("user.dir") + File.separator + "public" + File.separator + "uploads" + File.separator;
                    File dir = new File(publicDir);
                    if (!dir.exists() && !dir.mkdirs()) {
                        throw new IOException("Failed to create directory: " + publicDir);
                    }

                    try {
                        // Generate a unique filename
                        String originalFilename = mediaFile.getOriginalFilename();
                        String fileExtension = originalFilename != null && originalFilename.contains(".")
                                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                                : "";
                        String uniqueFileName = (originalFilename != null ? originalFilename.substring(0, originalFilename.lastIndexOf(".")) : "file")
                                + "_" + System.currentTimeMillis() + fileExtension;

                        String filePath = publicDir + uniqueFileName;

                        // Save the file
                        File fileToSave = new File(filePath);
                        mediaFile.transferTo(fileToSave);

                        // Save media info
                        Media media = new Media();
                        media.setEmergencyLog(emergencyLog);
                        media.setMediaType(mediaFile.getContentType());
                        media.setMediaPath(uniqueFileName);
                        mediaRepository.save(media);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Failed to save the file: " + (mediaFile.getOriginalFilename() != null ? mediaFile.getOriginalFilename() : ""), e);
                    }
                }
            }

            return ResponseEntity.ok("Report log updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }


    // Fetch all reports
    @GetMapping
    public List<Emergency> getAllReports() {
        return emergencyRepository.findAll();
    }

    // Fetch reports by user ID
    @GetMapping("/report-history/{userId}")
    public List<Emergency> getReportHistoryByUserId(@PathVariable Long userId) {
        LocalDateTime tenDaysAgo = LocalDateTime.now().minusDays(10); // Calculate the date 10 days ago
        return emergencyRepository.findByUserUseridAndTimestampAfterOrderByTimestampDesc(userId, tenDaysAgo);
    }

    // Fetch recent report
    @GetMapping("/recent-report")
    public List<Emergency> getRecentReport() {
        LocalDateTime oneDaysAgo = LocalDateTime.now().minusHours(36); // Calculate the date 1 days ago
        return emergencyRepository.findByIsPublishTrueAndTimestampAfterOrderByTimestampDesc(oneDaysAgo);
    }

    // Fetch recent report
    @GetMapping("/emergency-detail/{emergencyId}")
    public Emergency getReportDetail(@PathVariable Long emergencyId) {
        return emergencyRepository.findByEmergencyid(emergencyId);
    }

}