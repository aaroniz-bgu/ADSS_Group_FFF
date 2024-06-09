package bgu.adss.fff.dev.exceptions;

import org.springframework.http.HttpStatus;

public class CategoryException extends AppException{

    public CategoryException(String message, HttpStatus status){
        super(message, status);
    }

}
