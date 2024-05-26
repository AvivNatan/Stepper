package util;

import com.google.gson.Gson;

public class Constants {

    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static int REFRESH_RATE = 2000;

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/Tomcat_Web_exploded";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login_client";
    public final static String GET_DEFINITION = FULL_SERVER_PATH + "/get_flows_definitions";
    public final static String OPTIONAL_AND_INPUTS = FULL_SERVER_PATH + "/add_optionalEx_and_inputs";
    public final static String IS_MANDATORY_INPUTS = FULL_SERVER_PATH + "/mandatory_inputs";

    public final static String EXECUTE_FLOW = FULL_SERVER_PATH + "/execute_flow";
    public final static String GET_TYPE_INPUT= FULL_SERVER_PATH + "/type_free_input";
    public final static String UPDATE_INPUT_CONTENT= FULL_SERVER_PATH + "/update_input_content";

    public final static String GET_FLOWEX_DESCRIPTION= FULL_SERVER_PATH + "/flow_execution_description_client";
    public final static String GET_STEPEX_DESCRIPTION= FULL_SERVER_PATH + "/step_execution_description";
    public final static String GET_HISTORY_DESCRIPTION= FULL_SERVER_PATH + "/get_history_description";
    public final static String GET_STEP_HISTORY= FULL_SERVER_PATH + "/step_execution_history";
    public final static String GET_FLOWS_HISTORY= FULL_SERVER_PATH + "/get_flows_history";
    public final static String IS_EXECUTION_CURRENT= FULL_SERVER_PATH + "/current_execution";
    public final static String HEADER_REFRESHER= FULL_SERVER_PATH + "/header_refresher";

    public final static String UPDATE_EXECUTIONS= FULL_SERVER_PATH + "/executions_refresher";


}
