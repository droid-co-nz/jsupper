package nz.co.droid.jsupper.exception;

/**
 * Default error.
 * 
 * @author Tomas Adamek
 *
 */
public class JsupperException extends Exception {

  private static final long serialVersionUID = 1L;

  public JsupperException() {
    super();
  }

  public JsupperException(String message, Throwable cause) {
    super(message, cause);
  }

  public JsupperException(String message) {
    super(message);
  }

  public JsupperException(Throwable cause) {
    super(cause);
  }
}