package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.engine.EngineSystem;
import stepper.roles.Role;
import stepper.roles.RoleManager;
import stepper.users.User;
import stepper.users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "RemoveRoleFromUserServlet", urlPatterns = "/remove_role_from_user")
public class RemoveRoleFromUserServlet extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

            EngineSystem system = ServletUtils.getSystemManager(getServletContext());
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            RoleManager roleManager=ServletUtils.getRolesManager(getServletContext());
            // Gson gson = new Gson();
            String userName = req.getParameter("username");
            String roleName = req.getParameter("rolename");
            Role role=roleManager.getRole(roleName);
            User user=userManager.getUserByName(userName);
            system.removeRoleFromUserUpdateDefinition(role,roleManager,user);
            res.setStatus(HttpServletResponse.SC_OK);
        }
}
