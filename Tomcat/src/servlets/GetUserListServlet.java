package servlets;

import com.google.gson.Gson;
import dto.DtoUserList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.engine.EngineSystem;
import stepper.users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.Timer;
@WebServlet(name = "GetUserListServlet", urlPatterns = "/get_user_list")
public class GetUserListServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            Gson gson = new Gson();
            DtoUserList result=userManager.getUsersNames();
            String resultJSON = gson.toJson(result);
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write(resultJSON);


    }

}
