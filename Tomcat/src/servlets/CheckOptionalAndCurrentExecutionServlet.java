package servlets;

import com.google.gson.Gson;
import dto.DtoActivatedFlowDescription;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.engine.EngineSystem;
import stepper.flow.excution.FlowExecution;
import utils.ServletUtils;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "CheckOptionalAndCurrentExecutionServlet", urlPatterns = "/check_execution")
public class CheckOptionalAndCurrentExecutionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {


            EngineSystem system = ServletUtils.getSystemManager(getServletContext());
            Gson gson = new Gson();

            String userName = req.getParameter("username");
            String id = req.getParameter("id");

            FlowExecution optional = system.getOptionalExecution(userName);
            String resultJSON = gson.toJson(optional.getUniqueId().toString().equals(id));
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write(resultJSON);


    }

}

