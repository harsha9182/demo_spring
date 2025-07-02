package com.example.demo.service;


import com.example.demo.model.APIRequestValidation;
import com.example.demo.utils.AppUtils;
import com.example.demo.utils.ControllerUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class LocalStorageService {

    @Autowired
    private ControllerUtils controllerUtils;
    @Autowired
    private AppUtils appUtils;

    @Autowired
    private APIRequestValidationService apiRequestValidationService;


    @Autowired
    private ErrorLogService errorLogService;
    @Value("${upload.directory.windows}")
    private String windowsUploadDirectory;
    @Value("${upload.directory.linux}")
    private String linuxUploadDirectory;

    @Value("${upload.directory.default}")
    private String defaultUploadDirectory;
    long maxFileSize = 50 * 1024 * 1024;

    public String getUploadDirectory() {
        String os = System.getProperty("os.name").toLowerCase();
        String uploadDirectory;

        if (os.contains("win")) {
            uploadDirectory = windowsUploadDirectory;
        } else if (os.contains("nix") || os.contains("nux") || os.contains("ubuntu")) {

            uploadDirectory = linuxUploadDirectory;
        } else {

            uploadDirectory = defaultUploadDirectory;
        }

        return uploadDirectory;
    }




    public Map<String, Object> localFileUpload(MultipartFile multiPartFile, HttpServletRequest request) {
        try {
            // Validate the request


            // Validate file input
            if (multiPartFile.isEmpty()) {
                return controllerUtils.successResponse("Please select a file to upload");
            }
            if (multiPartFile.getSize() > maxFileSize) {
                return controllerUtils.failedResponse("File size exceeds the maximum limit of 50MB");
            }

            // Determine the base upload directory
            String uploadDirectory = getUploadDirectory();
            System.out.println("Base upload directory: " + uploadDirectory);

            // Get the current month and year in "MM-yyyy" format
            String monthYearFolder = new SimpleDateFormat("MM-yyyy").format(new Date());
            String subFolderPath = uploadDirectory + File.separator + monthYearFolder;

            // Ensure the subfolder exists
            File subFolder = new File(subFolderPath);
            if (!subFolder.exists()) {
                subFolder.mkdirs();
            }

            // Prepare file saving process
            String originalFilename = multiPartFile.getOriginalFilename().replaceAll("\\s+", "");
            String uuid = appUtils.generateUUID();
            String newFilename = uuid + "_" + originalFilename;
            Path path = Paths.get(subFolderPath, newFilename);

            // Save the file to the determined path
            Files.copy(multiPartFile.getInputStream(), path);

            // Generate a file URL for the saved file
            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .scheme(request.getScheme())
                    .path("/api/files/")
                    .path(monthYearFolder+"/")
                    .path(newFilename)
                    .build()
                    .toUriString();

            // Prepare the response
            Map<String, Object> response = new HashMap<>();
            response.put("url", fileUrl);
            return controllerUtils.successResponseList(response);

        } catch (Exception e) {
            // Log the error and return a failure response
            errorLogService.saveErrorLog("LocalStorageService/localFileUpload", "", "", e.getMessage(), "server");
            return controllerUtils.failedResponse("Error" + e.getMessage());
        }
    }



    public Map<String, Object> deleteFile(String url) {
        try {


            // Extract and decode the file name from the URL
            String rawFileName = url.substring(url.lastIndexOf("/") + 1);
            String decodedFileName = URLDecoder.decode(rawFileName, StandardCharsets.UTF_8.name());
            if (decodedFileName.endsWith("\"")) {
                decodedFileName = decodedFileName.substring(0, decodedFileName.length() - 1);
            }
            final String fileName = decodedFileName;

            // Determine the upload directory
            String uploadDirectory = getUploadDirectory();

            // Extract folder name from the URL (if present)
            String prefix = "files/";  // The known prefix before the folder name
            String folderName = "";
            int folderStart = url.indexOf(prefix) + prefix.length();
            int folderEnd = url.indexOf("/", folderStart);

            // Extract folder name from URL if found
            if (folderStart > prefix.length() - 1 && folderEnd > folderStart) {
                folderName = url.substring(folderStart, folderEnd);
            }

            // If folder name is provided, use it; otherwise, use the base directory
            File targetDirectory = (folderName != null && !folderName.isEmpty())
                    ? new File(uploadDirectory, folderName)
                    : new File(uploadDirectory);

            System.out.println("Searching for file: " + fileName + " in directory: " + targetDirectory.getAbsolutePath());

            // Validate the target directory
            if (!targetDirectory.exists() || !targetDirectory.isDirectory()) {
                return controllerUtils.failedResponse("Target directory does not exist or is invalid.");
            }

            // Locate and delete the file directly in the determined folder
            File targetFile = new File(targetDirectory, fileName);
            if (targetFile.exists()) {
                if (targetFile.delete()) {
                    return controllerUtils.successResponseList(Map.of("fileSelStatus", "File is successfully deleted"));
                } else {
                    return controllerUtils.failedResponse("Failed to delete file.");
                }
            }

            // File not found in the expected directory
            return controllerUtils.failedResponse("File not found in the specified directory.");

        } catch (Exception e) {
            // Log the error and respond with failure
            errorLogService.saveErrorLog("LocalStorageService/deleteFile", "", "", e.getMessage(), "server");
            return controllerUtils.failedResponse("" + e.getMessage());
        }
    }

    public String getFilePathInSystem() {

        String x=getUploadDirectory();
        System.out.println(x);
        return x;

    }

    public Map<String, Object> localFileUploadFolder(MultipartFile multiPartFile, HttpServletRequest request, String folderName) {
        try {
            // Validate API request


            // Validate folder name
            if (appUtils.isNull(folderName) || folderName.trim().isEmpty()) {
                return controllerUtils.failedResponse("Folder Name is empty.");
            }

            // Validate file
            if (multiPartFile == null || multiPartFile.isEmpty()) {
                return controllerUtils.failedResponse("Please select a file to upload.");
            }
            if (multiPartFile.getSize() > maxFileSize) {
                return controllerUtils.failedResponse("File size exceeds the maximum limit of 50MB.");
            }

            // Define upload directory and folder structure
            String uploadDirectory = getUploadDirectory(); // Base upload directory
            String targetFolder = Paths.get(uploadDirectory, folderName.trim()).toString();

            // Ensure directory exists
            File directory = new File(targetFolder);
            if (!directory.exists() && !directory.mkdirs()) {
                return controllerUtils.failedResponse("Failed to create upload directory.");
            }

            // Extract original filename and extension
            String originalFilename = multiPartFile.getOriginalFilename();
            if (originalFilename == null || originalFilename.trim().isEmpty()) {
                return controllerUtils.failedResponse("Invalid file name.");
            }

            // Save the file with its original name
            Path filePath = Paths.get(targetFolder, originalFilename);
            Files.copy(multiPartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Generate file access URL
            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .scheme(request.getScheme()) // Use request scheme (http/https)
                    .path("/api/files/")
                    .path(folderName.trim() + "/") // Folder name in URL
                    .path(originalFilename)
                    .build()
                    .toUriString();

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("url", fileUrl);

            return controllerUtils.successResponseList(response);

        } catch (Exception e) {
            // Log and return error
            errorLogService.saveErrorLog("LocalStorageService/localFileUploadFolder", "", "", e.getMessage(), "server");
            return controllerUtils.failedResponse("File upload failed: " + e.getMessage());
        }
    }

}
