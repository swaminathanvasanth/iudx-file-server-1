package iudx.file.server.apiserver.validations;

import static iudx.file.server.apiserver.utilities.Constants.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import io.vertx.core.json.JsonObject;
import iudx.file.server.apiserver.response.ResponseUrn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Set;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;

public class RequestValidator {

  private static final Logger LOGGER = LogManager.getLogger(RequestValidator.class);

  private static Set<String> validParams = new HashSet<String>();
  static {
    validParams.add(PARAM_TIME);
    validParams.add(PARAM_ID);
    validParams.add(PARAM_START_TIME);
    validParams.add(PARAM_END_TIME);
    validParams.add(PARAM_TIME_REL);
    validParams.add(PARAM_GEOREL);
    validParams.add(PARAM_GEOMETRY);
    validParams.add(PARAM_GEOPROPERTY);
    validParams.add(PARAM_COORDINATES);
    validParams.add(PARAM_OFFSET);
    validParams.add(PARAM_LIMIT);

  }


  public Future<Boolean> isValid(MultiMap map) {
    Promise<Boolean> promise = Promise.promise();
    if (isValidParams(map)) {
      LOGGER.debug("valid params");
      promise.complete(true);
    } else {
      LOGGER.error("Invalid query param found in request");
      promise.fail("Invalid query param.");
    }
    return promise.future();
  }


  private boolean isValidParams(MultiMap map) {
    final List<Entry<String, String>> entries = map.entries();
    for (final Entry<String, String> entry : entries) {
      if (!validParams.contains(entry.getKey())) {
        return false;
      }
    }
    return true;
  }

  public Future<Boolean> isValidArchiveRequest(MultiMap params) {
    Promise<Boolean> promise = Promise.promise();
    if (params.contains(PARAM_GEOMETRY) && params.contains(PARAM_COORDINATES)
        && params.contains(PARAM_START_TIME) && params.contains(PARAM_END_TIME)) {
      promise.complete(true);
    } else if (!params.contains(PARAM_GEOMETRY)) {
      handleResponse(promise, "Missing Param : [ "+PARAM_GEOMETRY+" ]");
    } else if (!params.contains(PARAM_COORDINATES)) {
      handleResponse(promise, "Missing Param : [ "+PARAM_COORDINATES+" ]");
    } else if (!params.contains(PARAM_START_TIME)) {
      handleResponse(promise, "Missing Param : [ "+PARAM_START_TIME+" ]");
    } else if (!params.contains(PARAM_END_TIME)) {
      handleResponse(promise, "Missing Param : [ "+PARAM_END_TIME+" ]");
    }

    return promise.future();
  }

  public void handleResponse(Promise<Boolean> promise, String message) {
    promise.fail(new JsonObject()
            .put("type", 400)
            .put("title", ResponseUrn.INVALID_PAYLOAD_FORMAT.getUrn())
            .put("errorMessage", message)
            .toString());
    LOGGER.error("Invalid archieve request, All mandatory fields are required");
  }

}
