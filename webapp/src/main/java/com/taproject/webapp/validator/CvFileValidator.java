package com.taproject.webapp.validator;

import org.springframework.web.multipart.MultipartFile;

import com.taproject.webapp.annotation.ValidCv;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CvFileValidator implements ConstraintValidator<ValidCv, MultipartFile>  {
    private static final long MAX_SIZE = 2 * 1024 * 1024;
    private static final String ERROR_NULL = "Please upload your CV! (PDF file of 2MB max. size)";
    private static final String ERROR_TOO_LARGE = "File too large, please compress before submitting!";
    private static final String ERROR_NOT_PDF = "Only PDFs are accepted (size <2MB)!";

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        boolean result = true;
        String msg = "Error Validating File Upload!";
        if (value.isEmpty()) {
            result = false;
            msg = ERROR_NULL;
        }else if (!value.getContentType().equals("application/pdf")) {
            result = false;
            msg = ERROR_NOT_PDF;
        }else if (value.getSize() > MAX_SIZE) {
            result = false;
            msg = ERROR_TOO_LARGE;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
        return result;
    }
}
