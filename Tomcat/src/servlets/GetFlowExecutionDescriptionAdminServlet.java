package servlets;

import com.google.gson.Gson;
import dto.DtoFlowPrevExecutionDescription;
import dto.DtoFlowPrevExecutionDescriptionResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.engine.EngineSystem;
import stepper.flow.excution.FlowExecution;
import stepper.users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "GetFlowExecutionDescriptionAdminServlet", urlPatterns = "/flow_execution_description_admin")
public class GetFlowExecutionDescriptionAdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

            EngineSystem system = ServletUtils.getSystemManager(getServletContext());
            UserManager userManager=ServletUtils.getUserManager(getServletContext());
            Gson gson = new Gson();
            UUID id = UUID.fromString(req.getParameter("id"));

            DtoFlowPrevExecutionDescription dto = system.getExecutionByID(id,userManager);

            String resultJSON = gson.toJson(dto);
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write(resultJSON);



    }
}
