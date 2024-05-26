package servlets;

import com.google.gson.Gson;
import dto.DtoHeaderResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import okhttp3.Response;
import stepper.users.User;
import stepper.users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "isAdminExistServlet", urlPatterns = "/admin_exist")
public class isAdminExistServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String error;
        String usernameFromParameter;
        String usernameFromSession = SessionUtils.getUsername(req);
        res.setContentType("text/plain;charset=UTF-8");

        if(usernameFromSession==null)
        {
            usernameFromParameter = "Admin";
            if(ServletUtils.isIsAdminExist())
            {
                error="Admin already logged in.";
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getOutputStream().print(error);
            }
            else
            {
                ServletUtils.setIsAdminExist(true);
                req.getSession(true).setAttribute("username",usernameFromParameter);
                res.setStatus(HttpServletResponse.SC_OK);
            }
        }
        else
        {
            res.setStatus(HttpServletResponse.SC_OK);
        }






    }

}

