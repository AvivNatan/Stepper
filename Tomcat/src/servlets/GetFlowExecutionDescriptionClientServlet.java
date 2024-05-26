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
import stepper.flow.definition.api.FlowDefinition;
import stepper.flow.excution.FlowExecution;
import stepper.users.User;
import stepper.users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "GetFlowExecutionDescriptionClientServlet", urlPatterns = "/flow_execution_description_client")
public class GetFlowExecutionDescriptionClientServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

            EngineSystem system = ServletUtils.getSystemManager(getServletContext());
            UserManager userManager=ServletUtils.getUserManager(getServletContext());
            Gson gson = new Gson();
            UUID id = UUID.fromString(req.getParameter("id"));
            String userName = req.getParameter("username");


            FlowExecution optional = system.getOptionalExecution(userName);

            DtoFlowPrevExecutionDescription dto = system.getExecutionByID(id,userManager);
            FlowDefinition flow=system.getFlowDefinitionByName(dto.getName(), system.getAllFlowsAllFiles());

            DtoFlowPrevExecutionDescriptionResponse result=new
                    DtoFlowPrevExecutionDescriptionResponse(dto
                    ,optional.getUniqueId().toString().equals(id.toString())
        , system.getFlowDefinitionsOfUser(userName).contains(flow));

            String resultJSON = gson.toJson(result);
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write(resultJSON);



    }

}

