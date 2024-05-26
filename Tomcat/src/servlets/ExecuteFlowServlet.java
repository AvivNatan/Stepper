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

@WebServlet(name = "ExecuteFlowServlet", urlPatterns = "/execute_flow")
public class ExecuteFlowServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {


            EngineSystem system = ServletUtils.getSystemManager(getServletContext());
            Gson gson = new Gson();

            String userName = req.getParameter("username");

            FlowExecution execution = system.getOptionalExecution(userName);

            system.ExecutionFlow(execution, userName);

            DtoActivatedFlowDescription dto = new DtoActivatedFlowDescription(
                    execution.getUniqueId(), execution.getFlowDefinition().getName(),
                    null);
            String resultJSON = gson.toJson(dto);
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write(resultJSON);



    }
}
