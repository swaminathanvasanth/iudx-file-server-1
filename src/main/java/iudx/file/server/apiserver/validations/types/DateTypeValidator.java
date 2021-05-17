package iudx.file.server.apiserver.validations.types;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DateTypeValidator implements Validator {

  private static final Logger LOGGER = LogManager.getLogger(DateTypeValidator.class);
  
  private final String value;
  private final boolean required;

  public DateTypeValidator(String value, boolean required) {
    this.value = value;
    this.required = required;
  }

  @Override
  public boolean isValid() {
    if (required && (value == null || value.isBlank())) {
      LOGGER.error("Validation error : null or blank value for required mandatory field");
      return false;
    } else {
      if (value == null || value.isBlank()) {
        return true;
      }
    }
    return isValidDate(value);
  }

  @Override
  public int failureCode() {
    // TODO Auto-generated method stub
    return 400;
  }

  @Override
  public String failureMessage() {
    // TODO Auto-generated method stub
    return "invalid date-time format";
  }

  private boolean isValidDate(String value) {
    String dateString = value.trim().replaceAll("\\s", "+");// since + is treated as space in uri
    try {
      ZonedDateTime.parse(dateString);
      return true;
    } catch (DateTimeParseException e) {
      LOGGER.error("Validation error : Invalid date-time format [ "+value+" ]");
      return false;
    }
  }

}
