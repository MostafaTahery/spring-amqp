package co.nilin.springamqp.exception.handlers;

import co.nilin.springamqp.data.dto.ErrorResponse;
import co.nilin.springamqp.exception.exceptions.BusinessException;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ComponentScan
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleException(Throwable ex) {
        if ( ex instanceof BusinessException ){
            return ResponseEntity.status(((BusinessException) ex).getStatus())
                    .body(new ErrorResponse(((BusinessException) ex).getStatus()
                            , ((BusinessException) ex).getError()
                            , ex.getMessage()));
        }
        return ResponseEntity.status(500).body(new ErrorResponse(500, ex.getMessage() , ex.getMessage()));
    }


}
