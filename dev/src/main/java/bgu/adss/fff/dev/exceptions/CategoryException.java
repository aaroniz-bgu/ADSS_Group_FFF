package bgu.adss.fff.dev.exceptions;

import org.springframework.http.HttpStatus;

public class CategoryException extends AppException{

    /**
     * CategoryException constructor
     * @param message exception message
     * @param status exception status
     */
    public CategoryException(String message, HttpStatus status){
        super(message, status);
    }

}
