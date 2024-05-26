package servlets;

import com.google.gson.Gson;
import dto.DtoRoleList;
import dto.DtoUserList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.roles.RoleManager;
import stepper.users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "GetRolesListServlet", urlPatterns = "/get_role_list")
public class GetRolesListServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        RoleManager roleManager = ServletUtils.getRolesManager(getServletContext());
        Gson gson = new Gson();
        DtoRoleList result=new DtoRoleList(roleManager.getRolesNames());
        String resultJSON = gson.toJson(result);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(resultJSON);


    }

}
