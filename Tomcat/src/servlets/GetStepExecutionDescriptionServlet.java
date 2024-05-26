package servlets;

import com.google.gson.Gson;
import dto.DtoStepExecutionDetails;
import dto.DtoStepExecutionDetailsResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.engine.EngineSystem;
import stepper.flow.excution.FlowExecution;
import utils.ServletUtils;

import java.io.IOException;
import java.util.Objects;

@WebServlet(name = "GetStepExecutionDescriptionServlet", urlPatterns = "/step_execution_description")
public class GetStepExecutionDescriptionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

            EngineSystem system = ServletUtils.getSystemManager(getServletContext());
            Gson gson = new Gson();
            DtoStepExecutionDetails dtoStep;
            String userName = req.getParameter("username");
            String value = req.getParameter("valueselected");


            FlowExecution flowExecution=system.getOptionalExecution(userName);
            boolean result=isFlowDetailsSelected(flowExecution,value);

            if(result)
                dtoStep =null;
            else
                dtoStep = system.getStepExecutionDetails(flowExecution.getUniqueId(),value);

            DtoStepExecutionDetailsResponse dto = new DtoStepExecutionDetailsResponse(dtoStep,
                   result);

            String resultJSON = gson.toJson(dto);
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write(resultJSON);


    }
    public boolean isFlowDetailsSelected(FlowExecution flowExecution,String value)
    {
       return Objects.equals(flowExecution.getFlowDefinition().getName(),value);
    }
}

