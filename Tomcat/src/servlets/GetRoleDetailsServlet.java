package servlets;

import com.google.gson.Gson;
import dto.DtoRoleDetails;
import dto.DtoUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.engine.EngineSystem;
import stepper.roles.Role;
import stepper.roles.RoleManager;
import stepper.users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetRoleDetailsServlet", urlPatterns = "/get_role_details")
public class GetRoleDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        RoleManager roleManager = ServletUtils.getRolesManager(getServletContext());
        EngineSystem system = ServletUtils.getSystemManager(getServletContext());

        Gson gson = new Gson();
        String roleName = req.getParameter("rolename");
        Role role = roleManager.getRole(roleName);

        DtoRoleDetails result = new DtoRoleDetails(role.getNameRole(),
                role.getDescription(), role.getUsersNames(),role.getFlowsNames(),
                system.getFlowsNotAssignedOfRole(role));
        String resultJSON = gson.toJson(result);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(resultJSON);
    }

}
