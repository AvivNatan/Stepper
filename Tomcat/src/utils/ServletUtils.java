package utils;

import jakarta.servlet.ServletContext;

import stepper.engine.EngineSystem;
import stepper.engine.EngineSystemImp;
import stepper.roles.RoleManager;
import stepper.users.UserManager;

public class ServletUtils {

    private  static boolean isAdminExist=false;
    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String SYSTEM_MANAGER_ATTRIBUTE_NAME = "systemManager";
    private static final String ROLE_MANAGER_ATTRIBUTE_NAME = "roleManager";

    private static final Object userManagerLock = new Object();
    private static final Object systemEngineLock = new Object();
    private static final Object roleManagerLock = new Object();


    public static RoleManager getRolesManager(ServletContext servletContext) {

        synchronized (roleManagerLock) {
            if (servletContext.getAttribute(ROLE_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(ROLE_MANAGER_ATTRIBUTE_NAME, new RoleManager());
            }
        }
        return (RoleManager) servletContext.getAttribute(ROLE_MANAGER_ATTRIBUTE_NAME);
    }

    public static UserManager getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static EngineSystem getSystemManager(ServletContext servletContext) {
        synchronized (systemEngineLock) {
            if (servletContext.getAttribute(SYSTEM_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(SYSTEM_MANAGER_ATTRIBUTE_NAME, new EngineSystemImp());
            }
        }
        return (EngineSystem) servletContext.getAttribute(SYSTEM_MANAGER_ATTRIBUTE_NAME);
    }

    public static boolean isIsAdminExist() {
        return isAdminExist;
    }

    public static void setIsAdminExist(boolean isAdminExist) {
        ServletUtils.isAdminExist = isAdminExist;
    }
}
