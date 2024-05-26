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

@WebServlet(name = "AddFlowToRoleServlet", urlPatterns = "/add_flow_to_role")
public class AddFlowToRoleServlet extends HttpServlet {
        protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

            EngineSystem system = ServletUtils.getSystemManager(getServletContext());

            RoleManager roleManager=ServletUtils.getRolesManager(getServletContext());
            String flowName = req.getParameter("flowname");
            String roleName = req.getParameter("rolename");
            Role role=roleManager.getRole(roleName);
            system.addNewFlowOfRoleInUser(flowName,role);
            role.addFlow(flowName);
            res.setStatus(HttpServletResponse.SC_OK);
        }

}
