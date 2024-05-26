package servlets;

import com.google.gson.Gson;
import dto.DtoUserDetails;
import dto.DtoUserList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.engine.EngineSystem;
import stepper.roles.RoleManager;
import stepper.users.User;
import stepper.users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet(name = "GetUserDetailsServlet", urlPatterns = "/get_user_details")
public class GetUserDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            EngineSystem system = ServletUtils.getSystemManager(getServletContext());
            RoleManager roleManager=ServletUtils.getRolesManager(getServletContext());
            Gson gson = new Gson();
            String userName = req.getParameter("username");
            User user=userManager.getUserByName(userName);

            DtoUserDetails result = new DtoUserDetails(userName,user.getRolesNames(),
                    roleManager.getRolesNotAssignedOfUser(user),
                    system.getFlowDefinitionsOfUser(userName).size(),
                    system.getFlowExecutionsOfUser(userName).size(),user.isManager());
            String resultJSON = gson.toJson(result);
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write(resultJSON);


    }


}
