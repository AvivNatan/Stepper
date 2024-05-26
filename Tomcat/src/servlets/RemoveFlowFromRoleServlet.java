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

@WebServlet(name = "RemoveFlowFromRoleServlet", urlPatterns = "/remove_flow_from_role")
public class RemoveFlowFromRoleServlet extends HttpServlet
{
        protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

            EngineSystem system = ServletUtils.getSystemManager(getServletContext());
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            RoleManager roleManager=ServletUtils.getRolesManager(getServletContext());
            // Gson gson = new Gson();
            String flowName = req.getParameter("flowname");
            String roleName = req.getParameter("rolename");
            Role role=roleManager.getRole(roleName);
            role.getFlowsNames().remove(flowName);

            system.removeFlowOfRoleInUser(userManager,roleManager,role);
            res.setStatus(HttpServletResponse.SC_OK);
        }

}
