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
import java.util.UUID;

@WebServlet(name = "GetStepExecutionHistoryServlet", urlPatterns = "/step_execution_history")

public class GetStepExecutionHistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

            EngineSystem system = ServletUtils.getSystemManager(getServletContext());
            Gson gson = new Gson();
            DtoStepExecutionDetails dtoStep;
            UUID id = UUID.fromString(req.getParameter("id"));
            String value = req.getParameter("valueselected");



            FlowExecution flowExecution = system.findActivatedExecutionByID(id);
            boolean result=isFlowDetailsSelected(flowExecution,value);

            if (result)
                dtoStep = null;
            else
                dtoStep = system.getStepExecutionDetails(flowExecution.getUniqueId(), value);

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


