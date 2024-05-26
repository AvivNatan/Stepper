package servlets;

import com.google.gson.Gson;
import dto.DtoHeaderResponse;
import dto.DtoUserList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.users.User;
import stepper.users.UserManager;
import utils.ServletUtils;

import javax.jws.soap.SOAPBinding;
import java.io.IOException;

@WebServlet(name = "HeaderRefresherServlet", urlPatterns = "/header_refresher")
public class HeaderRefresherServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = req.getParameter("username");
        User user=userManager.getUserByName(userName);
        Gson gson = new Gson();
        DtoHeaderResponse result = new DtoHeaderResponse(user.isManager(),user.getRolesNames());
        String resultJSON = gson.toJson(result);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(resultJSON);


    }
}
