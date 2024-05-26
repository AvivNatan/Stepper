package utils;

import com.google.gson.Gson;

public class Constants {
    public static final String USERNAME = "username";
    public final static int REFRESH_RATE = 2000;
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/Tomcat_Web_exploded";;
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String LOAD_FILE = FULL_SERVER_PATH + "/load_file";
    public final static String GET_FLOWEX_DESCRIPTION= FULL_SERVER_PATH + "/flow_execution_description_admin";
    public final static String GET_HISTORY_DESCRIPTION= FULL_SERVER_PATH + "/get_history_description";
    public final static String GET_STEP_HISTORY= FULL_SERVER_PATH + "/step_execution_history";
    public final static String GET_FLOWS_HISTORY= FULL_SERVER_PATH + "/get_flows_history";
    public final static String GET_STATISTICS= FULL_SERVER_PATH + "/get_statistics";
    public final static String UPDATE_EXECUTIONS= FULL_SERVER_PATH + "/executions_refresher";
    public final static String GET_USER_LIST= FULL_SERVER_PATH + "/get_user_list";
    public final static String GET_USER_DETAILS= FULL_SERVER_PATH + "/get_user_details";
    public final static String GET_ROLE_LIST= FULL_SERVER_PATH + "/get_role_list";

    public final static String GET_ROLE_DETAILS= FULL_SERVER_PATH + "/get_role_details";
    public final static String ADD_ROLE_TO_USER= FULL_SERVER_PATH + "/add_role_to_user";
    public final static String REMOVE_ROLE_FROM_USER= FULL_SERVER_PATH + "/remove_role_from_user";
    public final static String ADD_FLOW_TO_ROLE= FULL_SERVER_PATH + "/add_flow_to_role";

    public final static String REMOVE_FLOW_FROM_ROLE= FULL_SERVER_PATH + "/remove_flow_from_role";
    public final static String ADD_NEW_ROLE= FULL_SERVER_PATH + "/add_new_role";
    public final static String GET_FLOWS_NEW_ROLE= FULL_SERVER_PATH + "/get_flows_new_role";
    public final static String IS_ADMIN_EXIST= FULL_SERVER_PATH + "/admin_exist";













//    public final static String USERS_LIST = FULL_SERVER_PATH + "/usersListRefresher";
//    public final static String ROLES_LIST = FULL_SERVER_PATH + "/rolesListRefresher";
//    public final static String USER_DATA_INFO_IN_ADMIN = FULL_SERVER_PATH + "/userDataInfoAdminRefresher";

     //GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
