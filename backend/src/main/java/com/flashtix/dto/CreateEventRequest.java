package com.flashtix.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a new event")
public class CreateEventRequest {

    @NotBlank(message = "Event title is required")
    private String title;

    @NotBlank(message = "Event location is required")
    private String location;

    @NotBlank(message = "Event start time is required")
    private String startTime;

    @NotBlank(message = "Event end time is required")
    private String endTime;

    @Schema(description = "Event banner image file")
    private MultipartFile bannerImage;
}
