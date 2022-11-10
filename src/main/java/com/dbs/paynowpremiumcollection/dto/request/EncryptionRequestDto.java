package com.dbs.paynowpremiumcollection.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncryptionRequestDto {

    private String referenceId;
    private MultipartFile logo;
}
