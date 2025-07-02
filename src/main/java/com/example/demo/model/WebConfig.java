package com.example.demo.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Value("${upload.directory.windows}")
    private String windowsUploadDirectory;

    @Value("${upload.directory.linux}")
    private String linuxUploadDirectory;

    @Value("${upload.directory.default}")
    private String defaultUploadDirectory;

    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        FilterRegistrationBean<ForwardedHeaderFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new ForwardedHeaderFilter());
        return filterRegistrationBean;
    }

    // Get the upload directory based on the operating system
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

    // Configure resource handlers for serving files
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDirectory = getUploadDirectory();

        // Ensure the directory path is absolute and normalized
        Path uploadPath = Paths.get(uploadDirectory).toAbsolutePath().normalize();

        // Convert directory path to URI format for resource mapping
        String uploadDirUri = uploadPath.toUri().toString();

        // Ensure the URI ends with a '/'
        if (!uploadDirUri.endsWith("/")) {
            uploadDirUri += "/";
        }

        // Add resource handler for the "/api/files/**" path
        registry.addResourceHandler("/api/files/**")
                .addResourceLocations(uploadDirUri);
    }

    // Resolve the correct path for the file based on folder presence
    public String resolveFolderPath(String url) {
        String uploadDirectory = getUploadDirectory(); // Base upload directory

        // Extract folder name from URL (assuming /api/files/foldername/filename)
        String folderName = extractFolderNameFromURL(url);

        // If folder name exists, check that folder first
        if (folderName != null && !folderName.isEmpty()) {
            File folder = new File(uploadDirectory, folderName);
            if (folder.exists() && folder.isDirectory()) {
                return folder.getAbsolutePath(); // Return the folder path if exists
            } else {
                // If folder doesn't exist, return the base upload directory path
                return uploadDirectory;
            }
        }

        // If no folder name is found, return the base upload directory path
        return uploadDirectory;
    }

    // Helper method to extract folder name from the URL, e.g., /api/files/foldername/filename
    private String extractFolderNameFromURL(String url) {
        String prefix = "files/"; // The part of the URL that indicates the start of the folder name
        String folderName = "";

        // Look for the folder name after the 'files/' prefix and before the next '/'
        int folderStart = url.indexOf(prefix) + prefix.length();
        int folderEnd = url.indexOf("/", folderStart);

        if (folderStart > prefix.length() - 1 && folderEnd > folderStart) {
            folderName = url.substring(folderStart, folderEnd); // Extract the folder name
        }

        return folderName; // Return the folder name or empty string if not found
    }

}
