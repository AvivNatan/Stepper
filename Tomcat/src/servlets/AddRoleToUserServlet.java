package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.engine.EngineSystem;
import stepper.flow.excution.FlowExecution;
import stepper.roles.Role;
import stepper.roles.RoleManager;
import stepper.users.User;
import stepper.users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "AddRoleToUserServlet", urlPatterns = "/add_role_to_user")
public class AddRoleToUserServlet extends HttpServlet
{
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
        system.addRoleToUserUpdateDefinition(role,user);
        res.setStatus(HttpServletResponse.SC_OK);
    }
}
