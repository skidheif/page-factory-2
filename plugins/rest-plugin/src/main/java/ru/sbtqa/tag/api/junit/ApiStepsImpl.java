package ru.sbtqa.tag.api.junit;

import cucumber.api.DataTable;
import ru.sbtqa.tag.api.annotation.ParameterType;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.context.EndpointContext;
import ru.sbtqa.tag.api.environment.ApiEnvironment;
import ru.sbtqa.tag.api.exception.RestPluginException;
import ru.sbtqa.tag.api.manager.EndpointManager;
import ru.sbtqa.tag.api.storage.BlankStorage;
import ru.sbtqa.tag.api.storage.EndpointBlank;
import ru.sbtqa.tag.api.utils.FromResponseUtils;

import java.util.Map;

/**
 * Basic step definitions, that should be available on every project
 *
 * <p>
 * To pass a Cucumber {@link cucumber.api.DataTable} as a parameter to method,
 * supply a table in the following format after a step ini feature:
 * <p>
 * | name 1| name 2 | | value 1 | value 2 |
 * <p>
 * This table will be converted to a {@link cucumber.api.DataTable} object.
 * First line is not enforced to be a name.
 * <p>
 * To pass a list as parameter, use flattened table as follows: | value 1 | }
 * value 2 |
 *
 * @see <a href="https://cucumber.io/docs/reference#step-definitions">Cucumber
 * documentation</a>
 */
public class ApiStepsImpl<T extends ApiStepsImpl<T>> {

    public ApiStepsImpl() {
        ApiSetupSteps.initApi();
    }

    /**
     * Execute the last endpoint (request) in {@link BlankStorage} with no
     * parameters.
     */
    public T send() {
        String endpoint = ApiEnvironment.getBlankStorage().getLast().getTitle();
        EndpointManager.getEndpoint(endpoint).send();
        ApiEnvironment.getBlankStorage().removeLast();
        return (T) this;
    }

    /**
     * Execute endpoint (request) with no parameters
     *
     * @param endpoint class of the endpoint annotation to execute
     */
    public T send(Class endpoint) {
        EndpointManager.getEndpoint(endpoint).send();
        return (T) this;
    }

    /**
     * Execute endpoint (request) with no parameters
     *
     * @param endpoint name value of the endpoint annotation to execute
     */
    public T send(String endpoint) {
        EndpointManager.getEndpoint(endpoint).send();
        return (T) this;
    }

    /**
     * Execute endpoint endpoint (request) with parameters from given
     * {@link Map}
     *
     * @param endpoint name value of the endpoint annotation to execute
     * @param data table of parameters
     */
    public T send(String endpoint, Map<String, String> data) {
        EndpointManager.getEndpoint(endpoint).send(data);
        return (T) this;
    }

    /**
     * Execute endpoint endpoint (request) with parameters from given
     * {@link Map}
     *
     * @param endpoint class of the endpoint annotation to execute
     * @param data table of parameters
     */
    public T send(Class endpoint, Map<String, String> data) {
        EndpointManager.getEndpoint(endpoint).send(data);
        return (T) this;
    }

    /**
     * Execute a validation rule annotated by {@link Validation} on current
     * endpoint. Works if endpoint contains only one validation rule
     */
    public void validate() {
        EndpointContext.getCurrentEndpoint().validate();
    }

    /**
     * Execute a validation rule annotated by {@link Validation} on current
     * endpoint
     *
     * @param rule name of the validation rule (name value of the
     * {@link Validation} annotation)
     */
    public void validate(String rule) {
        EndpointContext.getCurrentEndpoint().validate(rule);
    }

    /**
     * Execute a validation rule annotated by {@link Validation} on current
     * endpoint with parameters from given {@link Map}
     *
     * @param rule name of the validation rule (name value of the
     * {@link Validation} annotation)
     * @param data map of parameters
     * @throws RestPluginException if there is an error while validation rule
     * executing
     */
    public void validate(String rule, Map<String, String> data) {
        EndpointContext.getCurrentEndpoint().validate(rule, data);
    }

    /**
     * Execute a validation rule annotated by {@link Validation} on current
     * endpoint with parameters from given {@link DataTable}
     *
     * @param rule name of the validation rule (name value of the
     * {@link Validation} annotation)
     * @param data datatable with parameters
     * @throws RestPluginException if there is an error while validation rule
     * executing
     */
    public void validate(String rule, DataTable data) {
        EndpointContext.getCurrentEndpoint().validate(rule, data);
    }

    /**
     * Start filling parameters in endpoint
     *
     * @param title endpoint title
     */
    public T fill(String title) {
        ApiEnvironment.getBlankStorage().add(new EndpointBlank(title));
        return (T) this;
    }

    public T fill(Class endpoint) {
        ApiEnvironment.getBlankStorage().add(new EndpointBlank(endpoint));
        return (T) this;
    }

    /**
     * Add parameter to the last endpoint blank in {@link BlankStorage}
     *
     * @param parameterType {@link ParameterType} of parameter
     * @param name with this name the parameter will be added to endpoint blank
     * @param value with this value the parameter will be added to endpoint
     * blank
     */
    public T add(String parameterType, String name, String value) {
        ParameterType type = ParameterType.valueOf(parameterType.toUpperCase());
        return add(type, name, value);
    }

    /**
     * Add parameter to the last endpoint blank in {@link BlankStorage}
     *
     * @param type {@link ParameterType} of parameter
     * @param name with this name the parameter will be added to endpoint blank
     * @param value with this value the parameter will be added to endpoint
     * blank
     */
    public T add(ParameterType type, String name, String value) {
        ApiEnvironment.getBlankStorage().getLast().addParameter(type, name, value);
        return (T) this;
    }

    /**
     * Add parameters to the last endpoint blank in {@link BlankStorage}
     *
     * @param type {@link ParameterType} of parameters
     * @param data parameters name-value pairs
     */
    public T add(ParameterType type, Map<String, String> data) {
        for (Map.Entry<String, String> dataTableRow : data.entrySet()) {
            ApiEnvironment.getBlankStorage().getLast().addParameter(type, dataTableRow.getKey(), dataTableRow.getValue());
        }
        return (T) this;
    }

    /**
     * Add parameter with {@link ParameterType} to the last endpoint blank in
     * {@link BlankStorage}. Get value from body of one of the previous
     * responses
     *
     * @param parameterType {@link ParameterType} of parameter
     * @param parameterName with this name the parameter will be added to
     * endpoint blank
     * @param fromEndpointTitle get response with this title
     * @param path get value from body by this path
     */
    public T addToBody(String parameterType, String parameterName, String fromEndpointTitle, String path) {
        addToBody(parameterType, parameterName, fromEndpointTitle, path, "");
        return (T) this;
    }

    /**
     * Add parameter with {@link ParameterType} to the last endpoint blank in
     * {@link BlankStorage}. Get value from body of one of the previous
     * responses
     *
     * @param parameterType {@link ParameterType} of parameter
     * @param parameterName with this name the parameter will be added to
     * endpoint blank
     * @param fromEndpointTitle get response with this title
     * @param path get value from body by this path
     * @param mask apply mask on this value
     */
    public T addToBody(String parameterType, String parameterName, String fromEndpointTitle, String path, String mask) {
        Class fromEndpoint = ApiEnvironment.getRepository().get(fromEndpointTitle);
        String value = (String) FromResponseUtils.getValueFromResponse(fromEndpoint, "", path, mask, true);

        ParameterType type = ParameterType.valueOf(parameterType.toUpperCase());
        ApiEnvironment.getBlankStorage().getLast().addParameter(type, parameterName, value);
        return (T) this;
    }

    /**
     * Add parameter with {@link ParameterType} to the last endpoint blank in
     * {@link BlankStorage}. Get value from header of one of the previous
     * responses
     *
     * @param parameterType {@link ParameterType} of parameter to add
     * @param parameterName with this name the parameter will be added to
     * endpoint blank
     * @param fromEndpointTitle get response with this title
     * @param headerName get value from header with this name
     */
    public T addToHeader(String parameterType, String parameterName, String fromEndpointTitle, String headerName) {
        addToBody(parameterType, parameterName, fromEndpointTitle, headerName, "");
        return (T) this;
    }

    /**
     * Add parameter with {@link ParameterType} to the last endpoint blank in
     * {@link BlankStorage}. Get value from header of one of the previous
     * responses
     *
     * @param parameterType {@link ParameterType} of parameter to add
     * @param parameterName with this name the parameter will be added to
     * endpoint blank
     * @param fromEndpointTitle get response with this title
     * @param headerName get value from header with this name
     * @param mask apply mask on this value
     */
    public T addToHeader(String parameterType, String parameterName, String fromEndpointTitle, String headerName, String mask) {
        Class fromEndpoint = ApiEnvironment.getRepository().get(fromEndpointTitle);
        String value = (String) FromResponseUtils.getValueFromResponse(fromEndpoint, headerName, "", mask, true);

        ParameterType type = ParameterType.valueOf(parameterType.toUpperCase());
        ApiEnvironment.getBlankStorage().getLast().addParameter(type, parameterName, value);
        return (T) this;
    }

}
